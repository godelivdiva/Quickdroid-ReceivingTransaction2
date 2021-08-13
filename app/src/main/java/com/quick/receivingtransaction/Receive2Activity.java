package com.quick.receivingtransaction;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.stetho.Stetho;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Receive2Activity extends AppCompatActivity {
    String mQuery, PO, SHIPMENT;
    ImageView refresh, home;
    TextView ReceiptNum, ShipmentNum, PoNumber, Item, ItemDesc, Requestor, Qty, Uom, TglDibuat, TglDiterima, Shippement, StatusBarang, Location, Comment;

    Connection mConn;
    dbHelp helper;
    ManagerSessionUserOracle session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive2);

        ReceiptNum = (TextView) findViewById(R.id.tv_ReceiptNum);
        PoNumber = (TextView) findViewById(R.id.tv_PoNumber);
        ShipmentNum = (TextView) findViewById(R.id.tv_ShipmentNumber);
        //Item = (TextView) findViewById(R.id.tv_Item);
        ItemDesc = (TextView) findViewById(R.id.tv_ItemDesc);
        Requestor = (TextView) findViewById(R.id.tv_Requestor);
//        Qty = (TextView) findViewById(R.id.tv_Qty);
//        Uom = (TextView) findViewById(R.id.tv_Uom);
//        TglDibuat = (TextView) findViewById(R.id.tv_TglDibuat);
//        TglDiterima = (TextView) findViewById(R.id.tv_TglDiterima);
//        Shippement = (TextView) findViewById(R.id.tv_Shippement);
        StatusBarang = (TextView) findViewById(R.id.tv_StatusBarang);
        Location = (TextView) findViewById(R.id.tv_Location);
        Comment = (TextView) findViewById(R.id.tv_Comment);

        refresh = (ImageView) findViewById(R.id.refresh);
        home = (ImageView) findViewById(R.id.home);

        new ModuleTool().allowNetworkOnMainThread();
        helper = new dbHelp(this);
        session = new ManagerSessionUserOracle(this);
        Stetho.initializeWithDefaults(this);
        mConn = session.connectDb();

        PO = getIntent().getStringExtra("PO");
        SHIPMENT = getIntent().getStringExtra("SHIPMENT");

        PoNumber.setText(""+PO);
        ShipmentNum.setText(""+SHIPMENT);
        System.out.println("PO:"+PO+"-SHIP:"+SHIPMENT);
        //insertDb2(PO, SHIPMENT);
        //setText();

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper.deleteDataRcv2();
                //insertDb2(PO, SHIPMENT);
                //showMessage();
                setText();
                Toast.makeText(Receive2Activity.this, "Refreshed", Toast.LENGTH_LONG).show();
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper.deleteDataRcv2();
                //insertDb2(PO, SHIPMENT);
                //showMessage();
                Intent i = new Intent(Receive2Activity.this, MenuActivity.class);
                startActivity(i);
            }
        });
    }

    public void setText() {
        Cursor c = helper.selectRcv2();

        ReceiptNum.setText(""+getShipment());
        PoNumber.setText(""+PO);
        ShipmentNum.setText(""+SHIPMENT);
//        Item.setText(c.getString(c.getColumnIndexOrThrow("item")));
//        ItemDesc.setText(c.getString(c.getColumnIndexOrThrow("item_desc")));
//        Requestor.setText(c.getString(c.getColumnIndexOrThrow("requestor")));
//        Qty.setText(c.getString(c.getColumnIndexOrThrow("qty")));
//        Uom.setText(c.getString(c.getColumnIndexOrThrow("uom")));
//        TglDibuat.setText(c.getString(c.getColumnIndexOrThrow("tgl_dibuat")));
//        TglDiterima.setText(c.getString(c.getColumnIndexOrThrow("tgl_diterima")));
//        Shippement.setText(c.getString(c.getColumnIndexOrThrow("shippement")));
//        StatusBarang.setText(c.getString(c.getColumnIndexOrThrow("status_barang")));
//        Location.setText(c.getString(c.getColumnIndexOrThrow("location")));
//        Comment.setText(c.getString(c.getColumnIndexOrThrow("comments")));
    }

    void insertDb2(String code, String shipment) {
        Log.d("Conn", "." + mConn);
        if (mConn == null) {
            Toast.makeText(this, "No Connection", Toast.LENGTH_SHORT).show();
            return;
        }
        String ReceiptNum = code;
        try {
            Statement statement = mConn.createStatement();
            mQuery = "SELECT DISTINCT \n" +
                    "       rsh.RECEIPT_NUM                                         receipt_num\n" +
                    "      ,pha.SEGMENT1                                            po_number\n" +
                    "      ,msib.SEGMENT1                                           item\n" +
                    "      ,msib.DESCRIPTION                                        item_desc\n" +
                    "      ,ppf2.FULL_NAME                                          requestor\n" +
                    "      ,rcvs.QUANTITY                                            qty\n" +
                    "      ,rt.UNIT_OF_MEASURE                                   uom\n" +
                    "      ,rt.CREATION_DATE                                      tgl_dibuat\n" +
                    "      ,rt.TRANSACTION_DATE                                tgl_diterima\n" +
                    "      ,rsh.SHIPMENT_NUM                                      shippement\n" +
                    "      ,CASE plc.displayed_field||' - '||rrh.routing_name\n" +
                    "            WHEN 'Accepted - Inspection Required'\n" +
                    "            THEN 'OK QC'\n" +
                    "            WHEN 'Not Inspected - Inspection Required'\n" +
                    "            THEN 'BLM QC'\n" +
                    "            WHEN 'Not Inspected - Standard Receipt'\n" +
                    "            THEN 'NON QC'\n" +
                    "            WHEN 'Rejected - Inspection Required'\n" +
                    "            THEN 'NOT OK QC'\n" +
                    "       END                                                            status_barang\n" +
                    "      ,hlat.LOCATION_CODE                                   location\n" +
                    "      ,decode(rt.COMMENTS,null,'kosong',rt.COMMENTS)  comments\n" +
                    "FROM rcv_transactions rt\n" +
                    "    ,rcv_supply rcvs\n" +
                    "    ,rcv_shipment_headers rsh\n" +
                    "    ,rcv_shipment_lines rsl\n" +
                    "    ,rcv_routing_headers rrh\n" +
                    "    ,mtl_system_items_b msib\n" +
                    "    --\n" +
                    "    ,per_people_f ppf2\n" +
                    "    ,fnd_lookup_values flv\n" +
                    "    ,po_lookup_codes plc\n" +
                    "    --\n" +
                    "    ,po_headers_all pha\n" +
                    "    ,po_requisition_lines_all prla\n" +
                    "    ,po_req_distributions_all prda\n" +
                    "    ,po_distributions_all pda\n" +
                    "    --\n" +
                    "    ,org_organization_definitions ood\n" +
                    "    ,hr_locations_all_tl hlat\n" +
                    "WHERE rt.ORGANIZATION_ID = rcvs.TO_ORGANIZATION_ID\n" +
                    "  AND rt.SHIPMENT_HEADER_ID = rsh.SHIPMENT_HEADER_ID\n" +
                    "  AND rt.SHIPMENT_LINE_ID = rsl.SHIPMENT_LINE_ID\n" +
                    "  AND rt.TRANSACTION_ID = rcvs.RCV_TRANSACTION_ID\n" +
                    "  AND rt.ROUTING_HEADER_ID = rrh.ROUTING_HEADER_ID\n" +
                    "  AND rcvs.SHIPMENT_HEADER_ID = rsh.SHIPMENT_HEADER_ID\n" +
                    "  AND rcvs.SHIPMENT_LINE_ID = rsl.SHIPMENT_LINE_ID\n" +
                    "  AND rsl.SHIPMENT_HEADER_ID = rsh.SHIPMENT_HEADER_ID\n" +
                    "  --\n" +
                    "  AND msib.INVENTORY_ITEM_ID = rcvs.ITEM_ID\n" +
                    "  AND msib.ORGANIZATION_ID = rcvs.TO_ORGANIZATION_ID\n" +
                    "  --\n" +
                    "  AND pha.PO_HEADER_ID = rcvs.PO_HEADER_ID\n" +
                    "  AND pha.PO_HEADER_ID = pda.PO_HEADER_ID\n" +
                    "  AND pda.REQ_DISTRIBUTION_ID = prda.DISTRIBUTION_ID\n" +
                    "  AND prda.REQUISITION_LINE_ID = prla.REQUISITION_LINE_ID\n" +
                    "  AND prla.TO_PERSON_ID = ppf2.PERSON_ID\n" +
                    "  --\n" +
                    "  AND hlat.LOCATION_ID(+) = rt.LOCATION_ID\n" +
                    "  AND plc.LOOKUP_CODE = rt.INSPECTION_STATUS_CODE\n" +
                    "  AND plc.LOOKUP_TYPE = 'INSPECTION STATUS'\n" +
                    "  AND TO_NUMBER(flv.LOOKUP_CODE(+)) = rt.ROUTING_HEADER_ID\n" +
                    "  AND flv.LOOKUP_TYPE = 'RCV_ROUTING_HEADERS'\n" +
                    "  AND flv.MEANING in ('Inspection Required', 'Standard Receipt')\n" +
                    "  AND rt.ORGANIZATION_ID = ood.ORGANIZATION_ID\n" +
                    "  AND rcvs.TO_ORGANIZATION_ID = ood.ORGANIZATION_ID\n" +
                    "  AND rsh.RECEIPT_NUM = nvl((select rsh.RECEIPT_NUM\n" +
                    "                               from rcv_shipment_lines rsl\n" +
                    "                                   ,rcv_shipment_headers rsh\n" +
                    "                                   ,po_headers_all pha\n" +
                    "                              where pha.SEGMENT1 = '"+ReceiptNum+"' --- nomorPO\n" +
                    "                                and rsh.SHIPMENT_NUM = nvl('"+shipment+"',rsh.SHIPMENT_NUM) --- shipmentnumber\n" +
                    "                                and rsl.PO_HEADER_ID = pha.PO_HEADER_ID\n" +
                    "                                and rsl.SHIPMENT_HEADER_ID = rsh.SHIPMENT_HEADER_ID\n" +
                    "                                and rsh.CREATION_DATE = (select max(rsh.CREATION_DATE)\n" +
                    "                                                           from rcv_shipment_headers rsh\n" +
                    "                                                               ,rcv_shipment_lines rsl\n" +
                    "                                                          where rsh.SHIPMENT_HEADER_ID = rsl.SHIPMENT_HEADER_ID\n" +
                    "                                                            and rsl.PO_HEADER_ID = pha.PO_HEADER_ID)\n" +
                    "                                    ),rsh.RECEIPT_NUM)    \n" +
                    "  AND rt.DESTINATION_TYPE_CODE = 'RECEIVING'\n" +
                    "  AND rt.TRANSACTION_TYPE = nvl('RECEIVE',rt.TRANSACTION_TYPE)\n" +
                    "  AND (\n" +
                    "       rcvs.shipment_line_id NOT IN\n" +
                    "       (SELECT shipment_line_id\n" +
                    "          FROM rcv_transactions \n" +
                    "         WHERE destination_type_code = 'INVENTORY'\n" +
                    "        )\n" +
                    "   OR rsh.shipment_header_id IN\n" +
                    "         (SELECT rcvt.shipment_header_id\n" +
                    "          FROM rcv_transactions_v rcvt\n" +
                    "          WHERE rcvt.to_organization_id = rt.organization_id\n" +
                    "          AND rcvt.shipment_header_id = rt.shipment_header_id\n" +
                    "                    AND (rcvt.quantity < rcvt.ordered_qty\n" +
                    "               OR\n" +
                    "               rcvt.secondary_quantity < rcvt.secondary_ordered_qty\n" +
                    "              )\n" +
                    "         )\n" +
                    "   OR\n" +
                    "     rsh.shipment_header_id IN\n" +
                    "         (SELECT rsh2.shipment_header_id\n" +
                    "          FROM rcv_shipment_headers rsh2\n" +
                    "          WHERE rsh2.shipment_header_id = rsh.shipment_header_id\n" +
                    "          AND rsh2.shipment_header_id \n" +
                    "              NOT IN(SELECT rcvs2.shipment_header_id\n" +
                    "                     FROM rcv_supply rcvs2\n" +
                    "                     WHERE LENGTH(rcvs2.shipment_header_id) > 0\n" +
                    "                    )\n" +
                    "         )\n" +
                    "    )";
            Log.d("QUERY", mQuery);
            ResultSet result = statement.executeQuery(mQuery);

            while (result.next()) {
                ContentValues values = new ContentValues();
                values.put("receipt_num", result.getString(1));
                values.put("po_number", result.getString(2));
                values.put("item", result.getString(3));
                values.put("item_desc", result.getString(4));
                values.put("requestor", result.getString(5));
                values.put("qty", result.getString(6));
                values.put("uom", result.getString(7));
                values.put("tgl_dibuat", result.getString(8));
                values.put("tgl_diterima", result.getString(9));
                values.put("shippement", result.getString(10));
                values.put("status_barang", result.getString(11));
                values.put("location", result.getString(12));
                values.put("comments", result.getString(13));
                Log.d("VALUES", values.toString());
                helper.insertDataRcv2(values);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    int getShipment() {
        try{
            Statement statement = mConn.createStatement();
            mQuery="SELECT DISTINCT rsh.receipt_num\n" +
                    "           FROM rcv_shipment_lines rsl\n" +
                    "    ,rcv_shipment_headers rsh\n" +
                    "    ,po_headers_all pha\n" +
                    "          WHERE pha.segment1 = '"+PO+"'\n" +
                    "          AND rsh.SHIPMENT_NUM = nvl('"+SHIPMENT+"',rsh.SHIPMENT_NUM)\n" +
                    "            AND rsl.po_header_id = pha.po_header_id\n" +
                    "            AND rsl.shipment_header_id = rsh.shipment_header_id\n" +
                    "            AND rsh.CREATION_DATE = (select max(rsh.CREATION_DATE)\n" +
                    "            from rcv_shipment_headers rsh\n" +
                    "            ,rcv_shipment_lines rsl\n" +
                    "            where rsh.SHIPMENT_HEADER_ID = rsl.SHIPMENT_HEADER_ID\n" +
                    "            and rsl.PO_HEADER_ID = pha.PO_HEADER_ID)";
            Log.d("request number", mQuery);
            ResultSet result = statement.executeQuery(mQuery);
            result.next();
            String resultString = result.getString(1);
            int resultInt = Integer.parseInt(resultString);
            System.out.println("Shipment Number ="+resultInt+"");
            return resultInt;
        }catch (SQLException e){
            e.printStackTrace();
        }
        Log.d("QTY", mQuery);
        return 0;
    }

}
