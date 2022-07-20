@file:Suppress("unused")

/*slightly modified code I stole from tornadofx*/

package matt.hurricanefx.tornadofx.fx

import javafx.beans.property.ListProperty
import javafx.collections.ObservableList
import javafx.collections.ObservableMap
import javafx.collections.ObservableSet
import javafx.event.EventTarget
import javafx.scene.Group
import javafx.scene.Node
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.SubScene
import javafx.scene.control.ButtonBase
import javafx.scene.control.CustomMenuItem
import javafx.scene.control.Label
import javafx.scene.control.MenuItem
import javafx.scene.control.ScrollPane
import javafx.scene.control.SplitPane
import javafx.scene.control.Tab
import javafx.scene.control.TabPane
import javafx.scene.control.TitledPane
import javafx.scene.control.ToolBar
import javafx.scene.layout.BorderPane
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import javafx.stage.Window
import matt.hurricanefx.eye.collect.ListConversionListener
import matt.hurricanefx.eye.collect.MapConversionListener
import matt.hurricanefx.eye.collect.SetConversionListener
import matt.hurricanefx.eye.collect.bind
import matt.hurricanefx.wrapper.EventTargetWrapper
import matt.hurricanefx.wrapper.NodeWrapper
import matt.hurricanefx.wrapper.NodeWrapper.Companion.wrapped
import matt.klib.lang.err
import kotlin.contracts.InvocationKind.EXACTLY_ONCE
import kotlin.contracts.contract


/**
 * Add the given node to the pane, invoke the node operation and return the node. The `opcr` name
 * is an acronym for "op connect & return".
 */
inline fun <T: NodeWrapper<*>> opcr(parent: EventTargetWrapper<*>, node: T, op: T.()->Unit = {}): T {
  contract {
	callsInPlace(op, EXACTLY_ONCE)
  }
  return node.apply {
	parent.addChildIfPossible(this)
	op(this)
  }
}

//inline fun <T: NodeWrapper<*>> opcr(parent: EventTarget, node: T, op: T.()->Unit = {}): T {
//  contract {
//	callsInPlace(op, EXACTLY_ONCE)
//  }
//  return node.apply {
//	parent.wrapped().addChildIfPossible(this)
//	op(this)
//  }
//}

/**
 * Attaches the node to the pane and invokes the node operation.
 */
inline fun <T: NodeWrapper<*>> T.attachTo(parent: EventTargetWrapper<*>, op: T.()->Unit = {}): T {
  contract {
	callsInPlace(op, EXACTLY_ONCE)
  }
  return opcr(parent, this, op)
}

/**
 * Attaches the node to the pane and invokes the node operation.
 * Because the framework sometimes needs to setup the node, another lambda can be provided
 */
inline fun <T: NodeWrapper<*>> T.attachTo(
  parent: EventTargetWrapper<*>,
  after: T.()->Unit,
  before: (T)->Unit
): T {
  contract {
	callsInPlace(before, EXACTLY_ONCE)
	callsInPlace(after, EXACTLY_ONCE)
  }
  return this.also(before).attachTo(parent, after)
}

