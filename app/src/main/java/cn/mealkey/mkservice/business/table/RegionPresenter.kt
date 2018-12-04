package cn.mealkey.mkservice.business.table

import cn.mealkey.mkservice.apis.OpenTableResp
import cn.mealkey.mkservice.apis.Region
import cn.mealkey.mkservice.apis.Table
import cn.mealkey.mkservice.apis.TableService
import cn.mealkey.mkservice.common.StoreHolder
import cn.mealkey.mkservice.common.asyncUntil
import com.trello.rxlifecycle2.android.ActivityEvent
import io.reactivex.Observable
import javax.inject.Inject

/**
 * Created by wuhaowen on 2018/3/28.
 */
class RegionPresenter @Inject constructor(val service: TableService,val view: RegionContract.View,val storeHolder: StoreHolder) : RegionContract.Presenter {

    override fun findRegions(error:(Throwable) -> Unit, success:(List<Region>) -> Unit){
        service.findRegions(storeHolder.storeId).concatMap { Observable.fromIterable(it) }
                .filter {!it.isTackOut}.toList()
                .asyncUntil(view.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe({success(it)}, error)
    }

    override fun findTables(regsionIds:String, state: Int, error:(Throwable) -> Unit, success:(List<Table>) -> Unit ){
        service.findRegionsTable(storeHolder.storeId, regsionIds).map { it[0].tableList }
                .concatMap{Observable.fromIterable(it)}
                .filter {
                    if (state == STATE_ALL) true
                    else{
                        when(state){
                            STATE_EMPTY -> it.tableStatus == 5
                            STATE_ORDER  -> it.tableStatus == 3
                            STATE_WAIT -> it.tableStatus == 2
                            STATE_UNFINISHED -> it.tableStatus == 1
                            STATE_FINISHED  -> it.tableStatus == 4
                            STATE_AFTER_PAY -> it.tableStatus != 5 && it.orderStatusId != 2
                            STATE_LOCK -> it.isLock == 1
                            else -> false

                        }
                    }

                }.toList()
                .asyncUntil(view.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe({ success(it) }) { error(it) }
    }


    override fun openTable(tableId:Long, num:Int, error:(Throwable) -> Unit, success:(OpenTableResp) -> Unit ){
        service.openTable(storeHolder.storeId, tableId, num)
                .asyncUntil(view.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(success,error)


    }

}