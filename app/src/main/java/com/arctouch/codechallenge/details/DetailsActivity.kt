package com.arctouch.codechallenge.details
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.util.Constants

import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.android.synthetic.main.content_details.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.text.SimpleDateFormat


class DetailsActivity : AppCompatActivity(), DetailsContract.View {

    lateinit var detailPresenter: DetailsContract

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val movie = intent.extras.getSerializable(Constants.MOVIE) as? Movie

        detailPresenter = DetailsPresenterFactory.createPresenter(this,movie)
        detailPresenter.loadData()

    }

    override fun populateFields(movie : Movie) {
        toolbar.title = movie.title

        txtName.text = movie.title
        txtOverview.text = movie.overview
        txtGenreList.text = movie.genres?.joinToString(separator = ",") { it.name }

        // Convertendo a data
        val fmt = SimpleDateFormat("yyyy-MM-dd")
        val dt = fmt.parse( movie.releaseDate)
        var dateFormat = android.text.format.DateFormat.getDateFormat(this)

        txtReleaseDate.text = dateFormat.format(dt)
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
