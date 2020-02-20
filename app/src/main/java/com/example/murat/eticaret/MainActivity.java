package com.example.murat.eticaret;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.murat.eticaret.Model.Users;
import com.example.murat.eticaret.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;


public class MainActivity extends AppCompatActivity {

    private Button btn_login,btn_join;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        btn_login=(Button)findViewById(R.id.login_btn);
        btn_join=(Button)findViewById(R.id.join_btn);

        Paper.init(this);
        //login butonunu tıkladığımda beni oturum ac sayfasina yonlendirecek
        btn_login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent=new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);


            }
        });
        // kayit ol sayfasına git
        btn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,KayitActivity.class);
                startActivity(intent);
            }
        });

        String userTelefonKey=Paper.book().read(Prevalent.userTelefonKey);
        String userSifreKey=Paper.book().read(Prevalent.userSifreKey);

        if(userSifreKey !="" && userTelefonKey!=""){
            if(!TextUtils.isEmpty(userSifreKey) && !TextUtils.isEmpty(userTelefonKey)){
                Kaydol(userTelefonKey,userSifreKey);
            }
        }
    }

    private void Kaydol(final String telefon, final String sifre) {

        final DatabaseReference rootRef;//veri tabanı baglantisi yapiliyor
        rootRef= FirebaseDatabase.getInstance().getReference();

        rootRef.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.child("Users").child(telefon).exists())
                {
                    Users usersData = dataSnapshot.child("Users").child(telefon).getValue(Users.class);


                    if (usersData.getTelefon().equals(telefon))
                    {
                        if(usersData.getSifre().equals(sifre))
                        {
                            Toast.makeText(MainActivity.this,"Lütfen bekleyiniz, zaten oturum açık",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                            Prevalent.currentOnlineUser = usersData;
                            startActivity(intent);
                        }
                        else
                        {
                            Toast.makeText(MainActivity.this, "Hey kara,Yanlış Şifre", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else
                {
                    Toast.makeText(MainActivity.this, telefon + " numaralı hesap bulunamadı", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
