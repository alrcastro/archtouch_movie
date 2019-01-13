package com.arctouch.codechallenge.home

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.arctouch.codechallenge.details.DetailsActivity
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.util.Constants
import kotlinx.android.synthetic.main.home_activity.*
import io.reactivex.processors.PublishProcessor


class HomeActivity : AppCompatActivity(), HomePresenter.View {

    lateinit var presenter : HomePresenter

    var pub = PublishProcessor.create<Long>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)
        presenter = HomePresenter(this)
        if (savedInstanceState?.get("list") != null)
            presenter.movieList = savedInstanceState.get("list") as ArrayList<Movie>
        presenter.start()

    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putSerializable("list", presenter.movieList)
    }

    override fun onClick(movie : Movie) {
        var intent = Intent(this, DetailsActivity::class.java)
        intent.putExtra(Constants.MOVIE, movie)
        startActivity(intent)
    }

    override fun setAdapter(homeAdapter: HomeAdapter) {
        recyclerView.adapter = homeAdapter
    }

    override fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {

        progressBar.visibility = View.GONE
    }


}
