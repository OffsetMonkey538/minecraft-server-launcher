package top.offsetmonkey538;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("Loading args files...");
        final String preLaunchJarArgs = loadArgs("pre-launch.txt");
        final String actualJarArgs = loadArgs("actual-jar.txt");


        runJarFile(preLaunchJarArgs);
        runJarFile(actualJarArgs);
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

    private static void runJarFile(@NotNull String argsFile) {
        final List<String> args = new ArrayList<>();
        args.add("java");
        args.addAll(Arrays.stream(argsFile.split(" ")).map(String::trim).toList());

        System.out.println("Starting process with args: " + args);

        try {
            final ProcessBuilder processBuilder = new ProcessBuilder(args);
            processBuilder.inheritIO();

            final Process process = processBuilder.start();

            process.waitFor();
            final int exitCode = process.exitValue();
            if (exitCode != 0) {
                throw new IOException("Failed to execute jar!");
            }
        } catch (final IOException | InterruptedException e) {
            throw new RuntimeException("Failed to execute jar!", e);
        }
    }
}