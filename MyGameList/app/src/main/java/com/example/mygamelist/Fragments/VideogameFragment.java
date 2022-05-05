package com.example.mygamelist.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mygamelist.Database.AppDatabase;
import com.example.mygamelist.Database.Entities.Game;
import com.example.mygamelist.Database.Entities.UserGame;
import com.example.mygamelist.Activities.HomeActivity;
import com.example.mygamelist.R;
import com.example.mygamelist.Recyclers.Top.CardAdapter;
import com.example.mygamelist.Recyclers.Top.TopCard;
import com.example.mygamelist.Utilities;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

public final class VideogameFragment extends Fragment {

    private static final String LOG = "VideogameFragment";
    private final List<List<Game>> rvGames = new ArrayList<>();
    private List<String> genders;
    private MaterialCardView addButton;
    private MaterialCardView favButton;
    private ImageView addImage;
    private ImageView favImage;
    private List<UserGame> usergames = new ArrayList<>();
    private List<UserGame> favgames = new ArrayList<>();
    private RatingBar ratingBar;
    private EditText editComment;
    private Button saveComment;
    private Button deleteComment;
    private TextView rt;
    private boolean isTextViewClicked = false;
    private AppDatabase db;
    private FragmentState fg;
    private TextView gameTitle;
    private TextView gameYear;
    private TextView gameSeries;
    private TextView gamePlatform;
    private TextView gamePublisher;
    private TextView gameDeveloper;

    @Nullable
    @Override
    public View onCreateView( @NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.videogame, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Activity activity = getActivity();
        if (activity != null) {
            this.db = AppDatabase.getInstance(activity.getApplicationContext());
            this.fg = FragmentState.getInstance(activity.getApplicationContext());
            if(!fg.isVideogameListEmpty()){
                if(fg.isVideogameBack()){
                    HomeActivity.currentGame = fg.getLastVideogame();
                } else {
                    this.fg.setVideogameGoing(false);
                }
            }
            this.usergames = db.generalDao().getAllUserGamesFromUser(HomeActivity.USER_ID);
            this.favgames = db.generalDao().getFavGamesFromUser(HomeActivity.USER_ID);
            this.addButton = getView().findViewById(R.id.addButton);
            this.favButton = getView().findViewById(R.id.favButton);
            this.addImage = getView().findViewById(R.id.addButtonImage);
            this.favImage = getView().findViewById(R.id.favButtonImage);
            setLists();
            setScrollbars(activity);
            setRating(activity);
            setAddFav(activity);
            setPanoramic(activity);
            setDescription(activity);
            setAverageScore();
            setCommentList(activity);
        } else {
            Log.e(LOG, "Activity is null");
        }
    }

    private void setCommentList(final Activity activity) {
        CardView commentList = getView().findViewById(R.id.commentListButton);
        commentList.setOnClickListener(v -> Utilities.pushFragment((AppCompatActivity)activity, new CommentsFragment(), "CommentsFragment"));
    }

    private void setLists(){
        setGameTextViews();
        Game curGame = setActualGame();
        List<Game> sameSeriesGame = db.generalDao().getSameSeriesGame(gameSeries.getText().toString(), HomeActivity.currentGame);
        checkIfEmpty(sameSeriesGame);
        List<Game> samePublisherGames = db.generalDao().getSamePublisherGame(gamePublisher.getText().toString(), HomeActivity.currentGame);
        checkIfEmpty(samePublisherGames);
        this.genders = new ArrayList<String>(){{
            add(curGame.gender1);
            add(curGame.gender2);
            add(curGame.gender3);
        }};
        List<Game> bestMatchingGames = new ArrayList<>();
        for(final Game g: db.generalDao().getAllOtherGames(String.valueOf(curGame.gameId), curGame.series)){
            int score = getGenderScore(g.gender1) + getGenderScore(g.gender2) + getGenderScore(g.gender3);
            if(score==3){
                bestMatchingGames.add(0, g); //3 matches, at the top
            } else if(score==2){
                bestMatchingGames.add(g);  //only 2 matches, ok but in the bottom
            }
        }
        checkIfEmpty(bestMatchingGames);
        this.rvGames.add(sameSeriesGame);
        this.rvGames.add(samePublisherGames);
        this.rvGames.add(bestMatchingGames);
    }

