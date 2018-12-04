package cn.mealkey.mkservice.apis

import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.*

interface TableService {
    @GET("runtime/pad/tables/region/{storeId}")
    fun findRegions(@Path("storeId") storeId: Long): Observable<ArrayList<Region>>

    @GET("middleware/regions/{storeId}/{regionIds}")
    fun findRegionsTable(@Path("storeId") storeId: Long, @Path("regionIds") regionIds: String): Observable<ArrayList<RegionTables>>

    @FormUrlEncoded
    @POST("runtime/pad/tables/open")
    abstract fun openTable(@Field("storeId") storeId: Long,
                           @Field("tableId") tableId: Long,
                           @Field("count") count: Int): Single<OpenTableResp>
}