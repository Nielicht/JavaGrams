package IO;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class FileSystem {
    private FileSystem() {
        throw new IllegalStateException("Utility class");
    }

    public static Path[] getFPathsFromResourceFolder(String folderRelPath) throws IOException {
        try (Stream<Path> list = Files.list(getResourcePath(folderRelPath))) {
            return list.sorted().toArray(Path[]::new);
        }
    }

    public static URL getResourceURL(String relativePath) {
        return ClassLoader.getSystemResource(relativePath);
    }

    public static String getResourceString(String relativePath) {
        return getResourceURL(relativePath).getPath();
    }

    public static Path getResourcePath(String relativePath) {
        return Path.of(getResourceString(relativePath));
    }

    public static BufferedReader getBReaderFromResource(String relativePath) throws FileNotFoundException {
        return getBReaderFromPath(getResourceString(relativePath));
    }

    public static BufferedReader getBReaderFromPath(String absolutePath) throws FileNotFoundException {
        return new BufferedReader(new FileReader(absolutePath));
    }
}
