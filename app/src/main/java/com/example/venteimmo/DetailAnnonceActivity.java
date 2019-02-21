package com.example.venteimmo;

import android.Manifest;
import android.arch.persistence.room.Room;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.venteimmo.models.Annonce;
import com.example.venteimmo.models.AppDatabase;
import com.example.venteimmo.models.Vendeur;
import com.example.venteimmo.services.AnnonceService;
import com.example.venteimmo.services.AsyncTaskRunner;
import com.example.venteimmo.utils.AnnonceApiResponse;


import com.glide.slider.library.Animations.DescriptionAnimation;
import com.glide.slider.library.SliderLayout;
import com.glide.slider.library.SliderTypes.BaseSliderView;
import com.glide.slider.library.SliderTypes.DefaultSliderView;
import com.glide.slider.library.SliderTypes.TextSliderView;
import com.glide.slider.library.Tricks.ViewPagerEx;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.venteimmo.Constants.ANNONCE_TRAVELLER_KEY;

@SuppressWarnings("SpellCheckingInspection")
public class DetailAnnonceActivity extends AppCompatActivity implements AsyncTaskRunner, BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {
    private Annonce item = null;

    private RelativeLayout loadingPanel;
    private TextView proprietePrix, proprieteVille, proprieteDescription, proprieteDatePublication, vendeurNom, vendeurEmail, vendeurTelephone,nmbrep,caract,codepos;
    private ImageView proprieteImage;
    private SliderLayout sliderLayout;
    private ImageButton favorite, unFavorite;
    private FloatingActionButton photo;
    private static final int PERMISSION_CODE = 1000;
    private static final int IMAGE_CAPTURE_CODE = 1001;
    Uri image_uri;
    ArrayList<String> listName = new ArrayList<>();


