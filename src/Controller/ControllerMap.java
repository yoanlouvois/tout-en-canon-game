package Controller;

import Application.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import Model.Salle;

import javafx.scene.image.ImageView;

import java.util.Objects;

public class ControllerMap {

    private App app;

    public Label labelJoueurJeu;
    public Label labelClasseJeu;
    public Label labelAttaqueJeu;
    public Label labelVitesseJeu;

    public ImageView imageVie;
    public ImageView imageCle;
    public ImageView imageInventaire;

    @FXML
    GridPane gridPane;

    public ControllerMap(App app){
        this.app =app;
    }

    public ControllerMap(){}

    @FXML
    public void initialize() {

        //Image de la clé
        if(App.getJeuInstance().getPersonnage().isCle()){
            Image pied_de_biche = new Image("/ressource/pied_de_biche.png");imageCle.setImage(pied_de_biche);}
        else{Image pied_de_biche = new Image("/ressource/non_cle.png");imageCle.setImage(pied_de_biche);}

        //Image de la vie selon les points de vie du personnage
        imageVie.setImage(image_de_la_vie());

        //Image des potions
        imageInventaire.setImage(imagePotion());

        labelJoueurJeu.setText(App.getJeuInstance().getPseudoUtilisateur());
        labelClasseJeu.setText(App.getJeuInstance().getPersonnage().getNomClasse());
        labelAttaqueJeu.setText("Attaque : " + App.getJeuInstance().getPersonnage().getAttaque());
        labelVitesseJeu.setText("Vitesse : " + App.getJeuInstance().getPersonnage().getVitesse());

        //initialise le gridPane pour cacher les salles
        mapViewInit(gridPane);
        //rend visible la salle correspondant à la position du joueur
        getNodeByRowColumnIndex(App.getJeuInstance().getPosXJoueurMap(),App.getJeuInstance()
                .getPosYJoueurMap(),gridPane).setVisible(true);


        gridPane.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.setOnKeyPressed(this::handleKeyPress);
                newScene.setOnKeyReleased(this::handleKeyRelease);
                //updateMapView();
            }
        });

    }


    private void handleKeyRelease(KeyEvent event) {
        KeyCode keyCode = event.getCode();
        if (keyCode == KeyCode.R) {
            updateMapView();
        }
    }

    private void handleKeyPress(KeyEvent event) {
        KeyCode keyCode = event.getCode();
        if (keyCode == KeyCode.Z || keyCode == KeyCode.Q || keyCode == KeyCode.S || keyCode == KeyCode.D) {
            //if(App.getJeuInstance().isDeplacementPossible()) {
                moveSalle(keyCode);
                updateMapView();
                //rend impossible le deplacement
                //App.getJeuInstance().setDeplacementPossible(false);
            //}
        } else if (keyCode == KeyCode.R) {
            App.setSceneJeu();
            updateMapView();
        }
    }

    //renvoie l'imageView d'indice row et column d'un gridpane
    public Node getNodeByRowColumnIndex(final int column, final int row, GridPane gridPane) {
        Node result = null;
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node) == row &&
                    GridPane.getColumnIndex(node) != null && GridPane.getColumnIndex(node) == column) {
                result = node;
                break;
            }
        }
        return result;
    }

    //initialise le gridpane avec toutes les imageView non-visible
    public void mapViewInit(GridPane gridPane){
        for (Node node : gridPane.getChildren()){
            if(GridPane.getRowIndex(node) != null && GridPane.getColumnIndex(node) != null){
                if(App.getJeuInstance().getMap().getSallePos(GridPane.getColumnIndex(node),
                        GridPane.getRowIndex(node)).isVisited()) {
                    node.setVisible(true);
                    node.setOpacity(0.6);
                }else{
                    node.setVisible(false);
                }
            }
        }
    }


    public void onPushBtnMP(ActionEvent actionEvent) {
        App.setSceneJeu();
    }



    private void moveSalle(KeyCode keyCode) {
        //currentSalle est la salle ou se trouve le joueur
        Salle currentSalle = App.getJeuInstance().getMap().getSalle(App.getJeuInstance().getCurrentSalle());
        if (keyCode == KeyCode.Z) {
            // Interchange la salle
            App.getJeuInstance().getMap().interchangerHaut(currentSalle);
        } else if (keyCode == KeyCode.Q) {
            // Interchange la salle
            App.getJeuInstance().getMap().interchangerGauche(currentSalle);
        } else if (keyCode == KeyCode.S) {
            // Interchange la salle
            App.getJeuInstance().getMap().interchangerBas(currentSalle);
        } else if (keyCode == KeyCode.D) {
            // Interchange la salle
            App.getJeuInstance().getMap().interchangerDroite(currentSalle);
        }
        int newPosx = App.getJeuInstance().getMap().getPosition(currentSalle).get(1);
        int newPosy = App.getJeuInstance().getMap().getPosition(currentSalle).get(0);
        App.getJeuInstance().setPosXJoueurMap(newPosx);
        App.getJeuInstance().setPosYJoueurMap(newPosy);
        System.out.println("le joueur est mtn à la position "+App.getJeuInstance().getPosXJoueurMap() + " ;" +
                App.getJeuInstance().getPosYJoueurMap() +"\n");
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

    public void updateMapView() {
        mapViewInit(gridPane);
        //rend visible la salle correspondant à la position du joueur
        getNodeByRowColumnIndex(App.getJeuInstance().getPosXJoueurMap(),
                App.getJeuInstance().getPosYJoueurMap(), gridPane).setVisible(true);
        getNodeByRowColumnIndex(App.getJeuInstance().getPosXJoueurMap(),
                App.getJeuInstance().getPosYJoueurMap(), gridPane).setOpacity(1);

    }

    @FXML
    void OnMouseClicked(MouseEvent event) {

    }
}