package fr.epita.assistants.myide.domain.entity;

import fr.epita.assistants.utils.Given;
import org.eclipse.jgit.api.errors.GitAPIException;

import javax.validation.constraints.NotNull;
import java.io.IOException;

@Given(overridden = true)
public interface Feature {

    /**
     * @param project {@link Project} on which the feature is executed.
     * @param params  Parameters given to the features.
     * @return {@link ExecutionReport}
     */
    @NotNull ExecutionReport execute(final Project project, final Object... params) throws GitAPIException, IOException;

    /**
     * @return The type of the Feature.
     */
    @NotNull Type type();

    interface ExecutionReport {
        boolean isSuccess();
    }

    interface Type {
    }
}
