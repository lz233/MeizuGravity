package moe.lz233.meizugravity.cloudmusic.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.ScaleXSpan;
import android.util.AttributeSet;

public class ChanTextView extends androidx.appcompat.widget.AppCompatTextView {
    private static Typeface mTypeface = null;
    private String TAG;
    private CharSequence originalText;
    private float spacing;
    private CharSequence text;

    public ChanTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.TAG = "ChanTextView";
        this.spacing = 0.0f;
        this.originalText = "";
        setFont(context);
        initAttributes(context, attrs);
    }

    public ChanTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        initAttributes(context, attrs);
    }

    public ChanTextView(Context context) {
        this(context, (AttributeSet) null);
        initAttributes(context, (AttributeSet) null);
    }

    private void setFont(Context context) {
        mTypeface = Typeface.createFromAsset(context.getAssets(), "font.otf");
        setTypeface(mTypeface);
    }

    private void initAttributes(Context context, AttributeSet attrs) {
    }

    public float getSpacing() {
        return this.spacing;
    }

    public void setSpacing(float spacing2) {
        this.spacing = spacing2;
    }

    private void applySpacing() {
        if (this.originalText != null) {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < this.originalText.length(); i++) {
                builder.append(this.originalText.charAt(i));
                if (i + 1 < this.originalText.length()) {
                    builder.append(" ");
                }
            }
            SpannableString finalText = new SpannableString(builder.toString());
            if (builder.toString().length() > 1) {
                for (int i2 = 1; i2 < builder.toString().length(); i2 += 2) {
                    finalText.setSpan(new ScaleXSpan((this.spacing + 1.0f) / 10.0f), i2, i2 + 1, 33);
                }
            }
            super.setText(finalText, BufferType.SPANNABLE);
            invalidate();
        }
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setHorizontalFadingEdgeEnabled(false);
    }

    public class Spacing {
        public static final float NORMAL = 0.0f;

        public Spacing() {
        }
    }
}
