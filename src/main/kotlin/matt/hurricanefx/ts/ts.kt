package matt.hurricanefx.ts

import javafx.application.Platform.runLater
import javafx.beans.property.SimpleStringProperty

class ThreadSafeStringProp: SimpleStringProperty() {
  override fun set(newValue: String?) {
	runLater {
	  super.set(newValue)
	}
  }

  override fun setValue(v: String?) {
	runLater {
	  super.setValue(v)
	}
  }
}