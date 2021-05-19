package com.kenyrim.mvvm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {
    var lst = MutableLiveData<ArrayList<String>>()
    var newList = arrayListOf<String>()

    fun add(model: String){
        newList.add(model)
        lst.value = newList
    }

    fun remove(model: String){
        newList.remove(model)
        lst.value = newList
    }
}