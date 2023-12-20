package com.example.trabalhoandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trabalhoandroid.Client.Client;
import com.example.trabalhoandroid.Client.RestService;
import com.example.trabalhoandroid.Model.Deputado;
import com.example.trabalhoandroid.Model.Partido;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailDeputadosActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ImageView button, foto_perfil;
    TextView nome, sigla, cpf, dataNascimento;
    String foto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_deputados);

        BottomNavigationView navigationView = findViewById(R.id.bottomNavigationViewDeputadosDetail);
        Menu menu = navigationView.getMenu();
        MenuItem menuItemHome = menu.findItem(R.id.home_footer);
        MenuItem menuItemProfile = menu.findItem(R.id.profile_footer);
        MenuItem menuItemSettings = menu.findItem(R.id.settings);
        menuItemHome.setOnMenuItemClickListener(this::onClickGoToHome);
        menuItemProfile.setOnMenuItemClickListener(this::onClickGoToProfile);
        menuItemSettings.setOnMenuItemClickListener(this::onClickGoToSettings);
        button = findViewById(R.id.go_back);
        nome = findViewById(R.id.textView3);
        sigla = findViewById(R.id.textView4);
        cpf = findViewById(R.id.textView5);
        dataNascimento = findViewById(R.id.textView6);
        foto_perfil = findViewById(R.id.imageView4);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailDeputadosActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        detailDeputados();
    }

    private void detailDeputados() {
        int id = getIntent().getIntExtra("DeputadoId", 0);

        RestService restService = Client.criarApiService();
        Call<ResponseBody> call = restService.detalharDeputado(id);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    try{
                        String responseData = response.body().string();

                        Deputado deputado = parseJson(responseData);

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private Deputado parseJson(String responseData) {
        try{
            JSONObject json = new JSONObject(responseData);
            JSONObject dadosObject = json.getJSONObject("dados");

           // sigla.setText("SIGLA PARTIDO: " + dadosObject.getString("sigla"));
            nome.setText("NOME: " + dadosObject.getString("nomeCivil"));
            cpf.setText("CPF: "+ dadosObject.getString("cpf"));
            dataNascimento.setText("DATA NASCIMENTO: "+ dadosObject.getString("dataNascimento"));
            JSONObject ultimoStatus = dadosObject.getJSONObject("ultimoStatus");
            foto = ultimoStatus.getString("urlFoto");
            Picasso.get().load(foto).into(foto_perfil);
            // foto = dadosObject.getString("urlFoto");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public boolean onClickGoToHome(@NonNull MenuItem item) {
        Intent intent = new Intent(DetailDeputadosActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
        return true;
    }

    private boolean onClickGoToProfile(MenuItem menuItem) {
        Intent intent = new Intent(DetailDeputadosActivity.this, DeputadosActivity.class);
        startActivity(intent);
        finish();
        return true;
    }
    private boolean onClickGoToSettings(MenuItem menuItem) {
        Intent intent = new Intent(DetailDeputadosActivity.this, ConfigActivity.class);
        startActivity(intent);
        finish();
        return true;
    }
}