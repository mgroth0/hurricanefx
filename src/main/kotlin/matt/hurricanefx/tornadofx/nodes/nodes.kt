@file:Suppress("UNCHECKED_CAST")

/*slightly modified code I stole from tornadofx*/

package matt.hurricanefx.tornadofx.nodes

import javafx.application.Platform
import javafx.beans.binding.BooleanBinding
import javafx.beans.property.DoubleProperty
import javafx.beans.property.Property
import javafx.beans.value.ObservableValue
import javafx.collections.ListChangeListener
import javafx.collections.ObservableList
import javafx.event.EventTarget
import javafx.geometry.HPos
import javafx.geometry.Insets
import javafx.geometry.Point2D
import javafx.geometry.Point3D
import javafx.geometry.Pos
import javafx.geometry.VPos
import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.control.ComboBox
import javafx.scene.control.ComboBoxBase
import javafx.scene.control.Control
import javafx.scene.control.ListCell
import javafx.scene.control.ListView
import javafx.scene.control.SplitPane
import javafx.scene.control.Tab
import javafx.scene.control.TableCell
import javafx.scene.control.TableColumn
import javafx.scene.control.TableRow
import javafx.scene.control.TableView
import javafx.scene.control.TextInputControl
import javafx.scene.control.ToggleGroup
import javafx.scene.control.ToolBar
import javafx.scene.control.Tooltip
import javafx.scene.control.TreeItem
import javafx.scene.control.TreeTableCell
import javafx.scene.control.TreeTableColumn
import javafx.scene.control.TreeTableRow
import javafx.scene.control.TreeTableView
import javafx.scene.control.cell.TextFieldTableCell
import javafx.scene.control.skin.TableColumnHeader
import javafx.scene.input.InputEvent
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.BorderPane
import javafx.scene.layout.GridPane
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import javafx.scene.layout.Priority
import javafx.scene.layout.Region
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import javafx.util.Callback
import javafx.util.StringConverter
import matt.hurricanefx.eye.bind.toBinding
import matt.hurricanefx.eye.lib.onChange
import matt.hurricanefx.eye.lib.proxypropDouble
import matt.hurricanefx.eye.prop.booleanBinding
import matt.hurricanefx.eye.prop.cleanBind
import matt.hurricanefx.tornadofx.control.properties
import matt.hurricanefx.tornadofx.fx.addChildIfPossible
import matt.hurricanefx.tornadofx.fx.getChildList
import matt.hurricanefx.wrapper.ComboBoxBaseWrapper
import matt.hurricanefx.wrapper.ComboBoxWrapper
import matt.hurricanefx.wrapper.EventTargetWrapper
import matt.hurricanefx.wrapper.ListViewWrapper
import matt.hurricanefx.wrapper.MenuItemWrapper
import matt.hurricanefx.wrapper.NodeW
import matt.hurricanefx.wrapper.NodeWrapper
import matt.hurricanefx.wrapper.PaneWrapper
import matt.hurricanefx.wrapper.RegionWrapper
import matt.hurricanefx.wrapper.SceneWrapper
import matt.hurricanefx.wrapper.TableViewWrapper
import matt.hurricanefx.wrapper.TextInputControlWrapper
import matt.hurricanefx.wrapper.TreeTableViewWrapper

fun EventTarget.getToggleGroup(): ToggleGroup? = properties["tornadofx.togglegroup"] as ToggleGroup?

fun NodeW.tooltip(text: String? = null, graphic: Node? = null, op: Tooltip.()->Unit = {}): Tooltip {
  val newToolTip = Tooltip(text)
  graphic?.apply { newToolTip.graphic = this }
  newToolTip.op()
  if (this is Control) tooltip = newToolTip else Tooltip.install(this.node, newToolTip)
  return newToolTip
}

fun SceneWrapper.reloadStylesheets() {
  val styles = stylesheets.toMutableList()
  stylesheets.clear()
  styles.forEachIndexed { i, s ->
	if (s.startsWith("css://")) {
	  val b = StringBuilder()
	  val queryPairs = mutableListOf<String>()

	  if (s.contains("?")) {
		val urlAndQuery = s.split(Regex("\\?"), 2)
		b.append(urlAndQuery[0])
		val query = urlAndQuery[1]

		val pairs = query.split("&")
		pairs.filterNot { s.startsWith("squash=") }.forEach { queryPairs.add(it) }
	  } else {
		b.append(s)
	  }

	  queryPairs.add("squash=${System.currentTimeMillis()}")
	  b.append("?").append(queryPairs.joinToString("&"))
	  styles[i] = b.toString()
	}
  }
  stylesheets.addAll(styles)
}


