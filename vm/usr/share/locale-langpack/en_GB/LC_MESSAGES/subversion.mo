��    4      �  G   \      x    y    �  "   �     �  {  �  L   a  1   �  %   �  *     &   1  /   X  9   �  )   �  4   �  0   !     R  %   q  R   �  $   �  -     e  =  6   �     �  �  �  ,   �   :   �   2   ,!     _!     |!      �!  /   �!  0   �!  6   "     R"  '   q"  )   �"     �"  %   �"  "   #  -   )#     W#  �  p#  ~   :*  9   �*     �*     +  u  !+  h  �-  v    0  q  w0  �   �2  �  p3  #  F5    j7  "   �9     �9  {  �9  L   8@  1   �@  %   �@  +   �@  '   	A  /   1A  9   aA  )   �A  4   �A  0   �A     +B  %   JB  R   pB  $   �B  -   �B  f  C  6   }D     �D  �  �D  ,   �Q  :   �Q  3   R     ;R     XR      uR  /   �R  0   �R  6   �R     .S  '   MS  )   uS     �S  %   �S  "   �S  -   T     3T  �  LT     [  9   �[     �[     �[  v  �[  h  v^  v   �`  q  Va  �   �c                               (   !   2          ,      .       -                %   1   $   *            
             )       0          3                   "   #                       4   /           +   &                  	                     '        
-----------------------------------------------------------------------
ATTENTION!  Your passphrase for client certificate:

   %s

can only be stored to disk unencrypted!  You are advised to configure
your system so that Subversion can store passphrase encrypted, if
possible.  See the documentation for details.

You can avoid future appearances of this warning by setting the value
of the 'store-ssl-client-cert-pp-plaintext' option to either 'yes' or
'no' in '%s'.
-----------------------------------------------------------------------
 
-----------------------------------------------------------------------
ATTENTION!  Your password for authentication realm:

   %s

can only be stored to disk unencrypted!  You are advised to configure
your system so that Subversion can store passwords encrypted, if
possible.  See the documentation for details.

You can avoid future appearances of this warning by setting the value
of the 'store-plaintext-passwords' option to either 'yes' or 'no' in
'%s'.
-----------------------------------------------------------------------
 '%s' has an unrecognized node kind Authorization failed Bring changes from the repository into the working copy.
usage: update [PATH...]

  If no revision is given, bring working copy up-to-date with HEAD rev.
  Else synchronize working copy to revision given by -r.

  For each updated item a line will start with a character reporting the
  action taken.  These characters have the following meaning:

    A  Added
    D  Deleted
    U  Updated
    C  Conflict
    G  Merged
    E  Existed

  A character in the first column signifies an update to the actual file,
  while updates to the file's properties are shown in the second column.
  A 'B' in the third column signifies that the lock for the file has
  been broken or stolen.

  If --force is used, unversioned obstructing paths in the working
  copy do not automatically cause a failure if the update attempts to
  add the same path.  If the obstructing path is the same type (file
  or directory) as the corresponding path in the repository it becomes
  versioned but its contents are left 'as-is' in the working copy.
  This means that an obstructing directory's unversioned children may
  also obstruct and become versioned.  For files, any content differences
  between the obstruction and the repository are treated like a local
  modification to the working copy.  All properties from the repository
  are applied to the obstructing path.  Obstructing paths are reported
  in the first column with code 'E'.

  Use the --set-depth option to set a new working copy depth on the
  targets of this operation.  Currently, the depth of a working copy
  directory can only be increased (telescoped more deeply); you cannot
  make a directory more shallow.
 Cannot copy revprops for a revision (%ld) that has not been synchronized yet Cannot initialize a repository with content in it Could not initialize the SASL library Couldn't obtain the authenticated username Couldn't perform atomic initialization Destination repository has not been initialized Destination repository is already synchronizing from '%s' Error initializing command line arguments Error normalizing edited contents to internal format Error normalizing log message to internal format Got unrecognized encoding '%s' Initialization of SSPI library failed NOTE: Normalized %s* properties to LF line endings (%d rev-props, %d node-props).
 Network socket initialization failed Not authorized to open root of edit operation Remove 'conflicted' state on working copy files or directories.
