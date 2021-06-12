package fr.epita.assistants.myide.domain.entity.aspects;

import fr.epita.assistants.myide.domain.entity.Aspect;
import fr.epita.assistants.myide.domain.entity.Feature;
import fr.epita.assistants.myide.domain.entity.Mandatory;
import fr.epita.assistants.myide.domain.entity.features.maven.*;

import java.util.List;

public class MavenAspect implements Aspect {

    @Override
    public List<Feature> getFeatureList() {
        return List.of(
                new CleanImplementation(), new CompileImplementation(), new ExecImplementation(),
                new InstallImplementation(), new PackageImplementation(), new TestImplementation(),
                new TreeImplementation()
        );
    }

    /**
     * @return The type of the Aspect.
     */
    @Override
    public Type getType() {
        return Mandatory.Aspects.MAVEN;
    }

    @Override
    public String toString() {
        return "MavenAspect{}";
    }
}
