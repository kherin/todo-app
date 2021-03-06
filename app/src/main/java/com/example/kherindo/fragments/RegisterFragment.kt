package com.example.kherindo.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.example.kherindo.R
import com.example.kherindo.utils.Validations
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth


class RegisterFragment : Fragment() {

    lateinit var emailEditTextField: TextInputEditText
    lateinit var passwordEditTextField: TextInputEditText
    lateinit var confirmPasswordEditTextField: TextInputEditText
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.button_register).setOnClickListener {

            // email
            emailEditTextField = view.findViewById(R.id.email_textInputEditText)
            val emailTextValue: String = emailEditTextField.text.toString()
            val (isEmailValid, emailErrorMessage) = Validations.isEmailValid(emailTextValue)
            emailEditTextField.error = emailErrorMessage

            // password
            passwordEditTextField = view.findViewById(R.id.password_textInputEditText)
            val passwordTextValue: String = passwordEditTextField.text.toString()
            val (isPasswordValid, passwordErrorMessage) = Validations.isPasswordValid(
                passwordTextValue
            )
            passwordEditTextField.error = passwordErrorMessage

            // confirm password
            confirmPasswordEditTextField =
                view.findViewById(R.id.confirm_password_textInputEditText)
            val confirmPasswordTextValue: String = confirmPasswordEditTextField.text.toString()
            val (doPasswordsMatch, confirmPasswordErrorMessage) = Validations.doPasswordsMatch(
                passwordTextValue,
                confirmPasswordTextValue
            )

            if (isEmailValid && isPasswordValid && doPasswordsMatch) {
                signUp(view, emailTextValue, passwordTextValue)
            }
        }
    }

    private fun signUp(view: View, email: String, password: String) {
        auth = FirebaseAuth.getInstance()
        val contextView = view.findViewById<View>(R.id.register_layout)
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(
                activity as AppCompatActivity
            ) { task ->
                if (task.isSuccessful) {
                    Log.d("registration", "Successful")
                    Snackbar.make(contextView, "Happy to see you!", Snackbar.LENGTH_SHORT).show()
                } else {
                    Log.d("registration", "UnSuccessful")
                    Snackbar.make(contextView, "Registration Failed", Snackbar.LENGTH_SHORT)
                        .show()
                }
            }
    }
}