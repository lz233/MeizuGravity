package io.github.lz233.meizugravity.utils;

import android.content.Context;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.view.View;
import android.widget.Toast;

import androidx.core.view.ViewCompat;

import io.github.lz233.meizugravity.view.ChanTextView;

public class ToastUtil {
    private static ChanTextView chanTextView;
    private static Toast toast = null;

    private ToastUtil() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static void showShort(Context context, CharSequence message) {
        Toast mToast = getToast(context);
        mToast.setView(getView(context, message));
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.show();
    }

    public static void showLong(Context context, CharSequence message) {
        Toast mToast = getToast(context);
        mToast.setView(getView(context, message));
        mToast.setDuration(Toast.LENGTH_LONG);
        mToast.show();
    }

    private static View getView(Context mContext, CharSequence message) {
        if (chanTextView == null) {
            chanTextView = new ChanTextView(mContext);
            chanTextView.setTextSize(20.0f);
            chanTextView.setTextColor(ViewCompat.MEASURED_STATE_MASK);
            chanTextView.setGravity(17);
            chanTextView.setBackgroundColor(-1);
            chanTextView.setWidth(180);
            chanTextView.setHeight(getHeight(String.valueOf(message)) + 20);
            //chanTextView.setHeight(mContext.getResources().getConfiguration().locale.equals(Locale.SIMPLIFIED_CHINESE) ? 40 : 50);
        }
        chanTextView.setText(message);
        return chanTextView;
    }

    private static int getHeight(String text) {
        TextPaint myTextPaint = new TextPaint();
        myTextPaint.setAntiAlias(true);
        myTextPaint.setTextSize(20.0f);
        int width = 180;
        Layout.Alignment alignment = Layout.Alignment.ALIGN_NORMAL;
        float spacingMultiplier = 1;
        float spacingAddition = 0;
        boolean includePadding = false;
        StaticLayout myStaticLayout = new StaticLayout(text, myTextPaint, width, alignment, spacingMultiplier, spacingAddition, includePadding);
        return myStaticLayout.getHeight();
    }

    private static Toast getToast(Context context) {
        if (toast == null) {
            toast = new Toast(context);
        }
        return toast;
    }
}
