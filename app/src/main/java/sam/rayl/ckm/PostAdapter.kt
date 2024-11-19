package sam.rayl.ckm

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PostAdapter(
    private val posts: List<Post>,
    private val clickListener: (Post) -> Unit
) : RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val profileImageView: ImageView = itemView.findViewById(R.id.postProfileImageView)
        private val userNameTextView: TextView = itemView.findViewById(R.id.postUserNameTextView)
        private val locationTextView: TextView = itemView.findViewById(R.id.postLocationTextView)
        private val postImageView: ImageView = itemView.findViewById(R.id.postImageView)

        fun bind(post: Post) {
            profileImageView.setImageResource(post.profileImageRes)
            userNameTextView.text = post.userName
            locationTextView.text = post.location
            postImageView.setImageResource(post.postImageRes)

            itemView.setOnClickListener {
                clickListener(post)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.post_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(posts[position])
    }

    override fun getItemCount(): Int = posts.size
}
