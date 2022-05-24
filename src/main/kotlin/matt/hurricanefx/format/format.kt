package matt.hurricanefx.format

import javafx.util.StringConverter
import matt.kjlib.byte.ByteSize
import matt.kjlib.date.formatDate
import matt.kjlib.jmath.sigFigs
import matt.kjlib.lang.NEVER
import java.util.Date

val BYTE_SIZE_FORMATTER = object: StringConverter<Number>() {
  override fun toString(`object`: Number?): String {
	if (`object` == null) return ""
	return ByteSize(`object`.toLong()).toString()
  }
  override fun fromString(string: String?) = NEVER
}

@Suppress("unused")
val UNIX_MS_FORMATTER = object: StringConverter<Number>() {
  override fun toString(`object`: Number?): String {
	if (`object` == null) return ""
	return Date(`object`.toLong()).formatDate()
  }

  override fun fromString(string: String?) = NEVER
}

@Suppress("unused")
val RATIO_TO_PERCENT_FORMATTER = object: StringConverter<Number>() {
  override fun toString(`object`: Number?): String {
	if (`object` == null) return ""
	return (`object`.toDouble()*100).sigFigs(3).toString() + "%"
  }

  override fun fromString(string: String?) = NEVER
}


val RATIO_TO_PERCENT_FORMATTER_NO_DECIMAL = object: StringConverter<Number>() {
  override fun toString(`object`: Number?): String {
	if (`object` == null) return ""
	return (`object`.toDouble()*100).toInt().toString() + "%"
  }
  override fun fromString(string: String?) = NEVER
}