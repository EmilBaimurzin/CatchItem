package com.item.game.ui.shop

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.item.game.core.library.shortToast
import com.item.game.databinding.FragmentShopBinding
import com.item.game.domain.others.AppPrefs
import com.item.game.ui.other.ViewBindingFragment

class FragmentShop : ViewBindingFragment<FragmentShopBinding>(FragmentShopBinding::inflate) {
    private val sp by lazy {
        AppPrefs(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setButtons()
        setBalance()
        binding.homeButton.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.apply {
            symbol5Button.setOnClickListener {
                buySymbol(5)
            }
            symbol6Button.setOnClickListener {
                buySymbol(6)
            }
            symbol7Button.setOnClickListener {
                buySymbol(7)
            }
            symbol8Button.setOnClickListener {
                buySymbol(8)
            }
            symbol9Button.setOnClickListener {
                buySymbol(9)
            }
            symbol10Button.setOnClickListener {
                buySymbol(10)
            }
        }
    }

    private fun setBalance() {
        binding.balanceTextView.text = sp.getBalance().toString()
    }

    private fun buySymbol(symbol: Int) {
        if (symbol in (5..7)) {
            if (sp.getBalance() >= 1000) {
                sp.increaseBalance(-1000)
                sp.buySymbol(symbol)
            } else {
                shortToast(requireContext(), "Not enough money")
            }
        } else {
            if (sp.getBalance() >= 10000) {
                sp.increaseBalance(-10000)
                sp.buySymbol(symbol)
            } else {
                shortToast(requireContext(), "Not enough money")
            }
        }
        setButtons()
        setBalance()
    }

    private fun setButtons() {
        binding.apply {
            symbol5Button.isVisible = !sp.isSymbolBought(5)
            symbol6Button.isVisible = !sp.isSymbolBought(6)
            symbol7Button.isVisible = !sp.isSymbolBought(7)
            symbol8Button.isVisible = !sp.isSymbolBought(8)
            symbol9Button.isVisible = !sp.isSymbolBought(9)
            symbol10Button.isVisible = !sp.isSymbolBought(10)
        }
    }
}