package com.yu.snake.gameView

import com.yu.snake.gameModel.GridSquare

/**
 *   CREATED BY DY ON 2020/6/11.
 *   TIME BY 10:25.
 *   @author DY
 **/
interface IGameView {
    /*
     * 初始化成功
     */
    fun InitSuccess(mGridSquare: ArrayList<ArrayList<GridSquare>>)
    fun PostInvalidate(mGridSquare: ArrayList<ArrayList<GridSquare>>)
    fun ShowMessageDialog(Msg:String,Title:String)
}