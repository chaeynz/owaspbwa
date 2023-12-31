��    ;      �  O   �        �   	  w   �    @  �  E  b  �  �   I
  �   �
  �   �  >   a  �   �  �  l  �   1  �   �  �  �  �     �  �  �  a  J     �  [  <     �  M  �  �  �   �  e  p  *   �  '     	   )     3     ?     _       ;  �  �   �   �   �!  �   F"  D   �"     #     .#    C#  A   L$  ;  �$  ;   �%  3   &  /   :&  +   j&  '   �&  #   �&     �&     '  0   '  *   O'  3   z'  ]   �'     (     $(  3   <(  7   p(  "   �(  �  �(  �   �*  }   b+    �+  �  �,  c  �.  �   �/  �   �0  �   �1  ?   2  �   O2  �  3  �   �4  �   �5  �  "6  �   �8  �  j9  �  �:  I   �<  �  �<  $   �>  �  �>  �  ~@  �   +B  f  �B  *   ^D  "   �D  	   �D     �D     �D     �D     E  <  "E  �   _F  �   EG  �   �G  N   ^H     �H     �H  
  �H  A   �I  <  "J  :   _K  2   �K  .   �K  *   �K  &   'L  "   NL     qL     �L  1   �L  *   �L  4   M  ]   =M     �M     �M  3   �M  7   �M  "   7N     #      !                  %         1           '      *       (                  /          	   ;                    
   ,      2             3   7                   "      .      )   &      4      +              -   8   :   5                       9          0       $   6           
  --check-order     check that the input is correctly sorted, even
                      if all input lines are pairable
  --nocheck-order   do not check that the input is correctly sorted
 
A field is a run of blanks (usually spaces and/or TABs), then non-blank
characters.  Fields are skipped before chars.
 
Both MAJOR and MINOR must be specified when TYPE is b, c, or u, and they
must be omitted when TYPE is p.  If MAJOR or MINOR begins with 0x or 0X,
it is interpreted as hexadecimal; otherwise, if it begins with 0, as octal;
otherwise, as decimal.  TYPE may be:
 
By default, color is not used to distinguish types of files.  That is
equivalent to using --color=none.  Using the --color option without the
optional WHEN argument is equivalent to using --color=always.  With
--color=auto, color codes are output only if standard output is connected
to a terminal (tty).  The environment variable LS_COLORS can influence the
colors, and can be set easily by the dircolors command.
 
By default, sparse SOURCE files are detected by a crude heuristic and the
corresponding DEST file is made sparse as well.  That is the behavior
selected by --sparse=auto.  Specify --sparse=always to create a sparse DEST
file whenever the SOURCE file contains a long enough sequence of zero bytes.
Use --sparse=never to inhibit creation of sparse files.
 
If -e is in effect, the following sequences are recognized:

  \0NNN   the character whose ASCII code is NNN (octal)
  \\     backslash
  \a     alert (BEL)
  \b     backspace
 
If FILE is specified, read it to determine which colors to use for which
file types and extensions.  Otherwise, a precompiled database is used.
For details on the format of these files, run `dircolors --print-database'.
 
NOTE: [ honors the --help and --version options, but test does not.
test treats each of those as it treats any other nonempty STRING.
 
Note, comparisons honor the rules specified by `LC_COLLATE'.
 
Note: 'uniq' does not detect repeated lines unless they are adjacent.
You may want to sort the input first, or use `sort -u' without `uniq'.
Also, comparisons honor the rules specified by `LC_COLLATE'.
 
POS is F[.C][OPTS], where F is the field number and C the character position
in the field; both are origin 1.  If neither -t nor -b is in effect, characters
in a field are counted from the beginning of the preceding whitespace.  OPTS is
one or more single-letter ordering options, which override global ordering
options for that key.  If no key is given, use the entire line as the key.

SIZE may be followed by the following multiplicative suffixes:
 
Special settings:
   N             set the input and output speeds to N bauds
 * cols N        tell the kernel that the terminal has N columns
 * columns N     same as cols N
 
TYPE is made up of one or more of these specifications:

  a          named character, ignoring high-order bit
  c          ASCII character or backslash escape
 
Unless -t CHAR is given, leading blanks separate fields and are ignored,
else fields are separated by CHAR.  Any FIELD is a field number counted
from 1.  FORMAT is one or more comma or blank separated specifications,
each being `FILENUM.FIELD' or `0'.  Default FORMAT outputs the join field,
the remaining fields from FILE1, the remaining fields from FILE2, all
separated by CHAR.

