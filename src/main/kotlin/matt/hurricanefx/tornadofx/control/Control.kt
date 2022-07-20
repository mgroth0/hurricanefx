@file:Suppress("UNCHECKED_CAST")

/*slightly modified code I stole from tornadofx*/

package matt.hurricanefx.tornadofx.control

import javafx.beans.property.ObjectProperty
import javafx.beans.property.Property
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.value.ObservableValue
import javafx.collections.ObservableMap
import javafx.event.EventTarget
import javafx.geometry.Orientation
import javafx.scene.Node
import javafx.scene.control.Button
import javafx.scene.control.ButtonBar
import javafx.scene.control.ButtonBase
import javafx.scene.control.CheckBox
import javafx.scene.control.CheckMenuItem
import javafx.scene.control.ChoiceBox
import javafx.scene.control.ColorPicker
import javafx.scene.control.ComboBoxBase
import javafx.scene.control.DatePicker
import javafx.scene.control.Hyperlink
import javafx.scene.control.Label
import javafx.scene.control.Labeled
import javafx.scene.control.ListView
import javafx.scene.control.MenuBar
import javafx.scene.control.MenuButton
import javafx.scene.control.MenuItem
import javafx.scene.control.PasswordField
import javafx.scene.control.ProgressBar
import javafx.scene.control.ProgressIndicator
import javafx.scene.control.RadioButton
import javafx.scene.control.Slider
import javafx.scene.control.Spinner
import javafx.scene.control.SplitMenuButton
import javafx.scene.control.Tab
import javafx.scene.control.TableView
import javafx.scene.control.TextArea
import javafx.scene.control.TextField
import javafx.scene.control.TextInputControl
import javafx.scene.control.TitledPane
import javafx.scene.control.ToggleButton
import javafx.scene.control.ToggleGroup
import javafx.scene.control.ToolBar
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import javafx.scene.text.Text
import javafx.scene.text.TextFlow
import javafx.util.StringConverter
import matt.hurricanefx.eye.bind.bindStringProperty
import matt.hurricanefx.eye.bind.internalBind
import matt.hurricanefx.eye.lib.onChange
import matt.hurricanefx.eye.prop.objectBinding
import matt.hurricanefx.eye.prop.stringBinding
import matt.hurricanefx.eye.sflist.SortedFilteredList
import matt.hurricanefx.tornadofx.fx.attachTo
import matt.hurricanefx.tornadofx.fx.opcr
import matt.hurricanefx.wrapper.ButtonWrapper
import matt.hurricanefx.wrapper.EventTargetWrapper
import matt.hurricanefx.wrapper.wrapped
import matt.klib.lang.err
import java.text.Format
import java.time.LocalDate
import kotlin.contracts.InvocationKind.EXACTLY_ONCE
import kotlin.contracts.contract


fun EventTargetWrapper<*>.colorpicker(
  color: Color? = null,
  op: ColorPicker.()->Unit = {}
) = ColorPicker().wrapped().attachTo(this, op) {
  if (color != null) it.value = color
}

fun EventTargetWrapper<*>.colorpicker(
  colorProperty: ObjectProperty<Color>,
  op: ColorPicker.()->Unit = {}
) = ColorPicker().apply { bind(colorProperty) }.attachTo(this, op) {
}

fun EventTargetWrapper<*>.textflow(op: TextFlow.()->Unit = {}) = TextFlow().attachTo(this, op)

fun EventTargetWrapper<*>.text(op: Text.()->Unit = {}) = Text().attachTo(this, op)

internal val EventTarget.properties: ObservableMap<Any, Any>
  get() = when (this) {
	is Node     -> properties
	is Tab      -> properties
	is MenuItem -> properties
	else        -> throw IllegalArgumentException("Don't know how to extract properties object from $this")
  }

//var EventTarget.tagProperty: Property<Any?>
//    get() = properties.getOrPut("tornadofx.value") {
//        SimpleObjectProperty<Any?>()
//    } as SimpleObjectProperty<Any?>
//    set(value) {
//        properties["tornadofx.value"] = value
//    }

