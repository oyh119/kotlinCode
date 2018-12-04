package cn.mealkey.mkservice.common

import android.text.TextUtils
import dagger.Lazy
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject

/**
 * Created by wuhaowen on 2017/7/12.
 */


class TokenInterceptor @Inject constructor() : Interceptor {

    @Inject
    lateinit var storeHolder: Lazy<StoreHolder>
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val s = storeHolder.get() as StoreHolder
        if (TextUtils.isEmpty(s.sessionId)) {
            return chain.proceed(originalRequest)
        }
        val builder = originalRequest.newBuilder()
                .addHeader("sessionId", s.sessionId)
        val tenantId = s.tenantId
        if (tenantId != -1L) {
            builder.addHeader("TENANT_ID", tenantId.toString())
        }
        val request = builder.build()
        return chain.proceed(request)
    }

}