    private int getGenderScore(final String gender){
        return gender.equals(this.genders.get(0)) || gender.equals(this.genders.get(1)) || gender.equals(this.genders.get(2)) ? 1 : 0;
    }

    private void setGameTextViews(){
        this.gameTitle = getView().findViewById(R.id.gameTitle);
        this.gameYear = getView().findViewById(R.id.releaseYear);
        this.gameSeries = getView().findViewById(R.id.series);
        this.gamePlatform = getView().findViewById(R.id.platform);
        this.gamePublisher = getView().findViewById(R.id.publisher);
        this.gameDeveloper = getView().findViewById(R.id.developer);
    }

    private Game setActualGame(){
        for(final Game g: db.generalDao().getAllGames()) {
            if(g.gameId == HomeActivity.currentGame){
                gameTitle.setText(g.gameName);
                gameSeries.setText(g.series);
                gameDeveloper.setText(g.developer);
                gamePublisher.setText(g.publisher);
                String day = g.publishDay;
                if(day.length()==1){
                    day = "0" + day;
                }
                String date = day + "/" + g.publishMonth + "/" +g.publishYear;
                gameYear.setText(date);
                gamePlatform.setText(g.platform);
                return g;
            }
        }
        return null;
    }

    private void checkIfEmpty(final List<Game> list){
        if(list.isEmpty()){
            Game emptyGame = new Game(getString(R.string.no_games), "", "", "-1", "", "", "", "",
                    "ic_videogame_24", "ic_launcher_background", "", "", "", "", false);
            list.add(emptyGame);
        }
    }


    private void setAverageScore() {
        TextView avgScore = getView().findViewById(R.id.averageScoreValue);
        float val = db.generalDao().getAverageScore(HomeActivity.currentGame);
        avgScore.setText(String.format("%.2f", val));
    }

    private void setDescription(final Activity activity) {
        TextView desc = getView().findViewById(R.id.gameDescription);
        desc.setText(db.generalDao().getGame(HomeActivity.currentGame).description);
        ImageButton ib = getView().findViewById(R.id.expandDescription);
        ib.setOnClickListener(v -> {
            desc.setMaxLines(isTextViewClicked ? 4 : 50);
            isTextViewClicked = !isTextViewClicked;
            ib.setBackgroundResource(isTextViewClicked ? R.drawable.ic_more_24 : R.drawable.ic_less_24);
        });
    }

