package com.example.myclick.Adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.course.db.UserData2
import com.example.myclick.databinding.ItemRecycAllBinding
import com.example.myclick.databinding.ItemRecycBinding
import java.io.File
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*
import kotlin.collections.ArrayList

class UserAdapter(
    val list: ArrayList<UserData2>,
    val OnItemClick: (UserData2) -> Unit
) : RecyclerView.Adapter<UserAdapter.VH>() {

    inner class VH(val itemRecycBinding: ItemRecycAllBinding) :
        RecyclerView.ViewHolder(itemRecycBinding.root) {
        fun onBind(data: UserData2, position: Int) {
            itemRecycBinding.apply {
                val dec = DecimalFormat("###,###,###,###,###", DecimalFormatSymbols(Locale.ENGLISH))

                tvSumm.text = dec.format(data.summa.toInt()).replace(",", " ")
                tvTitle.text = data.title

                val file = File(data.imageUserPath)
                val fromFile = Uri.fromFile(file)
                images.setImageURI(fromFile)

                btnCard.setOnClickListener {
                    OnItemClick.invoke(data)
                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(ItemRecycAllBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.onBind(list[position], position)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}