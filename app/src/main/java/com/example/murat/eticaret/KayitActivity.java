package com.example.murat.eticaret;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class KayitActivity extends AppCompatActivity {

    private Button kaydol;
    private EditText input_ad,input_telNo,input_sifre,input_sifreTkr;
    private ProgressBar progressBar;
    Handler handler;
    Runnable runnable;
    Timer timer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kayit);

        kaydol=(Button)findViewById(R.id.kayit_btn);
        input_ad=(EditText)findViewById(R.id.kayit_ad);
        input_telNo=(EditText)findViewById(R.id.kayit_telno);
        input_sifre=(EditText)findViewById(R.id.kayit_sifre);
        input_sifreTkr=(EditText)findViewById(R.id.kayit_tekarSifre);

        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);





        kaydol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HesapOlustur();
            }
        });
    }

    private void HesapOlustur() {
        //Editextlerden girilen degerler alınır
        String ad=input_ad.getText().toString();
        String telefon=input_telNo.getText().toString();
        String sifre=input_sifre.getText().toString();
        String sifre_tkr=input_sifreTkr.getText().toString();
        boolean sifreKontrol;

        //edittext alanları boş mu onu kontrol ediyor

        if(TextUtils.isEmpty(ad)){
            Toast.makeText(this,"Lütfen adınızı giriniz...",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(telefon)){
            Toast.makeText(this,"Lütfen telefon numaranızı giriniz...",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(sifre)){
            Toast.makeText(this,"Lütfen sifrenizi giriniz...",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(sifre_tkr)){
            Toast.makeText(this,"Lütfen şifrenizi tekrar giriniz...",Toast.LENGTH_LONG).show();

        }

        //ad, telefon sifre alanlari dogrulandıktan sonra progressbar göstersin
        else{
            progressBar();
            ValidatePhoneNumber(ad,telefon,sifre);

        }
}

    private void progressBar() {
        progressBar.setVisibility(View.VISIBLE);
        handler=new Handler();
        runnable=new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
                timer.cancel();

            }
        };
        timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(runnable);

            }
        },1000,1000);

    }

    private void ValidatePhoneNumber(final String ad, final String telefon, final String sifre) {
        final DatabaseReference rootRef;
        rootRef= FirebaseDatabase.getInstance().getReference();
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!(dataSnapshot.child("Users").child(telefon).exists()))
                {
                    HashMap<String,Object> userdataMap=new HashMap<>();
                    userdataMap.put("telefon",telefon);
                    userdataMap.put("sifre", sifre);
                    userdataMap.put("ad",ad);

                    rootRef.child("Users").child(telefon).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(KayitActivity.this,"Tebrikler hesabınız oluşturuldu",Toast.LENGTH_SHORT).show();
                                        Intent intent=new Intent(KayitActivity.this,LoginActivity.class);
                                        startActivity(intent);
                                        progressBar.setVisibility(View.INVISIBLE);
                                    }
                                    else
                                    {
                                        progressBar.setVisibility(View.INVISIBLE);
                                        Toast.makeText(KayitActivity.this,"Network Error: Lütfen yeniden deneyiniz",Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                }
                else
                {
                    Toast.makeText(KayitActivity.this,"Bu"+telefon+" numara zaten var",Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(KayitActivity.this,"lütfen başka bir numara deneyiniz...",Toast.LENGTH_SHORT).show();

                    Intent intent=new Intent(KayitActivity.this,MainActivity.class);
                    startActivity(intent);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

    }


}