infix fun NodeWrapper<*>.addTo(pane: EventTargetWrapper<*>) = pane.addChildIfPossible(this)

fun EventTarget.replaceChildren(vararg node: Node) {
  val children = requireNotNull(getChildList()) { "This node doesn't have a child list" }
  children.clear()
  children.addAll(node)
}

operator fun EventTargetWrapper<*>.plusAssign(node: NodeWrapper<*>) {
  addChildIfPossible(node)
}

fun PaneWrapper.clear() = children.clear()

fun <T: EventTarget> T.replaceChildren(op: T.()->Unit) {
  getChildList()?.clear()
  op(this)
}

fun EventTargetWrapper<*>.add(nw: NodeWrapper<*>) = plusAssign(nw)


var RegionWrapper.useMaxWidth: Boolean
  get() = maxWidth == Double.MAX_VALUE
  set(value) = if (value) maxWidth = Double.MAX_VALUE else Unit

var RegionWrapper.useMaxHeight: Boolean
  get() = maxHeight == Double.MAX_VALUE
  set(value) = if (value) maxHeight = Double.MAX_VALUE else Unit

var RegionWrapper.useMaxSize: Boolean
  get() = maxWidth == Double.MAX_VALUE && maxHeight == Double.MAX_VALUE
  set(value) = if (value) {
	useMaxWidth = true; useMaxHeight = true
  } else Unit

var RegionWrapper.usePrefWidth: Boolean
  get() = width == prefWidth
  set(value) = if (value) run {
	minWidth = (Region.USE_PREF_SIZE)
  } else Unit

var RegionWrapper.usePrefHeight: Boolean
  get() = height == prefHeight
  set(value) = if (value) run {minHeight = (Region.USE_PREF_SIZE) } else Unit

var RegionWrapper.usePrefSize: Boolean
  get() = maxWidth == Double.MAX_VALUE && maxHeight == Double.MAX_VALUE
  set(value) = if (value) setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE) else Unit

fun point(x: Number, y: Number) = Point2D(x.toDouble(), y.toDouble())
fun point(x: Number, y: Number, z: Number) = Point3D(x.toDouble(), y.toDouble(), z.toDouble())
infix fun Number.xy(y: Number) = Point2D(toDouble(), y.toDouble())

fun <T> TableViewWrapper<T>.selectWhere(scrollTo: Boolean = true, condition: (T)->Boolean) {
  items.asSequence().filter(condition).forEach {
	selectionModel.select(it)
	if (scrollTo) scrollTo(it)
  }
}


fun <T> ListViewWrapper<T>.selectWhere(scrollTo: Boolean = true, condition: (T)->Boolean) {
  items.asSequence().filter(condition).forEach {
	selectionModel.select(it)
	if (scrollTo) scrollTo(it)
  }
}

fun <T> TableViewWrapper<T>.moveToTopWhere(
  backingList: ObservableList<T> = items,
  select: Boolean = true,
  predicate: (T)->Boolean
) {
  if (select) selectionModel.clearSelection()
  backingList.filter(predicate).forEach {
	backingList.remove(it)
	backingList.add(0, it)
	if (select) selectionModel.select(it)
  }
}

fun <T> TableViewWrapper<T>.moveToBottomWhere(
  backingList: ObservableList<T> = items,
  select: Boolean = true,
  predicate: (T)->Boolean
) {
  val end = backingList.size - 1
  if (select) selectionModel.clearSelection()
  backingList.filter(predicate).forEach {
	backingList.remove(it)
	backingList.add(end, it)
	if (select) selectionModel.select(it)

  }
}

fun <T> TableViewWrapper<T>.selectFirst() = selectionModel.selectFirst()

fun <T> TreeTableViewWrapper<T>.selectFirst() = selectionModel.selectFirst()

val <T> ComboBoxWrapper<T>.selectedItem: T?
  get() = selectionModel.selectedItem

