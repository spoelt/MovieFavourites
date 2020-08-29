package com.spoelt.moviefavourites.ui.fragments


import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.spoelt.moviefavourites.R
import com.spoelt.moviefavourites.constants.DASH
import com.spoelt.moviefavourites.data.database.MovieDatabase
import com.spoelt.moviefavourites.data.model.Movie
import com.spoelt.moviefavourites.databinding.FragmentMovieDetailsBinding
import com.spoelt.moviefavourites.ui.viewModel.MovieDetailsViewModel
import com.spoelt.moviefavourites.ui.viewModel.ViewModelFactory
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso

class MovieDetailsFragment : Fragment() {
    private lateinit var binding: FragmentMovieDetailsBinding
    private lateinit var detailsViewModel: MovieDetailsViewModel
    private lateinit var args: MovieDetailsFragmentArgs
    private var movieInDb = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_movie_details, container, false)
        binding.lifecycleOwner = this

        setUpViewModel()
        setUpObserver()
        handlePassedData()
        setHasOptionsMenu(true)

        return binding.root
    }

    private fun setUpViewModel() {
        val application = requireNotNull(this.activity).application
        val dataSource = MovieDatabase.getInstance(application).movieDao
        val viewModelFactory = ViewModelFactory(dataSource, application)

        detailsViewModel =
            ViewModelProvider(
                this, viewModelFactory
            ).get(MovieDetailsViewModel::class.java)
    }

    private fun setUpObserver() {
        detailsViewModel.favouriteMovies.observe(viewLifecycleOwner, Observer {
            movieInDb = it.contains(args.movie)
        })
    }

    private fun handlePassedData() {
        args = MovieDetailsFragmentArgs.fromBundle(requireArguments())
        displayMovieDetails(args.movie)
    }

    private fun displayMovieDetails(movie: Movie) {
        binding.movieTitleDetail.text = resources.getString(
            R.string.movie_header,
            movie.title,
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_movie_detail, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.saveAsFavourite -> {
                if (movieInDb) {
                    detailsViewModel.deleteMovieFromFavourites(args.movie)
                } else {
                    detailsViewModel.saveMovieAsFavourite(args.movie)
                }
                displayMessage(args.movie.title, movieInDb)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun displayMessage(title: String, movieFavourited: Boolean) {
        val stringId: Int = when (movieFavourited) {
            true -> R.string.movie_removed
            else -> R.string.movie_added
        }
        Toast.makeText(context, resources.getString(stringId, title), Toast.LENGTH_SHORT)
            .show()
    }
}