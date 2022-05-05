package com.example.mygamelist.Fragments;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.fragment.app.Fragment;

import com.example.mygamelist.Database.AppDatabase;
import com.example.mygamelist.Database.Entities.Game;
import com.example.mygamelist.Database.Entities.UserGame;
import com.example.mygamelist.Activities.HomeActivity;
import com.example.mygamelist.R;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class StatsFragment extends Fragment {

    private static final String LOG = "StatsFragment";
    private AppDatabase db;
    private List<UserGame> usergames;
    private List<Long> gameIds;

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.stats, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Activity activity = getActivity();
        if (activity != null) {
            this.db = AppDatabase.getInstance(activity.getApplicationContext());
            this.usergames = db.generalDao().getAllUserGamesFromUser(HomeActivity.currentUser);
            setFlatData(activity);
            getDataFromDatabase();
            setInfos(activity);
        } else {
            Log.e(LOG, "Activity is null");
        }
    }

    private void showInfos(final String text, final Activity activity){
        new MaterialAlertDialogBuilder(new ContextThemeWrapper(activity, R.style.myDialog))
                .setTitle(getString(R.string.chart_infos))
                .setMessage(text)
                .setPositiveButton("Ok", null)
                .show();
    }

    private void setInfos(final Activity activity) {
        MaterialButton genresInfos = activity.findViewById(R.id.genresInfos);
        MaterialButton seriesInfos = activity.findViewById(R.id.seriesInfos);
        MaterialButton consoleInfos = activity.findViewById(R.id.consoleInfos);
        genresInfos.setOnClickListener(v -> showInfos(getString(R.string.genres_chart), activity));
        seriesInfos.setOnClickListener(v -> showInfos(getString(R.string.series_chart), activity));
        consoleInfos.setOnClickListener(v -> showInfos(getString(R.string.console_chart), activity));
    }

    @SuppressWarnings("DefaultLocale")
    private void setFlatData(final Activity activity) {
        TextView added = activity.findViewById(R.id.addedVideogamesValue);
        TextView fav = activity.findViewById(R.id.favouriteVideogamesValue);
        TextView ms = activity.findViewById(R.id.mediumScoreValue);
        TextView com = activity.findViewById(R.id.commentsValue);
        TextView comLike = activity.findViewById(R.id.commentLikesValue);
        TextView posts = activity.findViewById(R.id.forumPostsValue);
        added.setText(String.valueOf(db.generalDao().statsAdded(Long.parseLong(HomeActivity.currentUser))));
        fav.setText(String.valueOf(db.generalDao().statsFav(Long.parseLong(HomeActivity.currentUser))));
        ms.setText(String.format("%.2f", db.generalDao().statsMediumScore(Long.parseLong(HomeActivity.currentUser))));
        com.setText(String.valueOf(db.generalDao().statsCom(Long.parseLong(HomeActivity.currentUser))));
        comLike.setText(String.valueOf(db.generalDao().statsComLike(Long.parseLong(HomeActivity.currentUser))));
        posts.setText(String.valueOf(db.generalDao().statsPosts(Long.parseLong(HomeActivity.currentUser))));
    }

    private void getDataFromDatabase() {
        gameIds = new ArrayList<>();
        for(final UserGame u: usergames){
            gameIds.add(u.gameId);
        }
        setGendersPie();
        setSeriesPie();
        setConsolePie();
    }

    private void setupPieChart(final PieChart chart){
        chart.setDrawHoleEnabled(false);
        chart.setUsePercentValues(true);
        chart.setEntryLabelTextSize(12);
        chart.setEntryLabelColor(Color.BLACK);
        chart.getDescription().setEnabled(false);
        chart.getLegend().setEnabled(false);
    }

    private void loadPieChartData(final PieChart chart, final Map<String, Float> map) {
        List<PieEntry> entries = new ArrayList<>();
        for(final String s: map.keySet()){
            entries.add(new PieEntry(map.get(s), s));
        }
        List<Integer> colors = new ArrayList<>();
        for(final int color: ColorTemplate.MATERIAL_COLORS){
            colors.add(color);
        }
        for(final int color: ColorTemplate.VORDIPLOM_COLORS){
            colors.add(color);
        }
        PieDataSet dataset = new PieDataSet(entries, "Label");
        dataset.setColors(colors);
        PieData data = new PieData(dataset);
        data.setDrawValues(true);
        data.setValueFormatter(new PercentFormatter(chart));
        data.setValueTextSize(12f);
        data.setValueTextColor(Color.BLACK);
        chart.setData(data);
        chart.invalidate();

    }

    private void setSeriesPie() {
        HorizontalBarChart horChartSeries = getView().findViewById(R.id.chartSeriesHor);
        TextView noHorBar = getView().findViewById(R.id.noHorBar);
        Map<String, Integer> valuesSeries = new HashMap<>();
        Map<String, Float> seriesPercentTmp = new HashMap<>();
        Map<String, Float> seriesPercent = new HashMap<>();
        for(final long id: gameIds){
            Game g = db.generalDao().getGame(id);
            valuesSeries.put(g.series, valuesSeries.containsKey(g.series) ? valuesSeries.get(g.series)+1 : 1);
        }
        if(valuesSeries.size()<10){
            horChartSeries.setVisibility(View.GONE);
            noHorBar.setVisibility(View.VISIBLE);
        } else {
            float totalSeries = 0;
            for(final int v: valuesSeries.values()){
                totalSeries += v;
            }
            for(final String s: valuesSeries.keySet()){
                seriesPercentTmp.put(s, (valuesSeries.get(s)*100)/totalSeries);
            }
            float otherValue = 0;
            for(final String s: seriesPercentTmp.keySet()){
                float val = seriesPercentTmp.get(s);
                if(val>=6){
                    seriesPercent.put(s, val);
                } else {
                    otherValue += val;
                }
            }
            if(otherValue>=1){
                seriesPercent.put("Other", otherValue);
            }
            setupHorizontalChart(horChartSeries);
            loadHorizontalChartData(horChartSeries, valuesSeries);
        }
    }

    private void loadHorizontalChartData(final HorizontalBarChart chart, final Map<String, Integer> map) {
        List<BarEntry> entries = new ArrayList<>();
        ArrayList<String> names = new ArrayList<>();
        ArrayList<String> strings = new ArrayList<>();
        ArrayList<Integer> integers = new ArrayList<>();
        for(final String s: map.keySet()){
            strings.add(s);
            integers.add(map.get(s));
        }
        float barWidth = 9f;
        float barSpace = 10f;
        for(int i=0;i<5;i++){
            int max = Collections.max(integers);
            int pos = integers.indexOf(max);
            entries.add(new BarEntry(i*barSpace, integers.get(pos)));
            names.add(strings.get(pos));
            Log.e("STATS", strings.get(pos));
            integers.set(pos, -1);
        }
        BarDataSet set1 = new BarDataSet(entries, "Series");
        set1.setColors(ColorTemplate.MATERIAL_COLORS);
        BarData data = new BarData(set1);
        data.setBarWidth(barWidth);
        chart.setData(data);
        XAxis xaxis = chart.getXAxis();
        xaxis.setValueFormatter(new ValueFormatter(names));
        xaxis.setPosition(XAxis.XAxisPosition.TOP);
        xaxis.setDrawGridLines(false);
        xaxis.setDrawAxisLine(false);
        xaxis.setGranularity(1f);
        xaxis.setGranularityEnabled(true);
        xaxis.setTextSize(10);
        xaxis.setTextColor(Color.WHITE);
        xaxis.setLabelCount(set1.getEntryCount());
        chart.invalidate();
    }

    private void setupHorizontalChart(final HorizontalBarChart chart) {
        chart.getDescription().setEnabled(false);
        chart.getLegend().setEnabled(false);
    }

    private void setGendersPie() {
        PieChart chartGenders = getView().findViewById(R.id.chartGenders);
        TextView noGenres = getView().findViewById(R.id.noGenders);
        Map<String, Integer> valuesGenders = new HashMap<>();
        Map<String, Float> gendersPercentTmp = new HashMap<>();
        Map<String, Float> gendersPercent = new HashMap<>();
        for(final long id: gameIds){
            Game g = db.generalDao().getGame(id);
            valuesGenders.put(g.gender1, valuesGenders.containsKey(g.gender1) ? valuesGenders.get(g.gender1)+1 : 1);
            valuesGenders.put(g.gender2, valuesGenders.containsKey(g.gender2) ? valuesGenders.get(g.gender2)+1 : 1);
            valuesGenders.put(g.gender3, valuesGenders.containsKey(g.gender3) ? valuesGenders.get(g.gender3)+1 : 1);
        }
        if(valuesGenders.size()<5){
            chartGenders.setVisibility(View.GONE);
            noGenres.setVisibility(View.VISIBLE);
        } else {
            float totalGenders = 0;
            for(final int v: valuesGenders.values()){
                totalGenders += v;
            }
            for(final String s: valuesGenders.keySet()){
                gendersPercentTmp.put(s, (valuesGenders.get(s)*100)/totalGenders);
            }
            float otherValue = 0;
            for(final String s: gendersPercentTmp.keySet()){
                float val = gendersPercentTmp.get(s);
                if(val>=4){
                    gendersPercent.put(s, val);
                } else {
                    otherValue += val;
                }
            }
            if(otherValue>=1) {
                gendersPercent.put("Other", otherValue);
            }
            setupPieChart(chartGenders);
            loadPieChartData(chartGenders, gendersPercent);
        }
    }

    private void setConsolePie() {
        PieChart chartConsole = getView().findViewById(R.id.chartConsole);
        TextView noConsole = getView().findViewById(R.id.noConsole);
        Map<String, Integer> valuesConsole = new HashMap<>();
        Map<String, Float> consolePercentTmp = new HashMap<>();
        Map<String, Float> consolePercent = new HashMap<>();
        for(final long id: gameIds){
            Game g = db.generalDao().getGame(id);
            String console = g.platform;
            valuesConsole.put(console, valuesConsole.containsKey(console) ? valuesConsole.get(console) + 1 : 1);
        }
        if(valuesConsole.size()<3){
            chartConsole.setVisibility(View.GONE);
            noConsole.setVisibility(View.VISIBLE);
        } else {
            float totalGenders = 0;
            for(final int v: valuesConsole.values()){
                totalGenders += v;
            }
            for(final String s: valuesConsole.keySet()){
                consolePercentTmp.put(s, (valuesConsole.get(s)*100)/totalGenders);
            }
            float otherValue = 0;
            for(final String s: consolePercentTmp.keySet()){
                float val = consolePercentTmp.get(s);
                if(val>=4){
                    consolePercent.put(s, val);
                } else {
                    otherValue += val;
                }
            }
            if(otherValue>=1){
                consolePercent.put("Other", otherValue);
            }
            setupPieChart(chartConsole);
            loadPieChartData(chartConsole, consolePercent);
        }
    }

}
