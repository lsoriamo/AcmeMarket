package es.us.acme.market;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.NumberPicker;
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
    private FirebaseDatabaseService firebaseDatabaseService;
    private Context context;

    public ShoppingCartAdapter(Context context) {
        dataset = new HashMap<>();
        shoppingCart = new ShoppingCart();
        this.context = context;
        this.firebaseDatabaseService = FirebaseDatabaseService.getServiceInstance();
    }

    public Map<Item, Integer> getDataset() {
        return dataset;
    }

    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }


    public void addItem(Item item, Integer units) {
        dataset.put(item, units);
        if (units <= 0) {
            shoppingCart.getItems().remove(item.getSku());
            removeItem(item);
        } else {
            shoppingCart.getItems().put(item.getSku(), dataset.get(item));
            notifyDataSetChanged();
        }
        shoppingCart.setTotal(Stream.of(dataset.entrySet()).mapToDouble(elem -> elem.getKey().getPrice() * (double) elem.getValue()).sum());
    }

    private void setItem(Item item, Integer units) {
        addItem(item, units);
        shoppingCart.setTotal(Stream.of(dataset.entrySet()).mapToDouble(elem -> elem.getKey().getPrice() * (double) elem.getValue()).sum());
        firebaseDatabaseService.saveShoppingCart(shoppingCart, (databaseError, databaseReference) -> {
        });
    }

    private void removeItem(Item item) {
        if (dataset.containsKey(item)) {
            dataset.put(item, dataset.get(item) - 1);
            shoppingCart.getItems().put(item.getSku(), dataset.get(item));
            if (dataset.get(item) <= 0) {
                dataset.remove(item);
                shoppingCart.getItems().remove(item.getSku());
            }
            shoppingCart.setTotal(Stream.of(dataset.entrySet()).mapToDouble(elem -> elem.getKey().getPrice() * (double) elem.getValue()).sum());
            firebaseDatabaseService.saveShoppingCart(shoppingCart, (databaseError, databaseReference) -> {
            });
            notifyDataSetChanged();
        }
    }

    private void clearItems() {
        dataset.clear();
        shoppingCart.getItems().clear();
        shoppingCart.setTotal(0d);
        firebaseDatabaseService.cleanShoppingCart((databaseError, databaseReference) -> {
        });
        notifyDataSetChanged();
    }

    public boolean isEmpty() {
        return dataset.isEmpty();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        View view;
        if (viewType == 0) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_shopping_cart_item, parent, false);
            return new ShoppingCartViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_shopping_cart_summary, parent, false);
            return new ShoppingCartSummaryViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position < dataset.size())
            return 0;
        else
            return 1;
    }

    @Override
    public int getItemCount() {
        return dataset.isEmpty() ? 0 : dataset.size() + 1;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int listPosition) {
        if (listPosition < dataset.size()) {
            ShoppingCartViewHolder shoppingCartViewHolder = (ShoppingCartViewHolder) holder;
            List<Item> items = Stream.of(dataset.keySet()).sortBy(Item::getName).toList();
            Item item = items.get(listPosition);
            shoppingCartViewHolder.shopping_cart_item_name.setText(item.getName());
            shoppingCartViewHolder.shopping_cart_item_units.setText(dataset.get(item).toString());
            shoppingCartViewHolder.shopping_cart_change_number.setOnClickListener(v -> showNumberPickerDialog(item));
            shoppingCartViewHolder.shopping_cart_item_units.setOnClickListener(v -> showNumberPickerDialog(item));
            shoppingCartViewHolder.shopping_cart_item_price.setText(String.format(Locale.getDefault(), "%.2f", item.getPrice()));
            shoppingCartViewHolder.shopping_cart_item_button.setOnClickListener(v -> {
                removeItem(item);
            });
        }else{
            ShoppingCartSummaryViewHolder shoppingCartSummaryViewHolder = (ShoppingCartSummaryViewHolder) holder;
            shoppingCartSummaryViewHolder.shopping_cart_summary_clear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new android.support.v7.app.AlertDialog.Builder(context).setTitle(R.string.shopping_cart_clear_question).setMessage(R.string.categories_add_picture_empty_msg)
                            .setPositiveButton(R.string.shopping_cart_clear_question_yes, (dialog, which) -> {
                                clearItems();
                            }).setNegativeButton(R.string.shopping_cart_clear_question_no,  (dialog, which) -> {}).show();
                }
            });
            shoppingCartSummaryViewHolder.shopping_cart_summary_total.setText(String.format("%.2f", shoppingCart.getTotal()));
            shoppingCartSummaryViewHolder.shopping_cart_summary_total_currency.setText(Stream.of(dataset.keySet()).toList().get(0).getCurrency());
        }
    }

    private void showNumberPickerDialog(Item item) {
        final Dialog d = new Dialog(context);
        d.setTitle(context.getString(R.string.number_picker_dialog_title));
        d.setContentView(R.layout.number_picker_dialog);
        Button b1 = d.findViewById(R.id.button1);
        final NumberPicker np = d.findViewById(R.id.numberPicker1);
        np.setMaxValue(100);
        np.setMinValue(0);
        np.setValue(dataset.get(item));
        np.setWrapSelectorWheel(false);
        np.setOnValueChangedListener((picker, oldVal, newVal) -> {

        });
        b1.setOnClickListener(v -> {
            setItem(item, np.getValue());
            d.dismiss();
        });
        d.show();
    }

    private class ShoppingCartViewHolder extends RecyclerView.ViewHolder {
        TextView shopping_cart_item_name;
        TextView shopping_cart_item_units;
        TextView shopping_cart_item_price;
        ImageButton shopping_cart_item_button;
        ImageButton shopping_cart_change_number;

        ShoppingCartViewHolder(View itemView) {
            super(itemView);
            shopping_cart_item_name = itemView.findViewById(R.id.shopping_cart_item_name);
            shopping_cart_change_number = itemView.findViewById(R.id.shopping_cart_change_number);
            shopping_cart_item_units = itemView.findViewById(R.id.shopping_cart_item_units);
            shopping_cart_item_price = itemView.findViewById(R.id.shopping_cart_item_price);
            shopping_cart_item_button = itemView.findViewById(R.id.shopping_cart_item_button);
        }
    }

    private class ShoppingCartSummaryViewHolder extends RecyclerView.ViewHolder {
        TextView shopping_cart_summary_total;
        TextView shopping_cart_summary_total_currency;
        Button shopping_cart_summary_order;
        TextView shopping_cart_summary_clear;

        ShoppingCartSummaryViewHolder(View itemView) {
            super(itemView);
            shopping_cart_summary_total = itemView.findViewById(R.id.shopping_cart_summary_total);
            shopping_cart_summary_total_currency = itemView.findViewById(R.id.shopping_cart_summary_total_currency);
            shopping_cart_summary_order = itemView.findViewById(R.id.shopping_cart_summary_order);
            shopping_cart_summary_clear = itemView.findViewById(R.id.shopping_cart_summary_clear);
        }
    }
}
