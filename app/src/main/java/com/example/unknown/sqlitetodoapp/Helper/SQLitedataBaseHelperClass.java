package com.example.unknown.sqlitetodoapp.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.example.unknown.sqlitetodoapp.CreateNotes;
import com.example.unknown.sqlitetodoapp.DeleteNotes;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by UNKNOWN on 7/2/2016.
 */
public class SQLitedataBaseHelperClass extends SQLiteOpenHelper {

    private Context context;

    //DATABASE NAME
    private static final String DATABASE_NAME = "MYDATABASE";

    //DABASE VERSION
    private static final String DATABASE_VERSION = "1";

    //TABLE NAMES
    private static final String TABLE_TODO = "todos";
    private static final String TABLE_TAG = "tags";
    private static final String TABLE_TODO_TAG = "todos_tag";

    //COMMON COLUMNS
    private static final String KEY_ID = "id";
    private static final String KEY_CREATED_AT = "created_at";

    //COLUMNS FOR TABLE_TODO
    private static final String KEY_TODO = "todo";
    private static final String KEY_STATUS = "STATUS";

    //COLUMNS FOR TABLE_TAG
    private static final String KEY_TAG = "tag";

    //COLUMNS FOR TABLE_TODO_TAG
    private static final String KEY_TODO_ID = "todo_id";
    private static final String KEY_TAG_ID = "tag_id";


    private static final String CREATE_TABLE_TODO = "CREATE TABLE "+ TABLE_TODO + "( " + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_TODO + " TEXT," + KEY_STATUS + " INTERGER,"+ KEY_CREATED_AT + " DATETIME" + ")";

    private static final String CREATE_TABLE_TAG = "CREATE TABLE "+ TABLE_TAG + "( " + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_TAG + " TEXT," + KEY_CREATED_AT + " DATETIME" + ")";


    private static final String CREATE_TABLE_TODO_TAG = "CREATE TABLE "+ TABLE_TODO_TAG + "( " + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_TODO_ID + " INTEGER," + KEY_TAG_ID + " INTERGER,"+ KEY_CREATED_AT + " DATETIME" + ")";

