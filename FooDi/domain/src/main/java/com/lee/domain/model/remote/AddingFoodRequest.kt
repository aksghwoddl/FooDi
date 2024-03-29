package com.lee.domain.model.remote

import com.google.gson.annotations.SerializedName

data class AddingFoodRequest(
    @SerializedName("DESC_KOR")
    var foodName : String ,
    @SerializedName("SERVING_WT")
    var servingWeight : String ,
    @SerializedName("NUTR_CONT1")
    var calorie : String ,
    @SerializedName("NUTR_CONT2")
    var carbohydrate : String ,
    @SerializedName("NUTR_CONT3")
    var protein : String ,
    @SerializedName("NUTR_CONT4")
    var fat : String ,
    @SerializedName("NUTR_CONT5")
    var sugar : String ,
    @SerializedName("NUTR_CONT6")
    var salt : String ,
    @SerializedName("NUTR_CONT7")
    var cholesterol : String ,
    @SerializedName("NUTR_CONT8")
    var saturatedFat : String ,
    @SerializedName("NUTR_CONT9")
    var transFat : String ,
    @SerializedName("ANIMAL_PLANT")
    var company : String
)