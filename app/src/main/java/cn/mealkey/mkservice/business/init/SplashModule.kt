package cn.mealkey.mkservice.business.init

import dagger.Module
import dagger.Provides

@Module
class SplashModule {
    @Provides
    fun provideSplashView(activity:SplashActivity) = activity
}