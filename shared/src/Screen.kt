import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.github.aaziz993.cmp_lib.generated.resources.Res
import io.github.aaziz993.cmp_lib.generated.resources.flag_am
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
            Image(painterResource(Res.drawable.flag_am), null)
        }
    }
}
