package com.example.ferofly_task;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.vinay.stepview.HorizontalStepView;
import com.vinay.stepview.models.Step;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button TrackOrder;
    TextView Address;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HorizontalStepView horizontalStepView = findViewById(R.id.step_view);
        List<Step> stepList = new ArrayList<>();
        stepList.add(new Step("Cooking", Step.State.COMPLETED));
        stepList.add(new Step("Picked", Step.State.COMPLETED));
        stepList.add(new Step("On way", Step.State.CURRENT));
        stepList.add(new Step("Delivered"));
        stepList.add(new Step("Done"));

        horizontalStepView.setSteps(stepList);
        horizontalStepView
                .setCompletedStepIcon(AppCompatResources.getDrawable(MainActivity.this, R.drawable.ic_check_mark))
                .setNotCompletedStepIcon(AppCompatResources.getDrawable(MainActivity.this, R.drawable.ic_circle))
                .setCurrentStepIcon(AppCompatResources.getDrawable(MainActivity.this, R.drawable.ic_way))
                .setCompletedStepTextColor(Color.DKGRAY)
                .setNotCompletedStepTextColor(Color.DKGRAY)
                .setCurrentStepTextColor(Color.BLACK)
                .setCompletedLineColor(Color.parseColor("#FFC0CB"))
                .setNotCompletedLineColor(Color.parseColor("#FFC0CB"))
                .setTextSize(12)
                .setCircleRadius(16)
                .setLineLength(50);

        Address=findViewById(R.id.Address);
        TrackOrder=findViewById(R.id.TrackOrder);
        TrackOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address=Address.getText().toString();
                GeoLocation geolocation=new GeoLocation();
                geolocation.getAddress(address,getApplicationContext(), new GeoHandler());
            }
        });

        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                stepList.get(2).setState(Step.State.COMPLETED);
                stepList.get(3).setState(Step.State.CURRENT);
                horizontalStepView.setSteps(stepList);
            }
        },5000);
    }

    private class GeoHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            String address;
            switch (msg.what)
            {
                case 1:
                    Bundle bundle=msg.getData();
                    address=bundle.getString("address");
                    break;
                default:
                    address=null;
            }
            Intent intent=new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(address));
            Intent chooser=Intent.createChooser(intent,"Launch Maps");
            startActivity(chooser);
        }
    }
}