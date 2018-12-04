package cn.mealkey.mkservice.business.init

import android.os.Bundle
import cn.mealkey.mkservice.R
import cn.mealkey.mkservice.base.view.BaseMvpActivity
import cn.mealkey.mkservice.common.msg
import org.jetbrains.anko.*
import javax.inject.Inject

class MainActivity : BaseMvpActivity() ,MainContract.View{

    //presenter为非空变量，kotlin要求给默认值，但是我们用的是注入方式赋值，所以使用lateinit标记为延后赋值
    @Inject
    lateinit var presenter:MainContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MainActivityUI().setContentView(this)
    }

    fun login(name: CharSequence?,pwd: CharSequence?){
        presenter.login(name.toString(),pwd.toString(),{
            toast(it.msg())
        }){
            startActivity<LoginActivity>()
        }
    }



}


class MainActivityUI : AnkoComponent<MainActivity> {
    override fun createView(ui: AnkoContext<MainActivity>) = with(ui) {
        val ID_LOGIN = 1
        val ID_PWD = 2
        relativeLayout {
            topPadding = dip(50)
            leftPadding = dip(22)
            rightPadding = dip(22)
            bottomPadding = dip(30)
            textView(R.string.name){
            }.lparams{
                centerHorizontally()
            }

            val txtUser= editText{
                hintResource = R.string.name
            }.lparams(width = matchParent,height = dip(55)){
                topOf(ID_PWD)
                bottomMargin = dip(12)
            }

            val txtPwd = editText{
                id = ID_PWD
                hintResource = R.string.password
            }.lparams(width = matchParent,height = dip(55)){
                topOf(ID_LOGIN)
                bottomMargin = dip(12)
            }
            button {
                id = ID_LOGIN
                textResource = R.string.login
                setOnClickListener{
                    owner.login(txtUser.text,txtPwd.text)
                }
            }.lparams(width = matchParent,height = dip(55)){
                alignParentBottom()
                centerHorizontally()
            }
        }
    }
}
