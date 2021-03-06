MelnormeEclipse
================

MelnormeEclipse is a framework for building Eclipse based IDEs for general purpose languages. 

*  https://github.com/bruno-medeiros/MelnormeEclipse/wiki

### History/Background

MelnormeEclipse originated from the [DDT](http://ddt-ide.github.io/) project, and a desire to refactor non language-specific code to a separate project, so that it could be reused by other IDEs. DDT had been using the [DLTK](https://eclipse.org/dltk/) framework for a long time, and DLTK has a similar goal as MelnormeEclipse: leverage a JDT-style infrastructure for use by other IDEs (and not necessarily just dynamic languages). But development on DLTK slowed down to a near halt, whilst still being left with many API and functionality limitations. DLTK started as a JDT clone, but never got the chance to evolve much beyong the Java-centric internal model.

As such, in order to begin a project to provide common IDE infrastructure, it would be necessary to either fork DLTK, or build a new project from scractch. For several reasons the later option was chosen. Some MelnormeEclipse components where written from scratch, others were rewritten from existing JDT/DLTK code, others still were copied from JDT/DLTK with little to no modifications - whichever case was deemed better to serve MelnormeEclipse goals. 

### Functionality:

MelnormeEclipse is not currently as big or complete as JDT/DLTK in terms of provided functionality, but it covers a fair amount of ground already:

* New project wizard.

* Editor support:
 * Editor Outline and Quick-Outline (with filtering) support. Based on structure model
 * Editor source reconciliation and parse errors reporting.
 * An auto-indentation strategy for curly braces language.
 * Common editor actions: Indent/Deindent Source; Toggle Comment; Go To Matching Bracket.
 * Many other minor editor improvements and boilerplate.
 * Boilerplate code for Content Assist, Find-Definition, editor hyperlinks functionality.

* Content Assist "Code Snippets" functionality.

* Boilerplate project build support, parsing build tool output, creating error markers, etc..
 * Helper code for calling other external tools, their output displayed in Eclipse consoles.

* Boilerplate launch and debug support.
 * Debug support using CDT GDB integration.

* Preferences and preference pages: 
 * Source Coloring (aka syntax highlighting).
 * Typing/Auto-Indentation.
 * Content Assist.
 * Code snippets.
 * Build and other language tools.

* IDE build script (Maven/Tycho based).
 * Eclipse Update Site upload script. (uploads to a Git repo)
 * Skeleton project website, designed to be used by Github pages.

### Design notes:

##### Support for using external semantic tools.
There is a focus on an IDE paradigm of using external programs for building, code completion, and any others sorts of *language semantic functionality*. Most of MelnormeEclipse infrastructure is UI infrastructure, the core of a concrete IDE's engine functionality is usually driven by language-specific external programs. (This is not a requirement though - using internal tools is easily supported as well).

##### MelnormeEclipse source is embedded in host IDE.
MelnormeEclipse is designed to be used by embedding the its source code directly in the host IDE code. 
As opposed to DLTK or Xtext for example, where a runtime dependency on the framework plugins is required. 
As such, updating to a new MelnormeEclipse version is made by means of Git source control workflows. The motivation 
for this is so provide complete API control to host IDEs - if some change is desired that MelnormeEclipse is not able 
to be customized without changing MelnormeEclipse code, this won't be much of a problem, the local MelnormeEclipse source can be changed for theat host IDE. This also means different MelnormeEclipse-based IDEs can be installed on the same Eclipse installation, even if they are based on different (and otherwise incompatible) MelnormeEclipse versions or variations.
(The same would not be possible with DLTK for example)

A secondary reason for this approach, that only became apparent later, is so Melnorme code can refer to IDE-specific code directly, and thus preserve certain type-system constraints *statically*. Otherwise (using the library approach), you would sometimes need casts and runtime checks from a Melnorme generic type to the IDE-specific type. 
For example: `IStructureElement` is pure Melnorme code, but refers to `StructureElementKind` which is a language specific class and can be modified by the concrete IDE.
In a language such as C++ or D, one could write Melnorme as library that used templates and meta-programming, but it's not something one can do in Java, to this degree.

##### No IModelElement/IJavaElement model hierarchy
This is a key design difference between MelnormeEclipse and DLTK/JDT/CDT: avoiding the use of the IModelElement/IJavaElement model hierarchy (IModelElement is DLTK's analogue of IJavaElement). This model is seen as having unnecessary complexities, and several shortcomings. For example, it's too Java-centric, the model doesn't adapt well for IDEs of languages with structures significantly different than Java. Another issue is that it was felt that combining source elements (functions, types, etc.) and external elements (like source folders and package declarations) into the same hierarchy makes the design more complex that it should be, as these concerns are fairly separate.

 
### Writing a MelnormeEclipse-based IDE:

To get started creating a new MelnormeEclipse-based IDE, fork this repository.

#### Terminology
Some terminology used internally in MelnormeEclipse source:

* **Bundle** - A bundle is the same as a "package" in the context of a language package manager. In other words, it's the unit of source distribution and dependency management. So, it's a jar in Java, a crate in Rust, a gem in Ruby, etc. . A bundle manifest is the file that describe the bundle (usually defining the bundle  name, version, dependencies, source folders, build targets, etc.). Not all concrete languages might have a full-fledged bundle concept: in Go for example, Go's packages are not versioned, there is no explicit manifest file, etc. .

* **EngineTools** / **Daemon** - A language's external programs responsible for providing semantic functionality of use to the IDEs. Usually code-completion and find-definition, but can be a lot more (source outline, parse errors, find-references, search symbols, refactoring, etc.). It's sometimes called a deamon because a more advanced/optimized design for these tools requires them to be combined in a single program running as a resident process (deamon). This program caches in-memory semantic information about underlying projects, so that future requests can re-use this information.

#### Understanding MelnormeEclipse source embedding
The MelnormeEclipse source is embedded directly into the host IDE. To make it easier to manage source updates to and from MelnormeEclipse, the following rules should be observed. 

 * The vast majority of `melnorme.lang` code, or simply Lang code, is code not specific to any language, and should only depend on other `melnorme` code, or on Eclipse.org platform plugins. But not on IDE specific code.   
   * Such is placed on a separate source folder: `src-lang/`.
   * The source of all code placed in `src-lang/` should be the exact same for all MelnormeEclipse based IDEs (even if the binary or runtime structure/API may differ).
 * Then, the rest of `melnorme.lang` code will have IDE specific code. Such classes should either be annotated with the `melnorme.lang.tooling.LANG_SPECIFIC` annotation, or have an `_Actual` suffix in the name. This code will contain bindings to IDE-specific code (such as ids, other IDE constants, or even IDE-specific methods).
  * This language specific `melnorme.lang` code must not be place on `src-lang/`, but on the same source folder as the rest of the language specific code (`src` by default).

#### Modifying the starting template
Follow these steps:

 * [ ] Do a search-replace on the following strings, replace for the appropriate text for your IDE project:

| String 	| Description | Example |
|---------	|--------------	| -----	|
|LANGUAGE_ |  Name of language - for Java class names.| <sub><sup>`Dee`</sup></sub> |
|LANG_NAME  | Name of language - for UI display. | <sub><sup>`D`</sup></sub> |
|LANG_PROJECT_ID | Common prefix for all plugin identifiers. | <sub><sup>`org.dsource.ddt`</sup></sub> |
|LANG_IDE_NAME   | Name of the IDE. | <sub><sup>`DDT`</sup></sub> |
|LANG_IDE_SITE   |  URL of project website (with http prefix). | <sub><sup>`http://ddt-ide.github.io/`</sup></sub> |
|LANG_IDE_UPDATE_SITE| URL of project's Eclipse Update Site. | <sub><sup>`http://ddt-ide.github.io/releases`</sup></sub> |
|LANG_IDE_WEBSITE_GIT_REPO| URL of the Git project of the Github-Pages-based project website. | <sub><sup>`https://github.com/DDT-IDE/ddt-ide.github.io`</sup></sub>  |
|LANG_OTHER      | Other changes specific to each location. | N/A |

 Note: some strings like `LANGUAGE_` or `LANG_PROJECT_ID`  will replace Java class identifiers. After this replace you will also need to rename the compilation unit, and/or move them to a different folder. This can be done quickly in Eclipse with quick-fixes.

* [ ] Modify `plugin_ide.jvmcheck/icons/ide-logo.32x32.png`

* [ ] In `plugin_ide.ui/resources/intro/`, fix `intro.xml` and `intro.css`: the newly replaced LANG_PROJECT_ID string needs to have the dots replaced by hyphens.
So for example, if you replaced LANG_PROJECT_ID with `org.dsource.ddt`, then that needs to change to `org-dsource-ddt`.

* [ ] Update Changelog, Features, UserGuide, etc, in documentation folder, according to your IDE.

* [ ] Delete README-MelnormeEclipse.md
 
* [ ] Write language specific code. Several language specific implementations need to be created or customized. These are usually marked with the `TODO: LANG` Java comment. See next section for a more detailed guide.

#### Implementing language specific features.

###### Syntax highlighting:
Customize `LANGUAGE_CodeScanner`, and `LANGUAGE_PartitionScanner`. These follow the standard Eclipse way of doings things, see: http://help.eclipse.org/mars/index.jsp?topic=/org.eclipse.platform.doc.isv/guide/editors_highlighting.htm .

###### Outline and parser errors
Customize `LANGUAGE_SourceModelManager`. You need to integrate with a parser library or external tool, to then transform the parser output (the AST), into a simplified `SourceFileStructure`. This is a structure derived from a source file, with parse errors and structure elements. A structure element is just a description of top-level source-file definitions, like function/type/class definitions. This is used for the outline. (potentially code folding too, in the future)

###### #TODO: describe the other customization points


#### Merging new updates from upstream MelnormeEclipse

MelnormeEclipse doesn't have formal releases, so each new commit potentially needs to be handled and reviewed on its own. And because MelnormeEclipse is still in its infancy, there is a fast-and-furious approach to refactoring, so there are frequent breaking API changes. However there are some rules to help manage this process:

 * Commits with prefix `!MELNORME` in the commit message are intented for MelnormeEclipse only, not for the concrete IDEs.  Examples includes updates to `LANGUAGE_` classes or MelnormeEclipse-only documentation. These updates should be merged with an ignore strategy (`git merge -s ours` in command line, or in a Git UI: merge the commit without creating a new merge commit, reset the index to the previous revision, then commit).
 * Commits with prefix `!API` in the commit message introduce breaking API changes. Look into the rest of the commit message (and possibly the commit changes too), to understand the new API changes, and what needs to be updated in the concrete IDE. Make sure to build your project to check for compile errors. Note that sometimes, the API changes might just be rename/moves, not actual behavior changes. It is recommend *to NOT merge more than one `!API` commit at once*, but each one individually, and make sure your project compiles successfully on each merge. This is to make sure each independent refactoring step is applied correctly. Merging multiple `!API` commits at once and only then checking for compile errors can be done, but it's a bit of a risk, since it might introduce bugs that would otherwise have been caught by compiling and reviewing each commit individually.
 * For all other commits, you should be able to merge them safely without having to look into detail on what the change does. Example would be: bugfixes, minor performance improvements, or even API changes that are non-breaking. It should be safe to merge several non-`!API` commits at once. You might still want to look into the commit message, in case new API is introduced that the concrete IDE might make use of.
