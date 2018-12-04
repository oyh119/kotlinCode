package cn.mealkey.mkservice.base

import com.trello.rxlifecycle2.LifecycleTransformer
import com.trello.rxlifecycle2.android.ActivityEvent
import retrofit2.HttpException
import java.io.IOException

/**
 * Created by wuhaowen on 2018/3/22.
 */

interface BaseView {
    fun <T> bindToLifecycle(): LifecycleTransformer<T>
    fun <T> bindUntilEvent(event: ActivityEvent): LifecycleTransformer<T>
}

interface BasePresenter {
    fun simpleErrorHandler(t: Throwable, callback: (msg: String) -> Unit) {
        when (t) {
            is HttpException -> {
                when (t.code()) {
                    401 -> callback("未授权")
                    400, in 402..499 -> callback("系统错误：" + t.response()?.message())
                    in 500..599 -> callback("服务器错误：" + t.response()?.message())
                    else -> callback("Http未知错误：" + t.response()?.message())
                }
            }
            is IOException -> callback("网络错误")
            else -> callback("未知错误")
        }
    }

    fun simpleErrorHandlerWithCode(t: Throwable, code: Int = 0, callback: (msg: String, code: Int) -> Unit) {
        when (t) {
            is HttpException -> {
                when (t.code()) {
                    401 -> callback("未授权", code)
                    400, in 402..499 -> callback("系统错误：" + t.response()?.message(), code)
                    in 500..599 -> callback("服务器错误：" + t.response()?.message(), code)
                    else -> callback("Http未知错误：" + t.response()?.message(), code)
                }
            }
            is IOException -> callback("网络错误", code)
            else -> callback("未知错误", code)
        }
    }
}