package com.intentsoft.hashfactory

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.intentsoft.hashfactory.databinding.FragmentSuccessBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SuccessFragment : Fragment() {

    private val args: SuccessFragmentArgs by navArgs()

    private var binding: FragmentSuccessBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSuccessBinding.inflate(inflater, container, false)

        binding!!.hashTextView.text = args.hash

        binding!!.copyButton.setOnClickListener {
            onCopyClicked()
        }

        return binding!!.root
    }

    private fun onCopyClicked() {
        lifecycleScope.launch {
            copyToClipboard(args.hash)
            applyAnimation()
        }
    }

    private fun copyToClipboard(hash: String) {
        val clipboardManager =
            requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("Encryted text", hash)
        clipboardManager.setPrimaryClip(clipData)
    }

    private suspend fun applyAnimation() {
        binding!!.include.messageBackground.animate().translationY(80f).duration = 200L
        binding!!.include.messageTextView.animate().translationY(80f).duration = 200L

        delay(2000L)

        binding!!.include.messageBackground.animate().translationY(-80f).duration = 500L
        binding!!.include.messageTextView.animate().translationY(-80f).duration = 500L
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}