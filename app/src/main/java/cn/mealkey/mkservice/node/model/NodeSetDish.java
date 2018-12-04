package cn.mealkey.mkservice.node.model;


import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class NodeSetDish{
    public transient String pid;
    public transient String parentKey;
    public long dishId;
    public String userId = "0";
    public long orderTime = 0;

    public int from;
    public String normalDishName;

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
    public boolean weigh;
    public double weighQuantity;
    public double processingCharge;
    public boolean hasDiet;
    public transient String tasteNames = "";
    public Set<NodeTaste> tastes = new HashSet<>();

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
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

    public boolean isWeigh() {
        return weigh;
    }

    public boolean getWeigh() {
        return weigh;
    }

    public void setWeigh(boolean weigh) {
        this.weigh = weigh;
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

    public Set<NodeTaste> getTastes() {
        return tastes;
    }

    public void setTastes(Set<NodeTaste> tastes) {
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

    public String getParentKey() {
        return parentKey;
    }

    public void setParentKey(String parentKey) {
        this.parentKey = parentKey;
    }

    public String getDishTypeName() {
        return dishTypeName;
    }

    public void setDishTypeName(String dishTypeName) {
        this.dishTypeName = dishTypeName;
    }

    public String getTasteNames() {
        return tasteNames;
    }

    public void setTasteNames(String tasteNames) {
        this.tasteNames = tasteNames;
    }

    public String getNormalDishName() {
        return normalDishName;
    }

    public void setNormalDishName(String normalDishName) {
        this.normalDishName = normalDishName;
    }

    @Override
    public String toString() {
        return "NodeSetDish{" +
                "pid='" + pid + '\'' +
                ", parentKey='" + parentKey + '\'' +
                ", dishId=" + dishId +
                ", userId='" + userId + '\'' +
                ", orderTime=" + orderTime +
                ", from=" + from +
                ", normalDishName='" + normalDishName + '\'' +
                ", minQuantity=" + minQuantity +
                ", unitName='" + unitName + '\'' +
                ", memberPrice=" + memberPrice +
                ", price=" + price +
                ", dishName='" + dishName + '\'' +
                ", dishTypeName='" + dishTypeName + '\'' +
                ", flavorTypeName='" + flavorTypeName + '\'' +
                ", dishTasteId=" + dishTasteId +
                ", headimgurl='" + headimgurl + '\'' +
                ", nickname='" + nickname + '\'' +
                ", eatCount=" + eatCount +
                ", weigh=" + weigh +
                ", weighQuantity=" + weighQuantity +
                ", processingCharge=" + processingCharge +
                ", hasDiet=" + hasDiet +
                ", tasteNames='" + tasteNames + '\'' +
                ", tastes=" + tastes +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NodeSetDish)) return false;
        NodeSetDish setDish = (NodeSetDish) o;
        return dishTasteId == setDish.dishTasteId &&
                eatCount == setDish.eatCount &&
                Double.compare(setDish.weighQuantity, weighQuantity) == 0 &&
                Objects.equals(pid, setDish.pid) &&
                Objects.equals(tasteNames, setDish.tasteNames);
    }

    @Override
    public int hashCode() {

        return Objects.hash(pid, dishTasteId, eatCount, weighQuantity, tasteNames);
    }
}
