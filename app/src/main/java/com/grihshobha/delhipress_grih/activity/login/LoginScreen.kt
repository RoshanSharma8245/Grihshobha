package com.grihshobha.delhipress_grih.activity.login


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.conscent.framework.core.ConscentWrapper
import com.grihshobha.delhipress_grih.States
import com.grihshobha.delhipress_grih.activity.home.TestActivityConcent
import com.grihshobha.delhipress_grih.models.request.ConcentLoginRequest
import com.grihshobha.delhipress_grih.models.response.conscent_token_response.ConcentTokenResponse
import com.grihshobha.delhipress_grih.api.*
import com.grihshobha.delhipress_grih.databinding.LoginActivityBinding
import com.grihshobha.delhipress_grih.utils.SessionManagement
import com.grihshobha.delhipress_grih.fectory.ViewModelFactory
import kotlinx.coroutines.*


@Suppress("DEPRECATION")
class LoginScreen : AppCompatActivity() {
    val TAG="LoginScreen"
    lateinit var viewModel: ViewModelLogin
    lateinit var session: SessionManagement
    lateinit var binding: LoginActivityBinding
     var isConcentLoginAttemt=false

    @OptIn(DelicateCoroutinesApi::class)
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)


        session = SessionManagement(this)


        val mobileEdit = binding.mobileEdit



        setupViewModel()

        binding.loginBtn.setOnClickListener {


            if (binding.mobileEdit.text.toString().length==10){

                val body= ConcentLoginRequest(phoneNumber = mobileEdit.text.toString())
                viewModel.getConcentToken(body)

            }

        }

        binding.skipBtn.setOnClickListener {

            val i = Intent(this, TestActivityConcent::class.java)
            startActivity(i)
            finish()
        }


    }


    private fun setupViewModel() {
        val apiService = RetrofitBuilderFlow.apiService
        viewModel = ViewModelProvider(this, ViewModelFactory(ApiHelperImpl(apiService)))[ViewModelLogin::class.java]

        observeViewModel()
    }
    private fun observeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect{
                    when (it) {
                        States.Idle -> {
                            binding.progressBar.visibility=View.GONE
                            Log.d(TAG, "States Idle" )
                        }
                        is States.LoginState.Error -> {
                            binding.progressBar.visibility=View.GONE
                            Log.e(TAG, "States error  = ${it.errorMsg}" )
                        }
                        States.LoginState.Loading -> {
                            binding.progressBar.visibility=View.VISIBLE
                            Log.d(TAG, "States loading" )
                        }
                        is States.LoginState.Success -> {
                            binding.progressBar.visibility=View.GONE

                            concentLogin(it.response)

                        }
                        else -> {binding.progressBar.visibility=View.GONE}
                    }
                }
            }
        }
    }

    private suspend fun concentLogin(response: ConcentTokenResponse) {

        if(!isConcentLoginAttemt){
            if(response.tempAuthToken!=null){
                isConcentLoginAttemt=true
                ConscentWrapper.INSTANCE?.autoLogin(
                    phoneNumber = binding.mobileEdit.text.toString(),
                    clientActivity = this@LoginScreen,
                    tempToken = response.tempAuthToken
                )
            }else{
                Toast.makeText(this, "Error : null concent tempAuthToken", Toast.LENGTH_LONG).show()
            }
        }

    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d(TAG, "onActivityResult = " )
        if (resultCode == RESULT_OK) {
            Log.d(TAG, "RedirectionHandler STATUS = ${data?.getStringExtra("STATUS")}" )

            if(data?.getStringExtra("STATUS")=="true"){
                val i = Intent(this, TestActivityConcent::class.java)
                startActivity(i)
                finish()
            }else{
                Toast.makeText(this, "Error : Concent Login failed", Toast.LENGTH_LONG).show()
            }
        }
    }


}