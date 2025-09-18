/*
 * Copyright © 2019-2020, PearX Team
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package klib.data.type.primitives.string.case.splitter

/**
 * An interface that defines a word splitter that can be used to split a string into multiple words.
 */
public interface WordSplitter {
    /**
     * Splits [string] into multiple words and returns a list of them.
     */
    public fun splitToWords(string: String): List<String>
}
