modtype = LIB

dependencies {
  implementation(kotlin("reflect"))
  api(projects.kj.hurricanefx.hurricanefxEye)
  api(libs.fx.controls)
  api(projects.k.fx.fxImage)
  implementation(projects.k.caching)
  implementation(projects.k.stream)
  implementation(projects.k.async)
}