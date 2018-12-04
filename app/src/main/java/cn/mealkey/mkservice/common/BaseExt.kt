package cn.mealkey.mkservice.common

import android.app.Application
import android.content.Context
import android.util.Log
import com.trello.rxlifecycle2.LifecycleTransformer
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import java.io.IOException

/**
 * Created by wuhaowen on 2018/3/19.
 */
val SP_NAME = "shared_mealtime_phone"

/**
 * 获取SharedPreferences
 */
fun Context.sp() = getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)

/**
 * 获取SharedPreferences
 */
fun Application.sp() = getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)

/**
 * 获取设备唯一码
 */
fun Context.uid() = deviceUid()

fun <T> Single<T>.async() = this.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()) as Single<T>

fun <T> Single<T>.asyncUntil(transformer: LifecycleTransformer<T>) = this.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).compose(transformer) as Single<T>

fun <T> Observable<T>.async() = this.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()) as Observable<T>

fun <T> Observable<T>.asyncUntil(transformer: LifecycleTransformer<T>) = this.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).compose(transformer) as Observable<T>

fun <T> Flowable<T>.async() = this.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()) as Observable<T>

fun <T> Flowable<T>.asyncUntil(transformer: LifecycleTransformer<T>) = this.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).compose(transformer) as Observable<T>


inline fun Any.logv(block: () -> Any) = Log.v(this.javaClass.simpleName, block().toString())
fun Any.logv(msg: String) = Log.v(this.javaClass.simpleName, msg)

inline fun Any.logd(block: () -> Any) = Log.d(this.javaClass.simpleName, block().toString())
fun Any.logd(msg: String) = Log.d(this.javaClass.simpleName, msg)


inline fun Any.logi(block: () -> Any) = Log.i(this.javaClass.simpleName, block().toString())
fun Any.logi(msg: String) = Log.i(this.javaClass.simpleName, msg)


inline fun Any.logw(block: () -> Any) = Log.w(this.javaClass.simpleName, block().toString())
fun Any.logw(msg: String) = Log.w(this.javaClass.simpleName, msg)


inline fun Any.loge(block: () -> Any) = Log.e(this.javaClass.simpleName, block().toString())
fun Any.loge(msg: String) = Log.e(this.javaClass.simpleName, msg)

fun Throwable.msg(): String {
    return when (this) {
        is HttpException -> {
            when (this.code()) {
                401 -> "未授权"
                400, in 402..499 -> "系统错误：" + this.response()?.message()
                in 500..599 -> "服务器错误：" + this.response()?.message()
                else -> "Http未知错误：" + this.response()?.message()
            }
        }
        is IOException -> "网络错误"
        else -> "未知错误"
    }
}
