package es.us.lsi.acme.market.entities;

import java.util.Date;

public class Actor {
    private String _id;
    private String name;
    private String surname;
    private String email;
    private String phone;
    private ActorRole role;
    private Date created;

    public Actor() {
        this._id = "";
        this.name = "";
        this.surname = "";
        this.email = "";
        this.phone = "";
        this.role = ActorRole.CONSUMER;
        this.created = new Date();
    }

    public Actor(String _id, String name, String surname, String email, String phone, String role, Date created) {
        this._id = _id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.phone = phone;
        this.role = ActorRole.valueOf(role);
        this.created = created;
    }

    public Actor(String _id, String name, String surname, String email, String phone, ActorRole role, Date created) {
        this._id = _id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.phone = phone;
        this.role = role;
        this.created = created;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public ActorRole getRole() {
        return role;
    }

    public void setRole(ActorRole role) {
        this.role = role;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Actor actor = (Actor) o;

        if (_id != null ? !_id.equals(actor._id) : actor._id != null) return false;
        if (name != null ? !name.equals(actor.name) : actor.name != null) return false;
        if (surname != null ? !surname.equals(actor.surname) : actor.surname != null) return false;
        if (email != null ? !email.equals(actor.email) : actor.email != null) return false;
        if (phone != null ? !phone.equals(actor.phone) : actor.phone != null) return false;
        if (role != actor.role) return false;
        return created != null ? created.equals(actor.created) : actor.created == null;
    }

    @Override
    public int hashCode() {
        int result = _id != null ? _id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (surname != null ? surname.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (phone != null ? phone.hashCode() : 0);
        result = 31 * result + (role != null ? role.hashCode() : 0);
        result = 31 * result + (created != null ? created.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Actor{" +
                "_id='" + _id + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", role=" + role +
                ", created=" + created +
                '}';
    }

    public enum ActorRole{
        CLERK,
        ADMINISTRATOR,
        CONSUMER
    }
}
