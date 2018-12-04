package cn.mealkey.mkservice.node.model;


import java.util.List;
import java.util.Objects;

public class NodeDish extends NodeSetDish{
    public List<NodeSetDish> comboDishList;
    public long comboType = 1;
    public boolean defaultDish;

    public List<NodeSetDish> getComboDishList() {
        return comboDishList;
    }

    public void setComboDishList(List<NodeSetDish> comboDishList) {
        this.comboDishList = comboDishList;
    }

    public long getComboType() {
        return comboType;
    }

    public void setComboType(long comboType) {
        this.comboType = comboType;
    }

    public boolean isDefaultDish() {
        return defaultDish;
    }


    public boolean getDefaultDish() {
        return defaultDish;
    }
    public void setDefaultDish(boolean defaultDish) {
        this.defaultDish = defaultDish;
    }

    @Override
    public String toString() {
        return "NodeDish{" +
                "comboDishList=" + comboDishList +
                ", comboType=" + comboType +
                ", isDefaultDish=" + defaultDish +
                ", pid='" + pid + '\'' +
                ", parentKey='" + parentKey + '\'' +
                ", dishId=" + dishId +
                ", userId='" + userId + '\'' +
                ", orderTime=" + orderTime +
                ", from=" + from +
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
        if (!(o instanceof NodeDish)) return false;
        if (!super.equals(o)) return false;
        NodeDish dish = (NodeDish) o;
        return comboType == dish.comboType &&
                Objects.equals(comboDishList, dish.comboDishList);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), comboDishList, comboType);
    }
}
