package cn.mealkey.mkservice.node.model;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class RealmSetDish extends RealmObject{
    @PrimaryKey
    private String pid;
    private String parentKey;
    public long mealId;
    public long dishId;
    public String userId;
    public long orderTime;

    public int from;
    public long tableId;

    public int minQuantity;
    public String unitName;
    public double memberPrice;
    public double price;

    public String dishName;
    public String dishTypeName;
    public String flavorTypeName;

    public long dishTasteId;

    public String headimgurl;
    public String nickname;

    public int eatCount;
    public double weighQuantity;
    public double processingCharge;

    public boolean hasDiet;
    public RealmList<RealmTaste> tastes = new RealmList<>();

    public RealmSetDish() {
    }

    @SuppressLint("DefaultLocale")
    public RealmSetDish(NodeSetDish dish, long mealId, long tableId){
        this.pid = dish.pid;
        this.parentKey = dish.parentKey;
        this.dishId = dish.dishId;
        this.userId = dish.userId;
        this.mealId = mealId;
        this.tableId = tableId;
        this.orderTime = dish.orderTime;
        this.from = dish.from;
        this.minQuantity = dish.minQuantity;
        this.unitName = dish.unitName;
        this.memberPrice = dish.memberPrice;
        this.price = dish.price;
        this.dishName = dish.dishName;
        this.dishTypeName = dish.dishTypeName;
        this.flavorTypeName = dish.flavorTypeName;
        this.dishTasteId = dish.dishTasteId;
        this.headimgurl = dish.headimgurl;
        this.nickname = dish.nickname;
        this.eatCount = dish.eatCount;
        this.weighQuantity = dish.weighQuantity;
        this.processingCharge = dish.processingCharge;
        this.hasDiet = dish.hasDiet;
        if (dish.tastes!=null) {
            for (NodeTaste taste : dish.tastes) {
                this.tastes.add(new RealmTaste(taste));
            }
        }

    }


    public @NonNull
    NodeSetDish nodeSetDish(){
        NodeSetDish d = new NodeSetDish();
        d.pid = this.pid;
        d.parentKey = this.parentKey;
        d.dishId = this.dishId;
        d.userId = this.userId;
        d.orderTime = this.orderTime;
        d.from = this.from;
        d.minQuantity = this.minQuantity;
        d.unitName = this.unitName;
        d.memberPrice = this.memberPrice;
        d.price = this.price;
        d.dishName = this.dishName;
        d.dishTypeName = this.dishTypeName;
        d.flavorTypeName = this.flavorTypeName;
        d.dishTasteId = this.dishTasteId;
        d.headimgurl = this.headimgurl;
        d.nickname = this.nickname;
        d.eatCount = this.eatCount;
        d.weighQuantity = this.weighQuantity;
        d.weigh = d.weighQuantity > 0;
        d.processingCharge = this.processingCharge;
        d.hasDiet = this.hasDiet;
        if (this.tastes!=null) {
            int size = this.tastes.size();
            int last = size - 1;
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < size; i++) {
                RealmTaste t = this.tastes.get(i);
                d.tastes.add(new NodeTaste(t.id, t.name));
                sb.append(t.name);
                if (i != last) sb.append("、");

            }
            d.tasteNames = sb.toString();

        }
        return d;
    }



    public String getParentKey() {
        return parentKey;
    }

    public void setParentKey(String parentKey) {
        this.parentKey = parentKey;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public long getMealId() {
        return mealId;
    }

    public void setMealId(long mealId) {
        this.mealId = mealId;
    }

    public long getDishId() {
        return dishId;
    }

    public void setDishId(long dishId) {
        this.dishId = dishId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(long orderTime) {
        this.orderTime = orderTime;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getMinQuantity() {
        return minQuantity;
    }

    public void setMinQuantity(int minQuantity) {
        this.minQuantity = minQuantity;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public double getMemberPrice() {
        return memberPrice;
    }

    public void setMemberPrice(double memberPrice) {
        this.memberPrice = memberPrice;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getEatCount() {
        return eatCount;
    }

    public void setEatCount(int eatCount) {
        this.eatCount = eatCount;
    }

    public double getWeighQuantity() {
        return weighQuantity;
    }

    public void setWeighQuantity(double weighQuantity) {
        this.weighQuantity = weighQuantity;
    }

    public double getProcessingCharge() {
        return processingCharge;
    }

    public void setProcessingCharge(double processingCharge) {
        this.processingCharge = processingCharge;
    }


    public RealmList<RealmTaste> getTastes() {
        return tastes;
    }

    public void setTastes(RealmList<RealmTaste> tastes) {
        this.tastes = tastes;
    }

    public String getFlavorTypeName() {
        return flavorTypeName;
    }

    public void setFlavorTypeName(String flavorTypeName) {
        this.flavorTypeName = flavorTypeName;
    }

    public long getDishTasteId() {
        return dishTasteId;
    }

    public void setDishTasteId(long dishTasteId) {
        this.dishTasteId = dishTasteId;
    }

    public boolean isHasDiet() {
        return hasDiet;
    }

    public boolean getHasDiet() {
        return hasDiet;
    }

    public void setHasDiet(boolean hasDiet) {
        this.hasDiet = hasDiet;
    }

    public String getDishTypeName() {
        return dishTypeName;
    }

    public void setDishTypeName(String dishTypeName) {
        this.dishTypeName = dishTypeName;
    }

    public long getTableId() {
        return tableId;
    }

    public void setTableId(long tableId) {
        this.tableId = tableId;
    }
}
