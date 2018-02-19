package com.utad.sergio.examenandroid;

import org.json.JSONObject;

/**
 * Created by sergio on 19/2/18.
 */

public class DataHolder {

    // Definición de variables estáticas para almacenar la información del usuario, datos de JSON y Firebase
    public static DataHolder instance = new DataHolder();
    public static JSONObject jsonTwitter;
    public static FirebaseAdmin firebaseAdmin;

    public DataHolder(){
        firebaseAdmin=new FirebaseAdmin();
    }
}
