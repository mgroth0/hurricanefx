@file:Suppress("UNUSED_EXPRESSION")

package matt.hurricanefx.wrapper

import javafx.beans.property.DoubleProperty
import javafx.beans.property.ObjectProperty
import javafx.beans.property.ReadOnlyDoubleProperty
import javafx.beans.property.ReadOnlyObjectProperty
import javafx.collections.ObservableList
import javafx.event.ActionEvent
import javafx.event.EventTarget
import javafx.geometry.Orientation
import javafx.geometry.Pos
import javafx.scene.Group
import javafx.scene.Node
import javafx.scene.Parent
import javafx.scene.Scene
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
import javafx.scene.control.TabPane
import javafx.scene.control.TableView
import javafx.scene.control.TextArea
import javafx.scene.control.TextField
import javafx.scene.control.TextInputControl
import javafx.scene.control.TitledPane
import javafx.scene.control.ToggleButton
import javafx.scene.control.ToolBar
import javafx.scene.control.TreeItem
import javafx.scene.control.TreeTableView
import javafx.scene.control.TreeView
import javafx.scene.image.ImageView
import javafx.scene.input.DataFormat
import javafx.scene.input.KeyEvent
import javafx.scene.input.MouseEvent
import javafx.scene.input.ScrollEvent
import javafx.scene.input.TransferMode.ANY
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.Background
import javafx.scene.layout.Border
import javafx.scene.layout.BorderPane
import javafx.scene.layout.FlowPane
import javafx.scene.layout.GridPane
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import javafx.scene.layout.Region
import javafx.scene.layout.StackPane
import javafx.scene.layout.TilePane
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
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
import javafx.util.Callback
import matt.file.MFile
import matt.file.toMFile
import matt.hurricanefx.addAll
import matt.hurricanefx.eye.lib.onChange
import matt.hurricanefx.stage
import matt.hurricanefx.tornadofx.nodes.add
import matt.hurricanefx.tornadofx.nodes.getToggleGroup
import java.time.LocalDate

private typealias NW = NodeWrapper<*>

@DslMarker
annotation class FXNodeWrapperDSL

@FXNodeWrapperDSL
interface EventTargetWrapper<N: EventTarget> {
  val node: N
  fun getToggleGroup() = node.getToggleGroup()
}

fun EventTarget.wrapped() = object: EventTargetWrapper<EventTarget> {
  override val node = this@wrapped
}


interface NodeWrapper<N: Node>: EventTargetWrapper<N> {
  val scene: Scene? get() = node.scene
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

  fun setOnScroll(listener: (ScrollEvent)->Unit) {
	node.setOnScroll(listener)
  }

  fun managedProperty() = node.managedProperty()
  fun visibleProperty() = node.visibleProperty()
  fun disableProperty() = node.disableProperty()
  fun focusedProperty() = node.focusedProperty()

}

fun Node.wrapped() = object: NodeWrapper<Node> {
  override val node = this@wrapped
}


interface ParentWrapper: NodeWrapper<Parent> {
  override val node: Parent
}

fun Parent.wrapped() = object: ParentWrapper {
  override val node = this@wrapped
}


open class RegionWrapper(override val node: Region = Region()): ParentWrapper {

  companion object {
	fun Region.wrapped() = RegionWrapper(this)
  }

  var border: Border?
	get() = node.border
	set(value) {
	  node.border = value
	}

  val borderProperty: ObjectProperty<Border> get() = node.borderProperty()

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
  init {
	op()
  }
}


