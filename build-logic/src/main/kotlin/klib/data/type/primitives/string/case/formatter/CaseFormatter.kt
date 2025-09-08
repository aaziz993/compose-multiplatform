/*
 * Copyright © 2019-2020, PearX Team
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package klib.data.type.primitives.string.case.formatter

import klib.data.type.primitives.string.case.splitter.WordSplitter
import klib.data.type.primitives.string.case.universalWordSplitter

/**
 * An interface that defines a case formatter that can be used to join a collection of words into single string.
 */
public interface CaseFormatter {
    /**
     * Joins all elements of [words], appending the result to [appendable].
     */
    public fun formatTo(appendable: Appendable, words: Iterable<String>)
}

/**
 * Joins all elements of [words], appending the result to [appendable].
 */
public fun CaseFormatter.formatTo(appendable: Appendable, vararg words: String): Unit = formatTo(appendable, words.asIterable())

/**
 * Joins all elements of [words] and returns the resulting string.
 */
public fun CaseFormatter.format(words: Iterable<String>): String = buildString { formatTo(this, words) }

/**
 * Joins all elements of [words] and returns the resulting string.
 */
public fun CaseFormatter.format(vararg words: String): String = format(words.asIterable())

/**
 * Returns a copy of [string] converted to another case by splitting it into multiple words using [wordSplitter] and joining them using [CaseFormatter].
 */
public fun CaseFormatter.format(string: String, wordSplitter: WordSplitter = universalWordSplitter()): String = format(wordSplitter.splitToWords(string))
