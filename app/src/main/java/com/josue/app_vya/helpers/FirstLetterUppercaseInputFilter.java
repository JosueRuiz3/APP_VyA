package com.josue.app_vya.helpers;

import android.text.InputFilter;
import android.text.Spanned;

public class FirstLetterUppercaseInputFilter implements InputFilter {

    private boolean isFirstLetter = true;

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        // Si es el primer carácter, convertir a mayúscula
        if (isFirstLetter) {
            isFirstLetter = false;
            return source.toString().toUpperCase();
        }

        // Si es el segundo carácter, convertir a minúscula
        isFirstLetter = false;
        return source.toString().toLowerCase();
    }
}
