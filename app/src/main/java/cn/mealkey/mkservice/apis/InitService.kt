package cn.mealkey.mkservice.apis

import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.*

/**
 * Created by wuhaowen on 2018/3/22.
 */
interface InitService {
    @FormUrlEncoded
    @POST("runtime/pad/initializes/activate")
    fun activate(@Field("code") code: String, @Field("password") password: String,
                 @Field("idCode") uniqueCode: String): Observable<Activation>

    @GET("runtime/pad/initializes/roaders/{storeId}")
    fun getWaiters(@Path("storeId") storeId: Long, @Query("type") type: Int): Single<WaiterWraper>

    @POST("runtime/pad/initializes/login_code")
    fun loginCode(@Body resq: InitLoginResq):Single<InitLoginResp>

}