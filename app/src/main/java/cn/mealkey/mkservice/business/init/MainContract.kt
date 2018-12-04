package cn.mealkey.mkservice.business.init

import cn.mealkey.mkservice.base.BasePresenter
import cn.mealkey.mkservice.base.BaseView

/**
 * Created by wuhaowen on 2018/3/22.
 */
interface MainContract {
    interface Presenter : BasePresenter {
        fun login(storeCode: String, pwd: String,error:(Error) -> Unit, block: () -> Unit)
    }
    interface View : BaseView
}