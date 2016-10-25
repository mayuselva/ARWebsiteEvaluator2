package shanefuzz.arwebsiteevaluator.UserProfile.UserProfile.App.UserProfile;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;




/**
 * Handle activity functions with application database
 */
public class DatabaseHandler_user extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ARWebevaluator.db";
    private static final String TABLE_USER = "user";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PSWRD = "pswrd";
    private static final String COLUMN_IMAGE = "image";
    private static final String TAG = "DB";
    public byte[] ImgBuffer = {0, 1};
    private SQLiteDatabase db;

    public DatabaseHandler_user(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
        //create other table in same database;
        Log.e("DATABASE OPPERATION", "Database created/or open..");


    }

    /**
     * create database
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        String query = "CREATE TABLE " + TABLE_USER + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_EMAIL + " TEXT, " +
                COLUMN_PSWRD + " TEXT, " +
                COLUMN_IMAGE + " BLOB " +
                ");";
        db.execSQL(query);


        Log.e("DATABASE OPPERATION", "table created/or open..");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXIST" + TABLE_USER);
        onCreate(db);
    }


    /**
     * Add user details to the databse
     */
    public void addUser(UserProfile user) {

        Log.e(TAG, "addUser called");

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, user.get_name());
        values.put(COLUMN_EMAIL, user.get_email());
        values.put(COLUMN_PSWRD, user.get_pswrd());
        //values.put(COLUMN_PHONE, user.get_phone());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_USER, null, values);
        db.close();

        Log.e(TAG, "details added");

    }


    /**
     * Delete user account with all details
     */
    public boolean deleteUser(String userID) {

        boolean result = false;

        String query = "Select * FROM " + TABLE_USER + " WHERE " + COLUMN_ID + " =  \"" + userID + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor c = db.rawQuery(query, null);

        UserProfile user = new UserProfile();

        if (c.moveToFirst()) {
            user.set_id(Integer.parseInt(c.getString(0)));
            db.delete(TABLE_USER, COLUMN_ID + " = ?",
                    new String[]{String.valueOf(user.get_id())});
            c.close();
            result = true;
        }
        db.close();
        return result;
    }


    /**
     * Update user account details
     */
    public boolean updateUser(String id, String email, String phone) {


        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_EMAIL, email);
        //values.put(COLUMN_PHONE, phone);

        db.update(TABLE_USER, values, COLUMN_ID + " = ? ", new String[]{id});

        return true;

    }


    /**
     * Set user details to text fields in EditProfile
     */
    public UserProfile loadUserDetails(String userID) {
        String query = "Select * FROM " + TABLE_USER + " WHERE " + COLUMN_ID + " =  \"" + userID + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor c = db.rawQuery(query, null);

        UserProfile user = new UserProfile();

        if (c.moveToFirst()) {
            c.moveToFirst();
            user.set_name(c.getString(1));
            user.set_email(c.getString(2));
            //user.set_phone(c.getString(4));
            c.close();
        } else {
            user = null;
        }
        db.close();
        return user;
    }


    /**
     * Create user object and set values at the login
     */
    public UserProfile createUserObject(String userEmail, String userPassword) {

        String query = "SELECT * FROM " + TABLE_USER + " WHERE " + COLUMN_EMAIL + " =  \"" + userEmail + "\" AND " + COLUMN_PSWRD + " = \"" + userPassword + "\" ";

        SQLiteDatabase db = this.getWritableDatabase();


        Cursor c = db.rawQuery(query, null);

        UserProfile user = new UserProfile();


        /*if (c.moveToFirst()) {
            c.moveToFirst();
            user.set_id(Integer.parseInt(c.getString(0)));
            user.set_name(c.getString(1));
            user.set_email(c.getString(2));
            user.set_pswrd(c.getString(3));
            //user.set_phone(c.getString(4));

            Log.e(TAG, String.valueOf(c.getBlob(5)));

            if (!String.valueOf(c.getBlob(5)).equals("null")) {
                user.set_image(c.getBlob(5));
            } else {
                user.set_image(ImgBuffer);
            }
            c.close();

        } else {
            user = null;
        }*/
        db.close();

        return user;
    }


    /**
     * Change user password
     */
    public boolean changeUserPassword(String id, String password) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_PSWRD, password);

        db.update(TABLE_USER, values, COLUMN_ID + " = ? ", new String[]{id});

        return true;

    }


    /**
     * Reset user password in case user forgot it
     */
    public boolean resetUserPassword(String mail, String token) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_PSWRD, token);

        db.update(TABLE_USER, values, COLUMN_EMAIL + " = ? ", new String[]{mail});

        return true;
    }


    /**
     * Check the availability of the email address
     */
    public boolean isEmailStore(String userEmail) {

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USER + " WHERE " + COLUMN_EMAIL + " =  \"" + userEmail + "\" ";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;

    }


    /**
     * Check the credentials at the login(email address,password)
     */
    public boolean authenticateUserLogin(String userEmail, String userPassword) {

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USER + " WHERE " + COLUMN_EMAIL + " =  \"" + userEmail + "\" AND " + COLUMN_PSWRD + " = \"" + userPassword + "\" ";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;

    }


    /**
     * Store user profile image in the database
     */
    public boolean storeProfileImage(String id, byte[] image) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_IMAGE, image);


        db.update(TABLE_USER, values, COLUMN_ID + " = ? ", new String[]{id});


        return true;

    }


    public Cursor getdb() {

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USER + "";
        Cursor cursor = db.rawQuery(query, null);
        return cursor;

    }
}