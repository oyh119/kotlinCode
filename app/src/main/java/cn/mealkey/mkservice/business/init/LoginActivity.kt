package cn.mealkey.mkservice.business.init

import android.app.ProgressDialog
import android.graphics.Color
import android.os.Bundle
import android.view.KeyEvent
import cn.mealkey.mkservice.R
import cn.mealkey.mkservice.base.view.BaseMvpActivity
import cn.mealkey.mkservice.business.table.RegionActivity
import cn.mealkey.mkservice.common.logd
import cn.mealkey.mkservice.common.msg
import com.github.ybq.android.spinkit.style.DoubleBounce
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import javax.inject.Inject

class LoginActivity : BaseMvpActivity() ,LoginContract.View{

    @Inject
    lateinit var loginPresenter: LoginPresenter

    lateinit var loadingDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        btnLogin.setOnClickListener {
            loadingDialog = indeterminateProgressDialog("loading")
            val doubleBounce = DoubleBounce()
            doubleBounce.setBounds(0,0,100,100)
            doubleBounce.color = Color.WHITE
            loadingDialog.setIndeterminateDrawable(doubleBounce)
            loadingDialog.show()
            loginPresenter.waiterLogin(txtAccount.text.toString(),txtPasswd.text.toString(),{
                toast(it.msg())
                loadingDialog.dismiss()

            }) {
                loadingDialog.dismiss()
                logd("登陆成功")
                startActivity<RegionActivity>()
            }

        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

}
