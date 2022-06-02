package matt.hurricanefx.tsprogressbar

import javafx.scene.Node
import javafx.scene.control.ProgressBar
import matt.hurricanefx.tornadofx.async.runLater


/*easier to write in functions than ThreadSafeNodeWrapper<*>*/
sealed interface NodeWrapper {
  val node: Node
}

sealed class ThreadSafeNodeWrapper<N: Node>(
  override val node: N
): NodeWrapper

class TSProgressBar: ThreadSafeNodeWrapper<ProgressBar>(ProgressBar()) {
  var progress: Double
	get() = node.progress
	set(value) = runLater { node.progress = value }
}