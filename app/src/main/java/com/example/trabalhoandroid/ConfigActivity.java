package com.example.trabalhoandroid;

import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class ConfigActivity extends AppCompatActivity {

    AppCompatButton excluir_conta;
    TextView button_sair_settings;
    FirebaseUser user;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        BottomNavigationView navigationView = findViewById(R.id.bottomNavigationViewConfig);
        Menu menu = navigationView.getMenu();
        MenuItem menuItemHome = menu.findItem(R.id.home_footer);
        MenuItem menuItemProfile = menu.findItem(R.id.profile_footer);
        MenuItem menuItemSettings = menu.findItem(R.id.settings);
        menuItemHome.setOnMenuItemClickListener(this::onClickGoToHome);
        menuItemProfile.setOnMenuItemClickListener(this::onClickGoToProfile);
        menuItemSettings.setOnMenuItemClickListener(this::onClickGoToSettings);
        excluir_conta = findViewById(R.id.button_excluir_conta_settings);
        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        button_sair_settings = findViewById(R.id.button_sair_settings);


        excluir_conta.setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View view) {

                                                 AlertDialog.Builder aa = new AlertDialog.Builder(ConfigActivity.this);
                                                 aa.setTitle("Tem certeza?");
                                                 aa.setMessage("Deletar conta não pode ser desfeito");
                                                 aa.setPositiveButton("Deletar", new DialogInterface.OnClickListener() {
                                                     @Override
                                                     public void onClick(DialogInterface dialogInterface, int i) {
                                                         Intent ia = new Intent(ConfigActivity.this, LoginAndroidActivity.class);
                                                         startActivity(ia);
                                                         Toast.makeText(ConfigActivity.this, "Conta deletada com sucesso", Toast.LENGTH_SHORT).show();
                                                         user.delete();
                                                         FirebaseDatabase.getInstance().getReference().child("Users").child(uid).removeValue();
                                                         FirebaseAuth.getInstance().signOut();
                                                     }
                                                 });

                                                 aa.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                                     @Override
                                                     public void onClick(DialogInterface dialogInterface, int i) {
                                                                Toast.makeText(ConfigActivity.this, "Cancelado", Toast.LENGTH_SHORT).show();
                                                     }
                                                 });

                                                 aa.show();

                                             }
                                         });

        button_sair_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder aa = new AlertDialog.Builder(ConfigActivity.this);
                aa.setTitle("Tem certeza que deseja sair?");

                aa.setPositiveButton("Sair", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent ia = new Intent(ConfigActivity.this, LoginAndroidActivity.class);
                        startActivity(ia);
                        Toast.makeText(ConfigActivity.this, "Logout realizado com sucesso", Toast.LENGTH_SHORT).show();
                       FirebaseAuth.getInstance().signOut();
                    }
                });

                aa.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                aa.show();


            }
        });


    }

    public boolean onClickGoToHome(@NonNull MenuItem item) {
        Intent intent = new Intent(ConfigActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
        return true;
    }

    private boolean onClickGoToProfile(MenuItem menuItem) {
        Intent intent = new Intent(ConfigActivity.this, DeputadosActivity.class);
        startActivity(intent);
        finish();
        return true;
    }
    private boolean onClickGoToSettings(MenuItem menuItem) {
        Toast.makeText(ConfigActivity.this, "Você já está nas Configurações", Toast.LENGTH_SHORT).show();
        return true;
    }
}