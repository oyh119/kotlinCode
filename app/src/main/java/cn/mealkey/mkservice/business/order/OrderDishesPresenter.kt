package cn.mealkey.mkservice.business.order

import android.annotation.SuppressLint
import cn.mealkey.mkservice.apis.*
import cn.mealkey.mkservice.common.StoreHolder
import cn.mealkey.mkservice.common.asyncUntil
import cn.mealkey.mkservice.node.NodeDishSynchronizer
import cn.mealkey.mkservice.node.model.NodeDish
import cn.mealkey.mkservice.node.model.NodeTaste
import com.trello.rxlifecycle2.android.ActivityEvent
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers

import io.reactivex.rxkotlin.Singles
import io.reactivex.rxkotlin.toObservable
import io.reactivex.schedulers.Schedulers
import java.math.BigDecimal
import java.util.HashMap
import javax.inject.Inject

class WaiterOrderInfo {
    var waiterMealClass: DishMenu? = null
    var waiterAllTaste: AllTasteInfo? = null
    var selctedTaste: MutableList<NodeTaste> = ArrayList()
    var synergyOrder: Boolean = false


}

class OrderDishesPresenter @Inject constructor(val service: OrderService, val storeHolder: StoreHolder, val view: OrderDishesContract.View, var dishSynchronizer: NodeDishSynchronizer) : OrderDishesContract.Presenter {


    var allDishes: MutableList<Dish> = ArrayList()
    var onLineOrder = false
    var globalTastes: List<NodeTaste>? = null

    override fun init(mealId: Long, tableId: Long, error: (Throwable) -> Unit, success: (DishMenu?, AllTasteInfo?) -> Unit) {
        Singles.zip(service.getCuspadStoreDishes(storeId = storeHolder.storeId, mealId = mealId, orderOrchange = 1),
                service.getAllOrderTaboosData(storeId = storeHolder.storeId, mealId = mealId),
                service.isOnlineOrder(storeHolder.tenantId, storeHolder.storeId)
        ) { menu: DishMenu, alltaste: AllTasteInfo, sync: OrderSyn ->
            WaiterOrderInfo().apply {
                waiterMealClass = menu
                waiterAllTaste = alltaste
                synergyOrder = sync.isSynergyOrder
                for (i in alltaste.tasteList) {
                    if (i.isChecked) {
                        if (i.tasteId == -1L) {
                            selctedTaste.add(NodeTaste(i.tasteId, alltaste.customTaste))
                            continue
                        }
                        selctedTaste.add(NodeTaste(i.tasteId, i.tasteName))
                    }
                    i.isChecked = false
                }
            }

        }.asyncUntil(view.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe({
                    it.waiterMealClass?.typeList?.toObservable()!!.concatMap { Observable.fromIterable(it.dishList) }
                            .toList().subscribe { dishes ->
                                run {
                                    allDishes.addAll(dishes)
                                }
                                dishSynchronizer.saveGlobalTastes(mealId, tableId, it.selctedTaste)
                                success(it.waiterMealClass, it.waiterAllTaste)
                            }

                }) { error(it) }

    }



    fun refreshCount(selectedItems: List<NodeDish>?) {
        if (selectedItems == null) {
            view.onDishesCountChanged(0)
            return
        }
        Flowable.fromIterable(selectedItems)
                .distinct { it.dishId }
                .count()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(view.bindToLifecycle())
                .subscribe { i: Long -> view.onDishesCountChanged(i.toInt()) }
    }

    fun refreshPrice(selectedItems: List<NodeDish>?) {
        if (selectedItems == null) {
            view.onDishesPriceSum("¥0")
            return
        }
        Observable.fromIterable(selectedItems)
                .map { i ->
                    if (i.isWeigh) {
                        i.price.toBigDecimal()
                                .multiply(i.weighQuantity.toBigDecimal())
                                .add(i.processingCharge.toBigDecimal())
                                .multiply(i.eatCount.toBigDecimal())

                    } else {
                        i.price.toBigDecimal().add(i.processingCharge.toBigDecimal()).multiply(i.eatCount.toBigDecimal())
                    }

                }
                .reduce(BigDecimal(0)) { sum, next -> sum.add(next) }
                .map { s -> "¥" + java.text.DecimalFormat("0.00").format(s) }
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(view.bindToLifecycle())
                .subscribe { it -> view.onDishesPriceSum(it) }
    }

    @SuppressLint("DefaultLocale")
    fun initDishSynchronizer(mealId: Long, tableId: Long, tableName: String, synOrder: Boolean, isRefund: Boolean) {
        onLineOrder = synOrder && !isRefund
        val option = NodeDishSynchronizer.Option()
        option.synergyOrder = onLineOrder
        option.form = 1
        option.mealId = mealId
        option.observeMyDishesChanged = false
        option.sortByFrom = true
        option.storeId = storeHolder.storeId
        option.tableId = tableId
        option.tableName = tableName
        option.userId = "0"
        option.userName = "客服平板-$tableName"
        dishSynchronizer.setAllDishesChangeListener { dishes ->
            view.onAllDishesChanged(dishes)
            refreshCount(dishes)
            refreshPrice(dishes)
        }
        dishSynchronizer.setGlobalTasteChangedListener { tastes ->
            globalTastes = tastes
            val sb = StringBuilder()
            for (i in 0 until tastes.size) {
                sb.append(tastes[i].name)
                if (i != tastes.size - 1)
                    sb.append("，")
            }
            view.onGlobalTaste(sb.toString())
        }

        dishSynchronizer.start(option)
        if (!synOrder && !isRefund) {
            service.findDefaultDishes(storeHolder.tenantId, storeHolder.storeId, mealId)
                    .flatMap { Observable.fromIterable(it) }
                    .map { dish ->
                        val nodeDish = NodeDish()
                        nodeDish.defaultDish = true
                        nodeDish.dishId = dish.dishId
                        nodeDish.userId = "0"
                        nodeDish.orderTime = 0
                        nodeDish.from = 1
                        nodeDish.minQuantity = dish.minQuantity
                        nodeDish.unitName = dish.unitName
                        nodeDish.memberPrice = dish.memberPrice.toDouble()
                        nodeDish.price = dish.price.toDouble()
                        nodeDish.dishName = dish.dishName
                        nodeDish.dishTypeName = dish.dishTypeName
                        nodeDish.processingCharge = dish.processingCharge.toDouble()
                        nodeDish.eatCount = dish.eatCount
                        nodeDish
                    }
                    .toList()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe { it -> dishSynchronizer.setDefaultDishes(it) }
        }
    }

}