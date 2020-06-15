package com.yu.snake.gameView

import android.app.AlertDialog
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import com.yu.snake.R
import com.yu.snake.gameManager.GameManager
import com.yu.snake.gameModel.GridSquare

/**
 *   CREATED BY DY ON 2020/6/8.
 *   TIME BY 15:43.
 *   @author 2020/6/8
 **/
class GameView : View, IGameView {
    private var TAG: String = "GameView"
    //x轴格子数目
    var mGridSizeX: Int = 0
    //y轴格子数目
    var mGridSizeY: Int = 0
    var mRectSize: Int = GridSquare().GrldSize
    // 格子画笔
    private var mGridPaint: Paint = Paint()
    //边缘画笔
    private var mStrokePaint: Paint = Paint()
    //地图的二维数组
    private lateinit var mGridSquare: ArrayList<ArrayList<GridSquare>>

    private lateinit var gameManager: GameManager

    constructor(context: Context) : super(context) {
        Log.i(TAG, "init1")
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        Log.i(TAG, "init2")
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        Log.i(TAG, "init3")
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
//        mGridSizeX = (w / mRectSize) - 2
//        mGridSizeY = (h / mRectSize) - 2
        mGridSizeX = 10
        mGridSizeY = 10
        gameManager = GameManager(context, this, mGridSizeX, mGridSizeY)
        gameManager.InitData()
    }

    override fun InitSuccess(mGridSquare: ArrayList<ArrayList<GridSquare>>) {
        this.mGridSquare = mGridSquare
        gameManager.StartGame()
    }

    //刷新数据
    override fun PostInvalidate(mGridSquare: ArrayList<ArrayList<GridSquare>>) {
        this.mGridSquare = mGridSquare
        postInvalidate()
    }

    //显示弹框
    override fun ShowMessageDialog(Msg: String, Title: String) {
        post {
            AlertDialog.Builder(context).setMessage(Msg).setTitle(Title)
                .setCancelable(false)
                .setPositiveButton("开始") { dialog, _ ->
                    dialog.dismiss()
                    gameManager.StartGame()
                }
                .setNegativeButton("退出") { dialog, _ ->
                    dialog.dismiss()
                    Toast.makeText(context, "菜逼啊！", Toast.LENGTH_SHORT).show()
                    System.exit(0)
                }
                .create()
                .show()
        }
    }

    // 起始点和偏移点
    private var startX: Float = 0.toFloat()
    private var startY: Float = 0.toFloat()
    private var offsetX: Float = 0.toFloat()
    private var offsetY: Float = 0.toFloat()
    // 识别手势
    override fun onTouchEvent(event: MotionEvent): Boolean {
        /**
         * 交互逻辑 :我们其实只要知道两点，用户手指按下的坐标点和手指离开的坐标点，然后进行比对，就能识别出用户的意图了
         */
        when (event.action) {
            // 手指按下
            MotionEvent.ACTION_DOWN -> {
                // 记录按下的x,y坐标
                startX = event.x
                startY = event.y
            }
            // 手指离开
            MotionEvent.ACTION_UP -> {
                // 手指离开之后计算偏移量(离开的位置-按下的位置在进行判断是往哪个方向移动)
                offsetX = event.x - startX
                offsetY = event.y - startY
                // 开始识别方向
                // offsetX 的绝对值大于offsetY的绝对值 说明在水平方向
                if (Math.abs(offsetX) > Math.abs(offsetY)) {
                    // (直接<0 会有些许误差，我们可以 <-5)
                    if (offsetX < -5) {
                        // 左
                        gameManager.MoveLeft()
                    } else if (offsetX > 5) {
                        // 右
                        gameManager.MoveRight()
                    }
                    // 开始计算垂直方向上下的滑动
                } else {
                    if (offsetY < -5) {
                        // 上
                        gameManager.MoveUp()
                    } else if (offsetY > 5) {
                        // 下
                        gameManager.MoveDown()
                    }
                }
            }
        }
        return true
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawColor(Color.WHITE)
        //格子画笔
        mGridPaint.reset()
        mGridPaint.isAntiAlias = true
        mGridPaint.style = Paint.Style.FILL
        mGridPaint.isAntiAlias = true
        //边缘画笔
        mStrokePaint.reset()
        mStrokePaint.color = resources.getColor(R.color.colorRaw, null)
        mStrokePaint.style = Paint.Style.STROKE
        mStrokePaint.isAntiAlias = true

        for (i in 0 until mGridSizeX) {
            for (j in 0 until mGridSizeY) {
                val left = (i + 1) * mRectSize
                val top = (j + 1) * mRectSize
                val right = left + mRectSize
                val bottom = top + mRectSize
                canvas?.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), mStrokePaint)
                mGridPaint.color = mGridSquare[i][j].getColor()//更新格子的颜色
                canvas?.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), mGridPaint)
            }
        }
    }
}