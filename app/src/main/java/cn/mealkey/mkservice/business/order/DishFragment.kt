package cn.mealkey.mkservice.business.order

import android.app.Fragment
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.mealkey.mkservice.R
import cn.mealkey.mkservice.apis.Dish


const val columnCount:Int = 2

class DishFragment : Fragment() {

    var dishes: ArrayList<Dish>? = null


    private var listener: OnDishFragmentListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            dishes = it.getParcelableArrayList(CLASS_DISHES)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_dish_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter = MyDishRecyclerViewAdapter(dishes, listener)
            }
        }
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnDishFragmentListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnDishFragmentListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }


    interface OnDishFragmentListener {
        fun onDishClick(item: Dish)
    }

    companion object {

        const val CLASS_DISHES = "CLASS_DISHES"

        @JvmStatic
        fun newInstance(dishes: ArrayList<Dish>?) =
                DishFragment().apply {
                    arguments = Bundle().apply {
                        putParcelableArrayList(CLASS_DISHES, dishes)
                    }
                }
    }
}
