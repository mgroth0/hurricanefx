@file:Suppress("UNCHECKED_CAST", "CAST_NEVER_SUCCEEDS")

/*slightly modified code I stole from tornadofx*/

package matt.hurricanefx.tornadofx.bind

import javafx.beans.binding.Bindings
import javafx.beans.binding.BooleanBinding
import javafx.beans.binding.BooleanExpression
import javafx.beans.property.Property
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.StringProperty
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.beans.value.WritableValue
import javafx.collections.FXCollections
import javafx.collections.ObservableList
//import javafx.scene.control.*
//import javafx.scene.paint.Color
//import javafx.scene.text.Text
import javafx.util.StringConverter
import javafx.util.converter.*
import matt.hurricanefx.eye.lib.onChange
import java.math.BigDecimal
import java.math.BigInteger
import java.text.Format
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*
import java.util.concurrent.Callable




inline fun <reified S : T, reified T : Any> bindStringProperty(
    stringProperty: StringProperty,
    converter: StringConverter<T>?,
    format: Format?,
    property: ObservableValue<S>,
    readonly: Boolean
) {
    if (stringProperty.isBound) stringProperty.unbind()
    val effectiveReadonly = readonly || property !is Property<S> || S::class != T::class

    if (S::class == String::class) when {
        effectiveReadonly -> stringProperty.bind(property as ObservableValue<String>)
        else -> stringProperty.bindBidirectional(property as Property<String>)
    } else {
        val effectiveConverter = if (format != null) null else converter ?: getDefaultConverter<S>()
        if (effectiveReadonly) {
            val toStringConverter = Callable {
                when {
                    converter != null -> converter.toString(property.value)
                    format != null -> format.format(property.value)
                    else -> property.value?.toString()
                }
            }
            val stringBinding = Bindings.createStringBinding(toStringConverter, property)
            stringProperty.bind(stringBinding)
        } else when {
            effectiveConverter != null -> stringProperty.bindBidirectional(property as Property<S>, effectiveConverter as StringConverter<S>)
            format != null -> stringProperty.bindBidirectional(property as Property<S>, format)
            else -> throw IllegalArgumentException("Cannot convert from ${S::class} to String without an explicit converter or format")
        }
    }
}

inline fun <reified T : Any> getDefaultConverter() = when (T::class.javaPrimitiveType ?: T::class) {
    Int::class.javaPrimitiveType -> IntegerStringConverter()
    Long::class.javaPrimitiveType -> LongStringConverter()
    Double::class.javaPrimitiveType -> DoubleStringConverter()
    Float::class.javaPrimitiveType -> FloatStringConverter()
    Date::class -> DateStringConverter()
    BigDecimal::class -> BigDecimalStringConverter()
    BigInteger::class -> BigIntegerStringConverter()
    Number::class -> NumberStringConverter()
    LocalDate::class -> LocalDateStringConverter()
    LocalTime::class -> LocalTimeStringConverter()
    LocalDateTime::class -> LocalDateTimeStringConverter()
    Boolean::class.javaPrimitiveType -> BooleanStringConverter()
    else -> null
} as StringConverter<T>?

fun ObservableValue<Boolean>.toBinding() = object : BooleanBinding() {
    init {
        super.bind(this@toBinding)
    }

    override fun dispose() {
        super.unbind(this@toBinding)
    }

    override fun computeValue() = this@toBinding.value

    override fun getDependencies(): ObservableList<*> = FXCollections.singletonObservableList(this@toBinding)
}

fun <T, N> ObservableValue<T>.select(nested: (T) -> ObservableValue<N>): Property<N> {
    fun extractNested(): ObservableValue<N>? = value?.let(nested)

    var currentNested: ObservableValue<N>? = extractNested()

    return object : SimpleObjectProperty<N>() {
        val changeListener = ChangeListener<Any?> { _, _, _ ->
            invalidated()
            fireValueChangedEvent()
        }

        init {
            currentNested?.addListener(changeListener)
            this@select.addListener(changeListener)
        }

        override fun invalidated() {
            currentNested?.removeListener(changeListener)
            currentNested = extractNested()
            currentNested?.addListener(changeListener)
        }

        override fun get() = currentNested?.value

        override fun set(v: N?) {
            (currentNested as? WritableValue<N>)?.value = v
            super.set(v)
        }

    }

}

fun <T> ObservableValue<T>.selectBoolean(nested: (T) -> BooleanExpression): BooleanExpression {
    fun extractNested() = nested(value)

    val dis = this
    var currentNested = extractNested()

    return object : SimpleBooleanProperty() {
        val changeListener = ChangeListener<Boolean> { _, _, _ ->
            currentNested = extractNested()
            fireValueChangedEvent()
        }

        init {
            dis.onChange {
                fireValueChangedEvent()
                invalidated()
            }
        }

        override fun invalidated() {
            currentNested.removeListener(changeListener)
            currentNested = extractNested()
            currentNested.addListener(changeListener)
        }

        override fun getValue() = currentNested.value

        override fun setValue(v: Boolean?) {
            (currentNested as? WritableValue<*>)?.value = v
            super.setValue(v)
        }

    }

}
