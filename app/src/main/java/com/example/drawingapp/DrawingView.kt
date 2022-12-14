package com.example.drawingapp

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import androidx.core.content.res.ResourcesCompat

class DrawingView(context : Context, attrs:AttributeSet): View(context, attrs){

    private var myDrawPath: CustomPath? = null
    private var myCanvasBitmap: Bitmap? = null
    private var myDrawPaint: Paint? = null
    private var myCanvasPaint: Paint? = null

    /**
     * A variable for canvas which will be initialized later and used.
     *
     *The Canvas class holds the "draw" calls. To draw something, you need 4 basic components: A Bitmap to hold the pixels, a Canvas to host
     * the draw calls (writing into the bitmap), a drawing primitive (e.g. Rect,
     * Path, text, Bitmap), and a paint (to describe the colors and styles for the
     * drawing)
     */

    private var myCanvas: Canvas?= null
    private val myPath = ArrayList<CustomPath>()
    private var myBrushSize :Float = 0.toFloat()
    private var myColor = Color.BLACK

    private val myUndoPath = ArrayList<CustomPath>()


    init{
        drawingSetUp()
    }

    fun onClickUndo(){
        if (myPath.size>0){
            myUndoPath.add(myPath.removeAt(myPath.size-1))
            invalidate()  // internally calls the onDraw function
        }
    }

    fun onClickRedo(){
        if (myUndoPath.size > 0) {
            myPath.add(myUndoPath.removeAt(myUndoPath.size- 1))
            invalidate()
        }
    }

    private fun drawingSetUp(){
        myDrawPaint = Paint()
        myDrawPath = CustomPath(myColor,myBrushSize)
        myDrawPaint?.color= myColor
        myDrawPaint?.style = Paint.Style.STROKE
        myDrawPaint?.strokeJoin =Paint.Join.ROUND
        myDrawPaint?.strokeCap =Paint.Cap.ROUND
        myCanvasPaint= Paint(Paint.DITHER_FLAG)
       // myBrushSize=20.toFloat()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        myCanvasBitmap= Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        myCanvas = Canvas(myCanvasBitmap!!)
    }
//This method is called when a stroke is drawn on the canvas as a part of the painting.

    override fun onDraw(myCanvas: Canvas?) {
        super.onDraw(myCanvas)

        myCanvasBitmap?.let {
            myCanvas?.drawBitmap(it, 0f, 0f,myCanvasPaint)
        }

        for(path in myPath){// saves our drawn line in myPath
            myDrawPaint!!.strokeWidth = path.brushThickness
            myDrawPaint!!.color= path.color
            myCanvas?.drawPath(path, myDrawPaint!!)
        }

        if (!myDrawPath!!.isEmpty){
            myDrawPaint?.strokeWidth = myDrawPath!!.brushThickness
            myDrawPaint?.color= myDrawPath!!.color
            myCanvas?.drawPath(myDrawPath!!, myDrawPaint!!)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        val touchX = event?.x
        val touchY = event?.y

        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                myDrawPath!!.color = myColor
                myDrawPath!!.brushThickness = myBrushSize

                myDrawPath!!.reset()// Clear any lines and curves from the path, making it empty.

                if (touchX != null) {
                    if (touchY != null) {
                        myDrawPath!!.moveTo(touchX, touchX)// Set the beginning of the next contour to the point (x,y).
                    }
                }
            }
            MotionEvent.ACTION_MOVE -> {
                if (touchX != null) {
                    if (touchY != null) {
                        myDrawPath!!.lineTo(touchX, touchY)// Add a line from the last point to the specified point (x,y).
                    }
                }
            }
            MotionEvent.ACTION_UP->{
                myPath.add(myDrawPath!!) //Add when to stroke is drawn to canvas and added in the path arraylist
                myDrawPath = CustomPath(myColor, myBrushSize)
            }
            else -> return false

        }
        invalidate()
return true
    }

    internal inner class CustomPath(var color:Int, var brushThickness:Float): Path(){

    }

    fun setBrushSize(newSize:Float){
            myBrushSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                newSize, resources.displayMetrics)
        myDrawPaint!!.strokeWidth = myBrushSize
    }

    fun setColor(newColor: String){
        myColor = Color.parseColor(newColor)
        myDrawPaint!!.color= myColor

    }



}
