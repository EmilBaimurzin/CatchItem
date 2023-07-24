package com.item.game.ui.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.item.game.core.library.random
import com.item.game.domain.game.Cart
import com.item.game.domain.game.FallingHearts
import com.item.game.domain.game.FallingItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GameViewModel : ViewModel() {
    private var gameScope = CoroutineScope(Dispatchers.Default)
    private val _time = MutableLiveData(0)
    val time: LiveData<Int> = _time

    var moneyCallback: (() -> Unit)? = null
    var vibroCallback: (() -> Unit)? = null
    var gameState = true

    private val _lives = MutableLiveData(5)
    val lives: LiveData<Int> = _lives

    private val _fallingItems = MutableLiveData<List<FallingItem>>(emptyList())
    val fallingItems: LiveData<List<FallingItem>> = _fallingItems

    private val _fallingHearts = MutableLiveData<List<FallingHearts>>(emptyList())
    val fallingHearts: LiveData<List<FallingHearts>> = _fallingHearts

    private val _carts = MutableLiveData<List<Cart>>()
    val carts: LiveData<List<Cart>> = _carts

    init {
        _carts.postValue(
            listOf(
                Cart(1, 1, false),
                Cart(2, 2, false),
                Cart(3, 3, false),
                Cart(4, 4, false)
            )
        )
    }

    fun start() {
        gameScope = CoroutineScope(Dispatchers.Default)
    }

    fun stop() {
        gameScope.cancel()
    }

    fun startTimer() {
        gameScope.launch {
            while (true) {
                _time.postValue(_time.value!! + 1)
                delay(1000)
            }
        }
    }

    fun generateItems(y: Float, minX: Float, direction: Float, maxY: Float) {
        gameScope.launch {
            while (true) {
                delay(2000)
                val randomPosition = 0 random 3
                val item = FallingItem(itemPositionX = randomPosition + 1)
                if (randomPosition == 0) {
                    item.position = minX to y
                } else {
                    item.position = minX + direction * randomPosition to y
                }
                val newList = _fallingItems.value!!.toMutableList()
                newList.add(item)
                _fallingItems.postValue(newList)
            }
        }
        letItemsFall(maxY)
    }

    fun generateHearts(y: Float, minX: Float, direction: Float, maxY: Float) {
        gameScope.launch {
            while (true) {
                delay(40000)
                val randomPosition = 0 random 3
                val item = FallingHearts(itemPositionX = randomPosition + 1)
                if (randomPosition == 0) {
                    item.position = minX to y
                } else {
                    item.position = minX + direction * randomPosition to y
                }
                val newList = _fallingHearts.value!!.toMutableList()
                newList.add(item)
                _fallingHearts.postValue(newList)
            }
        }
        letHeartsFall(maxY)
    }

    private fun letHeartsFall(maxY: Float) {
        gameScope.launch {
            while (true) {
                delay(16)
                val list = _fallingHearts.value!!
                val newList = mutableListOf<FallingHearts>()
                list.forEach { item ->
                    item.position = item.position.first to item.position.second + 3
                    if (item.position.second.toInt() >= maxY) {
                        if (_lives.value != 5) {
                            _lives.postValue(_lives.value!! + 1)
                        }
                    } else {
                        newList.add(item)
                    }
                }
                _fallingHearts.postValue(newList)
            }
        }
    }

    private fun letItemsFall(maxY: Float) {
        gameScope.launch {
            while (true) {
                delay(16)
                val list = _fallingItems.value!!
                val newList = mutableListOf<FallingItem>()
                list.forEach { item ->
                    item.position = item.position.first to item.position.second + 3
                    if (item.position.second.toInt() >= maxY) {
                        if (item.value == _carts.value!!.find { it.position == item.itemPositionX }!!.value) {
                            moneyCallback?.invoke()
                        } else {
                            _lives.postValue(_lives.value!! - 1)
                            vibroCallback?.invoke()
                        }
                    } else {
                        newList.add(item)
                    }
                }
                _fallingItems.postValue(newList)
            }
        }
    }

    fun cartClick(clickedItemIndex: Int) {
        viewModelScope.launch {
            if (_carts.value!!.find { it.isSelected } != null) {
                val selectedItem = _carts.value!!.find { it.isSelected }
                val clickedItem = _carts.value!!.find { it.position == clickedItemIndex }
                val selectedItemIndex = _carts.value!!.indexOf(selectedItem)
                val selectedItemValue = selectedItem!!.value
                val clickedItemValue = clickedItem!!.value
                val newList = _carts.value!!.toMutableList()
                newList[clickedItemIndex - 1].value = selectedItemValue
                newList[selectedItemIndex].value = clickedItemValue
                newList.map { it.isSelected = false }
                _carts.postValue(newList)
            } else {
                val newList = _carts.value!!.toMutableList()
                newList[newList.indexOf(newList.find { it.position == clickedItemIndex })].isSelected =
                    true
                _carts.postValue(newList)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        gameScope.cancel()
    }
}