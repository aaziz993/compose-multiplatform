/*
 * Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package hello.world

import Screen
import android.os.Bundle
import android.app.resources.Res
import android.app.resources.compose_multiplatform
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import org.jetbrains.compose.resources.painterResource

public class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Column {
                Screen()
                Image(painterResource(Res.drawable.compose_multiplatform), null)
            }
        }
    }
}
