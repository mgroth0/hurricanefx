package matt.hurricanefx.wrapper

import javafx.collections.ObservableList
import javafx.geometry.Pos
import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.control.ScrollPane
import javafx.scene.control.TreeItem
import javafx.scene.control.TreeTableView
import javafx.scene.control.TreeView
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import matt.hurricanefx.addAll
import matt.hurricanefx.eye.lib.onChange
import matt.hurricanefx.stage
import matt.hurricanefx.tornadofx.nodes.add
import matt.hurricanefx.tornadofx.nodes.plusAssign

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

interface PaneWrapper<N: Pane>: NodeWrapper<N> {
  operator fun Collection<Node>.unaryPlus() {
	node.addAll(this)
  }

  val children: ObservableList<Node> get() = node.children
}


@FXNodeWrapperDSL
class VBoxWrapper(override val node: VBox = VBox(), op: VBoxWrapper.()->Unit = {}): PaneWrapper<VBox> {
  var alignment: Pos
	get() = node.alignment
	set(value) {
	  node.alignment = value
	}

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

  override fun setOnSelectionChange(listener: (TreeItem<T>?)->Unit) {
	node.selectionModel.selectedItemProperty().onChange(listener)
  }
}