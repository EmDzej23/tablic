/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mj.tablicturniriservermvn.start;

import com.mj.tablicturniriservermvn.kontroler.pl.KontrolerPL;
import com.mj.tablicturniriservermvn.kontroler.pl.TablicIgra;
import com.mj.tablicturniriservermvn.so.AbstractSO;
import com.mj.tablicturniriservermvn.so.operacije.UbaciSO;
import domain.Admin;
import domain.Card;
import domain.City;
import domain.Country;
import domain.Entrence;
import domain.EntrencePK;
import domain.Game;
import domain.GameTable;
import domain.Move;
import domain.MoveCard;
import domain.MoveCardPK;
import domain.OpstiDomenskiObjekat;
import domain.Player;
import domain.Poruka;
import domain.StatisticPlayer;
import com.mj.tablicturniriservermvn.forma.ServerGUI;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Timer;
import konstante.Konstante;
import transfer.KlijentTransferObjekat;
import transfer.ServerTransferObjekat;
import transfer.TablicIgraObjekat;
import transfer.TablicPotezObjekat;

/**
 *
 * @author Marko
 */
public class NitKlijent extends Thread {

    private Socket socket;
    private ServerGUI serverGUI;
    private boolean aktivan;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private List<Card> mojeKarte;
    private Entrence mojaPrijava;
    private String imeIPrezime;
    private Long kredit;
    private TablicIgra novaIgra;
    private Timer timer;

    public NitKlijent(Socket socket, ServerGUI serverGUI) {
        this.socket = socket;
        this.serverGUI = serverGUI;
        this.aktivan = true;
        this.novaIgra = null;
    }