fun <S> TableViewWrapper<S>.onSelectionChange(func: (S?)->Unit) =
  selectionModel.selectedItemProperty().addListener({ _, _, newValue -> func(newValue) })


fun <T> TreeTableViewWrapper<T>.bindSelected(property: Property<T>) {
  selectionModel.selectedItemProperty().onChange {
	property.value = it?.value
  }
}

class TableColumnCellCache<T>(private val cacheProvider: (T)->Node) {
  private val store = mutableMapOf<T, Node>()
  fun getOrCreateNode(value: T) = store.getOrPut(value) { cacheProvider(value) }
}

fun <S, T> TableColumn<S, T>.cellDecorator(decorator: TableCell<S, T>.(T)->Unit) {
  val originalFactory = cellFactory

  cellFactory = Callback { column: TableColumn<S, T> ->
	val cell = originalFactory.call(column)
	cell.itemProperty().addListener { _, _, newValue ->
	  if (newValue != null) decorator(cell, newValue)
	}
	cell
  }
}

fun <S, T> TreeTableColumn<S, T>.cellFormat(formatter: (TreeTableCell<S, T>.(T)->Unit)) {
  cellFactory = Callback { _: TreeTableColumn<S, T> ->
	object: TreeTableCell<S, T>() {
	  private val defaultStyle = style

	  // technically defined as TreeTableCell.DEFAULT_STYLE_CLASS = "tree-table-cell", but this is private
	  private val defaultStyleClass = listOf(*styleClass.toTypedArray())

	  override fun updateItem(item: T, empty: Boolean) {
		super.updateItem(item, empty)

		if (item == null || empty) {
		  text = null
		  graphic = null
		  style = defaultStyle
		  styleClass.setAll(defaultStyleClass)
		} else {
		  formatter(this, item)
		}
	  }
	}
  }
}

enum class EditEventType(val editing: Boolean) {
  StartEdit(true), CommitEdit(false), CancelEdit(false)
}

/**
 * Execute action when the enter key is pressed or the mouse is clicked

 * @param clickCount The number of mouse clicks to trigger the action
 * *
 * @param action The action to execute on select
 */
fun <T> TableViewWrapper<T>.onUserSelect(clickCount: Int = 2, action: (T)->Unit) {
  val isSelected = { event: InputEvent ->
	event.target.isInsideRow() && !selectionModel.isEmpty
  }

  addEventFilter(MouseEvent.MOUSE_CLICKED) { event ->
	if (event.clickCount == clickCount && isSelected(event))
	  action(selectedItem!!)
  }

  addEventFilter(KeyEvent.KEY_PRESSED) { event ->
	if (event.code == KeyCode.ENTER && !event.isMetaDown && isSelected(event))
	  action(selectedItem!!)
  }
}

fun Node.setOnDoubleClick(filter: Boolean = false, action: (MouseEvent)->Unit) {
  if (filter) {
	addEventFilter(MouseEvent.MOUSE_CLICKED) {
	  if (it.clickCount == 2)
		action(it)
	}
  } else {
	setOnMouseClicked {
	  if (it.clickCount == 2)
		action(it)
	}
  }

}

fun NodeWrapper<*>.setOnDoubleClick(filter: Boolean = false, action: (MouseEvent)->Unit) =
  node.setOnDoubleClick(filter, action)

fun NodeWrapper<*>.onLeftClick(clickCount: Int = 1, filter: Boolean = false, action: (MouseEvent)->Unit) {
  if (filter) {
	addEventFilter(MouseEvent.MOUSE_CLICKED) {
	  if (it.clickCount == clickCount && it.button === MouseButton.PRIMARY)
		action(it)
	}
  } else {
	setOnMouseClicked {
	  if (it.clickCount == clickCount && it.button === MouseButton.PRIMARY)
		action(it)
	}
  }
}

fun NodeWrapper<*>.onRightClick(clickCount: Int = 1, filter: Boolean = false, action: (MouseEvent)->Unit) {
  if (filter) {
	addEventFilter(MouseEvent.MOUSE_CLICKED) {
	  if (it.clickCount == clickCount && it.button === MouseButton.SECONDARY)
		action(it)
	}
  } else {
	setOnMouseClicked {
	  if (it.clickCount == clickCount && it.button === MouseButton.SECONDARY)
		action(it)
	}
  }
}


