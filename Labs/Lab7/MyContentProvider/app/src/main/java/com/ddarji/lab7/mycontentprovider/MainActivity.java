package com.ddarji.lab7.mycontentprovider;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.net.Uri;
import android.content.ContentValues;
import android.database.Cursor;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;

public class MainActivity extends Activity {

    ListView listView;
    ArrayList<String> arrayList;
    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView=(ListView)findViewById(R.id.listView);

        arrayList = new ArrayList<>();

        arrayAdapter =
                new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(arrayAdapter);

        Refresh();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
                String selectedStudent = arrayList.get(position);
                Toast.makeText(getApplicationContext(), selectedStudent, Toast.LENGTH_SHORT).show();
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
        }
        else if(id == R.id.refresh){
            Refresh();
        }
        else if(id == R.id.reset){
            Reset();
        }

        return super.onOptionsItemSelected(item);
    }

    private void Reset(){
        this.deleteDatabase(StudentsProvider.DATABASE_NAME);

    }

    /**
     * Refresh the Array Adapter.
     * Although this method is more of a Reset
     * (Inefficient)
     */
    private void Refresh(){
        Cursor c = getContentResolver().query(StudentsProvider.CONTENT_URI, null, null, null, "name");

        arrayAdapter.clear();

        if (c.moveToFirst()) {
            do {
                String data =
                        c.getString(c.getColumnIndex(StudentsProvider._ID)) +
                                ": " + c.getString(c.getColumnIndex(StudentsProvider.NAME)) +
                                " - " + c.getString(c.getColumnIndex(StudentsProvider.GRADE));
                arrayAdapter.add(data);
            } while (c.moveToNext());
        }

    }

    /**
     * Insert
     * @param view View Clicked
     */
    public void onClickAddName(View view) {
        // Add a new student record
        ContentValues values = new ContentValues();

        values.put(StudentsProvider.NAME,
                ((EditText)findViewById(R.id.txtName)).getText().toString());

//        ((EditText)findViewById(R.id.txtName)).setText(""); // Reset Text

        values.put(StudentsProvider.GRADE,
                ((EditText) findViewById(R.id.txtGrade)).getText().toString());

//        ((EditText)findViewById(R.id.txtGrade)).setText(""); // Reset Text


        Uri uri = getContentResolver().insert(
                StudentsProvider.CONTENT_URI, values);

        Refresh();

        Toast.makeText(getBaseContext(),
                uri.toString(), Toast.LENGTH_LONG).show();
    }

    /**
     * Update student records
     * @param view View Clicked
     */
    public void onClickUpdateName(View view) {
        ContentValues values = new ContentValues();

        values.put(StudentsProvider.NAME,
                ((EditText)findViewById(R.id.txtName)).getText().toString());

//        ((EditText)findViewById(R.id.txtName)).setText(""); // Reset Text

        values.put(StudentsProvider.GRADE,
                ((EditText) findViewById(R.id.txtGrade)).getText().toString());

//        ((EditText)findViewById(R.id.txtGrade)).setText(""); // Reset Text

        // Defines selection criteria for the rows you want to update
        String selectionClause = StudentsProvider.NAME +  " LIKE ?";
        String[] selectionArgs = { values.getAsString(StudentsProvider.NAME)};

        // Defines a variable to contain the number of updated rows
        int rowsUpdated = 0;

        rowsUpdated = getContentResolver().update(
                StudentsProvider.CONTENT_URI,           // the user dictionary content URI
                values,             // the columns to update
                selectionClause,    // the column to select on
                selectionArgs       // the value to compare to
        );

        Refresh();

        Toast.makeText(this, "Rows Updated: " + rowsUpdated,
                Toast.LENGTH_SHORT).show();
    }

    /**
     * Delete student records
     * @param view View Clicked
     */
    public void onClickDeleteName(View view) {

        // Defines selection criteria for the rows you want to update
        String selectionClause = StudentsProvider.NAME +  " LIKE ?";
        String[] selectionArgs = {((EditText)findViewById(R.id.txtName)).getText().toString()};

        // Defines a variable to contain the number of updated rows
        int rowsDeleted = 0;

        rowsDeleted = getContentResolver().delete(
                StudentsProvider.CONTENT_URI,           // the user dictionary content URI
                selectionClause,    // the column to select on
                selectionArgs       // the value to compare to
        );

//        ((EditText)findViewById(R.id.txtName)).setText(""); // Reset Text
//        ((EditText)findViewById(R.id.txtGrade)).setText(""); // Reset Text

        Refresh();

        Toast.makeText(this, "Rows Deleted: "+ rowsDeleted,
                Toast.LENGTH_SHORT).show();
    }

    /**
     * Retrieve student records
     * @param view View Clicked
     */
    public void onClickRetrieveStudents(View view) {
        Cursor c = getContentResolver().query(StudentsProvider.CONTENT_URI, null, null, null, "name");
        if (c.moveToFirst()) {
            do {
                Toast.makeText(this,
                        c.getString(c.getColumnIndex(StudentsProvider._ID)) +
                                ": " + c.getString(c.getColumnIndex(StudentsProvider.NAME)) +
                                " - " + c.getString(c.getColumnIndex(StudentsProvider.GRADE)),
                        Toast.LENGTH_SHORT).show();
            } while (c.moveToNext());
        }
    }

}
