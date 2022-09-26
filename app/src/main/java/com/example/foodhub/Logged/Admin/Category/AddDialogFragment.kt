package com.example.foodhub.Logged.Admin.Category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.DialogFragment
import com.example.foodhub.R
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText


class AddDialogFragment : DialogFragment(){
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var rootView: View = inflater.inflate(R.layout.add_category_dialog_layout, container, false)
        var cancelButton = rootView.findViewById<Button>(R.id.cancelButton)
        var addButton = rootView.findViewById<Button>(R.id.addButton)
        var categoryAddText = rootView.findViewById<TextInputEditText>(R.id.categoryAddText)

        var rootLayout = rootView.findViewById<ConstraintLayout>(R.id.viewCategoryLayout)

        cancelButton.setOnClickListener(){
            dismiss()
        }

        addButton.setOnClickListener(){
            var category = categoryAddText.text.toString()
            Snackbar.make(requireActivity().findViewById(R.id.viewCategoryLayout),"Category Added [${category}]", Snackbar.LENGTH_LONG).show()
            dismiss()
        }

        return rootView
    }
}