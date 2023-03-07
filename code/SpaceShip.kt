package se.umu.cs.dv21sln.ownapplication

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.view.View
import androidx.core.graphics.get

/**
 *
 */

class SpaceShip {

    var context: Context
    var spaceShip: Bitmap
    var posX: Float = 0.0f
    var posY: Float = 0.0f

    /**
     *
     */
    constructor(context: Context) {
        this.context = context
        spaceShip = BitmapFactory.decodeResource(context.resources, R.drawable.spaceship)
    }

}