package com.grihshobha.delhipress_grih

import ai.conscent.regularpaywalls.RegularPaywall
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope


import com.conscent.framework.core.Conscent
import com.conscent.framework.core.ConscentWrapper
import com.conscent.framework.core.OnConscentListener
import com.grihshobha.delhipress_grih.activity.home.TestActivityConcent
import com.grihshobha.delhipress_grih.utils.ConstantVaribles
import kotlinx.coroutines.launch

class BusinessFragment : Fragment(), OnConscentListener {

    private lateinit var paywall_view: FrameLayout
    private lateinit var parent_view: ConstraintLayout
    private lateinit var storyIdTv: TextView
    private lateinit var readFullStoryStatusTv: TextView

    lateinit var conscent: Conscent
    lateinit var activity: TestActivityConcent

    private var showSubscriptions: Boolean = false

    var TAG = "BusinessFragment"
    override fun onResume() {
        super.onResume()
        if (!showSubscriptions) {
            conscent.checkContentAccess(
                ConstantVaribles.titleForConscentUI,
                ConstantVaribles.subsUrlForConscentUI,
                canSubscribe = true,
                showClose = true
            )
        } else {
            conscent.checkSubscriptions(
                ConstantVaribles.titleForConscentUI,
                ConstantVaribles.subsUrlForConscentUI,
                false,
            )
        }


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.conscent_fragment_business, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity=requireActivity() as TestActivityConcent

        parent_view = view.findViewById(R.id.parent)
        paywall_view = view.findViewById(R.id.frame)
        storyIdTv = view.findViewById(R.id.storyIdTv)
        readFullStoryStatusTv = view.findViewById(R.id.readFullStoryStatusTv)

        val canNotRead= "<font color=#CC193D>${"Content restricted"}</font> \n" +
                "<font color=#000000>${"User Detail is : ${activity.session.concentUserDetail}"}</font> \n"

        readFullStoryStatusTv.text = HtmlCompat.fromHtml(canNotRead, HtmlCompat.FROM_HTML_MODE_LEGACY)


        val bundle = arguments
        if (bundle != null) {
           val id = bundle.getString("id")

            val contentId="Content ID:text-$id"
            storyIdTv.text=contentId

            conscent = ConscentWrapper.getConscentInstance(
                requireActivity(),
                parent_view,
                paywall_view,
                "text-$id",
                this)
            RegularPaywall.initRegularPaywall()
            conscent.popUpContainer = paywall_view

            showSubscriptions = false
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        conscent.onDestroy()
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Log.i(TAG, "RedirectionHandler.onActivityResult: ")
        if (resultCode == AppCompatActivity.RESULT_OK) {
            if (data?.getStringExtra("TYPE") == "PLANS") {
                conscent.checkSubscriptions(
                    ConstantVaribles.titleForConscentUI,
                    ConstantVaribles.subsUrlForConscentUI,
                )
            } else
                conscent.handledIntent()
        }
    }

    /** You can pass a listener which will get called after success or failure in processing.
     *  If you pass a listener, after successful processing, the success reference will be called and for a failed event,
     *  the failure event will be called  **/
    override fun onAdFree(clientId: String, contentId: String?) {
        Log.d(TAG, "onAdFree: ")
    }

    override fun onBuyPass(clientId: String, contentId: String) {
        Log.d(TAG, "onBuyPass: ")
    }

    override fun onError(clientId: String, contentId: String, errorMsg: String) {
        Log.e(TAG, "onError: $errorMsg  contentId:$contentId")
    }

    override fun onSignIn(clientId: String, contentId: String) {
        Log.d(TAG, "signIn: ")
    }

    override fun onSubscribe(clientId: String, contentId: String) {
        Log.d(TAG, "onSubscribe: ")

    }

    override fun onSuccess(clientId: String, contentId: String) {
        Log.d(TAG, "onSuccess: ")
        val canRead= "<font color=#257828>${"Congrats! You can read complete story"}</font> \n" +
                "<font color=#000000>${"User Detail is : ${activity.session.concentUserDetail}"}</font> "

        readFullStoryStatusTv.text = HtmlCompat.fromHtml(canRead, HtmlCompat.FROM_HTML_MODE_LEGACY)

        lifecycleScope.launch{
            val userDetail = ConscentWrapper.INSTANCE?.getUserDetails()

            activity.session.saveConcentUserDetail(userDetail)

        }
    }

}