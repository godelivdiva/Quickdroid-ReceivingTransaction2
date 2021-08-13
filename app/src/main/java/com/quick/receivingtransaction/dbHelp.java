package com.quick.receivingtransaction;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Admin on 11/4/2017.
 */

public class dbHelp extends SQLiteOpenHelper {
    String mQuery, mQuery2, mQuer3;

    static final String TABLE_DATA_AWAL         = "tb_data_awal";
    final String _id	                        = "_id";
    final String AWAL_C1_RECEIPT_NUM            = "RECEIPT_NUM";
    final String AWAL_C2_ITEM                   = "ITEM";
    final String AWAL_C3_ITEM_DESC	            = "ITEM_DESC";
    final String AWAL_C4_REQUESTOR	            = "REQUESTOR";
    final String AWAL_C5_QTY                    = "QTY";
    final String AWAL_C6_UOM	                = "UOM";
    final String AWAL_C7_STATUS_BARANG          = "STATUS_BARANG";
    final String AWAL_C8_LOCATION               = "LOCATION";
    final String AWAL_C9_COMMENTS               = "COMMENTS ";
    final String AWAL_C10_FLAG                  = "FLAG";
    final String AWAL_C11_PO_NUMBER             = "PO_NUMBER";

    static final String TABLE_DATA_RCV                 = "tb_data_rcv";
    final String _id2	                               = "_id";
    final String RCV_C1_PRIMARY_UNIT_OF_MEASURE        = "PRIMARY_UNIT_OF_MEASURE";
    final String RCV_C2_PO_HEADER_ID                   = "PO_HEADER_ID";
    final String RCV_C3_PO_LINE_ID	                   = "PO_LINE_ID";
    final String RCV_C4_LINE_LOCATION_ID	           = "LINE_LOCATION_ID";
    final String RCV_C5_ORGANIZATION_CODE              = "ORGANIZATION_CODE";
    final String RCV_C6_SEGMENT1	                   = "SEGMENT1";
    final String RCV_C7_LOCATION_CODE                  = "LOCATION_CODE";
    final String RCV_C8_ORGANIZATION_ID                = "ORGANIZATION_ID";
    final String RCV_C9_CLOSED_CODE                    = "CLOSED_CODE";
    final String RCV_C10_QUANTITY_ORDERED              = "QUANTITY_ORDERED";
    final String RCV_C11_QUANTITY_RECEIVED             = "QUANTITY_RECEIVED";
    final String RCV_C12_LINE_NUM                      = "LINE_NUM";
    final String RCV_C13_SHIPMENT_NUM                  = "SHIPMENT_NUM";
    final String RCV_C14_DESTINATION_ORGANIZATION_ID   = "DESTINATION_ORGANIZATION_ID";
    final String RCV_C15_ORG_ID                        = "ORG_ID";
    final String RCV_C16_INVENTORY_ITEM_ID             = "INVENTORY_ITEM_ID";
    final String RCV_C17_DESCRIPTION                   = "DESCRIPTION";
    final String RCV_C18_NOTE                          = "NOTE";
    final String RCV_C19_REQUESTOR                     = "REQUESTOR";
    final String RCV_C20_LOCATION_ID                          = "LOCATION_ID";
    final String RCV_C21_FLAG                   = "FLAG";
    final String RCV_C22_QTY_RECEIVE                   = "QTY_RECEIVE";

    static final String TABLE_DATA_RCV2                 = "tb_data_rcv2";
    final String _id3	                               = "_id";
    final String RCV_C1_receipt_num        = "receipt_num";
    final String RCV_C2_po_number                  = "po_number";
    final String RCV_C3_item	                   = "item";
    final String RCV_C4_item_desc	           = "item_desc";
    final String RCV_C5_requestor              = "requestor";
    final String RCV_C6_qty	                   = "qty";
    final String RCV_C7_uom                  = "uom";
    final String RCV_C8_tgl_dibuat                = "tgl_dibuat";
    final String RCV_C9_tgl_diterima                    = "tgl_diterima";
    final String RCV_C10_shippement              = "shippement";
    final String RCV_C11_status_barang            = "status_barang";
    final String RCV_C12_location                      = "location";
    final String RCV_C13_comments                  = "comments";


