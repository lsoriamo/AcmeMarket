package es.us.acme.market;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.annimon.stream.Stream;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import es.us.acme.market.entities.Item;
import es.us.acme.market.entities.ShoppingCart;
import es.us.acme.market.services.FirebaseDatabaseService;

public class ShoppingCartAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Map<Item, Integer> dataset;
    private ShoppingCart shoppingCart;

    public ShoppingCartAdapter(ShoppingCart shoppingCart) {
        dataset = new HashMap<>();
        this.shoppingCart = shoppingCart;
    }

    public void addItem(Item item) {
        if (dataset.containsKey(item))
            dataset.put(item, dataset.get(item) + 1);
        else
            dataset.put(item, 1);
        notifyDataSetChanged();
    }

    public void removeItem(Item item) {
        if (dataset.containsKey(item)) {
            dataset.put(item, dataset.get(item) - 1);
            if (dataset.get(item) == 0)
                dataset.remove(item);
            notifyDataSetChanged();
        }
    }

    public void clearItems() {
        dataset.clear();
        notifyDataSetChanged();
    }

    public boolean isEmpty() {
        return dataset.isEmpty();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_product_item, parent, false);
        return new ShoppingCartViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int listPosition) {
        ShoppingCartViewHolder shoppingCartViewHolder = (ShoppingCartViewHolder) holder;
        List<Item> items = Stream.of(dataset.keySet()).sortBy(Item::getName).toList();
        Item item = items.get(listPosition);
        shoppingCartViewHolder.shopping_cart_item_name.setText(item.getName());
        shoppingCartViewHolder.shopping_cart_item_units.setText(dataset.get(item));
        shoppingCartViewHolder.shopping_cart_item_price.setText(String.format(Locale.getDefault(), "%.2f", item.getPrice()));
        shoppingCartViewHolder.shopping_cart_item_button.setOnClickListener(v -> {
            if (dataset.get(item) > 1)
                shoppingCart.getItems().put(item.getSku(), dataset.get(item) - 1);
            else
                shoppingCart.getItems().remove(item.getSku());
            FirebaseDatabaseService.getServiceInstance().saveShoppingCart(shoppingCart, (databaseError, databaseReference) -> {
            });
        });
    }

    private class ShoppingCartViewHolder extends RecyclerView.ViewHolder {
        TextView shopping_cart_item_name;
        TextView shopping_cart_item_units;
        TextView shopping_cart_item_price;
        ImageButton shopping_cart_item_button;

        ShoppingCartViewHolder(View itemView) {
            super(itemView);
            shopping_cart_item_name = itemView.findViewById(R.id.shopping_cart_item_name);
            shopping_cart_item_units = itemView.findViewById(R.id.shopping_cart_item_units);
            shopping_cart_item_price = itemView.findViewById(R.id.shopping_cart_item_price);
            shopping_cart_item_button = itemView.findViewById(R.id.shopping_cart_item_button);
        }
    }
}
