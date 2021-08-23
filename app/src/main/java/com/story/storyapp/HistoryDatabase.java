package com.story.storyapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class HistoryDatabase extends SQLiteOpenHelper {

    private static final int DB_VERSION=1;
    private static final String TAG="history db helper";
    private static  final String TABLE_NAME="history";
    private static final String COL1="id";
    private static final String COL2="story_doc_id";

    public HistoryDatabase(Context context){
        super(context,TAG,null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable="CREATE TABLE "+TABLE_NAME+"( "+COL1+" INTEGER PRIMARY KEY AUTOINCREMENT,"+COL2 +" TEXT UNIQUE)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public void addToHistory(String story){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(COL2,story);
        long result=db.insert(TABLE_NAME,null,contentValues);
        db.close();

//        return result==-1?"Some erorr occured":"data added successfully";
    }

    public List<String> getHistory(){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT "+COL2+" FROM "+TABLE_NAME,null);

        List<String> list=new ArrayList<>();

        if(cursor.moveToFirst()){
            do{
                list.add(cursor.getString(0));
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    public void removeFromHistory(String storyid){
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(TABLE_NAME,COL2+" = \""+storyid+"\"",null);
        db.close();
    }




}