@Suppress("UNNECESSARY_SAFE_CALL")
fun EventTargetWrapper<*>.addChildIfPossible(node: NodeWrapper<*>, index: Int? = null) {
  val n = this.node
  when (n) {

	/*matt was here*/
	is Scene          -> {
	  n.root = node.node as Parent
	}

	is SubScene       -> {
	  n.root = node.node as Parent
	}

	is ScrollPane     -> {
	  /*content = node*/ /*TORNADOFX DEFAULT*/

	  n.content.wrapped().addChildIfPossible(node) /*MATT'S WAY*/
	}

	is Tab            -> {
	  // Map the tab to the UIComponent for later retrieval. Used to close tab with UIComponent.close()
	  // and to connect the onTabSelected callback
	  n.content = node.node
	}

	is ButtonBase     -> {
	  n.graphic = node.node
	}

	is BorderPane     -> {
	} // Either pos = builder { or caught by builderTarget above
	is TabPane        -> {
	  val tab = Tab(node.toString(), node.node)
	  n.tabs.add(tab)
	}

	is TitledPane     -> {
	  if (n.content is Pane) {
		n.content.wrapped().addChildIfPossible(node, index)
	  } else if (n.content is Node) {
		val container = VBox()
		container.children.addAll(n.content, node.node)
		n.content = container
	  } else {
		n.content = node.node
	  }
	}

	is CustomMenuItem -> {
	  n.content = node.node
	}

	is MenuItem       -> {
	  n.graphic = node.node
	}

	is Label          -> {
	  /*matt was here*/
	  n.graphic = node.node
	}

	else              -> if (n !is Pane) {
	  /*matt was here.. because this was getting really confusing*/
	  err("$n is a region. It can't add children.")
	} else n.getChildList()?.apply {

	  if (!n.children.contains(node.node)) {
		if (index != null && index < size)
		  add(index, node.node)
		else
		  add(node.node)
	  }
	}
  }
}

/**
 * Bind the children of this Layout node to the given observable list of items by converting
 * them into nodes via the given converter function. Changes to the source list will be reflected
 * in the children list of this layout node.
 */
fun <T> EventTarget.bindChildren(
  sourceList: ObservableList<T>,
  converter: (T)->Node
): ListConversionListener<T, Node> =
  requireNotNull(getChildList()?.bind(sourceList, converter)) { "Unable to extract child nodes from $this" }

/**
 * Bind the children of this Layout node to the items of the given ListPropery by converting
 * them into nodes via the given converter function. Changes to the source list and changing the list inside the ListProperty
 * will be reflected in the children list of this layout node.
 */
fun <T> EventTarget.bindChildren(sourceList: ListProperty<T>, converter: (T)->Node): ListConversionListener<T, Node> =
  requireNotNull(getChildList()?.bind(sourceList, converter)) { "Unable to extract child nodes from $this" }

/**
 * Bind the children of this Layout node to the given observable set of items
 * by converting them into nodes via the given converter function.
 * Changes to the source set will be reflected in the children list of this layout node.
 */
inline fun <reified T> EventTarget.bindChildren(
  sourceSet: ObservableSet<T>,
  noinline converter: (T)->Node
): SetConversionListener<T, Node> = requireNotNull(
  getChildList()?.bind(sourceSet, converter)
) { "Unable to extract child nodes from $this" }

inline fun <reified K, reified V> EventTarget.bindChildren(
  sourceMap: ObservableMap<K, V>,
  noinline converter: (K, V)->Node
): MapConversionListener<K, V, Node> = requireNotNull(
  getChildList()?.bind(sourceMap, converter)
) { "Unable to extract child nodes from $this" }


/**
 * Find the list of children from a Parent node. Gleaned code from ControlsFX for this.
 */
fun EventTarget.getChildList(): MutableList<Node>? = when (this) {
  is SplitPane -> items
  is ToolBar   -> items
  is Pane      -> children
  is Group     -> children
  //    is Control -> (skin as? SkinBase<*>)?.children ?: getChildrenReflectively()
  //      is Parent -> getChildrenReflectively()
  else         -> null
}

//@Suppress("UNCHECKED_CAST", "PLATFORM_CLASS_MAPPED_TO_KOTLIN")
//private fun Parent.getChildrenReflectively(): MutableList<Node>? {
//    val getter = this.javaClass.findMethodByName("getChildren")
//    if (getter != null && java.util.List::class.java.isAssignableFrom(getter.returnType)) {
//        getter.isAccessible = true
//        return getter.invoke(this) as MutableList<Node>
//    }
//    return null
//}

var Window.aboutToBeShown: Boolean
  get() = properties["tornadofx.aboutToBeShown"] == true
  set(value) {
	properties["tornadofx.aboutToBeShown"] = value
  }
