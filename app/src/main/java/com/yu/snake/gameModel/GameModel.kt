package com.yu.snake.gameModel

import android.os.Parcel
import android.os.Parcelable

/**
 *   CREATED BY DY ON 2020/6/11.
 *   TIME BY 10:14.
 *   @author DY
 **/
class GameModel : IGameModel {
    //    private lateinit var iGameModelCallback:IGameModelCallback
    lateinit var iGameModelCallback: (() -> Unit)
//    set(value) {iGameModelCallback = value}
    //地图的二维数组
    private lateinit var mGridSquare: ArrayList<ArrayList<GridSquare>>

    override fun Init(mGridSquare: ArrayList<ArrayList<GridSquare>>) {
//        //重置所有网格
//        mGridSquare = ArrayList<ArrayList<GridSquare>>()
//        var squares: ArrayList<GridSquare>
//        for (i in 0 until mGridSizeX) {
//            squares = ArrayList<GridSquare>()
//            for (j in 0 until mGridSizeY) {
//                squares.add(GridSquare(context, i, j, gameObject.GRID))//设置每个格子的属性为空白
//            }
//            mGridSquare.add(squares)
//        }
        this.mGridSquare = mGridSquare
        iGameModelCallback.invoke()
//        iGameModelCallback.Success()
    }

    constructor()

    //    interface IGameModelCallback{
//        fun Success()
//        fun Fail()
//    }
    fun SetIGameModelCallback(iGameModelCallback: (() -> Unit)) {
        this.iGameModelCallback = iGameModelCallback
    }
}