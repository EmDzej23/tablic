/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mj.tablicturniriservermvn.so.operacije;

import com.mj.tablicturniriservermvn.db.DatabaseBroker;
import com.mj.tablicturniriservermvn.so.AbstractSO;
import domain.OpstiDomenskiObjekat;
import exception.PreConditionException;
import exception.ValidationException;
import java.util.List;
import model.sesija.Sesija;

/**
 *
 * @author Marko
 */
public class VratiSO extends AbstractSO {
    private List<OpstiDomenskiObjekat> lista;
    private OpstiDomenskiObjekat objekat;
    @Override
    protected void izvrsiValidaciju() throws ValidationException {
        
    }

    @Override
    protected void proveriPreduslov() throws PreConditionException {
       
    }

    @Override
    protected void izvrsiTransakciju(Object o) throws Exception {
        lista = DatabaseBroker.getInstance().select((OpstiDomenskiObjekat)o,Sesija.vratiInstancu().getMapaUnosVrednost().get("uslov"));
        Sesija.vratiInstancu().getMapaUnosVrednost().put("uslov", "");
    }
    public List<OpstiDomenskiObjekat> vratiListu () {
        return lista;
    }
    public OpstiDomenskiObjekat vratiObjekat () {
        return objekat;
    }
}
