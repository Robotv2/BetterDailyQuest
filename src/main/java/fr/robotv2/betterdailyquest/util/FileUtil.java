package fr.robotv2.betterdailyquest.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;

public class FileUtil {

    public static String getFileNameWithoutExtension(File file) {
        return file.getName().substring(0, file.getName().lastIndexOf('.'));
    }

    public static void iterateFiles(File initial, Consumer<File> consumer) {
        if(!initial.exists()) {
            return;
        }

        final File[] files = initial.listFiles();

        if(files == null) {
            return;
        }

        for (File file : files) {
            if(file.isDirectory()) {
                iterateFiles(file, consumer);
            } else {
                consumer.accept(file);
            }
        }
    }

    public static void hideFolder(File folder) throws IOException {
        if (folder == null || !folder.isDirectory()) {
            throw new IllegalArgumentException("A valid directory was not provided.");
        }

        String osName = System.getProperty("os.name").toLowerCase();

        if (osName.contains("win")) {
            Path path = folder.toPath();
            Files.setAttribute(path, "dos:hidden", true);
        } else {
            if (folder.getName().startsWith(".")) {
                return;
            }

            File hiddenFolder = new File(folder.getParentFile(), "." + folder.getName());
            if (!folder.renameTo(hiddenFolder)) {
                throw new IOException("Failed to rename the folder to " + hiddenFolder.getAbsolutePath());
            }
        }
    }
}
