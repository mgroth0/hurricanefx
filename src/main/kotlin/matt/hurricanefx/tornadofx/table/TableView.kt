package matt.hurricanefx.tornadofx.table

/*slightly modified code I stole from tornadofx*/

import javafx.scene.control.SelectionMode
import javafx.scene.control.TableView


fun <T> TableView<T>.multiSelect(enable: Boolean = true) {
    selectionModel.selectionMode = if (enable) SelectionMode.MULTIPLE else SelectionMode.SINGLE
}