package es.us.acme.market.services;

import java.util.List;

import es.us.acme.market.entities.Category;
import es.us.acme.market.entities.Item;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiRestAdapterInterface {
    @GET("categories")
    Call<List<Category>> getCategoryById(@Query("categoryId") String categoryId);

    @GET("categories")
    Call<List<Category>> getCategories();

    /*@PUT("categories")
    Call<Category> createCategory(@Query("categoryId") String categoryId, @Body Category category);

    @DELETE("categories")
    Call<ResponseBody> deleteCategory(@Query("categoryId") String categoryId);*/

    @GET("items")
    Call<List<Item>> getItems();

    @GET("items")
    Call<List<Item>> getItemByCategory(@Query("categoryId") String categoryId);

    @GET("items")
    Call<List<Item>> getItemByCategorySorted(@Query("categoryId") String categoryId, @Query("sortedBy") String sortedBy, @Query("reverse") String reverse);

    @GET("items")
    Call<List<Item>> getItemByName(@Query("itemName") String itemName);
}
