package matt.hurricanefx.tornadofx.media

/*slightly modified code I stole from tornadofx*/

import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer

fun Media.play() = MediaPlayer(this).play()