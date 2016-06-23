package nl.joozt.abc_tts.gestures;

import android.content.Context;
import android.os.Handler;
import android.view.MotionEvent;

public class AlphabetGestureDetector {

    /**
     * We need a little start delay to give the tap, double tap and fling
     * gestures time to be detected.
     */
    public static final int DEFAULT_START_DELAY = 500;
    private int startDelay = DEFAULT_START_DELAY;

    private Handler handler = new Handler();
    private AlphabetGestureDetectorListener listener;
    private boolean down = false;
    private boolean startSent = false;

    public interface AlphabetGestureDetectorListener {
        void onStartAlphabetGesture();

        void onStopAlphabetGesture();
    }

    public AlphabetGestureDetector(Context context, final AlphabetGestureDetectorListener listener) {
        this.listener = listener;
    }

    public void setStartDelay(int startDelay) {
        this.startDelay = startDelay;
    }

    public void onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            startSent = false;
            down = true;

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (down) {
                        startSent = true;
                        listener.onStartAlphabetGesture();
                    }
                }
            }, startDelay);

        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            down = false;
            if (startSent) {
                listener.onStopAlphabetGesture();
            }
        }
    }
}
