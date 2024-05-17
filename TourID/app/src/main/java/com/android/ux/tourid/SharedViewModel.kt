package com.android.ux.tourid

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel: ViewModel() {
    val message = MutableLiveData<String>()
    val blogid = MutableLiveData<Long>()
}