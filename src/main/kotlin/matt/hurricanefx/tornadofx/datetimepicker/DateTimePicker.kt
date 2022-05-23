@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package matt.hurricanefx.tornadofx.datetimepicker

import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.value.ObservableValue
import javafx.scene.control.DatePicker
import javafx.util.StringConverter
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

/**
 * A DateTimePicker with configurable datetime format where both date and time can be changed
 * via the text field and the date can additionally be changed via the JavaFX default date picker.
 */
class DateTimePicker: DatePicker() {
  private var formatter: DateTimeFormatter? = null
  private val dateTimeValue: ObjectProperty<LocalDateTime?> = SimpleObjectProperty(LocalDateTime.now())
  private val format: ObjectProperty<String> = object: SimpleObjectProperty<String>() {
	override fun set(newValue: String) {
	  super.set(newValue)
	  formatter = DateTimeFormatter.ofPattern(newValue)
	}
  }

  fun alignColumnCountWithFormat() {
	editor.prefColumnCount = getFormat().length
  }

  fun pleasework(): LocalDateTime? {
	this.converter.fromString(this.editor.text)
	return dateTimeValue.get()
  }


  fun simulateEnterPressed() {
	editor.commitValue()
  }

  fun getDateTimeValue(): LocalDateTime? {
	return dateTimeValue.get()
  }

  fun setDateTimeValue(dateTimeValue: LocalDateTime?) {
	this.dateTimeValue.set(dateTimeValue)
  }

  fun dateTimeValueProperty(): ObjectProperty<LocalDateTime?> {
	return dateTimeValue
  }

  fun getFormat(): String {
	return format.get()
  }

  fun formatProperty(): ObjectProperty<String> {
	return format
  }

  fun setFormat(format: String) {
	this.format.set(format)
	alignColumnCountWithFormat()
  }

  internal inner class InternalConverter: StringConverter<LocalDate?>() {
	override fun toString(`object`: LocalDate?): String {
	  val value = getDateTimeValue()
	  return if (value != null) value.format(formatter) else ""
	}

	override fun fromString(value: String): LocalDate? {
	  if (value.isEmpty()) {
		dateTimeValue.set(null)
		return null
	  }
	  return try {
		dateTimeValue.set(LocalDateTime.parse(value, formatter))
		dateTimeValue.get()!!.toLocalDate()
	  } catch (e: DateTimeParseException) {
		dateTimeValue.set(null)
		null
	  }

	}
  }

  companion object {
	const val DefaultFormat = "yyyy-MM-dd HH:mm"
  }

  init {
	styleClass.add("datetime-picker")
	setFormat(DefaultFormat)
	converter = InternalConverter()
	alignColumnCountWithFormat()

	// Synchronize changes to the underlying date value back to the dateTimeValue
	valueProperty().addListener { _: ObservableValue<out LocalDate?>?, _: LocalDate?, newValue: LocalDate? ->
	  if (newValue == null) {
		dateTimeValue.set(null)
	  } else {
		if (dateTimeValue.get() == null) {
		  dateTimeValue.set(LocalDateTime.of(newValue, LocalTime.now()))
		} else {
		  val time = dateTimeValue.get()!!.toLocalTime()
		  dateTimeValue.set(LocalDateTime.of(newValue, time))
		}
	  }
	}

	// Synchronize changes to dateTimeValue back to the underlying date value
	dateTimeValue.addListener { _: ObservableValue<out LocalDateTime?>?, _: LocalDateTime?, newValue: LocalDateTime? ->
	  if (newValue != null) {
		val dateValue = newValue.toLocalDate()
		val forceUpdate = dateValue == valueProperty().get()
		// Make sure the display is updated even when the date itself wasn't changed
		value = dateValue
		if (forceUpdate) converter = InternalConverter()
	  } else {
		value = null
	  }
	}

	// Persist changes onblur
	editor.focusedProperty()
	  .addListener { _: ObservableValue<out Boolean?>?, _: Boolean?, newValue: Boolean? -> if (!newValue!!) simulateEnterPressed() }
  }
}