package it.danielmilano.pokedex.pokemon.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import it.danielmilano.pokedex.utils.ListStringConverter

@Entity
@TypeConverters(ListStringConverter::class)
data class Pokemon(
    @PrimaryKey @NonNull var id: String,
    @SerializedName("base_experience") val baseExperience: Int,
    val height: Int,
    val name: String,
    val weight: Int
) {
    val heightInMeters: Float
        get() = height.toFloat() / 10

    val weightInKilograms: Float
        get() = weight.toFloat() / 10

    val displayExp: String
        get() = baseExperience.toString()
}