    @Override
    public void run() {
        try {

            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
            KontrolerPL.getInstance().dodajKlijenta(this);
            while (!isInterrupted()) {
                try {
                    komunicirajSaKlijentom();
                } catch (Exception ex) {
                    interrupt();

                }
            }

            serverGUI.obrisiKlijentaSaListe();
            KontrolerPL.getInstance().getListaKlijenata().remove(this);
            
            List<Player> igraci = KontrolerPL.getInstance().vratiIgrace();
            for (Player pl : igraci) {
                System.out.println(pl.getUsername() + "==" + getImeIPrezime());
                if (pl.getUsername().equals(getImeIPrezime())) {
                    pl.setUlogovan("ne");
                    KontrolerPL.getInstance().zapamtiIgraca2(pl);
                    break;
                }
            }
            List<Player> igraci2 = KontrolerPL.getInstance().vratiIgrace();
            ServerTransferObjekat sto = new ServerTransferObjekat();
            sto.setUspesnostIzvrsenjaOperacije(3);
            sto.setPodaci(igraci2);
            for (NitKlijent nk : KontrolerPL.getInstance().getListaKlijenata()) {
                serverGUI.ispisiNovogKlijenta(nk.getImeIPrezime());
            }
            for (NitKlijent nk : KontrolerPL.getInstance().getListaKlijenata()) {
                nk.getOut().writeObject(sto);
            }
            System.out.println("Izasao je klijent " + getImeIPrezime());
        } catch (IOException ex) {
            Logger.getLogger(NitKlijent.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(NitKlijent.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void komunicirajSaKlijentom() throws IOException, ClassNotFoundException {

        if (!aktivan) {
            return;
        }
        KlijentTransferObjekat kto = (KlijentTransferObjekat) in.readObject();
        ServerTransferObjekat sto = new ServerTransferObjekat();
        sto.setPodaci(new Card());
        try {

            //OBRADI ZAHTEV
            serverGUI.ispisiObavestenje("Operacija: " + kto.getOperacija());
            switch (kto.getOperacija()) {

                case Konstante.OPERACIJA_PROVERI_VALIDNOST_ADMINA:

                    Admin admin = (Admin) kto.getParametar();
                    serverGUI.ispisiObavestenje("Proveravam da li je admin...\n" + admin.getUsername() + " " + admin.getPassword());
                    admin = KontrolerPL.getInstance().proveriValidnostAdmina(admin.getUsername(), admin.getPassword());
                    sto.setUspesnostIzvrsenjaOperacije(1);
                    sto.setPodaci(admin);
                    break;
                case Konstante.OPERACIJA_VRATI_IGRACE:
                    serverGUI.ispisiObavestenje("Vracam sve igrace...");
                    List<Player> igraci = KontrolerPL.getInstance().vratiIgrace();
                    sto.setUspesnostIzvrsenjaOperacije(Konstante.OPERACIJA_VRATI_IGRACE);
                    sto.setPodaci(igraci);
                    break;
                case Konstante.OPERACIJA_OBRISI_TURNIR:
                    serverGUI.ispisiObavestenje("Brisem turnir...");
                    KontrolerPL.getInstance().obrisiTurnir((Game) kto.getParametar());
                    sto.setPodaci(new Long(5));
                    break;
                case Konstante.OPERACIJA_VRATI_TURNIRE:
                    serverGUI.ispisiObavestenje("Vracam sve turnire...");
                    List<Game> turniri = KontrolerPL.getInstance().vratiTurnire();
                    sto.setUspesnostIzvrsenjaOperacije(Konstante.OPERACIJA_VRATI_TURNIRE);
                    sto.setPodaci(turniri);
                    break;
                case Konstante.OPERACIJA_VRATI_PRIJAVE:
                    serverGUI.ispisiObavestenje("Vracam sve prijave...");
                    List<Entrence> prijave = KontrolerPL.getInstance().vratiPrijave();
                    sto.setUspesnostIzvrsenjaOperacije(1);
                    sto.setPodaci(prijave);
                    break;
                case Konstante.OPERACIJA_KREIRAJ_NOVOG_IGRACA:
                    Player player = (Player) kto.getParametar();
                    serverGUI.ispisiObavestenje("Kreiram novog igraca...");
                    KontrolerPL.getInstance().kreirajNovogIgraca(player);
                    serverGUI.ispisiNovogKlijenta(player.getFirstName() + " "
                            + player.getLastName() + ", " + player.getUsername());
                    sto.setUspesnostIzvrsenjaOperacije(1);
                    sto.setPodaci(player);
                    break;
                case Konstante.OPERACIJA_KREIRAJ_NOVOG:
                    OpstiDomenskiObjekat e = (OpstiDomenskiObjekat) kto.getParametar();
                    serverGUI.ispisiObavestenje("Kreiram novog igraca...");
                    KontrolerPL.getInstance().kreirajNovog(e);
                    sto.setUspesnostIzvrsenjaOperacije(1);
                    setImeIPrezime("administrator");
                    sto.setPodaci(e);
                    break;
                case Konstante.OPERACIJA_KREIRAJ_NOVU_PRIJAVU:
                    OpstiDomenskiObjekat prijava = (OpstiDomenskiObjekat) kto.getParametar();
                    serverGUI.ispisiObavestenje("Kreiram novog igraca...");
                    setMojaPrijava((Entrence) prijava);
                    KontrolerPL.getInstance().kreirajNovuPrijavu(this, prijava);
                    //KontrolerPL.getInstance().prebrojPrijaveZaOvajTurnir(((Entrence)prijava).getGame1().getGameID(),0);
                    sto.setUspesnostIzvrsenjaOperacije(1);
                    sto.setPodaci(prijava);
                    break;
                case Konstante.OPERACIJA_KREIRAJ_TURNIR:
                    Game game = (Game) kto.getParametar();
                    serverGUI.ispisiObavestenje("Kreiram novi turnir...");
                    KontrolerPL.getInstance().kreirajTurnir(game);
                    sto.setUspesnostIzvrsenjaOperacije(1);
                    sto.setPodaci(game);
                    break;
                case Konstante.OPERACIJA_UBACI_STO:
                    GameTable gt = (GameTable) kto.getParametar();
                    serverGUI.ispisiObavestenje("Kreiram novi sto za turnir " + gt.getGame().getName());
                    KontrolerPL.getInstance().ubaciSto(gt);
                    sto.setUspesnostIzvrsenjaOperacije(1);
                    sto.setPodaci(gt);
                    break;
                case Konstante.OPERACIJA_VRATI_DRZAVE:
                    serverGUI.ispisiObavestenje("Vracam drzave...");
                    List<Country> lm = KontrolerPL.getInstance().vratiDrzave();
                    sto.setUspesnostIzvrsenjaOperacije(1);
                    sto.setPodaci(lm);
                    break;
                case Konstante.OPERACIJA_PRETRAZI_IGRACE:
                    serverGUI.ispisiObavestenje("Vracam trazene igrace...");
                    List<Player> li = KontrolerPL.getInstance().pretraziIgrace(kto.getParametar().toString());
                    sto.setUspesnostIzvrsenjaOperacije(1);
                    sto.setPodaci(li);
                    for (int i = 0; i < li.size(); i++) {
                        serverGUI.ispisiObavestenje((i + 1) + ". " + li.get(i));
                    }
                    break;
                case Konstante.OPERACIJA_ZAPAMTI_IGRACA:
                    serverGUI.ispisiObavestenje("Menjam podatke o igracu...");
                    Player pla = (Player) kto.getParametar();
                    KontrolerPL.getInstance().zapamtiIgraca2(pla);
                    sto.setUspesnostIzvrsenjaOperacije(1);
                    sto.setPodaci(pla);

                    break;
                case Konstante.OPERACIJA_VRATI_GRADOVE:
                    serverGUI.ispisiObavestenje("Vracam gradove...");
                    List<City> listam = KontrolerPL.getInstance().vratiGradove();
                    sto.setUspesnostIzvrsenjaOperacije(1);
                    sto.setPodaci(listam);
                    break;
                case Konstante.OPERACIJA_KREIRAJ_NOVI_GRAD:
                    serverGUI.ispisiObavestenje("Kreiram grad...");
                    KontrolerPL.getInstance().kreirajNoviGrad((City) kto.getParametar());
                    sto.setUspesnostIzvrsenjaOperacije(1);
                    sto.setPodaci("Grad je kreiran");
                    break;
                case Konstante.OPERACIJA_OBRADI_LOGIN:
                    //Player pl = (Player)kto.getParametar();
                    serverGUI.ispisiObavestenje("Proveravam postojanje igraca...");
                    String[] data = kto.getParametar().toString().split(":");
                    Player p = KontrolerPL.getInstance().obradiLogIn(data[0], data[1]);
                    p.setUlogovan("da");
                    KontrolerPL.getInstance().zapamtiIgraca2(p);
                    sto.setUspesnostIzvrsenjaOperacije(1);
                    sto.setPodaci(p);
                    setImeIPrezime(p.getUsername());
                    serverGUI.ispisiNovogKlijenta(p.getFirstName() + " " + p.getLastName() + " [" + p.getUsername() + "]");
                    break;
                case Konstante.OPERACIJA_DAJ_TALON_IGRACU:
                    //Player pl = (Player)kto.getParametar();
                    serverGUI.ispisiObavestenje("Dajem talon igracu...");
                    List<Card> talon = getNovaIgra().getTalon();
                    sto.setUspesnostIzvrsenjaOperacije(1);
                    sto.setPodaci(talon);
                    for (NitKlijent nk : novaIgra.getUcesnici()) {
                        System.out.println("Klijent " + nk.getMojaPrijava().getPlayer1().getUsername());
                        TablicIgraObjekat tai = new TablicIgraObjekat();
                        tai.setPoruka("Zdravo carevi, evo talona!");
                        tai.setPodaci(talon);
                        nk.getOut().writeObject(tai);
                    }
                    //serverGUI.ispisiNovogKlijenta(p.getFirstName() + " " + p.getLastName() + " [" + p.getUsername() + "]");
                    break;
                case Konstante.OPERACIJA_OBRADI_POTEZ:
                    serverGUI.ispisiObavestenje("Obradjujem potez...");
                    TablicPotezObjekat tpo = (TablicPotezObjekat) kto.getParametar();
                    Card c = tpo.getKarta();
                    Move move = new Move(new Date(), tpo.getOpisPoteza()+"{"+new Random().nextDouble()+"}", "Talon: " + tpo.getIndeksiTalon().size(), true,
                            getMojaPrijava().getGame1(), getMojaPrijava().getPlayer1());
                    Long id = KontrolerPL.getInstance().ubaciPotez(move);
                    System.out.println("s " + tpo.getOpisPoteza().length());
                    move.setMoveID(id);
                    MoveCard moveCard = new MoveCard(new MoveCardPK(c.getCardID(), id), new Date(), c, move);

                    KontrolerPL.getInstance().ubaciPotezKartu(moveCard);
                    if (!tpo.isNovaRunda()) {
                        System.out.println("gotova runda NIJE");
                        for (NitKlijent nk : KontrolerPL.getInstance().getListaKlijenata()) {
                            if (!nk.equals(this) && nk.getMojaPrijava().getGame1().getName().equals(getMojaPrijava().getGame1().getName())) {
                                System.out.println("Klijent " + nk.getImeIPrezime());
                                nk.getOut().writeObject(tpo);
                            }
                        }
                    } else if (tpo.isNovaRunda()) {
                        System.out.println("gotova runda");
                        for (NitKlijent nk : KontrolerPL.getInstance().getListaKlijenata()) {
                            if (nk.getMojaPrijava().getGame1().getName().equals(getMojaPrijava().getGame1().getName())) {
                                nk.getOut().writeObject("Obradi rundu");
                            }
                        }
                    }
                    //serverGUI.ispisiNovogKlijenta(p.getFirstName() + " " + p.getLastName() + " [" + p.getUsername() + "]");
                    break;
                case Konstante.OPERACIJA_NOVO_DELJENJE:
                    if (KontrolerPL.getInstance().isNemaslanja()) {
                        KontrolerPL.getInstance().setNemaslanja(false);
                        break;
                    }
                    if (KontrolerPL.getInstance().isNemaslanja() == false) {
                        KontrolerPL.getInstance().setNemaslanja(true);
                        serverGUI.ispisiObavestenje("New deal...");

                        int operacija = Integer.parseInt(kto.getParametar().toString());
                        novaIgra.setBrojIgraca(2);
                        novaIgra.podeliKarte();
                        System.out.println("ja: " + novaIgra.getUcesnici().get(operacija).getMojaPrijava().getPlayer1().getUsername());
                        System.out.println("ide...1");

                        for (int i = 0; i < 2; i++) {
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
                            tia.setPoruka("Nastavak");
                            novaIgra.getUcesnici().get(i).getOut().writeObject(tia);
                            System.out.println("Otvaram nit za username: " + novaIgra.getUcesnici().get(i).getMojaPrijava().
                                    getPlayer1().getUsername());
                        }

                    }
                    break;
                case Konstante.OPERACIJA_STATISTIKA_IGRACA:
                    serverGUI.ispisiObavestenje("Stat player...");
                    StatisticPlayer sp = (StatisticPlayer) kto.getParametar();
                    KontrolerPL.getInstance().dodajStatistikuIgraca(sp);

                    break;
                case Konstante.OPERACIJA_AZURIRAJ_LOGIN:
                    serverGUI.ispisiObavestenje("Login...");
                    sto.setUspesnostIzvrsenjaOperacije(3);
                    List<Player> igraci2 = KontrolerPL.getInstance().vratiIgrace();
                    sto.setPodaci(igraci2);
                    for (NitKlijent nk : KontrolerPL.getInstance().getListaKlijenata()) {
                        nk.getOut().writeObject(sto);
                    }

                    break;
                case Konstante.OPERACIJA_IZAZOVI:
                    serverGUI.ispisiObavestenje("IZAZOV...");
                    sto.setUspesnostIzvrsenjaOperacije(4);
                    String username = kto.getParametar().toString().split(":")[0];
                    sto.setPodaci(getImeIPrezime() + ":" + kto.getParametar().toString().split(":")[1]);
                    for (NitKlijent nk : KontrolerPL.getInstance().getListaKlijenata()) {
                        if (nk.getImeIPrezime().equals(username)) {
                            nk.getOut().writeObject(sto);
                            break;
                        }
                    }
                    break;
                case Konstante.OPERACIJA_SALJEM_PORUKU_SERVERU:
                    serverGUI.ispisiObavestenje("Saljem poruku...");
                    sto.setUspesnostIzvrsenjaOperacije(Konstante.OPERACIJA_SALJEM_PORUKU_SERVERU);
                    Poruka poruka = new Poruka(kto.getParametar().toString().split(":")[0],
                            kto.getParametar().toString().split(":")[1]);
                    AbstractSO s = new UbaciSO();
                    s.izvrsiOperaciju(poruka);
                    break;
                case Konstante.OPERACIJA_IZAZOV_ODBIJEN:
                    serverGUI.ispisiObavestenje("IZAZOV...");
                    sto.setUspesnostIzvrsenjaOperacije(Konstante.OPERACIJA_IZAZOV_ODBIJEN);
                    String user = kto.getParametar().toString();
                    sto.setPodaci(getImeIPrezime());
                    for (NitKlijent nk : KontrolerPL.getInstance().getListaKlijenata()) {
                        if (nk.getImeIPrezime().equals(user)) {
                            nk.getOut().writeObject(sto);
                            break;
                        }
                    }
                    break;
                case Konstante.OPERACIJA_CHAT_U_IGRI:
                    serverGUI.ispisiObavestenje("Chat...");
                    String chatPoruka = kto.getParametar().toString().split(":")[0];
                    String chatUser = kto.getParametar().toString().split(":")[1];
                    sto.setUspesnostIzvrsenjaOperacije(Konstante.OPERACIJA_CHAT_U_IGRI);
                    sto.setPodaci(chatPoruka + ":" + chatUser);
                    NitKlijent nk = null;
                    for (NitKlijent nik : KontrolerPL.getInstance().getListaKlijenata()) {
                        if (nik.getMojaPrijava().getGame1().getName().equals(getMojaPrijava().getGame1().getName())
                                && !(nik.getMojaPrijava().getPlayer1().getUsername().equals(getImeIPrezime()))) {
                            nk = nik;
                            break;
                        }
                    }
                    nk.getOut().writeObject(sto);
                    break;
                case Konstante.OPERACIJA_IZAZOV_PRIHVACEN:
                    serverGUI.ispisiObavestenje("IZAZOV PRIHVACEN...");
                    String tipIgre = kto.getParametar().toString().split(":")[1];
                    String name = kto.getParametar().toString().split(":")[0] + ":" + kto.getParametar().toString().split(":")[2] + ":" + new Random().nextDouble();
                    Admin adminm = KontrolerPL.getInstance().proveriValidnostAdmina("admin", "admin");
                    Game gamee = new Game(name, new Date(), new Date(), "0/2", 2, 50.0, 100.0, adminm);
                    KontrolerPL.getInstance().kreirajTurnir(gamee);
                    List<NitKlijent> svi = KontrolerPL.getInstance().getListaKlijenata();
                    NitKlijent nk1 = null;
                    NitKlijent nk2 = null;
                    Player p1 = null;
                    Player p2 = null;
                    List<Player> sviIgraci = KontrolerPL.getInstance().vratiIgrace();
                    System.out.println("sviigraci: " + sviIgraci.size());
                    for (Player playerr : sviIgraci) {
                        if (playerr.getUsername().equals(name.split(":")[0])) {
                            System.out.println(playerr.getUsername() + "==" + name.split(":")[0]);
                            p1 = playerr;
                        }
                        if (playerr.getUsername().equals(name.split(":")[1])) {
                            System.out.println(playerr.getUsername() + "==" + name.split(":")[1]);
                            p2 = playerr;
                        }
                    }
                    System.out.println("sviklijenti: " + svi.size());
                    for (NitKlijent play : svi) {
                        if (play.getImeIPrezime().equals(name.split(":")[0])) {
                            System.out.println(play.getImeIPrezime() + "==" + name.split(":")[0]);
                            nk1 = play;
                        }
                        if (play.getImeIPrezime().equals(name.split(":")[1])) {
                            System.out.println(play.getImeIPrezime() + "==" + name.split(":")[1]);
                            nk2 = play;
                        }
                    }
                    List<Game> turnirs = KontrolerPL.getInstance().vratiTurnire();
                    Long gameIDD = new Long(-5);
                    for (Game g : turnirs) {
                        if (g.getName().equals(gamee.getName())) {
                            gameIDD = g.getGameID();
                            break;
                        }
                    }
                    gamee.setGameID(gameIDD);
                    System.out.println(p1.getPlayerID() + "," + gameIDD);
                    OpstiDomenskiObjekat en1 = new Entrence(new EntrencePK(p1.getPlayerID(), gameIDD),
                            "Duel", "Pocinje..", new Date(), "ok", gamee, p1);
                    System.out.println(p2.getPlayerID() + "," + gameIDD);
                    OpstiDomenskiObjekat en2 = new Entrence(new EntrencePK(p2.getPlayerID(), gameIDD),
                            "Duel", "Pocinje..", new Date(), "ok", gamee, p2);
                    System.out.println("ovde>s");
                    nk1.setMojaPrijava((Entrence) en1);
                    nk2.setMojaPrijava((Entrence) en2);
                    System.out.println("prva");
                    KontrolerPL.getInstance().kreirajNovuPrijavu(nk1, en1);
                    System.out.println("druga");
                    KontrolerPL.getInstance().kreirajNovuPrijavu(nk2, en2);
                    break;
                case Konstante.OPERACIJA_VRATI_STATISTIKU_IGRACA:
                    serverGUI.ispisiObavestenje("Stat...");
                    List<StatisticPlayer> statistike = KontrolerPL.getInstance().vratiStatIgraca();
                    sto.setPodaci(statistike);
                    sto.setUspesnostIzvrsenjaOperacije(Konstante.OPERACIJA_VRATI_STATISTIKU_IGRACA);
                    break;
                case Konstante.OPERACIJA_VRATI_RANG_IGRACA:
                    serverGUI.ispisiObavestenje("Rang...");
                    List<Player> rang = KontrolerPL.getInstance().vratiIgrace();
                    sto.setPodaci(rang);
                    sto.setUspesnostIzvrsenjaOperacije(Konstante.OPERACIJA_VRATI_RANG_IGRACA);
                    break;
                case Konstante.OPERACIJA_IZASAO_IGRAC:
                    NitKlijent nkk = null;
                    serverGUI.ispisiObavestenje("Rang...");
                    for (NitKlijent nik : KontrolerPL.getInstance().getListaKlijenata()) {
                        if (nik.getMojaPrijava().getGame1().getName().equals(getMojaPrijava().getGame1().getName())
                                && !(nik.getMojaPrijava().getPlayer1().getUsername().equals(getImeIPrezime()))) {
                            nkk = nik;
                            break;
                        }
                    }
                    
                    sto.setUspesnostIzvrsenjaOperacije(Konstante.OPERACIJA_IZASAO_IGRAC);
                    nkk.getOut().writeObject(sto);
                    break;
                default:
                    sto = null;
                    break;

            }

            //ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        } catch (Exception ex) {
            sto = new ServerTransferObjekat();
            sto.setUspesnostIzvrsenjaOperacije(-1);
            sto.setException(ex);
        }
        if (!(sto.getPodaci() instanceof Card) && !(sto.getPodaci() instanceof TablicPotezObjekat)
                && !(sto.getPodaci() instanceof Long) && (kto.getOperacija() != Konstante.OPERACIJA_IZAZOVI)
                && (kto.getOperacija() != Konstante.OPERACIJA_IZAZOV_ODBIJEN)
                && (kto.getOperacija() != Konstante.OPERACIJA_IZAZOV_PRIHVACEN)
                && (kto.getOperacija() != Konstante.OPERACIJA_CHAT_U_IGRI)&&
                (kto.getOperacija()!=Konstante.OPERACIJA_IZASAO_IGRAC)) {
            
            out.writeObject(sto);

        }

    }

    public TablicIgra getNovaIgra() {
        return novaIgra;
    }

    public void setNovaIgra(TablicIgra novaIgra) {
        this.novaIgra = novaIgra;
    }

    public ServerGUI getServerGUI() {
        return serverGUI;
    }

    public void setServerGUI(ServerGUI serverGUI) {
        this.serverGUI = serverGUI;
    }

    public ObjectInputStream getIn() {
        return in;
    }

    public void setIn(ObjectInputStream in) {
        this.in = in;
    }

    public ObjectOutputStream getOut() {
        return out;
    }

    public void setOut(ObjectOutputStream out) {
        this.out = out;
    }

    public List<Card> getMojeKarte() {
        return mojeKarte;
    }

    public void setMojeKarte(List<Card> mojeKarte) {
        this.mojeKarte = mojeKarte;
    }

    public String getImeIPrezime() {
        return imeIPrezime;
    }

    public void setImeIPrezime(String imeIPrezime) {
        this.imeIPrezime = imeIPrezime;
    }

    public Long getKredit() {
        return kredit;
    }

    public void setKredit(Long kredit) {
        this.kredit = kredit;
    }

    public Entrence getMojaPrijava() {
        return mojaPrijava;
    }

    public void setMojaPrijava(Entrence mojaPrijava) {
        this.mojaPrijava = mojaPrijava;
    }

}