/**
 * Execute action when the enter key is pressed or the mouse is clicked

 * @param clickCount The number of mouse clicks to trigger the action
 * *
 * @param action The action to execute on select
 */
fun <T> TreeTableViewWrapper<T>.onUserSelect(clickCount: Int = 2, action: (T)->Unit) {
  val isSelected = { event: InputEvent ->
	event.target.isInsideRow() && !selectionModel.isEmpty
  }

  addEventFilter(MouseEvent.MOUSE_CLICKED) { event ->
	if (event.clickCount == clickCount && isSelected(event))
	  action(selectedItem!!.value)
  }

  addEventFilter(KeyEvent.KEY_PRESSED) { event ->
	if (event.code == KeyCode.ENTER && !event.isMetaDown && isSelected(event))
	  action(selectedItem!!.value)
  }
}

val <S, T> TableCell<S, T>.rowItem: S get() = tableView.items[index]
val <S, T> TreeTableCell<S, T>.rowItem: S get() = treeTableView.getTreeItem(index).value

fun <T> TableViewWrapper<T>.onUserDelete(action: (T)->Unit) {
  addEventFilter(KeyEvent.KEY_PRESSED, { event ->
	if (event.code == KeyCode.BACK_SPACE && selectedItem != null)
	  action(selectedItem!!)
  })
}

/**
 * Did the event occur inside a TableRow, TreeTableRow or ListCell?
 */
fun EventTarget.isInsideRow(): Boolean {
  if (this !is Node)
	return false

  if (this is TableColumnHeader)
	return false

  if (this is TableRow<*> || this is TableView<*> || this is TreeTableRow<*> || this is TreeTableView<*> || this is ListCell<*>)
	return true

  if (this.parent != null)
	return this.parent.isInsideRow()

  return false
}

/**
 * Access BorderPane constraints to manipulate and apply on this control
 */
inline fun <T: Node> T.borderpaneConstraints(op: (BorderPaneConstraint.()->Unit)): T {
  val bpc = BorderPaneConstraint(this)
  bpc.op()
  return bpc.applyToNode(this)
}

class BorderPaneConstraint(
  node: Node,
  override var margin: Insets? = BorderPane.getMargin(node),
  var alignment: Pos? = null
): MarginableConstraints() {
  fun <T: Node> applyToNode(node: T): T {
	margin.let { BorderPane.setMargin(node, it) }
	alignment?.let { BorderPane.setAlignment(node, it) }
	return node
  }
}

/**
 * Access GridPane constraints to manipulate and apply on this control
 */
inline fun <T: NodeW> T.gridpaneConstraints(op: (GridPaneConstraint.()->Unit)): T {
  val gpc = GridPaneConstraint(this.node)
  gpc.op()
  return gpc.applyToNode(this)
}

class GridPaneConstraint(
  node: Node,
  var columnIndex: Int? = null,
  var rowIndex: Int? = null,
  var hGrow: Priority? = null,
  var vGrow: Priority? = null,
  override var margin: Insets? = GridPane.getMargin(node),
  var fillHeight: Boolean? = null,
  var fillWidth: Boolean? = null,
  var hAlignment: HPos? = null,
  var vAlignment: VPos? = null,
  var columnSpan: Int? = null,
  var rowSpan: Int? = null

): MarginableConstraints() {
  var vhGrow: Priority? = null
	set(value) {
	  vGrow = value
	  hGrow = value
	  field = value
	}

  var fillHeightWidth: Boolean? = null
	set(value) {
	  fillHeight = value
	  fillWidth = value
	  field = value
	}

  fun columnRowIndex(columnIndex: Int, rowIndex: Int) {
	this.columnIndex = columnIndex
	this.rowIndex = rowIndex
  }

  fun fillHeightWidth(fill: Boolean) {
	fillHeight = fill
	fillWidth = fill
  }

  fun <T: NodeW> applyToNode(node: T): T {
	columnIndex?.let { GridPane.setColumnIndex(node.node, it) }
	rowIndex?.let { GridPane.setRowIndex(node.node, it) }
	hGrow?.let { GridPane.setHgrow(node.node, it) }
	vGrow?.let { GridPane.setVgrow(node.node, it) }
	margin.let { GridPane.setMargin(node.node, it) }
	fillHeight?.let { GridPane.setFillHeight(node.node, it) }
	fillWidth?.let { GridPane.setFillWidth(node.node, it) }
	hAlignment?.let { GridPane.setHalignment(node.node, it) }
	vAlignment?.let { GridPane.setValignment(node.node, it) }
	columnSpan?.let { GridPane.setColumnSpan(node.node, it) }
	rowSpan?.let { GridPane.setRowSpan(node.node, it) }
	return node
  }
}

