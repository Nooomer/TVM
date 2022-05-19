package com.tvmedicine


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class WindowFragment : BottomSheetDialogFragment(), OnBottomSheetCallbacks {


    private var currentState: Int = BottomSheetBehavior.STATE_EXPANDED
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        (activity as ChatActivity).setOnBottomSheetCallbacks(this)
        return inflater.inflate(R.layout.fragment_window, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
val filterImage = requireView().findViewById<ImageButton>(R.id.filterImage)
        val textResult = requireView().findViewById<AppCompatTextView>(R.id.textResult)
        textResult.setOnClickListener {
            (activity as ChatActivity).openBottomSheet()
        }
        filterImage.setOnClickListener {
            if (currentState == BottomSheetBehavior.STATE_EXPANDED) {
                (activity as ChatActivity).closeBottomSheet()
            } else  {
                (activity as ChatActivity).openBottomSheet()
            }
        }
    }
    override fun onStateChanged(bottomSheet: View, newState: Int) {
        val textResult = requireView().findViewById<AppCompatTextView>(R.id.textResult)
        val filterImage = requireView().findViewById<ImageButton>(R.id.filterImage)
        currentState = newState
        when (newState) {
            BottomSheetBehavior.STATE_EXPANDED -> {
                textResult.text = "0 results"
                filterImage.setImageResource(R.drawable.ic_baseline_filter_list_24)
            }
            BottomSheetBehavior.STATE_COLLAPSED -> {
                textResult.text = "See the results"
                filterImage.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24)
            }
        }
    }
}