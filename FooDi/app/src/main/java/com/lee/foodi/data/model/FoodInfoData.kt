package com.lee.foodi.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class FoodInfoData (
    @SerializedName("DESC_KOR")
    val foodName : String ,
    @SerializedName("SERVING_WT")
    val servingWeight : String ,
    @SerializedName("NUTR_CONT1")
    val calorie : String ,
    @SerializedName("NUTR_CONT2")
    val carbohydrate : String ,
    @SerializedName("NUTR_CONT3")
    val protein : String ,
    @SerializedName("NUTR_CONT4")
    val fat : String ,
    @SerializedName("NUTR_CONT5")
    val sugar : String ,
    @SerializedName("NUTR_CONT6")
    val salt : String ,
    @SerializedName("NUTR_CONT7")
    val cholesterol : String ,
    @SerializedName("NUTR_CONT8")
    val saturatedFat : String ,
    @SerializedName("NUTR_CONT9")
    val transFat : String ,
    @SerializedName("BGN_YEAR")
    val beginYear : String ,
    @SerializedName("ANIMAL_PLANT")
    val company : String
) : Serializable