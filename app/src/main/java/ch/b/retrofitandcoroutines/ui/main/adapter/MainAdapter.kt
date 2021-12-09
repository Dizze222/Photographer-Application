package ch.b.retrofitandcoroutines.ui.main.adapter


import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ch.b.retrofitandcoroutines.R
import ch.b.retrofitandcoroutines.data.model.UserDTO
import ch.b.retrofitandcoroutines.databinding.GridItemBinding
import com.bumptech.glide.Glide
import kotlin.collections.ArrayList
import kotlin.math.log

class MainAdapter(private val userDTO: ArrayList<UserDTO>, val adapterOnClick: AdapterOnClick) :
    RecyclerView.Adapter<MainAdapter.DataViewHolder>() {

    var isShimmer = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = GridItemBinding.inflate(layoutInflater, parent, false)
        return DataViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(userDTO[position])
        holder.onClickItem(userDTO[position])


    }

    override fun getItemCount() = userDTO.size

    inner class DataViewHolder(private val binding: GridItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: UserDTO) {
            binding.apply {
                if (isShimmer){
                    binding.shimmerLayout.startShimmerAnimation()
                }else if (!isShimmer) {
                    authorName.text = data.authorOfPicture
                    IdOfPicture.text = data.ID
                    Glide.with(imageView)
                        .load(data.downloadedPicture)
                        .placeholder(R.drawable.custom_color)
                        .into(imageView)
                }
            }
        }

        fun onClickItem(item: UserDTO) {
            binding.imageView.setOnClickListener {
                adapterOnClick.onClick(item)
                Log.i("TAG",item.toString())
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addUsers(userDTOS: List<UserDTO>) {
        this.userDTO.apply {
            clear()
            addAll(userDTOS)
            notifyDataSetChanged()
        }
    }
}


