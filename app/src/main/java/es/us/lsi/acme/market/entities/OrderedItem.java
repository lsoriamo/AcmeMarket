package es.us.lsi.acme.market.entities;

public class OrderedItem {
    private String sku;
    private boolean served;
    private String name;
    private Integer quantity;
    private Double price;
    private Double tax;

    public OrderedItem() {
        this.sku = "";
        this.served = false;
        this.name = "";
        this.quantity = 0;
        this.price = 0d;
        this.tax = 0d;
    }

    public OrderedItem(String sku, boolean served, String name, Integer quantity, Double price, Double tax) {
        this.sku = sku;
        this.served = served;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.tax = tax;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public boolean isServed() {
        return served;
    }

    public void setServed(boolean served) {
        this.served = served;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getTax() {
        return tax;
    }

    public void setTax(Double tax) {
        this.tax = tax;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderedItem that = (OrderedItem) o;

        if (served != that.served) return false;
        if (sku != null ? !sku.equals(that.sku) : that.sku != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (quantity != null ? !quantity.equals(that.quantity) : that.quantity != null)
            return false;
        if (price != null ? !price.equals(that.price) : that.price != null) return false;
        return tax != null ? tax.equals(that.tax) : that.tax == null;
    }

    @Override
    public int hashCode() {
        int result = sku != null ? sku.hashCode() : 0;
        result = 31 * result + (served ? 1 : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (quantity != null ? quantity.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + (tax != null ? tax.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "OrderedItem{" +
                "sku='" + sku + '\'' +
                ", served=" + served +
                ", name='" + name + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", tax=" + tax +
                '}';
    }
}