usage: resolved PATH...

  Note:  this subcommand does not semantically resolve conflicts or
  remove conflict markers; it merely removes the conflict-related
  artifact files and allows PATH to be committed again.  It has been
  deprecated in favor of running 'svn resolve --accept working'.
 Repository '%s' is not initialized for synchronization Schema format %d not recognized Set the value of a property on files, dirs, or revisions.
usage: 1. propset PROPNAME PROPVAL PATH...
       2. propset PROPNAME --revprop -r REV PROPVAL [TARGET]

  1. Changes a versioned file or directory property in a working copy.
  2. Changes an unversioned property on a repository revision.
     (TARGET only determines which repository to access.)

  The value may be provided with the --file option instead of PROPVAL.

  Note: svn recognizes the following special versioned properties
  but will store any arbitrary properties set:
    svn:ignore     - A newline separated list of file glob patterns to ignore.
    svn:keywords   - Keywords to be expanded.  Valid keywords are:
      URL, HeadURL             - The URL for the head version of the object.
      Author, LastChangedBy    - The last person to modify the file.
      Date, LastChangedDate    - The date/time the object was last modified.
      Rev, Revision,           - The last revision the object changed.
      LastChangedRevision
      Id                       - A compressed summary of the previous
                                   4 keywords.
      Header                   - Similar to Id but includes the full URL.
    svn:executable - If present, make the file executable.  Use
      'svn propdel svn:executable PATH...' to clear.
    svn:eol-style  - One of 'native', 'LF', 'CR', 'CRLF'.
    svn:mime-type  - The mimetype of the file.  Used to determine
      whether to merge the file, and how to serve it from Apache.
      A mimetype beginning with 'text/' (or an absent mimetype) is
      treated as text.  Anything else is treated as binary.
    svn:externals  - A newline separated list of module specifiers,
      each of which consists of a relative directory path, optional
      revision flags and an URL.  The ordering of the three elements
      implements different behavior.  Subversion 1.4 and earlier only
      support the following formats and the URLs cannot have peg
      revisions:
        foo             http://example.com/repos/zig
        foo/bar -r 1234 http://example.com/repos/zag
      Subversion 1.5 and greater support the above formats and the
      following formats where the URLs may have peg revisions:
                http://example.com/repos/zig foo
        -r 1234 http://example.com/repos/zig foo/bar
      Relative URLs are supported in Subversion 1.5 and greater for
      all above formats and are indicated by starting the URL with one
      of the following strings
        ../  to the parent directory of the extracted external
        ^/   to the repository root
        //   to the scheme
        /    to the server root
      The ambiguous format 'relative_path relative_path' is taken as
      'relative_url relative_path' with peg revision support.
    svn:needs-lock - If present, indicates that the file should be locked
      before it is modified.  Makes the working copy file read-only
      when it is not locked.  Use 'svn propdel svn:needs-lock PATH...'
      to clear.

  The svn:keywords, svn:executable, svn:eol-style, svn:mime-type and
  svn:needs-lock properties cannot be set on a directory.  A non-recursive
  attempt will fail, and a recursive attempt will set the property
  only on the file children of the directory.
 Source url '%s' is from different repository Summarizing diff can only compare repository to repository Svndiff data contains backward-sliding source view Unknown authorization method Unrecognized URL scheme '%s' Unrecognized URL scheme for '%s' Unrecognized binary data encoding; can't decode Unrecognized file in argument of --config-option Unrecognized format for the relative external URL '%s' Unrecognized line ending style Unrecognized line ending style for '%s' Unrecognized logfile element '%s' in '%s' Unrecognized node kind: '%s' Unrecognized node-action on node '%s' Unrecognized record type in stream Unrecognized revision type requested for '%s' Unrecognized stream data Update the working copy to a different URL.
