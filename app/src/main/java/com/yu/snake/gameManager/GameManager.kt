package com.yu.snake.gameManager

import android.content.Context
import android.util.Log
import com.yu.snake.GameConfig
import com.yu.snake.gameModel.Food
import com.yu.snake.gameModel.GridSquare
import com.yu.snake.gameModel.Snake
import com.yu.snake.gameView.IGameView
import java.lang.IndexOutOfBoundsException
import java.util.*
import kotlin.collections.ArrayList

/**
 *   CREATED BY DY ON 2020/6/10.
 *   TIME BY 15:09.
 *   @author DY
 **/
class GameManager : IGameManager {
    private var TAG: String = "GameManager"
    //地图的二维数组
    private lateinit var mGridSquare: ArrayList<ArrayList<GridSquare>>
    //速度
    private var mSpeed: Long = 300
    //是否结束
    private var mIsEndGame = false
    //蛇
    private lateinit var snake: Snake
    //食物
    private lateinit var food: Food

    private lateinit var iGameView: IGameView
    private lateinit var context: Context
    //x轴格子数目
    private var mGridSizeX: Int = 0
    //y轴格子数目
    private var mGridSizeY: Int = 0

    constructor()
    constructor(context: Context, iGameView: IGameView, mGridSizeX: Int, mGridSizeY: Int) {
        Log.i(TAG, "init")
        this.iGameView = iGameView
        this.context = context
        this.mGridSizeX = mGridSizeX
        this.mGridSizeY = mGridSizeY
        mIsEndGame = true
    }

    //初始化数据
    override fun InitData() {
        mGridSquare = ArrayList()
        var squares: ArrayList<GridSquare>
        for (i in 0 until mGridSizeX) {
            squares = ArrayList()
            for (j in 0 until mGridSizeY) {
                squares.add(GridSquare(context, i, j, GameConfig.GRID))//设置每个格子的属性为空白
            }
            mGridSquare.add(squares)
        }
        iGameView.InitSuccess(mGridSquare)
    }

    override fun StartGame() {
        if (!mIsEndGame) return
        mSpeed = 300
        //重置网格
        for (gridSquare in mGridSquare) {
            for (grid in gridSquare) {
                grid.Type = GameConfig.GRID
            }
        }
        snake = Snake(mGridSquare[mGridSizeX/2][mGridSizeY/2], GameConfig.RIGHT)
//        snake.EatFood(Food(mGridSquare[9][10]))
//        snake.EatFood(Food(mGridSquare[8][10]))
//        snake.EatFood(Food(mGridSquare[7][10]))
//        snake.EatFood(Food(mGridSquare[6][10]))
//        snake.EatFood(Food(mGridSquare[5][10]))
//        snake.EatFood(Food(mGridSquare[4][10]))
//        snake.EatFood(Food(mGridSquare[3][10]))
//        snake.EatFood(Food(mGridSquare[2][10]))
//        snake.EatFood(Food(mGridSquare[1][10]))
//        snake.EatFood(Food(mGridSquare[0][10]))
        generateFood()
        iGameView.PostInvalidate(mGridSquare)//重绘界面
        mIsEndGame = false
        val thread = GameMainThread()
        thread.start()
    }

    override fun MoveLeft() {
        if (snake.SnakeDirection !== GameConfig.RIGHT) {
            snake.SnakeDirection = GameConfig.LEFT
        } else {
            if (snake.SnakeBody.size == 1) {
                snake.SnakeDirection = GameConfig.LEFT
            }
        }
    }

    override fun MoveRight() {
        if (snake.SnakeDirection !== GameConfig.LEFT) {
            snake.SnakeDirection = GameConfig.RIGHT
        } else {
            if (snake.SnakeBody.size == 1) {
                snake.SnakeDirection = GameConfig.RIGHT
            }
        }

    }

    override fun MoveUp() {
        if (snake.SnakeDirection !== GameConfig.DOWN) {
            snake.SnakeDirection = GameConfig.UP
        } else {
            if (snake.SnakeBody.size == 1) {
                snake.SnakeDirection = GameConfig.UP
            }
        }
    }

    override fun MoveDown() {
        if (snake.SnakeDirection !== GameConfig.UP) {
            snake.SnakeDirection = GameConfig.DOWN
        } else {
            if (snake.SnakeBody.size == 1) {
                snake.SnakeDirection = GameConfig.DOWN
            }
        }
    }

    private inner class GameMainThread : Thread() {
        override fun run() {
            while (!mIsEndGame) {
                handleSpeed()
                moveSnake(snake.SnakeDirection)
            }
        }

        private fun handleSpeed() {
            try {
                sleep(mSpeed)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }

    private lateinit var snakeHead: GridSquare
    private fun moveSnake(snakeDirection: GameConfig) {
        iGameView.PostInvalidate(mGridSquare)//重绘界面
        try {
            when (snakeDirection) {
                GameConfig.LEFT -> {
                    snakeHead = mGridSquare[snake.getSnakeHead().x - 1][snake.getSnakeHead().y]
                }
                GameConfig.UP -> {
                    snakeHead = mGridSquare[snake.getSnakeHead().x][snake.getSnakeHead().y - 1]
                }
                GameConfig.RIGHT -> {
                    snakeHead = mGridSquare[snake.getSnakeHead().x + 1][snake.getSnakeHead().y]
                }
                GameConfig.DOWN -> {
                    snakeHead = mGridSquare[snake.getSnakeHead().x][snake.getSnakeHead().y + 1]
                }
                else -> {

                }
            }

            when (snakeHead.Type) {
                GameConfig.GRID -> {
                    //蛇移动
                    snake.getSnakeTail().Type = GameConfig.GRID
                    snake.SnakeBody.remove(snake.getSnakeTail())
                    snakeHead.Type = GameConfig.SNAKE
                    snake.SnakeBody.add(0, snakeHead)
                }
                GameConfig.FOOD -> {
                    snakeHead.Type = GameConfig.SNAKE
                    snake.SnakeBody.add(0, snakeHead)
                    generateFood()
                }
                GameConfig.SNAKE -> {
                    mIsEndGame = true
                    iGameView.ShowMessageDialog("啊！游戏结束了！想重来吗！！", "啊！想不到标题啊！")
                }
                else -> {

                }
            }
        } catch (e: IndexOutOfBoundsException) {
            mIsEndGame = true
            iGameView.ShowMessageDialog("啊！游戏结束了！想重来吗！！", "啊！想不到标题啊！")
        }
    }

    /**
     * 生成food
     */
    private fun generateFood() {
        var foodX = Random().nextInt(mGridSizeX)
        var foodY = Random().nextInt(mGridSizeY)
        var i = 0
        while (i < snake.SnakeBody.size - 1) {
            if (foodX == snake.SnakeBody[i].x && foodY == snake.SnakeBody[i].y) {
                //不能生成在蛇身上
                foodX = Random().nextInt(mGridSizeX)
                foodY = Random().nextInt(mGridSizeY)
                //重新循环
                i = 0
            }
            i++
        }
        food = Food(mGridSquare[foodX][foodY])
//        mSpeed -= 10
        Log.i("speed", "$mSpeed")
    }

}