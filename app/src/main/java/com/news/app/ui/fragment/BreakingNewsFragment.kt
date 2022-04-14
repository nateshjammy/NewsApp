package com.news.app.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.news.app.R
import com.news.app.databinding.FragmentBreakingnewsBinding
import com.news.app.ui.NewsActivity
import com.news.app.ui.adapter.NewsAdapter
import com.news.app.ui.db.ArticleDatabase
import com.news.app.ui.repositary.NewsRepository
import com.news.app.ui.repositary.NewsViewModelProviderFactory
import com.news.app.ui.util.Resource
import com.news.app.ui.viewModel.NewsViewModel
import timber.log.Timber

class BreakingNewsFragment : Fragment() {
    private lateinit var binding: FragmentBreakingnewsBinding

    lateinit var viewModel: NewsViewModel

    lateinit var newsAdapter: NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentBreakingnewsBinding.inflate(inflater, container, false)
        // viewModel = (activity as NewsActivity).viewModel
        val newsRepository = NewsRepository(ArticleDatabase(this.requireContext()))

        val viewModelProviderFactory = NewsViewModelProviderFactory(newsRepository)

        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(NewsViewModel::class.java)

        setNewsAdapter()
        viewModel.brakingNews.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    binding.paginationProgressBar.visibility = View.GONE
                    response.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles)
                    }
                }
                is Resource.Error -> {
                    binding.paginationProgressBar.visibility = View.VISIBLE

                    response.message?.let { message ->
                        Timber.d("An Error occured")
                    }
                }
                is Resource.Loading -> {
                    binding.paginationProgressBar.visibility = View.VISIBLE
                }
            }
        })
        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(R.id.action_breakingNewsFragment_to_articleFragment,
                bundle)
        }

        return binding.root
    }

    private fun setNewsAdapter() {
        newsAdapter = NewsAdapter()
        binding.rvBreakingNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}