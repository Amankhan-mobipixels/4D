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
implementation 'com.github.Amankhan-mobipixels:Admob-Ads:1.0.3'
}
````
How to use:

        loadInterstitialAd(this, getString(R.string.interstitial_ad)) { loaded, failed ->
            if (loaded) Log.d("adddddd", "loaded")
            if (failed) Log.d("adddddd", "failed")
        }
