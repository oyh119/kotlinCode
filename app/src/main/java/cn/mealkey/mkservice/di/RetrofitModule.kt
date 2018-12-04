package cn.mealkey.mkservice.di

import android.text.TextUtils
import cn.mealkey.mkservice.BuildConfig
import cn.mealkey.mkservice.common.TokenInterceptor
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Created by wuhaowen on 2018/3/19.
 */
@Module
class RetrofitModule {

    private var floatJsonSerializer = JsonDeserializer<Float> { json, _, _ ->
        try {
            return@JsonDeserializer if (TextUtils.isEmpty(json.asString)) 0.0f else json.asFloat
        } catch (e: NumberFormatException) {
            return@JsonDeserializer 0.0f
        }
    }

    private var doubleJsonSerializer = JsonDeserializer<Double> { json, _, _ ->
        try {
            return@JsonDeserializer if (TextUtils.isEmpty(json.asString)) 0.0 else json.asDouble
        } catch (e: NumberFormatException) {
            return@JsonDeserializer 0.0
        }
    }

    private var longJsonSerializer = JsonDeserializer<Long> { json, _, _ ->
        try {
            return@JsonDeserializer if (TextUtils.isEmpty(json.asString)) 0 else json.asLong
        } catch (e: NumberFormatException) {
            return@JsonDeserializer 0
        }
    }

    private var intJsonSerializer = JsonDeserializer<Int> { json, _, _ ->
        try {
            return@JsonDeserializer if (TextUtils.isEmpty(json.asString)) 0 else json.asInt
        } catch (e: NumberFormatException) {
            return@JsonDeserializer 0
        }
    }

    @Provides
    @Singleton
    fun proviceHttpLoggingInterceptor() = run {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        httpLoggingInterceptor
    }

    @Provides
    @Singleton
    fun provideOkhtppClient(httpLoggingInterceptor: HttpLoggingInterceptor, tokenInterceptor:TokenInterceptor) = OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .addNetworkInterceptor(tokenInterceptor)
            .addNetworkInterceptor(StethoInterceptor())
            .addInterceptor(httpLoggingInterceptor)
            .readTimeout(8, TimeUnit.SECONDS)
            .writeTimeout(8, TimeUnit.SECONDS)
            .build()

    @Provides
    @Singleton
    fun provideConverterFactory():GsonConverterFactory{
        val gson = GsonBuilder()
                .registerTypeAdapter(Int::class.java, intJsonSerializer)
                .registerTypeAdapter(Long::class.java, longJsonSerializer)
                .registerTypeAdapter(Float::class.java, floatJsonSerializer)
                .registerTypeAdapter(Double::class.java, doubleJsonSerializer)
                .registerTypeAdapter(Int::class.javaPrimitiveType, intJsonSerializer)
                .registerTypeAdapter(Long::class.javaPrimitiveType, longJsonSerializer)
                .registerTypeAdapter(Float::class.javaPrimitiveType, floatJsonSerializer)
                .registerTypeAdapter(Double::class.javaPrimitiveType, doubleJsonSerializer)
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create()
        return GsonConverterFactory.create(gson)
    }



    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient, factory: GsonConverterFactory) = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(client)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(factory)
            .build()


}