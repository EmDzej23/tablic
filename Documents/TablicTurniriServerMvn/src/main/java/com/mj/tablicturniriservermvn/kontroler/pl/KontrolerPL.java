/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mj.tablicturniriservermvn.kontroler.pl;

import com.mj.tablicturniriservermvn.so.AbstractSO;
import com.mj.tablicturniriservermvn.so.operacije.IzmeniSO;
import com.mj.tablicturniriservermvn.so.operacije.ObrisiSO;
import com.mj.tablicturniriservermvn.so.operacije.UbaciSO;
import com.mj.tablicturniriservermvn.so.operacije.VratiSO;
import com.mj.tablicturniriservermvn.start.NitKlijent;
import domain.Admin;
import domain.Card;
import domain.City;
import domain.Country;
import domain.Entrence;
import domain.Game;
import domain.GameTable;
import domain.Move;
import domain.MoveCard;
import domain.OpstiDomenskiObjekat;
import domain.Player;
import domain.StatisticPlayer;
import java.util.ArrayList;
import java.util.List;
import transfer.TablicIgraObjekat;

/**
 *
 * @author student1
 */
public class KontrolerPL {
    
    private static KontrolerPL instance = new KontrolerPL();
    private List<OpstiDomenskiObjekat> igraci;
    private List<GameTable> stolovi;
    private Player onajKojiJeUlogovan;
    private Admin adminN;
    private TablicIgra pk;
    private List<TablicIgra> listaIgara;
    private List<NitKlijent> listaKlijenata;
    private boolean nemaslanja = false;
    
    private KontrolerPL() {
        stolovi = new ArrayList<>();
        listaIgara = new ArrayList<>();
        listaKlijenata = new ArrayList<>();
    }
    
    public void dodajSto(GameTable gt) {
        stolovi.add(gt);
    }
    
    public static KontrolerPL getInstance() {
        return instance;
    }
    
    public boolean isNemaslanja() {
        return nemaslanja;
    }
    
    public void setNemaslanja(boolean nemaslanja) {
        this.nemaslanja = nemaslanja;
    }
    
    public List<GameTable> getStolovi() {
        return stolovi;
    }
    
    public void setStolovi(List<GameTable> stolovi) {
        this.stolovi = stolovi;
    }
    
    public List<Country> vratiDrzave() throws Exception {
        AbstractSO sos = new VratiSO();
        sos.izvrsiOperaciju(new Country());
        List<OpstiDomenskiObjekat> lista = ((VratiSO) sos).vratiListu();
        List<Country> drzave = new ArrayList<>();
        for (OpstiDomenskiObjekat c : lista) {
            drzave.add((Country) c);
        }
        return drzave;
    }
    
    public List<City> vratiGradove() throws Exception {
        AbstractSO sos = new VratiSO();
        sos.izvrsiOperaciju(new City());
        List<OpstiDomenskiObjekat> lista = ((VratiSO) sos).vratiListu();
        List<City> gradovi = new ArrayList<>();
        for (OpstiDomenskiObjekat c : lista) {
            gradovi.add((City) c);
        }
        return gradovi;
    }
    
    public List<Player> vratiIgrace() throws Exception {
        AbstractSO sos = new VratiSO();
        sos.izvrsiOperaciju(new Player());
        List<OpstiDomenskiObjekat> lista = ((VratiSO) sos).vratiListu();
        List<Player> players = new ArrayList<>();
        for (OpstiDomenskiObjekat c : lista) {
            players.add((Player) c);
        }
        return players;
    }

    public List<StatisticPlayer> vratiStatIgraca() throws Exception {
        AbstractSO sos = new VratiSO();
        sos.izvrsiOperaciju(new StatisticPlayer());
        List<OpstiDomenskiObjekat> lista = ((VratiSO) sos).vratiListu();
        List<StatisticPlayer> players = new ArrayList<>();
        for (OpstiDomenskiObjekat c : lista) {
            players.add((StatisticPlayer) c);
        }
        return players;
    }

    public List<Game> vratiTurnire() throws Exception {
        AbstractSO sos = new VratiSO();
        sos.izvrsiOperaciju(new Game());
        List<OpstiDomenskiObjekat> lista = ((VratiSO) sos).vratiListu();
        List<Game> players = new ArrayList<>();
        for (OpstiDomenskiObjekat c : lista) {
            players.add((Game) c);
        }
        return players;
    }
    
