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
import cn.mealkey.mkservice.apis.Region
import kotlinx.android.synthetic.main.fragment_item_region_dialog.*
import kotlinx.android.synthetic.main.fragment_item_region_dialog_item.view.*

const val REGIONS = "regions"

class RegionsDialogFragment : BottomSheetDialogFragment() {
    private var mListener: Listener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_item_region_dialog, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        list.layoutManager = LinearLayoutManager(context)
        list.adapter = regionAdapter(arguments?.getParcelableArrayList(REGIONS))
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
        fun onRegionClicked(position: Int)
    }

    private inner class ViewHolder internal constructor(inflater: LayoutInflater, parent: ViewGroup)
        : RecyclerView.ViewHolder(inflater.inflate(R.layout.fragment_item_region_dialog_item, parent, false)) {

        internal val text: TextView = itemView.text

        init {
            text.setOnClickListener {
                mListener?.let {
                    it.onRegionClicked(adapterPosition)
                    dismiss()
                }
            }
        }
    }

    private inner class regionAdapter internal constructor(private val regions: ArrayList<Region>?) : RecyclerView.Adapter<ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(parent.context), parent)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.text.text = regions!![position].regionName
        }

        override fun getItemCount(): Int {
            return regions?.size?:0
        }
    }

    companion object {

        fun newInstance(regions: ArrayList<Region>): RegionsDialogFragment =
                RegionsDialogFragment().apply {
                    arguments = Bundle().apply {
                        putParcelableArrayList(REGIONS, regions)
                    }
                }

    }
}
