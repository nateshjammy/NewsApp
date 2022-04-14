package com.news.app.ui.repositary

import com.news.app.ui.Article
import com.news.app.ui.api.RetrofitInstance
import com.news.app.ui.db.ArticleDatabase

class NewsRepository(
    val db :ArticleDatabase
) {

    suspend fun getBrakingNews(countryCode:String,pageNumber:Int) =
        RetrofitInstance.api.getBreakingNews(countryCode,pageNumber)

    suspend fun searchNews(searchQuery: String,pageNumber: Int) =
        RetrofitInstance.api.searchForNews(searchQuery,pageNumber)


    suspend fun insertArticle(article: Article) = db.getArticleDao().insert(article)

    fun getSaveArticle() = db.getArticleDao().getAllArticles()

    suspend fun deleteArticle(article: Article) = db.getArticleDao().deleteArticle(article)

}