//var EventTarget.tag: Any?
//    get() = tagProperty.value
//    set(value) {
//        tagProperty.value = value
//    }

@Deprecated(
  "Properties set on the fake node would be lost. Do not use this function.",
  ReplaceWith("Manually adding children")
)
fun children(addTo: MutableList<Node>, op: Pane.()->Unit) {
  val fake = Pane().also(op)
  addTo.addAll(fake.children)
}


fun EventTargetWrapper<*>.text(initialValue: String? = null, op: Text.()->Unit = {}) = Text().attachTo(this, op) {
  if (initialValue != null) it.text = initialValue
}


fun EventTargetWrapper<*>.text(property: Property<String>, op: Text.()->Unit = {}) = text().apply {
  bind(property)
  op(this)
}


fun EventTargetWrapper<*>.text(observable: ObservableValue<String>, op: Text.()->Unit = {}) = text().apply {
  bind(observable)
  op(this)
}




fun EventTargetWrapper<*>.textfield(value: String? = null, op: TextField.()->Unit = {}) = TextField().attachTo(this, op) {
  if (value != null) it.text = value
}

fun EventTargetWrapper<*>.textfield(property: ObservableValue<String>, op: TextField.()->Unit = {}) = textfield().apply {
  bind(property)
  op(this)
}

@JvmName("textfieldNumber")
fun EventTargetWrapper<*>.textfield(property: ObservableValue<Number>, op: TextField.()->Unit = {}) = textfield().apply {
  bind(property)
  op(this)
}

@JvmName("textfieldInt")
fun EventTargetWrapper<*>.textfield(property: ObservableValue<Int>, op: TextField.()->Unit = {}) = textfield().apply {
  bind(property)
  op(this)
}

fun EventTargetWrapper<*>.passwordfield(value: String? = null, op: PasswordField.()->Unit = {}) =
  PasswordField().attachTo(this, op) {
	if (value != null) it.text = value
  }

fun EventTargetWrapper<*>.passwordfield(property: ObservableValue<String>, op: PasswordField.()->Unit = {}) =
  passwordfield().apply {
	bind(property)
	op(this)
  }

fun <T> EventTargetWrapper<*>.textfield(property: Property<T>, converter: StringConverter<T>, op: TextField.()->Unit = {}) =
  textfield().apply {
	textProperty().bindBidirectional(property, converter)
	op(this)
  }

fun EventTargetWrapper<*>.datepicker(op: DatePicker.()->Unit = {}) = DatePicker().attachTo(this, op)
fun EventTargetWrapper<*>.datepicker(property: Property<LocalDate>, op: DatePicker.()->Unit = {}) = datepicker().apply {
  bind(property)
  op(this)
}

fun EventTargetWrapper<*>.textarea(value: String? = null, op: TextArea.()->Unit = {}) = TextArea().attachTo(this, op) {
  if (value != null) it.text = value
}


fun EventTargetWrapper<*>.textarea(property: ObservableValue<String>, op: TextArea.()->Unit = {}) = textarea().apply {
  bind(property)
  op(this)
}


fun <T> EventTargetWrapper<*>.textarea(property: Property<T>, converter: StringConverter<T>, op: TextArea.()->Unit = {}) =
  textarea().apply {
	textProperty().bindBidirectional(property, converter)
	op(this)
  }


fun EventTargetWrapper<*>.buttonbar(buttonOrder: String? = null, op: (ButtonBar.()->Unit)) = ButtonBar().attachTo(this, op) {
  if (buttonOrder != null) it.buttonOrder = buttonOrder
}


fun EventTargetWrapper<*>.checkbox(text: String? = null, property: Property<Boolean>? = null, op: CheckBox.()->Unit = {}) =
  CheckBox(text).attachTo(this, op) {
	if (property != null) it.bind(property)
  }


