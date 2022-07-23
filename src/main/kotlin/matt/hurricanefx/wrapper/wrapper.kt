@file:Suppress("UNUSED_EXPRESSION")

package matt.hurricanefx.wrapper

import javafx.beans.property.DoubleProperty
import javafx.beans.property.ObjectProperty
import javafx.beans.property.ReadOnlyDoubleProperty
import javafx.beans.property.ReadOnlyObjectProperty
import javafx.collections.ObservableList
import javafx.collections.ObservableSet
import javafx.css.CssMetaData
import javafx.css.PseudoClass
import javafx.css.Styleable
import javafx.event.ActionEvent
import javafx.event.Event
import javafx.event.EventHandler
import javafx.event.EventTarget
import javafx.event.EventType
import javafx.geometry.Bounds
import javafx.geometry.Insets
import javafx.geometry.Orientation
import javafx.geometry.Pos
import javafx.scene.Group
import javafx.scene.Node
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.SnapshotParameters
import javafx.scene.canvas.Canvas
import javafx.scene.chart.AreaChart
import javafx.scene.chart.Axis
import javafx.scene.chart.BarChart
import javafx.scene.chart.BubbleChart
import javafx.scene.chart.Chart
import javafx.scene.chart.LineChart
import javafx.scene.chart.PieChart
import javafx.scene.chart.ScatterChart
import javafx.scene.chart.StackedBarChart
import javafx.scene.control.Accordion
import javafx.scene.control.Button
import javafx.scene.control.ButtonBar
import javafx.scene.control.ButtonBase
import javafx.scene.control.CheckBox
import javafx.scene.control.CheckMenuItem
import javafx.scene.control.ChoiceBox
import javafx.scene.control.ColorPicker
import javafx.scene.control.ComboBox
import javafx.scene.control.ComboBoxBase
import javafx.scene.control.Control
import javafx.scene.control.DatePicker
import javafx.scene.control.Hyperlink
import javafx.scene.control.Label
import javafx.scene.control.Labeled
import javafx.scene.control.ListCell
import javafx.scene.control.ListView
import javafx.scene.control.MenuBar
import javafx.scene.control.MenuButton
import javafx.scene.control.MenuItem
import javafx.scene.control.MultipleSelectionModel
import javafx.scene.control.Pagination
import javafx.scene.control.PasswordField
import javafx.scene.control.ProgressBar
import javafx.scene.control.ProgressIndicator
import javafx.scene.control.RadioButton
import javafx.scene.control.ScrollPane
import javafx.scene.control.Separator
import javafx.scene.control.Slider
import javafx.scene.control.Spinner
import javafx.scene.control.SpinnerValueFactory
import javafx.scene.control.SplitMenuButton
import javafx.scene.control.SplitPane
import javafx.scene.control.Tab
import javafx.scene.control.TabPane
import javafx.scene.control.TableView
import javafx.scene.control.TextArea
import javafx.scene.control.TextField
import javafx.scene.control.TextInputControl
import javafx.scene.control.TitledPane
import javafx.scene.control.ToggleButton
import javafx.scene.control.ToolBar
import javafx.scene.control.TreeCell
import javafx.scene.control.TreeItem
import javafx.scene.control.TreeTableRow
import javafx.scene.control.TreeTableView
import javafx.scene.control.TreeView
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.image.WritableImage
import javafx.scene.input.DataFormat
import javafx.scene.input.DragEvent
import javafx.scene.input.KeyEvent
import javafx.scene.input.MouseEvent
import javafx.scene.input.ScrollEvent
import javafx.scene.input.TransferMode
import javafx.scene.input.TransferMode.ANY
import javafx.scene.input.ZoomEvent
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.Background
import javafx.scene.layout.Border
import javafx.scene.layout.BorderPane
import javafx.scene.layout.FlowPane
import javafx.scene.layout.GridPane
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import javafx.scene.layout.Priority
import javafx.scene.layout.Region
import javafx.scene.layout.StackPane
import javafx.scene.layout.TilePane
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.paint.Paint
import javafx.scene.shape.Arc
import javafx.scene.shape.Circle
import javafx.scene.shape.CubicCurve
import javafx.scene.shape.Ellipse
import javafx.scene.shape.Line
import javafx.scene.shape.Path
import javafx.scene.shape.PathElement
import javafx.scene.shape.Polygon
import javafx.scene.shape.Polyline
import javafx.scene.shape.QuadCurve
import javafx.scene.shape.Rectangle
import javafx.scene.shape.SVGPath
import javafx.scene.shape.Shape
import javafx.scene.text.Text
import javafx.scene.text.TextFlow
import javafx.stage.Stage
import javafx.stage.StageStyle
import javafx.stage.Window
import javafx.stage.WindowEvent
import javafx.util.Callback
import matt.file.MFile
import matt.file.toMFile
import matt.hurricanefx.addAll
import matt.hurricanefx.eye.lib.onChange
import matt.hurricanefx.stage
import matt.hurricanefx.tornadofx.nodes.add
import matt.hurricanefx.tornadofx.nodes.getToggleGroup
import matt.hurricanefx.tornadofx.nodes.tooltip
import matt.hurricanefx.wrapper.ParentWrapper.Companion.wrapped
import java.time.LocalDate

private typealias NW = NodeWrapper<*>

@DslMarker
annotation class FXNodeWrapperDSL

@FXNodeWrapperDSL
interface EventTargetWrapper<N: EventTarget> {
  companion object {
	fun EventTarget.wrapped() = object: EventTargetWrapper<EventTarget> {
	  override val node = this@wrapped
	}
  }

  val node: N
  fun getToggleGroup() = node.getToggleGroup()


}


interface StyleableWrapper {
  val node: Styleable
  fun getTypeSelector() = node.typeSelector
  fun getId() = node.id

  fun getCssMetaData() = node.cssMetaData
  fun getStyleableParent() = node.styleableParent
  fun getPseudoClassStates() = node.pseudoClassStates

  fun getStyleClass() = node.styleClass
  fun getStyle() = node.style

}


interface WindowWrapper<W: Window>: EventTargetWrapper<W> {
  companion object {
	fun <W: Window> W.wrapped() = object: WindowWrapper<W> {
	  override val node = this@wrapped
	}
  }
  var x
	get() = node.x
	set(value) {
	  node.x = value
	}

  fun xProperty() = node.xProperty()

  var y
	get() = node.y
	set(value) {
	  node.y = value
	}

  fun yProperty() = node.yProperty()


  var height
	get() = node.height
	set(value) {
	  node.height = value
	}

  fun heightProperty() = node.heightProperty()

  var width
	get() = node.width
	set(value) {
	  node.width = value
	}

  fun widthProperty() = node.widthProperty()

  fun setOnShowing(value: EventHandler<WindowEvent>) = node.setOnShowing(value)

  val isShowing get() = node.isShowing
  fun showingProperty() = node.showingProperty()

  fun hide() = node.hide()


  val scene get() = node.scene
  fun sceneProperty() = node.sceneProperty()
}

open class StageWrapper(override val node: Stage): WindowWrapper<Stage> {
  companion object {
	fun Stage.wrapped() = StageWrapper(this)
  }
  constructor(stageStyle: StageStyle): this(Stage(stageStyle))

  fun iconifiedProperty() = node.iconifiedProperty()
  fun show() = node.show()
  fun close() = node.close()
  fun toFront() = node.toFront()

  var isMaximized
	get() = node.isMaximized
	set(value) {
	  node.isMaximized = value
	}

  fun maximizedProperty() = node.maximizedProperty()

  override var scene
	get() = super.scene
	set(value) {
	  node.scene = value
	}



  fun <T: Event> addEventFilter(eventType: EventType<T>, handler: EventHandler<T>) =
	node.addEventFilter(eventType, handler)

