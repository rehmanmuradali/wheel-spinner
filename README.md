# Wheel Spinner

## Demo

![Demo](https://media.giphy.com/media/Maxw7vQla0dmLXGCqi/giphy.gif)


## Setup
1. Add it in your root build.gradle at the end of repositories:
```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

2. Add the dependency:
```
dependencies {
		implementation 'com.github.rehmanmuradali:wheel-spinner:1.0.0'
	}
```

```
<com.spin.wheelspinner.WheelSpinner
        android:id="@+id/wheelSpinner"
        android:layout_width="0dp"
        android:layout_height="0dp"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:m_arc_stroke_color="@android:color/white"
        app:m_circle_stroke_color="@android:color/white" />



<ImageView
        android:id="@+id/ivArrow"
        android:layout_width="70dp"
        android:layout_height="70dp"
        android:src="@drawable/wheel_pin"
        app:layout_constraintCircle="@id/wheelSpinner"
        app:layout_constraintCircleAngle="40"
        tools:ignore="MissingConstraints" />
        

```




```


wheelSpinner = findViewById(R.id.wheelSpinner);
ivArrow = findViewById(R.id.ivArrow);
 
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
        
            // On Item selected
            ivSelectedTattoo.setImageBitmap(bitmap);
            ivSelectedTattoo.setVisibility(View.VISIBLE);
        });

        btnSpin.setOnClickListener(v -> {
            ivSelectedTattoo.setVisibility(View.GONE);
            wheelSpinner.rotateWheel();
        });
        
        
```
