package fr.epita.assistants.myide.domain.entity.features.any;

import fr.epita.assistants.myide.domain.entity.Feature;
import fr.epita.assistants.myide.domain.entity.Mandatory;
import fr.epita.assistants.myide.domain.entity.Project;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class DistImplementation implements Feature {

    /**
     * @param project {@link Project} on which the feature is executed.
     * @param params  Parameters given to the features.
     * @return {@link ExecutionReport}
     */
    @Override
    public ExecutionReport execute(Project project, Object... params) {

        var cleanup = new CleanupImplementation();
        var got = cleanup.execute(project);
        if (!got.isSuccess())
            return got;


        var rootPath = project.getRootNode().getPath();
        var name = params.length > 0 ?
                params[0].toString() :
                project.getRootNode().getPath().getFileName().toString() + ".zip";
        var zipPath = project.getRootNode().getPath().getParent().resolve(name);
        try {
            var outputStream = new FileOutputStream(zipPath.toFile());
            var zipOutputStream = new ZipOutputStream(outputStream);
            Files.walkFileTree(rootPath, new SimpleFileVisitor<>() {
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    zipOutputStream.putNextEntry(new ZipEntry(rootPath.relativize(file).toString()));
                    Files.copy(file, zipOutputStream);
                    zipOutputStream.closeEntry();
                    return FileVisitResult.CONTINUE;
                }
            });
            zipOutputStream.close();
            return () -> true;
        } catch (IOException e) {
            return () -> false;
        }

    }

    /**
     * @return The type of the Feature.
     */
    @Override
    public Type type() {
        return Mandatory.Features.Any.DIST;
    }
}
