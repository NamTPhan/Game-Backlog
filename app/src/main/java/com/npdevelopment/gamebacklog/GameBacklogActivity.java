package com.npdevelopment.gamebacklog;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.npdevelopment.gamebacklog.database.GameBacklogRoomDatabase;
import com.npdevelopment.gamebacklog.model.Game;
import com.npdevelopment.gamebacklog.ui.GameBacklogAdapter;
import com.npdevelopment.gamebacklog.ui.MainViewModel;

import java.util.ArrayList;
import java.util.List;

public class GameBacklogActivity extends AppCompatActivity implements RecyclerView.OnItemTouchListener {

    public static final String GAME_ITEM_KEY = "gameItemKey";
    public static final int REQUEST_CODE_NEW_GAME = 200;
    public static final int REQUEST_CODE_EDIT_GAME = 300;
    private final int ITEMS_EACH_ROW = 1;

    private List<Game> gamesList, tempGamesList;
    private RecyclerView rvGameList;
    private Game mGame;
    private GameBacklogAdapter mGameBacklogAdapter;
    private GestureDetector mGestureDetector;
    private Snackbar mSnackBar;

    private MainViewModel mMainViewModel;
    private GameBacklogRoomDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_backlog);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        rvGameList = findViewById(R.id.rv_game_list);
        database = GameBacklogRoomDatabase.getDatabase(this);
        gamesList = new ArrayList<>();

        // Get View Model
        mMainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        // Create the observer which updates the UI
        mMainViewModel.getGames().observe(this, new Observer<List<Game>>() {
            @Override
            public void onChanged(@Nullable List<Game> games) {
                gamesList = games;
                updateUI();
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab_create_edit_game);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GameBacklogActivity.this, AddEditGameActivity.class);
                intent.putExtra(getString(R.string.request_code), REQUEST_CODE_NEW_GAME);
                startActivityForResult(intent, REQUEST_CODE_NEW_GAME);
            }
        });

        RecyclerView.LayoutManager mLayoutManager = new StaggeredGridLayoutManager(ITEMS_EACH_ROW, LinearLayoutManager.VERTICAL);

        rvGameList.setLayoutManager(mLayoutManager);
        rvGameList.setHasFixedSize(true);
        rvGameList.addOnItemTouchListener(this);

        mGestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });

         /*
            Recognize swipe gesture of the user
        */
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback =
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                        return false;
                    }

                    //Called when a user swipes left or right on a ViewHolder
                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                        //Get the index corresponding to the selected position
                        int position = (viewHolder.getAdapterPosition());

                        mGame = gamesList.get(position);
                        mMainViewModel.delete(gamesList.get(position));
                        mGameBacklogAdapter.notifyItemRemoved(position);

                        // Give the user the chance to restore the game
                        mSnackBar = Snackbar.make(findViewById(android.R.id.content), "Deleted: " +
                                gamesList.get(position).getTitle(), Snackbar.LENGTH_LONG).setAction(R.string.undo_string,
                                new View.OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        Toast.makeText(getApplicationContext(),getString(R.string.restore_string), Toast.LENGTH_SHORT).show();
                                        mMainViewModel.insert(mGame);
                                        mGameBacklogAdapter.refreshList(gamesList);
                                    }
                                });
                        mSnackBar.show();
                    }
                };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(rvGameList);

    }

    private void updateUI() {
        if (mGameBacklogAdapter == null) {
            mGameBacklogAdapter = new GameBacklogAdapter(gamesList);
            rvGameList.setAdapter(mGameBacklogAdapter);
        } else {
            mGameBacklogAdapter.refreshList(gamesList);
        }
    }

    // Receive result from another activity
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Check if the result code is the right one
        if (resultCode == Activity.RESULT_OK) {

            mGame = data.getParcelableExtra(GAME_ITEM_KEY);

            if (requestCode == REQUEST_CODE_NEW_GAME) {
                mMainViewModel.insert(mGame);
                mSnackBar = Snackbar.make(findViewById(android.R.id.content), getString(R.string.success_message), Snackbar.LENGTH_LONG);
            } else if (requestCode == REQUEST_CODE_EDIT_GAME) {
                mMainViewModel.update(mGame);
                mSnackBar = Snackbar.make(findViewById(android.R.id.content), getString(R.string.update_message), Snackbar.LENGTH_LONG);
            }

            View sbView = mSnackBar.getView();
            sbView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            mSnackBar.show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game_backlog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // Delete all games
        if (id == R.id.action_delete_item) {
            // Save the games list in a temporary list for restoring the data
            tempGamesList = gamesList;
            mMainViewModel.deleteAllGames(gamesList);

            mSnackBar = Snackbar.make(findViewById(android.R.id.content), getString(R.string.deleted_message),
                    Snackbar.LENGTH_LONG).setAction(R.string.undo_string,
                    new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            Toast.makeText(getApplicationContext(),getString(R.string.restore_string), Toast.LENGTH_SHORT).show();
                            mMainViewModel.insertAll(tempGamesList);
                            mGameBacklogAdapter.refreshList(gamesList);
                        }
                    });
            mSnackBar.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
        View child = rvGameList.findChildViewUnder(motionEvent.getX(), motionEvent.getY());

        if (child != null) {
            int adapterPosition = rvGameList.getChildAdapterPosition(child);

            if (mGestureDetector.onTouchEvent(motionEvent)) {
                Intent intent = new Intent(GameBacklogActivity.this, AddEditGameActivity.class);
                // Send request code of edit status
                intent.putExtra(getString(R.string.request_code), REQUEST_CODE_EDIT_GAME);
                // Send the game that has to be edited
                intent.putExtra(GAME_ITEM_KEY, gamesList.get(adapterPosition));
                startActivityForResult(intent, REQUEST_CODE_EDIT_GAME);
            }
        }
        return false;
    }

    @Override
    public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean b) {

    }
}
