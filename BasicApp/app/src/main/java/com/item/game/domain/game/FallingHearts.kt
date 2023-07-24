package com.item.game.domain.game

data class FallingHearts(
    var position: Pair<Float, Float> = 0f to 0f,
    val itemPositionX: Int
)