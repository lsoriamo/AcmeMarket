package es.us.acme.market.entities;

public class Comment {
    private String __id;
    private String itemSku;
    private String user;
    private String title;
    private String text;
    private Integer stars;

    public Comment() {
        this.__id = "";
        this.itemSku = "";
        this.user = "";
        this.title = "";
        this.text = "";
        this.stars = 0;
    }

    public Comment(String __id, String itemSku, String user, String title, String text, Integer stars) {
        this.__id = __id;
        this.itemSku = itemSku;
        this.user = user;
        this.title = title;
        this.text = text;
        this.stars = stars;
    }


    public String get__id() {
        return __id;
    }

    public void set__id(String __id) {
        this.__id = __id;
    }

    public String getItemSku() {
        return itemSku;
    }

    public void setItemSku(String itemSku) {
        this.itemSku = itemSku;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getStars() {
        return stars;
    }

    public void setStars(Integer stars) {
        this.stars = stars;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Comment comment = (Comment) o;

        if (itemSku != null ? !itemSku.equals(comment.itemSku) : comment.itemSku != null)
            return false;
        if (user != null ? !user.equals(comment.user) : comment.user != null) return false;
        if (title != null ? !title.equals(comment.title) : comment.title != null) return false;
        if (text != null ? !text.equals(comment.text) : comment.text != null) return false;
        return stars != null ? stars.equals(comment.stars) : comment.stars == null;
    }

    @Override
    public int hashCode() {
        int result = itemSku != null ? itemSku.hashCode() : 0;
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (text != null ? text.hashCode() : 0);
        result = 31 * result + (stars != null ? stars.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "itemSku='" + itemSku + '\'' +
                ", user='" + user + '\'' +
                ", title='" + title + '\'' +
                ", text='" + text + '\'' +
                ", stars=" + stars +
                '}';
    }
}
