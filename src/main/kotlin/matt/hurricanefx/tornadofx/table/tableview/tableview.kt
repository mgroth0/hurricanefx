package matt.hurricanefx.tornadofx.table.tableview

import javafx.beans.property.SimpleObjectProperty
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.TreeTableColumn
import javafx.scene.control.TreeTableView
import kotlin.reflect.KFunction
import kotlin.reflect.KProperty1

/**
 * Matt was here!
 */
@JvmName("coolColumn")
fun <S, T> TableViewWrapper<S>.column(getter: KFunction<T>, op: TableColumn<S,T>.() -> Unit = {}): TableColumn<S, T> {
  return column(getter.name) {
	SimpleObjectProperty(getter.call(it.value))
  }.apply(op)
}

/**
 * Matt was here!
 */
@JvmName("coolColumn2")
fun <S, T> TableViewWrapper<S>.column(getter: KProperty1<S, T>, op: TableColumn<S,T>.() -> Unit = {}): TableColumn<S, T> {
  return column(getter.name) {
	SimpleObjectProperty(getter.call(it.value))
  }.apply(op)
}

/**
 * Matt was here!
 */
@JvmName("coolColumn")
inline fun <reified S, T> TreeTableViewWrapper<S>.column(getter: KFunction<T>, op: TreeTableColumn<S,T>.() -> Unit = {}): TreeTableColumn<S, T> {
  return column(getter.name) {
	SimpleObjectProperty(getter.call(it.value.value))
  }.apply(op)
}

/**
 * Matt was here!
 */
@JvmName("coolColumn2")
inline fun <reified S, T> TreeTableViewWrapper<S>.column(getter: KProperty1<S, T>, op: TreeTableColumn<S,T>.() -> Unit = {}): TreeTableColumn<S, T> {
  return column(getter.name) {
	SimpleObjectProperty(getter.call(it.value.value))
  }.apply(op)
}