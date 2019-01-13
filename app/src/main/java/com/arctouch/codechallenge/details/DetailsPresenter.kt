package com.arctouch.codechallenge.details

import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.util.MovieImageUrlBuilder

class DetailsPresenter (val view: DetailsContract.View, val movie : Movie?) : DetailsContract {

    override fun loadData() {
        movie?.let{
            view.populateFields(it)
            val movieImageUrlBuilder = MovieImageUrlBuilder()
            val posterPath = movie.posterPath?.let { movieImageUrlBuilder.buildPosterUrl(it) };
            val backDropPath = movie.backdropPath?.let { movieImageUrlBuilder.buildPosterUrl(it) };
            view.loadImages(backDropPath,posterPath)
        }
    }
}