package com.arctouch.codechallenge.details
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.util.Constants

import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.android.synthetic.main.content_details.*
import android.graphics.drawable.Drawable
import com.arctouch.codechallenge.util.MovieImageUrlBuilder
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.movie_item.view.*


class DetailsActivity : AppCompatActivity(), DetailsPresenter.View {

    lateinit var detailPresenter: DetailsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        setSupportActionBar(toolbar)

        val movie = intent.extras.getSerializable(Constants.MOVIE) as? Movie

        detailPresenter = DetailsPresenter(this,movie)
        detailPresenter.start()

    }

    override fun populateFields(movie : Movie) {
       txtName.text = movie.title
        txtOverview.text = movie.overview
        txtGenreList.text = movie.genres?.joinToString(separator = ",") { it.name }
        txtReleaseDate.text = movie.releaseDate
    }

    override fun loadImages(backDrop: String?, poster: String?) {
        Glide.with(this)
                .load(poster)
                .apply(RequestOptions().placeholder(R.drawable.ic_image_placeholder))
                .into(imgPoster)

        Glide.with(this)
                .load(backDrop)
                .apply(RequestOptions().placeholder(R.drawable.ic_image_placeholder))
                .into(imgBackdrop)

    }


}
