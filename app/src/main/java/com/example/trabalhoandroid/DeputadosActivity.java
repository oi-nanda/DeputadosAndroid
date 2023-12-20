package com.example.trabalhoandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.trabalhoandroid.Adapter.DeputadosAdapter;
import com.example.trabalhoandroid.Adapter.PartidosAdapter;
import com.example.trabalhoandroid.Client.Client;
import com.example.trabalhoandroid.Client.RestService;
import com.example.trabalhoandroid.Model.Deputado;
import com.example.trabalhoandroid.Model.Partido;
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

public class DeputadosActivity extends AppCompatActivity {

    DeputadosAdapter deputadosAdapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deputados);

        BottomNavigationView navigationView = findViewById(R.id.bottomNavigationViewDeputados);
        Menu menu = navigationView.getMenu();
        MenuItem menuItemHome = menu.findItem(R.id.home_footer);
        MenuItem menuItemProfile = menu.findItem(R.id.profile_footer);
        MenuItem menuItemSettings = menu.findItem(R.id.settings);
        menuItemHome.setOnMenuItemClickListener(this::onClickGoToHome);
        menuItemProfile.setOnMenuItemClickListener(this::onClickGoToProfile);
        menuItemSettings.setOnMenuItemClickListener(this::onClickGoToSettings);

        recyclerView = findViewById(R.id.recycleViewDeputados);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        listarDeputados();
    }

    private void listarDeputados() {
        RestService restService = Client.criarApiService();
        Call<ResponseBody> call = restService.listarDeputados();

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();

                        List<Deputado> deputados = parseJson(responseData);

                       setupRecyclerView(deputados);
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



    private List<Deputado> parseJson(String responseData) {
        List<Deputado> deputados = new ArrayList<>();

        try{
            JSONObject jsonObject = new JSONObject(responseData);
            JSONArray array = jsonObject.getJSONArray("dados");

            for (int i = 0; i < array.length(); i++) {
                JSONObject deputadoJson = array.getJSONObject(i);

                int id = deputadoJson.getInt("id");
                String nome = deputadoJson.getString("nome");
                String siglaPartido = deputadoJson.getString("siglaPartido");
                String urlFoto = deputadoJson.getString("urlFoto");

                Deputado deputado = new Deputado(id, nome, siglaPartido, urlFoto);
                deputados.add(deputado);
            }

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return  deputados;
    }

    private void setupRecyclerView(List<Deputado> deputados) {
        deputadosAdapter = new DeputadosAdapter(deputados, new DeputadosAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Deputado deputado) {
                Intent intent = new Intent(DeputadosActivity.this, DetailDeputadosActivity.class);
                intent.putExtra("DeputadoId", deputado.getId());
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(deputadosAdapter);
    }

    public boolean onClickGoToHome(@NonNull MenuItem item) {
        Intent intent = new Intent(DeputadosActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
        return true;
    }

    private boolean onClickGoToProfile(MenuItem menuItem) {
        Toast.makeText(DeputadosActivity.this, "Você já está na página dos Deputados", Toast.LENGTH_SHORT).show();
        return true;
    }
    private boolean onClickGoToSettings(MenuItem menuItem) {
        Intent intent = new Intent(DeputadosActivity.this, ConfigActivity.class);
        startActivity(intent);
        finish();
        return true;
    }
}