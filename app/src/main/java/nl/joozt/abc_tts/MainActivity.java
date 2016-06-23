package nl.joozt.abc_tts;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.TextView;

import java.util.Locale;

import nl.joozt.abc_tts.gestures.AlphabetGestureDetector;
import nl.joozt.abc_tts.gestures.AlphabetSelectionGestureDetector;
import nl.joozt.abc_tts.gestures.AlphabetSpeedGestureDetector;
import nl.joozt.abc_tts.gestures.ClearGestureDetector;
import nl.joozt.abc_tts.gestures.SpeakGestureDetector;
import nl.joozt.abc_tts.gestures.UpdateGestureDetector;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private AlphabetPlayer alphabetPlayer;
    private TextRecorder textRecorder = new TextRecorder();
    private TTS textToSpeech;

    private AlphabetGestureDetector alphabetGestureDetector;
    private AlphabetSpeedGestureDetector alphabetSpeedGestureDetector;
    private AlphabetSelectionGestureDetector alphabetSelectionGestureDetector;
    private UpdateGestureDetector updateGestureDetector;
    private SpeakGestureDetector speakGestureDetector;
    private ClearGestureDetector clearGestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        alphabetPlayer = new AlphabetPlayer(this);
        alphabetPlayer.setCharacterDelay(Integer.parseInt(preferences.getString("alphabet_character_delay", Integer.toString(AlphabetPlayer.CHARACTER_DELAY_DEFAULT))));
        alphabetPlayer.setMinimumCharacterDelay(Integer.parseInt(preferences.getString("alphabet_character_min_delay", Integer.toString(AlphabetPlayer.CHARACTER_DELAY_DEFAULT_MIN))));
        alphabetPlayer.setMaximumCharacterDelay(Integer.parseInt(preferences.getString("alphabet_character_max_delay", Integer.toString(AlphabetPlayer.CHARACTER_DELAY_DEFAULT_MAX))));
        alphabetPlayer.setCharacterDelayStepSize(Integer.parseInt(preferences.getString("alphabet_character_delay_step_size", Integer.toString(AlphabetPlayer.CHARACTER_DELAY_DEFAULT_STEP_SIZE))));
        alphabetPlayer.setRepeatSelected(preferences.getBoolean("repeat_selected", true));
        alphabetPlayer.setRepeatSelectedDelay(Integer.parseInt(preferences.getString("repeat_selected_delay", Integer.toString(AlphabetPlayer.CHARACTER_DELAY_DEFAULT))));

        textToSpeech = new TTS(this);

        alphabetGestureDetector = new AlphabetGestureDetector(this, new AlphabetGestureDetector.AlphabetGestureDetectorListener() {

            @Override
            public void onStartAlphabetGesture() {
                textToSpeech.stop();
                alphabetPlayer.start();
            }

            @Override
            public void onStopAlphabetGesture() {
                final String character = alphabetPlayer.stop();
                textRecorder.addCharacter(character);
                ((TextView) findViewById(R.id.text)).setText(textRecorder.getText().toUpperCase(Locale.getDefault()));
            }
        });
        alphabetGestureDetector.setStartDelay(Integer.parseInt(preferences.getString("alphabet_start_delay", Integer.toString(AlphabetGestureDetector.DEFAULT_START_DELAY))));

        alphabetSpeedGestureDetector = new AlphabetSpeedGestureDetector(this, new AlphabetSpeedGestureDetector.AlphabetSpeedGestureDetectorListener() {

            @Override
            public void onSpeedUp() {
                alphabetPlayer.speedUp();
            }

            @Override
            public void onSpeedDown() {
                alphabetPlayer.speedDown();
            }
        });

        alphabetSelectionGestureDetector = new AlphabetSelectionGestureDetector(this, new AlphabetSelectionGestureDetector.AlphabetSelectionGestureDetectorListener() {

            @Override
            public void onChangeAlphabetGesture() {
                AlphabetPlayer.ALPHABET selectedAlphabet = alphabetPlayer.switchAlphabet();
                switch (selectedAlphabet) {
                    case OPTIMAL:
                        textToSpeech.speak(getString(R.string.alphabet_optimal));
                        break;
                    default:
                        textToSpeech.speak(getString(R.string.alphabet_normal));
                        break;
                }
            }
        });

        updateGestureDetector = new UpdateGestureDetector(this, new UpdateGestureDetector.UpdateGestureDetectorListener() {

            @Override
            public void onUpdateUpGesture() {
                String character = alphabetPlayer.updateUp();
                textRecorder.updateLastCharacter(character);
                ((TextView) findViewById(R.id.text)).setText(textRecorder.getText().toUpperCase(Locale.getDefault()));
            }

            @Override
            public void onUpdateDownGesture() {
                String character = alphabetPlayer.updateDown();
                textRecorder.updateLastCharacter(character);
                ((TextView) findViewById(R.id.text)).setText(textRecorder.getText().toUpperCase(Locale.getDefault()));
            }
        });

        speakGestureDetector = new SpeakGestureDetector(this, new SpeakGestureDetector.SpeakGestureDetectorListener() {

            @Override
            public void onSpeakGesture() {
                String text = textRecorder.getText();
                textToSpeech.speak(text);
                Log.i(TAG, "Speak: " + text);
            }
        });

        clearGestureDetector = new ClearGestureDetector(this, new ClearGestureDetector.ClearGestureDetectorListener() {

            @Override
            public void onClearGesture() {
                textRecorder.clear();
                ((TextView) findViewById(R.id.text)).setText("");
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        alphabetGestureDetector.onTouchEvent(event);
        alphabetSpeedGestureDetector.onTouchEvent(event);
        alphabetSelectionGestureDetector.onTouchEvent(event);
        updateGestureDetector.onTouchEvent(event);
        speakGestureDetector.onTouchEvent(event);
        clearGestureDetector.onTouchEvent(event);

        return super.onTouchEvent(event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // if (keyCode == KeyEvent.KEYCODE_BACK) {
        // return true;
        // }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        textToSpeech.destroy();
        super.onDestroy();
    }
}
