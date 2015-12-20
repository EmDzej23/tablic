/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.sesija;

import java.util.HashMap;
import java.util.Map;
import javax.swing.JLabel;

/**
 *
 * @author student1
 */
public class Sesija {

    private static Sesija sesija;
    private HashMap<String, String> mapaUnosVrednost;
    

    public static Sesija vratiInstancu() {
        if (sesija == null) {
            sesija = new Sesija();
        }
        return sesija;
    }

    private Sesija() {
        mapaUnosVrednost = new HashMap<>();
        mapaUnosVrednost.put("uslov","");
    }

    public HashMap<String, String> getMapaUnosVrednost() {
        return mapaUnosVrednost;
    }

    public void setMapaUnosVrednost(HashMap<String, String> mapaUnosVrednost) {
        this.mapaUnosVrednost = mapaUnosVrednost;
    }
    

}