fun EventTargetWrapper<*>.progressindicator(op: ProgressIndicator.()->Unit = {}) = ProgressIndicator().attachTo(this, op)
fun EventTargetWrapper<*>.progressindicator(property: Property<Number>, op: ProgressIndicator.()->Unit = {}) =
  progressindicator().apply {
	bind(property)
	op(this)
  }

fun EventTargetWrapper<*>.progressbar(initialValue: Double? = null, op: ProgressBar.()->Unit = {}) =
  ProgressBar().attachTo(this, op) {
	if (initialValue != null) it.progress = initialValue
  }

fun EventTargetWrapper<*>.progressbar(property: ObservableValue<Number>, op: ProgressBar.()->Unit = {}) = progressbar().apply {
  bind(property)
  op(this)
}

fun EventTargetWrapper<*>.slider(
  min: Number? = null,
  max: Number? = null,
  value: Number? = null,
  orientation: Orientation? = null,
  op: Slider.()->Unit = {}
) = Slider().attachTo(this, op) {
  if (min != null) it.min = min.toDouble()
  if (max != null) it.max = max.toDouble()
  if (value != null) it.value = value.toDouble()
  if (orientation != null) it.orientation = orientation
}

fun <T> EventTargetWrapper<*>.slider(
  range: ClosedRange<T>,
  value: Number? = null,
  orientation: Orientation? = null,
  op: Slider.()->Unit = {}
): Slider
	where T: Comparable<T>,
		  T: Number {
  return slider(range.start, range.endInclusive, value, orientation, op)
}


// Buttons
inline fun EventTargetWrapper<*>.button(text: String = "", graphic: Node? = null, crossinline op: ButtonWrapper.()->Unit = {}): ButtonWrapper {
  contract {
	callsInPlace(op, EXACTLY_ONCE)
  }
  return ButtonWrapper{
	this.text = text
	if (graphic != null) this.graphic = graphic
	op()
  }.attachTo(this, op)
}

fun EventTargetWrapper<*>.menubutton(text: String = "", graphic: Node? = null, op: MenuButton.()->Unit = {}) =
  MenuButton(text).attachTo(this, op) {
	if (graphic != null) it.graphic = graphic
  }

fun EventTargetWrapper<*>.splitmenubutton(text: String? = null, graphic: Node? = null, op: SplitMenuButton.()->Unit = {}) =
  SplitMenuButton().attachTo(this, op) {
	if (text != null) it.text = text
	if (graphic != null) it.graphic = graphic
  }

fun EventTargetWrapper<*>.button(text: ObservableValue<String>, graphic: Node? = null, op: Button.()->Unit = {}) =
  Button().attachTo(this, op) {
	it.textProperty().bind(text)
	if (graphic != null) it.graphic = graphic
  }.wrapped()

fun ToolBar.button(text: String = "", graphic: Node? = null, op: Button.()->Unit = {}) = Button(text).also {
  if (graphic != null) it.graphic = graphic
  items += it
  op(it)
}.wrapped()

fun ToolBar.button(text: ObservableValue<String>, graphic: Node? = null, op: Button.()->Unit = {}) = Button().also {
  it.textProperty().bind(text)
  if (graphic != null) it.graphic = graphic
  items += it
  op(it)
}.wrapped()

fun ButtonBar.button(
  text: String = "",
  type: ButtonBar.ButtonData? = null,
  graphic: Node? = null,
  op: Button.()->Unit = {}
) = Button(text).also {
  if (type != null) ButtonBar.setButtonData(it, type)
  if (graphic != null) it.graphic = graphic
  buttons += it
  op(it)
}.wrapped()

fun ButtonBar.button(
  text: ObservableValue<String>,
  type: ButtonBar.ButtonData? = null,
  graphic: Node? = null,
  op: Button.()->Unit = {}
) = Button().also {
  it.textProperty().bind(text)
  if (type != null) ButtonBar.setButtonData(it, type)
  if (graphic != null) it.graphic = graphic
  buttons += it
  op(it)
}.wrapped()

