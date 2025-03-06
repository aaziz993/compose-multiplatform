package gradle.model

import org.gradle.api.tasks.testing.TestFilter

internal interface TestFilter {

    /**
     * Appends a test name pattern to the inclusion filter. Wildcard '*' is supported, either test method name or class name is supported. Examples of test names: "com.foo.FooTest.someMethod",
     * "com.foo.FooTest", "*FooTest*", "com.foo*". See examples in the docs for [TestFilter].
     *
     * @param testNamePattern test name pattern to include, can be class or method name, can contain wildcard '*'
     * @return this filter object
     */
    val includeTestsMatchings: Set<String>?

    /**
     * Appends a test name pattern to the exclusion filter. Wildcard '*' is supported, either test
     * method name or class name is supported. Examples of test names: "com.foo.FooTest.someMethod",
     * "com.foo.FooTest", "*FooTest*", "com.foo*", "*someTestMethod". See examples in the docs for [TestFilter].
     *
     * @param testNamePattern test name pattern to exclude, can be class or method name, can contain wildcard '*'
     * @return this filter object
     * @since 5.0
     */
    val excludeTestsMatchings: Set<String>?

    /**
     * Sets the included test name patterns. They can be class or method names and may contain wildcard '*'. Test name patterns can be appended via [.includeTestsMatching] or set via
     * [.setIncludePatterns].
     *
     * @return included test name patterns
     */
    val includePatterns: Set<String>?

    /**
     * Sets the excluded test name patterns. They can be class or method names and may contain wildcard '*'.
     * Test name patterns can be appended via [.excludeTestsMatching] or set via
     * [.setExcludePatterns].
     *
     * @return included test name patterns
     * @since 5.0
     */
    val excludePatterns: Set<String>?

    /**
     * Add a test method specified by test class name and method name.
     *
     * @param className the class name of the test to execute
     * @param methodName the method name of the test to execute. Can be null.
     * @return this filter object
     */
    val includeTests: Set<Test>?

    /**
     * Excludes a test method specified by test class name and method name.
     *
     * @param className the class name of the test to exclude
     * @param methodName the method name of the test to exclude. Can be null.
     * @return this filter object
     * @since 5.0
     */
    val excludeTests: Set<Test>?

    /**
     * Sets whether the task should fail if no matching tests where found.
     * The default is true.
     */
    val failOnNoMatchingTests: Boolean?

    fun applyTo(defaultTestFilter: TestFilter) {
        includeTestsMatchings?.forEach(defaultTestFilter::includeTestsMatching)
        excludeTestsMatchings?.forEach(defaultTestFilter::excludeTestsMatching)
        includePatterns?.let(defaultTestFilter.includePatterns::addAll)
        excludePatterns?.let(defaultTestFilter.excludePatterns::addAll)
        includeTests?.forEach { (className, methodName) -> defaultTestFilter.includeTest(className, methodName) }
        excludeTests?.forEach { (className, methodName) -> defaultTestFilter.excludeTest(className, methodName) }
        failOnNoMatchingTests?.let(defaultTestFilter::setFailOnNoMatchingTests)
    }
}
