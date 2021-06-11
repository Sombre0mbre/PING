package fr.epita.assistants.myide.domain.entity.aspects;

import fr.epita.assistants.myide.domain.entity.Aspect;
import fr.epita.assistants.myide.domain.entity.Feature;
import fr.epita.assistants.myide.domain.entity.Mandatory;
import fr.epita.assistants.myide.domain.entity.features.git.AddImplementation;
import fr.epita.assistants.myide.domain.entity.features.git.CommitImplementation;
import fr.epita.assistants.myide.domain.entity.features.git.PullImplementation;
import fr.epita.assistants.myide.domain.entity.features.git.PushImplementation;
import org.eclipse.jgit.lib.Repository;

import java.util.List;

public class GitAspect implements Aspect {

    final Repository repository;

    public GitAspect(Repository repository) {
        this.repository = repository;
    }

    @Override
    public List<Feature> getFeatureList() {
        return List.of(
                new AddImplementation(), new CommitImplementation(),
                new PullImplementation(), new PushImplementation()
        );
    }

    /**
     * @return The type of the Aspect.
     */
    @Override
    public Type getType() {
        return Mandatory.Aspects.GIT;
    }
}
