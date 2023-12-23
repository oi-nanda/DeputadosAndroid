package com.example.trabalhoandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.trabalhoandroid.Adapter.PartidosAdapter;
import com.example.trabalhoandroid.Client.Client;
import com.example.trabalhoandroid.Model.Partido;
import com.example.trabalhoandroid.Client.RestService;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeActivity extends AppCompatActivity {


    PartidosAdapter partidosAdapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        BottomNavigationView navigationView = findViewById(R.id.bottomNavigationView);
        Menu menu = navigationView.getMenu();
        MenuItem menuItemHome = menu.findItem(R.id.home_footer);
        MenuItem menuItemProfile = menu.findItem(R.id.profile_footer);
        MenuItem menuItemSettings = menu.findItem(R.id.settings);
        menuItemHome.setOnMenuItemClickListener(this::onClickGoToHome);
        menuItemProfile.setOnMenuItemClickListener(this::onClickGoToProfile);
        menuItemSettings.setOnMenuItemClickListener(this::onClickGoToSettings);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        listarPartidos();
    }

    private void listarPartidos() {
        RestService restService = Client.criarApiService();
        Call<ResponseBody> call = restService.listarPartidos();

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();

                        List<Partido> partidos = parseJson(responseData);

                        setupRecyclerView(partidos);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }

    private List<Partido> parseJson(String responseData) {
        List<Partido> partidos = new ArrayList<>();

        try{
            JSONObject jsonObject = new JSONObject(responseData);
            JSONArray array = jsonObject.getJSONArray("dados");

            for (int i = 0; i < array.length(); i++) {
                JSONObject partidoJson = array.getJSONObject(i);

                int id = partidoJson.getInt("id");
                String sigla = partidoJson.getString("sigla");
                String nome = partidoJson.getString("nome");
                String urlLogo = partidoJson.getString("uri");

                Partido partido = new Partido(id, sigla, nome, urlLogo);
                partidos.add(partido);
            }

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return  partidos;
    }

    private void setupRecyclerView(List<Partido> partidos) {
        partidosAdapter = new PartidosAdapter(partidos, new PartidosAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Partido partido) {
                Intent intent = new Intent(HomeActivity.this, DetailPartidoActivity.class);
                intent.putExtra("PartidoId", partido.getId());
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(partidosAdapter);
    }


    public boolean onClickGoToHome(@NonNull MenuItem item) {
        Toast.makeText(HomeActivity.this, "Você já está na Home", Toast.LENGTH_SHORT).show();
        return true;
    }

    private boolean onClickGoToProfile(MenuItem menuItem) {
       Intent intent = new Intent(HomeActivity.this, DeputadosActivity.class);
       startActivity(intent);
       finish();
        return true;
    }
    private boolean onClickGoToSettings(MenuItem menuItem) {
        Intent intent = new Intent(HomeActivity.this, ConfigActivity.class);
        startActivity(intent);
        finish();
        return true;
    }



}