inline fun <T: Node> T.vboxConstraints(op: (VBoxConstraint.()->Unit)): T {
  val c = VBoxConstraint(this)
  c.op()
  return c.applyToNode(this)
}

inline fun <T: Node> T.stackpaneConstraints(op: (StackpaneConstraint.()->Unit)): T {
  val c = StackpaneConstraint(this)
  c.op()
  return c.applyToNode(this)
}

class VBoxConstraint(
  node: Node,
  override var margin: Insets? = VBox.getMargin(node),
  var vGrow: Priority? = null

): MarginableConstraints() {
  fun <T: Node> applyToNode(node: T): T {
	margin?.let { VBox.setMargin(node, it) }
	vGrow?.let { VBox.setVgrow(node, it) }
	return node
  }
}

class StackpaneConstraint(
  node: Node,
  override var margin: Insets? = StackPane.getMargin(node),
  var alignment: Pos? = null

): MarginableConstraints() {
  fun <T: Node> applyToNode(node: T): T {
	margin?.let { StackPane.setMargin(node, it) }
	alignment?.let { StackPane.setAlignment(node, it) }
	return node
  }
}

inline fun <T: Node> T.hboxConstraints(op: (HBoxConstraint.()->Unit)): T {
  val c = HBoxConstraint(this)
  c.op()
  return c.applyToNode(this)
}

class HBoxConstraint(
  node: Node,
  override var margin: Insets? = HBox.getMargin(node),
  var hGrow: Priority? = null
): MarginableConstraints() {

  fun <T: Node> applyToNode(node: T): T {
	margin?.let { HBox.setMargin(node, it) }
	hGrow?.let { HBox.setHgrow(node, it) }
	return node
  }
}


inline fun <T: Node> T.anchorpaneConstraints(op: AnchorPaneConstraint.()->Unit): T {
  val c = AnchorPaneConstraint()
  c.op()
  return c.applyToNode(this)
}

class AnchorPaneConstraint(
  var topAnchor: Number? = null,
  var rightAnchor: Number? = null,
  var bottomAnchor: Number? = null,
  var leftAnchor: Number? = null
) {
  fun <T: Node> applyToNode(node: T): T {
	topAnchor?.let { AnchorPane.setTopAnchor(node, it.toDouble()) }
	rightAnchor?.let { AnchorPane.setRightAnchor(node, it.toDouble()) }
	bottomAnchor?.let { AnchorPane.setBottomAnchor(node, it.toDouble()) }
	leftAnchor?.let { AnchorPane.setLeftAnchor(node, it.toDouble()) }
	return node
  }
}

inline fun <T: NodeW> T.splitpaneConstraints(op: SplitPaneConstraint.()->Unit): T {
  val c = SplitPaneConstraint()
  c.op()
  return c.applyToNode(this)
}

class SplitPaneConstraint(
  var isResizableWithParent: Boolean? = null
) {
  fun <T: NodeW> applyToNode(node: T): T {
	isResizableWithParent?.let { SplitPane.setResizableWithParent(node.node, it) }
	return node
  }
}

abstract class MarginableConstraints {
  abstract var margin: Insets?
  var marginTop: Double
	get() = margin?.top ?: 0.0
	set(value) {
	  margin = Insets(value, margin?.right ?: 0.0, margin?.bottom ?: 0.0, margin?.left ?: 0.0)
	}

  var marginRight: Double
	get() = margin?.right ?: 0.0
	set(value) {
	  margin = Insets(margin?.top ?: 0.0, value, margin?.bottom ?: 0.0, margin?.left ?: 0.0)
	}

