package matt.hurricanefx.tornadofx.tab

/*slightly modified code I stole from tornadofx*/

import javafx.beans.binding.BooleanBinding
import javafx.beans.value.ObservableValue
import javafx.event.EventTarget
import javafx.scene.Node
import javafx.scene.control.Tab
import javafx.scene.control.TabPane
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import matt.hurricanefx.eye.lib.onChange
import matt.hurricanefx.eye.prop.cleanBind
import matt.hurricanefx.tornadofx.eye.bind.toBinding
import matt.hurricanefx.tornadofx.fx.attachTo
import matt.hurricanefx.tornadofx.nodes.removeFromParent

fun EventTarget.tabpane(op: TabPane.()->Unit = {}) = TabPane().attachTo(this, op)

fun <T: Node> TabPane.tab(text: String, content: T, op: T.()->Unit = {}): Tab {
  return tab(tabs.size, text, content, op)
}

fun <T: Node> TabPane.tab(index: Int, text: String, content: T, op: T.()->Unit = {}): Tab {
  val tab = Tab(text, content)
  tabs.add(index, tab)
  op(content)
  return tab
}

/*matt was here*/
fun <T: Node> TabPane.staticTab(text: String, content: T, op: T.()->Unit = {}): Tab {
  return staticTab(tabs.size, text, content, op)
}

/*matt was here*/
fun <T: Node> TabPane.staticTab(index: Int, text: String, content: T, op: T.()->Unit = {}): Tab {
  val tab = Tab(text, content).apply {
	isClosable = false
  }
  tabs.add(index, tab)
  op(content)
  return tab
}

fun Tab.disableWhen(predicate: ObservableValue<Boolean>) = disableProperty().cleanBind(predicate)
fun Tab.enableWhen(predicate: ObservableValue<Boolean>) {
  val binding = if (predicate is BooleanBinding) predicate.not() else predicate.toBinding().not()
  disableProperty().cleanBind(binding)
}

fun Tab.closeableWhen(predicate: ObservableValue<Boolean>) {
  closableProperty().bind(predicate)
}

fun Tab.visibleWhen(predicate: ObservableValue<Boolean>) {
  val localTabPane = tabPane
  fun updateState() {
	if (predicate.value.not()) localTabPane.tabs.remove(this)
	else if (this !in tabPane.tabs) localTabPane.tabs.add(this)
  }
  updateState()
  predicate.onChange { updateState() }
}

fun Tab.close() = removeFromParent()


//fun TabPane.tab(text: String? = null, node: Node? = null, op: Tab.() -> Unit = {}): Tab {
//    val tab = Tab(text,node)
////    tab.tag = tag
//    tabs.add(tab)
//    return tab.also(op)
//}

fun Tab.whenSelected(op: ()->Unit) {
  selectedProperty().onChange { if (it) op() }
}

fun Tab.select() = apply { tabPane.selectionModel.select(this) }

@Deprecated(
  "No need to use the content{} wrapper anymore, just use a builder directly inside the Tab",
  ReplaceWith("no content{} wrapper"),
  DeprecationLevel.WARNING
)
fun Tab.content(op: Pane.()->Unit): Node {
  val fake = VBox()
  op(fake)
  content = if (fake.children.size == 1) fake.children.first() else fake
  return content
}
