package IO;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.stream.Stream;

public class FileSystem {
    private FileSystem() {
        throw new IllegalStateException("Utility class");
    }

    public static Path[] getFPathsFromResourceFolder(String folderRelPath) throws IOException, URISyntaxException {
        URL url = getResourceURL(folderRelPath);
        if (url == null) return null;
        URI uri = url.toURI();
        if (uri.getScheme().equals("jar")) return getFPathsFromResourceFolderJar(uri, folderRelPath);
        Path resourcePath = Path.of(getResourceString(folderRelPath));
        try (Stream<Path> list = Files.list(resourcePath)) {
            return list.sorted().toArray(Path[]::new);
        }
    }

    private static Path[] getFPathsFromResourceFolderJar(URI uri, String folderRelPath) throws IOException {
        try (java.nio.file.FileSystem fileSystem = FileSystems.newFileSystem(uri, Collections.emptyMap())) {
            Path resourcePath = fileSystem.getPath(folderRelPath);
            try (Stream<Path> list = Files.list(resourcePath)) {
                return list.sorted().toArray(Path[]::new);
            }
        }
    }

    public static URL getResourceURL(String relativePath) {
        URL resource = FileSystem.class.getResource(relativePath);
        return resource;
    }

    public static String getResourceString(String relativePath) {
        return getResourceURL(relativePath).getPath();
    }

    public static Path getResourcePath(String relativePath) {
        return Path.of(getResourceString(relativePath));
    }

    public static URI getResourceURI(String relativePath) {
        return getResourcePath(relativePath).toUri();
    }

    public static BufferedReader getBReaderFromPath(String absolutePath) throws FileNotFoundException {
        return new BufferedReader(new FileReader(absolutePath));
    }

    public static BufferedReader getBReaderFromResource(String relativePath) throws FileNotFoundException {
        return getBReaderFromPath(getResourceString(relativePath));
    }
}
