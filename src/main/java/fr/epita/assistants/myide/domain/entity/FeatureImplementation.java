package fr.epita.assistants.myide.domain.entity;

public class FeatureImplementation implements Feature {
    Feature.Type type;

    public FeatureImplementation(Type type) {
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
    }

    /**
     * @return The type of the Feature.
     */
    @Override
    public Type type() {
        return type;
    }
}
