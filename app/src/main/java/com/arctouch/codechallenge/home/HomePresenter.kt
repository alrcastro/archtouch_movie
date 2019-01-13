package com.arctouch.codechallenge.home

import android.content.Intent
import com.arctouch.codechallenge.api.TmdbApi
import com.arctouch.codechallenge.data.Cache
import com.arctouch.codechallenge.model.Movie
import io.reactivex.BackpressureStrategy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.processors.PublishProcessor
import io.reactivex.schedulers.Schedulers
import java.util.*

class HomePresenter(val view: View, val api : TmdbApi) {

    var page: Long = 1
    var pub = PublishProcessor.create<Long>()

    fun start() {

        view.showProgressBar()

        if (Cache.genres.count() == 0)
            loadGenres()
        else
            loadMovies()
    }

    fun loadMovies() {

         api.upcomingMovies(TmdbApi.API_KEY, TmdbApi.DEFAULT_LANGUAGE, page, TmdbApi.DEFAULT_REGION).toFlowable(BackpressureStrategy.DROP)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    val moviesWithGenres = it.results.map { movie ->
                        movie.copy(genres = Cache.genres.filter { movie.genreIds?.contains(it.id) == true })
                    }
                    val homeAdapter = HomeAdapter(moviesWithGenres)
                    view.setAdapter(homeAdapter)

                    homeAdapter.onItemClick = {
                        view.onClick(it)
                    }

                    view.hideProgressBar()
                }

        //pub.onNext(1)
    }

    fun loadGenres() {
        api.genres(TmdbApi.API_KEY, TmdbApi.DEFAULT_LANGUAGE)
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