package br.edu.fa7.pomodorofa7.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

/**
 * Created by MF on 07/06/16.
 */
public class PomodoroDao  extends GenericDao<Pomodoro> {

    private static final String TABLE_NAME = "pomodoro";

    public PomodoroDao(Context context) {
        super(context, TABLE_NAME);
    }

    @Override
    public ContentValues getContentValues(Pomodoro obj) {
        ContentValues cv = new ContentValues();
        cv.put("_id", obj.getId());
        cv.put("titulo", obj.getTitulo());
        cv.put("descricao", obj.getDescricao());
        cv.put("num_pomodoros", obj.getNumPomodoros());
        cv.put("is_concluido", obj.isConcluido());

        return cv;
    }

    @Override
    public Pomodoro getObjectFromCursor(Cursor cursor) {
        Integer id = cursor.getInt(cursor.getColumnIndex("_id"));
        String titulo = cursor.getString(cursor.getColumnIndex("titulo"));
        String descricao = cursor.getString(cursor.getColumnIndex("descricao"));
        Integer numPomodoros = cursor.getInt(cursor.getColumnIndex("num_pomodoros"));
        Boolean isConcluido = cursor.getInt(cursor.getColumnIndex("is_concluido")) == 1?true:false;

        return new Pomodoro(id, titulo, descricao, numPomodoros, isConcluido);
    }
}
