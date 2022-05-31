package matt.hurricanefx.tornadofx.clip

/*slightly modified code I stole from tornadofx*/

import javafx.scene.input.Clipboard
import javafx.scene.input.ClipboardContent
import javafx.scene.input.DataFormat
import matt.klib.file.MFile


fun Clipboard.setContent(op: ClipboardContent.() -> Unit) {
  val content = ClipboardContent()
  op(content)
  setContent(content)
}

fun Clipboard.putString(value: String) = setContent { putString(value) }
fun Clipboard.putFiles(files: MutableList<MFile>) = setContent { putFiles(files.map { it.userFile }) }
fun Clipboard.put(dataFormat: DataFormat, value: Any) = setContent { put(dataFormat, value) }