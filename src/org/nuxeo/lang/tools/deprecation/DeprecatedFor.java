/*
 * (C) Copyright 2017 Nuxeo SA (http://nuxeo.com/) and contributors.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-2.1.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * Contributors:
 *     Stephane Lacoin at Nuxeo (aka matic)
 */
package org.nuxeo.lang.tools.deprecation;

/**
 * A @DeprecatedFor program element annotated with {@link Reason#CodeRemoval} is one that programmers
 * are requested for usage removal. The annotated element should
 * provide Javadoc indication about the upgrade path to follow. Any usage of that element
 * will result in a compilation error after a given comparison with the value of "since".
 */
public @interface DeprecatedFor {
    /**
    * `CodeRemoval`
    * : an API being removed in next revisions.
    *
    * `CodeLegacy`
    * : a maintained API but that should be used only with extreme prudence.
    *
    */
    enum Reason {
        CodeRemoval,
        CodeLegacy
    }

    /**
    * the deprecation reason
    *
    */
    Reason reason();

    /**
    * the revision in which the annotated element became deprecated.
    */
    String since();
}
