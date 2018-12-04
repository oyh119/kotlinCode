package cn.mealkey.mkservice.base.view


import com.trello.rxlifecycle2.components.RxFragment


/**
 * mvp模式的Fragment基类，集成至Relifecycle库提供的RxFragment
 * 可以实现Rxjava链式操作绑定Fragment生命周期
 */
class BaseMvpFragment : RxFragment()