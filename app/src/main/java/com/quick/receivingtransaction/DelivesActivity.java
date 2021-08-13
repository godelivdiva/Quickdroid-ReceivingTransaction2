package com.quick.receivingtransaction;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.stetho.Stetho;
import com.google.zxing.client.android.CaptureActivity;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class DelivesActivity extends AppCompatActivity {


    ImageView refresh,iv_scann,search;
    EditText et_code;
    Button clear;
    List<listData> listItemData;
    ArrayList<String> dataArray;
    RecyclerView rv_item;
    CursorAdapter adapter;
    ListView hasildata;
    String mQuery, Content, UserName;
    int clickPosition;
    Connection mConn;
    AlertDialog.Builder bApi;
    AlertDialog aApi;
    dbHelp helper;
    ArrayList<DataRow> mDatSet;
    Button fab_transact, b_clear;
    CheckBox cb_selectall;
    Context mContext;
    int posisi;
    ManagerSessionUserOracle session;
    InputMethodManager imm;
    ArrayList<String> listSpinner;

    SweetAlertDialog sweetalert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delives);
        mContext = this;

        new ModuleTool().allowNetworkOnMainThread();
        helper = new dbHelp(this);
        session = new ManagerSessionUserOracle(this);
        Stetho.initializeWithDefaults(this);
        mConn = session.connectDb();

            setTitle("Deliver");

        UserName = (session.getUser());

        hasildata = (ListView) findViewById(R.id.hasil_data);
        iv_scann = (ImageView) findViewById(R.id.iv_scann);
        search = (ImageView) findViewById(R.id.iv_search);
        et_code = (EditText) findViewById(R.id.et_code);
//        nextbro = (Button) findViewById(R.id.nextbro);
//        clear = (Button) findViewById(R.id.clear);
//        refresh = (ImageView) findViewById(R.id.refresh);
        //refresh.setEnabled(true);
        helper.deleteDataAwal();
        mDatSet = getDataSet();
        adapter = new CursorAdapter(this, helper.select());
        hasildata.setAdapter(adapter);

        imm = (InputMethodManager) getSystemService(DelivesActivity.INPUT_METHOD_SERVICE);
        listSpinner = new ArrayList<>();

        et_code.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            Log.d("Button Done", "Berhasil");

                            if (et_code.length()<8) {
                                Toast.makeText(DelivesActivity.this, "Masukkan Receipt Num Secara Lengkap!", Toast.LENGTH_LONG).show();

                            //} else if (IsAny(et_code.getText().toString(), OrgId(et_code.getText().toString())).equals("0")){
                            } else if (IsAny(et_code.getText().toString(), OrgId(et_code.getText().toString())).equals("0")){

                                Toast.makeText(getApplicationContext(),
                                        "No Bon " + et_code.getText().toString() + " Pada Proses Ini Tidak Tersedia",
                                        Toast.LENGTH_LONG).show();
                            } else {
                                helper.deleteDataAwal();
                                String s = et_code.getText().toString();
                                goLoadingData(et_code.getText().toString());
//                                insertDb(et_code.getText().toString());
//                                showMessage();
                                //nextbro.setEnabled(true);
                                Log.d("berhasil", "buttonnya");
                            }
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_code.length()<8) {
                    Toast.makeText(DelivesActivity.this, "Masukkan Receipt Num Secara Lengkap!", Toast.LENGTH_LONG).show();

                } else if (IsAny(et_code.getText().toString(), OrgId(et_code.getText().toString())).equals("0")){

                    Toast.makeText(getApplicationContext(),
                            "No Bon " + et_code.getText().toString() + " Pada Proses Ini Tidak Tersedia",
                            Toast.LENGTH_LONG).show();
                } else {
                    helper.deleteDataAwal();
                    String s = et_code.getText().toString();
                    goLoadingData(et_code.getText().toString());
//                    insertDb(et_code.getText().toString());
//                    showMessage();
                    //nextbro.setEnabled(true);
                    Log.d("berhasil", "buttonnya");
                }
            }
        });


//        et_code.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (actionId == EditorInfo.IME_ACTION_DONE
//                        || event.getAction() == KeyEvent.KEYCODE_DPAD_CENTER
//                        && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
//                    if (IsAny(et_code.getText().toString(), OrgId(et_code.getText().toString())).equals("0")) {
//                        Toast.makeText(DelivesActivity.this, "No Data", Toast.LENGTH_SHORT).show();
//                    } else {
//                        et_code.setText(et_code.getText().toString());
//                        insertDb(et_code.getText().toString());
//                        showMessage();
//                    }
//
//                    //reconnect
//                    if (mConn == null) {
//                        mConn = new ManagerSessionUserOracle(getApplicationContext()).connectDb();
//                    }
//
//                    //checking
//                    if (mConn == null) {
//                        Toast.makeText(DelivesActivity.this, "No Connection", Toast.LENGTH_SHORT).show();
//                    } else {
//
//                    }
//                    return true;
//                }
//                return false;
//            }
//        });

        iv_scann.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper.deleteDataAwal();
                runtimePermission();
            }
        });

        hasildata.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                posisi = position;
                if(getQC().equals("BLM QC")){
                    Toast.makeText(DelivesActivity.this, "Belum QC", Toast.LENGTH_SHORT).show();
                }
                else {
                    LoadingDialog();
                }


            }

        });

