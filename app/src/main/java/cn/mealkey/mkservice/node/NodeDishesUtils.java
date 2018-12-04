package cn.mealkey.mkservice.node;



import android.annotation.SuppressLint;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

import cn.mealkey.mkservice.common.functions.Action1;
import cn.mealkey.mkservice.node.model.NodeDish;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class NodeDishesUtils {
    @SuppressLint("CheckResult")
    public static void calculatePrice(List<NodeDish> dishes, Action1<String> cb){
        if (dishes == null||dishes.isEmpty()){
            cb.call("0.00");
            return;
        }
        DecimalFormat priceFormat = new java.text.DecimalFormat("0.00");
        Observable.fromIterable(dishes)
                .map(dish->{
                    if (dish.weighQuantity > 0){
                        return new BigDecimal(dish.price)
                                .multiply(new BigDecimal(dish.weighQuantity))
                                .add(new BigDecimal(dish.processingCharge))
                                .multiply(new BigDecimal(dish.eatCount));

                    }else {
                        return new BigDecimal(dish.price)
                                .add(new BigDecimal(dish.processingCharge))
                                .multiply(new BigDecimal(dish.eatCount));
                    }

                }).reduce(new BigDecimal(0), BigDecimal::add)
                .map(priceFormat::format)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(cb::call);
    }

    @SuppressLint("CheckResult")
    public static void calculateCount(List<NodeDish> dishes, Action1<Long> cb){
        if (dishes == null||dishes.isEmpty()){
            cb.call(0L);
            return;
        }
        Observable.fromIterable(dishes)
                .distinct(item -> item.dishId)
                .count()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(cb::call);

    }
}
