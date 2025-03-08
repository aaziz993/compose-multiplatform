import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import io.github.aaziz993.cmp_lib.generated.resources.Res
import io.github.aaziz993.cmp_lib.generated.resources.compose_multiplatform
import org.jetbrains.compose.resources.painterResource

@Composable
public fun TestImage(): Unit = Image(painterResource(Res.drawable.compose_multiplatform), null)
