package gradle.api.tasks

import gradle.api.ProjectNamed
import gradle.api.applyTo
import gradle.api.elementType
import gradle.api.trySet
import gradle.collection.SerializableAnyMap
import gradle.serialization.serializer.JsonKeyValueTransformingContentPolymorphicSerializer
import groovy.lang.MissingPropertyException
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.TaskCollection
import org.gradle.api.tasks.TaskDependency

/**
 *
 * A `Task` represents a single atomic piece of work for a build, such as compiling classes or generating
 * javadoc.
 *
 *
 * Each task belongs to a [Project]. You can use the various methods on [ ] to create and lookup task instances. For example, [ ][org.gradle.api.tasks.TaskContainer.create] creates an empty task with the given name. You can also use the
 * `task` keyword in your build file:
 * <pre>
 * task myTask
 * task myTask { configure closure }
 * task myTask(type: SomeType)
 * task myTask(type: SomeType) { configure closure }
</pre> *
 *
 *
 * Each task has a name, which can be used to refer to the task within its owning project, and a fully qualified
 * path, which is unique across all tasks in all projects. The path is the concatenation of the owning project's path
 * and the task's name. Path elements are separated using the {@value org.gradle.api.Project#PATH_SEPARATOR}
 * character.
 *
 * <h2>Task Actions</h2>
 *
 *
 * A `Task` is made up of a sequence of [Action] objects. When the task is executed, each of the
 * actions is executed in turn, by calling [Action.execute]. You can add actions to a task by calling [ ][.doFirst] or [.doLast].
 *
 *
 * Groovy closures can also be used to provide a task action. When the action is executed, the closure is called with
 * the task as parameter.  You can add action closures to a task by calling [.doFirst] or
 * [.doLast].
 *
 *
 * There are 2 special exceptions which a task action can throw to abort execution and continue without failing the
 * build. A task action can abort execution of the action and continue to the next action of the task by throwing a
 * [org.gradle.api.tasks.StopActionException]. A task action can abort execution of the task and continue to the
 * next task by throwing a [org.gradle.api.tasks.StopExecutionException]. Using these exceptions allows you to
 * have precondition actions which skip execution of the task, or part of the task, if not true.
 *
 * <h2 id="dependencies">Task Dependencies and Task Ordering</h2>
 *
 *
 * A task may have dependencies on other tasks or might be scheduled to always run after another task.
 * Gradle ensures that all task dependencies and ordering rules are honored when executing tasks, so that the task is executed after
 * all of its dependencies and any "must run after" tasks have been executed.
 *
 *
 * Dependencies to a task are controlled using [.dependsOn] or [.setDependsOn],
 * and [.mustRunAfter], [.setMustRunAfter], [.shouldRunAfter] and
 * [.setShouldRunAfter] are used to specify ordering between tasks. You can use objects of any of
 * the following types to specify dependencies and ordering:
 *
 *
 *
 *  * A `String`, `CharSequence` or `groovy.lang.GString` task path or name. A relative path is interpreted relative to the task's [Project]. This
 * allows you to refer to tasks in other projects. These task references will not cause task creation.
 *
 *  * A [Task].
 *
 *  * A [TaskDependency] object.
 *
 *  * A [org.gradle.api.tasks.TaskReference] object.
 *
 *  * A [gradle.model.Buildable] object.
 *
 *  * A [org.gradle.api.file.RegularFileProperty] or [org.gradle.api.file.DirectoryProperty].
 *
 *  * A [Provider] object. May contain any of the types listed here.
 *
 *  * A `Iterable`, `Collection`, `Map` or array. May contain any of the types listed here. The elements of the
 * iterable/collection/map/array are recursively converted to tasks.
 *
 *  * A `Callable`. The `call()` method may return any of the types listed here. Its return value is
 * recursively converted to tasks. A `null` return value is treated as an empty collection.
 *
 *  * A Groovy `Closure` or Kotlin function. The closure may take a `Task` as parameter.
 * The closure or function may return any of the types listed here. Its return value is
 * recursively converted to tasks. A `null` return value is treated as an empty collection.
 *
 *  * Anything else is treated as an error.
 *
 *
 *
 * <h2>Using a Task in a Build File</h2>
 *
 * <h3 id="properties">Dynamic Properties</h3>
 *
 *
 * A `Task` has 4 'scopes' for properties. You can access these properties by name from the build file or by
 * calling the [.property] method. You can change the value of these properties by calling the [.setProperty] method.
 *
 *
 *
 *  * The `Task` object itself. This includes any property getters and setters declared by the `Task`
 * implementation class.  The properties of this scope are readable or writable based on the presence of the
 * corresponding getter and setter methods.
 *
 *  * The *extensions* added to the task by plugins. Each extension is available as a read-only property with the same
 * name as the extension.
 *
 *  * The *convention* properties added to the task by plugins. A plugin can add properties and methods to a task through
 * the task's [Convention] object.  The properties of this scope may be readable or writable, depending on the convention objects.
 *
 *  * The *extra properties* of the task. Each task object maintains a map of additional properties. These
 * are arbitrary name -&gt; value pairs which you can use to dynamically add properties to a task object.  Once defined, the properties
 * of this scope are readable and writable.
 *
 *
 *
 * <h4>Dynamic Methods</h4>
 *
 *
 * A [Plugin] may add methods to a `Task` using its [Convention] object.
 *
 * <h3>Parallel Execution</h3>
 *
 *
 * By default, tasks are not executed in parallel unless a task is waiting on asynchronous work and another task (which
 * is not dependent) is ready to execute.
 *
 * Parallel execution can be enabled by the `--parallel` flag when the build is initiated.
 * In parallel mode, the tasks of different projects (i.e. in a multi project build) are able to be executed in parallel.
 */
