package cn.mealkey.mkservice.business.order

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import cn.mealkey.mkservice.R
import cn.mealkey.mkservice.apis.Dish


import cn.mealkey.mkservice.business.order.DishFragment.OnDishFragmentListener

import kotlinx.android.synthetic.main.fragment_dish.view.*


class MyDishRecyclerViewAdapter(
        private val mValues: List<Dish>?,
        private val mListener: OnDishFragmentListener?)
    : RecyclerView.Adapter<MyDishRecyclerViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as Dish
            mListener?.onDishClick(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_dish, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues!![position]
        holder.mIdView.text = item.dishName
        if (item.count == 0){
            holder.mContentView.text = "已售罄"
        }else{
            if (item.memberPrice.compareTo(-1)>0){
                holder.mContentView.text = "￥${item.price} / VIP:￥${item.memberPrice}"
            }else{
                holder.mContentView.text = "￥${item.price}"
            }
        }



        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValues?.size?:0

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mIdView: TextView = mView.item_dish
        val mContentView: TextView = mView.item_price

        override fun toString(): String {
            return super.toString() + " '" + mContentView.text + "'"
        }
    }
}
