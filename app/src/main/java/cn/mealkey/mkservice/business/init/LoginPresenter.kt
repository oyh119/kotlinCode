package cn.mealkey.mkservice.business.init

import cn.mealkey.mkservice.apis.InitLoginResq
import cn.mealkey.mkservice.apis.InitService
import cn.mealkey.mkservice.common.StoreHolder
import cn.mealkey.mkservice.common.asyncUntil
import cn.mealkey.mkservice.common.deviceUid
import com.trello.rxlifecycle2.android.ActivityEvent
import javax.inject.Inject

class LoginPresenter @Inject constructor(val service:InitService, val storeHolder:StoreHolder, val view:LoginContract.View):LoginContract.Presenter {

    override fun waiterLogin(userName: String, pwd: String,error:(Error)->Unit ,success: () -> Unit) {
        val req = InitLoginResq(storeHolder.storeId, storeHolder.tenantId, userName, pwd, true, deviceUid(),"")

        service.loginCode(req)
                .asyncUntil(view.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe({
                    success()
                },{
                    error(it)
                })

    }
}