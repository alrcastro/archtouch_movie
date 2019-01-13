package com.arctouch.codechallenge.home

import com.arctouch.codechallenge.data.Cache
import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.repositories.IMovieRepository
import io.reactivex.BackpressureStrategy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.processors.PublishProcessor
import io.reactivex.schedulers.Schedulers
import java.util.*
import kotlin.collections.ArrayList

class HomePresenter(val view: HomeContract.View, val moviesRepository: IMovieRepository) : HomeContract {

    var page: Long = 1
    var pub = PublishProcessor.create<Long>()
    val compositeDisposable = CompositeDisposable()

    override var movieList:ArrayList<Movie> = arrayListOf()

    override fun loadData() {

        view.showProgressBar()

        if (Cache.genres.isEmpty())
            loadGenres()
        else {
            if (movieList.isEmpty())
                loadMovies()
            else
                setAdapter()
        }
    }

    private fun loadMovies() {

        val disposable = moviesRepository.getMovies(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    val moviesWithGenres = it.results.map { movie ->
                        movie.copy(genres = Cache.genres.filter { movie.genreIds?.contains(it.id) == true })
                    }
                    movieList = moviesWithGenres.toCollection(ArrayList())

                    setAdapter()
                }
        compositeDisposable.add(disposable)
        //pub.onNext(1)
    }

    private fun setAdapter() {
            val homeAdapter = HomeAdapter(movieList)
            view.setAdapter(homeAdapter)

            homeAdapter.onItemClick = {
                view.onClick(it)
            }
        view.hideProgressBar()
    }

   private fun loadGenres() {
          val disposable = moviesRepository.getGenres()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    Cache.cacheGenres(it.genres)
                    loadMovies()
                }

        compositeDisposable.add(disposable)
    }

    override fun dispose() {
        compositeDisposable.dispose()
    }

}