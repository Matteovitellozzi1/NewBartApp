package com.univpm.bartapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Profilo extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference firebaseDatabase;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();

    private final static int ALL_PERMISSIONS_RESULT = 107;
    private final static int PICK_IMAGE = 200;

    Button btnDelete;
    ImageButton btnModificaNome;
    ImageButton btnModificaPassword;
    Button btnSalvaModifica;
    TextView textNome, nomeUtente, emailProfilo;
    ImageView proPic;
    Uri imagePath;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == PICK_IMAGE) {
            Bitmap bitmap = null;
            if (resultCode == RESULT_OK) {
                if (getPickImageResultUri(intent) != null) {
                    imagePath = getPickImageResultUri(intent);
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imagePath);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    bitmap = (Bitmap) intent.getExtras().get("data");
                }
            }

            Glide.with(this)
                    .load(bitmap)
                    .centerCrop()
                    .into(proPic);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilo);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Profilo");
        actionBar.setDisplayHomeAsUpEnabled(true);

        textNome = findViewById(R.id.text_nome);
        nomeUtente = (TextView) findViewById(R.id.nome_utente);
        emailProfilo = (TextView) findViewById(R.id.emailProfilo);
        proPic = findViewById(R.id.propic);
        proPic.setClipToOutline(true);
        btnDelete = (Button) findViewById(R.id.btn_eliminaAccount);
        btnModificaNome = (ImageButton) findViewById(R.id.edit1);
        btnModificaPassword = (ImageButton) findViewById(R.id.edit2);
        btnSalvaModifica = (Button) findViewById(R.id.btn_modifica_immagine);
        btnDelete.setOnClickListener(this);
        btnModificaNome.setOnClickListener(this);
        btnModificaPassword.setOnClickListener(this);

        btnSalvaModifica.setVisibility(View.INVISIBLE);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        textNome.setText(currentUser.getDisplayName());
        nomeUtente.setText(currentUser.getDisplayName());
        emailProfilo.setText(currentUser.getEmail());

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        //carica l'immagine del profilo se esiste effettivamente nello storage di firebase
        //Picasso è una libreria che abbiamo trovato in Internet che ci ha aiutato nel caricaemento dell'immagine.
        if (storageReference.child("Image").child("Profile Pic").child(mAuth.getUid()).getDownloadUrl() != null) {
            storageReference.child("Image").child("Profile Pic").child(mAuth.getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.get().load(uri).fit().centerCrop().into(proPic);
                }
            });
        }

        //Listener che si attiva quando viene cliccata l'immagine in maniera tale che venga prima attivata la richiesta dei permessi
        //all'utente, il quale, se conferma, potrà inserire l'immagine del profilo nell'apposita ImageView
        proPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                permissions.add(Manifest.permission.CAMERA);
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
                permissionsToRequest = findUnaskedPermissions(permissions);
                if(permissionsToRequest.size() > 0) {
                    requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
                } else {
                    startActivityForResult(getPickImageChooserIntent(), PICK_IMAGE);
                }
                btnSalvaModifica.setVisibility(View.VISIBLE);
                btnSalvaModifica.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        StorageReference imageReference = storageReference.child("Image").child("Profile Pic").child(mAuth.getUid());
                        if (imagePath != null) {
                            sendUserData(); // metodo per caricare l'immagine nello storage di Firebase
                        }
                        else{
                            Toast.makeText(Profilo.this, "Non hai inserito alcuna immagine!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }

    private Uri getPickImageResultUri(Intent data) {
        boolean isCamera = true;
        if (data != null) {
            String action = data.getAction();
            isCamera = action != null && action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
        }

        return isCamera ? getCaptureImageOutputUri() : data.getData();
    }

    private Intent getPickImageChooserIntent() {

        Uri outputFileUri = getCaptureImageOutputUri();

        List<Intent> allIntents = new ArrayList<>();
        PackageManager packageManager = this.getPackageManager();

        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for(ResolveInfo res : listCam) {
            Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            if(outputFileUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            }
            allIntents.add(intent);
        }

        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        List<ResolveInfo> listGallery = packageManager.queryIntentActivities(galleryIntent, 0);
        for(ResolveInfo res : listGallery) {
            Intent intent = new Intent(galleryIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            allIntents.add(intent);
        }

        Intent mainIntent = allIntents.get(allIntents.size()-1);
        for(Intent intent : allIntents) {
            if(intent.getComponent().getClassName().equals("com.android.documentsui.DocumentsActivity")) {
                mainIntent = intent;
                break;
            }
        }
        allIntents.remove(mainIntent);

        Intent chooserIntent = Intent.createChooser(mainIntent, getString(R.string.selsorgente));

        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toArray(new Parcelable[allIntents.size()]));

        return chooserIntent;
    }

    public Uri getCaptureImageOutputUri() {
        Uri outputFileUri = null;
        File getImage = this.getExternalCacheDir();
        if(getImage != null) {
            outputFileUri = Uri.fromFile(new File(getImage.getPath(), "propic.png"));
        }
        return outputFileUri;
    }

    private ArrayList findUnaskedPermissions(ArrayList<String> wanted) {
        ArrayList<String> result = new ArrayList<>();

        for(String perm : wanted) {
            if(!(this.checkSelfPermission(perm) == PackageManager.PERMISSION_GRANTED)) {
                result.add(perm);
            }
        }

        return result;
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if(requestCode == ALL_PERMISSIONS_RESULT) {
            for(String perm: permissionsToRequest) {
                if(!(this.checkSelfPermission(perm)==PackageManager.PERMISSION_GRANTED)) {
                    permissionsRejected.add(perm);
                }
            }
            if(permissionsRejected.size() > 0) {
                if(shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                    Toast.makeText(this,"Approva tutto", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                startActivityForResult(getPickImageChooserIntent(), PICK_IMAGE);
            }
        }
    }

    private void sendUserData() { //carico l'immagine nello storage di Firebase
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference(mAuth.getUid());
        //Scelgo il percorso adatto per caricare l'immagine nello storage
        StorageReference imageReference = storageReference.child("Image").child("Profile Pic").child(mAuth.getUid()); //User id/Images/Profile Pic.jpg
        UploadTask uploadTask = imageReference.putFile(imagePath); //caricamento dell'immagine nello storage
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Profilo.this, "Error: Uploading profile picture", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(Profilo.this, "Profile picture uploaded", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override // gestione dei vari click che vengono fatti a livello di modifica password email e elimina account
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_eliminaAccount:  eliminaAccount(v);
                break;
            case R.id.edit1: modificaNome(v);
                break;
            case R.id.edit2: modificaPassword(v);
                break;
        }
    }


    public void modificaNome(View v){ //parte l'intent per la modifica del nome
        Intent intent= new Intent(Profilo.this, ChangeUsername.class);
        startActivity(intent);
    }

    public void modificaPassword(View v){
        Intent intent= new Intent(Profilo.this, ChangePassword.class);
        startActivity(intent);
    } //parte l'intent per la modifica della password


    public void eliminaAccount(View view) { //Elimina l'account solo in caso di conferma ddel dialog dato che come procedura risulta essere 'delicata' quindi viene eseguita solo in caso di una eventuale conferma

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        AlertDialog.Builder dialog= new AlertDialog.Builder(Profilo.this);
        dialog.setTitle("Sei sicuro?");

        dialog.setMessage("Cancellare questo account comporterà la sua totale" +
                "rimozione e non potrai più accedere all'app");
        dialog.setPositiveButton("Elimina", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                final String idUser =currentUser.getUid();
                //Elimino l'immagine del profilo dallo storage
                StorageReference storageReference=FirebaseStorage.getInstance().getReference().child("Image").child("Profile Pic");
                storageReference.child(idUser).delete();
                //Elimino tutti gli scambi associati all'utente
                operazione1(idUser); //Elimina gli scambi dove risulta aver ricevuto l'offerta
                operazione2(idUser); //Elimina gli scambi dove risulta aver inviato l'offerta

                firebaseDatabase=FirebaseDatabase.getInstance().getReference().child("oggetti");
                Query query=firebaseDatabase.orderByChild("idUser").equalTo(idUser);

                //Elimina tutti gli oggetti sul catalogo dell'utente viene eliminato
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot postsnapshot :dataSnapshot.getChildren()) {
                            FirebaseStorage.getInstance().getReference().child("Image").child("ImmaginiOggetti").child(idUser).child(postsnapshot.child("nome").getValue().toString()).delete();
                            postsnapshot.getRef().removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                currentUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Profilo.this, "Account Eliminato", Toast.LENGTH_LONG).show();
                            Intent intent= new Intent(Profilo.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(Profilo.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        });
        dialog.setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog= dialog.create();
        alertDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem menuItem) {
        Intent intent = new Intent(this, HomeScreen.class);
        startActivity(intent);
        this.finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, HomeScreen.class);
        startActivity(intent);
        this.finish();
    }

    public void operazione1 (String idUser) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("scambi").whereEqualTo("idAcq", idUser)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        document.getReference().delete();
                    }
                }
            }
        });
    }

    public void operazione2 (String idUser) { //oggetto verde dell'altro
        FirebaseFirestore db0 = FirebaseFirestore.getInstance();
        db0.collection("scambi").whereEqualTo("idVend", idUser).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        document.getReference().delete();
                    }
                }
            }
        });

    }
}