package com.utad.sergio.examenandroid;

import com.google.firebase.database.DataSnapshot;

/**
 * Created by sergio on 19/2/18.
 */

public interface FirebaseAdminListener {
    public void firebaseAdmin_LoginOK(boolean blOK);
    public void fireBaseAdminbranchDownload(String branch, DataSnapshot dataSnapshot);
}
