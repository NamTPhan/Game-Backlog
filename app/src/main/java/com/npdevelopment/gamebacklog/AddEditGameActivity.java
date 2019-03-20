package com.npdevelopment.gamebacklog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Spinner;

import com.npdevelopment.gamebacklog.model.Game;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class AddEditGameActivity extends AppCompatActivity {

    private TextInputEditText mTitleInput, mPlatformInput;
    private Snackbar mSnackBar;
    private Spinner statusSpinner;
    private Game mGame;
    private Date date = new Date();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_game);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Enable back arrow in toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mTitleInput = findViewById(R.id.gameTitleInput);
        mPlatformInput = findViewById(R.id.gamePlatformInput);
        statusSpinner = findViewById(R.id.status_dropdown);

        // Change default toolbar title to Edit Game if request code is edit game
        if (getIntent().getExtras().getInt(getString(R.string.request_code)) == GameBacklogActivity.REQUEST_CODE_EDIT_GAME) {
            getSupportActionBar().setTitle(getString(R.string.title_edit_game));
            editGameShowData();
        }

        FloatingActionButton fab = findViewById(R.id.fab_add_save_game);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check if input fields are empty
                if (TextUtils.isEmpty(mTitleInput.getText()) || TextUtils.isEmpty(mPlatformInput.getText())) {
                    mSnackBar = Snackbar.make(view, getString(R.string.fields_required), Snackbar.LENGTH_SHORT);

                    // Change color of snackbar and call the show method
                    View sbView = mSnackBar.getView();
                    sbView.setBackgroundColor(getResources().getColor(R.color.red));
                    mSnackBar.show();
                } else {

                    // Determine which method to call based on the sent request code
                    if (getIntent().getExtras().getInt(getString(R.string.request_code)) == GameBacklogActivity.REQUEST_CODE_NEW_GAME) {
                        addNewGame();
                    } else {
                        editGame();
                    }

                    Intent data = new Intent();
                    data.putExtra(GameBacklogActivity.GAME_ITEM_KEY, mGame);

                    //Send the result back to the activity
                    setResult(Activity.RESULT_OK, data);

                    //Go back to the previous activity
                    finish();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle back arrow
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Get all data from the input fields and put in the mGame object
     */
    private void addNewGame() {
        String gameTitle = mTitleInput.getText().toString();
        String gamePlatform = mPlatformInput.getText().toString();
        String gameStatus = statusSpinner.getSelectedItem().toString();

        mGame = new Game(gameTitle, gamePlatform, gameStatus, getCurrentDate(), getCurrentDate());
    }

    /**
     * Get all data from the input fields and update the mGame object
     */
    private void editGame() {
        mGame.setTitle(mTitleInput.getText().toString());
        mGame.setPlatform(mPlatformInput.getText().toString());
        mGame.setStatus(statusSpinner.getSelectedItem().toString());
        mGame.setDate_edited(getCurrentDate());
    }

    /**
     * Get the Game object from the sended data and view the values in the input fields
     */
    private void editGameShowData() {
        mGame = getIntent().getExtras().getParcelable(GameBacklogActivity.GAME_ITEM_KEY);

        mTitleInput.setText(mGame.getTitle());
        mPlatformInput.setText(mGame.getPlatform());
        // Find index of the status in the string array than use the index to set selection for the dropdown
        String[] statuses = getResources().getStringArray(R.array.spinnerItems);
        statusSpinner.setSelection(Arrays.asList(statuses).indexOf(mGame.getStatus()));
    }

    /**
     * Get current date in the format dd/MM/yyyy
     * @return the current date as string
     */
    private String getCurrentDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        date = Calendar.getInstance().getTime();
        String currentDate = dateFormat.format(date);
        return currentDate;
    }

}
