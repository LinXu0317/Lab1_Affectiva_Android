package com.example.lab1_affectiva_android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.affectiva.android.affdex.sdk.Frame;
import com.affectiva.android.affdex.sdk.detector.CameraDetector;
import com.affectiva.android.affdex.sdk.detector.Face;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements CameraDetector.CameraEventListener, CameraDetector.ImageListener{
    SurfaceView cameraDetectorSurfaceView;
    CameraDetector cameraDetector;

    int maxProcessingRate = 10;

    TextView joyTest;

    TextView weightedTest;

    TextView lastTen;

    ImageView smile;

    ArrayList<Float> arr = new ArrayList<Float>();

    ArrayList<Float> ar = new ArrayList<Float>();


    ArrayList<Float> br = new ArrayList<Float>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cameraDetectorSurfaceView = (SurfaceView) findViewById(R.id.CameraDetectorSurfaceView);
        cameraDetector = new CameraDetector(this, CameraDetector.CameraType.CAMERA_FRONT, cameraDetectorSurfaceView);
        cameraDetector.setMaxProcessRate(maxProcessingRate);
        cameraDetector.setImageListener(this);
        cameraDetector.setOnCameraEventListener(this);
        cameraDetector.setDetectAllEmotions(true);
        cameraDetector.start();

    }

    @Override
    public void onCameraSizeSelected(int cameraHeight, int cameraWidth, Frame.ROTATE rotation) {

        ViewGroup.LayoutParams params = cameraDetectorSurfaceView.getLayoutParams();

        params.height = cameraHeight;
        params.width = cameraWidth;

        cameraDetectorSurfaceView.setLayoutParams(params);
    }

    @Override
    public void onImageResults(List<Face> faces, Frame frame, float timeStamp) {

        joyTest = findViewById(R.id.textView);

        weightedTest = findViewById(R.id.textView2);

        lastTen = findViewById(R.id.textView3);

        smile = findViewById(R.id.imageView);

            if (faces == null)
            return; //frame was not processed

            if (faces.size() == 0)
            return; //no face found

            Face face = faces.get(0);


            float joy = face.emotions.getJoy();
            float anger = face.emotions.getAnger();
            float surprise = face.emotions.getSurprise();

            int threashold = 40;

            arr.add(joy);
        System.out.println(arr.size());
            if(arr.size() == 100){
                int sum = 0;
                int sum2 = 0;
                int j = 1;
                for(Float i: arr ){
                    sum += i;
                    sum2 += i*j;
                    j++;
                }
                System.out.println(sum);
                int mean = sum/100;
                int mean2 = sum2/5050;
                if (mean2 >= threashold){
                    weightedTest.setText("You have reached the threshold using WMA");
                }
                if(mean >= threashold){
                    joyTest.setText("You have reached the threshold using SMA");
                }
                arr.remove(0);
            }
            ar.add(joy);
            br.add(timeStamp);

            int mean3 = 0;
            int sum3 = 0;
            if (timeStamp >= 10){
                float diff = timeStamp - 10;
                for(int i = 0; i< ar.size(); i++){
                    if(br.get(i)< diff){
                        br.remove(i);
                        ar.remove(i);
                    }

            }

                    for(int i = 0; i<ar.size(); i++){
                        sum3 += ar.get(i);
                    }
                    mean3 = sum3/10;
                }
            if(mean3 >= threashold){
                lastTen.setText("You have reached the threshold using SMA using timestamps");
                smile.setImageResource(R.drawable.doge);
            }


            System.out.println("Joy: " + joy);
            System.out.println("Anger: " + anger);
            System.out.println("Surprise: " + surprise);
            }
        }
