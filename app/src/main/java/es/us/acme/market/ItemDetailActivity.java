package es.us.acme.market;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.annimon.stream.Optional;
import com.annimon.stream.Stream;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Locale;
import java.util.Objects;

import es.us.acme.market.entities.Category;
import es.us.acme.market.entities.Comment;
import es.us.acme.market.entities.Item;
import es.us.acme.market.entities.ShoppingCart;
import es.us.acme.market.services.FirebaseDatabaseService;
import es.us.acme.market.services.LocalPreferences;

public class ItemDetailActivity extends AppCompatActivity {

    public static final String EXTRA_ITEM_SKU = "EXTRA_ITEM_SKU";
    public static final String EXTRA_ITEM_CATEGORY_NAME = "EXTRA_ITEM_CATEGORY_NAME";
    private Item item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        CollapsingToolbarLayout toolbar_layout = findViewById(R.id.toolbar_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        ImageView item_image = findViewById(R.id.item_image);
        TextView item_price = findViewById(R.id.item_price);
        TextView item_currency = findViewById(R.id.item_currency);
        TextView item_description = findViewById(R.id.item_description);
        TextView item_category = findViewById(R.id.item_category);
        TextView item_detail_no_comments = findViewById(R.id.item_detail_no_comments);
        RecyclerView item_detail_comments = findViewById(R.id.item_detail_comments);
        Button item_detail_new_comment_button = findViewById(R.id.item_detail_new_comment_button);
        EditText item_detail_new_comment_title = findViewById(R.id.item_detail_new_comment_title);
        AppCompatRatingBar item_detail_rating_bar = findViewById(R.id.item_detail_rating_bar);
        EditText item_detail_new_comment_message = findViewById(R.id.item_detail_new_comment_message);
        TextView item_shopping_cart_present = findViewById(R.id.item_shopping_cart_present);
        ImageButton item_shopping_cart_button = findViewById(R.id.item_shopping_cart_button);

        item_detail_comments.setHasFixedSize(false);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        item_detail_comments.setLayoutManager(mLayoutManager);
        CommentsAdapter commentsAdapter = new CommentsAdapter();
        item_detail_comments.setAdapter(commentsAdapter);

        FirebaseDatabaseService firebaseDatabaseService = FirebaseDatabaseService.getServiceInstance();

        String item_sku = getIntent().getStringExtra(EXTRA_ITEM_SKU);
        String item_category_name = getIntent().getStringExtra(EXTRA_ITEM_CATEGORY_NAME);

        if (item_sku == null || item_sku.isEmpty()) {
            finish();
        }
        if (item_category_name == null || item_category_name.isEmpty()) {
            finish();
        }

        item_detail_new_comment_button.setOnClickListener(v -> {
            if (!item_detail_new_comment_title.getText().toString().isEmpty()) {
                Comment comment = new Comment("", item_sku, LocalPreferences.getLocalUserInformationId(), item_detail_new_comment_title.getText().toString(), item_detail_new_comment_message.getText().toString(), Math.round(item_detail_rating_bar.getRating()));
                firebaseDatabaseService.saveComment(comment, (databaseError, databaseReference) -> {
                    if (databaseError == null) {
                        Toast.makeText(ItemDetailActivity.this, R.string.item_detail_comment_published, Toast.LENGTH_SHORT).show();
                        item_detail_new_comment_title.setText("");
                        item_detail_new_comment_message.setText("");
                        item_detail_rating_bar.setRating(0);
                    } else {
                        Toast.makeText(ItemDetailActivity.this, R.string.item_detail_comment_published_error, Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                new AlertDialog.Builder(ItemDetailActivity.this).setTitle(R.string.item_detail_new_comment_empty_title)
                        .setMessage(R.string.item_detail_new_comment_empty_msg)
                        .setPositiveButton(R.string.item_detail_new_comment_empty_ok, (a1, a2) -> {
                        }).show();
            }
        });

        firebaseDatabaseService.getItem(item_sku).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists() && dataSnapshot.getValue() != null && dataSnapshot.getValue(Item.class) != null) {
                    item = dataSnapshot.getValue(Item.class);
                    assert item != null;
                    toolbar.setTitle(item.getName());
                    toolbar_layout.setTitle(item.getName());

                    if (!item.getPicture().isEmpty()) {
                        Picasso.get()
                                .load(item.getPicture())
                                .placeholder(R.drawable.item_default_icon)
                                .error(R.drawable.item_default_icon)
                                .into(item_image);
                    }
                    item_price.setText(String.format(Locale.getDefault(), "%.2f", item.getPrice()));
                    item_description.setText(item.getDescription());
                    item_currency.setText(item.getCurrency());
                } else {
                    Toast.makeText(ItemDetailActivity.this, R.string.item_detail_no_item, Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ItemDetailActivity.this, R.string.item_detail_no_item, Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        firebaseDatabaseService.getCategory(item_category_name).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getValue(Category.class) != null) {
                    item_category.setText(Objects.requireNonNull(dataSnapshot.getValue(Category.class)).getName());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        firebaseDatabaseService.getCommentsForItem(item_sku).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.exists() && dataSnapshot.getValue() != null && dataSnapshot.getValue(Comment.class) != null) {
                    commentsAdapter.addComment(dataSnapshot.getValue(Comment.class));
                    item_detail_no_comments.setVisibility(View.GONE);
                    item_detail_comments.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.exists() && dataSnapshot.getValue() != null && dataSnapshot.getValue(Comment.class) != null) {
                    commentsAdapter.removeComment(dataSnapshot.getValue(Comment.class));
                    commentsAdapter.addComment(dataSnapshot.getValue(Comment.class));
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getValue() != null && dataSnapshot.getValue(Comment.class) != null) {
                    commentsAdapter.removeComment(dataSnapshot.getValue(Comment.class));
                    if (commentsAdapter.isEmpty()) {
                        item_detail_no_comments.setVisibility(View.VISIBLE);
                        item_detail_comments.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        firebaseDatabaseService.getShoppingCart().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getValue(ShoppingCart.class) != null) {
                    ShoppingCart shoppingCart = dataSnapshot.getValue(ShoppingCart.class);
                    assert shoppingCart != null;
                    if (shoppingCart.getItems().containsKey(item_sku)) {
                        item_shopping_cart_present.setText(R.string.item_shopping_cart_present_yes);
                        item_shopping_cart_button.setImageResource(R.drawable.shopping_cart_remove);
                        item_shopping_cart_button.setOnClickListener(listener -> {
                            shoppingCart.getItems().put(item_sku, shoppingCart.getItems().get(item_sku) - 1);
                            firebaseDatabaseService.saveShoppingCart(shoppingCart, (databaseError, databaseReference) -> {
                                if (databaseError == null) {
                                    Toast.makeText(ItemDetailActivity.this, R.string.shopping_cart_updated, Toast.LENGTH_SHORT).show();
                                }
                            });
                        });
                    } else {
                        item_shopping_cart_present.setText(R.string.item_shopping_cart_present_no);
                        item_shopping_cart_button.setImageResource(R.drawable.shopping_cart_add);
                        item_shopping_cart_button.setOnClickListener(listener -> {
                            shoppingCart.getItems().put(item_sku, 1);
                            firebaseDatabaseService.saveShoppingCart(shoppingCart, (databaseError, databaseReference) -> {
                                if (databaseError == null) {
                                    Toast.makeText(ItemDetailActivity.this, R.string.shopping_cart_updated, Toast.LENGTH_SHORT).show();
                                }
                            });
                        });
                    }
                } else {
                    firebaseDatabaseService.saveShoppingCart(new ShoppingCart(), (databaseError, databaseReference) -> {

                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
