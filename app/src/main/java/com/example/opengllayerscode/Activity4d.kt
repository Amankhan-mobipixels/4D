package com.example.opengllayerscode

import android.app.WallpaperManager
import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.opengllayerscode.databinding.Preview4dBinding


class Activity4d : AppCompatActivity() {
    private lateinit var binding: Preview4dBinding
    private var renderer4D: Renderer4D? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = Preview4dBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        val imagesData = intent.getStringArrayListExtra("images")
        images?.clear()
            for (value in imagesData!!) {
               images?.add(value)
            }
        val masksData = intent.getStringArrayListExtra("masks")

        masks?.clear()
        for (value in masksData!!) {
            masks?.add(value)
        }

        binding.glView.setEGLContextClientVersion(2)
        renderer4D = Renderer4D(this)
        renderer4D!!.setLists(images, masks)
        binding.glView.setRenderer(renderer4D)
        binding.setWallpaper.setOnClickListener {
            openLWSetter(this)
        }

        binding.setting.setOnClickListener {
            val dialog = SettingDialog(this)
            dialog.show()
        }
    }

    fun openLWSetter(context: Context) {
        val intent = Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER)
        intent.putExtra(
            WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
            ComponentName(context, GLWallpaperService::class.java)
        )
        try {
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context, "Error", Toast.LENGTH_LONG).show()
        }

    }

    override fun onResume() {
        super.onResume()
        renderer4D?.start()
        binding.glView.onResume()
    }

    override fun onPause() {
        super.onPause()
        renderer4D?.stop()
        binding.glView.onPause()
    }
}