    private AppDatabase database;
    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Constants.API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_annonce);

        // Relie les attributs de vues à leur élément correspondant dans le XML
        this.declareViews();

        // Build the database
        this.database = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, Constants.APP_DATABASE_NAME).build();

        // Afficher le panel de loading
        this.loadingPanel.setVisibility(View.VISIBLE);

        // Ajouter un bouton pour revenir en arrière
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Aller recueillir les données
        this.fetchData();

        //camera
        this.camera();






    }

    private void declareViews() {
        this.loadingPanel = findViewById(R.id.loadingPanel);
        this.favorite = findViewById(R.id.makeAnnonceFavorite);
        this.unFavorite = findViewById(R.id.removeAnnonceFavorite);

        this.proprietePrix = findViewById(R.id.proprietePrix);
        this.proprieteVille = findViewById(R.id.proprieteVille);
        this.proprieteDescription = findViewById(R.id.proprieteDescription);
        this.proprieteDatePublication = findViewById(R.id.proprieteDatePublication);
        this.vendeurNom = findViewById(R.id.vendeurNom);
        this.vendeurEmail = findViewById(R.id.vendeurEmail);
        this.vendeurTelephone = findViewById(R.id.vendeurTelephone);
        this.proprieteImage = findViewById(R.id.proprieteImage);

        this.caract = findViewById(R.id.caract);
        this.codepos = findViewById(R.id.codePos);
        this.nmbrep = findViewById(R.id.Nbpiece);


        this.photo=findViewById(R.id.photo);

        this.sliderLayout=findViewById(R.id.imageSlider);
    }



    // Mettre à jour la vue quand la donnée est prête
    private void updateUI() {
        if (this.item != null) {
            // Mettre à jour le titre du toolbar avec le titre de l'annonce
            this.setTitle(this.item.getTitre());

            this.proprietePrix.setText(this.item.getFormattedPrice());
            this.proprieteVille.setText(this.item.getVille());
            this.proprieteDescription.setText(this.item.getDescription());
            this.proprieteDatePublication.setText(this.item.getFormattedDate());

            //   this.caract.setText(this.); manque caracteristique
            this.nmbrep.setText(this.item.getFormattedPiece());
            this.codepos.setText(this.item.getFormattedPostal());

            Vendeur vendeur = this.item.getVendeur();
            if (!Objects.equals(vendeur, null)) {
                this.vendeurNom.setText(vendeur.getNom());
                this.vendeurEmail.setText(vendeur.getEmail());
                this.vendeurTelephone.setText(vendeur.getTelephone());
            }

            // Affiche l'image de l'annonce
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.centerCrop();

            for (int i = 0; i < this.item.getImages().size(); i++) {
                DefaultSliderView sliderView = new DefaultSliderView(this) {
                    @Override
                    public View getView() {
                        return null;
                    }
                };
                // if you want show image only / without description text use DefaultSliderView instead

                // initialize SliderLayout
                sliderView
                        .image(item.getImages().get(i))
                        .setRequestOption(requestOptions)
                        .setOnSliderClickListener(this);

                //add your extra information
                sliderView.bundle(new Bundle());
                sliderLayout.addSlider(sliderView);

            }

            sliderLayout.setPresetTransformer(SliderLayout.Transformer.Default);
            sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);

            sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
            sliderLayout.setCustomAnimation(new DescriptionAnimation());
            sliderLayout.setDuration(4000);
            sliderLayout.addOnPageChangeListener(this);

            // Cacher le panel de loading*/
            this.loadingPanel.setVisibility(View.GONE);
        }
    }

    // Effectue l'appel API, parse le JSON et créé l'objet Java correspondant
    private void makeApiCall() {
        AnnonceService service = retrofit.create(AnnonceService.class);

        // Api call
        Call<AnnonceApiResponse> apiResponseCall = service.getAnnonce();

        apiResponseCall.enqueue(new Callback<AnnonceApiResponse>() {
            @Override
            public void onResponse(Call<AnnonceApiResponse> call, Response<AnnonceApiResponse> response) {
                // On reçoit la reponse ici, elle contient, le "success" et la "response" en soi
                AnnonceApiResponse apiResponse = response.body();
                if (apiResponse != null && apiResponse.isSuccess()) {
                    item = apiResponse.getAnnonce();
                    checkAnnonce();
                }
            }

            @Override
            public void onFailure(Call<AnnonceApiResponse> call, Throwable t) {
                // L'appel a échoué, ou le parsing n'a pas marché.
                // Dans le cas où "response" vaut probablement autre chose qu'un objet
            }
        });
    }

    private void fetchData() {
        Bundle data = getIntent().getExtras();

        // Si une donnée a été passée grace à l'intent, la prendre
        if (data != null) {
            this.item = data.getParcelable(ANNONCE_TRAVELLER_KEY);
            this.checkAnnonce();
        } else {
            // Sinon, faire un appel API pour prendre une donnée au hasard
            this.makeApiCall();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            // Quand l'utilisateur clique sur home, terminer l'activité
            supportFinishAfterTransition();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPostExecute(String method, List<Annonce> reponse) {
        if (reponse.isEmpty()) { // L'annonce n'existe pas dans la base de données locale
            this.favorite.setVisibility(View.VISIBLE);
            this.unFavorite.setVisibility(View.GONE);
        } else {
            this.favorite.setVisibility(View.GONE);
            this.unFavorite.setVisibility(View.VISIBLE);
        }

        this.updateUI();

        Toast.makeText(this, "J'ai fini", Toast.LENGTH_SHORT).show();
    }

    public void saveAnnonce(View view) {
        new AgentAsyncTask(this, this.database, this.item).prepareInsertion().execute();
    }

    private void checkAnnonce() {
        new AgentAsyncTask(this, this.database, this.item).prepareCheck().execute();
    }

    public void removeAnnonce(View view) {
        final AsyncTaskRunner master = this;

        new AlertDialog.Builder(this)
                .setTitle("Confirmer")
                .setMessage("Retirer cette annonce des favoris ?")
                .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new AgentAsyncTask(master, database, item).prepareDeletion().execute();
                    }

                })
                .setNegativeButton("Non", null)
                .show();
    }

    // pour le clique sur camera

    private void openCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        //Camera intent
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE);
    }

    //handling permission result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //cette méthode est appelée lorsque l'utilisateur appuie sur Autoriser ou Refuser à partir de la fenêtre de demande de permission
        switch (requestCode){
            case PERMISSION_CODE:{
                if (grantResults.length > 0 && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED){
                    //l'autorisation de popup a été accordée
                    openCamera();
                }
                else {
                    //l'autorisation de popup a été refusée
                    Toast.makeText(this, "Permission refusée...", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    // camera

    public void camera(){
        //voir si cette methode peut etre autre part
        this.photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if system os is >= marshmallow, request runtime permission
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if (checkSelfPermission(Manifest.permission.CAMERA) ==
                            PackageManager.PERMISSION_DENIED ||
                            checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                                    PackageManager.PERMISSION_DENIED){
                        //permission not enabled, request it
                        String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        //show popup to request permissions
                        requestPermissions(permission, PERMISSION_CODE);
                    }
                    else {
                        //permission already granted
                        openCamera();
                    }
                }
                else {
                    //system os < marshmallow
                    openCamera();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //appelé quand l'image a été capturée par la caméra

        if (resultCode == RESULT_OK){
            //set the image captured to our ImageView
            proprieteImage.setImageURI(image_uri);
        }
    }


    @Override
    public void onSliderClick(BaseSliderView slider) {
        Toast.makeText(this, slider.getBundle().get("extra")+"", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
