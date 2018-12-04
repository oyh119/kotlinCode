package cn.mealkey.mkservice.business.order


import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by wuhaowen on 2018/3/22.
 */
@Module
abstract class OrderDishesActivityBuilder {


    @ContributesAndroidInjector(modules = [(OrderDishesModule::class)])
    abstract fun contributeOrderDishesActivity(): OrderDishesActivity
}