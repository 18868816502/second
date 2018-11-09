package com.beiwo.klyjaz.tang;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;

/**
 * https://gitee.com/tangbuzhi
 *
 * @author: Tangbuzhi
 * @version:
 * @package:
 * @description:
 * @modify:
 * @date: 2018/11/8
 */

public class RoundCornerTransformation implements Transformation<Bitmap> {
    private BitmapPool bitmapPool;
    private float radius;//半径
    private float diameter;//直径
    private CornerType default_type = CornerType.ALL;

    public RoundCornerTransformation(Context context, int radiusValue, CornerType type) {
        bitmapPool = Glide.get(context).getBitmapPool();
        if (type != null) default_type = type;
        radius = Resources.getSystem().getDisplayMetrics().density * radiusValue;
        diameter = radius * 2;
    }

    @Override
    public Resource<Bitmap> transform(Resource<Bitmap> resource, int outWidth, int outHeight) {
        Bitmap source = resource.get();
        int width = source.getWidth();
        int height = source.getHeight();
        Bitmap bitmap = bitmapPool.get(width, height, Bitmap.Config.ARGB_8888);
        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        }
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(new BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
        drawRoundRect(canvas, paint, width, height);
        return BitmapResource.obtain(bitmap, bitmapPool);
    }

    private void drawRoundRect(Canvas canvas, Paint paint, float width, float height) {
        switch (default_type) {
            case LEFT:
                drawLeftCorner(canvas, paint, width, height);
                break;
            case TOP:
                drawTopCorner(canvas, paint, width, height);
                break;
            case RIGHT:
                drawRightCorner(canvas, paint, width, height);
                break;
            case BOTTOM:
                drawBottomCorner(canvas, paint, width, height);
                break;
            case LEFT_TOP:
                drawLeftTopCorner(canvas, paint, width, height);
                break;
            case LEFT_BOTTOM:
                drawLeftBottomCorner(canvas, paint, width, height);
                break;
            case RIGHT_TOP:
                drawRightTopCorner(canvas, paint, width, height);
                break;
            case RIGHT_BOTTOM:
                drawRightBottomCorner(canvas, paint, width, height);
                break;
            case ALL:
            default:
                canvas.drawRoundRect(new RectF(0f, 0f, width, height), radius, radius, paint);
                break;
        }
    }

    private void drawLeftCorner(Canvas canvas, Paint paint, float width, float height) {
        canvas.drawArc(new RectF(0f, 0f, diameter, diameter), 180f, 90f, true, paint);//左上
        canvas.drawArc(new RectF(0f, height - diameter, diameter, height), 90f, 90f, true, paint);//左下
        canvas.drawRect(0f, radius, radius, height - radius, paint);
        canvas.drawRect(radius, 0f, width, height, paint);
    }

    private void drawTopCorner(Canvas canvas, Paint paint, float width, float height) {
        canvas.drawArc(new RectF(0f, 0f, diameter, diameter), 180f, 90f, true, paint);//左上
        canvas.drawArc(new RectF(width - diameter, 0f, width, diameter), 270f, 90f, true, paint);//右上
        canvas.drawRect(radius, 0f, width - radius, radius, paint);
        canvas.drawRect(0f, radius, width, height, paint);
    }

    private void drawRightCorner(Canvas canvas, Paint paint, float width, float height) {
        canvas.drawArc(new RectF(width - diameter, 0f, width, diameter), 270f, 90f, true, paint);//右上
        canvas.drawArc(new RectF(width - diameter, height - diameter, width, height), 0f, 90f, true, paint);//右下
        canvas.drawRect(0f, 0f, width - radius, height, paint);
        canvas.drawRect(width - radius, radius, width, height - radius, paint);
    }

    private void drawBottomCorner(Canvas canvas, Paint paint, float width, float height) {
        canvas.drawArc(new RectF(0f, height - diameter, diameter, height), 90f, 90f, true, paint);//左下
        canvas.drawArc(new RectF(width - diameter, height - diameter, width, height), 0f, 90f, true, paint);//右下
        canvas.drawRect(0f, 0f, width, height - radius, paint);
        canvas.drawRect(radius, height - radius, width - radius, height, paint);
    }

    private void drawLeftTopCorner(Canvas canvas, Paint paint, float width, float height) {
        canvas.drawArc(new RectF(0f, 0f, diameter, diameter), 180f, 90f, true, paint);//左上
        canvas.drawRect(radius, 0f, width, radius, paint);
        canvas.drawRect(0f, radius, width, height, paint);
    }

    private void drawLeftBottomCorner(Canvas canvas, Paint paint, float width, float height) {
        canvas.drawArc(new RectF(0f, height - diameter, diameter, height), 90f, 90f, true, paint);//左下
        canvas.drawRect(0f, 0f, width, height - radius, paint);
        canvas.drawRect(radius, height - radius, width, height, paint);
    }

    private void drawRightTopCorner(Canvas canvas, Paint paint, float width, float height) {
        canvas.drawArc(new RectF(width - diameter, 0f, width, diameter), 270f, 90f, true, paint);//右上
        canvas.drawRect(0f, 0f, width - radius, height, paint);
        canvas.drawRect(width - radius, radius, width, height, paint);
    }

    private void drawRightBottomCorner(Canvas canvas, Paint paint, float width, float height) {
        canvas.drawArc(new RectF(width - diameter, height - diameter, width, height), 0f, 90f, true, paint);//右下
        canvas.drawRect(0f, 0f, width, height - radius, paint);
        canvas.drawRect(0f, height - radius, width - radius, height, paint);
    }

    @Override
    public String getId() {
        return getClass().getName();
    }

    public enum CornerType {
        ALL,//全部
        LEFT,//左上+左下
        TOP,//左上+右上
        RIGHT,//右上+右下
        BOTTOM,//左下+右下
        LEFT_TOP,//左上
        LEFT_BOTTOM,//左下
        RIGHT_TOP,//右上
        RIGHT_BOTTOM//右下
    }
}