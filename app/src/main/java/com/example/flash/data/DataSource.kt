package com.example.flash.data
import androidx.annotation.StringRes
import  com.example.flash.R

object DataSource {
fun loadCategories(): List<Categories>{
    return listOf<Categories>(
        Categories(stringResourceId = R.string.fresh_fruits, imageResourceId = R.drawable.fresh ),
        Categories(R.string.packaged_food, R.drawable.pakaged_food),
        Categories(R.string.pet_food, R.drawable.pet_food),
        Categories(R.string.beverages, R.drawable.beverages),
        Categories(R.string.bread_biscuits, R.drawable.bread_biscuit),
        Categories(R.string.kitchen_essentials, R.drawable.kitchen),
        Categories(R.string.stationary, R.drawable.stationary),
        Categories(R.string.cleaning_essentials, R.drawable.bathroom),
        Categories(R.string.munchies, R.drawable.munchies),
        Categories(R.string.vegetables, R.drawable.vegetables),

    )
}
   fun loadItems(
       @StringRes categoryName: Int
   ): List<Item> {
   return  listOf(
    Item(R.string.banana_robusta, R.string.fresh_fruits, "1kg", 100, R.drawable.banana),
    Item(R.string.shimla_apple, R.string.fresh_fruits, "1kg", 250, R.drawable.apple),
    Item(R.string.papaya_seal_ripe, R.string.fresh_fruits, "1kg", 150, R.drawable.papaya),
    Item(R.string.pomegranate, R.string.fresh_fruits, "500g", 150, R.drawable.pomegrante),
    Item(R.string.pineapple, R.string.fresh_fruits, "1kg", 130, R.drawable.download),
    Item(R.string.pepsi_can, R.string.beverages, "1", 40, R.drawable.pepsi)


).filter {
    it.itemCategoryId == categoryName
   }
   }
}