package fr.epita.assistants.myide.domain.entity.features.git;

import fr.epita.assistants.myide.domain.entity.Mandatory;
import fr.epita.assistants.myide.domain.entity.Project;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;

import java.util.Arrays;

public class AddImplementation extends GitFeature {
    public AddImplementation(Git git) {
        super(git);
    }

    /**
     * @param project {@link Project} on which the feature is executed.
     * @param params  Parameters given to the features.
     * @return {@link ExecutionReport}
     */
    @Override
    public ExecutionReport execute(Project project, Object... params) {

        for(var pattern : params) {
            try {
                git.add().addFilepattern((String) pattern).call();
            } catch (GitAPIException e) {
                e.printStackTrace();
                return () -> false;
            }
        }
        return () -> true;

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
        return Mandatory.Features.Git.ADD;
    }
}
