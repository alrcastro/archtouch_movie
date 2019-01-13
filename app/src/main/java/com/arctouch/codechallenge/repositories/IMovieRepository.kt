package com.arctouch.codechallenge.repositories

import com.arctouch.codechallenge.model.GenreResponse
import com.arctouch.codechallenge.model.UpcomingMoviesResponse
import io.reactivex.Observable

interface IMovieRepository {

    fun getMovies(page: Long) : Observable<UpcomingMoviesResponse>

    fun searchMovies()

    fun getGenres() : Observable<GenreResponse>

}