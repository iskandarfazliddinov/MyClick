package com.example.myclick.ui.gallery

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.course.db.AppDatabaseSec
import com.example.course.db.UserDataSec
import com.example.myclick.Adapters.UserAdapter
import com.example.myclick.Adapters.UserAdapterSec
import com.example.myclick.R
import com.example.myclick.databinding.FragmentGalleryBinding
import com.google.android.material.button.MaterialButton
import kotlinx.android.synthetic.main.item_select.view.*
import java.io.File

class GalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var appDatabaseSec: AppDatabaseSec
    private lateinit var userDataSec: ArrayList<UserDataSec>
    private lateinit var userAdapter: UserAdapterSec

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        val root: View = binding.root
        appDatabaseSec = AppDatabaseSec.getInstance(requireContext())


        binding.apply {
            if (appDatabaseSec.userDao().getAllUser().isNotEmpty()) {
                iconEmp.visibility = View.GONE
            } else {
                iconEmp.visibility = View.VISIBLE
            }
            val all = appDatabaseSec.userDao().getAllUser()
            userDataSec = ArrayList()
            userDataSec.addAll(all)

            tvSize.text = "${appDatabaseSec.userDao().getAllUserSize().size} ta "

            userAdapter = UserAdapterSec(userDataSec, { data ->
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
            recycGall.adapter = userAdapter
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}