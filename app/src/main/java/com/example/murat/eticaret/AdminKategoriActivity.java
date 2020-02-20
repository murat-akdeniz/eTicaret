package com.example.murat.eticaret;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class AdminKategoriActivity extends AppCompatActivity {

    private ImageView t_shirt, spor_tshirt, kadin_elbise, sweater;
    private ImageView gozluk, sapka, el_canta, ayakkabi;
    private ImageView kulaklik, laptop, saat,cep_telefonu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_kategori);

        t_shirt=(ImageView)findViewById(R.id.t_shirt);
        spor_tshirt=(ImageView)findViewById(R.id.spor_tshirt);
        kadin_elbise=(ImageView)findViewById(R.id.kadin_elbise);
        sweater=(ImageView)findViewById(R.id.sweater);

        gozluk=(ImageView)findViewById(R.id.gozluk);
        sapka=(ImageView)findViewById(R.id.sapka);
        el_canta=(ImageView)findViewById(R.id.el_canta);
        ayakkabi=(ImageView)findViewById(R.id.ayyakabi);

        kulaklik=(ImageView)findViewById(R.id.kulaklik);
        laptop=(ImageView)findViewById(R.id.laptop);
        saat=(ImageView)findViewById(R.id.saat);
        cep_telefonu=(ImageView)findViewById(R.id.cep_telefonu);

        /*
        Intent sayfalar arası(activity) geçiş yapmamızı sağlar
        ilk değişken bulunduğumuz yer, ikincisi hedef sayfa
        intentler ile veri göndermek için putExtra metodu kullanılır.
        iki parametre alır
        birincisi "key" ikincisi "value"
        hedef activity de veri çekmek istersek buradaki "key" parametresini kullanıyoruz
         */

        t_shirt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminKategoriActivity.this
                        ,AdminYeniUrunEkleActivity.class);
                intent.putExtra("kategori","t-Shirt");
                startActivity(intent);
            }
        });
        spor_tshirt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminKategoriActivity.this
                        ,AdminYeniUrunEkleActivity.class);
                intent.putExtra("kategori","Spor t-Shirt");
                startActivity(intent);
            }
        });

        kadin_elbise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminKategoriActivity.this
                        ,AdminYeniUrunEkleActivity.class);
                intent.putExtra("kategori","Elbise");
                startActivity(intent);
            }
        });

        sapka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminKategoriActivity.this
                        ,AdminYeniUrunEkleActivity.class);
                intent.putExtra("kategori","Şapka");
                startActivity(intent);
            }
        });

    }
}
