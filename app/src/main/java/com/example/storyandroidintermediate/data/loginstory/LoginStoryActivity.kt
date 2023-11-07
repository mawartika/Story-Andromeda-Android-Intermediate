package com.example.storyandroidintermediate.data.loginstory

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
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
import com.example.storyandroidintermediate.data.main.MainActivity
import com.example.storyandroidintermediate.data.retrofit.Result
import com.example.storyandroidintermediate.data.pref.StoryModel
import com.example.storyandroidintermediate.data.view.ViewModelFactoryStory
import com.example.storyandroidintermediate.data.view.signupstory.SignupStoryActivity
import com.example.storyandroidintermediate.databinding.ActivityLoginstoryBinding

class LoginStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginstoryBinding


    private val viewModel by viewModels<LoginViewModelStory>{
        ViewModelFactoryStory.getInstance(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginstoryBinding.inflate(layoutInflater)
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

        binding.loginButton.setOnClickListener {
            val email = binding.emailEditTextLogin.text.toString()
            val pass = binding.passwordEditTextLogin.text.toString()

            val title = getString(R.string.head_notif)
            val message = getString(R.string.login_succes_notif)
            val next = getString(R.string.next_notif)

            viewModel.login(email = email, password = pass).observe(this){hoho ->
                when(hoho){
                    is Result.Loading ->{
                        showLoading(true)
                    }
                    is Result.Success -> {
                        showLoading(false)
                        val user = StoryModel(
                            token = hoho.data.loginResult?.token ?: "Success",
                            email = email,
                        )
                        viewModel.saveSession(user)

                        AlertDialog.Builder(this).apply {
                            setTitle(title)
                            setMessage(message)
                            setPositiveButton(next) { _, _ ->
                                val intent = Intent(context, MainActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                startActivity(intent)
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
        binding.ButtonSignUp.setOnClickListener {
            startActivity(Intent(this, SignupStoryActivity::class.java))
        }
    }
    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageViewLogin, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val titlemail = ObjectAnimator.ofFloat(binding.emailTextViewLogin, View.ALPHA, 1f).setDuration(200)
        val emailedit = ObjectAnimator.ofFloat(binding.emailEditTextLogin, View.ALPHA, 1f).setDuration(200)
        val titlepass = ObjectAnimator.ofFloat(binding.passwordTextViewLogin, View.ALPHA, 1f).setDuration(200)
        val passedit = ObjectAnimator.ofFloat(binding.passwordEditTextLogin, View.ALPHA, 1f).setDuration(200)
        val login = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(200)
        val signup = ObjectAnimator.ofFloat(binding.ButtonSignUp, View.ALPHA, 1f).setDuration(200)

        AnimatorSet().apply {
            playSequentially(titlemail,emailedit,titlepass,passedit,login,signup)
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