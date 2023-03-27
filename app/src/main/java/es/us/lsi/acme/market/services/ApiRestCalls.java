package es.us.lsi.acme.market.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import es.us.lsi.acme.market.entities.Category;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiRestCalls {

    ApiRestAdapterInterface service;

    public ApiRestCalls(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://dataservice.accuweather.com/")
                .addConverterFactory(GsonConverterFactory.create()).build();

        service = retrofit.create(ApiRestAdapterInterface.class);

        service.getForecast("37,-6", "8CAr6WFuq2NGNNpQ6bnNJDuvueRnQuJp", "es-es").enqueue(new Callback<Forecast>() {
            @Override
            public void onResponse(Call<Forecast> call, Response<Forecast> response) {
                if (response.body() == null)
                    return;

                String frase = response.body().getSummary().getPhrase();
            }

            @Override
            public void onFailure(Call<Forecast> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void getCategoryById(String categoryId){
        service.getCategoryById(categoryId).enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                List<Category> categories = response.body();
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {

            }
        });
    }

    public void addCategory(Category category){
        service.createCategory("ID", category).enqueue(new Callback<Category>() {
            @Override
            public void onResponse(Call<Category> call, Response<Category> response) {

            }

            @Override
            public void onFailure(Call<Category> call, Throwable t) {

            }
        });
    }
}
