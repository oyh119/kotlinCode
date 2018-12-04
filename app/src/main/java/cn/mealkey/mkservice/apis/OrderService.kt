package cn.mealkey.mkservice.apis


import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface OrderService {
    @GET("runtime/pad/self/meal/cuspad/dishes/{storeId}")
    fun getCuspadStoreDishes(@Path("storeId") storeId: Long, @Query("mealId") mealId: Long, @Query("orderOrchange") orderOrchange: Int): Single<DishMenu> //orderOrchange 1点菜，2换菜

    @GET("runtime/pad/waiter/meals/getStoreTasteList/{storeId}")
    fun getAllOrderTaboosData(@Path("storeId") storeId: Long, @Query("mealId") mealId: Long): Single<AllTasteInfo>

    @GET("runtime/pad/meals/synergy_order")
    fun isOnlineOrder(@Query("tenantId") tenantId: Long, @Query("storeId") storeId: Long): Single<OrderSyn>

    @GET("runtime/pad/meals/default_dish")
    fun findDefaultDishes(@Query("tenantId") tenantId: Long, @Query("storeId") storeId: Long, @Query("mealId") mealId: Long): Observable<List<DefaultDish>>

}