package com.noumi0k.fabulousnumberformatter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

import com.noumi0k.fabulous_number_formatter.FabulousNumberFormatter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by noumi0k on 2017/12/23.
 */

public class SampleApp extends AppCompatActivity implements TextWatcher {

    @BindView(R.id.number_edittext)
    EditText numberEditText;

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        updateAmountJpyEditTextWithCommaSeparators(editable.toString());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        return super.onCreateView(parent, name, context, attrs);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (numberEditText != null) {
            numberEditText.addTextChangedListener(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (numberEditText != null) {
            numberEditText.removeTextChangedListener(this);
        }
    }

    void updateAmountJpyEditTextWithCommaSeparators(String amountString) {
        int beforeCursor = numberEditText.getSelectionStart();
        int beforeSize = numberEditText.getText().length();
        numberEditText.removeTextChangedListener(this);
        numberEditText.setText(FabulousNumberFormatter.keepSignificantForDecimalInput(beforeCursor, amountString));
        numberEditText.addTextChangedListener(this);
        int afterSize = numberEditText.getText().length();
        int sizeDifference = afterSize - beforeSize;
        numberEditText.setSelection(Math.max(0, beforeCursor + sizeDifference));
    }
}