@Serializable(with = TaskKeyValueTransformingContentPolymorphicSerializer::class)
internal interface Task<T : org.gradle.api.Task> : ProjectNamed<T> {

    /**
     *
     * Sets the dependencies of this task. See [here](./Task.html#dependencies) for a description of the types of
     * objects which can be used as task dependencies.
     *
     * @param dependsOnTasks The set of task paths.
     */
    val dependsOn: LinkedHashSet<String>?

    /**
     *
     * Execute the task only if the given closure returns true.  The closure will be evaluated at task execution
     * time, not during configuration.  The closure will be passed a single parameter, this task. If the closure returns
     * false, the task will be skipped.
     *
     *
     * You may add multiple such predicates. The task is skipped if any of the predicates return false.
     *
     *
     * Typical usage:`myTask.onlyIf { isProductionEnvironment() }`
     *
     * @param onlyIfClosure code to execute to determine if task should be run
     */
    val onlyIf: Boolean?

    /**
     * Do not track the state of the task.
     *
     *
     * Instructs Gradle to treat the task as untracked.
     *
     * @see org.gradle.api.tasks.UntrackedTask
     *
     * @since 7.3
     */
    val doNotTrackState: String?

    /**
     * Specifies that this task is not compatible with the configuration cache.
     *
     *
     *
     * Configuration cache problems found in the task will be reported but won't cause the build to fail.
     *
     *
     *
     *
     * The presence of incompatible tasks in the task graph will cause the configuration state to be discarded
     * at the end of the build unless the global `configuration-cache-problems` option is set to `warn`,
     * in which case the configuration state would still be cached in a best-effort manner as usual for the option.
     *
     *
     *
     *
     * **IMPORTANT:** This setting doesn't affect how Gradle treats problems found in other tasks also present in the task graph and those
     * could still cause the build to fail.
     *
     *
     * @since 7.4
     */
    val notCompatibleWithConfigurationCache: String?

    /**
     * Sets whether the task actually did any work.  Most built-in tasks will set this automatically, but
     * it may be useful to manually indicate this for custom user tasks.
     * @param didWork indicates if the task did any work
     */
    val didWork: Boolean?

    /**
     *
     * Set the enabled state of a task. If a task is disabled none of the its actions are executed. Note that
     * disabling a task does not prevent the execution of the tasks which this task depends on.
     *
     * @param enabled The enabled state of this task (true or false)
     */
    val enabled: Boolean?

