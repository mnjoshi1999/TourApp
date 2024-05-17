package com.android.ux.tourid

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TourFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TourFragment : Fragment() {

    val PICK_IMAGE_REQUEST=1
    private lateinit var imageViewPreview : ImageView
    private lateinit var imageUri : Uri

    var button_date: Button? = null
    var textview_date: TextView? = null
    var cal = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_tour, container, false)

        // In your fragment code:
        imageViewPreview = view.findViewById<ImageView>(R.id.imageViewPreview)
        val buttonUploadImage = view.findViewById<Button>(R.id.buttonUploadImage)
        val title = view.findViewById<TextView>(R.id.editTextTitle)
        val desc = view.findViewById<TextView>(R.id.editTextDescription)
        val post = view.findViewById<Button>(R.id.buttonPost)
        var imageUri: Uri? = null
        buttonUploadImage.setOnClickListener {
            // Open an image picker or camera intent here, and once the user selects an image:
              // Uri of the selected image
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            val PICK_IMAGE_REQUEST=1
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        // get the references from layout file
        textview_date = view.findViewById<TextView>(R.id.text_view_date_1)
        button_date = view.findViewById<Button>(R.id.button_date_1)
        textview_date!!.text = "--/--/----"

        val dateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int,
                                   dayOfMonth: Int) {
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView()
            }
        }

        button_date!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                DatePickerDialog(requireContext(),
                    dateSetListener,
                    // set DatePickerDialog to point to today's date when it loads up
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show()
            }

        })


        post.setOnClickListener {
            if(title.text.toString().length > 0 && desc.text.toString().length > 0 && this.imageUri.toString().length > 0 && textview_date!!.text.toString().length > 0){
                val databaseHandler: DatabaseHandler= DatabaseHandler(requireContext())
                try {
                    databaseHandler.addTourPost(title.text.toString(),desc.text.toString(),this.imageUri.toString(),textview_date!!.text.toString())
                    Toast.makeText(requireContext(),"Posted well",Toast.LENGTH_SHORT).show()

                }catch (e:Exception){
                    Toast.makeText(requireContext(),""+e.message,Toast.LENGTH_SHORT).show()
                }

            }
            else{
                Toast.makeText(requireContext(),"An error",Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            this.imageUri = data?.data!!
//            imageViewPreview.visibility = View.VISIBLE
//            Picasso.get().load(imageUri).into(imageViewPreview)
        }

    }

    private fun updateDateInView() {
        val myFormat = "MM/dd/yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        textview_date!!.text = sdf.format(cal.getTime())
    }

}