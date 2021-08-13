package com.quick.receivingtransaction;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class MenuActivity extends AppCompatActivity {

    ManagerSessionUserOracle session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        setTitle("Pilih Fitur");

        session = new ManagerSessionUserOracle(this);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_awal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_logout:
                logoutApp();
                break;
        }
        return true;
    }

    public void logoutApp() {
        session.logoutUser();
        finish();
    }

    public void receive(View view) {
        Intent i = new Intent(MenuActivity.this, ReceiveActivity.class);
        startActivity(i);
    }

    public void transfer(View view) {
        Intent i = new Intent(MenuActivity.this, TransferActivity.class);
        startActivity(i);
    }

    public void accept(View view) {
        Intent i = new Intent(MenuActivity.this, AcceptActivity.class);
        startActivity(i);
    }

    public void delives(View view) {
        Intent i = new Intent(MenuActivity.this, DelivesActivity.class);
        startActivity(i);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed(){
//        Intent a = new Intent(Intent.ACTION_MAIN);
//        a.addCategory(Intent.CATEGORY_HOME);
//        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(a);
        recreate();
        finish();
    }
}
