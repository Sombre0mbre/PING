package fr.epita.assistants.myide.domain.entity.features.git;

import fr.epita.assistants.myide.domain.entity.Feature;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;

public abstract class GitFeature implements Feature {
    final Git git;

    public GitFeature(Git git) {
        this.git = git;
    }
}
