package com.example.projekt

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.stepstone.stepper.BlockingStep
import com.stepstone.stepper.StepperLayout
import com.stepstone.stepper.VerificationError
import kotlinx.android.synthetic.main.fragment_register_one.*
import kotlinx.android.synthetic.main.fragment_register_one.view.*


class RegisterFragmentOne : Fragment(), BlockingStep {

    private lateinit var viewOfLayout: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        viewOfLayout = inflater.inflate(
            com.example.projekt.R.layout.fragment_register_one,
            container,
            false
        )
        viewOfLayout.loginText.setOnClickListener{
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
        }

        //initialize your UI
        return viewOfLayout
    }

    override fun verifyStep(): VerificationError? {
        //return null if the user can go to the next step, create a new VerificationError instance otherwise
        return null
    }

    override fun onSelected() {
        //update UI when selected
    }

    override fun onError(error: VerificationError) {
        //handle error inside of the fragment, e.g. show error on EditText
    }

    override fun onNextClicked(callback: StepperLayout.OnNextClickedCallback?) {
        val email: String = mailText.text.toString()
        val password: String = passwordText.text.toString()
        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(activity, "Wypełnij wszystkie pola", Toast.LENGTH_LONG).show()
        }
        else{
            if(email.isEmailValid())
                callback!!.goToNextStep()
            else
                Toast.makeText(activity, "Niepoprawny adres e-mailasd", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCompleteClicked(callback: StepperLayout.OnCompleteClickedCallback?) { }

    override fun onBackClicked(callback: StepperLayout.OnBackClickedCallback?) { }

    fun String.isEmailValid(): Boolean {
        return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }
}