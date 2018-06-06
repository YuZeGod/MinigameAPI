package net.afyer.minigameapi.scoreboard;

public class LinePair
{
    private final Line line;
    private final int score;

    private LinePair(Line line, int score)
    {
        this.line = line;
        this.score = score;
    }

    public static LinePair of(Line line, int i)
    {
        return new LinePair(line, i);
    }

    public Line getLine()
    {
        return line;
    }

    public int getScore()
    {
        return score;
    }

    public String getText()
    {
        return line.getText();
    }
}
