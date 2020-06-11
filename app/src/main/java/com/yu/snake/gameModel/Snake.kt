package com.yu.snake.gameModel

import com.yu.snake.gameManager.GameManager

/**
 *   CREATED BY DY ON 2020/6/10.
 *   TIME BY 17:45.
 *   @author DY
 **/
class Snake {
    var SnakeBody: ArrayList<GridSquare> = ArrayList<GridSquare>()
    var SnakeDirection: Int = 0

    constructor(SnakeHead: GridSquare, SnakeDirection: Int) {
        SnakeHead.Type = GameManager().gameObject.SNAKE
        SnakeBody.add(SnakeHead)
        this.SnakeDirection = SnakeDirection
    }

    //蛇头
    fun getSnakeHead(): GridSquare {
        return SnakeBody[0]
    }

    //蛇尾
    fun getSnakeTail(): GridSquare {
        return SnakeBody[SnakeBody.size - 1]
    }

    fun EatFood(food: Food) {
        food.gridSquare.Type = GameManager().gameObject.SNAKE
        SnakeBody.add(food.gridSquare)
    }

}