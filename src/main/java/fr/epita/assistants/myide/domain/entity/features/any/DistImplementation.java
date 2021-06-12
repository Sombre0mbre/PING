package fr.epita.assistants.myide.domain.entity.features.any;

import fr.epita.assistants.myide.domain.entity.Feature;
import fr.epita.assistants.myide.domain.entity.Mandatory;
import fr.epita.assistants.myide.domain.entity.Project;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.file.Path;
import java.util.Objects;

public class DistImplementation implements Feature {


    private void zipDirContent(Path rootPath, File dir, ArchiveOutputStream archive) throws IOException {
        if (!dir.isDirectory())
            return;
        StringBuilder pathStr = new StringBuilder(rootPath.relativize(dir.toPath()).toString());
        if (!pathStr.toString().endsWith("/"))
            pathStr.append("/");

        var entry = new ZipArchiveEntry(pathStr.toString());
        archive.putArchiveEntry(entry);
        archive.closeArchiveEntry();



        for (var i : Objects.requireNonNull(dir.listFiles())) {
            pathStr = new StringBuilder(rootPath.relativize(i.toPath()).toString());
            if (i.isDirectory()) {
                if (!pathStr.toString().endsWith("/"))
                    pathStr.append("/");

                entry = new ZipArchiveEntry(pathStr.toString());
                archive.putArchiveEntry(entry);
                archive.closeArchiveEntry();

                zipDirContent(rootPath, i, archive);
            } else {
                entry = new ZipArchiveEntry(pathStr.toString());
                archive.putArchiveEntry(entry);
                BufferedInputStream input = new BufferedInputStream(new FileInputStream(i));

                IOUtils.copy(input, archive);
                input.close();

                archive.closeArchiveEntry();
            }
        }
    }



    private String getName(Path rootPath, File i) {
        StringBuilder pathStr = new StringBuilder(rootPath.relativize(i.toPath()).toString());
        if (i.isDirectory() && !pathStr.toString().endsWith("/"))
            pathStr.append("/");
        return pathStr.toString();
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
            var archive = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, outputStream);

            zipDirContent(rootPath.getParent(), rootPath.toFile(), archive);

            archive.finish();
            outputStream.close();
            return () -> true;
        } catch (IOException | ArchiveException e) {
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
