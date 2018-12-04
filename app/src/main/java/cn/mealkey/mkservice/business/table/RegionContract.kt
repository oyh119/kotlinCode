package cn.mealkey.mkservice.business.table

import cn.mealkey.mkservice.apis.OpenTableResp
import cn.mealkey.mkservice.apis.Region
import cn.mealkey.mkservice.apis.Table
import cn.mealkey.mkservice.base.BasePresenter
import cn.mealkey.mkservice.base.BaseView

/**
 * Created by wuhaowen on 2018/3/28.
 */
interface RegionContract {
    interface Presenter : BasePresenter {
        fun findRegions(error:(Throwable) -> Unit, success:(List<Region>) -> Unit)
        fun findTables(regsionIds:String, state: Int, error:(Throwable) -> Unit, success:(List<Table>) -> Unit )
        fun openTable(tableId:Long, num:Int, error:(Throwable) -> Unit, success:(OpenTableResp) -> Unit )
    }
    interface View : BaseView
}