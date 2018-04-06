//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package apps.joe.com.jnotes;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatImageButton;
import android.util.AttributeSet;


public class RTextEditorButton extends AppCompatImageButton {

    private int toolType;

    public RTextEditorButton(Context context) {
        super(context);
    }

    public RTextEditorButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.RTextEditorButton, 0, 0);

        try {
            this.toolType = a.getInteger(R.styleable.RTextEditorButton_toolType, 0);
        } finally {
            a.recycle();
        }

    }

    public RTextEditorButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public int getToolType() {
        return this.toolType;
    }

    public void setToolType(int toolType) {
        this.toolType = toolType;
    }
}
