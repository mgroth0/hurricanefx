modtype = LIB

dependencies {
  implementation(kotlin("reflect"))
  api(projects.kj.hurricanefx.eye)
  api(libs.fx.controls)
  api(projects.kj.fx.image)
  implementation(projects.kj.caching)
}