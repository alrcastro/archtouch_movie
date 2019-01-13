package com.arctouch.codechallenge.home

import android.content.Intent
import com.arctouch.codechallenge.api.Api
import com.arctouch.codechallenge.api.TmdbApi
import com.arctouch.codechallenge.data.Cache
import com.arctouch.codechallenge.model.Movie
import io.reactivex.BackpressureStrategy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.processors.PublishProcessor
import io.reactivex.schedulers.Schedulers
import java.util.*
import kotlin.collections.ArrayList

class HomePresenter(val view: View) {

    var page: Long = 1
    var pub = PublishProcessor.create<Long>()
    lateinit var movieList:ArrayList<Movie>

    fun start() {

        view.showProgressBar()

        if (Cache.genres.isEmpty())
            loadGenres()
        else {
            if (movieList?.isEmpty())
                loadMovies()
            else
                setAdapter()
        }
    }

    fun loadMovies() {

            Api.mbdApi.upcomingMovies(TmdbApi.API_KEY, TmdbApi.DEFAULT_LANGUAGE, page, TmdbApi.DEFAULT_REGION)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    val moviesWithGenres = it.results.map { movie ->
                        movie.copy(genres = Cache.genres.filter { movie.genreIds?.contains(it.id) == true })
                    }
                    movieList = moviesWithGenres.toCollection(ArrayList())

                    setAdapter()
                }

        //pub.onNext(1)
    }

    fun setAdapter() {
        val homeAdapter = HomeAdapter(movieList)
        view.setAdapter(homeAdapter)

        homeAdapter.onItemClick = {
            view.onClick(it)
        }

        view.hideProgressBar()
    }

    fun loadGenres() {
        Api.mbdApi.genres(TmdbApi.API_KEY, TmdbApi.DEFAULT_LANGUAGE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    Cache.cacheGenres(it.genres)
                    loadMovies()
                }
    }

    fun stop() {

    }

    interface View {
        fun showProgressBar()
        fun hideProgressBar()
        fun setAdapter(homeAdapter: HomeAdapter)
        fun onClick(movie : Movie)
    }

}