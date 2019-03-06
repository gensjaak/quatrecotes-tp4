package com.example.venteimmo;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private FloatingActionButton fab;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Declare views
        this.declareViews();

        setSupportActionBar(this.toolbar);

        this.toggle = new ActionBarDrawerToggle(this, this.drawer, this.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        // Bind listener to views
        this.listenToViews();

        // Shows a fragment at startup
        this.showAtStartup();
    }

    private void showAtStartup() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = new LastAnnoncesFragment();

        fragmentManager.beginTransaction().replace(R.id.dynamicContent, fragment).commit();
        this.setTitle(R.string.dernieres_annonces);
    }

    private void listenToViews() {
        // The FAB
        this.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*FragmentManager fragmentManager = getSupportFragmentManager();
                AppMainFragment fragment = new DeposerAnnonceFragment();
                fragmentManager.beginTransaction().replace(R.id.dynamicContent, fragment).commit();
                toolbar.setTitle(R.string.deposer_une_annonce);*/
                vueEnMaintenance();
            }
        });

        // The drawer toggler
        this.drawer.addDrawerListener(this.toggle);
        this.toggle.syncState();

        // The navigation view listener binding
        this.navigationView.setNavigationItemSelectedListener(this);
    }

    private void vueEnMaintenance() {
        new AlertDialog.Builder(this)
                .setTitle("Oups !")
                .setMessage("La vue \n\"Dépôt d'une annonce\"\n est en cours de maintenance, veuillez revenir plus tard.")
                .setPositiveButton("Okay", null)
                .show();
    }

    private void declareViews() {
        this.toolbar = findViewById(R.id.toolbar);
        this.fab = findViewById(R.id.fab);
        this.drawer = findViewById(R.id.drawer_layout);
        this.navigationView = findViewById(R.id.nav_view);
    }

    @Override
    public void onBackPressed() {
        if (this.drawer.isDrawerOpen(GravityCompat.START)) {
            this.drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_about) {
            new AlertDialog.Builder(this)
                    .setTitle("Equipe de développement")
                    .setMessage("Quatre côtés: \n\n\t\t\tChekina TATY - 21710425\n\t\t\tJean-Jacques AKAKPO - 21811857\n\t\t\tPaule OBONE ENGOUANG - 21713181\n\t\t\tDesty MPASSI MATONDO - 21711427")
                    .setPositiveButton("Félicitations", null)
                    .show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        FragmentManager fragmentManager = getSupportFragmentManager();
        AppMainFragment fragment = null;

        switch (id) {
            case R.id.annonce_au_hasard:
                fragment = null;
                Intent intent = new Intent(this, DetailAnnonceActivity.class);

                startActivity(intent);
                break;

            case R.id.deposer_une_annonce:
                // fragment = new DeposerAnnonceFragment();
                vueEnMaintenance();
                break;

            case R.id.mes_annonces:
                fragment = new AnnoncesFavoritesFragment();
                break;

            case R.id.toutes_les_annonces:
                fragment = new ListAnnoncesFragment();
                break;

            case R.id.dernieres_annonces:
                fragment = new LastAnnoncesFragment();
                break;

            case R.id.mon_profil:
                fragment = null;
                Intent intent1 = new Intent(this, MonProfil.class);

                startActivity(intent1);
                break;

            case R.id.deconnexion:
                deconnexion();
                break;

            default:
                Toast.makeText(this, "Not implemented yet", Toast.LENGTH_SHORT).show();
        }

        if (fragment != null) {
            fragmentManager.beginTransaction().replace(R.id.dynamicContent, fragment).commit();
            this.toolbar.setTitle(item.getTitle().toString());
        }
        this.drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    private void deconnexion() {
        new AlertDialog.Builder(this)
                .setTitle("Confirmer")
                .setMessage("Voulez-vous quitter l'application?")
                .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                })
                .setNegativeButton("Non", null)
                .show();
    }
}
