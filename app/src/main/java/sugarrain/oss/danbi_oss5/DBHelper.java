package sugarrain.oss.danbi_oss5;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

class DBHelper extends SQLiteOpenHelper {
    public static String dbName = "Memo.db";
    public static int dbVersion = 2;

    String _TABLENAME0 = "MEMO";

    Context context;
    String name;
    int version;
    SQLiteDatabase.CursorFactory factory;

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
        this.name = name;
        this.factory = factory;
        this.version = version;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuffer sb = new StringBuffer();
        sb.append(String.format(" CREATE TABLE %s ( ", _TABLENAME0));
        sb.append(" _ID INTEGER PRIMARY KEY AUTOINCREMENT, ");
        sb.append(" DATE INTEGER, ");
        sb.append(" MEMO TEXT )");

        db.execSQL(sb.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(String.format("DROP TABLE IF EXISTS %s", _TABLENAME0));
        onCreate(db);
    }

    public void addData(MemoData data) {
        SQLiteDatabase db = getWritableDatabase();

        StringBuffer sb = new StringBuffer();
        sb.append(String.format(" INSERT INTO %s ( ", _TABLENAME0));
        sb.append(" DATE, MEMO )");
        sb.append(" VALUES ( ?, ? )");

        db.execSQL(
                sb.toString(),
                new Object[] {
                        data.getTime(),
                        data.getMemo()
                }
        );
    }

    public void deleteData(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(String.format(" DELETE FROM %s WHERE _ID = %d", _TABLENAME0, id));
    }

    public MemoData getDataById(int id) {
        StringBuffer sb = new StringBuffer();
        sb.append(String.format(" SELECT _ID, DATE, MEMO FROM %s WHERE _ID = %d", _TABLENAME0, id));

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(sb.toString(), null);

        MemoData data = new MemoData();
        while (cursor.moveToNext()) {
            data.setId(cursor.getInt(0));
            data.setTime(cursor.getLong(1));
            data.setMemo(cursor.getString(2));
        }

        return data;
    }

    public List<MemoData> getAllData() {
        StringBuffer sb = new StringBuffer();
        sb.append(String.format(" SELECT _ID, DATE, MEMO FROM %s ", _TABLENAME0));

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(sb.toString(), null);

        List<MemoData> datas = new ArrayList<>();
        while (cursor.moveToNext()) {
            MemoData data = new MemoData();
            data.setId(cursor.getInt(0));
            data.setTime(cursor.getLong(1));
            data.setMemo(cursor.getString(2));
            datas.add(data);
        }

        return datas;
    }
}
