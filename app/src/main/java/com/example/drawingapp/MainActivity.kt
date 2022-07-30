package com.example.drawingapp

import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.get
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.brusg_size_dialog.*

class MainActivity : AppCompatActivity() {

    private var drawingView: DrawingView?= null
    private var myCurrentPaint : ImageButton?= null

    val openGallery : ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
        result ->
        if (result.resultCode == RESULT_OK && result.data != null){
            val imageBg : ImageView = findViewById(R.id.iv_background)

            imageBg.setImageURI(result.data?.data)
        }
    }

    private val galleryLauncher : ActivityResultLauncher<Array<String>> = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()){
                permissions ->
            permissions.entries.forEach{
                val permissionName = it.key
                val isGranted = it.value
                if (isGranted){
                   Toast.makeText(this, "permission Granted", Toast.LENGTH_LONG).show()

                    val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    openGallery.launch(intent)

                }else{
                    Toast.makeText(this, "permission Not Granted", Toast.LENGTH_LONG).show()
                }
            }
        }


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

        galleryButton.setOnClickListener{
            requestStoragePermission()
        }

        //undo btn
        undoButton.setOnClickListener{
                drawingView?.onClickUndo()
        }
        //redo Btn
        redoButton.setOnClickListener{
            drawingView?.onClickRedo()
        }


    }

    private fun requestStoragePermission() {
    if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)){
        showRationalDialog("Drawing App", "Drawing App"+" needs to access your external storage to import images from gallery ")

    }
    else{
        galleryLauncher.launch(arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE

        ))
    }
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

    private fun showRationalDialog(Title:String, message: String){
        val builder : AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(Title)
            .setMessage(message)
            .setPositiveButton("Cancel"){
                dialog, _->
                dialog.dismiss()
            }
        builder.create().show()
    }
}