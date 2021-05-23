package matt.hurricanefx


import javafx.beans.property.BooleanProperty
import javafx.beans.property.DoubleProperty
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.scene.Node
import javafx.scene.Parent
import javafx.scene.control.Button
import javafx.scene.control.ScrollPane
import javafx.scene.image.ImageView
import javafx.scene.input.TransferMode
import javafx.scene.layout.ColumnConstraints
import javafx.scene.layout.Region
import javafx.scene.layout.RowConstraints
import javafx.scene.paint.Color
import javafx.scene.web.WebView
import javafx.stage.Stage
import matt.hurricanefx.eye.lang.BProp
import matt.hurricanefx.eye.lang.DProp
import matt.hurricanefx.eye.lib.ChangeListener
import matt.hurricanefx.tornadofx.async.runLater
import matt.hurricanefx.tornadofx.clip.putFiles
import matt.kjlib.cache.LRUCache
import matt.kjlib.log.NEVER
import matt.klib.dmap.withStoringDefault
import java.awt.image.BufferedImage
import java.io.File
import java.lang.ref.WeakReference
import java.util.WeakHashMap
import javax.swing.JFileChooser



inline fun <T: Any, V> inRunLater(crossinline op: T.(V)->Unit): T.(V)->Unit {
  return {
	runLater {
	  op(it)
	}
  }
}

val Node.boundsInScene
  get() = localToScene(boundsInLocal)

val Node.boundsInScreen
  get() = localToScreen(boundsInLocal)

fun Node.minYRelativeTo(ancestor: Node): Double? {
  var p: Parent? = parent
  var y = boundsInParent.minY
  while (true) {
	when (p) {
	  null     -> return null
	  ancestor -> return y
	  else     -> {
		y += p.boundsInParent.minY  //NOTE: maxY may not be this simple
		p = p.parent
	  }
	}
  }
}

interface Scrolls {
  val scrollPane: ScrollPane
}

fun Scrolls.scrollToMinYOf(node: Node) {
  scrollPane.scrollToMinYOf(node)
}

fun ScrollPane.scrollToMinYOf(node: Node): Boolean {
  val height: Double = content.boundsInLocal.height
  //  val y = node.boundsInParent.maxY
  val y = node.minYRelativeTo(content)

  // scrolling values range from 0 to 1
  y?.let {
	vvalue = (it/height)*1.1 // NOTE: IDK why, but y is always coming up a bit short, but this fixes it
	return true
  }
  return false

  //  hvalue = x/width

  // just for usability
  //  node.requestFocus()
}


fun Region.exactWidthProperty() = SimpleDoubleProperty().also {
  minWidthProperty().bind(it)
  maxWidthProperty().bind(it)
}

fun Region.exactHeightProperty() = SimpleDoubleProperty().also {
  minHeightProperty().bind(it)
  maxHeightProperty().bind(it)
}

fun ColumnConstraints.exactWidthProperty() = SimpleDoubleProperty().also {
  minWidthProperty().bind(it)
  maxWidthProperty().bind(it)
}

fun RowConstraints.exactHeightProperty() = SimpleDoubleProperty().also {
  minHeightProperty().bind(it)
  maxHeightProperty().bind(it)
}

fun WebView.exactWidthProperty() = SimpleDoubleProperty().also {
  minWidthProperty().bind(it)
  maxWidthProperty().bind(it)
}

fun WebView.exactHeightProperty() = SimpleDoubleProperty().also {
  minHeightProperty().bind(it)
  maxHeightProperty().bind(it)
}

var Region.exactWidth: Number
  set(value) {
	exactWidthProperty().bind(DProp(value.toDouble()))
  }
  get() = NEVER
var Region.exactHeight: Number
  set(value) {
	exactHeightProperty().bind(DProp(value.toDouble()))
  }
  get() = NEVER
var WebView.exactWidth: Number
  set(value) {
	exactWidthProperty().bind(DProp(value.toDouble()))
  }
  get() = NEVER
var WebView.exactHeight: Number
  set(value) {
	exactHeightProperty().bind(DProp(value.toDouble()))
  }
  get() = NEVER
var ColumnConstraints.exactWidth: Number
  set(value) {
	exactWidthProperty().bind(DProp(value.toDouble()))
  }
  get() = NEVER
var RowConstraints.exactHeight: Number
  set(value) {
	exactHeightProperty().bind(DProp(value.toDouble()))
  }
  get() = NEVER


val fitBothProps = WeakHashMap<ImageView, DoubleProperty>().withStoringDefault {
  SimpleDoubleProperty().apply {
	it.fitWidthProperty().bind(this)
	it.fitWidthProperty().bind(this)
  }
}

fun ImageView.fitBothProp(): DoubleProperty = fitBothProps[this]
fun ImageView.bindFitTo(r: Region) {
  fitWidthProperty().bind(r.widthProperty())
  fitHeightProperty().bind(r.heightProperty())
}

fun Node.visibleAndManagedProp(): BooleanProperty {
  val r = BProp()
  visibleProperty().bind(r)
  managedProperty().bind(r)
  return r
}

var Node.visibleAndManaged: Boolean
  get() = isVisible && isManaged
  set(value) {
	isVisible = value
	isManaged = value
  }


fun BooleanProperty.toggle() {
  value = !value
}

var Button.op: ()->Unit
  set(value) {
	setOnAction {
	  value()
	}
  }
  get() = NEVER

val Node.stage get() = (scene.window as? Stage)

fun <T> ObservableValue<T>.onChangeWithWeak(
  o: Any,
  op: (T?)->Unit
) = apply {

  var listener: ChangeListener<T>? = null
  val weakRef = WeakReference(o)
  listener = ChangeListener { _, _, new ->
	if (weakRef.get() == null) {
	  removeListener(listener!!)
	}
	op(new)
  }
  addListener(listener)
}



fun intColorToFXColor(i: Int): Color {
  return Color.rgb(i shr 16 and 0xFF, i shr 8 and 0xFF, i and 0xFF)
}

fun Node.drags(file: File) {
  setOnDragDetected {
	val db = startDragAndDrop(*TransferMode.ANY)
	db.putFiles(mutableListOf(file))
	it.consume()
  }
}

val fileIcons = LRUCache<File, BufferedImage>(50).withStoringDefault {
  val icon = JFileChooser().let { it.ui.getFileView(it) }.getIcon(it)
  val bufferedImage = BufferedImage(
	icon.iconWidth,
	icon.iconHeight,
	BufferedImage.TYPE_INT_ARGB
  )
  icon.paintIcon(null, bufferedImage.graphics, 0, 0)
  bufferedImage
}

