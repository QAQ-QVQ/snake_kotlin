# 贪吃蛇

---

## 从零开始使用kotlin搭建安卓贪吃蛇游戏

---

~~（说的这么煞有介事其实就是个菜鸡尝试kotlin的项目）小声BB~~

*这里会写一些我在学习时的思路以及心得，话不多说，开写*

---

### 整体的搭建思路

- 游戏机制的确立
- 用面向对象的思维设计与实现
- 初步实现视图分离
- 未来打算加入的机制

---

### 游戏机制的确立

- 初步确立游戏的机制如下
  - 游戏开始
    - 出现蛇，长度默认为1，位置默认在屏幕中间，默认向右移动
    - 当蛇长度不为一时，不能朝相反的方向移动
      - 食物的位置不能出现在蛇身上
        - 碰到食物，蛇的长度加一
          - 出现下一个食物
            - 撞到墙或者蛇，游戏失败
            - 胜利条件待定

---

### 用面向对象的思维设计与实现

- 游戏的对象，区分对象的关键在于不同的颜色

  - 格子

    - 作为所有的基础格子的存在至关重要，他应该具有每一个种类以方便界面展示，我以颜色来辨别是何对象

    - 属性

      - 格子的种类（用来渲染画面）
    - x坐标，y坐标（用来确定位置）
      
  - 格子的尺寸
      
  - 方法
  
      - 获取格子的颜色
      
      - ```kotlin
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
        ```
      
  - 对象
  
      - ```kotlin
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
      ```
  
    - 其中GameConfig为
  
        - ```kotlin
          enum class GameConfig {
              LEFT,RIGHT,UP,DOWN,GRID,SNAKE,FOOD
          }
        ```
  
        其主要作用是用来区分状态
  
- 食物
  
  - 食物作为特殊的格子，其于格子的区别就在于格子的颜色不同
  
  - 属性
  
      - 格子
      
      - ```kotlin
        class Food {
            var gridSquare: GridSquare
            constructor(gridSquare: GridSquare) {
                gridSquare.Type = GameConfig.FOOD
                this.gridSquare = gridSquare
            }
        }
        ```
- 蛇
  
    - 属性
      - 蛇身体
      - 蛇方向


    - 方法
      - 蛇头
      - 蛇尾
    
    - 对象
    
      - ```kotlin
        class Snake {
            var SnakeBody: LinkedList<GridSquare> = LinkedList()
            var SnakeDirection: GameConfig
        
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
        }
        ```


​        

---

### 初步实现视图分离

