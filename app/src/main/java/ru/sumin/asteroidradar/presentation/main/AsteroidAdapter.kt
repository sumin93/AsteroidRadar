package ru.sumin.asteroidradar.presentation.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.sumin.asteroidradar.databinding.AsteroidItemBinding
import ru.sumin.asteroidradar.domain.AsteroidEntity

class AsteroidAdapter(
    private val onAsteroidClickListener: ((AsteroidEntity) -> Unit)? = null
) : ListAdapter<AsteroidEntity, AsteroidAdapter.AsteroidViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidViewHolder {
        val binding = AsteroidItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        val holder = AsteroidViewHolder(binding)
        binding.root.setOnClickListener {
            onAsteroidClickListener?.invoke(getItem(holder.adapterPosition))
        }
        return holder
    }

    override fun onBindViewHolder(holder: AsteroidViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class AsteroidViewHolder(
        private val binding: AsteroidItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(asteroid: AsteroidEntity) {
            binding.asteroid = asteroid
        }
    }

    private companion object {
        val diffCallback = object : DiffUtil.ItemCallback<AsteroidEntity>() {
            override fun areItemsTheSame(
                oldItem: AsteroidEntity,
                newItem: AsteroidEntity
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: AsteroidEntity,
                newItem: AsteroidEntity
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
