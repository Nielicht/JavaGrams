package IO;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.stream.Stream;

public class FileSystem {
    private FileSystem() {
        throw new IllegalStateException("Utility class");
    }

    public static Path[] getFPathsFromResourceFolder(String folderRelPath) {
        Path[] paths = null;

        try {
            URL url = getResourceURL(folderRelPath);
            if (url == null) return null;
            URI uri = url.toURI();
            String[] sArray = uri.toString().split("!");
//            URI uri = URI.create(url.toURI().toString().split("!")[0]);
            Path folder;

            if (uri.getScheme().equals("jar")) {
                try (java.nio.file.FileSystem fs = FileSystems.newFileSystem(URI.create(sArray[0]), Collections.emptyMap())) {
                    folder = fs.getPath(sArray[1]);
                    paths = getFolderPaths(folder);
                }
            } else {
                folder = Path.of(uri);
                paths = getFolderPaths(folder);
            }
//            try (java.nio.file.FileSystem fs = FileSystems.newFileSystem(uri, Collections.emptyMap())) {
//                folder = fs.getPath(folderRelPath);
//                paths = getFolderPaths(folder);
//            }
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }

        return paths;
    }

    private static Path[] getFolderPaths(Path folder) throws IOException {
        try (Stream<Path> files = Files.list(folder)) {
            return files.sorted().toArray(Path[]::new);
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

    public static BufferedReader getBReader(String path) {
        BufferedReader br = null;
        URL url = getResourceURL(path);
        URI uri = null;

        try {
            if (url != null) uri = URI.create(url.toURI().toString().split("!")[0]);
            if (uri != null && uri.getScheme().equals("jar")) {
                br = new BufferedReader(new InputStreamReader(FileSystem.class.getResourceAsStream(path)));
            } else {
                br = new BufferedReader(new FileReader(path));
            }
        } catch (FileNotFoundException | URISyntaxException e) {
            throw new RuntimeException(e);
        }

        return br;
    }
}
