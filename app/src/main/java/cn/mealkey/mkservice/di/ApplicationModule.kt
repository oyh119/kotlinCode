package cn.mealkey.mkservice.di

import android.app.Application
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by wuhaowen on 2018/3/20.
 */
@Module
class ApplicationModule {

    @Provides
    @Singleton
    fun provideContext(application: Application) = application
}