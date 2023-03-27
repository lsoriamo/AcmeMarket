package es.us.lsi.acme.market.services;

import java.util.List;

import es.us.lsi.acme.market.entities.Category;
import es.us.lsi.acme.market.entities.Item;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiRestAdapterInterface {
    @GET("forecasts/v1/minute")
    Call<Forecast> getForecast(@Query("q") String latlon, @Query("apikey") String apikey, @Query("language") String language);

    @GET("categories/{categoryId}")
    Call<List<Category>> getCategoryById(@Path("categoryId") String categoryId);

    @GET("categories")
    Call<List<Category>> getCategories();

    @POST("categories")
    Call<Category> createCategory(@Query("categoryId") String categoryId, @Body Category category);

    @DELETE("categories")
    Call<ResponseBody> deleteCategory(@Query("categoryId") String categoryId);

    @GET("items")
    Call<List<Item>> getItems();

    @GET("items")
    Call<List<Item>> getItemByCategory(@Query("categoryId") String categoryId);

    @GET("items")
    Call<List<Item>> getItemByCategorySorted(@Query("categoryId") String categoryId, @Query("sortedBy") String sortedBy, @Query("reverse") String reverse);

    @GET("items")
    Call<List<Item>> getItemByName(@Query("itemName") String itemName);
}
