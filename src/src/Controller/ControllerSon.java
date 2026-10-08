package Controller;

import Application.App;
import javafx.event.ActionEvent;
import javafx.scene.control.Slider;

public class ControllerSon {

    private App app;

    public ControllerSon(App app){
        this.app =app;
    }

    public ControllerSon(){}

    public void pushBtnMenuP(ActionEvent actionEvent) {
        App.setSceneAccueil();
    }
}
