package com.arctouch.codechallenge.home

import com.arctouch.codechallenge.model.Movie

interface HomeContract {

    var movieList:ArrayList<Movie>
    fun loadData()
    fun dispose()

    interface View {
        fun showProgressBar()
        fun hideProgressBar()
        fun setAdapter(homeAdapter: HomeAdapter)
        fun onClick(movie : Movie)
    }
}