  fun <T: Event> addEventHandler(eventType: EventType<T>, handler: EventHandler<T>) =
	node.addEventHandler(eventType, handler)

  var isAlwaysOnTop
	get() = node.isAlwaysOnTop
	set(value) {
	  node.isAlwaysOnTop = value
	}

  fun alwaysOnTopProperty() = node.alwaysOnTopProperty()

}

open class SceneWrapper(override val node: Scene): EventTargetWrapper<Scene> {
  companion object {
	fun Scene.wrapped() = SceneWrapper(this)
  }
  constructor(root: ParentWrapper): this(Scene(root.node))

  val stylesheets get() = node.stylesheets



  var fill
	get() = node.fill
	set(value) {
	  node.fill = value
	}

  fun fillProperty() = node.fillProperty()


  fun <T: Event> addEventFilter(eventType: EventType<T>, handler: EventHandler<T>) =
	node.addEventFilter(eventType, handler)

  fun <T: Event> addEventHandler(eventType: EventType<T>, handler: EventHandler<T>) =
	node.addEventHandler(eventType, handler)


  val window get() = node.window
}


typealias NodeW = NodeWrapper<*>

interface NodeWrapper<N: Node>: EventTargetWrapper<N>, StyleableWrapper {
  companion object {

	fun <N: Node> N.wrapped(): NodeWrapper<N> = object: NodeWrapper<N> {
	  override val node = this@wrapped

	}
  }

  fun setStyle(value: String) {
	node.style = value
  }


  var opacity
	get() = node.opacity
	set(value) {
	  node.opacity = value
	}

  fun opacityProperty() = node.opacityProperty()
  var rotate
	get() = node.rotate
	set(value) {
	  node.rotate = value
	}
  val rotateProperty get() = node.rotateProperty()

  var rotationAxis
	get() = node.rotationAxis
	set(value) {
	  node.rotationAxis = value
	}
  val rotationAxisProperty get() = node.rotationAxisProperty()


  val properties get() = node.properties

  var isCache
	get() = node.isCache
	set(value) {
	  node.isCache = value
	}

  fun cacheProperty() = node.cacheProperty()

  var cacheHint
	get() = node.cacheHint
	set(value) {
	  node.cacheHint = value
	}

  fun cacheHintProperty() = node.cacheHintProperty()


  fun snapshot(params: SnapshotParameters?, image: WritableImage?) = node.snapshot(params, image)

  fun startDragAndDrop(vararg transferModes: TransferMode) = node.startDragAndDrop(*transferModes)
  fun startFullDrag() = node.startFullDrag()

  fun lookupAll(selector: String) = node.lookupAll(selector)

  var cursor
	get() = node.cursor
	set(value) {
	  node.cursor = value
	}

  fun cursorProperty() = node.cursorProperty()


  var clip
	get() = node.clip
	set(value) {
	  node.clip = value
	}

  fun clipProperty() = node.clipProperty()

  var blendMode
	get() = node.blendMode
	set(value) {
	  node.blendMode = value
	}

  fun blendModeProperty() = node.blendModeProperty()

  fun autosize() = node.autosize()

  fun <T: Event> addEventFilter(eventType: EventType<T>, handler: EventHandler<T>) =
	node.addEventFilter(eventType, handler)

  fun <T: Event> addEventHandler(eventType: EventType<T>, handler: EventHandler<T>) =
	node.addEventHandler(eventType, handler)


  fun <T: Event> removeEventFilter(eventType: EventType<T>, handler: EventHandler<T>) =
	node.removeEventFilter(eventType, handler)

  fun <T: Event> removeEventHandler(eventType: EventType<T>, handler: EventHandler<T>) =
	node.removeEventHandler(eventType, handler)

  fun localToScene(bounds: Bounds) = node.localToScene(bounds)
  fun localToScreen(bounds: Bounds) = node.localToScreen(bounds)
  val boundsInLocal get() = node.boundsInLocal
  val boundsInParent get() = node.boundsInParent


  val scene: Scene? get() = node.scene
  fun sceneProperty() = node.sceneProperty()
  val stage get() = node.stage


  operator fun NW.unaryPlus() {
	this@NodeWrapper.add(this)
  }

  fun setOnKeyPressed(listener: (KeyEvent)->Unit) {
	node.setOnKeyPressed(listener)
  }

  fun setOnMousePressed(listener: (MouseEvent)->Unit) {
	node.setOnMousePressed(listener)
  }


  fun setOnMouseClicked(listener: (MouseEvent)->Unit) {
	node.setOnMouseClicked(listener)
  }


  fun setOnMouseDragged(listener: (MouseEvent)->Unit) {
	node.setOnMouseDragged(listener)
  }

  fun setOnMouseReleased(listener: (MouseEvent)->Unit) {
	node.setOnMouseReleased(listener)
  }

  fun setOnMouseExited(listener: (MouseEvent)->Unit) {
	node.setOnMouseExited(listener)
  }

  fun setOnDragEntered(listener: (DragEvent)->Unit) {
	node.setOnDragEntered(listener)
  }

  fun setOnDragOver(listener: (DragEvent)->Unit) {
	node.setOnDragOver(listener)
  }

  fun setOnDragDetected(listener: (MouseEvent)->Unit) {
	node.setOnDragDetected(listener)
  }

  fun setOnDragDone(listener: (DragEvent)->Unit) {
	node.setOnDragDone(listener)
  }

  fun setOnDragExited(listener: (DragEvent)->Unit) {
	node.setOnDragExited(listener)
  }

  fun setOnDragDropped(listener: (DragEvent)->Unit) {
	node.setOnDragDropped(listener)
  }


  fun toFront() = node.toFront()
  fun toBack() = node.toBack()

  fun setOnScroll(listener: (ScrollEvent)->Unit) {
	node.setOnScroll(listener)
  }

  fun managedProperty() = node.managedProperty()
  fun visibleProperty() = node.visibleProperty()


  var isDisable
	get() = node.isDisable
	set(value) {
	  node.isDisable = value
	}

  fun disableProperty() = node.disableProperty()

  val isFocused get() = node.isFocused
  fun focusedProperty() = node.focusedProperty()


  var hGrow: Priority
	get() = HBox.getHgrow(node)
	set(value) {
	  HBox.setHgrow(node, value)
	}
  var vGrow: Priority
	get() = VBox.getVgrow(node)
	set(value) {
	  VBox.setVgrow(node, value)
	}

  fun setOnZoom(op: (ZoomEvent)->Unit) = node.setOnZoom(op)


  var isVisible
	get() = node.isVisible
	set(value) {
	  node.isVisible = value
	}

  var isManaged
	get() = node.isManaged
	set(value) {
	  node.isManaged = value
	}

  var translateX
	get() = node.translateX
	set(value) {
	  node.translateX = value
	}

  fun translateXProperty() = node.translateXProperty()

  var translateY
	get() = node.translateY
	set(value) {
	  node.translateY = value
	}

  fun translateYProperty() = node.translateYProperty()


  var layoutX
	get() = node.layoutX
	set(value) {
	  node.layoutX = value
	}

  fun layoutXProperty() = node.layoutXProperty()
  var layoutY
	get() = node.layoutY
	set(value) {
	  node.layoutY = value
	}

  fun layoutYProperty() = node.layoutYProperty()


  var scaleX
	get() = node.scaleX
	set(value) {
	  node.scaleX = value
	}

  fun scaleXProperty() = node.scaleXProperty()
  var scaleY
	get() = node.scaleY
	set(value) {
	  node.scaleY = value
	}

  fun scaleYProperty() = node.scaleYProperty()

  fun requestFocus() = node.requestFocus()


}


interface ParentWrapper: NodeWrapper<Parent> {
  companion object {
	fun Parent.wrapped() = object: ParentWrapper {
	  override val node = this@wrapped
	}
  }

  override val node: Parent

  val childrenUnmodifiable get() = node.childrenUnmodifiable

