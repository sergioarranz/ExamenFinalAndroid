package com.utad.sergio.examenandroid;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by sergio on 19/2/18.
 */

public class FirebaseAdmin {

    public FirebaseAuth mAuth;
    public FirebaseDatabase database;
    public FirebaseAdminListener listener;
    public FirebaseUser user;
    public DatabaseReference myRef;

    public FirebaseAdmin(){
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
    }

    public void setListener(FirebaseAdminListener listener){
        this.listener=listener;
    }

    public void downloadAndObserveBranch(final String branch) {
        DatabaseReference refBranch = myRef.child(branch);
        refBranch.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                listener.fireBaseAdminbranchDownload(branch,dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                listener.fireBaseAdminbranchDownload(branch,null);
            }
        });
    }

    public void LogOut(){
        FirebaseAuth.getInstance().signOut();
    }
}
