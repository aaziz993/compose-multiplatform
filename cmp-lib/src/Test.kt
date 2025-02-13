import abrakadabra.Res
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.painterResource

@Composable
fun TestImage() = Image(painterResource(Res.drawable.compose_multiplatform), null)
