package top.offsetmonkey538;

import org.jetbrains.annotations.NotNull;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;

public class Main {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        new Main().run();
    }

    private void run() throws IOException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        final String preLaunchJarString = System.getProperty("preLaunchJar");
        final String actualJarString = System.getProperty("actualJar");

        if (preLaunchJarString == null) throw new IllegalArgumentException("Property 'preLaunchJar' is null!");
        if (actualJarString == null) throw new IllegalArgumentException("Property 'actualJar' is null!");

        final Path preLaunchJar = Path.of(preLaunchJarString);
        final Path actualJar = Path.of(actualJarString);

        if (!Files.exists(preLaunchJar)) throw new IllegalArgumentException("File '" + preLaunchJarString + "' does not exist!");
        if (!Files.exists(actualJar)) throw new IllegalArgumentException("File '" + actualJar + "' does not exist!");


        final String preLaunchJarArgs = System.getProperty("preLaunchJarArgs", "");
        final String actualJarArgs = System.getProperty("actualJarArgs", "");

        runJarFile(preLaunchJar, preLaunchJarArgs);
        runJarFile(actualJar, actualJarArgs);
    }

    @NotNull
    private static String loadArgs(final String fileName) {
        final Path filePath = Path.of(fileName);

        try {
            return Files.readString(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load args for file " + fileName, e);
        }
    }

    private void runJarFile(@NotNull Path jarFile, @NotNull String launchArgs) throws IOException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        final JarInputStream jarInputStream = new JarInputStream(new FileInputStream(jarFile.toFile()));
        final Manifest manifest = jarInputStream.getManifest();
        jarInputStream.close();

        final String mainClass = manifest.getMainAttributes().getValue("Main-Class");

        final URLClassLoader jarClassLoader = new URLClassLoader(new URL[]{jarFile.toUri().toURL()}, this.getClass().getClassLoader());
        final Class<?> classToLoad = Class.forName(mainClass, true, jarClassLoader);
        final Method mainMethod = classToLoad.getMethod("main", String[].class);
        mainMethod.invoke(null, (Object) launchArgs.split(" "));
    }
}