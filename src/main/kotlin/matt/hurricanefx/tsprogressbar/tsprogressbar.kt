package matt.hurricanefx.tsprogressbar

import matt.hurricanefx.wrapper.NodeWrapper
import javafx.application.Platform.runLater
import javafx.scene.Node
import javafx.scene.control.ProgressBar


sealed class ThreadSafeNodeWrapper<N: Node>(
  override val node: N
): NodeWrapper<N>

class TSProgressBar: ThreadSafeNodeWrapper<ProgressBar>(ProgressBar()) {
  var progress: Double
	get() = node.progress
	set(value) = runLater { node.progress = value }
}