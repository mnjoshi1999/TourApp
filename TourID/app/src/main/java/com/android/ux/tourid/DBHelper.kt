package com.android.ux.tourid

import android.annotation.SuppressLint
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteException
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import java.util.Date

//creating the database logic, extending the SQLiteOpenHelper base class
class DatabaseHandler(context: Context): SQLiteOpenHelper(context,DATABASE_NAME,null,DATABASE_VERSION) {
    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "ClientsDatabase"
        private val TABLE_CONTACTS = "ClientsTable"
        private val KEY_ID = "id"
        private val KEY_NAME = "name"
        private val KEY_EMAIL = "email"
        private val KEY_PASSWORD = "password"

        private val TABLE_BLOG = "BlogTable"
        private val BLOG_ID = "id"
        private val BLOG_TITLE = "title"
        private val BLOG_TEXT = "content"

        private val TABLE_COMMENT = "CommentTable"
        private val COMMENT_ID = "comment_id"
        private val COMMENT_OWNER = "owner"
        private val COMMENT_TEXT = "content"


        private val TABLE_TOUR = "TourTable"
        private val TOUR_TITLE = "tite"
        private val TOUR_DESC = "description"
        private val IMAGE_URI = "image"
        private val EVENT_TIME="eventtime"

        private val TABLE_RESERVATION = "ReservationTable"
        private val RESERVATION_ID = "id"
        private val ATTENDEE_NAME = "name"
        private val RESERVATION_DATE = "reserveddate"

