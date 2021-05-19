package com.kenyrim.mvvm.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.kenyrim.mvvm.ArticleModel;

import java.util.ArrayList;

public class DataBase {

    long id;
    String  source,
            title,
             link,
             guid,
             enclosure,
             description,
             pubDate;

    private static final String DATABASE_NAME = "offline.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "myTable";

    private static final String ID = "_id";

    private static final String COL_TIT = "title";
    private static final String COL_SOR = "source";
    private static final String COL_LIN = "link";
    private static final String COL_GUI = "guid";
    private static final String COL_ENC = "enclosure";
    private static final String COL_DES = "description";
    private static final String COL_PUB = "pubDate";


    private static final int NUM_ID  = 0;
    private static final int NUM_TIT = 1;
    private static final int NUM_SOR = 2;
    private static final int NUM_LIN = 3;
    private static final int NUM_GUI = 4;
    private static final int NUM_ENC = 5;
    private static final int NUM_DES = 6;
    private static final int NUM_PUB = 7;



    private SQLiteDatabase database;
    private Context context;

    public void insert(ArticleModel model) {
        open(context);
        ContentValues cv = new ContentValues();
        cv.put(COL_TIT    , model.getTitle());
        cv.put(COL_SOR    , model.getSource());
        cv.put(COL_LIN    , model.getLink());
        cv.put(COL_GUI    , model.getGuid());
        cv.put(COL_ENC    , model.getEnclosure());
        cv.put(COL_DES    , model.getDescription());
        cv.put(COL_PUB    , model.getPubDate());
        database.insert(TABLE_NAME, null, cv);
        close();
    }

    public DataBase(Context context){
//        DbOpenHelper openHelper = new DbOpenHelper(context);
//        database = openHelper.getWritableDatabase();
        this.context = context;
    }

    public void delete(long id){
        database.delete(TABLE_NAME, ID + " = ?", new String[] {String.valueOf(id)});
    }

    public void deleteAll(){
        database.delete(TABLE_NAME, null, null);
        context.deleteDatabase(DATABASE_NAME);
    }

    public ArrayList<ArticleModel> selectAll() {
        Log.e("SQLiteDatabase","selectAll");
        open(context);
        Cursor cursor = database.query
                (TABLE_NAME, null,null,null,null, null, COL_PUB);
        ArrayList<ArticleModel> arr = new ArrayList<>();
        cursor.moveToFirst();
        if (!cursor.isAfterLast()){
            do {
                id = cursor.getLong(NUM_ID);

                title = cursor.getString(NUM_TIT);
                source = cursor.getString(NUM_SOR);
                link = cursor.getString(NUM_LIN);
                guid = cursor.getString(NUM_GUI);
                enclosure = cursor.getString(NUM_ENC);
                description = cursor.getString(NUM_DES);
                pubDate = cursor.getString(NUM_PUB);

                arr.add(new ArticleModel(id,title,source,link,guid,enclosure,description,pubDate));
            }while (cursor.moveToNext());

        }
        cursor.close();
        database.close();
        return arr;
    }

    public void close() {
        database.close();
    }

    public void open(Context context){
        DbOpenHelper openHelper = new DbOpenHelper(context);
        database = openHelper.getWritableDatabase();
    }


    private class DbOpenHelper extends SQLiteOpenHelper {
        public DbOpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            database = db;
            String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                    ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_TIT + " TEXT, " +
                    COL_SOR + " TEXT, " +
                    COL_LIN + " TEXT, " +
                    COL_GUI + " TEXT, " +
                    COL_ENC + " TEXT, " +
                    COL_DES + " TEXT, " +
                    COL_PUB + " TEXT, " +
                    "UNIQUE( " + COL_LIN + " ) ON CONFLICT REPLACE);";
            db.execSQL(sql);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }
}
