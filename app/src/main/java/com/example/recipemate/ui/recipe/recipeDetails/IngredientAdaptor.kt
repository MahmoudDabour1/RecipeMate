package com.example.recipemate.ui.recipe.recipeDetails

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipemate.databinding.IngredientRawItemBinding


data class Ingredient(
    val name: String,
    val measure: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(measure)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Ingredient> {
        override fun createFromParcel(parcel: Parcel): Ingredient {
            return Ingredient(parcel)
        }

        override fun newArray(size: Int): Array<Ingredient?> {
            return arrayOfNulls(size)
        }
    }
}

class IngredientAdaptor(
    private var ingredients: List<Ingredient>
) : RecyclerView.Adapter<IngredientAdaptor.IngredientViewHolder>() {
    class IngredientViewHolder(private val binding: IngredientRawItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(ingredient: Ingredient) {
            binding.tvIngredientName.text = ingredient.name
            binding.tvIngredientMeasure.text = ingredient.measure
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        val binding = IngredientRawItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return IngredientViewHolder(binding)
    }

    override fun getItemCount(): Int = ingredients.size

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        holder.bind(ingredients[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateIngredients(newIngredients: List<Ingredient>) {
        ingredients = newIngredients
        notifyDataSetChanged()
    }

}