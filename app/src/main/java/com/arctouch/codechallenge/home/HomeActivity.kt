package com.arctouch.codechallenge.home

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.arctouch.codechallenge.details.DetailsActivity
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.util.Constants
import kotlinx.android.synthetic.main.home_activity.*

class HomeActivity : AppCompatActivity(), HomeContract.View {

    lateinit var presenter : HomeContract
    lateinit var adapter : HomeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)
        presenter = HomePresenterFactory.createPresenter(this)

        if (savedInstanceState?.get(Constants.LIST) != null)
            presenter.movieList = savedInstanceState.get(Constants.LIST) as ArrayList<Movie>

        if (savedInstanceState?.get(Constants.PAGE) != null)
            presenter.page = savedInstanceState.getLong(Constants.PAGE)

        createAdapter()
        presenter.loadData()
        setRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        if (presenter.movieList.isEmpty())
            presenter.loadMoviesPage()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putSerializable(Constants.LIST, presenter.movieList)
        outState?.putLong(Constants.PAGE, presenter.page)
    }

    override fun onClick(movie : Movie) {
        val intent = Intent(this, DetailsActivity::class.java)
        intent.putExtra(Constants.MOVIE, movie)
        startActivity(intent)
    }

    private fun setRecyclerView() {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                val totalItemCount = presenter.movieList.count()

                val lastVisibleItem = (recyclerView!!.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()

                if (!presenter.loading && totalItemCount == lastVisibleItem + 1) {
                    presenter.loadNextPage()
                }
            }
        })
    }

    fun createAdapter() {
        adapter = HomeAdapter(arrayListOf())

        adapter.onItemClick = {
            onClick(it)
        }
        recyclerView.adapter = adapter
    }

    override fun addMovies(movieList: List<Movie>) {
        adapter.addItens(movieList)
        adapter.notifyDataSetChanged()
    }

    override fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        progressBar.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.dispose()
    }

}
