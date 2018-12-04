package cn.mealkey.mkservice.business.order

import cn.mealkey.mkservice.apis.AllTasteInfo
import cn.mealkey.mkservice.apis.DishMenu
import cn.mealkey.mkservice.base.BasePresenter
import cn.mealkey.mkservice.base.BaseView
import cn.mealkey.mkservice.node.model.NodeDish

interface OrderDishesContract {
    interface Presenter : BasePresenter{
        fun init(mealId: Long, tableId: Long, error: (Throwable) -> Unit, success: (DishMenu?, AllTasteInfo?) -> Unit)
    }
    interface View : BaseView{
        fun onDishesCountChanged(count: Int)
        fun onDishesPriceSum(price: String)
        fun onAllDishesChanged(dishes: List<NodeDish>)
        fun onGlobalTaste(tastes: String)

    }
}