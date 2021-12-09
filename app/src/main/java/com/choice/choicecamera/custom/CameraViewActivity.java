package com.choice.choicecamera.custom;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.PointF;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.choice.choicecamera.R;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.FileCallback;
import com.otaliastudios.cameraview.PictureResult;
import com.otaliastudios.cameraview.controls.Facing;
import com.otaliastudios.cameraview.controls.PictureFormat;
import com.otaliastudios.cameraview.filter.Filter;
import com.otaliastudios.cameraview.filter.Filters;
import com.otaliastudios.cameraview.frame.Frame;
import com.otaliastudios.cameraview.frame.FrameProcessor;
import com.otaliastudios.cameraview.gesture.Gesture;
import com.otaliastudios.cameraview.gesture.GestureAction;
import com.otaliastudios.cameraview.size.Size;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class CameraViewActivity extends AppCompatActivity {

    CameraView cameraView;
    ImageView switch_camera, focusView, filterButton, upcomming, bread, nut;

    FilterAdapter filterAdapter;
    RecyclerView filterSet;
    float upcomingx;
    int screenwidth;
    int screenheight;
    float upcomingy;
    float breadx;
    float bready;
    float nutx;
    Handler handler = new Handler();
    float nuty;

    Context context;
    List<Filter> cameraFilters = new ArrayList<>();
    String outputFile;

    File theFile;
    String[] allpermissions = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_view);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        viewByID();

        if (!allPermissionGranted()) {
            requestNeededPermision();
        }
        ContentResolver resolver = getContentResolver();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/ChoiceCamera");
        String path = String.valueOf(resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues));

        theFile = new File(path);


        if (!theFile.exists()) {
            theFile.mkdir();
            //Toast.makeText(this, "created", Toast.LENGTH_SHORT).show();
        }

        cameraView.setLifecycleOwner(this);
        cameraConfigs();
        filterProperties();
        addFilters();
        context = this;
        overLayProperty();


    }

    public void addFilters() {

        cameraFilters.add(Filters.NONE.newInstance());
        cameraFilters.add(Filters.DOCUMENTARY.newInstance());
        cameraFilters.add(Filters.SEPIA.newInstance());
        cameraFilters.add(Filters.GRAYSCALE.newInstance());
        cameraFilters.add(Filters.LOMOISH.newInstance());
        cameraFilters.add(Filters.VIGNETTE.newInstance());
        cameraFilters.add(Filters.POSTERIZE.newInstance());
        cameraFilters.add(Filters.GRAIN.newInstance());
        cameraFilters.add(Filters.TEMPERATURE.newInstance());
    }

    private void overLayProperty() {
        WindowManager manager = getWindowManager();
        Display display = manager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        screenwidth = size.x;
        screenheight = size.y;
        upcomming.setX(-90f);

        upcomming.setY(-90f);
        bread.setY(-90);
        nut.setY(-90);
        bread.setX(-90);
        nut.setX(-90);


        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        changepose();
                    }
                });
            }
        }, 0, 20);

    }

    private void changepose() {
        nut.setVisibility(View.VISIBLE);
        upcomingy -= 15;
        bready -= 10;
        nuty -= 10;
        if (upcomming.getY() + upcomming.getHeight() <= 0) {
            upcomingx = (float) Math.floor(Math.random() * (screenwidth - upcomming.getWidth()));
            upcomingy = screenheight + 100.0f;
            nuty = screenheight + 100.0f;
            bready = screenheight + 100.0f;
            nutx = (float) Math.floor(Math.random() * (screenwidth - nut.getWidth()));
            breadx = (float) Math.floor(Math.random() * (screenwidth - bread.getWidth()));

        }
        upcomming.setX(upcomingx);
        upcomming.setY(upcomingy);
        bread.setX(breadx);
        bread.setY(bready);
        nut.setX(nutx);
        nut.setY(nuty);
    }

    int itemPosition = 5;

    private float translateX(View view, float x) {


        float withScaleFactor = (float) view.getWidth() / screenwidth;
        float scaleX = x * withScaleFactor;

        return view.getWidth() - scaleX;

    }


    private float translateY(View view, float x) {

        float withScaleFactor = (float) view.getHeight() / screenheight;
        float scaleX = x * withScaleFactor;

        return scaleX;
    }
    private void filterProperties() {

        filterAdapter = new FilterAdapter();


        filterAdapter.setFilters(cameraFilters);
        filterSet.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        filterSet.setLayoutManager(layoutManager);
        filterAdapter.setItemSelectedListener(new FilterAdapter.OnItemSelectedListener() {

            @Override
            public int currentSelect() {
                return itemPosition;
            }

            @Override
            public void itemVisible(int position, View view) {

            }

            @Override
            public void itemClicked(int position, View view) {
                itemPosition = position;
                filterAdapter.notifyDataSetChanged();
                filterSet.scrollToPosition(position);
                Filter filter = cameraFilters.get(position);

                cameraView.setFilter(filter);


            }
        });
        filterSet.scrollToPosition(itemPosition);
        filterSet.setAdapter(filterAdapter);

        filterButton.setOnClickListener(v -> {
            if (filterSet.getVisibility() == View.GONE) {
                filterSet.setVisibility(View.VISIBLE);
                filterButton.setImageResource(R.drawable.close);

            } else {
                filterButton.setImageResource(R.drawable.emoji_tint);

                filterSet.setVisibility(View.GONE);
            }
        });
    }

    ImageView showCapturedImage, flashlight;
    TextView coordinate1;

    private void viewByID() {

        cameraView = findViewById(R.id.cameraView);
        switch_camera = findViewById(R.id.switch_camera);
        filterSet = findViewById(R.id.filterSet);
        filterButton = findViewById(R.id.filterButton);
        coordinate1 = findViewById(R.id.coordinate1);
        flashlight = findViewById(R.id.flashlight);
        upcomming = findViewById(R.id.upcomming);
        nut = findViewById(R.id.nuts);
        showCapturedImage = findViewById(R.id.showCapturedImage);
        bread = findViewById(R.id.bread);
        focusView = findViewById(R.id.focusView);
    }

    private void cameraConfigs() {
        WindowManager manager = getWindowManager();
        Display display = manager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        cameraView.setFrameProcessingMaxHeight(size.y);
        cameraView.mapGesture(Gesture.PINCH, GestureAction.ZOOM);
        cameraView.mapGesture(Gesture.TAP, GestureAction.AUTO_FOCUS);
        cameraView.setPictureFormat(PictureFormat.JPEG);

        cameraView.setSoundEffectsEnabled(false);
        cameraView.setFrameProcessingMaxWidth(720);
        cameraView.setFrameProcessingMaxHeight(1920);
        cameraView.setPreviewFrameRate(30f);


        cameraView.setPlaySounds(false);

        cameraView.setFrameProcessingMaxWidth(size.x);
        cameraView.addFrameProcessor(new FrameProcessor() {

            @Override
            public void process(@NonNull Frame frame) {


                frameProcessor(frame);
            }
        });
        cameraView.addCameraListener(new CameraListener() {
            @Override
            public void onExposureCorrectionChanged(float newValue, @NonNull float[] bounds, @Nullable PointF[] fingers) {
                super.onExposureCorrectionChanged(newValue, bounds, fingers);
            }

            @Override
            public void onAutoFocusStart(@NonNull PointF point) {
                super.onAutoFocusStart(point);
                focusView.setVisibility(View.VISIBLE);
                YoYo.with(Techniques.Pulse).duration(500).playOn(focusView);
                focusView.setX(point.x);
                focusView.setY(point.y);
            }

            @Override
            public void onPictureTaken(@NonNull PictureResult result) {

                try {
                    File mediaFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
                    result.toFile(mediaFile, new FileCallback() {
                        @Override
                        public void onFileReady(@Nullable File file) {
                            Uri uri = Uri.fromFile(file);
                            galleryAddPic(uri);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }


            @Override
            public void onAutoFocusEnd(boolean successful, @NonNull PointF point) {
                super.onAutoFocusEnd(successful, point);
                focusView.setVisibility(View.INVISIBLE);
            }
        });


    }


    private void frameProcessor(Frame frame) {


        long time = frame.getTime();
        Size size = frame.getSize();
        int format = frame.getFormat();
        int userRotation = frame.getRotationToUser();
        int viewRotation = frame.getRotationToView();


        if (frame.getDataClass() == byte[].class) {
            byte[] data = frame.getData();

            // Process byte array...
            InputImage inputImage =
                    InputImage.fromByteArray(data, size.getWidth(), size.getHeight(),
                            userRotation, InputImage.IMAGE_FORMAT_NV21);
            FaceDetectorOptions faceDetectorOptions = new FaceDetectorOptions.Builder()
                    .setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
                    .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)

                    .build();
            FaceDetector faceDetection = FaceDetection.getClient(faceDetectorOptions);

            faceDetection.process(inputImage)
                    .addOnSuccessListener(new OnSuccessListener<List<Face>>() {
                        @Override
                        public void onSuccess(List<Face> faces) {


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });


        } else if (frame.getDataClass() == Image.class) {
            Image data = frame.getData();


            InputImage inputImage = InputImage.fromMediaImage(data, degreesToFirebaseRotation(userRotation));
            FaceDetectorOptions faceDetectorOptions = new FaceDetectorOptions.Builder()
                    .setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
                    .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)

                    .build();
            FaceDetector faceDetection = FaceDetection.getClient(faceDetectorOptions);

            faceDetection.process(inputImage)
                    .addOnSuccessListener(new OnSuccessListener<List<Face>>() {
                        @Override
                        public void onSuccess(List<Face> faces) {


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });

        }
    }

    private int degreesToFirebaseRotation(int orientation) {
        int rotation;
        if (orientation >= 45 && orientation < 135) {
            rotation = Surface.ROTATION_270;
        } else if (orientation >= 135 && orientation < 225) {
            rotation = Surface.ROTATION_180;
        } else if (orientation >= 225 && orientation < 315) {
            rotation = Surface.ROTATION_90;
        } else {
            rotation = Surface.ROTATION_0;
        }
        return rotation;
    }

    public void switchCameraLense(View view) {

        Facing facing = cameraView.getFacing();
        switch (facing) {
            case FRONT:
                cameraView.setFacing(Facing.BACK);
                break;
            case BACK:
                cameraView.setFacing(Facing.FRONT);
                break;

        }
    }

    public void captureImage(View view) {

        cameraView.takePictureSnapshot();


    }

    public void toggleFlash(View view) {
        switch (cameraView.getFlash()) {
            case OFF:
                flashlight.setImageResource(R.drawable.flash_on);
                break;
            case ON:
                flashlight.setImageResource(R.drawable.flash_off);
                break;
        }
    }

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    /**
     * Create a file Uri for saving an image or video
     */
    public File savebitmap(Bitmap bmp) throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 60, bytes);
        File f = new File(Environment.getExternalStorageDirectory()
                + File.separator + "IMG" + System.currentTimeMillis() + ".jpg");
        f.createNewFile();
        FileOutputStream fo = new FileOutputStream(f);
        fo.write(bytes.toByteArray());
        fo.close();
        return f;
    }

    public void createFile() {


        File mediaStorageDir =
                new File(Environment.
                        getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                        "ChoiceCamera");
        if (!mediaStorageDir.exists()) {
            mediaStorageDir.mkdir();
        }
        File file = new File(mediaStorageDir.getAbsolutePath() + File.separator + "newfile.txt");
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Create a File for saving an image or video
     */
    private File getOutputMediaFile(int type) throws IOException {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir =
                new File(Environment.
                        getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                        "ChoiceImages");
        if (!mediaStorageDir.exists()) {
            mediaStorageDir.mkdir();
        }
        File file = new File(mediaStorageDir.getAbsolutePath() + File.separator + "IMG_" + System.currentTimeMillis() + ".jpg");
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;
    }

    private void galleryAddPic(Uri contentUri) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);

        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    public void requestNeededPermision() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this, allpermissions, PackageManager.PERMISSION_GRANTED);

        }


    }

    private boolean allPermissionGranted() {

        for (String permission : allpermissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }


}