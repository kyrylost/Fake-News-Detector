package com.example.fakenewsdetector.view

import android.content.res.Configuration
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import androidx.databinding.DataBindingUtil
import com.example.fakenewsdetector.R
import com.example.fakenewsdetector.databinding.ActivityMainBinding
import com.example.fakenewsdetector.viewmodel.AppViewModel

class MainActivity : AppCompatActivity() {

    private val viewModel = AppViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding : ActivityMainBinding = DataBindingUtil.setContentView(this,
            R.layout.activity_main
        )
        binding.lifecycleOwner = this

        binding.googleTranslatorWebView.webViewClient = WebViewClient()
        binding.googleTranslatorWebView.settings.javaScriptEnabled = true
        binding.googleTranslatorWebView.loadUrl("https://translate.google.com/")

        binding.check.setOnClickListener{
            val news = binding.newsText.text.toString()
            viewModel.check(news)
            binding.progressBar.apply {
                indeterminateDrawable
                    .setColorFilter(
                        getColor(R.color.blue_primary),
                        PorterDuff.Mode.MULTIPLY
                    )
                visibility = View.VISIBLE
            }
        }

        binding.newsLanguageHint.setOnClickListener{
            if (binding.googleTranslatorWebView.visibility == View.GONE)
                binding.googleTranslatorWebView.visibility = View.VISIBLE
            else binding.googleTranslatorWebView.visibility = View.GONE
        }

        viewModel.trueNewsDetectedMutableLiveData.observe(this) {
            binding.progressBar.visibility = View.GONE
            binding.response.text = getString(R.string.news_is_true)

            val nightModeFlags: Int = this.resources.configuration.uiMode and
                    Configuration.UI_MODE_NIGHT_MASK
            when(nightModeFlags) {
                Configuration.UI_MODE_NIGHT_NO ->
                    binding.mainLayout.setBackgroundColor(getColor(R.color.true_news_color))
            }
        }
        viewModel.fakeNewsDetectedMutableLiveData.observe(this) {
            binding.progressBar.visibility = View.GONE
            binding.response.text = getString(R.string.news_is_fake)

            val nightModeFlags: Int = this.resources.configuration.uiMode and
                    Configuration.UI_MODE_NIGHT_MASK
            when(nightModeFlags) {
                Configuration.UI_MODE_NIGHT_NO ->
                    binding.mainLayout.setBackgroundColor(getColor(R.color.fake_news_color))
            }
        }
    }
}