    private void setPanoramic(final Activity activity) {
        ImageView panoramic = getView().findViewById(R.id.gameImage);
        Game game = db.generalDao().getGame(HomeActivity.currentGame);
        Drawable drawable = ContextCompat.getDrawable(activity, activity.getResources().getIdentifier(game.gamePanoramic,"drawable",activity.getPackageName()));
        panoramic.setImageDrawable(drawable);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setRating(final Activity activity) {
        this.ratingBar = getView().findViewById(R.id.ratingBar);
        this.editComment = getView().findViewById(R.id.editComment);
        this.saveComment = getView().findViewById(R.id.saveCommentButton);
        this.deleteComment = getView().findViewById(R.id.deleteCommentButton);
        List<Long> ids = new ArrayList<>();
        for(UserGame ug: usergames){
            ids.add(ug.gameId);
        }
        this.rt = getView().findViewById(R.id.ratingText);
        if(ids.contains(HomeActivity.currentGame)){
            int r = db.generalDao().getRating(HomeActivity.USER_ID, HomeActivity.currentGame);
            this.ratingBar.setRating(r);
            this.editComment.setText(db.generalDao().getComment(HomeActivity.USER_ID, HomeActivity.currentGame));
            this.editComment.setOnTouchListener((v, event) -> {
                if (editComment.hasFocus()) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    if((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_SCROLL) {
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        return true;
                    }
                }
                return false;
            });
        } else {
            rt.setVisibility(View.GONE);
            this.ratingBar.setVisibility(View.GONE);
            this.editComment.setVisibility(View.GONE);
            saveComment.setVisibility(View.GONE);
            deleteComment.setVisibility(View.GONE);
        }
        this.ratingBar.setOnTouchListener((v, event) -> {
            switch (event.getAction()){
                case MotionEvent.ACTION_UP:
                    List<Long> gameAddIds = new ArrayList<>();
                    for(final UserGame ug: usergames){
                        gameAddIds.add(ug.gameId);
                    }
                    if(gameAddIds.contains(HomeActivity.currentGame)){
                        float touchPositionX = event.getX();
                        float width = ratingBar.getWidth();
                        float starsf = (touchPositionX / width) * 5.0f;
                        int stars = (int)starsf + 1;
                        ratingBar.setRating(stars);
                        db.generalDao().setRating(stars, HomeActivity.USER_ID, HomeActivity.currentGame);
                    }
                    v.setPressed(false);
                    break;
                case MotionEvent.ACTION_DOWN:
                    v.setPressed(true);
                    break;
                case MotionEvent.ACTION_CANCEL:
                    v.setPressed(false);
                    break;
            }
            return true;
        });
        saveComment.setOnClickListener(v -> modifyComment(activity, "save"));
        deleteComment.setOnClickListener(v -> {
            modifyComment(activity, "delete");
            this.editComment.setText("");
        });
    }

    private void modifyComment(final Activity activity, String type){
        String comment = this.editComment.getText().toString();
        if(!comment.isEmpty()) {
            db.generalDao().setComment(type.equals("save") ? comment : "", HomeActivity.USER_ID, HomeActivity.currentGame);
        }
        new MaterialAlertDialogBuilder(new ContextThemeWrapper(activity, R.style.myDialog))
                .setTitle(getString(comment.isEmpty()? R.string.empty_comment : (type.equals("save") ? R.string.comment_saved : R.string.comment_deleted)))
                .setPositiveButton("Ok", null)
                .show();
    }

    private void setAddFav(final Activity activity) {
        addGameToList(activity);
        setFavGame(activity);
    }

    private void addGameToList(final Activity activity) {
        List<Long> gameAddIds = new ArrayList<>();
        for(final UserGame ug: this.usergames){
            gameAddIds.add(ug.gameId);
        }
        boolean condAdd = gameAddIds.contains(HomeActivity.currentGame);
        this.favButton.setEnabled(condAdd);
        this.favButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.isFav)));
        this.addImage.setImageResource(condAdd ? R.drawable.ic_remove_24 : R.drawable.ic_add_24);
        CharSequence[] array = new CharSequence[] {getString(R.string.programmed), getString(R.string.played), getString(R.string.playing)};
        this.addButton.setOnClickListener(v -> {
            this.fg.setVideogameGoing(true);
            if(gameAddIds.contains(HomeActivity.currentGame)){
                db.generalDao().deleteUserGame(new UserGame(Long.parseLong(HomeActivity.USER_ID),
                        HomeActivity.currentGame, 0, "", 1, false, ""));
                this.ratingBar.setVisibility(View.GONE);
                this.editComment.setVisibility(View.GONE);
                this.deleteComment.setVisibility(View.GONE);
                this.rt.setVisibility(View.GONE);
                this.saveComment.setVisibility(View.GONE);
                this.ratingBar.setRating(0);
                this.editComment.setText("");
                this.favButton.setEnabled(false);
                new MaterialAlertDialogBuilder(new ContextThemeWrapper(activity, R.style.myDialog))
                        .setMessage(getString(R.string.removed_from_list))
                        .setPositiveButton("Ok", (dialog, which) -> dialog.dismiss())
                        .show();
                Utilities.pushFragment((AppCompatActivity)activity, new RefreshFragment(), "RefreshFragment");
            } else {
                new MaterialAlertDialogBuilder(new ContextThemeWrapper(activity, R.style.myDialog))
                        .setTitle(getString(R.string.add_videogame))
                        .setSingleChoiceItems(array, 2, (dialog, which) -> {})
                        .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                            ListView lw = ((AlertDialog)dialog).getListView();
                            String checkedItem = lw.getAdapter().getItem(lw.getCheckedItemPosition()).toString();
                            int pos = checkedItem.equals(getString(R.string.programmed)) ? 0 : checkedItem.equals(getString(R.string.playing)) ? 1 : 2;
                            db.generalDao().insertUserGame(new UserGame(Long.parseLong(HomeActivity.USER_ID), HomeActivity.currentGame, 0,
                                    "", pos, false, HomeActivity.getTimestamp()));
                            favButton.setEnabled(false);
                            new MaterialAlertDialogBuilder(new ContextThemeWrapper(activity, R.style.myDialog))
                                    .setMessage(getString(R.string.added_to_list))
                                    .setPositiveButton("Ok", (dialog1, which1) -> dialog1.dismiss())
                                    .show();
                            Utilities.pushFragment((AppCompatActivity)activity, new RefreshFragment(), "RefreshFragment");
                            dialog.dismiss();
                        })
                        .setNegativeButton(android.R.string.no, (dialog, which) -> dialog.dismiss())
                        .show();
            }
        });
    }

    private void setFavGame(final Activity activity){
        List<Long> gameFavIds = new ArrayList<>();
        for(final UserGame ug: this.favgames){
            gameFavIds.add(ug.gameId);
        }
        boolean condFav = gameFavIds.contains(HomeActivity.currentGame);
        this.favImage.setImageResource(condFav ? R.drawable.ic_favourite_24 : R.drawable.ic_not_favourite_24);
        this.favButton.setOnClickListener(v -> {
            this.fg.setVideogameGoing(true);
            db.generalDao().setFavourite(condFav ? 0 : 1, HomeActivity.USER_ID, HomeActivity.currentGame);
            new MaterialAlertDialogBuilder(new ContextThemeWrapper(activity, R.style.myDialog))
                    .setMessage(getString(condFav ? R.string.removed_from_favourite : R.string.added_to_favourite))
                    .setPositiveButton("Ok", (dialog, which) -> dialog.dismiss())
                    .show();
            Utilities.pushFragment((AppCompatActivity)activity, new RefreshFragment(), "RefreshFragment");
        });
    }

    private void setScrollbars(final Activity activity) {
        List<RecyclerView> layouts = new ArrayList<>();
        layouts.add(getView().findViewById(R.id.sameSeriesGamesView));
        layouts.add(getView().findViewById(R.id.samePublisherGamesView));
        layouts.add(getView().findViewById(R.id.similarGamesView));
        int i = 0;
        for(final RecyclerView r: layouts) {
            List<TopCard> list = new ArrayList<>();
            for(final Game v : this.rvGames.get(i)) {
                list.add(new TopCard(v.gameImage, v.gameName, v.platform));
            }
            int j = i;
            CardAdapter cardAdapter;
            if(rvGames.get(i).size()==1 && rvGames.get(i).get(0).publishYear.equals("-1")){
                r.setAdapter(new CardAdapter(list, activity, position ->{}));
            } else {
                cardAdapter = new CardAdapter(list, activity, position -> {
                    AppCompatActivity appCompatActivity = (AppCompatActivity) activity;
                    if(appCompatActivity != null) {
                        this.fg.addVideogame(HomeActivity.currentGame);
                        this.fg.setVideogameGoing(true);
                        HomeActivity.currentGame = this.rvGames.get(j).get(position).gameId;
                        Utilities.pushFragment(appCompatActivity, new VideogameFragment(), "VideogameFragment");
                    }
                });
                r.setAdapter(cardAdapter);
            }
            i++;
        }

    }

}
