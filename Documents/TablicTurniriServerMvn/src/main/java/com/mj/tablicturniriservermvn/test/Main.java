/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mj.tablicturniriservermvn.test;

import com.mj.tablicturniriservermvn.kontroler.pl.KontrolerPL;
import com.mj.tablicturniriservermvn.so.AbstractSO;
import com.mj.tablicturniriservermvn.so.operacije.IzmeniSO;
import com.mj.tablicturniriservermvn.so.operacije.UbaciSO;
import com.mj.tablicturniriservermvn.so.operacije.VratiSO;
import domain.Game;
import domain.Move;
import domain.OpstiDomenskiObjekat;
import domain.Player;
import domain.StatisticPlayer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Marko
 */
public class Main {
    
    public static void main(String[] args) throws Exception {
       /* Main main = new Main();
        
        main.vrati();
        main.unesi(0);
        main.unesi(7);
        
        System.out.println("Posle unosa...");
        main.vrati();
        main.izmeni(8, 87, 0, 1);
        main.izmeni(1, 101, 1, 0);
        System.out.println("Posle izmene...");
        main.vrati();*/
        /*Admin admin = KontrolerPL.getInstance().proveriValidnostAdmina("admin", "admin");
        Game gamee = new Game("ppapapa", new Date(), new Date(), "2/2", 2,50.0,100.0,admin);
                    KontrolerPL.getInstance().kreirajTurnir(gamee);*/
        Game game = KontrolerPL.getInstance().vratiTurnire().get(0);
        Player player = KontrolerPL.getInstance().vratiIgrace().get(20);
        
       
        Move move = new Move(new Date(),"N","Talon 1",true,game,player);
        KontrolerPL.getInstance().ubaciPotez(move);
    }
    public List<StatisticPlayer> vrati () {
        try {
            AbstractSO sos = new VratiSO();
            System.out.println("ppp");
            sos.izvrsiOperaciju(new StatisticPlayer());
            List<OpstiDomenskiObjekat> lista = ((VratiSO)sos).vratiListu();
            List<StatisticPlayer> stats = new ArrayList<>();
            System.out.println("size: "+lista.size());
            for (OpstiDomenskiObjekat o : lista) {
                stats.add((StatisticPlayer)o);
            }
            for (StatisticPlayer s : stats) {
                System.out.println(s.getStatisticID()+". "+s);
            }
            
            return stats;
            
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    public void unesi(int broj) {
        try {
            AbstractSO sos = new UbaciSO();
            System.out.println("ppp");
            AbstractSO sos2 = new VratiSO();
            sos2.izvrsiOperaciju(new Player());
            List<OpstiDomenskiObjekat> igraci = ((VratiSO)sos2).vratiListu();
            Player player = (Player)igraci.get(broj);
            StatisticPlayer statisticPlayer = new StatisticPlayer(1, 101, 1, 0, 100.0, player);
            sos.izvrsiOperaciju(statisticPlayer);
            
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void izmeni (int broj,int poeni,int won,int lose) {
        try {
            AbstractSO sos = new IzmeniSO();
            StatisticPlayer statisticPlayer=null;
            List<StatisticPlayer> igraciStat = vrati();
            for (StatisticPlayer sp : igraciStat) {
                if (sp.getPlayer().getPlayerID()==broj) {
                    System.out.println("Ovaj je: "+sp.getPlayer());
                    statisticPlayer = sp;
                    break;
                }
            }
            statisticPlayer.setGames(statisticPlayer.getGames()+1);
            statisticPlayer.setLose(statisticPlayer.getLose()+lose);
            statisticPlayer.setWon(statisticPlayer.getWon()+won);
            statisticPlayer.setTotalPoints(statisticPlayer.getTotalPoints()+poeni);
            double percent = 0;
           
            if (statisticPlayer.getGames()==0) percent = 0;
            else {
                percent = statisticPlayer.getWon()*100/statisticPlayer.getGames();
            }
            statisticPlayer.setPercent(percent);
            sos.izvrsiOperaciju(statisticPlayer);
            
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
