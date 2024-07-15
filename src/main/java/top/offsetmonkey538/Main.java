package top.offsetmonkey538;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
        final String preLaunchJarString = loadProperty("preLaunchJar");
        final String actualJarString = loadProperty("actualJar");

        final Path preLaunchJar = Path.of(preLaunchJarString);
        final Path actualJar = Path.of(actualJarString);

        if (!Files.exists(preLaunchJar)) throw new IllegalArgumentException("File '" + preLaunchJarString + "' does not exist!");
        if (!Files.exists(actualJar)) throw new IllegalArgumentException("File '" + actualJar + "' does not exist!");


        final String preLaunchJarArgs = loadProperty("preLaunchJarArgs", true);
        final String actualJarArgs = loadProperty("actualJarArgs", true);

        runJarFile(preLaunchJar, preLaunchJarArgs);
        runJarFile(actualJar, actualJarArgs);
    }

    @NotNull
    private String loadProperty(@NotNull final String propertyName) throws IOException {
        return loadProperty(propertyName, false);
    }

    @NotNull
    private String loadProperty(@NotNull final String propertyName, boolean mayBeEmpty) throws IOException {
        String result = System.getProperty(propertyName);

        if (result != null) return result;

        System.out.println("Property '" + propertyName + "' is null! Checking for '" + propertyName + ".txt' file...");
        final Path propertyTxtFile = Path.of(propertyName + ".txt");
        if (Files.exists(propertyTxtFile)) return Files.readString(propertyTxtFile).trim();

        if (!mayBeEmpty) throw new RuntimeException("Couldn't find '" + propertyName + ".txt' file!");

        System.out.println("Couldn't find '" + propertyName + ".txt' file! Continuing with empty string...");
        return "";
    }

    private void runJarFile(@NotNull Path jarFile, @NotNull String launchArgs) throws IOException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        final JarInputStream jarInputStream = new JarInputStream(new FileInputStream(jarFile.toFile()));
        final Manifest manifest = jarInputStream.getManifest();
        jarInputStream.close();

        @Nullable final String mainClass = manifest.getMainAttributes().getValue("Main-Class");

        if (mainClass == null) throw new RuntimeException("Couldn't find main class of jar file '" + jarFile + "'!");

        final URLClassLoader jarClassLoader = new URLClassLoader(new URL[]{jarFile.toUri().toURL()}, this.getClass().getClassLoader());
        final Class<?> classToLoad = Class.forName(mainClass, true, jarClassLoader);
        final Method mainMethod = classToLoad.getMethod("main", String[].class);

        System.out.println("Running method '" + mainMethod.getName() + "' from class '" + mainClass + "' in jar file '" + jarFile + "' with arguments '" + launchArgs + "'...");
        mainMethod.invoke(null, (Object) launchArgs.split(" "));
    }
}