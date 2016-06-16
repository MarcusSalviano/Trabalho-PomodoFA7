package br.edu.fa7.pomodorofa7.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by MF on 06/06/16.
 */
public abstract class GenericDao<T extends IModel> implements IDatabase<T> {

    protected SQLiteDatabase db;
    protected String tableName;

    public GenericDao(Context context, String databaseName) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        this.db = dbHelper.getWritableDatabase();
        this.tableName = databaseName;
    }

    @Override
    public void insert(T obj) {
        db.insert(tableName,null,getContentValues(obj));
    }

    @Override
    public void update(T obj) {
        db.update(tableName, getContentValues(obj), "_id = ?", new String[]{obj.getId().toString()});
    }

    @Override
    public void delete(T obj) {
        db.delete(tableName, "_id = ?", new String[]{obj.getId().toString()});
    }

    @Override
    public T find(Integer id) {

        T obj = null;

        Cursor c = db.query(tableName, null, "_id = ?", new String[]{id.toString()}, null, null, "_id DESC");

        if(c.getCount() > 0) {
            c.moveToFirst();
            obj = getObjectFromCursor(c);
        }

        return obj;
    }

    @Override
    public List<T> findAll() {

        List<T> list = null;

        Cursor c = db.query(tableName, null, null, null, null, null, "_id DESC");

        if(c.getCount() > 0) {
            list = new ArrayList<>();
            c.moveToFirst();
            do {
                list.add(getObjectFromCursor(c));
            } while (c.moveToNext());
        }

        if(list == null) {
            list = Collections.emptyList();
        }

        return list;
    }

    public abstract ContentValues getContentValues(T obj);

    public abstract T getObjectFromCursor(Cursor cursor);
}
