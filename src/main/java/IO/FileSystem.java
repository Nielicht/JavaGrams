package IO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

    public static URL getResourceURL(String relativePath) {
        return FileSystem.class.getResource(relativePath);
    }

    public static InputStream getResourceStream(String relativePath) {
        return FileSystem.class.getResourceAsStream(relativePath);
    }

    public static BufferedReader getResourceBReader(String relativePath) {
//        Printer.print(Map.of("buffered reader path", relativePath));
        return new BufferedReader(new InputStreamReader(FileSystem.class.getResourceAsStream(relativePath)));
    }

    public static Path[] getFPathsFromResourceFolder(String folderRelPath) {
        Path[] paths = null;
//        Printer.print(Map.of("Folder", folderRelPath));

        try {
            String[] stringPaths = getResourceURL(folderRelPath).toURI().toString().split("!");
            URI uri = URI.create(stringPaths[0]);
            Path folder;

            switch (uri.getScheme()) {
                case "jar" -> {
                    try (java.nio.file.FileSystem fs = FileSystems.newFileSystem(uri, Collections.emptyMap())) {
                        folder = fs.getPath(stringPaths[1]);
                        paths = getFolderPaths(folder);
//                        for (int i = 0; i < paths.length; i++) Printer.print(Map.of("Path" + i, paths[i].toString()));
                    }
                }
                default -> {
                    folder = Path.of(uri);
                    paths = getFolderPaths(folder);
//                    for (int i = 0; i < paths.length; i++) Printer.print(Map.of("Path" + i, paths[i].toString()));
                }
            }
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }

        return paths;
    }

    private static Path[] getFolderPaths(Path folder) throws IOException {
        Path[] finalPaths;
        try (Stream<Path> paths = Files.list(folder)) {
            Path[] temporalPaths = paths.sorted().toArray(Path[]::new);         // Paths extracted from given folder
            finalPaths = new Path[temporalPaths.length];                        // Paths to return

            // Converts the paths to relative ones
            for (int i = 0; i < temporalPaths.length; i++) {
                // Necessary variables
                String temporalPathAsString = temporalPaths[i].toString();      // Original Path to string form
                String finalPathAsString = temporalPathAsString;                // Final Path placeholder string
                int index = temporalPathAsString.lastIndexOf("/classes");   // Reference for trimming original path

                // Applies the transformation only if path is not already relative
                // (JAR absolutes look like relatives, so this is not executed in those cases)
                if (temporalPathAsString.contains("/classes")) {
                    finalPathAsString = temporalPathAsString.substring(index + 8);  // It adds 8 as lastIndexOf
                    // returns the position of
                    // the first letter in the
                    // matched text
                }

                // Paths extracted and adapted from the folder are used to build the final array
                finalPaths[i] = Path.of(finalPathAsString);
            }
        }
        return finalPaths;
    }
}
