package com.arctouch.codechallenge.home

import com.arctouch.codechallenge.repositories.MovieRepository

    class HomePresenterFactory {
            companion object {
                fun createPresenter(view: HomeContract.View) : HomeContract {
                    return HomePresenter(view, MovieRepository())
                }
            }
    }