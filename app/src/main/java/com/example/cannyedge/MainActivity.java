package com.example.cannyedge;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget .Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton picker;
    ImageView image;
    Uri imge;
    Uri imageUri;
    Mat obj;
    Mat mat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        picker = findViewById(R.id.picker);
        image = findViewById(R.id.img);
        picker.setOnClickListener(view -> {
            FromCard();
            // startActivityForResult(Intent.createChooser(intent, "Select Picture"), 200);
        });
        OpenCVLoader.initDebug();
    }
    public void FromCamera() {

        Log.i("camera", "startCameraActivity()");
        Intent intent = new Intent(
                android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 1);

    }

    public void FromCard() {
        Intent i = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, 2);
    }
    @Override
    protected void onActivityResult(int requestCode , int resultCode , Intent data) {
        if (resultCode == RESULT_OK) {
            imge= data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imge);
                image.setImageBitmap(bitmap);
                mat = new Mat();
                Bitmap bmp32 = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                Utils.bitmapToMat(bmp32, mat);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        super.onActivityResult(requestCode, resultCode, data);

    }
    public void applyFilter(View v){
        Mat img = mat;



        Imgproc.cvtColor(img, img, Imgproc.COLOR_RGB2BGRA);

        Mat img_result = img.clone();
        Imgproc.Canny(img, img_result, 50, 150);
        Bitmap img_bitmap = Bitmap.createBitmap(img_result.cols(), img_result.rows(),Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(img_result, img_bitmap);
        ImageView imageView = findViewById(R.id.img);
        imageView.setImageBitmap(img_bitmap);
    }
}