package cn.mealkey.mkservice.business.init

import cn.mealkey.mkservice.apis.InitService
import cn.mealkey.mkservice.common.StoreHolder
import cn.mealkey.mkservice.common.asyncUntil
import cn.mealkey.mkservice.common.deviceUid
import com.trello.rxlifecycle2.android.ActivityEvent
import javax.inject.Inject

/**
 * Created by wuhaowen on 2018/3/22.
 */
class MainPresenter @Inject constructor(val service:InitService, val view:MainContract.View, private val storeHolder: StoreHolder) :MainContract.Presenter{

    override fun login(storeCode: String, pwd: String,error:(Error) -> Unit, success: () -> Unit) {
        service.activate(storeCode,pwd, deviceUid())
                .doOnNext {
                    it.apply{
                        storeHolder.sessionId = sessionId
                        storeHolder.storeId = storeId
                        storeHolder.storeName = storeName
                        storeHolder.tenantId = tenantId
                        storeHolder.tenantName = tenantName
                        storeHolder.typeId = typeId
                    }
                }
                .asyncUntil(view.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe({
                    success()
                },{
                    error(it)
                })
    }
}