usage: 1. switch URL[@PEGREV] [PATH]
       2. switch --relocate FROM TO [PATH...]

  1. Update the working copy to mirror a new URL within the repository.
     This behavior is similar to 'svn update', and is the way to
     move a working copy to a branch or tag within the same repository.
     If specified, PEGREV determines in which revision the target is first
     looked up.

     If --force is used, unversioned obstructing paths in the working
     copy do not automatically cause a failure if the switch attempts to
     add the same path.  If the obstructing path is the same type (file
     or directory) as the corresponding path in the repository it becomes
     versioned but its contents are left 'as-is' in the working copy.
     This means that an obstructing directory's unversioned children may
     also obstruct and become versioned.  For files, any content differences
     between the obstruction and the repository are treated like a local
     modification to the working copy.  All properties from the repository
     are applied to the obstructing path.

     Use the --set-depth option to set a new working copy depth on the
     targets of this operation.  Currently, the depth of a working copy
     directory can only be increased (telescoped more deeply); you cannot
     make a directory more shallow.

  2. Rewrite working copy URL metadata to reflect a syntactic change only.
     This is used when repository's root URL changes (such as a scheme
     or hostname change) but your working copy still reflects the same
     directory within the same repository.

  See also 'svn help update' for a list of possible characters
  reporting the action taken.
 Upgrade of this repository's underlying versioned filesystem is not supported; consider dumping and loading the data elsewhere Write denied:  not authorized to read all of revision %ld authorization failed authorization failed: %s usage: svnadmin dump REPOS_PATH [-r LOWER[:UPPER] [--incremental]]

Dump the contents of filesystem to stdout in a 'dumpfile'
portable format, sending feedback to stderr.  Dump revisions
LOWER rev through UPPER rev.  If no revisions are given, dump all
revision trees.  If only LOWER is given, dump that one revision tree.
If --incremental is passed, the first revision dumped will describe
only the paths changed in that revision; otherwise it will describe
every path present in the repository as of that revision.  (In either
case, the second and subsequent revisions, if any, describe only paths
changed in those revisions.)
 usage: svnsync copy-revprops DEST_URL [REV[:REV2]]

Copy the revision properties in a given range of revisions to the
destination from the source with which it was initialized.

If REV and REV2 are provided, copy properties for the revisions
specified by that range, inclusively.  If only REV is provided,
copy properties for that revision alone.  If REV is not provided,
copy properties for all revisions previously transferred to the
destination.

REV and REV2 must be revisions which were previously transferred
to the destination.  You may use "HEAD" for either revision to
mean "the last revision transferred".
 usage: svnsync info DEST_URL

Print information about the synchronization destination repository
located at DEST_URL.
 usage: svnsync initialize DEST_URL SOURCE_URL

Initialize a destination repository for synchronization from
another repository.

The destination URL must point to the root of a repository with
no committed revisions.  The destination repository must allow
revision property changes.

If the source URL is not the root of a repository, only the
specified part of the repository will be synchronized.

You should not commit to, or make revision property changes in,
the destination repository by any method other than 'svnsync'.
In other words, the destination repository should be a read-only
mirror of the source repository.
 usage: svnsync synchronize DEST_URL

Transfer all pending revisions to the destination from the source
with which it was initialized.
 Project-Id-Version: subversion
Report-Msgid-Bugs-To: FULL NAME <EMAIL@ADDRESS>
POT-Creation-Date: 2010-04-23 06:57+0000
PO-Revision-Date: 2010-03-07 12:57+0000
Last-Translator: Steve Holmes <Unknown>
Language-Team: English (United Kingdom) <en_GB@li.org>
MIME-Version: 1.0
Content-Type: text/plain; charset=UTF-8
Content-Transfer-Encoding: 8bit
Plural-Forms: nplurals=2; plural=n != 1;
X-Launchpad-Export-Date: 2010-07-14 13:39+0000
X-Generator: Launchpad (build 9518)
 
-----------------------------------------------------------------------
ATTENTION!  Your passphrase for client certificate:

   %s

can only be unencrypted when stored to disk!  You are advised to configure
your system so that Subversion can store passphrase encrypted, if
possible.  See the documentation for details.

You can avoid future appearances of this warning by setting the value
of the 'store-ssl-client-cert-pp-plaintext' option to either 'yes' or
'no' in '%s'.
-----------------------------------------------------------------------
 
-----------------------------------------------------------------------
ATTENTION!  Your password for authentication realm:

   %s

can only be unencrypted when stored to disk!  You are advised to configure
your system so that Subversion can store passwords encrypted, if
possible.  See the documentation for details.

You can avoid future appearances of this warning by setting the value
of the 'store-plaintext-passwords' option to either 'yes' or 'no' in
'%s'.
-----------------------------------------------------------------------
 '%s' has an unrecognised node kind Authorisation failed Bring changes from the repository into the working copy.
