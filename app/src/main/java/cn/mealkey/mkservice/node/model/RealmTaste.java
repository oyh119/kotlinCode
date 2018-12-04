package cn.mealkey.mkservice.node.model;

import io.realm.RealmObject;

public class RealmTaste extends RealmObject{

    public RealmTaste() {
    }

    public RealmTaste(NodeTaste nodeTaste) {
        this.id = nodeTaste.id;
        this.name = nodeTaste.name;
    }

    public long id;
    public String name;

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
}
