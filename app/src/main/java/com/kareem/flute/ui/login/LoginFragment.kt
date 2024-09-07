package com.kareem.flute.ui.login

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.kareem.flute.databinding.FragmentLoginBinding

import com.kareem.flute.R
import com.kareem.flute.ui.MainFragment

class LoginFragment : Fragment() {

    private lateinit var auth: FirebaseAuth

    private var _binding: FragmentLoginBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        val loginButton = binding.login
        val loadingProgressBar = binding.loading

        loginButton.setOnClickListener {
            val email = binding.username.text.toString().trim()
            val password = binding.password.text.toString()

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.username.error = "Invalid Email"
                return@setOnClickListener
            }

            if (password.length < 6) {
                binding.password.error = "Password must be at least 6 characters"
                return@setOnClickListener
            }

            loginWithFirebase(email, password)
        }

        binding.register.setOnClickListener {
            replaceFragment(SignupFragment())
        }
    }

    private fun loginWithFirebase(email : String, password: String){
        setInProgress(true)
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
            .addOnSuccessListener {
                setInProgress(false)
               replaceFragment(MainFragment())
            }.addOnFailureListener {
                setInProgress(false)
                Toast.makeText(context,"Login account failed", Toast.LENGTH_SHORT).show()
            }
    }


    private fun setInProgress(inProgress: Boolean) {
        if (inProgress) {
            binding.login.visibility = View.GONE
            binding.loading.visibility = View.VISIBLE
        } else {
            binding.login.visibility = View.VISIBLE
            binding.loading.visibility = View.GONE

        }
    }

    private fun replaceFragment(fragment: Fragment) {

        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_layout, fragment)
        requireActivity().supportFragmentManager.popBackStack()
        transaction.commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}