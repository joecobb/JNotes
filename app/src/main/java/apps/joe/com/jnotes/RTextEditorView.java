package apps.joe.com.jnotes;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build.VERSION;
import android.support.annotation.ColorInt;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class RTextEditorView extends WebView {
    private static final String ASSETS_EDITOR_HTML = "file:///android_asset/editor.html";
    private boolean isReady;
    private boolean isIncognitoModeEnabled;
    private String content;
    private RTextEditorView.OnTextChangeListener onTextChangeListener;

    public RTextEditorView(Context context) {
        super(context);
    }

    public RTextEditorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init();
    }

    public RTextEditorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init();
    }

    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface", "WrongConstant"})
    private void init() {
        WebSettings settings = this.getSettings();
        settings.setCacheMode(2);
        settings.setTextZoom(120);
        settings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        this.setWebChromeClient(new WebChromeClient());
        this.setWebViewClient(new RTextEditorView.RTextEditorWebViewClient(null));
        this.addJavascriptInterface(this, "RTextEditorView");
        this.loadUrl("file:///android_asset/editor.html");
    }

    public void destroy() {
        this.exec("javascript:destroy();");
        ViewGroup parent = (ViewGroup)this.getParent();
        if(parent != null) {
            parent.removeView(this);
        }

        super.destroy();
    }

    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        InputConnection inputConnection = super.onCreateInputConnection(outAttrs);
        if(this.isIncognitoModeEnabled) {
            outAttrs.imeOptions |= 16777216;
        }

        return inputConnection;
    }

    public void setIncognitoModeEnabled(boolean enabled) {
        this.isIncognitoModeEnabled = enabled;
    }

    public void setOnTextChangeListener(@Nullable RTextEditorView.OnTextChangeListener listener) {
        this.onTextChangeListener = listener;
    }

    @JavascriptInterface
    public void onEditorContentChanged(String content) {
        if(this.onTextChangeListener != null) {
            this.onTextChangeListener.onTextChanged(content);
        }

        this.content = content;
    }

    @JavascriptInterface
    public void updateCurrentStyle(String style) {
    }

    public void enable() {
        this.exec("javascript:enable();");
    }

    public void disable() {
        this.exec("javascript:disable();");
    }

    public void undo() {
        this.exec("javascript:undo();");
    }

    public void redo() {
        this.exec("javascript:redo();");
    }

    public void clear() {
        this.exec("javascript:clear();");
    }

    public void focus() {
        this.exec("javascript:setFocus();");
    }

    public void setHtml(@NonNull String html) {
        this.exec("javascript:setHtml('" + html + "');");
    }

    public String getHtml() {
        return this.content;
    }

    public void setBold() {
        this.exec("javascript:setBold();");
    }

    public void setItalic() {
        this.exec("javascript:setItalic();");
    }

    public void setUnderline() {
        this.exec("javascript:setUnderline();");
    }

    public void setStrikeThrough() {
        this.exec("javascript:setStrikeThrough();");
    }

    public void removeFormat() {
        this.exec("javascript:removeFormat();");
    }

    public void setFontSize(int sizeInPx) {
        this.exec("javascript:setFontSize(" + sizeInPx + ");");
    }

    public void setNormal() {
        this.exec("javascript:setNormal();");
    }

    public void setHeading(@IntRange(from = 1L,to = 6L) int value) {
        this.exec("javascript:setHeading('" + value + "');");
    }

    public void setLineHeight(int heightInPx) {
        this.exec("javascript:setLineHeight(" + heightInPx + ");");
    }

    public void setSuperscript() {
        this.exec("javascript:setSuperscript();");
    }

    public void setSubscript() {
        this.exec("javascript:setSubscript()");
    }

    public void setTextColor(@ColorInt int color) {
        this.setTextColor(String.format("#%06X", new Object[]{Integer.valueOf(16777215 & color)}));
    }

    public void setTextColor(@NonNull String hexColor) {
        this.exec("javascript:setTextForeColor('" + hexColor + "');");
    }

    public void setTextBackgroundColor(@ColorInt int color) {
        this.setTextBackgroundColor(String.format("#%06X", new Object[]{Integer.valueOf(16777215 & color)}));
    }

    public void setTextBackgroundColor(@NonNull String hexColor) {
        this.exec("javascript:setTextBackColor('" + hexColor + "');");
    }

    public void setBlockCode() {
        this.exec("javascript:setBlockCode();");
    }

    public void setUnorderedList() {
        this.exec("javascript:insertUnorderedList();");
    }

    public void setOrderedList() {
        this.exec("javascript:insertOrderedList();");
    }

    public void setBlockQuote() {
        this.exec("javascript:setBlockQuote();");
    }

    public void setAlignLeft() {
        this.exec("javascript:setAlignLeft();");
    }

    public void setAlignCenter() {
        this.exec("javascript:setAlignCenter();");
    }

    public void setAlignRight() {
        this.exec("javascript:setAlignRight();");
    }

    public void setAlignJustify() {
        this.exec("javascript:setAlignJustify();");
    }

    public void insertHorizontalRule() {
        this.exec("javascript:insertHorizontalRule();");
    }

    public void setIndent() {
        this.exec("javascript:indent();");
    }

    public void setOutdent() {
        this.exec("javascript:outdent();");
    }

    public void insertTable(int colCount, int rowCount) {
        this.exec("javascript:insertTable('" + colCount + "x" + rowCount + "');");
    }

    public void insertLink(@Nullable String title, @NonNull String url) {
        this.exec("javascript:insertLink('" + title + "','" + url + "');");
    }

    public void setUnlink() {
        this.exec("javascript:unlink();");
    }

    public void insertText(@NonNull String text) {
        this.exec("javascript:insertText('" + text + "');");
    }

    public void editHtml() {
        this.exec("javascript:editHtml();");
    }

    public void setFormat(int type) {
        switch(type) {
        case 0:
        case 15:
        case 16:
        case 28:
        case 29:
        default:
            break;
        case 1:
            this.setBold();
            break;
        case 2:
            this.setItalic();
            break;
        case 3:
            this.setUnderline();
            break;
        case 4:
            this.setStrikeThrough();
            break;
        case 5:
            this.removeFormat();
            break;
        case 6:
            this.setNormal();
            break;
        case 7:
            this.setHeading(1);
            break;
        case 8:
            this.setHeading(2);
            break;
        case 9:
            this.setHeading(3);
            break;
        case 10:
            this.setHeading(4);
            break;
        case 11:
            this.setHeading(5);
            break;
        case 12:
            this.setHeading(6);
            break;
        case 13:
            this.setSuperscript();
            break;
        case 14:
            this.setSubscript();
            break;
        case 17:
            this.setBlockCode();
            break;
        case 18:
            this.setUnorderedList();
            break;
        case 19:
            this.setOrderedList();
            break;
        case 20:
            this.setBlockQuote();
            break;
        case 21:
            this.setAlignLeft();
            break;
        case 22:
            this.setAlignCenter();
            break;
        case 23:
            this.setAlignRight();
            break;
        case 24:
            this.setAlignJustify();
            break;
        case 25:
            this.insertHorizontalRule();
            break;
        case 26:
            this.setIndent();
            break;
        case 27:
            this.setOutdent();
            break;
        case 30:
            this.setUnlink();
            break;
        case 31:
            this.clear();
            break;
        case 32:
            this.editHtml();
        }

    }

    protected void exec(@NonNull final String trigger) {
        if(this.isReady) {
            this.load(trigger);
        } else {
            this.postDelayed(new Runnable() {
                public void run() {
                    RTextEditorView.this.exec(trigger);
                }
            }, 100L);
        }

    }

    private void load(@NonNull String trigger) {
        if(VERSION.SDK_INT >= 19) {
            this.evaluateJavascript(trigger, (ValueCallback)null);
        } else {
            this.loadUrl(trigger);
        }

    }

    private class RTextEditorWebViewClient extends WebViewClient {
        private RTextEditorWebViewClient(Object o) {
        }

        public void onPageFinished(WebView view, String url) {
            RTextEditorView.this.isReady = url.equalsIgnoreCase("file:///android_asset/editor.html");
            super.onPageFinished(view, url);
        }

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @TargetApi(24)
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            view.loadUrl(request.getUrl().toString());
            return true;
        }
    }

    public interface OnTextChangeListener {
        void onTextChanged(String var1);
    }
}
