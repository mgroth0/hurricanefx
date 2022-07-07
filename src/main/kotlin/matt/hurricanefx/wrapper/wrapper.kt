package matt.hurricanefx.wrapper

import javafx.beans.property.DoubleProperty
import javafx.beans.property.ReadOnlyDoubleProperty
import javafx.collections.ObservableList
import javafx.geometry.Pos
import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.control.MultipleSelectionModel
import javafx.scene.control.ScrollPane
import javafx.scene.control.TreeItem
import javafx.scene.control.TreeTableView
import javafx.scene.control.TreeView
import javafx.scene.layout.Border
import javafx.scene.layout.HBox
import javafx.scene.layout.Pane
import javafx.scene.layout.Region
import javafx.scene.layout.VBox
import javafx.util.Callback
import matt.hurricanefx.addAll
import matt.hurricanefx.eye.lib.onChange
import matt.hurricanefx.stage
import matt.hurricanefx.tornadofx.nodes.add
import matt.hurricanefx.tornadofx.nodes.plusAssign
import matt.hurricanefx.tornadofx.tree.selectedValue

private typealias NW = NodeWrapper<*>

@DslMarker
annotation class FXNodeWrapperDSL

@FXNodeWrapperDSL
interface NodeWrapper<N: Node> {
  val node: N

  val scene: Scene? get() = node.scene
  val stage get() = node.stage

  operator fun Node.unaryPlus() {
	this@NodeWrapper.node.add(this)
  }

  operator fun NW.unaryPlus() {
	this@NodeWrapper.node.add(this)
  }

  operator fun plusAssign(n: Node) {
	node += n
  }

  operator fun plusAssign(n: NodeWrapper<*>) {
	node += n.node
  }


}

interface RegionWrapper<N: Region>: NodeWrapper<N> {

  var border: Border?
	get() = node.border
	set(value) {
	  node.border = value
	}

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
}

interface PaneWrapper<N: Pane>: RegionWrapper<N> {
  operator fun Collection<Node>.unaryPlus() {
	node.addAll(this)
  }


  val children: ObservableList<Node> get() = node.children
}


interface BoxWrapper<N: Pane>: PaneWrapper<N> {

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
class VBoxWrapper(override val node: VBox = VBox(), op: VBoxWrapper.()->Unit = {}): BoxWrapper<VBox> {
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

sealed interface TreeLikeWrapper<N: Node, T>: NodeWrapper<N> {
  var root: TreeItem<T>
  var isShowRoot: Boolean
  fun setOnSelectionChange(listener: (TreeItem<T>?)->Unit)
  val selectedItem: TreeItem<T>?
  val selectedValue: T?
  val selectionModel: MultipleSelectionModel<TreeItem<T>>
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

  override val selectedItem: TreeItem<T>? get() = node.selectionModel.selectedItem
  override val selectedValue: T? get() = node.selectedValue
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

  override val selectedItem: TreeItem<T>? get() = node.selectionModel.selectedItem
  override val selectedValue: T? get() = selectedItem?.value
  override val selectionModel: MultipleSelectionModel<TreeItem<T>> get() = node.selectionModel
  override fun scrollTo(i: Int) = node.scrollTo(i)

}