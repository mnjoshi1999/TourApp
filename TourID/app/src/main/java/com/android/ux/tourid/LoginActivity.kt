package com.android.ux.tourid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import com.android.ux.tourid.databinding.ActivityLoginBinding
import com.android.ux.tourid.databinding.ActivityMainBinding

class LoginActivity : AppCompatActivity(), View.OnClickListener, View.OnFocusChangeListener, View.OnKeyListener {

    private lateinit var mBinding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityLoginBinding.inflate(LayoutInflater.from(this))
        setContentView(mBinding.root)

        mBinding.emailLogined.onFocusChangeListener = this
        mBinding.passLogined.onFocusChangeListener = this
        mBinding.toRegister.setOnClickListener(this)
        mBinding.loginbutton.setOnClickListener(this)

    }

    private fun validateEmail(): Boolean{
        var errorMessage: String? = null
        val value: String = mBinding.emailLogined.text.toString()
        if(value.isEmpty()){
            errorMessage="Email Address is required"
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(value).matches()){
            errorMessage="Emaill address is invalid"
        }

        if(errorMessage != null){
            mBinding.emailLoginlay.apply{
                isErrorEnabled = true
                error=errorMessage
            }
        }

        return errorMessage == null
    }


    private fun validatePassword(): Boolean{
        var errorMessage : String? = null
        val value: String = mBinding.passLogined.text.toString()
        if(value.isEmpty()){
            errorMessage="Password is required"
            return false
        }
        else if(value.length < 8){
            errorMessage="Password must be at least 8 characters long"
            return false
        }

        if(errorMessage != null){
            mBinding.passLoginlay.apply{
                isErrorEnabled = true
                error=errorMessage
            }
        }

        return errorMessage == null
    }

    override fun onClick(view: View?) {
        if(view != null){
            when (view.id){
                R.id.toRegister ->{
                    intent = Intent(applicationContext,MainActivity::class.java)
                    startActivity(intent)
                }

                R.id.loginbutton->{
                    val databaseHandler: DatabaseHandler= DatabaseHandler(this)
                    val value: String = mBinding.passLogined.text.toString()
                    val value1: String = mBinding.emailLogined.text.toString()
                    if(validateEmail() && validatePassword()){
                        if(databaseHandler.viewEmployee(value1,value)){
                            Toast.makeText(applicationContext,"Login Successfully", Toast.LENGTH_LONG).show()
                            if(value1 == "admin@gmail.com" && value == "admin12345"){
                                intent = Intent(applicationContext,HomeActivity::class.java)
                                startActivity(intent)
                            }
                            else{
                                val databaseHandler: DatabaseHandler= DatabaseHandler(this)
                                databaseHandler.createcurrent(value,value1)
                                intent = Intent(applicationContext,ClientActivity::class.java)
                                startActivity(intent)
                            }
                        }
                        else{
                            Toast.makeText(applicationContext,"Invalid credentials", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    }

    override fun onFocusChange(view: View?, hasFocus: Boolean) {
        if(view!= null){
            when(view.id){
                R.id.emailLogined->{
                    if(hasFocus){
                        if(mBinding.emailLoginlay.isErrorEnabled){
                            mBinding.emailLoginlay.isErrorEnabled=false
                        }
                    }
                    else{
                        validateEmail()
                    }
                }
                R.id.passLogined->{
                    if(hasFocus){
                        if(mBinding.passLoginlay.isErrorEnabled){
                            mBinding.passLoginlay.isErrorEnabled=false
                        }
                    }
                    else{
                        validatePassword()
                    }
                }
            }
        }

    }

    override fun onKey(p0: View?, p1: Int, p2: KeyEvent?): Boolean {
        TODO("Not yet implemented")
    }
}