  fun requestLayout() = node.requestLayout()


}

val NodeWrapper<*>.parent get() : ParentWrapper? = node.parent?.wrapped()
fun NodeWrapper<*>.parentProperty() = node.parentProperty()


open class RegionWrapper(override val node: Region = Region()): ParentWrapper {

  companion object {
	fun Region.wrapped() = RegionWrapper(this)
  }


  fun setMinSize(minWidth: Double, minHeight: Double) = node.setMinSize(minWidth, minHeight)

  var border: Border?
	get() = node.border
	set(value) {
	  node.border = value
	}
  val borderProperty: ObjectProperty<Border> get() = node.borderProperty()

  var padding
	get() = node.padding
	set(value) {
	  node.padding = value
	}
  val paddingProperty: ObjectProperty<Insets> get() = node.paddingProperty()


  var background: Background?
	get() = node.background
	set(value) {
	  node.background = value
	}
  val backgroundProperty: ObjectProperty<Background> get() = node.backgroundProperty()

  val width get() = widthProperty.value
  val widthProperty: ReadOnlyDoubleProperty get() = node.widthProperty()
  val prefWidthProperty: DoubleProperty get() = node.prefWidthProperty()
  var prefWidth: Double
	get() = node.prefWidth
	set(value) {
	  node.prefWidth = value
	}
  val minWidthProperty: DoubleProperty get() = node.minWidthProperty()
  var minWidth: Double
	get() = node.minWidth
	set(value) {
	  node.minWidth = value
	}


  val maxWidthProperty: DoubleProperty get() = node.maxWidthProperty()
  var maxWidth: Double
	get() = node.maxWidth
	set(value) {
	  node.maxWidth = value
	}

  val height get() = heightProperty.value
  val heightProperty: ReadOnlyDoubleProperty get() = node.heightProperty()
  val prefHeightProperty: DoubleProperty get() = node.prefHeightProperty()
  var prefHeight: Double
	get() = node.prefHeight
	set(value) {
	  node.prefHeight = value
	}
  val minHeightProperty: DoubleProperty get() = node.minHeightProperty()
  var minHeight: Double
	get() = node.minHeight
	set(value) {
	  node.minHeight = value
	}
  val maxHeightProperty: DoubleProperty get() = node.maxHeightProperty()
  var maxHeight: Double
	get() = node.maxHeight
	set(value) {
	  node.maxHeight = value
	}

  fun setOnFilesDropped(op: (List<MFile>)->Unit) {
	node.setOnDragEntered {
	  it.acceptTransferModes(*ANY)
	}
	node.setOnDragOver {
	  it.acceptTransferModes(*ANY)
	}
	node.setOnDragDropped {
	  if (DataFormat.FILES in it.dragboard.contentTypes) {
		op(it.dragboard.files.map { it.toMFile() })
	  }
	  it.consume()
	}
  }


}


open class PaneWrapper(
  override val node: Pane = Pane()
): RegionWrapper(node) {

  companion object {
	fun Pane.wrapped() = PaneWrapper(this)
  }

  operator fun Collection<Node>.unaryPlus() {
	node.addAll(this)
  }


  val children: ObservableList<Node> get() = node.children
}


abstract class BoxWrapper<N: Pane>(override val node: N): PaneWrapper(node) {

  var alignment: Pos
	get() = (node as? HBox)?.alignment ?: (node as VBox).alignment
	set(value) {
	  if (node is HBox) (node as HBox).alignment = value
	  else (node as VBox).alignment = value
	}
  var spacing: Double
	get() = (node as? HBox)?.spacing ?: (node as VBox).spacing
	set(value) {
	  if (node is HBox) (node as HBox).spacing = value
	  else (node as VBox).spacing = value
	}
}


open class VBoxWrapper(node: VBox = VBox(), op: VBoxWrapper.()->Unit = {}): BoxWrapper<VBox>(node) {
  constructor(vararg nodes: Node): this(VBox(*nodes))
  constructor(vararg nodes: NodeWrapper<*>): this(VBox(*nodes.map { it.node }.toTypedArray()))

  init {
	op()
  }
}


open class HBoxWrapper(node: HBox = HBox(), op: HBoxWrapper.()->Unit = {}): BoxWrapper<HBox>(node) {
  constructor(vararg nodes: Node): this(HBox(*nodes))

  init {
	op()
  }
}


class ScrollingVBoxWrapper(vbox: VBox = VBox(), op: ScrollingVBoxWrapper.()->Unit = {}): NodeWrapper<ScrollPane> {
  constructor(vbox: VBoxWrapper, op: ScrollingVBoxWrapper.()->Unit = {}): this(vbox.node, op)

  override val node = ScrollPane(VBox())

  init {
	op()
	node.content = vbox
  }
}


open class StackPaneWrapper(override val node: StackPane = StackPane()): PaneWrapper(node) {
  companion object {
	fun StackPane.wrapped() = StackPaneWrapper(this)
  }

  constructor(vararg nodes: Node): this(StackPane(*nodes))


  var alignment
	get() = node.alignment
	set(value) {
	  node.alignment = value
	}

  fun alignmentProperty() = node.alignmentProperty()
}


open class AnchorPaneWrapper(override val node: AnchorPane = AnchorPane()): PaneWrapper(node) {
  companion object {
	fun AnchorPane.wrapped() = AnchorPaneWrapper(this)
	fun setTopAnchor(n: Node, offset: Double) = AnchorPane.setTopAnchor(n, offset)
	fun setBottomAnchor(n: Node, offset: Double) = AnchorPane.setBottomAnchor(n, offset)
	fun setLeftAnchor(n: Node, offset: Double) = AnchorPane.setLeftAnchor(n, offset)
	fun setRightAnchor(n: Node, offset: Double) = AnchorPane.setRightAnchor(n, offset)
  }


}


open class BorderPaneWrapper(override val node: BorderPane = BorderPane()): PaneWrapper(node) {
  companion object {
	fun BorderPane.wrapped() = BorderPaneWrapper(this)
  }

  var center
	get() = node.center
	set(value) {
	  node.center = value
	}
  var top
	get() = node.top
	set(value) {
	  node.top = value
	}
  var left
	get() = node.left
	set(value) {
	  node.left = value
	}
  var right
	get() = node.right
	set(value) {
	  node.right = value
	}
  var bottom
	get() = node.bottom
	set(value) {
	  node.bottom = value
	}
}


open class SplitPaneWrapper(override val node: SplitPane = SplitPane()): ControlWrapper(node) {
  companion object {
	fun SplitPane.wrapped() = SplitPaneWrapper(this)
  }

  var orientation
	get() = node.orientation
	set(value) {
	  node.orientation = value
	}
  val items get() = node.items
}


open class GridPaneWrapper(override val node: GridPane = GridPane()): PaneWrapper(node) {
  companion object {
	fun GridPane.wrapped() = GridPaneWrapper(this)
	fun setConstraints(child: Node, columnIndex: Int, rowIndex: Int) =
	  GridPane.setConstraints(child, columnIndex, rowIndex)
  }

  val columnConstraints = node.columnConstraints
  val rowConstraints = node.rowConstraints

}


open class TilePaneWrapper(override val node: TilePane = TilePane()): PaneWrapper(node) {
  companion object {
	fun TilePane.wrapped() = TilePaneWrapper(this)
  }
}


open class FlowPaneWrapper(override val node: FlowPane = FlowPane()): PaneWrapper(node) {
  companion object {
	fun FlowPane.wrapped() = FlowPaneWrapper(this)
  }

  var orientation
	get() = node.orientation
	set(value) {
	  node.orientation = value
	}

  fun orientationProperty() = node.orientationProperty()

  var alignment
	get() = node.alignment
	set(value) {
	  node.alignment = value
	}

  fun alignmentProperty() = node.alignmentProperty()