class HBoxWrapper(node: HBox = HBox(), op: HBoxWrapper.()->Unit = {}): BoxWrapper<HBox>(node) {
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


open class StackPaneWrapper(override val node: StackPane = StackPane()): PaneWrapper(node)


open class AnchorPaneWrapper(override val node: AnchorPane = AnchorPane()): PaneWrapper(node)


open class BorderPaneWrapper(override val node: BorderPane = BorderPane()): PaneWrapper(node) {
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
}


open class SplitPaneWrapper(override val node: SplitPane = SplitPane()): ControlWrapper(node) {
  var orientation
	get() = node.orientation
	set(value) {
	  node.orientation = value
	}
  val items get() = node.items
}


open class GridPaneWrapper(override val node: GridPane = GridPane()): PaneWrapper(node)


open class TilePaneWrapper(override val node: TilePane = TilePane()): PaneWrapper(node)


open class FlowPaneWrapper(override val node: FlowPane = FlowPane()): PaneWrapper(node)


open class ScrollPaneWrapper(override val node: ScrollPane = ScrollPane()): ControlWrapper(node) {
  var isFitToWidth
	get() = node.isFitToWidth
	set(value) {
	  node.isFitToWidth = value
	}
  var isFitToHeight
	get() = node.isFitToHeight
	set(value) {
	  node.isFitToHeight = value
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


}


class ChoiceBoxWrapper<T>(
  override val node: ChoiceBox<T> = ChoiceBox(),
  op: ChoiceBoxWrapper<T>.()->Unit = {}
): ControlWrapper(node) {
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


class TabPaneWrapper(
  override val node: TabPane = TabPane(),
  op: TabPaneWrapper.()->Unit = {}
): ControlWrapper(node) {
  init {
	op()
  }

  val tabs get() = node.tabs
}


class ButtonBaseWrapper(override val node: ButtonBase): LabeledWrapper(node) {

  fun fire() = node.fire()
  fun setOnAction(op: (ActionEvent)->Unit) {
	node.setOnAction(op)
  }
}


abstract class ControlWrapper(override val node: Control): RegionWrapper(node)


class ButtonWrapper(
  override val node: Button = Button(),
  op: ButtonWrapper.()->Unit = {}
): ButtonBaseWrapper {
  companion object {
	fun Button.wrapped() = ButtonWrapper(this)
  }

  init {
	op()
  }
}


class ComboBoxBaseWrapper<T>(override val node: ComboBoxBase<T>): ControlWrapper(node) {

  var value
	get() = node.value
	set(theVal) {
	  node.value = theVal
	}

  fun valueProperty() = node.valueProperty()


}


class ComboBoxWrapper<T>(
  override val node: ComboBox<T> = ComboBox<T>(),
  op: ComboBoxWrapper<T>.()->Unit = {}
): ComboBoxBaseWrapper<T>(node) {
  companion object {
	fun <T> ComboBox<T>.wrapped() = ComboBoxWrapper(this)
  }

  init {
	op()
  }


  var items
	get() = node.items
	set(value) {
	  node.items = value
	}

  fun itemsProperty() = node.itemsProperty()

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


class TextFlowWrapper(
  override val node: TextFlow = TextFlow(),
  op: TextFlowWrapper.()->Unit = {}
): RegionWrapper(node) {
  companion object {
	fun TextFlow.wrapped() = TextFlowWrapper(this)
  }

  init {
	op()
  }

}


interface ShapeWrapper: NodeWrapper<Shape> {
  override val node: Shape
}


class TextWrapper(
  override val node: Text = Text(),
  op: TextWrapper.()->Unit = {}
): ShapeWrapper {
  companion object {
	fun Text.wrapped() = TextWrapper(this)
  }

  init {
	op()
  }

  var text
	get() = node.text
	set(value) {
	  node.text = value
	}

  fun textProperty() = node.textProperty()
}


class TextInputControlWrapper(override val node: TextInputControl): ControlWrapper(node) {
  var text
	get() = node.text
	set(value) {
	  node.text = value
	}

  fun textProperty() = node.textProperty()
}


open class TextFieldWrapper(
  override val node: TextField = TextField(),
  op: TextFieldWrapper.()->Unit = {}
): TextInputControlWrapper(node) {
  companion object {
	fun TextField.wrapped() = TextFieldWrapper(this)
  }

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


class TextAreaWrapper(
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


class LabeledWrapper(override val node: Labeled): ControlWrapper(node) {

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
): ControlWrapper {
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
}


open class MenuItemWrapper(
  override val node: MenuItem = MenuItem(),
  op: MenuItemWrapper.()->Unit = {}
): EventTargetWrapper<MenuItem> {

  init {
	op()
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
): ButtonBaseWrapper {
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
): ButtonBaseWrapper {
  companion object {
	fun CheckBox.wrapped() = CheckBoxWrapper(this)
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


  var buttonOrder
	get() = node.buttonOrder
	set(value) {
	  node.buttonOrder = value
	}
}


class ProgressBarWrapper(
  override val node: ProgressBar = ProgressBar(),
  op: ProgressBarWrapper.()->Unit = {}
): ControlWrapper {
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
): ButtonBaseWrapper {
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

  init {
	op()
  }
}


class HyperlinkWrapper(
  override val node: Hyperlink = Hyperlink(),
  op: HyperlinkWrapper.()->Unit = {}
): ButtonBaseWrapper {
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

  init {
	op()
  }

  var image
	get() = node.image
	set(value) {
	  node.image = value
	}

  fun imageProperty() = node.imageProperty()

}


class LabelWrapper(
  override val node: Label = Label(),
  op: LabelWrapper.()->Unit = {}
): LabeledWrapper(node) {
  companion object {
	fun Label.wrapped() = LabelWrapper(this)
  }

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

  init {
	op()
  }
}


class ChartWrapper(override val node: Chart): RegionWrapper(node) {


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
): ChartWrapper {
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
): ChartWrapper {
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
): ChartWrapper {
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
): ChartWrapper {
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
): ChartWrapper {
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
): ChartWrapper {
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
): ChartWrapper {
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
  var startY
	get() = node.startY
	set(value) {
	  node.startY = value
	}
  var endX
	get() = node.endX
	set(value) {
	  node.endX = value
	}
  var endY
	get() = node.endY
	set(value) {
	  node.endY = value
	}


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

  init {
	op()
  }

  var items
	get() = node.items
	set(value) {
	  node.items = value
	}

  fun itemsProperty() = node.itemsProperty()
}


open class TableViewWrapper<E>(
  override val node: TableView<E> = TableView<E>(),
  op: TableViewWrapper<E>.()->Unit = {}
): ControlWrapper(node) {
  companion object {
	fun <E> TableView<E>.wrapped() = TableViewWrapper<E>(this)
  }

  init {
	op()
  }


  var items
	get() = node.items
	set(value) {
	  node.items = value
	}

  fun itemsProperty() = node.itemsProperty()

  fun comparatorProperty() = node.comparatorProperty()

  val columns get() = node.columns
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

class GroupWrapper(override val node: Group = Group()): ParentWrapper

class CanvasWrapper(override val node: Canvas = Canvas()): NodeWrapper<Canvas> {
  constructor(
	width: Double,
	height: Double
  ): this(Canvas(width, height))

}

class AccordionWrapper(override val node: Accordion = Accordion()): ControlWrapper(node) {
  val panes get() = node.panes
}

class PaginationWrapper(override val node: Pagination = Pagination()): ControlWrapper(node)
