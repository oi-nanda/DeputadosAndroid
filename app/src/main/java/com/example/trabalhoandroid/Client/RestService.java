package com.example.trabalhoandroid.Client;

import com.example.trabalhoandroid.Model.Deputado;
import com.example.trabalhoandroid.Model.Partido;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface RestService {

    @GET("partidos")
    Call<ResponseBody> listarPartidos();

    @GET("partidos/{id}")
    Call<ResponseBody> detalharPartido(@Path("id") int id);

    @GET("deputados")
    Call<ResponseBody> listarDeputados();

    @GET("deputados/{id}")
    Call<ResponseBody> detalharDeputado(@Path("id") int id);

}
