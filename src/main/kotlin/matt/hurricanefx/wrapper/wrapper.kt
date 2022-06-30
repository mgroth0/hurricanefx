package matt.hurricanefx.wrapper

import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.control.ScrollPane
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import matt.hurricanefx.addAll
import matt.hurricanefx.stage
import matt.hurricanefx.tornadofx.nodes.add

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


}

interface PaneWrapper<N: Pane>: NodeWrapper<N> {
  operator fun Collection<Node>.unaryPlus() {
	node.addAll(this)
  }
}

@FXNodeWrapperDSL
class VBoxWrapper(override val node: VBox = VBox(), op: VBoxWrapper.()->Unit = {}): PaneWrapper<VBox> {
  init {
	op()
  }
}

@FXNodeWrapperDSL
class ScrollingVBoxWrapper(vbox: VBox = VBox(), op: ScrollingVBoxWrapper.()->Unit = {}): NodeWrapper<ScrollPane> {
  constructor(vbox: VBoxWrapper, op: ScrollingVBoxWrapper.()->Unit = {}): this(vbox.node, op)

  init {
	op()
  }

  override val node = ScrollPane(VBox())
}