package com.item.game.ui.game

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.item.game.R
import com.item.game.databinding.FragmentGameBinding
import com.item.game.domain.others.AppPrefs
import com.item.game.ui.other.ViewBindingFragment
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


class FragmentGame : ViewBindingFragment<FragmentGameBinding>(FragmentGameBinding::inflate) {
    private val viewModel: GameViewModel by viewModels()
    private val sharedPrefs by lazy {
        AppPrefs(requireContext())
    }
    private var item1 = R.drawable.img_symbols01
    private var item2 = R.drawable.img_symbols02
    private var item3 = R.drawable.img_symbols03
    private var item4 = R.drawable.img_symbols04
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBalance()

        item1 = getSymbolById(sharedPrefs.getSelectedSymbol(1))
        item2 = getSymbolById(sharedPrefs.getSelectedSymbol(2))
        item3 = getSymbolById(sharedPrefs.getSelectedSymbol(3))
        item4 = getSymbolById(sharedPrefs.getSelectedSymbol(4))

        lifecycleScope.launch {
            delay(20)
            if (viewModel.gameState) {
                viewModel.start()
                viewModel.startTimer()
                viewModel.generateItems(
                    binding.sticksView.y,
                    binding.sticksView.x + dpToPx(17),
                    dpToPx(85).toFloat(),
                    binding.cart1Layout.y - dpToPx(50)
                )
                viewModel.generateHearts(
                    binding.sticksView.y,
                    binding.sticksView.x + dpToPx(27),
                    dpToPx(85).toFloat(),
                    binding.cart1Layout.y - dpToPx(50)
                )
            }
        }

        viewModel.moneyCallback = {
            increaseBalance(10)
            setBalance()
        }

        viewModel.vibroCallback = {
            if (sharedPrefs.getVibroState()) {
                if (Build.VERSION.SDK_INT >= 26) {
                    (requireActivity().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator).vibrate(
                        VibrationEffect.createOneShot(150, VibrationEffect.DEFAULT_AMPLITUDE)
                    )
                } else {
                    (requireActivity().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator).vibrate(150)
                }
            }
        }

        binding.homeButton.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.cart1Layout.setOnClickListener {
            viewModel.cartClick(1)
        }
        binding.cart2Layout.setOnClickListener {
            viewModel.cartClick(2)
        }
        binding.cart3Layout.setOnClickListener {
            viewModel.cartClick(3)
        }
        binding.cart4Layout.setOnClickListener {
            viewModel.cartClick(4)
        }

        viewModel.time.observe(viewLifecycleOwner) { totalSecs ->
            val minutes = (totalSecs % 3600) / 60;
            val seconds = totalSecs % 60;

            binding.timeText.text = String.format("%02d:%02d", minutes, seconds);
        }

        viewModel.lives.observe(viewLifecycleOwner) { lives ->
            binding.livesLayout.removeAllViews()
            repeat(lives) {
                val liveView = ImageView(requireContext())
                liveView.layoutParams = LinearLayout.LayoutParams(dpToPx(30), dpToPx(30)).also {
                    it.marginStart = dpToPx(3)
                    it.marginEnd = dpToPx(3)
                }
                liveView.setImageResource(R.drawable.img_heart)
                binding.livesLayout.addView(liveView)
            }

            if (lives == 0 && viewModel.gameState) {
                endGame()
            }
        }

        viewModel.carts.observe(viewLifecycleOwner) { list ->
            val cart1 =
                when (list.find { it.position == 1 }!!.value) {
                    1 -> item1
                    2 -> item2
                    3 -> item3
                    else -> item4
                }

            val cart2 =
                when (list.find { it.position == 2 }!!.value) {
                    1 -> item1
                    2 -> item2
                    3 -> item3
                    else -> item4
                }

            val cart3 =
                when (list.find { it.position == 3 }!!.value) {
                    1 -> item1
                    2 -> item2
                    3 -> item3
                    else -> item4
                }

            val cart4 =
                when (list.find { it.position == 4 }!!.value) {
                    1 -> item1
                    2 -> item2
                    3 -> item3
                    else -> item4
                }

            binding.cart1Img.setImageResource(cart1)
            binding.cart2Img.setImageResource(cart2)
            binding.cart3Img.setImageResource(cart3)
            binding.cart4Img.setImageResource(cart4)

            if (list.find { it.isSelected } != null) {
                binding.cart1Layout.alpha =
                    if (list.find { it.isSelected }!!.position == 1) 1f else 0.5f
                binding.cart2Layout.alpha =
                    if (list.find { it.isSelected }!!.position == 2) 1f else 0.5f
                binding.cart3Layout.alpha =
                    if (list.find { it.isSelected }!!.position == 3) 1f else 0.5f
                binding.cart4Layout.alpha =
                    if (list.find { it.isSelected }!!.position == 4) 1f else 0.5f
            } else {
                binding.cart1Layout.alpha = 1f
                binding.cart2Layout.alpha = 1f
                binding.cart3Layout.alpha = 1f
                binding.cart4Layout.alpha = 1f
            }
        }

        viewModel.fallingItems.observe(viewLifecycleOwner) { list ->
            binding.gameLayout.removeAllViews()
            list.forEach { item ->
                val itemView = ImageView(requireContext())
                val itemValue = when (item.value) {
                    1 -> item1
                    2 -> item2
                    3 -> item3
                    else -> item4
                }
                itemView.layoutParams = ViewGroup.LayoutParams(dpToPx(50), dpToPx(50))
                itemView.setImageResource(itemValue)
                itemView.x = item.position.first
                itemView.y = item.position.second
                binding.gameLayout.addView(itemView)
            }
        }

        viewModel.fallingHearts.observe(viewLifecycleOwner) { list ->
            binding.heartsLayout.removeAllViews()
            list.forEach { item ->
                val itemView = ImageView(requireContext())
                itemView.layoutParams = ViewGroup.LayoutParams(dpToPx(30), dpToPx(30))
                itemView.setImageResource(R.drawable.img_heart)
                itemView.x = item.position.first
                itemView.y = item.position.second
                binding.heartsLayout.addView(itemView)
            }
        }
    }

    private fun setBalance() {
        binding.balanceTextView.text = sharedPrefs.getBalance().toString()
    }

    private fun increaseBalance(value: Int) = sharedPrefs.increaseBalance(value)

    private fun endGame() {
        viewModel.stop()
        viewModel.gameState = false
        findNavController().navigate(FragmentGameDirections.actionFragmentGameToDialogEnd(viewModel.time.value!!))
    }

    private fun getSymbolById(value: Int): Int {
        return when (value) {
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
    }

    override fun onPause() {
        super.onPause()
        viewModel.stop()
    }

    private fun dpToPx(dp: Int): Int {
        val displayMetrics = requireContext().resources.displayMetrics
        return (dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)).roundToInt()
    }
}