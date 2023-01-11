package com.example.myclick.Adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.course.db.UserData
import com.example.course.db.UserData2
import com.example.myclick.databinding.ItemRecycBinding
import java.io.File
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*
import kotlin.collections.ArrayList

class UserAdapters(
    val list: ArrayList<UserData>,
    val OnItemClick: (UserData) -> Unit,
    val OnItemClicks: (UserData,str:String) -> Unit
) : ListAdapter<UserData, UserAdapters.VH>(MyDiffUtil()) {

    inner class VH(val itemRecycBinding: ItemRecycBinding) :
        RecyclerView.ViewHolder(itemRecycBinding.root) {
        fun onBind(data: UserData, position: Int) {
            itemRecycBinding.apply {

                if(data.click == "Add"){
                    btnLoveAdd.visibility = View.INVISIBLE
                    btnLoveAdds.visibility = View.VISIBLE
                }else{
                    btnLoveAdd.visibility = View.VISIBLE
                    btnLoveAdds.visibility = View.INVISIBLE
                }

                val dec = DecimalFormat("###,###,###,###,###", DecimalFormatSymbols(Locale.ENGLISH))

                tvSumm.text = dec.format(data.summa.toInt()).replace(",", " ")
                tvTitle.text = data.title

                val file = File(data.imageUserPath)
                val fromFile = Uri.fromFile(file)
                images.setImageURI(fromFile)

                btnCard.setOnClickListener {
                    OnItemClick.invoke(data)
                }

                btnLoveAdd.setOnClickListener {
                    OnItemClicks.invoke(data,"Add")

                    btnLoveAdd.visibility = View.INVISIBLE
                    btnLoveAdds.visibility = View.VISIBLE
                }

                btnLoveAdds.setOnClickListener {
                    OnItemClicks.invoke(data,"Del")

                    btnLoveAdd.visibility = View.VISIBLE
                    btnLoveAdds.visibility = View.INVISIBLE
                }

            }
        }
    }

    class MyDiffUtil : DiffUtil.ItemCallback<UserData>() {
        override fun areItemsTheSame(oldItem: UserData, newItem: UserData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: UserData, newItem: UserData): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(ItemRecycBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.onBind(getItem(position), position)
    }


}