    public SQLitedataBaseHelperClass(Context context) {

        super(context, DATABASE_NAME,null, Integer.parseInt(DATABASE_VERSION));

        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_TODO);
        db.execSQL(CREATE_TABLE_TAG);
        db.execSQL(CREATE_TABLE_TODO_TAG);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_TODO );
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_TAG );
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_TODO_TAG );

        onCreate(db);
    }


    //CRETE A TODO
    public long createToDo(ToDo todo,long[] tag_ids){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TODO,todo.getNote());
        values.put(KEY_STATUS,todo.getStatus());
        values.put(KEY_CREATED_AT,getDateTime());

        long todo_id = db.insert(TABLE_TODO,null,values);

        for(long tag_id : tag_ids){
            createToDoTag(todo_id,tag_id);
        }

        return todo_id;
    }

    // TABLE TODO GET SINGLE NOTE

    public ToDo getToDo(long todo_id){
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_TODO + " WHERE " + KEY_ID + " = " + todo_id ;
        Cursor c = db.rawQuery(selectQuery,null);
        if(c!= null){
            c.moveToFirst();
            ToDo td = new ToDo();
            td.setId(c.getInt(c.getColumnIndex(KEY_ID)));
            td.setNote(c.getString(c.getColumnIndex(KEY_TODO)));
            td.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
            return td;
        }
        else{
            return null;
        }
    }

    public ToDo getToDoId(String todo){
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_TODO + " WHERE " + KEY_TODO + " = " + "'" +  todo + "'";
        Cursor cursor = db.rawQuery(selectQuery,null);

        if(cursor != null){
            cursor.moveToFirst();
            ToDo toDo = new ToDo();
            toDo.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
            return toDo;
        }

        return null;
    }

    // TABLE TODO GET ALL NOTES

    public List<ToDo> getAllToDos(){

        List<ToDo> toDos = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_TODO ;
        Cursor c = db.rawQuery(selectQuery,null);

        if(c.moveToFirst()){
            do {
                ToDo td = new ToDo();
                td.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                td.setNote(c.getString(c.getColumnIndex(KEY_TODO)));
                td.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
                toDos.add(td);
            }while (c.moveToNext());

            return toDos;
        }

        else {
            return null;
        }
    }

    public List<ToDo> getAllToDosByTag(String tag_name){
        List<ToDo> toDos = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_TODO + " td, " + TABLE_TAG + " tg, " + TABLE_TODO_TAG + " tt "
                + "WHERE tg." + KEY_TAG + " = " + "'" + tag_name + "'" + " AND tg." + KEY_ID + " = " + "tt." + KEY_TAG_ID
                + " AND td." + KEY_ID + " = " + "tt." +  KEY_TODO_ID ;

        Cursor c = db.rawQuery(selectQuery,null);

        if(c.moveToFirst()){
            do {
                ToDo td = new ToDo();
                td.setNote(c.getString(c.getColumnIndex(KEY_TODO)));
                td.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                td.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
                toDos.add(td);
            }while (c.moveToNext());
            return toDos;
        }else{
            return null;
        }
    }


    public int updateToDo(ToDo toDo){

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values =new ContentValues();
        values.put(KEY_TODO,toDo.getNote());
        values.put(KEY_CREATED_AT,getDateTime());

        return db.update(TABLE_TODO,values, KEY_ID + " = ? " , new String[] {String.valueOf(toDo.getId())});
    }

    public int deleteToDo(ToDo toDo){
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(TABLE_TODO, KEY_ID + " = ? " , new String[] {String.valueOf(toDo.getId())});
    }

    /**
     * getting todo count
     */
    public int getToDoCount() {
        String countQuery = "SELECT  * FROM " + TABLE_TODO;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        // return count
        return count;
    }


    //METHODS FOR TABLE_TAG TABLE

    public long createTag(Tag tag){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TAG,tag.getTag());
        values.put(KEY_CREATED_AT,getDateTime());
        long tag_id = db.insert(TABLE_TAG,null,values);
        return tag_id;
    }

    public Tag getTagId(String tag){
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_TAG+ " WHERE " + KEY_TAG + " = " + "'" +  tag + "'";
        Cursor cursor = db.rawQuery(selectQuery,null);

        if(cursor != null){
            cursor.moveToFirst();

            Tag tag1 = new Tag();
            tag1.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
            tag1.setTag(cursor.getString(cursor.getColumnIndex(KEY_TAG)));
            return tag1;
        }

        return null;
    }

    public List<Tag> getAllTags(){

        List<Tag> tags = new ArrayList<>();
        SQLiteDatabase db =getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_TAG;

        Cursor c = db.rawQuery(selectQuery,null);

        if(c.moveToFirst())
        {
            do {
                Tag tag = new Tag();
                tag.setTag(c.getString(c.getColumnIndex(KEY_TAG)));
                tag.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
                tags.add(tag);
            }while (c.moveToNext());

            return tags;
        }
        else {
            return null;
        }
    }

    public int updateTag(Tag tag){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TAG,tag.getTag());
        values.put(KEY_CREATED_AT,getDateTime());

        return db.update(TABLE_TAG,values,KEY_ID + " = ?",new String[] {String.valueOf(tag.getId())});
    }

    public int deleteTag(Tag tag , boolean should_delete_all_tag_todos){
        SQLiteDatabase db = getWritableDatabase();

        if(should_delete_all_tag_todos){
            List<ToDo> toDos = getAllToDosByTag(tag.getTag());
            for(ToDo toDo : toDos){
                deleteToDo(toDo);
            }
        }
        return db.delete(TABLE_TAG,KEY_ID + " = ?",new String[] {String.valueOf(tag.getId())});
    }

    public long createToDoTag(long todo_id, long tag_id){

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TAG_ID,tag_id);
        values.put(KEY_TODO_ID,todo_id);
        values.put(KEY_CREATED_AT,getDateTime());

        return db.insert(TABLE_TODO_TAG,null,values);
    }

    public void removeToDoTag(long id){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_TODO_TAG,KEY_ID + " = ?",new String[] {String.valueOf(id)} );
    }

    public int updateToDoTag(long id, long tag_id){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TAG_ID,tag_id);
        return db.update(TABLE_TODO_TAG,values,KEY_ID + " = ?", new String[] {String.valueOf(id)});
    }


    public void closeDB(){

        SQLiteDatabase db = getReadableDatabase();
        if(db != null && db.isOpen()){
            db.close();
        }
    }

    private String getDateTime(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
        Date date = new Date();
        return format.format(date);
    }

}
