package cn.mealkey.mkservice.di

import cn.mealkey.mkservice.apis.InitService
import cn.mealkey.mkservice.apis.OrderService
import cn.mealkey.mkservice.apis.TableService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Created by wuhaowen on 2018/3/22.
 */
@Module
class ApiModule {
    @Provides
    @Singleton
    fun provideInitService(retrofit: Retrofit) = retrofit.create(InitService::class.java)

    @Provides
    @Singleton
    fun provideTableService(retrofit: Retrofit) = retrofit.create(TableService::class.java)

    @Provides
    @Singleton
    fun provideOrderService(retrofit: Retrofit) = retrofit.create(OrderService::class.java)
}