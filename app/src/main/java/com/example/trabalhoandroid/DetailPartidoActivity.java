
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.trabalhoandroid.Client.Client;
import com.example.trabalhoandroid.Client.RestService;
import com.example.trabalhoandroid.Model.Partido;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailPartidoActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView nome, sigla, numero, website;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_partido);

        BottomNavigationView navigationView = findViewById(R.id.bottomNavigationViewPartidoDetail);
        Menu menu = navigationView.getMenu();
        MenuItem menuItemHome = menu.findItem(R.id.home_footer);
        MenuItem menuItemProfile = menu.findItem(R.id.profile_footer);
        MenuItem menuItemSettings = menu.findItem(R.id.settings);
        menuItemHome.setOnMenuItemClickListener(this::onClickGoToHome);
        menuItemProfile.setOnMenuItemClickListener(this::onClickGoToProfile);
        menuItemSettings.setOnMenuItemClickListener(this::onClickGoToSettings);

        nome = findViewById(R.id.textView8);
        sigla = findViewById(R.id.textView9);
        numero = findViewById(R.id.textView10);
        website = findViewById(R.id.textView11);
        back = findViewById(R.id.back);

        detailPartido();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailPartidoActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void detailPartido() {
        int id = getIntent().getIntExtra("PartidoId", 0);

        RestService restService = Client.criarApiService();
        Call<ResponseBody> call = restService.detalharPartido(id);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    try{
                        String responseData = response.body().string();

                        Partido partido = parseJson(responseData);

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


    private Partido parseJson(String responseData) {
        try{
            JSONObject json = new JSONObject(responseData);
            JSONObject dadosObject = json.getJSONObject("dados");

            sigla.setText(dadosObject.getString("sigla"));
            nome.setText(dadosObject.getString("nome"));

            website.setText(dadosObject.getString("urlWebSite"));
            numero.setText(dadosObject.getString("numeroEleitoral"));

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public boolean onClickGoToHome(@NonNull MenuItem item) {
        Intent intent = new Intent(DetailPartidoActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
        return true;
    }

    private boolean onClickGoToProfile(MenuItem menuItem) {
        Intent intent = new Intent(DetailPartidoActivity.this, DeputadosActivity.class);
        startActivity(intent);
        finish();
        return true;
    }
    private boolean onClickGoToSettings(MenuItem menuItem) {
        Intent intent = new Intent(DetailPartidoActivity.this, ConfigActivity.class);
        startActivity(intent);
        finish();
        return true;
    }

}