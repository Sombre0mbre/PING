package fr.epita.assistants.myide.domain.entity;

public class AspectImplementation implements Aspect {
    Aspect.Type type;

    public AspectImplementation(Type type) {
        this.type = type;
    }

    /**
     * @return The type of the Aspect.
     */
    @Override
    public Type getType() {
        return type;
    }
}
