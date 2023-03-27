package es.us.lsi.acme.market;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import es.us.lsi.acme.market.R;
import es.us.lsi.acme.market.services.LocalPreferences;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String EXTRA_USER_ID = "EXTRA_USER_ID";
    private GridView shortcut_gridview;
    private MainGridAdapter adapter;
    public static String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        userId = getIntent().getStringExtra(EXTRA_USER_ID);

        shortcut_gridview = findViewById(R.id.shortcut_gridview);
        adapter = new MainGridAdapter();
        shortcut_gridview.setAdapter(adapter);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        adapter.addShortcut(new MainShorcuts(getString(R.string.main_section_explore_title), getString(R.string.main_section_explore_msg), "http://icons.iconarchive.com/icons/webalys/kameleon.pics/512/Shop-icon.png", CategoriesActivity.class));
        //adapter.addShortcut(new MainShorcuts(getString(R.string.main_section_warehouse_title), getString(R.string.main_section_warehouse_msg), "https://housing.umn.edu/sites/housing.umn.edu/files/hall_involvement.png", CategoriesActivity.class));
        adapter.addShortcut(new MainShorcuts(getString(R.string.main_section_cart_title), getString(R.string.main_section_cart_msg), "https://checkout.advancedshippingmanager.com/wp-content/uploads/2015/10/Cart-Icon-PNG-Graphic-Cave-e1461785088730-300x300.png", ShoppingCartActivity.class));
        adapter.addShortcut(new MainShorcuts(getString(R.string.main_section_orders_title), getString(R.string.main_section_orders_msg), "https://www.mageworx.com/media/catalog/product/cache/1/image/265x265/9df78eab33525d08d6e5fb8d27136e95/o/r/order_editor.png", OrderMapActivity.class));
        adapter.addShortcut(new MainShorcuts(getString(R.string.main_section_contact_title), getString(R.string.main_section_contact_msg), "http://marrybd.com/assets/icon/about.png", MainAboutActivity.class));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_logout) {
            new AlertDialog.Builder(this).setTitle(R.string.main_logout_title).setMessage(R.string.main_logout_msg).setPositiveButton(R.string.main_logout_yes, (dialog, which) -> {
                LocalPreferences.userLogout();
                Intent intent = new Intent(AcmeMarketApplication.getContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                AcmeMarketApplication.getContext().startActivity(intent);
            }).setNegativeButton(R.string.main_logout_no, (dialog, which) -> {

            }).show();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        startActivity(new Intent(this, item.getClass()));

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    class MainShorcuts {
        private String title;
        private String description;
        private String imageUrl;
        private Class referenceClass;

        public MainShorcuts(String title, String description, String imageUrl, Class referenceClass) {
            this.title = title;
            this.description = description;
            this.imageUrl = imageUrl;
            this.referenceClass = referenceClass;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public Class getReferenceClass() {
            return referenceClass;
        }

        public void setReferenceClass(Class referenceClass) {
            this.referenceClass = referenceClass;
        }
    }

    class MainGridAdapter extends BaseAdapter {
        List<MainShorcuts> shortcuts;

        MainGridAdapter() {
            this.shortcuts = new ArrayList<>();
        }

        public void addShortcut(MainShorcuts shorcut) {
            this.shortcuts.add(shorcut);
            this.notifyDataSetChanged();
        }

        public void removeShortcut(MainShorcuts shorcut) {
            this.shortcuts.remove(shorcut);
            this.notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return shortcuts.size();
        }

        @Override
        public Object getItem(int position) {
            return shortcuts.get(position);
        }

        @Override
        public long getItemId(int position) {
            return shortcuts.get(position).hashCode();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final MainShorcuts shortcut = shortcuts.get(position);

            if (convertView == null) {
                final LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
                convertView = layoutInflater.inflate(R.layout.adapter_main_grid_item, null);
            }

            final CardView cardView = convertView.findViewById(R.id.shortcut_cardview);
            final ImageView pictureView = convertView.findViewById(R.id.shortcut_picture);
            final TextView nameView = convertView.findViewById(R.id.shortcut_name);
            final TextView descriptionView = convertView.findViewById(R.id.shortcut_description);

            nameView.setText(shortcut.getTitle());
            descriptionView.setText(shortcut.getDescription());
            if (!shortcut.getImageUrl().isEmpty()) {
                Picasso.get()
                        .load(shortcut.getImageUrl())
                        .placeholder(R.drawable.category_default_icon)
                        .error(R.drawable.category_default_icon)
                        .into(pictureView);
            }
            cardView.setOnClickListener(listener -> {
                Intent intent = new Intent(MainActivity.this, shortcut.getReferenceClass());
                startActivity(intent);
            });

            return convertView;
        }
    }
}
