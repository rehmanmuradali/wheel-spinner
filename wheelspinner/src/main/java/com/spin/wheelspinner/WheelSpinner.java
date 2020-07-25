package com.spin.wheelspinner;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WheelSpinner extends View {


    // Default Properties
    private static final int DEFAULT_CIRCLE_STROKE_COLOR = 0xEEEEEE;
    private static final int DEFAULT_ARC_STROKE_COLOR = 0xFFFFFF;
    private static final int DEFAULT_CIRCLE_RADIUS_IN_DP = 70;
    private static final int DEFAULT_INNER_CIRCLE_RADIUS_IN_DP = 20;
    private static final int DEFAULT_CIRCLE_STROKE_WIDTH = 20;
    private static final int DEFAULT_ARC_MARGIN = 5;
    public static final float DEFAULT_SELECTED_ANGE = 292.5f;
    public static final int APP_BAR_SIZE_IN_DP = 50;
    public static final int ITEM_SIZE = 16;


    //Paint Objects
    private Paint paintOuterCircle;
    private Paint paintInnerBoundaryCircle;
    private Paint paintInnerBoundaryCircle2;
    private Paint paintInnerCircle;
    private Paint archPaint;
    private Paint spinnerPaint;
    private Paint tintTattooPaint;


    //Center Point of Screen Properties
    private float yLocation;
    private float xLocation;
    //Outer Circle Properties
    private float circleRadiusInPX = 400;
    private float circleRadiusInnerInPX = 100;
    private float circleStrokeWidthInPx = 2;
    private float rotationDegrees;
    private int colorCircleStroke;
    //Boundary wheel properties
    private int boundaryWheelWidth;
    private int boundaryWheelHeight;
    private int bitmapTattooWidth;
    private int bitmapTattooHeight;

    //Inner Arc Properties
    private RectF arcRectContainer;
    private RectF spinnerContainer;

    private Bitmap bitmapBoundryWheel;
    private Bitmap bitmapTattoo;

    private boolean isRotating = false;
    private float sweepAngle = 0;
    private float spinnerStartAngle = 0;
    private float spinnerSweepAngle = 270;
    private float middleTattooDegree = 0;
    private boolean isSelected = false;

    private Random random = new Random();

    private OnItemSelectListener onItemSelectListener;

    private List<Bitmap> bitmapTattooList = new ArrayList<>();

    private int deviceWidth = -1;
    private int deviceHeight = -1;

    private ImageView ivArrow;
    private ValueAnimator rotationAnimator;
    private boolean isCancel = false;

    public WheelSpinner(Context context) {
        this(context, null);
    }

    public WheelSpinner(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WheelSpinner(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, -1);
    }

    public WheelSpinner(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.WheelSpinner);
            try {
                circleRadiusInPX = dpToPx(typedArray.getInt(R.styleable.WheelSpinner_m_circle_radius, DEFAULT_CIRCLE_RADIUS_IN_DP));
                circleRadiusInnerInPX = dpToPx(typedArray.getInt(R.styleable.WheelSpinner_m_circle_inner_radius, DEFAULT_INNER_CIRCLE_RADIUS_IN_DP));
                circleStrokeWidthInPx = dpToPx(typedArray.getInt(R.styleable.WheelSpinner_m_cicle_stroke_width, DEFAULT_CIRCLE_STROKE_WIDTH));
                colorCircleStroke = typedArray.getColor(R.styleable.WheelSpinner_m_circle_stroke_color, DEFAULT_CIRCLE_STROKE_COLOR);

            } finally {

                typedArray.recycle();
            }

        }


        init();
    }

    private void init() {


        Display display = ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        deviceWidth = size.x;
        deviceHeight = size.y;


        circleRadiusInPX = Math.min(deviceHeight * 0.8f, (float) deviceWidth / 2);

        circleStrokeWidthInPx = (circleRadiusInPX * 0.1f / 3);

        //Outer Circle Paint Object initialization
        paintOuterCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintOuterCircle.setColor(colorCircleStroke);
        paintOuterCircle.setStrokeWidth(circleStrokeWidthInPx);
        paintOuterCircle.setStyle(Paint.Style.STROKE);

        paintInnerBoundaryCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintInnerBoundaryCircle.setColor(getResources().getColor(R.color.dark_charcoal));
        paintInnerBoundaryCircle.setStrokeWidth(circleStrokeWidthInPx);
        paintInnerBoundaryCircle.setStyle(Paint.Style.STROKE);

        paintInnerCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintInnerCircle.setColor(getResources().getColor(R.color.color_black));
        paintInnerCircle.setAlpha(50);
        paintInnerCircle.setStrokeWidth(circleStrokeWidthInPx);
        paintInnerCircle.setStyle(Paint.Style.FILL_AND_STROKE);

        paintInnerBoundaryCircle2 = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintInnerBoundaryCircle2.setColor(getResources().getColor(R.color.color_black));
        paintInnerBoundaryCircle2.setAlpha(50);
        paintInnerBoundaryCircle2.setStrokeWidth(circleStrokeWidthInPx);
        paintInnerBoundaryCircle2.setStyle(Paint.Style.STROKE);

        archPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        archPaint.setColor(Color.WHITE);
        archPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        spinnerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        spinnerPaint.setColor(Color.GRAY);
        spinnerPaint.setStyle(Paint.Style.STROKE);
        spinnerPaint.setStrokeWidth(circleStrokeWidthInPx / 4);


        tintTattooPaint = new Paint();
        tintTattooPaint.setColorFilter(new PorterDuffColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN));

        //Initialize Arc Object
        arcRectContainer = new RectF();
        spinnerContainer = new RectF();


        bitmapTattooWidth = (int) (circleRadiusInPX * 0.1f);
        bitmapTattooHeight = (int) (circleRadiusInPX * 0.1f);

        bitmapTattoo = BitmapFactory.decodeResource(getResources(), R.drawable.tatoo_1);
        bitmapTattoo = Bitmap.createScaledBitmap(bitmapTattoo, bitmapTattooWidth, bitmapTattooHeight, false);

        //bitmap for wheel End
        boundaryWheelWidth = (int) (circleRadiusInPX * 0.1f);
        boundaryWheelHeight = (int) (circleRadiusInPX * 0.1f);
        bitmapBoundryWheel = BitmapFactory.decodeResource(getResources(), R.drawable.wheel_light);
        bitmapBoundryWheel = Bitmap.createScaledBitmap(bitmapBoundryWheel, boundaryWheelWidth, boundaryWheelHeight, false);

        rotationAnimator = ValueAnimator.ofFloat(0, 1);
        rotationAnimator.setDuration(1);
        rotationAnimator.setRepeatCount(ValueAnimator.INFINITE);
        rotationAnimator.setInterpolator(new LinearInterpolator());

    }

    public void setBitmapsId(List<Integer> bitmapsId) {
        for (Integer bitmapId : bitmapsId) {
            bitmapTattoo = BitmapFactory.decodeResource(getResources(), bitmapId);
            bitmapTattoo = Bitmap.createScaledBitmap(bitmapTattoo, bitmapTattooWidth, bitmapTattooHeight, false);
            bitmapTattooList.add(bitmapTattoo);
        }

        invalidate();
    }

    public void setArrowPointer(ImageView ivArrow){
        this.ivArrow = ivArrow;
        setArrowConstraints(ivArrow);
        invalidate();
    }

    private void setArrowConstraints(ImageView ivArrow) {
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) ivArrow.getLayoutParams();
        layoutParams.circleAngle = (float) 33.75;
        layoutParams.width = (int) (circleRadiusInPX * 0.2f);
        layoutParams.height = (int) (circleRadiusInPX * 0.2f);
        layoutParams.circleRadius = (int) (circleRadiusInPX + circleStrokeWidthInPx);
        ivArrow.setLayoutParams(layoutParams);
    }


    public void setOnItemSelectListener(OnItemSelectListener onItemSelectListener){
        this.onItemSelectListener = onItemSelectListener;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {


        int measuredWidth = measureWidth();
        int measuredHeight = measureHeight();


        yLocation = (float) measuredHeight / 2;
        xLocation = (float) measuredWidth / 2;


        setMeasuredDimension(measuredWidth, measuredHeight);


        setTranslationY((float) measuredHeight / 2);
        setTranslationX(-(float) measuredWidth / 2);

        if (ivArrow != null) {
            ivArrow.setTranslationY((float) measuredHeight / 2);
            ivArrow.setTranslationX(-(float) measuredWidth / 2);
        }


    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //Set Inner Arc Position
        arcRectContainer.set(xLocation - circleRadiusInPX, yLocation - circleRadiusInPX, xLocation + circleRadiusInPX, yLocation + circleRadiusInPX);

        canvas.save();


        int itemSize = ITEM_SIZE;
        sweepAngle = 360 / (float) itemSize;
        float tempAngle = 0;


        for (int i = 0; i < itemSize; i++) {
            archPaint.setColor(i % 2 == 0 ? Color.WHITE : getResources().getColor(R.color.dark_charcoal));
            float angle = DEFAULT_SELECTED_ANGE - middleTattooDegree;
            if (angle < 0) angle += 360;
            if (isSelected && tempAngle == angle) {
                archPaint.setColor(getResources().getColor(R.color.light_purple));
            }
            canvas.drawArc(arcRectContainer, tempAngle, sweepAngle, true, archPaint);
            tempAngle += sweepAngle;
        }


        //Rotate and Draw Circle and Inner Arc
        canvas.rotate(rotationDegrees, xLocation, yLocation);
        canvas.drawCircle(xLocation, yLocation, circleRadiusInPX, paintOuterCircle);
        canvas.drawCircle(xLocation, yLocation, circleRadiusInPX - circleStrokeWidthInPx, paintInnerBoundaryCircle);
        canvas.drawCircle(xLocation, yLocation, circleRadiusInPX - 2 * circleStrokeWidthInPx, paintInnerBoundaryCircle2);
        canvas.drawCircle(xLocation, yLocation, circleRadiusInPX * 0.2f, paintInnerCircle);

        tempAngle = 0;
        for (int i = 0; i < itemSize; i++) {
            canvas.save();
            canvas.rotate(tempAngle, xLocation, yLocation);
            canvas.drawBitmap(bitmapBoundryWheel, xLocation - (float) boundaryWheelWidth / 2, yLocation - circleRadiusInPX - (circleStrokeWidthInPx / 2), null);
            tempAngle += sweepAngle;
            canvas.restore();
        }

        tempAngle = sweepAngle / 2;
        for (int i = 0; i < itemSize; i++) {
            canvas.save();
            canvas.rotate(tempAngle, xLocation, yLocation);
            float left = xLocation - (float) bitmapTattooWidth / 2;
            float top = yLocation - (0.8f * circleRadiusInPX);

          /*  if (bitmapTattooList == null || bitmapTattooList.size() < (i + 1)) {
                spinnerContainer.set(left, top, left + circleRadiusInPX * 0.06f, top + circleRadiusInPX * 0.06f);
                canvas.drawArc(spinnerContainer, spinnerStartAngle, spinnerSweepAngle, false, spinnerPaint);

            } else {
                canvas.drawBitmap(bitmapTattooList.get(i % bitmapTattooList.size()), left, top, i % 2 == 0 ? null : tintTattooPaint);
            }*/
            canvas.drawBitmap(bitmapTattooList.get(i % bitmapTattooList.size()), left, top, i % 2 == 0 ? null : tintTattooPaint);
            tempAngle += sweepAngle;
            canvas.restore();
        }

        canvas.restore();


    }


    public void resetInitialState() {
        animate().setDuration(0)
                .rotation(0).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                rotate();
                clearAnimation();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    private void rotate() {
        int DEFAULT_ROTATION_TIME = 9000;
        // 50 + is done due to item size in not exactly divisible for circle. error removing
        float randomness = random.nextInt(361);
        animate().setInterpolator(new DecelerateInterpolator())
                .setDuration(DEFAULT_ROTATION_TIME)
                .rotation((360 * 5) + randomness)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        isRotating = true;
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        rotateInMiddle(randomness);
                        clearAnimation();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                })
                .start();

    }

    private void rotateInMiddle(float randomness) {

        middleTattooDegree = getMiddleElement(randomness);
        float difference = Math.abs(randomness - middleTattooDegree);
        float rotate = randomness > middleTattooDegree ? -difference : difference;
        sleep(1000);
        animate().setInterpolator(new AccelerateDecelerateInterpolator())
                .setDuration(1000)
                .rotationBy(rotate)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        isRotating = false;
                        isSelected = true;
                        invalidate();
                        onTattooSelected();
                        clearAnimation();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                })
                .start();
    }


    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void onTattooSelected() {
        int index = getCirclePositionFromDegree(middleTattooDegree) % bitmapTattooList.size();

        if (onItemSelectListener != null) onItemSelectListener.onTattooSelected(bitmapTattooList.get(index));
    }

    public void rotateWheel() {
        if (isRotating) return;
        isSelected = false;
        invalidate();
        resetInitialState();
    }

    public void loadWheel() {
        rotationAnimator.addUpdateListener(valueAnimator -> {
            spinnerStartAngle = (spinnerStartAngle + 10) % 360;
            invalidate();
        });


        isSelected = false;
        isCancel = false;
        rotationAnimator.start();
        bitmapTattooList.clear();
        invalidate();
    }

    private void pauseLoading() {
        rotationAnimator.removeAllUpdateListeners();
        invalidate();
    }

    public void cancelLoading() {
        isCancel = true;
        rotationAnimator.removeAllUpdateListeners();
        invalidate();
    }


    /**
     * Set the height with respect to Circle Radius
     *
     * @return height
     */
    private int measureHeight() {

        return (int) (circleRadiusInPX * 2 + circleStrokeWidthInPx + getPaddingTop() + getPaddingBottom());
    }


    /**
     * Set the width with respect to Circle Radius
     *
     * @return width
     */
    private int measureWidth() {
        return (int) (circleRadiusInPX * 2 + circleStrokeWidthInPx + getPaddingRight() + getPaddingLeft());
    }


  /*  public void setTattoos(List<RandomTattooResponse> tattoos, TattoosLoadedListener tattoosLoadedListener) {
        new FetchTattoo(tattoos, tattoosLoadedListener).execute();
    }*/

    private int getCirclePositionFromDegree(float middleTattooDegree) {
        if (middleTattooDegree == 0) {
            return 1;
        } else if (middleTattooDegree == 22.5) {
            return 0;
        } else if (middleTattooDegree == 45) {
            return 15;
        } else if (middleTattooDegree == 67.5) {
            return 14;
        } else if (middleTattooDegree == 90) {
            return 13;
        } else if (middleTattooDegree == 112.5) {
            return 12;
        } else if (middleTattooDegree == 135) {
            return 11;
        } else if (middleTattooDegree == 157.5f) {
            return 10;
        } else if (middleTattooDegree == 180f) {
            return 9;
        } else if (middleTattooDegree == 202.5f) {
            return 8;
        } else if (middleTattooDegree == 225f) {
            return 7;
        } else if (middleTattooDegree == 247.5f) {
            return 6;
        } else if (middleTattooDegree == 270f) {
            return 5;
        } else if (middleTattooDegree == 292.5f) {
            return 4;
        } else if (middleTattooDegree == 315f) {
            return 3;
        } else if (middleTattooDegree == 337.5f) {
            return 2;
        }

        return 0;
    }

    private float getMiddleElement(float randomness) {

        if (randomness < 11.25) {
            return 0;
        } else if (randomness < 33.75) {
            return 22.5f;
        } else if (randomness < 56.25) {
            return 45f;
        } else if (randomness < 78.75) {
            return 67.5f;
        } else if (randomness < 101.25) {
            return 90f;
        } else if (randomness < 123.75) {
            return 112.5f;
        } else if (randomness < 146.25) {
            return 135f;
        } else if (randomness < 168.75) {
            return 157.5f;
        } else if (randomness < 191.25) {
            return 180f;
        } else if (randomness < 213.75) {
            return 202.5f;
        } else if (randomness < 236.25) {
            return 225f;
        } else if (randomness < 258.75) {
            return 247.5f;
        } else if (randomness < 281.25) {
            return 270f;
        } else if (randomness < 303.75) {
            return 292.5f;
        } else if (randomness < 326.25) {
            return 315f;
        } else if (randomness < 348.75) {
            return 337.5f;
        }
        return 360;
    }


    /*private class FetchTattoo extends AsyncTask<Void, Void, Void> {

        private List<RandomTattooResponse> tattoos;
        private TattoosLoadedListener tattoosLoadedListener;

        private FetchTattoo(List<RandomTattooResponse> tattoos, TattoosLoadedListener tattoosLoadedListener) {
            this.tattoos = tattoos;
            this.tattoosLoadedListener = tattoosLoadedListener;
        }


        @Override
        protected Void doInBackground(Void... voids) {
            bitmapTattooList.clear();
            for (RandomTattooResponse tattoo : tattoos) {
                bitmapTattoo = getBitmapFromURL(tattoo.getImage());
                if (bitmapTattoo != null) {
                    bitmapTattoo = Bitmap.createScaledBitmap(bitmapTattoo, bitmapTattooWidth, bitmapTattooHeight, false);
                    bitmapTattooList.add(bitmapTattoo);
                }

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (bitmapTattooList.size() == ITEM_SIZE) {
                tattoosLoadedListener.onTattooLoaded();
                pauseLoading();
            } else {
                cancelLoading();
                tattoosLoadedListener.onTattooLoadingFailure();
            }

            invalidate();

        }
    }
*/
    /*private static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }*/


    /**
     * Convert Dp to Pixels
     *
     * @param value Value in Dp
     * @return Value in Pixels
     */
    private float dpToPx(final float value) {
        return value * getContext().getResources().getDisplayMetrics().density;
    }

    public interface OnItemSelectListener {
        void onTattooSelected(Bitmap bitmap);
    }

    public interface TattoosLoadedListener {
        void onTattooLoaded();

        void onTattooLoadingFailure();
    }


}
