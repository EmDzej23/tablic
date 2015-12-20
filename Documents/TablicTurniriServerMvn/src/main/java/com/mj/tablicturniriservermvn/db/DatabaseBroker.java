/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mj.tablicturniriservermvn.db;

import domain.Game;
import domain.OpstiDomenskiObjekat;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 *
 * @author student1
 */
public class DatabaseBroker {

    private static DatabaseBroker instance = new DatabaseBroker();
    private Connection konekcija;

    private DatabaseBroker() {

    }

    public static DatabaseBroker getInstance() {
        return instance;
    }

    public void ucitajDriver() throws Exception {
        try {
            Class.forName(Util.getInstance().get("driver"));
        } catch (ClassNotFoundException ex) {
            throw new Exception("Driver nije ucitan ", ex);
        }
    }

    public void otvoriKonekciju() throws Exception {
        try {
            konekcija = DriverManager.getConnection(Util.getInstance().get("url"),
                    Util.getInstance().get("user"), Util.getInstance().get("pass"));
            konekcija.setAutoCommit(false);
        } catch (SQLException ex) {
            throw new Exception("Konekcija nije otvorena ", ex);
        }
    }

    public void zatvoriKonekciju() throws Exception {
        try {
            konekcija.close();
        } catch (SQLException ex) {
            throw new Exception("Konekcija nije zatvorena ", ex);
        }
    }

    public void commitTransakcije() throws Exception {
        try {
            konekcija.commit();
        } catch (SQLException ex) {
            throw new Exception("Neuspesan commit transakcije ", ex);
        }
    }

    public void rollbackTransakcije() throws Exception {
        try {
            konekcija.rollback();
        } catch (SQLException ex) {
            throw new Exception("Neuspesan rollback transakcije ", ex);
        }
    }

    public List<OpstiDomenskiObjekat> select(OpstiDomenskiObjekat odo,String uslov) throws Exception {
        try {
            String sql = "SELECT * FROM " + odo.vratiNazivTabeleZaPrikaz()+uslov;
            System.out.println("SQL "+sql);
            Statement sqlStatement = konekcija.createStatement();
            ResultSet rs = sqlStatement.executeQuery(sql);
            List<OpstiDomenskiObjekat> lista = odo.napuni(rs);
            sqlStatement.close();
            return lista;
        } catch (SQLException ex) {
            throw new Exception("Neuspesan prikaz podataka iz baze ", ex);
        }
    }

    public void insert(OpstiDomenskiObjekat odo) throws Exception {
        try {
            
            String sql = "INSERT INTO " + odo.vratiNazivTabele()
                    + " VALUES (" + odo.vratiVrednostiZaInsert() + ")";
            System.out.println("sql: "+sql);
            Statement sqlStatement = konekcija.createStatement();
            sqlStatement.executeUpdate(sql);
            sqlStatement.close();
        } catch (SQLException ex) {
            throw new Exception("Neuspesan prikaz podataka iz baze ", ex);
        }
    }

    public void update(OpstiDomenskiObjekat odo) throws Exception {
        try {
            
            String sql = "UPDATE "+ odo.vratiUslovZaUpdate(odo);
            System.out.println("SQL UPDATE"+sql);
            Statement sqlStatement = konekcija.createStatement();
            sqlStatement.executeUpdate(sql);
            sqlStatement.close();
        } catch (SQLException ex) {
            throw new Exception("Neuspesan prikaz podataka iz baze ", ex);
        }
    }
    public void delete(OpstiDomenskiObjekat odo,String id) throws Exception {
        try {
            String sql = "DELETE FROM " + odo.getClass().getSimpleName() +" WHERE "+id+"="+((Game)odo).getGameID();
            System.out.println("SQL "+sql);
            Statement sqlStatement = konekcija.createStatement();
            sqlStatement.executeUpdate(sql);
            sqlStatement.close();
        } catch (SQLException ex) {
            throw new Exception("Neuspesan prikaz podataka iz baze ", ex);
        }
    }

}
