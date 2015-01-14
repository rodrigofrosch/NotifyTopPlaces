package com.fd.alertplaces;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by frog on 06/01/15.
 */
public class DataPlace extends SQLiteOpenHelper
{
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "NotifyTopPlaces";

    public DataPlace(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /*CREATE TABLES*/
    private static final String CREATE_TABLE_PLACES = "create table if not exists places (id integer primary key autoincrement,name text,typeid integer,location text,comment text,refercomment text,openninghours text,averageprice text, address text, cityid integer, hasnotify integer, notifycounter text, foreign key(typeid) REFERENCES types(id), foreign key(cityid) references cities(id));";
    private static final String CREATE_TABLE_TYPES = "create table if not exists types (id integer primary key autoincrement,name text);";
    private static final String CREATE_TABLE_CITIES = "create table if not exists cities (id integer primary key autoincrement,city text,provinceid integer,foreign key (provinceid) references provinces(id));";
    private static final String CREATE_TABLE_PROVINCES = "create table if not exists provinces (id integer primary key autoincrement,name text);";

    /*INSERTS DEFAULTS*/
    private static final String INSERT_INTO_PROVINCES = "insert into provinces (name) values ('SP');";
    private static final String INSERT_INTO_CITIES = "insert into cities (city, provinceid) values ('São Paulo', 1);";
    private static final String INSERT_INTO_TYPES = "insert into types (name) values ('Supermercados');";
    private static final String INSERT_INTO_PLACES = "insert into places (name, typeid, location,comment, refercomment,openninghours,averageprice,address, cityid, hasnotify, notifycounter) values ('Abevê Supermercados', 1, '-23.584298,-46.580397','Comentário sobre indicação de fontes confiáveis','fonte do comentário','8:00,20:00 or 24','6,00','Rua tal de tals, 73 - Vila dos tals', 1, 0, '');";



    @Override
    public void onCreate(SQLiteDatabase db) {
        createTables(db);
        defaultInserts(db);
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }

    private void createTables(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_PLACES);
        db.execSQL(CREATE_TABLE_TYPES);
        db.execSQL(CREATE_TABLE_CITIES);
        db.execSQL(CREATE_TABLE_PROVINCES);
    }

    private void defaultInserts(SQLiteDatabase db){
        db.execSQL(INSERT_INTO_PROVINCES);
        db.execSQL(INSERT_INTO_CITIES);
        db.execSQL(INSERT_INTO_TYPES);
        db.execSQL(INSERT_INTO_PLACES);
    }

    public ArrayList<Place> getAllPlaces() {
        ArrayList<Place> list = new ArrayList<>();

        String query = "select pl.id, pl.name, t.name as type, pl.location, pl.comment, pl.refercomment, " +
                "pl.openninghours, pl.averageprice, pl.address, c.city as city, p.name as province from places as pl " +
                "inner join types as t on t.id = pl.typeid inner join cities as c on c.id = pl.cityid inner join provinces as p on p.id = c.provinceid;";

        SQLiteDatabase db = getReadableDatabase();

        Cursor c = db.rawQuery(query, null);

        //int id, String name, int typeId, double longitude, double latitude, String comment, String referComment, String openingHours, String averagePrice, String address, int cityId
        if (c.moveToFirst()){
            list.add(new Place(
                    Integer.parseInt(c.getString(c.getColumnIndex("id"))),
                    c.getString(c.getColumnIndex("name")),
                    c.getString(c.getColumnIndex("type")),
                    getLongitudeByLocation(c.getString(c.getColumnIndex("location"))),
                    getLatitudeByLocation(c.getString(c.getColumnIndex("location"))),
                    c.getString(c.getColumnIndex("comment")),
                    c.getString(c.getColumnIndex("refercomment")),
                    c.getString(c.getColumnIndex("openninghours")),
                    c.getString(c.getColumnIndex("averageprice")),
                    c.getString(c.getColumnIndex("address")),
                    c.getString(c.getColumnIndex("city")),
                    c.getString(c.getColumnIndex("province"))
            ));
        }
        c.close();
        db.close();
        return list;
    }

    private double getLatitudeByLocation(String location) {
        List<String> list = new ArrayList<String>(Arrays.asList(location.split(",")));
        return Double.parseDouble(list.get(0));
    }

    private double getLongitudeByLocation(String location) {
        List<String> list = new ArrayList<String>(Arrays.asList(location.split(",")));
        return Double.parseDouble(list.get(1));
    }

    public boolean placeHasNotify(String name) {
        Boolean result = null;

        String query = "select hasnotify from places where name = '"+name+"';";

        SQLiteDatabase db = getReadableDatabase();

        Cursor c = db.rawQuery(query, null);

        if (c.moveToFirst()){
            result = (Integer.parseInt(c.getString(c.getColumnIndex("hasnotify"))) != 0) ? true : false;
        }
        c.close();
        db.close();

        return result;
    }

    public boolean setHasNotify(String name, String milliseconds){
        SQLiteDatabase db = getWritableDatabase();
        Boolean result = null;

        String query = "update places set hasnotify = 1, notifycounter = '"+milliseconds+"';";

        try {
            db.execSQL(query);
            result = true;

        } catch (Exception e) {
            result = false;
            e.printStackTrace();
        }
        db.close();
        return result;
    }


    public boolean setNoNotify(String name){
        SQLiteDatabase db = getWritableDatabase();
        Boolean result = null;

        String query = "update places set hasnotify = 0, notifycounter = '';";

        try {
            db.execSQL(query);
            result = true;

        } catch (Exception e) {
            result = false;
            e.printStackTrace();
        }
        db.close();
        return result;
    }

    public ArrayList<String> getPlacesHasNotify() {
        ArrayList<String> list = new ArrayList<>();
        list.add("");
        SQLiteDatabase db = getReadableDatabase();
        String query = "select name from places where hasnotify = 1;";
        Cursor c= db.rawQuery(query, null);
        try {
            c.moveToFirst();
            while (c.isAfterLast() == false){
                list.add(c.getString(c.getColumnIndex("name")));
            }
            c.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public String getMilliseconds(String name) {
        SQLiteDatabase db = getReadableDatabase();

        String query = "select notifycounter from places where name = '"+name+"';";

        Cursor c = db.rawQuery(query, null);

        if (c.moveToFirst()) {
            return c.getString(c.getColumnIndex("notifycounter"));
        }
        c.close();
        db.close();
        return "";
    }

    public ArrayList<String> getAllTypes(){
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<String> list;
        list = new ArrayList<>();
        String query = "select name from types";

        Cursor c = db.rawQuery(query, null);
        while (!c.isAfterLast()){
            list.add(c.getString(c.getColumnIndex("name")));
        }
        c.close();
        db.close();
        return list;
    }

    public ArrayList<String> getPlacesByType(String typename){
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<String > list = new ArrayList<>();
        String query = "select pl.name from places as pl inner join types as t on t.id = pl.typeid where t.name = '"+typename+"';";
        Cursor c = db.rawQuery(query, null);
        while (!c.isAfterLast()){
            list.add(c.getString(c.getColumnIndex("name")));
        }
        c.close();
        db.close();
        return list;
    }
}
