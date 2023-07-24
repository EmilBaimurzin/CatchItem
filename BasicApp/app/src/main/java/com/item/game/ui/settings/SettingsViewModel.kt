package com.item.game.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SettingsViewModel: ViewModel() {
    private val _list = MutableLiveData<List<Int>>(emptyList())
    val list: LiveData<List<Int>> = _list

    private val _currentItem = MutableLiveData<Int>(0)
    val currentItem: LiveData<Int> = _currentItem

    private val _symbolState = MutableLiveData(1)
    val symbolState: LiveData<Int> = _symbolState

    fun initList(list: List<Int>) {
        _list.postValue(list)
    }

    fun setCurrentItem(currentItem: Int) {
        _currentItem.postValue(currentItem)
    }

    fun changeSymbolState(value: Int) {
        _symbolState.postValue(value)
    }

    fun moveLeft() {
        if (_currentItem.value!! > 0) {
            _currentItem.postValue(_currentItem.value!! - 1)
        }
    }

    fun moveRight() {
        if (_currentItem.value!! + 1 < _list.value!!.size) {
            _currentItem.postValue(_currentItem.value!! + 1)
        }
    }
}