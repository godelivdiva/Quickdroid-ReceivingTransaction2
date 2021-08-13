package com.quick.receivingtransaction;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.sql.Connection;

public class CursorAdapterRcv extends android.widget.CursorAdapter {
    int urutan = 1;
    String nilaiAtt;
    Connection mCon;
    dbHelp helper;
    Connection mConPostgre;

    public CursorAdapterRcv(Context context, Cursor cursor) {
        super(context, cursor, 0);
        mCon = new ManagerSessionUserOracle(context).connectDb();

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        //Inflate view
        return LayoutInflater.from(context).inflate(R.layout.row_list_item_rcv, viewGroup, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
//        int att = Integer.parseInt(getNilaiAtt());
        //set data
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.activity_dialog_rcv);

        TextView tv_urut = (TextView) view.findViewById(R.id.tv_urut);

        TextView ItemCode = (TextView) view.findViewById(R.id.ItemCode);
        TextView QtyOrder = (TextView) view.findViewById(R.id.QtyOrder);
        TextView QtyReceive = (TextView) view.findViewById(R.id.QtyReceive);
        TextView Uom = (TextView) view.findViewById(R.id.uom);
        TextView Location = (TextView) view.findViewById(R.id.Location);
        //TextView Comment = (TextView) view.findViewById(R.id.Comment);

        TextView tv_ItemCode = (TextView) view.findViewById(R.id.tv_ItemCode);
        TextView tv_QtyOrder = (TextView) view.findViewById(R.id.tv_QtyOrder);
        TextView tv_QtyReceive = (TextView) view.findViewById(R.id.tv_QtyReceive);
        TextView tv_uom = (TextView) view.findViewById(R.id.tv_uom);
        TextView tv_Location = (TextView) view.findViewById(R.id.tv_Location);
        //TextView tv_Comment = (TextView) view.findViewById(R.id.tv_Comment);


        CardView linear = (CardView) view.findViewById(R.id.cv_data);

        final EditText et_qty = (EditText) view.findViewById(R.id.et_qty);


        final String SEGMENT1 = cursor.getString(cursor.getColumnIndexOrThrow("SEGMENT1"));
        String QUANTITY_ORDERED = cursor.getString(cursor.getColumnIndexOrThrow("QUANTITY_ORDERED"));
        String QUANTITY_RECEIVED = cursor.getString(cursor.getColumnIndexOrThrow("QUANTITY_RECEIVED"));
        String UOM = cursor.getString(cursor.getColumnIndexOrThrow("PRIMARY_UNIT_OF_MEASURE"));
        String LOCATION_CODE = cursor.getString(cursor.getColumnIndexOrThrow("LOCATION_CODE"));
        //String LOCATION = cursor.getString(cursor.getColumnIndexOrThrow("LOCATION"));
        //String COMMENTS = cursor.getString(cursor.getColumnIndexOrThrow("COMMENTS"));

        tv_urut.setText("" + (cursor.getPosition() + 1));

//        ItemCode.setText("Item Code        : ");
//        QtyOrder.setText("Qty Ordered     : ");
//        QtyReceive.setText("Qty Received   : ");
//        Uom.setText("UOM                 : ");
//        Location.setText("Location          : ");
        //Comment.setText("Comments       : ");


        tv_ItemCode.setText(SEGMENT1);
        tv_QtyOrder.setText(QUANTITY_ORDERED);
        tv_QtyReceive.setText(QUANTITY_RECEIVED);
        tv_uom.setText(UOM);
        tv_Location.setText(LOCATION_CODE);
        //tv_Comment.setText(COMMENTS);
    }
}
