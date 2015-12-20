/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mj.tablicturniriservermvn.kontroler.pl;

import com.mj.tablicturniriservermvn.start.NitKlijent;
import domain.Card;
import domain.Game;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Marko
 */
public class TablicIgra {
    private int brojKarataPoIgracu = 0;
    private int igraciKojiSuDobiliKarte;
    private List<Card> talon;
    private List<Card> sveKarte;
    private List<NitKlijent> ucesnici;
    private int brojIgraca;
    private Game igra;

    public TablicIgra(int brojIgraca,Game igra) {
        this.brojIgraca = brojIgraca;
        this.igra = igra;
        ucesnici = new ArrayList<>();
        igraciKojiSuDobiliKarte = 0;
        
    }

    public void podeliKarte() throws Exception {
        sveKarte = KontrolerPL.getInstance().ucitajKarte();
        talon = new ArrayList<>();
        Collections.shuffle(sveKarte);
        for (int i = 0; i < 4; i++) {
            talon.add(sveKarte.get(i));
        }
        setTalon(talon);
        if (brojIgraca == 2) {
            brojKarataPoIgracu = 24;
        }
        if (brojIgraca == 3) {
            brojKarataPoIgracu = 16;
        }
        if (brojIgraca == 4) {
            brojKarataPoIgracu = 12;
        }

    }

    public int getIgraciKojiSuDobiliKarte() {
        return igraciKojiSuDobiliKarte;
    }

    public void setIgraciKojiSuDobiliKarte(int igraciKojiSuDobiliKarte) {
        this.igraciKojiSuDobiliKarte = igraciKojiSuDobiliKarte;
    }
    
    public List<Card> getTalon() {
        return talon;
    }

    public void setTalon(List<Card> talon) {
        this.talon = talon;
    }

    public int getBrojKarataPoIgracu() {
        return brojKarataPoIgracu;
    }

    public void setBrojKarataPoIgracu(int brojKarataPoIgracu) {
        this.brojKarataPoIgracu = brojKarataPoIgracu;
    }
    
    public List<Card> getSveKarte() {
        return sveKarte;
    }

    public void setSveKarte(List<Card> sveKarte) {
        this.sveKarte = sveKarte;
    }

    public List<NitKlijent> getUcesnici() {
        return ucesnici;
    }

    public void setUcesnici(List<NitKlijent> ucesnici) {
        this.ucesnici = ucesnici;
    }

    public Game getIgra() {
        return igra;
    }

    public void setIgra(Game igra) {
        this.igra = igra;
    }
    
    public int getBrojIgraca() {
        return brojIgraca;
    }

    public void setBrojIgraca(int brojIgraca) {
        this.brojIgraca = brojIgraca;
    }
    

}
