package com.arctouch.codechallenge.details

import com.arctouch.codechallenge.model.Movie


class DetailsPresenterFactory {
        companion object {
            fun createPresenter(view: DetailsContract.View, movie: Movie?) : DetailsContract{
                return DetailsPresenter(view, movie)
            }
        }
    }