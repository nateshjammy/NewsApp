package com.news.app.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.NavArgs
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.news.app.R
import com.news.app.databinding.FragmentArticleBinding
import com.news.app.databinding.FragmentBreakingnewsBinding
import com.news.app.ui.Article
import com.news.app.ui.NewsActivity
import com.news.app.ui.viewModel.NewsViewModel

class ArticleFragment : Fragment() {

    lateinit var binding: FragmentArticleBinding

    lateinit var viewModel: NewsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentArticleBinding.inflate(inflater, container, false)
        viewModel = (activity as NewsActivity).viewModel

        val article = arguments?.get("article") as Article

        binding.webView.apply {
            webViewClient = WebViewClient()
            loadUrl(article.url)
        }


        binding.fab.setOnClickListener {
            viewModel.saveArticle(article)
            view?.let { it1 ->
                Snackbar.make(it1,
                    "Article Saved Sucessfully",
                    Snackbar.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }
}