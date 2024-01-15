package server;

import client.ClientConnection;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * A multi-threaded host server which handles client connections and runs the game.
 * @author Harry Xu
 * @version 1.0 - December 24th 2023
 */
public class HostServer implements AutoCloseable {
    /** Functions to call when a socket successfully connects */
    private final List<Consumer<ClientConnection>> onConnectSubscribers;

    /** Functions to call when a socket disconnects */
    private final List<Consumer<ClientConnection>> onDisconnectSubscribers;

    /** Functions to call when a client submits their code */
    private final List<Consumer<ClientConnection>> onSubmissionSubscribers;

    private final ServerSocket serverSocket;
    private final Set<ClientConnection> connections;
    private final Set<String> nameSet;

    private volatile ServerState state;

    /**
     * Constructs a {@link HostServer}.
     * @throws IOException if an I/O error occurs when opening the socket.
     */
    public HostServer() throws IOException {
        this.onConnectSubscribers = new ArrayList<>();
        this.onDisconnectSubscribers = new ArrayList<>();
        this.onSubmissionSubscribers = new ArrayList<>();

        // Use port 0 to get auto-allocated port
        this.serverSocket = new ServerSocket(0);

        // Use "ConcurrentHashMap.newKeySet()" to create a concurrent set
        // which is both thread-safe and more performant than a synchronized set
        this.connections = ConcurrentHashMap.newKeySet();
        this.nameSet = ConcurrentHashMap.newKeySet();

        // Default server state
        this.state = ServerState.ACCEPTING;

        // JVM shutdown hook to close server when program exits
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                this.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));
    }

    /**
     * serve
     * Starts the server.
     */
    public void serve() {
        ServerThread serverThread = new ServerThread();
        serverThread.start();
    }

    /**
     * onConnect
     * Adds a listener function to call when a client socket connects.
     * @param subscriber the callback function
     */
    public void onConnect(Consumer<ClientConnection> subscriber) {
        this.onConnectSubscribers.add(subscriber);
    }

    /**
     * onDisconnect
     * Adds a listener function to call when a client socket disconnects.
     * @param subscriber the callback function
     */
    public void onDisconnect(Consumer<ClientConnection> subscriber) {
        this.onDisconnectSubscribers.add(subscriber);
    }

    /**
     * onSubmit
     * Adds a listener function to call when a client socket submits code
     * @param subscriber the callback function
     */
    public void onSubmit(Consumer<ClientConnection> subscriber) {
        this.onSubmissionSubscribers.add(subscriber);
    }

    /**
     * getInetAddress
     * Gets the ip address of the socket.
     * @return the ip address
     */
    public InetAddress getInetAddress() {
        return this.serverSocket.getInetAddress();
    }

    /**
     * getPort
     * Gets the port of the socket.
     * @return the port
     */
    public int getPort() {
        return this.serverSocket.getLocalPort();
    }

    /**
     * getNumConnections
     * Gets the number of concurrent connections.
     * @return the number of connections
     */
    public int getNumConnections() {
        return this.connections.size();
    }

    /**
     * getConnections
     * Gets the connection pool of the server
     * @return the connection pool
     */
    public Set<ClientConnection> getConnections() {
        return this.connections;
    }

    /**
     * setState
     * Changes the server state.
     * @param state the new server state
     */
    public void setState(ServerState state) {
        this.state = state;
    }

    /**
     * close
     * closes the server.
     * @throws IOException if an I/O error occurs when closing the socket
     */
    public void close() throws IOException {
        synchronized (serverSocket) {
            serverSocket.close();
            state = ServerState.CLOSED;
        }
    }

    /**
     * Runs the server on a single thread.
     * @author Harry Xu
     * @version 1.0 - December 24th 2023
     */
    public class ServerThread extends Thread {
        public void run() {
            while (state == ServerState.ACCEPTING) {
                // Accept connection
                ClientConnection client;

                try {
                    client = new ClientConnection(serverSocket.accept());
                } catch (IOException e) {
                    continue;
                }

                // Dispatch client connection to helper thread
                try {
                    HandlerThread handlerThread = new HandlerThread(client);
                    handlerThread.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Handles an individual client connection.
     * @author Harry Xu
     * @version 1.0 - December 24th 2023
     */
    public class HandlerThread extends Thread {
        private final ClientConnection client;
        private final BufferedReader input;
        private final BufferedWriter output;

        /**
         * Constructs a {@link HandlerThread} with a client connections.
         * @param client the client to handle
         * @throws IOException if an error occurs while accessing the client socket's I/O streams
         */
        public HandlerThread(ClientConnection client) throws IOException {
            this.client = client;
            this.input = new BufferedReader(new InputStreamReader(this.client.getSocket().getInputStream()));
            this.output = new BufferedWriter(new OutputStreamWriter(this.client.getSocket().getOutputStream()));
        }

        /**
         * createHeartbeat
         * schedules a task to repeatedly send messages to the client socket to ensure accurate client pool.
         * @return the timer used to schedule the task
         */
        public Timer createHeartbeat() {
            Timer heartbeat = new Timer();
            TimerTask heartbeatTask;

            try {
                heartbeatTask = new SocketHeartbeat(client);
            } catch (IOException e) {
                System.out.println("Cannot open heartbeat thread");
                throw new RuntimeException(e);
            }

            heartbeat.schedule(heartbeatTask, 0, 500);

            return heartbeat;
        }

        /**
         * close
         * closes the client socket and its I/O streams.
         * @throws IOException If an I/O error occurs
         */
        public void close() throws IOException {
            this.input.close();
            this.output.close();
            this.client.getSocket().close();
        }

        /**
         * handle
         * handles the client socket connection.
         * @throws IOException If an I/O error occurs
         */
        public void handle() throws IOException {
            // Check for valid name
            String name = this.input.readLine();

            if (nameSet.contains(name)) {
                output.write(ServerCode.DISCONNECT.ordinal());
                output.flush();
                return;
            }

            this.client.setName(name);
            nameSet.add(name);
            connections.add(this.client);

            // Call connect listeners
            synchronized (onConnectSubscribers) {
                for (Consumer<ClientConnection> onConnect : onConnectSubscribers) {
                    onConnect.accept(this.client);
                }
            }

            // Heartbeat
            Timer heartbeat = createHeartbeat();

            StringBuilder code = new StringBuilder();
            boolean broadcastNext = false;

            while (state != ServerState.CLOSED) {
                if (state == ServerState.CORRESPONDING) {
                    // Send next screen message to client
                    if (!broadcastNext) {
                        heartbeat.cancel();

                        this.output.flush();
                        this.output.write(ServerCode.NEXT_SCREEN.ordinal());
                        this.output.flush();
                        broadcastNext = true;
                    }

                    // Read sent code
                    int c = this.input.read();

                    if (c != ServerCode.SUBMISSION_FINISHED.ordinal()) {
                        code.append((char) c);
                    } else {
                        this.client.setCode(code.toString());
                        code = new StringBuilder();

                        // Call submit listeners
                        synchronized (onSubmissionSubscribers) {
                            for (Consumer<ClientConnection> onSubmit : onSubmissionSubscribers) {
                                onSubmit.accept(this.client);
                            }
                        }
                    }
                }
            }


            // Close clients
            this.output.write(ServerCode.NEXT_SCREEN.ordinal());
            this.output.flush();
        }

        /**
         * run
         * handles the client socket connection.
         */
        @Override
        public void run() {
            try {
                this.handle();
            } catch (IOException e) {
                System.out.println("Error occurred while handling client socket.");
                e.printStackTrace();
            } finally {
                try {
                    this.close();
                } catch (IOException e) {
                    System.out.println("Error occurred while attempting to close client socket");
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Sends a message to the client socket to verify its aliveness.
     * @author Harry Xu
     * @version 1.0 - December 24th 2023
     */
    public class SocketHeartbeat extends TimerTask {
        private final ClientConnection client;
        private final OutputStreamWriter output;

        /**
         * Instantiates a {@link SocketHeartbeat} with a client connection.
         * @throws IOException if an I/O error occurs
         */
        public SocketHeartbeat(ClientConnection client) throws IOException {
            this.client = client;
            this.output = new OutputStreamWriter(client.getSocket().getOutputStream());
        }

        /**
         * run
         * handles the client socket connection.
         */
        @Override
        public void run() {
            try {
                this.output.write(ServerCode.HEARTBEAT.ordinal());
                this.output.flush();
            } catch (SocketException e) {
                // Socket closed
                connections.remove(client);

                // Remove name from name set
                String clientName = client.getName();

                if (clientName != null) {
                    nameSet.remove(clientName);
                }

                // Call disconnect listeners
                synchronized (onDisconnectSubscribers) {
                    for (Consumer<ClientConnection> onDisconnect : onDisconnectSubscribers) {
                        onDisconnect.accept(client);
                    }
                }

                // Cancel task
                this.cancel();
            } catch (IOException e) {
                e.printStackTrace();

                // Cancel task
                this.cancel();
            }
        }
    }
}