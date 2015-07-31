package arshsingh93.una;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by Student on 7/28/2015.
 */
public class BlogHelper extends SQLiteOpenHelper{
    public static final String DB_NAME = "una.db";
    public static final int DB_VERSION = 1;

    //table functionality
    public static final String BLOG_TABLE = "BLOG";
    public static final String COLUMN_BLOG_ASSET = "ASSET";
    public static final String COLUMN_BLOG_NAME = "NAME";
    private static String CREATE_BLOGS = "CREATE TABLE " + BLOG_TABLE + " (" +
            BaseColumns._ID + "  INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_BLOG_ASSET + " TEXT," +
            COLUMN_BLOG_NAME + " TEXT";



    public BlogHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    public BlogHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_BLOGS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
