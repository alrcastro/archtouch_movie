package com.arctouch.codechallenge.home

import android.util.Log
import com.arctouch.codechallenge.data.Cache
import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.model.UpcomingMoviesResponse
import com.arctouch.codechallenge.repositories.IMovieRepository
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import kotlin.collections.ArrayList

class HomePresenter(val view: HomeContract.View, val moviesRepository: IMovieRepository) : HomeContract {

    var publishSubject = PublishSubject.create<Long>()
    val compositeDisposable = CompositeDisposable()

    override var page: Long = 1
    override var movieList:ArrayList<Movie> = arrayListOf()
    override var loading: Boolean = false

    override fun loadData() {

        view.showProgressBar()
        loadMovies()
    }

    private fun loadMovies() {

        val disposable =
                publishSubject.flatMap {
                    loadGenres(it)
                }.flatMap {
                moviesRepository.getMovies(it).subscribeOn(Schedulers.io())  }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe ({
                    val moviesWithGenres = it.results.map { movie ->
                        movie.copy(genres = Cache.genres.filter { movie.genreIds?.contains(it.id) == true })
                    }
                    val newMovies = moviesWithGenres.toCollection(ArrayList())
                    movieList.addAll(newMovies)

                    // Se ta vazio continua na mesma pagina
                    if (newMovies.isEmpty() && page > 1)
                        page--

                    view.addMovies(newMovies)
                    performLoading(false)
                },{
                it.printStackTrace()
                 })
        compositeDisposable.add(disposable)
    }

    override fun loadMoviesPage() {
        if (movieList.isEmpty()) {
            performLoading(true)
            publishSubject.onNext(page)
        }
        else {
            view.addMovies(movieList)
            performLoading(false)
        }
    }

    override fun loadNextPage() {
        performLoading(true)
        page++
        publishSubject.onNext(page)
    }

    private fun performLoading(value: Boolean) {
        loading = value
        if (value)
            view.showProgressBar()
        else
            view.hideProgressBar()
    }

   private fun loadGenres(page: Long) : Observable<Long>{
       if (Cache.genres.isEmpty())
          return moviesRepository.getGenres()
                .subscribeOn(Schedulers.io())
                .doOnNext {
                    Cache.cacheGenres(it.genres)

                }.flatMap {
                       Observable.just(page)
                   }
       return Observable.just(page)
    }

    override fun dispose() {
        compositeDisposable.dispose()
    }

}