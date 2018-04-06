//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package apps.joe.com.jnotes;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.view.View;


public class RTextEditorToolbar extends LinearLayoutCompat {
    private RTextEditorView rTextEditorView;

    public RTextEditorToolbar(Context context) {
        super(context);
    }

    public RTextEditorToolbar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RTextEditorToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected void onFinishInflate() {
        super.onFinishInflate();
        this.init();
    }

    private void init() {
        for(int view = 0; view < this.getChildCount(); ++view) {
            View toolButton = this.getChildAt(view);
            if(toolButton instanceof RTextEditorButton) {
                RTextEditorButton button = (RTextEditorButton)toolButton;
                final int toolType = button.getToolType();
                button.setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        if(RTextEditorToolbar.this.rTextEditorView != null) {
                            RTextEditorToolbar.this.rTextEditorView.setFormat(toolType);
                        }

                    }
                });
            }
        }

    }

    public void setEditorView(@NonNull RTextEditorView rTextEditorView) {
        this.rTextEditorView = rTextEditorView;
    }
}
