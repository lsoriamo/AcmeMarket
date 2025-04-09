package es.us.lsi.acme.market;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import es.us.lsi.acme.market.R;
import es.us.lsi.acme.market.entities.Category;
import es.us.lsi.acme.market.entities.Item;
import es.us.lsi.acme.market.services.FirebaseDatabaseService;

public class ItemsActivity extends AppToolbarBaseActivity implements ChildEventListener {

    public static final String EXTRA_CATEGORY = "EXTRA_CATEGORY";
    private RecyclerView items_recycler_view;
    private SwipeRefreshLayout items_refresh_view;
    private LinearLayout items_empty_layout;
    private ItemsAdapter itemsAdapter;
    private Category category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Gson gson = new Gson();

        if (getIntent().getStringExtra(EXTRA_CATEGORY) != null){
            category = gson.fromJson(getIntent().getStringExtra(EXTRA_CATEGORY), Category.class);
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(ItemsActivity.this, ItemsAddActivity.class);
            intent.putExtra(EXTRA_CATEGORY, gson.toJson(category));
            startActivity(intent);
        });

        items_recycler_view = findViewById(R.id.items_recycler_view);
        items_empty_layout = findViewById(R.id.items_empty_layout);
        items_refresh_view = findViewById(R.id.items_refresh_view);

        items_recycler_view.setHasFixedSize(false);

        TextView items_empty = findViewById(R.id.items_empty);

        SearchView items_search = findViewById(R.id.items_search);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        items_recycler_view.setLayoutManager(mLayoutManager);

        itemsAdapter = new ItemsAdapter(this, category);
        items_recycler_view.setAdapter(itemsAdapter);

        FirebaseDatabaseService firebaseDatabaseService = FirebaseDatabaseService.getServiceInstance();
        firebaseDatabaseService.getItemsInCategory(category.getName()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() == 0) {
                    items_empty.setText(R.string.items_empty_list);
                    items_empty_layout.setVisibility(View.VISIBLE);
                    items_refresh_view.setVisibility(View.GONE);
                } else {
                    items_empty.setText(R.string.items_empty_list_filtered);
                    items_empty_layout.setVisibility(View.GONE);
                    items_refresh_view.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                items_empty_layout.setVisibility(View.VISIBLE);
                items_refresh_view.setVisibility(View.GONE);
            }
        });
        firebaseDatabaseService.getItemsInCategory(category.getName()).addChildEventListener(this);

        items_refresh_view.setOnRefreshListener(() -> {
            items_search.setQuery("", true);
            items_refresh_view.setRefreshing(false);
        });

        items_search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                itemsAdapter.getFilter().filter(query);
                return false;
            }
        });
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        if (dataSnapshot.exists() && dataSnapshot.getValue(Category.class) != null) {
            itemsAdapter.addItem(dataSnapshot.getValue(Item.class));
            if (!itemsAdapter.isEmpty()) {
                items_empty_layout.setVisibility(View.GONE);
                items_refresh_view.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        if (dataSnapshot.exists() && dataSnapshot.getValue(Item.class) != null) {
            itemsAdapter.removeItem(dataSnapshot.getValue(Item.class));
            itemsAdapter.addItem(dataSnapshot.getValue(Item.class));
        }
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        if (dataSnapshot.exists() && dataSnapshot.getValue(Item.class) != null) {
            itemsAdapter.removeItem(dataSnapshot.getValue(Item.class));
            if (itemsAdapter.isEmpty()) {
                items_empty_layout.setVisibility(View.VISIBLE);
                items_refresh_view.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
