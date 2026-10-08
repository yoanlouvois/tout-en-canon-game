package Controller;

import Application.App;
import Model.Monstre;
import Model.Salle;
import javafx.animation.AnimationTimer;
import javafx.animation.PathTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.util.Duration;
import javafx.scene.paint.Color;


import java.awt.*;
import java.awt.geom.Point2D;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static com.sun.glass.ui.Cursor.setVisible;
import static java.awt.Color.BLUE;
import static java.awt.Color.blue;

public class ControllerJeu {

    public Pane panePrincipal;
    public Label labelJoueurJeu;
    public Label labelClasseJeu;
    public Label labelAttaqueJeu;
    public Label labelVitesseJeu;
    public Button btnMP;
    public Pane mouvementArea;
    public ImageView imageCharactere;
    public ImageView imageVie;
    public ImageView imageCle;

    public ImageView imageCleSalle;
    public ImageView imageInventaire;
    public Label label;
    private Set<KeyCode> pressedKeys = new HashSet<>();

    public ImageView imagePotionSalle;

    @FXML
    private Circle bille;

    private double offsetX;
    private double offsetY;


    private App app;

    public ControllerJeu(App app) {
        this.app = app;
    }

    public ControllerJeu() {}

    @FXML
    public void initialize() {
        //Image de la clé
        Image pied_de_biche = new Image("/ressource/non_cle.png");
        imageCle.setImage(pied_de_biche);
        // Ajouter un listener sur la propriété cle du personnage
        App.getJeuInstance().getPersonnage().cleProperty().addListener((observable, oldValue, newValue) -> {
            updateCleImage();
        });

        //initialise la première salle comme étant visité
        App.getJeuInstance().getMap().getSallePos(App.getJeuInstance().getPosXJoueurMap(),
                App.getJeuInstance().getPosYJoueurMap()).setVisited(true);

        // Configurer les événements de drag and drop
        imageCle.setOnMousePressed(this::handleMousePressed);
        imageCle.setOnMouseDragged(this::handleMouseDragged);
        imageCle.setOnMouseReleased(this::handleMouseReleased);

        imageVie.setImage(image_de_la_vie());
        // Listener sur la propriété nbDeVie du personnage
        App.getJeuInstance().getPersonnage().nbDeVieProperty().addListener((observable, oldValue, newValue) -> {
            imageVie.setImage(image_de_la_vie());
        });

        //Image des potions
        imageInventaire.setImage(imagePotion());
        App.getJeuInstance().getPersonnage().nbDePotionsProperty().addListener((observable, oldValue, newValue) -> {
            imageInventaire.setImage(imagePotion());
        });

        labelJoueurJeu.setText(App.getJeuInstance().getPseudoUtilisateur());
        labelClasseJeu.setText(App.getJeuInstance().getPersonnage().getNomClasse());
        labelAttaqueJeu.setText("Attaque : " + App.getJeuInstance().getPersonnage().getAttaque());
        labelVitesseJeu.setText("Vitesse : " + App.getJeuInstance().getPersonnage().getVitesse());

        if(Objects.equals(App.getJeuInstance().getPersonnage().getNomClasse(), "Egyptien")){
            Image egyptien = new Image("/ressource/egyptien.png");
            imageCharactere.setImage(egyptien);
        }
        else {
            Image momie = new Image("/ressource/momie.png");
            imageCharactere.setImage(momie);
        }

        imageCharactere.setLayoutX(mouvementArea.getPrefWidth() / 4 - imageCharactere.getFitWidth() / 4);
        imageCharactere.setLayoutY(mouvementArea.getPrefHeight() / 4 - imageCharactere.getFitHeight() / 4);



        // Création des monstres sur le terrain
        for (Monstre monstre : App.getJeuInstance().getMap().getSalle(App.getJeuInstance().getCurrentSalle()).getMonstresSalle()) {
            ImageView monstreImageView = monstre.getMonstreImageView();
            System.out.println("Ajout du monstre aux coordonnées : (" + monstre.getPosX() + ", " + monstre.getPosY() + ")");
            monstreImageView.setLayoutX(monstre.getPosX());
            monstreImageView.setLayoutY(monstre.getPosY());
            monstreImageView.setVisible(true);
            mouvementArea.getChildren().add(monstreImageView);
            monstre.deplacementMonstre(imageCharactere.getLayoutX(),imageCharactere.getLayoutY());

            AnimationTimer timer = new AnimationTimer() {
                @Override
                public void handle(long now) {
                    int posXJoueur = (int) imageCharactere.getLayoutX();
                    int posYJoueur = (int) imageCharactere.getLayoutY();
                    monstre.deplacementMonstre(posXJoueur, posYJoueur);
                    monstreImageView.setImage(monstre.getMonstreImageView().getImage());
                }
            };
            timer.start();

            // Ajout de listeners pour mettre à jour la position des monstres
            monstre.posXProperty().addListener((observable, oldValue, newValue) -> {
                monstreImageView.setLayoutX(newValue.doubleValue());
                monstreImageView.setImage(monstre.getMonstreImageView().getImage());

            });
            monstre.posYProperty().addListener((observable, oldValue, newValue) -> {
                monstreImageView.setLayoutY(newValue.doubleValue());
                monstreImageView.setImage(monstre.getMonstreImageView().getImage());

            });
        }


        // Configurer l'événement de clic de souris pour lancer la bille sur l'ensemble de l'écran
        panePrincipal.setOnMouseClicked(this::handleMouseClicked);

        // Ajouter un listener pour les événements de touche à la scène
        Scene scene = mouvementArea.getScene();
        if (scene != null) {
            scene.setOnKeyPressed(this::handleKeyPress);
            scene.setOnKeyReleased(this::handleKeyRelease);
        } else {
            mouvementArea.sceneProperty().addListener((observable, oldScene, newScene) -> {
                if (newScene != null) {
                    newScene.setOnKeyPressed(this::handleKeyPress);
                    newScene.setOnKeyReleased(this::handleKeyRelease);
                }
            });
        }
        System.out.println("salle numero "+App.getJeuInstance().getPosXJoueurMap() + " ;" +
                App.getJeuInstance().getPosYJoueurMap());
        System.out.println("la salle courante est la numero :"+App.getJeuInstance().getMap().getSalle
                (App.getJeuInstance().getCurrentSalle()));
    }

