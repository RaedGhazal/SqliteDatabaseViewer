package com.raedghazal.sqlitedatabaseviewerexample;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.raedghazal.sqlite_database_viewer.RGDatabaseViewer;

public class MainActivity extends AppCompatActivity {
private DatabaseHelper databaseHelper = new DatabaseHelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new RGDatabaseViewer(this,databaseHelper).getView());
    }
    public class DatabaseHelper extends SQLiteOpenHelper{
        private DatabaseHelper(Context context) {
            super(context, "db.db",null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("Create Table table1 (Id Integer ,Name Varchar)");
            db.execSQL("Create Table table2 (CityName Integer ,Name Varchar)");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
