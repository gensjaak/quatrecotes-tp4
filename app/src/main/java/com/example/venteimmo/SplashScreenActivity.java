package com.example.venteimmo;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

public class SplashScreenActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Pas de toolbar, statusbar absolute
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        setContentView(R.layout.activity_splashscreen);

        this.gotoMainActivity();
    }

    public void monProfil(View view) {

    }

    public void listAnnonces(View view) {
        Intent intent = new Intent(this, ListAnnonceActivity.class);
        startActivity(intent);
    }

    public void gotoMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void postAnnonce(View view) {
    }

    public void randomAnnonce(View view) {
        Intent intent = new Intent(this, DetailAnnonceActivity.class);
        startActivity(intent);
    }

    public void listFavoritesAnnonces(View view) {
        Intent intent = new Intent(this, ListFavoriteAnnoncesActivity.class);
        startActivity(intent);
    }
}
