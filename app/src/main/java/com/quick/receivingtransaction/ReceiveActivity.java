package com.quick.receivingtransaction;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.text.format.Formatter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.facebook.stetho.Stetho;
import com.google.zxing.client.android.CaptureActivity;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ReceiveActivity extends AppCompatActivity implements RecyclerItem.ItemClickListener{
    final Context context = this;
    ImageView refresh, iv_scann, next, search;
    EditText et_code;
    List<listData> listItemData;
    ArrayList<String> dataArray;
    ArrayList<DataRowRcv> mDataRowRcv;
    Button bt_receipt, bt_shipped, btn_unselect_all;
    RecyclerView rv_item;

    private TimePickerDialog timePickerDialog;
    RecyclerView hasildata;
    RecyclerItem adapter;
    String mQuery, Content, UserName, userId, shipmentText, orgCode;
    Spinner s_orgCode;
    int clickPosition;
    Connection mConn;
    AlertDialog.Builder bApi;
    AlertDialog aApi;
    dbHelp helper;
    ArrayList<DataRowRcv> mDatSet;
    Button fab_transact, b_clear;
    ProgressDialog progressDialog;
    CheckBox cb_selectall;
    Context mContext;
    int posisi;
    ManagerSessionUserOracle session;
    InputMethodManager imm;
    ArrayList<String> listSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive);
        mContext = this;

        new ModuleTool().allowNetworkOnMainThread();
        helper = new dbHelp(this);
//        mDataRowRcv = new dataRowRcv;
        session = new ManagerSessionUserOracle(this);
        Stetho.initializeWithDefaults(this);
        mConn = session.connectDb();
        progressDialog = new ProgressDialog(this);
        listSpinner = new ArrayList<>();

        setTitle("Receive");

        UserName = (session.getUser());
        userId = new ManagerSessionUserOracle(this).getUserId();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        hasildata = (RecyclerView) findViewById(R.id.hasil_data);
        s_orgCode = findViewById(R.id.s_org_code);
        iv_scann = (ImageView) findViewById(R.id.iv_scann);
        et_code = (EditText) findViewById(R.id.et_code);
        refresh = (ImageView) findViewById(R.id.refresh);
        next = (ImageView) findViewById(R.id.next);
        search = (ImageView) findViewById(R.id.iv_search);
        cb_selectall = (CheckBox) findViewById(R.id.cb_selectall);
        //btn_unselect_all = (Button) findViewById(R.id.bt_unselect);
        helper.deleteDataRcv();
        mDatSet = getDataSet();
        adapter = new RecyclerItem(this, mDatSet, this);
        hasildata.hasFixedSize();
        hasildata.setLayoutManager(layoutManager);
        hasildata.setAdapter(adapter);
        imm = (InputMethodManager) getSystemService(DelivesActivity.INPUT_METHOD_SERVICE);

        listSpinner.clear();
        spinner();

        ArrayAdapter adapterSpinner = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, listSpinner);
        s_orgCode.setAdapter(adapterSpinner);

        orgCode = s_orgCode.getSelectedItem().toString();

        cb_selectall.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                int jmlData =mDatSet.size();
                if (compoundButton.isChecked()){
                    Toast.makeText(ReceiveActivity.this, "All Selected", Toast.LENGTH_SHORT).show();
                    for (int i=0;i<jmlData;i++){
                        mDatSet.get(i).setSelected("Sudah Centang");
                    }
                    helper.updateFlagAll("Sudah Centang");
                    adapter.notifyDataSetChanged();
                }
                else {
                    int selectedTemp=0;
                    int totalTemp=0;

                    for (int c=0;c < jmlData;c++){
                        if(!mDatSet.get(c).getSelected().equals("Sudah Centang")){
                            selectedTemp=selectedTemp+1;
                            if (selectedTemp>0){
                                adapter.notifyDataSetChanged();
                            }
                        }else{
                            totalTemp=totalTemp+1;
                            if (totalTemp==jmlData){
                                for (int a=0;a < jmlData;a++) {
                                    mDatSet.get(a).setSelected("N");
                                }
                                Toast.makeText(ReceiveActivity.this, "Unselected All", Toast.LENGTH_SHORT).show();
                                helper.updateFlagAll("N");
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }
                }
            }
        });

        et_code.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            Log.d("Button Done", "Berhasil");

                            if (et_code.length()<8||et_code.length()>8) {
                                Toast.makeText(ReceiveActivity.this, "Masukkan Receipt Num Secara Benar!", Toast.LENGTH_LONG).show();

                            } else if (IsAny(et_code.getText().toString())){
                                helper.deleteDataRcv();
                                String s = et_code.getText().toString();
                                insertDb(et_code.getText().toString());
                                showMessage();
                                //nextbro.setEnabled(true);
                                Log.d("berhasil", "buttonnya");
                            } else {
                                Toast.makeText(getApplicationContext(),
                                        "No Bon " + et_code.getText().toString() + " Pada Proses Ini Tidak Tersedia",
                                        Toast.LENGTH_LONG).show();
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
                if (et_code.length()<8||et_code.length()>8) {
                    Toast.makeText(ReceiveActivity.this, "Masukkan Receipt Num Secara Benar!", Toast.LENGTH_LONG).show();

                } else if (IsAny(et_code.getText().toString())){
                    helper.deleteDataRcv();
                    String s = et_code.getText().toString();
                    insertDb(et_code.getText().toString());
                    showMessage();
                    //nextbro.setEnabled(true);
                    Log.d("berhasil", "buttonnya");
                } else {
                    Toast.makeText(getApplicationContext(),
                            "No Bon " + et_code.getText().toString() + " Pada Proses Ini Tidak Tersedia",
                            Toast.LENGTH_LONG).show();
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
//                        Toast.makeText(ReceiveActivity.this, "No Data", Toast.LENGTH_SHORT).show();
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
//                        Toast.makeText(ReceiveActivity.this, "No Connection", Toast.LENGTH_SHORT).show();
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
                helper.deleteDataRcv();
                runtimePermission();
            }
        });

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper.deleteDataRcv();
                String s = et_code.getText().toString();
                insertDb(et_code.getText().toString());
                showMessage();
                Toast.makeText(ReceiveActivity.this, "Refreshed", Toast.LENGTH_LONG).show();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadingDialogNext();
                Toast.makeText(ReceiveActivity.this, "Next", Toast.LENGTH_LONG).show();
            }
        });

