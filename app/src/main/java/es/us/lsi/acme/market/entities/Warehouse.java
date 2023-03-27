package es.us.lsi.acme.market.entities;

import java.util.HashMap;
import java.util.Map;

public class Warehouse{
    private String name;
    private String address;
    private Map<Item, Integer> items;

    public Warehouse() {
        this.name = "";
        this.address = "";
        this.items = new HashMap<>();
    }

    public Warehouse(String name, String address, Map<Item, Integer> items) {
        this.name = name;
        this.address = address;
        this.items = items;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Map<Item, Integer> getItems() {
        return items;
    }

    public void setItems(Map<Item, Integer> items) {
        this.items = items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Warehouse warehouse = (Warehouse) o;

        if (name != null ? !name.equals(warehouse.name) : warehouse.name != null) return false;
        if (address != null ? !address.equals(warehouse.address) : warehouse.address != null)
            return false;
        return items != null ? items.equals(warehouse.items) : warehouse.items == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (items != null ? items.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Warehouse{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", items=" + items +
                '}';
    }
}
