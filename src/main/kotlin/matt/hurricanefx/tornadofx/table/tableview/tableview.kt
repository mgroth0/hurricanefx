package matt.hurricanefx.tornadofx.table.tableview

import javafx.beans.property.SimpleObjectProperty
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.TreeTableColumn
import javafx.scene.control.TreeTableView
import matt.hurricanefx.tornadofx.item.column
import kotlin.reflect.KFunction
import kotlin.reflect.KProperty1

/**
 * Matt was here!
 */
@JvmName("coolColumn")
fun <S, T> TableView<S>.column(getter: KFunction<T>): TableColumn<S, T> {
  return column(getter.name) {
	SimpleObjectProperty(getter.call(it.value))
  }
}

/**
 * Matt was here!
 */
@JvmName("coolColumn2")
fun <S, T> TableView<S>.column(getter: KProperty1<S, T>): TableColumn<S, T> {
  return column(getter.name) {
	SimpleObjectProperty(getter.call(it.value))
  }
}

/**
 * Matt was here!
 */
@JvmName("coolColumn")
inline fun <reified S, T> TreeTableView<S>.column(getter: KFunction<T>): TreeTableColumn<S, T> {
  return column(getter.name) {
	SimpleObjectProperty(getter.call(it.value.value))
  }
}

/**
 * Matt was here!
 */
@JvmName("coolColumn2")
inline fun <reified S, T> TreeTableView<S>.column(getter: KProperty1<S, T>): TreeTableColumn<S, T> {
  return column(getter.name) {
	SimpleObjectProperty(getter.call(it.value.value))
  }
}