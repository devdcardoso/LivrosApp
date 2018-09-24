package br.com.opet.tds.firebasestorage;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;

import me.iwf.photopicker.PhotoPicker;

public class MainActivity extends Activity {

    private ImageView imgSelected;
    private StorageReference mStorageRef;
    private ArrayList<String> photos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        photos = new ArrayList<>();
        imgSelected = findViewById(R.id.imgSelected);
        mStorageRef = FirebaseStorage.getInstance().getReference();
    }

    public void photoPickerFunction(View view){
        PhotoPicker.builder()
                .setPhotoCount(1)
                .setShowCamera(true)
                .setShowGif(true)
                .setPreviewEnabled(false)
                .start(this, PhotoPicker.REQUEST_CODE);
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == PhotoPicker.REQUEST_CODE) {
            if (data != null) {
                photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                imgSelected.setImageURI(Uri.parse(photos.get(0)));
            }
        }
    }

    private void resetForm(){
        photos.clear();
        imgSelected.setImageResource(0);
    }

    public void sendPhotoFunction(View view) {
        if(photos.size() > 0){
            Uri file = Uri.fromFile(new File(photos.get(0)));
            StorageReference photoRef = mStorageRef.child("images");
            photoRef.putFile(file).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(MainActivity.this, "Arquivo Enviado com sucesso!", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainActivity.this, "Falha ao enviar arquivo.", Toast.LENGTH_SHORT).show();
                }
            });
            resetForm();
        }else{
            Toast.makeText(this, "Nenhum arquivo carregado.", Toast.LENGTH_SHORT).show();
        }
    }
}
