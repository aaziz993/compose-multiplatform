/*
 * Copyright 2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package plugin.project.gradle.model

import kotlinx.serialization.Serializable
import org.gradle.api.Named
import org.gradle.api.Project
import org.gradle.api.tasks.AbstractCopyTask
import plugin.project.kotlin.model.Task

/**
 * `AbstractCopyTask` is the base class for all copy tasks.
 */
@Serializable
internal abstract class AbstractCopyTask : Task, CopySpec {

    /**
     * {@inheritDoc}
     */
    abstract val caseSensitive: Boolean?

    context(Project)
    override fun applyTo(named: Named) {
        super<Task>.applyTo(named)

        named as AbstractCopyTask

        super<CopySpec>.applyTo(named)
        caseSensitive?.let(named::setCaseSensitive)
    }
}
