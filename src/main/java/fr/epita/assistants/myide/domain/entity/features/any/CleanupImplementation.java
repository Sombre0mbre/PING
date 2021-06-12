package fr.epita.assistants.myide.domain.entity.features.any;

import fr.epita.assistants.myide.domain.entity.Feature;
import fr.epita.assistants.myide.domain.entity.Mandatory;
import fr.epita.assistants.myide.domain.entity.Project;
import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;

public class CleanupImplementation implements Feature {
    /**
     * @param project {@link Project} on which the feature is executed.
     * @param params  Parameters given to the features.
     * @return {@link ExecutionReport}
     */
    @Override
    public ExecutionReport execute(Project project, Object... params) {
        var ideIgnore = project.getRootNode().getPath().resolve(".myideignore");
        if (!ideIgnore.toFile().exists() || !ideIgnore.toFile().isFile())
            return () -> false;
        try {
            var lines = new HashSet<>(Files.readAllLines(ideIgnore));

            Files.walkFileTree(project.getRootNode().getPath(), new SimpleFileVisitor<>() {
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    if (lines.contains(file.getFileName().toString())) {
                        if (file.toFile().isDirectory()) {
                            FileUtils.deleteDirectory(file.toFile());
                        } else {
                            Files.delete(file);
                        }
                    }
                    return FileVisitResult.CONTINUE;
                }
            });

            return () -> true;
        } catch (IOException e) {
            e.printStackTrace();
            return () -> false;
        }
    }

    /**
     * @return The type of the Feature.
     */
    @Override
    public Type type() {
        return Mandatory.Features.Any.CLEANUP;
    }
}
