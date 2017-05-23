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
/**
 *  
 * # Summary
 * 
 * Enhanced @Deprecated annotation usage, and tools to strengthen the API life cycle.
 * 
 * # Goals
 * 
 * Insure deprecated APIs being removed from the code and provide tools for static deprecation code analysis.
 * 
 * # What's we have
 * 
 * 
 * The API specification for the @Deprecated annotation, mirrored in The Java Language Specification, is:
 * 
 * ``A program element annotated @Deprecated is one that programmers are discouraged from using, typically because it is dangerous, or because a better alternative exists. 
 *   Compilers warn when a deprecated program element is used or overridden in non-deprecated code.``
 * 
 * The motivation for deprecating an API could be one or a any combination of the following
 * 
 * * the API is flawed and is impractical to fix,
 * * the API usage is likely to lead to errors,
 * * the API has been superseded by another one,
 * * the API is obsolete
 * * the API is experimental and is subject to incompatible changes
 * 
 * As the software is evolving, the number of deprecation occurrence increase, and we don't have at this time any means for encouraging coders to
 * remove their deprecated API usages while maintaining their code.
 * 
 * From years, we've adopted a rule which implies coder to use the javadoc tag @since when we're introducing new or deprecate older APIs. But we 
 * generally release the new code in that state without proceeding with the cleaning of our own deprecated API usage. This has drawbacks
 * 
 * * proceeding with the removal later is a boring task which can become complex.
 * * we have an increased risk of introducing incompatible APIs.
 * * we're not providing the good example for externals, ie: we can't ask others to do the cleanup, if we didn't achieved it ourselves.
 * 
 * # The plan
 * 
 * At sometime Oracle faced to the same issues and add proposed the [Enhanced Deprecation][JEP257]  for solving. As a result, in the JDK9 the @Deprecated annotation
 * has been extended with the following two attributes
 * 
 * since
 * : Returns the version in which the annotated element became deprecated.
 * 
 * forRemoval
 * : Indicates whether the annotated element is subject to removal in a  future version.
 * 
 * With the combination of the @SuppressWarnings annotation, we can introduce and implement these rules
 * 
 * * there is no possibility to merge new code which introduce new regression warns at compile time
 * * any LTS release of nuxeo could not include deprecated API tagged for removal older than two revisions
 * 
 * At the time we're deprecating the API we're introducing new deprecation warns at compile time. These warns should be solved before we can merge the code back
 * in the trunk. In case of for removal, we should replace elsewhere all the deprecated API usage with the new way of doing. In the other case, we should acknowledge
 * the warn by propagating the deprecation on the client's API or annotate the code with the @SuppressWarning annotation. This should insure that we will always be able
 * to safely remove deprecated APIs from the code. The effective removal of deprecated API will be part of the release work plan.   
 * 
 * As we don't have access to the [JEP257], we're putting in place the @DeprecatedFor annotation instead which will support the following attributes
 * 
 * since
 * : the revision in which the annotated element became deprecated.
 * 
 * reason
 * : ``CodeRemoval`` tags the deprecation for being removed in next revisions, ``CodeLegacy`` tags the deprecation as being maintained but which should be used only with extreme prudence.
 * 
 * Then, we're going to implement an ``APT`` module which will notify warns and errors at compile time regarding the annotation values. This will prevent us for merging 
 * wrong usages in the trunk. We've also planned to integrate the same logic in the nuxeo package installer for introspecting marketplace's classpath and warns 
 * about wrong deployment regarding the deprecation usages (mainly related to out-dated marketplace usage).
 *
 */

package org.nuxeo.lang.tools.deprecation;