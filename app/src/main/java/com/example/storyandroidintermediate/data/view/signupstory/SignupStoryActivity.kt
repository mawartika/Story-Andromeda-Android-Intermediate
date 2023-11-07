package com.example.storyandroidintermediate.data.view.signupstory

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Build
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.storyandroidintermediate.R
import com.example.storyandroidintermediate.data.view.ViewModelFactoryStory
import com.example.storyandroidintermediate.databinding.ActivitySignupstoryBinding
import com.example.storyandroidintermediate.data.retrofit.Result


class SignupStoryActivity : AppCompatActivity(){
    private lateinit var binding: ActivitySignupstoryBinding

    private val viewModel by viewModels<SignupViewModel>{
        ViewModelFactoryStory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupstoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
        playAnimation()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()

    }

    private fun setupAction() {
        binding.seePasswordLogin.setOnCheckedChangeListener { _, isChecked ->
            binding.passwordEditTextLogin.transformationMethod = if (isChecked) {
                HideReturnsTransformationMethod.getInstance()
            } else {
                PasswordTransformationMethod.getInstance()
            }
            binding.passwordEditTextLogin.text?.let { binding.passwordEditTextLogin.setSelection(it.length) }
        }

        binding.ButtonSignup.setOnClickListener {
            val email = binding.emailEditTextStory.text.toString()
            val name = binding.nameEditTextSignup.text.toString()
            val pass = binding.passwordEditTextLogin.text.toString()

            val title = getString(R.string.head_notif)
            val message = getString(R.string.register_succes_notif)
            val next = getString(R.string.next_notif)

            viewModel.signup(email = email, name = name, password = pass).observe(this){hoho ->
                when(hoho){
                    is Result.Loading ->{
                        showLoading(true)
                    }
                    is Result.Success -> {
                        showLoading(false)
                        AlertDialog.Builder(this).apply {
                            setTitle(title)
                            setMessage(message)
                            setPositiveButton(next) { _, _ ->
                                finish()
                            }
                            create()
                            show()
                        }
                    }
                    is Result.Error -> {
                        showLoading(false)
                        showToast(hoho.error)
                    }
                }
            }

        }
    }
    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageViewLogin, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val titleregister = ObjectAnimator.ofFloat(binding.titleTextViewSignup, View.ALPHA, 1f).setDuration(200)
        val titlename = ObjectAnimator.ofFloat(binding.nameTextViewSignup, View.ALPHA, 1f).setDuration(200)
        val nameedit = ObjectAnimator.ofFloat(binding.nameEditTextLayoutSignup, View.ALPHA, 1f).setDuration(200)
        val titlemail = ObjectAnimator.ofFloat(binding.emailTextViewLogin, View.ALPHA, 1f).setDuration(200)
        val emailedit = ObjectAnimator.ofFloat(binding.emailEditTextLayoutLogin, View.ALPHA, 1f).setDuration(200)
        val titlepass = ObjectAnimator.ofFloat(binding.passwordTextViewLogin, View.ALPHA, 1f).setDuration(200)
        val passedit = ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(200)
        val login = ObjectAnimator.ofFloat(binding.ButtonSignup, View.ALPHA, 1f).setDuration(200)

        AnimatorSet().apply {
            playSequentially(titleregister,titlename,nameedit,titlemail,emailedit,titlepass,passedit,login)
            start()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicatorLogin.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}