  var marginBottom: Double
	get() = margin?.bottom ?: 0.0
	set(value) {
	  margin = Insets(margin?.top ?: 0.0, margin?.right ?: 0.0, value, margin?.left ?: 0.0)
	}

  var marginLeft: Double
	get() = margin?.left ?: 0.0
	set(value) {
	  margin = Insets(margin?.top ?: 0.0, margin?.right ?: 0.0, margin?.bottom ?: 0.0, value)
	}

  fun marginTopBottom(value: Double) {
	marginTop = value
	marginBottom = value
  }

  fun marginLeftRight(value: Double) {
	marginLeft = value
	marginRight = value
  }
}

fun <T> TableViewWrapper<T>.regainFocusAfterEdit() = apply {
  editingCellProperty().onChange {
	if (it == null)
	  requestFocus()
  }
}

fun <T, S: Any> TableColumn<S, T>.makeEditable(converter: StringConverter<T>): TableColumn<S, T> = apply {
  tableView?.isEditable = true
  cellFactory = TextFieldTableCell.forTableColumn(converter)
}

fun <S: Any> TableColumn<S, String>.makeEditable(): TableColumn<S, String> = apply {
  tableView?.isEditable = true
  cellFactory = TextFieldTableCell.forTableColumn()
}


fun <T> TreeTableViewWrapper<T>.populate(
  itemFactory: (T)->TreeItem<T> = { TreeItem(it) },
  childFactory: (TreeItem<T>)->Iterable<T>?
) =
  populateTree(root, itemFactory, childFactory)

/**
 * Add children to the given item by invoking the supplied childFactory function, which converts
 * a TreeItem&lt;T> to a List&lt;T>?.
 *
 * If the childFactory returns a non-empty list, each entry in the list is converted to a TreeItem&lt;T>
 * via the supplied itemProcessor function. The default itemProcessor from TreeTableView.populate and TreeTable.populate
 * simply wraps the given T in a TreeItem, but you can override it to add icons etc. Lastly, the populateTree
 * function is called for each of the generated child items.
 */
fun <T> populateTree(item: TreeItem<T>, itemFactory: (T)->TreeItem<T>, childFactory: (TreeItem<T>)->Iterable<T>?) {
  val children = childFactory.invoke(item)

  children?.map { itemFactory(it) }?.apply {
	item.children.setAll(this)
	forEach { populateTree(it, itemFactory, childFactory) }
  }

  (children as? ObservableList<T>)?.addListener(ListChangeListener { change ->
	while (change.next()) {
	  if (change.wasPermutated()) {
		item.children.subList(change.from, change.to).clear()
		val permutated = change.list.subList(change.from, change.to).map { itemFactory(it) }
		item.children.addAll(change.from, permutated)
		permutated.forEach { populateTree(it, itemFactory, childFactory) }
	  } else {
		if (change.wasRemoved()) {
		  val removed = change.removed.flatMap { removed -> item.children.filter { it.value == removed } }
		  item.children.removeAll(removed)
		}
		if (change.wasAdded()) {
		  val added = change.addedSubList.map { itemFactory(it) }
		  item.children.addAll(change.from, added)
		  added.forEach { populateTree(it, itemFactory, childFactory) }
		}
	  }
	}
  })
}


fun EventTargetWrapper<*>.removeFromParent() {
  val n = node
  when (n) {
	is Tab         -> n.tabPane?.tabs?.remove(n)
	is Node        -> {
	  (n.parent?.parent as? ToolBar)?.items?.remove(n) ?: n.parent?.getChildList()?.remove(n)
	}

	is TreeItem<*> -> n.parent.children.remove(n)
  }
}

/**
 * Listen for changes to an observable value and replace all content in this Node with the
 * new content created by the onChangeBuilder. The builder operates on the node and receives
 * the new value of the observable as it's only parameter.
 *
 * The onChangeBuilder is run immediately with the current value of the property.
 */
fun <S: EventTarget, T> S.dynamicContent(property: ObservableValue<T>, onChangeBuilder: S.(T?)->Unit) {
  val onChange: (T?)->Unit = {
	getChildList()?.clear()
	onChangeBuilder(this@dynamicContent, it)
  }
  property.onChange(onChange)
  onChange(property.value)
}

const val TRANSITIONING_PROPERTY = "tornadofx.transitioning"