Important: FILE1 and FILE2 must be sorted on the join fields.
E.g., use `sort -k 1b,1' if `join' has no options.
Note, comparisons honor the rules specified by `LC_COLLATE'.
If the input is not sorted and some lines cannot be joined, a
warning message will be given.
 
With no options, produce three-column output.  Column one contains
lines unique to FILE1, column two contains lines unique to FILE2,
and column three contains lines common to both files.
       --files0-from=F   summarize disk usage of the NUL-terminated file
                          names specified in file F;
                          If F is - then read names from standard input
  -H                    equivalent to --dereference-args (-D)
  -h, --human-readable  print sizes in human readable format (e.g., 1K 234M 2G)
      --si              like -h, but use powers of 1000 not 1024
   -C                         list entries by columns
      --color[=WHEN]         control whether color is used to distinguish file
                               types.  WHEN may be `never', `always', or `auto'
  -d, --directory            list directory entries instead of contents,
                               and do not dereference symbolic links
  -D, --dired                generate output designed for Emacs' dired mode
   -Z, --context=CTX  set the SELinux security context of each NAME to CTX
   -f, --canonicalize            canonicalize by following every symlink in
                                every component of the given name recursively;
                                all but the last component must exist
  -e, --canonicalize-existing   canonicalize by following every symlink in
                                every component of the given name recursively,
                                all components must exist
   -g                         like -l, but do not list owner
   -h, --header=HEADER
                    use a centered HEADER instead of filename in page header,
                    -h "" prints a blank line, don't use -h""
  -i[CHAR[WIDTH]], --output-tabs[=CHAR[WIDTH]]
                    replace spaces with CHARs (TABs) to tab WIDTH (8)
  -J, --join-lines  merge full lines, turns off -W line truncation, no column
                    alignment, --sep-string[=STRING] sets separators
   -m, --canonicalize-missing    canonicalize by following every symlink in
                                every component of the given name recursively,
                                without requirements on components existence
  -n, --no-newline              do not output the trailing newline
  -q, --quiet,
  -s, --silent                  suppress most error messages
  -v, --verbose                 report error messages
   -s, --signal=SIGNAL
                   specify the signal to be sent on timeout.
                   SIGNAL may be a name like `HUP' or a number.
                   See `kill -l` for a list of signals
   -v FILENUM        like -a FILENUM, but suppress joined output lines
  -1 FIELD          join on this FIELD of file 1
  -2 FIELD          join on this FIELD of file 2
  --check-order     check that the input is correctly sorted, even
                      if all input lines are pairable
  --nocheck-order   do not check that the input is correctly sorted
   dsync     use synchronized I/O for data
  %s-blocks      Used Available Capacity %b %e  %Y %b %e %H:%M %s: unrecognized option `%c%s'
 %s: unrecognized option `--%s'
 %s:%lu: unrecognized keyword %s * log-structured or journaled file systems, such as those supplied with
AIX and Solaris (and JFS, ReiserFS, XFS, Ext3, etc.)

* file systems that write redundant data and carry on even if some writes
fail, such as RAID-based file systems

* file systems that make snapshots, such as Network Appliance's NFS server

 Diagnose invalid or unportable file names.

  -p                  check for most POSIX systems
  -P                  check for empty names and leading "-"
      --portability   check for all POSIX systems (equivalent to -p -P)
 Output pieces of FILE separated by PATTERN(s) to files `xx00', `xx01', ...,
and output byte counts of each piece to standard output.

 Remove (unlink) the FILE(s).

  -f, --force           ignore nonexistent files, never prompt
  -i                    prompt before every removal
 Report %s translation bugs to <http://translationproject.org/team/>
 Request canceled Request not canceled Run COMMAND with an adjusted niceness, which affects process scheduling.
With no COMMAND, print the current niceness.  Nicenesses range from
%d (most favorable scheduling) to %d (least favorable).

  -n, --adjustment=N   add integer N to the niceness (default 10)
 Summarize disk usage of each FILE, recursively for directories.

 This default behavior is not desirable when you really want to
track the actual name of the file, not the file descriptor (e.g., log
rotation).  Use --follow=name in that case.  That causes tail to track the
named file by reopening it periodically to see if it has been removed and
recreated by some other program.
 Written by %s, %s, %s,
%s, %s, %s, %s,
%s, %s, and others.
 Written by %s, %s, %s,
%s, %s, %s, %s,
%s, and %s.
 Written by %s, %s, %s,
%s, %s, %s, %s,
and %s.
 Written by %s, %s, %s,
%s, %s, %s, and %s.
 Written by %s, %s, %s,
%s, %s, and %s.
 Written by %s, %s, %s,
%s, and %s.
 Written by %s, %s, %s,
