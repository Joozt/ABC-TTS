package nl.joozt.abc_tts.gestures;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;

public class UpdateGestureDetector {
    private GestureDetectorCompat gestureDetector;

    public interface UpdateGestureDetectorListener {
        void onUpdateUpGesture();

        void onUpdateDownGesture();
    }

    public UpdateGestureDetector(Context context, final UpdateGestureDetectorListener listener) {
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

                if (velocityX > 1000) {
                    Log.i("UpdateGestureDetector", "velocityX: " + velocityX);
                    listener.onUpdateUpGesture();
                } else if (velocityX < -1000) {
                    Log.i("UpdateGestureDetector", "velocityX: " + velocityX);
                    listener.onUpdateDownGesture();
                }

                return true;
            }

            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }
        });
    }

    public void onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
    }
}
