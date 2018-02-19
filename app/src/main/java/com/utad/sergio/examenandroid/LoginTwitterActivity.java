package com.utad.sergio.examenandroid;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.TwitterAuthProvider;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DataSnapshot;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginTwitterActivity extends AppCompatActivity {

    TwitterLoginButton loginButton;
    LoginTwitterActivityEvents events;

    // JSON que almacenará la info del usuario para pasársela a otro activity a través del DataHolder
    private JSONObject data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inicializacion de Twitter tras añadir el botón al XML y las dependencias en el Gradle
        Twitter.initialize(this);
        setContentView(R.layout.activity_login_twitter);

        // Inicialización del gestor de eventos
        events=new LoginTwitterActivityEvents(this);

        // Escuchador de eventos de FirebaseAdmin
        DataHolder.firebaseAdmin = new FirebaseAdmin();
        DataHolder.instance.firebaseAdmin.setListener(events);

        // Botón Twitter
        loginButton = (TwitterLoginButton) findViewById(R.id.login_button);
        loginButton.setCallback(new Callback<TwitterSession>() {
            // LOGIN OK
            @Override
            public void success(Result<TwitterSession> result) {
                data = new JSONObject();
                try {
                    data.put("user", result.data.getUserName().toString());
                    data.put("id", result.data.getUserId());
                } catch (JSONException e){
                    FirebaseCrash.report(new Exception("Datos de usuario creados de forma errónea"));
                    e.printStackTrace();
                }
                handleTwitterSession(result.data);
            }
            // FALLO LOGIN
            @Override
            public void failure(TwitterException exception) {
                Log.w("failure", "twitterLogin:failure", exception);
                //updateUI(null);
            }
        });
    }

    // Método que ejecuta cosas externas (para FB o Twitter)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result to the login button.
        loginButton.onActivityResult(requestCode, resultCode, data);
    }

    // Login con Twitter usando las claves de autenticación
    private void handleTwitterSession(TwitterSession session) {
        Log.d("TWITTER LOGIN EXITO", "handleTwitterSession:" + session);

        AuthCredential credential = TwitterAuthProvider.getCredential(
                session.getAuthToken().token,
                session.getAuthToken().secret);

        DataHolder.instance.firebaseAdmin.mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("LoginSuccessful", "signInWithCredential:success");
                            DataHolder.instance.firebaseAdmin.user = DataHolder.instance.firebaseAdmin.mAuth.getCurrentUser();
                            DataHolder.instance.firebaseAdmin.listener.firebaseAdmin_LoginOK(true);
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            DataHolder.instance.firebaseAdmin.listener.firebaseAdmin_LoginOK(false);
                            Log.w("LoginFailure", "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginTwitterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
        DataHolder.instance.jsonTwitter = data;
    }
}

// Gestor de Eventos del Login de Twitter
class LoginTwitterActivityEvents implements FirebaseAdminListener {

    LoginTwitterActivity loginTwitterActivity;

    // Constructor
    public LoginTwitterActivityEvents(LoginTwitterActivity loginTwitterActivity){
        this.loginTwitterActivity = loginTwitterActivity;
    }

    // Método por el que pasa una vez recibe el OK del login
    @Override
    public void firebaseAdmin_LoginOK(boolean blOK) {
        Log.v("LoginTwitterActivity", "LOGIN TWITTER CORRECTO");
        if (blOK) {
            Intent intent = new Intent(loginTwitterActivity,MainActivity.class);
            loginTwitterActivity.startActivity(intent);
            loginTwitterActivity.finish();
        } else {

        }
    }

    @Override
    public void fireBaseAdminbranchDownload(String branch, DataSnapshot dataSnapshot) {

    }
}