package com.spoelt.moviefavourites.ui.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.spoelt.moviefavourites.R
import com.spoelt.moviefavourites.data.database.MovieDatabase
import com.spoelt.moviefavourites.data.model.Movie
import com.spoelt.moviefavourites.databinding.FragmentFavouriteMoviesBinding
import com.spoelt.moviefavourites.ui.adapter.FavouriteMoviesAdapter
import com.spoelt.moviefavourites.ui.viewModel.FavouritesViewModel
import com.spoelt.moviefavourites.ui.viewModel.ViewModelFactory

/**
 * A simple [Fragment] subclass.
 */
class FavouriteMoviesFragment : Fragment(), FavouriteMoviesAdapter.OnMovieClickListener {
    private lateinit var viewModel: FavouritesViewModel
    private lateinit var favouriteMoviesAdapter: FavouriteMoviesAdapter
    private lateinit var binding: FragmentFavouriteMoviesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_favourite_movies, container, false)
        binding.lifecycleOwner = this

        setUpViewModel()
        setUpObserver()

        return binding.root
    }

    private fun setUpViewModel() {
        val application = requireNotNull(this.activity).application
        val dataSource = MovieDatabase.getInstance(application).movieDao
        val viewModelFactory = ViewModelFactory(dataSource, application)

        viewModel =
            ViewModelProvider(
                this, viewModelFactory
            ).get(FavouritesViewModel::class.java)
    }

    private fun setUpObserver() {
        viewModel.favouriteMovies.observe(viewLifecycleOwner, Observer {
            it?.let { movies ->
                setUpRecyclerView(movies)
            }
        })
    }

    private fun setUpRecyclerView(movieList: List<Movie>) {
        favouriteMoviesAdapter = FavouriteMoviesAdapter(movieList, this)
        binding.movieOverviewRecyclerView.adapter = favouriteMoviesAdapter
    }

    override fun onItemClick(movie: Movie) {
        val action =
            FavouriteMoviesFragmentDirections.actionFavouriteMoviesFragmentToMovieDetailsFragment(
                movie
            )
        view?.findNavController()?.navigate(action)
    }
}
