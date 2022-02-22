package com.example.contact;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class ContactProvider extends ContentProvider {
    private static final UriMatcher uriMatcher = new UriMatcher(-1);
    private static final int SUCCESS = 1;
    private MyOpenHelper myOpenHelper;

    static {
        uriMatcher.addURI("com.example.contact", "contact", SUCCESS);
    }

    @Override
    public boolean onCreate() {
        myOpenHelper = new MyOpenHelper(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        int code = uriMatcher.match(uri);
        if (code == SUCCESS) {
            SQLiteDatabase db = myOpenHelper.getReadableDatabase();
            return db.query("contact", projection, selection, selectionArgs,
                    null, null, sortOrder);
        } else {
            throw new IllegalArgumentException("路径不对，我是不会给你提供数据的！");
        }
    }

    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        int code = uriMatcher.match(uri);
        if (code == SUCCESS) {
            SQLiteDatabase db = myOpenHelper.getWritableDatabase();
            long rowId = db.insert("contact", null, values);
            if (rowId > 0) {
                Uri insertedUri = ContentUris.withAppendedId(uri, rowId);
                getContext().getContentResolver().notifyChange(insertedUri, null);
                return insertedUri;
            }
            db.close();
            return uri;
        } else {
            throw new IllegalArgumentException("路径不正确，我是不会给你插入数据的！");
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int code = uriMatcher.match(uri);
        if (code == SUCCESS) {
            SQLiteDatabase db = myOpenHelper.getWritableDatabase();
            int count = db.delete("contact", selection, selectionArgs);

            if (count > 0){
                getContext().getContentResolver().notifyChange(uri, null);
            }
            db.close();
            return count;
        } else {
            throw new IllegalArgumentException("路径不正确，我是不会让你随便删除数据的！");
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int code = uriMatcher.match(uri);
        if (code == SUCCESS) {
            SQLiteDatabase db = myOpenHelper.getWritableDatabase();
            int count = db.update("contact", values, selection, selectionArgs);
            if (count > 0) {
                getContext().getContentResolver().notifyChange(uri, null);
            }
            db.close();
            return count;
        } else {
            throw new IllegalArgumentException("路径不正确，我是不会随便让你更新数据的！");
        }
    }
}
