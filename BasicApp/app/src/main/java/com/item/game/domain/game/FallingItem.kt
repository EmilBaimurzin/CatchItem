package com.item.game.domain.game

import com.item.game.core.library.random

data class FallingItem(
    val value: Int = 1 random 4,
    var position: Pair<Float, Float> = 0f to 0f,
    val itemPositionX: Int
)