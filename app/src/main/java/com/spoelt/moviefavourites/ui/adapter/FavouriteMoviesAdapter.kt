package com.spoelt.moviefavourites.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.spoelt.moviefavourites.R
import com.spoelt.moviefavourites.constants.DASH
import com.spoelt.moviefavourites.data.model.Movie
import kotlinx.android.synthetic.main.movie_layout.view.*

class FavouriteMoviesAdapter(private val clickListener: ClickListener) :
    RecyclerView.Adapter<FavouriteMoviesAdapter.FavouriteMoviesViewHolder>() {
    private var favouriteMoviesList: List<Movie> = ArrayList()

    class FavouriteMoviesViewHolder(itemView: View, private val clickListener: ClickListener) :
        RecyclerView.ViewHolder(itemView) {
        private val textViewMovieTitle: TextView = itemView.movieTitle
        private val textViewReleaseYear: TextView = itemView.releaseYear

        fun bind(movie: Movie) {
            textViewMovieTitle.text = movie.title
            textViewReleaseYear.text = movie.release_date.substringBefore(DASH)

            itemView.setOnClickListener {
                clickListener(movie)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteMoviesViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.favourite_layout, parent, false)
        return FavouriteMoviesViewHolder(itemView, clickListener)
    }

    override fun getItemCount(): Int = favouriteMoviesList.size

    override fun onBindViewHolder(holder: FavouriteMoviesViewHolder, position: Int) {
        holder.bind(favouriteMoviesList[position])
    }

    fun updateList(updatedList: List<Movie>) {
        favouriteMoviesList = updatedList
        notifyItemRangeChanged(0, itemCount)
    }
}