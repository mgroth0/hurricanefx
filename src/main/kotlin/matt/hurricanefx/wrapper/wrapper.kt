package matt.hurricanefx.wrapper

import javafx.beans.property.DoubleProperty
import javafx.beans.property.ObjectProperty
import javafx.beans.property.ReadOnlyDoubleProperty
import javafx.beans.property.ReadOnlyObjectProperty
import javafx.collections.ObservableList
import javafx.event.EventTarget
import javafx.geometry.Pos
import javafx.scene.Node
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.ButtonBar
import javafx.scene.control.ButtonBase
import javafx.scene.control.CheckBox
import javafx.scene.control.CheckMenuItem
import javafx.scene.control.ChoiceBox
import javafx.scene.control.ColorPicker
import javafx.scene.control.ComboBoxBase
import javafx.scene.control.Control
import javafx.scene.control.DatePicker
import javafx.scene.control.Hyperlink
import javafx.scene.control.Labeled
import javafx.scene.control.MenuButton
import javafx.scene.control.MenuItem
import javafx.scene.control.MultipleSelectionModel
import javafx.scene.control.PasswordField
import javafx.scene.control.ProgressBar
import javafx.scene.control.ProgressIndicator
import javafx.scene.control.RadioButton
import javafx.scene.control.ScrollPane
import javafx.scene.control.Slider
import javafx.scene.control.Spinner
import javafx.scene.control.SplitMenuButton
import javafx.scene.control.TabPane
import javafx.scene.control.TextArea
import javafx.scene.control.TextField
import javafx.scene.control.TextInputControl
import javafx.scene.control.TitledPane
import javafx.scene.control.ToggleButton
import javafx.scene.control.ToggleGroup
import javafx.scene.control.ToolBar
import javafx.scene.control.TreeItem
import javafx.scene.control.TreeTableView
import javafx.scene.control.TreeView
import javafx.scene.image.ImageView
import javafx.scene.input.DataFormat
import javafx.scene.input.KeyEvent
import javafx.scene.input.MouseEvent
import javafx.scene.input.TransferMode.ANY
import javafx.scene.layout.Background
import javafx.scene.layout.Border
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import javafx.scene.layout.Region
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
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

@FXNodeWrapperDSL
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

  fun managedProperty() = node.managedProperty()
  fun visibleProperty() = node.visibleProperty()
  fun disableProperty() = node.disableProperty()

}

fun Node.wrapped() = object: NodeWrapper<Node> {
  override val node = this@wrapped
}

@FXNodeWrapperDSL
interface ParentWrapper: NodeWrapper<Parent> {
  override val node: Parent
}

fun Parent.wrapped() = object: ParentWrapper {
  override val node = this@wrapped
}

@FXNodeWrapperDSL
interface RegionWrapper: ParentWrapper {
  override val node: Region

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


fun Region.wrapped() = object: RegionWrapper {
  override val node = this@wrapped
}

@FXNodeWrapperDSL
interface PaneWrapper: RegionWrapper {
  override val node: Pane
  operator fun Collection<Node>.unaryPlus() {
	node.addAll(this)
  }


  val children: ObservableList<Node> get() = node.children
}

fun Pane.wrapped() = object: PaneWrapper {
  override val node = this@wrapped
}

@FXNodeWrapperDSL
interface BoxWrapper<N: Pane>: PaneWrapper {

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

@FXNodeWrapperDSL
open class VBoxWrapper(override val node: VBox = VBox(), op: VBoxWrapper.()->Unit = {}): BoxWrapper<VBox> {
  init {
	op()
  }
}

@FXNodeWrapperDSL
class HBoxWrapper(override val node: HBox = HBox(), op: HBoxWrapper.()->Unit = {}): BoxWrapper<HBox> {
  init {
	op()
  }
}


@FXNodeWrapperDSL
class ScrollingVBoxWrapper(vbox: VBox = VBox(), op: ScrollingVBoxWrapper.()->Unit = {}): NodeWrapper<ScrollPane> {
  constructor(vbox: VBoxWrapper, op: ScrollingVBoxWrapper.()->Unit = {}): this(vbox.node, op)

