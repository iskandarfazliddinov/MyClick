package com.example.myclick.ui.first

import android.Manifest
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.BoringLayout
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.navigation.fragment.findNavController
import com.example.course.db.AppDatabase2
import com.example.course.db.AppDatabaseCheck
import com.example.course.db.UserData2
import com.example.course.db.UserDataCheck
import com.example.myclick.R
import com.example.myclick.databinding.FragmentFirstBinding
import com.example.myclick.ui.dialog.DogFragment
import com.github.dhaval2404.imagepicker.ImagePicker
import com.github.dhaval2404.imagepicker.ImagePicker.Companion.REQUEST_CODE
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.fragment_dog.view.*
import kotlinx.android.synthetic.main.fragment_dog.view.btnCancle
import kotlinx.android.synthetic.main.fragment_dog.view.btnSave
import kotlinx.android.synthetic.main.item_dialog.*
import kotlinx.android.synthetic.main.item_dialog.view.*
import kotlinx.android.synthetic.main.itemdialogcamera.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FirstFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FirstFragment : Fragment(), PermissionListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var bools: Boolean? = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
            bools = it.getBoolean("bool")
        }
    }

    private lateinit var binding: FragmentFirstBinding
    private lateinit var curruntImagePath: String
    private lateinit var appDatabaseCheck: AppDatabaseCheck


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFirstBinding.inflate(inflater, container, false)
        appDatabaseCheck = AppDatabaseCheck.getInstance(requireContext())


        binding.apply {
            btnExit.setOnClickListener {
                System.exit(-1)
            }
            btnAddNew.setOnClickListener {
                val builder = AlertDialog.Builder(requireContext())
                val dialogView =
                    LayoutInflater.from(requireContext()).inflate(R.layout.item_dialog, null, false)

                builder.setView(dialogView)
                val dialog = builder.create()

                val name = dialogView.findViewById<EditText>(R.id.editName)
                val summ = dialogView.findViewById<EditText>(R.id.editMoney)


                dialogView.btnSave.setOnClickListener {

                    if (::curruntImagePath.isInitialized && name.text.isNotEmpty() && summ.text.isNotEmpty()) {
                        val userDataCheck = UserDataCheck(
                            0, name.text.toString(), summ.text.toString(), curruntImagePath
                        )
                        if (bools == true) {
                            appDatabaseCheck.userDaos().editUser(
                                UserDataCheck(
                                    appDatabaseCheck.userDaos().getAllUser().id,
                                    name.text.toString(),
                                    summ.text.toString(),
                                    curruntImagePath
                                )
                            )
                        }
                        appDatabaseCheck.userDaos().addUser(userDataCheck)

                        findNavController().navigate(
                            R.id.action_firstFragment_to_secondFragment,
                        )
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
                dialogView.btnImageAdds.setOnClickListener {
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
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
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