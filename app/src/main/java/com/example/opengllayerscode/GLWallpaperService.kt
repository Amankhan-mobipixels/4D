package com.example.opengllayerscode

import android.content.Context
import android.opengl.GLSurfaceView
import android.service.wallpaper.WallpaperService
import android.view.SurfaceHolder

class GLWallpaperService : WallpaperService() {

    override fun onCreateEngine(): Engine {
        return GLEngine()
    }

    inner class GLEngine : Engine() {

        inner class WallpaperGLSurfaceView(context: Context) : GLSurfaceView(context) {
            override fun getHolder(): SurfaceHolder {
                return surfaceHolder
            }
            fun onDestroy() {
                super.onDetachedFromWindow()
            }
        }

        private var glSurfaceView: WallpaperGLSurfaceView? =null
        private  var renderer4D: Renderer4D? = null
        override fun onCreate(surfaceHolder: SurfaceHolder) {
            super.onCreate(surfaceHolder)
            glSurfaceView = WallpaperGLSurfaceView(this@GLWallpaperService)
            glSurfaceView?.setEGLContextClientVersion(2)
            renderer4D = Renderer4D(this@GLWallpaperService)
            renderer4D?.setLists(images, masks)
            glSurfaceView?.setRenderer(renderer4D)
        }

        override fun onVisibilityChanged(visible: Boolean) {
            super.onVisibilityChanged(visible)
            if (visible) {
                glSurfaceView?.onResume()
                renderer4D?.start()
            } else {
                renderer4D?.stop()
                glSurfaceView?.onPause()
            }
        }

        override fun onDestroy() {
            super.onDestroy()
            glSurfaceView?.onDestroy()
        }

    }
}