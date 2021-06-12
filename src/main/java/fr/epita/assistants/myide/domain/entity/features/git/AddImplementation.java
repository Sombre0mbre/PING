package fr.epita.assistants.myide.domain.entity.features.git;

import fr.epita.assistants.myide.domain.entity.Mandatory;
import fr.epita.assistants.myide.domain.entity.Project;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

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

        try {
            var add = git.add();
            for (var param : params)
                add.addFilepattern(param.toString());
            add.call();
            return () -> true;
        } catch (GitAPIException e) {
            e.printStackTrace();
            return () -> false;
        }
    }

    /**
     * @return The type of the Feature.
     */
    @Override
    public Type type() {
        return Mandatory.Features.Git.ADD;
    }
}
