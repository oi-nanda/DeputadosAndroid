package com.example.trabalhoandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CadastrandoActivity extends AppCompatActivity {

    ImageButton btn_back_home;
    Button button_cadastrar;
    FirebaseAuth mAuth;
    DatabaseReference reference;
    String nome, email, senha, confirmarSenha;
    EditText edt_senha, edt_email, edt_confirm_senha, edt_nome;
    FirebaseDatabase db;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar);

        btn_back_home = findViewById(R.id.btn_back_home);
        button_cadastrar = findViewById(R.id.button_cadastrar);
        mAuth = ConfigBD.FirebaseAutenticacao();
        edt_email = findViewById(R.id.edit_email);
        edt_senha = findViewById(R.id.edit_password);
        edt_confirm_senha = findViewById(R.id.edit_confirm_password);
        edt_nome = findViewById(R.id.edit_email2);
        progressDialog = new ProgressDialog(this);
        email = edt_email.getText().toString();
        senha = edt_senha.getText().toString();
        confirmarSenha = edt_confirm_senha.getText().toString();

        btn_back_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CadastrandoActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        button_cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nome = edt_nome.getText().toString();
                email = edt_email.getText().toString();
                senha = edt_senha.getText().toString();
                confirmarSenha = edt_confirm_senha.getText().toString();

                progressDialog.setMessage("Realizando cadastro de usuário...");
                progressDialog.setTitle("Cadastro");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();


                mAuth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                            Users users = new Users(nome, email, senha);
                            db = FirebaseDatabase.getInstance();
                            reference = db.getReference("Users");
                            String id = mAuth.getUid();
                            reference.child(id).setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    edt_nome.setText("");
                                    edt_email.setText("");
                                    edt_senha.setText("");
                                    edt_confirm_senha.setText("");
                                }
                            });
                            progressDialog.dismiss();
                            Intent intent = new Intent(CadastrandoActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                            Toast.makeText(CadastrandoActivity.this, "Usuário criado com sucesso", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }
}