    private void handleMousePressed(MouseEvent event) {
        // Enregistrer l'offset entre la position de la souris et l'ImageView
        offsetX = event.getSceneX() - imageCle.getLayoutX();
        offsetY = event.getSceneY() - imageCle.getLayoutY();
    }

    private void handleMouseDragged(MouseEvent event) {
        imageCle.setLayoutX(event.getSceneX() - offsetX);
        imageCle.setLayoutY(event.getSceneY() - offsetY);
    }

    private void handleMouseReleased(MouseEvent event) {
        // Détecter si la clé est utilisée sur une porte du boss
        double finalX = imageCle.getLayoutX();
        double finalY = imageCle.getLayoutY();

        if (detecterPorteBoss(finalX, finalY)) {
            App.getJeuInstance().getPersonnage().setCle(false);
            App.getJeuInstance().setBossDeverouille(true);
            label.setText("Porte du boss dévérouillée !");
            imageCle.setLayoutX(0);
            imageCle.setLayoutY(600);
        } else {
            // Remettre la clé à sa position initiale ou une autre position si nécessaire
            imageCle.setLayoutX(0);
            imageCle.setLayoutY(600);
        }
    }

    public boolean detecterPorteBoss(double offsetX,double offsetY){
        Salle currentSalle = App.getJeuInstance().getMap().getSalle(App.getJeuInstance().getCurrentSalle());
        if(App.getJeuInstance().getMap().isPorteBoss(currentSalle)){
            if (App.getJeuInstance().getMap().isTopBoss(currentSalle)){
                return offsetX <= 335 && offsetX >= 315 && offsetY <= 15;
            }
            else if (App.getJeuInstance().getMap().isBottomBoss(currentSalle)){
                return offsetY >= 605 && offsetX >= 315 && offsetX <= 335;
            }
            else if (App.getJeuInstance().getMap().isLeftBoss(currentSalle)){
                return offsetX < 35 && offsetY >= 315 && offsetY <= 355;
            }
            else if (App.getJeuInstance().getMap().isRightBoss(currentSalle)){
                return offsetX > 615 && offsetY >= 315 && offsetY <= 355;
            }
        }
        return false;
    }

    private void handleKeyPress(KeyEvent event) {
        pressedKeys.add(event.getCode());
        moveCharacter();
    }

    private void handleKeyRelease(KeyEvent event) {
        pressedKeys.remove(event.getCode());
        moveCharacter();
    }

