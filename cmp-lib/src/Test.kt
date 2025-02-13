import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import `cmp-lib`.resources.Res
import `cmp-lib`.resources.flag_es
import org.jetbrains.compose.resources.painterResource

@Composable
public fun TestImage(): Unit = Image(painterResource(Res.drawable.flag_es), null)
