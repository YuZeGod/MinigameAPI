package net.afyer.minigameapi.tellraw;

public class MessagePart
{
    private static String TEXT_FORMAT = "\"text\":\"%s\"";
    private static String CLICK_FORMAT = "\"clickEvent\":{\"action\":\"%s\",\"value\":\"%s\"}";
    private static String HOVER_FORMAT = "\"hoverEvent\":{\"action\":\"%s\",\"value\":\"%s\"}";
    private static String INSERT_FORMAT = " \"insertion\":\"%s\"";
    public String text;
    public String clickActionName;
    public String clickActionData;
    public String hoverActionName;
    public String hoverActionData;
    public String insertionData;

    public MessagePart()
    {
        this("");
    }

    public MessagePart(String text)
    {
        this.text = text;
    }

    public boolean hasText()
    {
        return (text != null) && (!text.isEmpty());
    }

    public void writeJson(StringBuilder str)
    {
        str.append("{");
        str.append(String.format(TEXT_FORMAT, new Object[] { new JsonBuilder(text) }));
        if (clickActionName != null)
        {
            str.append(",");
            str.append(String.format(CLICK_FORMAT,
                    new Object[] { clickActionName, new JsonBuilder(clickActionData) }));
        }
        if (hoverActionName != null)
        {
            str.append(",");
            str.append(String.format(HOVER_FORMAT,
                    new Object[] { hoverActionName, new JsonBuilder(hoverActionData) }));
        }
        if (insertionData != null)
        {
            str.append(",");
            str.append(String.format(INSERT_FORMAT,
                    new Object[] { new JsonBuilder(insertionData) }));
        }
        str.append("}");
    }
}