  var rowValignment
	get() = node.rowValignment
	set(value) {
	  node.rowValignment = value
	}

  fun rowValignmentProperty() = node.rowValignmentProperty()

  var columnHalignment
	get() = node.columnHalignment
	set(value) {
	  node.columnHalignment = value
	}

  fun columnHalignmentProperty() = node.columnHalignmentProperty()


  var hgap
	get() = node.hgap
	set(value) {
	  node.hgap = value
	}

  fun hgapProperty() = node.hgapProperty()


  var vgap
	get() = node.vgap
	set(value) {
	  node.vgap = value
	}

  fun vgapProperty() = node.vgapProperty()


  var prefWrapLength
	get() = node.prefWrapLength
	set(value) {
	  node.prefWrapLength = value
	}

  fun prefWrapLengthProperty() = node.prefWrapLengthProperty()


}


open class ScrollPaneWrapper(override val node: ScrollPane = ScrollPane()): ControlWrapper(node) {

  companion object {
	fun ScrollPane.wrapped() = ScrollPaneWrapper(this)
  }

  constructor(content: Node?): this(ScrollPane(content))


  var viewportBounds
	get() = node.viewportBounds
	set(value) {
	  node.viewportBounds = value
	}

  var vmin
	get() = node.vmin
	set(value) {
	  node.vmin = value
	}

  var vmax
	get() = node.vmax
	set(value) {
	  node.vmax = value
	}


  var hmin
	get() = node.hmin
	set(value) {
	  node.hmin = value
	}
  var hmax
	get() = node.hmax
	set(value) {
	  node.hmax = value
	}


  var vbarPolicy
	get() = node.vbarPolicy
	set(value) {
	  node.vbarPolicy = value
	}

  var hbarPolicy
	get() = node.hbarPolicy
	set(value) {
	  node.hbarPolicy = value
	}

  var isFitToWidth
	get() = node.isFitToWidth
	set(value) {
	  node.isFitToWidth = value
	}

  fun fitToWidthProperty() = node.fitToWidthProperty()
  var isFitToHeight
	get() = node.isFitToHeight
	set(value) {
	  node.isFitToHeight = value
	}

  fun fitToHeightProperty() = node.fitToHeightProperty()

  var prefViewportWidth
	get() = node.prefViewportWidth
	set(value) {
	  node.prefViewportWidth = value
	}
  var prefViewportHeight
	get() = node.prefViewportHeight
	set(value) {
	  node.prefViewportHeight = value
	}

  var hvalue
	get() = node.hvalue
	set(value) {
	  node.hvalue = value
	}
  var vvalue
	get() = node.vvalue
	set(value) {
	  node.vvalue = value
	}

  var content
	get() = node.content
	set(value) {
	  node.content = value
	}

}


sealed class TreeLikeWrapper<N: Region, T>(node: N): RegionWrapper(node) {
  abstract var root: TreeItem<T>
  abstract var isShowRoot: Boolean
  abstract fun setOnSelectionChange(listener: (TreeItem<T>?)->Unit)
  abstract val selectionModel: MultipleSelectionModel<TreeItem<T>>
  val selectedItem: TreeItem<T>? get() = selectionModel.selectedItem
  val selectedItemProperty: ReadOnlyObjectProperty<TreeItem<T>> get() = selectionModel.selectedItemProperty()
  val selectedValue: T? get() = selectedItem?.value
  abstract fun scrollTo(i: Int)
  abstract fun getRow(ti: TreeItem<T>): Int
}


class TreeViewWrapper<T>(override val node: TreeView<T> = TreeView(), op: TreeViewWrapper<T>.()->Unit = {}):
  TreeLikeWrapper<TreeView<T>, T>(node) {
  init {
	op()
  }


  fun editableProperty() = node.editableProperty()




  var cellFactory
	get() = node.cellFactory
	set(value) {
	  node.cellFactory = value

	}

  fun cellFactoryProperty() = node.cellFactoryProperty()
  @JvmName("setCellFactory1") fun setCellFactory(value: Callback<TreeView<T>, TreeCell<T>>) = node.setCellFactory(value)


  override var root: TreeItem<T>
	get() = node.root
	set(value) {
	  node.root = value
	}
  override var isShowRoot: Boolean
	get() = node.isShowRoot
	set(value) {
	  node.isShowRoot = value
	}


  override fun setOnSelectionChange(listener: (TreeItem<T>?)->Unit) {
	node.selectionModel.selectedItemProperty().onChange(listener)
  }

  override val selectionModel: MultipleSelectionModel<TreeItem<T>> get() = node.selectionModel
  override fun scrollTo(i: Int) = node.scrollTo(i)
  override fun getRow(ti: TreeItem<T>) = node.getRow(ti)


}


class TreeTableViewWrapper<T>(
  override val node: TreeTableView<T> = TreeTableView(),
  op: TreeTableViewWrapper<T>.()->Unit = {}
): TreeLikeWrapper<TreeTableView<T>, T>(node) {
  init {
	op()
  }


  fun editableProperty() = node.editableProperty()


  val sortOrder get() = node.sortOrder

  override var root: TreeItem<T>
	get() = node.root
	set(value) {
	  node.root = value
	}
  override var isShowRoot: Boolean
	get() = node.isShowRoot
	set(value) {
	  node.isShowRoot = value
	}

  var columnResizePolicy: Callback<TreeTableView.ResizeFeatures<*>, Boolean>
	get() = node.columnResizePolicy
	set(value) {
	  node.columnResizePolicy = value
	}
  val columns get() = node.columns
  override fun getRow(ti: TreeItem<T>) = node.getRow(ti)

  override fun setOnSelectionChange(listener: (TreeItem<T>?)->Unit) {
	node.selectionModel.selectedItemProperty().onChange(listener)
  }

  override val selectionModel: MultipleSelectionModel<TreeItem<T>> get() = node.selectionModel
  override fun scrollTo(i: Int) = node.scrollTo(i)

  fun setRowFactory(value: Callback<TreeTableView<T>, TreeTableRow<T>>) = node.setRowFactory(value)

  fun sort() = node.sort()

}


class ChoiceBoxWrapper<T>(
  override val node: ChoiceBox<T> = ChoiceBox(),
  op: ChoiceBoxWrapper<T>.()->Unit = {}
): ControlWrapper(node) {

  constructor(items: ObservableList<T>): this(ChoiceBox(items))


  var converter
	get() = node.converter
	set(value) {
	  node.converter = value
	}

  fun converterProperty() = node.converterProperty()


  var items
	get() = node.items
	set(value) {
	  node.items = value
	}
  var value
	get() = node.value
	set(value) {
	  node.value = value
	}

  fun valueProperty() = node.valueProperty()

  init {
	op()
  }

  fun setOnAction(op: (ActionEvent)->Unit) {
	node.setOnAction(op)
  }
}


open class TabPaneWrapper(
  override val node: TabPane = TabPane(),
  op: TabPaneWrapper.()->Unit = {}
): ControlWrapper(node) {

  constructor(vararg tabs: Tab): this(TabPane(*tabs))

  init {
	op()
  }

  val tabs get() = node.tabs

  val selectionModel get() = node.selectionModel
}


open class ButtonBaseWrapper(override val node: ButtonBase): LabeledWrapper(node) {

  fun fire() = node.fire()
  fun setOnAction(op: (ActionEvent)->Unit) {
	node.setOnAction(op)
  }
}


abstract class ControlWrapper(override val node: Control): RegionWrapper(node)


open class ButtonWrapper(
  override val node: Button = Button(),
  op: ButtonWrapper.()->Unit = {}
): ButtonBaseWrapper(node) {
  companion object {
	fun Button.wrapped() = ButtonWrapper(this)
  }

  constructor(text: String?, graphic: Node? = null): this(Button(text, graphic))

  init {
	op()
  }


}


