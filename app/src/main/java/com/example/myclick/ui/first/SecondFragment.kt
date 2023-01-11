package com.example.myclick.ui.first

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.navigation.fragment.findNavController
import com.example.course.db.AppDatabaseCheck
import com.example.course.db.UserDataCheck
import com.example.myclick.MainActivity
import com.example.myclick.R
import com.example.myclick.databinding.FragmentSecondBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.fragment_second.view.*
import kotlinx.android.synthetic.main.fragment_second.view.btnEdit
import kotlinx.android.synthetic.main.item_dialog.view.*
import kotlinx.android.synthetic.main.item_edit.*
import kotlinx.android.synthetic.main.item_edit.view.*
import java.io.File
import kotlin.properties.Delegates

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SecondFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SecondFragment : Fragment(), PermissionListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private lateinit var binding: FragmentSecondBinding
    private lateinit var appDatabaseCheck: AppDatabaseCheck
    private lateinit var curruntImagePath: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSecondBinding.inflate(inflater, container, false)



        binding.apply {

            var bool: Boolean = false
            var bools: Boolean = true

            appDatabaseCheck = AppDatabaseCheck.getInstance(requireContext())
            val all = appDatabaseCheck.userDaos().getAllUser()

            tvCardName.text = all.name
            tvSumm.text = all.summa

            curruntImagePath = appDatabaseCheck.userDaos().getAllUser().imageUserPath

            val file = File(curruntImagePath)
            val fromFile = Uri.fromFile(file)
            images.setImageURI(fromFile)

            btnNext.setOnClickListener {
                if (appDatabaseCheck.userDaos().getAll().isNotEmpty()) {
                    startActivity(Intent(requireContext(), MainActivity::class.java))
                    activity?.finish()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Malumotlar to`liq kiritilmagan",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            materialButton.setOnClickListener {
                val bundle = Bundle()
                bundle.putBoolean("bool", bools)
                findNavController().navigate(R.id.action_secondFragment_to_firstFragment, bundle)
            }
            btnDelet.setOnClickListener {
                appDatabaseCheck.userDaos().deletUser(
                    UserDataCheck(
                        appDatabaseCheck.userDaos().getAllUser().id,
                        appDatabaseCheck.userDaos().getAllUser().name,
                        appDatabaseCheck.userDaos().getAllUser().summa,
                        appDatabaseCheck.userDaos().getAllUser().imageUserPath
                    )
                )
                bools = false
                cardsS.visibility = View.INVISIBLE
            }
            btnEdit.setOnClickListener {
                val builder = AlertDialog.Builder(requireContext())
                val dialogView =
                    LayoutInflater.from(requireContext()).inflate(R.layout.item_edit, null, false)

                val name = dialogView.findViewById<EditText>(R.id.editNameE)
                val summ = dialogView.findViewById<EditText>(R.id.editMoneyE)
                val addimg = dialogView.findViewById<CardView>(R.id.addImage)
                val fond = dialogView.findViewById<ImageView>(R.id.imgFon)

                builder.setView(dialogView)
                val dialog = builder.create()

                name.setText(appDatabaseCheck.userDaos().getAllUser().name)
                summ.setText(appDatabaseCheck.userDaos().getAllUser().summa)

                val file = File(curruntImagePath)
                val fromFile = Uri.fromFile(file)
                fond.setImageURI(fromFile)

                dialogView.btnEditE.setOnClickListener {
                    if (name.text.isNotEmpty() && summ.text.isNotEmpty()) {
                        tvSumm.text = summ.text
                        tvCardName.text = name.text

                        val file = File(curruntImagePath)
                        val fromFile = Uri.fromFile(file)
                        images.setImageURI(fromFile)

                        appDatabaseCheck.userDaos().editUser(
                            UserDataCheck(
                                appDatabaseCheck.userDaos().getAllUser().id,
                                name.text.toString(),
                                summ.text.toString(),
                                curruntImagePath

                            )
                        )

                        dialog.dismiss()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Iltimos Malumotlarni to`liq kiriting",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                dialogView.btnCancleE.setOnClickListener {
                    dialog.dismiss()
                }
                addimg.setOnClickListener {
                    loadDexter()
                }

                dialog.setCancelable(false)
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialog.show()
            }
        }

        return binding.root
    }

    private fun loadDexter() {
        Dexter.withContext(requireContext())
            .withPermission(Manifest.permission.CAMERA)
            .withListener(this)
            .check()
    }

    override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
        ImagePicker.with(this)
            .crop()
            .compress(1024)
            .maxResultSize(1080, 1080)
            .start()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ImagePicker.REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            curruntImagePath = data?.data?.path.toString()

        }


    }

    override fun onPermissionDenied(p0: PermissionDeniedResponse?) {

    }

    override fun onPermissionRationaleShouldBeShown(p0: PermissionRequest?, p1: PermissionToken?) {

    }
}