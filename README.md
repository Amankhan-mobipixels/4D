# 4D
add maven in your project level gradle
````
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' 
		}
	}
}
````
add dependency in module level gradle
````
dependencies:
{
implementation 'com.github.Amankhan-mobipixels:4D:1.1.4'
}
````
How to use:

     //versions 1.1.5
     val intent = Intent(this, Activity4d::class.java)
        intent.putIntegerArrayListExtra("images", images)
        intent.putIntegerArrayListExtra("masks", masks)
        startActivity(intent)

   //version 1.1.6
   
    //launch service directly
    
    val sharedPreferences = getSharedPreferences("settingsData", Context.MODE_PRIVATE)
        images?.clear()
        masks?.clear()
        images?.add(value)
	masks?.add(value)
        val editor = sharedPreferences.edit()
        editor.putString("effect", effectValue)
        editor.putString("intensity", intensityValue)
        editor.putString("zoom", zoomValue)
        editor.putString("rotationx", rotationXValue)
        editor.putString("rotationy", rotationYValue)
        editor.apply()

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

    //use 4d in view
    
     //xml
      <android.opengl.GLSurfaceView
        android:id="@+id/gl_view"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:layout_constraintBottom_toTopOf="@+id/set_wallpaper"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />

// kotlin
{
       private var renderer4D: Renderer4D? = null
     
     override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
        binding.glView.setEGLContextClientVersion(2)
        renderer4D = Renderer4D(this)
	// must add masks and images first to call serLists
        renderer4D!!.setLists(images, masks)
        binding.glView.setRenderer(renderer4D)
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
