package matt.hurricanefx.tornadofx.shapes

/*slightly modified code I stole from tornadofx*/

import javafx.scene.Parent
import javafx.scene.shape.ArcTo
import javafx.scene.shape.ClosePath
import javafx.scene.shape.CubicCurveTo
import javafx.scene.shape.FillRule
import javafx.scene.shape.HLineTo
import javafx.scene.shape.LineTo
import javafx.scene.shape.MoveTo
import javafx.scene.shape.PathElement
import javafx.scene.shape.QuadCurveTo
import javafx.scene.shape.VLineTo
import matt.hurricanefx.tornadofx.fx.attachTo
import matt.hurricanefx.wrapper.ArcWrapper
import matt.hurricanefx.wrapper.CircleWrapper
import matt.hurricanefx.wrapper.CubicCurveWrapper
import matt.hurricanefx.wrapper.EllipseWrapper
import matt.hurricanefx.wrapper.LineWrapper
import matt.hurricanefx.wrapper.NodeWrapper
import matt.hurricanefx.wrapper.PathWrapper
import matt.hurricanefx.wrapper.PolygonWrapper
import matt.hurricanefx.wrapper.PolylineWrapper
import matt.hurricanefx.wrapper.QuadCurveWrapper
import matt.hurricanefx.wrapper.RectangleWrapper
import matt.hurricanefx.wrapper.SVGPathWrapper

fun NodeWrapper<out Parent>.arc(
  centerX: Number = 0.0,
  centerY: Number = 0.0,
  radiusX: Number = 0.0,
  radiusY: Number = 0.0,
  startAngle: Number = 0.0,
  length: Number = 0.0,
  op: ArcWrapper.()->Unit = {}
) =
  ArcWrapper(
	centerX.toDouble(), centerY.toDouble(), radiusX.toDouble(), radiusY.toDouble(), startAngle.toDouble(),
	length.toDouble()
  ).attachTo(this, op)

fun NodeWrapper<out Parent>.circle(
  centerX: Number = 0.0,
  centerY: Number = 0.0,
  radius: Number = 0.0,
  op: CircleWrapper.()->Unit = {}
) =
  CircleWrapper(centerX.toDouble(), centerY.toDouble(), radius.toDouble()).attachTo(this, op)

fun NodeWrapper<out Parent>.cubiccurve(
  startX: Number = 0.0,
  startY: Number = 0.0,
  controlX1: Number = 0.0,
  controlY1: Number = 0.0,
  controlX2: Number = 0.0,
  controlY2: Number = 0.0,
  endX: Number = 0.0,
  endY: Number = 0.0,
  op: CubicCurveWrapper.()->Unit = {}
) =
  CubicCurveWrapper(
	startX.toDouble(), startY.toDouble(), controlX1.toDouble(), controlY1.toDouble(), controlX2.toDouble(),
	controlY2.toDouble(), endX.toDouble(), endY.toDouble()
  ).attachTo(this, op)

fun NodeWrapper<out Parent>.ellipse(
  centerX: Number = 0.0,
  centerY: Number = 0.0,
  radiusX: Number = 0.0,
  radiusY: Number = 0.0,
  op: EllipseWrapper.()->Unit = {}
) =
  EllipseWrapper(centerX.toDouble(), centerY.toDouble(), radiusX.toDouble(), radiusY.toDouble()).attachTo(this, op)

fun NodeWrapper<out Parent>.line(
  startX: Number = 0.0,
  startY: Number = 0.0,
  endX: Number = 0.0,
  endY: Number = 0.0,
  op: LineWrapper.()->Unit = {}
) =
  LineWrapper(startX.toDouble(), startY.toDouble(), endX.toDouble(), endY.toDouble()).attachTo(this, op)

fun NodeWrapper<out Parent>.path(vararg elements: PathElement, op: PathWrapper.()->Unit = {}) =
  PathWrapper(*elements).attachTo(this, op)

