package cn.mealkey.mkservice.business.order

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.CheckBox

import cn.mealkey.mkservice.R
import cn.mealkey.mkservice.apis.Dish
import kotlinx.android.synthetic.main.dialog_order_method_weigh.*
import kotlinx.android.synthetic.main.view_order_dish_method.*

private const val METHOD_DISH = "METHOD_DISH"
private const val WEIGH = 1
private const val METHOD = 2

class OrderMethodWeighDialog private constructor(context: Context) : Dialog(context) {
    lateinit var dish: Dish
    val views = mutableListOf<View>()
    var operation = 0


    constructor(context: Context, dish:Dish) : this(context) {
        this.dish = dish
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_order_method_weigh)

        dish.dishMethodTypeList?.let { operation += METHOD }
        if (dish.isWeigh) operation += WEIGH
        context?.let {
            val inflater = LayoutInflater.from(it)
            when (operation) {
                WEIGH+ METHOD -> {
                    val methodView = inflater.inflate(R.layout.view_order_dish_method, view_pager,false)
                    val weighView = inflater.inflate(R.layout.view_order_dish_weigh, view_pager,false)
                    views.add(methodView)
                    views.add(weighView)

                }
                METHOD -> {
                    val methodView = inflater.inflate(R.layout.view_order_dish_method, view_pager,false)
                    views.add(methodView)

                }
                else -> {
                    val weighView = inflater.inflate(R.layout.view_order_dish_weigh, view_pager,false)
                    views.add(weighView)
                }
            }
            view_pager.adapter = object :PagerAdapter(){

                override fun instantiateItem(container: ViewGroup?, position: Int): Any {
                    val view = views[position]
                    container?.addView(view)
                    return view

                }

                override fun isViewFromObject(view: View?, view1: Any?): Boolean {
                    return view == view1
                }

                override fun getCount(): Int {
                    return if (operation > 2) 2 else 1
                }

                override fun destroyItem(container: ViewGroup?, position: Int, view: Any?) {
                    container?.removeView(view as View)
                }
            }

        }
        setOnShowListener {
            context?.let {
                if (operation >= 2) initMethodView(LayoutInflater.from(it))
            }
        }
    }


    fun initMethodView(inflater: LayoutInflater){
        val methodSize = dish.dishMethodTypeList!!.size
        for (i in 0 until methodSize) {
            val chk = inflater.inflate(R.layout.item_order_method, box_method, false) as CheckBox
            chk.text = dish.dishMethodTypeList!![i].methoadName
            chk.isChecked = i == 0

            box_method.addView(chk)
           // chk.setOnCheckedChangeListener(chkListener)
        }
        val emptyCount = 3 - methodSize % 3
        if (emptyCount != 3) {
            for (i in 0 until emptyCount) {
                val emptyView = inflater.inflate(R.layout.item_order_method_empty, box_method, false)
                box_method.addView(emptyView)
            }
        }
    }




}
