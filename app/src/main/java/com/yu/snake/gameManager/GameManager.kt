package com.yu.snake.gameManager

import android.content.Context
import android.util.Log
import com.yu.snake.gameModel.Food
import com.yu.snake.gameModel.GameModel
import com.yu.snake.gameModel.GridSquare
import com.yu.snake.gameModel.Snake
import com.yu.snake.gameView.IGameView
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
    lateinit var mGridSquare: ArrayList<ArrayList<GridSquare>>
    //速度
    var mSpeed: Long = 300
    //是否结束
    var mIsEndGame = false
    //蛇
    lateinit var snake: Snake
    //食物
    lateinit var food: Food

    var gameObject: GameObject = GameObject()
    var gameDirection: GameDirection = GameDirection()


    private lateinit var iGameView: IGameView
    private lateinit var gameModel: GameModel
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
//        gameModel = GameModel()
//        gameModel.SetIGameModelCallback {
//            //初始化成功
//            iGameView.InitSuccess()
//        }
//        gameModel.Init(ArrayList<ArrayList<GridSquare>>())
        mIsEndGame = true
    }

    //初始化数据
    override fun InitData() {
        mGridSquare = ArrayList<ArrayList<GridSquare>>()
        var squares: ArrayList<GridSquare>
        for (i in 0 until mGridSizeX) {
            squares = ArrayList<GridSquare>()
            for (j in 0 until mGridSizeY) {
                squares.add(GridSquare(context, i, j, gameObject.GRID))//设置每个格子的属性为空白
            }
            mGridSquare.add(squares)
        }
        iGameView.InitSuccess()
    }

    override fun StartGame() {
        if (!mIsEndGame) return
        //重置网格
        for (gridSquare in mGridSquare) {
            for (grid in gridSquare) {
                grid.Type = gameObject.GRID
            }
        }
        snake = Snake(mGridSquare[10][10], gameDirection.RIGHT)
//        gameManager.snake.EatFood(Food(gameManager.mGridSquare[9][10]))
//        gameManager.snake.EatFood(Food(gameManager.mGridSquare[8][10]))
//        gameManager.snake.EatFood(Food(gameManager.mGridSquare[7][10]))
//        gameManager.snake.EatFood(Food(gameManager.mGridSquare[6][10]))
        generateFood()
        iGameView.PostInvalidate()//重绘界面
        mIsEndGame = false
        val thread = GameMainThread()
        thread.start()
    }

    private inner class GameMainThread : Thread() {
        override fun run() {
            while (!mIsEndGame) {
                moveSnake(snake.SnakeDirection)
                checkCollision()
                iGameView.PostInvalidate()//重绘界面
                handleSpeed()
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

    lateinit var snakeHead: GridSquare
    private fun moveSnake(snakeDirection: Int) {
        when (snakeDirection) {
            gameDirection.LEFT -> if (snake.getSnakeHead().x <= 0) {
                mIsEndGame = true
                iGameView.ShowMessageDialog("啊！游戏结束了！想重来吗！！", "啊！想不到标题啊！")
            } else {
                MoveLeft()
            }
            gameDirection.UP -> if (snake.getSnakeHead().y <= 0) {
                mIsEndGame = true
                iGameView.ShowMessageDialog("啊！游戏结束了！想重来吗！！", "啊！想不到标题啊！")
            } else {
                MoveUp()
            }
            gameDirection.RIGHT -> if (snake.getSnakeHead().x + 1 >= mGridSizeX) {
                mIsEndGame = true
                iGameView.ShowMessageDialog("啊！游戏结束了！想重来吗！！", "啊！想不到标题啊！")
            } else {
                MoveRight()
            }
            gameDirection.DOWN -> if (snake.getSnakeHead().y + 1 >= mGridSizeY) {
                mIsEndGame = true
                iGameView.ShowMessageDialog("啊！游戏结束了！想重来吗！！", "啊！想不到标题啊！")
            } else {
                MoveDown()
            }
        }
    }

    private fun MoveLeft() {
        if (snake.getSnakeHead().x === food.gridSquare.x && snake.getSnakeHead().y === food.gridSquare.y) {
            snakeHead = mGridSquare[food.gridSquare.x - 1][food.gridSquare.y]
            generateFood()
        } else {
            snake.getSnakeTail().Type = gameObject.GRID
            snakeHead = mGridSquare[snake.getSnakeHead().x - 1][snake.getSnakeHead().y]
            snake.SnakeBody.remove(snake.getSnakeTail())
        }
        snakeHead.Type = gameObject.SNAKE
        snake.SnakeBody.add(0, snakeHead)
    }

    private fun MoveRight() {
        if ((snake.getSnakeHead().x === food.gridSquare.x) && (snake.getSnakeHead().y === food.gridSquare.y)) {
            snakeHead = mGridSquare[food.gridSquare.x + 1][food.gridSquare.y]
            generateFood()
        } else {
            snake.getSnakeTail().Type = gameObject.GRID
            snakeHead = mGridSquare[snake.getSnakeHead().x + 1][snake.getSnakeHead().y]
            snake.SnakeBody.remove(snake.getSnakeTail())
        }
        snakeHead.Type = gameObject.SNAKE
        snake.SnakeBody.add(0, snakeHead)
    }

    private fun MoveUp() {
        if (snake.getSnakeHead().x === food.gridSquare.x && snake.getSnakeHead().y === food.gridSquare.y) {
            snakeHead = mGridSquare[food.gridSquare.x][food.gridSquare.y - 1]
            generateFood()
        } else {
            snake.getSnakeTail().Type = gameObject.GRID
            snakeHead =
                mGridSquare[snake.getSnakeHead().x][snake.getSnakeHead().y - 1]
            snake.SnakeBody.remove(snake.getSnakeTail())
        }
        snakeHead.Type = gameObject.SNAKE
        snake.SnakeBody.add(0, snakeHead)
    }

    private fun MoveDown() {
        if (snake.getSnakeHead().x === food.gridSquare.x && snake.getSnakeHead().y === food.gridSquare.y) {
            snakeHead = mGridSquare[food.gridSquare.x][food.gridSquare.y + 1]
            generateFood()
        } else {
            snake.getSnakeTail().Type = gameObject.GRID
            snakeHead =
                mGridSquare[snake.getSnakeHead().x][snake.getSnakeHead().y + 1]
            snake.SnakeBody.remove(snake.getSnakeTail())
        }
        snakeHead.Type = gameObject.SNAKE
        snake.SnakeBody.add(0, snakeHead)
    }


    //检测碰撞
    private fun checkCollision() {
        //检测是否咬到自己
        for (i in 1 until snake.SnakeBody.size) {
            if (snake.getSnakeHead().x === snake.SnakeBody[i].x && snake.getSnakeHead().y === snake.SnakeBody[i].y) {
                //咬到自己 停止游戏
                mIsEndGame = true
                iGameView.ShowMessageDialog("啊！游戏结束了！想重来吗！！", "啊！想不到标题啊！")
                return
            }
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
    }

    inner class GameObject {
        val GRID: Int = 0
        val FOOD: Int = 1
        val SNAKE: Int = 2
    }

    inner class GameDirection {
        val LEFT: Int = 3
        val RIGHT: Int = 4
        val UP: Int = 5
        val DOWN: Int = 6
    }

}