/**
 * Whether this node is currently being used in a [ViewTransition]. Used to determine whether it can be used in a
 * transition. (Nodes can only exist once in the scenegraph, so it cannot be in two transitions at once.)
 */
internal var NodeW.isTransitioning: Boolean
  get() {
	val x = node.properties[TRANSITIONING_PROPERTY]
	return x != null && (x !is Boolean || x != false)
  }
  set(value) {
	node.properties[TRANSITIONING_PROPERTY] = value
  }


fun NodeW.hide() {
  isVisible = false
  isManaged = false
}

fun NodeW.show() {
  isVisible = true
  isManaged = true
}

fun NodeW.whenVisible(runLater: Boolean = true, op: ()->Unit) {
  visibleProperty().onChange {
	if (it) {
	  if (runLater) Platform.runLater(op) else op()
	}
  }
}


val RegionWrapper.paddingTopProperty: DoubleProperty
  get() = node.properties.getOrPut("paddingTopProperty") {
	proxypropDouble(paddingProperty, { value.top }) {
	  Insets(it, value.right, value.bottom, value.left)
	}
  } as DoubleProperty

val RegionWrapper.paddingBottomProperty: DoubleProperty
  get() = node.properties.getOrPut("paddingBottomProperty") {
	proxypropDouble(paddingProperty, { value.bottom }) {
	  Insets(value.top, value.right, it, value.left)
	}
  } as DoubleProperty

val RegionWrapper.paddingLeftProperty: DoubleProperty
  get() = node.properties.getOrPut("paddingLeftProperty") {
	proxypropDouble(paddingProperty, { value.left }) {
	  Insets(value.top, value.right, value.bottom, it)
	}
  } as DoubleProperty

val RegionWrapper.paddingRightProperty: DoubleProperty
  get() = node.properties.getOrPut("paddingRightProperty") {
	proxypropDouble(paddingProperty, { value.right }) {
	  Insets(value.top, it, value.bottom, value.left)
	}
  } as DoubleProperty


// -- Node helpers
/**
 * This extension function will automatically bind to the managedProperty of the given node
 * and will make sure that it is managed, if the given [expr] returning an observable boolean value equals true.
 *
 * @see https://docs.oracle.com/javase/8/javafx/api/javafx/scene/Node.html#managedProperty
 */
fun <T: NodeWrapper<*>> T.managedWhen(expr: ()->ObservableValue<Boolean>): T = managedWhen(expr())

/**
 * This extension function will automatically bind to the managedProperty of the given node
 * and will make sure that it is managed, if the given [predicate] an observable boolean value equals true.
 *
 * @see https://docs.oracle.com/javase/8/javafx/api/javafx/scene/Node.html#managedProperty)
 */
fun <T: NodeWrapper<*>> T.managedWhen(predicate: ObservableValue<Boolean>) = apply {
  managedProperty().cleanBind(predicate)
}

/**
 * This extension function will automatically bind to the visibleProperty of the given node
 * and will make sure that it is visible, if the given [predicate] an observable boolean value equals true.
 *
 * @see https://docs.oracle.com/javase/8/javafx/api/javafx/scene/Node.html#visibleProperty
 */
fun <T: NodeWrapper<*>> T.visibleWhen(predicate: ObservableValue<Boolean>) = apply {

  visibleProperty().cleanBind(predicate)
}

/**
 * This extension function will automatically bind to the visibleProperty of the given node
 * and will make sure that it is visible, if the given [expr] returning an observable boolean value equals true.
 *
 * @see https://docs.oracle.com/javase/8/javafx/api/javafx/scene/Node.html#visibleProperty
 */
fun <T: NodeWrapper<*>> T.visibleWhen(expr: ()->ObservableValue<Boolean>): T = visibleWhen(expr())

/**
 * This extension function will make sure to hide the given node,
 * if the given [expr] returning an observable boolean value equals true.
 */
fun <T: NodeWrapper<*>> T.hiddenWhen(expr: ()->ObservableValue<Boolean>): T = hiddenWhen(expr())

/**
 * This extension function will make sure to hide the given node,
 * if the given [predicate] an observable boolean value equals true.
 */