open class ComboBoxBaseWrapper<T>(override val node: ComboBoxBase<T>): ControlWrapper(node) {

  fun editableProperty() = node.editableProperty()

  var value
	get() = node.value
	set(theVal) {
	  node.value = theVal
	}

  fun valueProperty() = node.valueProperty()

  var promptText
	get() = node.promptText
	set(value) {
	  node.promptText = value
	}

  fun promptTextProperty() = node.promptTextProperty()


}


class ComboBoxWrapper<T>(
  override val node: ComboBox<T> = ComboBox<T>(),
  op: ComboBoxWrapper<T>.()->Unit = {}
): ComboBoxBaseWrapper<T>(node) {
  companion object {
	fun <T> ComboBox<T>.wrapped() = ComboBoxWrapper(this)
  }

  constructor(items: ObservableList<T>): this(ComboBox(items))

  init {
	op()
  }


  var cellFactory
	get() = node.cellFactory
	set(value) {
	  node.cellFactory = value

	}

  fun cellFactoryProperty() = node.cellFactoryProperty()
  @JvmName("setCellFactory1") fun setCellFactory(value: Callback<ListView<T>, ListCell<T>>) = node.setCellFactory(value)


  var items
	get() = node.items
	set(value) {
	  node.items = value
	}

  fun itemsProperty() = node.itemsProperty()

  var converter
	get() = node.converter
	set(value) {
	  node.converter = value
	}

  fun converterProperty() = node.converterProperty()

  val selectionModel get() = node.selectionModel


}


class ColorPickerWrapper(
  override val node: ColorPicker = ColorPicker(),
  op: ColorPickerWrapper.()->Unit = {}
): ComboBoxBaseWrapper<Color>(node) {
  companion object {
	fun ColorPicker.wrapped() = ColorPickerWrapper(this)
  }

  init {
	op()
  }
}


class DatePickerWrapper(
  override val node: DatePicker = DatePicker(),
  op: DatePickerWrapper.()->Unit = {}
): ComboBoxBaseWrapper<LocalDate>(node) {

  companion object {
	fun DatePicker.wrapped() = DatePickerWrapper(this)
  }

  init {
	op()
  }


}


open class TextFlowWrapper(
  override val node: TextFlow = TextFlow(),
  op: TextFlowWrapper.()->Unit = {}
): PaneWrapper(node) {
  companion object {
	fun TextFlow.wrapped() = TextFlowWrapper(this)
  }

  constructor(vararg nodes: Node?): this(TextFlow(*nodes))

  init {
	op()
  }

}


interface ShapeWrapper: NodeWrapper<Shape> {
  override val node: Shape
  var stroke
	get() = node.stroke
	set(value) {
	  node.stroke = value
	}

  fun strokeProperty() = node.strokeProperty()


  var strokeWidth
	get() = node.strokeWidth
	set(value) {
	  node.strokeWidth = value
	}

  fun strokeWidthProperty() = node.strokeWidthProperty()

  var strokeType
	get() = node.strokeType
	set(value) {
	  node.strokeType = value
	}

  fun strokeTypeProperty() = node.strokeTypeProperty()


  var fill
	get() = node.fill
	set(value) {
	  node.fill = value
	}

  fun fillProperty(): ObjectProperty<Paint> = node.fillProperty()
}


open class TextWrapper(
  override val node: Text = Text(),
  op: TextWrapper.()->Unit = {}
): ShapeWrapper {
  companion object {
	fun Text.wrapped() = TextWrapper(this)
  }

  constructor(text: String): this(Text(text))

  init {
	op()
  }

  var text
	get() = node.text
	set(value) {
	  node.text = value
	}

  fun textProperty() = node.textProperty()

  var font
	get() = node.font
	set(value) {
	  node.font = value
	}

  fun fontProperty() = node.fontProperty()

  var textAlignment
	get() = node.textAlignment
	set(value) {
	  node.textAlignment = value
	}

  fun textAlignmentProperty() = node.textAlignmentProperty()

}


open class TextInputControlWrapper(override val node: TextInputControl): ControlWrapper(node) {


  fun editableProperty() = node.editableProperty()


  var text
	get() = node.text
	set(value) {
	  node.text = value
	}

  fun textProperty() = node.textProperty()

  var promptText
	get() = node.promptText
	set(value) {
	  node.promptText = value
	}

  fun promptTextProperty() = node.promptTextProperty()

  var font
	get() = node.font
	set(value) {
	  node.font = value
	}

  fun fontProperty() = node.fontProperty()


  val caretPosition get() = node.caretPosition
  fun caretPositionProperty() = node.caretPositionProperty()

  fun end() = node.end()
  fun home() = node.home()

}


open class TextFieldWrapper(
  override val node: TextField = TextField(),
  op: TextFieldWrapper.()->Unit = {}
): TextInputControlWrapper(node) {
  companion object {
	fun TextField.wrapped() = TextFieldWrapper(this)
  }

  constructor(text: String?): this(TextField(text))

  init {
	op()
  }

  fun setOnAction(op: (ActionEvent)->Unit) {
	node.setOnAction(op)
  }


}


class PasswordFieldWrapper(
  override val node: PasswordField = PasswordField(),
  op: PasswordFieldWrapper.()->Unit = {}
): TextFieldWrapper(node) {
  init {
	op()
  }
}

fun PasswordField.wrapped() = PasswordFieldWrapper(this)


open class TextAreaWrapper(
  override val node: TextArea = TextArea(),
  op: TextAreaWrapper.()->Unit = {}
): TextInputControlWrapper(node) {
  companion object {
	fun TextArea.wrapped() = TextAreaWrapper(this)
  }

  init {
	op()
  }

}


open class LabeledWrapper(override val node: Labeled): ControlWrapper(node) {


  var textAlignment
	get() = node.textAlignment
	set(value) {
	  node.textAlignment = value
	}

  fun textAlignmentProperty() = node.textAlignmentProperty()


  var isMnemonicParsing
	get() = node.isMnemonicParsing
	set(value) {
	  node.isMnemonicParsing = value
	}

  fun mnemonicParsingProperty() = node.mnemonicParsingProperty()

  var text
	get() = node.text
	set(value) {
	  node.text = value
	}

  fun textProperty() = node.textProperty()
  var graphic
	get() = node.graphic
	set(value) {
	  node.graphic = value
	}

  fun graphicProperty() = node.graphicProperty()


  var contentDisplay
	get() = node.contentDisplay
	set(value) {
	  node.contentDisplay = value
	}

  var font
	get() = node.font
	set(value) {
	  node.font = value
	}

  fun fontProperty() = node.fontProperty()

  var isWrapText
	get() = node.isWrapText
	set(value) {
	  node.isWrapText = value
	}

  fun wrapTextProperty() = node.wrapTextProperty()


  var textFill
	get() = node.textFill
	set(value) {
	  node.textFill = value
	}

  fun textFillProperty() = node.textFillProperty()


}


class TitledPaneWrapper(
  override val node: TitledPane = TitledPane(),
  op: TitledPaneWrapper.()->Unit = {}
): LabeledWrapper(node) {
  companion object {
	fun TitledPane.wrapped() = TitledPaneWrapper(this)
  }

  init {
	op()
  }

  var isCollapsible
	get() = node.isCollapsible
	set(value) {
	  node.isCollapsible = value
	}
  var isExpanded
	get() = node.isExpanded
	set(value) {
	  node.isExpanded = value
	}
}


