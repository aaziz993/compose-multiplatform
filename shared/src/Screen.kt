import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cmp.lib.resources.Res
import cmp.lib.resources.flag_af
import cmp.lib.resources.flag_bz
import org.jetbrains.compose.resources.painterResource

@Composable
public fun Screen() {
    MaterialTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            BasicText("Hello!")
            TestImage()
            Image(painterResource(Res.drawable.flag_bz), null)
        }
    }
}
