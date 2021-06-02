package matt.hurricanefx


import javafx.beans.property.BooleanProperty
import javafx.beans.property.DoubleProperty
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.embed.swing.SwingFXUtils
import javafx.scene.Node
import javafx.scene.Parent
import javafx.scene.SnapshotParameters
import javafx.scene.control.Button
import javafx.scene.control.ScrollPane
import javafx.scene.control.Tab
import javafx.scene.image.ImageView
import javafx.scene.input.DataFormat
import javafx.scene.input.MouseEvent
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
import matt.hurricanefx.eye.lib.onChangeUntilAfterFirst
import matt.hurricanefx.tornadofx.async.runLater
import matt.hurricanefx.tornadofx.clip.put
import matt.hurricanefx.tornadofx.clip.putFiles
import matt.kjlib.cache.LRUCache
import matt.kjlib.commons.TEMP_DIR
import matt.kjlib.file.get
import matt.kjlib.log.NEVER
import matt.kjlib.recurse.chain
import matt.klib.dmap.withStoringDefault
import java.awt.image.BufferedImage
import java.io.File
import java.lang.ref.WeakReference
import java.util.WeakHashMap
import javax.imageio.ImageIO
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
  //  println("${this} minYRelative to ${ancestor}")
  var p: Parent? = parent
  var y = boundsInParent.minY
  //  tab("y = ${y}")
  var r: Double? = null
  while (true) {
	when (p) {
	  null     -> {
		r = null
		//		tab("r=null")
		break
	  }
	  ancestor -> {
		r = y
		//		tab("r=${y}")
		break
	  }
	  else     -> {
		y += p.boundsInParent.minY
		//		tab("p.boundsInParent.minY=${p.boundsInParent.minY}")
		//		tab("y=${y}")
		p = p.parent
	  }
	}
  }
  return r
}

fun Node.maxYRelativeTo(ancestor: Node): Double? {
  return minYRelativeTo(ancestor)?.plus(boundsInParent.height)
}

interface Scrolls {
  val scrollPane: ScrollPane
}

fun Scrolls.scrollToMinYOf(node: Node) {
  scrollPane.scrollToMinYOf(node)
}

fun ScrollPane.scrollToMinYOf(node: Node): Boolean {
  /*scrolling values range from 0 to 1*/
  node.minYRelativeTo(content)?.let {
	vvalue = (it/content.boundsInLocal.height)*1.1 /*IDK why, but y is always coming up a bit short, but this fixes it*/
	return true
  }
  return false
}

val ScrollPane.vValueConverted
  get() = vvalue*((content.boundsInParent.height - viewportBounds.height).takeIf { it > 0 } ?: 0.0)

val ScrollPane.vValueConvertedMax get() = vValueConverted + viewportBounds.height

fun Node.isFullyVisibleIn(sp: ScrollPane): Boolean {
  require(sp.vmin == 0.0)
  require(sp.vmax == 1.0)
  if (this.parent.chain { it.parent }.none { it == sp }) return false
  if (!this.isVisible) return false
  if (!this.isManaged) return false
  val minY = this.minYRelativeTo(sp.content)
  val maxY = this.maxYRelativeTo(sp.content)
  // /* println("vValueConverted=${sp.vValueConverted},vValueConvertedMax=${sp.vValueConvertedMax},minY=${minY},maxY=${maxY}")*/ /*,boundsInParent.height=${boundsInParent.height},boundsInLocal.height=${boundsInLocal.height},boundsInScene.height=${boundsInScene.height}*/
  require(minY != null && maxY != null)
  return minY >= sp.vValueConverted && maxY <= sp.vValueConvertedMax
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
	db.dragView = this.snapshot(SnapshotParameters(), null)
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


fun Node.dragsSnapshot() {
  addEventFilter(MouseEvent.DRAG_DETECTED) {
	println("drag detected")
	val params = SnapshotParameters()
	params.fill = Color.BLACK
	val snapshot = snapshot(params, null)
	val img = SwingFXUtils.fromFXImage(snapshot, null)
	val imgFile = TEMP_DIR["drag_image.png"]
	ImageIO.write(img, "png", imgFile)
	val db = startDragAndDrop(*TransferMode.ANY)
	db.put(DataFormat.FILES, mutableListOf(imgFile))
	it.consume()
	println("drag consumed")
  }
}

fun lazyTab(name: String, nodeOp: ()->Node) = Tab(name).apply {
  if (isSelected) {
	content = nodeOp()
  } else {
	selectedProperty().onChangeUntilAfterFirst(true) {
	  if (it == true) {
		content = nodeOp()
	  }
	}
  }
}