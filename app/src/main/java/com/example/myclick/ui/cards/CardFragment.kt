package com.example.myclick.ui.cards

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
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.graphics.translationMatrix
import androidx.navigation.fragment.findNavController
import com.example.course.db.AppDatabaseCheck
import com.example.course.db.UserDataCheck
import com.example.myclick.MainActivity
import com.example.myclick.R
import com.example.myclick.databinding.FragmentCardBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.fragment_dog.view.*
import kotlinx.android.synthetic.main.fragment_dog.view.btnCancle
import kotlinx.android.synthetic.main.fragment_dog.view.btnSave
import kotlinx.android.synthetic.main.item_dialog.view.*
import kotlinx.android.synthetic.main.item_dialog_edits.*
import kotlinx.android.synthetic.main.item_dialog_edits.view.*
import kotlinx.android.synthetic.main.itemdialogcamera.view.*
import java.io.File
import java.text.DecimalFormat

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CardFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CardFragment : Fragment(), PermissionListener {
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

    private lateinit var binding: FragmentCardBinding
    private lateinit var curruntImagePath: String
    private lateinit var appDatabaseCheck: AppDatabaseCheck

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCardBinding.inflate(inflater, container, false)
        appDatabaseCheck = AppDatabaseCheck.getInstance(requireContext())

        binding.apply {
            curruntImagePath = appDatabaseCheck.userDaos().getAllUser().imageUserPath
            tvCardsName.text = appDatabaseCheck.userDaos().getAllUser().name
            tvCardsSumm.text = appDatabaseCheck.userDaos().getAllUser().summa



            val file = File(curruntImagePath)
            val fromFile = Uri.fromFile(file)
            imgCards.setImageURI(fromFile)

            btnDelets.setOnClickListener {

            }
            btnEdits.setOnClickListener {

                val builder = AlertDialog.Builder(requireContext())
                val dialogView =
                    LayoutInflater.from(requireContext())
                        .inflate(R.layout.item_dialog_edits, null, false)

                val name = dialogView.findViewById<EditText>(R.id.editName)
                val summ = dialogView.findViewById<EditText>(R.id.editMoney)
                val img = dialogView.findViewById<ImageView>(R.id.imgDg)

                val file = File(curruntImagePath)
                val fromFile = Uri.fromFile(file)
                img.setImageURI(fromFile)

                builder.setView(dialogView)
                val dialog = builder.create()

                name.setText(appDatabaseCheck.userDaos().getAllUser().name)
                summ.setText(appDatabaseCheck.userDaos().getAllUser().summa)

                dialogView.btnSave.setOnClickListener {

                    if (name.text.isNotEmpty() && summ.text.isNotEmpty()) {

                        appDatabaseCheck.userDaos().editUser(
                            UserDataCheck(
                                appDatabaseCheck.userDaos().getAllUser().id,
                                name = name.text.toString(),
                                summa = summ.text.toString(),
                                curruntImagePath
                            )
                        )
                        Toast.makeText(requireContext(), "Card Edits", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(requireContext(), MainActivity::class.java))

                        dialog.dismiss()

                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Iltimos malumotlarni to`liq kiriting",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                dialogView.btnCancle.setOnClickListener {
                    dialog.dismiss()
                }
                dialogView.btnImageAdd.setOnClickListener {
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

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CardFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CardFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
        val builder = AlertDialog.Builder(requireContext())
        val dialogView =
            LayoutInflater.from(requireContext()).inflate(R.layout.itemdialogcamera, null, false)

        builder.setView(dialogView)
        val dialog = builder.create()

        dialogView.btnCameraOpen.setOnClickListener {

            ImagePicker.with(this)
                .cameraOnly()
                .start()
            dialog.dismiss()
        }

        dialogView.btnGallOpen.setOnClickListener {
            ImagePicker.with(this)
                .galleryOnly()
                .crop()
                .start()

            dialog.dismiss()
        }
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ImagePicker.REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            curruntImagePath = data?.data?.path.toString()
        }

    }

    override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
        val dialog = AlertDialog.Builder(requireContext())
        dialog.setTitle("Cameraga ruhsat berish")
        dialog.setMessage("Siz cameraga ruxsat bermasangiz bu dastur uchun camereadan foydalana olmaysiz !")
        dialog.setPositiveButton("RUXSAT BERISH") { _, _ ->
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.CAMERA),
                1
            )
        }
        dialog.setNegativeButton("RAD ETISH") { _, _ ->
            dialog.create().dismiss()
        }
        dialog.show()

    }

    override fun onPermissionRationaleShouldBeShown(p0: PermissionRequest?, p1: PermissionToken?) {

    }
}