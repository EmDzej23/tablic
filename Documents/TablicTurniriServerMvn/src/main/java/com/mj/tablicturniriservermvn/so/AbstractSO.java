/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mj.tablicturniriservermvn.so;

import com.mj.tablicturniriservermvn.db.DatabaseBroker;
import exception.PreConditionException;
import exception.SistemOperationException;
import exception.ValidationException;

/**
 *
 * @author student1
 */
public abstract class AbstractSO {


    public void izvrsiOperaciju(Object o) throws Exception {
        otvoriKonekciju();
        try {
            izvrsiValidaciju();
            proveriPreduslov();
            izvrsiTransakciju(o);
            potvrdiTransakciju();
        } catch (ValidationException ve) {
            throw new Exception(ve.getMessage());
        } catch (PreConditionException pce) {
            throw new Exception(pce.getMessage());
        } catch (SistemOperationException soe) {
            ponistiTransakciju();
            throw new Exception(soe.getMessage());
        } 
//        potvrdiTransakciju();
//        ponistiTransakciju();
        finally{
            zatvoriKonekciju();
        }
    }

    private void otvoriKonekciju() throws Exception {
        DatabaseBroker.getInstance().otvoriKonekciju();
    }

    private void zatvoriKonekciju() throws Exception {
        DatabaseBroker.getInstance().zatvoriKonekciju();
    }

    protected abstract void izvrsiValidaciju() throws ValidationException;

    protected abstract void proveriPreduslov() throws PreConditionException;

    protected abstract void izvrsiTransakciju(Object o) throws Exception;

    private void potvrdiTransakciju() throws Exception {
        DatabaseBroker.getInstance().commitTransakcije();
    }

    private void ponistiTransakciju() throws Exception {
        DatabaseBroker.getInstance().rollbackTransakcije();
    }

}
