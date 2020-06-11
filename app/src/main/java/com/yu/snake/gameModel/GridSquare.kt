package com.yu.snake.gameModel

import android.content.Context
import android.graphics.Color
import android.util.Log
import com.yu.snake.gameManager.GameManager
import com.yu.snake.R


/**
 *   CREATED BY DY ON 2020/6/10.
 *   TIME BY 9:03.
 *   @author 2020/6/10
 **/
class GridSquare(context: Context, x: Int, y: Int, Type: Int) {
    private var TAG: String = "GridSquare"
    private var context: Context
    var Type: Int
    var x: Int
    var y: Int

    init {
        this.context = context
        this.Type = Type
        this.x = x
        this.y = y
    }

    fun getColor(): Int {
        return when (this.Type) {
            GameManager().GameObject().GRID//空格子
            -> context.resources.getColor(R.color.colorBox, null)
            GameManager().GameObject().FOOD//食物
            -> context.resources.getColor(R.color.colorFood, null)
            GameManager().GameObject().SNAKE//蛇
            -> context.resources.getColor(R.color.colorSnake, null)
            else -> Color.WHITE
        }
    }
}