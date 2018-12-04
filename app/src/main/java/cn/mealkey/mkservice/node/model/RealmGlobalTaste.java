package cn.mealkey.mkservice.node.model;

import io.realm.RealmObject;

public class RealmGlobalTaste extends RealmObject{
    public long mealId;
    public long id;
    public String name;
    public long tableId;

    public RealmGlobalTaste(NodeTaste taste, long mealId, long tableId) {
        this.mealId = mealId;
        this.tableId = tableId;
        this.id = taste.id;
        this.name = taste.name;

    }

    public NodeTaste nodeTaste(){
        return new NodeTaste(this.id, this.name);
    }


    public RealmGlobalTaste() {
    }

    public long getMealId() {
        return mealId;
    }

    public void setMealId(long mealId) {
        this.mealId = mealId;
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

    public long getTableId() {
        return tableId;
    }

    public void setTableId(long tableId) {
        this.tableId = tableId;
    }
}
