package Model;

public class Jeu {

    private int time;
    private final String pseudoUtilisateur;
    private final Personnage joueur;
    private final Map map;
    private int currentSalleID = 0;
    private int posXJoueurMap;
    private int posYJoueurMap;
    private boolean pause = true;
    private boolean deplacementPossible;

    private boolean bossDeverouille;

    public void pause(){
        pause = true;
    }

    public Jeu(Personnage selectedCharatere,String pseudoUtilisateur){
        this.time = 0;
        this.pseudoUtilisateur=pseudoUtilisateur;
        this.joueur=selectedCharatere;
        this.map = new Map();
        this.currentSalleID = 0;
        this.pause = false;
        this.posYJoueurMap = 0;
        this.posXJoueurMap = 0;
        this.deplacementPossible = true;
        this.bossDeverouille = false;
    }

    public boolean isDeplacementPossible() {
        return deplacementPossible;
    }

    public void setDeplacementPossible(boolean deplacementPossible) {
        this.deplacementPossible = deplacementPossible;
    }

    public int getPosXJoueurMap() {
        return posXJoueurMap;
    }

    public int getPosYJoueurMap() {
        return posYJoueurMap;
    }

    public int getCurrentSalle() {
        return currentSalleID;
    }

    public void setPosXJoueurMap(int posXJoueurMap) {
        this.posXJoueurMap = posXJoueurMap;
    }

    public void setPosYJoueurMap(int posYJoueurMap) {
        this.posYJoueurMap = posYJoueurMap;
    }

    public Map getMap() {
        return map;
    }

    public Personnage getPersonnage(){
        return joueur;
    }

    public String getPseudoUtilisateur() {
        return pseudoUtilisateur;
    }

    public boolean isBossDeverouille() {return bossDeverouille;}

    public void setBossDeverouille(boolean bossDeverouille) {this.bossDeverouille = bossDeverouille;}

    public void setCurrentSalleID(int currentSalleID) {this.currentSalleID = currentSalleID;}

    public static void findujeu(){
        System.out.println("Game Over");
    }

}
