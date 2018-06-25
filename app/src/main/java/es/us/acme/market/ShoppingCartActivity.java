package es.us.acme.market;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import es.us.acme.market.entities.Category;
import es.us.acme.market.entities.Item;
import es.us.acme.market.entities.ShoppingCart;
import es.us.acme.market.services.FirebaseDatabaseService;

public class ShoppingCartActivity extends AppCompatActivity {

    private RecyclerView shopping_cart_recycler_view;
    private LinearLayout shopping_cart_empty_layout;
    private ShoppingCartAdapter itemsAdapter;
    private ShoppingCart shoppingCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            FirebaseDatabaseService.getServiceInstance().saveShoppingCart(shoppingCart, (databaseError, databaseReference) -> {
            });
            Toast.makeText(this, R.string.shopping_cart_order, Toast.LENGTH_SHORT).show();
        });

        shopping_cart_recycler_view = findViewById(R.id.shopping_cart_recycler_view);
        shopping_cart_empty_layout = findViewById(R.id.shopping_cart_empty_layout);

        shopping_cart_recycler_view.setHasFixedSize(false);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        shopping_cart_recycler_view.setLayoutManager(mLayoutManager);

        FirebaseDatabaseService firebaseDatabaseService = FirebaseDatabaseService.getServiceInstance();
        firebaseDatabaseService.getShoppingCart().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getValue(ShoppingCart.class) != null) {
                    shoppingCart = dataSnapshot.getValue(ShoppingCart.class);
                    itemsAdapter = new ShoppingCartAdapter(ShoppingCartActivity.this);
                    shopping_cart_recycler_view.setAdapter(itemsAdapter);
                    if (shoppingCart.getItems().isEmpty()) {
                        shopping_cart_empty_layout.setVisibility(View.VISIBLE);
                        shopping_cart_recycler_view.setVisibility(View.GONE);
                        fab.setEnabled(false);
                    }else{
                        shopping_cart_empty_layout.setVisibility(View.GONE);
                        shopping_cart_recycler_view.setVisibility(View.VISIBLE);
                        fab.setEnabled(true);
                        for (String itemSku: shoppingCart.getItems().keySet()){
                            firebaseDatabaseService.getItem(itemSku).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists() && dataSnapshot.getValue(Item.class) != null){
                                        Item item = dataSnapshot.getValue(Item.class);
                                        assert item != null;
                                        item.setSku(itemSku);
                                        itemsAdapter.addItem(item, shoppingCart.getItems().get(item.getSku()));
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    }
                }else{
                    shopping_cart_empty_layout.setVisibility(View.VISIBLE);
                    shopping_cart_recycler_view.setVisibility(View.GONE);
                    fab.setEnabled(false);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                shopping_cart_empty_layout.setVisibility(View.VISIBLE);
                shopping_cart_recycler_view.setVisibility(View.GONE);
                fab.setEnabled(false);
            }
        });
    }
}
