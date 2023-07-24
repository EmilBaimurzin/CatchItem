package com.item.game.domain.others

import android.content.Context

class AppPrefs(context: Context) {
    val sp = context.getSharedPreferences("SHARED_PREFS", Context.MODE_PRIVATE)

    fun increaseBalance(value: Int) = sp.edit().putInt("BALANCE", getBalance() + value).apply()

    fun getBalance(): Int = sp.getInt("BALANCE", 0)

    fun getVibroState(): Boolean = sp.getBoolean("VIBRO", true)

    fun setVibroState() = sp.edit().putBoolean("VIBRO", !getVibroState()).apply()

    fun isSymbolBought(value: Int): Boolean {
        return when (value) {
            1 -> sp.getBoolean("1", true)
            2 -> sp.getBoolean("2", true)
            3 -> sp.getBoolean("3", true)
            4 -> sp.getBoolean("4", true)
            5 -> sp.getBoolean("5", false)
            6 -> sp.getBoolean("6", false)
            7 -> sp.getBoolean("7", false)
            8 -> sp.getBoolean("8", false)
            9 -> sp.getBoolean("9", false)
            else -> sp.getBoolean("10", false)
        }
    }

    fun getSelectedSymbol(value: Int): Int {
        return when (value) {
            1 -> sp.getInt("SELECTED1", 1)
            2 -> sp.getInt("SELECTED2", 2)
            3 -> sp.getInt("SELECTED3", 3)
            else -> sp.getInt("SELECTED4", 4)
        }
    }

    fun setSelectedSymbol(value: Int, position: Int) {
        when (position) {
            1 -> sp.edit().putInt("SELECTED1", value).apply()
            2 -> sp.edit().putInt("SELECTED2", value).apply()
            3 -> sp.edit().putInt("SELECTED3", value).apply()
            else -> sp.edit().putInt("SELECTED4", value).apply()
        }
    }

    fun buySymbol(value: Int) {
        when (value) {
            1 -> sp.edit().putBoolean("1", true).apply()
            2 -> sp.edit().putBoolean("2", true).apply()
            3 -> sp.edit().putBoolean("3", true).apply()
            4 -> sp.edit().putBoolean("4", true).apply()
            5 -> sp.edit().putBoolean("5", true).apply()
            6 -> sp.edit().putBoolean("6", true).apply()
            7 -> sp.edit().putBoolean("7", true).apply()
            8 -> sp.edit().putBoolean("8", true).apply()
            9 -> sp.edit().putBoolean("9", true).apply()
            else -> sp.edit().putBoolean("10", true).apply()
        }
    }
}