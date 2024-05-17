package com.android.ux.tourid

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.util.Date

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CreateBlogFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CreateBlogFragment : Fragment(),View.OnClickListener, View.OnFocusChangeListener, View.OnKeyListener {
    private  lateinit var titleEditLay : TextInputLayout
    private  lateinit var titleEditLay1 : TextInputLayout
    private  lateinit var titleEditText1 : TextInputEditText
    private  lateinit var titleEditText : TextInputEditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_create_blog, container, false)
        titleEditText = view?.findViewById(R.id.blogtitleed) as TextInputEditText
        titleEditLay = view?.findViewById(R.id.blogtitlelay) as TextInputLayout
        titleEditText1 = view?.findViewById(R.id.blogContented) as TextInputEditText
        titleEditLay1 = view?.findViewById(R.id.blogContentlay) as TextInputLayout
        var create = view?.findViewById(R.id.createbutton) as MaterialButton

        titleEditText.onFocusChangeListener=this
        titleEditText1.onFocusChangeListener=this
        create.setOnClickListener(this)
        return view
    }

    private fun validateBlogTitle(): Boolean{
        var errorMessage: String? = null
        val value = titleEditText.text.toString()
        if(value.isEmpty()){
            errorMessage="Blog Title is required"
        }

        if(errorMessage != null){
            titleEditLay.apply{
                isErrorEnabled = true
                error=errorMessage
            }
        }

        return errorMessage == null
    }

    private fun validateBlogContent(): Boolean{
        var errorMessage: String? = null
        val value = titleEditText.text.toString()
        if(value.isEmpty()){
            errorMessage="Blog Title is required"
        }

        if(errorMessage != null){
            titleEditLay.apply{
                isErrorEnabled = true
                error=errorMessage
            }
        }

        return errorMessage == null
    }

    private fun ToBlog() {
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        if (transaction != null) {
            transaction.replace(R.id.container, BlogFragment())
        }
        if (transaction != null) {
            transaction.disallowAddToBackStack()
        }
        if (transaction != null) {
            transaction.commit()
        }
    }

    override fun onClick(view: View?) {
        if(view != null){
            when (view.id){
                R.id.createbutton ->{
                    val databaseHandler: DatabaseHandler= DatabaseHandler(requireContext())
                    if(validateBlogTitle() && validateBlogContent()){
                        val value = titleEditText.text.toString()
                        val value1 = titleEditText1.text.toString()
                        val id = (Date().time)
                        val status = databaseHandler.addBlog(EmpBlogModelClass(id,value,value1))
                        if(status > -1){
                            Toast.makeText(requireContext(),"Successfully Created", Toast.LENGTH_LONG).show()
                            ToBlog()
                        }
                        else{
                            Toast.makeText(requireContext(),"An error occurred", Toast.LENGTH_LONG).show()
                        }
                    }else{
                        Toast.makeText(requireContext(),"The fields cannot be blank", Toast.LENGTH_LONG).show()

                    }

                    }
                }
            }
        }


    override fun onFocusChange(view: View?, hasFocus: Boolean) {

        if(view != null){
//            var titleEditLay = view.findViewById(R.id.blogtitlelay) as TextInputLayout
//            var titleEditLay1 = view.findViewById(R.id.blogContentlay) as TextInputLayout
            when (view.id){
                R.id.blogContented->{
                    if(hasFocus){
                        if(titleEditLay1.isErrorEnabled){
                            titleEditLay1.isErrorEnabled = false
                        }
                    }else{
                        validateBlogContent()
                    }
                }
                R.id.blogtitleed->{
                    if(hasFocus){
                        if(titleEditLay.isErrorEnabled){
                            titleEditLay.isErrorEnabled = false
                        }
                    }else{
                        validateBlogTitle()
                    }
                }
            }
        }
    }

    override fun onKey(p0: View?, p1: Int, p2: KeyEvent?): Boolean {
        TODO("Not yet implemented")
    }


}