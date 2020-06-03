package com.example.zikea

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.Dispatchers.Main

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val webView = view.findViewById(R.id.webView) as WebView
        //val webView = view?.findViewById(R.id.webView) as WebView
        webView.settings.javaScriptEnabled = true
        webView.settings.setAppCacheEnabled(true)
        webView.webViewClient = WebViewClient()
        webView.loadUrl("http://zikea.tech/")

        view.findViewById<Button>(R.id.button_first).setOnClickListener {
            val intent = Intent (activity, ArActivity::class.java)
            activity?.startActivity(intent)
            //findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
    }
}
