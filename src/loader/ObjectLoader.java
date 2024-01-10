package loader;

import client.ClientConnection;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class ObjectLoader {

    private final JavaCompiler compiler;
    private final File root;

    public ObjectLoader() throws IOException {
        this.compiler = ToolProvider.getSystemJavaCompiler();
        this.root = Files.createTempDirectory("players").toFile();
    }

    public Object load(ClientConnection client) throws IOException {
        if (!client.hasSubmitted() || client.getName() == null) {
            throw new IllegalArgumentException("Client has invalid data.");
        }

        String className = client.getName() + "Player";

        File sourceFile = new File(this.root, className + ".java");
        Files.write(sourceFile.toPath(), client.getCode().getBytes(StandardCharsets.UTF_8));

        this.compiler.run(null, null, null, sourceFile.getPath());

        try (URLClassLoader classLoader = new URLClassLoader(new URL[]{ root.toURI().toURL() })) {
            Class<?> cls = Class.forName(className, true, classLoader);
            return cls.getDeclaredConstructor().newInstance();
        } catch (ClassNotFoundException | InvocationTargetException | InstantiationException |
                 IllegalAccessException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        record
    }
}
