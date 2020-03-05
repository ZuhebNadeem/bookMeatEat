package com.example.mappe2.DBHandler;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ContentProvider extends android.content.ContentProvider {

    //ContentProvider variabler
    public final static String PROVIDER = "com.example.mappe2";
    private static final int RESTAURANT_ID = 1;
    private static final int RESTAURANTER = 2;
    DB DBhelper;
    SQLiteDatabase db;

    //URI verdier
    public static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER +"/RestaurantTabell");
    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER, "RestaurantTabell", RESTAURANTER);
        uriMatcher.addURI(PROVIDER, "RestaurantTabell/#", RESTAURANT_ID);
    }



    @Override
    public boolean onCreate() {
        DBhelper = new DB(getContext());
        db = DBhelper.getWritableDatabase();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        Cursor cur = null;
        if(uriMatcher.match(uri) == RESTAURANT_ID) {
            cur= db.query("RestaurantTabell", strings, "ID=" + uri.getPathSegments().get(1), strings1, null, null, s1);

            return cur;
        }
        else {
            cur= db.query("RestaurantTabell", null, null, null, null, null, null);
            return cur;
        }    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)) {
            case RESTAURANTER:
                return "vnd.android.cursor.dir/vnd.example.mappe2";
            case RESTAURANT_ID:
                return "nd.android.cursor.item/vnd.example.mappe2";
            default:
                throw new IllegalArgumentException("Ugyldig URI" + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        SQLiteDatabase db = DBhelper.getWritableDatabase();
        db.insert("RestaurantTabell", null, contentValues);
        Cursor c = db.query("RestaurantTabell", null, null, null, null, null, null);
        c.moveToLast();
        long minid= c.getLong(0);
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, minid);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
