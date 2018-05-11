package es.us.acme.market;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.annimon.stream.Stream;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import es.us.acme.market.entities.Category;
import es.us.acme.market.entities.Item;

public class ItemsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
    private Category category;
    private String currentQuery;
    private List<Item> dataSetOriginal;
    private List<Item> dataSetFiltered;
    private Context context;

    public ItemsAdapter(Context context, Category category) {
        this.category = category;
        this.context = context;
        this.currentQuery = "";
        dataSetOriginal = new ArrayList<>();
        dataSetFiltered = new ArrayList<>();
    }

    public void addItem(Item item) {
        dataSetOriginal.add(item);
        if (checkFilter(item, currentQuery)) {
            dataSetFiltered.add(item);
            notifyItemInserted(dataSetFiltered.indexOf(item));
        }
    }

    public void removeItem(Item item) {
        dataSetOriginal.remove(item);
        int index = dataSetFiltered.indexOf(item);
        if (index > -1) {
            dataSetFiltered.remove(item);
            notifyItemRemoved(index);
        }
    }

    public void clearItems() {
        dataSetOriginal.clear();
        dataSetFiltered.clear();
        notifyDataSetChanged();
    }

    public boolean isEmpty() {
        return dataSetFiltered.isEmpty();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_product_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return dataSetFiltered.size();
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int listPosition) {
        ItemViewHolder trainingViewHolder = (ItemViewHolder) holder;
        Item item = dataSetFiltered.get(listPosition);
        trainingViewHolder.item_name.setText(item.getName());
        trainingViewHolder.item_description.setText(item.getDescription());
        if (!item.getPicture().isEmpty()) {
            Picasso.get()
                    .load(item.getPicture())
                    .placeholder(R.drawable.item_default_icon)
                    .error(R.drawable.item_default_icon)
                    .into(trainingViewHolder.item_image);
        }
        trainingViewHolder.item_price.setText(String.format(Locale.getDefault(), "%.2f", item.getPrice()));
        trainingViewHolder.item_currency.setText(item.getCurrency());
        trainingViewHolder.item_cardview.setOnClickListener(v -> {
            Intent intent = new Intent(context, ItemDetailActivity.class);
            intent.putExtra(ItemDetailActivity.EXTRA_ITEM_SKU, item.getSku());
            intent.putExtra(ItemDetailActivity.EXTRA_ITEM_CATEGORY_NAME, category.getName());
            context.startActivity(intent);
        });
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults results = new FilterResults();
                if (charSequence == null || charSequence.length() == 0) {
                    currentQuery = "";
                    results.values = dataSetOriginal;
                    results.count = dataSetOriginal.size();
                } else {
                    currentQuery = charSequence.toString();
                    List<Item> filterResultsData = Stream.of(dataSetOriginal).filter(item -> checkFilter(item, currentQuery)).toList();
                    results.values = filterResultsData;
                    results.count = filterResultsData.size();
                }

                return results;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence charSequence,
                                          FilterResults filterResults) {
                dataSetFiltered = (List<Item>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    private boolean checkFilter(Item item, String query) {
        return item.getName().toLowerCase().contains(query.toLowerCase());
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView item_name;
        TextView item_description;
        ImageView item_image;
        TextView item_price;
        TextView item_currency;
        CardView item_cardview;

        ItemViewHolder(View itemView) {
            super(itemView);
            item_cardview = itemView.findViewById(R.id.item_cardview);
            item_name = itemView.findViewById(R.id.item_name);
            item_description = itemView.findViewById(R.id.item_description);
            item_image = itemView.findViewById(R.id.item_image);
            item_price = itemView.findViewById(R.id.item_price);
            item_currency = itemView.findViewById(R.id.item_currency);
        }
    }
}