class SpinnerWrapper<T>(
  override val node: Spinner<T> = Spinner<T>(),
  op: SpinnerWrapper<T>.()->Unit = {}
): ControlWrapper(node) {
  companion object {
	fun <T> Spinner<T>.wrapped() = SpinnerWrapper<T>(this)
  }

  constructor(min: Int, max: Int, initial: Int, step: Int): this(Spinner(min, max, initial, step))
  constructor(min: Double, max: Double, initiall: Double, step: Double): this(Spinner(min, max, initiall, step))
  constructor(items: ObservableList<T>): this(Spinner(items))
  constructor(valueFactory: SpinnerValueFactory<T>): this(Spinner(valueFactory))

  init {
	op()
  }

  val value get() = node.value
  fun valueProperty() = node.valueProperty()

  var valueFactory
	get() = node.valueFactory
	set(value) {
	  node.valueFactory = value
	}


  var isEditable
	get() = node.isEditable
	set(value) {
	  node.isEditable = value
	}


  fun editableProperty() = node.editableProperty()

  fun increment() = node.increment()
  fun decrement() = node.decrement()
  fun increment(steps: Int) = node.increment(steps)
  fun decrement(steps: Int) = node.decrement(steps)
}


class SliderWrapper(
  override val node: Slider = Slider(),
  op: SliderWrapper.()->Unit = {}
): ControlWrapper(node) {
  companion object {
	fun Slider.wrapped() = SliderWrapper(this)
  }

  init {
	op()
  }

  var max
	get() = node.max
	set(value) {
	  node.max = value
	}

  var min
	get() = node.min
	set(value) {
	  node.min = value
	}

  var orientation
	get() = node.orientation
	set(value) {
	  node.orientation = value
	}

  var value
	get() = node.value
	set(value) {
	  node.value = value
	}

  fun valueProperty() = node.valueProperty()

  var valueChanging
	get() = node.isValueChanging
	set(value) {
	  node.isValueChanging = value
	}

  fun valueChangingProperty() = node.valueChangingProperty()

  var isSnapToTicks
	get() = node.isSnapToTicks
	set(value) {
	  node.isSnapToTicks = value
	}

  fun snapToTicksProperty() = node.snapToTicksProperty()


  var isShowTickMarks
	get() = node.isShowTickMarks
	set(value) {
	  node.isShowTickMarks = value
	}

  fun showTickMarksProperty() = node.showTickMarksProperty()


  var isShowTickLabels
	get() = node.isShowTickLabels
	set(value) {
	  node.isShowTickLabels = value
	}

  fun showTickLabelsProperty() = node.showTickLabelsProperty()


  var majorTickUnit
	get() = node.majorTickUnit
	set(value) {
	  node.majorTickUnit = value
	}

  fun majorTickUnitProperty() = node.majorTickUnitProperty()


  var minorTickCount
	get() = node.minorTickCount
	set(value) {
	  node.minorTickCount = value
	}

  fun minorTickCountProperty() = node.minorTickCountProperty()
}


open class MenuItemWrapper(
  override val node: MenuItem = MenuItem(),
  op: MenuItemWrapper.()->Unit = {}
): EventTargetWrapper<MenuItem> {

  constructor(text: String?, g: Node? = null): this(MenuItem(text, g))

  init {
	op()

  }

  fun visibleProperty() = node.visibleProperty()
  fun disableProperty() = node.disableProperty()

  var isMnemonicParsing
	get() = node.isMnemonicParsing
	set(value) {
	  node.isMnemonicParsing = value
	}

  fun mnemonicParsingProperty() = node.mnemonicParsingProperty()

  var onAction
	get() = node.onAction
	set(value) {
	  node.onAction = value
	}

  fun setOnAction(op: (ActionEvent)->Unit) {
	node.setOnAction(op)
  }

  var text
	get() = node.text
	set(value) {
	  node.text = value
	}

  fun textProperty() = node.textProperty()
  var graphic
	get() = node.graphic
	set(value) {
	  node.graphic = value
	}
  var accelerator
	get() = node.accelerator
	set(value) {
	  node.accelerator = value
	}
}


class CheckMenuItemWrapper(
  override val node: CheckMenuItem = CheckMenuItem(),
  op: CheckMenuItemWrapper.()->Unit = {}
): MenuItemWrapper(node) {
  companion object {
	fun CheckMenuItem.wrapped() = CheckMenuItemWrapper(this)
  }

  init {
	op()
  }

  var isSelected
	get() = node.isSelected
	set(value) {
	  node.isSelected = value
	}

  fun selectedProperty() = node.selectedProperty()
}


open class MenuButtonWrapper(
  override val node: MenuButton = MenuButton(),
  op: MenuButtonWrapper.()->Unit = {}
): ButtonBaseWrapper(node) {
  companion object {
	fun MenuButton.wrapped() = MenuButtonWrapper(this)
  }

  init {
	op()
  }
}


class SplitMenuButtonWrapper(
  override val node: SplitMenuButton = SplitMenuButton(),
  op: SplitMenuButtonWrapper.()->Unit = {}
): MenuButtonWrapper(node) {
  companion object {
	fun SplitMenuButton.wrapped() = SplitMenuButtonWrapper(this)
  }

  init {
	op()
  }
}


class CheckBoxWrapper(
  override val node: CheckBox = CheckBox(),
  op: CheckBoxWrapper.()->Unit = {}
): ButtonBaseWrapper(node) {
  companion object {
	fun CheckBox.wrapped() = CheckBoxWrapper(this)
  }

  constructor(text: String?): this(CheckBox(text))

  init {
	op()
  }

  var isSelected
	get() = node.isSelected
	set(value) {
	  node.isSelected = value
	}

  fun selectedProperty() = node.selectedProperty()
}


class ProgressIndicatorWrapper(
  override val node: ProgressIndicator = ProgressIndicator(),
  op: ProgressIndicatorWrapper.()->Unit = {}
): ControlWrapper(node) {
  companion object {
	fun ProgressIndicator.wrapped() = ProgressIndicatorWrapper(this)
  }

  init {
	op()
  }

  var progress
	get() = node.progress
	set(value) {
	  node.progress = value
	}

  fun progressProperty() = node.progressProperty()
}


class ButtonBarWrapper(
  override val node: ButtonBar = ButtonBar(),
  op: ButtonBarWrapper.()->Unit = {}
): ControlWrapper(node) {
  companion object {
	fun ButtonBar.wrapped() = ButtonBarWrapper(this)
  }

  init {
	op()
  }

  val buttons get() = node.buttons

  var buttonOrder
	get() = node.buttonOrder
	set(value) {
	  node.buttonOrder = value
	}

  //  fun setButtonData() = node.butt
}


class ProgressBarWrapper(
  override val node: ProgressBar = ProgressBar(),
  op: ProgressBarWrapper.()->Unit = {}
): ControlWrapper(node) {
  companion object {
	fun ProgressBar.wrapped() = ProgressBarWrapper(this)
  }

  init {
	op()
  }

  var progress
	get() = node.progress
	set(value) {
	  node.progress = value
	}

  fun progressProperty() = node.progressProperty()
}


class ToolBarWrapper(
  override val node: ToolBar = ToolBar(),
  op: ToolBarWrapper.()->Unit = {}
): ControlWrapper(node) {
  companion object {
	fun ToolBar.wrapped() = ToolBarWrapper(this)
  }

  init {
	op()
  }

  val items: ObservableList<Node> get() = node.items

}


open class ToggleButtonWrapper(
  override val node: ToggleButton = ToggleButton(),
  op: ToggleButtonWrapper.()->Unit = {}
): ButtonBaseWrapper(node) {
  companion object {
	fun ToggleButton.wrapped() = ToggleButtonWrapper(this)
  }

  init {
	op()
  }


  var isSelected
	get() = node.isSelected
	set(value) {
	  node.isSelected = value
	}


  //    var toggleGroup: ToggleGroup
  //  	get() = node.toggleGroup
  //  	set(value) {
  //  	  node.toggleGroup = value
  //  	}
}


class RadioButtonWrapper(
  override val node: RadioButton = RadioButton(),
  op: RadioButtonWrapper.()->Unit = {}
): ToggleButtonWrapper(node) {
  companion object {
	fun RadioButton.wrapped() = RadioButtonWrapper(this)
  }

  constructor(text: String): this(RadioButton(text))

  init {
	op()
  }
}


