import res.Res
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.painterResource

@Composable
public fun TestImage(): Unit = Image(painterResource(Res.drawable.compose_multiplatform), null)
