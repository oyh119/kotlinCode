package cn.mealkey.mkservice.business.table

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import cn.mealkey.mkservice.R
import cn.mealkey.mkservice.apis.Table
import kotlinx.android.synthetic.main.fragment_person_num_dialog.*
import kotlinx.android.synthetic.main.fragment_person_num_dialog_item.view.*

const val OPEN_FOR = "from"
const val TABLE = "table"

class PersonNumDialogFragment : com.trello.rxlifecycle2.components.support.RxDialogFragment() {

    private var listener: OnFragmentInteractionListener? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_person_num_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rcy_num.layoutManager = GridLayoutManager(view.context, 4)
        rcy_num.adapter = numAdapter()

        btnOk.setOnClickListener {
            if (txt_person_num.text.toString().isEmpty())
                return@setOnClickListener
            listener?.onNumInput(txt_person_num.text.toString().toInt(), arguments.getInt(OPEN_FOR), arguments.getParcelable(TABLE))
            dismiss()

        }

        btnClose.setOnClickListener {
            dismiss()
        }

    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    private inner class ViewHolder internal constructor(inflater: LayoutInflater, parent: ViewGroup)
        : RecyclerView.ViewHolder(inflater.inflate(R.layout.fragment_person_num_dialog_item, parent, false)) {

        internal val text: TextView = itemView.txt_Num

        init {
            text.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    txt_person_num.setText("${adapterPosition + 1}")
                }
            }
        }
    }

    private inner class numAdapter internal constructor() : RecyclerView.Adapter<PersonNumDialogFragment.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonNumDialogFragment.ViewHolder {
            return ViewHolder(LayoutInflater.from(parent.context), parent)
        }

        override fun onBindViewHolder(holder: PersonNumDialogFragment.ViewHolder, position: Int) {
            holder.text.text = "${position + 1} 人"
        }

        override fun getItemCount(): Int = 100
    }

    interface OnFragmentInteractionListener {
        fun onNumInput(num: Int, openFor: Int, table: Table)
    }

    companion object {
        @JvmStatic
        fun newInstance(table: Table, openFor: Int) = PersonNumDialogFragment().apply {
            arguments = Bundle().apply {
                putInt(OPEN_FOR, openFor)
                putParcelable(TABLE, table)
            }
        }
    }
}
