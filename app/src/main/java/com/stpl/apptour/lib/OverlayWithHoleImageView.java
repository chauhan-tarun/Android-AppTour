package com.stpl.apptour.lib;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.View;

public class OverlayWithHoleImageView extends AppCompatImageView {

    private RectF circleRect;
    private int radius;

    private Paint paint;
    private PorterDuffXfermode porterDuff;

    public OverlayWithHoleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        //In versions > 3.0 need to define layer Type
        if (android.os.Build.VERSION.SDK_INT >= 11) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        porterDuff = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);
    }

    public void setCircle(RectF rect) {
        this.circleRect = rect;
        this.radius = (int) (rect.right - rect.left);

        //Redraw after defining circle
        postInvalidate();
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (circleRect != null) {

            // Draw Overlay
            paint.setColor(Color.parseColor("#99000000"));
            paint.setStyle(Paint.Style.FILL);
            canvas.drawPaint(paint);

            // Draw transparent shape
            paint.setXfermode(porterDuff);
            canvas.drawRoundRect(circleRect, radius, radius, paint);
        }
    }
}
