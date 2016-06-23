package nl.joozt.abc_tts.gestures;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;

public class ClearGestureDetector {
    private GestureDetectorCompat gestureDetector;

    public interface ClearGestureDetectorListener {
        void onClearGesture();
    }

    public ClearGestureDetector(Context context, final ClearGestureDetectorListener listener) {
        gestureDetector = new GestureDetectorCompat(context, new OnGestureListener() {

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return false;
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
                return false;
            }
        });

        gestureDetector.setOnDoubleTapListener(new OnDoubleTapListener() {

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                return false;
            }

            @Override
            public boolean onDoubleTapEvent(MotionEvent e) {
                listener.onClearGesture();
                return true;
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                return false;
            }
        });
    }

    public void onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
    }
}
