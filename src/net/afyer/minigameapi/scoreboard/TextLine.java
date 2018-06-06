package net.afyer.minigameapi.scoreboard;

public class TextLine implements Line
{
    private final String text;

    private TextLine(String text)
    {
        this.text = text;
    }

    public static Line of(String text)
    {
        return new TextLine(text);
    }

    @Override public String getText()
    {
        return text;
    }

    @Override public String toString()
    {
        return text;
    }
}