    private void moveCharacter() {
        double x = imageCharactere.getLayoutX();
        double y = imageCharactere.getLayoutY();
        int speed = App.getJeuInstance().getPersonnage().getVitesse();

        if (pressedKeys.contains(KeyCode.Z) && y - speed >= 0) {
            //System.out.println(x+" :"+y);
            imageCharactere.setLayoutY(y - speed);
            deplacementSalle();
            effacePotion();
            effaceCle();
        }
        if (pressedKeys.contains(KeyCode.S) && y + speed <= mouvementArea.getPrefHeight() - imageCharactere.getFitHeight()) {
            //System.out.println(x+" :"+y);
            imageCharactere.setLayoutY(y + speed);
            deplacementSalle();
            effacePotion();
            effaceCle();
        }
        if (pressedKeys.contains(KeyCode.Q) && x - speed >= 0) {
            //System.out.println(x+" :"+y);
            imageCharactere.setLayoutX(x - speed);
            deplacementSalle();
            effacePotion();
            effaceCle();
        }
        if (pressedKeys.contains(KeyCode.D) && x + speed <= mouvementArea.getPrefWidth() - imageCharactere.getFitWidth()) {
            //System.out.println(x+" :"+y);
            imageCharactere.setLayoutX(x + speed);
            deplacementSalle();
            effacePotion();
            effaceCle();
        }
        if (pressedKeys.contains(KeyCode.R)) {
            App.setSceneMap();
            pressedKeys.clear();
        }
        if (pressedKeys.contains(KeyCode.ESCAPE)) {
            App.setSceneAccueil();
            pressedKeys.clear();
        }
        if (pressedKeys.contains(KeyCode.A)) {
            App.getJeuInstance().getPersonnage().seSoigner();
            pressedKeys.clear();
        }
        if (pressedKeys.contains(KeyCode.F)) {
            System.out.println("F enfoncé");
            pressedKeys.clear();
        }

    }
    private void handleMouseClicked(MouseEvent event) {
        // Vérifier que c'est un clic gauche uniquement
        if (event.getButton() == MouseButton.PRIMARY) {
            // Obtenir la position de la souris
            double targetX = event.getSceneX();
            double targetY = event.getSceneY();

            // Créer une nouvelle bille avec un dégradé de couleur
            Circle newBille = createGradientBille();
            panePrincipal.getChildren().add(newBille);

            // Placer la bille à la position du personnage
            double startX = imageCharactere.getLayoutX() + 400 + imageCharactere.getFitWidth() / 2 - newBille.getRadius();
            double startY = imageCharactere.getLayoutY() + imageCharactere.getFitHeight() / 2 - newBille.getRadius();
            newBille.setCenterX(startX);
            newBille.setCenterY(startY);

            // Calculer la direction et la distance pour que la bille continue son chemin
            double dx = targetX - startX;
            double dy = targetY - startY;
            double distance = Math.sqrt(dx * dx + dy * dy);

            // Normaliser la direction
            dx /= distance;
            dy /= distance;

            // Définir la vitesse de la bille
            double speed = 100; // pixels par seconde

            // Utiliser AnimationTimer pour animer la bille
            double finalDx = dx;
            double finalDy = dy;
            AnimationTimer timer = new AnimationTimer() {
                @Override
                public void handle(long now) {
                    // Calculer le déplacement
                    double deltaX = finalDx * speed / 60; // 60 FPS
                    double deltaY = finalDy * speed / 60;

                    // Mettre à jour la position de la bille
                    newBille.setCenterX(newBille.getCenterX() + deltaX);
                    newBille.setCenterY(newBille.getCenterY() + deltaY);

                    // Vérifier la collision avec un monstre
                    if (App.getJeuInstance().getMap().getSalle(App.getJeuInstance().getCurrentSalle()).checkCollisionWithMonstres(newBille)) {
                        panePrincipal.getChildren().remove(newBille);
                        this.stop();
                        System.out.println("Bille retirée après collision avec un monstre.");
                        return;
                    }

                    // Vérifier si la bille dépasse les limites
                    if (isOutOfBounds(newBille)) {
                        panePrincipal.getChildren().remove(newBille);
                        this.stop();
                        System.out.println("Bille retirée après dépassement des limites.");
                        return;
                    }
                }
            };
            timer.start();
        }
    }

    private boolean isOutOfBounds(Circle bille) {
        double centerX = bille.getCenterX();
        double centerY = bille.getCenterY();
        double radius = bille.getRadius();
        return centerX < 400 || centerX > 1200 || centerY < 0 || centerY > 800;
    }

    private Circle createGradientBille() {
        Circle bille = new Circle(10);

        // Créer un dégradé de couleur pour la bille
        Stop[] stops = new Stop[] {
                new Stop(0, Color.YELLOWGREEN),
                new Stop(1, Color.LIGHTYELLOW)
        };
        LinearGradient gradient = new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE, stops);
        bille.setFill(gradient);

