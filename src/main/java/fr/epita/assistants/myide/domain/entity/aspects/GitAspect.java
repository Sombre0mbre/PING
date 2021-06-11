package fr.epita.assistants.myide.domain.entity.aspects;

import fr.epita.assistants.myide.domain.entity.Aspect;
import fr.epita.assistants.myide.domain.entity.Feature;
import fr.epita.assistants.myide.domain.entity.Mandatory;
import fr.epita.assistants.myide.domain.entity.features.git.AddImplementation;
import fr.epita.assistants.myide.domain.entity.features.git.CommitImplementation;
import fr.epita.assistants.myide.domain.entity.features.git.PullImplementation;
import fr.epita.assistants.myide.domain.entity.features.git.PushImplementation;
import org.eclipse.jgit.api.Git;

import java.util.List;

public class GitAspect implements Aspect {

    final Git git;

    public GitAspect(Git git) {
        this.git = git;
    }

    @Override
    public List<Feature> getFeatureList() {
        return List.of(
                new AddImplementation(git), new CommitImplementation(git),
                new PullImplementation(git), new PushImplementation(git)
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
