import com.google.gson.annotations.SerializedName

data class Ingredient(
    @SerializedName("strIngredient")
    val strIngredient: String
)