usage: update [PATH...]

  If no revision is given, bring working copy up-to-date with HEAD rev.
  Else synchronise working copy to revision given by -r.

  For each updated item a line will start with a character reporting the
  action taken.  These characters have the following meaning:

    A  Added
    D  Deleted
    U  Updated
    C  Conflict
    G  Merged
    E  Existed

  A character in the first column signifies an update to the actual file,
  while updates to the file's properties are shown in the second column.
  A 'B' in the third column signifies that the lock for the file has
  been broken or stolen.

  If --force is used, unversioned obstructing paths in the working
  copy do not automatically cause a failure if the update attempts to
  add the same path.  If the obstructing path is the same type (file
  or directory) as the corresponding path in the repository it becomes
  versioned but its contents are left 'as-is' in the working copy.
  This means that an obstructing directory's unversioned children may
  also obstruct and become versioned.  For files, any content differences
  between the obstruction and the repository are treated like a local
  modification to the working copy.  All properties from the repository
  are applied to the obstructing path.  Obstructing paths are reported
  in the first column with code 'E'.

  Use the --set-depth option to set a new working copy depth on the
  targets of this operation.  Currently, the depth of a working copy
  directory can only be increased (telescoped more deeply); you cannot
  make a directory more shallow.
 Cannot copy revprops for a revision (%ld) that has not been synchronised yet Cannot initialise a repository with content in it Could not initialise the SASL library Could not obtain the authenticated username Could not perform atomic initialisation Destination repository has not been initialised Destination repository is already synchronising from '%s' Error initialising command line arguments Error normalising edited contents to internal format Error normalising log message to internal format Got unrecognised encoding '%s' Initialisation of SSPI library failed NOTE: Normalised %s* properties to LF line endings (%d rev-props, %d node-props).
 Network socket initialisation failed Not authorised to open root of edit operation Remove 'conflicted' state on working copy files or directories.
usage: resolved PATH...

  Note:  this subcommand does not semantically resolve conflicts or
  remove conflict markers; it merely removes the conflict-related
  artifact files and allows PATH to be committed again.  It has been
  deprecated in favour of running 'svn resolve --accept working'.
 Repository '%s' is not initialised for synchronisation Schema format %d not recognised Set the value of a property on files, dirs, or revisions.
usage: 1. propset PROPNAME PROPVAL PATH...
       2. propset PROPNAME --revprop -r REV PROPVAL [TARGET]

  1. Changes a versioned file or directory property in a working copy.
  2. Changes an unversioned property on a repository revision.
     (TARGET only determines which repository to access.)

  The value may be provided with the --file option instead of PROPVAL.

  Note: svn recognizes the following special versioned properties
  but will store any arbitrary properties set:
    svn:ignore     - A newline separated list of file glob patterns to ignore.
    svn:keywords   - Keywords to be expanded.  Valid keywords are:
      URL, HeadURL             - The URL for the head version of the object.
      Author, LastChangedBy    - The last person to modify the file.
      Date, LastChangedDate    - The date/time the object was last modified.
      Rev, Revision,           - The last revision the object changed.
      LastChangedRevision
      Id                       - A compressed summary of the previous
                                   4 keywords.
      Header                   - Similar to Id but includes the full URL.
    svn:executable - If present, make the file executable.  Use
      'svn propdel svn:executable PATH...' to clear.
    svn:eol-style  - One of 'native', 'LF', 'CR', 'CRLF'.
    svn:mime-type  - The mimetype of the file.  Used to determine
      whether to merge the file, and how to serve it from Apache.
      A mimetype beginning with 'text/' (or an absent mimetype) is
      treated as text.  Anything else is treated as binary.
    svn:externals  - A newline separated list of module specifiers,
      each of which consists of a relative directory path, optional
      revision flags and an URL.  The ordering of the three elements
      implements different behaviour.  Subversion 1.4 and earlier only
      support the following formats and the URLs cannot have peg
      revisions:
        foo             http://example.com/repos/zig
        foo/bar -r 1234 http://example.com/repos/zag
      Subversion 1.5 and greater support the above formats and the
      following formats where the URLs may have peg revisions:
                http://example.com/repos/zig foo
        -r 1234 http://example.com/repos/zig foo/bar
      Relative URLs are supported in Subversion 1.5 and greater for
      all above formats and are indicated by starting the URL with one
      of the following strings
        ../  to the parent directory of the extracted external
        ^/   to the repository root
        //   to the scheme
        /    to the server root
      The ambiguous format 'relative_path relative_path' is taken as
      'relative_url relative_path' with peg revision support.
    svn:needs-lock - If present, indicates that the file should be locked
      before it is modified.  Makes the working copy file read-only
      when it is not locked.  Use 'svn propdel svn:needs-lock PATH...'
      to clear.

  The svn:keywords, svn:executable, svn:eol-style, svn:mime-type and
  svn:needs-lock properties cannot be set on a directory.  A non-recursive
  attempt will fail, and a recursive attempt will set the property
  only on the file children of the directory.
 Source URL '%s' is from different repository Summarising diff can only compare repository to repository Svndiff data contains backwards-sliding source view Unknown authorisation method Unrecognised URL scheme '%s' Unrecognised URL scheme for '%s' Unrecognised binary data encoding; can't decode Unrecognised file in argument of --config-option Unrecognised format for the relative external URL '%s' Unrecognised line ending style Unrecognised line ending style for '%s' Unrecognised logfile element '%s' in '%s' Unrecognised node kind: '%s' Unrecognised node-action on node '%s' Unrecognised record type in stream Unrecognised revision type requested for '%s' Unrecognised stream data Update the working copy to a different URL.
