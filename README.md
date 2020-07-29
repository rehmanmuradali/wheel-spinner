[![Android Arsenal]( https://img.shields.io/badge/Android%20Arsenal-Wheel%20Spinner-green.svg?style=flat )]( https://android-arsenal.com/details/1/8139 )

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

## LICENSE
Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

[http://www.apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0)

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.

## Author
**Rehman Murad Ali**

