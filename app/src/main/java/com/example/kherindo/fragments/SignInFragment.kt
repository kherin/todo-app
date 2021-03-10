package com.example.kherindo.fragments

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.kherindo.R
import com.example.kherindo.utils.Validations
import com.github.razir.progressbutton.bindProgressButton
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlin.math.sign


class SignInFragment : Fragment() {

    lateinit var emailEditTextField: TextInputEditText;
    lateinit var passwordEditTextField: TextInputEditText;
    lateinit var signInButton: MaterialButton
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signin, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.signInButton = view.findViewById<MaterialButton>(R.id.login_button)

        bindProgressButton(signInButton)
        signInButton.setOnClickListener {
            // email
            emailEditTextField = view.findViewById(R.id.email_textInputEditText)
            val emailTextValue: String = emailEditTextField.text.toString()
            val (isEmailValid, emailErrorMessage) = Validations.isEmailValid(emailTextValue)
            emailEditTextField.error = emailErrorMessage

            //password
            passwordEditTextField = view.findViewById(R.id.password_textInputEditText)
            val passwordTextValue: String = passwordEditTextField.text.toString()
            val (isPasswordValid, passwordErrorMessage) = Validations.isPasswordValid(
                passwordTextValue
            )
            passwordEditTextField.error = passwordErrorMessage

            if (isEmailValid && isPasswordValid) {
                signIn(view, emailTextValue, passwordTextValue)
            }
        }
    }

    private fun signIn(view: View, email: String, password: String) {
        // show progress indicator
        signInButton.showProgress {
            buttonTextRes = R.string.signin_loading_text
            progressColor = Color.WHITE
        }

        auth = FirebaseAuth.getInstance()
        val contextView = view.findViewById<View>(R.id.signin_layout)
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(
                activity as AppCompatActivity
            ) { task ->
                if (task.isSuccessful) {
                    Log.d("auth", "isSuccessful")
                    Snackbar.make(contextView, "Welcome back!", Snackbar.LENGTH_SHORT).show()
                    view.findNavController()
                        .navigate(R.id.action_SignInFragment_to_TodoListFragment)
                } else {
                    Log.d("auth", "UnSuccessful")
                    Snackbar.make(contextView, "Authentication Failed", Snackbar.LENGTH_SHORT)
                        .show()
                }

                signInButton.hideProgress()
            }
    }
}