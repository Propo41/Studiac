package com.valhalla.studiac.holders;

/**
 * A holder class that holds only the text and used in custom spinners
 */
public class TextItem {
    private String text;

    public TextItem(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
