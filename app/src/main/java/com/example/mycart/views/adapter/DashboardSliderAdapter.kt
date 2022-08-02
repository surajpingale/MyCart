package com.example.mycart.views.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mycart.databinding.ImageSliderContainerBinding


class DashboardSliderAdapter(
    private val context: Context,
    private val imagesList: List<String>
) :
    RecyclerView.Adapter<DashboardSliderAdapter.ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ImageSliderContainerBinding.inflate(layoutInflater, parent, false)
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val string = imagesList[position]
        holder.binding.apply {
            Glide.with(context).load(string)
                .into(ivDashboardSliding)

        }
    }

    override fun getItemCount(): Int {
        return imagesList.size
    }

    inner class ImageViewHolder(itemView: ImageSliderContainerBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        var binding: ImageSliderContainerBinding

        init {
            binding = itemView
        }
    }
}