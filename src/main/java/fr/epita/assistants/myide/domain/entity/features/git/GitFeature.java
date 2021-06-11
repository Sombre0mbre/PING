package fr.epita.assistants.myide.domain.entity.features.git;

import fr.epita.assistants.myide.domain.entity.Feature;
import fr.epita.assistants.myide.domain.entity.Project;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;

public abstract class GitFeature implements Feature {
    final Repository repository;
    final Git git;

    public GitFeature(Repository repository) {
        this.repository = repository;
        this.git = new Git(repository);
    }
}
