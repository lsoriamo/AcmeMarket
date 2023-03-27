package es.us.lsi.acme.market.entities;

import java.util.HashMap;
import java.util.Map;

public class ShoppingCart {
    private String comment;
    private Double total;
    private Map<String, Integer> items;

    public ShoppingCart() {
        this.comment = "";
        this.total = 0d;
        this.items = new HashMap<>();
    }

    public ShoppingCart(String comment, Double total, Map<String, Integer> items) {
        this.comment = comment;
        this.total = total;
        this.items = items;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Map<String, Integer> getItems() {
        return items;
    }

    public void setItems(Map<String, Integer> items) {
        this.items = items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ShoppingCart that = (ShoppingCart) o;

        if (comment != null ? !comment.equals(that.comment) : that.comment != null) return false;
        if (total != null ? !total.equals(that.total) : that.total != null) return false;
        return items != null ? items.equals(that.items) : that.items == null;
    }

    @Override
    public int hashCode() {
        int result = comment != null ? comment.hashCode() : 0;
        result = 31 * result + (total != null ? total.hashCode() : 0);
        result = 31 * result + (items != null ? items.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ShoppingCart{" +
                "comment='" + comment + '\'' +
                ", total=" + total +
                ", items=" + items +
                '}';
    }
}
