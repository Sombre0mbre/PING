package fr.epita.assistants.myide.domain.entity.features.any;

import fr.epita.assistants.myide.domain.entity.Feature;
import fr.epita.assistants.myide.domain.entity.Mandatory;
import fr.epita.assistants.myide.domain.entity.Project;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class DistImplementation implements Feature {
    private void zipDirContent(Path rootPath, File dir, ZipOutputStream zipOutputStream) throws IOException {
        if (!dir.isDirectory())
            return;

        for (var i : Objects.requireNonNull(dir.listFiles())) {
            Path name;
            if (i.isDirectory()) {
                StringBuilder pathStr = new StringBuilder(rootPath.relativize(i.toPath()).toString());
                if (!pathStr.toString().endsWith("/"))
                    pathStr.append("/");

                zipOutputStream.putNextEntry(new ZipEntry(pathStr.toString()));
                zipOutputStream.closeEntry();

                zipDirContent(rootPath, i, zipOutputStream);
            } else if (i.isFile()) {
                zipOutputStream.putNextEntry(new ZipEntry(rootPath.relativize(i.toPath()).toString()));
                Files.copy(i.toPath(), zipOutputStream);
                zipOutputStream.closeEntry();
            }
        }
    }


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

            zipDirContent(rootPath, rootPath.toFile(), zipOutputStream);

            zipOutputStream.close();
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
        return Mandatory.Features.Any.DIST;
    }
}
