package nl.joozt.abc_tts;

public class TextRecorder {
    private String text = "";

    public void addCharacter(String character) {
        text = text + character;
    }

    public void addSpace() {
        text = text + " ";
    }

    public void clear() {
        text = "";
    }

    public void updateLastCharacter(String newLastCharacter) {
        if (text.length() == 0 || newLastCharacter == null || newLastCharacter.length() != 1) {
            return;
        }

        StringBuilder newText = new StringBuilder(text);
        newText.setCharAt(newText.length() - 1, newLastCharacter.charAt(0));
        text = newText.toString();
    }

    public String getText() {
        return text;
    }
}
