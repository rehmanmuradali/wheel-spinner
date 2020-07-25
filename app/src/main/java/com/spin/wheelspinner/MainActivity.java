package com.spin.wheelspinner;

import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.spin.spinner.WheelSpinner;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ImageView ivArrow;
    private Button btnSpin;
    private ImageView ivSelectedTattoo;
    private WheelSpinner wheelSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ivArrow = findViewById(R.id.ivArrow);
        wheelSpinner = findViewById(R.id.wheelSpinner);
        ivSelectedTattoo = findViewById(R.id.ivSelectedTattoo);
        btnSpin = findViewById(R.id.btnSpin);


        List<Integer> bitmaps = new ArrayList<>();
        bitmaps.add(R.drawable.tatoo_1);
        bitmaps.add(R.drawable.tatoo_2);
        bitmaps.add(R.drawable.tatoo_3);
        bitmaps.add(R.drawable.tatoo_4);
        bitmaps.add(R.drawable.tatoo_5);
        bitmaps.add(R.drawable.tatoo_6);
        wheelSpinner.setArrowPointer(ivArrow);
        wheelSpinner.setBitmapsId(bitmaps);
        wheelSpinner.setOnItemSelectListener(bitmap -> {
            ivSelectedTattoo.setImageBitmap(bitmap);
            ivSelectedTattoo.setVisibility(View.VISIBLE);
        });

        btnSpin.setOnClickListener(v -> {
            ivSelectedTattoo.setVisibility(View.GONE);
            wheelSpinner.rotateWheel();
        });
    }
}