fun <T: NodeWrapper<*>> T.hiddenWhen(predicate: ObservableValue<Boolean>) = apply {
  val binding = if (predicate is BooleanBinding) predicate.not() else predicate.toBinding().not()
  visibleProperty().cleanBind(binding)
}

/**
 * This extension function will automatically bind to the disableProperty of the given node
 * and will disable it, if the given [expr] returning an observable boolean value equals true.
 *
 * @see https://docs.oracle.com/javase/8/javafx/api/javafx/scene/Node.html#disable
 */
fun <T: NodeWrapper<*>> T.disableWhen(expr: ()->ObservableValue<Boolean>): T = disableWhen(expr())

/**
 * This extension function will automatically bind to the disableProperty of the given node
 * and will disable it, if the given [predicate] observable boolean value equals true.
 *
 * @see https://docs.oracle.com/javase/8/javafx/api/javafx/scene/Node.html#disableProperty
 */
fun <T: NodeWrapper<*>> T.disableWhen(predicate: ObservableValue<Boolean>) = apply {
  disableProperty().cleanBind(predicate)
}

/**
 * This extension function will make sure that the given node is enabled when ever,
 * the given [expr] returning an observable boolean value equals true.
 */
fun <T: NodeWrapper<*>> T.enableWhen(expr: ()->ObservableValue<Boolean>): T = enableWhen(expr())

/**
 * This extension function will make sure that the given node is enabled when ever,
 * the given [predicate] observable boolean value equals true.
 */
fun <T: NodeWrapper<*>> T.enableWhen(predicate: ObservableValue<Boolean>) = apply {
  val binding = if (predicate is BooleanBinding) predicate.not() else predicate.toBinding().not()
  disableProperty().cleanBind(binding)
}

/**
 * This extension function will make sure that the given node will only be visible in the scene graph,
 * if the given [expr] returning an observable boolean value equals true.
 */
fun <T: NodeWrapper<*>> T.removeWhen(expr: ()->ObservableValue<Boolean>): T = removeWhen(expr())

/**
 * This extension function will make sure that the given node will only be visible in the scene graph,
 * if the given [predicate] observable boolean value equals true.
 */
fun <T: NodeWrapper<*>> T.removeWhen(predicate: ObservableValue<Boolean>) = apply {
  val remove = booleanBinding(predicate) { predicate.value.not() }
  visibleProperty().cleanBind(remove)
  managedProperty().cleanBind(remove)
}

fun TextInputControlWrapper.editableWhen(predicate: ObservableValue<Boolean>) = apply {
  editableProperty().bind(predicate)
}

fun ComboBoxBaseWrapper<*>.editableWhen(predicate: ObservableValue<Boolean>) = apply {
  editableProperty().bind(predicate)
}

fun TableViewWrapper<*>.editableWhen(predicate: ObservableValue<Boolean>) = apply {
  editableProperty().bind(predicate)
}

fun TreeTableViewWrapper<*>.editableWhen(predicate: ObservableValue<Boolean>) = apply {
  editableProperty().bind(predicate)
}

fun ListViewWrapper<*>.editableWhen(predicate: ObservableValue<Boolean>) = apply {
  editableProperty().bind(predicate)
}

/**
 * This extension function will make sure that the given [onHover] function will always be calles
 * when ever the hoverProperty of the given node changes.
 */
fun NodeWrapper<*>.onHover(onHover: (Boolean)->Unit) = apply {
  node.hoverProperty().onChange { onHover(node.isHover) }
}

// -- MenuItem helpers
fun MenuItemWrapper.visibleWhen(expr: ()->ObservableValue<Boolean>) = visibleWhen(expr())

fun MenuItemWrapper.visibleWhen(predicate: ObservableValue<Boolean>) = visibleProperty().cleanBind(predicate)
fun MenuItemWrapper.disableWhen(expr: ()->ObservableValue<Boolean>) = disableWhen(expr())
fun MenuItemWrapper.disableWhen(predicate: ObservableValue<Boolean>) = disableProperty().cleanBind(predicate)
fun MenuItemWrapper.enableWhen(expr: ()->ObservableValue<Boolean>) = enableWhen(expr())
fun MenuItemWrapper.enableWhen(obs: ObservableValue<Boolean>) {
  val binding = if (obs is BooleanBinding) obs.not() else obs.toBinding().not()
  disableProperty().cleanBind(binding)
}
