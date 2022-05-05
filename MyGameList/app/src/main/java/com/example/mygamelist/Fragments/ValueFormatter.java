package com.example.mygamelist.Fragments;

import java.util.ArrayList;

public final class ValueFormatter extends com.github.mikephil.charting.formatter.ValueFormatter {
    private final ArrayList<String> mLabels;

    public ValueFormatter(final ArrayList<String> labels) {
        mLabels = labels;
    }

    @Override
    public String getFormattedValue(final float value) {
        return mLabels.get(((int)value)/10);
    }
}