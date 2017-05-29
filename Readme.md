Draft about the way of managing deprecated API in nuxeo.



# Summary

Enhanced @Deprecated annotation usage, and tools to strengthen the API life cycle.

# Goals

Help developers to document and qualify the code deprecation.

Help developers to follow the API changes.

Discourage deprecated usage but allow legacy code.

Guarantee a comfortable timeframe for code upgrade.

Provide tools for static, compile-time and runtime analysis.

# The current context

Code deprecation is motivated by API or feature replacement (improvement, security or design flaw...), removal (extraneous, superfluous, costly, obsolete, no more maintained...), standardization (naming, usage...), structural changes, relocation...

The main impacts, questions and constraints are:

- the backward compatibility
- the customer guarantees and contract, his confidence in the Platform
- the developer guidelines
- the upgrade means and costs

What is the expected removal date? Can the legacy code live longer?

https://doc.nuxeo.com/nxdoc/upgrading-the-nuxeo-platform/#nuxeo-upgrade-policy
> In order to make upgrade easy, we are very careful not to break anything:
>
> - We don't break the APIs between two versions: we add new APIs and deprecate old ones,
> - There are several minor versions between the deprecation and the removal so you have time to adapt your code,
> - If we completely replace a service (that was the case for SearchService and EventService for example), we provide compatibility packages so you can continue using the old API (even if migrating to the new API is highly recommended).

https://doc.nuxeo.com/corg/java-code-style/

> On API changes, forward and backward compliance must be maintained over multiple versions. Help API users and code reviewers to manage the deprecated code using both Java and Javadoc annotations.
>
> Indicate the version of deprecation. That will be used for later removal and help deprecated API users to guess the refactoring priority. Points to the new API and usage.

The API specification for the @Deprecated annotation:

> A program element annotated @Deprecated is one that programmers are discouraged from using, typically because it is dangerous, or because a better alternative exists.
  Compilers warn when a deprecated program element is used or overridden in non-deprecated code.


# The problem

The current process is focused on the API stability and compliance, it is missing a formal cleanup strategy, concrete upgrade means, the migration path, the available timeframe and a little bit of documentation.

As the software is evolving, the number of deprecation occurrences increase and we don't have at this time any means for encouraging coders to
remove their deprecated API usages while maintaining their code.

From years, we've adopted a rule which implies coder to use the Javadoc tag @since when we're introducing new or deprecate older APIs. But we
generally release the new code in that state without proceeding with the cleaning of our own deprecated API usage. This has drawbacks:

- proceeding with the removal later is a boring task which can become complex, nay impossible.
- we have an increased risk of introducing incompatible APIs.
- we're not providing the good example for externals, ie: we can't ask others to do the cleanup, if we didn't achieved it ourselves.

# The plan

Oracle faced to the same issues and proposed the [Enhanced Deprecation][JEP257]. As a result, in the JDK9 the @Deprecated annotation
has been extended with the following two attributes

- since: Returns the version in which the annotated element became deprecated.
- forRemoval: Indicates whether the annotated element is subject to removal in a  future version.

With the combination of the @SuppressWarnings annotation, we can implement some rules like:

- cannot merge code which introduces regressions (compile time)
- cannot release LTS including deprecated API tagged for removal older than two revisions (release time)

At the time we're deprecating the API we're introducing new deprecation warns at compile time. These warns should be solved before we can merge the code back
in the trunk.
In case of forRemoval, we should replace elsewhere all the deprecated API usage with the new way of doing. In the other case, we should acknowledge
the warn by propagating the deprecation on the client's API or annotate the code with the @SuppressWarning annotation. This should ensure that we will always be able
to safely remove deprecated APIs from the code. The effective removal of deprecated API will be part of the release work plan.

# The implementation

As we don't have access to the [JEP257], we'll use a @DeprecatedFor annotation instead which will support the following attributes:

- since: the revision in which the annotated element became deprecated.
- reason:
 - ``CodeRemoval`` tags the deprecation for being removed in next revisions,
 - ``CodeLegacy`` tags the deprecation as being maintained but which should be used only with extreme prudence.

## API Deprecation (step 1)
Some code is deprecated with:

- Javadoc @Deprecated since x.y.z
- @Deprecated annotation
- @DeprecatedFor annotation with the reason (CodeRemoval or CodeLegacy)

### At compile time (IDE and Maven build):

- check on syntax and usage: `@DeprecatedFor` is mandatory on Nuxeo code
- check on version: if `CodeRemoval` org.nuxeo.lang.tools.deprecation.samples.SomeAPI.forRemoval()
 - before `since + 2 LTS`: warn
 - after `since + 2 LTS`: error (code is expected to be removed => expected N/A)
- check on version: if `CodeLegacy` org.nuxeo.lang.tools.deprecation.samples.SomeAPI.legacy()
 - implementation must propagates the deprecation org.nuxeo.lang.tools.deprecation.samples.SomeImplementation.legacy()
 - client must acknowledge the warn with a `@SuppressWarnings` org.nuxeo.lang.tools.deprecation.samples.SomeClient.invokeLegacy()

The developer must qualify the deprecation using the `@DeprecatedFor` annotation.
The developer is encouraged, but not forced, to upgrade as much code as possible.

### At packaging / deployment

Introspect JAR bundles, report according to the compile time deprecation policy.

When using `ant-maven-assembly-plugin`


When installing a Nuxeo Package with `nuxeoctl`


On user request with `nuxeoctl show-deprecation`

## API Removal (step 2)



# The tools

An ``APT`` module which will notify warns and errors at compile time regarding the annotation values.

A nuxeoctl feature introspecting Nuxeo Packages' content which will warn about wrong deployment regarding the deprecation usages.


