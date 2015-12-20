/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mj.tablicturniriservermvn.db;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author Marko
 */
public class Util {
    private static Util instance;
    private Properties prop;
    
    public Util () throws IOException {
        prop = new Properties();
        prop.load(new FileInputStream("db.properties"));
        
    }
    public static Util getInstance () throws IOException {
        if (instance==null) {
            instance = new Util ();
        }
        return instance;
    }
    public String get (String key) {
        return prop.getProperty(key,"");
    }
}
