package ru.training.karaf.model;

import javax.persistence.*;

@Entity
public class TagDO {

    @GeneratedValue
    @Id
    private long id;

    private String name;
    private String value;

    @ManyToOne
    private UserDO user;

    public TagDO() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public UserDO getUser() {
        return user;
    }

    public void setUser(UserDO user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "TagDO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
