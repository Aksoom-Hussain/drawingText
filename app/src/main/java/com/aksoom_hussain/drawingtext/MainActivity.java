package com.aksoom_hussain.drawingtext;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;

import static android.view.View.TEXT_ALIGNMENT_GRAVITY;

public class MainActivity extends AppCompatActivity {
    Button btnLoadImage1;
    TextView textSource1;
    EditText editTextCaption;
    Button btnProcessing;
    ImageView imageResult;

    final int RQS_IMAGE1 =1;
    Uri source;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnLoadImage1 =(Button) findViewById(R.id.loadImage1);
        textSource1 = (TextView) findViewById(R.id.sourceUri1);
        editTextCaption = (EditText) findViewById(R.id.caption);
        btnProcessing = (Button) findViewById(R.id.processing);
        imageResult = (ImageView) findViewById(R.id.source);

        btnLoadImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,RQS_IMAGE1);

            }
        });

        btnProcessing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (source!=null){
                    Bitmap processedBitmap = ProcessingBitmap();
                    if (processedBitmap != null){

                        imageResult.setImageBitmap(processedBitmap);
                        Toast.makeText(getApplicationContext(),"Done",Toast.LENGTH_LONG).show();
                }else {
                        Toast.makeText(getApplicationContext(),"Something went wrong ! ",Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(),"Select both image ",Toast.LENGTH_LONG).show();

                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            switch (requestCode){
                case RQS_IMAGE1:
                    source = data.getData();
                    textSource1.setText(source.toString());

            }
        }
    }

    private Bitmap ProcessingBitmap(){
        Bitmap bml = null;
        Bitmap newBitmap = null;

        try{
            bml = BitmapFactory.decodeStream(getContentResolver().openInputStream(source));
            Bitmap.Config config = bml.getConfig();
            if (config==null)
            {config = Bitmap.Config.ARGB_8888;}
            newBitmap =Bitmap.createBitmap(bml.getWidth(),bml.getHeight(),config);
            Canvas newCanvas = new Canvas(newBitmap);

            newCanvas.drawBitmap(bml,0,0,null);
            String captionString =editTextCaption.getText().toString();
            if(captionString !=null) {
                Paint paintText = new Paint(Paint.ANTI_ALIAS_FLAG);

                paintText.setTextAlign(Paint.Align.CENTER);
                paintText.setColor(Color.BLACK);
                paintText.setTextSize(100);
                paintText.setStyle(Paint.Style.FILL);
                paintText.setShadowLayer(10f,10f,10f, Color.BLACK);

                Rect rectText = new Rect();
                paintText.getTextBounds(captionString, 0, captionString.length(), rectText);
                newCanvas.drawText(captionString, 0, captionString.length(), paintText);
                Toast.makeText(getApplicationContext(),"DrawText: "+captionString, Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getApplicationContext(),"Cation Empty!", Toast.LENGTH_LONG).show();

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return newBitmap;
    }

}
