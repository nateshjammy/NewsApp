package com.news.app.ui.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.news.app.ui.Article
import com.news.app.ui.NewsResponse
import com.news.app.ui.repositary.NewsRepository
import com.news.app.ui.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel(
     val newsRepository: NewsRepository,
) : ViewModel() {

    val brakingNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    val brakingNewsPage = 1

    val searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    val searchNewsPage = 1

    init {

        getBreakingNews("us")
    }

    fun getBreakingNews(countryCode: String) = viewModelScope.launch {
        brakingNews.postValue(Resource.Loading())
        val response = newsRepository.getBrakingNews(countryCode, brakingNewsPage)
         brakingNews.postValue(breakingNewsResponse(response))
    }
    fun serarchNews(searchQuerry:String) = viewModelScope.launch {
        searchNews.postValue(Resource.Loading())
        val response = newsRepository.searchNews(searchQuerry,searchNewsPage)
        searchNews.postValue(handlesearchNewsResponse(response))
    }

     private fun breakingNewsResponse(response:Response<NewsResponse>) :Resource<NewsResponse>{
          if(response.isSuccessful){
               response.body()?.let { resultResponse->
                    return Resource.Success(resultResponse)
               }
          }
          return Resource.Error(response.message())
     }

    private fun handlesearchNewsResponse(response: Response<NewsResponse>):Resource<NewsResponse>{
        if(response.isSuccessful){
            response.body()?.let { resultResponse->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun saveArticle(article: Article) = viewModelScope.launch {
        newsRepository.insertArticle(article)
    }

    fun deleteArticle(article: Article) = viewModelScope.launch {
        newsRepository.deleteArticle(article)
    }

    fun getSavedNews() = newsRepository.getSaveArticle()
}