and %s.
 Written by %s, %s, and %s.
 can't apply partial context to unlabeled file %s cannot both summarize and show all entries delimiter list ends with an unescaped backslash: %s invalid option -- %c; -WIDTH is recognized only when it is the first
option; use -w N instead unrecognized operand %s unrecognized prefix: %s warning: summarizing conflicts with --max-depth=%lu warning: summarizing is the same as using --max-depth=0 warning: unrecognized escape `\%c' Project-Id-Version: coreutils
Report-Msgid-Bugs-To: FULL NAME <EMAIL@ADDRESS>
POT-Creation-Date: 2009-05-07 14:14+0200
PO-Revision-Date: 2010-02-11 20:05+0000
Last-Translator: Robert Readman <Unknown>
Language-Team: English (United Kingdom) <en_GB@li.org>
MIME-Version: 1.0
Content-Type: text/plain; charset=UTF-8
Content-Transfer-Encoding: 8bit
Plural-Forms: nplurals=2; plural=n != 1;
X-Launchpad-Export-Date: 2010-07-14 10:19+0000
X-Generator: Launchpad (build 9518)
 
  --check-order     check that the input is correctly sorted, even
                      if all input lines can be paired
  --nocheck-order   do not check that the input is correctly sorted
 
A field is a run of blanks (usually spaces and/or TABs) followed by non-blank
characters.  Fields are skipped before chars.
 
Both MAJOR and MINOR must be specified when TYPE is b, c, or u, and they
must be omitted when TYPE is p.  If MAJOR or MINOR begins with 0x or 0X,
it is interpreted as hexadecimal; if it begins with 0, as octal;
otherwise, it is interpreted as decimal.  TYPE may be:
 
By default, colour is not used to distinguish types of files.  That is
equivalent to using --color=none.  Using the --color option without the
optional WHEN argument is equivalent to using --color=always.  With
--color=auto, colour codes are output only if standard output is connected
to a terminal (tty).  The environment variable LS_COLORS can influence the
colours, and can be set easily by the dircolors command.
 
By default, sparse SOURCE files are detected by a crude heuristic and the
corresponding DEST file is made sparse as well.  That is the behaviour
selected by --sparse=auto.  Specify --sparse=always to create a sparse DEST
file whenever the SOURCE file contains a long enough sequence of zero bytes.
Use --sparse=never to inhibit creation of sparse files.
 
If -e is in effect, the following sequences are recognised:

  \0NNN   the character whose ASCII code is NNN (octal)
  \\     backslash
  \a     alert (BEL)
  \b     backspace
 
If FILE is specified, read it to determine which colours to use for which
file types and extensions.  Otherwise, a precompiled database is used.
For details on the format of these files, run `dircolors --print-database'.
 
