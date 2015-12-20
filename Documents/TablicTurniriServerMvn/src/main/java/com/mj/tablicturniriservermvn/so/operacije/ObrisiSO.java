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

/**
 *
 * @author Marko
 */
public class ObrisiSO extends AbstractSO {

    @Override
    protected void izvrsiValidaciju() throws ValidationException {
       
    }

    @Override
    protected void proveriPreduslov() throws PreConditionException {
      
    }

    @Override
    protected void izvrsiTransakciju(Object o) throws Exception {
        List<Object> l = (List<Object>)o;
        DatabaseBroker.getInstance().delete((OpstiDomenskiObjekat)l.get(0),l.get(1).toString());
    }
    
}
