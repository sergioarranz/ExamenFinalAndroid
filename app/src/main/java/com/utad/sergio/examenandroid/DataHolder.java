package com.utad.sergio.examenandroid;

/**
 * Created by sergio on 19/2/18.
 */

public class DataHolder {

    public static DataHolder instance = new DataHolder();

    public FirebaseAdmin fireBaseAdmin;

    public DataHolder(){
        fireBaseAdmin=new FirebaseAdmin();
    }
}
