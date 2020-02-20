package com.example.murat.eticaret;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminYeniUrunEkleActivity extends AppCompatActivity {

    private String kategoriAdi,urun_tanimi,urun_fiyati,urun_adi;
    private String suankiTarihiKaydet,suankiZamaniKaydet;
    private String rastgeleUrunAnahtari,downloadImageUrl;

    private Button yeniUrunEkle_btn;
    private ImageView urun_resmi;
    private EditText input_urun_adi,input_urun_tanimi,input_urun_fiyati;

    private static final int galeriSec=1;
    private Uri ImageUri;

    private StorageReference urunResmiRef;
    private DatabaseReference urunRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_admin_yeni_urun_ekle);

        /*
        adminyeniurunekleactivity de bulanan Button EditText ve Imageviewlar
        xml ile bağlantı kuruldu
         */
        yeniUrunEkle_btn=(Button)findViewById(R.id.yeni_urun_ekle_btn);
        urun_resmi=(ImageView)findViewById(R.id.urun_resmi_sec);
        input_urun_adi=(EditText)findViewById(R.id.urun_adi);
        input_urun_tanimi=(EditText)findViewById(R.id.urun_tanimi);
        input_urun_fiyati=(EditText)findViewById(R.id.urun_fiyati);

        /*
        adminkategori activitysindeki kategori resimlerine tıklandığında
        ait oluduğu kategori adını açılan activity de Toast metin olarak görüntüleyecek
         */
        kategoriAdi=getIntent().getExtras().get("kategori").toString();
        Toast.makeText(this,kategoriAdi,Toast.LENGTH_SHORT).show();



        /*
        FirebaseStorage ile bağlantı kuruldu
        FirabaseDatabase ile bağlantı kuruldu
         */
        urunResmiRef= FirebaseStorage.getInstance().getReference().child("Urun resimleri");
        urunRef= FirebaseDatabase.getInstance().getReference().child("Urunler");


        //urun_resmine tıklandında GaleriyeGit metodu çalışacak
        urun_resmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GaleriyeGit();
            }
        });

        //yeni ürün ekle butonuna tıklandığında UrunDogrulama metodu çalışacak
        yeniUrunEkle_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UrunBilgisiDogrulama();
            }
        });
    }

    /*
    ürün tanımı , ürün fiyatı ve ürün adı alanlarının kontrol etme
    kontrol edildikten sonra alanları boş değilse
    UrunBilgisiDepola() metodunu çağır
     */
    private void UrunBilgisiDogrulama() {

        urun_tanimi=input_urun_tanimi.getText().toString();
        urun_fiyati=input_urun_fiyati.getText().toString();
        urun_adi=input_urun_adi.getText().toString();

        if(ImageUri==null){
            Toast.makeText(this, "Ürün resmi seçmelisiniz...", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(urun_tanimi)){
            Toast.makeText(this, "Ürün tanımı yapmalısınız...", Toast.LENGTH_SHORT).show();
        }

        else if(TextUtils.isEmpty(urun_fiyati)){
            Toast.makeText(this, "Ürün fiyatı girmelisiniz...", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(urun_adi)){
            Toast.makeText(this, "Ürün Adı girmelisiniz...", Toast.LENGTH_SHORT).show();
        }
        else{
            UrunBilgisiDepola();
        }
    }

    private void UrunBilgisiDepola() {

        /*
        Tarih işlemleri için Calendar sınıfı tanımlandı
         */
        Calendar takvim=Calendar.getInstance();
        SimpleDateFormat suankiTarih=new SimpleDateFormat("MMM dd,yyyy");

        suankiTarihiKaydet=suankiTarih.format(takvim.getTime());

        SimpleDateFormat suankiZaman =new SimpleDateFormat("HH:mm:ss a");
        suankiZamaniKaydet=suankiZaman.format(takvim.getTime());

        rastgeleUrunAnahtari=suankiTarihiKaydet+" "+suankiZamaniKaydet;

        /*
        dosayyolu değişkenine resmin firebase de tutulduğu yere bugünün tarihi saatini verdik
        uploda task ile firebasestorage upload edeceğimiz dosayyı resmi yüklenme durumunu kontrol ediyoruz
         */

        final StorageReference dosyaYolu=urunResmiRef.child(ImageUri.getLastPathSegment()+rastgeleUrunAnahtari+".jpg");
        final UploadTask uploadTask=dosyaYolu.putFile(ImageUri);

        //yüklenme başarısız ise Error versin
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String mesaj=e.toString();
                Toast.makeText(AdminYeniUrunEkleActivity.this, "Error: "+mesaj, Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            //Başarılı ise
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {
                Toast.makeText(AdminYeniUrunEkleActivity.this, "Ürün resmi başarı ile yüklendi...", Toast.LENGTH_SHORT).show();

                Task<Uri> urlTask=uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>()
                {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
                    {
                       if(!task.isSuccessful())
                       {
                           throw task.getException();
                       }
                        downloadImageUrl=dosyaYolu.getDownloadUrl().toString();
                        return dosyaYolu.getDownloadUrl();

                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful())
                        {
                            downloadImageUrl=task.getResult().toString();
                            Toast.makeText(AdminYeniUrunEkleActivity.this, "Ürün resmi Url tamamlandı...", Toast.LENGTH_SHORT).show();
                            urunBilDBKaydet();
                        }
                    }
                });
            }
        });
    }

    private void urunBilDBKaydet() {

        HashMap<String,Object>urunMap=new HashMap<>();
        urunMap.put("urunID",rastgeleUrunAnahtari);
        urunMap.put("date",suankiTarihiKaydet);
        urunMap.put("time",suankiZamaniKaydet);
        urunMap.put("urunTanimi",urun_tanimi);
        urunMap.put("image",downloadImageUrl);
        urunMap.put("kategori",kategoriAdi);
        urunMap.put("urunFiyati",urun_fiyati);
        urunMap.put("urunAdi",urun_adi);

        urunRef.child(rastgeleUrunAnahtari).updateChildren(urunMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Intent intent=new Intent(AdminYeniUrunEkleActivity.this,AdminKategoriActivity.class);
                    startActivity(intent);

                    Toast.makeText(AdminYeniUrunEkleActivity.this, "Ürün başarıyla eklendi...", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String mesaj=task.getException().toString();
                    Toast.makeText(AdminYeniUrunEkleActivity.this, "Error: "+mesaj, Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    private void GaleriyeGit(){

        Intent galeri=new Intent();
        galeri.setAction(Intent.ACTION_GET_CONTENT);
        galeri.setType("image/*");
        startActivityForResult(galeri,galeriSec);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==galeriSec && resultCode==RESULT_OK && data!=null)
        {
            ImageUri=data.getData();
            urun_resmi.setImageURI(ImageUri);

        }
    }
}
