package cn.mealkey.mkservice.business.table

import dagger.Module
import dagger.Provides

/**
 * Created by wuhaowen on 2018/3/28.
 */
@Module
class RegionModule {
    @Provides
    fun provideRegionView(activity: RegionActivity): RegionContract.View = activity

    @Provides
    fun provideRegionPresenter(presenter: RegionPresenter): RegionContract.Presenter = presenter
}