package org.d3if3062.mobpro1.ui.api

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.d3if3062.mobpro1.R
import org.d3if3062.mobpro1.databinding.ListItemBinding
import org.d3if3062.mobpro1.model.Kegiatan
import org.d3if3062.mobpro1.netwrok.KegiatanApi

class ApiAdapter : RecyclerView.Adapter<ApiAdapter.ViewHolder>() {

    private val data = mutableListOf<Kegiatan>()
    fun updateData(newData: List<Kegiatan>) {
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }

    class ViewHolder(
        private val binding: ListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(kegiatan: Kegiatan) = with(binding) {
            textView2.text = kegiatan.aktivitas
            deskripsiTextView.text = kegiatan.deskripsi

            Log.d("ApiAdapter", "Image URL: ${KegiatanApi.getKegiatanUrl(kegiatan.imageId)}")
            Glide.with(imageView.context)
                .load(KegiatanApi.getKegiatanUrl(kegiatan.imageId))
                .error(R.drawable.ic_baseline_broken_image_24)
                .into(imageView)

            root.setOnClickListener {
                val message = root.context.getString(R.string.message, kegiatan.aktivitas)
                Toast.makeText(root.context, message, Toast.LENGTH_LONG).show()
            }


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])


    }
    override fun getItemCount(): Int {
        return data.size
    }
}