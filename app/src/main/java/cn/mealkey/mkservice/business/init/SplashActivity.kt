package cn.mealkey.mkservice.business.init

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import cn.mealkey.mkservice.R
import cn.mealkey.mkservice.base.view.BaseMvpActivity
import cn.mealkey.mkservice.common.StoreHolder
import org.jetbrains.anko.*
import javax.inject.Inject

class SplashActivity : BaseMvpActivity() {

    @Inject
    lateinit var storeHolder:StoreHolder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SplashActivityUI().setContentView(this)
    }


    override fun onResume() {
        super.onResume()
        if (storeHolder.storeId == -1L) {
            startActivity<MainActivity>()
        }else{
            startActivity<LoginActivity>()
        }
    }
}

class SplashActivityUI : AnkoComponent<SplashActivity> {
    override fun createView(ui: AnkoContext<SplashActivity>) = ui.apply {
        verticalLayout {
            backgroundColor = resources.getColor(R.color.colorPrimaryDark)
            topPadding = dip(50)
            bottomPadding = dip(20)

            gravity = Gravity.CENTER_HORIZONTAL


            textView("餐时间"){
                textSize = 24f
            }.lparams(width= wrapContent){
                topMargin = dip(30)
            }

            space().lparams(height = 0){
                weight = 1f
            }

            textView {
                textColor = Color.WHITE
                textSize = px2sp(20)
            }.lparams(width = wrapContent,height = wrapContent)

        }
    }.view
}