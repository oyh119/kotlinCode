package cn.mealkey.mkservice.node;


import java.util.List;

import cn.mealkey.mkservice.node.model.NodeTaste;

public interface NodeGlobalTasteChangedListener {
    void onGlobalTasteChanged(List<NodeTaste> tastes);

}
