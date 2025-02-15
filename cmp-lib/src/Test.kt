import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.painterResource
import resources.Res
import resources.compose_multiplatform

@Composable
public fun TestImage(): Unit = Image(painterResource(Res.drawable.compose_multiplatform), null)