usage: 1. switch URL[@PEGREV] [PATH]
       2. switch --relocate FROM TO [PATH...]

  1. Update the working copy to mirror a new URL within the repository.
     This behaviour is similar to 'svn update', and is the way to
     move a working copy to a branch or tag within the same repository.
     If specified, PEGREV determines in which revision the target is first
     looked up.

     If --force is used, unversioned obstructing paths in the working
     copy do not automatically cause a failure if the switch attempts to
     add the same path.  If the obstructing path is the same type (file
     or directory) as the corresponding path in the repository it becomes
     versioned but its contents are left 'as-is' in the working copy.
     This means that an obstructing directory's unversioned children may
     also obstruct and become versioned.  For files, any content differences
     between the obstruction and the repository are treated like a local
     modification to the working copy.  All properties from the repository
     are applied to the obstructing path.

     Use the --set-depth option to set a new working copy depth on the
     targets of this operation.  Currently, the depth of a working copy
     directory can only be increased (telescoped more deeply); you cannot
     make a directory more shallow.

  2. Rewrite working copy URL metadata to reflect a syntactic change only.
     This is used when repository's root URL changes (such as a scheme
     or hostname change) but your working copy still reflects the same
     directory within the same repository.

  See also 'svn help update' for a list of possible characters
  reporting the action taken.
 Upgrade of this repository's underlying versioned file system is not supported; consider dumping and loading the data elsewhere Write denied:  not authorised to read all of revision %ld authorisation failed authorisation failed: %s usage: svnadmin dump REPOS_PATH [-r LOWER[:UPPER] [--incremental]]

Dump the contents of file-system to stdout in a 'dumpfile'
portable format, sending feedback to stderr.  Dump revisions
LOWER rev through UPPER rev.  If no revisions are given, dump all
revision trees.  If only LOWER is given, dump that one revision tree.
If --incremental is passed, the first revision dumped will describe
only the paths changed in that revision; otherwise it will describe
every path present in the repository as of that revision.  (In either
case, the second and subsequent revisions, if any, describe only paths
changed in those revisions.)
 usage: svnsync copy-revprops DEST_URL [REV[:REV2]]

Copy the revision properties in a given range of revisions to the
destination from the source with which it was initialised.

If REV and REV2 are provided, copy properties for the revisions
specified by that range, inclusively.  If only REV is provided,
copy properties for that revision alone.  If REV is not provided,
copy properties for all revisions previously transferred to the
destination.

REV and REV2 must be revisions which were previously transferred
to the destination.  You may use "HEAD" for either revision to
mean "the last revision transferred".
 usage: svnsync info DEST_URL

Print information about the synchronisation destination repository
located at DEST_URL.
 usage: svnsync initialize DEST_URL SOURCE_URL

Initialise a destination repository for synchronisation from
another repository.

The destination URL must point to the root of a repository with
no committed revisions.  The destination repository must allow
revision property changes.

If the source URL is not the root of a repository, only the
specified part of the repository will be synchronised.

You should not commit to, or make revision property changes in,
the destination repository by any method other than 'svnsync'.
In other words, the destination repository should be a read-only
mirror of the source repository.
 usage: svnsync synchronise DEST_URL

Transfer all pending revisions to the destination from the source
with which it was initialised.
 