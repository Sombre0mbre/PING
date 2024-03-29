package fr.epita.assistants.myide.domain.entity.features.any;

import fr.epita.assistants.myide.domain.entity.Feature;
import fr.epita.assistants.myide.domain.entity.Mandatory;
import fr.epita.assistants.myide.domain.entity.Project;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class CleanupImplementation implements Feature {
    private void cleanup(File file, Set<String> toDelete) throws IOException {
        if (!file.isDirectory())
            return;

        for (var i : Objects.requireNonNull(file.listFiles())) {
            cleanup(i, toDelete);

            if (toDelete.contains(i.getName())) {
                if (i.isDirectory())
                    FileUtils.deleteDirectory(i);
                else if (i.isFile())
                    Files.delete(i.toPath());
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
        var ideIgnore = project.getRootNode().getPath().resolve(".myideignore");
        if (!ideIgnore.toFile().exists() || !ideIgnore.toFile().isFile())
            return () -> false;
        try {
            var lines = new HashSet<>(Files.readAllLines(ideIgnore));
            cleanup(project.getRootNode().getPath().toFile(), lines);

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