    public List<Entrence> vratiPrijave() throws Exception {
        AbstractSO sos = new VratiSO();
        sos.izvrsiOperaciju(new Entrence());
        List<OpstiDomenskiObjekat> lista = ((VratiSO) sos).vratiListu();
        List<Entrence> players = new ArrayList<>();
        for (OpstiDomenskiObjekat c : lista) {
            players.add((Entrence) c);
        }
        return players;
    }
    
    public void kreirajNoviGrad(City city) throws Exception {
        AbstractSO sos = new VratiSO();
        sos.izvrsiOperaciju(new City());
        List<OpstiDomenskiObjekat> lista = ((VratiSO) sos).vratiListu();
        
        for (OpstiDomenskiObjekat c : lista) {
            if (((City) c).equals(city)) {
                return;
            }
        }
        AbstractSO sos2 = new UbaciSO();
        sos2.izvrsiOperaciju(city);
        
    }
    
    public void kreirajNovogIgraca(Player player) throws Exception {
        AbstractSO sos = new UbaciSO();
        List<Player> igraci = vratiIgrace();
        if (player.getUsername().contains(":")) {
            throw new Exception("Molimo Vas, nemojte da koristite ':' u kor. imenu! ");
        }
        for (Player p : igraci) {
            
            if (p.getUsername().equals(player.getUsername())) {
                throw new Exception("Isti username vec postoji u bazi");
            }
        }
        sos.izvrsiOperaciju(player);
    }
    
    public void kreirajNovog(OpstiDomenskiObjekat player) throws Exception {
        AbstractSO sos = new UbaciSO();
        sos.izvrsiOperaciju(player);
    }
    
    public void kreirajNovuPrijavu(NitKlijent klijent, OpstiDomenskiObjekat entrence) throws Exception {
        System.out.println("tu sam...");
        Entrence prijava = (Entrence) entrence;
        int ukupanBrojIgraca = prijava.getGame1().getPlayersCount();
        TablicIgra novaIgra = null;
        if (prebrojPrijaveZaOvajTurnir(prijava.getGame1().getGameID(), 0) == 0) {
            System.out.println("Nula prijava");
            novaIgra = new TablicIgra(prijava.getGame1().getPlayersCount(), prijava.getGame1());
            listaIgara.add(novaIgra);
        } else {
            for (TablicIgra ti : listaIgara) {
                if (ti.getIgra().getName().equals(prijava.getGame1().getName())) {
                    novaIgra = ti;
                    break;
                }
            }
        }
        TablicIgraObjekat tai = new TablicIgraObjekat();
        tai.setPoruka("srediformu");
        tai.setPodaci(entrence);
        for (NitKlijent nk : listaKlijenata) {
            System.out.println("nk " + nk.getMojaPrijava().getPlayer1().getUsername());
           // nk.getOut().writeObject(tai);
        }
        AbstractSO sos = new UbaciSO();
        sos.izvrsiOperaciju(entrence);
        novaIgra.getUcesnici().add(klijent);
        System.out.println("Dodao sam klijenta: " + klijent.getMojaPrijava().getPlayer1().getUsername());
        int prijavaljenBrojIgraca = novaIgra.getUcesnici().size();
        Game game = prijava.getGame1();
        game.setState(prijavaljenBrojIgraca + "/" + ukupanBrojIgraca);
        AbstractSO sos2 = new IzmeniSO();
        sos2.izvrsiOperaciju(game);
        System.out.println("Trenutan broj ucesnika na turniru " + prijava.getGame1().getName() + " je "
                + prijavaljenBrojIgraca);
        if (ukupanBrojIgraca == prijavaljenBrojIgraca) {
            game.setState("zavrsena");
            AbstractSO sos3 = new IzmeniSO();
            sos3.izvrsiOperaciju(game);
            novaIgra.setBrojIgraca(ukupanBrojIgraca);
            novaIgra.podeliKarte();
            System.out.println("IGRA POCINJE!");
            for (int i = 0; i < ukupanBrojIgraca; i++) {
                List<Card> karte = new ArrayList<>();
                int limit = 4 + i * novaIgra.getBrojKarataPoIgracu();
                for (int i1 = limit; i1 < novaIgra.getBrojKarataPoIgracu() + limit; i1++) {
                    karte.add(novaIgra.getSveKarte().get(i1));
                }
                novaIgra.getUcesnici().get(i).setMojeKarte(karte);
                TablicIgraObjekat tia = new TablicIgraObjekat();
                tia.setOperacija(i);
                tia.setPodaci(karte);
                tia.setTalon(novaIgra.getTalon());
                tia.setSveKarte(novaIgra.getSveKarte());
                tia.setPoruka("Ovo su tvoje karte!");
                novaIgra.getUcesnici().get(i).getOut().writeObject(novaIgra.getUcesnici().get(i).getMojaPrijava().
                        getPlayer1());
                novaIgra.getUcesnici().get(i).getOut().writeObject(tia);
                novaIgra.getUcesnici().get(i).setNovaIgra(novaIgra);
                System.out.println("Otvaram nit za username: " + novaIgra.getUcesnici().get(i).getMojaPrijava().
                        getPlayer1().getUsername());
            }
            for (TablicIgra ti : listaIgara) {
                if (ti.getIgra().getGameID().equals(prijava.getGame1().getGameID())) {
                    new ObrisiSO().izvrsiOperaciju(ti.getIgra());
                    break;
                }
            }
            List<Object> l = new ArrayList<>();
            /*l.add(prijava);
             l.add("game");
             AbstractSO obrisi1 = new ObrisiSO();
             obrisi1.izvrsiOperaciju(l);
             l.clear();*/
            l.add(prijava.getGame1());
            l.add("GameID");
            AbstractSO obrisi2 = new ObrisiSO();
            obrisi2.izvrsiOperaciju(l);
            listaIgara.remove(novaIgra);
        }
    }
    
