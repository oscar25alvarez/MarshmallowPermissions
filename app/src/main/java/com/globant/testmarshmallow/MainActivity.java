package com.globant.testmarshmallow;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity
    extends AppCompatActivity
    implements View.OnClickListener{

    private static final int REQUEST_CODE_ASK_PERMISSIONS_CALENDAR = 126; //REQUEST CODE 1 TO 255
    private static final int REQUEST_CODE_ASK_PERMISSIONS_SMS = 128;
    private static final int REQUEST_CODE_ASK_PERMISSIONS_MMS = 132;
    private static final int REQUEST_CODE_ASK_PERMISSIONS_MULTIPLE = 134;
    private Button buttonCalendar;
    private Button buttonSms;
    private Button buttonTwoPermissions;
    private Button buttonMmsPermission;

    private void findViews() {
        buttonCalendar = (Button)findViewById( R.id.button_calendar );
        buttonSms = (Button)findViewById(R.id.button_sms );
        buttonTwoPermissions = (Button)findViewById( R.id.button_two_permissions );
        buttonMmsPermission = (Button)findViewById( R.id.button_mms_permission );

        buttonCalendar.setOnClickListener( this );
        buttonSms.setOnClickListener( this );
        buttonTwoPermissions.setOnClickListener( this );
        buttonMmsPermission.setOnClickListener( this );
    }

    @Override
    public void onClick(View v) {
        if ( v == buttonCalendar ) {
            int hasReadCalendarPermission = ContextCompat.checkSelfPermission(MainActivity.this,
                                                                              Manifest.permission.READ_CALENDAR);
            if (hasReadCalendarPermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this,
                                                  new String[]{Manifest.permission.READ_CALENDAR},
                                                  REQUEST_CODE_ASK_PERMISSIONS_CALENDAR);
            } else {
                // READ CONTACTS WITHOUT PROBLEM
                readCalendar();
            }

        }
        if ( v == buttonSms ) {
            // Handle clicks for buttonSms
            int hasReadSMSPermission = ContextCompat.checkSelfPermission(MainActivity.this,
                                                                              Manifest.permission.READ_SMS);
            if (hasReadSMSPermission != PackageManager.PERMISSION_GRANTED) {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_SMS)) {
                    showNeedsPermissionMessage("YOU CAN'T WORK IF YOU DENIED THIS PERMISSION.",
                                               new DialogInterface.OnClickListener() {
                                                   @Override
                                                   public void onClick(DialogInterface dialog,
                                                                       int which) {
                                                       startActivityForResult(new Intent(Settings.ACTION_APPLICATION_SETTINGS), 0);
                                                   }
                                               });
                    return;
                }
                ActivityCompat.requestPermissions(MainActivity.this,
                                                  new String[]{Manifest.permission.READ_SMS},
                                                  REQUEST_CODE_ASK_PERMISSIONS_SMS);
            } else {
                // READ SMS WITHOUT PROBLEM
                readSMS();
            }
        }
        if (v == buttonTwoPermissions) {
            List<String> permissionsNeeded = new ArrayList<>();
            final List<String> permissionsList = new ArrayList<>();//***

            if (!addPermission(permissionsList, Manifest.permission.ACCESS_FINE_LOCATION, MainActivity.this))
                permissionsNeeded.add("GPS");
            if (!addPermission(permissionsList, Manifest.permission.BODY_SENSORS, MainActivity.this))
                permissionsNeeded.add("Body Sensors");
            if (!addPermission(permissionsList, Manifest.permission.READ_EXTERNAL_STORAGE, MainActivity.this))
                permissionsNeeded.add("Read External Storage");

            if (permissionsList.size() > 0) {
                if (permissionsNeeded.size() > 0) {
                    // Need Rationale
                    String message = "Se necesitan permisos para " + permissionsNeeded.get(0);
                    for (int i = 1; i < permissionsNeeded.size(); i++)
                        message = message + ", " + permissionsNeeded.get(i);
                    showNeedsPermissionMessage(message,
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                startActivityForResult(new Intent(Settings.ACTION_APPLICATION_SETTINGS), 0);
                                            }
                                        });
                    return;
                }
                ActivityCompat.requestPermissions(MainActivity.this,
                                                  permissionsList.toArray(new String[permissionsList.size()]),
                                   REQUEST_CODE_ASK_PERMISSIONS_MULTIPLE);
                return;
            }

            readMultiple();

        }
        if (v == buttonMmsPermission) {
            // Handle clicks for buttonMMS
            int hasReadSMSPermission = ContextCompat.checkSelfPermission(MainActivity.this,
                                                                         Manifest.permission.RECEIVE_MMS);
            if (hasReadSMSPermission != PackageManager.PERMISSION_GRANTED) {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.RECEIVE_MMS)) {
                    showNeedsPermissionMessage("YOU CAN'T WORK IF YOU DENIED THIS PERMISSION.",
                                               new DialogInterface.OnClickListener() {
                                                   @Override
                                                   public void onClick(DialogInterface dialog,
                                                                       int which) {
                                                       startActivityForResult(new Intent(Settings.ACTION_APPLICATION_SETTINGS), 0);
                                                   }
                                               });
                    return;
                }
                ActivityCompat.requestPermissions(MainActivity.this,
                                                  new String[]{Manifest.permission.RECEIVE_MMS},
                                                  REQUEST_CODE_ASK_PERMISSIONS_MMS);
            } else {
                // READ SMS WITHOUT PROBLEM
                readSMS();
            }
        }
    }

    private boolean addPermission(List<String> permissionsList, String permission, AppCompatActivity activity) {
        if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            // Check for Rationale Option
            if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, permission))
                return false;
        }
        return true;
    }

    private void readSMS() {
        Toast.makeText(MainActivity.this, "I CAN READ SMS :D.", Toast.LENGTH_SHORT).show();
    }

    private void readCalendar() {
        Toast.makeText(MainActivity.this, "I CAN READ CONTACTS :D.", Toast.LENGTH_SHORT).show();
    }

    private void readPhoneAndMore() {
        Toast.makeText(MainActivity.this, "I CAN READ PHONE AND MORE :D.", Toast.LENGTH_SHORT).show();
    }

    private void readMMS() {
        Toast.makeText(MainActivity.this, "I CAN READ MMS :D.", Toast.LENGTH_SHORT).show();
    }

    private void readMultiple() {
        Toast.makeText(MainActivity.this, "I CAN READ BODY SENSORS, EXTERNAL STORAGE AND LOCATION :D.", Toast.LENGTH_SHORT).show();
    }

    private void permissionsNotGranted() {
        Toast.makeText(MainActivity.this, "PFF NO PERMISSION.", Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        findViews();
    }

    private void showNeedsPermissionMessage(String message, DialogInterface.OnClickListener listener) {
        new AlertDialog.Builder(MainActivity.this)
            .setMessage(message)
            .setPositiveButton("Go to Settings", listener)
            .setNegativeButton("Cancel", null)
            .create()
            .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
        case REQUEST_CODE_ASK_PERMISSIONS_CALENDAR:
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                readCalendar();
            } else {
                permissionsNotGranted();
            }
            break;
        case REQUEST_CODE_ASK_PERMISSIONS_SMS:
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                readSMS();
            } else {
                permissionsNotGranted();
            }
            break;
        case REQUEST_CODE_ASK_PERMISSIONS_MULTIPLE:
            //Two at same time
            boolean result = true;
            for (int i : grantResults) {
                if (i == -1) {
                    result = false;
                    break;
                }
            }
            if (result) {
                readPhoneAndMore();
            } else {
                permissionsNotGranted();
            }
            break;
        case REQUEST_CODE_ASK_PERMISSIONS_MMS:
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                readMMS();
            } else {
                permissionsNotGranted();
            }
            break;

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}