//        refresh.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                helper.deleteDataRcv();
//                String s = et_code.getText().toString();
//                insertDb(et_code.getText().toString());
//                showMessage();
//                //nextbro.setEnabled(true);
//                Log.d("berhasil", "buttonnya");
//            }
//
//
//
//        });
    }

    void loading(){
        sweetalert = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        sweetalert.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        sweetalert.setTitleText("\nLoading Data. . .");
        sweetalert.setCancelable(false);
        sweetalert.show();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                sweetalert.dismissWithAnimation();
            }
        }, 2000);
    }

    void goLoadingData(final String code) {
        sweetalert = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        sweetalert.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        sweetalert.setTitleText("\nLoading Data. . .");
        sweetalert.setCancelable(false);
        sweetalert.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                insertDb(code);
                showMessage();

            }
        }, 1000);
    }

    public void refresh(View view) {
        helper.deleteDataAwal();
        String s = et_code.getText().toString();
        goLoadingData(et_code.getText().toString());
//        insertDb(et_code.getText().toString());
//        showMessage();
        Toast.makeText(this, "Refreshed", Toast.LENGTH_LONG).show();
    }

    //Scan
    void runtimePermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (permissionCheck == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 26);
        } else {
            //normal
            Intent i = new Intent(this, CaptureActivity.class);
            i.putExtra("TITLE_SCAN", "Scan");
            i.putExtra("SAVE_HISTORY", false);
            i.setAction("com.google.zxing.client.android.SCAN");
            startActivityForResult(i, 0);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 26 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
            Toast.makeText(this, "Izinkan aplikasi mengakses kamera untuk melakukan SCANN", Toast.LENGTH_SHORT).show();
        } else {
            runtimePermission();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            String code = data.getStringExtra("SCAN_RESULT");
            if (IsAny(code, OrgId(code)).equals("0")) {
                Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
            } else {
                et_code.setText(code);
                goLoadingData(code);
//                insertDb(code);
//                showMessage();
            }
        } else if (resultCode == RESULT_CANCELED) {
            Toast.makeText(getApplicationContext(), "Pembacaan QR Code Dibatalkan..", Toast.LENGTH_LONG).show();
            recreate();
        }
    }
    //Selesai Scan


    void insertDb(String code) {
        Log.d("Conn", "." + mConn);
        if (mConn == null) {
            Toast.makeText(this, "No Connection", Toast.LENGTH_SHORT).show();
            return;
        }
        String ReceiptNum = code.substring(3,code.length());
        try {
            Statement statement = mConn.createStatement();
            mQuery = "SELECT DISTINCT \n" +
                    "       rsh.RECEIPT_NUM                                                          receipt_num\n" +
                    "      ,msib.SEGMENT1                                                            item\n" +
                    "      ,msib.DESCRIPTION                                                         item_desc\n" +
                    "      ,ppf2.FULL_NAME                                                           requestor\n" +
                    "      ,rcvs.QUANTITY                                                            qty\n" +
                    "      ,rt.UNIT_OF_MEASURE                                                       uom\n" +
                    "      ,CASE plc.displayed_field||' - '||rrh.routing_name\n" +
                    "            WHEN 'Accepted - Inspection Required'\n" +
                    "            THEN 'OK QC'\n" +
                    "            WHEN 'Not Inspected - Inspection Required'\n" +
                    "            THEN 'BLM QC'\n" +
                    "            WHEN 'Not Inspected - Standard Receipt'\n" +
                    "            THEN 'NON QC'\n" +
                    "            WHEN 'Rejected - Inspection Required'\n" +
                    "            THEN 'NOT OK QC'\n" +
                    "       END                                                                      status_barang\n" +
                    "      ,pda.DESTINATION_SUBINVENTORY                                                       location\n" + //hlat.LOCATION_CODE
                    "      ,decode(rt.COMMENTS,null,'kosong',rt.COMMENTS)                        comments\n" +
                    "      ,pha.SEGMENT1                                                             po_number\n" +
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
                    "  AND rsh.RECEIPT_NUM = nvl("+ReceiptNum+",rsh.RECEIPT_NUM)    \n" +
                    "  AND rt.DESTINATION_TYPE_CODE = 'RECEIVING'\n" +
                    "  AND rt.TRANSACTION_TYPE in('RECEIVE','ACCEPT')\n" +
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
                values.put("RECEIPT_NUM", result.getString(1));
                values.put("ITEM", result.getString(2));
                values.put("ITEM_DESC", result.getString(3));
                values.put("REQUESTOR", result.getString(4));
                values.put("QTY", result.getInt(5));
                values.put("UOM", result.getString(6));
                values.put("STATUS_BARANG", result.getString(7));
                values.put("LOCATION", result.getString(8));
                values.put("COMMENTS", result.getString(9));
                values.put("PO_NUMBER", result.getString(10));
                values.put("FLAG", "N");
                Log.d("VALUES", values.toString());
                helper.insertData(values);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void showMessage() {
        Cursor c = helper.select();
        adapter = new CursorAdapter(this, c);
        hasildata.setAdapter(adapter);
        sweetalert.dismissWithAnimation();
    }

    public void OnItemClick(int position, View itemView) {
        clickPosition = position;
        boolean isSerial;
        LoadingDialog();
    }

    void dialogInputQty() {
        aApi.dismiss();
        listSpinner.clear();
        spinner();

        Cursor c = helper.select();
        c.moveToPosition(posisi);
        LayoutInflater inflater = LayoutInflater.from(this);
        View viewInflate = inflater.inflate(R.layout.activity_dialog_dlv, (ViewGroup) findViewById(android.R.id.content), false);
        final EditText et_qty = (EditText) viewInflate.findViewById(R.id.et_qty);
        final Spinner s_locator = (Spinner) viewInflate.findViewById(R.id.s_locator);
        final EditText et_comments = (EditText) viewInflate.findViewById(R.id.et_coments);
        final TextView tv_code = (TextView) viewInflate.findViewById(R.id.tv_code);
        final TextView tv_desc = (TextView) viewInflate.findViewById(R.id.tv_desc);
        final TextView tv_QtyReceive = (TextView) viewInflate.findViewById(R.id.tv_QtyReceive);
        final TextView tv_Remaining = (TextView) viewInflate.findViewById(R.id.tv_Remaining);

        ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, listSpinner);
        s_locator.setAdapter(adapter);

        tv_code.setText(c.getString(c.getColumnIndexOrThrow("ITEM")));
        tv_desc.setText(c.getString(c.getColumnIndexOrThrow("ITEM_DESC")));
        tv_QtyReceive.setText(c.getString(c.getColumnIndexOrThrow("QTY")));
        et_comments.setText(et_code.getText().toString()+" ");

        if (isCount0(tv_code.getText().toString())) {
            tv_Remaining.setText("0");
        } else {
            tv_Remaining.setText("" + getQtyDeliver(c.getString(c.getColumnIndexOrThrow("ITEM"))));
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(viewInflate)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String Locator;
                        if(s_locator.getSelectedItem()==null){
                            Locator = "";
                        }else{
                            Locator = s_locator.getSelectedItem().toString();
                        }

                        if(getQC().equals("NON QC")){
                            if (et_qty.length() == 0) {
                                Toast.makeText(DelivesActivity.this, "Kosong", Toast.LENGTH_SHORT).show();
                                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                            } else {
                                double qtyreceive = Double.valueOf(tv_QtyReceive.getText().toString());
                                double qtyTrans = Double.valueOf(et_qty.getText().toString());

                                if (qtyreceive < qtyTrans) {
                                    Toast.makeText(DelivesActivity.this, "Tidak boleh melebihi", Toast.LENGTH_SHORT).show();
                                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                                } else {
                                    //Toast.makeText(mContext, "Submitted", Toast.LENGTH_SHORT).show();
                                    LoadingDialogAPI(et_code.getText().toString(), invIId(tv_code.getText().toString()), et_qty.getText().toString(), Locator, et_comments.getText().toString());
                                }
                            }
                        } else  {
                            if(NoReceipt(tv_code.getText().toString())==null){
                                Toast.makeText(DelivesActivity.this, "Belum QC, Hubungi Seksi QC", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                if (et_qty.length() == 0) {
                                    Toast.makeText(DelivesActivity.this, "Kosong", Toast.LENGTH_SHORT).show();
                                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                                } else {
                                    double qtyreceive = Double.valueOf(tv_QtyReceive.getText().toString());
                                    double qtyTrans = Double.valueOf(et_qty.getText().toString());

                                    if (qtyreceive < qtyTrans) {
                                        Toast.makeText(DelivesActivity.this, "Tidak boleh melebihi", Toast.LENGTH_SHORT).show();
                                        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                                    } else {
                                        //Toast.makeText(mContext, "Submitted", Toast.LENGTH_SHORT).show();
                                        LoadingDialogAPIQC(et_code.getText().toString(), invIId(tv_code.getText().toString()), et_qty.getText().toString(), Locator, et_comments.getText().toString());
                                    }
                                }
                            }
                        }

                    }

                }).setNegativeButton(android.R.string.cancel, null)
                .create().show();
    }

    public String spinner() {
        String hasil = "";
        Connection dbConnection = null;
        Statement statement = null;
        ResultSet theResultSet;

        dbConnection = mConn;
        try {


            statement = dbConnection.createStatement();
            System.out.println("SELECT mil.segment1 LOCATOR\n" +
                    "  FROM mtl_item_locations mil\n" +
                    " WHERE mil.SUBINVENTORY_CODE = '"+getLocation()+"'");
            theResultSet = statement.executeQuery("SELECT mil.segment1 LOCATOR\n" +
                    "  FROM mtl_item_locations mil\n" +
                    " WHERE mil.SUBINVENTORY_CODE = '"+getLocation()+"'");

//            System.out.println("SELECT mil.segment1 LOCATOR\n" +
//                    "  FROM mtl_item_locations mil, mtl_system_items_b msib\n" +
//                    " WHERE mil.organization_id = msib.organization_id\n" +
//                    "   AND mil.inventory_item_id = msib.inventory_item_id\n" +
//                    "   AND msib.segment1 = 'MEP04G005'");
//            theResultSet = statement.executeQuery("SELECT mil.segment1 LOCATOR\n" +
//                    "  FROM mtl_item_locations mil, mtl_system_items_b msib\n" +
//                    " WHERE mil.organization_id = msib.organization_id\n" +
//                    "   AND mil.inventory_item_id = msib.inventory_item_id\n" +
//                    "   AND msib.segment1 = 'MEP04G005'");
            while (theResultSet.next()) {
                listSpinner.add(theResultSet.getString(1));
            }
//            listSpinner.add(0, "-Pilih Locator-");
        } catch (SQLException e) {

            e.printStackTrace();
        }

        return hasil;
    }

    String getheaderId() {
        try {
            Content = et_code.getText().toString();
            Statement statement = mConn.createStatement();
            mQuery = "select header_id\n" +
                    "    from mtl_txn_request_headers\n" +
                    "    where request_number = '" + Content + "'";
            ResultSet result = statement.executeQuery(mQuery);
            result.next();
            return result.getString(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    void LoadingDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View viewInflated = inflater.inflate(R.layout.activity_loading, (ViewGroup) findViewById(android.R.id.content), false);

        bApi = new AlertDialog.Builder(this);
        bApi.setView(viewInflated).setCancelable(false);

        aApi = bApi.create();
        aApi.show();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dialogInputQty();
            }
        }, 3000);
    }

    String OrgId(String Content) {
        try {
            String OrgCode = Content.substring(0, 3);
            Statement statement = mConn.createStatement();
            System.out.println("select mp.ORGANIZATION_ID from mtl_parameters mp " +
                    "where mp.ORGANIZATION_CODE = '" + OrgCode + "'");
            mQuery = "select mp.ORGANIZATION_ID from mtl_parameters mp " +
                    "where mp.ORGANIZATION_CODE = '" + OrgCode + "'";
            ResultSet result = statement.executeQuery(mQuery);
            result.next();
            return result.getString(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    String NotQC_QC(String ItemCode) {
        try {
            Statement statement = mConn.createStatement();
            System.out.println("select msib.RECEIVING_ROUTING_ID from mtl_system_items_b msib " +
                    "where msib.ORGANIZATION_ID = 81 " +
                    "and msib.SEGMENT1 = '" + ItemCode + "'");
            mQuery = "select msib.RECEIVING_ROUTING_ID from mtl_system_items_b msib " +
                    "where msib.ORGANIZATION_ID = 81 " +
                    "and msib.SEGMENT1 = '" + ItemCode + "'";
            ResultSet result = statement.executeQuery(mQuery);
            result.next();
            return result.getString(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    String NoReceipt(String ItemCode) {
        try {
            String ReceiptNum = et_code.getText().toString().substring(3,et_code.getText().toString().length());
            Statement statement = mConn.createStatement();
            mQuery = "select" +
                    "( " +
                    "    select\n" +
                    "    max(transaction_date)\n" +
                    "    from\n" +
                    "    rcv_transactions\n" +
                    "    where\n" +
                    "    transaction_type = 'ACCEPT'\n" +
                    "    and shipment_line_id = rsl.shipment_line_id\n" +
                    ") accept_date\n" +
                    "from\n" +
                    "rcv_shipment_headers rsh\n" +
                    ",rcv_shipment_lines rsl\n" +
                    ",mtl_system_items_b msib\n" +
                    "where\n" +
                    "rsh.shipment_header_id = rsl.shipment_header_id\n" +
                    "and msib.inventory_item_id = rsl.item_id\n" +
                    "and msib.organization_id = 81\n" +
                    "and rsh.RECEIPT_NUM = '"+ReceiptNum+"'\n" +
                    "and msib.SEGMENT1 = '"+ItemCode+"'\n" +
                    "order by 1";
            Log.d("NoReceipt",mQuery);
            ResultSet result = statement.executeQuery(mQuery);
            result.next();
            return result.getString(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    String QC(String ItemCode) {
        try {
            String ReceiptNum = et_code.getText().toString().substring(3,et_code.getText().toString().length());
            Statement statement = mConn.createStatement();
            mQuery = "select DISTINCT(CASE plc.displayed_field||' - '||rrh.routing_name\n" +
                    "                     WHEN 'Accepted - Inspection Required'\n" +
                    "                     THEN 'OK QC'\n" +
                    "                     WHEN 'Not Inspected - Inspection Required'\n" +
                    "                     THEN 'BLM QC'\n" +
                    "                     WHEN 'Not Inspected - Standard Receipt'\n" +
                    "                     THEN 'NON QC'\n" +
                    "                     WHEN 'Rejected - Inspection Required'\n" +
                    "                     THEN 'NOT OK QC'\n" +
                    "                     END)                                                                    status_barang\n" +
                    "            from MTL_SYSTEM_ITEMS_B msib,\n" +
                    "            RCV_SHIPMENT_HEADERS rsh,\n" +
                    "            RCV_TRANSACTIONS rt,\n" +
                    "            PO_HEADERS_ALL pha,\n" +
                    "            po_lines_all pla,\n" +
                    "            po_lookup_codes            plc,\n" +
                    "            rcv_routing_headers        rrh,\n" +
                    "            fnd_lookup_values          flv\n" +
                    "            where rsh.SHIPMENT_HEADER_ID = rt.SHIPMENT_HEADER_ID\n" +
                    "            and rt.TRANSACTION_TYPE = 'RECEIVE'\n" +
                    "            and pha.PO_HEADER_ID = rt.PO_HEADER_ID\n" +
                    "            and pha.PO_HEADER_ID = pla.PO_HEADER_ID\n" +
                    "            and msib.ORGANIZATION_ID = rsh.SHIP_TO_ORG_ID\n" +
                    "            AND rt.inspection_status_code        = plc.lookup_code \n" +
                    "            AND rt.routing_header_id             = rrh.routing_header_id\n" +
                    "            AND rt.routing_header_id             = TO_NUMBER(flv.lookup_code(+))\n" +
                    "            AND plc.lookup_type                  = 'INSPECTION STATUS'\n" +
                    "            AND flv.lookup_type                  = 'RCV_ROUTING_HEADERS'\n" +
                    "            AND flv.meaning                     IN ('Inspection Required', 'Standard Receipt') \n" +
                    "            and rsh.RECEIPT_NUM = '" + ReceiptNum + "'\n" +
                    "            and msib.SEGMENT1 = '" + ItemCode + "'";
            Log.d("QC", mQuery);
            ResultSet result = statement.executeQuery(mQuery);
            result.next();
            return result.getString(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    String getQC() {
        Cursor c = helper.queryDataAwal();
        c.moveToPosition(posisi);
        return c.getString(c.getColumnIndex(helper.AWAL_C7_STATUS_BARANG));
    }

    String getPoNumber() {
        Cursor c = helper.queryDataAwal();
        c.moveToPosition(posisi);
        return c.getString(c.getColumnIndex(helper.AWAL_C11_PO_NUMBER));
    }

    String getItemCode() {
        Cursor c = helper.queryDataAwal();
        c.moveToPosition(posisi);
        return c.getString(c.getColumnIndex(helper.AWAL_C2_ITEM));
    }

    String getLocation() {
        Cursor c = helper.queryDataAwal();
        c.moveToPosition(posisi);
        return c.getString(c.getColumnIndex(helper.AWAL_C8_LOCATION));
    }

    void updateQTYAll(String qty, String OrgId, String ItemId) {
        try {
            Statement statement = mConn.createStatement();
            mQuery = "update MTL_MATERIAL_TRANSACTIONS_TEMP MMTT " +
                    "set MMTT.TRANSACTION_QUANTITY = '" + qty + "' , MMTT.PRIMARY_QUANTITY = '" + qty + "' " +
                    "where MMTT.MOVE_ORDER_HEADER_ID  = '" + getheaderId() + "' " +
                    "AND mmtt.ORGANIZATION_ID ='" + OrgId + "' " +
                    "AND mmtt.INVENTORY_ITEM_ID = '" + ItemId + "'";
            int rowAfect = statement.executeUpdate(mQuery);
            Log.d("proses", "detailed : " + mQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void commit() {
        try {
            Statement statement = mConn.createStatement();
            mQuery = "COMMIT";
            statement.executeUpdate(mQuery);
            Log.d("proses", "Commit " + mQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    ArrayList<DataRow> getDataSet() {
        ArrayList<DataRow> dataSet = new ArrayList<>();

        Cursor c = helper.queryDataAwal();
        while (c.moveToNext()) {
            DataRow data = new DataRow();
            data.setData(
                    c.getString(c.getColumnIndex(helper.AWAL_C1_RECEIPT_NUM)),
                    c.getString(c.getColumnIndex(helper.AWAL_C2_ITEM)),
                    c.getString(c.getColumnIndex(helper.AWAL_C3_ITEM_DESC)),
                    c.getString(c.getColumnIndex(helper.AWAL_C4_REQUESTOR)),
                    c.getString(c.getColumnIndex(helper.AWAL_C5_QTY)),
                    c.getString(c.getColumnIndex(helper.AWAL_C6_UOM)),
                    c.getString(c.getColumnIndex(helper.AWAL_C7_STATUS_BARANG)),
                    c.getString(c.getColumnIndex(helper.AWAL_C8_LOCATION)),
                    c.getString(c.getColumnIndex(helper.AWAL_C9_COMMENTS)),
                    c.getString(c.getColumnIndex(helper.AWAL_C10_FLAG)),
                    c.getString(c.getColumnIndex(helper._id))
            );
            dataSet.add(data);
        }

        return dataSet;
    }

    void LoadingDialogAPI(final String NomerReceipt, final String ItemId, final String Qty, final String Locator, final String Comments) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View viewInflated = inflater.inflate(R.layout.activity_loading, (ViewGroup) findViewById(android.R.id.content), false);

        bApi = new AlertDialog.Builder(this);
        bApi.setView(viewInflated).setCancelable(false);

        aApi = bApi.create();
        aApi.show();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                insertToMisc(NomerReceipt, ItemId, Qty,getPoNumber(), Locator, Comments);
                commit();
                runApiNonQc();
                commit();
                runApiDummy();
                commit();
                DeleteFromMisc(NomerReceipt);
                commit();
                aApi.dismiss();

                helper.deleteDataAwal();
                String s = et_code.getText().toString();
//                goLoadingData(et_code.getText().toString());
//                insertDb(et_code.getText().toString());
//                showMessage();
                //nextbro.setEnabled(true);
            }
        }, 3000);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(DelivesActivity.this, "Proses Berhasil", Toast.LENGTH_SHORT).show();
                recreate();
            }
        }, 3000);
    }

    void LoadingDialogAPIQC(final String NomerReceipt, final String ItemId, final String Qty, final String Locator, final String Comments) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View viewInflated = inflater.inflate(R.layout.activity_loading, (ViewGroup) findViewById(android.R.id.content), false);

        bApi = new AlertDialog.Builder(this);
        bApi.setView(viewInflated).setCancelable(false);

        aApi = bApi.create();
        aApi.show();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                insertToMisc(NomerReceipt, ItemId, Qty,getPoNumber(), Locator, Comments);
                commit();
                runApiQc();
                commit();
                runApiDummy();
                commit();
                DeleteFromMisc(NomerReceipt);
                commit();
            }
        }, 3000);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                aApi.dismiss();
                Toast.makeText(DelivesActivity.this, "Proses Berhasil", Toast.LENGTH_SHORT).show();
                recreate();
            }
        }, 3000);
    }

    void  runApiNonQc() {
        Connection dbConnection;
        Integer userId = 0;
        Statement statement;
        ResultSet theResultSet;
        String ReceiptNum = et_code.getText().toString().substring(3,et_code.getText().toString().length());


        try {
            dbConnection = mConn;
            statement = dbConnection.createStatement();


            System.out.println("begin APPS.KHS_DELIVER_PO('" + UserName + "','" + ReceiptNum + "','"+getPoNumber()+"'); end;");

            //API issue
            CallableStatement funcnone = dbConnection
                    .prepareCall("begin APPS.KHS_DELIVER_PO('" + UserName + "','" + ReceiptNum + "','"+getPoNumber()+"'); end;");
            funcnone.executeUpdate();
            funcnone.close();


        } catch (SQLException e) {

            Toast.makeText(getBaseContext(), "Error :" + e.toString(),
                    Toast.LENGTH_LONG).show();
            Log.d("ERROR:", e.toString());

        }
    }

    void runApiQc() {
        Connection dbConnection;
        Integer userId = 0;
        Statement statement;
        ResultSet theResultSet;
        String ReceiptNum = et_code.getText().toString().substring(3,et_code.getText().toString().length());


        try {
            dbConnection = mConn;
            statement = dbConnection.createStatement();


            System.out.println("begin APPS.KHS_DELIVER_PO('" + UserName + "','" + ReceiptNum + "','"+getPoNumber()+"'); end;");

            //API issue
            CallableStatement funcnone = dbConnection
                    .prepareCall("begin APPS.KHS_DELIVER_PO('" + UserName + "','" + ReceiptNum + "','"+getPoNumber()+"'); end;");
            funcnone.executeUpdate();
            funcnone.close();


        } catch (SQLException e) {

            Toast.makeText(getBaseContext(), "Error :" + e.toString(),
                    Toast.LENGTH_LONG).show();
            Log.d("ERROR:", e.toString());

        }
    }

    void runApiDummy() {
        Connection dbConnection = null;
        Integer userId = 0;
        Statement statement = null;
        ResultSet theResultSet;

        try {
            dbConnection = mConn;
            statement = dbConnection.createStatement();

            theResultSet = statement
                    .executeQuery("select user_id from fnd_user where user_name = '"
                            + UserName + "'");

            if (theResultSet.next()) {
                userId = Integer.parseInt(theResultSet.getString(1));
            }

            CallableStatement funcnone = dbConnection.prepareCall
                    ("BEGIN Fnd_Global.apps_initialize(" + userId + ",50630,20003); :x := apps.fnd_request.submit_request('PO','RVCTP',NULL,SYSDATE,FALSE,'BATCH'," + InterfaceGroup() + "); END;");
            System.out.println("BEGIN Fnd_Global.apps_initialize(" + userId + ",50630,20003); :x := apps.fnd_request.submit_request('PO','RVCTP',NULL,SYSDATE,FALSE,'BATCH'," + InterfaceGroup() + "); END;");

            funcnone.registerOutParameter(1, Types.INTEGER);
            funcnone.executeUpdate();
            System.out.println("Return value is : " + funcnone.getInt(1));
            funcnone.close();
        } catch (SQLException e) {

            Toast.makeText(getBaseContext(),
                    "Error :" + e.toString(), Toast.LENGTH_LONG)
                    .show();
        }
    }

    public void insertToMisc(String NomerReceipt, String ItemId, String Qty, String PoNumber, String Locator, String Comments) {
        Statement stmt = null;
        Connection conn = null;
        String dlv = "DELIVER";

        String ReceiptNum = NomerReceipt.substring(3,NomerReceipt.length());
        try {

            conn = mConn;
            stmt = conn.createStatement();

            System.out
                    .println("insert into khs_delivery_po_temp3 (no_receipt,inventory_item_id,quantity,po_number,status,locator,comments) values ('" + ReceiptNum + "','" + ItemId + "','" + Qty + "','"+PoNumber+"','"+dlv+"','"+Locator+"','"+Comments+"')");

            stmt.executeUpdate("insert into khs_delivery_po_temp3 (no_receipt,inventory_item_id,quantity,po_number,status,locator,comments) values ('" + ReceiptNum + "','" + ItemId + "','" + Qty + "','"+PoNumber+"','"+dlv+"','"+Locator+"','"+Comments+"')");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }


    public void DeleteFromMisc(String NomerReceipt) {
        Statement stmt = null;
        Connection conn = null;

        String ReceiptNum = NomerReceipt.substring(3,NomerReceipt.length());
        try {

            conn = mConn;
            stmt = conn.createStatement();

            System.out
                    .println("DELETE FROM khs_delivery_po_temp3 WHERE no_receipt = '"+ReceiptNum+"'");

            stmt.executeUpdate("DELETE FROM khs_delivery_po_temp3 WHERE no_receipt = '"+ReceiptNum+"'");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    String invIId(String itemCode) {
        String user_id = "";
        Connection dbConnection = null;
        Statement statement = null;
        ResultSet theResultSet;

        dbConnection = mConn;
        try {
            statement = dbConnection.createStatement();
            theResultSet = statement.executeQuery("SELECT DISTINCT inventory_item_id FROM MTL_SYSTEM_ITEMS_B WHERE segment1 = '" + itemCode + "'");
            if (theResultSet.next()) {
                user_id = theResultSet.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user_id;

    }

    String InterfaceGroup() {
        String user_id = "";
        Connection dbConnection = null;
        Statement statement = null;
        ResultSet theResultSet;

        dbConnection = mConn;
        try {
            statement = dbConnection.createStatement();
            theResultSet = statement.executeQuery("select rcv_interface_groups_s.currval from dual");
            if (theResultSet.next()) {
                user_id = theResultSet.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user_id;

    }

    String IsAny(String content, String OrgId) {
        try {
            String ReceiptNum = content.substring(3,content.length());
            Statement statement = mConn.createStatement();
            mQuery = "SELECT \n" +
                    "     COUNT(rsh.receipt_num) receipt_num\n" +
                    "    FROM rcv_transactions rt\n" +
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
                    "  AND rsh.RECEIPT_NUM = nvl('"+ReceiptNum+"',rsh.RECEIPT_NUM)    \n" +
                    "  AND rt.DESTINATION_TYPE_CODE = 'RECEIVING'\n" +
                    "  AND rt.TRANSACTION_TYPE in('RECEIVE','ACCEPT')\n" +
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
            Log.d("Ada",mQuery);
            ResultSet result = statement.executeQuery(mQuery);
            result.next();
            return result.getString(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean isCount0(String ItemCode) {
        Integer ver = 0;
        Connection dbConnection;
        Statement statement;
        ResultSet theResultSet;

        dbConnection = mConn;
        try {
            String ReceiptNum = et_code.getText().toString().substring(3,et_code.getText().toString().length());
            statement = dbConnection.createStatement();
            theResultSet = statement.executeQuery("select  count(rt.QUANTITY)\n" +
                    "            from MTL_SYSTEM_ITEMS_B msib,\n" +
                    "            RCV_SHIPMENT_HEADERS rsh,\n" +
                    "            RCV_TRANSACTIONS rt,\n" +
                    "            RCV_SHIPMENT_LINES rsl,\n" +
                    "            PO_HEADERS_ALL pha,\n" +
                    "            po_lines_all pla,\n" +
                    "            po_line_locations_all plla,\n" +
                    "            mtl_parameters mp,\n" +
                    "            HR_LOCATIONS hrl,\n" +
                    "            po_distributions_all pda\n" +
                    "            where rsh.SHIPMENT_HEADER_ID = rt.SHIPMENT_HEADER_ID\n" +
                    "            and rt.TRANSACTION_TYPE = 'DELIVER'\n" +
                    "            and rsl.SHIPMENT_LINE_ID = rt.SHIPMENT_LINE_ID\n" +
                    "            and rsl.ITEM_ID = pla.ITEM_ID\n" +
                    "            and pha.PO_HEADER_ID = rt.PO_HEADER_ID\n" +
                    "            and pha.PO_HEADER_ID = pla.PO_HEADER_ID\n" +
                    "            and rsl.ITEM_ID = msib.INVENTORY_ITEM_ID\n" +
                    "            and msib.ORGANIZATION_ID = rsh.SHIP_TO_ORG_ID\n" +
                    "            and pha.PO_HEADER_ID = plla.PO_HEADER_ID\n" +
                    "            and rt.PO_HEADER_ID  = plla.PO_HEADER_ID\n" +
                    "            and pla.PO_LINE_ID = plla.PO_LINE_ID\n" +
                    "            and plla.PO_LINE_ID = rt.PO_LINE_ID\n" +
                    "            and rsl.TO_ORGANIZATION_ID = mp.ORGANIZATION_ID\n" +
                    "            AND rt.LOCATION_ID = hrl.LOCATION_ID (+)\n" +
                    "            AND plla.line_location_id = pda.line_location_id\n" +
                    "            and rsh.RECEIPT_NUM = '" + ReceiptNum + "'\n" +
                    "            and msib.SEGMENT1 = '" + ItemCode + "'");
            System.out.println("select  count(rt.QUANTITY)\n" +
                    "            from MTL_SYSTEM_ITEMS_B msib,\n" +
                    "            RCV_SHIPMENT_HEADERS rsh,\n" +
                    "            RCV_TRANSACTIONS rt,\n" +
                    "            RCV_SHIPMENT_LINES rsl,\n" +
                    "            PO_HEADERS_ALL pha,\n" +
                    "            po_lines_all pla,\n" +
                    "            po_line_locations_all plla,\n" +
                    "            mtl_parameters mp,\n" +
                    "            HR_LOCATIONS hrl,\n" +
                    "            po_distributions_all pda\n" +
                    "            where rsh.SHIPMENT_HEADER_ID = rt.SHIPMENT_HEADER_ID\n" +
                    "            and rt.TRANSACTION_TYPE = 'DELIVER'\n" +
                    "            and rsl.SHIPMENT_LINE_ID = rt.SHIPMENT_LINE_ID\n" +
                    "            and rsl.ITEM_ID = pla.ITEM_ID\n" +
                    "            and pha.PO_HEADER_ID = rt.PO_HEADER_ID\n" +
                    "            and pha.PO_HEADER_ID = pla.PO_HEADER_ID\n" +
                    "            and rsl.ITEM_ID = msib.INVENTORY_ITEM_ID\n" +
                    "            and msib.ORGANIZATION_ID = rsh.SHIP_TO_ORG_ID\n" +
                    "            and pha.PO_HEADER_ID = plla.PO_HEADER_ID\n" +
                    "            and rt.PO_HEADER_ID  = plla.PO_HEADER_ID\n" +
                    "            and pla.PO_LINE_ID = plla.PO_LINE_ID\n" +
                    "            and plla.PO_LINE_ID = rt.PO_LINE_ID\n" +
                    "            and rsl.TO_ORGANIZATION_ID = mp.ORGANIZATION_ID\n" +
                    "            AND rt.LOCATION_ID = hrl.LOCATION_ID (+)\n" +
                    "            AND plla.line_location_id = pda.line_location_id\n" +
                    "            and rsh.RECEIPT_NUM = '" + ReceiptNum + "'\n" +
                    "            and msib.SEGMENT1 = '" + ItemCode + "'");
            if (theResultSet.next()) {
                ver = Integer.parseInt(theResultSet.getString(1));
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }

        if (ver > 0)
            return true;
        else
            return false;
    }


    int getQtyDeliver(String ItemCode) {
        try {
            String ReceiptNum = et_code.getText().toString().substring(3,et_code.getText().toString().length());
            Statement statement = mConn.createStatement();
            mQuery = "select  nvl(rt.QUANTITY,0)\n" +
                    "            from MTL_SYSTEM_ITEMS_B msib,\n" +
                    "            RCV_SHIPMENT_HEADERS rsh,\n" +
                    "            RCV_TRANSACTIONS rt,\n" +
                    "            RCV_SHIPMENT_LINES rsl,\n" +
                    "            PO_HEADERS_ALL pha,\n" +
                    "            po_lines_all pla,\n" +
                    "            po_line_locations_all plla,\n" +
                    "            mtl_parameters mp,\n" +
                    "            HR_LOCATIONS hrl,\n" +
                    "            po_distributions_all pda\n" +
                    "            where rsh.SHIPMENT_HEADER_ID = rt.SHIPMENT_HEADER_ID\n" +
                    "            and rt.TRANSACTION_TYPE = 'DELIVER'\n" +
                    "            and rsl.SHIPMENT_LINE_ID = rt.SHIPMENT_LINE_ID\n" +
                    "            and rsl.ITEM_ID = pla.ITEM_ID\n" +
                    "            and pha.PO_HEADER_ID = rt.PO_HEADER_ID\n" +
                    "            and pha.PO_HEADER_ID = pla.PO_HEADER_ID\n" +
                    "            and rsl.ITEM_ID = msib.INVENTORY_ITEM_ID\n" +
                    "            and msib.ORGANIZATION_ID = rsh.SHIP_TO_ORG_ID\n" +
                    "            and pha.PO_HEADER_ID = plla.PO_HEADER_ID\n" +
                    "            and rt.PO_HEADER_ID  = plla.PO_HEADER_ID\n" +
                    "            and pla.PO_LINE_ID = plla.PO_LINE_ID\n" +
                    "            and plla.PO_LINE_ID = rt.PO_LINE_ID\n" +
                    "            and rsl.TO_ORGANIZATION_ID = mp.ORGANIZATION_ID\n" +
                    "            AND rt.LOCATION_ID = hrl.LOCATION_ID (+)\n" +
                    "            AND plla.line_location_id = pda.line_location_id\n" +
                    "            and rsh.RECEIPT_NUM = '" + ReceiptNum + "'\n" +
                    "            and msib.SEGMENT1 = '" + ItemCode + "'";
            ResultSet result = statement.executeQuery(mQuery);
            Log.d("deliver qty", mQuery);
            if (result.next()) {
                return result.getInt(3);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    void alertdialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Sudah Berhasil")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        recreate();
                        dialog.cancel();
                    }
                })
                .create().show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_awal, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_logout:
                new ManagerSessionUserOracle(DelivesActivity.this).logoutUser();
                Intent i = new Intent(DelivesActivity.this, LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}