package com.android.ux.tourid

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Patterns
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.ux.tourid.databinding.ActivityMainBinding
import java.util.Date


class MainActivity : AppCompatActivity(), View.OnClickListener, View.OnFocusChangeListener, View.OnKeyListener {

    private lateinit var mBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(mBinding.root)
        mBinding.nameed.onFocusChangeListener = this
        mBinding.emailed.onFocusChangeListener = this
        mBinding.passed.onFocusChangeListener = this
        mBinding.confpassEd.onFocusChangeListener = this
        mBinding.toLogin.setOnClickListener(this)
        mBinding.regbutton.setOnClickListener(this)
}

    private fun validateFullName(): Boolean{
        var errorMessage: String? = null
        val value: String = mBinding.nameed.text.toString()
        if(value.isEmpty()){
            errorMessage="Full name is required"
        }

        if(errorMessage != null){
            mBinding.namelay.apply{
                isErrorEnabled = true
                error=errorMessage
            }
        }

        return errorMessage == null
    }

    private fun validateEmail(): Boolean{
        var errorMessage: String? = null
        val value: String = mBinding.emailed.text.toString()
        if(value.isEmpty()){
            errorMessage="Email Address is required"
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(value).matches()){
            errorMessage="Emaill address is invalid"
        }

        if(errorMessage != null){
            mBinding.emaillay.apply{
                isErrorEnabled = true
                error=errorMessage
            }
        }

        return errorMessage == null
    }

    private fun validatePassword(): Boolean{
        var errorMessage : String? = null
        val value: String = mBinding.passed.text.toString()
        if(value.isEmpty()){
            errorMessage="Password is required"
        }
        else if(value.length < 8){
            errorMessage="Password must be at least 8 characters long"
        }

        if(errorMessage != null){
            mBinding.passlay.apply{
                isErrorEnabled = true
                error=errorMessage
            }
        }

        return errorMessage == null
    }

    private fun validateConfirmPassword(): Boolean{
        var errorMessage: String? = null
        val value: String = mBinding.confpassEd.text.toString()
        if(value.isEmpty()){
            errorMessage="Confirm Password is required"
        }
        else if(value.length < 8){
            errorMessage="Confirm Password must be at least 8 characters long"
        }

        if(errorMessage != null){
            mBinding.confpasslay.apply{
                isErrorEnabled = true
                error=errorMessage
            }
        }

        return errorMessage == null
    }

    private fun validatePasswordAndConfirmPassword(): Boolean{
        var errorMessage: String? = null
        var confPass: String = mBinding.confpassEd.text.toString()
        var Pass: String = mBinding.passed.text.toString()
        if(Pass != confPass){
            errorMessage="Confirm Password does not match with Password"
        }

        if(errorMessage != null){
            mBinding.confpasslay.apply{
                isErrorEnabled = true
                error=errorMessage
            }
        }

        return errorMessage == null
    }

    override fun onClick(view: View?) {

        if(view != null){
            when (view.id){
                R.id.toLogin ->{
                    intent = Intent(applicationContext,LoginActivity::class.java)
                    startActivity(intent)
                }

                R.id.regbutton -> {
                    val databaseHandler: DatabaseHandler= DatabaseHandler(this)
                    if(validateFullName() && validateEmail() && validatePassword() && validateConfirmPassword() && validatePasswordAndConfirmPassword()){
                        val name = mBinding.nameed.text.toString()
                        val email = mBinding.emailed.text.toString()
                        val password = mBinding.passed.text.toString()
                        val id = (Date().time)
                        val status = databaseHandler.addEmployee(EmpModelClass(id,name, email,password))

                        if(status > -1){
                            Toast.makeText(applicationContext,"Registered Successfully",Toast.LENGTH_LONG).show()
                            if(name=="admin" && email == "admin@gmail.com" && password == "admin12345"){
                                intent = Intent(applicationContext,HomeActivity::class.java)
                                startActivity(intent)
                            }
                            else{
                                val databaseHandler: DatabaseHandler= DatabaseHandler(this)
                                databaseHandler.createcurrent(name,email)
                                intent = Intent(applicationContext,ClientActivity::class.java)
                                startActivity(intent)
                            }
                        }
                        else{
                            Toast.makeText(applicationContext,"An error occurred",Toast.LENGTH_LONG).show()
                        }
                    }else{
                            Toast.makeText(applicationContext,"The fields cannot be blank",Toast.LENGTH_LONG).show()

                    }
            }
        }

    }

}

    override fun onFocusChange(view: View?, hasFocus: Boolean) {
        if(view != null){
            when (view.id){
                R.id.nameed ->{
                    if(hasFocus){
                        if(mBinding.namelay.isErrorEnabled){
                            mBinding.namelay.isErrorEnabled = false
                        }
                    }else{
                        validateFullName()
                    }
                }

                R.id.emailed ->{
                    if(hasFocus){

                        if(mBinding.emaillay.isErrorEnabled){
                            mBinding.emaillay.isErrorEnabled = false
                        }

                    }else{
                        validateEmail()
                    }
                }

                R.id.passed ->{
                    if(hasFocus){

                        if(mBinding.passlay.isErrorEnabled){
                            mBinding.passlay.isErrorEnabled = false
                        }

                    }else{
                        if(validatePassword() && mBinding.confpassEd.text!!.isNotEmpty() && validateConfirmPassword() && validatePasswordAndConfirmPassword()){
                            if(mBinding.confpasslay.isErrorEnabled){
                                mBinding.confpasslay.isErrorEnabled = false
                            }
                            mBinding.confpasslay.apply{
                                setStartIconDrawable(R.drawable.baseline_check_circle_24)
                                setStartIconTintList(ColorStateList.valueOf(Color.GREEN))
                            }
                        }
                    }
                }

                R.id.confpassEd ->{
                    if(hasFocus){

                        if(mBinding.confpasslay.isErrorEnabled){
                            mBinding.confpasslay.isErrorEnabled = false
                        }

                    }else{
                        if(validateConfirmPassword() && validatePassword() && validatePasswordAndConfirmPassword()){
                            if(mBinding.passlay.isErrorEnabled){
                                mBinding.passlay.isErrorEnabled = false
                            }
                            mBinding.confpasslay.apply{
                                setStartIconDrawable(R.drawable.baseline_check_circle_24)
                                setStartIconTintList(ColorStateList.valueOf(Color.GREEN))
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onKey(p0: View?, p1: Int, p2: KeyEvent?): Boolean {
        TODO("Not yet implemented")
    }
}