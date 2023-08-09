package com.example.opengllayerscode;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;
import androidx.annotation.DrawableRes;
import java.util.List;

public class GLTextureHelper {

    private static final String TAG = "GLTextureHelper";

    public static void generateLayers(int[] textures, List<Integer> drawableIds, int overlayColor,Context context) {
        int layerCount = drawableIds.size();
        Bitmap tempBitmap = null;

        for (int i = 0; i < textures.length; i++) {
            if (i < layerCount) {
                // Load bitmap from drawable resource
                int drawableId = drawableIds.get(i);
                tempBitmap = decodeScaledFromRes(context.getResources(), drawableId);
                if (tempBitmap == null) {
                    Log.e(TAG, "Failed to decode bitmap from drawable: " + context.getResources().getResourceName(drawableId));
                    return;
                }
            } else {
                // Generate overlay bitmap
                if (tempBitmap != null) {
                    tempBitmap.eraseColor(overlayColor);
                } else {
                    Log.e(TAG, "Failed to generate overlay bitmap. Layer bitmap is null.");
                    return;
                }
            }

            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[i]);

            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT);

            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, tempBitmap, 0);

            // Free memory
            tempBitmap.recycle();
        }
    }

    private static Bitmap decodeScaledFromRes(Resources res, @DrawableRes int id) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeResource(res, id, options);

        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeResource(res, id, options);
    }

}
