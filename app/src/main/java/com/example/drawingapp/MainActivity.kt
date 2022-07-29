package com.example.drawingapp

import android.app.Dialog
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.view.get
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.brusg_size_dialog.*

class MainActivity : AppCompatActivity() {

    private var drawingView: DrawingView?= null
    private var myCurrentPaint : ImageButton?= null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawingView =  findViewById(R.id.drawing_view)

        brush_dialog_button.setOnClickListener{
            showBrushSizeDialog()
        }
        //Color selection starts here
        val linearPaintColor = findViewById<LinearLayout>(R.id.Color_palete_layout)
        myCurrentPaint = linearPaintColor[1] as ImageButton
        myCurrentPaint!!.setImageDrawable(
            ContextCompat.getDrawable(this, R.drawable.color_palete_pressed)
        )
    }

    fun paintClicked(view: View){
        if(view != myCurrentPaint){
            val imageBtn = view as ImageButton
            val colorTag = imageBtn.tag.toString()
            drawingView?.setColor(colorTag)

            imageBtn.setImageDrawable(
                ContextCompat.getDrawable(this, R.drawable.color_palete_pressed)
            )
            myCurrentPaint?.setImageDrawable(
                ContextCompat.getDrawable(this, R.drawable.color_palete)
            )
            myCurrentPaint=view
        }
    }

    private  fun showBrushSizeDialog(){
        val brushSizeDialog = Dialog(this)
        brushSizeDialog.setContentView(R.layout.brusg_size_dialog)
        brushSizeDialog.setTitle("Brush Size: ")
        val smallBtn = brushSizeDialog.brush_size_small
        smallBtn.setOnClickListener{
            drawingView?.setBrushSize(10f)
            brushSizeDialog.dismiss()
        }
        val mediumBtn = brushSizeDialog.brush_size_medium
        mediumBtn.setOnClickListener{
            drawingView?.setBrushSize(12f)
            brushSizeDialog.dismiss()
        }
        val largeBtn = brushSizeDialog.brush_size_large
        largeBtn.setOnClickListener{
            drawingView?.setBrushSize(18f)
            brushSizeDialog.dismiss()
        }
        brushSizeDialog.show()
    }

}