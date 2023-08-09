package com.example.opengllayerscode

import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.SeekBar
import com.example.opengllayerscode.databinding.CustomDialogLayoutBinding

class SettingDialog(context: Context) :Dialog(context){
    private lateinit var binding: CustomDialogLayoutBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CustomDialogLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = context.getSharedPreferences("seekbarprefs", Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()

        binding.effectSeek.progress = sharedPreferences.getInt("3deffect", 5)
        binding.effectTxt.text = "3D Effect: ${binding.effectSeek.progress}"

        binding.zoomSpeedSeek.progress = sharedPreferences.getInt("zoomspeed", 2)
        binding.zoomSpeedTxt.text = "Zoom Speed: ${binding.zoomSpeedSeek.progress}"

        binding.zoomIntensitySeek.progress = sharedPreferences.getInt("zoomintensity", 5)
        binding.zoomIntensityTxt.text = "Zoom Intensity: ${ binding.zoomIntensitySeek.progress}"

        // SeekBar listeners
        binding.effectSeek.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                binding.effectTxt.text = "3D Effect: $progress"
                editor.putInt("3deffect", progress)
                editor.apply()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        binding.zoomSpeedSeek.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                binding.zoomSpeedTxt.text = "Zoom Speed: $progress"
                editor.putInt("zoomspeed", progress)
                editor.apply()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        binding.zoomIntensitySeek.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                binding.zoomIntensityTxt.text = "Zoom Intensity: $progress"
                editor.putInt("zoomintensity", progress)
                editor.apply()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
    }
}