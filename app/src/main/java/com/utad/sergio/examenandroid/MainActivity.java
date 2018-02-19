package com.utad.sergio.examenandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.GenericTypeIndicator;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    TextView nombre, email;
    Map<String,Profile> profiles;
    MainActivityEvents events;
    SupportMapFragment mapFragment;
    MarkerProfileFragment markerProfileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializacion de FirebaseAdmin, gestor de eventos y escuchador
        DataHolder.firebaseAdmin = new FirebaseAdmin();
        events = new MainActivityEvents(this);
        DataHolder.instance.firebaseAdmin.setListener(events);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        // Set de Color global para la app
        toolbar.setBackgroundColor(getResources().getColor(R.color.colour3));
        setSupportActionBar(toolbar);

        // Boton Floating (esquina inferior derecha)
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Menú de navegación
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);

        // Set datos de Twitter en NavigationView
        nombre = (TextView)header.findViewById(R.id.nombreNav);
        email = (TextView)header.findViewById(R.id.emailNav);
        try {
            nombre.setText(DataHolder.jsonTwitter.get("user").toString());
            events.setData();
        }catch (JSONException e){
            Log.v("Exception",""+e.getMessage());
        }

        DataHolder.instance.firebaseAdmin.downloadAndObserveBranch("profiles");


        // Inicialización de los fragments

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(events);
        markerProfileFragment = (MarkerProfileFragment) getSupportFragmentManager().findFragmentById(R.id.UserInfoFragment);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            LogOut();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {

        } else if (id == R.id.nav_shopping) {

        } else if (id == R.id.nav_videogames) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // Métodos linkeados vinculados al clic del botón de MarkerProfileFragment
    public void botonClicked(View v){

    }

    public void botonClicked2(View v){

    }

    // Método cerrar sesión usuario Firebase vinculado a botón Cerrar sesión

    public void LogOut(){
        try{
            DataHolder.instance.firebaseAdmin.LogOut();
            Intent intent = new Intent(getBaseContext(),LoginTwitterActivity.class);
            startActivity(intent);
            finish();
        }catch (Exception e){
            FirebaseCrash.report(new Exception("NO SE PUDO DESLOGUEAR CORRECTAMENTE"));
        }


    }
}
// Escuchador de eventos implementando las clases de Firebase y Geolocalización de Google
class MainActivityEvents implements FirebaseAdminListener, OnMapReadyCallback,GoogleMap.OnMarkerClickListener {

    MainActivity mainActivity;
    // Instancia de Mapa
    private GoogleMap mMap;

    public MainActivityEvents(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }

    @Override
    public void firebaseAdmin_LoginOK(boolean blOK) {

    }
    // Descarga de datos rama Firebase
    @Override
    public void fireBaseAdminbranchDownload(String branch, DataSnapshot dataSnapshot) {
        if(branch.equals("profiles")){
            mainActivity.profiles = new HashMap<>();
            GenericTypeIndicator<Map<String,Profile>> indicator = new GenericTypeIndicator<Map<String,Profile>>(){};
            mainActivity.profiles = dataSnapshot.getValue(indicator);
            agregarPinesProfiles();
        }
    }
    // Seteo de datos en el NavigationView
    public void setData(){
        mainActivity.email.setText(DataHolder.firebaseAdmin.mAuth.getCurrentUser().getEmail());
    }

    // Pines pinchados
    @Override
    public boolean onMarkerClick(Marker marker) {
        Profile profile = (Profile) marker.getTag();
        mainActivity.markerProfileFragment.txtUser.setText(profile.name.toString());
        mainActivity.markerProfileFragment.txtNumber.setText(String.valueOf(profile.number));
        return  true;
    }
    // Primer método por el que pasa el mapa
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);
    }

    public void quitarPinesProfiles(){
        if(mainActivity.profiles!=null) {
            for (Object i : mainActivity.profiles.keySet()) {
                Profile perfilTemp = mainActivity.profiles.get(i.toString());
                if (perfilTemp.getMarker() != null) {
                    perfilTemp.getMarker().remove();
                }
            }
        }
    }

    public void agregarPinesProfiles(){
        LatLng perfilPos=null;
        for (Object i: mainActivity.profiles.keySet()){
            Profile perfilTemp = mainActivity.profiles.get(i.toString());

            perfilPos = new LatLng(perfilTemp.lat, perfilTemp.lon);

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(perfilPos);
            markerOptions.title(perfilTemp.name);

            if(mMap!= null){
                Marker marker = mMap.addMarker(markerOptions);
                marker.setTag(perfilTemp);
                perfilTemp.setMarker(marker);
            }

        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(perfilPos,5));
    }
}
