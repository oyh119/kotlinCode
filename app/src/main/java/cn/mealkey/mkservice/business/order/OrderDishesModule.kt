package cn.mealkey.mkservice.business.order

import dagger.Module
import dagger.Provides

/**
 * Created by wuhaowen on 2018/3/28.
 */
@Module
class OrderDishesModule {
    @Provides
    fun provideView(activity: OrderDishesActivity): OrderDishesContract.View = activity

    @Provides
    fun providePresenter(presenter: OrderDishesPresenter): OrderDishesContract.Presenter = presenter
}