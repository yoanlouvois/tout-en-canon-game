package Controller;

import Application.App;
import Model.Jeu;
import Model.Personnage;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;

public class ControllerNouvellePartie{

    public TextField pseudoTextField;
    public Spinner<String> charactereSpinner;
    public Label labelVitesse;
    public Label labelAttaque;
    public Label labelVie;
    public ImageView imageCharacter;



    private App app;
    private Personnage joueur;

    public ControllerNouvellePartie(App app,Personnage joueur){
        this.app =app;
        this.joueur = joueur;
    }

    public ControllerNouvellePartie(){}

    public ControllerNouvellePartie(App app) {
        this.app =app;
    }

    @FXML
    public void initialize() {
        // Configure le Spinner
        SpinnerValueFactory<String> valueFactory = new SpinnerValueFactory.ListSpinnerValueFactory<>(
                FXCollections.observableArrayList("Momie", "Egyptien")
        );
        valueFactory.setValue("Momie");
        charactereSpinner.setValueFactory(valueFactory);




        // Ajouter un listener pour mettre à jour les labels et l'image en fonction du choix
        charactereSpinner.valueProperty().addListener((obs, oldValue, newValue) -> updateLabels(newValue));

        // Initialiser les labels avec la valeur par défaut
        updateLabels("Momie");
    }

    private void updateLabels(String characterType) {
        Personnage selectedPersonnage;
        if ("Egyptien".equals(characterType)) {
            selectedPersonnage = App.egyptien;
            Image egyptien = new Image("/egyptien_nouvell_partie.png");
            imageCharacter.setImage(egyptien);
        } else {
            selectedPersonnage = App.momie;
            Image momie = new Image("/momie_nouvelle_partie.png");
            imageCharacter.setImage(momie);
        }
        labelAttaque.setText("Attaque : " + selectedPersonnage.getAttaque());
        labelVie.setText("Vie : " + selectedPersonnage.getNbDeVie());
        labelVitesse.setText("Vitesse : " + selectedPersonnage.getVitesse());
    }

    public void onPushBtnJouer(ActionEvent actionEvent) throws IOException {
       if(pseudoTextField.getText() != ""){
           Personnage selectedPersonnage;
           if ("Egyptien".equals(charactereSpinner.getValue())) {
               selectedPersonnage = App.egyptien;
           } else {
               selectedPersonnage = App.momie;
           }
           Jeu jeu = new Jeu(selectedPersonnage,pseudoTextField.getText());
           App.setJeuInstance(jeu);
           creationSceneJeu();
           creationSceneMap();
           App.setSceneJeu();

       }
       else{System.out.println("Pas de pseudo");}
    }

    public void onPushBtnMP(ActionEvent actionEvent) {
        App.setSceneAccueil();
    }

    public void creationSceneJeu() throws IOException {
        //On prépare le fichier FXML de la page d'accueil
        FXMLLoader loaderAccueil = new FXMLLoader(getClass().getResource("../View/EcranJeu.fxml"));
        //On charge le fichier FXML
        Parent rootAccueil = loaderAccueil.load();
        //On crée la scène
        App.modifierSceneJeu(new Scene(rootAccueil));
    }


    public void creationSceneMap() throws IOException {
        //On prépare le fichier FXML de la page d'accueil
        FXMLLoader loaderAccueil = new FXMLLoader(getClass().getResource("../View/EcranMap.fxml"));
        //On charge le fichier FXML
        Parent rootAccueil = loaderAccueil.load();
        //On crée la scène
        App.modifierSceneMap(new Scene(rootAccueil));
    }
}
