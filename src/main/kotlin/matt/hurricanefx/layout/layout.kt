package matt.hurricanefx.layout

import javafx.event.EventTarget
import javafx.geometry.Bounds
import javafx.geometry.Rectangle2D
import javafx.scene.layout.Region
import javafx.stage.Stage
import matt.hurricanefx.tornadofx.layout.vbox


infix fun Region.minBind(other: Region) {
  minHeightProperty().bind(other.heightProperty())
  minWidthProperty().bind(other.widthProperty())
}

infix fun Region.minBind(other: Stage) {
  minHeightProperty().bind(other.heightProperty())
  minWidthProperty().bind(other.widthProperty())
}


infix fun Region.maxBind(other: Region) {
  maxHeightProperty().bind(other.heightProperty())
  maxWidthProperty().bind(other.widthProperty())
}

infix fun Region.maxBind(other: Stage) {
  maxHeightProperty().bind(other.heightProperty())
  maxWidthProperty().bind(other.widthProperty())
}


infix fun Region.perfectBind(other: Region) {
  this minBind other
  this maxBind other
}

infix fun Region.perfectBind(other: Stage) {
  this minBind other
  this maxBind other
}

fun EventTarget.spacer() {
  this.vbox {
	minWidth = 20.0
	minHeight = 20.0
  }
}

fun Bounds.toRect() = Rectangle2D(minX, minY, width, height)
fun Rectangle2D.shrink(n: Int) = Rectangle2D(minX + n, minY + n, width - (n*2), height - (n*2))