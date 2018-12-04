package cn.mealkey.mkservice.business.init

import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by wuhaowen on 2018/3/22.
 */
@Module
abstract class InitActivityBuilder {
    @ContributesAndroidInjector(modules = [(MainModule::class)])
    abstract fun contributeMainActivity(): MainActivity

    @ContributesAndroidInjector(modules = [(SplashModule::class)])
    abstract fun contributeSplashActivity(): SplashActivity

    @ContributesAndroidInjector(modules = [(LoginModule::class)])
    abstract fun contributeLoginActivity(): LoginActivity
}