fun Node.togglegroup(property: ObservableValue<Any>? = null, op: ToggleGroup.()->Unit = {}) =
  ToggleGroup().also { tg ->
	properties["matt.tornadofx.togglegroup"] = tg
	property?.let { tg.bind(it) }
	op(tg)
  }

/**
 * Bind the selectedValueProperty of this toggle group to the given property. Passing in a writeable value
 * will result in a bidirectional matt.klib.bind.binding, while passing in a read only value will result in a unidirectional matt.klib.bind.binding.
 *
 * If the toggles are configured with the value parameter (@see #togglebutton and #radiogroup), the corresponding
 * button will be selected when the value is changed. Likewise, if the selected toggle is changed,
 * the property value will be updated if it is writeable.
 */
fun <T> ToggleGroup.bind(property: ObservableValue<T>) = selectedValueProperty<T>().apply {
  (property as? Property<T>)?.also { bindBidirectional(it) }
	?: bind(property)
}

/**
 * Generates a writable property that represents the selected value for this toggele group.
 * If the toggles are configured with a value (@see #togglebutton and #radiogroup) the corresponding
 * toggle will be selected when this value is changed. Likewise, if the toggle is changed by clicking
 * it, the value for the toggle will be written to this property.
 *
 * To bind to this property, use the #ToggleGroup.bind() function.
 */
fun <T> ToggleGroup.selectedValueProperty(): ObjectProperty<T> =
  properties.getOrPut("matt.tornadofx.selectedValueProperty") {
	SimpleObjectProperty<T>().apply {
	  selectedToggleProperty().onChange {
		value = it?.properties?.get("tornadofx.toggleGroupValue") as T?
	  }
	  onChange { selectedValue ->
		selectToggle(toggles.find { it.properties["tornadofx.toggleGroupValue"] == selectedValue })
	  }
	}
  } as ObjectProperty<T>

/**
 * Create a togglebutton inside the current or given toggle group. The optional value parameter will be matched against
 * the extension property `selectedValueProperty()` on Toggle Group. If the #ToggleGroup.selectedValueProperty is used,
 * it's value will be updated to reflect the value for this radio button when it's selected.
 *
 * Likewise, if the `selectedValueProperty` of the ToggleGroup is updated to a value that matches the value for this
 * togglebutton, it will be automatically selected.
 */
fun EventTargetWrapper<*>.togglebutton(
  text: String? = null,
  group: ToggleGroup? = getToggleGroup(),
  selectFirst: Boolean = true,
  value: Any? = null,
  op: ToggleButton.()->Unit = {}
) = ToggleButton().attachTo(this, op) {
  it.text = if (value != null && text == null) value.toString() else text ?: ""
  it.properties["tornadofx.toggleGroupValue"] = value ?: text
  if (group != null) it.toggleGroup = group
  if (it.toggleGroup?.selectedToggle == null && selectFirst) it.isSelected = true
}

fun EventTargetWrapper<*>.togglebutton(
  text: ObservableValue<String>? = null,
  group: ToggleGroup? = getToggleGroup(),
  selectFirst: Boolean = true,
  value: Any? = null,
  op: ToggleButton.()->Unit = {}
) = ToggleButton().attachTo(this, op) {
  it.textProperty().bind(text)
  it.properties["tornadofx.toggleGroupValue"] = value ?: text
  if (group != null) it.toggleGroup = group
  if (it.toggleGroup?.selectedToggle == null && selectFirst) it.isSelected = true
}

fun EventTargetWrapper<*>.togglebutton(
  group: ToggleGroup? = getToggleGroup(),
  selectFirst: Boolean = true,
  value: Any? = null,
  op: ToggleButton.()->Unit = {}
) = ToggleButton().attachTo(this, op) {
  it.properties["tornadofx.toggleGroupValue"] = value
  if (group != null) it.toggleGroup = group
  if (it.toggleGroup?.selectedToggle == null && selectFirst) it.isSelected = true
}

fun ToggleButton.whenSelected(op: ()->Unit) {
  selectedProperty().onChange { if (it) op() }
}

