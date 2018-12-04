package cn.mealkey.mkservice.apis

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Region(var regionId:Long = 0,
                  var regionName:String?,
                  var isTackOut:Boolean): Parcelable

data class RegionTables(var regionId: Long,
                        var regionName: String?,
                        var tableList:List<Table>
)

@Parcelize
data class Table(
        var cardReaded: Boolean,
        var regionId: Long = 0,
        var regionName: String? = null,
        var tableId: Long = 0,
        var tableName: String? = null,
        var okDish: Int = 0,
        var dishes: Int = 0,
        var customers: Int = 0,
        var openTable: Boolean = false,
        var tableTypeId: Long = 0,
        var tableTypeName: String? = null,
        var mealId: Long = 0,
        var tableStatus: Int = 0,
        var isLock: Int = 0,
        var isTimeout: Int = 0,
        var orderStatusId: Int = 0, //1未付款 2已付款 3后付款
        var isVirtual: Int = 0,
        var isAddition: Int = 0,
        var isShow: Int = 0,
        var isAddDish: Int = 0,
        var reservationorderId: Long = 0,
        var orderClient: Int = 0,//"点菜设备 1平板，2微信"
        var platformFlag: Int = 0,//外卖平台标记1,饿了么，2美团
        var deliveryTime: String?,//外卖订单时间
        var orderStatus: Int = 0,//	integer	订单状态  1待确认  2叫起  3已确认  4 已完成  5待确认取消  6已确认取消 7已退款  8未完成部分退款 9已完成部分退款
        var orderFailed: Boolean = false,//	boolean	是否下单失败
        var orderFailedDescription: String?, //下单失败说明
        var orderDate: String?//外卖订单时间

): Parcelable


data class OpenTableResp (
        var storeId: Long = 0,
        var storeName: String? = null,
        var regionId: Long = 0,
        var regionName: String? = null,
        var tableId: Long = 0,
        var tableName: String? = null,
        var loginDate: String? = null,
        var mealId: Long = 0
)
