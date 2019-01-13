package com.arctouch.codechallenge.details

import com.arctouch.codechallenge.model.Movie

interface DetailsContract {

    fun loadData()

    interface View {
        fun populateFields(movie: Movie)
        fun loadImages(backDrop: String?, poster: String?)
    }
}