package com.apps.manu.backgroundservices;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private TextView txtView;
    private NotificationReceiver nReceiver;
    private  BroadcastReceiver myreceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtView = (TextView) findViewById(R.id.textView);
        nReceiver = new NotificationReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.apps.manu.backgroundservices.NOTIFICATION_LISTENER_EXAMPLE");
        registerReceiver(nReceiver, filter);

        myreceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                Toast.makeText(MainActivity.this, "received the notification", Toast.LENGTH_SHORT).show();

            }
        };

        IntentFilter changeColorFilter = new IntentFilter("com.apps.manu.backgroundservices.NOTIFICATION_LISTENER_SERVICE_EXAMPLE");

        //Finally, register the receiver.
        registerReceiver(myreceiver, changeColorFilter);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(nReceiver);
    }

    public void buttonClicked(View v){

        if(v.getId() == R.id.btnCreateNotify){
            NotificationManager nManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            NotificationCompat.Builder ncomp = new NotificationCompat.Builder(this);
            ncomp.setContentTitle("My Notification");
            ncomp.setContentText("Notification Listener Service Example");
            ncomp.setTicker("Notification Listener Service Example");
            ncomp.setSmallIcon(android.R.drawable.sym_def_app_icon);
            ncomp.setAutoCancel(true);
            nManager.notify((int) System.currentTimeMillis(), ncomp.build());

            Intent i = new Intent("com.apps.manu.backgroundservices.NOTIFICATION_LISTENER_SERVICE_EXAMPLE");
            i.putExtra("command", "notify");
            sendBroadcast(i);
        }
        else if(v.getId() == R.id.btnClearNotify){
            Intent i = new Intent("com.apps.manu.backgroundservices.NOTIFICATION_LISTENER_SERVICE_EXAMPLE");
            i.putExtra("command", "clearall");
            sendBroadcast(i);
            Log.d("broad", "sent broad clear" + i);

        }
        else if(v.getId() == R.id.btnListNotify){
            Intent i = new Intent("com.apps.manu.backgroundservices.NOTIFICATION_LISTENER_SERVICE_EXAMPLE");
            i.putExtra("command","list");
            sendBroadcast(i);
            Log.d("broad", "sent broad notify"+i);
        }

    }

    class NotificationReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            String temp = intent.getStringExtra("notification_event") + "n" + txtView.getText();
            txtView.setText(temp);
        }
    }

}