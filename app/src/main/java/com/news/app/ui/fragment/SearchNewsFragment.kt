package com.news.app.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.news.app.R
import com.news.app.databinding.FragmentSearchnewsBinding
import com.news.app.databinding.FrgmentSavednewsBinding
import com.news.app.ui.NewsActivity
import com.news.app.ui.adapter.NewsAdapter
import com.news.app.ui.db.ArticleDatabase
import com.news.app.ui.repositary.NewsRepository
import com.news.app.ui.repositary.NewsViewModelProviderFactory
import com.news.app.ui.util.Resource
import com.news.app.ui.viewModel.NewsViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

class SearchNewsFragment : Fragment() {

    private lateinit var binding: FragmentSearchnewsBinding

    lateinit var viewModel: NewsViewModel

    lateinit var newsAdapter: NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentSearchnewsBinding.inflate(inflater, container, false)
        val newsRepository = NewsRepository(ArticleDatabase(this.requireContext()))

        val viewModelProviderFactory = NewsViewModelProviderFactory(newsRepository)

        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(NewsViewModel::class.java)
        setNewsAdapter()

        var job: Job? = null
        binding.etSearch.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(5000)
                editable?.let {
                    if (editable.toString().isNotEmpty()) {
                        viewModel.serarchNews(editable.toString())
                    }
                }
            }
        }

        viewModel.searchNews.observe(viewLifecycleOwner, Observer { response ->
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
            findNavController().navigate(R.id.action_searchNewsFragment_to_articleFragment,
                bundle)
        }

        return binding.root
    }

    private fun setNewsAdapter() {
        newsAdapter = NewsAdapter()
        binding.rvSearchNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}