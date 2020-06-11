package com.yu.snake.gameView

/**
 *   CREATED BY DY ON 2020/6/11.
 *   TIME BY 10:25.
 *   @author DY
 **/
interface IGameView {
    /*
     * 初始化成功
     */
    fun InitSuccess()
    fun PostInvalidate()
    fun ShowMessageDialog(Msg:String,Title:String)
}