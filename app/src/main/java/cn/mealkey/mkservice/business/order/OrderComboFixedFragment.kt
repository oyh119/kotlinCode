package cn.mealkey.mkservice.business.order

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import cn.mealkey.mkservice.R
import cn.mealkey.mkservice.apis.ComboDish
import cn.mealkey.mkservice.apis.Dish
import com.trello.rxlifecycle2.components.RxDialogFragment
import kotlinx.android.synthetic.main.fragment_order_combo_fixed.*
import kotlinx.android.synthetic.main.item_combo_dish.view.*

private const val COMBO_DISH = "COMBO_DISH"

class OrderComboFixedFragment : RxDialogFragment() {
    private lateinit var dish: Dish
    private var listener: OrderComboFragment.OnFragmentInteractionListener? = null

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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order_combo_fixed, container, false)
    }

    private inner class ViewComboHolder internal constructor(inflater: LayoutInflater, parent: ViewGroup)
        : RecyclerView.ViewHolder(inflater.inflate(R.layout.item_combo_fixed_dish, parent, false)) {

        internal val txtName: TextView = itemView.item_dish
        internal val txtPrice: TextView = itemView.item_price

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rcyCombo.layoutManager = GridLayoutManager(view.context, 2, LinearLayoutManager.VERTICAL,false)

        rcyCombo.setHasFixedSize(true)
        rcyCombo.adapter = ComboDishesAdapter(dish.comboDishTypeList!![0].comboDishList!!)
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
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        @JvmStatic
        fun newInstance(dish: Dish) =
                OrderComboFixedFragment().apply {
                    arguments = Bundle().apply {
                        putParcelable(COMBO_DISH, dish)
                    }
                }
    }
}
