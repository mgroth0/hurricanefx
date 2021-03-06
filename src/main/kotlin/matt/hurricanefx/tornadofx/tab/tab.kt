package matt.hurricanefx.tornadofx.tab

/*slightly modified code I stole from tornadofx*/

import javafx.beans.binding.BooleanBinding
import javafx.beans.value.ObservableValue
import javafx.scene.Node
import javafx.scene.control.Tab
import javafx.scene.control.TabPane
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import matt.hurricanefx.eye.bind.toBinding
import matt.hurricanefx.eye.lib.onChange
import matt.hurricanefx.eye.prop.cleanBind
import matt.hurricanefx.tornadofx.fx.attachTo
import matt.hurricanefx.tornadofx.nodes.removeFromParent
import matt.hurricanefx.wrapper.EventTargetWrapper
import matt.hurricanefx.wrapper.TabPaneWrapper
import matt.hurricanefx.wrapper.TabWrapper
import matt.hurricanefx.wrapper.wrapped

fun EventTargetWrapper<*>.tabpane(op: TabPaneWrapper.()->Unit = {}) = TabPaneWrapper().attachTo(this, op)

fun <T: Node> TabPaneWrapper.tab(text: String, content: T, op: T.()->Unit = {}): Tab {
  return tab(tabs.size, text, content, op)
}

fun <T: Node> TabPaneWrapper.tab(index: Int, text: String, content: T, op: T.()->Unit = {}): Tab {
  val tab = Tab(text, content)
  tabs.add(index, tab)
  op(content)
  return tab
}

/*matt was here*/
fun <T: Node> TabPaneWrapper.staticTab(text: String, content: T, op: T.()->Unit = {}): TabWrapper {
  return staticTab(tabs.size, text, content, op)
}

/*matt was here*/
fun <T: Node> TabPaneWrapper.staticTab(index: Int, text: String, content: T, op: T.()->Unit = {}): TabWrapper {
  val tab = TabWrapper(text, content).apply {
	isClosable = false
  }
  tabs.add(index, tab.node)
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

fun TabWrapper.close() = removeFromParent()


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
