package com.news.app.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.news.app.R
import com.news.app.databinding.ActivityNewsBinding
import com.news.app.ui.db.ArticleDatabase
import com.news.app.ui.repositary.NewsRepository
import com.news.app.ui.repositary.NewsViewModelProviderFactory
import com.news.app.ui.viewModel.NewsViewModel

class NewsActivity : AppCompatActivity() {

   private lateinit var binding: ActivityNewsBinding

     lateinit var viewModel: NewsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_news)


        val newsRepository = NewsRepository(ArticleDatabase(this))

        val viewModelProviderFactory = NewsViewModelProviderFactory(newsRepository)

        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(NewsViewModel::class.java)

        binding.bottomNavigationView.setupWithNavController(findNavController(R.id.newsNavHostFragment))
    }
}