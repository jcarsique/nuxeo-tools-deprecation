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
package org.nuxeo.lang.tools.deprecation.samples;

public class SomeImplementation implements SomeAPI {

    @Override
    @Deprecated
    public void legacy() {
    }

    // Wrong usage
    @Override
    public void forRemoval() {
    }

    // N/A: Wrong API
    @Override
    public void wrongDeprecation() {
        throw new UnsupportedOperationException();
    }


}