        return bille;
    }
    //methode pour adapter l'image de la clé en fonction de si l'utilisateur l'a ou non
    private void updateCleImage() {
        if (App.getJeuInstance().getPersonnage().isCle()) {
            Image pied_de_biche = new Image("/ressource/pied_de_biche.png");
            imageCle.setImage(pied_de_biche);
        } else {
            Image pied_de_biche = new Image("/ressource/non_cle.png");
            imageCle.setImage(pied_de_biche);
        }
    }

    public Image image_de_la_vie(){
        if(Objects.equals(App.getJeuInstance().getPersonnage().getNomClasse(), "Momie")){
            if(App.getJeuInstance().getPersonnage().getNbDeVie()==1){return new Image("/ressource/Momie_1_vie.png");}
            else if(App.getJeuInstance().getPersonnage().getNbDeVie()==2){return new Image("/ressource/Momie_2_vie.png");}
            else if(App.getJeuInstance().getPersonnage().getNbDeVie()==3){return new Image("/ressource/Momie_3_vie.png");}
            else if(App.getJeuInstance().getPersonnage().getNbDeVie()==4){return new Image("/ressource/Momie_4_vie.png");}
            else{return new Image("/ressource/Momie_5_vie.png");}
        }
        else{
            if (App.getJeuInstance().getPersonnage().getNbDeVie()==1){return new Image("/ressource/Egyptien_1_vie.png");}
            else if (App.getJeuInstance().getPersonnage().getNbDeVie()==2){return new Image("/ressource/Egyptien_2_vie.png");}
            else if (App.getJeuInstance().getPersonnage().getNbDeVie()==3){return new Image("/ressource/Egyptien_3_vie.png");}
            else if (App.getJeuInstance().getPersonnage().getNbDeVie()==4){return new Image("/ressource/Egyptien_4_vie.png");}
            else{return new Image("/ressource/Egyptien_5_vie.png");}
        }
    }
    public Image imagePotion(){
        if (App.getJeuInstance().getPersonnage().getPotion()==0){return new Image("/ressource/potions_0.png");}
        if (App.getJeuInstance().getPersonnage().getPotion()==1){return new Image("/ressource/potion_1.png");}
        if (App.getJeuInstance().getPersonnage().getPotion()==2){return new Image("/ressource/potion_2.png");}
        else{return new Image("/ressource/potion_3.png");}
    }

    public boolean proximiteHaut() {
        double y = imageCharactere.getLayoutY();
        double x = imageCharactere.getLayoutX();
        return (x<=335 && x>=315 && y<=15);
    }

    public boolean proximiteBas() {
        double y = imageCharactere.getLayoutY();
        double x = imageCharactere.getLayoutX();
        return (y >= 605 && x >= 315 && x <= 335);
    }

    public boolean proximiteDroite() {
        double y = imageCharactere.getLayoutY();
        double x = imageCharactere.getLayoutX();
        return (x > 615 && y >= 315 && y <= 355);
    }

    public boolean proximiteGauche() {
        double y = imageCharactere.getLayoutY();
        double x = imageCharactere.getLayoutX();
        return (x < 35 && y >= 315 && y <= 355);
    }
    public boolean proximiteCentre() {
        double y = imageCharactere.getLayoutY();
        double x = imageCharactere.getLayoutX();
        return (x >= 325 && x <= 475 && y >= 325 && y <= 375);
    }

    public void deplacementSalle(){
        Salle currentSalle = App.getJeuInstance().getMap().getSalle(App.getJeuInstance().getCurrentSalle());
        currentSalle.setVisited(true);
        if(App.getJeuInstance().getMap().isPorteBoss(currentSalle)){label.setText("La porte du boss est proche");}
        else{label.setText("");}
        if(currentSalle.isSalleBoss()){label.setText("CEST GAGNE !");}
        if (proximiteHaut()) {
            if(App.getJeuInstance().getMap().franchirPorteHaut(currentSalle,App.getJeuInstance().isBossDeverouille())){
                System.out.println("changement depuis la salle pos "+App.getJeuInstance().getPosXJoueurMap() + " ;" +
                        App.getJeuInstance().getPosYJoueurMap());
            //modifier la position du joueur dans la map
            App.getJeuInstance().setPosYJoueurMap(App.getJeuInstance().getPosYJoueurMap()-1);
                System.out.println("le joueur est mtn à la position "+App.getJeuInstance().getPosXJoueurMap() + " ;" +
                        App.getJeuInstance().getPosYJoueurMap() +"\n");
            //modifie le currentSalleID du joueur
            int posx =  App.getJeuInstance().getPosXJoueurMap();
            int posy = App.getJeuInstance().getPosYJoueurMap();
            App.getJeuInstance().setCurrentSalleID(App.getJeuInstance().getMap().getSallePos
                    (posx,posy).getIdSalle());
                System.out.println("Le id de la salle est :"+App.getJeuInstance().getCurrentSalle());
            //donne un interchangement possible de salle
            App.getJeuInstance().setDeplacementPossible(true);
            //modifier la position du joueur dans la nouvelle salle
            imageCharactere.setLayoutX(325);
            imageCharactere.setLayoutY(555);
            }
        } else if (proximiteBas()) {
            if(App.getJeuInstance().getMap().franchirPorteBas(currentSalle,App.getJeuInstance().isBossDeverouille())) {
                //modifier la position du joueur dans la map
                App.getJeuInstance().setPosYJoueurMap(App.getJeuInstance().getPosYJoueurMap()+1);
                //modifie le currentSalleID du joueur
                App.getJeuInstance().setCurrentSalleID(App.getJeuInstance().getMap().getSallePos
                        (App.getJeuInstance().getPosXJoueurMap(),App.getJeuInstance().getPosYJoueurMap()).getIdSalle());
                //donne un interchangement possible de salle
                App.getJeuInstance().setDeplacementPossible(true);
                //modifier la position du joueur dans la nouvelle salle
                imageCharactere.setLayoutX(325);
                imageCharactere.setLayoutY(25);
            }
        } else if (proximiteGauche()) {
            if(App.getJeuInstance().getMap().franchirPorteGauche(currentSalle,App.getJeuInstance().isBossDeverouille())) {
                //modifier la position du joueur dans la map
                App.getJeuInstance().setPosXJoueurMap(App.getJeuInstance().getPosXJoueurMap()-1);
                //modifie le currentSalleID du joueur
                App.getJeuInstance().setCurrentSalleID(App.getJeuInstance().getMap().getSallePos
                        (App.getJeuInstance().getPosXJoueurMap(),App.getJeuInstance().getPosYJoueurMap()).getIdSalle());
                //donne un interchangement possible de salle
                App.getJeuInstance().setDeplacementPossible(true);
                //modifier la position du joueur dans la nouvelle salle
                imageCharactere.setLayoutX(605);
                imageCharactere.setLayoutY(335);
            }
        } else if (proximiteDroite()) {
            if(App.getJeuInstance().getMap().franchirPorteDroite(currentSalle,App.getJeuInstance().isBossDeverouille())) {
                //modifier la position du joueur dans la map
                App.getJeuInstance().setPosXJoueurMap(App.getJeuInstance().getPosXJoueurMap()+1);
                //modifie le currentSalleID du joueur
                App.getJeuInstance().setCurrentSalleID(App.getJeuInstance().getMap().getSallePos
                        (App.getJeuInstance().getPosXJoueurMap(),App.getJeuInstance().getPosYJoueurMap()).getIdSalle());
                //donne un interchangement possible de salle
                App.getJeuInstance().setDeplacementPossible(true);
                //modifier la position du joueur dans la nouvelle salle
                imageCharactere.setLayoutX(45);
                imageCharactere.setLayoutY(335);
            }
        }
        if(currentSalle.isPotion()) {
            imagePotionSalle.setVisible(true);
            }else{
            imagePotionSalle.setVisible(false);
        }
        if(currentSalle.isCle()) {
            imageCleSalle.setVisible(true);
        }else{
            imageCleSalle.setVisible(false);
        }
        
    }

    public void effacePotion(){
        Salle currentSalle = App.getJeuInstance().getMap().getSalle(App.getJeuInstance().getCurrentSalle());
        if(currentSalle.isPotion()) {
            if (proximiteCentre()) {
                currentSalle.setPotion(false);
                App.getJeuInstance().getPersonnage().recupererPotion();
                imagePotionSalle.setVisible(false);
            }
        }
    }

    public void effaceCle(){
        Salle currentSalle = App.getJeuInstance().getMap().getSalle(App.getJeuInstance().getCurrentSalle());
        if(currentSalle.isCle()) {
            if (proximiteCentre()) {
                currentSalle.setCle(false);
                App.getJeuInstance().getPersonnage().setCle(true);
                imagePotionSalle.setVisible(false);
            }
        }
    }



    public void onPushBtnMP(ActionEvent actionEvent) {
        App.setSceneAccueil();
    }
}
