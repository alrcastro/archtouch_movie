package com.arctouch.codechallenge.home

import com.arctouch.codechallenge.model.Movie

interface HomeContract {


    var movieList:ArrayList<Movie>
    var loading:Boolean
    var page:Long
    fun loadData()
    fun dispose()
    fun loadMoviesPage()
    fun loadNextPage()

    interface View {
        fun showProgressBar()
        fun hideProgressBar()
        fun addMovies(movieList: List<Movie>)
        fun onClick(movie : Movie)
    }
}