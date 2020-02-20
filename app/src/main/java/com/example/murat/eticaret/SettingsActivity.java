package com.example.murat.eticaret;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.murat.eticaret.Prevalent.Prevalent;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {

    private CircleImageView profileImageView;
    private EditText edTxt_adSoyad,edTxt_telefon,edTxt_adres;
    private TextView profilResmiDegistir,kaydet,guncelle;

    private Uri imageUri;
    private String myUrl="";
    private StorageTask uploadTask;
    private StorageReference  storageProfilePictureRef;
    private String checker="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        storageProfilePictureRef= FirebaseStorage.getInstance().getReference().child("Profil Resimleri");

        profileImageView=(CircleImageView)findViewById(R.id.settings_profile_image);
        edTxt_adSoyad=(EditText)findViewById(R.id.settings_adSoyad);
        edTxt_telefon=(EditText)findViewById(R.id.settings_telefonNo);
        edTxt_adres=(EditText)findViewById(R.id.settings_adres);
        profilResmiDegistir=(TextView)findViewById(R.id.profile_degistir_image);
        kaydet=(TextView)findViewById(R.id.kapat_settings);
        guncelle=(TextView)findViewById(R.id.guncelle_settings);

        kullaniciBilgisiGoruntule(profileImageView,edTxt_adSoyad,edTxt_telefon,edTxt_adres );

        kaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        guncelle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checker.equals("clicked"))
                {
                    userInfoSaved();
                }
                else
                {
                    updateOnlyUserInfo();
                }
            }
        });

        profilResmiDegistir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checker="clicked";

                CropImage.activity(imageUri)
                        .setAspectRatio(1, 1)
                        .start(SettingsActivity.this);
            }
        });
    }

    private void userInfoSaved()
    {
        if(TextUtils.isEmpty(edTxt_adSoyad.getText().toString()))
        {
            Toast.makeText(this,"Adınızı girmelisiniz",Toast.LENGTH_SHORT).show();

        }
        else if (TextUtils.isEmpty(edTxt_adres.getText().toString()))
        {
            Toast.makeText(this,"Adresinizi girmelisiniz",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(edTxt_telefon.getText().toString()))
        {
            Toast.makeText(this,"Numaranızı girmelisiniz",Toast.LENGTH_SHORT).show();
        }
        else if(checker.equals("clicked"))
        {
            uploadImage();
        }
    }

    private void uploadImage() {
        if(imageUri !=null)
        {
            final StorageReference fileRef=storageProfilePictureRef
                    .child(Prevalent.currentOnlineUser.getTelefon()+".jpg");

            uploadTask=fileRef.putFile(imageUri);

            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {

                    if(!task.isSuccessful())
                    {
                        throw task.getException();
                    }
                    return fileRef.getDownloadUrl();
                }
            })
            .addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {

                    if(task.isSuccessful())
                    {
                        Uri downloadUrl=task.getResult();
                        myUrl=downloadUrl.toString();
                        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Users");

                        HashMap<String ,Object> userMap=new HashMap<>();
                        userMap.put("ad",edTxt_adSoyad.getText().toString());
                        userMap.put("adres",edTxt_adres.getText().toString());
                        userMap.put("telefonSipariş",edTxt_telefon.getText().toString());
                        userMap.put("image",myUrl);
                        ref.child(Prevalent.currentOnlineUser.getTelefon()).updateChildren(userMap);

                        startActivity(new Intent(SettingsActivity.this,HomeActivity.class));
                        Toast.makeText(SettingsActivity.this,"Profil bilgileriniz başarı ile güncellendi",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else
                    {
                        Toast.makeText(SettingsActivity.this, "Hata.", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
        else
        {
            Toast.makeText(this, "Resim seçili değil", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateOnlyUserInfo() {
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Users");
        HashMap<String , Object>userMap=new HashMap<>();
        userMap.put("ad",edTxt_adSoyad.getText().toString());
        userMap.put("adres",edTxt_adres.getText().toString());
        userMap.put("telefonSiparis",edTxt_telefon.getText().toString());
        ref.child(Prevalent.currentOnlineUser.getTelefon()).updateChildren(userMap);

        startActivity(new Intent(SettingsActivity.this,HomeActivity.class));
        Toast.makeText(SettingsActivity.this,"Profil bilgileriniz başarı ile güncellendi...",Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE  &&  resultCode==RESULT_OK  &&  data!=null)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();

            profileImageView.setImageURI(imageUri);
        }
        else
        {
            Toast.makeText(this, "Error, Lütfen yeniden deneyiniz.", Toast.LENGTH_SHORT).show();

            startActivity(new Intent(SettingsActivity.this, SettingsActivity.class));
            finish();
        }
    }

    private void kullaniciBilgisiGoruntule(final CircleImageView profileImageView, final EditText edTxt_adSoyad, final EditText edTxt_telefon, final EditText edTxt_adres) {
        DatabaseReference kullaniciRef= FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUser.getTelefon());
        kullaniciRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    if(dataSnapshot.child("image").exists())
                    {
                        String image=dataSnapshot.child("image").getValue().toString();
                        String ad=dataSnapshot.child("ad").getValue().toString();
                        String adres=dataSnapshot.child("adres").getValue().toString();
                        String telefon=dataSnapshot.child("telefon").getValue().toString();

                        Picasso.get().load(image).into(profileImageView);
                        edTxt_adSoyad.setText(ad);
                        edTxt_telefon.setText(telefon);
                        edTxt_adres.setText(adres);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}
