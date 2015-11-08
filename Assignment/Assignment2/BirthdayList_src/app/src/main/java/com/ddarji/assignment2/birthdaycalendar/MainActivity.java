package com.ddarji.assignment2.birthdaycalendar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.fortysevendeg.swipelistview.BaseSwipeListViewListener;
import com.fortysevendeg.swipelistview.SwipeListView;

import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements AddBirthdayFragment.NoticeDialogListener, EditBirthdayFragment.NoticeDialogListener {

    SwipeListView listView;
    private BirthdayAdapter arrayAdapter;
    private ArrayList<BirthdayItem> birthdays;

    TextView emptyText;

    int openItem = -1;
    int lastClosedItem = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        emptyText = (TextView) findViewById(R.id.empty);

        birthdays = new ArrayList<>();

        listView = (SwipeListView) findViewById(R.id.listView);

        listView.setSwipeListViewListener(new BaseSwipeListViewListener() {
            @Override
            public void onOpened(int position, boolean toRight) {
                if (openItem > -1 && lastClosedItem != position) {
                    listView.closeAnimate(openItem);
                }
                openItem = position;
            }

            @Override
            public void onClosed(int position, boolean fromRight) {
                lastClosedItem = position;
            }

            @Override
            public void onListChanged() {
                listView.closeOpenedItems();
            }

            @Override
            public void onMove(int position, float x) {

            }

            @Override
            public void onStartOpen(int position, int action, boolean right) {
                Log.d("swipe", String.format("onStartOpen %d - action %d", position, action));
            }

            @Override
            public void onStartClose(int position, boolean right) {
                Log.d("swipe", String.format("onStartClose %d", position));
            }

            @Override
            public void onClickFrontView(int position) {
                BirthdayItem selectedBirthday = arrayAdapter.getItem(position);
                Toast.makeText(getApplicationContext(), selectedBirthday.toString(), Toast.LENGTH_SHORT).show();
                Log.d("swipe", String.format("onClickFrontView %d", position));
            }

            @Override
            public void onClickBackView(int position) {
                Log.d("swipe", String.format("onClickBackView %d", position));
                listView.closeOpenedItems();
            }

            @Override
            public void onDismiss(int[] reverseSortedPositions) {
                for (int position : reverseSortedPositions) {
                    birthdays.remove(position);
                }
                arrayAdapter.notifyDataSetChanged();
            }

        });

        arrayAdapter = new BirthdayAdapter(this, android.R.layout.simple_list_item_1, birthdays){
            @Override
            public void editButton(int p) {
                super.editButton(p);
                BirthdayItem b = arrayAdapter.getItem(p);
                showEditDialog(b.getId(), b.getName(), b.getBirthday());
                listView.closeOpenedItems();
            }

            @Override
            public void deleteButton(int p) {
                super.deleteButton(p);
                BirthdayItem b = arrayAdapter.getItem(p);
                showDeleteDialog(b.getId());
                listView.closeOpenedItems();
            }
        };

        listView.setAdapter(arrayAdapter);

        Refresh();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Add Birthday", Snackbar.LENGTH_LONG)
                        .setAction("Action", addBirthday()).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_about) {
            return true;
        } else if (id == R.id.action_refresh) {
            Refresh();
        } else if (id == R.id.action_reset) {
            Reset();
        } else if (id == R.id.action_retrieve) {
            onClickRetrieveBirthdays(null);
        }

        return super.onOptionsItemSelected(item);
    }

    private void Reset() {
        this.deleteDatabase(BirthdayProvider.DATABASE_NAME);
        Refresh();
    }

    private View.OnClickListener addBirthday() {

        showAddDialog();
        return null;
    }

    public void showAddDialog() {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = new AddBirthdayFragment();
        dialog.show(getFragmentManager(), "AddBirthdayFragment");
    }

    public void showEditDialog(Long id, String name, String birthday) {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = EditBirthdayFragment.newInstance(id, name, birthday);
        dialog.show(getFragmentManager(), "EditBirthdayFragment");
    }

    public void showDeleteDialog(final Long id) {
        Dialog d = new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Deleting Birthday")
                .setMessage("Are you sure you want to delete this Birthday?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteBirthday(id);
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }

    // The dialog fragment receives a reference to this Activity through the
    // Fragment.onAttach() callback, which it uses to call the following methods
    // defined by the NoticeDialogFragment.NoticeDialogListener interface
    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        // User touched the dialog's positive button
        String function = ((TextView) dialog.getDialog().findViewById(getResources()
                .getIdentifier("alertTitle", "id", "android"))).getText().toString();

        if (function.equals(getString(R.string.addBirthday))) {
            // Add a new birthday record
            ContentValues values = new ContentValues();

            values.put(BirthdayProvider.NAME,
                    ((EditText) dialog.getDialog().findViewById(R.id.txtName)).getText().toString());

            values.put(BirthdayProvider.BIRTHDAY,
                    ((EditText) dialog.getDialog().findViewById(R.id.txtBirthday)).getText().toString());

            Uri uri = getContentResolver().insert(
                    BirthdayProvider.CONTENT_URI, values);

            Refresh();

            Toast.makeText(getBaseContext(),
                    uri.toString(), Toast.LENGTH_LONG).show();
        } else if (function.equals(getString(R.string.editBirthday))) {
            ContentValues values = new ContentValues();

            values.put(BirthdayProvider._ID,
                    ((TextView) dialog.getDialog().findViewById(R.id.txtID)).getText().toString());

            values.put(BirthdayProvider.NAME,
                    ((EditText) dialog.getDialog().findViewById(R.id.txtName)).getText().toString());

            values.put(BirthdayProvider.BIRTHDAY,
                    ((EditText) dialog.getDialog().findViewById(R.id.txtBirthday)).getText().toString());

            Log.d("Updating Valued: ", values.toString());

            // Defines selection criteria for the rows you want to update
            String selectionClause = BirthdayProvider._ID + " LIKE ?";
            String[] selectionArgs = {values.getAsString(BirthdayProvider._ID)};

            // Defines a variable to contain the number of updated rows
            int rowsUpdated;

            rowsUpdated = getContentResolver().update(
                    BirthdayProvider.CONTENT_URI,           // the user dictionary content URI
                    values,             // the columns to update
                    selectionClause,    // the column to select on
                    selectionArgs       // the value to compare to
            );

            Refresh();

            Toast.makeText(this, "Rows Updated: " + rowsUpdated,
                    Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        // User touched the dialog's negative button
        dialog.dismiss();
    }

    /**
     * Refresh the Array Adapter.
     * Although this method is more of a Reset
     * (Inefficient)
     */
    private void Refresh() {
        Cursor c = getContentResolver().query(BirthdayProvider.CONTENT_URI, null, null, null, "name");

        arrayAdapter.clear();

        if (c.moveToFirst()) {
            do {
                Long id = c.getLong(c.getColumnIndex(BirthdayProvider._ID));
                String name = c.getString(c.getColumnIndex(BirthdayProvider.NAME));
                String birthday = c.getString(c.getColumnIndex(BirthdayProvider.BIRTHDAY));
                BirthdayItem data = new BirthdayItem(id, name, birthday);
                arrayAdapter.add(data);
            } while (c.moveToNext());
        }

        c.close();

        if (arrayAdapter.isEmpty()) {
            emptyText.setVisibility(View.VISIBLE);
        } else {
            emptyText.setVisibility(View.GONE);
        }

    }

    /**
     * Insert
     *
     * @param view View Clicked
     */
    public void onClickAddBirthday(View view) {
        // Add a new birthday record
        ContentValues values = new ContentValues();

        values.put(BirthdayProvider.NAME,
                ((EditText) findViewById(R.id.txtName)).getText().toString());

        values.put(BirthdayProvider.BIRTHDAY,
                ((EditText) findViewById(R.id.txtBirthday)).getText().toString());

        ((EditText) findViewById(R.id.txtName)).setText(""); // Reset Text
        ((EditText) findViewById(R.id.txtBirthday)).setText(""); // Reset Text


        Uri uri = getContentResolver().insert(
                BirthdayProvider.CONTENT_URI, values);

        Refresh();

        Toast.makeText(getBaseContext(),
                uri.toString(), Toast.LENGTH_LONG).show();
    }

    /**
     * Update birthday records
     *
     * @param view View Clicked
     */
    public void onClickUpdateName(View view) {
        ContentValues values = new ContentValues();

        values.put(BirthdayProvider.NAME,
                ((EditText) findViewById(R.id.txtName)).getText().toString());

        values.put(BirthdayProvider.BIRTHDAY,
                ((EditText) findViewById(R.id.txtBirthday)).getText().toString());

        ((EditText) findViewById(R.id.txtName)).setText(""); // Reset Text
        ((EditText) findViewById(R.id.txtBirthday)).setText(""); // Reset Text

        // Defines selection criteria for the rows you want to update
        String selectionClause = BirthdayProvider.NAME + " LIKE ?";
        String[] selectionArgs = {values.getAsString(BirthdayProvider.NAME)};

        // Defines a variable to contain the number of updated rows
        int rowsUpdated;

        rowsUpdated = getContentResolver().update(
                BirthdayProvider.CONTENT_URI,           // the user dictionary content URI
                values,             // the columns to update
                selectionClause,    // the column to select on
                selectionArgs       // the value to compare to
        );

        Refresh();

        Toast.makeText(this, "Rows Updated: " + rowsUpdated,
                Toast.LENGTH_SHORT).show();
    }

    /**
     * Delete birthday records
     *
     * @param view View Clicked
     */
    public void onClickDeleteBirthday(View view) {

        // Defines selection criteria for the rows you want to update
        String selectionClause = BirthdayProvider.NAME + " LIKE ?";
        String[] selectionArgs = {((EditText) findViewById(R.id.txtName)).getText().toString()};

        // Defines a variable to contain the number of updated rows
        int rowsDeleted;

        rowsDeleted = getContentResolver().delete(
                BirthdayProvider.CONTENT_URI,           // the user dictionary content URI
                selectionClause,    // the column to select on
                selectionArgs       // the value to compare to
        );

        ((EditText) findViewById(R.id.txtName)).setText(""); // Reset Text
        ((EditText) findViewById(R.id.txtBirthday)).setText(""); // Reset Text

        Refresh();

        Toast.makeText(this, "Rows Deleted: " + rowsDeleted,
                Toast.LENGTH_SHORT).show();
    }

    public void deleteBirthday(Long id) {

        // Defines selection criteria for the rows you want to update
        String selectionClause = BirthdayProvider._ID + " LIKE ?";

        String[] selectionArgs = {id+""};

        // Defines a variable to contain the number of updated rows
        int rowsDeleted;

        rowsDeleted = getContentResolver().delete(
                BirthdayProvider.CONTENT_URI,           // the user dictionary content URI
                selectionClause,    // the column to select on
                selectionArgs       // the value to compare to
        );

        Refresh();

        Toast.makeText(this, "Rows Deleted: " + rowsDeleted,
                Toast.LENGTH_SHORT).show();
    }

    /**
     * Retrieve birthday records
     *
     * @param view View Clicked
     */
    public void onClickRetrieveBirthdays(View view) {
        Cursor c = getContentResolver().query(BirthdayProvider.CONTENT_URI, null, null, null, "name");
        if (c.moveToFirst()) {
            do {
                Toast.makeText(this,
                        c.getString(c.getColumnIndex(BirthdayProvider._ID)) +
                                ": " + c.getString(c.getColumnIndex(BirthdayProvider.NAME)) +
                                " - " + c.getString(c.getColumnIndex(BirthdayProvider.BIRTHDAY)),
                        Toast.LENGTH_SHORT).show();
            } while (c.moveToNext());
        }
        c.close();
    }

}
