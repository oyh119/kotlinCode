package cn.mealkey.mkservice.business.init

import dagger.Module
import dagger.Provides

/**
 * Created by wuhaowen on 2018/3/22.
 */
@Module
class MainModule {

    @Provides
    fun provideMainView(mainActivity: MainActivity):MainContract.View = mainActivity

    @Provides
    fun provideMainPresenter(mainPresenter: MainPresenter):MainContract.Presenter = mainPresenter
}