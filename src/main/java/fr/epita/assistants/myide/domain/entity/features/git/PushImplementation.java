package fr.epita.assistants.myide.domain.entity.features.git;

import fr.epita.assistants.myide.domain.entity.Feature;
import fr.epita.assistants.myide.domain.entity.Mandatory;
import fr.epita.assistants.myide.domain.entity.Project;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.File;
import java.io.IOException;

public class PushImplementation implements Feature {
    /**
     * @param project {@link Project} on which the feature is executed.
     * @param params  Parameters given to the features.
     * @return {@link ExecutionReport}
     */
    @Override
    public ExecutionReport execute(Project project, Object... params) {
        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        Repository repository = null;
        try {
            repository = builder.setGitDir(new File(String.valueOf(project.getRootNode().getPath())))
                    .readEnvironment()
                    .findGitDir()
                    .build();
        } catch (IOException e) {
            e.printStackTrace();
            return () -> false;
        }

        assert repository != null;
        Git git = new Git(repository);
        try {
            git.push().call();
            return () -> true;
        } catch (GitAPIException e) {
            e.printStackTrace();
            return () -> false;
        }
        //throw new UnsupportedOperationException("FIXME");
        /*
        int returnCode = exec("mvn", "compile");
        return () -> (returnCode == 0);
        */
    }

    /**
     * @return The type of the Feature.
     */
    @Override
    public Type type() {
        return Mandatory.Features.Git.PUSH;
    }
}
