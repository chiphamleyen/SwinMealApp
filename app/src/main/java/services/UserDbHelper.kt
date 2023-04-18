package services

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class UserDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "MyDatabase.db"
        private const val DATABASE_VERSION = 1

        // Table names
        private const val TABLE_USERS = "users"

        // Column names
        private const val COLUMN_USER_ID = "id"
        private const val COLUMN_USER_NAME = "name"
        private const val COLUMN_USER_EMAIL = "email"
        private const val COLUMN_USER_EMPLOYEEID = "employeeId"
        private const val COLUMN_USER_MOBILE = "mobile"
        private const val COLUMN_USER_PASSWORD = "password"
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Create Users table
        val createUserTableQuery = "CREATE TABLE $TABLE_USERS (" +
                "$COLUMN_USER_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_USER_NAME TEXT," +
                "$COLUMN_USER_EMAIL TEXT," +
                "$COLUMN_USER_EMPLOYEEID TEXT," +
                "$COLUMN_USER_MOBILE TEXT," +
                "$COLUMN_USER_PASSWORD TEXT" +
                ")"
        db.execSQL(createUserTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Drop existing tables and recreate them on database upgrade
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        onCreate(db)
    }

    // User registration
    fun registerUser(name: String, email: String, employeeId: String, mobile: String, password: String) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_USER_NAME, name)
        values.put(COLUMN_USER_EMAIL, email)
        values.put(COLUMN_USER_EMPLOYEEID, employeeId)
        values.put(COLUMN_USER_MOBILE, mobile)
        values.put(COLUMN_USER_PASSWORD, password)
        db.insert(TABLE_USERS, null, values)

        Log.d("registerUser", "Email: $email")
        Log.d("registerUser", "Password: $password")
    }

    // User login
    fun loginUser(email: String, password: String): Boolean {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_USERS WHERE $COLUMN_USER_EMAIL = ? AND $COLUMN_USER_PASSWORD = ?"
        val cursor = db.rawQuery(query, arrayOf(email, password))
        val count = cursor.count
        cursor.close()
        return count > 0

        Log.d("loginUser", "Email: $email")
        Log.d("loginUser", "Password: $password")
    }

    // Get user profile
    @SuppressLint("Range")
    fun getUserProfile(email: String): User? {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_USERS WHERE $COLUMN_USER_EMAIL = ?"
        val cursor = db.rawQuery(query, arrayOf(email))
        with(cursor) {
            while (moveToNext()) {
                val name = getString(getColumnIndexOrThrow(COLUMN_USER_NAME))
                val userEmail = getString(getColumnIndexOrThrow(COLUMN_USER_EMAIL))
                val employeeId = getString(getColumnIndexOrThrow(COLUMN_USER_EMPLOYEEID))
                val mobile = getString(getColumnIndexOrThrow(COLUMN_USER_MOBILE))
                val password = getString(getColumnIndexOrThrow(COLUMN_USER_PASSWORD))
                cursor.close()
                return User(name, userEmail, employeeId, mobile, password)
            }
        }
        cursor.close()
        return null
    }
}

data class User(
    var name: String = "name",
    var email: String = "email",
    var employeeId: String = "id",
    var mobile: String = "mobile",
    var password: String = "password"
)