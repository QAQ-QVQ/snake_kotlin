package com.yu.snake

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.yu.snake.gameView.GameView

/**
 * @author dy
 * @time 2020/6/8 11:17
 */
class MainActivity : AppCompatActivity() {
    lateinit var gameView: GameView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        gameView = GameView(this)
    }
}
