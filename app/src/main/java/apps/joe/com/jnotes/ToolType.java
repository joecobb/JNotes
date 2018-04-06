package apps.joe.com.jnotes;


import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;



@IntDef({
        ToolType.NONE, ToolType.BOLD, ToolType.ITALIC, ToolType.UNDERLINE, ToolType.STRIKETHROUGH, ToolType.REMOVE_FORMAT, ToolType.NORMAL, ToolType.H1, ToolType.H2, ToolType.H3, ToolType.H4, ToolType.H5, ToolType.H6,
        ToolType.SUPERSCRIPT, ToolType.SUBSCRIPT, ToolType.TEXT_FORE_COLOR, ToolType.TEXT_BACK_COLOR, ToolType.CODE, ToolType.UNORDERED_LIST,
        ToolType.ORDERED_LIST, ToolType.QUOTE, ToolType.ALIGN_LEFT, ToolType.ALIGN_CENTER, ToolType.ALIGN_RIGHT, ToolType.ALIGN_JUSTIFY, ToolType.HORIZONTAL_RULE,
        ToolType.INDENT, ToolType.OUTDENT, ToolType.TABLE, ToolType.LINK, ToolType.UNLINK, ToolType.CLEAR, ToolType.EDIT_HTML
})
@Retention(RetentionPolicy.SOURCE)
public @interface ToolType {
    int NONE = 0;
    int BOLD = 1;
    int ITALIC = 2;
    int UNDERLINE = 3;
    int STRIKETHROUGH = 4;
    int REMOVE_FORMAT = 5;
    int NORMAL = 6;
    int H1 = 7;
    int H2 = 8;
    int H3 = 9;
    int H4 = 10;
    int H5 = 11;
    int H6 = 12;
    int SUPERSCRIPT = 13;
    int SUBSCRIPT = 14;
    int TEXT_FORE_COLOR = 15;
    int TEXT_BACK_COLOR = 16;
    int CODE = 17;
    int UNORDERED_LIST = 18;
    int ORDERED_LIST = 19;
    int QUOTE = 20;
    int ALIGN_LEFT = 21;
    int ALIGN_CENTER = 22;
    int ALIGN_RIGHT = 23;
    int ALIGN_JUSTIFY = 24;
    int HORIZONTAL_RULE = 25;
    int INDENT = 26;
    int OUTDENT = 27;
    int TABLE = 28;
    int LINK = 29;
    int UNLINK = 30;
    int CLEAR = 31;
    int EDIT_HTML = 32;
}
