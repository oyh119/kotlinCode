package cn.mealkey.mkservice.node;


import java.util.List;

import cn.mealkey.mkservice.node.model.NodeDish;

public interface NodeMyDishesChangeListener {
    void myDishesChanged(List<NodeDish> dishes);
}
