/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mj.tablicturniriservermvn.start;

import com.mj.tablicturniriservermvn.forma.ServerGUI;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author student1
 */
public class ServerStart extends Thread {

    private int port;
    private String IPAdresa;
    private ServerGUI serverGUI;

    public ServerStart(int port, String IPAdresa, ServerGUI serverGUI) {
        this.port = port;
        this.serverGUI = serverGUI;
        this.IPAdresa = IPAdresa;
    }

    @Override
    public void run() {
        try {
            ServerSocket ss = new ServerSocket(9098);
            serverGUI.ispisiObavestenje("Server je pokrenut na adresi: " + IPAdresa + ", port: " + port + " i ceka klijenta ");
            while (true) {
                Socket s = ss.accept();
                serverGUI.ispisiObavestenje("Klijent se povezao sa serverom");
                NitKlijent nk = new NitKlijent(s, serverGUI);
                //KontrolerPL.getInstance().dodajKlijenta(nk);
                nk.start();
            }
            //serverGUI.ispisiObavestenje("Server je zavrsio sa radom");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
