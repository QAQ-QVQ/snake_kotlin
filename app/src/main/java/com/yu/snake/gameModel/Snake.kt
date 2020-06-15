package com.yu.snake.gameModel

import com.yu.snake.GameConfig
import java.util.*

/**
 *   CREATED BY DY ON 2020/6/10.
 *   TIME BY 17:45.
 *   @author DY
 **/
class Snake {
    var SnakeBody: LinkedList<GridSquare> = LinkedList()
   lateinit var SnakeDirection: GameConfig

    constructor(SnakeHead: GridSquare, SnakeDirection: GameConfig) {
        SnakeHead.Type = GameConfig.SNAKE
        SnakeBody.add(SnakeHead)
        this.SnakeDirection = SnakeDirection
    }

    //蛇头
    fun getSnakeHead(): GridSquare {
        return SnakeBody.first
    }

    //蛇尾
    fun getSnakeTail(): GridSquare {
        return SnakeBody.last
    }

    fun EatFood(food: Food) {
        food.gridSquare.Type = GameConfig.SNAKE
        SnakeBody.add(food.gridSquare)
    }

    fun move(SnakeDirection: GameConfig) {
        when (SnakeDirection) {
            GameConfig.LEFT -> "SS"
            GameConfig.RIGHT -> "ss"
//                    snake.getSnakeTail().Type = gameObject.GRID
//                    snakeHead = mGridSquare[snake.getSnakeHead().x + 1][snake.getSnakeHead().y]
//                    snake.SnakeBody.remove(snake.getSnakeTail())
//                snakeHead.Type = gameObject.SNAKE
//                snake.SnakeBody.add(0, snakeHead)
//
//            }
            GameConfig.UP -> "qq"
            GameConfig.DOWN -> "ww"
        }
    }
}