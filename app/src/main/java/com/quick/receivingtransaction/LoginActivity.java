package com.quick.receivingtransaction;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LoginActivity extends AppCompatActivity {

    TextInputLayout til_username, til_password;
    EditText et_username, et_password;
    RadioButton rb_dev, rb_prod;
    Button b_login;
    Koneksi konek;
    ManagerSessionUserOracle session;
    String cName, cPort, cSid, cUsername, cPassword,res="0";
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        progressDialog = new ProgressDialog(this);
        new ModuleTool().allowNetworkOnMainThread();
        konek = new Koneksi();

        getSupportActionBar().hide();

        session = new ManagerSessionUserOracle(this);
        if (session.isUserLogin()) {
            Intent i = new Intent(getApplicationContext(), MenuActivity.class);
//            i.putExtra("locator", et_username.getText().toString());
            startActivity(i);
            finish();
        }

        til_username = (TextInputLayout) findViewById(R.id.til_username);
        til_password = (TextInputLayout) findViewById(R.id.til_password);
        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
        rb_dev = (RadioButton) findViewById(R.id.rb_dev);
        rb_prod = (RadioButton) findViewById(R.id.rb_prod);
        b_login = (Button) findViewById(R.id.b_login);

        rb_dev.setChecked(true);

        b_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username, password;
                username = et_username.getText().toString();
                password = et_password.getText().toString();

                if (rb_dev.isChecked()){
                    cName = "192.168.7.3";
                    cPort = "1522";
                    cSid = "DEV";
                    cUsername = "APPS";
                    cPassword = "APPS";
//                    cobaCekPassword(username,password);
//                    checkPassword(username,password);
                }

                if (rb_prod.isChecked()){
                    cName = "192.168.7.1";
                    cPort = "1521";
                    cSid = "PROD";
                    cUsername = "APPS";
                    cPassword = "APPS";
//                    cobaCekPassword(username,password);
//                    checkPassword(username,password);
                }

                if (!rb_dev.isChecked()&&rb_prod.isChecked()){
                    Toast.makeText(LoginActivity.this, "Pilih Dev / Prod", Toast.LENGTH_SHORT).show();
                }

                if(checkPassword(username,password)){
                    session.createUserSession(username, password, getUserId(username),cName, cPort, cSid,cUsername, cPassword);
                    recreate();
                }
            }
        });
    }

    private boolean checkPassword(String username, String password){
        til_username.setError(null);
        til_password.setError(null);
        Connection mConn = new Koneksi().getConnection(cName,cPort,cSid,cUsername, cPassword);
        Toast.makeText(this, cSid, Toast.LENGTH_SHORT).show();
        try{
            String mQuery = "SELECT xxta_get ('APPS', encrypted_user_password ) password \n" +
                    "FROM fnd_user WHERE user_name = '"+username+"'";
            Statement statement = mConn.createStatement();
            ResultSet result = statement.executeQuery(mQuery);
            if(result.next()){
                if(result.getString(1).equals(password)){
                    String name = et_username.getText().toString();
                    Toast.makeText(this, "Selamat datang "+name, Toast.LENGTH_SHORT).show();
                    return true;
                }else {
                    til_password.setError("Password salah");
                    //Memberikan alert saat salah password
                }
            }else {
                til_username.setError("Username tidak terdaftar");
                //Memberikan alert saat salah username
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    String getUserId(String username){
        Connection conn = new Koneksi().getConnection(cName,cPort,cSid,cUsername,cPassword);
        Statement statement;
        ResultSet result;
        try{
            statement = conn.createStatement();
            String Query = "SELECT user_id FROM fnd_user \n" +
                    "WHERE user_name = '"+username+"'";
            result = statement.executeQuery(Query);
            if(result.next()) return result.getString(1);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return "";
    }
}
