package cn.mealkey.mkservice.di.components

import android.app.Application
import cn.mealkey.mkservice.MKAplication
import cn.mealkey.mkservice.business.init.InitActivityBuilder
import cn.mealkey.mkservice.business.order.OrderDishesActivityBuilder
import cn.mealkey.mkservice.business.table.TableActivityBuilder
import cn.mealkey.mkservice.di.ApiModule
import cn.mealkey.mkservice.di.ApplicationModule
import cn.mealkey.mkservice.di.RetrofitModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

/**
 * Created by wuhaowen on 2018/3/22.
 */
@Component(modules = [
    (AndroidInjectionModule::class),
    (ApplicationModule::class),
    (RetrofitModule::class),
    (ApiModule::class),
    (InitActivityBuilder::class),
    (TableActivityBuilder::class),
    (OrderDishesActivityBuilder::class)
])
@Singleton
interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(application: MKAplication)
}