package sam.rayl.ckm

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class StoryAdapter(
    private val stories: List<Story>,
    private val clickListener: (Story) -> Unit
) : RecyclerView.Adapter<StoryAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val storyImageView: ImageView = itemView.findViewById(R.id.storyImageView)
        val storyNameTextView: TextView = itemView.findViewById(R.id.storyNameTextView)

        fun bind(story: Story) {
            storyNameTextView.text = story.name
            storyImageView.setImageResource(story.imageRes)
            itemView.setOnClickListener {
                clickListener(story)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.story_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(stories[position])
    }

    override fun getItemCount() = stories.size
}