    public void dodajStatistikuIgraca(StatisticPlayer sp) throws Exception {
        AbstractSO sos = new UbaciSO();
        
        List<StatisticPlayer> players = vratiStatIgraca();
        boolean imaga = false;
        for (StatisticPlayer pl : players) {
            if (pl.getPlayer().getPlayerID().equals(sp.getPlayer().getPlayerID())) {
                imaga = true;
                break;
            }
        }
        if (!imaga) {
            sos.izvrsiOperaciju(sp);
        }
        if (imaga) {
            StatisticPlayer statp = null;
            List<StatisticPlayer> stats = vratiStatIgraca();
            for (StatisticPlayer st : stats) {
                if (st.getPlayer().getPlayerID().equals(sp.getPlayer().getPlayerID())) {
                    statp = st;
                    break;
                }
            }
            int games = statp.getGames();
            int totalPoints = statp.getTotalPoints();
            int won = statp.getWon();
            int lose = statp.getLose();
            double percent = 0;
            
            games++;
            totalPoints += sp.getTotalPoints();
            won += sp.getWon();
            lose += sp.getLose();
            statp.setGames(games);
            statp.setTotalPoints(totalPoints);
            statp.setWon(won);
            statp.setLose(lose);
            if (games == 0) {
                percent = 0;
            } else {
                percent = 100 * won / games;
            }
            statp.setPercent(percent);
            AbstractSO sos2 = new IzmeniSO();
            sos2.izvrsiOperaciju(statp);
        }
    }
    
    public void podelaKarata() {
        List<Card> karte = new ArrayList<>();
        int limit = 4 + pk.getIgraciKojiSuDobiliKarte() * pk.getBrojKarataPoIgracu();
        if (pk.getBrojIgraca() == 2) {
            for (int i = limit; i < pk.getBrojKarataPoIgracu() + limit; i++) {
                karte.add(pk.getSveKarte().get(i));
            }
        }
        int igraciSKartama = pk.getIgraciKojiSuDobiliKarte();
        igraciSKartama++;
        pk.setIgraciKojiSuDobiliKarte(igraciSKartama);
        
    }
    
    public void zapamtiIgraca(Player player) throws Exception {
        List<Player> igraci = vratiIgrace();
        for (Player p : igraci) {
            if (p.getUsername().equals(player.getUsername())) 
                throw new Exception("Takav username postoji u bazi");
        }
        AbstractSO sos = new IzmeniSO();
        sos.izvrsiOperaciju(player);
    }
    public void zapamtiIgraca2(Player player) throws Exception {
        AbstractSO sos = new IzmeniSO();
        sos.izvrsiOperaciju(player);
    }
    
