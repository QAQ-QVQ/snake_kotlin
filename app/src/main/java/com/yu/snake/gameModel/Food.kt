package com.yu.snake.gameModel

import com.yu.snake.gameManager.GameManager

/**
 *   CREATED BY DY ON 2020/6/10.
 *   TIME BY 17:51.
 *   @author DY
 **/
class Food {
    var gridSquare: GridSquare

    constructor(gridSquare: GridSquare) {
        gridSquare.Type = GameManager().GameObject().FOOD
        this.gridSquare = gridSquare
    }
}