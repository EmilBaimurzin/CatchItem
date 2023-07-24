package com.item.game.ui.settings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.item.game.R
import com.item.game.core.library.shortToast
import com.item.game.databinding.FragmentSettingsBinding
import com.item.game.domain.others.AppPrefs
import com.item.game.ui.other.ViewBindingFragment

class FragmentSettings :
    ViewBindingFragment<FragmentSettingsBinding>(FragmentSettingsBinding::inflate) {
    private val viewModel: SettingsViewModel by viewModels()
    private val sp by lazy {
        AppPrefs(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setVibroButton()

        binding.vibroButton.setOnClickListener {
            sp.setVibroState()
            setVibroButton()
        }

        binding.homeButton.setOnClickListener {
            findNavController().popBackStack()
        }

        if (viewModel.list.value!!.isEmpty()) {
            viewModel.initList(getAvailableSkins())
        }

        binding.buttonLeft.setOnClickListener {
            viewModel.moveLeft()
        }

        binding.buttonRight.setOnClickListener {
            viewModel.moveRight()
        }

        viewModel.currentItem.observe(viewLifecycleOwner) {
            setItem(it + 1)
        }

        binding.symbol1Button.setOnClickListener {
            viewModel.setCurrentItem(sp.getSelectedSymbol(1) - 1)
            viewModel.changeSymbolState(1)
        }

        binding.symbol2Button.setOnClickListener {
            viewModel.setCurrentItem(sp.getSelectedSymbol(2) - 1)
            viewModel.changeSymbolState(2)
        }

        binding.symbol3Button.setOnClickListener {
            viewModel.setCurrentItem(sp.getSelectedSymbol(3) - 1)
            viewModel.changeSymbolState(3)
        }

        binding.symbol4Button.setOnClickListener {
            viewModel.setCurrentItem(sp.getSelectedSymbol(4) - 1)
            viewModel.changeSymbolState(4)
        }

        binding.selectButton.setOnClickListener {
            val currentItem = viewModel.currentItem.value!! + 1
            val currentSymbol = viewModel.symbolState.value!!
            val listOfSymbols = mutableListOf(1, 2, 3, 4)
            val booleanList = mutableListOf<Boolean>()
            listOfSymbols.forEach {
                if (sp.getSelectedSymbol(it) == currentItem) {
                    booleanList.add(false)
                } else {
                    booleanList.add(true)
                }
            }
            if (booleanList.contains(false)) {
                shortToast(requireContext(), "Selected Symbols should be diffrent")
            } else {
                sp.setSelectedSymbol(currentItem, currentSymbol)
            }
        }

        viewModel.symbolState.observe(viewLifecycleOwner) {
            binding.symbol1Button.alpha = if (it == 1) 1f else 0.6f
            binding.symbol2Button.alpha = if (it == 2) 1f else 0.6f
            binding.symbol3Button.alpha = if (it == 3) 1f else 0.6f
            binding.symbol4Button.alpha = if (it == 4) 1f else 0.6f
        }
    }

    private fun setVibroButton() {
        binding.vibroButton.setImageResource(if (sp.getVibroState()) R.drawable.button_yes else R.drawable.button_no)
    }

    private fun setItem(value: Int) {
        val image = when (value) {
            1 -> R.drawable.img_symbols01
            2 -> R.drawable.img_symbols02
            3 -> R.drawable.img_symbols03
            4 -> R.drawable.img_symbols04
            5 -> R.drawable.img_symbols05
            6 -> R.drawable.img_symbols06
            7 -> R.drawable.img_symbols07
            8 -> R.drawable.img_symbols08
            9 -> R.drawable.img_symbols09
            else -> R.drawable.img_symbols10
        }
        binding.selectedImage.setImageResource(image)
    }

    private fun getAvailableSkins(): MutableList<Int> {
        val listToReturn = mutableListOf<Int>()
        repeat(10) {
            if (sp.isSymbolBought(it + 1)) {
                listToReturn.add(it + 1)
            }
        }
        return listToReturn
    }
}