/**
 * Create a radiobutton inside the current or given toggle group. The optional value parameter will be matched against
 * the extension property `selectedValueProperty()` on Toggle Group. If the #ToggleGroup.selectedValueProperty is used,
 * it's value will be updated to reflect the value for this radio button when it's selected.
 *
 * Likewise, if the `selectedValueProperty` of the ToggleGroup is updated to a value that matches the value for this
 * radiobutton, it will be automatically selected.
 */
fun EventTargetWrapper<*>.radiobutton(
  text: String? = null,
  group: ToggleGroup? = getToggleGroup(),
  value: Any? = null,
  op: RadioButton.()->Unit = {}
) = RadioButton().attachTo(this, op) {
  it.text = if (value != null && text == null) value.toString() else text ?: ""
  it.properties["tornadofx.toggleGroupValue"] = value ?: text
  if (group != null) it.toggleGroup = group
}

fun EventTargetWrapper<*>.label(text: String = "", graphic: Node? = null, op: Label.()->Unit = {}) =
  Label(text).attachTo(this, op) {
	if (graphic != null) it.graphic = graphic
  }


inline fun <reified T> EventTargetWrapper<*>.label(
  observable: ObservableValue<T>,
  graphicProperty: ObservableValue<Node>? = null,
  converter: StringConverter<in T>? = null,
  noinline op: Label.()->Unit = {}
) = label().apply {
  if (converter == null) {
	if (T::class == String::class) {
	  @Suppress("UNCHECKED_CAST")
	  textProperty().bind(observable as ObservableValue<String>)
	} else {
	  textProperty().bind(observable.stringBinding { it?.toString() })
	}
  } else {
	textProperty().bind(observable.stringBinding { converter.toString(it) })
  }
  if (graphic != null) graphicProperty().bind(graphicProperty)
  op(this)
}


fun EventTargetWrapper<*>.hyperlink(text: String = "", graphic: Node? = null, op: Hyperlink.()->Unit = {}) =
  Hyperlink(text, graphic).attachTo(this, op)

fun EventTargetWrapper<*>.hyperlink(observable: ObservableValue<String>, graphic: Node? = null, op: Hyperlink.()->Unit = {}) =
  hyperlink(graphic = graphic).apply {
	bind(observable)
	op(this)
  }

fun EventTargetWrapper<*>.menubar(op: MenuBar.()->Unit = {}) = MenuBar().attachTo(this, op)

fun EventTargetWrapper<*>.imageview(url: String? = null, lazyload: Boolean = true, op: ImageView.()->Unit = {}) =
  opcr(this, if (url == null) ImageView() else ImageView(Image(url, lazyload)), op)

fun EventTargetWrapper<*>.imageview(
  url: ObservableValue<String>,
  lazyload: Boolean = true,
  op: ImageView.()->Unit = {}
) = ImageView().attachTo(this, op) { imageView ->
  imageView.imageProperty().bind(objectBinding(url) { value?.let { Image(it, lazyload) } })
}

fun EventTargetWrapper<*>.imageview(image: ObservableValue<Image?>, op: ImageView.()->Unit = {}) =
  ImageView().attachTo(this, op) {
	it.imageProperty().bind(image)
  }

fun EventTargetWrapper<*>.imageview(image: Image, op: ImageView.()->Unit = {}) = ImageView(image).attachTo(this, op)

/**
 * Listen to changes and update the value of the property if the given mutator results in a different value
 */
fun <T: Any?> Property<T>.mutateOnChange(mutator: (T?)->T?) = onChange {
  val changed = mutator(value)
  if (changed != value) value = changed
}

/**
 * Remove leading or trailing whitespace from a Text Input Control.
 */
fun TextInputControl.trimWhitespace() = focusedProperty().onChange { focused: Boolean? ->
  if (focused == null) err("here it is")
  if (!focused && text != null) text = text.trim()
}

/**
 * Remove any whitespace from a Text Input Control.
 */
fun TextInputControl.stripWhitespace() = textProperty().mutateOnChange { it?.replace(Regex("\\s*"), "") }

