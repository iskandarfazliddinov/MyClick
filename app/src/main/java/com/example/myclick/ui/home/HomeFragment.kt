package com.example.myclick.ui.home

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.course.db.*
import com.example.myclick.Adapters.UserAdapters
import com.example.myclick.R
import com.example.myclick.databinding.FragmentHomeBinding
import com.google.android.material.button.MaterialButton
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.item_select.view.*
import java.io.File
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*
import kotlin.collections.ArrayList

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var userAdapters: UserAdapters
    private lateinit var userData: ArrayList<UserData>
    private lateinit var appDatabase: AppDatabase
    private lateinit var appDatabaseSec: AppDatabaseSec
    private lateinit var appDatabaseCheck: AppDatabaseCheck
    private lateinit var appDatabase2: AppDatabase2

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        appDatabase = AppDatabase.getInstance(requireContext())
        appDatabaseSec = AppDatabaseSec.getInstance(requireContext())
        appDatabaseCheck = AppDatabaseCheck.getInstance(requireContext())
        appDatabase2 = AppDatabase2.getInstance2(requireContext())

        userData = ArrayList()

        binding.apply {
            if (appDatabase2.userDaos().getAllUser().isEmpty()) {
                iconEmp.visibility = View.VISIBLE
            } else {
                iconEmp.visibility = View.GONE
            }

            val dec = DecimalFormat("###,###,###,###,###", DecimalFormatSymbols(Locale.ENGLISH))

            progressBarHom.max = (appDatabaseCheck.userDaos().getAllUser().summa.toInt())
            progressBarHom.setProgress(appDatabase2.userDaos().getSumms())

            tvProg.text = dec.format(appDatabase2.userDaos().getSumms()).replace(",", " ")

            tvMinus.text = dec.format(
                appDatabaseCheck.userDaos().getAllUser().summa.toInt() - appDatabase2.userDaos()
                    .getSumms()
            ).replace(",", " ")

            tvSh.text = dec.format(
                appDatabaseCheck.userDaos().getAllUser().summa.toInt() - appDatabase2.userDaos()
                    .getSumms()
            ).replace(",", " ")


            btnAdds.setOnClickListener {
                findNavController().navigate(R.id.action_nav_home_to_dogFragment2)
            }
        }

        binding.apply {
            userAdapters = UserAdapters(userData, {
                val builder = AlertDialog.Builder(requireContext())
                val dialogView =
                    LayoutInflater.from(requireContext()).inflate(R.layout.item_select, null, false)

                builder.setView(dialogView)
                val dialog = builder.create()

                val btn = dialogView.findViewById<MaterialButton>(R.id.btnCancle)

                btn.setOnClickListener {
                    dialog.dismiss()
                }
                dialogView.dgTitle.text = it.title
                dialogView.dgSumm.text = it.summa

                val file = File(it.imageUserPath)
                val fromFile = Uri.fromFile(file)
                dialogView.dgImage.setImageURI(fromFile)


                dialog.setCancelable(false)
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialog.show()
            }, { it, str ->
                if (str == "Add") {
                    appDatabaseSec.userDao().addUser(
                        UserDataSec(
                            0,
                            it.title,
                            it.summa,
                            it.imageUserPath
                        )
                    )
                    appDatabase.userDaos().editUser(
                        UserData(
                            it.id, it.title, it.summa, it.imageUserPath, "Add"
                        )
                    )
                    Toast.makeText(requireContext(), "Add", Toast.LENGTH_SHORT).show()
                } else {

                    appDatabase.userDaos().editUser(
                        UserData(
                            it.id, it.title, it.summa, it.imageUserPath, "Del"
                        )
                    )
                }

            })
            recycHome.adapter = userAdapters
        }

        appDatabase.userDaos().getAllUser()
            .subscribeOn(Schedulers.io())
            .subscribe {
                userAdapters.submitList(it)
            }

        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}