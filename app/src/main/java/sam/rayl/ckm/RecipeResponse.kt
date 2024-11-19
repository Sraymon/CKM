package sam.rayl.ckm

import com.google.gson.annotations.SerializedName

data class RecipeResponse(
    @SerializedName("results") val results: List<RecipeItem>, // Changed to RecipeItem
    @SerializedName("offset") val offset: Int,
    @SerializedName("number") val number: Int,
    @SerializedName("totalResults") val totalResults: Int
)

data class RecipeItem(  // Renamed from Recipe to RecipeItem
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("image") val image: String
)
