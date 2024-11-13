package sam.rayl.ckm

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

// Import RecipeResponse if it’s in the same package or another package
import sam.rayl.ckm.RecipeResponse

interface SpoonacularApiService {
    @GET("recipes/complexSearch")
    fun searchRecipes(
        @Query("query") query: String,
        @Query("apiKey") apiKey: String
    ): Call<RecipeResponse>
}