//        hasildata.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                System.out.println("Card View Clicked");
//                posisi = position;
//
//                LoadingDialog();
//            }
//
//        });

//        refresh.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                                helper.deleteDataRcv();
////                                String s = et_code.getText().toString();
////                                insertDb(et_code.getText().toString());
////                                showMessage();
////                                //nextbro.setEnabled(true);
////                                Log.d("berhasil", "buttonnya");
//                Toast.makeText(context, "mhyghj", Toast.LENGTH_SHORT).show();
//                    }
//        });
    }

    public void refresh(View view) {
        helper.deleteDataRcv();
        String s = et_code.getText().toString();
        insertDb(et_code.getText().toString());
        showMessage();
        Toast.makeText(this, "Refreshed", Toast.LENGTH_LONG).show();
    }

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

    void showMessage() {
        mDatSet.clear();
        mDatSet.addAll(getDataSet());
        adapter.notifyDataSetChanged();
    }

    public void OnItemClick(int position, View itemView) {
        clickPosition = position;
        posisi = position;
        boolean isSerial;
        dialogInputQty();
    }

    void dialogInputQty() {

        Cursor c = helper.selectRcv();
        c.moveToPosition(posisi);
//        final DataRowRcv DataRowRcv = mDataRowRcv.get(posisi);
        LayoutInflater inflater = LayoutInflater.from(this);
        View viewInflate = inflater.inflate(R.layout.activity_dialog_rcv, (ViewGroup) findViewById(android.R.id.content), false);
        final EditText et_qty = (EditText) viewInflate.findViewById(R.id.et_qty);
        final TextView tv_code = (TextView) viewInflate.findViewById(R.id.tv_code);
        final TextView tv_QtyOrder = (TextView) viewInflate.findViewById(R.id.tv_QtyOrder);
        final TextView tv_QtyReceive = (TextView) viewInflate.findViewById(R.id.tv_QtyReceive);

        tv_code.setText(getSegment1());
        tv_QtyOrder.setText(getQtyOrder());
        tv_QtyReceive.setText(getQtyReceived());

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(viewInflate)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (et_qty.length() == 0) {
                            Toast.makeText(ReceiveActivity.this, "Kosong", Toast.LENGTH_SHORT).show();
                            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                        } else {
                            double qtyOrder = Double.valueOf(tv_QtyOrder.getText().toString());
                            double qtyreceive = Double.valueOf(tv_QtyReceive.getText().toString());
                            double qtyTrans = Double.valueOf(et_qty.getText().toString());
                            System.out.println(qtyreceive);
                            System.out.println(qtyTrans);

                            if ( qtyTrans+qtyreceive > qtyOrder) {
                                Toast.makeText(ReceiveActivity.this, "Tidak boleh melebihi", Toast.LENGTH_SHORT).show();
                                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                            } else {
                                helper.inputQtyRcv(et_qty.getText().toString(), tv_code.getText().toString(), getLocation(), getPoLine(), getLineLoc(), getID());
                                //System.out.println("UPDATE QTY PROSES:"+et_qty.getText().toString()+","+tv_code.getText().toString()+","+getLocation()+","+DataRowRcv.M_C3_PO_LINE_ID+","+DataRowRcv.M_C4_LINE_ORGANIZATION_ID_ID);
                                mDatSet.get(posisi).setSelected("Sudah Centang");
                                //LoadingDialogAPIQC(et_code.getText().toString(), et_qty.getText().toString());
                                //Toast.makeText(mContext, "Transfer Berhasil", Toast.LENGTH_SHORT).show();
                                //helper.deleteDataRcv();
                                //insertDb(et_code.getText().toString());
                                showMessage();
                                //nextbro.setEnabled(true);
                                Log.d("berhasil", "buttonnya");
                            }
                        }
                            }

                })
                .create().show();
    }

    void dialogNext() {

        aApi.dismiss();
        Cursor c = helper.selectRcv();
        c.moveToPosition(posisi);
        LayoutInflater inflater = LayoutInflater.from(this);
        View viewInflate = inflater.inflate(R.layout.activity_dialog_rcv_next, (ViewGroup) findViewById(android.R.id.content), false);
        final TextView tv_receipt = (TextView) viewInflate.findViewById(R.id.tv_receipt);
        final TextView tv_shipped = (TextView) viewInflate.findViewById(R.id.tv_shipped);
        final TextView tv_receipt_time = (TextView) viewInflate.findViewById(R.id.tv_time);
        final Button bt_receipt = (Button) viewInflate.findViewById(R.id.bt_receipt);
        final Button bt_shipped = (Button) viewInflate.findViewById(R.id.bt_shipped);
        final Button bt_receipt_time = (Button) viewInflate.findViewById(R.id.bt_Time);
        final EditText et_shipment = (EditText) viewInflate.findViewById(R.id.et_shipment);


        bt_receipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerRcp(tv_receipt);
            }
        });

        bt_shipped.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerSpd(tv_shipped);
            }
        });

        bt_receipt_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimeDialog(tv_receipt_time);
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(viewInflate)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO ISI ACTION ONCLICK NEXT (BUTTON POJOK BAWAH KANAN)

                            LoadingDialogAPIQC(et_code.getText().toString(), et_shipment.getText().toString(), tv_receipt.getText().toString(), tv_shipped.getText().toString(), tv_receipt_time.getText().toString());
                            //insertDb2(et_code.getText().toString(), et_shipment.getText().toString());
                            shipmentText = et_shipment.getText().toString();
                        Toast.makeText(context, "Submitted!", Toast.LENGTH_SHORT).show();
                    }

                }).create().show();
    }

    void successDialog() {

        aApi.dismiss();
        Cursor c = helper.selectRcv();
        c.moveToPosition(posisi);
        LayoutInflater inflater = LayoutInflater.from(this);
        View viewInflate = inflater.inflate(R.layout.activity_success_rcv, (ViewGroup) findViewById(android.R.id.content), false);
        final TextView shipment = viewInflate.findViewById(R.id.tv_no_receipt);

        shipment.setText(""+getShipment()+"");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(viewInflate)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                Intent i = new Intent(ReceiveActivity.this, Receive2Activity.class);
//                startActivity(i);
                //dialog.cancel();
                //recreate();
            }
        })
                .create().show();
    }