  override val node = ScrollPane(VBox())

  init {
	op()
	node.content = vbox
  }
}

@FXNodeWrapperDSL
sealed interface TreeLikeWrapper<N: Region, T>: RegionWrapper {
  var root: TreeItem<T>
  var isShowRoot: Boolean
  fun setOnSelectionChange(listener: (TreeItem<T>?)->Unit)
  val selectionModel: MultipleSelectionModel<TreeItem<T>>
  val selectedItem: TreeItem<T>? get() = selectionModel.selectedItem
  val selectedItemProperty: ReadOnlyObjectProperty<TreeItem<T>> get() = selectionModel.selectedItemProperty()
  val selectedValue: T? get() = selectedItem?.value
  fun scrollTo(i: Int)
  fun getRow(ti: TreeItem<T>): Int
}

@FXNodeWrapperDSL
class TreeViewWrapper<T>(override val node: TreeView<T> = TreeView(), op: TreeViewWrapper<T>.()->Unit = {}):
  TreeLikeWrapper<TreeView<T>, T> {
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

@FXNodeWrapperDSL
class TreeTableViewWrapper<T>(
  override val node: TreeTableView<T> = TreeTableView(),
  op: TreeTableViewWrapper<T>.()->Unit = {}
): TreeLikeWrapper<TreeTableView<T>, T> {
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


@FXNodeWrapperDSL
class ChoiceBoxWrapper<T>(
  override val node: ChoiceBox<T> = ChoiceBox(),
  op: ChoiceBoxWrapper<T>.()->Unit = {}
): RegionWrapper {
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

  init {
	op()
  }
}


@FXNodeWrapperDSL
class TabPaneWrapper(
  override val node: TabPane = TabPane(),
  op: TabPaneWrapper.()->Unit = {}
): RegionWrapper {
  init {
	op()
  }
}


@FXNodeWrapperDSL
interface ButtonBaseWrapper: LabeledWrapper {
  override val node: ButtonBase
  fun fire() = node.fire()
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


@FXNodeWrapperDSL
interface ControlWrapper: RegionWrapper {
  override val node: Control
}

@FXNodeWrapperDSL
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


@FXNodeWrapperDSL
interface ComboBoxBaseWrapper<T>: ControlWrapper {
  override val node: ComboBoxBase<T>
  var value
	get() = node.value
	set(theVal) {
	  node.value = theVal
	}

  fun valueProperty() = node.valueProperty()
}


@FXNodeWrapperDSL
class ColorPickerWrapper(
  override val node: ColorPicker = ColorPicker(),
  op: ColorPickerWrapper.()->Unit = {}
): ComboBoxBaseWrapper<Color> {
  companion object {
	fun ColorPicker.wrapped() = ColorPickerWrapper(this)
  }

  init {
	op()
  }
}


@FXNodeWrapperDSL
class DatePickerWrapper(
  override val node: DatePicker = DatePicker(),
  op: DatePickerWrapper.()->Unit = {}
): ComboBoxBaseWrapper<LocalDate> {

  companion object {
	fun DatePicker.wrapped() = DatePickerWrapper(this)
  }

  init {
	op()
  }


}


@FXNodeWrapperDSL
class TextFlowWrapper(
  override val node: TextFlow = TextFlow(),
  op: TextFlowWrapper.()->Unit = {}
): RegionWrapper {
  companion object {
	fun TextFlow.wrapped() = TextFlowWrapper(this)
  }

  init {
	op()
  }

}


@FXNodeWrapperDSL
interface ShapeWrapper: NodeWrapper<Shape> {
  override val node: Shape
}


@FXNodeWrapperDSL
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


@FXNodeWrapperDSL
interface TextInputControlWrapper: ControlWrapper {
  override val node: TextInputControl

  var text
	get() = node.text
	set(value) {
	  node.text = value
	}

  fun textProperty() = node.textProperty()
}


@FXNodeWrapperDSL
open class TextFieldWrapper(
  override val node: TextField = TextField(),
  op: TextFieldWrapper.()->Unit = {}
): TextInputControlWrapper {
  companion object {
	fun TextField.wrapped() = TextFieldWrapper(this)
  }

  init {
	op()
  }

}


@FXNodeWrapperDSL
class PasswordFieldWrapper(
  override val node: PasswordField = PasswordField(),
  op: PasswordFieldWrapper.()->Unit = {}
): TextFieldWrapper(node) {
  init {
	op()
  }
}

fun PasswordField.wrapped() = PasswordFieldWrapper(this)


@FXNodeWrapperDSL
class TextAreaWrapper(
  override val node: TextArea = TextArea(),
  op: TextAreaWrapper.()->Unit = {}
): TextInputControlWrapper {
  companion object {
	fun TextArea.wrapped() = TextAreaWrapper(this)
  }

  init {
	op()
  }

}


@FXNodeWrapperDSL
interface LabeledWrapper: ControlWrapper {
  override val node: Labeled
}


@FXNodeWrapperDSL
class TitledPaneWrapper(
  override val node: TitledPane = TitledPane(),
  op: TitledPaneWrapper.()->Unit = {}
): LabeledWrapper {
  companion object {
	fun TitledPane.wrapped() = TitledPaneWrapper(this)
  }

  init {
	op()
  }
}


@FXNodeWrapperDSL
class SpinnerWrapper<T>(
  override val node: Spinner<T> = Spinner<T>(),
  op: SpinnerWrapper<T>.()->Unit = {}
): ControlWrapper {
  companion object {
	fun <T> Spinner<T>.wrapped() = SpinnerWrapper<T>(this)
  }

  init {
	op()
  }

  var valueFactory
	get() = node.valueFactory
	set(value) {
	  node.valueFactory = value
	}
}

@FXNodeWrapperDSL
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
}


@FXNodeWrapperDSL
interface MenuItemWrapper: EventTargetWrapper<MenuItem> {
  override val node: MenuItem
}

@FXNodeWrapperDSL
class CheckMenuItemWrapper(
  override val node: CheckMenuItem = CheckMenuItem(),
  op: CheckMenuItemWrapper.()->Unit = {}
): MenuItemWrapper {
  companion object {
	fun CheckMenuItem.wrapped() = CheckMenuItemWrapper(this)
  }

  init {
	op()
  }
}


@FXNodeWrapperDSL
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

@FXNodeWrapperDSL
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


@FXNodeWrapperDSL
class CheckBoxWrapper(
  override val node: CheckBox = CheckBox(),
  op: CheckBoxWrapper.()->Unit = {}
): ControlWrapper {
  companion object {
	fun CheckBox.wrapped() = CheckBoxWrapper(this)
  }

  init {
	op()
  }
}


@FXNodeWrapperDSL
class ProgressIndicatorWrapper(
  override val node: ProgressIndicator = ProgressIndicator(),
  op: ProgressIndicatorWrapper.()->Unit = {}
): ControlWrapper {
  companion object {
	fun ProgressIndicator.wrapped() = ProgressIndicatorWrapper(this)
  }

  init {
	op()
  }
}


@FXNodeWrapperDSL
class ButtonBarWrapper(
  override val node: ButtonBar = ButtonBar(),
  op: ButtonBarWrapper.()->Unit = {}
): ControlWrapper {
  companion object {
	fun ButtonBar.wrapped() = ButtonBarWrapper(this)
  }

  init {
	op()
  }
}


@FXNodeWrapperDSL
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
}


@FXNodeWrapperDSL
class ToolBarWrapper(
  override val node: ToolBar = ToolBar(),
  op: ToolBarWrapper.()->Unit = {}
): ControlWrapper {
  companion object {
	fun ToolBar.wrapped() = ToolBarWrapper(this)
  }

  init {
	op()
  }

  val items: ObservableList<Node> get() = node.items

}

@FXNodeWrapperDSL
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


  var toggleGroup: ToggleGroup
	get() = node.toggleGroup
	set(value) {
	  node.toggleGroup = value
	}
}



@FXNodeWrapperDSL
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



@FXNodeWrapperDSL
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


@FXNodeWrapperDSL
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
}