    public List<Player> pretraziIgrace(String player) throws Exception {
        AbstractSO sos = new VratiSO();
        sos.izvrsiOperaciju(new Player());
        Player p = null;
        List<OpstiDomenskiObjekat> lista = ((VratiSO) sos).vratiListu();
        List<Player> igraci = new ArrayList<>();
        for (OpstiDomenskiObjekat c : lista) {
            if (((Player) c).getFirstName().contains(player) || ((Player) c).getLastName().contains(player)
                    || ((Player) c).getFirstName().contains(player) || ((Player) c).getFirstName().contains(player)
                    || ((Player) c).getFirstName().contains(player) || ((Player) c).getFirstName().contains(player)) {
                igraci.add((Player) c);
            }
        }
        System.out.println("igraci " + igraci.size());
        return igraci;
    }
    
    public List<Entrence> pretraziTurnire(String turnir) throws Exception {
        AbstractSO sos = new VratiSO();
        sos.izvrsiOperaciju(new Entrence());
        Player p = null;
        List<OpstiDomenskiObjekat> lista = ((VratiSO) sos).vratiListu();
        List<Entrence> turniri = new ArrayList<>();
        for (OpstiDomenskiObjekat c : lista) {
            if (((Entrence) c).getGameType().equals("tournament") && ((Entrence) c).getGame1().getState().equals("pending")) {
                turniri.add((Entrence) c);
            }
        }
        return turniri;
    }
    
    public void nemaslanja() {
        setNemaslanja(true);
    }
    
    public List<Card> ucitajKarte() throws Exception {
        AbstractSO sos = new VratiSO();
        sos.izvrsiOperaciju(new Card());
        List<OpstiDomenskiObjekat> lista = ((VratiSO) sos).vratiListu();
        List<Card> karte = new ArrayList<>();
        for (OpstiDomenskiObjekat c : lista) {
            karte.add((Card) c);
        }
        return karte;
    }
    
    public Player pronadjiUlogovanogIgraca() {
        return onajKojiJeUlogovan;
    }
    
    public List<Card> dajTalonIgracu() {
        List<Card> talon = pk.getTalon();
        return talon;
    }
    
    public List<Card> dajKarteIgracu() {
        List<Card> karte = new ArrayList<>();
        int limit = 4 + pk.getIgraciKojiSuDobiliKarte() * pk.getBrojKarataPoIgracu();
        if (pk.getBrojIgraca() == 2) {
            for (int i = limit; i < pk.getBrojKarataPoIgracu() + limit; i++) {
                karte.add(pk.getSveKarte().get(i));
            }
        }
        int igraciSKartama = pk.getIgraciKojiSuDobiliKarte();
        igraciSKartama++;
        pk.setIgraciKojiSuDobiliKarte(igraciSKartama);
        return karte;
    }
    
    public void pokreniIgru(GameTable sto) {
        // pk = new TablicIgra(sto.getGame().getPlayersCount());
    }
    
    public Player obradiLogIn(String username, String password) throws Exception {
        AbstractSO sos = new VratiSO();
        sos.izvrsiOperaciju(new Player());
        List<OpstiDomenskiObjekat> lista = ((VratiSO) sos).vratiListu();
        for (OpstiDomenskiObjekat c : lista) {
            if (((Player) c).getUsername().equals(username) && ((Player) c).getPassword().equals(password)) {
                if (((Player)c).getUlogovan().equals("da")) {
                    throw new Exception("Ulogovan je ovaj igrac vec!");
                }
                onajKojiJeUlogovan = (Player) c;
                return (Player) c;
            }
        }
        throw new Exception("Nema takvog igraca u bazi!");
    }
    
    public void kreirajTurnir(Game game) throws Exception {
        AbstractSO sos = new UbaciSO();
        
        List<Game> turniri = vratiTurnire();
        for (Game g : turniri) {
            if (g.getName().equals(game.getName())) {
                throw new Exception("Polako, turnir sa tim nazivom postoji vec!");
            }
        }
        
        sos.izvrsiOperaciju(game);
    }
    
    public void zapamtiTurnir(Game game) throws Exception {
        AbstractSO sos = new IzmeniSO();
        sos.izvrsiOperaciju(game);
    }
    
    public void obrisiTurnir(Game game) throws Exception {
        AbstractSO sos = new ObrisiSO();
        sos.izvrsiOperaciju(game);
    }
    
