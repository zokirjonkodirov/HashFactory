package com.intentsoft.hashfactory

import android.content.Context
import android.content.Context.VIBRATOR_SERVICE
import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.intentsoft.hashfactory.databinding.FragmentHomeBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import android.os.VibrationEffect

import android.os.Build

import androidx.core.content.ContextCompat.getSystemService

import android.os.Vibrator





class HomeFragment : Fragment() {

    private val homeViewModel: HomeViewModel by viewModels()

    //    private var _binding: FragmentHomeBinding? = null
    protected var binding: FragmentHomeBinding? = null

    override fun onResume() {
        super.onResume()
        val hashAlgorithms = resources.getStringArray(R.array.hash_algorithms)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.drop_down_item, hashAlgorithms)
        binding!!.autoCompleteTextView.setAdapter(arrayAdapter)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        setHasOptionsMenu(true)

        binding!!.generateButton.setOnClickListener {
            onGenerateClicked()
        }

        return binding!!.root
    }

    private fun onGenerateClicked() {
        if (binding!!.plainText.text.isEmpty()) {
            showSnackBar("Field is empty")
            shakeItBaby(requireContext())

        } else {
            lifecycleScope.launch {
                applyAnimations()
                navigateToSuccess(getHashData())
            }
        }

    }

    private suspend fun applyAnimations() {
        binding!!.generateButton.isClickable = false
        binding!!.titleTextView.animate().alpha(0f).duration = 400L
        binding!!.generateButton.animate().alpha(0f).duration = 400L
        binding!!.textInputLayout.animate()
            .alpha(0f)
            .translationXBy(1200f)
            .duration = 400L
        binding!!.plainText.animate()
            .alpha(0f)
            .translationXBy(-1200f)
            .duration = 400L

        delay(300)

        binding!!.succesBackground.animate().alpha(1f).duration = 600L
        binding!!.succesBackground.animate().rotationBy(720f).duration = 600L
        binding!!.succesBackground.animate().scaleXBy(900f).duration = 800L
        binding!!.succesBackground.animate().scaleYBy(900f).duration = 800L

        delay(300)

        binding!!.successImageView.animate().alpha(1f).duration = 1000L
        binding!!.successImageView.playAnimation()

        delay(1500L)
    }

    private fun navigateToSuccess(hash: String) {
        val directions = HomeFragmentDirections.actionHomeFragmentToSuccessFragment(hash)
        findNavController().navigate(directions)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.clear_menu) {
            binding!!.plainText.text.clear()
            showSnackBar("Cleared")
            return true
        }

        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
    private fun showSnackBar(message: String) {
        val snackBar = Snackbar.make(
            binding!!.rootAyout,
            message,
            Snackbar.LENGTH_SHORT
        )
        snackBar.setAction("Okay"){}
        snackBar.setActionTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        snackBar.show()
    }

    private fun getHashData(): String {
        val alogrithm = binding!!.autoCompleteTextView.text.toString()
        val plainText = binding!!.plainText.text.toString()

        return homeViewModel.getHash(plainText, alogrithm)
    }

    // Vibrate for 150 milliseconds
    private fun shakeItBaby(context: Context) {
        if (Build.VERSION.SDK_INT >= 26) {
            (context.getSystemService(VIBRATOR_SERVICE) as Vibrator).vibrate(VibrationEffect.createOneShot(150, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            (context.getSystemService(VIBRATOR_SERVICE) as Vibrator).vibrate(150)
        }
    }
}