        private val TABLE_CURRENT = "CurrentTable"
        private val CLIENT_NAME = "name"
        private val CLIENT_EMAIL = "email"



    }
    override fun onCreate(db: SQLiteDatabase?) {
        //creating table with fields
        val CREATE_CONTACTS_TABLE = ("CREATE TABLE IF NOT EXISTS " + TABLE_CONTACTS + "("
                + KEY_ID + " BIGINT PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_EMAIL + " TEXT," + KEY_PASSWORD + " TEXT" +")")
        db?.execSQL(CREATE_CONTACTS_TABLE)

        val CREATE_BLOG_TABLE = ("CREATE TABLE IF NOT EXISTS " + TABLE_BLOG + "("
                + BLOG_ID + " BIGINT PRIMARY KEY," + BLOG_TITLE + " TEXT,"
                + BLOG_TEXT + " TEXT"+")")
        db?.execSQL(CREATE_BLOG_TABLE)

        val CREATE_COMMENT_TABLE = ("CREATE TABLE IF NOT EXISTS " + TABLE_COMMENT + "("
                + COMMENT_ID + " BIGINT PRIMARY KEY," + COMMENT_OWNER + " TEXT,"
                + COMMENT_TEXT + " TEXT,"+ BLOG_ID +" BIGINT" +")")
        db?.execSQL(CREATE_COMMENT_TABLE)

        val CREATE_TOUR_TABLE = ("CREATE TABLE IF NOT EXISTS " + TABLE_TOUR + "("
                + TOUR_TITLE + " TEXT," + TOUR_DESC + " TEXT,"
                + IMAGE_URI + " TEXT," + EVENT_TIME + " TEXT" +")")
        db?.execSQL(CREATE_TOUR_TABLE)

        val CREATE_RESERVATION_TABLE = ("CREATE TABLE IF NOT EXISTS " + TABLE_RESERVATION + "("
                + RESERVATION_ID + " TEXT PRIMARY KEY," + ATTENDEE_NAME + " TEXT,"
                + RESERVATION_DATE + " TEXT," + TOUR_TITLE + " TEXT" +")")
        db?.execSQL(CREATE_RESERVATION_TABLE)

        val CREATE_ACTIVE_TABLE = ("CREATE TABLE IF NOT EXISTS " + TABLE_CURRENT + "("
                + CLIENT_NAME + " TEXT PRIMARY KEY," + CLIENT_EMAIL + " TEXT"+")")
        db?.execSQL(CREATE_ACTIVE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS)
        onCreate(db)
    }


    //method to insert data
    fun addEmployee(emp: EmpModelClass):Long{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, emp.userId)
        contentValues.put(KEY_NAME, emp.userName) // EmpModelClass Name
        contentValues.put(KEY_EMAIL,emp.userEmail ) // EmpModelClass Phone
        contentValues.put(KEY_PASSWORD,emp.userPassword)
        // Inserting Row
        val success = db.insert(TABLE_CONTACTS, null, contentValues)
        //2nd argument is String containing nullColumnHack
        db.close() // Closing database connection
        return success
    }

    @SuppressLint("Range")
    fun viewEmployee(email:String,password:String):Boolean{
        var found = ArrayList<String>()
        val selectQuery = "SELECT  * FROM $TABLE_CONTACTS WHERE $KEY_EMAIL = '$email' AND $KEY_PASSWORD='$password'"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try{
            cursor = db.rawQuery(selectQuery, null)
        }catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return false
        }
        var userId: Long
        var userName: String
        var userEmail: String
        if (cursor.moveToFirst()) {
            do {
                userId = cursor.getLong(cursor.getColumnIndex("id"))
                userName = cursor.getString(cursor.getColumnIndex("name"))
                userEmail = cursor.getString(cursor.getColumnIndex("email"))
               found.add(userEmail)
            } while (cursor.moveToNext())
        }

        return found.size>0
    }

    //method to read data
    @SuppressLint("Range")
    fun viewEmployees():List<EmpModelClass>{
        val empList:ArrayList<EmpModelClass> = ArrayList<EmpModelClass>()
        val selectQuery = "SELECT  * FROM $TABLE_CONTACTS"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try{
            cursor = db.rawQuery(selectQuery, null)
        }catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }
        var userId: Long
        var userName: String
        var userEmail: String
        if (cursor.moveToFirst()) {
            do {
                userId = cursor.getLong(cursor.getColumnIndex("id"))
                userName = cursor.getString(cursor.getColumnIndex("name"))
                userEmail = cursor.getString(cursor.getColumnIndex("email"))
                val emp= EmpModelClass(userId = userId, userName = userName, userEmail = userEmail, userPassword = "")
                empList.add(emp)
            } while (cursor.moveToNext())
        }
        return empList
    }

    //method to insert data
    fun addBlog(emp: EmpBlogModelClass):Long{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(BLOG_TITLE, emp.blogTitle)
        contentValues.put(BLOG_TEXT, emp.blogContent) // EmpModelClass Name
        contentValues.put(BLOG_ID, emp.blogid)
        // Inserting Row
        val success = db.insert(TABLE_BLOG, null, contentValues)
        //2nd argument is String containing nullColumnHack
        db.close() // Closing database connection
        return success
    }



    @SuppressLint("Range")
    fun viewBlogs():ArrayList<EmpBlogModelClass>{
        val BlogList = ArrayList<EmpBlogModelClass>()
        val selectQuery = "SELECT  * FROM $TABLE_BLOG"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try{
            cursor = db.rawQuery(selectQuery, null)
        }catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }
        var blogId: Long
        var BlogTitle: String
        var blogContent: String
        if (cursor.moveToFirst()) {
            do {
                blogId = cursor.getLong(cursor.getColumnIndex(BLOG_ID))
                BlogTitle = cursor.getString(cursor.getColumnIndex(BLOG_TITLE))
                blogContent = cursor.getString(cursor.getColumnIndex(BLOG_TEXT))
                val emp= EmpBlogModelClass(blogid = blogId, blogTitle = BlogTitle, blogContent = blogContent)
                BlogList.add(emp)
            } while (cursor.moveToNext())
        }
        return BlogList
    }

    @SuppressLint("Range")
    fun viewBlog(blog_id:String):EmpBlogModelClass{
        var Blog :EmpBlogModelClass = EmpBlogModelClass(0,"","")
        val selectQuery = "SELECT  * FROM $TABLE_BLOG WHERE $BLOG_TITLE='$blog_id'"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try{
            cursor = db.rawQuery(selectQuery, null)
        }catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return EmpBlogModelClass(0,"","")
        }
        var blogId: Long
        var BlogTitle: String
        var blogContent: String
        if (cursor.moveToFirst()) {
            do {
                blogId = cursor.getLong(cursor.getColumnIndex(BLOG_ID))
                BlogTitle = cursor.getString(cursor.getColumnIndex(BLOG_TITLE))
                blogContent = cursor.getString(cursor.getColumnIndex(BLOG_TEXT))
                Blog= EmpBlogModelClass(blogid = blogId, blogTitle = BlogTitle, blogContent = blogContent)
            } while (cursor.moveToNext())
        }
        return Blog
    }
    //method to update data
    fun updateEmployee(emp: EmpModelClass):Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, emp.userId)
        contentValues.put(KEY_NAME, emp.userName) // EmpModelClass Name
        contentValues.put(KEY_EMAIL,emp.userEmail ) // EmpModelClass Email

        // Updating Row
        val success = db.update(TABLE_CONTACTS, contentValues,"id="+emp.userId,null)
        //2nd argument is String containing nullColumnHack
        db.close() // Closing database connection
        return success
    }
    //method to delete data
    fun deleteEmployee(emp: EmpModelClass):Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, emp.userId) // EmpModelClass UserId
        // Deleting Row
        val success = db.delete(TABLE_CONTACTS,"id="+emp.userId,null)
        //2nd argument is String containing nullColumnHack
        db.close() // Closing database connection
        return success
    }

    @SuppressLint("Range")
    fun viewComments(blogid:Long):ArrayList<EmpCommentModel>{
        val CommentList = ArrayList<EmpCommentModel>()
        val selectQuery = "SELECT  * FROM $TABLE_COMMENT WHERE $BLOG_ID = $blogid"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try{
            cursor = db.rawQuery(selectQuery, null)
        }catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }
        var BlogTitle: String
        var blogContent: String
        if (cursor.moveToFirst()) {
            do {
                BlogTitle = cursor.getString(cursor.getColumnIndex(COMMENT_OWNER))
                blogContent = cursor.getString(cursor.getColumnIndex(COMMENT_TEXT))
                val emp= EmpCommentModel(blogid = 0, commentowner = BlogTitle, commentContent = blogContent, commentid = 0)
                CommentList.add(emp)
            } while (cursor.moveToNext())
        }
        return CommentList
    }

    @SuppressLint("Range")
    fun addComment(blog_id:Long,comment:String) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(BLOG_ID, blog_id)
        contentValues.put(COMMENT_OWNER, "") // EmpModelClass Name
        contentValues.put(COMMENT_TEXT, comment)
        contentValues.put(COMMENT_ID, Date().time)
        // Inserting Row
        db.insert(TABLE_COMMENT, null, contentValues)
        //2nd argument is String containing nullColumnHack
        db.close() // Closing database connection
    }

    @SuppressLint("Range")
    fun addTourPost(title:String,description:String,imageuri:String,time:String) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(TOUR_TITLE, title)
        contentValues.put(TOUR_DESC, description) // EmpModelClass Name
        contentValues.put(IMAGE_URI, imageuri)
        contentValues.put(EVENT_TIME, time)
        // Inserting Row
        db.insert(TABLE_TOUR, null, contentValues)
        //2nd argument is String containing nullColumnHack
        db.close() // Closing database connection
    }

    @SuppressLint("Range")
    fun makeresevation(owner:String,date:String,event_name:String) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(ATTENDEE_NAME, owner)
        contentValues.put(RESERVATION_DATE, date) // EmpModelClass Name
        contentValues.put(RESERVATION_ID, Date().time.toString())
        contentValues.put(TOUR_TITLE, event_name)
        // Inserting Row
        db.insert(TABLE_RESERVATION, null, contentValues)
        //2nd argument is String containing nullColumnHack
        db.close() // Closing database connection
    }

    @SuppressLint("Range")
    fun viewReservations():ArrayList<EmpReservationsModelClass>{
        val CommentList = ArrayList<EmpReservationsModelClass>()
        val selectQuery = "SELECT  * FROM $TABLE_RESERVATION"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try{
            cursor = db.rawQuery(selectQuery, null)
        }catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }
        var res_id: String
        var attendee: String
        var res_date: String
        var event_title: String
        if (cursor.moveToFirst()) {
            do {
                res_id = cursor.getString(cursor.getColumnIndex(RESERVATION_ID))
                attendee = cursor.getString(cursor.getColumnIndex(ATTENDEE_NAME))
                res_date = cursor.getString(cursor.getColumnIndex(RESERVATION_DATE))
                event_title = cursor.getString(cursor.getColumnIndex(TOUR_TITLE))
                val emp= EmpReservationsModelClass(reservation_id = res_id, attendee = attendee, reservation_date = res_date, event_title = event_title)
                CommentList.add(emp)
            } while (cursor.moveToNext())
        }
        return CommentList
    }

    @SuppressLint("Range")
    fun viewTours():ArrayList<TourModelClass>{
        val CommentList = ArrayList<TourModelClass>()
        val selectQuery = "SELECT  * FROM $TABLE_TOUR"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try{
            cursor = db.rawQuery(selectQuery, null)
        }catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }
        var event_title: String
        var event_desc: String
        var event_date: String
        var event_img: String
        if (cursor.moveToFirst()) {
            do {
                event_img = cursor.getString(cursor.getColumnIndex(IMAGE_URI))
                event_desc = cursor.getString(cursor.getColumnIndex(TOUR_DESC))
                event_date = cursor.getString(cursor.getColumnIndex(EVENT_TIME))
                event_title = cursor.getString(cursor.getColumnIndex(TOUR_TITLE))
                val emp= TourModelClass(eventtitle = event_title, eventtime = event_date, eventdesc = event_desc, eventimg = event_img)
                CommentList.add(emp)
            } while (cursor.moveToNext())
        }
        return CommentList
    }

    @SuppressLint("Range")
    fun createcurrent(name:String,email:String) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(CLIENT_NAME, name)
        contentValues.put(CLIENT_EMAIL, email) // EmpModelClass Name
        // Inserting Row
        db.insert(TABLE_CURRENT, null, contentValues)
        //2nd argument is String containing nullColumnHack
        db.close() // Closing database connection
    }
    @SuppressLint("Range")
    fun getcurrent():String {
        var result:String=""
        val selectQuery = "SELECT  * FROM $TABLE_CURRENT"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try{
            cursor = db.rawQuery(selectQuery, null)
        }catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return result
        }
        var email: String
        if (cursor.moveToFirst()) {
            do {
                email = cursor.getString(cursor.getColumnIndex(CLIENT_EMAIL))
                result=email
            } while (cursor.moveToNext())
        }

        return result
    }

    @SuppressLint("Range")
    fun delcurrent() {
        val selectQuery = "DELETE FROM $TABLE_CURRENT"
        val db = this.readableDatabase
        db.execSQL(selectQuery)
        db.close()
    }




}