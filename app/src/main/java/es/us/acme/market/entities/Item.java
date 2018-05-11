package es.us.acme.market.entities;

import java.util.ArrayList;
import java.util.List;

public class Item {
    private String sku;
    private boolean deleted;
    private String name;
    private String description;
    private Double price;
    private List<String> tags;
    private String picture;
    private String currency;
    private String category;

    public Item() {
        this.sku = "";
        this.deleted = false;
        this.name = "";
        this.description = "";
        this.price = 0d;
        this.tags = new ArrayList<>();
        this.picture = "";
        this.currency = "";
        this.category = "";
    }

    public Item(String sku, boolean deleted, String name, String description, Double price, List<String> tags, String picture, String currency, String category) {
        this.sku = sku;
        this.deleted = deleted;
        this.name = name;
        this.description = description;
        this.price = price;
        this.tags = tags;
        this.picture = picture;
        this.currency = currency;
        this.category= category;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Item item = (Item) o;

        if (deleted != item.deleted) return false;
        if (sku != null ? !sku.equals(item.sku) : item.sku != null) return false;
        if (name != null ? !name.equals(item.name) : item.name != null) return false;
        if (description != null ? !description.equals(item.description) : item.description != null)
            return false;
        if (price != null ? !price.equals(item.price) : item.price != null) return false;
        if (tags != null ? !tags.equals(item.tags) : item.tags != null) return false;
        if (picture != null ? !picture.equals(item.picture) : item.picture != null) return false;
        return currency != null ? currency.equals(item.currency) : item.currency == null;
    }

    @Override
    public int hashCode() {
        int result = sku != null ? sku.hashCode() : 0;
        result = 31 * result + (deleted ? 1 : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + (tags != null ? tags.hashCode() : 0);
        result = 31 * result + (picture != null ? picture.hashCode() : 0);
        result = 31 * result + (currency != null ? currency.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Item{" +
                "sku='" + sku + '\'' +
                ", deleted=" + deleted +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", tags=" + tags +
                ", picture='" + picture + '\'' +
                ", currency='" + currency + '\'' +
                '}';
    }
}
