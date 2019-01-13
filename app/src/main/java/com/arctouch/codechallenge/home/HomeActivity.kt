package com.arctouch.codechallenge.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.arctouch.codechallenge.details.DetailsActivity
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.base.BaseActivity
import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.util.Constants
import kotlinx.android.synthetic.main.home_activity.*
import io.reactivex.processors.PublishProcessor


class HomeActivity : BaseActivity(), HomePresenter.View {

    lateinit var presenter : HomePresenter

    var pub = PublishProcessor.create<Long>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)

        presenter = HomePresenter(this, api)
        presenter.start()

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
