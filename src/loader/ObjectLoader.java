package loader;

import client.ClientConnection;
import game.Player;

import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class ObjectLoader {

    private final JavaCompiler compiler;
    private final StandardJavaFileManager fileManager;
    private final File root;

    public ObjectLoader() throws IOException {
        this.compiler = ToolProvider.getSystemJavaCompiler();
        this.fileManager = this.compiler.getStandardFileManager(null, null, null);
        this.root = Files.createTempDirectory("players").toFile();
    }

    public Player load(ClientConnection client) throws IOException, ObjectLoaderException {
        if (!client.hasSubmitted() || client.getName() == null) {
            throw new IllegalArgumentException("Client has invalid data.");
        }

        String className = client.getName() + "Player";

        File sourceFile = new File(this.root, className + ".java");
        Files.write(sourceFile.toPath(), client.getCode().getBytes(StandardCharsets.UTF_8));


        int resultCode = this.compiler.run(null, null, System.out, sourceFile.getPath());

        if (resultCode != 0) {
            throw new ObjectLoaderException("Compilation Failed");
        }

        try (URLClassLoader classLoader = new URLClassLoader(new URL[]{ root.toURI().toURL() })) {
            Class<?> cls = Class.forName(className, true, classLoader);
            return (Player) cls.getDeclaredConstructor().newInstance();
        } catch (ClassNotFoundException |InvocationTargetException | InstantiationException |
                 IllegalAccessException | NoSuchMethodException e) {
            throw new ObjectLoaderException(e);
        } catch (ClassCastException e) {
            throw new ObjectLoaderException("Could not cast " + client.getName() + "'s class to a Player instance.");
        }
    }
}
