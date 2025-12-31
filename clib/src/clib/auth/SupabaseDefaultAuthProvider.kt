package clib.auth

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Token
import androidx.compose.ui.graphics.vector.ImageVector
import com.sunildhiman90.kmauth.supabase.model.SupabaseDefaultAuthProvider

public fun SupabaseDefaultAuthProvider.imageVector(): ImageVector = when (this) {
    SupabaseDefaultAuthProvider.EMAIL -> Icons.Default.Email
    SupabaseDefaultAuthProvider.ID_TOKEN -> Icons.Default.Token
    SupabaseDefaultAuthProvider.PHONE -> Icons.Default.Phone
}
