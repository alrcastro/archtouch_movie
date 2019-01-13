package com.arctouch.codechallenge.repositories

import com.arctouch.codechallenge.api.Api
import com.arctouch.codechallenge.api.TmdbApi
import com.arctouch.codechallenge.model.GenreResponse
import com.arctouch.codechallenge.model.UpcomingMoviesResponse
import io.reactivex.Observable

class MovieRepository : IMovieRepository {

    override fun getGenres(): Observable<GenreResponse> {
        return Api.mbdApi.genres(TmdbApi.API_KEY, TmdbApi.DEFAULT_LANGUAGE)
    }

    override fun getMovies(page: Long): Observable<UpcomingMoviesResponse> {
        return Api.mbdApi.upcomingMovies(TmdbApi.API_KEY, TmdbApi.DEFAULT_LANGUAGE, page, TmdbApi.DEFAULT_REGION)
    }

    override fun searchMovies() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}