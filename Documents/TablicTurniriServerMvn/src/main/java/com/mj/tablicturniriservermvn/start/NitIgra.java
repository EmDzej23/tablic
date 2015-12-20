/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mj.tablicturniriservermvn.start;

import domain.Entrence;
import domain.Game;
import domain.Player;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Marko
 */
public class NitIgra extends Thread {

    
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Entrence prijava;
    private Game game;
    private Player player;
    private List<NitKlijent> klijenti;

    public NitIgra(NitKlijent klijent,List<NitKlijent> klijenti) {
        this.in = klijent.getIn();
        this.out = klijent.getOut();
        this.klijenti = klijenti;
        this.prijava = klijent.getMojaPrijava();
        this.game = klijent.getMojaPrijava().getGame1();
        this.player = klijent.getMojaPrijava().getPlayer1();
    }

    @Override
    public void run() {
        try {
            Object objekat;
            for (NitKlijent k:klijenti) {
                System.out.println("Klijent kom saljem: "+k.getMojaPrijava().getPlayer1().getUsername());
                k.getOut().writeObject("POCINJE VAM TURNIR DRUGARI!!!");
            }
            while ((objekat = in.readObject()) != null) {
                System.out.println(""+objekat.toString());
            }
        } catch (IOException ex) {
            Logger.getLogger(NitIgra.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(NitIgra.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
