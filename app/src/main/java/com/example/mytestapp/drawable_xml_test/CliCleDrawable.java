package com.example.mytestapp.drawable_xml_test;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class CliCleDrawable extends Drawable {

    private Paint paint;
    private int radiu;
    private int width;
    private int height;
    private Bitmap bitmap;

    public CliCleDrawable(Bitmap bitmap) {
        this.bitmap = bitmap;
        BitmapShader bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(bitmapShader);
        width = Math.min(bitmap.getWidth(), bitmap.getHeight());


    }


    @Override
    public void draw(@NonNull Canvas canvas) {

        canvas.drawCircle(width / 2, width / 2, width / 2, paint);
    }

    @Override
    public void setAlpha(int alpha) {

        paint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        paint.setColorFilter(colorFilter);

    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public int getIntrinsicHeight() {
        return width;
    }

    @Override
    public int getIntrinsicWidth() {
        return width;
    }
}
