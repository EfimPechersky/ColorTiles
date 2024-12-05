package com.example.colortiles

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.DialogFragment

class StartGameDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction.
            val builder = AlertDialog.Builder(it)
            builder.setMessage("You Win!")
                .setPositiveButton("Ok") { dialog, id ->
                }
            // Create the AlertDialog object and return it.
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}

data class Coord(val x: Int, val y: Int)
class MainActivity : AppCompatActivity() {
    lateinit var tiles: Array<Array<View>>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        tiles = arrayOf(
            arrayOf(findViewById(R.id.t00),findViewById(R.id.t01),findViewById(R.id.t02),findViewById(R.id.t03) ),
            arrayOf(findViewById(R.id.t10),findViewById(R.id.t11),findViewById(R.id.t12),findViewById(R.id.t13)),
            arrayOf(findViewById(R.id.t20),findViewById(R.id.t21),findViewById(R.id.t22),findViewById(R.id.t23)),
            arrayOf(findViewById(R.id.t30),findViewById(R.id.t31),findViewById(R.id.t32),findViewById(R.id.t33))
        )
        initField()
    }
    fun getCoordFromString(s: String): Coord {
        val x=s[0].toString().toInt()
        val y=s[1].toString().toInt()
        return Coord(x,y) // вернуть полученные координаты
    }
    fun changeColor(view: View) {
        val brightColor = resources.getColor(R.color.bright)
        val darkColor = resources.getColor(R.color.dark)
        val drawable = view.background as ColorDrawable
        if (drawable.color ==brightColor ) {
            view.setBackgroundColor(darkColor)
        } else {
            view.setBackgroundColor(brightColor)
        }
    }
    fun onClick(v: View) {
        val coord = getCoordFromString(v.getTag().toString())
        changeColor(v)

        for (i in 0..3) {
            changeColor(tiles[coord.x][i])
            changeColor(tiles[i][coord.y])
        }

        if (checkVictory()){
            StartGameDialogFragment().show(supportFragmentManager, "GAME_DIALOG")

        }
    }

    fun checkVictory():Boolean {
        val allcolor = tiles[0][0].background  as ColorDrawable
        var tilecolor = tiles[0][0].background  as ColorDrawable;
        for (row in tiles){
            for (tile in row){
                tilecolor = tile.background  as ColorDrawable
                if (tilecolor.color!=allcolor.color){
                    return false
                }
            }
        }
        return true
    }

    fun initField() {
        var rand=0;
        for (row in tiles){
            for (tile in row){
                rand=(0..1).random();
                if (rand==0){
                    tile.setBackgroundColor(resources.getColor(R.color.bright))
                }else{
                    tile.setBackgroundColor(resources.getColor(R.color.dark))
                }
            }
        }
    }
}