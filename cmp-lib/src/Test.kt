import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import `cmp-lib`.resources.Res
import `cmp-lib`.resources.compose_multiplatform
import org.jetbrains.compose.resources.painterResource

@Composable
public fun TestImage(): Unit = Image(painterResource(Res.drawable.compose_multiplatform), null)
