package com.example.csitgis.lab6_sqllite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper{
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "student_dp";
    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCrete(SQLiteDatabase dp){
        dp.execSQL(Student.CREATE_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase dp, int i, int il){
        dp.execSQL("DROP TABLE IF EXISTS " + Student.TABLE_NAME);
        onCrete(dp);
    }
    public void insertStudent(String id, String student){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Student.COLUMN_ID, id);
        values.put(Student.COLUMN_NAME, student);

        db.insert(Student.TABLE_NAME, null, values);
        db.close();
    }

    public Student getStudent(String id){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Student.TABLE_NAME,
                new String[]{Student.COLUMN_ID, Student.COLUMN_NAME},
                Student.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
    if (cursor !=null){
        cursor.moveToFirst();
    }

    Student student = new Student(
            cursor.getString(cursor.getColumnIndex(Student.COLUMN_ID)),
            cursor.getString(cursor.getColumnIndex(Student.COLUMN_NAME)));
    cursor.close();
    return student;
    }
    public List<Student> getAllStudent(){
        List<Student> students = new ArrayList<>();
        String selectQuery = "SELECT * FROM" + Student.TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()){
            do {
                Student student = new Student();
                Student.setId(cursor.getString(cursor.getColumnIndex(Student.COLUMN_ID)));
                Student.setName(cursor.getString(cursor.getColumnIndex(Student.COLUMN_NAME)));
                Student.add(student);
            }while (cursor.moveToNext());
        }
        db.close();
        return students;
    }
    public int updateStudent(Student student){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Student.COLUMN_ID, student.getId());
        values.put(Student.COLUMN_NAME, student.getName());

        return db.update(Student.TABLE_NAME, values, Student.COLUMN_ID + " =?",
                new String[]{String.valueOf(student.getId())});
    }
    public  void deleteStudent(Student student) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Student.TABLE_NAME, Student.COLUMN_ID + " =? ",
                new String[]{String.valueOf(student.getId())});
        db.close();
    }
}

