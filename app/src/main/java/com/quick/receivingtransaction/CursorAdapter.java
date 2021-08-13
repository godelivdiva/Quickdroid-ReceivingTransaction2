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

/**
 * Created by aldi on 10/27/17.
 */

public class CursorAdapter extends android.widget.CursorAdapter {
    int urutan = 1;
    String nilaiAtt;
    Connection mCon;
    dbHelp helper;
    Connection mConPostgre;

    public CursorAdapter(Context context, Cursor cursor){
        super(context,cursor,0);
        mCon = new ManagerSessionUserOracle(context).connectDb();

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        //Inflate view
        return LayoutInflater.from(context).inflate(R.layout.row_list_item,viewGroup,false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
//        int att = Integer.parseInt(getNilaiAtt());
        //set data
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.activity_dialog);

        TextView ItemCode = (TextView) view.findViewById(R.id.ItemCode);
        TextView ItemDesc = (TextView) view.findViewById(R.id.ItemDesc);
        TextView Requestor = (TextView) view.findViewById(R.id.Requestor);
        TextView QtyReceive = (TextView) view.findViewById(R.id.QtyReceive);
        TextView StatusBarang = (TextView) view.findViewById(R.id.StatusBarang);
        TextView Location = (TextView) view.findViewById(R.id.Location);
        TextView Comment = (TextView) view.findViewById(R.id.Comment);

        TextView tv_ItemCode = (TextView) view.findViewById(R.id.tv_ItemCode);
        TextView tv_ItemDesc = (TextView) view.findViewById(R.id.tv_ItemDesc);
        TextView tv_Requestor = (TextView) view.findViewById(R.id.tv_Requestor);
        TextView tv_QtyReceive = (TextView) view.findViewById(R.id.tv_QtyReceive);
        TextView tv_StatusBarang = (TextView) view.findViewById(R.id.tv_StatusBarang);
        TextView tv_Location = (TextView) view.findViewById(R.id.tv_Location);
        TextView tv_Comment = (TextView) view.findViewById(R.id.tv_Comment);
        TextView tv_urut = (TextView) view.findViewById(R.id.tv_urut);

        CardView linear = (CardView) view.findViewById(R.id.cv_data);

        final EditText et_qty = (EditText) view.findViewById(R.id.et_qty);


        final String ITEM_CODE = cursor.getString(cursor.getColumnIndexOrThrow("ITEM"));
        String ITEM_DESC = cursor.getString(cursor.getColumnIndexOrThrow("ITEM_DESC"));
        String TRANSACT_QTY = cursor.getString(cursor.getColumnIndexOrThrow("QTY"));
        String REQUESTOR = cursor.getString(cursor.getColumnIndexOrThrow("REQUESTOR"));
        String STATUS_BARANG = cursor.getString(cursor.getColumnIndexOrThrow("STATUS_BARANG"));
        String LOCATION = cursor.getString(cursor.getColumnIndexOrThrow("LOCATION"));
        String COMMENTS = cursor.getString(cursor.getColumnIndexOrThrow("COMMENTS"));

        tv_urut.setText(""+(cursor.getPosition()+1));

        ItemCode.setText("Item Cod          : ");
        ItemDesc.setText("Item Desc       : ");
        Requestor.setText("Requestor       : ");
        QtyReceive.setText("Quantity            : ");
        StatusBarang.setText("Status Barang : ");
        Location.setText("Location          : ");
        Comment.setText("Comments      : ");


        tv_ItemCode.setText(ITEM_CODE);
        tv_ItemDesc.setText(ITEM_DESC);
        tv_Requestor.setText(REQUESTOR);
        tv_StatusBarang.setText(STATUS_BARANG);
        tv_Location.setText(LOCATION);
        tv_Comment.setText(COMMENTS);
        tv_QtyReceive.setText(TRANSACT_QTY);

//        Button bt_dialog = (Button) dialog.findViewById(R.id.bt_dialog);
//        bt_dialog.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                System.out.print("OK BUTTON");
//                String newQty=et_qty.getText().toString();
//                String seq=ITEM_CODE;
//                if (newQty.length()>0){
//                    helper.changeQty(newQty,seq);
//                    ((TransferActivity)context).refreshData();
//                    dialog.dismiss();
//                }else {
//                    et_qty.setError("Harus diisi!!!");
//                }
//            }
//        });

    }


}
