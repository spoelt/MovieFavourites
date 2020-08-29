package com.spoelt.moviefavourites.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.spoelt.moviefavourites.R
import com.spoelt.moviefavourites.constants.DASH
import com.spoelt.moviefavourites.data.model.Movie
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.movie_layout.view.*

class MovieAdapter(val clickListener: OnMovieClickListener) :
    RecyclerView.Adapter<MovieAdapter.MoviesViewHolder>() {
    private var movieList: List<Movie> = ArrayList()
    private lateinit var onBottomReachedListener: OnBottomReachedListener

    class MoviesViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        private val imageViewMoviePoster: ImageView = itemView.moviePoster
        private val textViewMovieTitle: TextView = itemView.movieTitle
        private val textViewReleaseYear: TextView = itemView.releaseYear

        fun bind(movie: Movie, action: OnMovieClickListener) {
            textViewMovieTitle.text = movie.title
            textViewReleaseYear.text = movie.release_date.substringBefore(DASH)
            Picasso.get()
                .load("https://image.tmdb.org/t/p/w185_and_h278_bestv2/${movie.poster_path}")
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .memoryPolicy(MemoryPolicy.NO_STORE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .error(R.drawable.ic_broken_image_gray_24dp)
                .into(imageViewMoviePoster)

            itemView.setOnClickListener {
                action.onItemClick(movie)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.movie_layout, parent, false)
        return MoviesViewHolder(itemView)
    }

    override fun getItemCount(): Int = movieList.size

    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {
        holder.bind(movieList.get(position), clickListener)

        if (position == movieList.size - 1) {
            onBottomReachedListener.onBottomReached(position)
        }
    }

    fun setOnBottomReachedListener(listener: OnBottomReachedListener) {
        onBottomReachedListener = listener
    }

    fun updateList(updatedList: List<Movie>) {
        movieList = updatedList
        notifyItemRangeChanged(0, itemCount)
    }

    interface OnMovieClickListener {
        fun onItemClick(movie: Movie)
    }
}

interface OnBottomReachedListener {
    fun onBottomReached(position: Int)
}