//    void coba(TextView a){
//        a.setText("cobaaaa");
//    }

    public void showDatePickerRcp(final TextView receipt) {

        Calendar calendar = Calendar.getInstance();

        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setup(calendar, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String SpdDate = ""+view.getDayOfMonth()+"-"+view.getMonth()+"-"+view.getYear()+"";
                String Tanggal = SpdDate.substring( 0, SpdDate.indexOf("-"));
                String BulanString = SpdDate.substring( SpdDate.indexOf("-")+1, SpdDate.lastIndexOf("-"));
                int BulanPrs = Integer.parseInt(BulanString);
                int Bulan = BulanPrs+1;
                String Tahun = SpdDate.substring( SpdDate.lastIndexOf("-")+1, SpdDate.length());

                String FinalSpdDate = ""+Tanggal+"-"+Bulan+"-"+Tahun+"";

                //receipt.setText(view.getYear()+"/"+view.getMonth()+"/"+view.getDayOfMonth());
                receipt.setText(FinalSpdDate);
            }
        });
        fragment.show(getSupportFragmentManager(), null);

    }

    public void showDatePickerSpd(final TextView shipped) {


        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, 1);

        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setup(calendar, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String SpdDate = ""+view.getDayOfMonth()+"-"+view.getMonth()+"-"+view.getYear()+"";
                String Tanggal = SpdDate.substring( 0, SpdDate.indexOf("-"));
                String BulanString = SpdDate.substring( SpdDate.indexOf("-")+1, SpdDate.lastIndexOf("-"));
                int BulanPrs = Integer.parseInt(BulanString);
                int Bulan = BulanPrs+1;
                String Tahun = SpdDate.substring( SpdDate.lastIndexOf("-")+1, SpdDate.length());

                String FinalSpdDate = ""+Tanggal+"-"+Bulan+"-"+Tahun+"";
                shipped.setText(FinalSpdDate);
                getShippedDate(SpdDate);
            }
        });
        fragment.show(getSupportFragmentManager(), null);
        String ShippedDate = fragment.toString();

    }

    private void showTimeDialog(final TextView time) {

        /**
         * Calendar untuk mendapatkan waktu saat ini
         */
        Calendar calendar = Calendar.getInstance();

        /**
         * Initialize TimePicker Dialog
         */
        timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                /**
                 * Method ini dipanggil saat kita selesai memilih waktu di DatePicker
                 */
                time.setText(" "+hourOfDay+":"+minute+":00");
            }
        },
                /**
                 * Tampilkan jam saat ini ketika TimePicker pertama kali dibuka
                 */
                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),

                /**
                 * Cek apakah format waktu menggunakan 24-hour format
                 */
                DateFormat.is24HourFormat(this));

        timePickerDialog.show();
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

    void LoadingDialogNext() {
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
                dialogNext();
            }
        }, 3000);
    }

    String OrgId(String Content) {
        try {
            String OrgCode = Content;
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
            String ReceiptNum = et_code.getText().toString();
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
            String ReceiptNum = et_code.getText().toString();
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

    String getIp() {
        WifiManager wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
        return ip;
    }

    String getQC() {
        Cursor c = helper.queryDataRcv();
        c.moveToPosition(posisi);
        // c.moveToPosition(clickPosition);
        return c.getString(c.getColumnIndex(helper.AWAL_C7_STATUS_BARANG));
    }

    String getOrgId() {
        Cursor c = helper.queryDataRcv();
        c.moveToPosition(posisi);
        // c.moveToPosition(clickPosition);
        return c.getString(c.getColumnIndex(helper.RCV_C15_ORG_ID));
    }

    String getPoHeader() {
        Cursor c = helper.queryDataRcv();
        c.moveToPosition(posisi);
        //c.moveToPosition(clickPosition);
        return c.getString(c.getColumnIndex(helper.RCV_C2_PO_HEADER_ID));
    }

    String getInvItemId() {
        Cursor c = helper.queryDataRcv();
        c.moveToPosition(posisi);
        // c.moveToPosition(clickPosition);
        return c.getString(c.getColumnIndex(helper.RCV_C16_INVENTORY_ITEM_ID));
    }

    String getSegment1() {
        Cursor c = helper.queryDataRcv();
        c.moveToPosition(posisi);
        //c.moveToPosition(clickPosition);
        return c.getString(c.getColumnIndex(helper.RCV_C6_SEGMENT1));
    }

    String getQtyOrder() {
        Cursor c = helper.queryDataRcv();
        c.moveToPosition(posisi);
        //c.moveToPosition(clickPosition);
        return c.getString(c.getColumnIndex(helper.RCV_C10_QUANTITY_ORDERED));
    }

    String getQtyReceived() {
        Cursor c = helper.queryDataRcv();
        c.moveToPosition(posisi);
        //c.moveToPosition(clickPosition);
        return c.getString(c.getColumnIndex(helper.RCV_C11_QUANTITY_RECEIVED));
    }

    String getLocation() {
        Cursor c = helper.queryDataRcv();
        c.moveToPosition(posisi);
        //c.moveToPosition(clickPosition);
        return c.getString(c.getColumnIndex(helper.RCV_C7_LOCATION_CODE));
    }

    String getPoLine() {
        Cursor c = helper.queryDataRcv();
        c.moveToPosition(posisi);
        //c.moveToPosition(clickPosition);
        return c.getString(c.getColumnIndex(helper.RCV_C3_PO_LINE_ID));
    }

    String getLineLoc() {
        Cursor c = helper.queryDataRcv();
        c.moveToPosition(posisi);
        //c.moveToPosition(clickPosition);
        return c.getString(c.getColumnIndex(helper.RCV_C4_LINE_LOCATION_ID));
    }

    String getID() {
        Cursor c = helper.queryDataRcv();
        c.moveToPosition(posisi);
        //c.moveToPosition(clickPosition);
        return c.getString(c.getColumnIndex(helper._id2));
    }

    String getFlag() {
        Cursor c = helper.queryDataRcv();
        c.moveToPosition(posisi);
        //c.moveToPosition(clickPosition);
        return c.getString(c.getColumnIndex(helper.RCV_C21_FLAG));
    }

    public String getShippedDate(String shippedDate) {
        return shippedDate;
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
            aApi.dismiss();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    ArrayList<DataRowRcv> getDataSet() {
        ArrayList<DataRowRcv> dataSet = new ArrayList<>();

        Cursor c = helper.queryDataRcv();
        while (c.moveToNext()) {
            DataRowRcv data = new DataRowRcv();
            data.setData(
                    c.getString(c.getColumnIndex(helper.RCV_C1_PRIMARY_UNIT_OF_MEASURE)),
                    c.getString(c.getColumnIndex(helper.RCV_C2_PO_HEADER_ID)),
                    c.getString(c.getColumnIndex(helper.RCV_C3_PO_LINE_ID)),
                    c.getString(c.getColumnIndex(helper.RCV_C4_LINE_LOCATION_ID)),
                    c.getString(c.getColumnIndex(helper.RCV_C5_ORGANIZATION_CODE)),
                    c.getString(c.getColumnIndex(helper.RCV_C6_SEGMENT1)),
                    c.getString(c.getColumnIndex(helper.RCV_C7_LOCATION_CODE)),
                    c.getString(c.getColumnIndex(helper.RCV_C8_ORGANIZATION_ID)),
                    c.getString(c.getColumnIndex(helper.RCV_C9_CLOSED_CODE)),
                    c.getString(c.getColumnIndex(helper.RCV_C10_QUANTITY_ORDERED)),
                    c.getString(c.getColumnIndex(helper.RCV_C11_QUANTITY_RECEIVED)),
                    c.getString(c.getColumnIndex(helper.RCV_C12_LINE_NUM)),
                    c.getString(c.getColumnIndex(helper.RCV_C13_SHIPMENT_NUM)),
                    c.getString(c.getColumnIndex(helper.RCV_C14_DESTINATION_ORGANIZATION_ID)),
                    c.getString(c.getColumnIndex(helper.RCV_C15_ORG_ID)),
                    c.getString(c.getColumnIndex(helper.RCV_C16_INVENTORY_ITEM_ID)),
                    c.getString(c.getColumnIndex(helper.RCV_C17_DESCRIPTION)),
                    c.getString(c.getColumnIndex(helper.RCV_C18_NOTE)),
                    c.getString(c.getColumnIndex(helper.RCV_C19_REQUESTOR)),
                    c.getString(c.getColumnIndex(helper.RCV_C20_LOCATION_ID)),
                    c.getString(c.getColumnIndex(helper.RCV_C21_FLAG)),
                    c.getString(c.getColumnIndex(helper.RCV_C22_QTY_RECEIVE)),
                    c.getString(c.getColumnIndex(helper._id))
            );
            dataSet.add(data);
        }

        return dataSet;
    }

     void LoadingDialogAPIQC(final String NomerReceipt, final String NomerShipment, final String ReceiptDate, final String ShippedDate, final String ReceiptTime) {
        progressDialog.setMessage("Sedang Diproses...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                insertToMisc(NomerReceipt);
                commit();
                runApiNonQc(NomerShipment, ReceiptDate, ShippedDate, ReceiptTime);
                commit();
                runApiDummy();
                commit();
                DeleteFromMisc(NomerReceipt);
                commit();

                helper.deleteDataRcv();
                String s = et_code.getText().toString();
                insertDb(et_code.getText().toString());
                showMessage();

            }
        }, 1000);
                progressDialog.dismiss();
                //successDialog();

                Intent i = new Intent(ReceiveActivity.this, Receive2Activity.class);
                i.putExtra("PO", NomerReceipt);
                i.putExtra("SHIPMENT", NomerShipment);
                startActivity(i);

            }
        }, 1000);

    }

    void runApiNonQc(String NomerShipment,String ReceiptDate ,String ShippedDate,String ReceiptTime) {
        Connection dbConnection;
        Integer userId = 0;
        Statement statement;
        ResultSet theResultSet;
        String ReceiptNum = et_code.getText().toString();

        //Receipt
        System.out.println("Date :"+ReceiptDate+"");
        String TanggalR = ReceiptDate.substring( 0, ReceiptDate.indexOf("-"));
        System.out.println("Tgl :"+TanggalR+"");
        String BulanR = ReceiptDate.substring( ReceiptDate.indexOf("-")+1, ReceiptDate.lastIndexOf("-"));
        String TahunR = ReceiptDate.substring( ReceiptDate.lastIndexOf("-")+1, ReceiptDate.length());

        //Shipped
        System.out.println("Date :"+ShippedDate+"");
        String Tanggal = ShippedDate.substring( 0, ShippedDate.indexOf("-"));
        System.out.println("Tgl :"+Tanggal+"");
        String Bulan = ShippedDate.substring( ShippedDate.indexOf("-")+1, ShippedDate.lastIndexOf("-"));
        String Tahun = ShippedDate.substring( ShippedDate.lastIndexOf("-")+1, ShippedDate.length());

        int blnR = Integer.parseInt(BulanR);
        String BulanTextR="";
        int bln = Integer.parseInt(Bulan);
        String BulanText="";

        switch(bln){
            case 1:
                BulanText = "JAN";
                break;
            case 2:
                BulanText = "FEB";
                break;
            case 3:
                BulanText = "MAR";
                break;
            case 4:
                BulanText = "APR";
                break;
            case 5:
                BulanText = "MAY";
                break;
            case 6:
                BulanText = "JUN";
                break;
            case 7:
                BulanText = "JUL";
                break;
            case 8:
                BulanText = "AUG";
                break;
            case 9:
                BulanText = "SEP";
                break;
            case 10:
                BulanText = "OCT";
                break;
            case 11:
                BulanText = "NOV";
                break;
            case 12:
                BulanText = "DEC";
                break;
            default:
                System.out.println("Bulan Salah");
        }

        switch(blnR){
            case 1:
                BulanTextR = "JAN";
                break;
            case 2:
                BulanTextR = "FEB";
                break;
            case 3:
                BulanTextR = "MAR";
                break;
            case 4:
                BulanTextR = "APR";
                break;
            case 5:
                BulanTextR = "MAY";
                break;
            case 6:
                BulanTextR = "JUN";
                break;
            case 7:
                BulanTextR = "JUL";
                break;
            case 8:
                BulanTextR = "AUG";
                break;
            case 9:
                BulanTextR = "SEP";
                break;
            case 10:
                BulanTextR = "OCT";
                break;
            case 11:
                BulanTextR = "NOV";
                break;
            case 12:
                BulanTextR = "DEC";
                break;
            default:
                System.out.println("Bulan Salah");
        }

        System.out.println("Bln :"+BulanText+"");
        System.out.println("Thn :"+Tahun+"");
        String FinalShippedDate = ""+Tanggal+"-"+BulanText+"-"+Tahun+"";
        System.out.println("Final Shipped Date :"+FinalShippedDate+"");


//        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
//        String formattedTime = df.format(ReceiptTime);

        String FinalReceiptDate = ""+TanggalR+"-"+BulanTextR+"-"+TahunR+" "+ReceiptTime+"";
        System.out.println("Final Receipt Date :"+FinalReceiptDate+"");

        try {
            dbConnection = mConn;
            statement = dbConnection.createStatement();

            System.out.println("begin APPS.KHS_RECEIPT_PO('" + UserName + "','" + ReceiptNum + "','"+getOrgId()+"','"+getIp()+"','"+FinalReceiptDate+"','"+FinalShippedDate+"','"+NomerShipment+"'); end;");

            //API issue
            CallableStatement funcnone = dbConnection
                    .prepareCall("begin APPS.KHS_RECEIPT_PO('" + UserName + "','" + ReceiptNum + "','"+getOrgId()+"','"+getIp()+"','"+FinalReceiptDate+"','"+FinalShippedDate+"','"+NomerShipment+"'); end;");
            funcnone.executeUpdate();
            funcnone.close();


        } catch (SQLException e) {

            Toast.makeText(getBaseContext(), "Error :" + e.toString(),
                    Toast.LENGTH_LONG).show();
            Log.d("ERROR:", e.toString());

        }
    }


    int groupId() {
        try{
            String ReceiptNum = et_code.getText().toString();
            Statement statement = mConn.createStatement();
            mQuery="SELECT RTI.GROUP_ID\n" +
                    "                    FROM RCV_TRANSACTIONS_INTERFACE RTI,\n" +
                    "                    PO_HEADERS_ALL PHA\n" +
                    "              WHERE PHA.PO_HEADER_ID = RTI.PO_HEADER_ID\n" +
                    "                    AND RTI.PROCESSING_STATUS_CODE = 'PENDING'\n" +
                    "                    AND PHA.SEGMENT1 = '"+ReceiptNum+"'\n" +
                    "                    AND RTI.ITEM_ID = '"+getInvItemId()+"'";
            Log.d("request number", mQuery);
            ResultSet result = statement.executeQuery(mQuery);
            result.next();
            String resultString = result.getString(1);
            int resultInt = Integer.parseInt(resultString);
            return resultInt;
        }catch (SQLException e){
            e.printStackTrace();
        }
        Log.d("QTY", mQuery);
        return 0;
    }

    int getShipment() {
        try{
            String ReceiptNum = et_code.getText().toString();
            Statement statement = mConn.createStatement();
            mQuery="SELECT DISTINCT rsh.receipt_num\n" +
                    "           FROM rcv_shipment_lines rsl\n" +
                    "    ,rcv_shipment_headers rsh\n" +
                    "    ,po_headers_all pha\n" +
                    "          WHERE pha.segment1 = '"+ReceiptNum+"'\n" +
                    "          AND rsh.SHIPMENT_NUM = nvl('"+shipmentText+"',rsh.SHIPMENT_NUM)\n" +
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
            int resultInt = Integer.parseInt(resultString)+1;
            System.out.println("Shipment Number ="+resultInt+"");
            return resultInt;
        }catch (SQLException e){
            e.printStackTrace();
        }
        Log.d("QTY", mQuery);
        return 0;
    }

    void runApiDummy() {
        Connection dbConnection = null;
        Statement statement = null;
        ResultSet theResultSet;

        try {
            dbConnection = mConn;
            statement = dbConnection.createStatement();

            System.out.println("begin APPS.KHS_RUN_FND_RECEIVING('" + userId + "','" + groupId() + "'); end;");

            //API issue
            CallableStatement funcnone = dbConnection
                    .prepareCall("begin APPS.KHS_RUN_FND_RECEIVING('" + userId + "','" + groupId() + "'); end;");
            funcnone.executeUpdate();
            funcnone.close();

        } catch (SQLException e) {

            Toast.makeText(getBaseContext(), "Error :" + e.toString(),
                    Toast.LENGTH_LONG).show();
            Log.d("ERROR:", e.toString());

        }
    }

    public void insertToMisc(String NomerReceipt) {
        Statement stmt = null;
        Connection conn = null;

        Cursor c = helper.queryDataToTransact();
        while (c.moveToNext()) {
            String PoHeader = c.getString(0);
            String InvItemId = c.getString(1);
            String PoLine = c.getString(2);
            String QtyReceive = c.getString(3);
            System.out.println("PO:"+PoHeader+", INV ID:"+InvItemId+", PO LINE:"+PoLine+"QTY:"+QtyReceive+"");

            try {

                conn = mConn;
                stmt = conn.createStatement();

                System.out
                        .println("insert into khs_receipt_po_temp(po_header_id, inventory_item_id, quantity_receipt, po_line_id, ip_address)\n" +
                                "                        values ('" + PoHeader + "','" + InvItemId + "','" + QtyReceive + "','"+PoLine+"','"+getIp()+"')");

                stmt.executeUpdate("insert into khs_receipt_po_temp(po_header_id, inventory_item_id, quantity_receipt, po_line_id, ip_address)\n" +
                        "                        values ('" + PoHeader + "','" + InvItemId + "','" + QtyReceive + "','"+PoLine+"','"+getIp()+"')");

            } catch (Exception e) {
                e.printStackTrace();
            } finally {

            }
        }
    }


    public void DeleteFromMisc(String NomerReceipt) {
        Statement stmt = null;
        Connection conn = null;

        //String ReceiptNum = NomerReceipt.substring(3,11);
        try {

            conn = mConn;
            stmt = conn.createStatement();

            System.out
                    .println("DELETE FROM KHS_RECEIPT_PO_TEMP WHERE PO_HEADER_ID = '"+getPoHeader()+"'");

            stmt.executeUpdate("DELETE FROM KHS_RECEIPT_PO_TEMP WHERE PO_HEADER_ID = '"+getPoHeader()+"'");

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

    public boolean IsAny(String content) {
        Integer ver = 0;
        ResultSet theResultSet;
        try {
            String ReceiptNum = content;
            Statement statement = mConn.createStatement();
            theResultSet = statement.executeQuery("SELECT count(msib.SEGMENT1)\n" +
                    "FROM MTL_SYSTEM_ITEMS_B msib,\n" +
                    "            PO_HEADERS_ALL pha,\n" +
                    "            po_lines_all pla,\n" +
                    "            po_line_locations_all plla,\n" +
                    "            mtl_parameters mp,\n" +
                    "            HR_LOCATIONS hrl,\n" +
                    "            po_distributions_all pda,\n" +
                    "            per_people_f ppf\n" +
                    "            where pha.PO_HEADER_ID = pla.PO_HEADER_ID\n" +
                    "            and pla.ITEM_ID = msib.INVENTORY_ITEM_ID\n" +
                    "            and msib.ORGANIZATION_ID = plla.SHIP_TO_ORGANIZATION_ID\n" +
                    "            and pha.PO_HEADER_ID = plla.PO_HEADER_ID\n" +
                    "            and pla.PO_LINE_ID = plla.PO_LINE_ID\n" +
                    "            and plla.SHIP_TO_ORGANIZATION_ID = mp.ORGANIZATION_ID\n" +
                    "            AND plla.SHIP_TO_LOCATION_ID = hrl.LOCATION_ID (+)\n" +
                    "            AND plla.line_location_id = pda.line_location_id\n" +
                    "            AND ppf.PERSON_ID = pha.AGENT_ID\n" +
                    "            and plla.CLOSED_CODE = 'OPEN'\n" +
                    "            and plla.QUANTITY_RECEIVED < plla.QUANTITY\n" +
                    "            and mp.ORGANIZATION_CODE = '"+s_orgCode.getSelectedItem().toString()+"'\n" +
                    "            and pha.segment1 = '"+ReceiptNum+"'");
            //Log.d("Ada",mQuery);
            //ResultSet result = statement.executeQuery(mQuery);
            System.out.println(" "+theResultSet+" ");
            //result.next();
            if (theResultSet.next()) {
                ver = Integer.parseInt(theResultSet.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (ver>0){
            return true;}
        else{
            return false;}
    }

    public boolean IsAny2(String content) {
        Integer ver = 0;
        ResultSet theResultSet;
        try {
            String ReceiptNum = content;
            Statement statement = mConn.createStatement();
            theResultSet = statement.executeQuery("SELECT COUNT(rsh.RECEIPT_NUM)\n" +
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
                    "                              where pha.SEGMENT1 = '19013162' --- nomorPO\n" +
                    "                                and rsh.SHIPMENT_NUM = nvl('gdgdgdgd',rsh.SHIPMENT_NUM) --- shipmentnumber\n" +
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
                    "    )");
            //Log.d("Ada",mQuery);
            //ResultSet result = statement.executeQuery(mQuery);
            System.out.println(" "+theResultSet+" ");
            //result.next();
            if (theResultSet.next()) {
                ver = Integer.parseInt(theResultSet.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (ver>0){
            return true;}
        else{
            return false;}
    }

    public boolean isCount0(String ItemCode) {
        Integer ver = 0;
        Connection dbConnection;
        Statement statement;
        ResultSet theResultSet;

        dbConnection = mConn;
        try {
            String ReceiptNum = et_code.getText().toString();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            String code = data.getStringExtra("SCAN_RESULT");
            et_code.setText(code);
            if (IsAny(code)) {
                //Toast.makeText(this, "No data", Toast.LENGTH_LONG).show();
                //Toast.makeText(this, "IF", Toast.LENGTH_LONG).show();
                insertDb(code);
                showMessage();
            } else {
                Toast.makeText(this, "No data", Toast.LENGTH_LONG).show();
                //Toast.makeText(this, "ELSE", Toast.LENGTH_LONG).show();
//                insertDb(code);
//                showMessage();
            }
        } else if (resultCode == RESULT_CANCELED) {
            Toast.makeText(getApplicationContext(), "Pembacaan QR Code Dibatalkan..", Toast.LENGTH_LONG).show();
            recreate();
        }
    }

    void  insertDb(String code) {
        Log.d("Conn", "." + mConn);
        if (mConn == null) {
            Toast.makeText(this, "No Connection", Toast.LENGTH_SHORT).show();
            return;
        }
        String ReceiptNum = code;
        try {
            Statement statement = mConn.createStatement();
            mQuery = "SELECT DISTINCT\n" +
                    "msib.PRIMARY_UNIT_OF_MEASURE \n" +
                    "            ,pha.PO_HEADER_ID\n" +
                    "            ,pla.PO_LINE_ID\n" +
                    "            ,plla.LINE_LOCATION_ID\n" +
                    "            ,mp.ORGANIZATION_CODE\n" +
                    "            ,msib.SEGMENT1\n" +
                    "            ,hrl.LOCATION_CODE\n" +
                    "            ,mp.ORGANIZATION_ID\n" +
                    "            ,plla.CLOSED_CODE\n" +
                    "            ,plla.QUANTITY QUANTITY_ORDERED\n" +
                    "            ,plla.QUANTITY_RECEIVED\n" +
                    "            ,pla.LINE_NUM\n" +
                    "            ,plla.SHIPMENT_NUM\n" +
                    "            ,pda.DESTINATION_ORGANIZATION_ID\n" +
                    "            ,plla.ORG_ID\n" +
                    "            ,msib.INVENTORY_ITEM_ID\n" +
                    "            ,msib.DESCRIPTION\n" +
                    "            ,plla.NOTE_TO_RECEIVER\n" +
                    "            ,ppf.FULL_NAME REQUESTOR\n" +
                    "            ,hrl.LOCATION_ID\n" +
                    "FROM MTL_SYSTEM_ITEMS_B msib,\n" +
                    "            PO_HEADERS_ALL pha,\n" +
                    "            po_lines_all pla,\n" +
                    "            po_line_locations_all plla,\n" +
                    "            mtl_parameters mp,\n" +
                    "            HR_LOCATIONS hrl,\n" +
                    "            po_distributions_all pda,\n" +
                    "            per_people_f ppf\n" +
                    "WHERE pha.PO_HEADER_ID = pla.PO_HEADER_ID\n" +
                    "            and pla.ITEM_ID = msib.INVENTORY_ITEM_ID\n" +
                    "            and msib.ORGANIZATION_ID = plla.SHIP_TO_ORGANIZATION_ID\n" +
                    "            and pha.PO_HEADER_ID = plla.PO_HEADER_ID\n" +
                    "            and pla.PO_LINE_ID = plla.PO_LINE_ID\n" +
                    "            and plla.SHIP_TO_ORGANIZATION_ID = mp.ORGANIZATION_ID\n" +
                    "            AND plla.SHIP_TO_LOCATION_ID = hrl.LOCATION_ID (+)\n" +
                    "            AND plla.line_location_id = pda.line_location_id\n" +
                    "            AND ppf.PERSON_ID = pha.AGENT_ID\n" +
                    "            and plla.CLOSED_CODE = 'OPEN'\n" +
                    "            and plla.QUANTITY_RECEIVED < plla.QUANTITY\n" +
                    "            and mp.ORGANIZATION_CODE = '"+s_orgCode.getSelectedItem().toString()+"'\n" +
                    "            and pha.segment1 = '"+ReceiptNum+"'";
            Log.d("QUERY", mQuery);
            ResultSet result = statement.executeQuery(mQuery);

            while (result.next()) {
                ContentValues values = new ContentValues();
                values.put("PRIMARY_UNIT_OF_MEASURE", result.getString(1));
                values.put("PO_HEADER_ID", result.getString(2));
                values.put("PO_LINE_ID", result.getString(3));
                values.put("LINE_LOCATION_ID", result.getString(4));
                values.put("ORGANIZATION_CODE", result.getString(5));
                values.put("SEGMENT1", result.getString(6));
                values.put("LOCATION_CODE", result.getString(7));
                values.put("ORGANIZATION_ID", result.getString(8));
                values.put("CLOSED_CODE", result.getString(9));
                values.put("QUANTITY_ORDERED", result.getString(10));
                values.put("QUANTITY_RECEIVED", result.getString(11));
                values.put("LINE_NUM", result.getString(12));
                values.put("SHIPMENT_NUM", result.getString(13));
                values.put("DESTINATION_ORGANIZATION_ID", result.getString(14));
                values.put("ORG_ID", result.getString(15));
                values.put("INVENTORY_ITEM_ID", result.getString(16));
                values.put("DESCRIPTION", result.getString(17));
                values.put("NOTE", result.getString(18));
                values.put("REQUESTOR", result.getString(19));
                values.put("LOCATION_ID", result.getString(20));
                values.put("FLAG", "N");
                values.put("QTY_RECEIVE", "0");
                Log.d("VALUES", values.toString());
                helper.insertDataRcv(values);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    int getQtyDeliver(String ItemCode) {
        try {
            String ReceiptNum = et_code.getText().toString();
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

    public String spinner() {
        String hasil = "";
        Connection dbConnection = null;
        Statement statement = null;
        ResultSet theResultSet;

        dbConnection = mConn;
        try {


            statement = dbConnection.createStatement();
            System.out.println("select distinct ood.ORGANIZATION_CODE from org_organization_definitions ood where ood.DISABLE_DATE is null ORDER BY ood.ORGANIZATION_CODE asc");
            theResultSet = statement.executeQuery("select distinct ood.ORGANIZATION_CODE from org_organization_definitions ood where ood.DISABLE_DATE is null ORDER BY ood.ORGANIZATION_CODE asc");
            while (theResultSet.next()) {
                listSpinner.add(theResultSet.getString(1));
            }
//            listSpinner.add(0, "-Pilih Locator-");
        } catch (SQLException e) {

            e.printStackTrace();
        }

        return hasil;
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
                new ManagerSessionUserOracle(ReceiveActivity.this).logoutUser();
                Intent i = new Intent(ReceiveActivity.this, LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

//    public void refreshData(){
//        adapter = new CursorAdapterRcv(this, helper.selectRcv());
//        hasildata.setAdapter(adapter);
//    }


}

