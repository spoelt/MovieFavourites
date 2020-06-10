package com.spoelt.moviefavourites.ui.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.spoelt.moviefavourites.R
import com.spoelt.moviefavourites.constants.DASH
import com.spoelt.moviefavourites.constants.MOVIE_KEY
import com.spoelt.moviefavourites.data.model.Movie
import com.spoelt.moviefavourites.databinding.FragmentMovieDetailsBinding
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso

class MovieDetailsFragment : Fragment() {
    private lateinit var binding: FragmentMovieDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_movie_details, container, false)

        binding.lifecycleOwner = this

        val movie = arguments?.getParcelable<Movie>(MOVIE_KEY)
        movie?.let { displayMovieDetails(it) }

        return binding.root
    }

    private fun displayMovieDetails(movie: Movie) {
        binding.movieTitleDetail.text = resources.getString(
            R.string.movie_header,
            movie.original_title,
            movie.release_date.substringBefore(DASH)
        )
        binding.movieOverview.text = movie.overview
        binding.voteAverage.text = movie.vote_average.toString()
        Picasso.get()
            .load("https://image.tmdb.org/t/p/w185_and_h278_bestv2/${movie.poster_path}")
            .memoryPolicy(MemoryPolicy.NO_CACHE)
            .memoryPolicy(MemoryPolicy.NO_STORE)
            .networkPolicy(NetworkPolicy.NO_CACHE)
            .error(R.drawable.ic_broken_image_gray_24dp)
            .into(binding.moviePosterDetail)
    }
}
