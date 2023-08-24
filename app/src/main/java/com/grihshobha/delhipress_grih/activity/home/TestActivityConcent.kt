@file:OptIn(DelicateCoroutinesApi::class)

package com.grihshobha.delhipress_grih.activity.home


import ai.conscent.regularpaywalls.RegularPaywall
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.conscent.framework.core.Conscent
import com.conscent.framework.core.ConscentWrapper
import com.conscent.framework.core.OnConscentListener
import com.grihshobha.delhipress_grih.BusinessFragment
import com.grihshobha.delhipress_grih.R
import com.grihshobha.delhipress_grih.States
import com.grihshobha.delhipress_grih.activity.login.LoginScreen
import com.grihshobha.delhipress_grih.api.ApiHelperImpl
import com.grihshobha.delhipress_grih.api.RetrofitBuilderFlow
import com.grihshobha.delhipress_grih.databinding.HomeActivityBinding
import com.grihshobha.delhipress_grih.fectory.ViewModelFactory
import com.grihshobha.delhipress_grih.models.response.stories_response.StoriesResponse
import com.grihshobha.delhipress_grih.utils.ConstantVaribles.*
import com.grihshobha.delhipress_grih.utils.SessionManagement
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TestActivityConcent : AppCompatActivity(), OnConscentListener {
    val TAG = "TestActivityConcent"
    lateinit var binding: HomeActivityBinding
    val login = "Login"
    val logout = "Logout"
    var count = 0
    lateinit var viewModel: ViewModelHome
    lateinit var instance: Conscent
    lateinit var session: SessionManagement
    lateinit var storiesAdapter: StoriesAdapter
    private var storiesFetched: Boolean = false
    private var showSubscriptions: Boolean = false

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        if (!showSubscriptions) {
            instance.checkContentAccess(
                titleForConscentUI, //titleForConscentUI
                subsUrlForConscentUI,//subsUrlForConscentUI
                true,
                showClose = false
            )
        } else {
            instance.checkSubscriptions(
                titleForConscentUI,
                subsUrlForConscentUI,
                false,
            )
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = HomeActivityBinding.inflate(layoutInflater)

        val view = binding.root
        setContentView(view)

        session = SessionManagement(this)


        val linearLayoutManager =
            LinearLayoutManager(this.applicationContext, LinearLayoutManager.VERTICAL, false)
        binding.storyRecyclerView.layoutManager = linearLayoutManager
        binding.storyRecyclerView.isNestedScrollingEnabled = true

        instance = ConscentWrapper.getConscentInstance(
            this,
            binding.constraintLay,
            binding.testFrameLayout,
            "text-155621",
            this
        )

        RegularPaywall.initRegularPaywall()
        instance.popUpContainer = binding.testFrameLayout


        binding.logoutTv.setOnClickListener {

            if (binding.logoutTv.text == logout) {
                lifecycleScope.launch {
                    val response = ConscentWrapper.INSTANCE?.logoutUser()
                    Log.d("logoutUser", "$response")
                    session.removeUserSession()
                    binding.logoutTv.text = login
                }
            } else {
                val i = Intent(this, LoginScreen::class.java)
                startActivity(i)
                finish()
            }


        }

        binding.subscribeTv.setOnClickListener {

            CoroutineScope(Dispatchers.IO).launch {
                instance.onSoftSubscribeClick(subsUrlForConscentUI)
            }

        }

        setupViewModel()

        viewModel.getStories()

        onNewIntent(null)


    }


    private fun setupViewModel() {
        val apiService = RetrofitBuilderFlow.apiServiceGH
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(ApiHelperImpl(apiService))
        )[ViewModelHome::class.java]

        observeViewModel()
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect {
                    when (it) {
                        States.Idle -> {
                            binding.progressBar.visibility = View.GONE
                            Log.d(TAG, "States Idle")
                        }

                        is States.HomeState.Error -> {
                            binding.progressBar.visibility = View.GONE
                            Log.e(TAG, "States error  = ${it.errorMsg}")
                        }

                        States.HomeState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                            Log.d(TAG, "States loading")
                        }

                        is States.HomeState.Success -> {
                            Log.d(TAG, "States Success")
                            binding.progressBar.visibility = View.GONE

                            storiesList(it.response)

                        }

                        else -> {
                            binding.progressBar.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }

    private fun storiesList(response: StoriesResponse?) {

        if (!storiesFetched) {
            if (response != null) {
                storiesFetched = true

                val sliderData = response.data!![0]?.items

                storiesAdapter = StoriesAdapter(this, sliderData)

                binding.storyRecyclerView.adapter = storiesAdapter

            } else {
                Toast.makeText(this, "Error : null concent tempAuthToken", Toast.LENGTH_LONG).show()
            }
        }

    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {

        if (this::instance.isInitialized) {
            instance.onTouch()
        }
        return super.dispatchTouchEvent(event)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Log.i("Result", "RedirectionHandler.onActivityResult: ")
        if (resultCode == RESULT_OK) {
            if (data?.getStringExtra("TYPE") == "PLANS") {
                instance.checkSubscriptions(
                    titleForConscentUI,
                    subsUrlForConscentUI,
                )
            } else
                instance.handledIntent()
        }
    }


    override fun onResume() {
        super.onResume()


    }

    private fun getUserDetails() {
        lifecycleScope.launch {
            count += 1
            val userDetail = ConscentWrapper.INSTANCE?.getUserDetails()
            Toast.makeText(this@TestActivityConcent, "$userDetail", Toast.LENGTH_LONG).show()
            Log.d("Conscent_Fired", " onResumeCount $count concentUserDetail = $userDetail")

            session.saveConcentUserDetail(userDetail)

            if (userDetail == null)
                binding.logoutTv.text = login
            else
                binding.logoutTv.text = logout

        }
    }

    override fun onAdFree(clientId: String, contentId: String?) {
        Log.d("Conscent_Fired", "onAdFree  clientId = $clientId contentId = $contentId")
    }

    override fun onBuyPass(clientId: String, contentId: String) {
        Log.d("Conscent_Fired", " onBuyPass  clientId = $clientId contentId = $contentId")
    }

    override fun onError(clientId: String, contentId: String, errorMsg: String) {
        Log.d(
            "Conscent_Fired",
            "onError  clientId = $clientId contentId = $contentId errorMsg = $errorMsg"
        )
    }

    override fun onSignIn(clientId: String, contentId: String) {
        Log.d("Conscent_Fired", "onSignIn  clientId = $clientId contentId = $contentId")
    }

    override fun onSubscribe(clientId: String, contentId: String) {
        Log.d("Conscent_Fired", "onSubscribe  clientId = $clientId contentId = $contentId")
    }

    override fun onSuccess(clientId: String, contentId: String) {
        Log.d("Conscent_Fired", "onSuccess  clientId = $clientId contentId = $contentId")
        getUserDetails()
    }


    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()

        binding.nestedLayout.visibility = View.VISIBLE
        binding.rootLayout.visibility = View.GONE
    }

    fun contentDetailFragment(storyId: Int?) {
        if (storyId != null && storyId.toString() != "") {
            binding.nestedLayout.visibility = View.GONE
            binding.rootLayout.visibility = View.VISIBLE
            val fragment = BusinessFragment()
            val args = Bundle()
            args.putString("id", storyId.toString())
            fragment.arguments = args
            this.supportFragmentManager.beginTransaction()
                .add(R.id.root_layout, fragment, "BusinessFrag").addToBackStack("").commit()
            instance.onDestroy()
        } else {
            Toast.makeText(this, "Content ID : Null or Empty", Toast.LENGTH_LONG).show()
        }

    }

}


//