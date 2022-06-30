@file:Suppress("unused")

package matt.hurricanefx


//import javafx.embed.swing.SwingFXUtils

import com.sun.javafx.application.PlatformImpl.runLater
import javafx.application.Application
import javafx.beans.property.BooleanProperty
import javafx.beans.property.DoubleProperty
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.event.Event
import javafx.event.EventHandler
import javafx.event.EventType
import javafx.geometry.Bounds
import javafx.scene.Cursor
import javafx.scene.Node
import javafx.scene.Parent
import javafx.scene.chart.NumberAxis
import javafx.scene.control.Button
import javafx.scene.control.CheckBox
import javafx.scene.control.ScrollPane
import javafx.scene.control.Tab
import javafx.scene.control.TreeTableView
import javafx.scene.image.ImageView
import javafx.scene.input.ContextMenuEvent
import javafx.scene.input.MouseEvent
import javafx.scene.layout.ColumnConstraints
import javafx.scene.layout.Pane
import javafx.scene.layout.Region
import javafx.scene.layout.RowConstraints
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import javafx.stage.FileChooser
import javafx.stage.Stage
import matt.caching.cache.LRUCache
import matt.file.MFile
import matt.file.toMFile
import matt.fx.image.jswingIconToImage
import matt.fx.image.toBufferedImage
import matt.hurricanefx.Corner.NE
import matt.hurricanefx.Corner.NW
import matt.hurricanefx.Corner.SE
import matt.hurricanefx.Corner.SW
import matt.hurricanefx.eye.lang.BProp
import matt.hurricanefx.eye.lang.DProp
import matt.hurricanefx.eye.lib.ChangeListener
import matt.hurricanefx.eye.lib.onChangeUntilAfterFirst
import matt.hurricanefx.tornadofx.nodes.add
import matt.hurricanefx.tsprogressbar.ThreadSafeNodeWrapper
import matt.hurricanefx.wrapper.NodeWrapper
import matt.klib.commons.thisMachine
import matt.klib.dmap.withStoringDefault
import matt.klib.lang.NEVER
import matt.klib.sys.WINDOWS
import matt.stream.recurse.chain
import java.awt.image.BufferedImage
import java.lang.ref.WeakReference
import java.util.WeakHashMap
import javax.swing.JFileChooser
import javax.swing.filechooser.FileSystemView

fun Node.saveChoose(
  initialDir: MFile, title: String
): MFile? {
  return FileChooser().apply {
	initialDirectory = initialDir
	this.title = title
  }.showSaveDialog(stage).toMFile()
}


inline fun <T: Any, V> inRunLater(crossinline op: T.(V)->Unit): T.(V)->Unit {
  return {
	runLater {
	  op(it)
	}
  }
}

val Node.boundsInScene: Bounds
  get() = localToScene(boundsInLocal)

val Node.boundsInScreen: Bounds
  get() = localToScreen(boundsInLocal)