    public dbHelp(Context context){
        super(context,"db_data",null,5);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        mQuery = "CREATE TABLE "+TABLE_DATA_AWAL+" (" +
                _id                         +" INTEGER PRIMARY KEY," +
                AWAL_C1_RECEIPT_NUM         + " TEXT,"+
                AWAL_C2_ITEM	            + " TEXT,"+
                AWAL_C3_ITEM_DESC       	+ " TEXT,"+
                AWAL_C4_REQUESTOR	        + " TEXT,"+
                AWAL_C5_QTY	                + " TEXT,"+
                AWAL_C6_UOM                 + " TEXT,"+
                AWAL_C7_STATUS_BARANG       + " TEXT,"+
                AWAL_C8_LOCATION            + " TEXT,"+
                AWAL_C9_COMMENTS            + " TEXT,"+
                AWAL_C10_FLAG               + " TEXT,"+
                AWAL_C11_PO_NUMBER          + " TEXT"+
                ")";
        db.execSQL(mQuery);

        mQuery2 = "CREATE TABLE "+TABLE_DATA_RCV+" (" +
                _id2                                +" INTEGER PRIMARY KEY," +
                RCV_C1_PRIMARY_UNIT_OF_MEASURE      + " TEXT,"+
                RCV_C2_PO_HEADER_ID	                + " TEXT,"+
                RCV_C3_PO_LINE_ID       	        + " TEXT,"+
                RCV_C4_LINE_LOCATION_ID	            + " TEXT,"+
                RCV_C5_ORGANIZATION_CODE	        + " TEXT,"+
                RCV_C6_SEGMENT1                     + " TEXT,"+
                RCV_C7_LOCATION_CODE                + " TEXT,"+
                RCV_C8_ORGANIZATION_ID              + " TEXT,"+
                RCV_C9_CLOSED_CODE                  + " TEXT,"+
                RCV_C10_QUANTITY_ORDERED            + " TEXT,"+
                RCV_C11_QUANTITY_RECEIVED           + " TEXT,"+
                RCV_C12_LINE_NUM                    + " TEXT,"+
                RCV_C13_SHIPMENT_NUM                + " TEXT,"+
                RCV_C14_DESTINATION_ORGANIZATION_ID + " TEXT,"+
                RCV_C15_ORG_ID                      + " TEXT,"+
                RCV_C16_INVENTORY_ITEM_ID           + " TEXT,"+
                RCV_C17_DESCRIPTION           + " TEXT,"+
                RCV_C18_NOTE           + " TEXT,"+
                RCV_C19_REQUESTOR           + " TEXT,"+
                RCV_C20_LOCATION_ID                        + " TEXT,"+
                RCV_C21_FLAG                        + " TEXT,"+
                RCV_C22_QTY_RECEIVE                 + " TEXT"+
                ")";
        db.execSQL(mQuery2);

        mQuery2 = "CREATE TABLE "+TABLE_DATA_RCV2+" (" +
                _id3                                +" INTEGER PRIMARY KEY," +
                RCV_C1_receipt_num      + " TEXT,"+
                RCV_C2_po_number	                + " TEXT,"+
                RCV_C3_item       	        + " TEXT,"+
                RCV_C4_item_desc	            + " TEXT,"+
                RCV_C5_requestor	        + " TEXT,"+
                RCV_C6_qty                     + " TEXT,"+
                RCV_C7_uom                + " TEXT,"+
                RCV_C8_tgl_dibuat              + " TEXT,"+
                RCV_C9_tgl_diterima                  + " TEXT,"+
                RCV_C10_shippement            + " TEXT,"+
                RCV_C11_status_barang           + " TEXT,"+
                RCV_C12_location                    + " TEXT,"+
                RCV_C13_comments                + " TEXT"+
                ")";
        db.execSQL(mQuery2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("NEW",""+newVersion);
        Log.d("OLD",""+oldVersion);
        if(newVersion>oldVersion){
            db.execSQL("DROP TABLE "+TABLE_DATA_AWAL);
            db.execSQL("DROP TABLE "+TABLE_DATA_RCV);
            db.execSQL("DROP TABLE "+TABLE_DATA_RCV2);
            onCreate(db);
        }
    }

//    void insertDataAwal(ContentValues values){
//        SQLiteDatabase db = getWritableDatabase();
//        db.insert(TABLE_DATA_AWAL,null,values);
//        db.close();
//    }
    public void insertData(ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("Values", "" + values.toString());
        db.insert("tb_data_awal", null, values);
    }

    public void insertDataRcv(ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("Values", "" + values.toString());
        db.insert("tb_data_rcv", null, values);
    }

    public void insertDataRcv2(ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("Values", "" + values.toString());
        db.insert("tb_data_rcv2", null, values);
    }

    Cursor queryDataAwal(){
        SQLiteDatabase db = getWritableDatabase();
        mQuery = "SELECT * FROM "+TABLE_DATA_AWAL;
        return db.rawQuery(mQuery,null);
    }

    Cursor queryDataRcv(){
        SQLiteDatabase db = getWritableDatabase();
        mQuery = "SELECT * FROM "+TABLE_DATA_RCV;
        return db.rawQuery(mQuery,null);
    }

    Cursor queryDataRcv2(){
        SQLiteDatabase db = getWritableDatabase();
        mQuery = "SELECT * FROM "+TABLE_DATA_RCV2;
        return db.rawQuery(mQuery,null);
    }

    Cursor select() {
        SQLiteDatabase db = this.getWritableDatabase();
        mQuery = "SELECT * FROM tb_data_awal";
        Cursor c = db.rawQuery(mQuery, null);
        return c;
    }

    Cursor selectRcv() {
        SQLiteDatabase db = this.getWritableDatabase();
        mQuery = "SELECT * FROM tb_data_rcv";
        Cursor c = db.rawQuery(mQuery, null);
        return c;
    }

    Cursor selectRcv2() {
        SQLiteDatabase db = this.getWritableDatabase();
        mQuery = "SELECT * FROM tb_data_rcv2";
        Cursor c = db.rawQuery(mQuery, null);
        return c;
    }

    public void changeQty(String qty, String seq){
        SQLiteDatabase db = this.getWritableDatabase();
        mQuery = "UPDATE tb_data_awal SET QTY = '"+qty+"' WHERE RECEIPT_NUM = '"+seq+"'";
        db.execSQL(mQuery);
    }

    public void changeQtyRcv(String qty, String seq){
        SQLiteDatabase db = this.getWritableDatabase();
        mQuery = "UPDATE tb_data_rcv SET QTY = '"+qty+"' WHERE RECEIPT_NUM = '"+seq+"'";
        db.execSQL(mQuery);
    }

    void deleteDataAwal(){
        SQLiteDatabase db = getWritableDatabase();
        mQuery = "DELETE FROM "+TABLE_DATA_AWAL;
        db.execSQL(mQuery);
    }

    void deleteDataRcv(){
        SQLiteDatabase db = getWritableDatabase();
        mQuery = "DELETE FROM "+TABLE_DATA_RCV;
        db.execSQL(mQuery);
    }

    void deleteDataRcv2(){
        SQLiteDatabase db = getWritableDatabase();
        mQuery = "DELETE FROM "+TABLE_DATA_RCV2;
        db.execSQL(mQuery);
    }

    void updateFlag(String flag, String itemId, String location, String po_line, String line_location, String id){
        SQLiteDatabase db = getWritableDatabase();
        mQuery = "UPDATE "+TABLE_DATA_RCV+" " +
                "SET "+ RCV_C21_FLAG +" = '"+flag+"' " +
                "WHERE "+RCV_C6_SEGMENT1+" = '"+itemId+"'" +
                "AND "+RCV_C7_LOCATION_CODE+" = '"+location+"'"+
                "AND "+RCV_C3_PO_LINE_ID+" = '"+po_line+"'"+
                "AND "+RCV_C4_LINE_LOCATION_ID+" = '"+line_location+"'"+
                "AND "+_id2+" = '"+id+"'";
        Log.d("UPDATE", mQuery);
        db.execSQL(mQuery);
    }

    void updateFlagAll(String flag){
        SQLiteDatabase db = getWritableDatabase();
        mQuery = "UPDATE "+TABLE_DATA_RCV+" " +
                "SET "+ RCV_C21_FLAG +" = '"+flag+"' ";
        Log.d("UPDATE", mQuery);
        db.execSQL(mQuery);
    }

    void updateFlagUnSelect(String flag){
        SQLiteDatabase db = getWritableDatabase();
        mQuery = "UPDATE "+TABLE_DATA_RCV+" " +
                "SET "+ RCV_C21_FLAG +" = '"+flag+"' ";
        Log.d("UPDATE", mQuery);
        db.execSQL(mQuery);
    }

    void inputQtyRcv(String qty, String itemId, String location, String po_line, String line_location, String id){
        SQLiteDatabase db = getWritableDatabase();
        mQuery = "UPDATE "+TABLE_DATA_RCV+" " +
                "SET "+ RCV_C22_QTY_RECEIVE +" = '"+qty+"' " +
                "WHERE "+RCV_C6_SEGMENT1+" = '"+itemId+"'"+
                "AND "+RCV_C7_LOCATION_CODE+" = '"+location+"'"+
                "AND "+RCV_C3_PO_LINE_ID+" = '"+po_line+"'"+
                "AND "+RCV_C4_LINE_LOCATION_ID+" = '"+line_location+"'"+
                "AND "+_id2+" = '"+id+"'";
        Log.d("UPDATE", mQuery);
        db.execSQL(mQuery);
    }

    Cursor queryDataToTransact(){
        SQLiteDatabase db = getWritableDatabase();
        System.out.println("SELECT PO_HEADER_ID, INVENTORY_ITEM_ID, PO_LINE_ID " +
                "FROM tb_data_rcv WHERE flag = 'Sudah Centang' ");
        mQuery = "SELECT PO_HEADER_ID, INVENTORY_ITEM_ID, PO_LINE_ID, QTY_RECEIVE " +
                "FROM tb_data_rcv WHERE flag = 'Sudah Centang' ";
        return db.rawQuery(mQuery,null);
    }
}
