package com.gunerfaruk.marketreceipts.ui.home;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.gunerfaruk.marketreceipts.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HomeFragment extends Fragment {

    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private static final int MY_Storage_PERMISSION_CODE = 101;

    private HomeViewModel homeViewModel;
    private Button chooseButton,uploadButton, cameraButton;
    private ImageView imageView;
    private Uri takenPicUri;
    private ContentValues values;
    private ScaleGestureDetector scaleGestureDetector;
    private float mScaleFactor = 1.0f;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        chooseButton = root.findViewById(R.id.btnChoose);
        uploadButton = root.findViewById(R.id.btnUpload);
        cameraButton = root.findViewById(R.id.btnCamera);
        imageView = root.findViewById(R.id.ivPhoto);

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hasPermission())
                {
                    values = new ContentValues();
                    values.put(MediaStore.Images.Media.TITLE, "New Picture");
                    values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
                    takenPicUri =getContext().getContentResolver().insert(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, takenPicUri);
                    startActivityForResult(intent, CAMERA_REQUEST);
                }
            }
        });


        scaleGestureDetector = new ScaleGestureDetector(getContext(), new ScaleListener());

        root.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                scaleGestureDetector.onTouchEvent(event);
                return true;
            }
        });

        return root;
    }

    //ImageView'de eklenen resmi yakınlaştırık kontrol etmek için eklendi.
    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            mScaleFactor *= scaleGestureDetector.getScaleFactor();
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 10.0f));
            imageView.setScaleX(mScaleFactor);
            imageView.setScaleY(mScaleFactor);
            return true;
        }
    }

    //Fotoğraf çektikten sonra ok ile gelen event ile imageview'e resim atılıyor.
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK)
        {
            Bitmap thumbnail = null;
            try {
                thumbnail = MediaStore.Images.Media.getBitmap(
                        getContext().getContentResolver(), takenPicUri);
                thumbnail = rotate(thumbnail, 90);
                imageView.setImageBitmap(thumbnail);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    //Uygulamada gerekli izin kontrolleri için kullanılıyor.
    private boolean hasPermission(){
        if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) ==
           PackageManager.PERMISSION_DENIED)
        {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[] {Manifest.permission.CAMERA},
                    MY_CAMERA_PERMISSION_CODE);
            return false;
        }else if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(getActivity(),
                    new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_Storage_PERMISSION_CODE);
            return false;
        }
        return true;
    }

    //Kamera'dan alınan fotoğrafı düzeltmek için döndürmek gerekiyor.
    public static Bitmap rotate(Bitmap bitmap, int degree) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        Matrix mtx = new Matrix();
        mtx.setRotate(degree);

        return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
    }

}