fun Node.minYRelativeTo(ancestor: Node): Double? { //  println("${this} minYRelative to ${ancestor}")
  var p: Parent? = parent
  var y = boundsInParent.minY //  tab("y = ${y}")
  while (true) {
	when (p) {
	  null     -> {
		return null
	  }

	  ancestor -> {
		return y
	  }

	  else     -> {
		y += p.boundsInParent.minY
		p = p.parent
	  }
	}
  }
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

fun ScrollPane.scrollToMinYOf(node: Node): Boolean {/*scrolling values range from 0 to 1*/
  node.minYRelativeTo(content)?.let {
	vvalue =
	  (it/content.boundsInLocal.height)*1.1 /*IDK why, but y is always coming up a bit short, but this fixes it*/
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
  val maxY =
	this.maxYRelativeTo(
	  sp.content
	) // /* println("vValueConverted=${sp.vValueConverted},vValueConvertedMax=${sp.vValueConvertedMax},minY=${minY},maxY=${maxY}")*/ /*,boundsInParent.height=${boundsInParent.height},boundsInLocal.height=${boundsInLocal.height},boundsInScene.height=${boundsInScene.height}*/
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
  val r = BProp(isVisible && isManaged)
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

fun ThreadSafeNodeWrapper<*>.visibleAndManagedProp(): BooleanProperty {
  val r = BProp(node.isVisible && node.isManaged)
  node.visibleProperty().bind(r)
  node.managedProperty().bind(r)
  return r
}

var ThreadSafeNodeWrapper<*>.visibleAndManaged: Boolean
  get() = node.isVisible && node.isManaged
  set(value) {
	node.isVisible = value
	node.isManaged = value
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
  o: Any, op: (T?)->Unit
) = apply {

  var listener: ChangeListener<T>? = null
  val weakRef = WeakReference(o)
  listener = ChangeListener { _, _, new ->
	if (weakRef.get() == null) {
	  removeListener(listener)
	}
	op(new)
  }
  addListener(listener)
}


fun intColorToFXColor(i: Int): Color {
  return Color.rgb(i shr 16 and 0xFF, i shr 8 and 0xFF, i and 0xFF)
}


val fileIcons = LRUCache<MFile, BufferedImage>(500).withStoringDefault { f ->

  if (thisMachine is WINDOWS) jswingIconToImage(
	FileSystemView.getFileSystemView().getSystemIcon(f)
  )!!.toBufferedImage() else {
	val icon = JFileChooser().let { it.ui.getFileView(it) }.getIcon(f)
	val bi = BufferedImage(
	  icon.iconWidth, icon.iconHeight, BufferedImage.TYPE_INT_ARGB
	)
	icon.paintIcon(null, bi.graphics, 0, 0)
	bi
  }


}


enum class Corner { NW, NE, SW, SE }

fun Pane.resizer(corner: Corner) {/*var y = 0.0
  var x = 0.0*/
  var initEventX = 0.0
  var initEventY = 0.0
  var initStageX = 0.0
  var initStageY = 0.0
  var initStageWidth = 0.0
  var initStageHeight = 0.0
  var initStageMaxX = 0.0
  var initStageMaxY = 0.0/*val MIN = 100.0*/
  var dragging = false
  fun isInDraggableZone(
	@Suppress(
	  "UNUSED_PARAMETER"
	) event: MouseEvent
  ): Boolean {    /*return event.y > region.height - RESIZE_MARGIN*/
	return true
  }
  add(Rectangle(50.0, 50.0, Color.BLUE).apply {
	setOnMouseReleased {
	  dragging = false
	  cursor = Cursor.DEFAULT
	}
	setOnMouseMoved {
	  cursor = if (isInDraggableZone(it) || dragging) {
		when (corner) {
		  NW -> Cursor.NW_RESIZE
		  NE -> Cursor.NE_RESIZE
		  SW -> Cursor.SW_RESIZE
		  SE -> Cursor.SE_RESIZE
		}
	  } else {
		Cursor.DEFAULT
	  }
	}
	setOnMouseDragged {
	  if (dragging) {

		when (corner) {
		  NW -> {
			stage!!.y = initStageY + (it.screenY - initEventY)
			stage!!.height = initStageMaxY - stage!!.y
			stage!!.x = initStageX + (it.screenX - initEventX)
			stage!!.width = initStageMaxX - stage!!.x
		  }

		  NE -> {
			stage!!.y = initStageY + (it.screenY - initEventY)
			stage!!.height = initStageMaxY - stage!!.y
			stage!!.width = initStageWidth + (it.screenX - initEventX)
		  }

		  SW -> {
			stage!!.height = initStageHeight + (it.screenY - initEventY)
			stage!!.x = initStageX + (it.screenX - initEventX)
			stage!!.width = initStageMaxX - stage!!.x
		  }

		  SE -> {
			stage!!.height = initStageHeight + (it.screenY - initEventY)
			stage!!.width = initStageWidth + (it.screenX - initEventX)
		  }
		}
	  }
	}
	setOnMousePressed {
	  if (isInDraggableZone(it)) {
		dragging = true
		initEventX = it.screenX
		initEventY = it.screenY
		initStageHeight = stage!!.height
		initStageWidth = stage!!.width
		initStageX = stage!!.x
		initStageY = stage!!.y
		initStageMaxY = initStageHeight + initStageY
		initStageMaxX = initStageWidth + initStageX
	  }
	}
  })

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


fun Node.disableContextMenu() {
  addEventFilter(ContextMenuEvent.ANY) {
	it.consume()
  }
}

fun <T: Event> Node.filterAndConsume(eventType: EventType<T>, handler: EventHandler<T>) {
  addEventFilter(eventType) {
	handler.handle(it)
	it.consume()
  }
}

fun <T: Event> Node.handleAndConsume(eventType: EventType<T>, handler: EventHandler<T>) {
  addEventHandler(eventType) {
	handler.handle(it)
	it.consume()
  }
}

class DummyAppForFxThreadForScreen: Application() {
  override fun start(primaryStage: Stage?) = Unit
}

fun minimalNumberAxis() = NumberAxis().apply {
  minorTickCount = 0
  isAutoRanging = false
}

fun <N: Node> Parent.addr(child: N, op: (N.()->Unit)? = null): N {
  op?.invoke(child)
  add(child)
  return child
}
fun <N: Node> NodeWrapper<Parent>.addr(child: N, op: (N.()->Unit)? = null): N {
  op?.invoke(child)
  node.add(child)
  return child
}

typealias FXDuration = javafx.util.Duration

fun matt.async.date.Duration.toFXDuration(): FXDuration = FXDuration.millis(this.inMilliseconds)


fun BooleanProperty.checkbox() = CheckBox(name).also {
  it.selectedProperty().bindBidirectional(this)
}

fun Pane.addAll(vararg nodes: Node) = children.addAll(nodes)
fun Pane.addAll(nodes: Iterable<Node>) = children.addAll(nodes)

fun NodeWrapper<Pane>.addAll(vararg nodes: Node) = node.addAll(*nodes)
fun NodeWrapper<Pane>.addAll(nodes: Iterable<Node>) = node.addAll(nodes)

/*fun Stage.setOnClosed(op: () -> Unit) {
  showingProperty()
}*/


fun Node.onDoubleClickConsume(action: ()->Unit) {
  setOnMouseClicked {
	if (it.clickCount == 2) {
	  action()
	  it.consume()
	}
  }
}

class TreeTableTreeView<T>(val table: Boolean): TreeTableView<T>() {
  override fun resize(width: Double, height: Double) {
	super.resize(width, height)
	if (!table) {
	  val header = lookup("TableHeaderRow") as Pane
	  header.minHeight = 0.0
	  header.prefHeight = 0.0
	  header.maxHeight = 0.0
	  header.isVisible = false
	}
  }
}

fun Button.disable() {
  isDisable = true
}

fun Button.enable() {
  isDisable = false
}