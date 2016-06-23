package nl.joozt.abc_tts;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;

import java.util.HashMap;
import java.util.Locale;

public class TTS {
    private static final String TAG = TTS.class.getSimpleName();
    private TextToSpeech textToSpeech;

    private HashMap<String, String> alternativePronunciations = new HashMap<String, String>();

    public TTS(Context context) {

        // Alternative pronunciations
        alternativePronunciations.put("Y", "ei");

        textToSpeech = new TextToSpeech(context, new OnInitListener() {

            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = textToSpeech.setLanguage(Locale.getDefault());
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e(TAG, "Language is not supported");
                    } else {
                        Log.i(TAG, "TextToSpeech is Initialized");
                    }
                } else {
                    Log.e(TAG, "Failed to Initialize");
                }
            }
        });
    }

    public void speak(String text) {
        if (textToSpeech == null || text == null || text.length() == 0) {
            return;
        }

        String alternativePronunciation = alternativePronunciations.get(text);
        if (alternativePronunciation != null) {
            text = alternativePronunciation;
        }

        Log.i(TAG, "Speak: " + text);
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

    public void stop() {
//		if (textToSpeech != null) {
//			textToSpeech.stop();
//		}
    }

    public void destroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
            textToSpeech = null;
        }
    }
}
