README
======

Svndump-to-git-repository-tools

The svndump-to-git-converter uses these tools to read the subversion version 2 dumpfile and to write into the bare git repository.

There is also a JUNG2 based graph viewer.  This was created because of very hairy history and one initial rewriting approch centered on splitting the converted repository into parts that could be rejoined later using grafts or replacement refs.

License:
========

	Copyright 2013-2014 Kuali Foundation
	Copyright 2017 Michael O'Cleirigh 
	Licensed under the
	Educational Community License, Version 2.0 (the "License"); you may
	not use this file except in compliance with the License. You may
	obtain a copy of the License at

	http://www.osedu.org/licenses/ECL-2.0

	Unless required by applicable law or agreed to in writing,
	software distributed under the License is distributed on an "AS IS"
	BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
	or implied. See the License for the specific language governing
	permissions and limitations under the License.

The Java code uses FileInputStream's and FileOutputStream's and some code copied from SVNKit that allows us to stream.readLine() in a way similiar to how a BufferedReader works.

See IOUtils for the usage of the methods.  It is a copy of org.tmatesoft.svn.core.internal.wc.SVNUtil version 1.7.8 which is licenced by the SVN Kit Licence.

http://svnkit.com/license.html

History
-------

This is a fork of code originally written as part of the Kuali Student git migration project.

A 77740 revision repository with 20,631 branches:
https://github.com/kuali-student/archived-from-svn


Artifacts:
----------

repository-tools-common:

	Code common to both git and svn

	BranchData class which represents a branch part and path part.  (an absolute path to a blob is split into an instance of this class)

repository-tools-common-svn:
	
	Contains the SvnDumpFilter and INodeFilter constructs used to inject alternate copyfrom property values.

repository-tools-common-git:

	Common code related to branch detection and out mutable GitTreeNode object model (it generates unmutable git objects)

	The main purpose of this module is to allow sharing code with the Kuali Foundation's fusion-maven-plugin.
	
