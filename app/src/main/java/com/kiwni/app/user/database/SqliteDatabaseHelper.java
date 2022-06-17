package com.kiwni.app.user.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.kiwni.app.user.models.SqliteModelClass;

import java.util.ArrayList;

public class SqliteDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "favorite.db";
    SQLiteDatabase db;

    private static final int DATABASE_VERSION = 1;
    private static final String ADDRESS = "address";
    private static final String ADDRESS_TYPE = "address_type";
    private static final String FAVORITE_LAT = "favorite_lat";
    private static final String FAVORITE_LNG = "favorite_lng";

    public SqliteDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String q = "CREATE TABLE Favorite (" + ADDRESS + " TEXT," + ADDRESS_TYPE + " TEXT,"
                + FAVORITE_LAT + " TEXT," + FAVORITE_LNG + " TEXT)";

        sqLiteDatabase.execSQL(q);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists favorite");
        onCreate(sqLiteDatabase);
    }

    public boolean insertData(String address, String addressType, String favoriteLat, String favoriteLng)
    {
        db = this.getWritableDatabase();

        ContentValues Values = new ContentValues();
        Values.put(ADDRESS, address);
        Values.put(ADDRESS_TYPE, addressType);
        Values.put(FAVORITE_LAT, favoriteLat);
        Values.put(FAVORITE_LNG, favoriteLng);

        long r = this.db.insert("Favorite", null, Values);

        if (r == -1)
            return false;
        else
            db.close();
        return true;
    }

    @SuppressLint("Range")
    public ArrayList<SqliteModelClass> getAllData() {
        ArrayList<SqliteModelClass> array_list = new ArrayList<>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from Favorite", null );
        res.moveToFirst();

        int count = 0;

        Log.d("TAG","DBData"+res);


        while(!res.isAfterLast()){

            String addressType = res.getString(res.getColumnIndex(ADDRESS_TYPE));
            String address = res.getString(res.getColumnIndex(ADDRESS));
            String favoriteLat = res.getString(res.getColumnIndex(FAVORITE_LAT));
            String favoriteLng = res.getString(res.getColumnIndex(FAVORITE_LNG));


            /*Log.d("TAG","addressType ; " +addressType);*/

            array_list.add(new SqliteModelClass(addressType,address,favoriteLat,favoriteLng));
            res.moveToNext();

            Log.d("Tag","addList"+array_list);


        }


        return array_list;
    }

    // below is the method for updating our courses
    public boolean updateData (String address, String addressType, String favoriteLat, String favoriteLng){

        db = this.getWritableDatabase();

        ContentValues Values = new ContentValues();
        Values.put(ADDRESS, address);
        Values.put(ADDRESS_TYPE, addressType);
        Values.put(FAVORITE_LAT, favoriteLat);
        Values.put(FAVORITE_LNG, favoriteLng);

        Cursor cursor = db.rawQuery("select * from Favorite where ADDRESS_TYPE=? ", new String[]{addressType});

        if (cursor.getCount() > 0){
            long r = db.update("Favorite",Values,"address_type=?", new String[]{addressType});
            if (r == -1)
                return false;
            else
                return true;
        }
        return false;
    }

}
