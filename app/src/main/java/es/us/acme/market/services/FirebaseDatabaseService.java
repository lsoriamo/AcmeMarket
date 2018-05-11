package es.us.acme.market.services;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import es.us.acme.market.entities.Actor;
import es.us.acme.market.entities.Category;
import es.us.acme.market.entities.Comment;
import es.us.acme.market.entities.FirebaseEntity;
import es.us.acme.market.entities.Item;
import es.us.acme.market.entities.Order;
import es.us.acme.market.entities.ShoppingCart;
import es.us.acme.market.entities.Warehouse;

public class FirebaseDatabaseService {
    private static String userId;
    private static FirebaseDatabaseService service;
    private static FirebaseDatabase mDatabase;

    public static synchronized FirebaseDatabaseService getServiceInstance() {
        if (service == null || mDatabase == null) {
            service = new FirebaseDatabaseService();
            mDatabase = FirebaseDatabase.getInstance();
            mDatabase.setPersistenceEnabled(true);
        }

        if (userId == null || userId.isEmpty()) {
            FirebaseDatabaseService.userId = LocalPreferences.getLocalUserInformationId();
        }
        return service;
    }

    public static FirebaseDatabaseService getServiceInstance(String userId) {
        FirebaseDatabaseService.userId = userId;
        FirebaseDatabaseService.getServiceInstance();
        return service;
    }

    public Task<Void> saveUser(Actor user) {
        return mDatabase.getReference("users/" + user.get_id() + "/info").setValue(user);
    }

    public Task<Void> removeUser(String userId) {
        return mDatabase.getReference("users/" + userId).removeValue();
    }

    public void saveFirebaseToken(FirebaseEntity token) {
        mDatabase.getReference("users/" + userId + "/firebase_token").setValue(token);
    }

    public DatabaseReference getUser() {
        return mDatabase.getReference("users/" + userId + "/info/");
    }

    public void saveItem(Item item, DatabaseReference.CompletionListener completionListener) {
        mDatabase.getReference("items/").push().setValue(item, completionListener);
    }

    public void removeItem(Item item, DatabaseReference.CompletionListener completionListener) {
        mDatabase.getReference("items/" + item.getSku()).removeValue(completionListener);
    }

    public DatabaseReference getItem(String sku){
        return mDatabase.getReference("items/" + sku).getRef();
    }

    public void saveComment(Comment comment, DatabaseReference.CompletionListener completionListener) {
        mDatabase.getReference("items/" + comment.getItemSku() + "/comments").push().setValue(comment, completionListener);
    }

    public void removeComment(Comment comment, DatabaseReference.CompletionListener completionListener) {
        mDatabase.getReference("items/" + comment.getItemSku() + "/comments/" + comment.get__id()).removeValue(completionListener);
    }

    public DatabaseReference getCommentsForItem(String itemSku) {
        return mDatabase.getReference("items/" + itemSku + "/comments").getRef();
    }

    public DatabaseReference getCommentsForItem(String itemSku, int lastN) {
        return mDatabase.getReference("items/" + itemSku + "/comments").limitToLast(lastN).getRef();
    }

    public void saveShoppingCart(ShoppingCart shoppingCart, DatabaseReference.CompletionListener completionListener) {
        mDatabase.getReference("users/" + userId + "/cart").setValue(shoppingCart, completionListener);
    }
    
    public void cleanShoppingCart(DatabaseReference.CompletionListener completionListener) {
        mDatabase.getReference("users/" + userId + "/cart").removeValue(completionListener);
    }

    public DatabaseReference getShoppingCart() {
        return mDatabase.getReference("users/" + userId + "/cart").getRef();
    }

    public void saveCategory(Category category, DatabaseReference.CompletionListener completionListener) {
        mDatabase.getReference("categories/" + category.getName()).setValue(category, completionListener);
    }

    public void removeCategory(String categoryName, DatabaseReference.CompletionListener completionListener) {
        mDatabase.getReference("categories/" + categoryName).removeValue(completionListener);
    }

    public DatabaseReference getCategories() {
        return mDatabase.getReference("categories/").getRef();
    }

    public DatabaseReference getCategory(String categoryName) {
        return mDatabase.getReference("categories/" + categoryName).getRef();
    }

    public void saveItemInCategory(Item item, DatabaseReference.CompletionListener completionListener) {
        mDatabase.getReference("categories/" + item.getCategory() + "/items/" + item.getSku()).setValue(item, completionListener);
    }

    public void removeItemFromCategory(Item item, DatabaseReference.CompletionListener completionListener) {
        mDatabase.getReference("categories/" + item.getCategory() + "/items/" + item.getSku()).removeValue(completionListener);
    }

    public DatabaseReference getItemsInCategory(String categoryName) {
        return mDatabase.getReference("categories/" + categoryName + "/items").getRef();
    }

    public void saveOrder(Order order, DatabaseReference.CompletionListener completionListener) {
        mDatabase.getReference("users/" + userId + "/orders").push().setValue(order, completionListener);
    }

    public void removeOrder(String orderId, DatabaseReference.CompletionListener completionListener) {
        mDatabase.getReference("users/" + userId + "/orders/" + orderId).removeValue(completionListener);
    }

    public DatabaseReference getOrders() {
        return mDatabase.getReference("users/" + userId + "/orders").getRef();
    }

    public void saveWarehouse(Warehouse warehouse, DatabaseReference.CompletionListener completionListener) {
        mDatabase.getReference("warehouses/" + warehouse.getName()).setValue(warehouse, completionListener);
    }

    public DatabaseReference getWarehouses() {
        return mDatabase.getReference("warehouses").getRef();
    }

    public void removeWarehouse(String warehouseName, DatabaseReference.CompletionListener completionListener) {
        mDatabase.getReference("warehouses/" + warehouseName).removeValue(completionListener);
    }

    public void setItemInWarehouse(String warehouseName, Item item, int quantity, DatabaseReference.CompletionListener completionListener) {
        mDatabase.getReference("warehouses/" + warehouseName + "/items/" + item.getSku() + "/" + item).setValue(quantity, completionListener);
    }

    public void removeItemFromWarehouse(String warehouseName, Item item, DatabaseReference.CompletionListener completionListener) {
        mDatabase.getReference("warehouses/" + warehouseName + "/items/" + item.getSku()).removeValue(completionListener);
    }
}
