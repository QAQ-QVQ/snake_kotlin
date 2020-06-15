package com.yu.snake.gameModel

import android.content.Context
import android.graphics.Color
import com.yu.snake.GameConfig
import com.yu.snake.R


/**
 *   CREATED BY DY ON 2020/6/10.
 *   TIME BY 9:03.
 *   @author 2020/6/10
 **/
class GridSquare {
    private var TAG: String = "GridSquare"
    private lateinit var context: Context
    //格子类型
    lateinit var Type: GameConfig
    var x: Int = 0
    var y: Int = 0
    //格子尺寸 单位px
    var GrldSize: Int = 40

    constructor()
    constructor(context: Context, x: Int, y: Int, Type: GameConfig) {
        this.context = context
        this.Type = Type
        this.x = x
        this.y = y
    }

    fun getColor(): Int {
        return when (this.Type) {
            GameConfig.GRID//空格子
            -> context.resources.getColor(R.color.colorBox, null)
            GameConfig.FOOD//食物
            -> context.resources.getColor(R.color.colorFood, null)
            GameConfig.SNAKE//蛇
            -> context.resources.getColor(R.color.colorSnake, null)
            else -> Color.WHITE
        }
    }
}