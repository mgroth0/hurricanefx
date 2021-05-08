package matt.hurricanefx.tornadofx.style

/*slightly modified code I stole from tornadofx*/

import javafx.geometry.Insets

fun insets(all: Number) = Insets(all.toDouble(), all.toDouble(), all.toDouble(), all.toDouble())
fun insets(horizontal: Number? = null, vertical: Number? = null) = Insets(
  vertical?.toDouble() ?: 0.0,
  horizontal?.toDouble() ?: 0.0,
  vertical?.toDouble() ?: 0.0,
  horizontal?.toDouble() ?: 0.0
)

fun insets(
  top: Number? = null,
  right: Number? = null,
  bottom: Number? = null,
  left: Number? = null
) = Insets(
  top?.toDouble() ?: 0.0,
  right?.toDouble() ?: 0.0,
  bottom?.toDouble() ?: 0.0,
  left?.toDouble() ?: 0.0
)

fun Insets.copy(
  top: Number? = null,
  right: Number? = null,
  bottom: Number? = null,
  left: Number? = null
) = Insets(
  top?.toDouble() ?: this.top,
  right?.toDouble() ?: this.right,
  bottom?.toDouble() ?: this.bottom,
  left?.toDouble() ?: this.left
)


fun Insets.copy(
  horizontal: Number? = null,
  vertical: Number? = null
) = Insets(
  vertical?.toDouble() ?: this.top,
  horizontal?.toDouble() ?: this.right,
  vertical?.toDouble() ?: this.bottom,
  horizontal?.toDouble() ?: this.left
)

val Insets.horizontal get() = (left + right)/2
val Insets.vertical get() = (top + bottom)/2
val Insets.all get() = (left + right + top + bottom)/4