package fr.epita.assistants.myide.domain.entity.aspects;

import fr.epita.assistants.MyIde;
import fr.epita.assistants.myide.domain.entity.Aspect;
import fr.epita.assistants.myide.domain.entity.Feature;
import fr.epita.assistants.myide.domain.entity.Mandatory;
import fr.epita.assistants.myide.domain.entity.features.any.CleanupImplementation;
import fr.epita.assistants.myide.domain.entity.features.any.DistImplementation;
import fr.epita.assistants.myide.domain.entity.features.any.SearchImplementation;

import java.util.List;

public class AnyAspect implements Aspect {
    final MyIde.Configuration configuration;

    public AnyAspect(MyIde.Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public List<Feature> getFeatureList() {
        return List.of(
                new CleanupImplementation(), new DistImplementation(), new SearchImplementation(configuration)
        );
    }

    /**
     * @return The type of the Aspect.
     */
    @Override
    public Type getType() {
        return Mandatory.Aspects.ANY;
    }

    @Override
    public String toString() {
        return "AnyAspect{" +
                "configuration=" + configuration +
                '}';
    }
}
