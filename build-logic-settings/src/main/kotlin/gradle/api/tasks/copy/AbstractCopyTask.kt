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
package gradle.api.tasks.copy

import gradle.api.tasks.ConventionTask
import gradle.api.tasks.Expand
import gradle.api.tasks.FilesMatching
import gradle.api.tasks.applyTo
import klib.data.type.serialization.json.serializer.SerializableAnyMap
import klib.data.type.reflection.trySet
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.api.file.DuplicatesStrategy
import org.gradle.kotlin.dsl.withType

/**
 * `AbstractCopyTask` is the base class for all copy tasks.
 */
internal abstract class AbstractCopyTask<T : org.gradle.api.tasks.AbstractCopyTask> : ConventionTask<T>(), CopySpec<T> {

    /**
     * {@inheritDoc}
     */
    abstract val caseSensitive: Boolean?

    context(Project)
    override fun applyTo(receiver: T) {
        super<ConventionTask>.applyTo(receiver)

        super<CopySpec>.applyTo(receiver)

        receiver::setCaseSensitive trySet caseSensitive
    }
}

@Serializable
@SerialName("AbstractCopyTask")
internal data class AbstractCopyTaskImpl(
    override val caseSensitive: Boolean? = null,
    override val dependsOn: LinkedHashSet<String>? = null,
    override val onlyIf: Boolean? = null,
    override val doNotTrackState: String? = null,
    override val notCompatibleWithConfigurationCache: String? = null,
    override val didWork: Boolean? = null,
    override val enabled: Boolean? = null,
    override val properties: SerializableAnyMap? = null,
    override val description: String? = null,
    override val group: String? = null,
    override val mustRunAfter: Set<String>? = null,
    override val finalizedBy: LinkedHashSet<String>? = null,
    override val shouldRunAfter: Set<String>? = null,
    override val isCaseSensitive: Boolean? = null,
    override val includeEmptyDirs: Boolean? = null,
    override val duplicatesStrategy: DuplicatesStrategy? = null,
    override val filesMatching: FilesMatching? = null,
    override val filesNotMatching: FilesMatching? = null,
    override val filteringCharset: String? = null,
    override val from: @Serializable(with = FromContentPolymorphicSerializer::class) Any? = null,
    override val into: @Serializable(with = IntoContentPolymorphicSerializer::class) Any? = null,
    override val renames: Set<Rename>? = null,
    override val filePermissions: Int? = null,
    override val dirPermissions: Int? = null,
    override val eachFile: FileCopyDetails? = null,
    override val expand: SerializableAnyMap? = null,
    override val expandDetails: Expand? = null,
    override val includes: Set<String>? = null,
    override val setIncludes: Set<String>? = null,
    override val excludes: Set<String>? = null,
    override val setExcludes: Set<String>? = null,
    override val name: String? = null,
) : AbstractCopyTask<org.gradle.api.tasks.AbstractCopyTask>() {

    context(Project)
    override fun applyTo() =
        applyTo(project.tasks.withType<org.gradle.api.tasks.AbstractCopyTask>())
}
