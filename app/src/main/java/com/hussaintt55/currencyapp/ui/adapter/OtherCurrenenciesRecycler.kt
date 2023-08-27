package com.hussaintt55.currencyapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.hussaintt55.currencyapp.R
import com.hussaintt55.currencyapp.model.adapter.Conversations

class OtherCurrenenciesRecycler(
    listener: ListItemClickListener,
    list: Int,
    my: List<Conversations>
) : RecyclerView.Adapter<OtherCurrenenciesRecycler.viewHolder>() {
    val mOnClick: ListItemClickListener
    private val itemCount: Int
    private val ProjectsList: List<Conversations>
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): viewHolder {
        val context = viewGroup.context
        val LayoutListItem: Int = R.layout.other_currencies_adapter
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(LayoutListItem, viewGroup, false)
        return viewHolder(view)
    }
    override fun onBindViewHolder(viewHolder: viewHolder, i: Int) {
        viewHolder.bind(i)
    }
    override fun getItemCount(): Int {
        return itemCount
    }
    interface ListItemClickListener {
        fun onClickListener(clickedItemIndex: Int)
    }
    inner class viewHolder(itemView: View) : ViewHolder(itemView),
        View.OnClickListener {
        var from: TextView
        var to: TextView
        var finalValue : TextView
        fun bind(possition: Int) {
            from.text = ProjectsList[possition].From
            to.text = ProjectsList[possition].To
            finalValue.text = ProjectsList[possition].finalValue.toString()

        }
        override fun onClick(view: View) {
            val getPossition = adapterPosition
            mOnClick.onClickListener(getPossition)
        }
        init {
            itemView.setOnClickListener(this)
            from = itemView.findViewById(R.id.from)
            to = itemView.findViewById(R.id.to)
            finalValue = itemView.findViewById(R.id.ToValue2)
        }
    }
    init {
        mOnClick = listener
        itemCount = list
        ProjectsList = my
    }
}