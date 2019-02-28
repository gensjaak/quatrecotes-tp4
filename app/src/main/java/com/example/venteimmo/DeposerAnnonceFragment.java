package com.example.venteimmo;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

import static android.content.Context.WINDOW_SERVICE;


public class DeposerAnnonceFragment extends AppMainFragment  {

    EditText titre, description, caracteristiques, code_postal, prix, nombre_de_pieces;
    String titr, desc;
    ArrayList<String> caract;
    ArrayList<Uri> imageAff ;
    int prx, code_post, nbr_de_pieces;
    private ImageButton favorite, unFavorite;
    private FloatingActionButton photo;

    private static final int PERMISSION_CODE = 931;
    private static final int IMAGE_CAPTURE_CODE = 1001;
    Uri image_uri;

    public DeposerAnnonceFragment() {
        this.imageAff = new ArrayList<Uri>();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_deposer_annonce, container, false);

        titre = view.findViewById(R.id.titre);
        description = view.findViewById(R.id.description);
        caracteristiques = view.findViewById(R.id.caracteristiques);
        code_postal = view.findViewById(R.id.code_postal);
        prix = view.findViewById(R.id.prix);
        nombre_de_pieces = view.findViewById(R.id.nombre_de_pieces);

        photo = view.findViewById(R.id.photo);

        camera();
        return view;
    }

    public void valider(View view) {

        if (!String.valueOf(titre.getText()).equals("")
                && !String.valueOf(description.getText()).equals("") && !String.valueOf(caracteristiques.getText()).equals("")
                && !String.valueOf(code_postal.getText()).equals("") && !String.valueOf(prix.getText()).equals("")
                && !String.valueOf(nombre_de_pieces.getText()).equals("")) {

            titr = String.valueOf(titre.getText());
            desc = String.valueOf(description.getText());

        }



    }


    /*debut*/


    // pour le clique sur camera

    private void openCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera");
        image_uri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
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
                    Toast.makeText(getActivity(), "Permission refusée...", Toast.LENGTH_SHORT).show();
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
                    if (getActivity().checkSelfPermission(Manifest.permission.CAMERA) ==
                            PackageManager.PERMISSION_DENIED ||
                            getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
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
    public void onResume() {
        super.onResume();

        this.udpateUI(image_uri);
    }

    private void udpateUI(Uri imageV) {
        int h =270;
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) getActivity().getSystemService(WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);
        int w = Math.round(dm.widthPixels / dm.density)*(50/100);

        imageAff.add(imageV);
        ImageView addphoto = new ImageView(getActivity());
        addphoto.setImageURI(imageV);
        LinearLayout.LayoutParams dim = new LinearLayout.LayoutParams(h,w);

       // addphoto.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        addphoto.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        // addphoto.setLayoutParams(dim);
        ((ViewGroup)getActivity().findViewById(R.id.imageAdd)).addView(addphoto);
    }



    /*fin*/

}
