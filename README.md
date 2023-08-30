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
implementation 'com.github.Amankhan-mobipixels:4D:1.1.3'
}
````
How to use:

     val intent = Intent(this, Activity4d::class.java)
        intent.putIntegerArrayListExtra("images", images)
        intent.putIntegerArrayListExtra("masks", masks)
        startActivity(intent)
