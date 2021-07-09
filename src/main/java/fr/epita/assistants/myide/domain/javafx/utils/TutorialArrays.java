package fr.epita.assistants.myide.domain.javafx.utils;

public class TutorialArrays {
    public final static TutorialElement[] gitElements = {
            new TutorialElement(
                    "Add",
                    """
                            La commande Add est utilisée pour ajouter des fichiers au dépôt.
                            Par exemple, la commande suivante ajoutera un fichier nommé temp.txt au répertoire local :

                            git add temp.txt
                            """
            ),
            new TutorialElement(
                    "Commit",
                    """
                            La commande Commit permet de valider les changements apportés.
                            Attention, il ne valide les changements que localement, sur votre ordinateur.
                            Il est habituellement accompagné d’un message de description :
                                                        
                            git commit –m “Description du commit”
                            """
            ),
            new TutorialElement(
                    "Push",
                    """
                            La commande Push permet d’envoyer sur le dépôt toutes les modifications
                            que vous avez enregistrées localement, soit tout ce que vous avez Commit.
                            """
            ),
            new TutorialElement(
                    "Pull",
                    """
                            La commande Pull permet de fusionner toutes les modifications présentes
                            sur le dépôt distant, c’est-à-dire tout ce que vos camardes ont pu Push sur le dépôt,
                            dans le répertoire de travail local, autrement dit sur votre ordinateur.
                            """
            )
    };

    public final static TutorialElement[] mavenElements = {
            new TutorialElement(
                    "Compile",
                    """
                            La commande Compile permet de compiler tout le code source,
                            c’est à dire qu’elle va transformer le code que vous venez d’écrire
                            en un programme exécutable par un ordinateur.
                            """
            ),
            new TutorialElement(
                    "Clean",
                    """
                            La commande Clean permet d’effacer tous les fichiers
                            ou dossiers générés durant la création (le build) d’un projet.
                            """
            ),
            new TutorialElement(
                    "Exec",
                    "Description 6"
            ),
            new TutorialElement(
                    "Install",
                    """
                            La commande Install permet de partager le livrable (le package)
                            pour d’autres projets en local, c’est-à-dire sur le même ordinateur.
                            """
            ),
            new TutorialElement(
                    "Package",
                    """
                            La commande Package permet d’assembler le code compilé en un livrable,
                            c’est-à-dire en un format qui peut être distribué (jar, war, …).
                            """
            ),
            new TutorialElement(
                    "Test",
                    """
                            La commande Test permet de lancer les tests unitaires.
                            """
            ),
            new TutorialElement(
                    "Tree",
                    """
                            La commande Tree permet d’afficher l’arbre des dépendances du projet,
                            avec le format de votre choix : text, DOT, GraphML, etc.
                            """
            )
    };

    public final static TutorialElement[] javaElements = {
            new TutorialElement(
                    "Types",
                    """
                            Un type désigne un ensemble de valeur.
                            Pour exemple les “Int” représente tous les entiers représentables d’une machine.
                            Ce sont donc des catégories qui permettent en outre, une meilleure gestion de la mémoire.
                            On n'a pas besoin d’allouer plus qu'un certain montant suivant le type.
                                                        
                            Les types primitifs de base sont :
                                                        
                            Byte, short, int, long, double, boulean , float, char.
                            """
            ),
            new TutorialElement(
                    "Conditions",
                    "Description 10"
            ),
            new TutorialElement(
                    "Boucles",
                    """
                            While :
                            Le but de la boucle while est de répéter une instruction ou un ensemble d’instructions,
                            tant qu’une condition particulière est vraie.
                                                        
                            For :
                            Le but de la boucle for est le même que la boucle while, mais possède quant à elle
                            en plus de l’expression de condition, une expression d’initialisation ainsi que d’une expression d’incrémentation.
                            """
            ),
            new TutorialElement(
                    "Fonctions",
                    "Description 12"
            ),
            new TutorialElement(
                    "Classes",
                    "Description 13"
            )
    };
}
