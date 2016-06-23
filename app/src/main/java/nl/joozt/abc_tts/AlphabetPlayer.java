package nl.joozt.abc_tts;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;
import android.util.Log;

public class AlphabetPlayer {
    private static final String TAG = AlphabetPlayer.class.getSimpleName();
    private static final String[] ABC_NORMAL = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
    private static final String[] ABC_OPTIMAL = {"e", "n", "a", "t", "i", "r", "o", "d", "s", "l", "g", "v", "h", "k", "m", "u", "b", "p", "w", "j", "z", "c", "f", "x", "y", "q"};
    public static final int CHARACTER_DELAY_DEFAULT = 400;
    public static final int CHARACTER_DELAY_DEFAULT_MIN = 200;
    public static final int CHARACTER_DELAY_DEFAULT_MAX = 600;
    public static final int CHARACTER_DELAY_DEFAULT_STEP_SIZE = 10;

    private int characterDelay = CHARACTER_DELAY_DEFAULT;
    private int characterDelayIncludingChangedSpeed = CHARACTER_DELAY_DEFAULT;
    private int characterDelayMinimum = CHARACTER_DELAY_DEFAULT_MIN;
    private int characterDelayMaximum = CHARACTER_DELAY_DEFAULT_MAX;
    private int characterDelayStepSize = CHARACTER_DELAY_DEFAULT_STEP_SIZE;
    private boolean repeatSelected = true;
    private int repeatSelectedDelay = CHARACTER_DELAY_DEFAULT;

    private final Context context;
    private final Handler handler = new Handler();
    private SoundPool soundPool;
    private int[] soundIds = new int[26];
    private int currentStreamId = 0;
    private int currentSoundIndex = -1;
    private boolean shouldStop = false;

    public enum ALPHABET {
        NORMAL, OPTIMAL
    }

    private ALPHABET activeAlphabet = ALPHABET.NORMAL;

    public AlphabetPlayer(Context context) {
        this.context = context;
        soundPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);

        loadSounds();
    }

    private void loadSounds() {
        String[] characters = getCharacters();

        for (int i = 0; i < characters.length; i++) {
            String character = characters[i];
            int resourceId = context.getResources().getIdentifier(character, "raw", context.getPackageName());
            int soundId = soundPool.load(context, resourceId, 1);
            soundIds[i] = soundId;
        }
    }

    private String[] getCharacters() {
        String[] characters;
        switch (activeAlphabet) {
            case OPTIMAL:
                characters = ABC_OPTIMAL;
                break;
            default:
                characters = ABC_NORMAL;
                break;
        }
        return characters;
    }

    public void setCharacterDelay(int characterDelay) {
        this.characterDelay = characterDelay;
    }

    public void setMinimumCharacterDelay(int mimimumCharacterDelay) {
        this.characterDelayMinimum = mimimumCharacterDelay;
    }

    public void setMaximumCharacterDelay(int maximumCharacterDelay) {
        this.characterDelayMaximum = maximumCharacterDelay;
    }

    public void setCharacterDelayStepSize(int characterDelayStepSize) {
        this.characterDelayStepSize = characterDelayStepSize;
    }

    public void setRepeatSelected(boolean repeatSelected) {
        this.repeatSelected = repeatSelected;
    }

    public void setRepeatSelectedDelay(int repeatSelectedDelay) {
        this.repeatSelectedDelay = repeatSelectedDelay;
    }

    public void start() {
        if (currentStreamId != 0) {
            soundPool.stop(currentStreamId);
            currentStreamId = 0;
        }

        currentSoundIndex = -1;
        characterDelayIncludingChangedSpeed = characterDelay;
        shouldStop = false;
        playSound();
    }

    public void playSound() {
        if (shouldStop) {
            return;
        }

        // Play the next sound
        currentSoundIndex++;
        currentStreamId = soundPool.play(soundIds[currentSoundIndex], 1, 1, 1, 0, 1f);

        // Queue the next sound recursively
        if (currentSoundIndex < soundIds.length - 1) {
            handler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    playSound();
                }
            }, characterDelayIncludingChangedSpeed);
        }
    }

    public String stop() {
        shouldStop = true;

        if (currentStreamId != 0) {
            soundPool.stop(currentStreamId);
            currentStreamId = 0;
        }

        String currentCharacter = "";
        if (currentSoundIndex >= 0) {

            if (repeatSelected) {
                repeatSelectedCharacter(currentSoundIndex);
            }

            String[] characters = getCharacters();
            currentCharacter = characters[currentSoundIndex];
        }

        return currentCharacter;
    }

    private void repeatSelectedCharacter(final int soundIndex) {
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                currentStreamId = soundPool.play(soundIds[soundIndex], 1, 1, 1, 0, 1f);
            }
        }, repeatSelectedDelay);
    }

    public void speedUp() {
        characterDelayIncludingChangedSpeed -= characterDelayStepSize;
        if (characterDelayIncludingChangedSpeed < characterDelayMinimum) {
            characterDelayIncludingChangedSpeed = characterDelayMinimum;
        }
        Log.i(TAG, "Delay: " + characterDelayIncludingChangedSpeed);
    }

    public void speedDown() {
        characterDelayIncludingChangedSpeed += characterDelayStepSize;
        if (characterDelayIncludingChangedSpeed > characterDelayMaximum) {
            characterDelayIncludingChangedSpeed = characterDelayMaximum;
        }
        Log.i(TAG, "Delay: " + characterDelayIncludingChangedSpeed);
    }

    public String updateUp() {
        if (currentSoundIndex >= soundIds.length) {
            return "";
        }

        currentSoundIndex++;

        String currentCharacter = "";
        if (currentSoundIndex >= 0) {
            currentStreamId = soundPool.play(soundIds[currentSoundIndex], 1, 1, 1, 0, 1f);
            String[] characters = getCharacters();
            currentCharacter = characters[currentSoundIndex];
        }

        return currentCharacter;
    }

    public String updateDown() {
        if (currentSoundIndex < 1) {
            return "";
        }

        currentSoundIndex--;

        String currentCharacter = "";
        if (currentSoundIndex >= 0) {
            currentStreamId = soundPool.play(soundIds[currentSoundIndex], 1, 1, 1, 0, 1f);
            String[] characters = getCharacters();
            currentCharacter = characters[currentSoundIndex];
        }

        return currentCharacter;
    }

    public ALPHABET switchAlphabet() {
        for (int soundId : soundIds) {
            soundPool.unload(soundId);
        }

        if (activeAlphabet == ALPHABET.NORMAL) {
            activeAlphabet = ALPHABET.OPTIMAL;
        } else {
            activeAlphabet = ALPHABET.NORMAL;
        }

        loadSounds();

        return activeAlphabet;
    }
}
