package com.ddarji.assignment2.birthdaycalendar;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.Objects;

public class BirthdayProvider extends ContentProvider {

    private static final String AUTHORITY =
            "com.ddarji.provider.BirthdayProvider";
    private static final String BIRTHDAYS_TABLE = "birthdays";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BIRTHDAYS_TABLE);

    static final String _ID = "_id";
    static final String NAME = "name";
    static final String BIRTHDAY = "birthday";

    private static HashMap<String, String> BIRTHDAYS_PROJECTION_MAP;

    public static final int BIRTHDAYS = 1;
    public static final int BIRTHDAYS_ID = 2;

    private static final UriMatcher sURIMatcher =
            new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sURIMatcher.addURI(AUTHORITY, BIRTHDAYS_TABLE, BIRTHDAYS);
        sURIMatcher.addURI(AUTHORITY, BIRTHDAYS_TABLE + "/#", BIRTHDAYS_ID);
    }

    private SQLiteDatabase db;
    static final String DATABASE_NAME = "MyBirthdays";
    static final int DATABASE_VERSION = 1;
    static final String CREATE_DB_TABLE =
            " CREATE TABLE " + BIRTHDAYS_TABLE +
                    " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    " name TEXT NOT NULL, " +
                    " birthday TEXT NOT NULL);";

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase db)
        {
            db.execSQL(CREATE_DB_TABLE);
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion,
                              int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " +  BIRTHDAYS_TABLE);
            onCreate(db);
        }
    }

    public BirthdayProvider() {

    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
        return (db != null);
    }

    @Override
    public String getType(Uri uri) {
        switch (sURIMatcher.match(uri)) {
            case BIRTHDAYS:
                return "vnd.android.cursor.dir/vnd.ddarji.birthdays";
            case BIRTHDAYS_ID:
                return "vnd.android.cursor.item/vnd.ddarji.birthdays";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(BIRTHDAYS_TABLE);
        switch (sURIMatcher.match(uri)) {
            case BIRTHDAYS:
                qb.setProjectionMap(BIRTHDAYS_PROJECTION_MAP);
                break;
            case BIRTHDAYS_ID:
                qb.appendWhere( _ID + "=" + uri.getPathSegments().get(1));
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        if (sortOrder == null || Objects.equals(sortOrder, "")){
            sortOrder = NAME;
        }

        Cursor c = qb.query(db,	projection,	selection, selectionArgs,
                null, null, sortOrder);
        c.setNotificationUri(getContext().getContentResolver(), uri);

        return c;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long rowID = db.insert(BIRTHDAYS_TABLE, "", values);
        if (rowID > 0)
        {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }
        throw new SQLException("Failed to add a record into " + uri);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int count;
        switch (sURIMatcher.match(uri)){
            case BIRTHDAYS:
                count = db.update(BIRTHDAYS_TABLE, values,
                        selection, selectionArgs);
                break;
            case BIRTHDAYS_ID:
                count = db.update(BIRTHDAYS_TABLE, values, _ID +
                        " = " + uri.getPathSegments().get(1) +
                        (!TextUtils.isEmpty(selection) ? " AND (" +
                                selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri );
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count;
        switch (sURIMatcher.match(uri)){
            case BIRTHDAYS:
                count = db.delete(BIRTHDAYS_TABLE, selection, selectionArgs);
                break;
            case BIRTHDAYS_ID:
                String id = uri.getPathSegments().get(1);
                count = db.delete(BIRTHDAYS_TABLE, _ID + " = " + id +
                        (!TextUtils.isEmpty(selection) ? " AND (" +
                                selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

}
