package fr.epita.assistants.myide.domain.entity.features.any;

import fr.epita.assistants.myide.domain.entity.Feature;
import fr.epita.assistants.myide.domain.entity.Project;

public class SearchImplementation implements Feature {
    Type type;

    public SearchImplementation(Type type) {
        this.type = type;
    }

    /**
     * @param project {@link Project} on which the feature is executed.
     * @param params  Parameters given to the features.
     * @return {@link ExecutionReport}
     */
    @Override
    public ExecutionReport execute(Project project, Object... params) {
        throw new UnsupportedOperationException("FIXME");
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
        return type;
    }
}
