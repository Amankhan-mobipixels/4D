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

        sharedPreferences = context.getSharedPreferences("settingsData", Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()

        binding.effectSeek.progress = sharedPreferences.getInt("effect", 5)
        binding.effectTxt.text = "4D Effect: ${binding.effectSeek.progress}"

        binding.zoomSpeedSeek.progress = sharedPreferences.getInt("zoom", 2)
        binding.zoomSpeedTxt.text = "Background Zoom Speed: ${binding.zoomSpeedSeek.progress}"

        binding.zoomIntensitySeek.progress = sharedPreferences.getInt("intensity", 5)
        binding.zoomIntensityTxt.text = "Background Zoom Intensity: ${ binding.zoomIntensitySeek.progress}"

        binding.rotationXAxisSeek.progress = sharedPreferences.getInt("rotationx", 10)
        binding.rotationXAxisTxt.text = "Background Rotation x-axis: ${ binding.rotationXAxisSeek.progress}"

        binding.rotationYAxisSeek.progress = sharedPreferences.getInt("rotationy", 5)
        binding.rotationYAxisTxt.text = "Background Rotation y-axis: ${ binding.rotationYAxisSeek.progress}"
        // SeekBar listeners
        binding.effectSeek.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                binding.effectTxt.text = "4D Effect: $progress"
                editor.putInt("4deffect", progress)
                editor.apply()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        binding.zoomSpeedSeek.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                binding.zoomSpeedTxt.text = "Background Zoom Speed: $progress"
                editor.putInt("zoomspeed", progress)
                editor.apply()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        binding.zoomIntensitySeek.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                binding.zoomIntensityTxt.text = "Background Zoom Intensity: $progress"
                editor.putInt("zoomintensity", progress)
                editor.apply()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        binding.rotationXAxisSeek.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                binding.rotationXAxisTxt.text = "Background Rotation x-axis: $progress"
                editor.putInt("rotationx", progress)
                editor.apply()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        binding.rotationYAxisSeek.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                binding.rotationYAxisTxt.text = "Background Rotation y-axis: $progress"
                editor.putInt("rotationy", progress)
                editor.apply()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
    }
}