package cn.mealkey.mkservice.business.table


import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by wuhaowen on 2018/3/22.
 */
@Module
abstract class TableActivityBuilder {


    @ContributesAndroidInjector(modules = [(RegionModule::class)])
    abstract fun contributeRegionActivity(): RegionActivity
}