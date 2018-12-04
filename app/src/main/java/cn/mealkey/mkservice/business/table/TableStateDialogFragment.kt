package cn.mealkey.mkservice.business.table

import android.content.Context
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import cn.mealkey.mkservice.R
import kotlinx.android.synthetic.main.fragment_tablestate_dialog.*
import kotlinx.android.synthetic.main.fragment_tablestate_dialog_item.view.*

const val STATE_ALL = 0
const val STATE_EMPTY = 1
const val STATE_ORDER = 2
const val STATE_WAIT = 3
const val STATE_UNFINISHED = 4
const val STATE_FINISHED = 5
const val STATE_AFTER_PAY = 6
const val STATE_LOCK = 7

class TableStateDialogFragment : BottomSheetDialogFragment() {



    val STATE_TITLES = listOf("不限","空台","点菜中","叫起","未上齐","已上齐","未付款","锁台")


    private var mListener: Listener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_tablestate_dialog, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        list.layoutManager = LinearLayoutManager(context)
        list.adapter = TableStateAdapter(STATE_TITLES)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val parent = parentFragment
        if (parent != null) {
            mListener = parent as Listener
        } else {
            mListener = context as Listener
        }
    }

    override fun onDetach() {
        mListener = null
        super.onDetach()
    }

    interface Listener {
        fun onTableStateClicked(position: Int)
    }

    private inner class ViewHolder internal constructor(inflater: LayoutInflater, parent: ViewGroup)
        : RecyclerView.ViewHolder(inflater.inflate(R.layout.fragment_tablestate_dialog_item, parent, false)) {

        internal val text: TextView = itemView.text

        init {
            text.setOnClickListener {
                mListener?.let {
                    it.onTableStateClicked(adapterPosition)
                    dismiss()
                }
            }
        }
    }

    private inner class TableStateAdapter internal constructor(private val dataSource: List<String>) : RecyclerView.Adapter<ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(parent.context), parent)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.text.text = dataSource[position]
        }

        override fun getItemCount(): Int {
            return dataSource.size
        }
    }

    companion object {
        fun newInstance(): TableStateDialogFragment = TableStateDialogFragment()
    }
}
