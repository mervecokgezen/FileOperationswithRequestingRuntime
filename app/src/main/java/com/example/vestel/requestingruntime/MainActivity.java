package com.example.vestel.requestingruntime;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.logging.Logger;


public class MainActivity extends AppCompatActivity {

    Button  btn_location, btn_writestorage, btn_readstorage, btn_call, btn_camera, btn_account;
    Button  btn_save, btn_sdcardwritepublic, btn_sdcardreadprivate,
            btn_sdcardreadpublic, btn_savesharedprivate, btn_readsharedprivate;
    EditText edt_data, edt_filename, edt_readfilename, edt_shareddata;
    TextView tv_data;

    private         StringBuilder text, publicfile;
    static final    Integer WRITE_EXST = 0x3;
    static final    Integer READ_EXST = 0x4;

    GoogleApiClient client;

    private String data_private, filename;
    public  String data_public;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edt_data                = (EditText)findViewById(R.id.edt_data);
        edt_filename            = (EditText)findViewById(R.id.edt_filename);
        edt_readfilename        = (EditText)findViewById(R.id.edt_readfilename);
        edt_shareddata          = (EditText)findViewById(R.id.edt_shareddata);
        tv_data                 = (TextView)findViewById(R.id.tv_data);

        btn_save                = (Button)findViewById(R.id.btn_save);
        btn_sdcardwritepublic   = (Button)findViewById(R.id.btn_sdcardwritepublic);
        btn_sdcardreadprivate   = (Button)findViewById(R.id.btn_sdcardreadprivate);
        btn_sdcardreadpublic    = (Button)findViewById(R.id.btn_sdcardreadpublic);
        btn_savesharedprivate   = (Button)findViewById(R.id.btn_savesharedprivate);
        btn_readsharedprivate   = (Button)findViewById(R.id.btn_readsharedprivate);

        final SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);

        askForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,WRITE_EXST);
        askForPermission(Manifest.permission.READ_EXTERNAL_STORAGE,READ_EXST);

        client = new GoogleApiClient.Builder(this)
                .addApi(AppIndex.API)
                .addApi(LocationServices.API)
                .build();

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                filename        = edt_filename.getText().toString();

                if(filename.isEmpty()){
                    Toast.makeText(MainActivity.this, "Filename can not be empty!", Toast.LENGTH_LONG).show();

                }else {
                try {
                    data_private = edt_data.getText().toString();
                    File dataFile = new File("mnt/sdcard/"+filename);
                    dataFile.createNewFile();
                    FileOutputStream fOut = new FileOutputStream(dataFile);
                    OutputStreamWriter myOutWriter =
                            new OutputStreamWriter(fOut);
                    myOutWriter.append(data_private);
                    myOutWriter.close();
                    fOut.close();
                    Toast.makeText(MainActivity.this, filename+" named file was created.", Toast.LENGTH_LONG).show();

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Opppps!", Toast.LENGTH_LONG).show();
                }}
            }
        });
        btn_sdcardwritepublic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                data_public     = edt_data.getText().toString();
                filename        = edt_filename.getText().toString();

                if(filename.isEmpty()){
                    Toast.makeText(MainActivity.this, "Filename can not be empty!", Toast.LENGTH_LONG).show();

                }else {
                try {
                    data_private = edt_data.getText().toString();
                    File dataFile = new File("mnt/sdcard/"+filename);
                    dataFile.createNewFile();
                    FileOutputStream fOut = new FileOutputStream(dataFile);
                    OutputStreamWriter myOutWriter =
                            new OutputStreamWriter(fOut);
                    myOutWriter.append(data_private);
                    myOutWriter.close();
                    fOut.close();
                    Toast.makeText(MainActivity.this, filename+" named file was created.", Toast.LENGTH_LONG).show();


                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Opppps!", Toast.LENGTH_LONG).show();
                }
                }
            }
        });
        btn_sdcardreadprivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String readfilename = edt_readfilename.getText().toString();

                if (readfilename.isEmpty()){
                    Toast.makeText(MainActivity.this, "File name can not be empty",Toast.LENGTH_LONG).show();
                }else{

                File sdcard = Environment.getExternalStorageDirectory();

                File file = new File(sdcard,readfilename);

                text = new StringBuilder();

                try {
                    BufferedReader br = new BufferedReader(new FileReader(file));
                    String line;

                    while ((line = br.readLine()) != null) {
                        text.append(line);
                        text.append('\n');
                    }
                    br.close();
                }
                catch (IOException e) {
                    Toast.makeText(MainActivity.this, "File not found!", Toast.LENGTH_LONG).show();
                }

                tv_data.setText(text);

            }
            }
        });
        btn_sdcardreadpublic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String readfilename = edt_readfilename.getText().toString();

                if (readfilename.isEmpty()){
                    Toast.makeText(MainActivity.this, "File name can not be empty",Toast.LENGTH_LONG).show();
                }else {

                    File sdcard = Environment.getExternalStorageDirectory();

                    //Get the text file
                    File file = new File(sdcard, readfilename);

                    //Read text from file
                    publicfile = new StringBuilder();

                    try {
                        BufferedReader br = new BufferedReader(new FileReader(file));
                        String line;

                        while ((line = br.readLine()) != null) {
                            publicfile.append(line);
                            publicfile.append('\n');
                        }
                        br.close();
                    } catch (IOException e) {
                        //You'll need to add proper error handling here
                        Toast.makeText(MainActivity.this, "File not found!", Toast.LENGTH_LONG).show();
                    }

                    tv_data.setText(publicfile);
                }
            }
        });
        btn_savesharedprivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPref.edit();
                data_private = edt_shareddata.getText().toString();
                if(data_private.isEmpty()){
                    Toast.makeText(MainActivity.this, "Data can not be empty!", Toast.LENGTH_LONG).show();
                }else{
                    editor.putString("Data", data_private);
                    editor.commit();
                }

            }
        });
        btn_readsharedprivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String data = sharedPref.getString("Data","Data is Null");
                tv_data.setText(data);

            }
        });
    }

    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permission)) {

                 ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);
            }
        } else {

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED){
            switch (requestCode) {
                case 3:


                    break;
                //Read External Storage
                case 4:
                    Intent imageIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(imageIntent, 11);
                    break;

            }

            Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        client.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        client.disconnect();
    }

  }
