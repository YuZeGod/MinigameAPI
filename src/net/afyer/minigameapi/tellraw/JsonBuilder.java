package net.afyer.minigameapi.tellraw;

public class JsonBuilder
{
    public static String[] REPLACEMENT_CHARS = new String['\u0080'];

    static
    {
        for (int i = 0; i <= 31; i++)
        {
            REPLACEMENT_CHARS[i] = String
                    .format("\\u%04x", new Object[] { Integer.valueOf(i) });
        }
        REPLACEMENT_CHARS[34] = "\\\"";
        REPLACEMENT_CHARS[92] = "\\\\";
        REPLACEMENT_CHARS[9] = "\\t";
        REPLACEMENT_CHARS[8] = "\\b";
        REPLACEMENT_CHARS[10] = "\\n";
        REPLACEMENT_CHARS[13] = "\\r";
        REPLACEMENT_CHARS[12] = "\\f";
    }

    StringBuilder json;

    public JsonBuilder()
    {
        json = new StringBuilder();
    }

    public JsonBuilder(String string)
    {
        this();
        append(string);
    }

    public void append(String value)
    {
        int last = 0;
        int length = value.length();
        for (int i = 0; i < length; i++)
        {
            char c = value.charAt(i);
            String replacement;
            if (c < '\u0080')
            {
                replacement = REPLACEMENT_CHARS[c];
                if (replacement == null)
                {
                    continue;
                }
            }
            else
            {
                if (c == '\u2028')
                {
                    replacement = "\\u2028";
                }
                else
                {
                    if (c != '\u2029')
                    {
                        continue;
                    }
                    replacement = "\\u2029";
                }
            }
            if (last < i)
            {
                json.append(value, last, i);
            }
            json.append(replacement);
            last = i + 1;
        }
        if (last < length)
        {
            json.append(value, last, length);
        }
    }

    public void deleteLastChar()
    {
        json.deleteCharAt(json.length() - 1);
    }

    public boolean isEmpty()
    {
        return json.length() == 0;
    }

    public int length()
    {
        return json.length();
    }

    @Override public String toString()
    {
        return json.toString();
    }
}
