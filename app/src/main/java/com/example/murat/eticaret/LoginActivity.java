package com.example.murat.eticaret;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
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

import static com.example.murat.eticaret.R.layout;



public class LoginActivity extends AppCompatActivity {
    private EditText input_telNo,input_sifre;
    private Button login_btn;
    private TextView admin,notAdmin;

    private  String parentDbAd="Users";
    private CheckBox chck_beniHatirla;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_login);



        input_telNo=(EditText) findViewById(R.id.login_telno);
        input_sifre=(EditText) findViewById(R.id.login_sifre);
        login_btn=(Button) findViewById(R.id.login_btn);
        admin=(TextView) findViewById(R.id.admin_link);
        notAdmin=(TextView) findViewById(R.id.No_admin_link);
        chck_beniHatirla=(CheckBox)findViewById(R.id.login_checkbox);


        Paper.init(this);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                LoginUser();
            }
        });

        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login_btn.setText("Oturum Aç Admin");
                admin.setVisibility(View.INVISIBLE);
                notAdmin.setVisibility(View.VISIBLE);
                parentDbAd="Admins";


            }
        });

        notAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login_btn.setText("Oturum Aç");
                admin.setVisibility(View.VISIBLE);
                notAdmin.setVisibility(View.INVISIBLE);
                parentDbAd="Users";
            }
        });
    }

    private void LoginUser(){



        String telefon=input_telNo.getText().toString();
        String sifre=input_sifre.getText().toString();

        if(TextUtils.isEmpty(telefon))
        {
            Toast.makeText(this,"Lütfen telefon numaranızı giriniz",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(sifre))
        {
            Toast.makeText(this,"Lütfen şifrenizi giriniz",Toast.LENGTH_SHORT).show();
        }
        else
        {
           KayitOl(sifre,telefon);
        }

    }

    private void KayitOl(final String sifre, final String telefon) {

        if(chck_beniHatirla.isChecked()){

            Paper.book().write(Prevalent.userTelefonKey,telefon);
            Paper.book().write(Prevalent.userSifreKey,sifre);

        }



        final DatabaseReference rootRef;//veri tabanı baglantisi yapiliyor
        rootRef= FirebaseDatabase.getInstance().getReference();

        rootRef.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.child(parentDbAd).child(telefon).exists())
                {
                    Users usersData = dataSnapshot.child(parentDbAd).child(telefon).getValue(Users.class);


                    if (usersData.getTelefon().equals(telefon))
                    {
                        if(usersData.getSifre().equals(sifre))
                        {
                            if (parentDbAd.equals("Admins")) {
                                Toast.makeText(LoginActivity.this, "Hoşgeldin Admin,başarı ile oturum açtınız... ", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, AdminKategoriActivity.class);
                                startActivity(intent);
                            } else if (parentDbAd.equals("Users")) {
                                Toast.makeText(LoginActivity.this, "Başarı ile oturum açtınız...", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                Prevalent.currentOnlineUser = usersData;
                                startActivity(intent);
                            }
                        }
                        else
                        {
                            Toast.makeText(LoginActivity.this, "Yanlış Şifre", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else
                {
                    Toast.makeText(LoginActivity.this, telefon + " numaralı hesap bulunamadı", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
