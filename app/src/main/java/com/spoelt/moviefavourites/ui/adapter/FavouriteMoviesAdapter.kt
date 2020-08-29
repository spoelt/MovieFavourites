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

class FavouriteMoviesAdapter(val movies: List<Movie>, val clickListener: OnMovieClickListener) :
    RecyclerView.Adapter<FavouriteMoviesAdapter.FavouriteMoviesViewHolder>() {
    private var favouriteMoviesList: List<Movie> = ArrayList(movies)

    class FavouriteMoviesViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        private val textViewMovieTitle: TextView = itemView.movieTitle
        private val textViewReleaseYear: TextView = itemView.releaseYear
        private val imageViewMoviePoster: ImageView = itemView.moviePoster

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteMoviesViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.favourite_layout, parent, false)
        return FavouriteMoviesViewHolder(itemView)
    }

    override fun getItemCount(): Int = favouriteMoviesList.size

    override fun onBindViewHolder(holder: FavouriteMoviesViewHolder, position: Int) {
        holder.bind(favouriteMoviesList.get(position), clickListener)
    }

    interface OnMovieClickListener {
        fun onItemClick(movie: Movie)
    }
}