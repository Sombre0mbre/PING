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
            /*
            new TutorialElement(
                    "Exec",
                    """
                            La commande Exec permet d’exécuter les programmes et les programmes Java,
                            soit dans deux process différents, soit dans la même machine virtuelle.
                            """
            ),
             */
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
                    "Variables",
                    """
                            Une variable est une donnée possédant un nom.
                            Elle pourra être lu et utilisée plus tard dans le programme. 
                            Le nom d’une variable ne peut pas commencer par un chiffre, 
                            contenir des espaces ou s’écrire de la même manière qu'un mot réservé.
                                                        
                            Déclaration de variables :
                            Toutes les variables doivent être déclarées pour qu'elles soient utilisables :
                                TypeDeLaVariable nomDeLaVariable;
                            
                            Déclaration de 2 variables possédant le même type :
                                TypeDeLaVariable nom1, nom2;
                                
                            Exexmple concret :
                             String bonjour;
                            
                            Une variable permet de stocker une valeur que nous devons lui assigner. 
                                nomDeLaVariable = valeur;
                          
                            Ici, l’opérateur “=” affecter la valeur à la variable.
                            
                            Il est important de noter que la valeur et la variable doivent posséder
                            des types indentiques.
                            
                            Il est également possible d'effectuer l'opération suivante :
                                TypeDeLaVariable nomDeLaVariable = valeur;
                            """
            ),
            new TutorialElement(
                    "Conditions",
                    """
                            If :
                            L'instruction if est la structure de test la plus basique, 
                            on la retrouve dans une majorité des langages (sous différentes syntaxes). 
                            Elle permet d'exécuter une série d'instructions si jamais une condition est réalisée.
                            
                            Else :
                            Instruction complémentaire à l'if, elle permet d’effectuer des opérations 
                            lorsque la condition de l'if le précédant n’est pas respectée.
                            """
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
                    """
                            Une fonction est un bout de code que nous pouvons réutiliser à volonté.
                            On peut lui passer des paramètres et elle peut posséder une valeur de retour.
                            
                            La fonction suivante possède prend un entier et ne renvoie pas de valeur (void) :
                                public void maFonction(int a) {
                                    System.println(a);
                                }
                            La fonction suivante possède prend un entier et renvoie un entier :
                                public int square(int a) {
                                    return a * a;
                                }
                            Une fonction peut être publique (public, accessible partout), 
                            privée (private, accessible uniquement par la classe qui définit la fonction)
                            ou protégée (protected, accessible uniquement par la classe qui définit la fonction 
                            et ses classes filles).
                            """
            )
    };
}
