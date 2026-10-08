package Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Map {
    private final int longueur;
    private final int largeur;
    private ArrayList<ArrayList<Salle>> salles;

    /**Crée une map de taille 4x4 avec une salle de boss placé
     * aléatoirement
     */
    public Map() {
        this.longueur = 4;
        this.largeur = 4;
        this.salles = new ArrayList<>();
        for (int i = 0; i < longueur; i++) {
            ArrayList<Salle> row = new ArrayList<>();
            for (int j = 0; j < largeur; j++) {
                row.add(new Salle());
            }
            salles.add(row);
        }
        List<Integer> pos = numeroSalleAleatoire();
        salles.get(3).get(1).setCle(true);
        salles.get(0).get(3).setPotion(true);
        salles.get(1).get(0).setPotion(true);
        salles.get(3).get(3).setPotion(true);
        salles.get(pos.get(0)).get(pos.get(1)).salleBoss();
        //salles.get(0).get(1).salleBoss();
    }

    public List<Integer> numeroSalleAleatoire() {
        Random rand = new Random();
        List<Integer> aleatoire = new ArrayList<>();
        aleatoire.add(rand.nextInt(3)+1);
        aleatoire.add(rand.nextInt(3)+1);
        return aleatoire;
    }


    /**Trouve la position (i,j) d'une salle
     *
     * @param salle
     * @return les positions (i,j) de la salle
     */
    public List<Integer> getPosition(Salle salle) {
        for (int i = 0; i < longueur; i++) {
            for (int j = 0; j < largeur; j++) {
                if (salles.get(i).get(j) == salle) {
                    List<Integer> list = new ArrayList<Integer>();
                    list.add(i);list.add(j);
                    return list;
                }
            }
        }
        return null; // Retourne null si la salle n'est pas trouvée
    }

    //retourne la salle ayant l'id en paramètre
    public Salle getSalle(int id) {
        for (ArrayList<Salle> row : salles) {
            for (Salle salle : row) {
                if (salle.getIdSalle() == id) {
                    return salle;
                }
            }
        }
        return null; // Retourne null si aucune salle avec l'ID donné n'est trouvée
    }

    public Salle getSallePos(int x, int y) {
        if (x >= 0 && x < longueur && y >= 0 && y < largeur) {
            return salles.get(y).get(x);
        }
        return null; // Retourne null si les coordonnées sont en dehors des limites de la carte
    }

    /**Interchange une salle a avec la salle à sa droite
     *
     * @param a
     */
    public void interchangerDroite(Salle a){
        List<Integer> pos = getPosition(a);
        if (pos != null && pos.get(1) < largeur - 1) {
            int i = pos.get(0);
            int j = pos.get(1);
            Salle temp = salles.get(i).get(j + 1);
            salles.get(i).set(j + 1, a);
            salles.get(i).set(j, temp);
        }
    }

    /**Interchange une salle a avec la salle à sa gauche
     *
     * @param a
     */
    public void interchangerGauche(Salle a){
        List<Integer> pos = getPosition(a);
        if (pos != null && pos.get(1) > 0) {
            int i = pos.get(0);
            int j = pos.get(1);
            Salle temp = salles.get(i).get(j - 1);
            salles.get(i).set(j - 1, a);
            salles.get(i).set(j, temp);
        }
    }

    /**Interchange une salle a avec la salle en bas
     *
     * @param a
     */
    public void interchangerBas(Salle a){
        List<Integer> pos = getPosition(a);
        if (pos != null && pos.get(0) < longueur - 1) {
            int i = pos.get(0);
            int j = pos.get(1);
            Salle temp = salles.get(i + 1).get(j);
            salles.get(i + 1).set(j, a);
            salles.get(i).set(j, temp);
        }
    }

    /**Interchange une salle a avec la salle en haut
     *
     * @param a
     */
    public void interchangerHaut(Salle a){
        List<Integer> pos = getPosition(a);
        if (pos != null && pos.get(0) > 0) {
            int i = pos.get(0);
            int j = pos.get(1);
            Salle temp = salles.get(i - 1).get(j);
            salles.get(i - 1).set(j, a);
            salles.get(i).set(j, temp);
        }
    }

    /** Verifie si on peut traverser la porte de droite d'une salle a et renvoie
     * true si c'est possible et false sinon
     * @param a
     * @return un boolean qui vaut true si on peut traverser cette porte
     */
    public boolean franchirPorteDroite(Salle a, boolean bossDeverouille) {
        List<Integer> pos = getPosition(a);
        if (pos != null && pos.get(1) < largeur - 1) {
            int i = pos.get(0);
            int j = pos.get(1);
            Salle b = salles.get(i).get(j + 1);
            if(bossDeverouille) {
                return (a.isPorteRIGHT() && b.isPorteLEFT());
            }else{
                if(b.isSalleBoss()){
                    return(false);
                }else{
                    return (a.isPorteRIGHT() && b.isPorteLEFT());
                }
            }
        } else {
            return (false);
        }
    }

    public boolean isRightBoss (Salle a){
        List<Integer> pos = getPosition(a);
        if (pos != null && pos.get(1) < largeur - 1) {
            int i = pos.get(0);
            int j = pos.get(1);
            Salle b = salles.get(i).get(j + 1);
            return (a.isPorteRIGHT() && b.isPorteLEFT() && b.isSalleBoss());
        }else{
            return(false);
        }
    }

    /** Verifie si on peut traverser la porte de gauche d'une salle a et renvoie
     * true si c'est possible et false sinon
     * @param a
     * @return un boolean qui vaut true si on peut traverser cette porte
     */
    public boolean franchirPorteGauche(Salle a, boolean bossDeverouille){
        List<Integer> pos = getPosition(a);
        if (pos != null && pos.get(1) > 0) {
            int i = pos.get(0);
            int j = pos.get(1);
            Salle b = salles.get(i).get(j - 1);
            if(bossDeverouille) {
                return (a.isPorteLEFT() && b.isPorteRIGHT());
            }else{
                if(b.isSalleBoss()){
                    return(false);
                }else{
                    return(a.isPorteLEFT() && b.isPorteRIGHT());
                }
            }
        }else{
            return(false);
        }
    }

    public boolean isLeftBoss (Salle a){
        List<Integer> pos = getPosition(a);
        if (pos != null && pos.get(1) > 0) {
            int i = pos.get(0);
            int j = pos.get(1);
            Salle b = salles.get(i).get(j - 1);
            return (a.isPorteLEFT() && b.isPorteRIGHT() && b.isSalleBoss());
        }else{
            return(false);
        }
    }

    /** Verifie si on peut traverser la porte d'en bas d'une salle a et renvoie
     * true si c'est possible et false sinon
     * @param a
     * @return un boolean qui vaut true si on peut traverser cette porte
     */
    public boolean franchirPorteBas(Salle a, boolean bossDeverouille){
        List<Integer> pos = getPosition(a);
        if (pos != null && pos.get(0) < longueur - 1) {
            int i = pos.get(0);
            int j = pos.get(1);
            Salle b = salles.get(i + 1).get(j);
            if(bossDeverouille) {
                return (a.isPorteBOTTOM() && b.isPorteTOP());
            }else{
                if(b.isSalleBoss()){
                    return(false);
                }else{
                    return(a.isPorteBOTTOM() && b.isPorteTOP());
                }
            }
        }else{
            return(false);
        }
    }

    public boolean isBottomBoss (Salle a){
        List<Integer> pos = getPosition(a);
        if (pos != null && pos.get(0) < longueur - 1) {
            int i = pos.get(0);
            int j = pos.get(1);
            Salle b = salles.get(i + 1).get(j);
            return (a.isPorteBOTTOM() && b.isPorteTOP() && b.isSalleBoss());
        }else{
            return(false);
        }
    }

    public boolean isPorteBoss(Salle a){
        return(isTopBoss(a)||isBottomBoss(a)||isLeftBoss(a)||isRightBoss(a));
    }

    /** Verifie si on peut traverser la porte d'en haut d'une salle a et renvoie
     * true si c'est possible et false sinon
     * @param a
     * @return un boolean qui vaut true si on peut traverser cette porte
     */
    public boolean franchirPorteHaut(Salle a, boolean bossDeverouille){
        List<Integer> pos = getPosition(a);
        if (pos != null && pos.get(0) > 0) {
            int i = pos.get(0);
            int j = pos.get(1);
            Salle b = salles.get(i - 1).get(j);
            if(bossDeverouille) {
                return (a.isPorteTOP() && b.isPorteBOTTOM());
            }else{
                if(b.isSalleBoss()){
                    return(false);
                }else{
                    return(a.isPorteTOP() && b.isPorteBOTTOM());
                }
            }
        }else{
            return(false);
        }
    }

    public boolean isTopBoss (Salle a){
        List<Integer> pos = getPosition(a);
        if (pos != null && pos.get(0) > 0) {
            int i = pos.get(0);
            int j = pos.get(1);
            Salle b = salles.get(i - 1).get(j);
            return (a.isPorteTOP() && b.isPorteBOTTOM() && b.isSalleBoss());
        }else{
            return(false);
        }
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (ArrayList<Salle> row : salles) {
            for (Salle salle : row) {
                sb.append(salle).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        Map map = new Map();
        System.out.println(map);
        map.interchangerBas(map.salles.get(0).get(0));
        map.interchangerDroite(map.salles.get(1).get(0));
        System.out.println(map);
    }

    }
