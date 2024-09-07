package com.kareem.flute.ui.login


import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import android.os.Bundle

import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.kareem.flute.R
import com.kareem.flute.databinding.FragmentSignupBinding
import com.kareem.flute.ui.MainFragment

class SignupFragment : Fragment() {

    private lateinit var auth: FirebaseAuth

    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSignupBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentSignupBinding.bind(view)



        // check information
        binding.SignUpBtn.setOnClickListener {
            val email = binding.username.text.toString().trim()
            val password = binding.password.text.toString()
            val confirmPassword = binding.confirmPassword.text.toString()

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.username.error = "Invalid Email"
                return@setOnClickListener
            }
            if (password != confirmPassword) {
                binding.password.error = "Passwords do not match"
                return@setOnClickListener
            }
            if (password.length < 6) {
                binding.password.error = "Password must be at least 6 characters"
                return@setOnClickListener
            }

            createAccountWithFirebase(email, password)

        }
        binding.login.setOnClickListener {
            replaceFragment(LoginFragment(),false)
        }

    }


    private fun createAccountWithFirebase(email: String, password: String) {
        setInProgress(true)
        FirebaseAuth
            .getInstance()
            .createUserWithEmailAndPassword(email, password).addOnSuccessListener {
                Toast.makeText(context, "Account Created Successfully", Toast.LENGTH_SHORT).show()
                setInProgress(false)
                replaceFragment(MainFragment(),true)
            }.addOnFailureListener {
                Toast.makeText(context, "Error Occurred ! ${it.message}", Toast.LENGTH_SHORT).show()

            }

    }

    private fun setInProgress(inProgress: Boolean) {
        if (inProgress) {
            binding.SignUpBtn.visibility = View.GONE
            binding.loading.visibility = View.VISIBLE
        } else {
            binding.SignUpBtn.visibility = View.VISIBLE
            binding.loading.visibility = View.GONE

        }
    }

    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    Toast.makeText(context, "Authentication Successful.", Toast.LENGTH_SHORT).show()
                    // Navigate to the main activity or dashboard
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(context, "Authentication Failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun replaceFragment(fragment: Fragment,popStack:Boolean) {

        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_layout, fragment)
        if (popStack){
            requireActivity().supportFragmentManager.popBackStack()
        }
        else{
            transaction.addToBackStack(null)
        }

        transaction.commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}