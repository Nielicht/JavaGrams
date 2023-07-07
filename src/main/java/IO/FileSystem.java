package IO;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class FileSystem {
    private FileSystem() {
        throw new IllegalStateException("Utility class");
    }

    public static Path[] getFPathsFromDirectory(String dPath) throws IOException {
        try (Stream<Path> list = Files.list(Path.of(ClassLoader.getSystemResource(dPath).getPath()))) {
            return list.sorted().toArray(Path[]::new);
        }
    }

    public static String getResourceSPath(String fPath) {
        return ClassLoader.getSystemResource(fPath).getPath();
    }

    public static BufferedReader getBReaderFromResource(String rPath) throws FileNotFoundException {
        return getBReaderFromPath(getResourceSPath(rPath));
    }

    public static BufferedReader getBReaderFromPath(String fPath) throws FileNotFoundException {
        return new BufferedReader(new FileReader(fPath));
    }
}
