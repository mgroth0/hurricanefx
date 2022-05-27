package matt.hurricanefx.tornadofx.table

/*slightly modified code I stole from tornadofx*/

import javafx.scene.control.SelectionMode
import javafx.scene.control.TableView
import matt.hurricanefx.tornadofx.item.column


fun <T> TableView<T>.multiSelect(enable: Boolean = true) {
    selectionModel.selectionMode = if (enable) SelectionMode.MULTIPLE else SelectionMode.SINGLE
}
/*
fun <T,V> TableView<T>.simpleColumn(propGetter: (T) -> V) {
    matt.hurricanefx.tableview.coolColumn()
}*/
