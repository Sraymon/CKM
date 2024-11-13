package sam.rayl.ckm

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class RecipeAdapter(private var recipes: List<Recipe>) : RecyclerView.Adapter<RecipeAdapter.ViewHolder>() {

    // ViewHolder class to hold references to each item view
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val recipeImageView: ImageView = itemView.findViewById(R.id.recipeImageView)
        val recipeTitleTextView: TextView = itemView.findViewById(R.id.recipeTitleTextView)
    }

    // Inflate the item layout and create the ViewHolder instance
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recipe_item, parent, false)
        return ViewHolder(view)
    }

    // Bind the data to the views in each ViewHolder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recipe = recipes[position]
        holder.recipeTitleTextView.text = recipe.title
        Picasso.get().load(recipe.image).into(holder.recipeImageView)
    }

    // Return the total number of items in the list
    override fun getItemCount() = recipes.size

    // Update the list of recipes and refresh the RecyclerView
    fun updateData(newRecipes: List<Recipe>) {
        recipes = newRecipes
        notifyDataSetChanged()
    }
}