    /**
     *
     * Sets a property of this task.  This method searches for a property with the given name in the following
     * locations, and sets the property on the first location where it finds the property.
     *
     *
     *
     *  1. The task object itself.  For example, the `enabled` project property.
     *
     *  1. The task's convention object.
     *
     *  1. The task's extra properties.
     *
     *
     *
     * If the property is not found, a [MissingPropertyException] is thrown.
     *
     * @param name The name of the property
     * @param value The value of the property
     */
    val properties: SerializableAnyMap?

    /**
     * Sets a description for this task. This should describe what the task does to the user of the build. The
     * description will be displayed when `gradle tasks` is called.
     *
     * @param description The description of the task. Might be null.
     */
    val description: String?

    /**
     * Sets the task group which this task belongs to. The task group is used in reports and user interfaces to
     * group related tasks together when presenting a list of tasks to the user.
     *
     * @param group The task group for this task. Can be null.
     */
    val group: String?

    /**
     *
     * Specifies that this task must run after all of the supplied tasks.
     *
     * <pre class='autoTested'>
     * task taskY {
     * mustRunAfter "taskX"
     * }
    </pre> *
     *
     *
     * For each supplied task, this action adds a task 'ordering', and does not specify a 'dependency' between the tasks.
     * As such, it is still possible to execute 'taskY' without first executing the 'taskX' in the example.
     *
     *
     * See [here](./Task.html#dependencies) for a description of the types of objects which can be used to specify
     * an ordering relationship.
     *
     * @param paths The tasks this task must run after.
     *
     * @return the task object this method is applied to
     */
    val mustRunAfter: Set<String>?

    /**
     *
     * Adds the given finalizer tasks for this task.
     *
     * <pre class='autoTested'>
     * task taskY {
     * finalizedBy "taskX"
     * }
    </pre> *
     *
     *
     * See [here](./Task.html#dependencies) for a description of the types of objects which can be used to specify
     * a finalizer task.
     *
     * @param paths The tasks that finalize this task.
     *
     * @return the task object this method is applied to
     */
    val finalizedBy: LinkedHashSet<String>?

    /**
     *
     * Specifies that this task should run after all of the supplied tasks.
     *
     * <pre class='autoTested'>
     * task taskY {
     * shouldRunAfter "taskX"
     * }
    </pre> *
     *
     *
     * For each supplied task, this action adds a task 'ordering', and does not specify a 'dependency' between the tasks.
     * As such, it is still possible to execute 'taskY' without first executing the 'taskX' in the example.
     *
     *
     * See [here](./Task.html#dependencies) for a description of the types of objects which can be used to specify
     * an ordering relationship.
     *
     * @param paths The tasks this task should run after.
     *
     * @return the task object this method is applied to
     */
    val shouldRunAfter: Set<String>?

    context(Project)
    override fun applyTo(receiver: T) {
        receiver::setDependsOn trySet dependsOn
        onlyIf?.let { onlyIf -> receiver.onlyIf { onlyIf } }
        receiver::doNotTrackState trySet doNotTrackState
        receiver::notCompatibleWithConfigurationCache trySet notCompatibleWithConfigurationCache
        receiver::setDidWork trySet didWork
        receiver::setEnabled trySet enabled
        properties?.forEach { (name, value) -> receiver.setProperty(name, value) }
        receiver::setDescription trySet description
        receiver::setGroup trySet group
        receiver::setMustRunAfter trySet mustRunAfter
        receiver::setFinalizedBy trySet finalizedBy
        receiver::setShouldRunAfter trySet shouldRunAfter
    }

    context(Project)
    fun applyTo()
}

private object TaskKeyValueTransformingContentPolymorphicSerializer : JsonKeyValueTransformingContentPolymorphicSerializer<Task<*>>(
    Task::class,
)

context(Project)
internal fun <T : org.gradle.api.Task> Task<T>.applyTo(receiver: TaskCollection<out T>) =
    applyTo(receiver) { name, action ->
        project.tasks.register(name, receiver.elementType(), action)
    }



@Serializable
@SerialName("Task")
internal data class TaskImpl(
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
    override val name: String? = null,
) : Task<org.gradle.api.Task> {

    context(Project)
    override fun applyTo() = applyTo(project.tasks as TaskCollection<org.gradle.api.Task>)
}
