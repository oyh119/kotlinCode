package cn.mealkey.mkservice.business.order

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.TextView

import cn.mealkey.mkservice.R
import cn.mealkey.mkservice.apis.Dish
import cn.mealkey.mkservice.apis.ComboClass
import cn.mealkey.mkservice.apis.ComboDish
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper
import com.trello.rxlifecycle2.components.RxDialogFragment
import kotlinx.android.synthetic.main.fragment_order_combo.*
import kotlinx.android.synthetic.main.item_combo_dish.view.*
import kotlinx.android.synthetic.main.item_order_combo.view.*
import android.graphics.Color
import android.util.DisplayMetrics
import android.graphics.drawable.ColorDrawable
import android.view.*


private const val COMBO_DISH = "COMBO_DISH"


class OrderComboFragment : RxDialogFragment() {
    private lateinit var dish:Dish
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            dish = it.getParcelable(COMBO_DISH)
        }
    }

    override fun onStart() {
        super.onStart()
        dialog.setCanceledOnTouchOutside(false)
        val win = dialog.window
        win.setBackgroundDrawable(ColorDrawable(Color.BLACK))
        val dm = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(dm)
        val params = win.attributes
        params.gravity = Gravity.CENTER
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        params.height = ViewGroup.LayoutParams.MATCH_PARENT
        win.attributes = params
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        return inflater.inflate(R.layout.fragment_order_combo, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rcyCombo.layoutManager = LinearLayoutManager(view.context)
        rcyCombo.setHasFixedSize(true)
        rcyCombo.adapter = ComboAdapter(dish.comboDishTypeList!!)
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
//        if (context is OnFragmentInteractionListener) {
//            listener = context
//        } else {
//            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
//        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }


    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {

        @JvmStatic
        fun newInstance(dish: Dish) =
                OrderComboFragment().apply {
                    arguments = Bundle().apply {
                        putParcelable(COMBO_DISH, dish)
                    }
                }
    }

    private inner class ViewHolder internal constructor(inflater: LayoutInflater, parent: ViewGroup)
        : RecyclerView.ViewHolder(inflater.inflate(R.layout.item_order_combo, parent, false)) {

        internal val txtClass: TextView = itemView.txtClass
        internal val rcyDishes: RecyclerView = itemView.rcyComboDishes

    }

    private inner class ComboAdapter internal constructor(private val dataSource: List<ComboClass>) : RecyclerView.Adapter<ViewHolder>(), GravitySnapHelper.SnapListener {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(parent.context), parent)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.txtClass.text = dataSource[position].itemGroupName
            holder.rcyDishes.layoutManager = LinearLayoutManager(holder
                    .rcyDishes.context, LinearLayoutManager.HORIZONTAL, false)
            GravitySnapHelper(Gravity.END, false, this).attachToRecyclerView(holder.rcyDishes)
            dataSource[position].comboDishList?.run {
                holder.rcyDishes.adapter = ComboDishesAdapter(this)

            }
        }

        override fun getItemCount(): Int {
            return dataSource.size
        }


        override fun onSnap(position: Int) {
            //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }
    private inner class ViewComboHolder internal constructor(inflater: LayoutInflater, parent: ViewGroup)
        : RecyclerView.ViewHolder(inflater.inflate(R.layout.item_combo_dish, parent, false)) {

        internal val txtName: TextView = itemView.item_dish
        internal val txtPrice: TextView = itemView.item_price

    }
    private inner class ComboDishesAdapter internal constructor(private val dishes: List<ComboDish>): RecyclerView.Adapter<ViewComboHolder>(){

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewComboHolder {
            return ViewComboHolder(LayoutInflater.from(parent.context), parent)

        }

        override fun getItemCount(): Int = dishes.size


        override fun onBindViewHolder(holder: ViewComboHolder, position: Int) {
            val item = dishes[position]
            holder.txtName.text = item.dishName
            if (item.count == 0){
                holder.txtPrice.text = "已售罄"
            }else {
                if (item.memberPrice.compareTo(-1)>0){
                    holder.txtPrice.text = "￥${item.price} / VIP:￥${item.memberPrice}"
                }else{
                    holder.txtPrice.text = "￥${item.price}"
                }
            }


        }
    }

}
