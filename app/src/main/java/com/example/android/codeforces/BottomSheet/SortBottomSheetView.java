package com.example.android.codeforces.BottomSheet;

import static com.example.android.codeforces.Constants.CHANGE;
import static com.example.android.codeforces.Constants.NEGATIVE_CHANGE;
import static com.example.android.codeforces.Constants.POSITIVE_CHANGE;

import android.annotation.SuppressLint;
import android.app.Dialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import android.view.View;
import android.widget.Button;
import com.example.android.codeforces.Listeners.SortClickListener;
import com.example.android.codeforces.R;

public class SortBottomSheetView extends BottomSheetDialogFragment {

    private Button change;
    private Button negativeButton;
    private Button positiveButton;
    private SortClickListener listener;

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(final Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.bottom_sheet_sort_view, null);
        dialog.setContentView(contentView);

        change = contentView.findViewById(R.id.change);
        negativeButton = contentView.findViewById(R.id.negativeChange);
        positiveButton = contentView.findViewById(R.id.positiveChange);

        int sortState = CHANGE;

        if (getArguments() == null) {
            dismiss();
        } else {
            sortState = getArguments().getInt("sortValue");
        }

        switch (sortState) {
            case CHANGE:
                {
                    change.setPressed(true);
                    break;
                }
            case NEGATIVE_CHANGE:
                {
                    negativeButton.setPressed(true);
                    break;
                }
            case POSITIVE_CHANGE:
                {
                    positiveButton.setPressed(true);
                    break;
                }
        }

        change.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.buttonClicked(CHANGE);
                        dismiss();
                    }
                });

        negativeButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.buttonClicked(NEGATIVE_CHANGE);
                        dismiss();
                    }
                });

        positiveButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.buttonClicked(POSITIVE_CHANGE);
                        dismiss();
                    }
                });
    }

    public void setListener(SortClickListener listener) {
        this.listener = listener;
    }
}