class HyperlinkWrapper(
  override val node: Hyperlink = Hyperlink(),
  op: HyperlinkWrapper.()->Unit = {}
): ButtonBaseWrapper(node) {
  companion object {
	fun Hyperlink.wrapped() = HyperlinkWrapper(this)
  }

  init {
	op()
  }
}


class ImageViewWrapper(
  override val node: ImageView = ImageView(),
  op: ImageViewWrapper.()->Unit = {}
): NodeWrapper<ImageView> {
  companion object {
	fun ImageView.wrapped() = ImageViewWrapper(this)
  }

  constructor(image: Image): this(ImageView(image))
  constructor(imageURL: String): this(ImageView(imageURL))

  init {
	op()
  }

  var image
	get() = node.image
	set(value) {
	  node.image = value
	}

  fun imageProperty() = node.imageProperty()


  var isPreserveRatio
	get() = node.isPreserveRatio
	set(value) {
	  node.isPreserveRatio = value
	}

  fun preserveRatioProperty() = node.preserveRatioProperty()


  var fitWidth
	get() = node.fitWidth
	set(value) {
	  node.fitWidth = value
	}

  fun fitWidthProperty() = node.fitWidthProperty()

  var fitHeight
	get() = node.fitHeight
	set(value) {
	  node.fitHeight = value
	}

  fun fitHeightProperty() = node.fitHeightProperty()


  var isSmooth
	get() = node.isSmooth
	set(value) {
	  node.isSmooth = value
	}

  fun smoothProperty() = node.smoothProperty()

}


open class LabelWrapper(
  override val node: Label = Label(),
  op: LabelWrapper.()->Unit = {}
): LabeledWrapper(node) {
  companion object {
	fun Label.wrapped() = LabelWrapper(this)
  }

  constructor(text: String?, graphic: Node? = null): this(Label(text, graphic))

  init {
	op()
  }


}


open class MenuBarWrapper(
  override val node: MenuBar = MenuBar(),
  op: MenuBarWrapper.()->Unit = {}
): ControlWrapper(node) {
  companion object {
	fun MenuBar.wrapped() = MenuBarWrapper(this)
  }

  val menus get() = node.menus

  init {
	op()
  }
}


open class ChartWrapper(override val node: Chart): RegionWrapper(node) {


  var title
	get() = node.title
	set(value) {
	  node.title = value
	}

  fun titleProperty() = node.titleProperty()

}


open class PieChartWrapper(
  override val node: PieChart = PieChart(),
  op: PieChartWrapper.()->Unit = {}
): ChartWrapper(node) {
  companion object {
	fun PieChart.wrapped() = PieChartWrapper(this)
  }

  constructor(data: ObservableList<PieChart.Data>): this(PieChart(data))

  init {
	op()
  }

  val data = node.data
}


open class LineChartWrapper<X, Y>(
  override val node: LineChart<X, Y>,
  op: LineChartWrapper<X, Y>.()->Unit = {}
): ChartWrapper(node) {
  companion object {
	fun <X, Y> LineChart<X, Y>.wrapped() = LineChartWrapper(this)
  }

  constructor(x: Axis<X>, y: Axis<Y>): this(LineChart(x, y))

  init {
	op()
  }
}


open class AreaChartWrapper<X, Y>(
  override val node: AreaChart<X, Y>,
  op: AreaChartWrapper<X, Y>.()->Unit = {}
): ChartWrapper(node) {
  companion object {
	fun <X, Y> AreaChart<X, Y>.wrapped() = AreaChartWrapper(this)
  }

  constructor(x: Axis<X>, y: Axis<Y>): this(AreaChart(x, y))

  init {
	op()
  }
}


open class BubbleChartWrapper<X, Y>(
  override val node: BubbleChart<X, Y>,
  op: BubbleChartWrapper<X, Y>.()->Unit = {}
): ChartWrapper(node) {
  companion object {
	fun <X, Y> BubbleChart<X, Y>.wrapped() = BubbleChartWrapper(this)
  }

  constructor(x: Axis<X>, y: Axis<Y>): this(BubbleChart(x, y))

  init {
	op()
  }
}


open class ScatterChartWrapper<X, Y>(
  override val node: ScatterChart<X, Y>,
  op: ScatterChartWrapper<X, Y>.()->Unit = {}
): ChartWrapper(node) {
  companion object {
	fun <X, Y> ScatterChart<X, Y>.wrapped() = ScatterChartWrapper(this)
  }

  constructor(x: Axis<X>, y: Axis<Y>): this(ScatterChart(x, y))

  init {
	op()
  }
}


open class BarChartWrapper<X, Y>(
  override val node: BarChart<X, Y>,
  op: BarChartWrapper<X, Y>.()->Unit = {}
): ChartWrapper(node) {
  companion object {
	fun <X, Y> BarChart<X, Y>.wrapped() = BarChartWrapper(this)
  }

  constructor(x: Axis<X>, y: Axis<Y>): this(BarChart(x, y))

  init {
	op()
  }
}


open class StackedBarChartWrapper<X, Y>(
  override val node: StackedBarChart<X, Y>,
  op: StackedBarChartWrapper<X, Y>.()->Unit = {}
): ChartWrapper(node) {
  companion object {
	fun <X, Y> StackedBarChart<X, Y>.wrapped() = StackedBarChartWrapper(this)
  }

  constructor(x: Axis<X>, y: Axis<Y>): this(StackedBarChart(x, y))

  init {
	op()
  }
}


open class RectangleWrapper(
  override val node: Rectangle = Rectangle(),
  op: RectangleWrapper.()->Unit = {}
): ShapeWrapper {
  companion object {
	fun Rectangle.wrapped() = RectangleWrapper(this)
  }

  constructor(x: Double, y: Double, width: Double, height: Double): this(Rectangle(x, y, width, height))
  constructor(width: Double, height: Double, fill: Paint): this(Rectangle(width, height, fill))
  constructor(width: Double, height: Double): this(Rectangle(width, height))

  var width
	get() = node.width
	set(value) {
	  node.width = value
	}
  val widthProperty = node.widthProperty()

  var height
	get() = node.height
	set(value) {
	  node.height = value
	}
  val heightProperty = node.heightProperty()


  init {
	op()
  }
}


open class ArcWrapper(
  override val node: Arc = Arc(),
  op: ArcWrapper.()->Unit = {}
): ShapeWrapper {
  companion object {
	fun Arc.wrapped() = ArcWrapper(this)
  }

  constructor(
	centerX: Double,
	centerY: Double,
	radiusX: Double,
	radiusY: Double,
	startAngle: Double,
	length: Double
  ): this(Arc(centerX, centerY, radiusX, radiusY, startAngle, length))

  init {
	op()
  }
}


open class CubicCurveWrapper(
  override val node: CubicCurve = CubicCurve(),
  op: CubicCurveWrapper.()->Unit = {}
): ShapeWrapper {
  companion object {
	fun CubicCurve.wrapped() = CubicCurveWrapper(this)
  }

  constructor(
	startX: Double,
	startY: Double,
	controlX1: Double,
	controlY1: Double,
	controlX2: Double,
	controlY2: Double,
	endX: Double,
	endY: Double
  ): this(CubicCurve(startX, startY, controlX1, controlY1, controlX2, controlY2, endX, endY))

  init {
	op()
  }
}


open class EllipseWrapper(
  override val node: Ellipse = Ellipse(),
  op: EllipseWrapper.()->Unit = {}
): ShapeWrapper {
  companion object {
	fun Ellipse.wrapped() = EllipseWrapper(this)
  }

  constructor(
	centerX: Double, centerY: Double, radiusX: Double, radiusY: Double
  ): this(Ellipse(centerX, centerY, radiusX, radiusY))


  init {
	op()
  }


  var radiusX
	get() = node.radiusX
	set(value) {
	  node.radiusX = value
	}

  fun radiusXProperty() = node.radiusXProperty()


  var radiusY
	get() = node.radiusY
	set(value) {
	  node.radiusY = value
	}

  fun radiusYProperty() = node.radiusYProperty()


}


