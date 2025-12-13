package klib.data.share

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import klib.data.model.MIME_TYPES
import klib.data.share.model.ShareFileModel

public actual class Share(private val context: Context) {

    public actual suspend fun shareText(text: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            flags += Intent.FLAG_ACTIVITY_NEW_TASK
            putExtra(Intent.EXTRA_TEXT, text)
        }
        val chooser = Intent.createChooser(intent, null)
        context.startActivity(chooser)
    }

    public actual suspend fun shareFile(file: ShareFileModel): Result<Unit> {
        return runCatching {
            withContext(Dispatchers.IO) {
                val savedFile = saveFile(file.fileName, file.bytes)
                val uri = FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.provider",
                    savedFile,
                )
                withContext(Dispatchers.Main) {
                    val intent = Intent(Intent.ACTION_SEND).apply {
                        putExtra(Intent.EXTRA_STREAM, uri)
                        flags += Intent.FLAG_ACTIVITY_NEW_TASK
                        flags += Intent.FLAG_GRANT_READ_URI_PERMISSION
                        type = MIME_TYPES[file.mime]
                    }
                    val chooser = Intent.createChooser(intent, null)
                    context.startActivity(chooser)
                }
            }
        }
    }

    private fun saveFile(name: String, data: ByteArray): File {
        val cache = context.cacheDir
        val savedFile = File(cache, name)
        savedFile.writeBytes(data)
        return savedFile
    }
}