fun PathWrapper.moveTo(x: Number = 0.0, y: Number = 0.0) = apply {
  elements.add(MoveTo(x.toDouble(), y.toDouble()))
}

fun PathWrapper.hlineTo(x: Number) = apply { elements.add(HLineTo(x.toDouble())) }

fun PathWrapper.vlineTo(y: Number) = apply { elements.add(VLineTo(y.toDouble())) }

fun PathWrapper.quadcurveTo(
  controlX: Number = 0.0,
  controlY: Number = 0.0,
  x: Number = 0.0,
  y: Number = 0.0,
  op: QuadCurveTo.()->Unit = {}
) = apply {
  elements.add(QuadCurveTo(controlX.toDouble(), controlY.toDouble(), x.toDouble(), y.toDouble()).also(op))
}

fun PathWrapper.cubiccurveTo(
  controlX1: Number = 0.0,
  controlY1: Number = 0.0,
  controlX2: Number = 0.0,
  controlY2: Number = 0.0,
  x: Number = 0.0,
  y: Number = 0.0,
  op: CubicCurveTo.()->Unit = {}
) = apply {
  elements.add(
	CubicCurveTo(
	  controlX1.toDouble(), controlY1.toDouble(), controlX2.toDouble(), controlY2.toDouble(), x.toDouble(), y.toDouble()
	).also(op)
  )
}

fun PathWrapper.lineTo(x: Number = 0.0, y: Number = 0.0) = apply {
  elements.add(LineTo(x.toDouble(), y.toDouble()))
}

fun PathWrapper.arcTo(
  radiusX: Number = 0.0, radiusY: Number = 0.0,
  xAxisRotation: Number = 0.0, x: Number = 0.0,
  y: Number = 0.0, largeArcFlag: Boolean = false,
  sweepFlag: Boolean = false, op: ArcTo.()->Unit = {}
) = apply {
  elements.add(
	ArcTo(
	  radiusX.toDouble(), radiusY.toDouble(), xAxisRotation.toDouble(), x.toDouble(), y.toDouble(), largeArcFlag,
	  sweepFlag
	).also(op)
  )
}

fun PathWrapper.closepath() = apply { elements.add(ClosePath()) }

fun NodeWrapper<out Parent>.polygon(vararg points: Number, op: PolygonWrapper.()->Unit = {}) =
  PolygonWrapper(*points.map(Number::toDouble).toDoubleArray()).attachTo(this, op)

fun NodeWrapper<out Parent>.polyline(vararg points: Number, op: PolylineWrapper.()->Unit = {}) =
  PolylineWrapper(*points.map(Number::toDouble).toDoubleArray()).attachTo(this, op)

fun NodeWrapper<out Parent>.quadcurve(
  startX: Number = 0.0,
  startY: Number = 0.0,
  controlX: Number = 0.0,
  controlY: Number = 0.0,
  endX: Number = 0.0,
  endY: Number = 0.0,
  op: QuadCurveWrapper.()->Unit = {}
) =
  QuadCurveWrapper(
	startX.toDouble(), startY.toDouble(), controlX.toDouble(), controlY.toDouble(), endX.toDouble(), endY.toDouble()
  ).attachTo(this, op)

fun NodeWrapper<out Parent>.rectangle(
  x: Number = 0.0,
  y: Number = 0.0,
  width: Number = 0.0,
  height: Number = 0.0,
  op: RectangleWrapper.()->Unit = {}
) =
  RectangleWrapper(x.toDouble(), y.toDouble(), width.toDouble(), height.toDouble()).attachTo(this, op)

fun NodeWrapper<out Parent>.svgpath(
  content: String? = null,
  fillRule: FillRule? = null,
  op: SVGPathWrapper.()->Unit = {}
) = SVGPathWrapper().attachTo(this, op) {
  if (content != null) it.content = content
  if (fillRule != null) it.fillRule = fillRule
}