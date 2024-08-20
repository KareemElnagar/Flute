package com.kareem.viewModels

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.kareem.flute.models.CategoryModel

class MainViewModel : ViewModel() {
    private lateinit var db: FirebaseFirestore

     fun getCategories(func:(MutableList<CategoryModel?>) -> Unit) {
        db.collection("category")
            .get().addOnSuccessListener {
                val categoryList = it.toObjects(CategoryModel::class.java)
                func(categoryList)
            }
    }

}