- 我打算把视图和游戏的数据操作分离开

  - 视图

    - 属性

      - x轴格子的数目

      - y轴格子的数目

      - 绘制格子内容（其他平台以及语言可能会不同）

      - 绘制格子边缘（画笔工具）

    - 方法

      - 初始化

        - 这里重写view的生命周期以获取当前屏幕的宽高

        - ```kotlin
           override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
                  super.onSizeChanged(w, h, oldw, oldh)
               //x轴格子数目等于屏幕宽度除以格子尺寸减二，这个二表示左右各留出一个格子的距离
                  mGridSizeX = (w / mRectSize) - 2
               //y轴格子数目等于屏幕高度除以格子尺寸减二，这个二表示上下各留出一个格子的距离
                  mGridSizeY = (h / mRectSize) - 2
               //初始化操作
                  gameManager = GameManager(context, this, mGridSizeX, mGridSizeY)
               //初始化数据
                  gameManager.InitData()
              }
          ```

          

      - 刷新页面

        - ```kotlin
          override fun PostInvalidate(mGridSquare: ArrayList<ArrayList<GridSquare>>) {
                  this.mGridSquare = mGridSquare
              //这个方法会触发view的onDraw方法
                  postInvalidate()
              }
          ```

          

      - 显示提示弹框

        - ```kotlin
           override fun ShowMessageDialog(Msg: String, Title: String) {
                  post {
                      AlertDialog.Builder(context).setMessage(Msg).setTitle(Title)
                          .setCancelable(false)
                          .setPositiveButton("开始") { dialog, _ ->
                              dialog.dismiss()
            //开始游戏   
                                                    gameManager.StartGame()
                          }
                          .setNegativeButton("退出") { dialog, _ ->
                              dialog.dismiss()
             //退出游戏操作待定   
                                                    Toast.makeText(context, "菜逼啊！", Toast.LENGTH_SHORT).show()
                              System.exit(0)
                          }
                          .create()
                          .show()
                  }
              }
          ```

          

      - 识别手势

        - 重写view的onTouchEvent（这个方法会在手指解除屏幕后触发）

        - ```kotlin
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
          ```

          

      - 绘制页面

        - 这里重写view的onDraw方法（这个方法会在第一次创建view的时候触发）

        - ```kotlin
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
                          //这个参数为距离上下左右的距离
                          val left = (i + 1) * mRectSize
                          val top = (j + 1) * mRectSize
                          val right = left + mRectSize
                          val bottom = top + mRectSize
                          //这个参数负责绘制接收五个参数，分别是，左，上，右，下，画笔
                          canvas?.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), mStrokePaint)
                          //更新格子的颜色
                          mGridPaint.color = mGridSquare[i][j].getColor()
                          canvas?.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), mGridPaint)
                      }
                  }
              }
          }
          ```

          - 对象

            - ```kotlin
              class GameView : View, IGameView {
                  private var TAG: String = "GameView"
                  //x轴格子数目
                  private var mGridSizeX: Int = 0
                  //y轴格子数目
                  private var mGridSizeY: Int = 0
                  private var mRectSize: Int = GridSquare().GrldSize
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
                      mGridSizeX = (w / mRectSize) - 2
                      mGridSizeY = (h / mRectSize) - 2
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
              ```

              

  - 操作

    - 属性

      - 停顿时间
      - 地图
        - 我用一个二维数组来记录地图上的格子
      - 是否结束游戏
      - 蛇
      - 食物

    - 方法

      - 初始化

        - ```kotlin
          constructor(context: Context, iGameView: IGameView, mGridSizeX: Int, mGridSizeY: Int) {
                  Log.i(TAG, "init")
                  this.iGameView = iGameView
                  this.context = context
                  this.mGridSizeX = mGridSizeX
                  this.mGridSizeY = mGridSizeY
                  mIsEndGame = true
              }
          ```

      - 初始化数据

        - ```kotlin
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
          ```

      - 开始游戏

        - ```kotlin
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
              //用来测试用，增加初试蛇的长度
          //        snake.EatFood(Food(mGridSquare[mGridSizeX/2-1][mGridSizeY/2]))
                  generateFood()
                  iGameView.PostInvalidate(mGridSquare)//重绘界面
                  mIsEndGame = false
                  val thread = GameMainThread()
                  thread.start()
              }
          ```

          

      - 操作蛇

        - 蛇方向

          - 上

            - ```kotlin
                override fun MoveUp() {
                    /*
                    在蛇的方向为上时，不允许改变蛇的方向为下
                    换而言之就是不允许改变方向为相反的方向
                    例外就是，当蛇的长度为1时，这个时候允许改变方向为相反的方向
                    */
                      if (snake.SnakeDirection !== GameConfig.DOWN) {
                          snake.SnakeDirection = GameConfig.UP
                      } else {
                          if (snake.SnakeBody.size == 1) {
                              snake.SnakeDirection = GameConfig.UP
                          }
                      }
                  }
              ```

          - 下

            - ```kotlin
                 override fun MoveDown() {
                      if (snake.SnakeDirection !== GameConfig.UP) {
                          snake.SnakeDirection = GameConfig.DOWN
                      } else {
                          if (snake.SnakeBody.size == 1) {
                              snake.SnakeDirection = GameConfig.DOWN
                          }
                      }
                  }
              ```

          - 左

            - ```kotlin
              override fun MoveLeft() {
                      if (snake.SnakeDirection !== GameConfig.RIGHT) {
                          snake.SnakeDirection = GameConfig.LEFT
                      } else {
                          if (snake.SnakeBody.size == 1) {
                              snake.SnakeDirection = GameConfig.LEFT
                          }
                      }
                  }
              ```

          - 右

            - ```kotlin
               override fun MoveRight() {
                      if (snake.SnakeDirection !== GameConfig.LEFT) {
                          snake.SnakeDirection = GameConfig.RIGHT
                      } else {
                          if (snake.SnakeBody.size == 1) {
                              snake.SnakeDirection = GameConfig.RIGHT
                          }
                      }
                  }
              ```

        - 蛇的移动

          - ```kotlin
            private lateinit var snakeHead: GridSquare
                private fun moveSnake(snakeDirection: GameConfig) {
                    //重绘界面
                    iGameView.PostInvalidate(mGridSquare)
                    /*
            			这个地方可能有点绕
                        首先蛇移动有两种方案
                        1：每一个节点的坐标变为上一个节点的坐标整体移动
                        这个方案优点是好理解，缺点是时间复杂度太高是O(2^n),整体执行太耗时所以不推荐
                        2：移除尾部节点，添加头部节点
                        其实仔细想想就能明白，蛇的每次移动其实都只有头尾在动，头部向前移动一格，同时移除掉尾部
                        这个方案需要一点思考，优点是时间复杂度低是O(n),速度很快也可以节约资源
                    */
                    try {
                        //根据移动的方向不同蛇头的下一个位置也不同
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
            			//根据蛇头的类型判断蛇头下一个是什么
                        when (snakeHead.Type) {
                            //格子
                            GameConfig.GRID -> {
                                //蛇移动
                                snake.getSnakeTail().Type = GameConfig.GRID
                                snake.SnakeBody.remove(snake.getSnakeTail())
                                snakeHead.Type = GameConfig.SNAKE
                                snake.SnakeBody.add(0, snakeHead)
                            }
                            //食物
                            GameConfig.FOOD -> {
                                snakeHead.Type = GameConfig.SNAKE
                                snake.SnakeBody.add(0, snakeHead)
                                generateFood()
                            }
                            //蛇
                            GameConfig.SNAKE -> {
                                mIsEndGame = true
                                iGameView.ShowMessageDialog("啊！游戏结束了！想重来吗！！", "啊！想不到标题啊！")
                            }
                            else -> {
            
                            }
                        }
                        /*
                        这里有个思维方式，如果蛇头移动超出了数组的边界也就是抛出数组下标越界，代表着这个蛇已经撞到墙了
                        所以这里捕获到这个异常表示游戏结束
                       */
                    } catch (e: IndexOutOfBoundsException) {
                        mIsEndGame = true
                        iGameView.ShowMessageDialog("啊！游戏结束了！想重来吗！！", "啊！想不到标题啊！")
                    }
                }
            ```

            

      - 生成食物

        - ```kotlin
          private fun generateFood() {
              /*
              	在整个棋盘上随机一个xy坐标
              */
                  var foodX = Random().nextInt(mGridSizeX)
                  var foodY = Random().nextInt(mGridSizeY)
                  var i = 0
                  while (i < snake.SnakeBody.size) {
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
          ```

      - 实时刷新

        - 为了让游戏连续，我创建了一个线程

        - ```kotlin
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
          ```

      - 对象

        - ```kotlin
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
                  while (i < snake.SnakeBody.size) {
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
          ```

---

### 未来打算加入的机制

- 至此所有逻辑就梳理完毕了
- 目前只是最基本的游戏实现，未来可能会考虑加入新的机制，例如吃到多少速度增加，吃到有特殊效果的食物会有特殊效果，例如会无敌，加很多格子等等
- ~~至于什么时候加，就看我什么时候能想起来了~~
- 好了，十分感谢你观看这段代码，如果有什么意见或者建议欢迎提issue。