    public void ubaciSto(GameTable sto) throws Exception {
        AbstractSO sos = new UbaciSO();
        List<GameTable> stolovi = getStolovi();
        int br = 0;
        for (GameTable oto : stolovi) {
            //br = 0;
            if (sto.getTableNumber() == oto.getTableNumber()) {
                br++;
            }
        }
        System.out.println("brrrr " + br);
        if (br > 1) {
            throw new Exception("Pazi!!! Redni broj " + sto.getTableNumber() + " postoji na turniru!");
        }
        sos.izvrsiOperaciju(sto);
    }
    
    public Admin proveriValidnostAdmina(String username, String password) throws Exception {
        AbstractSO sos = new VratiSO();
        sos.izvrsiOperaciju(new Admin());
        List<OpstiDomenskiObjekat> lista = ((VratiSO) sos).vratiListu();
        for (OpstiDomenskiObjekat c : lista) {
            if (((Admin) c).getUsername().equals(username) && ((Admin) c).getPassword().equals(password)) {
                adminN = (Admin) c;
                return adminN;
            }
        }
        throw new Exception("Nisi admin!");
    }
    
    public void zapamtiPotez(MoveCard move) {
        
    }
    
    public void setOnajKojiJeUlogovan(Player onajKojiJeUlogovan) {
        this.onajKojiJeUlogovan = onajKojiJeUlogovan;
    }
    
    public List<OpstiDomenskiObjekat> getIgraci() {
        return igraci;
    }
    
    public void setIgraci(List<OpstiDomenskiObjekat> igraci) {
        this.igraci = igraci;
    }
    
    public Admin getAdminN() {
        return adminN;
    }
    
    public void setAdminN(Admin adminN) {
        this.adminN = adminN;
    }
    
    public TablicIgra getPk() {
        return pk;
    }
    
    public void setPk(TablicIgra pk) {
        this.pk = pk;
    }
    
    public int prebrojPrijaveZaOvajTurnir(Long gameID, int nova) throws Exception {
        System.out.println("gameID: "+gameID);
        List<Entrence> prijave = vratiPrijave();
        int brojPrijava = nova;
        int ukupanBroj = 0;
        Game game = null;
        Game gameUpadate = null;
        for (Entrence prijava : prijave) {
            System.out.println("PrijavaID: "+prijava.getGame1().getGameID());
            if (prijava.getGame1().getGameID().equals(gameID)) {
                if (game == null) {
                    game = prijava.getGame1();
                    gameUpadate = new Game(game.getName(), game.getStartTime(), game.getEndTime(),
                            game.getState(), game.getPlayersCount(), game.getBuyin(), game.getWinning(), game.getAdmin());
                    
                }
                brojPrijava++;
            }
        }
        
        System.out.println("brojP: "+brojPrijava);
        if (game != null) {
            ukupanBroj = game.getPlayersCount();
            gameUpadate.setState(brojPrijava + "/" + ukupanBroj);
            zapamtiTurnir(gameUpadate);
        } else {
            
            List<Game> turniri = vratiTurnire();
            for (Game g : turniri) {
                if (g.getGameID().equals(gameID)) {
                    g.setState(1+"/"+g.getPlayersCount());
                    zapamtiTurnir(g);
                }
            }
        }
        System.out.println("Stanje: " + brojPrijava + "/" + ukupanBroj);
        return brojPrijava;
    }
    
    public void dodajKlijenta(NitKlijent nk) {
        listaKlijenata.add(nk);
    }
    
    public List<NitKlijent> getListaKlijenata() {
        return listaKlijenata;
    }
    
    public void setListaKlijenata(List<NitKlijent> listaKlijenata) {
        this.listaKlijenata = listaKlijenata;
    }

    public Long ubaciPotez(Move move) throws Exception {
        AbstractSO sos = new UbaciSO();
        sos.izvrsiOperaciju(move);
        AbstractSO sos1 = new VratiSO();
        sos1.izvrsiOperaciju(new Move());
        List<OpstiDomenskiObjekat> lista = ((VratiSO) sos1).vratiListu();
        Move mowe = null;
        for (OpstiDomenskiObjekat c : lista) {
            if (((Move)c).getDescription().equals(move.getDescription())) {
                mowe = (Move)c;
                break;
            }
        }
        
        System.out.println("size: "+(lista.size()-1));
        return mowe.getMoveID();
    }
    public void ubaciPotezKartu(MoveCard moveCard) throws Exception {
        AbstractSO sos = new UbaciSO();
        sos.izvrsiOperaciju(moveCard);
        
    }
    
}
