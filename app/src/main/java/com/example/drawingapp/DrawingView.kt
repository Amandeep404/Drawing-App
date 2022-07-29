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
    private var myCanvas: Canvas?= null
    private val myPath = ArrayList<CustomPath>()
    private var myBrushSize :Float = 0.toFloat()
    private var myColor = Color.BLACK

    init{
        drawingSetUp()
    }

    private fun drawingSetUp(){
        myDrawPaint = Paint()
        myDrawPath = CustomPath(myColor,myBrushSize)
        myDrawPaint!!.color= myColor
        myDrawPaint!!.style = Paint.Style.STROKE
        myDrawPaint!!.strokeJoin =Paint.Join.ROUND
        myDrawPaint!!.strokeCap =Paint.Cap.ROUND
        myCanvasPaint= Paint(Paint.DITHER_FLAG)
       // myBrushSize=20.toFloat()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        myCanvasBitmap= Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        myCanvas = Canvas(myCanvasBitmap!!)
    }

    override fun onDraw(myCanvas: Canvas?) {
        super.onDraw(myCanvas)
        myCanvas!!.drawBitmap(myCanvasBitmap!!, 0F,0F , myCanvasPaint)

        for(path in myPath){// saves our drawn line in myPath
            myDrawPaint!!.strokeWidth = path.brushThickness
            myDrawPaint!!.color= path.color
            myCanvas.drawPath(path, myDrawPaint!!)
        }

        if (!myDrawPath!!.isEmpty){
            myDrawPaint!!.strokeWidth = myDrawPath!!.brushThickness
            myDrawPaint!!.color= myDrawPath!!.color
            myCanvas.drawPath(myDrawPath!!, myDrawPaint!!)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {

        val touchX = event?.x
        val touchY = event?.y

        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                myDrawPath!!.color = myColor
                myDrawPath!!.brushThickness = myBrushSize

                myDrawPath!!.reset()

                if (touchX != null) {
                    if (touchY != null) {
                        myDrawPath!!.moveTo(touchX, touchX)
                    }
                }
            }
            MotionEvent.ACTION_MOVE -> {
                if (touchX != null) {
                    if (touchY != null) {
                        myDrawPath!!.lineTo(touchX, touchY)
                    }
                }
            }
            MotionEvent.ACTION_UP->{
                myPath.add(myDrawPath!!)// stores our drawn line
                myDrawPath = CustomPath(myColor, myBrushSize)
            }
            else->return false

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
