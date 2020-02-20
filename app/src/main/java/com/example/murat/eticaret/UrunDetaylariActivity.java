package com.example.murat.eticaret;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.murat.eticaret.Model.Urunler;
import com.example.murat.eticaret.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class UrunDetaylariActivity extends AppCompatActivity {

    private Button arti_btn,eksi_btn;
    private TextView urun_sayisi;
    private Button sepete_ekle_btn;

    private ImageView urunResmi;
    private TextView urunFiyati,urunAdi,urunTanimi;
    private int sayi=1;
    private String urunID="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_urun_detaylari);

        /*arti_btn=(Button)findViewById(R.id.arti_btn);
        eksi_btn=(Button)findViewById(R.id.eksi_btn);
        integer_sayi=(TextView)findViewById(R.id.integer_sayi);*/


        urunResmi=(ImageView)findViewById(R.id.urun_resim_detaylari);
        urunAdi=(TextView)findViewById(R.id.urun_adi_detaylari);
        urunFiyati=(TextView)findViewById(R.id.urun_fiyat_detaylari);
        urunTanimi=(TextView)findViewById(R.id.urun_tanim_detaylari);
        urun_sayisi=(TextView)findViewById(R.id.urun_sayisi);

        sepete_ekle_btn=(Button)findViewById(R.id.sepete_ekle_btn);

        sepete_ekle_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sepeteUrunEkle();
            }
        });

        urunID=getIntent().getStringExtra("urunID");

        getUrunDetaylari(urunID);

    }

    private void sepeteUrunEkle()
    {
        String anlikTarihKaydet,anlikZamanKaydet;
        Calendar tarihGetir=Calendar.getInstance();

        SimpleDateFormat anlikTarih=new SimpleDateFormat("MMM dd,yyyy");
        anlikTarihKaydet=anlikTarih.format(tarihGetir.getTime());

        SimpleDateFormat anlikZaman=new SimpleDateFormat("HH:mm:ss a");
        anlikZamanKaydet=anlikZaman.format(tarihGetir.getTime());

        final DatabaseReference sepetRef= FirebaseDatabase.getInstance().getReference().child("Alisveris Sepeti");

        final HashMap<String , Object>sepetMap=new HashMap<>();
        sepetMap.put("urunID",urunID);
        sepetMap.put("urunAdi",urunAdi.getText().toString());
        sepetMap.put("urunFiyati",urunFiyati.getText().toString());
        sepetMap.put("date",anlikTarihKaydet);
        sepetMap.put("time",anlikZamanKaydet);
        sepetMap.put("miktar",urun_sayisi);
        sepetMap.put("indirim","");

        sepetRef.child("user view").child(Prevalent.currentOnlineUser.getTelefon())
                .child("Urunler")
                .child(urunID).updateChildren(sepetMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            sepetRef.child("Admin view").child(Prevalent.currentOnlineUser.getTelefon())
                                    .child("Urunler")
                                    .child(urunID).updateChildren(sepetMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful())
                                            {
                                                Toast.makeText(UrunDetaylariActivity.this, "Sepete Eklendi...", Toast.LENGTH_SHORT).show();
                                                Intent intent=new Intent(UrunDetaylariActivity.this,HomeActivity.class);
                                                startActivity(intent);
                                            }
                                        }
                                    });

                        }
                    }
                });

    }

    private void getUrunDetaylari(String urunID)
    {
        DatabaseReference urunRef= FirebaseDatabase.getInstance().getReference().child("Urunler");

        urunRef.child(urunID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    Urunler urunler=dataSnapshot.getValue(Urunler.class);
                    urunAdi.setText(urunler.getUrunAdi());
                    urunFiyati.setText(urunler.getUrunFiyati());
                    urunTanimi.setText(urunler.getUrunTanimi());

                    Picasso.get().load(urunler.getImage()).into(urunResmi);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void degerArtir(View view) {
        sayi = sayi + 1;
        goster(sayi);

    }public void degerAzalt(View view) {
        sayi = sayi - 1;
        goster(sayi);
    }

    private void goster(int number) {
        TextView degeriGoster = (TextView) findViewById(
                R.id.urun_sayisi);
        degeriGoster.setText("" + number);
    }
}
