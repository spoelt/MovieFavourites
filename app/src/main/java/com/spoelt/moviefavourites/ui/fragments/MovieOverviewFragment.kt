package com.spoelt.moviefavourites.ui.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.spoelt.moviefavourites.R
import com.spoelt.moviefavourites.constants.MOVIE_KEY
import com.spoelt.moviefavourites.databinding.FragmentMovieOverviewBinding
import com.spoelt.moviefavourites.ui.adapter.MovieAdapter
import com.spoelt.moviefavourites.ui.adapter.OnBottomReachedListener
import com.spoelt.moviefavourites.ui.viewModel.OverviewViewModel


class MovieOverviewFragment : Fragment() {
    private lateinit var viewModel: OverviewViewModel
    private lateinit var movieAdapter: MovieAdapter
    private lateinit var binding: FragmentMovieOverviewBinding
    private var reloadingNeeded = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_movie_overview, container, false)

        viewModel = ViewModelProvider(this).get(OverviewViewModel::class.java)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        setUpRecyclerView()
        setUpListeners()
        setUpObservers()

        return binding.root
    }

    private fun setUpRecyclerView() {
        movieAdapter = MovieAdapter { movie ->
            val bundle = Bundle()
            bundle.putParcelable(MOVIE_KEY, movie)
            findNavController().navigate(R.id.movieDetailsFragment, bundle)
        }

        movieAdapter.setOnBottomReachedListener(object : OnBottomReachedListener {
            override fun onBottomReached(position: Int) {
                if (reloadingNeeded) {
                    viewModel.loadNewMovies()
                    reloadingNeeded = false
                }
            }
        })

        binding.movieOverviewRecyclerView.layoutManager = GridLayoutManager(context, 2)
        binding.movieOverviewRecyclerView.adapter = movieAdapter
    }

    private fun setUpListeners() {
        binding.swipeLayout.setOnRefreshListener {
            viewModel.refreshMovies()
            binding.swipeLayout.isRefreshing = false
            reloadingNeeded = true
        }
    }

    private fun setUpObservers() {
        viewModel.movieList.observe(viewLifecycleOwner, Observer {
            it?.let {
                movieAdapter.updateList(it)
                reloadingNeeded = true
            }
        })

        viewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            it?.let {
                toggleErrorView(it)
                reloadingNeeded = true
            }
        })
    }

    private fun toggleErrorView(message: String) {
        if (message.isEmpty()) {
            binding.errorMessage.visibility = GONE
            binding.refresh.hide()
            binding.swipeLayout.visibility = VISIBLE
        } else {
            binding.errorMessage.visibility = VISIBLE
            binding.errorMessage.text = message
            binding.refresh.show()
            binding.swipeLayout.visibility = GONE
        }
    }
}
