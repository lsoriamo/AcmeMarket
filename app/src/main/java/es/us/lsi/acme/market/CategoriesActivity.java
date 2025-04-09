package es.us.lsi.acme.market;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import es.us.lsi.acme.market.R;
import es.us.lsi.acme.market.entities.Category;
import es.us.lsi.acme.market.services.FirebaseDatabaseService;

public class CategoriesActivity extends AppToolbarBaseActivity implements ChildEventListener {

    private CategoriesGridAdapter adapter;
    private GridView categories_gridview;
    private LinearLayout categories_empty_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        FloatingActionButton fab = findViewById(R.id.category_add);
        fab.setOnClickListener(view -> startActivity(new Intent(this, CategoriesAddActivity.class)));

        categories_gridview = findViewById(R.id.categories_gridview);
        adapter = new CategoriesGridAdapter();
        categories_gridview.setAdapter(adapter);

        categories_empty_layout = findViewById(R.id.categories_empty_layout);

        FirebaseDatabaseService firebaseDatabaseService = FirebaseDatabaseService.getServiceInstance();
        firebaseDatabaseService.getCategories().getRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() == 0) {
                    categories_empty_layout.setVisibility(View.VISIBLE);
                    categories_gridview.setVisibility(View.GONE);
                } else {
                    categories_empty_layout.setVisibility(View.GONE);
                    categories_gridview.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                categories_empty_layout.setVisibility(View.VISIBLE);
                categories_gridview.setVisibility(View.GONE);
            }
        });
        firebaseDatabaseService.getCategories().getRef().addChildEventListener(this);
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        if (dataSnapshot.exists() && dataSnapshot.getValue(Category.class) != null) {
            adapter.addCategory(dataSnapshot.getValue(Category.class));
            if (!adapter.isEmpty()) {
                categories_empty_layout.setVisibility(View.GONE);
                categories_gridview.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        if (dataSnapshot.exists() && dataSnapshot.getValue(Category.class) != null) {
            adapter.removeCategory(dataSnapshot.getValue(Category.class));
            adapter.addCategory(dataSnapshot.getValue(Category.class));
        }
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        if (dataSnapshot.exists() && dataSnapshot.getValue(Category.class) != null) {
            adapter.removeCategory(dataSnapshot.getValue(Category.class));
            if (adapter.isEmpty()) {
                categories_empty_layout.setVisibility(View.VISIBLE);
                categories_gridview.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
        if (dataSnapshot.exists() && dataSnapshot.getValue(Category.class) != null) {
            adapter.removeCategory(dataSnapshot.getValue(Category.class));
            adapter.addCategory(dataSnapshot.getValue(Category.class));
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    class CategoriesGridAdapter extends BaseAdapter {
        List<Category> categories;

        CategoriesGridAdapter() {
            this.categories = new ArrayList<>();
        }

        public void addCategory(Category category) {
            this.categories.add(category);
            this.notifyDataSetChanged();
        }

        public void removeCategory(Category category) {
            this.categories.remove(category);
            this.notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return categories.size();
        }

        @Override
        public Object getItem(int position) {
            return categories.get(position);
        }

        @Override
        public long getItemId(int position) {
            return categories.get(position).hashCode();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final Category category = categories.get(position);

            if (convertView == null) {
                final LayoutInflater layoutInflater = LayoutInflater.from(CategoriesActivity.this);
                convertView = layoutInflater.inflate(R.layout.adapter_category_grid_item, null);
            }

            final CardView cardView = convertView.findViewById(R.id.category_cardview);
            final ImageView pictureView = convertView.findViewById(R.id.category_picture);
            final TextView nameView = convertView.findViewById(R.id.category_name);
            final TextView descriptionView = convertView.findViewById(R.id.category_description);

            nameView.setText(category.getName());
            descriptionView.setText(category.getDescription());
            if (!category.getPicture().isEmpty()) {
                Picasso.get()
                        .load(category.getPicture())
                        .placeholder(R.drawable.category_default_icon)
                        .error(R.drawable.category_default_icon)
                        .into(pictureView);
            }
            cardView.setOnClickListener(listener -> {
                Intent intent = new Intent(CategoriesActivity.this, ItemsActivity.class);
                Gson gson = new Gson();
                intent.putExtra(ItemsActivity.EXTRA_CATEGORY, gson.toJson(category));
                startActivity(intent);
            });

            return convertView;
        }
    }
}
