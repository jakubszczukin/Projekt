package com.example.projekt

import android.content.ClipData.Item
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class SearchViewModel: ViewModel() {

    var fCity = MutableLiveData<String>()

    fun select(city: String){
        fCity.value = city
    }

}