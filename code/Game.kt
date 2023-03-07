package se.umu.cs.dv21sln.ownapplication

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View

class Game: View{

    private var context: Context
    var spaceShip: SpaceShip

    var screenWidth = 0
    var screenHeight = 0


    constructor(context: Context): super(context) {
        this.context = context

        spaceShip = SpaceShip(context)
    }

    /*override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if(spaceShip.posX > screenWidth - spaceShip.spaceShip.width) {
            spaceShip.posX = screenWidth - spaceShip.spaceShip.width
        }

        else if(spaceShip.posX < 0) {
            spaceShip.posX = 0
        }

        draw(canvas)
        invalidate()

        //canvas?.drawBitmap(spaceShip.spaceShip, spaceShip.posX, spaceShip.posY, null)
    }*/
}

private fun Canvas?.drawBitmap(spaceShip: Bitmap, posX: Int, posY: Int, nothing: Nothing?) {

}
