package cn.mealkey.mkservice.base.view

import android.os.Bundle
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import dagger.android.AndroidInjection

/**
 * mvp模式的Activity基类，集成至Relifecycle库提供的RxAppCompatActivity
 * 可以实现Rxjava链式操作绑定Activity生命周期
 */
open class BaseMvpActivity : RxAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
    }
}
