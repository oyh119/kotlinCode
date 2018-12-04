package cn.mealkey.mkservice.business.init

import cn.mealkey.mkservice.base.BasePresenter
import cn.mealkey.mkservice.base.BaseView

interface LoginContract {

    interface Presenter : BasePresenter {
        fun waiterLogin(userName: String, pwd: String,error:(Error)->Unit ,success: () -> Unit)
    }
    interface View : BaseView
}