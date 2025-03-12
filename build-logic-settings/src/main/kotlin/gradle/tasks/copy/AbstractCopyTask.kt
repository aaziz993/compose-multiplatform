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
package gradle.tasks.copy

import gradle.collection.SerializableAnyMap
import gradle.tasks.Expand
import gradle.tasks.FilesMatching
import gradle.tasks.Task
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Named
import org.gradle.api.Project
import org.gradle.api.file.DuplicatesStrategy
import org.gradle.kotlin.dsl.withType

/**
 * `AbstractCopyTask` is the base class for all copy tasks.
 */
internal abstract class AbstractCopyTask : Task, CopySpec {

    /**
     * {@inheritDoc}
     */
    abstract val caseSensitive: Boolean?

    context(Project)
    override fun applyTo(named: Named) {
        super<Task>.applyTo(named)

        named as org.gradle.api.tasks.AbstractCopyTask

        super<CopySpec>.applyTo(named)

        caseSensitive?.let(named::setCaseSensitive)
    }

    context(Project)
    override fun applyTo() =
        super<Task>.applyTo(tasks.withType<org.gradle.api.tasks.AbstractCopyTask>())
}

@Serializable
@SerialName("AbstractCopyTask")
internal data class AbstractCopyTaskImpl(
    override val caseSensitive: Boolean? = null,
    override val dependsOn: List<String>? = null,
    override val onlyIf: Boolean? = null,
    override val doNotTrackState: String? = null,
    override val notCompatibleWithConfigurationCache: String? = null,
    override val didWork: Boolean? = null,
    override val enabled: Boolean? = null,
    override val properties: SerializableAnyMap? = null,
    override val description: String? = null,
    override val group: String? = null,
    override val mustRunAfter: List<String>? = null,
    override val finalizedBy: List<String>? = null,
    override val shouldRunAfter: List<String>? = null,
    override val isCaseSensitive: Boolean? = null,
    override val includeEmptyDirs: Boolean? = null,
    override val duplicatesStrategy: DuplicatesStrategy? = null,
    override val filesMatching: FilesMatching? = null,
    override val filesNotMatching: FilesMatching? = null,
    override val filteringCharset: String? = null,
    override val from: List<String>? = null,
    override val fromSpec: FromSpec? = null,
    override val into: String? = null,
    override val intoSpec: IntoSpec? = null,
    override val rename: Map<String, String>? = null,
    override val renamePattern: Map<String, String>? = null,
    override val filePermissions: Int? = null,
    override val dirPermissions: Int? = null,
    override val eachFile: FileCopyDetails? = null,
    override val expand: SerializableAnyMap? = null,
    override val expandDetails: Expand? = null,
    override val includes: List<String>? = null,
    override val setIncludes: List<String>? = null,
    override val excludes: List<String>? = null,
    override val setExcludes: List<String>? = null,
    override val name: String = "",
) : AbstractCopyTask()