NOTE: [ honours the --help and --version options, but test does not.
test treats each of those as it treats any other non-empty STRING.
 
Note, comparisons honour the rules specified by `LC_COLLATE'.
 
Note: 'uniq' does not detect repeated lines unless they are adjacent.
You may want to sort the input first, or use `sort -u' without `uniq'.
Also, comparisons honour the rules specified by `LC_COLLATE'.
 
POS is F[.C][OPTS], where F is the field number and C the character position
in the field; both are origin 1.  If neither -t nor -b is in effect, characters
in a field are counted from the beginning of the preceding white space.  OPTS is
one or more single-letter ordering options, which override global ordering
options for that key.  If no key is given, use the entire line as the key.

SIZE may be followed by the following multiplicative suffixes:
 
Special settings:
   N             set the input and output speeds to N baud
 * cols N        tell the kernel that the terminal has N columns
 * columns N     same as cols N
 
TYPE is made up of one or more of these specifications:

  a named character, ignoring high-order bit
  c ASCII character or backslash escape
 
Unless -t CHAR is given, leading blanks separate fields and are ignored,
else fields are separated by CHAR.  Any FIELD is a field number counted
from 1.  FORMAT is one or more comma or blank separated specifications,
each being `FILENUM.FIELD' or `0'.  Default FORMAT outputs the join field,
the remaining fields from FILE1, the remaining fields from FILE2, all
separated by CHAR.

Important: FILE1 and FILE2 must be sorted on the join fields.
E.g., use `sort -k 1b,1' if `join' has no options.
Note, comparisons honour the rules specified by `LC_COLLATE'.
If the input is not sorted and some lines cannot be joined, a
warning message will be given.
 
With no options, produce three-column output.  Column one contains
lines unique to FILE1, column two contains lines unique to FILE2
and column three contains lines common to both files.
       --files0-from=F   summarise disk usage of the NUL-terminated file
                          names specified in file F;
                          If F is - then read names from standard input
  -H                    equivalent to --dereference-args (-D)
  -h, --human-readable  print sizes in human readable format (e.g., 1K 234M 2G)
      --si              like -h, but use powers of 1000 not 1024
   -C                         list entries by columns
      --color[=WHEN]         control whether colour is used to distinguish file
                               types.  WHEN may be `never', `always', or `auto'
  -d, --directory            list directory entries instead of contents,
                               and do not dereference symbolic links
  -D, --dired                generate output designed for Emacs' dired mode
   -Z, --context=CTX set the SELinux security context of each NAME to CTX
   -f, --canonicalize            canonicalise by following every symlink in
                                every component of the given name recursively;
                                all but the last component must exist
  -e, --canonicalize-existing   canonicalise by following every symlink in
                                every component of the given name recursively,
                                all components must exist
   -g like -l, but do not list owner
   -h, --header=HEADER
                    use a centred HEADER instead of file-name in page header,
                    -h "" prints a blank line, don't use -h""
  -i[CHAR[WIDTH]], --output-tabs[=CHAR[WIDTH]]
                    replace spaces with CHARs (TABs) to tab WIDTH (8)
  -J, --join-lines  merge full lines, turns off -W line truncation, no column
                    alignment, --sep-string[=STRING] sets separators
   -m, --canonicalize-missing    canonicalise by following every symlink in
                                every component of the given name recursively,
                                without requirements on components existence
  -n, --no-newline              do not output the trailing newline
  -q, --quiet,
  -s, --silent                  suppress most error messages
  -v, --verbose                 report error messages
   -s, --signal=SIGNAL
                   specify the signal to be sent on time-out.
                   SIGNAL may be a name like `HUP' or a number.
                   See `kill -l` for a list of signals
   -v FILENUM        like -a FILENUM, but suppress joined output lines
  -1 FIELD          join on this FIELD of file 1
  -2 FIELD          join on this FIELD of file 2
  --check-order     check that the input is correctly sorted, even
                      if all input lines can be paired
  --nocheck-order   do not check that the input is correctly sorted
   dsync     use synchronised I/O for data
  %s-blocks Used Available Capacity %e %b  %Y %e %b %H:%M %s: unrecognised option `%c%s'
 %s: unrecognised option `--%s'
 %s:%lu: unrecognised keyword %s * log-structured or journalled file systems, such as those supplied with
AIX and Solaris (and JFS, ReiserFS, XFS, Ext3, etc.)

* file systems that write redundant data and carry on even if some writes
fail, such as RAID-based file systems

* file systems that make snapshots, such as Network Appliance's NFS server

 Diagnose invalid or non-portable file names.

  -p                  check for most POSIX systems
  -P                  check for empty names and leading "-"
      --portability   check for all POSIX systems (equivalent to -p -P)
 Output pieces of FILE separated by PATTERN(s) to files `xx00', `xx01', ...
and output byte counts of each piece to standard output.

 Remove (unlink) the FILE(s).

  -f, --force           ignore non-existent files, never prompt
  -i                    prompt before every removal
 Report %s translation bugs to <http://translationproject.org/team/en_GB.html>
 Request cancelled Request not cancelled Run COMMAND with an adjusted niceness, which affects process scheduling.
With no COMMAND, print the current niceness.  Nicenesses range from
%d (most favourable scheduling) to %d (least favourable).

  -n, --adjustment=N   add integer N to the niceness (default 10)
 Summarise disk usage of each FILE, recursively for directories.

 This default behaviour is not desirable when you really want to
track the actual name of the file, not the file descriptor (e.g., log
rotation).  Use --follow=name in that case.  That causes tail to track the
named file by reopening it periodically to see if it has been removed and
recreated by some other program.
 Written by %s, %s, %s,
%s, %s, %s, %s,
%s, %s and others.
 Written by %s, %s, %s,
%s, %s, %s, %s,
%s and %s.
 Written by %s, %s, %s,
%s, %s, %s, %s
and %s.
 Written by %s, %s, %s,
%s, %s, %s and %s.
 Written by %s, %s, %s,
%s, %s and %s.
 Written by %s, %s, %s,
%s and %s.
 Written by %s, %s, %s
and %s.
 Written by %s, %s and %s.
 can't apply partial context to unlabelled file %s cannot both summarise and show all entries delimiter list ends with a non-escaped backslash: %s invalid option -- %c; -WIDTH is recognised only when it is the first
option; use -w N instead unrecognised operand %s unrecognised prefix: %s warning: summarising conflicts with --max-depth=%lu warning: summarising is the same as using --max-depth=0 warning: unrecognised escape `\%c' 