package com.example.timer

import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.view.KeyEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.timer.databinding.ActivityTimerBinding

class TimerActivity : AppCompatActivity() {
    //엑티비티 시작 시간 저장
    private var initTime = 0L
    //뒤로 가기 할 때 누른 시각 저장
    private var backtime = 0L
    //멈춘 시각 저장
    private var pauseTime = 0L
    // 넘겨줄 시각 저장
    private var thisTime = 0L
    //chronometer 사용 여부 체크
    var chronometercheck = false
    lateinit var binding: ActivityTimerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTimerBinding.inflate(layoutInflater)
        title = "타이머"
        setContentView(binding.root)

        binding.startBtn.setOnClickListener{
            binding.chronometer.base = SystemClock.elapsedRealtime() + pauseTime
            initTime = System.currentTimeMillis()
            pauseTime = 0L
            chronometercheck = true
            binding.chronometer.start()
            // 버튼 표시 여부 변경.
            binding.stopBtn.isEnabled = true
            binding.resetBtn.isEnabled = true
            binding.startBtn.isEnabled = false
        }
        binding.stopBtn.setOnClickListener {
            pauseTime = binding.chronometer.base - SystemClock.elapsedRealtime()
            binding.chronometer.stop()
            binding.stopBtn.isEnabled = false
            binding.resetBtn.isEnabled = true
            binding.startBtn.isEnabled = true
        }
        binding.resetBtn.setOnClickListener {
            pauseTime = 0L
            thisTime = 0L
            chronometercheck = false
            binding.chronometer.base = SystemClock.elapsedRealtime()
            binding.chronometer.stop()
            binding.stopBtn.isEnabled = false
            binding.resetBtn.isEnabled = false
            binding.startBtn.isEnabled = true
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(System.currentTimeMillis() - backtime>3000){
                Toast.makeText(this, "저장하려면 한 번 더 누르세요!!",
                    Toast.LENGTH_SHORT).show()
                backtime = System.currentTimeMillis()
                //main으로 시간 넘겨주기.
                thisTime = -pauseTime
                if (thisTime == 0L && chronometercheck == true){
                    thisTime = System.currentTimeMillis() - initTime
                }
                // 밀리초 단위를 초단위로 계산.
                val time:Int = (thisTime / 1000).toInt()
                //시간 계산
                val hour:Int = (time / (60*60)).toInt()
                val min:Int = (time % (60*60) / 60).toInt()
                val sec:Int = (time % 60).toInt()
                val intent = Intent()
                val resultTime:String = hour.toString() + " : " + min.toString() + " : " + sec.toString()
                intent.putExtra("thisTime", resultTime)
                setResult(RESULT_OK, intent)
                finish()

                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }
}