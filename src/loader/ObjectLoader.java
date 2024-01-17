package loader;

import client.ClientConnection;
import game.Player;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

/**
 * Class for compiling, loading, and instantiating submitted {@link Player} classes.
 * @author Harry Xu
 * @version January 6th 2024
 * */
public class ObjectLoader {
    private final JavaCompiler compiler;
    private final File root;

    /**
     * Constructs an {@link ObjectLoader}.
     */
    public ObjectLoader() throws IOException {
        this.compiler = ToolProvider.getSystemJavaCompiler();
        this.root = Files.createTempDirectory("players").toFile();
    }

    /**
     * load
     * Loads a single {@link Player} object with a player name and the code.
     * @param client the client which has submitted the code
     * @return an instance of submitted {@link Player} class
     */
    public Player load(ClientConnection client) throws IOException, ObjectLoaderException {
        if (!client.hasSubmitted() || client.getName() == null) {
            throw new IllegalArgumentException("Client has invalid data.");
        }

        return this.load(client.getName(), client.getCode());
    }

    /**
     * load
     * Loads a single {@link Player} object with a player name and the code.
     * @param name the player name
     * @param code the player class source code as a {@link String}
     * @return an instance of submitted {@link Player} class
     */
    public Player load(String name, String code) throws IOException, ObjectLoaderException {
        String className = name + "Player";

        // Create temporary source file
        File sourceFile = new File(this.root, className + ".java");
        Files.write(sourceFile.toPath(), code.getBytes(StandardCharsets.UTF_8));

        int resultCode = this.compiler.run(null, null, null, sourceFile.getPath());

        if (resultCode != 0) {
            throw new ObjectLoaderException("Compilation Failed");
        }

        // Attempt to load class file
        try (URLClassLoader classLoader = new URLClassLoader(new URL[]{ root.toURI().toURL() })) {
            Class<?> cls = Class.forName(className, true, classLoader);
            Player player = (Player) cls.getDeclaredConstructor().newInstance();
            player.setName(name);
            return player;
        } catch (ClassNotFoundException |InvocationTargetException | InstantiationException |
                 IllegalAccessException | NoSuchMethodException e) {
            throw new ObjectLoaderException(e);
        } catch (ClassCastException e) {
            throw new ObjectLoaderException("Could not cast " + name + "'s class to a Player instance.");
        }
    }
}
