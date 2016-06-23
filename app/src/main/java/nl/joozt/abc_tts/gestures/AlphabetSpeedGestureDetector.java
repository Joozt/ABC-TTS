package nl.joozt.abc_tts.gestures;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;

public class AlphabetSpeedGestureDetector {
    private static final int THRESHOLD = 50;

    private GestureDetectorCompat gestureDetector;
    private AlphabetSpeedGestureDetectorListener listener;
    private float currentDistanceY = 0;

    public interface AlphabetSpeedGestureDetectorListener {
        void onSpeedUp();

        void onSpeedDown();
    }

    public AlphabetSpeedGestureDetector(Context context, final AlphabetSpeedGestureDetectorListener listener) {
        this.listener = listener;

        gestureDetector = new GestureDetectorCompat(context, new OnGestureListener() {
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                onVerticalScroll(distanceY);
                return true;
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {
            }

            @Override
            public void onLongPress(MotionEvent e) {
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return false;
            }

            @Override
            public boolean onDown(MotionEvent e) {
                currentDistanceY = 0;
                return true;
            }
        });
    }

    public void onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
    }

    private void onVerticalScroll(float distanceY) {
        currentDistanceY += distanceY;
        if (currentDistanceY > THRESHOLD) {
            listener.onSpeedUp();
            currentDistanceY = 0;
        } else if (currentDistanceY < THRESHOLD * -1) {
            listener.onSpeedDown();
            currentDistanceY = 0;
        }
    }
}