/**
 * Remove any non integer values from a Text Input Control.
 */
fun TextInputControl.stripNonInteger() = textProperty().mutateOnChange { it?.replace(Regex("[^0-9-]"), "") }

/**
 * Remove any non integer values from a Text Input Control.
 */
fun TextInputControl.stripNonNumeric(vararg allowedChars: String = arrayOf(".", ",", "-")) =
  textProperty().mutateOnChange { it?.replace(Regex("[^0-9${allowedChars.joinToString("")}]"), "") }

fun ChoiceBox<*>.action(op: ()->Unit) = setOnAction { op() }
fun ButtonBase.action(op: ()->Unit) = setOnAction { op() }
fun TextField.action(op: ()->Unit) = setOnAction { op() }
fun MenuItem.action(op: ()->Unit) = setOnAction { op() }


fun <T> ComboBoxBase<T>.bind(property: ObservableValue<T>, readonly: Boolean = false) =
  valueProperty().internalBind(property, readonly)

fun ColorPicker.bind(property: ObservableValue<Color>, readonly: Boolean = false) =
  valueProperty().internalBind(property, readonly)

fun DatePicker.bind(property: ObservableValue<LocalDate>, readonly: Boolean = false) =
  valueProperty().internalBind(property, readonly)

fun ProgressIndicator.bind(property: ObservableValue<Number>, readonly: Boolean = false) =
  progressProperty().internalBind(property, readonly)

fun <T> ChoiceBox<T>.bind(property: ObservableValue<T>, readonly: Boolean = false) =
  valueProperty().internalBind(property, readonly)

fun CheckBox.bind(property: ObservableValue<Boolean>, readonly: Boolean = false) =
  selectedProperty().internalBind(property, readonly)

fun CheckMenuItem.bind(property: ObservableValue<Boolean>, readonly: Boolean = false) =
  selectedProperty().internalBind(property, readonly)

fun Slider.bind(property: ObservableValue<Number>, readonly: Boolean = false) =
  valueProperty().internalBind(property, readonly)

fun <T> Spinner<T>.bind(property: ObservableValue<T>, readonly: Boolean = false) =
  valueFactory.valueProperty().internalBind(property, readonly)


inline fun <reified S: T, reified T: Any> Labeled.bind(
  property: ObservableValue<S>,
  readonly: Boolean = false,
  converter: StringConverter<T>? = null,
  format: Format? = null
) {
  bindStringProperty(textProperty(), converter, format, property, readonly)
}

inline fun <reified S: T, reified T: Any> TitledPane.bind(
  property: ObservableValue<S>,
  readonly: Boolean = false,
  converter: StringConverter<T>? = null,
  format: Format? = null
) = bindStringProperty(textProperty(), converter, format, property, readonly)

inline fun <reified S: T, reified T: Any> Text.bind(
  property: ObservableValue<S>,
  readonly: Boolean = false,
  converter: StringConverter<T>? = null,
  format: Format? = null
) = bindStringProperty(textProperty(), converter, format, property, readonly)

inline fun <reified S: T, reified T: Any> TextInputControl.bind(
  property: ObservableValue<S>,
  readonly: Boolean = false,
  converter: StringConverter<T>? = null,
  format: Format? = null
) = bindStringProperty(textProperty(), converter, format, property, readonly)


/**
 * Bind this data object to the given TableView.
 *
 * The `tableView.items` is set to the underlying sortedItems.
 *
 * The underlying sortedItems.comparatorProperty` is automatically bound to `tableView.comparatorProperty`.
 */
fun <T> SortedFilteredList<T>.bindTo(tableView: TableView<T>): SortedFilteredList<T> = apply {
  tableView.items = this
  sortedItems.comparatorProperty().bind(tableView.comparatorProperty())
}

/**
 * Bind this data object to the given ListView.
 *
 * The `listView.items` is set to the underlying sortedItems.
 *
 */
fun <T> SortedFilteredList<T>.bindTo(listView: ListView<T>): SortedFilteredList<T> = apply { listView.items = this }