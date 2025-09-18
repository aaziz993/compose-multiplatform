/*
 * Copyright © 2019-2020, PearX Team
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package klib.data.type.primitives.string.case

import klib.data.type.primitives.string.case.formatter.CaseFormatter
import klib.data.type.primitives.string.case.formatter.CaseFormatterConfig
import klib.data.type.primitives.string.case.formatter.CaseFormatterConfigurable
import klib.data.type.primitives.string.case.formatter.format
import klib.data.type.primitives.string.case.splitter.WordSplitter
import klib.data.type.primitives.string.case.splitter.WordSplitterConfig
import klib.data.type.primitives.string.case.splitter.WordSplitterConfigurable

/**
 * Returns a copy of this string converted to another case by splitting it into multiple words using [specified][from] [WordSplitter] and joining them using [specified][to] [CaseFormatter].
 */
public fun String.toCase(to: CaseFormatter, from: WordSplitter = universalWordSplitter()): String = to.format(this, from)

/**
 * Returns a copy of this string converted to another case by splitting it into multiple words using [specified][from] [WordSplitter] and joining them using [CaseFormatterConfigurable] configured with [toConfig].
 */
public fun String.toCase(toConfig: CaseFormatterConfig, from: WordSplitter = universalWordSplitter()): String = toCase(CaseFormatterConfigurable(toConfig), from)

/**
 * Returns a copy of this string converted to another case by splitting it into multiple words using [WordSplitterConfigurable] configured with [fromConfig] and joining them using [CaseFormatterConfigurable] configured with [toConfig].
 */
public fun String.toCase(toConfig: CaseFormatterConfig, fromConfig: WordSplitterConfig): String = toCase(toConfig, WordSplitterConfigurable(fromConfig))

/** Returns a copy of this string converted to SCREAMING_SNAKE_CASE by splitting it into multiple words using [specified][from] [WordSplitter] and joining them using [CaseFormat.UPPER_UNDERSCORE]. */
public fun String.toScreamingSnakeCase(from: WordSplitter = universalWordSplitter()): String = toCase(CaseFormat.UPPER_UNDERSCORE, from)

/** Returns a copy of this string converted to snake_case by splitting it into multiple words using [specified][from] [WordSplitter] and joining them using [CaseFormat.LOWER_UNDERSCORE].. */
public fun String.toSnakeCase(from: WordSplitter = universalWordSplitter()): String = toCase(CaseFormat.LOWER_UNDERSCORE, from)

/** Returns a copy of this string converted to PascalCase by splitting it into multiple words using [specified][from] [WordSplitter] and joining them using [CaseFormat.CAPITALIZED_CAMEL].. */
public fun String.toPascalCase(from: WordSplitter = universalWordSplitter()): String = toCase(CaseFormat.CAPITALIZED_CAMEL, from)

/** Returns a copy of this string converted to camelCase by splitting it into multiple words using [specified][from] [WordSplitter] and joining them using [CaseFormat.CAMEL].. */
public fun String.toCamelCase(from: WordSplitter = universalWordSplitter()): String = toCase(CaseFormat.CAMEL, from)

/** Returns a copy of this string converted to TRAIN-CASE by splitting it into multiple words using [specified][from] [WordSplitter] and joining them using [CaseFormat.UPPER_HYPHEN].. */
public fun String.toTrainCase(from: WordSplitter = universalWordSplitter()): String = toCase(CaseFormat.UPPER_HYPHEN, from)

/** Returns a copy of this string converted to kebab-case by splitting it into multiple words using [specified][from] [WordSplitter] and joining them using [CaseFormat.LOWER_HYPHEN].. */
public fun String.toKebabCase(from: WordSplitter = universalWordSplitter()): String = toCase(CaseFormat.LOWER_HYPHEN, from)

/** Returns a copy of this string converted to UPPER SPACE CASE by splitting it into multiple words using [specified][from] [WordSplitter] and joining them using [CaseFormat.UPPER_SPACE].. */
public fun String.toUpperSpaceCase(from: WordSplitter = universalWordSplitter()): String = toCase(CaseFormat.UPPER_SPACE, from)

/** Returns a copy of this string converted to Title Case by splitting it into multiple words using [specified][from] [WordSplitter] and joining them using [CaseFormat.CAPITALIZED_SPACE].. */
public fun String.toTitleCase(from: WordSplitter = universalWordSplitter()): String = toCase(CaseFormat.CAPITALIZED_SPACE, from)

/** Returns a copy of this string converted to Sentence case by splitting it into multiple words using [specified][from] [WordSplitter] and joining them using [CaseFormat.SENTENCE_SPACE].. */
public fun String.toSentenceCase(from: WordSplitter = universalWordSplitter()): String = toCase(CaseFormat.SENTENCE_SPACE, from)

/** Returns a copy of this string converted to lower space case by splitting it into multiple words using [specified][from] [WordSplitter] and joining them using [CaseFormat.LOWER_SPACE].. */
public fun String.toLowerSpaceCase(from: WordSplitter = universalWordSplitter()): String = toCase(CaseFormat.LOWER_SPACE, from)

/** Returns a copy of this string converted to UPPER.DOT.CASE by splitting it into multiple words using [specified][from] [WordSplitter] and joining them using [CaseFormat.UPPER_DOT].. */
public fun String.toUpperDotCase(from: WordSplitter = universalWordSplitter()): String = toCase(CaseFormat.UPPER_DOT, from)

/** Returns a copy of this string converted to dot.case by splitting it into multiple words using [specified][from] [WordSplitter] and joining them using [CaseFormat.LOWER_DOT].. */
public fun String.toDotCase(from: WordSplitter = universalWordSplitter()): String = toCase(CaseFormat.LOWER_DOT, from)


/**
 * Splits this string into multiple words using [specified][splitter] [WordSplitter] and returns a list of words.
 */
public fun String.splitToWords(splitter: WordSplitter = universalWordSplitter()): List<String> = splitter.splitToWords(this)

/**
 * Splits this string into multiple words using [WordSplitterConfigurable] configured with [config] and returns a list of words.
 */
public fun String.splitToWords(config: WordSplitterConfig): List<String> = splitToWords(WordSplitterConfigurable(config))
