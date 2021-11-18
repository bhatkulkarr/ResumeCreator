package com.example.resumecreator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity {


    EditText nameET;
    EditText mobileET;
    EditText emailET;
    EditText desigET;
    EditText orgET;
    EditText linkdeinET;
    EditText profexET;
    EditText summaryET;
    Button genratecvBtn;
    private static final int PERMISSION_REQUEST_CODE = 200;
    Bitmap bmp, scaledbmp;
    int pageHeight = 1120;
    int pagewidth = 792;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nameET = (EditText)findViewById(R.id.nameEt);
        mobileET = (EditText)findViewById(R.id.mobileNoEt);
        emailET = (EditText)findViewById(R.id.emailEt);
        desigET = (EditText)findViewById(R.id.desigEt);
        orgET = (EditText)findViewById(R.id.orgnizaNameEt);
        linkdeinET = (EditText)findViewById(R.id.linkdeinProfileEt);
        profexET = (EditText)findViewById(R.id.proffexpEt);
        summaryET = (EditText)findViewById(R.id.summaryEt);
        genratecvBtn = (Button)findViewById(R.id.genrate_btn);
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.jade);
        scaledbmp = Bitmap.createScaledBitmap(bmp, 140, 140, false);

        if (checkPermission()) {
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
        } else {
            requestPermission();
        }


        genratecvBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                genratePDF();
            }
        });

    }

    private boolean checkPermission() {
        // checking of permissions.
        int permission1 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int permission2 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        // requesting permissions if not provided.
        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }



    private void genratePDF(){
        PdfDocument pdfDocument = new PdfDocument();
       // Paint paint = new Paint();
        Paint title = new Paint();
        PdfDocument.PageInfo mypageInfo = new PdfDocument.PageInfo.Builder(pagewidth, pageHeight, 1).create();
        PdfDocument.Page myPage = pdfDocument.startPage(mypageInfo);
        mypageInfo.getContentRect();


        Canvas canvas = myPage.getCanvas();
       // canvas.drawBitmap(scaledbmp, 112, 40, paint);
        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        title.setTextSize(18);
        title.setColor(ContextCompat.getColor(this, R.color.black));
        canvas.drawText(nameET.getText().toString(), 109, 100, title);
        canvas.drawText(desigET.getText().toString() + "at" + orgET.getText().toString(), 109, 120, title);
        canvas.drawText(mobileET.getText().toString(), 650, 100, title);
        canvas.drawLine(87, 797,87, 482,title);
        canvas.drawText(emailET.getText().toString(), 600, 120, title);


        title.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        title.setColor(ContextCompat.getColor(this, R.color.black));
        title.setTextSize(15);
        pdfDocument.finishPage(myPage);

        File file = new File(Environment.getExternalStorageDirectory(), "JadeResume.pdf");
        try {
            // after creating a file name we will
            // write our PDF file to that location.
            pdfDocument.writeTo(new FileOutputStream(file));

            // below line is to print toast message
            // on completion of PDF generation.
            Toast.makeText(MainActivity.this, "PDF file generated successfully.", Toast.LENGTH_SHORT).show();
            Toast.makeText(MainActivity.this, file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            // below line is used
            // to handle error
            e.printStackTrace();
        }
        pdfDocument.close();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0) {

                // after requesting permissions we are showing
                // users a toast message of permission granted.
                boolean writeStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean readStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                if (writeStorage && readStorage) {
                    Toast.makeText(this, "Permission Granted..", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permission Denined.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }




}