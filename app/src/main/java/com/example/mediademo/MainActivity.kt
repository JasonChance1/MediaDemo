package com.example.mediademo

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.ToastUtils
import com.example.mediademo.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    val musics = listOf(R.raw.msc2,R.raw.msc1)
    val names = mutableListOf<String>()
    var currentIndex = 0
    private var index = 0
    private var mediaPlayer: MediaPlayer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        for(i in musics.indices){
            names.add("msc${i+1}")
        }

        val permissionUtil = PermissionUtil()

        if (!permissionUtil.isExternalStoragePermission()){
            permissionUtil.requestExternalStoragePermission()
        }

        if(!permissionUtil.isStorageManagerPermission()){
            permissionUtil.requestStorageManagerPermission(this)
        }

        val rotateAnimator = ObjectAnimator.ofFloat(binding.cover, View.ROTATION, 0f, 360f)
        rotateAnimator.duration = 5000
        rotateAnimator.repeatCount = ValueAnimator.INFINITE
        rotateAnimator.interpolator = LinearInterpolator()

        val animatorSet = AnimatorSet()
        animatorSet.play(rotateAnimator)

        mediaPlayer = MediaPlayer.create(this, R.raw.msc2)

        if (mediaPlayer != null) {
            binding.play.setOnClickListener {
                if (mediaPlayer!!.isPlaying) {
                    mediaPlayer!!.pause()
                } else {
                    mediaPlayer!!.start()
                }
            }
        } else {
            // 处理创建MediaPlayer失败的情况
            ToastUtils.showLong("无法创建MediaPlayer")
        }

        binding.play.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // 开始动画
                animatorSet.start()
                playCurrent()
            } else {
                // 暂停动画
                animatorSet.pause()
                stopCurrent()
            }
        }

        binding.next.setOnClickListener {
            playNextSong()
        }
    }

    private fun playCurrent(){
        val msc = musics[index]

        binding.name.text = names[currentIndex]
        if(mediaPlayer!=null){
            mediaPlayer?.start()
        }else{
            mediaPlayer = MediaPlayer.create(this,msc)
            mediaPlayer?.start()
        }

    }

    private fun stopCurrent(){
        mediaPlayer?.stop()
    }

    private fun playNextSong() {
        if(mediaPlayer!=null){
            // 停止当前歌曲
            mediaPlayer!!.stop()
            mediaPlayer!!.release()

            // 移到下一首歌曲
            index = (index + 1) % musics.size
            binding.name.text = names[index]
            currentIndex = index

            // 创建并播放下一首歌曲
            mediaPlayer = MediaPlayer.create(this, musics[index])
            mediaPlayer!!.start()

        }

    }
}