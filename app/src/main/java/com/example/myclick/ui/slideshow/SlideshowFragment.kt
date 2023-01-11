package com.example.myclick.ui.slideshow

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.course.db.AppDatabase2
import com.example.course.db.UserData2
import com.example.myclick.Adapters.UserAdapter
import com.example.myclick.R
import com.example.myclick.databinding.FragmentSlideshowBinding
import com.google.android.material.button.MaterialButton
import kotlinx.android.synthetic.main.fragment_dog.view.*
import kotlinx.android.synthetic.main.item_dialog.view.*
import kotlinx.android.synthetic.main.item_select.*
import kotlinx.android.synthetic.main.item_select.view.*
import java.io.File

class SlideshowFragment : Fragment() {

    private var _binding: FragmentSlideshowBinding? = null
    private lateinit var appDatabase2: AppDatabase2
    private lateinit var userAdapter: UserAdapter
    private lateinit var userData2: ArrayList<UserData2>

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSlideshowBinding.inflate(inflater, container, false)
        val root: View = binding.root

        appDatabase2 = AppDatabase2.getInstance2(requireContext())
        val all = appDatabase2.userDaos().getAllUser()
        userData2 = ArrayList()

        userData2.addAll(all)

        binding.apply {
            if(appDatabase2.userDaos().getAllUser().isNotEmpty()){
                iconEmp.visibility = View.GONE
            }else{
                iconEmp.visibility = View.VISIBLE
            }
            userAdapter = UserAdapter(userData2, { data ->

                val builder = AlertDialog.Builder(requireContext())
                val dialogView =
                    LayoutInflater.from(requireContext()).inflate(R.layout.item_select, null, false)

                builder.setView(dialogView)
                val dialog = builder.create()

                val btn = dialogView.findViewById<MaterialButton>(R.id.btnCancle)

                btn.setOnClickListener {
                    dialog.dismiss()
                }
                dialogView.dgTitle.text = data.title
                dialogView.dgSumm.text = data.summa

                val file = File(data.imageUserPath)
                val fromFile = Uri.fromFile(file)
                dialogView.dgImage.setImageURI(fromFile)


                dialog.setCancelable(false)
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialog.show()

            })
            tvSize.text = "${userData2.size} ta "
            reycAll.adapter = userAdapter
            userAdapter.notifyDataSetChanged()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}