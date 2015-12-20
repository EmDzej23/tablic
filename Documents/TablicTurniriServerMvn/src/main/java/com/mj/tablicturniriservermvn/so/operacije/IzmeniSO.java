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

/**
 *
 * @author Marko
 */
public class IzmeniSO extends AbstractSO{

    @Override
    protected void izvrsiValidaciju() throws ValidationException {
        
    }

    @Override
    protected void proveriPreduslov() throws PreConditionException {
        
    }

    @Override
    protected void izvrsiTransakciju(Object o) throws Exception {
        System.out.println("...MENJAM...");
        DatabaseBroker.getInstance().update((OpstiDomenskiObjekat)o);
    }
    
}
