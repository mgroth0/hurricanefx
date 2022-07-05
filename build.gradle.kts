modtype = LIB

dependencies {
  implementation(kotlin("reflect"))
  api(projects.k.hurricanefx.eye)
  api(libs.fx.controls)
  api(projects.k.fx.image)
  implementation(projects.k.caching)
  implementation(projects.k.stream)
  implementation(projects.k.async)
}