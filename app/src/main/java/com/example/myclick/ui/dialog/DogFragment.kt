package com.example.myclick.ui.dialog

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.fragment.app.DialogFragment
import com.example.course.db.AppDatabase
import com.example.course.db.AppDatabase2
import com.example.course.db.UserData
import com.example.course.db.UserData2
import com.example.myclick.Adapters.UserAdapter
import com.example.myclick.Adapters.UserAdapters
import com.example.myclick.MainActivity
import com.example.myclick.R
import com.example.myclick.databinding.FragmentDogBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.itemdialogcamera.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DogFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DogFragment : DialogFragment(), PermissionListener {
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

    private lateinit var binding: FragmentDogBinding
    private lateinit var appDatabase2: AppDatabase2
    private lateinit var curruntImagePath2: String
    private lateinit var appDatabase: AppDatabase
    private lateinit var curruntImagePath: String
    private lateinit var userData: ArrayList<UserData>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentDogBinding.inflate(inflater, container, false)
        appDatabase2 = AppDatabase2.getInstance2(requireContext())
        appDatabase = AppDatabase.getInstance(requireContext())

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.setCancelable(false)



        binding.apply {
            userData = ArrayList()

            btnCancle.setOnClickListener {

                startActivity(Intent(requireContext(), MainActivity::class.java))
                activity?.overridePendingTransition(0, 0)

                dialog!!.dismiss()
            }
            btnSave.setOnClickListener {
                if (::curruntImagePath2.isInitialized && editName.text.isNotEmpty() && editMoney.text.isNotEmpty()) {
                    val userData2 = UserData2(
                        0,
                        editName.text.toString(),
                        editMoney.text.toString(),
                        curruntImagePath2
                    )
                    appDatabase2.userDaos().addUser(userData2)

                    startActivity(Intent(requireContext(), MainActivity::class.java))
                    activity?.overridePendingTransition(0, 0)

                    dialog!!.dismiss()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Iltimos malumotlarni to`liq kiriting",
                        Toast.LENGTH_SHORT
                    ).show()
                }


                if (appDatabase.userDaos()
                        .getAllUserSize().size == 0 && ::curruntImagePath.isInitialized && editName.text.isNotEmpty() && editMoney.text.isNotEmpty()
                ) {
                    val userData = UserData(
                        1,
                        editName.text.toString(),
                        editMoney.text.toString(),
                        curruntImagePath,
                        "Del"
                    )

                    appDatabase.userDaos().addUser(userData)

                    startActivity(Intent(requireContext(), MainActivity::class.java))
                    activity?.overridePendingTransition(0, 0)

                    dialog!!.dismiss()

                } else if (appDatabase.userDaos()
                        .getAllUserSize().size == 1 && ::curruntImagePath.isInitialized && editName.text.isNotEmpty() && editMoney.text.isNotEmpty()
                ) {
                    val userData = UserData(
                        2,
                        editName.text.toString(),
                        editMoney.text.toString(),
                        curruntImagePath,
                        "Del"
                    )
                    appDatabase.userDaos().addUser(userData)

                    startActivity(Intent(requireContext(), MainActivity::class.java))
                    activity?.overridePendingTransition(0, 0)

                    dialog!!.dismiss()
                } else if (::curruntImagePath2.isInitialized && editName.text.isNotEmpty() && editMoney.text.isNotEmpty()) {
                    val all = appDatabase.userDaos().getUserById(2)
                    val userData = UserData(
                        1,
                        all.title,
                        all.summa,
                        all.imageUserPath,
                        all.click
                    )
                    appDatabase.userDaos().editUser(userData)
                    appDatabase.userDaos().deletUser(all)

                    val userData2 = UserData(
                        2,
                        editName.text.toString(),
                        editMoney.text.toString(),
                        curruntImagePath,
                        "Del"
                    )
                    appDatabase.userDaos().addUser(userData2)

                    startActivity(Intent(requireContext(), MainActivity::class.java))
                    activity?.overridePendingTransition(0, 0)

                    dialog!!.dismiss()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Iltimos malumotlarni to`liq kiriting",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }
            addImage.setOnClickListener {
                loadDexter()
            }


        }


        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DogFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DogFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
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
        if (requestCode == ImagePicker.REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            curruntImagePath2 = data?.data?.path!!
            curruntImagePath = data?.data?.path!!
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