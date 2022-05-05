package com.example.mygamelist.Fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.mygamelist.Activities.LoginActivity;
import com.example.mygamelist.Database.AppDatabase;
import com.example.mygamelist.Database.Entities.User;
import com.example.mygamelist.Activities.HomeActivity;
import com.example.mygamelist.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

public final class SettingsFragment extends Fragment {

    private static final String LOG = "SettingsFragment";
    private AppDatabase db;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Activity activity = getActivity();
        if (activity != null) {
            this.db = AppDatabase.getInstance(activity.getApplicationContext());
            exitSet(activity);
            removeAccountSet(activity);
            uploadPhotoSet(activity);
            modifyInfosSet(activity);
            modifyPswSet(activity);
            modifyBackgroundSet(activity);
        } else {
            Log.e(LOG, "Activity is null");
        }
    }

    private void exitSet(final Activity activity) {
        CardView exit = getView().findViewById(R.id.exitProfileCard);
        exit.setOnClickListener(v -> new MaterialAlertDialogBuilder(new ContextThemeWrapper(activity, R.style.myDialog))
                .setTitle("Logout")
                .setMessage(getString(R.string.exit_message))
                .setPositiveButton(getString(R.string.exit), (dialog, which) -> {
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
                    prefs.edit().clear().apply();
                    startActivity(new Intent(activity, LoginActivity.class));
                    dialog.dismiss();
                })
                .setNegativeButton(android.R.string.no, (dialog, which) -> dialog.dismiss())
                .show());
    }

    private void modifyBackgroundSet(final Activity activity) {
        CardView background = getView().findViewById(R.id.cardBackgroundChange);
        background.setOnClickListener(v -> new MaterialAlertDialogBuilder(new ContextThemeWrapper(activity, R.style.myDialog))
                .setTitle(getString(R.string.next_update))
                .setPositiveButton("Ok", null)
                .show());
    }

    private void modifyPswSet(final Activity activity) {
        CardView modifyPsw = getView().findViewById(R.id.modifyPasswordCard);
        modifyPsw.setOnClickListener(v -> {
            Dialog pswDialog = new Dialog(getActivity());
            pswDialog.setContentView(R.layout.password_refresh);
            pswDialog.setCancelable(false);
            MaterialButton mod = pswDialog.findViewById(R.id.refreshPassword);
            MaterialButton canc = pswDialog.findViewById(R.id.refreshPasswordCancel);
            EditText lastPsw = pswDialog.findViewById(R.id.lastPswRefresh);
            EditText newPsw = pswDialog.findViewById(R.id.newPswRefresh);
            EditText confirmPsw = pswDialog.findViewById(R.id.confirmPsw);
            pswDialog.show();
            mod.setOnClickListener(f -> {
                String last = lastPsw.getText().toString();
                String neww = newPsw.getText().toString();
                String conf = confirmPsw.getText().toString();
                String actualPsw = db.generalDao().getActualPsw(Long.parseLong(HomeActivity.USER_ID));
                TextView errorpsw = pswDialog.findViewById(R.id.errorRefreshPsw);
                if(last.isEmpty() || neww.isEmpty() || conf.isEmpty()){
                    errorpsw.setVisibility(View.VISIBLE);
                    errorpsw.setText(R.string.error_fields);
                } else if(!neww.equals(conf)) {
                    errorpsw.setVisibility(View.VISIBLE);
                    errorpsw.setText(R.string.not_same_psw);
                } else if(!last.equals(actualPsw)) {
                    errorpsw.setVisibility(View.VISIBLE);
                    errorpsw.setText(R.string.not_old_psw);
                } else {
                    db.generalDao().setNewPsw(Long.parseLong(HomeActivity.USER_ID), neww);
                    new MaterialAlertDialogBuilder(new ContextThemeWrapper(activity, R.style.myDialog))
                            .setTitle(getString(R.string.password_refreshed))
                            .setPositiveButton("Ok", (dialog, which) -> pswDialog.dismiss())
                            .show();
                }
            });
            canc.setOnClickListener(f -> pswDialog.dismiss());
        });
    }

    private void removeAccountSet(final Activity activity) {
        CardView removeAccount = getView().findViewById(R.id.cardRemoveAccount);
        removeAccount.setOnClickListener(v -> new MaterialAlertDialogBuilder(new ContextThemeWrapper(activity, R.style.myDialog))
                .setTitle(getString(R.string.remove_title))
                .setMessage(getString(R.string.remove_text))
                .setPositiveButton(getString(R.string.remove), (dialog, which) -> {
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
                    prefs.edit().clear().apply();
                    for(final User u: db.generalDao().getAllUsers()){
                        if(String.valueOf(u.userId).equals(HomeActivity.USER_ID)){
                            db.generalDao().deleteUser(u);
                            break;
                        }
                    }
                    startActivity(new Intent(activity, LoginActivity.class));
                    dialog.dismiss();
                })
                .setNegativeButton(android.R.string.no, (dialog, which) -> dialog.dismiss())
                .show());
    }

    private void modifyInfosSet(final Activity activity) {
        CardView modifyInfos = getView().findViewById(R.id.modifyInfosCard);
        modifyInfos.setOnClickListener(v -> {
            Dialog infosDialog = new Dialog(getActivity());
            infosDialog.setContentView(R.layout.modify_infos);
            infosDialog.setCancelable(false);
            MaterialButton mod = infosDialog.findViewById(R.id.modifyInfos);
            MaterialButton canc = infosDialog.findViewById(R.id.modifyInfosCancel);
            EditText userEdit = infosDialog.findViewById(R.id.usernameRefresh);
            EditText firstEdit = infosDialog.findViewById(R.id.firstNameRefresh);
            EditText lastEdit = infosDialog.findViewById(R.id.lastNameRefresh);
            infosDialog.show();
            mod.setOnClickListener(f -> {
                String user = userEdit.getText().toString();
                String first = firstEdit.getText().toString();
                String last = lastEdit.getText().toString();
                List<String> usnames = db.generalDao().getAllUsernames();
                TextView errorpsw = infosDialog.findViewById(R.id.errorRefreshInfos);
                if(user.isEmpty() || first.isEmpty() || last.isEmpty()){
                    errorpsw.setVisibility(View.VISIBLE);
                    errorpsw.setText(R.string.error_fields);
                } else if(usnames.contains(user)) {
                    errorpsw.setVisibility(View.VISIBLE);
                    errorpsw.setText(R.string.error_username);
                } else {
                    db.generalDao().updateUsername(Long.parseLong(HomeActivity.USER_ID), user);
                    new MaterialAlertDialogBuilder(new ContextThemeWrapper(activity, R.style.myDialog))
                            .setTitle(getString(R.string.infos_updated))
                            .setPositiveButton("Ok", (dialog, which) -> infosDialog.dismiss())
                            .show();
                }
            });
            canc.setOnClickListener(f -> infosDialog.dismiss());
        });
    }

    private void uploadPhotoSet(final Activity activity) {
        CardView uploadPhoto = getView().findViewById(R.id.uploadPhotoCard);
        uploadPhoto.setOnClickListener(v -> new MaterialAlertDialogBuilder(new ContextThemeWrapper(activity, R.style.myDialog))
                .setTitle(getString(R.string.next_update))
                .setPositiveButton("Ok", null)
                .show());
    }

}
