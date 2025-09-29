/**
 *         Copyright 2025 Aziz Atoev
 *
 * Licensed under the [project_license_name] (the "License");
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         Apache License, Version 2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
import androidx.compose.ui.window.ComposeUIViewController
import platform.UIKit.UIViewController

@Suppress("FunctionName")
public fun MainViewController(): UIViewController =
    ComposeUIViewController(
        {
            // info.plist
            // The app will crash by default, if CADisableMinimumFrameDurationOnPhone is not set to true in
            // Use newly added ComposeUIViewControllerConfiguration.enforceStrictPlistSanityCheck to opt-out
            enforceStrictPlistSanityCheck = false
        },
    ) {
        App()
    }
