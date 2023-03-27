package es.us.lsi.acme.market.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Order {
    private String _id;
    private String ticker;
    private Date placementMoment;
    private Date deliveryMoment;
    private Date cancellationMoment;
    private String consumerName;
    private String deliveryAddress;
    private String comment;
    private Double total;
    private String consumer;
    private List<OrderedItem> orderedItems;

    public Order() {
        this._id = "";
        this.ticker = "";
        this.placementMoment = null;
        this.deliveryMoment = null;
        this.cancellationMoment = null;
        this.consumerName = "";
        this.deliveryAddress = "";
        this.comment = "";
        this.total = 0d;
        this.consumer = "";
        this.orderedItems = new ArrayList<>();
    }

    public Order(String _id, String ticker, Date placementMoment, Date deliveryMoment, Date cancellationMoment, String consumerName, String deliveryAddress, String comment, Double total, String consumer, List<OrderedItem> orderedItems) {
        this._id = _id;
        this.ticker = ticker;
        this.placementMoment = placementMoment;
        this.deliveryMoment = deliveryMoment;
        this.cancellationMoment = cancellationMoment;
        this.consumerName = consumerName;
        this.deliveryAddress = deliveryAddress;
        this.comment = comment;
        this.total = total;
        this.consumer = consumer;
        this.orderedItems = orderedItems;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public Date getPlacementMoment() {
        return placementMoment;
    }

    public void setPlacementMoment(Date placementMoment) {
        this.placementMoment = placementMoment;
    }

    public Date getDeliveryMoment() {
        return deliveryMoment;
    }

    public void setDeliveryMoment(Date deliveryMoment) {
        this.deliveryMoment = deliveryMoment;
    }

    public Date getCancellationMoment() {
        return cancellationMoment;
    }

    public void setCancellationMoment(Date cancellationMoment) {
        this.cancellationMoment = cancellationMoment;
    }

    public String getConsumerName() {
        return consumerName;
    }

    public void setConsumerName(String consumerName) {
        this.consumerName = consumerName;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
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

    public String getConsumer() {
        return consumer;
    }

    public void setConsumer(String consumer) {
        this.consumer = consumer;
    }

    public List<OrderedItem> getOrderedItems() {
        return orderedItems;
    }

    public void setOrderedItems(List<OrderedItem> orderedItems) {
        this.orderedItems = orderedItems;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Order order = (Order) o;

        if (_id != null ? !_id.equals(order._id) : order._id != null) return false;
        if (ticker != null ? !ticker.equals(order.ticker) : order.ticker != null) return false;
        if (placementMoment != null ? !placementMoment.equals(order.placementMoment) : order.placementMoment != null)
            return false;
        if (deliveryMoment != null ? !deliveryMoment.equals(order.deliveryMoment) : order.deliveryMoment != null)
            return false;
        if (cancellationMoment != null ? !cancellationMoment.equals(order.cancellationMoment) : order.cancellationMoment != null)
            return false;
        if (consumerName != null ? !consumerName.equals(order.consumerName) : order.consumerName != null)
            return false;
        if (deliveryAddress != null ? !deliveryAddress.equals(order.deliveryAddress) : order.deliveryAddress != null)
            return false;
        if (comment != null ? !comment.equals(order.comment) : order.comment != null) return false;
        if (total != null ? !total.equals(order.total) : order.total != null) return false;
        if (consumer != null ? !consumer.equals(order.consumer) : order.consumer != null)
            return false;
        return orderedItems != null ? orderedItems.equals(order.orderedItems) : order.orderedItems == null;
    }

    @Override
    public int hashCode() {
        int result = _id != null ? _id.hashCode() : 0;
        result = 31 * result + (ticker != null ? ticker.hashCode() : 0);
        result = 31 * result + (placementMoment != null ? placementMoment.hashCode() : 0);
        result = 31 * result + (deliveryMoment != null ? deliveryMoment.hashCode() : 0);
        result = 31 * result + (cancellationMoment != null ? cancellationMoment.hashCode() : 0);
        result = 31 * result + (consumerName != null ? consumerName.hashCode() : 0);
        result = 31 * result + (deliveryAddress != null ? deliveryAddress.hashCode() : 0);
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        result = 31 * result + (total != null ? total.hashCode() : 0);
        result = 31 * result + (consumer != null ? consumer.hashCode() : 0);
        result = 31 * result + (orderedItems != null ? orderedItems.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Order{" +
                "_id='" + _id + '\'' +
                ", ticker='" + ticker + '\'' +
                ", placementMoment=" + placementMoment +
                ", deliveryMoment=" + deliveryMoment +
                ", cancellationMoment=" + cancellationMoment +
                ", consumerName='" + consumerName + '\'' +
                ", deliveryAddress='" + deliveryAddress + '\'' +
                ", comment='" + comment + '\'' +
                ", total=" + total +
                ", consumer='" + consumer + '\'' +
                ", orderedItems=" + orderedItems +
                '}';
    }
}
