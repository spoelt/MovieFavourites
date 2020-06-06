package com.spoelt.moviefavourites.ui.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.spoelt.moviefavourites.R
import com.spoelt.moviefavourites.databinding.FragmentMovieOverviewBinding
import com.spoelt.moviefavourites.ui.adapter.MovieAdapter
import com.spoelt.moviefavourites.ui.viewModel.OverviewViewModel

/**
 * A simple [Fragment] subclass.
 */
class MovieOverviewFragment : Fragment() {
    private lateinit var viewModel: OverviewViewModel
    private lateinit var movieAdapter: MovieAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentMovieOverviewBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_movie_overview, container, false)

        viewModel = ViewModelProvider(this).get(OverviewViewModel::class.java)

        binding.lifecycleOwner = this

        movieAdapter = MovieAdapter { movie ->
            viewModel.displayMovieDetails(movie)
        }

        binding.movieOverviewRecyclerView.layoutManager = GridLayoutManager(context, 2)
        binding.movieOverviewRecyclerView.adapter = movieAdapter

        viewModel.movieList.observe(viewLifecycleOwner, Observer {
            it?.let {
                movieAdapter.updateList(it)
            }
        })

        /*viewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it.isNotEmpty()) {
                   // showErrorView(it)
                } else {
                   // hideErrorView()
                }
            }
        })*/

        return binding.root
    }
}