open class LineWrapper(
  override val node: Line = Line(),
  op: LineWrapper.()->Unit = {}
): ShapeWrapper {
  companion object {
	fun Line.wrapped() = LineWrapper(this)
  }


  constructor(
	startX: Double, startY: Double, endX: Double, endY: Double
  ): this(Line(startX, startY, endX, endY))


  init {
	op()
  }


  var startX
	get() = node.startX
	set(value) {
	  node.startX = value
	}

  fun startXProperty() = node.startXProperty()
  var startY
	get() = node.startY
	set(value) {
	  node.startY = value
	}

  fun startYProperty() = node.startYProperty()
  var endX
	get() = node.endX
	set(value) {
	  node.endX = value
	}

  fun endXProperty() = node.endXProperty()
  var endY
	get() = node.endY
	set(value) {
	  node.endY = value
	}

  fun endYProperty() = node.endYProperty()


}


open class PathWrapper(
  override val node: Path = Path(),
  op: PathWrapper.()->Unit = {}
): ShapeWrapper {
  companion object {
	fun Path.wrapped() = PathWrapper(this)
  }

  constructor(
	vararg elements: PathElement
  ): this(Path(*elements))


  val elements get() = node.elements

  init {
	op()
  }
}


open class PolygonWrapper(
  override val node: Polygon = Polygon(),
  op: PolygonWrapper.()->Unit = {}
): ShapeWrapper {
  companion object {
	fun Polygon.wrapped() = PolygonWrapper(this)
  }

  constructor(
	vararg points: Double
  ): this(Polygon(*points))

  val points get() = node.points

  init {
	op()
  }
}


open class PolylineWrapper(
  override val node: Polyline = Polyline(),
  op: PolylineWrapper.()->Unit = {}
): ShapeWrapper {
  companion object {
	fun Polyline.wrapped() = PolylineWrapper(this)
  }

  constructor(
	vararg points: Double
  ): this(Polyline(*points))


  init {
	op()
  }
}


open class QuadCurveWrapper(
  override val node: QuadCurve = QuadCurve(),
  op: QuadCurveWrapper.()->Unit = {}
): ShapeWrapper {
  companion object {
	fun QuadCurve.wrapped() = QuadCurveWrapper(this)
  }

  constructor(
	startX: Double, startY: Double, controlX: Double, controlY: Double, endX: Double, endY: Double
  ): this(QuadCurve(startX, startY, controlX, controlY, endX, endY))


  init {
	op()
  }
}


open class CircleWrapper(
  override val node: Circle = Circle(),
  op: CircleWrapper.()->Unit = {}
): ShapeWrapper {
  companion object {
	fun Circle.wrapped() = CircleWrapper(this)
  }

  constructor(
	centerX: Double,
	centerY: Double,
	radius: Double,
  ): this(Circle(centerX, centerY, radius))

  constructor(radius: Double, fill: Paint): this(Circle(radius, fill))
  constructor(radius: Double): this(Circle(radius))

  init {
	op()
  }
}


open class SVGPathWrapper(
  override val node: SVGPath = SVGPath(),
  op: SVGPathWrapper.()->Unit = {}
): ShapeWrapper {
  companion object {
	fun SVGPath.wrapped() = SVGPathWrapper(this)
  }


  var content
	get() = node.content
	set(value) {
	  node.content = value
	}
  var fillRule
	get() = node.fillRule
	set(value) {
	  node.fillRule = value
	}


  init {
	op()
  }
}


open class ListViewWrapper<E>(
  override val node: ListView<E> = ListView<E>(),
  op: ListViewWrapper<E>.()->Unit = {}
): ControlWrapper(node) {
  companion object {
	fun <E> ListView<E>.wrapped() = ListViewWrapper<E>(this)
  }

  constructor(items: ObservableList<E>): this(ListView(items))

  init {
	op()
  }

  fun scrollTo(i: Int) = node.scrollTo(i)
  fun scrollTo(e: E) = node.scrollTo(e)

  var cellFactory
	get() = node.cellFactory
	set(value) {
	  node.cellFactory = value

	}

  fun cellFactoryProperty() = node.cellFactoryProperty()
  @JvmName("setCellFactory1") fun setCellFactory(value: Callback<ListView<E>, ListCell<E>>) = node.setCellFactory(value)

  var isEditable
	get() = node.isEditable
	set(value) {
	  node.isEditable = value
	}

  fun editableProperty() = node.editableProperty()

  var items
	get() = node.items
	set(value) {
	  node.items = value
	}

  fun itemsProperty() = node.itemsProperty()

  val selectionModel get() = node.selectionModel
  val selectedItem get() = selectionModel.selectedItem


}


open class TableViewWrapper<E>(
  override val node: TableView<E> = TableView<E>(),
  op: TableViewWrapper<E>.()->Unit = {}
): ControlWrapper(node) {
  companion object {
	fun <E> TableView<E>.wrapped() = TableViewWrapper<E>(this)
  }

  constructor(items: ObservableList<E>): this(TableView(items))



  init {
	op()
  }

  fun editableProperty() = node.editableProperty()

  val sortOrder get() = node.sortOrder

  var columnResizePolicy
	get() = node.columnResizePolicy
	set(value) {
	  node.columnResizePolicy = value
	}

  fun columnResizePolicyProperty() = node.columnResizePolicyProperty()

  var items
	get() = node.items
	set(value) {
	  node.items = value
	}

  fun itemsProperty() = node.itemsProperty()

  fun comparatorProperty() = node.comparatorProperty()

  val columns get() = node.columns

  val selectionModel get() = node.selectionModel
  val selectedItem get() = selectionModel.selectedItem
  fun scrollTo(i: Int) = node.scrollTo(i)
  fun scrollTo(e: E) = node.scrollTo(e)
  fun sort() = node.sort()
  fun editingCellProperty() = node.editingCellProperty()
}


open class SeparatorWrapper(
  override val node: Separator = Separator(),
  op: SeparatorWrapper.()->Unit = {}
): ControlWrapper(node) {
  companion object {
	fun Separator.wrapped() = SeparatorWrapper(this)
  }

  constructor(
	o: Orientation
  ): this(Separator(o))


  init {
	op()
  }


}

class GroupWrapper(override val node: Group = Group()): ParentWrapper {
  val children get() = node.children
}

class CanvasWrapper(override val node: Canvas = Canvas()): NodeWrapper<Canvas> {
  constructor(
	width: Double,
	height: Double
  ): this(Canvas(width, height))


  val width get() = widthProperty.value
  val widthProperty: ReadOnlyDoubleProperty get() = node.widthProperty()
  val height get() = heightProperty.value
  val heightProperty: ReadOnlyDoubleProperty get() = node.heightProperty()


  val graphicsContext2D get() = node.graphicsContext2D

}

class AccordionWrapper(override val node: Accordion = Accordion()): ControlWrapper(node) {
  val panes get() = node.panes
}

class PaginationWrapper(override val node: Pagination = Pagination()): ControlWrapper(node) {
  var pageCount
	get() = node.pageCount
	set(value) {
	  node.pageCount = value
	}
  var currentPageIndex
	get() = node.currentPageIndex
	set(value) {
	  node.currentPageIndex = value
	}
}

class TabWrapper(override val node: Tab = Tab()): EventTargetWrapper<Tab> {
  constructor(text: String?, content: Node?): this(Tab(text, content))

  var isClosable
	get() = node.isClosable
	set(value) {
	  node.isClosable = value
	}
}