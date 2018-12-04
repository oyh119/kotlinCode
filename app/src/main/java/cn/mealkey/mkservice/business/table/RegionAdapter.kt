package cn.mealkey.mkservice.business.table

import android.graphics.Rect
import android.graphics.Typeface
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.mealkey.mkservice.R
import cn.mealkey.mkservice.apis.Table
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum
import com.nightonke.boommenu.BoomButtons.HamButton
import com.nightonke.boommenu.Piece.PiecePlaceEnum
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_region.*

const val TYPE_CLICK_OPEN = 1
const val TYPE_CLICK_CLOSE = 2
const val TYPE_CLICK_LOCK = 3
const val TYPE_CLICK_UNLOCK = 4
const val TYPE_CLICK_DETIAL = 5
const val TYPE_CLICK_MORE = 6
const val TYPE_CLICK_ORDER = 7
const val TYPE_CLICK_OPEN_ORDER = 8

/**
 * Created by wuhaowen on 2018/3/28.
 */
class RegionAdapter(val listener:RegionAdapterListener):RecyclerView.Adapter<RegionHolder>(){

    var tables:List<Table>? = null

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int) = parent?.let {
        LayoutInflater.from(it.context).inflate(R.layout.item_region,it,false).let { RegionHolder(it) }
    }

    override fun onBindViewHolder(holder: RegionHolder?, position: Int) {
        holder?.setup(tables!![position], listener)
    }

    override fun getItemCount(): Int  = tables?.size?:0

}

interface RegionAdapterListener{
    fun handleTable(table:Table, type:Int):Unit
}

class RegionHolder(override val containerView: View):RecyclerView.ViewHolder(containerView),LayoutContainer {
    fun setup(table : Table, listener: RegionAdapterListener) {
        table.apply {
            txtRegionName.text = tableName
            txt_lock.visibility = if (isLock == 1) View.VISIBLE else View.GONE
            txt_not_paid.visibility = if (tableStatus!=5 && orderStatusId != 2) View.VISIBLE else View.GONE
            txt_wait.visibility = if (orderStatus == 2) View.VISIBLE else View.GONE
            txt_msg.text = when(tableStatus){
                1-> "$okDish/$dishes 道菜"
                3-> "点菜中"
                4-> "已上齐"
                6-> "预定"
                else -> ""
            }
            btnBmb.builders.clear()


            if (tableStatus == 5||tableStatus == 6){
                //开台 开台点菜 //锁定
                btnBmb.piecePlaceEnum = PiecePlaceEnum.HAM_3
                btnBmb.buttonPlaceEnum = ButtonPlaceEnum.HAM_3
                btnBmb.addBuilder(HamButton.Builder()
                        .listener {listener.handleTable(this, TYPE_CLICK_OPEN)}
                        .normalText("开台")
                        .normalColorRes(R.color.c4CAF50)
                        .typeface(Typeface.DEFAULT_BOLD)
                        .imagePadding(Rect(35, 35, 35, 35))
                        .subNormalText("帮顾客开启桌台")
                        .normalImageRes(R.drawable.ic_menu_table))
                btnBmb.addBuilder(HamButton.Builder()
                        .listener {listener.handleTable(this, TYPE_CLICK_OPEN_ORDER)}
                        .normalText("开台点菜")
                        .normalColorRes(R.color.cF44336)
                        .typeface(Typeface.DEFAULT_BOLD)
                        .imagePadding(Rect(35, 35, 35, 35))
                        .subNormalText("开启桌台并开始点菜")
                        .normalImageRes(R.drawable.ic_menu_order))
                if (isLock == 1)
                    btnBmb.addBuilder(HamButton.Builder()
                            .normalText("解锁")
                            .listener {listener.handleTable(this, TYPE_CLICK_UNLOCK)}
                            .normalColorRes(R.color.c00BCD4)
                            .typeface(Typeface.DEFAULT_BOLD)
                            .imagePadding(Rect(35, 35, 35, 35))
                            .subNormalText("解锁桌台，迎宾可分配该桌台")
                            .normalImageRes(R.drawable.ic_menu_unlock))
                else
                    btnBmb.addBuilder(HamButton.Builder()
                            .normalText("锁台")
                            .listener {listener.handleTable(this, TYPE_CLICK_LOCK)}
                            .normalColorRes(R.color.c3F51B5)
                            .typeface(Typeface.DEFAULT_BOLD)
                            .imagePadding(Rect(35, 35, 35, 35))
                            .subNormalText("解锁桌台，迎宾不可分配该桌台")
                            .normalImageRes(R.drawable.ic_menu_lock))
            }else{

                //收台 消费详情 点菜 更多操作

                btnBmb.addBuilder(HamButton.Builder()
                        .normalText("收台")
                        .listener {listener.handleTable(this, TYPE_CLICK_CLOSE)}
                        .normalColorRes(R.color.c607D8B)
                        .typeface(Typeface.DEFAULT_BOLD)
                        .imagePadding(Rect(35, 35, 35, 35))
                        .subNormalText("清台，该桌台可以开启下一个餐次")
                        .normalImageRes(R.drawable.ic_menu_table))
                btnBmb.addBuilder(HamButton.Builder()
                        .normalText("点菜")
                        .listener {listener.handleTable(this, TYPE_CLICK_ORDER)}
                        .normalColorRes(R.color.cF44336)
                        .typeface(Typeface.DEFAULT_BOLD)
                        .imagePadding(Rect(35, 35, 35, 35))
                        .subNormalText("为该桌台点菜")
                        .normalImageRes(R.drawable.ic_menu_order))
                if (tableStatus == 1||tableStatus == 2||tableStatus == 4) {
                    btnBmb.piecePlaceEnum = PiecePlaceEnum.HAM_4
                    btnBmb.buttonPlaceEnum = ButtonPlaceEnum.HAM_4
                    btnBmb.addBuilder(HamButton.Builder()
                            .normalText("消费详情")
                            .listener {listener.handleTable(this, TYPE_CLICK_DETIAL)}
                            .normalColorRes(R.color.c9C27B0)
                            .typeface(Typeface.DEFAULT_BOLD)
                            .imagePadding(Rect(35, 35, 35, 35))
                            .subNormalText("查看该桌台的消费详情")
                            .normalImageRes(R.drawable.ic_menu_detial))
                }else{
                    btnBmb.piecePlaceEnum = PiecePlaceEnum.HAM_3
                    btnBmb.buttonPlaceEnum = ButtonPlaceEnum.HAM_3

                }
                btnBmb.addBuilder(HamButton.Builder()
                        .normalText("更多操作")
                        .listener {listener.handleTable(this, TYPE_CLICK_MORE)}
                        .normalColorRes(R.color.c3F51B5)
                        .typeface(Typeface.DEFAULT_BOLD)
                        .imagePadding(Rect(35, 35, 35, 35))
                        .subNormalText("对该桌台及订单进行更多操作")
                        .normalImageRes(R.drawable.ic_menu_more))


            }

        }

        containerView.setOnClickListener { btnBmb.boom() }
    }
}