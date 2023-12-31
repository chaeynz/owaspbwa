#!/usr/bin/perl -w
# If your copy of perl is not in /usr/bin, please adjust the line above.
#
# Copyright 1998-2008 VMware, Inc.  All rights reserved.
#
# Host configurator for VMware

use strict;

# Use Config module to update VMware host-wide configuration file
# BEGINNING_OF_CONFIG_DOT_PM
#!/usr/bin/perl

###
### TODOs:
###  config file hierarchies
###  open/close/check file
###  error handling
###  config file checker
###  pretty print should print not present devices not in misc
###

use strict;
package VMware::Config;

my %PREF;

#$PREF{'commentChanges'} = 1;

sub new() {
  my $proto = shift;
  my $class = ref($proto) || $proto;
  my $self = $proto->create();
  bless($self, $class);
  return($self);
}

sub create {
  my $self = {};
  $self->{db} = {};
  $self->{tr} = 1;
  return($self);  
}

sub preserve_case($) {
  my $self = shift;
  my $preserve = shift;
  $self->{tr} = !$preserve;
}

sub clear() {
  my $self = {};
  $self->{db} = {};
}

sub readin($) {
  my $self = shift;
  my ($file) = @_;

  my $text = "";
  
  my @stat = stat($file);
  $self->{timestamp} = $stat[9];

  open(CFG, "< $file") || return undef;
  
  while (<CFG>) {
    $text = $text . $_;
  }
  
  close(CFG);

  my $ret = $self->parse($text);
  if (!defined($ret)) {
    return undef;
  }

  $self->{file} = $file;
  $self->{text} = $text;

  return 1;
}

sub writeout($) {
  my $self = shift;
  my ($file) = @_;

  if (!defined($file)) {
    $file = $self->{file};
  }

  open(CFG, "> $file") || return undef;
  print CFG $self->update($self->{text});
  close(CFG);

  return 1;
}

sub overwrite($$) {
  my $self = shift;
  my($orig, $file) = @_;
  
  if (!defined($file)) {
    $file = $orig->{file};
  }
  
  open(CFG, "> $file") || return undef;
  print CFG $self->update($orig->{text});
  close(CFG);

  return 1;  
}

sub pretty_overwrite {
  my $self = shift;
  my($file) = @_;
  
  if (!defined($file)) {
    $file = $self->{file};
  }
  
  open(CFG, "> $file") || return undef;
  print CFG $self->pretty_print();
  close(CFG);

  return 1;  
}

sub parse($) {
  my $self = shift;
  my ($text) = @_;
  my(@lines, $line, $num);
  
  @lines = split(/\n/, $text);
  $num = 1;
  
  foreach $line (@lines) {
    my($status, $name, $value, $start, $end) = $self->parse_line($line);
    if (!defined($status)) {
      $self->clear();
      # syntax error on line $num
      return undef;
    } elsif ($status == 1) {
      if ($self->{tr}) {
        $name =~ tr/A-Z/a-z/;
      }
      $self->{db}{$name}{value} = $value;
      $self->{db}{$name}{modified} = 0;
      $self->{db}{$name}{mark} = 0;
    } elsif ($status == 0) {
      # noop
    } else {
      $self->clear();
      # internal error
      return undef;
    }
    $num++;
  }
  
  return 1;
}

sub timestamp() {
  my $self = shift;
  return $self->{timestamp};
}

sub get() {
  my $self = shift;
  my($name, $default) = @_;
  if ($self->{tr}) {
    $name =~ tr/A-Z/a-z/;
  }
  if (defined($self->{db}{$name})) {
    $self->{db}{$name}{mark} = 1;
    return $self->{db}{$name}{value};
  } else {
    return $default;
  }
}
        
sub get_bool() {
  my $self = shift;
  my($name, $default) = @_;
  my $val = $self->get($name);
  if (!defined($val)) {
    $val = $default;
  }
  if ($val =~ /TRUE|1|Y|YES/i) {
    $val = 1;
  } else {
    $val = 0;
  }
  return $val;
}
        
sub set($$) {
  my $self = shift;
  my($name, $value) = @_;
  if ($self->{tr}) {
    $name =~ tr/A-Z/a-z/;
  }
  $self->{db}{$name}{value} = $value;
  $self->{db}{$name}{modified} = 1;
  $self->{db}{$name}{mark} = 0;
}

sub remove($) {
  my $self = shift;
  my($name) = @_;
  if ($self->{tr}) {
    $name =~ tr/A-Z/a-z/;
  }
  delete $self->{db}{$name};
}

sub list($) {
  my $self = shift;
  my($pattern) = @_;
  return sort(grep(/$pattern/, keys(%{$self->{db}})));
}

sub device_list {
  my $self = shift;
  my($name, $pattern, $show_all) = @_;
  my($dev, $val, %present);

  $show_all = 0 if (!defined($show_all));

  foreach $_ (keys(%{$self->{db}})) {
    if (/$name($pattern)\.present/) {
      $dev = $name . $1;
      $val = $self->get_bool("$dev.present");
      if ($show_all || !defined($val) || ($val)) {
        $present{$dev} = 1;
      }
    }
  }

  return sort(keys(%present));
}

sub update($) {
  my $self = shift;
  my ($text) = @_;
  my $out = "";
  my($line, $name);
  
  my @lines;
  if (defined($text)) {
    @lines = split(/\n/, $text);
  }
  my $num = 1;

  $self->unmark_all();
  
  foreach $line (@lines) {
    my($status, $name, $value, $start, $end) = $self->parse_line($line);

    if (defined($name)) {
      if ($self->{tr}) {
        $name =~ tr/A-Z/a-z/;
      }
    }

    ###
    ### five cases
    ###
    ###   1. deleted
    ###   2. modified
    ###   3. unmodified
    ###   4. comment or blank line
    ###   5. new (handled at the end)
    ###

    $line = $line . "\n";

    if (!defined($status)) {
      # XXX syntax error on line $num
      return undef;
      
    } elsif ($status == 1) {
      if (!defined($self->{db}{$name})) {
        ###
        ### Case 1. removed
        ###
        
        if (defined($PREF{'commentChanges'})) {
          $line = "# " . $line;
        } else {
          $line = "";
        }
        
      } else {
        $self->mark($name);

        if ($self->{db}{$name}{value} ne $value) {  
          ###
          ### Case 2. modified
          ###
          
          my $newline = substr($line, 0, $start) 
            . "\"" . $self->{db}{$name}{value} . "\"" . substr($line, $end);
          
          if (defined($PREF{'commentChanges'})) {
            $line = "# " . $line . $newline;
          } else {
            $line = $newline;
          }
          
        } else {
          ###
          ### Case 3. unmodified
          ###
        }
      }

    } elsif ($status == 0) {
      ###
      ### Case 4. comment or blank line
      ###
      
    } else {
      # XXX internal error: parse_line returned unknown status \"$status\"
      return undef;
    }
    
    $out = $out . "$line";
    $num++;
  }

  ###
  ### Case 5. new entries
  ###

  $out = $out . $self->print_unmarked();

  return $out;
}

sub dump_all() {
  my $self = shift;
  my $out = "";
  my $name;
  
  foreach $name (keys(%{$self->{db}})) {
    $out = $out . "$name = \"$self->{db}{$name}{value}\"\n";
  }
  
  return $out;
}

sub pretty_print($) {
  my $self = shift;
  my($templ) = @_;
  my $out = "";
  my $sec;

  $self->unmark_all();
  
  foreach $sec (@{$templ}) {  
    $out = $out . $self->print_section($sec, "");
  }
  
  $out = $out . "###\n### Misc.\n###\n\n";
  $out = $out . $self->print_unmarked();

  return $out;
}

sub print_section {
  my $self = shift;
  my($sec, $prefix) = @_;
  my $out = "";

  my @list;
  my $dev;
  
  if (defined($sec->{header})) {
    $out = $out . "###\n### $sec->{header}\n###\n\n";
  }
  
  ## name is here for compatibility, it should go away soon.
  my $name = defined($sec->{name}) ? $sec->{name} : "";

  if (defined($sec->{pattern})) {
    @list = $self->device_list($prefix . $name, $sec->{pattern}, 1);
    foreach $dev (@list) {
      if (defined($sec->{title})) {
        $out = $out . sprintf("# $sec->{title}\n\n", $dev);
      }
      $out = $out . $self->print_values("$dev", $sec->{values});
      if (defined($sec->{sublist})) {
        $out = $out . $self->print_section($sec->{sublist}, "$dev");
      }
    }
  } else {
    if (defined($sec->{values})) {
      $out = $out . $self->print_values($prefix . $name, $sec->{values});
    } else {
      $out = $out . $self->print_value($prefix . $name, "is not set");
      $out = $out . "\n";
    }
  }

  return $out;
}

sub print_values {
  my $self = shift;
  my($name, $vars) = @_;
  my $var;

  my $out = "";
  
   foreach $var (@{$vars}) {
     my $v = ($name ne "") ? "$name.$var" : $var;
     $out = $out . $self->print_value($v);
  }

  $out = $out . "\n";

  return $out;
}

sub print_value {
  my $self = shift;
  my($name, $notset) = @_;
  my $val = $self->get($name);
  if (defined($val)) {
    $self->mark($name);
    return "$name = \"$val\"\n";
  } elsif (defined($notset)) {
    return "# $name $notset\n";
  }
}

sub mark($) {
  my $self = shift;
  my($name) = @_;
  if ($self->{tr}) {
    $name =~ tr/A-Z/a-z/;
  }
  $self->{db}{$name}{mark} = 1;
}

sub unmark_all() {
  my $self = shift;
  my $name;
  foreach $name (keys %{$self->{db}}) {
    $self->{db}{$name}{mark} = 0;
  }
}

sub get_unmarked() {
  my $self = shift;
  my $name;
  my @list = ();
  foreach $name (keys %{$self->{db}}) {
    if (!$self->{db}{$name}{mark}) {
      push(@list, $name);
    }
  }
  return @list;
}

sub print_unmarked() {
  my $self = shift;
  my @unmarked = $self->get_unmarked();
  my $out = "";
  my $name;
  
  foreach $name (@unmarked) {
    $out = $out . "$name = \"$self->{db}{$name}{value}\"\n";
  }

  return $out;
}

sub parse_line($) {
  my $self = shift;
  ($_) = @_;

  if (/^\s*(\#.*)?$/) {
    return (0);
  } elsif (/^((\s*(\S+)\s*=\s*)(([\"]([^\"]*)[\"])|(\S+)))\s*(\#.*)?$/) {
    my $prefix1 = $2;
    my $prefix2 = $1;
    my $name = $3;
    my $value;
    if (defined($6)) {
      $value = $6;
    } else {
      $value = $7;
    }

    return (1, $name, $value, length($prefix1), length($prefix2));
  } 

  return (undef);  
}



1;

# END_OF_CONFIG_DOT_PM

# BEGINNING_OF_UTIL_DOT_PL
#!/usr/bin/perl

use strict;
no warnings 'once'; # Warns about use of Config::Config in config.pl

# Moved out of config.pl to support $gOption in spacechk_answer
my %gOption;
# Moved from various scripts that include util.pl
my %gHelper;

my @cKernelModules = qw(vmblock vmdesched vmhgfs vmmemctl vmxnet vmci vmmon vmnet);
my $cTerminalLineSize = 79;

# Flags
my $cFlagTimestamp     =   0x1;
my $cFlagConfig        =   0x2;
my $cFlagDirectoryMark =   0x4;
my $cFlagUserModified  =   0x8;
my $cFlagFailureOK     =  0x10;

# Constants
my $gWebAccessWorkDir = '/var/log/vmware/webAccess/work';

# Needed to access $Config{...}, the Perl system configuration information.
require Config;

# Use the Perl system configuration information to make a good guess about
# the bit-itude of our platform.  If we're running on Solaris we don't have
# to guess and can just ask isainfo(1) how many bits userland is directly.
sub is64BitUserLand {
  if (vmware_product() eq 'tools-for-solaris') {
    if (direct_command(shell_string($gHelper{'isainfo'}) . ' -b') =~ /64/) {
      return 1;
    } else {
      return 0;
    }
  }
  if ($Config::Config{archname} =~ /^(x86_64|amd64)-/) {
    return 1;
  } else {
    return 0;
  }
}

# Return whether or not this is a hosted desktop product.
sub isDesktopProduct {
   return vmware_product() eq "ws" || vmware_product() eq "player";
}

# Return whether or not this is a hosted server product.
sub isServerProduct {
   return vmware_product() eq "wgs";
}

sub isToolsProduct {
   return vmware_product() =~ /tools-for-/;
}

#  Call to specify lib suffix, mainly for FreeBSD tools where multiple versions
#  of the tools are packaged up in 32bit and 64bit instances.  So rather than
#  simply lib or bin, there is lib32-6 or bin64-53, where -6 refers to FreeBSD
#  version 6.0 and 53 to FreeBSD 5.3.
sub getFreeBSDLibSuffix {
   return getFreeBSDSuffix();
}

#  Call to specify lib suffix, mainly for FreeBSD tools where multiple versions
#  of the tools are packaged up in 32bit and 64bit instances.  So rather than
#  simply lib or bin, there is lib32-6 or bin64-53, where -6 refers to FreeBSD
#  version 6.0 and 53 to FreeBSD 5.3.
sub getFreeBSDBinSuffix {
   return getFreeBSDSuffix();
}

#  Call to specify lib suffix, mainly for FreeBSD tools where multiple versions
#  of the tools are packaged up in 32bit and 64bit instances.  In the case of
#  sbin, a lib compatiblity between 5.0 and older systems appeared.  Rather
#  than sbin32, which exists normally for 5.0 and older systems, there needs
#  to be a specific sbin:  sbin32-5.  There is no 64bit set.
sub getFreeBSDSbinSuffix {
   my $suffix = '';
   my $release = `uname -r | cut -f1 -d-`;
   chomp($release);
   if (vmware_product() eq 'tools-for-freebsd' && $release == 5.0) {
      $suffix = '-5';
   } else {
      $suffix = getFreeBSDSuffix();
   }
   return $suffix;
}

sub getFreeBSDSuffix {
  my $suffix = '';

  # On FreeBSD, we ship different builds of binaries for different releases.
  #
  # For FreeBSD 6.0 and higher (which shipped new versions of libc) we use the
  # binaries located in the -6 directories.
  #
  # For releases between 5.3 and 6.0 (which were the first to ship with 64-bit
  # userland support) we use binaries from the -53 directories.
  #
  # For FreeBSD 5.0, we use binaries from the sbin32-5 directory.
  #
  # Otherwise, we just use the normal bin and sbin directories, which will
  # contain binaries predominantly built against 3.2.
  if (vmware_product() eq 'tools-for-freebsd') {
    my $release = `uname -r | cut -f1 -d-`;
    if ($release >= 6.0) {
      $suffix = '-6';
    } elsif ($release >= 5.3) {
      $suffix = '-53';
    } elsif ($release >= 5.0) {
      # sbin dir is a special case here and is handled within getFreeBSDSbinSuffix().
      $suffix = '';
    }
  }

  return $suffix;
}

# Determine what version of FreeBSD we're on and convert that to
# install package values.
sub getFreeBSDVersion {
   my $version;
   my $system_version = direct_command("uname -v");
   if ($system_version =~ /FreeBSD 3/) {
      $version = '3.2';
   } elsif ($system_version =~ /FreeBSD 4.9/) {
      $version = '4.9';
   } elsif ($system_version =~ /FreeBSD 4/) {
      $version = '4.0';
   } elsif ($system_version =~ /FreeBSD 5.[0-2]/) {
      $version = '5.0';
   } elsif ($system_version =~ /FreeBSD 5/) {
      $version = '5.3';
   } elsif ($system_version =~ /FreeBSD 6/) {
      $version = '6.0';
   } elsif ($system_version =~ /FreeBSD 7/) {
      $version = '7.0';
   }
   return "$version";
}

# Determine whether SELinux is enabled.
sub is_selinux_enabled {
   if (-x "/usr/sbin/selinuxenabled") {
      my $rv = system("/usr/sbin/selinuxenabled");
      return ($rv eq 0);
   } else {
      return 0;
   }
}

# Wordwrap system: append some content to the output
sub append_output {
  my $output = shift;
  my $pos = shift;
  my $append = shift;

  $output .= $append;
  $pos += length($append);
  if ($pos >= $cTerminalLineSize) {
    $output .= "\n";
    $pos = 0;
  }

  return ($output, $pos);
}

# Wordwrap system: deal with the next character
sub wrap_one_char {
  my $output = shift;
  my $pos = shift;
  my $word = shift;
  my $char = shift;
  my $reserved = shift;
  my $length;

  if (not (($char eq "\n") || ($char eq ' ') || ($char eq ''))) {
    $word .= $char;

    return ($output, $pos, $word);
  }

  # We found a separator.  Process the last word

  $length = length($word) + $reserved;
  if (($pos + $length) > $cTerminalLineSize) {
    # The last word doesn't fit in the end of the line. Break the line before
    # it
    $output .= "\n";
    $pos = 0;
  }
  ($output, $pos) = append_output($output, $pos, $word);
  $word = '';

  if ($char eq "\n") {
    $output .= "\n";
    $pos = 0;
  } elsif ($char eq ' ') {
    if ($pos) {
      ($output, $pos) = append_output($output, $pos, ' ');
    }
  }

  return ($output, $pos, $word);
}

# Wordwrap system: word-wrap a string plus some reserved trailing space
sub wrap {
  my $input = shift;
  my $reserved = shift;
  my $output;
  my $pos;
  my $word;
  my $i;

  if (!defined($reserved)) {
      $reserved = 0;
  }

  $output = '';
  $pos = 0;
  $word = '';
  for ($i = 0; $i < length($input); $i++) {
    ($output, $pos, $word) = wrap_one_char($output, $pos, $word,
                                           substr($input, $i, 1), 0);
  }
  # Use an artifical last '' separator to process the last word
  ($output, $pos, $word) = wrap_one_char($output, $pos, $word, '', $reserved);

  return $output;
}

# Print an error message and exit
sub error {
  my $msg = shift;

  print STDERR wrap($msg . 'Execution aborted.' . "\n\n", 0);
  exit 1;
}

# Convert a string to its equivalent shell representation
sub shell_string {
  my $single_quoted = shift;

  $single_quoted =~ s/'/'"'"'/g;
  # This comment is a fix for emacs's broken syntax-highlighting code
  return '\'' . $single_quoted . '\'';
}

# Send an arbitrary RPC command to the VMX
sub send_rpc {
  my $command = shift;
  my $rpctoolSuffix;
  my $rpctoolBinary;
  my $libDir;
  my @rpcResultLines;


  if (vmware_product() eq 'tools-for-solaris') {
     $rpctoolSuffix = is64BitUserLand() ? '/sbin/amd64' : '/sbin/i86';
  } else {
     $rpctoolSuffix = is64BitUserLand() ? '/sbin64' : '/sbin32';
  }

  $rpctoolSuffix .= getFreeBSDSbinSuffix() . '/vmware-rpctool';

  # We don't yet know if vmware-rpctool was copied into place.
  # Let's first try getting the location from the DB.
  $libDir = db_get_answer_if_exists('LIBDIR');
  if (defined($libDir)) {
    $rpctoolBinary = $libDir . $rpctoolSuffix;
    if (not (-e $rpctoolBinary)) {
      # The DB didn't help, let's pretend we're in the context
      # of an open tarball and look for vmware-rpctool.
      $rpctoolBinary = "./lib" .  $rpctoolSuffix;
      if (not (-e $rpctoolBinary)) {
	$rpctoolBinary = undef;
      }
    }
  }

  # If we found the binary, send the RPC.
  if (defined($rpctoolBinary)) {
    open (RPCRESULT, shell_string($rpctoolBinary) . " " . 
          shell_string($command) . ' 2> /dev/null |');

    @rpcResultLines = <RPCRESULT>;
    close RPCRESULT;
    return (join("\n", @rpcResultLines));
  }
}

# Create a temporary directory
#
# They are a lot of small utility programs to create temporary files in a
# secure way, but none of them is standard. So I wrote this
sub make_tmp_dir {
  my $prefix = shift;
  my $tmp;
  my $serial;
  my $loop;

  $tmp = defined($ENV{'TMPDIR'}) ? $ENV{'TMPDIR'} : '/tmp';

  # Don't overwrite existing user data
  # -> Create a directory with a name that didn't exist before
  #
  # This may never succeed (if we are racing with a malicious process), but at
  # least it is secure
  $serial = 0;
  for (;;) {
    # Check the validity of the temporary directory. We do this in the loop
    # because it can change over time
    if (not (-d $tmp)) {
      error('"' . $tmp . '" is not a directory.' . "\n\n");
    }
    if (not ((-w $tmp) && (-x $tmp))) {
      error('"' . $tmp . '" should be writable and executable.' . "\n\n");
    }

    # Be secure
    # -> Don't give write access to other users (so that they can not use this
    # directory to launch a symlink attack)
    if (mkdir($tmp . '/' . $prefix . $serial, 0755)) {
      last;
    }

    $serial++;
    if ($serial % 200 == 0) {
      print STDERR 'Warning: The "' . $tmp . '" directory may be under attack.' . "\n\n";
    }
  }

  return $tmp . '/' . $prefix . $serial;
}

# Check available space when asking the user for destination directory.
sub spacechk_answer {
  my $msg = shift;
  my $type = shift;
  my $default = shift;
  my $srcDir = shift;
  my $id = shift;
  my $answer;
  my $space = -1;

  while ($space < 0) {

    if (!defined($id)) {
      $answer = get_answer($msg, $type, $default);
    } else {
      $answer = get_persistent_answer($msg, $id, $type, $default);
    }

    # XXX check $answer for a null value which can happen with the get_answer
    # in config.pl but not with the get_answer in pkg_mgr.pl.  Moving these
    # (get_answer, get_persistent_answer) routines into util.pl eventually.
    if ($answer && ($space = check_disk_space($srcDir, $answer)) < 0) {
      my $lmsg;
      $lmsg = 'There is insufficient disk space available in ' . $answer
              . '.  Please make at least an additional ' . -$space
              . 'k available';
      if ($gOption{'default'} == 1) {
        error($lmsg . ".\n");
      }
      print wrap($lmsg . " or choose another directory.\n", 0);
    }
  }
  return $answer;
}

# Returns a list of VMware kernel modules that were found on the system that were not
# placed there by the installer.
sub non_vmware_modules_installed {
   my @installed_modules;

   if (vmware_product() eq 'wgs' || vmware_product() eq 'ws' ||
       vmware_product() eq 'tools-for-linux') {
      system(shell_string($gHelper{'depmod'}) . ' -a');
      my $kvers = direct_command(shell_string($gHelper{'uname'}) . ' -r');
      chomp($kvers);

      if (not open(MODULESDEP, "/lib/modules/$kvers/modules.dep")) {
         error("Unable to open kernel module dependency file\n.");
      }

      while (<MODULESDEP>) {
         foreach my $module (@cKernelModules) {
            if (/(.+$module.ko):$/ && ! db_file_in($1)) {
               push @installed_modules, $module;
            }
         }
      }

      close(MODULESDEP);
   }

   return @installed_modules;
}

# Append a clearly delimited block to an unstructured text file
# Result:
#  1 on success
#  -1 on failure
sub block_append {
   my $file = shift;
   my $begin = shift;
   my $block = shift;
   my $end = shift;

   if (not open(BLOCK, '>>' . $file)) {
      return -1;
   }

   print BLOCK $begin . $block . $end;

   if (not close(BLOCK)) {
      return -1;
   }

   return 1;
}


# Test if specified file contains line matching regular expression
# Result:
#  undef on failure
#  first matching line on success
sub block_match {
   my $file = shift;
   my $block = shift;
   my $line = undef;

   if (open(BLOCK, '<' . $file)) {
      while (defined($line = <BLOCK>)) {
         chomp $line;
         last if ($line =~ /$block/);
      }
      close(BLOCK);
   }
   return defined($line);
}


# Remove all clearly delimited blocks from an unstructured text file
# Result:
#  >= 0 number of blocks removed on success
#  -1 on failure
sub block_remove {
   my $src = shift;
   my $dst = shift;
   my $begin = shift;
   my $end = shift;
   my $count;
   my $state;

   if (not open(SRC, '<' . $src)) {
      return -1;
   }

   if (not open(DST, '>' . $dst)) {
      close(SRC);
      return -1;
   }

   $count = 0;
   $state = 'outside';
   while (<SRC>) {
      if      ($state eq 'outside') {
         if ($_ eq $begin) {
            $state = 'inside';
            $count++;
         } else {
            print DST $_;
         }
      } elsif ($state eq 'inside') {
         if ($_ eq $end) {
            $state = 'outside';
         }
      }
   }

   if (not close(DST)) {
      close(SRC);
      return -1;
   }

   if (not close(SRC)) {
      return -1;
   }

   return $count;
}

# Similar to block_remove().  Find the delimited text, bracketed by $begin and $end,
# and filter it out as the file is written out to a tmp file. Typicaly, block_remove()
# is used in the pattern:  create tmp dir, create tmp file, block_remove(), mv file,
# remove tmp dir. This encapsulates the pattern.
sub block_restore {
  my $src_file = shift;
  my $begin_marker = shift;
  my $end_marker = shift;
  my $tmp_dir = make_tmp_dir('block-restore');
  my $tmp_file = $tmp_dir . '/tmp_file';
  my $rv;

  $rv = block_remove($src_file, $tmp_file, $begin_marker, $end_marker);
  if ($rv >= 0) {
    system(shell_string($gHelper{'mv'}) . ' ' . $tmp_file . ' ' . $src_file);
  }
  remove_tmp_dir($tmp_dir);

  return $rv;
}

# match the output of 'uname -s' to the product. These are compared without
# case sensitivity.
sub DoesOSMatchProduct {

 my %osProductHash = (
    'tools-for-linux'   => 'linux',
    'tools-for-solaris' => 'sunos',
    'tools-for-freebsd' => 'freebsd'
 );

 my $OS = `uname -s`;
 chomp($OS);

 return ($osProductHash{vmware_product()} =~ m/$OS/i) ? 1 : 0;

}

# Remove leading and trailing whitespaces
sub remove_whitespaces {
  my $string = shift;

  $string =~ s/^\s*//;
  $string =~ s/\s*$//;
  return $string;
}

# Ask a question to the user and propose an optional default value
# Use this when you don't care about the validity of the answer
sub query {
    my $message = shift;
    my $defaultreply = shift;
    my $reserved = shift;
    my $reply;
    my $default_value = $defaultreply eq '' ? '' : ' [' . $defaultreply . ']';
    my $terse = 'no';

    # Allow the script to limit output in terse mode.  Usually dictated by
    # vix in a nested install and the '--default' option.
    if (db_get_answer_if_exists('TERSE')) {
      $terse = db_get_answer('TERSE');
      if ($terse eq 'yes') {
        $reply = remove_whitespaces($defaultreply);
        return $reply;
      }
    }

    # Reserve some room for the reply
    print wrap($message . $default_value, 1 + $reserved);

    # This is what the 1 is for
    print ' ';

    if ($gOption{'default'} == 1) {
      # Simulate the enter key
      print "\n";
      $reply = '';
    } else {
      $reply = <STDIN>;
      $reply = '' unless defined($reply);
      chomp($reply);
    }

    print "\n";
    $reply = remove_whitespaces($reply);
    if ($reply eq '') {
      $reply = $defaultreply;
    }
    return $reply;
}

# Execute the command passed as an argument
# _without_ interpolating variables (Perl does it by default)
sub direct_command {
  return `$_[0]`;
}

# If there is a pid for this process, consider it running.
sub check_is_running {
  my $proc_name = shift;
  my $rv = system(shell_string($gHelper{'pidof'}) . " " . $proc_name . " > /dev/null");
  return $rv eq 0;
}

# OS-independent method of loading a kernel module by module name
# Returns true (non-zero) if the operation succeeded, false otherwise.
sub kmod_load_by_name {
    my $modname = shift; # IN: Module name
    my $doSilent = shift; # IN: Flag to indicate whether loading should be done silently

    my $silencer = '';
    if (defined($doSilent) && $doSilent) {
	$silencer = ' >/dev/null 2>&1';
    }

    if (defined($gHelper{'modprobe'})) { # Linux
	return !system(shell_string($gHelper{'modprobe'}) . ' ' . shell_string($modname)
		       . $silencer);
    } elsif (defined($gHelper{'kldload'})) { # FreeBSD
	return !system(shell_string($gHelper{'kldload'}) . ' ' . shell_string($modname)
		       . $silencer);
    } elsif (defined($gHelper{'modload'})) { # Solaris
	return !system(shell_string($gHelper{'modload'}) . ' ' . shell_string($modname)
		       . $silencer);
    }

    return 0; # Failure
}

# OS-independent method of loading a kernel module by object path
# Returns true (non-zero) if the operation succeeded, false otherwise.
sub kmod_load_by_path {
    my $modpath = shift; # IN: Path to module object file
    my $doSilent = shift; # IN: Flag to indicate whether loading should be done silently
    my $doForce = shift; # IN: Flag to indicate whether loading should be forced
    my $probe = shift; # IN: 1 if to probe only, 0 if to actually load

    my $silencer = '';
    if (defined($doSilent) && $doSilent) {
	$silencer = ' >/dev/null 2>&1';
    }

    if (defined($gHelper{'insmod'})) { # Linux
	return !system(shell_string($gHelper{'insmod'}) . ($probe ? ' -p ' : ' ')
		       . ((defined($doForce) && $doForce) ? ' -f ' : ' ')
		       . shell_string($modpath)
		       . $silencer);
    } elsif (defined($gHelper{'kldload'})) { # FreeBSD
	return !system(shell_string($gHelper{'kldload'}) . ' ' . shell_string($modpath)
		       . $silencer);
    } elsif (defined($gHelper{'modload'})) { # Solaris
	return !system(shell_string($gHelper{'modload'}) . ' ' . shell_string($modpath)
		       . $silencer);
    }

    return 0; # Failure
}

# OS-independent method of unloading a kernel module by name
# Returns true (non-zero) if the operation succeeded, false otherwise.
sub kmod_unload {
    my $modname = shift;     # IN: Module name
    my $doRecursive = shift; # IN: Whether to also try loading modules that
                             # become unused as a result of unloading $modname

    if (defined($gHelper{'modprobe'})
	&& defined($doRecursive) && $doRecursive) { # Linux (with $doRecursive)
	return !system(shell_string($gHelper{'modprobe'}) . ' -r ' . shell_string($modname)
		       . ' >/dev/null 2>&1');
    } elsif (defined($gHelper{'rmmod'})) { # Linux (otherwise)
	return !system(shell_string($gHelper{'rmmod'}) . ' ' . shell_string($modname)
		       . ' >/dev/null 2>&1');
    } elsif (defined($gHelper{'kldunload'})) { # FreeBSD
	return !system(shell_string($gHelper{'kldunload'}) . ' ' . shell_string($modname)
		       . ' >/dev/null 2>&1');
    } elsif (defined($gHelper{'modunload'})) { # Solaris
	# Solaris won't let us unload by module name, so we have to find the ID from modinfo
	my $aline;
	my @lines = split('\n', direct_command(shell_string($gHelper{'modinfo'})));

	foreach $aline (@lines) {
	    chomp($aline);
	    my($amodid, $dummy2, $dummy3, $dummy4, $dummy5, $amodname) = split(/\s+/, $aline);

	    if ($modname eq $amodname) {
		return !system(shell_string($gHelper{'modunload'}) . ' -i ' . $amodid
			       . ' >/dev/null 2>&1');
	    }
	}

	return 0; # Failure - module not found
    }

    return 0; # Failure
}

#
# check to see if the certificate files exist (and
# that they appear to be a valid certificate).
#
sub certificateExists {
  my $certLoc = shift;
  my $openssl_exe = shift;
  my $certPrefix = shift;
  my $ld_lib_path = $ENV{'LD_LIBRARY_PATH'};
  my $libdir = db_get_answer('LIBDIR') . '/lib';
  $ld_lib_path .= ';' . $libdir . '/libssl.so.0.9.8;' . $libdir . '/libcrypto.so.0.9.8';

  my $ld_lib_string = "LD_LIBRARY_PATH='" . $ld_lib_path . "'";
  return ((-e "$certLoc/$certPrefix.crt") &&
          (-e "$certLoc/$certPrefix.key") &&
          !(system($ld_lib_string . " " . shell_string("$openssl_exe") . ' x509 -in '
                   . shell_string("$certLoc") . "/$certPrefix.crt " .
                   ' -noout -subject > /dev/null 2>&1')));
}

# Helper function to create SSL certificates
sub createSSLCertificates {
  my ($openssl_exe, $vmware_version, $certLoc, $certPrefix, $unitName) = @_;
  my $certUniqIdent = "(564d7761726520496e632e)";
  my $certCnf;
  my $curTime = time();
  my $hostname = direct_command(shell_string($gHelper{"hostname"}));

  my $cTmpDirPrefix = "vmware-ssl-config";
  my $tmpdir;

  if (certificateExists($certLoc, $openssl_exe, $certPrefix)) {
    print wrap("Using Existing SSL Certificate.\n", 0);
    return;
  }

  # Create the directory
  if (! -e $certLoc) {
    create_dir($certLoc, $cFlagDirectoryMark);
  }

  $tmpdir = make_tmp_dir($cTmpDirPrefix);
  $certCnf = "$tmpdir/certificate.cnf";
  if (not open(CONF, '>' . $certCnf)) {
    error("Unable to open $certCnf to create SSL certificate.")
  }
  print CONF <<EOF;
  # Conf file that we will use to generate SSL certificates.
RANDFILE                = $tmpdir/seed.rnd
[ req ]
default_bits		= 1024
default_keyfile 	= $certPrefix.key
distinguished_name	= req_distinguished_name

#Don't encrypt the key
encrypt_key             = no
prompt                  = no

string_mask = nombstr

[ req_distinguished_name ]
countryName= US
stateOrProvinceName     = California
localityName            = Palo Alto
0.organizationName      = VMware, Inc.
organizationalUnitName  = $unitName
commonName              = $hostname
unstructuredName        = ($curTime),$certUniqIdent
emailAddress            = ssl-certificates\@vmware.com
EOF
  close(CONF);

  my $ld_lib_path = $ENV{'LD_LIBRARY_PATH'};
  my $libdir = db_get_answer('LIBDIR') . '/lib';
  $ld_lib_path .= ';' . $libdir . '/libssl.so.0.9.8;' . $libdir . '/libcrypto.so.0.9.8';
  my $ld_lib_string = "LD_LIBRARY_PATH='" . $ld_lib_path . "'";
 
  system($ld_lib_string . " " . shell_string("$openssl_exe") . ' req -new -x509 -keyout '
         . shell_string("$certLoc") . '/' . shell_string("$certPrefix")
         . '.key -out ' . shell_string("$certLoc") . '/'
         . shell_string("$certPrefix") . '.crt -config '
         . shell_string("$certCnf") . ' -days 5000 > /dev/null 2>&1');
  db_add_file("$certLoc/$certPrefix.key", $cFlagTimestamp);
  db_add_file("$certLoc/$certPrefix.crt", $cFlagTimestamp);

  remove_tmp_dir($tmpdir);
  # Make key readable only by root (important)
  safe_chmod(0400, "$certLoc" . '/' . "$certPrefix" . '.key');

  # Let anyone read the certificate
  safe_chmod(0444, "$certLoc" . '/' . "$certPrefix" . '.crt');
}

# Install a pair of S/K startup scripts for a given runlevel
sub link_runlevel {
   my $level = shift;
   my $service = shift;
   my $S_level = shift;
   my $K_level = shift;

   #
   # Create the S symlink
   #
   install_symlink(db_get_answer('INITSCRIPTSDIR') . '/' . $service,
                   db_get_answer('INITDIR') . '/rc' . $level . '.d/S'
                   . $S_level . $service);

   #
   # Create the K symlink
   #
   install_symlink(db_get_answer('INITSCRIPTSDIR') . '/' . $service,
                   db_get_answer('INITDIR') . '/rc' . $level . '.d/K'
                   . $K_level . $service);
}

# Create the links for VMware's services taking the service name and the
# requested levels
sub link_services {
   my @fields;
   my $service = shift;
   my $S_level = shift;
   my $K_level = shift;

   # Create the links the LSB way when possible
   if ($gHelper{'insserv'} ne '') {
      if (0 == system(shell_string($gHelper{'insserv'}) . ' '
                 . shell_string(db_get_answer('INITSCRIPTSDIR')
                                . '/' . $service))) {
         return;
      }
   }

   # Now try using chkconfig if available.
   # Note: RedHat's chkconfig reads LSB INIT INFO if present.
   if ($gHelper{'chkconfig'} ne '') {
      if (0 == system(shell_string($gHelper{'chkconfig'}) . ' '
                      . $service . ' reset')) {
         return;
      }
   }

   # Set up vmware to start/stop at run levels 2, 3 and 5
   link_runlevel(2, $service, $S_level, $K_level);
   link_runlevel(3, $service, $S_level, $K_level);
   link_runlevel(5, $service, $S_level, $K_level);

   # Set up vmware to stop at run levels 0 and 6
   install_symlink(db_get_answer('INITSCRIPTSDIR') . '/' . $service,
                   db_get_answer('INITDIR') . '/rc0' . '.d/K'
                   . $K_level . $service);
   install_symlink(db_get_answer('INITSCRIPTSDIR') . '/' . $service,
                   db_get_answer('INITDIR') . '/rc6' . '.d/K'
                   . $K_level . $service);
}

# Emulate a simplified ls program for directories
sub internal_ls {
  my $dir = shift;
  my @fn;

  opendir(LS, $dir) or return ();
  @fn = grep(!/^\.\.?$/, readdir(LS));
  closedir(LS);

  return @fn;
}


#
# unconfigure_autostart_legacy --
#
#      Remove VMware-added blocks relating to vmware-user autostart from
#      pre-XDG resource files, scripts, etc.
#
# Results:
#      OpenSuSE:        Revert xinitrc.common.
#      Debian/Ubuntu:   Remove script from Xsession.d.
#      xdm:             Revert xdm-config(s).
#      gdm:             None.  (gdm mechanism used install_symlink, so that will be
#                       cleaned up separately.)
#
# Side effects:
#      None.
#

sub unconfigure_autostart_legacy {
   my $markerBegin = shift;     # IN: block begin marker
   my $markerEnd = shift;       # IN: block end marker

   if (!defined($markerBegin) || !defined($markerEnd)) {
      return;
   }

   my $chompedMarkerBegin = $markerBegin; # block_match requires chomped markers
   chomp($chompedMarkerBegin);

   #
   # OpenSuSE (xinitrc.common)
   #
   my $xinitrcCommon = '/etc/X11/xinit/xinitrc.common';
   if (-f $xinitrcCommon && block_match($xinitrcCommon, $chompedMarkerBegin)) {
      block_restore($xinitrcCommon, $markerBegin, $markerEnd);
   }

   #
   # Debian (Xsession.d) - We forgot to simply call db_add_file() after
   # creating this one.
   #
   my $dotdScript = '/etc/X11/Xsession.d/99-vmware_vmware-user';
   if (-f $dotdScript && !db_file_in($dotdScript)) {
      unlink($dotdScript);
   }

   #
   # xdm
   #
   my @xdmcfgs = ("/etc/X11/xdm/xdm-config");
   my $x11Base = db_get_answer_if_exists('X11DIR');
   if (defined($x11Base)) {
      push(@xdmcfgs, "$x11Base/lib/X11/xdm/xdm-config");
   }
   foreach (@xdmcfgs) {
      if (-f $_ && block_match($_, "!$chompedMarkerBegin")) {
         block_restore($_, "!$markerBegin", "!$markerEnd");
      }
   }
}
# END_OF_UTIL_DOT_PL


# Constants
my $cKernelModuleDir = '/lib/modules';
my $cTmpDirPrefix = 'vmware-config';
my $cConnectSocketDir = '/var/run/vmware';
my $cVixProductName = ' VMware VIX API';
my $machine = 'host';
my $os = 'host';
if (vmware_product() eq 'server') {
  $machine = 'machine';
  $os = "Console OS";
}
my $cServices = '/etc/services';
my $cMarkerBegin = "# Beginning of the block added by the VMware software\n";
my $cMarkerEnd = "# End of the block added by the VMware software\n";

my $cNetmapConf = '/etc/vmware/netmap.conf';

my $cConfiguratorFileName = 'vmware-config.pl';
my $cNICAlias = 'vmnics';

if (vmware_product() eq 'tools-for-linux' ||
    vmware_product() eq 'tools-for-freebsd' ||
    vmware_product() eq 'tools-for-solaris') {
  $cConfiguratorFileName = 'vmware-config-tools.pl';
}

my $cModulesBuildEnv;
if (vmware_product() eq 'tools-for-solaris') {
  $cModulesBuildEnv = ' please upgrade to a newer Solaris release.';
} else {
  $cModulesBuildEnv = ' you can install the driver by running '
                      . $cConfiguratorFileName
                      . ' again after making sure that gcc, binutils, make '
                      . 'and the kernel sources for your running kernel are '
                      . 'installed on your machine. These packages are '
                      . 'available on your distribution\'s installation CD.';
}

# kernels to avoid when rmmod'ing pcnet32
my %cPCnet32KernelBlacklist = (
                          '2.4.2'  => 'yes',
                          '2.4.9'  => 'yes',
                          '2.6.0'  => '-test',
                          '2.6.0'  => '-test5_2',
                          '2.6.16' => '.13-4-default',
                          '2.6.8'  => '-1',
                          );

my $cDirExists = '1';
my $cCreateDirSuccess = '0';
my $cCreateDirFailure = '-1';

#
# Global variables
#
my $gRegistryDir;
my $gStateDir;
my $gInstallerMainDB;
my $gConfFlag;
my %gSystem;
my $gLogDir;
my $gManifest;
my @gManifestNames;
my @gManifestVersions;
my @gManifestInstFlags;
# List of all ethernet adapters on the system
my @gAllEthIf;
# List of ethernet adapters that have not been bridged
my @gAvailEthIf;
# By convention, vmnet1 is the virtual ethernet interface connected to the
# private virtual network that Samba uses.  We are also reserving vmnet0
# for bridged networks.  These are reserved vmnets.
my $gDefBridged = '0';
my $gDefHostOnly = '1';
my $gDefNat = '8';
# Reserved vmnets
my @gReservedVmnet = ($gDefBridged, $gDefHostOnly, $gDefNat);
# Constant defined as the smallest vmnet that is allowed
my $gMinVmnet = '0';
# Constant defined as the largest vmnet that is allowed
# Although 256 are supported, #255 is reserved
# Note: Max length of interface name is 8
my $gMaxVmnet = '254';
# Constant defines as the number of vmnets to be pre-created
my $gNumVmnet = 10;
my $gFirstModuleBuild;
my $gDefaultAuthdPort = 902;
my @gDefaultHttpProxy = (80, 8222);
my @gDefaultHttpSProxy = (443, 8333);
# BEGINNING OF THE SECOND LIBRARY FUNCTIONS
# Global variables
my %gDBAnswer;
my %gDBFile;
my %gDBDir;
my %gDBUserFile;
my $cBackupExtension = '.BeforeVMwareToolsInstall';
my $cRestorePrefix = 'RESTORE_';
my $cRestoreBackupSuffix = '_BAK';
my $cRestoreBackList = 'RESTORE_BACK_LIST';
my $cSwitchedToHost = 'SWITCHED_TO_HOST';
my $cXModulesDir = '/usr/X11R6/lib/modules';
my $cX64ModulesDir = '/usr/X11R6/lib64/modules';
my $gXMouseDriverFile = '';
my $gXVideoDriverFile = '';
my $gIs64BitX = 0;
my $gSavedPath = $ENV{'PATH'};
my $gNoXDrivers = 0;

# Load the installer database
sub db_load {
  undef %gDBAnswer;
  undef %gDBFile;
  undef %gDBDir;

  if (not open(INSTALLDB, '<' . $gInstallerMainDB)) {
    error('Unable to open the installer database ' . $gInstallerMainDB
          . ' in read-mode.' . "\n\n");
  }
  while (<INSTALLDB>) {
    chomp;
    if (/^answer (\S+) (.+)$/) {
      $gDBAnswer{$1} = $2;
    } elsif (/^answer (\S+)/) {
      $gDBAnswer{$1} = '';
    } elsif (/^remove_answer (\S+)/) {
      delete $gDBAnswer{$1};
    } elsif (/^file (.+) (\d+)$/) {
      $gDBFile{$1} = $2;
    } elsif (/^file (.+)$/) {
      $gDBFile{$1} = 0;
    } elsif (/^modified (.+)$/) {
      if (defined $gDBFile{$1}) {
         $gDBUserFile{$1} = 0;
      }
    } elsif (/^remove_file (.+)$/) {
      delete $gDBFile{$1};
      delete $gDBUserFile{$1}; # harmless if not in there
    } elsif (/^directory (.+)$/) {
      $gDBDir{$1} = '';
    } elsif (/^remove_directory (.+)$/) {
      delete $gDBDir{$1};
    }
  }
  close(INSTALLDB);
}

# Open the database on disk in append mode
sub db_append {
  if (not open(INSTALLDB, '>>' . $gInstallerMainDB)) {
    error('Unable to open the installer database ' . $gInstallerMainDB
          . ' in append-mode.' . "\n\n");
  }
  # Force a flush after every write operation.
  # See 'Programming Perl' 3rd edition, p. 781 (p. 110 in an older edition)
  select((select(INSTALLDB), $| = 1)[0]);
}

# Add a file to the tar installer database
# flags:
#  0x1 - write time stamp ($cFlagTimestamp)
#  0x2 - is config file ($cFlagConfig)
#  0x8 - is user-modified file ($cFlagUserModified)
sub db_add_file {
  my $file = shift;
  my $flags = shift;

  if ($flags & $cFlagTimestamp) {
    my @statbuf;

    @statbuf = stat($file);
    if (not (defined($statbuf[9]))) {
      error('Unable to get the last modification timestamp of the destination '
            . 'file ' . $file . '.' . "\n\n");
    }

    $gDBFile{$file} = $statbuf[9];
    print INSTALLDB 'file ' . $file . ' ' . $statbuf[9] . "\n";
  } else {
    $gDBFile{$file} = 0;
    print INSTALLDB 'file ' . $file . "\n";
  }

  if ($flags & $cFlagUserModified) {
    print INSTALLDB 'modified ' . $file . "\n";
    $gDBUserFile{$file} = 0;
  }

  if ($flags & $cFlagConfig) {
    print INSTALLDB 'config ' . $file . "\n";
  }
}

# Mark a file as modified without changing it.
sub db_set_userfile {
   my $file = shift;

   if (!db_userfile_in($file)) {
      print INSTALLDB 'modified ' . $file . "\n";
      $gDBUserFile{$file} = 0;
   }
}

# Remove a file from the tar installer database
sub db_remove_file {
  my $file = shift;

  print INSTALLDB 'remove_file ' . $file . "\n";
  delete $gDBFile{$file};
  delete $gDBUserFile{$file};
}

# Remove a directory from the tar installer database
sub db_remove_dir {
  my $dir = shift;

  print INSTALLDB 'remove_directory ' . $dir . "\n";
  delete $gDBDir{$dir};
}

# Determine if a file belongs to the tar installer database
sub db_file_in {
  my $file = shift;

  return defined($gDBFile{$file});
}

# Determine if a directory belongs to the tar installer database
sub db_dir_in {
  my $dir = shift;

  return defined($gDBDir{$dir});
}

# Determine if a directory belongs to the tar installer database
sub db_userfile_in {
  my $file = shift;

  return defined($gDBUserFile{$file});
}

# Return the timestamp of an installed file
sub db_file_ts {
  my $file = shift;

  return $gDBFile{$file};
}

# Remove the timestamp data for a file.
sub db_remove_ts {
  my $file = shift;
  db_remove_file($file);
  db_add_file($file, 0);
}

# Reset the timestamp data for a file.
sub db_update_ts {
  my $file = shift;
  db_remove_file($file);
  db_add_file($file, 1);
}

# Add a directory to the tar installer database
sub db_add_dir {
  my $dir = shift;

  $gDBDir{$dir} = '';
  print INSTALLDB 'directory ' . $dir . "\n";
}

# Remove an answer from the tar installer database
sub db_remove_answer {
  my $id = shift;

  if (defined($gDBAnswer{$id})) {
    print INSTALLDB 'remove_answer ' . $id . "\n";
    delete $gDBAnswer{$id};
  }
}

# Add an answer to the tar installer database
sub db_add_answer {
  my $id = shift;
  my $value = shift;

  db_remove_answer($id);
  $gDBAnswer{$id} = $value;
  print INSTALLDB 'answer ' . $id . ' ' . $value . "\n";
}

# Retrieve an answer that must be present in the database
sub db_get_answer {
  my $id = shift;

  if (not defined($gDBAnswer{$id})) {
    error('Unable to find the answer ' . $id . ' in the installer database ('
          . $gInstallerMainDB . ').  You may want to re-install '
          . vmware_product_name() . ".\n\n");
  }

  return $gDBAnswer{$id};
}


# Retrieves an answer if it exists in the database, else returns undef;
sub db_get_answer_if_exists {
  my $id = shift;
  if (not defined($gDBAnswer{$id})) {
    return undef;
  }
  if ($gDBAnswer{$id} eq '') {
    return undef;
  }
  return $gDBAnswer{$id};
}

# Save the tar installer database
sub db_save {
  close(INSTALLDB);
}
# END OF THE SECOND LIBRARY FUNCTIONS

# BEGINNING OF THE LIBRARY FUNCTIONS
# Global variables
my %gAnswerSize;
my %gCheckAnswerFct;

# Tell if the user is the super user
sub is_root {
  return $> == 0;
}

# Contrary to a popular belief, 'which' is not always a shell builtin command.
# So we cannot trust it to determine the location of other binaries.
# Moreover, SuSE 6.1's 'which' is unable to handle program names beginning with
# a '/'...
#
# Return value is the complete path if found, or '' if not found
sub internal_which {
  my $bin = shift;
  my $useSavedPath = shift;     # (optional) Define this if you'd like to look
                                # around using the user's original PATH.

  if (substr($bin, 0, 1) eq '/') {
    # Absolute name
    if ((-f $bin) && (-x $bin)) {
      return $bin;
    }
  } else {
    # Relative name
    my @paths;
    my $path;

    if (index($bin, '/') == -1) {
      # There is no other '/' in the name
      @paths = split(':', $useSavedPath ? $gSavedPath : $ENV{'PATH'});
      foreach $path (@paths) {
         my $fullbin;

         $fullbin = $path . '/' . $bin;
         if ((-f $fullbin) && (-x $fullbin)) {
               return $fullbin;
         }
      }
    }
  }

  return '';
}

# Check the validity of an answer whose type is yesno
# Return a clean answer if valid, or ''
sub check_answer_binpath {
  my $answer = shift;
  my $source = shift;

  my $fullpath = internal_which($answer);
  if (not ("$fullpath" eq '')) {
    return $fullpath;
  }

  if ($source eq 'user') {
    print wrap('The answer "' . $answer . '" is invalid.  It must be the '
               . 'complete name of a binary file.' . "\n\n", 0);
  }
  return '';
}
$gAnswerSize{'binpath'} = 20;
$gCheckAnswerFct{'binpath'} = \&check_answer_binpath;

# Prompts the user if a binary is not found
# Return value is:
#  '': the binary has not been found
#  the binary name if it has been found
sub DoesBinaryExist_Prompt {
  my $bin = shift;
  my $answer;
  my $prefix = 'BIN_';

  $answer = check_answer_binpath($bin, 'default');
  if ($answer ne '') {
    return $answer;
  } else {
    if (defined db_get_answer_if_exists($prefix . $bin)) {
      return db_get_answer($prefix . $bin);
    }
  }

  if (get_answer('Setup is unable to find the "' . $bin . '" program on your '
                 . 'machine.  Please make sure it is installed.  Do you want '
                 . 'to specify the location of this program by hand?', 'yesno',
                 'yes') eq 'no') {
    return '';
  }

  $answer = get_answer('What is the location of the "' . $bin . '" program on '
		       . 'your machine?', 'binpath', '');
  if ($answer ne '' &&
      not defined db_get_answer_if_exists($prefix . $bin)) {
    db_add_answer($prefix . $bin, $answer);
  }
  return $answer;
}

# chmod() that reports errors
sub safe_chmod {
  my $mode = shift;
  my $file = shift;

  if (chmod($mode, $file) != 1) {
    error('Unable to change the access rights of the file ' . $file . '.'
          . "\n\n");
  }
}

# chown() that reports errors.
sub safe_chown {
  my $uid = shift;
  my $gid = shift;
  my $file = shift;

  if (chown($uid, $gid, $file) != 1) {
    error('Unable to change the owner of the file ' . $file . '.'
          . "\n\n");
  }
}

# Install a file permission
sub install_permission {
  my $src = shift;
  my $dst = shift;
  my @statbuf;

  @statbuf = stat($src);
  if (not (defined($statbuf[2]))) {
    error('Unable to get the access rights of source file "' . $src . '".'
          . "\n\n");
  }
  safe_chmod($statbuf[2] & 07777, $dst);
}

# Emulate a simplified sed program
# Return 1 if success, 0 if failure
# XXX as a side effect, if the string being replaced is '', remove
# the entire line.  Remove this, once we have better "block handling" of
# our config data in config files.
sub internal_sed {
  my $src = shift;
  my $dst = shift;
  my $append = shift;
  my $patchRef = shift;
  my @patchKeys;

  if (not open(SRC, '<' . $src)) {
    return 0;
  }
  if (not open(DST, (($append == 1) ? '>>' : '>') . $dst)) {
    return 0;
  }

  @patchKeys = keys(%$patchRef);
  if ($#patchKeys == -1) {
    while (defined($_ = <SRC>)) {
      print DST $_;
    }
  } else {
    while (defined($_ = <SRC>)) {
      my $patchKey;
      my $del = 0;

      foreach $patchKey (@patchKeys) {
        if (s/$patchKey/$$patchRef{$patchKey}/g) {
          if ($_ eq "\n") {
            $del = 1;
          }
        }
      }
      if ($del) {
        next;
      }
      print DST $_;
    }
  }

  close(SRC);
  close(DST);
  return 1;
}

# Check if a file name exists
sub file_name_exist {
  my $file = shift;

  # Note: We must test for -l before, because if an existing symlink points to
  #       a non-existing file, -e will be false
  return ((-l $file) || (-e $file))
}

# Check if a file name already exists and prompt the user
# Return 0 if the file can be written safely, 1 otherwise
sub file_check_exist {
  my $file = shift;

  if (not file_name_exist($file)) {
    return 0;
  }

  # The default must make sure that the product will be correctly installed
  # We give the user the choice so that a sysadmin can perform a normal
  # install on a NFS server and then answer 'no' NFS clients
  return (get_answer('The file ' . $file . ' that this program was about to '
                     . 'install already exists.  Overwrite?', 'yesno', 'yes')
            eq 'yes') ? 0 : 1;
}

# Returns 1 if a file has changed with respect to its timestamp in the database,
# 0 otherwise
sub file_changed_db_ts {
   my $file = shift;
   my @statbuf;

   # This doesn't matter if (a) file doesn't exist, (b) doesn't have a
   # timestamp anyway or (c) the timestamp is zero. Usually (b) and (c)
   # are equivalent but the use is undefined.
   if (!file_name_exist($file)) {
      return 0;
   }

   if (!defined(db_file_ts($file)) || db_file_ts($file) == 0) {
      return 0;
   }

   @statbuf = stat($file);
   if (defined($statbuf[9])) {
      return (db_file_ts($file) != $statbuf[9]);
   } else {
      error('Unable to get the last modification timestamp of the destination '
            . 'file ' . $file . '.' . "\n\n");
      return 0;
   }
}

# Install one file
# flags are forwarded to db_add_file()
sub install_file {
  my $src = shift;
  my $dst = shift;
  my $patchRef = shift;
  my $flags = shift;

  # If we are installing a config file and such a config file already exists
  # AND it has changed timestamp with regards to the DB,
  # OR
  # it's marked as a user-modified config file...
  if (($flags & $cFlagConfig) && file_name_exist($dst)) {
      if (db_userfile_in($dst)) {
         # Note the default choice. We should not require users to pass
         # a command line option to preserve userfiles. That should be the
         # default.
         my $default = ($gOption{'overwrite'} ? 'no' : 'yes');
         my $rv = get_answer('You have previously modified the configuration '
                           . 'file ' . $dst . ' and chosen to keep your '
                           . 'version.  Would you still like to keep it '
                           . 'instead of having this program create a new '
                           . 'version?', 'yesno', $default);

         if ($rv eq 'yes') {
            return;
         }
      } elsif (file_changed_db_ts($dst)) {
         # When this branch is reached, we default to clobbering unless
         # --preserve is used.
         my $default = ($gOption{'preserve'} ? 'yes' : 'no');
         my $rv = get_answer('The configuration file ' . $dst . ' already '
          . 'exists and has been modified (possibly by you) since the '
          . 'last install. Would you like to keep your version of the '
          . 'file instead of installing a new one?', 'yesno', $default);

         if ($rv eq 'yes') {
            db_remove_file($dst);
            db_set_userfile($dst);
            print wrap("Note that you may need to change this configuration "
                  . "file yourself. For example, if you reconfigure your "
                  . "networking settings using this script, and choose to keep "
                  . "your version of a configuration file, you may need to "
                  . "update it to reflect the new layout of the network."
                  . "\n\n", 0);

            return;
         }
      }
  }


  # Well, if that's not true, just clobber it, whatever it is or was.
  # Doing this will also undo its userfile status.
  uninstall_file($dst);
  if (file_check_exist($dst)) {
    return;
  }
  # The file could be a symlink to another location.  Remove it
  unlink($dst);
  if (not internal_sed($src, $dst, 0, $patchRef)) {
    error('Unable to copy the source file ' . $src . ' to the destination '
          . 'file ' . $dst . '.' . "\n\n");
  }
  db_add_file($dst, $flags);
  install_permission($src, $dst);
}

# mkdir() that reports errors
sub safe_mkdir {
  my $file = shift;

  if (-d $file) {
    return 1;
  }

  if (mkdir($file, 0777) == 0) {
    error('Unable to create the directory ' . $file . '.' . "\n\n");
  }
  return 1;
}

# Remove trailing slashes in a dir path
sub dir_remove_trailing_slashes {
  my $path = shift;

  for (;;) {
    my $len;
    my $pos;

    $len = length($path);
    if ($len < 2) {
      # Could be '/' or any other character.  Ok.
      return $path;
    }

    $pos = rindex($path, '/');
    if ($pos != $len - 1) {
      # No trailing slash
      return $path;
    }

    # Remove the trailing slash
    $path = substr($path, 0, $len - 1)
  }
}

# Emulate a simplified basename program
sub internal_basename {
  return substr($_[0], rindex($_[0], '/') + 1);
}

# Emulate a simplified dirname program
sub internal_dirname {
  my $path = shift;
  my $pos;

  $path = dir_remove_trailing_slashes($path);

  $pos = rindex($path, '/');
  if ($pos == -1) {
    # No slash
    return '.';
  }

  if ($pos == 0) {
    # The only slash is at the beginning
    return '/';
  }

  return substr($path, 0, $pos);
}

# Create a hierarchy of directories with permission 0755
# flags:
#  0x4 - write this directory creation in the installer database
#        ($cFlagDirectoryMark)
# Return 1 if the directory existed before
sub create_dir {
  my $dir = shift;
  my $parentDir = internal_dirname($dir);
  my $flags = shift;

  if (-d $dir) {
    return $cDirExists;
  }

  if (index($dir, '/') != -1) {
    create_dir($parentDir, $flags);
  }

  if ($flags & $cFlagFailureOK) {
    if (mkdir($dir, 0777) == 0) {
      return $cCreateDirFailure;
    }
  } else {
    safe_mkdir($dir, $flags);
  }

  if ($flags & $cFlagDirectoryMark) {
    db_add_dir($dir);
  }
  return $cCreateDirSuccess;
}

# Get a valid non-persistent answer to a question
# Use this when the answer shouldn't be stored in the database
sub get_answer {
  my $msg = shift;
  my $type = shift;
  my $default = shift;
  my $answer;

  if (not defined($gAnswerSize{$type})) {
    die 'get_answer(): type ' . $type . ' not implemented :(' . "\n\n";
  }
  for (;;) {
    $answer = check_answer(query($msg, $default, $gAnswerSize{$type}), $type,
                           'user');
    if ($answer ne '') {
      return $answer;
    }

    # Let the error propagate to callers
    if ($gOption{'default'} == 1) {
      return '';
    }
  }
}

# Get a valid persistent answer to a question
# Use this when you want an answer to be stored in the database
sub get_persistent_answer {
  my $msg = shift;
  my $id = shift;
  my $type = shift;
  my $default = shift;
  my $answer;

  if (defined($gDBAnswer{$id})) {
    # There is a previous answer in the database
    $answer = check_answer($gDBAnswer{$id}, $type, 'db');
    if ($answer ne '') {
      # The previous answer is valid.  Make it the default value
      $default = $answer;
    }
  }

  $answer = get_answer($msg, $type, $default);
  db_add_answer($id, $answer);
  return $answer;
}

# Find a suitable backup name and backup a file
sub backup_file {
  my $file = shift;
  my $i;

  for ($i = 0; $i < 100; $i++) {
    if (not file_name_exist($file . '.old.' . $i)) {
      my %patch;

      undef %patch;
      if (internal_sed($file, $file . '.old.' . $i, 0, \%patch)) {
         print wrap('File ' . $file . ' is backed up to ' . $file . '.old.'
                    . $i . '.' . "\n\n", 0);
      } else {
         print STDERR wrap('Unable to backup the file ' . $file . ' to '
                           . $file . '.old.' . $i .'.' . "\n\n", 0);
      }
      return;
    }
  }

  print STDERR wrap('Unable to backup the file ' . $file . '.  You have too '
                    . 'many backups files.  They are files of the form '
                    . $file . '.old.N, where N is a number.  Please delete '
                    . 'some of them.' . "\n\n", 0);
}

# Backup a file in the idea to restore it in the future.
sub backup_file_to_restore {
  my $file = shift;
  my $restoreStr = shift;
  if (file_name_exist($file) &&
      (not file_name_exist($file . $cBackupExtension))) {
    my %p;
    undef %p;
    rename $file, $file . $cBackupExtension;
    db_add_answer($cRestorePrefix . $restoreStr, $file);
    db_add_answer($cRestorePrefix . $restoreStr . $cRestoreBackupSuffix,
                  $file . $cBackupExtension);

    if (defined db_get_answer_if_exists($cRestoreBackList)) {
      my $allRestoreStr;
      $allRestoreStr = db_get_answer($cRestoreBackList);
      db_add_answer($cRestoreBackList,$allRestoreStr . ':' . $restoreStr);
    } else {
      db_add_answer($cRestoreBackList, $restoreStr);
    }
  }
}

# XXX Duplicated in pkg_mgr.pl
# format of the returned hash:
#          - key is the system file
#          - value is the backed up file.
# This function should never know about filenames. Only database
# operations.
sub db_get_files_to_restore {
  my %fileToRestore;
  undef %fileToRestore;

  if (defined db_get_answer_if_exists($cRestoreBackList)) {
    my $restoreStr;
    foreach $restoreStr (split(/:/, db_get_answer($cRestoreBackList))) {
      if (defined db_get_answer_if_exists($cRestorePrefix . $restoreStr)) {
        $fileToRestore{db_get_answer($cRestorePrefix . $restoreStr)} =
          db_get_answer($cRestorePrefix . $restoreStr
                        . $cRestoreBackupSuffix);
      }
    }
  }
  return %fileToRestore;
}

# Uninstall a file previously installed by us
sub uninstall_file {
  my $file = shift;

  if (not db_file_in($file)) {
    # Not installed by this program
    return;
  }

  if (file_name_exist($file)) {
    if (file_changed_db_ts($file) || db_userfile_in($file)) {
      backup_file($file);
      db_remove_file($file);
      return;
    }

    if (not unlink($file)) {
      error('Unable to remove the file "' . $file . '".' . "\n");
    } else {
      db_remove_file($file);
    }

  } else {
    print wrap('This program previously created the file ' . $file . ', and '
               . 'was about to remove it.  Somebody else apparently did it '
               . 'already.' . "\n\n", 0);
    db_remove_file($file);
  }
}

# Uninstall a directory previously installed by us
sub uninstall_dir {
  my $dir = shift;

  if (not db_dir_in($dir)) {
    # Not installed by this program
    return;
  }

  if (-d $dir) {
    if (not rmdir($dir)) {
      print wrap('This program previously created the directory ' . $dir . ', '
                 . 'and was about to remove it. Since there are files in that '
                 . 'directory that this program did not create, it will not be '
                 . 'removed.' . "\n\n", 0);
      if (   defined($ENV{'VMWARE_DEBUG'})
          && ($ENV{'VMWARE_DEBUG'} eq 'yes')) {
        system('ls -AlR ' . shell_string($dir));
      }
    }
  } else {
    print wrap('This program previously created the directory ' . $dir
               . ', and was about to remove it. Somebody else apparently did '
               . 'it already.' . "\n\n", 0);
  }

  db_remove_dir($dir);
}

# Install one directory (recursively)
sub install_dir {
  my $src_dir = shift;
  my $dst_dir = shift;
  my $patchRef = shift;
  my $file;

  if (create_dir($dst_dir, $cFlagDirectoryMark) == $cDirExists) {
    my @statbuf;

    @statbuf = stat($dst_dir);
    if (not (defined($statbuf[2]))) {
      error('Unable to get the access rights of destination directory "' . $dst_dir . '".' . "\n\n");
    }

    # Was bug 15880
    if (   ($statbuf[2] & 0555) != 0555
        && get_answer('Current access permissions on directory "' . $dst_dir
                      . '" will prevent some users from using '
                      . vmware_product_name()
                      . '. Do you want to set those permissions properly?',
                      'yesno', 'yes') eq 'yes') {
      safe_chmod(($statbuf[2] & 07777) | 0555, $dst_dir);
    }
  } else {
    install_permission($src_dir, $dst_dir);
  }
  foreach $file (internal_ls($src_dir)) {
    if (-d $src_dir . '/' . $file) {
      install_dir($src_dir . '/' . $file, $dst_dir . '/' . $file, $patchRef);
    } else {
      install_file($src_dir . '/' . $file, $dst_dir . '/' . $file, $patchRef, $cFlagTimestamp);
    }
  }
}

# Uninstall files and directories beginning with a given prefix
sub uninstall_prefix {
  my $prefix = shift;
  my $prefix_len;
  my $file;
  my $dir;

  $prefix_len = length($prefix);

  # Remove all files beginning with $prefix
  foreach $file (keys %gDBFile) {
    if (substr($file, 0, $prefix_len) eq $prefix) {
      uninstall_file($file);
    }
  }

  # Remove all directories beginning with $prefix
  # We sort them by decreasing order of their length, to ensure that we will
  # remove the inner ones before the outer ones
  foreach $dir (sort {length($b) <=> length($a)} keys %gDBDir) {
    if (substr($dir, 0, $prefix_len) eq $prefix) {
      uninstall_dir($dir);
    }
  }
}

# Return the version of VMware
sub vmware_version {
  my $buildNr;

  $buildNr = '7.8.5 build-156735';
  return remove_whitespaces($buildNr);
}

# Return product name and version
sub vmware_longname {
   my $name = vmware_product_name() . ' ' . vmware_version();

   if (not (vmware_product() eq 'server')) {
      $name .= ' for ' . $gSystem{'system'};
   }

   return $name;
}

# Check the validity of an answer whose type is yesno
# Return a clean answer if valid, or ''
sub check_answer_yesno {
  my $answer = shift;
  my $source = shift;

  if (lc($answer) =~ /^y(es)?$/) {
    return 'yes';
  }

  if (lc($answer) =~ /^n(o)?$/) {
    return 'no';
  }

  if ($source eq 'user') {
    print wrap('The answer "' . $answer . '" is invalid.  It must be one of '
               . '"y" or "n".' . "\n\n", 0);
  }
  return '';
}
$gAnswerSize{'yesno'} = 3;
$gCheckAnswerFct{'yesno'} = \&check_answer_yesno;

# Check the validity of an answer based on its type
# Return a clean answer if valid, or ''
sub check_answer {
  my $answer = shift;
  my $type = shift;
  my $source = shift;

  if (not defined($gCheckAnswerFct{$type})) {
    die 'check_answer(): type ' . $type . ' not implemented :(' . "\n\n";
  }
  return &{$gCheckAnswerFct{$type}}($answer, $source);
}
# END OF THE LIBRARY FUNCTIONS

# Set the name of the main /etc/vmware* directory.
sub initialize_globals {

  if (vmware_product() eq 'tools-for-linux' ||
      vmware_product() eq 'tools-for-freebsd' ||
      vmware_product() eq 'tools-for-solaris') {
    $gRegistryDir = '/etc/vmware-tools';
  } else {
    $gRegistryDir = '/etc/vmware';
  }
  $gLogDir = '/var/log/vmware';
  $gStateDir = $gRegistryDir . '/state';
  $gInstallerMainDB = $gRegistryDir . '/locations';
  $gConfFlag = $gRegistryDir . '/not_configured';

  $gOption{'default'} = 0;
  $gOption{'compile'} = 0;
  $gOption{'prebuilt'} = 0;
  $gOption{'try-modules'} = 0;
  $gOption{'tools-switch'} = 0;
  $gOption{'overwriteSVGA'} = 0;
  $gOption{'preserve'} = 0;
  $gOption{'overwrite'} = 0;
  $gOption{'skip-stop-start'} = vmware_product() eq 'server';
  $gOption{'rpc-on-end'} = 0;
  $gOption{'create_shortcuts'} = 1;
  $gOption{'modules_only'} = 0;
  $gOption{'kernel_version'} = '';
}

# Set up the location of external helpers
sub initialize_external_helpers {
  my $program;
  my @programList;

  if (not defined($gHelper{'more'})) {
    $gHelper{'more'} = '';
    if (defined($ENV{'PAGER'})) {
      my @tokens;

      # The environment variable sometimes contains the pager name _followed by
      # a few command line options_.
      #
      # Isolate the program name (we are certain it does not contain a
      # whitespace) before dealing with it.
      @tokens = split(' ', $ENV{'PAGER'});
      $tokens[0] = DoesBinaryExist_Prompt($tokens[0]);
      if (not ($tokens[0] eq '')) {
        # This is _already_ a shell string
        $gHelper{'more'} = join(' ', @tokens);
      }
    }
    if ($gHelper{'more'} eq '') {
      $gHelper{'more'} = DoesBinaryExist_Prompt('more');
      if ($gHelper{'more'} eq '') {
        error('Unable to continue.' . "\n\n");
      }
      # Save it as a shell string
      $gHelper{'more'} = shell_string($gHelper{'more'});
    }
  }

  if (vmware_product() eq 'tools-for-freebsd') {
    @programList = ('cp', 'uname', 'grep', 'ldd', 'mknod', 'kldload',
                    'kldunload', 'mv', 'rm', 'ldconfig');
  } elsif (vmware_product() eq 'tools-for-solaris') {
    # Note that svcprop(1) is added for Solaris 10 and later after it is
    # guaranteed that uname(1) has been found
    @programList = ('cp', 'uname', 'grep', 'ldd', 'mknod', 'modload', 'modinfo',
                    'modunload', 'add_drv', 'rem_drv', 'update_drv',
                    'rm', 'isainfo', 'ifconfig', 'cat', 'mv', 'sed',
                    'cut');
  } elsif ((vmware_product() eq 'wgs') || (vmware_product() eq 'server')) {
    @programList = ('cp', 'uname', 'grep', 'ldd', 'mknod', 'depmod', 'insmod',
                    'lsmod', 'modprobe', 'rmmod', 'ifconfig', 'rm', 'tar',
                    'killall', 'perl', 'mv', 'touch', 'hostname', 'pidof');
  } else {
    @programList = ('cp', 'uname', 'grep', 'ldd', 'mknod', 'depmod', 'insmod',
                    'lsmod', 'modprobe', 'mv', 'rmmod', 'ifconfig', 'rm', 'tar');
  }

  foreach $program (@programList) {
    if (not defined($gHelper{$program})) {
      $gHelper{$program} = DoesBinaryExist_Prompt($program);
      if ($gHelper{$program} eq '') {
        error('Unable to continue.' . "\n\n");
      }
    }
  }

  if (vmware_product() eq 'tools-for-solaris' &&
      solaris_10_or_greater() eq 'yes') {
    $gHelper{'svcprop'} = DoesBinaryExist_Prompt('svcprop');
    if ($gHelper{'svcprop'} eq '') {
      error('Unable to continue.' . "\n\n");
    }
  }
  $gHelper{'insserv'} = internal_which('insserv');
  $gHelper{'chkconfig'} = internal_which('/sbin/chkconfig');
  if (vmware_product() eq 'server' &&
      $gHelper{'chkconfig'} eq '') {
         error('No initscript installer found.' . "\n\n");
  }
}

# Check the validity of an answer whose type is dirpath
# Return a clean answer if valid, or ''
sub check_answer_dirpath {
    my $answer = shift;
    my $source = shift;

    $answer = dir_remove_trailing_slashes($answer);

    if (substr($answer, 0, 1) ne '/') {
	print wrap('The path "' . $answer . '" is a relative path. Please enter '
		   . 'an absolute path.' . "\n\n", 0);
	return '';
    }

    if (-d $answer) {
	# The path is an existing directory
	return $answer;
    }

    # The path is not a directory
    if (file_name_exist($answer)) {
	if ($source eq 'user') {
	    print wrap('The path "' . $answer . '" exists, but is not a directory.'
		       . "\n\n", 0);
	}
	return '';
    }

    # The path does not exist
    if ($source eq 'user') {
	return (get_answer('The path "' . $answer . '" does not exist currently. '
			   . 'This program is going to create it, including needed '
			   . 'parent directories. Is this what you want?',
			   'yesno', 'yes') eq 'yes') ? $answer : '';
    } else {
	return $answer;
    }
}
$gAnswerSize{'dirpath'} = 20;
$gCheckAnswerFct{'dirpath'} = \&check_answer_dirpath;


# Check the validity of an answer whose type is dirpath_existing
# Return an existing directory if valid, or ''
sub check_answer_dirpath_existing {
    my $answer = shift;
    my $source = shift;

    $answer = dir_remove_trailing_slashes($answer);

    if (substr($answer, 0, 1) ne '/') {
	print wrap('The path "' . $answer . '" is a relative path. Please enter '
		   . 'an absolute path.' . "\n\n", 0);
	return '';
    }

    if (-d $answer) {
	# The path is an existing directory
	return $answer;
    }

    # The path is not a directory
    if (file_name_exist($answer) && ($source eq 'user')) {
      print wrap('The path "' . $answer . '" exists, but is not a directory.'
		 . "\n\n", 0);
    } else {
      # The path does not exist
      print wrap('The path "' . $answer . '" does not exist.' . "\n\n", 0);
    }
    return '';
}
$gAnswerSize{'dirpath_existing'} = 20;
$gCheckAnswerFct{'dirpath_existing'} = \&check_answer_dirpath_existing;


# Check the validity of an answer whose type is headerdir
# Return a clean answer if valid, or ''
sub check_answer_headerdir {
  my $answer = shift;
  my $source = shift;
  my $pattern = '@@VMWARE@@';
  my $header_version_uts;
  my $header_smp;
  my $uts_headers;

  $answer = dir_remove_trailing_slashes($answer);

  if (not (-d $answer)) {
    if ($source eq 'user') {
      print wrap('The path "' . $answer . '" is not an existing directory.'
                 . "\n\n", 0);
    }
    return '';
  }

  if ($answer =~ m|^/usr/include(/.*)?$|) { #/# Broken colorizer.
    if ($source eq 'user') {
      if (get_answer('The header files in /usr/include are generally for C '
                     . 'libraries, not for the running kernel. If you do not '
                     . 'have kernel header files in your /usr/src directory, '
                     . 'you probably do not have the kernel-source package '
                     . 'installed. Are you sure that /usr/include contains '
                     . 'the header files associated with your running kernel?',
                     'yesno', 'no') eq 'no') {
        return '';
      }
    }
  }

  if (not (-d $answer . '/linux')) {
    if ($source eq 'user') {
      print wrap('The path "' . $answer . '" is an existing directory, but it '
                 . 'does not contain a "linux" subdirectory as expected.'
                 . "\n\n", 0);
    }
    return '';
  }

  #
  # Check that the running kernel matches the set of header files
  #

  if (not (-r $answer . '/linux/version.h')) {
    if ($source eq 'user') {
      print wrap('The path "' . $answer . '" is a kernel header file '
                 . 'directory, but it does not contain the file '
                 . '"linux/version.h" as expected.  This can happen if the '
                 . 'kernel has never been built, or if you have invoked the '
                 . '"make mrproper" command in your kernel directory.  In any '
                 . 'case, you may want to rebuild your kernel.' . "\n\n", 0);
    }
    return '';
  }

  #
  # Kernels before 2.6.18 declare UTS_RELEASE in version.h.  Newer kernels
  # use utsrelease.h.  We include both just in case somebody moves UTS_RELEASE
  # back while leaving utsrelease.h file in place.
  #
  if ($gOption{'kernel_version'} eq '') {
    $uts_headers = "#include <linux/version.h>\n";
    if (-e $answer . '/linux/utsrelease.h') {
      $uts_headers .= "#include <linux/utsrelease.h>\n";
    }
    $header_version_uts = direct_command(
      shell_string($gHelper{'echo'}) . ' '
      . shell_string($uts_headers . $pattern
                     . ' UTS_RELEASE') . ' | ' . shell_string($gHelper{'gcc'})
      . ' ' . shell_string('-I' . $answer) . ' -E - | '
      . shell_string($gHelper{'grep'}) . ' ' . shell_string($pattern));
    chomp($header_version_uts);
    $header_version_uts =~ s/^$pattern \"([^\"]*)\".*$/$1/;
    if (not ($header_version_uts eq $gSystem{'uts_release'})) {
      if ($source eq 'user') {
        print wrap('The directory of kernel headers (version '
                   . $header_version_uts . ') does not match your running '
                   . 'kernel (version ' . $gSystem{'uts_release'} . ').  Even '
                   . 'if the module were to compile successfully, it would not '
                   . 'load into the running kernel.' . "\n\n", 0);
      }
      return '';
    }
  }

  if (not (-r $answer . '/linux/autoconf.h')) {
    if ($source eq 'user') {
      print wrap('The path "' . $answer . '" is a kernel header file '
                 . 'directory, but it does not contain the file '
                 . '"linux/autoconf.h" as expected.  This can happen if the '
                 . 'kernel has never been built, or if you have invoked the '
                 . '"make mrproper" command in your kernel directory.  In any '
                 . 'case, you may want to rebuild your kernel.' . "\n\n", 0);
    }
    return '';
  }
  $header_smp = direct_command(shell_string($gHelper{'grep'}) . ' CONFIG_SMP '
                               . shell_string($answer . '/linux/autoconf.h'));
  if (not ($header_smp eq '')) {
    # linux/autoconf.h contains the up/smp information
    $header_smp = direct_command(
      shell_string($gHelper{'echo'}) . ' '
      . shell_string('#include <linux/autoconf.h>' . "\n" . $pattern
                     . ' CONFIG_SMP') . ' | ' . shell_string($gHelper{'gcc'})
      . ' ' . shell_string('-I' . $answer) . ' -E - | '
      . shell_string($gHelper{'grep'}) . ' ' . shell_string($pattern));
    chomp($header_smp);
    $header_smp =~ s/^$pattern (\S+).*$/$1/;
    $header_smp = ($header_smp eq '1') ? 'yes' : 'no';
    if (not (lc($header_smp) eq lc($gSystem{'smp'}))) {
      if ($source eq 'user') {
        print wrap('The kernel defined by this directory of header files is '
                   . (($header_smp eq 'yes') ? 'multiprocessor'
                                             : 'uniprocessor') . ', while '
                   . 'your running kernel is '
                   . (($gSystem{'smp'} eq 'yes') ? 'multiprocessor'
                                                 : 'uniprocessor') . '.'
                   . "\n\n", 0);
      }
      return '';
    }
  }

  #
  # For kernels before 2.6.0 require asm and net subdirectories.  And verify
  # that PAGE_OFFSET for running kernel matches one specified in kernel
  # headers.  We use our Makefiles to build kernel modules on these kernels,
  # so we know that asm and net directories must be here for successful build,
  # and PAGE_OFFSET must match.
  #
  # For kernel 2.6.0 and above require ../Makefile and ../.config presence.
  # Although they could be theoretically missing, they are present on all
  # currently existing systems.  And check for ../.config presence
  # rules out /usr/src/linux/include eliminates false positive we
  # currently hit on SuSE 9.x systems.  And do not verify PAGE_OFFSET value
  # at all, asm/page.h needs special processing on 2.6.15+ kernels.
  #
  if ($header_version_uts =~ /^2\.[0-5]\./) {
    if (   (not (-d $answer . '/asm'))
        || (not (-d $answer . '/net'))) {
      if ($source eq 'user') {
        print wrap('The path "' . $answer . '" is an existing directory, but it '
                   . 'does not contain subdirectories "asm" and "net" as expected.'
                   . "\n\n", 0);
      }
      return '';
    }
    if (not (-r $answer . '/asm/page.h')) {
      if ($source eq 'user') {
        print wrap('The path "' . $answer . '" is a kernel header file '
                   . 'directory, but it does not contain the file "asm/page.h" '
                   . 'as expected.' . "\n\n", 0);
      }
      return '';
    }
    my $header_page_offset = direct_command(
      shell_string($gHelper{'echo'}) . ' '
      . shell_string('#define __KERNEL__' . "\n" . '#include <asm/page.h>'
                     . "\n" . $pattern . ' __PAGE_OFFSET') . ' | '
      . shell_string($gHelper{'gcc'}) . ' ' . shell_string('-I' . $answer)
      . ' -E - | ' . shell_string($gHelper{'grep'}) . ' '
      . shell_string($pattern));
    chomp($header_page_offset);
    # Ignore PAGE_OFFSET if we cannot parse it.
    if ($header_page_offset =~ /^$pattern \(?0x([0-9a-fA-F]{8,})/) {
      # We found a valid page offset
      $header_page_offset = $1;
      if (defined($gSystem{'page_offset'}) and
          not (lc($header_page_offset) eq lc($gSystem{'page_offset'}))) {
        if ($source eq 'user') {
          print wrap('The kernel defined by this directory of header files does '
                     . 'not have the same address space size as your running '
                     . 'kernel.' . "\n\n", 0);
        }
        return '';
      }
    }
  } else {
    if (not (-r $answer . '/../Makefile')) {
      if ($source eq 'user') {
        print wrap('The path "' . $answer . '" is a kernel header file '
                   . 'directory, but it is not part of kernel source tree.'
                   . "\n\n", 0);
      }
      return '';
    }
    if (not (-r $answer . '/../.config')) {
      if ($source eq 'user') {
        print wrap('The path "' . $answer . '" is a kernel header file '
                   . 'directory, but it is not configured yet.'
                   . "\n\n", 0);
      }
      return '';
    }
  }
  return $answer;
}
$gAnswerSize{'headerdir'} = 20;
$gCheckAnswerFct{'headerdir'} = \&check_answer_headerdir;

# Check the validity of an answer whose type is ip
# Return a clean answer if valid, or ''
sub check_answer_ip {
  my $answer = shift;
  my $source = shift;
  my $re;

  $re = '^([0-9]|[1-9][0-9]|1[0-9][0-9]|2([0-4][0-9]|5[0-5]))'
        . '(\.([0-9]|[1-9][0-9]|1[0-9][0-9]|2([0-4][0-9]|5[0-5]))){3}$';
  # This comment fixes emacs's broken syntax highlighting
  if ($answer =~ /$re/) {
    return $answer;
  }

  if ($source eq 'user') {
    print wrap('The answer "' . $answer . '" is invalid.  It must be of the '
               . 'form a.b.c.d where a, b, c and d are decimal numbers '
               . 'between 0 and 255.' . "\n\n", 0);
  }
  return '';
}
$gAnswerSize{'ip'} = 15;
$gCheckAnswerFct{'ip'} = \&check_answer_ip;

# Check the validity of an answer whose type is serial number
# Return a clean answer if valid, or ''
sub check_answer_serialnum {
  my $answer = shift;
  my $source = shift;
  my $re;

  if ($answer eq '') {
      return ' ';
  }

  $re = '^(([0-9]|[A-Z]){5}-){3}(([0-9]|[A-Z]){5})$';
  # This comment fixes emacs's broken syntax highlighting
  if ($answer =~ /$re/) {
    return $answer;
  }

  if ($source eq 'user') {
    print wrap('The answer "' . $answer . '" is invalid.  It must be of the '
               . 'form XXXXX-XXXXX-XXXXX-XXXXX where X is a digit 0-9 or a '
	       . 'capital letter A-Z' . "\n\n", 0);
  }
  return '';
}
$gAnswerSize{'serialnum'} = 23;
$gCheckAnswerFct{'serialnum'} = \&check_answer_serialnum;

# Check the validity of an answer whose type is editorwizardhelp
# Return a clean answer if valid, or ''
sub check_answer_editorwizardhelp {
  my $answer = shift;
  my $source = shift;

  if (lc($answer) =~ /^e(ditor)?$/) {
    return 'editor';
  }

  if (lc($answer) =~ /^w(izard)?$/) {
    return 'wizard';
  }

  if (lc($answer) =~ /^h(elp)?$/) {
    return 'help';
  }

  if ($source eq 'user') {
    print wrap('The answer "' . $answer . '" is invalid. It must be one of '
               . '"w", "e" or "h".' . "\n\n", 0);
  }
  return '';
}
$gAnswerSize{'editorwizardhelp'} = 6;
$gCheckAnswerFct{'editorwizardhelp'} = \&check_answer_editorwizardhelp;

# Check the validity of an answer whose type is yesnohelp
# Return a clean answer if valid, or ''
sub check_answer_yesnohelp {
  my $answer = shift;
  my $source = shift;

  if (lc($answer) =~ /^y(es)?$/) {
    return 'yes';
  }

  if (lc($answer) =~ /^n(o)?$/) {
    return 'no';
  }

  if (lc($answer) =~ /^h(elp)?$/) {
    return 'help';
  }

  if ($source eq 'user') {
    print wrap('The answer "' . $answer . '" is invalid.  It must be one of '
               . '"y", "n" or "h".' . "\n\n", 0);
  }
  return '';
}
$gAnswerSize{'yesnohelp'} = 4;
$gCheckAnswerFct{'yesnohelp'} = \&check_answer_yesnohelp;

# Check the validity of an answer whose type is vmnet
# Return a clean answer if valid, or ''
sub check_answer_vmnet {
  my $answer = shift;
  my $source = shift;

  if ($answer =~ /^\d+$/) {
    if ($answer >= $gMinVmnet && $answer <= $gMaxVmnet) {
      return $answer;
    }
  }

  if ($source eq 'user') {
    print wrap('The answer "' . $answer . '" is invalid. It must be an '
               . 'integer between ' . $gMinVmnet . ' and ' . $gMaxVmnet . '.'
               . "\n\n", 0);
  }

  return '';
}
$gAnswerSize{'vmnet'} = length("$gMaxVmnet");
$gCheckAnswerFct{'vmnet'} = \&check_answer_vmnet;

# Check the validity of an answer whose type is nettype
# Return a clean answer if valid, or ''
sub check_answer_nettype {
  my $answer = shift;
  my $source = shift;

  if (lc($answer) =~ /^h(ostonly)?$/) {
    return 'hostonly';
  }

  if (lc($answer =~ /^b(ridged)?$/)) {
    return 'bridged';
  }

  if (lc($answer =~ /^n(at)?$/)) {
    return 'nat';
  }

  if (lc($answer =~ /^none$/)) {
    return 'none';
  }

  if ($source eq 'user') {
    print wrap('The answer "' . $answer . '" is invalid. It must be either '
               . '"b", "h", "n", or "none".' . "\n\n", 0);
  }
  return '';
}
$gAnswerSize{'nettype'} = 8;
$gCheckAnswerFct{'nettype'} = \&check_answer_nettype;

# Check the validity of an answer whose type is availethif
# Return a clean answer if valid, or ''
sub check_answer_availethif {
  my $answer = shift;
  my $source = shift;

  if (grep($answer eq $_, @gAvailEthIf)) {
    return $answer;
  }

  if ($source eq 'user') {
    if (grep($answer eq $_, @gAllEthIf)) {
      print wrap('The ethernet device "' . $answer . '" is already configured '
                 . 'as a bridged device.' . "\n\n", 0);
      return '';
    }
    if (get_answer('The ethernet device "' . $answer . '" was not detected on '
                   . 'your system.  Available ethernet devices detected on '
                   . 'your system include ' . join(', ', @gAvailEthIf) . '.  '
                   . 'Are you sure you want to use this device? (yes/no)',
                   'yesno', 'no') eq 'no') {
      return '';
    } else {
      return $answer;
    }
  }
  return '';
}
$gAnswerSize{'availethif'} = 4;
$gCheckAnswerFct{'availethif'} = \&check_answer_availethif;

# check the validity of a user or group name against the set of authenticatable users,
# return the answer if valid or ''
sub check_answer_usergrp {
  my $answer = shift;
  my $source = shift;

  if ($answer=~/^[^-][a-zA-Z0-9.\@\$_-]+$/) {
    my @id_params = getpwnam $answer;
    if ((scalar(@id_params) != 0) && length($answer) < 32) {
      return $answer;
    }
  }

  if ($source eq 'user') {
    my $answer_string = '""';
    if (defined($answer)) {
       $answer_string = '"' . $answer . '"';
    }
    print wrap('The answer ' . $answer_string .' is invalid. Please enter a valid '
               . "name of length < 32 and containing any of letters of the alphabet, "
               . "numbers, '.\@_-', and and not beginning with a '-'.  The name must "
               . "be a valid user on this system."
               . "\n\n", 0);
  }

  return '';
}

$gAnswerSize{'usergrp'} = 32;
$gCheckAnswerFct{'usergrp'} = \&check_answer_usergrp;

# check the validity of a timeout value
# return the answer if valid or ''
sub check_answer_timeout {
  my $answer = shift;
  my $source = shift;

  if ($answer=~/^-?\d+$/ && $answer >= -1) {
    return $answer;
  }

  if ($source eq 'user') {
    print wrap('The answer "' . $answer . '" is invalid. Please enter a valid'
               . ' number of minutes in the range -1 to 99999' . "\n\n", 0);
  }

  return '';
}

$gAnswerSize{'timeout'} = 5;
$gCheckAnswerFct{'timeout'} = \&check_answer_timeout;


# Check the validity of an answer whose type is nocheck
# Always returns answer.
sub check_answer_anyethif {
  my $answer = shift;
  my $source = shift;

  return $answer;
}
$gAnswerSize{'anyethif'} = 4;
$gCheckAnswerFct{'anyethif'} = \&check_answer_anyethif;

# Check the validity of an answer whose type is inetport
# Return a clean answer if valid, or ''
sub check_answer_inetport {
  my $answer = shift;
  my $source = shift;

  if ($source eq 'default' || $source eq 'db') {
    if (check_if_port_free($answer) != 1) {
      return '';
    }
  }

  if (($answer !~ /^\d+$/) || ($answer < 0) || ($answer > 65536)) {
    my $filler = '';
    if ($answer ne '') {
      $filler = ", $answer,";
    }
    my $msg = "The port you selected" . $filler . " is invalid.  A port value "
            . "must be between 0 - 65536 and contain only decimal digits." . "\n\n";
    if ($source eq 'user') {
      print wrap($msg, 0);
    }
    return '';
  }

  if (check_if_port_free($answer) != 1) {
    if ($source eq 'user') {
      print wrap("The port you chose is not available for use.  Please select another "
               . "port value." . "\n", 0);
    }
    return '';
  }

  return $answer;
}
$gAnswerSize{'inetport'} = 5;
$gCheckAnswerFct{'inetport'} = \&check_answer_inetport;

# Check the validity of an answer whose type is number
# Return a clean number if valid, or '0'
# Default value for the 'number' type of answer.
# This $gMaxNumber as well as the $gAnswerSize{'number'} has to be updated
# before calling get_*_answer functions so that wrap() leaves enough room
# for the reply.
my $gMaxNumber = 0;
sub check_answer_number {
  my $answer = shift;
  my $source = shift;

  if (($answer =~ /^\d+$/) && ($answer > 0) && ($answer <= $gMaxNumber)) {
    return $answer;
  }

  if ($source eq 'user') {
    print wrap('The answer "' . $answer . '" is invalid. Please enter a valid '
               . 'number in the range 1 to ' . $gMaxNumber . "\n\n", 0);
  }

  return '';
}
$gAnswerSize{'number'} = length($gMaxNumber);
$gCheckAnswerFct{'number'} = \&check_answer_number;

# Check the validity of an answer whose type is netname
# Always returns answer.
sub check_answer_netname {
  my $answer = shift;
  my $source = shift;

  if (length($answer) > 255) {
    print wrap("That name is too long, please enter a name"
               . " shorter than 256 characters.\n");
    $answer = '';
  }
  return $answer;
}
$gAnswerSize{'netname'} = 32;
$gCheckAnswerFct{'netname'} = \&check_answer_netname;

my %gPortCache;
# Check $cServices file for specified port
# If port not in $cServices return 1
# If port is in $cServices return 0
sub check_port_not_registered {
  my $port = shift;
  if (defined($gPortCache{$port}) && $gPortCache{$port} == 2) {
    return 0;
  }
  return 1;
}

# Fill the cache with port assignments from /etc/services
# that aren't already filled by earlier calls to get_tcp_listeners()
# and get_proc_tcp_entries().  Ignore vmware* entries.
sub get_services_ports {
  if (not open(CONF, $cServices)) {
    return -1;
  }

  while (<CONF>) {
    if (/(.*)\b(\d+)\/(tcp)\b/i) {
      if ((!defined($gPortCache{$2})) && ($1 !~ /^vmware/i)) {
        $gPortCache{$2} = 2;
      }
    }
  }
  close(CONF);

}

# Use /proc/net/tcp as a list of ports in use and fillout the
# port cache with those entries.
sub get_proc_tcp_entries {
  undef %gPortCache;

  foreach my $i (qw(tcp tcp6)) {
    if (not open(TCP, "</proc/net/" . $i)) {
      next;
    }
    while (<TCP>) {
      if (/^\s*\d+:\s*[0-9a-fA-F]+:([0-9a-fA-F]{4})\s*[0-9a-fA-F]+:[0-9a-fA-F]{4}\s*([0-9a-fA-F]{2}).*$/) {
        # We'll consider a socket free if it is in TIME_WAIT state
        if ($2 eq "06") {
          next;
        }
        # Ignore if port is already defined, unless its value is 2.  That is,
        # the port is really a 'maybe' in use.
        if (!defined($gPortCache{$1}) || $gPortCache{$1} eq 2) {
          $gPortCache{hex($1)} = 1;
        }
      }
    }
    close TCP;
  }
}

sub check_if_port_active {
  my $port = shift;

  # In this case, only want ports that are active on the system, not just
  # place holders in /etc/services.
  if (defined($gPortCache{$port}) && $gPortCache{$port} == 1) {
    return 1;
  }

  return 0;
}

# if the port is already in use i.e. in the port cache.
# If port is free, return 1;
# If port is in use, return 0;
sub check_if_port_free {
  my $port = shift;

  if (check_if_port_active($port)) {
    return 0;
  }

  return check_port_not_registered($port);
}


# Display the end-user license agreement
sub show_EULA {
  if (   (not defined($gDBAnswer{'EULA_AGREED'}))
      || (db_get_answer('EULA_AGREED') eq 'no')) {
    if ($gOption{'default'} == 1) {
       print wrap('You must read and accept the End User License Agreement to '
                  . 'continue.' . "\n\n" . 'To display End User License '
                  . 'Agreement please restart ' . $0 . ' in the '
                  . 'interactive mode, without using `-d\' option.' . "\n\n", 0);
       exit 0;
    }
    query('You must read and accept the End User License Agreement to '
          . 'continue.' . "\n" . 'Press enter to display it.', '', 0);

    open(EULA, db_get_answer('DOCDIR') . '/EULA') ||
      error("$0: can't open EULA file: $!\n");

    my $origRecordSeparator = $/;
    undef $/;

    my $eula = <EULA>;
    close(EULA);

    $/ = $origRecordSeparator;

    $eula =~ s/(.{50,76})\s/$1\n/g;

    # Trap the PIPE signal to avoid broken pipe errors on RHEL4 U4.
    local $SIG{PIPE} = sub {};

    open(PAGER, '| ' . $gHelper{'more'}) ||
      error("$0: can't open $gHelper{'more'}: $!\n");
    print PAGER $eula . "\n";
    close(PAGER);

    print "\n";

    # Make sure there is no default answer here
    if (get_persistent_answer('Do you accept? (yes/no)', 'EULA_AGREED',
                              'yesno', '') eq 'no') {
      print wrap('Please try again when you are ready to accept.' . "\n\n", 0);
      exit 1;
    }

    print wrap('Thank you.' . "\n\n", 0);
  }
}

# Build a Linux kernel integer version
sub kernel_version_integer {
  my $version = shift;
  my $patchLevel = shift;
  my $subLevel = shift;

  return $version * 65536 + $patchLevel * 256 + $subLevel;
}

# Retrieve distribution information
sub distribution_info {
  my $issue = '/etc/issue';
  my $system;

  # First use the accurate method that are intended to work reliably on recent
  # distributions (if an FHS guy is listening, we really need a generic way to
  # do this)
  if (-e '/etc/debian_version') {
    return 'debian';
  }
  if (-e '/etc/redhat-release') {
    return 'redhat';
  }
  if (-e '/etc/SuSE-release') {
    return 'suse';
  }
  if (-e '/etc/turbolinux-release') {
    return 'turbolinux';
  }
  if (-e '/etc/mandrake-release') {
    return 'mandrake';
  }

  # Then use less accurate methods that should work even on old distributions,
  # if people haven't customized their system too much
  if (-e $issue) {
    if (not (direct_command(shell_string($gHelper{'grep'}) . ' -i '
                            . shell_string('debian') . ' '
                            . shell_string($issue)) eq '')) {
      return 'debian';
    }
    if (not (direct_command(shell_string($gHelper{'grep'}) . ' -i '
                            . shell_string('red *hat') . ' '
                            . shell_string($issue)) eq '')) {
      return 'redhat';
    }
    if (not (direct_command(shell_string($gHelper{'grep'}) . ' -i '
                            . shell_string('suse\|s\.u\.s\.e') . ' '
                            . shell_string($issue)) eq '')) {
      return 'suse';
    }
    if (not (direct_command(shell_string($gHelper{'grep'}) . ' -i '
                            . shell_string('caldera') . ' '
                            . shell_string($issue)) eq '')) {
      return 'caldera';
    }
  }

  return 'unknown';
}

sub vmware_check_vm_app_name {
  return db_get_answer('SBINDIR') . '/vmware-checkvm';
}

sub vmware_vmx_app_name {
  return db_get_answer('LIBDIR') . '/bin/vmware-vmx';
}

sub is64BitKernel {
  if (vmware_product() eq 'tools-for-solaris') {
    if (direct_command(shell_string($gHelper{'isainfo'}) . ' -k') =~ /amd64/) {
      return 1;
    } else {
      return 0;
    }
  }

  if (direct_command(shell_string($gHelper{'uname'}) . ' -m') =~ /(x86_64|amd64)/) {
    return 1;
  } else {
    return 0;
  }
}

# SIGINT handler (only gets used in tools configurations)
sub sigint_handler {
  send_rpc('toolinstall.end 0');
  error('');
}

# The installer packages up both 32 and 64 bit userlevel binaries, leaving
# them all in LIBDIR. This function links the correct thing in BINDIR and
# SBINDIR. This "installs" vmware-checkvm, vmware-guestd, vmware-user,
#          vmware-vmdesched, vmware-tools-upgrader, vmware-hgfsclient,
#          vmware-hgfsmounter, and wrapper script for vmware-toolbox.
sub setup32or64Symlinks {
  my $is64BitUserland = is64BitUserLand();
  my $libdir = db_get_answer('LIBDIR');
  my $libbindir = $libdir . ($is64BitUserland ? '/bin64' : '/bin32');
  my $libsbindir = $libdir . ($is64BitUserland ? '/sbin64' : '/sbin32');
  my $liblibdir = $libdir . ($is64BitUserland ? '/lib64' : '/lib32');
  my $pamdfile = $libdir . '/configurator/pam.d/vmware-guestd' . ($is64BitUserland ? '-x64' : '');
  my $bindir = db_get_answer('BINDIR');
  my $sbindir = db_get_answer('SBINDIR');

  $libbindir .= getFreeBSDBinSuffix();
  $liblibdir .= getFreeBSDLibSuffix();
  $libsbindir .= getFreeBSDSbinSuffix();

  install_symlink($libsbindir . '/vmware-checkvm',
                  $sbindir . '/vmware-checkvm');

  install_symlink($libsbindir . '/vmware-rpctool',
                  $sbindir . '/vmware-rpctool');

  install_symlink($libsbindir . '/vmware-guestd-wrapper',
                  $sbindir . '/vmware-guestd');

  # Both the toolbox and vmware-user get special attention as their dependency
  # on shipped gtk libraries require us to use special wrapper scripts if not
  # cut them out entirely for a particular product. Here's the scoop:
  #
  # On Linux and Solaris, we use the gtk toolbox everywhere, and use wrapper
  # scripts for both the toolbox and vmware-user. Note that the vmware-user
  # wrapper script is marked setuid.
  #
  # On FreeBSD, we use the gtk toolbox for release 5.0 and higher, and the tcl
  # toolbox otherwise. vmware-user is only available from release 5.3 or
  # higher, and both it and the toolbox are run via wrapper scripts (like
  # Linux and Solaris, the vmware-user wrapper script is marked setuid).
  if (vmware_product() eq 'tools-for-linux') {
     my $xorgMajorVer;
     my $xorgMinorVer;

     ($xorgMajorVer, $xorgMinorVer) = split_X_version(getXorgVersionAll());
     if (($xorgMajorVer == 7 && $xorgMinorVer >= 2) || ($xorgMajorVer > 7)) {
        install_symlink($libbindir . '/xorg71/vmware-user-wrapper',
                        $bindir . '/vmware-user-wrapper');
     } else {
        install_symlink($libbindir . '/vmware-user-wrapper',
                        $bindir . '/vmware-user-wrapper');
     }

     install_symlink($libbindir . '/vmware-user-suid-wrapper',
                     $bindir . '/vmware-user');
     install_symlink($libbindir . '/vmware-toolbox',
		     $bindir . '/vmware-toolbox');

     set_manifest_component('vmwareuser', 'TRUE');
     set_manifest_component('toolboxgtk', 'TRUE');

  } elsif (vmware_product() eq 'tools-for-freebsd') {
    my $release = `uname -r | cut -f1 -d-`;
    if ($release < 5.0) {
      install_symlink($libbindir . '/vmware-toolbox-tcl',
		      $bindir . '/vmware-toolbox');
    } else {
      install_symlink($libbindir . '/vmware-toolbox-gtk-wrapper',
		      $bindir . '/vmware-toolbox');
      if ($release >= 5.3) {
        install_symlink($libbindir . '/vmware-user-wrapper',
                        $bindir . '/vmware-user-wrapper');
        install_symlink($libbindir . '/vmware-user-suid-wrapper',
                        $bindir . '/vmware-user');
      }
    }
  }
  # Generic spots for the wrapper to access so it won't need to know lib32-6, etc.
  install_symlink($liblibdir, $libdir . "/lib");
  install_symlink($libbindir, $libdir . "/bin");
  install_symlink($libsbindir, $libdir . "/sbin");
  install_symlink($liblibdir . "/libconf", $libdir . "/libconf");

  if (vmware_product() eq 'tools-for-linux') {
     install_symlink($libsbindir . '/vmware-vmdesched',
                     $sbindir .  '/vmware-vmdesched');
     install_symlink($libsbindir . '/vmware-tools-upgrader',
                     $sbindir .  '/vmware-tools-upgrader');
     install_symlink($pamdfile, '/etc/pam.d/vmware-guestd');

     if (!$is64BitUserland) {
        install_symlink($liblibdir . '/libvmGuestLib.so', $libdir . '/../libvmGuestLib.so');
        install_symlink($liblibdir . '/libvmGuestLibJava.so', $libdir . '/../libvmGuestLibJava.so');
        set_manifest_component('vmguestlib', 'TRUE');
        set_manifest_component('vmguestlibjava', 'TRUE');
     }

     install_symlink($liblibdir . '/libDeployPkg.so', $libdir . '/../libDeployPkg.so');
     install_symlink($libbindir . '/vmware-hgfsclient',
                     $bindir . '/vmware-hgfsclient');
     install_symlink($libbindir . '/vmware-xferlogs',
                     $bindir . '/vmware-xferlogs');
     # Hardcoded because mount(8) expects mounting apps to be /sbin/mount.fs
     install_symlink($libsbindir . '/vmware-hgfsmounter',
		     '/sbin/mount.vmhgfs');

     my @vicf_libs = qw(libVICF.so libVICFbase.so libilu.so libilu-c.so);
     my $syslibdir = internal_dirname($libdir);
     foreach my $libfile (@vicf_libs) {
       install_symlink($liblibdir . '/' . $libfile, $syslibdir . '/' . $libfile);
     }

     set_manifest_component('guestd', 'TRUE');
     set_manifest_component('upgrader', 'TRUE');
     set_manifest_component('hgfsclient', 'TRUE');
     set_manifest_component('hgfsmounter', 'TRUE');
     set_manifest_component('checkvm', 'TRUE');
     set_manifest_component('vmdesched_service', 'TRUE');
     set_manifest_component('vicf', 'TRUE');
  }

  if(vmware_product() eq 'tools-for-freebsd') {
      my $hgfsmounterBinary = $libsbindir . '/vmware-hgfsmounter';
      if (-f $hgfsmounterBinary) {
	  safe_chmod(0555, $hgfsmounterBinary);

	  # Hardcoded because FreeBSD's mount(8) expects mounting apps to be /sbin/mount_fs
	  install_symlink($hgfsmounterBinary,
			  '/sbin/mount_vmhgfs');
      }
  }
}

# Solaris can boot into either its 32-bit or 64-bit kernel and invokes the
# appropriate binary through use of its isaexec(3C) program.  This means that
# we need to add symlinks for both the 32-bit and 64-bit versions and a hard
# link to /usr/lib/isaexec.
sub install_solaris_symlink {
   my $targetdir = shift;
   my $targetname = shift;
   my $linkdir = shift;
   my $linkname = shift;

   # Create i86 and amd64 directories if necessary
   create_dir($linkdir . '/i86', $cFlagDirectoryMark);
   create_dir($linkdir . '/amd64', $cFlagDirectoryMark);

   install_symlink($targetdir . '/i86/' . $targetname,
                   $linkdir . '/i86/' . $linkname);
   install_symlink($targetdir . '/amd64/' . $targetname,
                   $linkdir . '/amd64/' . $linkname);

   # Try to install a hard link to /usr/lib/isaexec.  If that doesn't work, we
   # copy isaexec to $linkdir and create a hard link to that one.
   if (install_hardlink('/usr/lib/isaexec', $linkdir . '/' . $linkname) eq 'no') {
      my $isaexec = $linkdir . '/isaexec';
      system(shell_string($gHelper{'cp'}) . ' /usr/lib/isaexec ' . $isaexec);
      db_add_file($isaexec, 0);
      install_hardlink($isaexec, $linkdir . '/' . $linkname);
   }
}

# See the comment above install_solaris_symlink().
sub setupSolarisSymlinks {
   my $libdir = db_get_answer('LIBDIR');
   my $libbindir = $libdir . '/bin';
   my $libsbindir = $libdir . '/sbin';
   my $bindir = db_get_answer('BINDIR');
   my $sbindir = db_get_answer('SBINDIR');


   install_solaris_symlink($libsbindir, 'vmware-checkvm',
                           $sbindir, 'vmware-checkvm');
   install_solaris_symlink($libsbindir, 'vmware-rpctool',
                           $sbindir, 'vmware-rpctool');
   install_solaris_symlink($libsbindir, 'vmware-guestd',
                           $sbindir, 'vmware-guestd');
   install_solaris_symlink($libsbindir, 'vmware-memctld',
                           $sbindir, 'vmware-memctld');

   install_solaris_symlink($libbindir, 'vmware-toolbox',
                           $bindir, 'vmware-toolbox');

   install_solaris_symlink($libbindir, 'vmware-user-suid-wrapper',
                           $bindir, 'vmware-user');
}

# We must set up various symlinks for each of our Tools products.
sub setupSymlinks {
   if (vmware_product() eq 'tools-for-linux') {
      setup32or64Symlinks();
   } elsif (vmware_product() eq 'tools-for-freebsd') {
      setup32or64Symlinks();
   } elsif (vmware_product() eq 'tools-for-solaris') {
      setupSolarisSymlinks();
   }
}

# Open a file binary and read the ELF header. We really only care about the fifth
# byte, EI_CLASS. I pulled the values from /usr/include/elf.h
sub is64BitElf {
  my $file = shift;
  my ($buf, $buf2);
  my $cEI_CLASS = 4;
  my $cELFCLASS64 = 2;
  my $cEI_MAG0 = 0;
  my $cSELFMAG = 4;
  my $cELFMAG = "\x7FELF";

  open(X_BIN, '<' . $file) || return 0;
  seek(X_BIN, $cEI_MAG0, 0) || return 0;
  read(X_BIN, $buf, $cSELFMAG)  || return 0;
  ($buf2) = unpack("a4", $buf);
  if ($buf2 ne $cELFMAG) {
      return 0;
  }

  seek(X_BIN, $cEI_CLASS, 0) || return 0;
  read(X_BIN, $buf, 1)  || return 0;
  ($buf2) = unpack("C", $buf);
  return ($buf2 eq $cELFCLASS64);
}


# Retrieve and check system information
sub system_info {
  my $fullVersion;
  my $version;
  my $patchLevel;
  my $subLevel;
  my $runSystem;

  $gSystem{'system'} = direct_command(shell_string($gHelper{'uname'}) . ' -s');
  chomp($gSystem{'system'});

  if (vmware_product() eq 'tools-for-freebsd') {
     $runSystem = 'FreeBSD';
  } elsif (vmware_product() eq 'tools-for-solaris') {
     $runSystem = 'SunOS';
  } else {
     $runSystem = 'Linux';
  }

  if (not ($gSystem{'system'} eq $runSystem)) {
    error('You are not running ' . $runSystem . '. This version of the product '
          . 'only runs on ' . $runSystem . '.' . "\n\n");
  }

  # Users will expect the output to be "Solaris", despite what uname -s says
  if (vmware_product() eq 'tools-for-solaris') {
    $gSystem{'system'} = 'Solaris';
  }

  $gSystem{'uts_release'} = direct_command(shell_string($gHelper{'uname'})
                                             . ' -r');
  chomp($gSystem{'uts_release'});
  $gSystem{'uts_version'} = direct_command(shell_string($gHelper{'uname'})
                                           . ' -v');
  chomp($gSystem{'uts_version'});

  if ($runSystem eq 'Linux') {

    ($version, $patchLevel, $subLevel) = split(/\./, $gSystem{'uts_release'});
    # Clean the subLevel in case there is an extraversion
    ($subLevel) = split(/[^0-9]/, $subLevel);
    $gSystem{'version_utsclean'} = $version . '.' . $patchLevel . '.'
                                   . $subLevel;

    $gSystem{'version_integer'} = kernel_version_integer($version, $patchLevel,
                                                         $subLevel);
    if ($gSystem{'version_integer'} < kernel_version_integer(2, 0, 0)) {
      error('You are running Linux version ' . $gSystem{'version_utsclean'}
            . '.  This product only runs on 2.0.0 and later kernels.' . "\n\n");
    }

    if (vmware_product() eq 'server') {
      $gSystem{'smp'} = 'no';
      $gSystem{'versioned'} = 'yes';
    } else {
      $gSystem{'smp'} = (direct_command(shell_string($gHelper{'uname'})
                                        . ' -v') =~ / SMP /) ? 'yes' : 'no';
      $gSystem{'versioned'} = (direct_command(shell_string($gHelper{'grep'}) . ' '
        . shell_string('^[0-9a-fA-F]\{8\} Using_Versions') . ' /proc/ksyms 2> /dev/null')
          eq '') ? 'no' : 'yes';
    }

    $gSystem{'distribution'} = distribution_info();

    if (is64BitKernel()) {
      $gSystem{'page_offset'} = '0000010000000000';
    } else {
      $gSystem{'page_offset'} = 'C0000000';
    }

    if ($gSystem{'version_integer'} >= kernel_version_integer(2, 1, 0)) {
      # 2.1.0+ kernels have hardware verify_area() support
      my @fields;

      @fields = split(' ', direct_command(
        shell_string($gHelper{'grep'}) . ' '
        . shell_string('^[0-9a-fA-F]\{8\} printk') . ' /proc/ksyms 2> /dev/null'));
      if (not defined($fields[0])) {
        @fields = split(' ', direct_command(
	  shell_string($gHelper{'grep'}) . ' '
	  . shell_string('^[0-9a-fA-F]\{8\} \w printk') . ' /proc/kallsyms 2> /dev/null'));
      }
      if (defined($fields[0])) {
        my $page_offset;

        # printk is always located in first 256KB of kernel - that is from
        # PAGE_OFFSET to PAGE_OFFSET + 256KB on normal kernel and
        # PAGE_OFFSET + 1MB to PAGE_OFFSET + 1.25MB for bzImage kernel.
        # Both ranges are well below 16MB granularity we are allowing.
        if ($fields[0] =~ /^([0-9a-fA-F]{2})/) {
          $page_offset = uc($1).'000000';
        } else {
	  $page_offset = undef;
        }
        $gSystem{'page_offset'} = $page_offset;
      } else {
        # Unable to find page_offset: accept anything
	$gSystem{'page_offset'} = undef;
      }
    }

    # Linux kernel build bug
    $gSystem{'build_bug'} = (direct_command(shell_string($gHelper{'grep'}) . ' '
      . shell_string('^[0-9a-fA-F]\{8\} __global_cli_R__ver___global_cli')
      . ' /proc/ksyms 2> /dev/null') eq '') ? 'no' : 'yes';
  }

  # Warning, the return after the end of the if statement
  # will void everything after.
  if (vmware_product() eq 'tools-for-linux' ||
      vmware_product() eq 'tools-for-freebsd' ||
      vmware_product() eq 'tools-for-solaris') {

    $gSystem{'product'} =
      direct_command(shell_string(vmware_check_vm_app_name()) . ' -p');
    if (direct_command(shell_string(vmware_check_vm_app_name())) =~ /good/) {
      $gSystem{'invm'} = 'yes';
    } else {
      $gSystem{'invm'} = 'no';
    }
    $gSystem{'resolution'} =
      direct_command(shell_string(vmware_check_vm_app_name()) . ' -r');
    chomp($gSystem{'resolution'});
    return;
  }

  if (vmware_product() eq 'wgs') {
    if ($gSystem{'uts_release'} =~ m/2\.2\.14-(5|5\.0)/) {
         print wrap('You are running kernel ' . $gSystem{'uts_release'} . '. '
                    . 'There is a known issue with this specific kernel that '
                    . 'can cause corruption of memory on a system wide level '
                    . 'under heavy load, such as when running '
                    . vmware_product_name() . '.' . "\n\n" . 'We recommend '
                    . 'you download the patch from Red Hat, or upgrade your '
                    . 'kernel to 2.2.17.' . "\n" . 'See http://www.redhat.com'
                    . '/support/errata/RHBA-2000013-01.html and consult your '
                    . 'distribution\'s documentation for instructions on how '
                    . 'to upgrade your kernel.' . "\n\n", 0);
    }
  }

  # CONFIG_UMISC on 2.0 kernels
  if ($gSystem{'version_integer'} < kernel_version_integer(2, 1, 0)) {
    if (   (direct_command(shell_string($gHelper{'grep'}) . ' '
                           . shell_string('^[0-9a-fA-F]\{8\} misc_register')
                           . ' /proc/ksyms') eq '')
        || (direct_command(shell_string($gHelper{'grep'}) . ' '
                           . shell_string('^[0-9a-fA-F]\{8\} misc_deregister')
                           . ' /proc/ksyms') eq '')) {
      error('You are running a Linux kernel version '
            . $gSystem{'version_utsclean'} . ' that was not built with the '
            . 'CONFIG_UMISC configuration parameter set.  '
            . vmware_product_name() . ' will not run on this system.'
            . "\n\n");
    }
  }

  # 3Com bug on 2.0.3[45] kernels
  if (   ($gSystem{'version_integer'} >= kernel_version_integer(2, 0, 34))
      && ($gSystem{'version_integer'} <= kernel_version_integer(2, 0, 35))) {
    if (   (not (-r '/proc/ioports'))
        || (not (direct_command(shell_string($gHelper{'grep'}) . ' -i '
                                . shell_string('3c90\|3c59')
                                . ' /proc/ioports') eq ''))) {
      if (get_answer('You are running Linux version '
                     . $gSystem{'version_utsclean'} . ' possibly with a 3Com '
                     . 'networking card.  Linux kernel versions 2.0.34 and '
                     . '2.0.35 have a bug in the 3Com driver that interacts '
                     . 'badly with this product.  Specifically, your physical '
                     . 'machine will occasionally hang and will require a '
                     . 'hard reset.  This bug has been fixed in 2.0.36 and '
                     . 'later kernels.  Do you want to continue the '
                     . 'configuration anyway?', 'yesno', 'no') eq 'no') {
         exit 1;
      }
    }
  }

  # These commands are Linux-specific
  if (vmware_product() ne 'tools-for-freebsd' &&
      vmware_product() ne 'tools-for-solaris') {
    # C library
    # XXX This relies on the locale
    my @missing;
    my $ldd_out = direct_command(shell_string($gHelper{'ldd'}) . ' ' . vmware_vmx_app_name());
    foreach my $lib (split(/\n/, $ldd_out)) {
      if ($lib =~ '(\S+) => not found') {
         push(@missing, $1);
      }
    }

    if (scalar(@missing) > 0) {
       print wrap("The following libraries could not be found on your system:\n", 0);
       print join("\n", @missing);
       print "\n\n";

       query('You will need to install these manually before you can run ' .
             vmware_product_name() . ".\n\nPress enter to continue.", '', 0);
    }

    # Processor
    foreach my $instruction ('^cpuid', 'cmov') {
      if (direct_command(shell_string($gHelper{'grep'}) . ' '
          . shell_string($instruction) . ' /proc/cpuinfo') eq '') {
        # Read the current config file;
        open(CPUINFO, '/proc/cpuinfo')
           or error('Unable to open /proc/cpuinfo in read-mode' . "\n\n");
        my @cpuinfo = <CPUINFO>;
        close(CPUINFO);
        error('Your ' . (($gSystem{'smp'} eq 'yes') ? 'processors do'
                                                    : 'processor does') . ' not '
              . 'support the ' . $instruction . ' instruction. '
              . vmware_product_name() . ' will not run on this system.' . "\n\n"
              . 'Your /proc/cpuinfo is:' . "\n\n" . "@cpuinfo");
      }
    }
    # The "flags" field became the "features" field in 2.4.0-test11-pre5
    if (direct_command(shell_string($gHelper{'grep'}) . ' '
                       . shell_string('^\(flags\|features\).* tsc')
                       . ' /proc/cpuinfo') eq '') {
      error('Your ' . (($gSystem{'smp'} eq 'yes') ? 'processors do'
                                                  : 'processor does') . ' not '
            . 'have a Time Stamp Counter.  ' . vmware_product_name()
            . ' will not run on this system.' . "\n\n");
    }
  }
}

# Point the user to a URL dealing with module-related problems and exits
sub module_error {
  error('For more information on how to troubleshoot module-related problems, '
        . 'please visit our Web site at "http://www.vmware.com/go/'
        . 'unsup-linux-products" and "http://www.vmware.com/go/'
        . 'unsup-linux-tools".' . "\n\n");
}

# Install a module if it suitable
# Return 1 if success, 0 if failure
sub try_module {
  my $name = shift;
  my $mod = shift;
  my $force = shift;
  my $silent = shift;
  my $dst_dir;
  my %patch;

  if (not (-e $mod)) {
    # The module does not exist
    return 0;
  }

  if (not (vmware_product() eq 'server')) {
    # Probe the module without loading it or executing its code.  It is cool
    # because it avoids problems like 'Device or resource busy'
    # Note: -f bypasses only the kernel version check, not the symbol
    # resolution
    if(!kmod_load_by_path($mod, $silent, $force, 1)) {
      return 0;
    }

    # If we are using new module-init-tools, they just ignore
    # '-p' option, and they just loaded module into the memory.
    # Just try rmmod-ing it. Silently.
    kmod_unload($name, 0);
  }

  if (-d $cKernelModuleDir . '/'. $gSystem{'uts_release'}) {
    $dst_dir = $cKernelModuleDir . '/' . $gSystem{'uts_release'};
  } else {
    print wrap('This program does not know where to install the ' . $name
               . ' module because the "' . $cKernelModuleDir . '/'
               . $gSystem{'uts_release'} . '" directory (the usual '
               . 'location where the running kernel would look for the '
               . 'module) is missing.  Please make sure that this '
               . 'directory exists before re-running this program.'
               . "\n\n", 0);
    return 0;
  }
  create_dir($dst_dir . '/misc', $cFlagDirectoryMark);
  undef %patch;
  # Install the module with a .o extension, as the Linux kernel does
  my $modDest = $dst_dir . '/misc/' . $name;
  install_file($mod, $modDest . '.o', \%patch, $cFlagTimestamp);
  # install a .ko symlink for 2.6 kernels
  install_symlink($modDest . '.o', $modDest . '.ko');
  # The old installer allowed people to manually build modules without .o
  # extension.  Such modules were not removed by the old uninstaller, and
  # unfortunately, insmod tries them first.  Let's move them.
  if (file_name_exist($dst_dir . '/misc/' . $name)) {
    backup_file($dst_dir . '/misc/' . $name);
    if (not unlink($dst_dir . '/misc/' . $name)) {
      print STDERR wrap('Unable to remove the file ' . $dst_dir . '/misc/'
                        . $name . '.' . "\n\n", 0);
    }
  }

  return 1;
}

# Remove a temporary directory
sub remove_tmp_dir {
  my $dir = shift;

  if (system(shell_string($gHelper{'rm'}) . ' -rf ' . shell_string($dir))) {
    print STDERR wrap('Unable to remove the temporary directory ' . $dir . '.'
                      . "\n\n", 0);
  };
}

sub get_cc {
  $gHelper{'gcc'} = '';
  if (defined($ENV{'CC'}) && (not ($ENV{'CC'} eq ''))) {
    $gHelper{'gcc'} = internal_which($ENV{'CC'});
    if ($gHelper{'gcc'} eq '') {
      print wrap('Unable to find the compiler specified in the CC environnment variable: "'
                 . $ENV{'CC'} . '".' . "\n\n", 0);
    }
  }
  if ($gHelper{'gcc'} eq '') {
    $gHelper{'gcc'} = internal_which('gcc');
    if ($gHelper{'gcc'} eq '') {
      $gHelper{'gcc'} = internal_which('egcs');
      if ($gHelper{'gcc'} eq '') {
        $gHelper{'gcc'} = internal_which('kgcc');
        if ($gHelper{'gcc'} eq '') {
          $gHelper{'gcc'} = DoesBinaryExist_Prompt('gcc');
        }
      }
    }
  }
  print wrap('Using compiler "' . $gHelper{'gcc'}
             . '". Use environment variable CC to override.' . "\n\n", 0);
  return $gHelper{'gcc'};
}

sub get_gcc_version {
  my ($gcc) = @_;
  # See bug 330893. Previously, we retrieved the gcc version from the output
  # of "gcc -dumpversion". Unfortunately, SuSE doesn't use this string like
  # any other distribution, and so we'll retrieve this from parsing the
  # output of "gcc -v" instead.
  my $gcc_version = direct_command(shell_string($gcc) . " -v 2>&1 | tail -1");
  chomp($gcc_version);
  # Two examples of $gcc_version at this stage are:
  #
  # gcc version 4.1.2 20070115 (prerelease) (SUSE Linux)
  # gcc version 4.1.2 20071124 (Red Hat 4.1.2-42)
  #
  # Parse through this to retrieve the version information.
  if ($gcc_version =~ /^gcc version (egcs-)?(\d+\.\d+(\.\d+)*)/) {
    return $2;
  } else {
    print wrap('Your compiler "' . $gHelper{'gcc'} . '" version "' .
	       $gcc_version . '" is not supported ' .
	       'by this version of ' . vmware_product_name() . '.' .
	       "\n\n", 0);
    return 'no';
  }

}

# Verify gcc version, finding a better match if needed.
sub check_gcc_version {
  my ($kernel_gcc_version) = undef;

  if (open(PROC_VERSION, '</proc/version')) {
    my $line;
    if (defined($line = <PROC_VERSION>)) {
      close PROC_VERSION;
      if ($line =~ /gcc version (egcs-)?(\d+(\.\d+)*)/) {
        $kernel_gcc_version = $2;
        if ($kernel_gcc_version eq $gSystem{'gcc_version'}) {
          return 'yes';
        }
      }
    } else {
      close PROC_VERSION;
    }
  }
  my $msg;
  my $g_major = '0';
  if ($gSystem{'gcc_version'} =~ /^(\d+)\./) {
    $g_major = $1;
  }
  if (defined($kernel_gcc_version)) {
    my $k_major = '0';
    my $k_minor = '0';

    if ($kernel_gcc_version =~ /^(\d+)\.(\d+)/) {
      $k_major = $1;
      $k_minor = $2;
    }
    if ($g_major ne $k_major) {
      # Try a to find a gcc-x.y binary
      my $newGcc = internal_which("gcc-$k_major.$k_minor");
      if ($newGcc ne '') {
	# We found one, we need to update the global values.
	$gHelper{'gcc'} = $newGcc;
	$gSystem{'gcc_version'} = get_gcc_version($newGcc);
	if ($gSystem{'gcc_version'} eq 'no') {
	  return 'no';
        } else {
	  $gSystem{'gcc_version'} =~ /^(\d+)\./;
	  $g_major = $1;
	  if ($kernel_gcc_version eq $gSystem{'gcc_version'}) {
	    return 'yes';
	  }
	}
      }
    }
    $msg = 'Your kernel was built with "gcc" version "' . $kernel_gcc_version .
           '", while you are trying to use "' . $gHelper{'gcc'} .
           '" version "' . $gSystem{'gcc_version'} . '". ';
    if ($g_major ne $k_major) {
      $msg .= 'This configuration is not supported and ' .
              vmware_product_name() . ' cannot work in such configuration. ' .
              'Please either recompile your kernel with "' . $gHelper{'gcc'} .
              '" version "'. $gSystem{'gcc_version'} . '", or restart ' . $0 .
              ' with CC environment variable pointing to the "gcc" version "' .
              $kernel_gcc_version . '".' . "\n\n";
      print wrap($msg, 0);
      return 'no';
    }
    $msg .= 'This configuration is not recommended and ' .
            vmware_product_name() . ' may crash if you\'ll continue. ' .
            'Please try to use exactly same compiler as one used for ' .
            'building your kernel. Do you want to go with compiler "' .
            $gHelper{'gcc'} .'" version "' . $gSystem{'gcc_version'} .'" anyway?';
  }
  if (defined($msg) and get_answer($msg, 'yesno', 'no') eq 'no') {
    return 'no';
  }
  return 'yes';
}

# Determine whether it is remotely plausible to build a module from source
sub can_build_module {
  my $name = shift;

  if (vmware_product() eq 'tools-for-freebsd' ||
      vmware_product() eq 'tools-for-solaris') {
      return 'no'; # Right now we only build tools from source on Linux
  }

  return 'yes';
}

# Build a module
sub build_module {
  my $name = shift;
  my $dir = shift;
  my $ideal = shift;
  my $build_dir;
  my $gcc_version;

  # Lazy initialization
  if ($gFirstModuleBuild == 1) {
    my $program;
    my $headerdir;

    foreach $program ('make', 'echo', 'tar', 'rm') {
      if (not defined($gHelper{$program})) {
        $gHelper{$program} = DoesBinaryExist_Prompt($program);
        if ($gHelper{$program} eq '') {
          return 'no';
        }
      }
    }

    if (get_cc() eq '') {
      return 'no';
    }

    $gSystem{'gcc_version'} = get_gcc_version($gHelper{'gcc'});
    if ($gSystem{'gcc_version'} eq 'no') {
      return 'no';
    }

    if (check_gcc_version() eq 'no') {
      return 'no';
    }

    # When installing the modules, kernels 2.4+ setup a symlink to the kernel
    # source directory
    $headerdir = $cKernelModuleDir . '/preferred/build/include';
    if (($gOption{'kernel_version'} ne '') or (check_answer_headerdir($headerdir, 'default') eq '')) {
      $headerdir = $cKernelModuleDir . '/' .
                   (($gOption{'kernel_version'} eq '')?
                    $gSystem{'uts_release'}:
                    $gOption{'kernel_version'})
                   . '/build/include';
      if (check_answer_headerdir($headerdir, 'default') eq '') {
        # Use a default usual location
        $headerdir = '/usr/src/linux/include';
      }
    }
    db_remove_answer("HEADER_DIR");
    get_persistent_answer('What is the location of the directory of C header '
                          . 'files that match your running kernel?',
                          'HEADER_DIR', 'headerdir', $headerdir);

    $gFirstModuleBuild = 0;
  }

  print wrap('Extracting the sources of the ' . $name . ' module.' . "\n\n",
             0);
  $build_dir = make_tmp_dir($cTmpDirPrefix);

  if (system(shell_string($gHelper{'tar'}) . ' -C ' . shell_string($build_dir)
             . ' -xopf ' . shell_string($dir . '/' . $name . '.tar'))) {
    print wrap('Unable to untar the "' . $dir . '/' . $name . '.tar'
               . '" file in the "' . $build_dir . '" directory.' . "\n\n", 0);
    return 'no';
  }

  print wrap('Building the ' . $name . ' module.' . "\n\n", 0);
  if (system(shell_string($gHelper{'make'}) . ' -C '
             . shell_string($build_dir . '/' . $name . '-only')
             . ' auto-build ' . (($gSystem{'smp'} eq 'yes') ? 'SUPPORT_SMP=1 '
                                                            : '')
             . (($gOption{'kernel_version'} ne '')?
                 shell_string('VM_UNAME=' . $gOption{'kernel_version'}) . ' ':'')
             . shell_string('HEADER_DIR=' . db_get_answer('HEADER_DIR')) . ' '
             . shell_string('CC=' . $gHelper{'gcc'}) . ' '
             . shell_string('GREP=' . $gHelper{'grep'}) . ' '
             . shell_string('IS_GCC_3='
             . (($gSystem{'gcc_version'} =~ /^3\./) ? 'yes' : 'no')))) {
    print wrap('Unable to build the ' . $name . ' module.' . "\n\n", 0);
    return 'no';
  }

  if ($gOption{'kernel_version'} eq '')
  {
    # Don't use the force flag: the module is supposed to perfectly load
    if (try_module($name, $build_dir . '/' . $name . '.o', 0, 1)) {
      print wrap('The ' . $name . ' module loads perfectly into the running kernel.'
                 . "\n\n", 0);
      remove_tmp_dir($build_dir);
      return 'yes';
    }
  } else {
    print wrap('Not trying to load the module as it is for a different kernel version.' . "\n\n", 0);
    remove_tmp_dir($build_dir);
    return 'yes';
  }
  # Don't remove the build dir so that the user can investiguate
  print wrap('Unable to make a ' . $name . ' module that can be loaded in the '
             . 'running kernel:' . "\n", 0);
  try_module($name, $build_dir . '/' . $name . '.o', 0, 0);
  # Try to analyze some usual suspects
  if ($gSystem{'build_bug'} eq 'yes') {
    print wrap('It appears that your running kernel has not been built from a '
               . 'kernel source tree that was completely clean (i.e. the '
               . 'person who built your running kernel did not use the "make '
               . 'mrproper" command).  You may want to ask the provider of '
               . 'your Linux distribution to fix the problem.  In the '
               . 'meantime, you can do it yourself by rebuilding a kernel '
               . 'from a kernel source tree that is completely clean.'
               . "\n\n", 0);
  } else {
    print wrap('There is probably a slight difference in the kernel '
               . 'configuration between the set of C header files you '
               . 'specified and your running kernel.  You may want to rebuild '
               . 'a kernel based on that directory, or specify another '
               . 'directory.' . "\n\n", 0);
  }
  return 'no';
}

# Converts (currently RedHat EL 3, 4 and 5 only) version to the opaque token - if
# tokens from two kernels are identical, these two kernels are probably
# ABI compatible.
sub get_module_compatible_version {
  my $utsrel = shift;

  # RHEL3: 2.4.21-9.0.1.ELhugemem => 2.4.21-ELhugemem
  # RHEL4: 2.6.9-11.ELsmp => 2.6.9-ELsmp
  if ($utsrel =~ /^(\d+\.\d+\.\d+-)[0-9.]+\.(EL.*)$/) {
    return $1.$2;
  }
  # RHEL5: 2.6.18-8.1.1.el5 => 2.6.18-el5
  if ($utsrel =~ /^(\d+\.\d+\.\d+-)[0-9.]+\.(el.*)$/) {
    return $1.$2;
  }
  return $utsrel;
}

# Create a list of modules suitable for the running kernel
# The kernel module loader does quite a good job when modules are versioned.
# But in the other case, we must be _very_ careful
sub get_suitable_modules {
  my $dir = shift;
  my @perfect = ();
  my @compatible = ();
  my @dangerous = ();
  my $candidate;
  my $uts_release = $gSystem{'uts_release'};
  my $uts_compatible = get_module_compatible_version($uts_release);

  foreach $candidate (internal_ls($dir)) {
    my %prop;
    my $list;

    # Read the properties file
    if (not open(PROP, '<' . $dir . '/' . $candidate . '/properties')) {
      print STDERR wrap('Unable to open the property file "' . $dir . '/'
                        . $candidate . '/properties".  Skipping this kernel.'
                        . "\n\n", 0);
      next;
    }
    undef %prop;
    while (<PROP>) {
      if (/^UtsVersion (.+)$/) {
        $prop{'UtsVersion'} = $1;
      } elsif (/^(\S+) (\S+)/) {
        $prop{$1} = $2;
      }
    }
    close(PROP);

    if (not (lc($gSystem{'smp'}) eq lc($prop{'SMP'}))) {
      # SMP does not match
      next;
    }
    if (defined($gSystem{'page_offset'}) and
        not (lc($gSystem{'page_offset'}) eq lc($prop{'PageOffset'}))) {
      # Page offset does not match
      next;
    }

    # By default module is not good for anything
    $list = undef;
    # If user asked to try all modules, do as he wishes.  I do not believe
    # that this option has any real use, it is way too dangerous.
    if ($gOption{'try-modules'} == 1) {
      $list = \@dangerous;
    }
    # If module is versioned, try "compatible" match (ModVersion is requied
    # due to 2.4.19-4GB being delivered by both SuSE8.1 and SLES8)
    if (defined($prop{'ModVersion'}) and
        $prop{'ModVersion'} eq 'yes' and
        $uts_compatible eq get_module_compatible_version($prop{'UtsRelease'})) {
      $list = \@compatible;
    }
    # If version matches exactly, great.  But only if UtsVersion matches,
    # otherwise it is second class match equivalent to the "compatible" match
    if ($uts_release eq $prop{'UtsRelease'}
        && (!defined($prop{'UtsVersion'})
            || $gSystem{'uts_version'} eq $prop{'UtsVersion'})) {
      $list = \@perfect;
    }
    if (defined($list)) {
      push @$list, ($candidate, $prop{'ModVersion'});
    }
  }

  return (@perfect, @compatible, @dangerous);
}

# Find the first file that exists from the list of files.
# Returns undefined if none of them exists.
sub find_first_exist {
  my $return_val;
  my $file = shift;
  while (defined $file) {
    if (-e $file) {
      $return_val = $file;
      last;
    }
    $file = shift;
  }
  return $return_val;
}

# Configure a module
sub configure_module {
  my $name = shift;
  my $mod_dir;

  if (defined($gDBAnswer{'ALT_MOD_DIR'})
      && ($gDBAnswer{'ALT_MOD_DIR'} eq 'yes')) {
    $mod_dir = db_get_answer('LIBDIR') . '/modules.new';
  } else {
    $mod_dir = db_get_answer('LIBDIR') . '/modules';
  }

  if ($gOption{'compile'} == 1
      && can_build_module($name) eq 'yes') {
    db_add_answer('BUILDR_' . $name, 'yes');
  } else {
    my @mod_list;

    @mod_list = get_suitable_modules($mod_dir . '/binary');
    while ($#mod_list > -1) {
      my $candidate = shift(@mod_list);
      my $modversion = shift(@mod_list);

      # Note: When using the force flag,
      #        Non-versioned modules can load into a     versioned kernel.
      #            Versioned modules can load into a non-versioned kernel.
      #
      # Consequently, it is only safe to use the force flag if _both_ the
      # kernel and the module are versioned.
      # This is not always the case as demonstrated by bug 18371.
      #
      # I would stop using force flag immediately, it does nothing good.

      if (try_module($name,
                     $mod_dir . '/binary/' . $candidate . '/objects/'
                     . $name . '.o',
                        ($gSystem{'versioned'} eq 'yes')
                     && ($modversion eq 'yes'), 1)) {
        print wrap('The ' . $candidate . ' - ' . $name . ' module '
                 . 'loads perfectly into the ' . 'running kernel.' . "\n\n", 0);
        return 'yes';
      }
    }

    if ($gOption{'prebuilt'} == 1) {
      db_add_answer('BUILDR_' . $name, 'no');
      print wrap('None of the pre-built ' . $name . ' modules for '
		 . vmware_product_name() . ' is suitable for your '
		 . 'running kernel.' . "\n\n", 0);
      return 'no';
    }

    # No more building modules for 'ws' unless forced to.
    if (vmware_product() eq 'ws' && !$gOption{'compile'}) {
       # don't restart services at the end, no modules are installed
       $gOption{'skip-stop-start'} = 1;
       return 'yes';
    }

    if (can_build_module($name) ne "yes"
	|| get_persistent_answer('None of the pre-built ' . $name . ' modules for '
			      . vmware_product_name() . ' is suitable '
                              . 'for your running kernel.  Do you want this '
                              . 'program to try to build the ' . $name
                              . ' module for your system (you need to have a '
                              . 'C compiler installed on your system)?',
                              'BUILDR_' . $name, 'yesno', 'yes') eq 'no') {
      return 'no';
    }
  }

  if (build_module($name, $mod_dir . '/source') eq 'no') {
    return 'no';
  }
  return 'yes';
}

# Determines whether a solaris driver is already configured using the provided
# driver name and alias (alias may be '' if none is required for this driver).
#  Results: yes if configured, no if not
sub solaris_driver_configured {
  my $driver = shift;
  my $alias = shift;

  if (system(shell_string($gHelper{'grep'}) . ' ' . shell_string($driver)
             . ' /etc/name_to_major > /dev/null 2>&1') == 0) {
    if ($alias eq '' ||
        direct_command('grep ' . $driver . ' /etc/driver_aliases') =~ /$alias/) {
      return 'yes';
    }
  }

  return 'no';
}

sub solaris_module_id {
   my $module=shift;
   my $moduleId=undef;

   # tail +2 skips the header line (why is it not +1?)
   open(MODINFO, 'modinfo | tail +2 |');
   while (<MODINFO>) {
     s/^\ +//;
     my @modinfo = split(/[ ]+/, $_);
     if ($module eq $modinfo[5]) {
        $moduleId=$modinfo[0];
     }
   }
   close(MODINFO);

   return $moduleId;
}

sub solaris_os_version {
  my $solVersion = direct_command(shell_string($gHelper{'uname'}) . ' -r');
  chomp($solVersion);
  my ($major, $minor) = split /\./, $solVersion;
  return ($major, $minor);
}

sub solaris_10_or_greater {
  my $answer = 'no';
  my ($major, $minor) = solaris_os_version();

  if ($major > 5 || ($major == 5 &&  $minor >= 10)) {
    $answer = 'yes';
  }

  return $answer;
}

sub solaris_9_or_greater {
  my $answer = 'no';
  my ($major, $minor) = solaris_os_version();

  if ($major > 5 || ($major == 5 &&  $minor >= 9)) {
    $answer = 'yes';
  }

  return $answer;
}

sub configure_module_solaris {
  my $module = shift;
  my %patch;
  my $dir = db_get_answer('LIBDIR') . '/modules/binary/';
  my ($major, $minor) = solaris_os_version();
  my $osDir;
  my $currentMinor = 10;   # The most recent version we build the drivers for

  if ($major != 5 || $minor < 9) {
    print "VMware Tools for Solaris is only available for Solaris 9 and later.\n";
    return 'no';
  }

  if ($minor < $currentMinor) {
    $osDir = $minor;
  } else {
    $osDir = $currentMinor;
  }

  if ($module eq 'vmmemctl') {
    # Install the corresponding 32-bit driver
    undef %patch;
    install_file($dir . $osDir . '/vmmemctl.conf',
                 '/kernel/drv/vmmemctl.conf', \%patch, $cFlagTimestamp);
    undef %patch;
    install_file($dir . $osDir . '/vmmemctl',
                 '/kernel/drv/vmmemctl', \%patch, $cFlagTimestamp);

    if ($minor > 9) {
      # Also install the 64-bit version
      undef %patch;
      install_file($dir . $osDir . '_64/vmmemctl',
                   '/kernel/drv/amd64/vmmemctl', \%patch, $cFlagTimestamp);
    }

    if (solaris_driver_configured('vmmemctl', '') eq 'no') {
        system(shell_string($gHelper{'add_drv'}) . ' vmmemctl >/dev/null 2>&1');
    }
    db_add_answer('VMMEMCTL_CONFED', 'yes');
    return 'yes';
  }

  if ($module eq 'vmhgfs') {
    my $newMinor;

    # vmhgfs is supported on Solaris 11
    if ($minor == 11) {
       $newMinor = $minor;
    } else {
       $newMinor = $osDir;
    }
    
    undef %patch;
    install_file($dir . $newMinor . '/vmhgfs.conf',
                 '/kernel/drv/vmhgfs.conf', \%patch,
                 $cFlagTimestamp | $cFlagConfig);

    undef %patch;
    install_file($dir . $newMinor . '/vmhgfs',
                 '/kernel/drv/vmhgfs', \%patch, $cFlagTimestamp);

    if ($minor > 9) {
      undef %patch;
      install_file($dir . $newMinor . '_64/vmhgfs',
                   '/kernel/drv/amd64/vmhgfs', \%patch, $cFlagTimestamp);
    }

    if (solaris_driver_configured('vmhgfs', '') eq 'no') {
      system(shell_string($gHelper{'cat'}) . ' ' .
             $dir . $newMinor . '/vmhgfs-devlink.tab ' .
             '>> /etc/devlink.tab');
      system(shell_string($gHelper{'add_drv'}) .
             ' -m \'* 0644 root sys\' vmhgfs >/dev/null 2>&1');
    }

    # configure_vmhgfs() is nice enough to add the VMHGFS_CONFED entry for us
    return 'yes';
  }

  if ($module eq 'vmblock') {
    if ($minor == 9) {
       # We don't currently support vmblock on Solaris 9; that's okay because
       # there's no vmware-user yet either
       return 'no';
    }

    undef %patch;
    install_file($dir . $osDir . '/vmblock',
                 '/kernel/drv/vmblock', \%patch, $cFlagTimestamp);

    if ($minor > 9) {
      undef %patch;
      install_file($dir . $osDir . '_64/vmblock',
                   '/kernel/drv/amd64/vmblock', \%patch, $cFlagTimestamp);
    }

    # build_vmblock() is nice enough to add the VMBLOCK_CONFED entry for us
    return 'yes';
  }

  if ($module eq 'vmxnet') {

    my $pcnId;
    undef %patch;

    # Remove pcn's hold on "pci1022,2000".
    if ($minor == 9) {
      # It seems we actually need to remove the pcn driver on 9 for the
      # binding of vmxnet to pci1022,2000 to take effect; just manually
      # editing /etc/driver_aliases is not enough
      system(shell_string($gHelper{'rem_drv'}) . ' pcn >/dev/null 2>&1');
    } else {
      # Note that it's okay if this fails since the module can't be removed;
      # /etc/driver_aliases will still be updated and the change will take
      # affect on reboot.
      system(shell_string($gHelper{'update_drv'}) . ' -d -i \'"pci1022,2000"\' '
                          . 'pcn >/dev/null 2>&1');
    }

    # Installation of the vmxnet driver is comprised of placing the driver in
    # /kernel/drv and adding it to the system with add_drv(1M).  add_drv(1M)
    # usually handles adding an entry to /etc/driver_aliases, loading the
    # module and invoking devfsadm(1M) to add appropriate symlinks from /dev
    # to /devices.  Here we are only concerned with installing the driver on
    # the system (this should be done regardless of whether the VM currently
    # has a vmxnet device), and save the module loading and /dev symlinks
    # until there actually is a device.  As such, we invoke add_drv(1M) with
    # the -n flag so the driver is not loaded.  Later, in our /etc/init.d
    # script, we look for the vmxnet device and invoke devfsadm(1M) manually
    # ourselves (we don't invoke modload(1M) since the module is automatically
    # loaded when the interface is brought up).  More explicitly:
    #  Here:   $ cp vmxnet /kernel/drv
    #  Here:   $ /usr/sbin/add_drv -n -m '* 0600 root sys' \
    #                              -i '"pci15ad,720" "pci1022,2000"' vmxnet
    #  init.d: $ /usr/sbin/devfsadm -i vmxnet
    install_file($dir . $osDir . '/vmxnet', '/kernel/drv/vmxnet', \%patch, $cFlagTimestamp);

    if ($minor > 9) {
       undef %patch;
       install_file($dir . $osDir . '_64/vmxnet',
                    '/kernel/drv/amd64/vmxnet', \%patch, $cFlagTimestamp);
    }

    # Prevent adding the driver if we already have; prevents errors on two
    # successive invocations of this script
    if (solaris_driver_configured('vmxnet', 'pci15ad,720') eq 'no') {
      system(shell_string($gHelper{'add_drv'}) . ' -n -m \'* 0600 root sys\''
             . ' -i \'"pci15ad,720" "pci1022,2000"\' vmxnet >/dev/null 2>&1');
    }
    migrate_network_files('/etc/hostname.pcn', '/etc/hostname.vmxnet', 'vmx');
    migrate_network_files('/etc/hostname6.pcn', '/etc/hostname6.vmxnet', 'vmx6');
    migrate_network_files('/etc/dhcp.pcn', '/etc/dhcp.vmxnet', 'dhcp');

    db_add_answer('VMXNET_CONFED', 'yes');
    return 'yes';
  }

  if ($module eq 'vmxnet3s') {
    my $result = 'no';

    if ($minor >= 10) {
      # First copy the module to /kernel/drv/ and /kernel/drv/amd64
      undef %patch;
      install_file($dir . $osDir . '/vmxnet3s',
                   '/kernel/drv/vmxnet3s',
                   \%patch, $cFlagTimestamp);
      undef %patch;
      install_file($dir . $osDir . '_64/vmxnet3s',
                   '/kernel/drv/amd64/vmxnet3s',
                   \%patch, $cFlagTimestamp);
      # Check if the module is already configured, otherwise run add_drv
      if (solaris_driver_configured('vmxnet3s', 'pci15ad,7b0') eq 'no') {
        system(shell_string($gHelper{'add_drv'}) . ' -n -m \'* 0600 root sys\''
               . ' -i \'"pci15ad,7b0"\' vmxnet3s >/dev/null 2>&1');
      }
      $result = 'yes'
    }

    db_add_answer('VMXNET3S_CONFED', $result);
    return $result;
  }

  return 'no';
}

#
# Look for all of the network nodes based on the paths passed in and
# copy from the first to the second.  In particular, when moving from
# the pcnet driver on a 32bit machine to the vmxnet driver, the files
# in etc, /etc/hostname.pcnet0, /etc/dhcp.pcn0, ..., need to reflect
# the new vmxnet driver: /etc/hostname.vmxnet0, /etc/dhcp.vmxnet0.
#
sub migrate_network_files {
    my $index = 0;
    my $src_base = shift;
    my $trgt_base = shift;
    my $Id = shift;

    my $src = $src_base . $index;
    while (file_name_exist($src)) {
      my $trgt = $trgt_base  . $index;
      if ( ! -e $trgt) {
        system(shell_string($gHelper{'cp'}) . ' ' . $src . ' ' . $trgt);
        db_add_file($trgt, 0);
        backup_file_to_restore($src, 'SOLARIS_NET_' . $index . '_' . $Id);
      }
      $index++;
      $src = $src_base . $index;
    }
}

sub configure_module_bsd {
  my $module = shift;
  my %patch;
  my $dir = db_get_answer('LIBDIR') . '/modules/binary/FreeBSD';
  my $BSDModPath;
  my $moduleArch;
  my $moduleConfed = 'no';
  my $moduleVersion;

  if ($gSystem{'uts_version'} =~ /FreeBSD [34]/) {
     $BSDModPath = '/modules';
  } else { # Assume 5.x+
     $BSDModPath = '/boot/modules';
  }

  if (is64BitKernel()) {
     $moduleArch = "amd64";
  } else {
     $moduleArch = "i386";
  }

  if ($module eq 'vmmemctl') {
     if ($gSystem{'uts_version'} =~ /FreeBSD 3/) {
	$moduleVersion = '3.2';
     } elsif ($gSystem{'uts_version'} =~ /FreeBSD 4/) {
	$moduleVersion = '4.0';
     } elsif ($gSystem{'uts_version'} =~ /FreeBSD 5.[0-2]/) {
	$moduleVersion = '5.0';
     } elsif ($gSystem{'uts_version'} =~ /FreeBSD 5/) {
	$moduleVersion = '5.3';
     } elsif ($gSystem{'uts_version'} =~ /FreeBSD 6/) {
	$moduleVersion = '6.0';
     } elsif ($gSystem{'uts_version'} =~ /FreeBSD 7/) {
	$moduleVersion = '7.0';
     }
     if (defined($moduleVersion)) {
        undef %patch;
        install_file($dir . $moduleVersion . '-' . $moduleArch . '/vmmemctl.ko',
                     $BSDModPath . '/vmmemctl.ko',
                     \%patch, $cFlagTimestamp);
        $moduleConfed = 'yes';
     }
     db_add_answer('VMMEMCTL_CONFED', $moduleConfed);
  } elsif ($module eq 'vmxnet') {
     if ($gSystem{'uts_version'} =~ /FreeBSD 4\.(\d+)/ &&
	 $1 >= 8) {
	# Version 4.8 included because I did testing in a Free BSD 4.8 VM
	$moduleVersion = '4.9';
     } elsif ($gSystem{'uts_version'} =~ /FreeBSD 5.[0-2]/) {
	$moduleVersion = '5.0';
     } elsif ($gSystem{'uts_version'} =~ /FreeBSD 5/) {
	$moduleVersion = '5.3';
     } elsif ($gSystem{'uts_version'} =~ /FreeBSD 6/) {
	$moduleVersion = '6.0';
     } elsif ($gSystem{'uts_version'} =~ /FreeBSD 7/) {
	$moduleVersion = '7.0';
     }
     if (defined($moduleVersion)) {
        undef %patch;
        install_file($dir . $moduleVersion . '-' . $moduleArch . '/vmxnet.ko',
                     $BSDModPath . '/vmxnet.ko',
                     \%patch, $cFlagTimestamp);
        $moduleConfed = 'yes';

	# Configure autoloading only if vmxnet_load is not mentioned in
	# loader config.  Besides that it fixes /boot/loader.conf growing
	# without limits we now honor administrator decision to disable
	# vmxnet loading.
	# We look for vmxnet_load even in the middle of line, so administrator
	# can just comment out vmxnet_load line instead of setting it to NO.
	if (not block_match('/boot/loader.conf', 'vmxnet_load=')) {
	   block_append('/boot/loader.conf',
			$cMarkerBegin,
			'vmxnet_load="YES"' . "\n",
			$cMarkerEnd);
	}
     }
     db_add_answer('VMXNET_CONFED', $moduleConfed);
  } elsif ($module eq 'vmhgfs') {
    # For now, vmhgfs supported only on FreeBSD 6.0+.
    if ($gSystem{'uts_version'} =~ /FreeBSD 6/) {
       $moduleVersion = '6.0';
    } elsif ($gSystem{'uts_version'} =~ /FreeBSD 7/) {
       $moduleVersion = '7.0';
    }
    if (defined($moduleVersion)) {
       undef %patch;
       install_file($dir . $moduleVersion . '-' . $moduleArch . '/vmhgfs.ko',
		    $BSDModPath . '/vmhgfs.ko',
		    \%patch, $cFlagTimestamp);
       $moduleConfed = 'yes';
    }
    db_add_answer('VMHGFS_CONFED', $moduleConfed);
  } elsif ($module eq 'vmblock') {
    # For now, vmblock supported only on FreeBSD 6.0+.
    if ($gSystem{'uts_version'} =~ /FreeBSD 6/) {
       $moduleVersion = '6.0';
    } elsif ($gSystem{'uts_version'} =~ /FreeBSD 7/) {
       $moduleVersion = '7.0';
    }
    if (defined($moduleVersion)) {
       undef %patch;
       install_file($dir . $moduleVersion . '-' . $moduleArch . '/vmblock.ko',
		    $BSDModPath . '/vmblock.ko',
		    \%patch, $cFlagTimestamp);
       $moduleConfed = 'yes';
    }
    db_add_answer('VMBLOCK_CONFED', $moduleConfed);
  }
  return $moduleConfed;
}

# Create a device name
sub configure_dev {
   # Call the below function with 0 flags to ensure we don't timestamp file
   configure_dev_flags(shift, shift, shift, shift, 0);
}

sub configure_dev_flags {
  my $name = shift;
  my $major = shift;
  my $minor = shift;
  my $chr = shift;
  my $flags = shift;
  my $type;
  my $typename;

  if ($chr == 1) {
    $type = 'c';
    $typename = 'character';
  }
  else {
    $type = 'b';
    $typename = 'block';
  }
  uninstall_file($name);
  if (-e $name) {
    if (-c $name) {
      my @statbuf;

      @statbuf = stat($name);
      if (   defined($statbuf[6])
          && (($statbuf[6] >> 8) == $major)
          && (($statbuf[6] & 0xFF) == $minor)
          && ($chr == 1 && ($statbuf[2] & 0020000) != 0 ||
               $chr == 0 && ($statbuf[2] & 0020000) == 0)) {
         # The device is already correctly configured
         return;
      }
    }

    if (get_answer('This program wanted to create the ' . $typename . ' device '
                   . $name . ' with major number ' . $major . ' and minor '
                   . 'number ' . $minor . ', but there is already a different '
                   . 'kind of file at this location.  Overwrite?', 'yesno',
                   'yes') eq 'no') {
      error('Unable to continue.' . "\n\n");
    }

  }

  if (system('rm -f ' . shell_string($name) . ' && ' . shell_string($gHelper{'mknod'})
             . ' ' . shell_string($name) . ' ' . shell_string($type) . ' '
             . shell_string($major) . ' ' . shell_string($minor))) {
    error('Unable to create the ' . $typename . ' device ' . $name . ' with '
          . 'major number ' . $major . ' and minor number ' . $minor . '.'
          . "\n\n");
  }
  safe_chmod(0600, $name);
  db_add_file($name, $flags);
}

# Determine whether /dev is populated dynamically
sub is_dev_dynamic {
  if (-e '/dev/.devfs' || -e '/dev/.udev.tdb' || -e '/dev/.udevdb' || -e '/dev/.udev') {
    # Either the devfs" or "udev" filesystem is mounted on the "/dev" directory
    return 'yes';
  }

  return 'no';
}

# Configuration related to the monitor
sub configure_mon {
  if (configure_module('vmmon') eq 'no') {
    module_error();
  }

  if (is_dev_dynamic() eq 'yes') {
    # Either the devfs" or "udev" filesystem is mounted on the "/dev" directory,
    # so the "/dev/vmmon" block device file is magically created/removed when the
    # "vmmon" module is loaded/unloaded (was bug 15571 and 72114)
  } else {
    configure_dev('/dev/vmmon', 10, 165, 1);
  }
}

# Configuration related to parallel ports
sub configure_pp {
  my $i;

  if ($gSystem{'version_integer'} < kernel_version_integer(2, 1, 127)) {
    query('You are running Linux version ' . $gSystem{'version_utsclean'}
          . ', and this kernel cannot provide ' . vmware_product_name()
          . ' with Bidirectional Parallel Port support.  A fully-featured '
          . vmware_product_name() . ' requires Linux version 2.1.127 or '
          . 'higher.' . "\n\n" . 'Without this support, '
          . vmware_product_name() . ' will run flawlessly, but will lack the '
          . 'ability to use parallel ports in a bidirectional way.  This '
          . 'means that it is possible that some parallel port devices '
          . '(scanners, dongles, ...) will not work inside a virtual machine.'
          . "\n\n" . 'Hit enter to continue.', '', 0);
    return;
  }

  if ($gSystem{'version_integer'} <= kernel_version_integer(2, 3, 9)) {
    # Those kernels don't support ppdev.  We need to supply our vmppuser module

    print wrap('Making sure that both the parport and parport_pc kernel '
               . 'services are available.' . "\n\n", 0);

    # The vmppuser module relies on the parport modules.  Let's
    # make sure it is loaded before beginning our tests
    if (direct_command(shell_string($gHelper{'grep'}) . '  '
                       . shell_string(' parport_release[^' . "\t" . ']*$')
                       . ' /proc/ksyms') eq '') {
      # This comment fixes emacs's broken syntax highlighting
      # parport support is not built in the kernel
      if (!kmod_load_by_name('parport', 0)) {
        query('Unable to load the parport module that is required by the '
              . 'vmppuser module.  You may want to load it manually before '
              . 're-running this program.' . "\n\n" . 'Without this support, '
              . vmware_product_name() . ' will run flawlessly, but will lack '
              . 'the ability to use parallel ports in a bidirectional way.  '
              . 'This means that it is possible that some parallel port '
              . 'devices (scanners, dongles, ...) will not work inside a '
              . 'virtual machine.' . "\n\n"
              . 'Hit enter to continue.', '', 0);
        return;
      }
    }

    # The vmppuser module relies on the parport_pc modules.  Let's
    # make sure it is loaded before beginning our tests
    if (direct_command(shell_string($gHelper{'grep'}) . '  '
                       . shell_string(' parport_pc_[^' . "\t" . ']*$')
                       . ' /proc/ksyms') eq '') {
      # This comment fixes emacs's broken syntax highlighting
      # parport_pc support is not built in the kernel
      if (!kmod_load_by_name('parport_pc', 0)) {
        query('Unable to load the parport_pc module that is required by the '
              . 'vmppuser module.  You may want to load it manually before '
              . 're-running this program.' . "\n\n" . 'Without this support, '
              . vmware_product_name() . ' will run flawlessly, but will lack '
              . 'the ability to use parallel ports in a bidirectional way.  '
              . 'This means that it is possible that some parallel port '
              . 'devices (scanners, dongles, ...) will not work inside a '
              . 'virtual machine.' . "\n\n" . 'Hit enter to continue.', '', 0);
        return;
      }
    }

    if (configure_module('vmppuser') eq 'no') {
      module_error();
    }

    # Try to unload the modules.  Failure is allowed because some other
    # process could be using them.
    kmod_unload('parport_pc', 1);
    kmod_unload('parport', 1);
  }

  # The parport numbering scheme in 2.2.X is confusing:
  # Because devices can be daisy-chained on a port, the first port
  # (/proc/parport/0) is /dev/parport0, but the second one (/proc/parport/1)
  # is /dev/parport16 (not /dev/parport1), and so on...

  # This message is wrong.  I have found no evidence for this.
  # On all the linux machines that I've looked at /dev/parport1 is the 2nd port
  # That's my story and I'm sticking to it  - DavidE

  if (is_dev_dynamic() eq 'no') {
    for ($i = 0; $i < 4; $i++) {
      configure_dev_flags('/dev/parport' . $i, 99, $i, 1, 0x1);
    }
  }
}

# Configuration of the vmmemctl tools device
sub configure_vmmemctl {
  my $result;
  if (vmware_product() eq 'tools-for-freebsd') {
    $result = configure_module_bsd('vmmemctl');
  } elsif (vmware_product() eq 'tools-for-solaris') {
    $result = configure_module_solaris('vmmemctl');
  } else {
    $result = configure_module('vmmemctl');
    if ($result eq 'no') {
      query('The memory manager driver (vmmemctl module) is used by '
            . 'VMware host software to efficiently reclaim memory from a '
            . 'virtual machine.' . "\n"
            . 'If the driver is not available, VMware host software may '
            . 'instead need to swap guest memory to disk, which may reduce '
            . 'performance.' . "\n"
            . 'The rest of the software provided by '
            . vmware_product_name()
            . ' is designed to work independently of '
            . 'this feature.' . "\n"
            . 'If you want the memory management feature,'
            . $cModulesBuildEnv
            . "\n", ' Press Enter key to continue ', 0);
    }
    if ($result eq 'yes' && vmware_product() eq 'tools-for-linux') {
      set_manifest_component('vmmemctl', 'TRUE');
    }
    db_add_answer('VMMEMCTL_CONFED', $result);
  }
}

# Configuration of the vmhgfs tools device
sub configure_vmhgfs {
  # vmhgfs is supported only since 2.4.0 Linux kernels, Solaris 9, and FreeBSD 6.0
  if (   (   vmware_product() eq 'tools-for-linux'
          && $gSystem{'uts_release'} =~ /^(\d+)\.(\d+)/
          && $1 * 1000 + $2 >= 2004)
      || (   vmware_product() eq 'tools-for-solaris'
          && solaris_9_or_greater())) {
    my $result;
    if (vmware_product() eq 'tools-for-linux') {
      if (create_dir('/mnt/hgfs', $cFlagDirectoryMark | $cFlagFailureOK)
          != $cCreateDirFailure) {
        $result = configure_module('vmhgfs');
      } else {
        $result = 'no';
        my $msg = "Could not create the '/mnt/hgfs' directory.\n";
        print wrap($msg, 0);
      }

      if ($result eq 'yes') {
        if (file_name_exist('/etc/updatedb.conf')) {
          # If vmhgfs was successfully configured, modify /etc/updatedb.conf so
          # that filesystems of type 'vmhgfs' are excluded from its crawling.
          my $updatedb_conf_file = '/etc/updatedb.conf';
          backup_file_to_restore($updatedb_conf_file, 'UPDATEDB_CONF');
          configure_updatedb_dot_conf($updatedb_conf_file,
				      $updatedb_conf_file . $cBackupExtension);
        }

        # Now that the vmhgfs filesystem module is loaded, add a default
        # mountpoint entry in /etc/fstab.
        my $file = '/etc/fstab';
        my $mnt_str = '.host:/                 /mnt/hgfs               '
                       . 'vmhgfs  defaults,ttl=5     0 0' . "\n";
        if (!block_match($file, '.host:/ .* vmhgfs')) {
          block_append($file, $cMarkerBegin, $mnt_str, $cMarkerEnd);
          if (is_selinux_enabled()) {
             system("/sbin/restorecon " . $file);
          }
        }
      }
    } elsif (vmware_product() eq 'tools-for-solaris') {
      # It's common to mount over /mnt in Solaris so we use /hgfs
      if (create_dir('/hgfs', $cFlagDirectoryMark | $cFlagFailureOK)
          != $cCreateDirFailure) {
        symlink_if_needed('/hgfs', '/mnt/hgfs');
        $result = configure_module_solaris('vmhgfs');
      } else {
        $result = 'no';
        my $msg = "Could not create the '/hgfs' directory.\n";
        print wrap($msg, 0);
      }
    } elsif (vmware_product() eq 'tools-for-freebsd'
	&& get_persistent_answer('[EXPERIMENTAL] The VMware Host-Guest Filesystem'
				 . ' allows using shared folders inside the '
				 . 'guest OS. Do you wish to enable this feature?',
				 'XPRMNTL_VMHGFS_FREEBSD', 'yesno', 'no')
	     eq 'yes') {
	$result = configure_module_bsd('vmhgfs');
	if ($result eq 'yes') {
            if (create_dir('/mnt/hgfs', $cFlagDirectoryMark | $cFlagFailureOK)
                != $cCreateDirFailure) {
	       db_add_answer('HGFS_MOUNT_POINT', '/mnt/hgfs');

	       # Now that the vmhgfs filesystem module is loaded, add a default
	       # mountpoint entry in /etc/fstab.  Since the backup operation
	       # removes the original file, copy the backup back to the original
	       # file so the default mount point may be added.
	       my $file = '/etc/fstab';
	       my $mnt_str = '.host:/                 /mnt/hgfs               '
		   . 'vmhgfs  rw     0 0' . "\n";
	       backup_file_to_restore($file, 'OLD_FSTAB');
	       system(shell_string($gHelper{'cp'}) . ' ' . $file . $cBackupExtension
		   . ' ' .  $file);
	       block_append($file, $cMarkerBegin, $mnt_str, $cMarkerEnd);
            } else {
                $result = 'no';
                my $msg = "Could not create the '/mnt/hgfs' directory.\n";
                print wrap($msg, 0);
            }
	}
    } else {
      $result = 'no';
    }
    if ($result eq 'no') {
      my $msg = 'The filesystem driver (vmhgfs module) is used only for the '
              . 'shared folder feature. The rest of the software provided by '
              .  vmware_product_name() . ' is designed to work independently of '
              . 'this feature.' . "\n\n" . 'If you wish to have the shared folders '
              . 'feature,' . $cModulesBuildEnv . "\n";
      query ($msg, ' Press Enter key to continue ', 0);
    }
    if ($result eq 'yes' && vmware_product() eq 'tools-for-linux') {
      set_manifest_component('vmhgfs', 'TRUE');
    }
    db_add_answer('VMHGFS_CONFED', $result);
  }
}

# Configuration of the vmxnet3 ethernet driver
sub configure_vmxnet3 {
    my $result = 'no';
    # vmxnet3 is supported only since 2.6.0 Linux kernels
    if ($gSystem{'version_integer'} < kernel_version_integer(2, 6, 0)) {
       query('You are running Linux version ' . $gSystem{'version_utsclean'}
             . '.  The advanced network device driver (vmxnet3 module) is '
             . 'only available for 2.6.0 and later kernels.'
             . "\n", ' Press Enter key to continue ', 0);
    } else {
       # Add 'vmxnet' module to initrd image. When pcnet32 is morphed to vmxnet,
       # without the 'vmxnet' module in the initrd image, the pcnet32 module will
       # get loaded from the initrd image at boot and take control of the
       # morphable Lance NIC.
       $result = configure_module('vmxnet3');
       if ($result eq 'no') {
          query('The advanced network device driver (vmxnet3 module) is used only for '
                . 'our advanced networking interface. '
                . 'The rest of the software provided by '
                . vmware_product_name()
                . ' is designed to work independently of '
                . 'this feature.' . "\n"
                . 'If you wish to have the advanced network driver enabled,'
                . $cModulesBuildEnv
                . "\n", ' Press Enter key to continue ', 0);
       }
    }
    if ($result eq 'yes' && vmware_product() eq 'tools-for-linux') {
      set_manifest_component('vmxnet3', 'TRUE');
    }
    db_add_answer('VMXNET3_CONFED', $result);
}

# Configuration of the vmdesched (Timer Sponge) feature
sub configure_vmdesched {
  # vmdesched only works on Linux UP kernels >= 2.4
  if (vmware_product() eq 'tools-for-linux' &&
      $gSystem{'uts_release'} =~ /^(\d+)\.(\d+)/ &&
      $1 * 1000 + $2 >= 2004 && ($gSystem{'smp'} eq 'no')) {

    if (get_persistent_answer(
                   '[EXPERIMENTAL] The VMware CPU Time Accounting daemon '
                   . '(vmware-vmdesched) can help increase the accuracy of CPU '
                   . 'time accounting performed by the guest operating system. '
                   . 'Please refer to the VMware Knowledge Base for more '
                   . 'details on this capability. Do you wish to enable '
                   . 'this feature?', 'XPRMNTL_VMDESCHED',
                   'yesno', 'no') eq 'yes') {

      my $result = configure_module('vmdesched');
      if ($result eq 'no') {
	query('The CPU time accounting daemon is '
	      . 'used only for correcting CPU time '
	      . 'accounting inaccuracies in the guest. '
	      . 'The rest of the software provided by '
	      . vmware_product_name()
	      . ' is designed to work independently of '
	      . 'this feature.' . "\n"
	      . 'If you wish to have the vmdesched feature,'
	      . $cModulesBuildEnv
	      . "\n", ' Press Enter key to continue ', 0);
      }
      if ($result eq 'yes' && vmware_product() eq 'tools-for-linux') {
        set_manifest_component('vmdesched', 'TRUE');
      }
      db_add_answer('VMDESCHED_CONFED', $result);
    }
  }
}

sub configure_vmci {
  my $result = 'no';

  if (!(isDesktopProduct() || isServerProduct() ||
      vmware_product() eq 'tools-for-linux' )) {
    return undef;
  }

  $result = configure_module('vmci');
  if ($result eq 'no') {
    query('The communication service is used in addition to the '
          . 'standard communication between the guest and the host.  '
          . 'The rest of the software provided by ' . vmware_product_name()
          . ' is designed to work independently of this feature.' . "\n"
          . 'If you wish to have the VMCI feature,'
          . $cModulesBuildEnv
          . "\n", ' Press Enter key to continue ', 0);
  }
  if ($result eq 'yes' && vmware_product() eq 'tools-for-linux') {
    set_manifest_component('vmci', 'TRUE');
  }
  db_add_answer('VMCI_CONFED', $result);
}

sub configure_vsock {
   my $result = 'no';

   if (defined(db_get_answer_if_exists('VMCI_CONFED')) &&
      db_get_answer('VMCI_CONFED') ne 'yes') {
      return 1;
   }

   # vsock needs the vmci module loaded first.
   kmod_load_by_path($cKernelModuleDir . '/' . $gSystem{'uts_release'} . '/misc/vmci.o', 1, 0, 0);
   $result = configure_module('vsock');
   if ($result eq 'no') {
      my $msg = "The VM communication interface socket family is used in conjunction "
              . "with the VM communication interface to provide a new communication "
              . "path among guests and host.  The rest of this software "
              . "provided by " . vmware_product_name() . " is designed to work "
              . "independently of this feature.  If you wish to have the VSOCK "
              . "feature " . $cModulesBuildEnv . "\n";
      query($msg, " Press the Enter key to continue.", 0);
   }
   kmod_unload('vmci');
   if ($result eq 'yes' && vmware_product() eq 'tools-for-linux') {
      set_manifest_component('vsock', 'TRUE');
   }
   db_add_answer('VSOCK_CONFED', $result);
}

sub configure_vmsync {
   my $result = 'no';

   if ($gSystem{'version_integer'} >= kernel_version_integer(2, 6, 6)) {
      if (get_persistent_answer(
                   '[EXPERIMENTAL] The VMware FileSystem Sync Driver '
                   . '(vmsync) is a new feature that creates backups '
                   . 'of virtual machines. '
                   . 'Please refer to the VMware Knowledge Base for more '
                   . 'details on this capability. Do you wish to enable '
                   . 'this feature?', 'XPRMNTL_VMSYNC',
                   'yesno', 'no') eq 'yes') {
         $result = configure_module('vmsync');
         if ($result eq 'no') {
            query('The file system sync driver (vmsync) is only used to create safe '
               . 'backups of the virtual machine. The rest of the software '
               . 'provided by ' . vmware_product_name()
               . ' is designed to work independently of this feature.' . "\n"
               . 'If you wish to have the vmsync feature,'
               . $cModulesBuildEnv
               . "\n", ' Press Enter key to continue ', 0);
         }
         if ($result eq 'yes' && vmware_product() eq 'tools-for-linux') {
            set_manifest_component('vmsync', 'TRUE');
         }
      }
   }
   db_add_answer('VMSYNC_CONFED', $result);
}


#
# This function is taken from the old tools installer.
# The first argument is a complete path to a file which will be read and
# overwritten with the result.
# The second argument will be only read and should be the system file present
# before configuration.
#
sub configure_modules_dot_conf {
  my ($newModulesConf, $systemModulesConf, $ethAliases, $sounddrv, $soundopt) = @_;
  my $inline;
  my $soundfound;
  my %emittedAliases = ();

  if (not open(SYSMODCONF, "<$systemModulesConf")) {
    error('Unable to open the file "' . $systemModulesConf . '".' . "\n\n");
  }

  if (not open(NEWMODCONF, ">$newModulesConf")) {
    error('Unable to open the file "' . $newModulesConf . '".' . "\n\n");
  }

  # Look for matches and selectively replace drivers
  $soundfound = 0;
  while (defined($inline = <SYSMODCONF>)) {
    if ($inline =~ /^\s*(\w+)\s+(\w+)/) {
      my ($cmd, $val) = ($1, $2);

      if ($cmd eq 'alias') {
	if ($val eq 'sound') {
	  $inline = sprintf 'alias sound %s' . "\n", $sounddrv;
	  $inline .= $soundopt;
	  $soundfound = 1;
	} elsif ($val eq 'char-major-14') {
	  $inline = sprintf 'alias char-major-14 %s' . "\n", $sounddrv;
          $inline .= $soundopt;
	  $soundfound = 1;
	} elsif (defined($ethAliases->{$val})) {
	  $inline = 'alias ' . $val . ' ' . $ethAliases->{$val} . "\n";
	  $emittedAliases{$val} = 1;
	}
      } elsif ($cmd eq 'options') {
	if ($soundfound and $inline eq $soundopt) {
	  # Silently remove sound options line if we already generated identical one
	  $inline = '';
	} elsif ($val eq $sounddrv) {
	  $inline = '# Commented out by ' . vmware_product_name() . "\n" . '# ' . $inline;
	}
      }
    }
    print NEWMODCONF $inline;
  }

  my @output;

  # Then pick up any drivers we haven't got yet.
  foreach my $key (sort keys %$ethAliases) {
    if (not defined($emittedAliases{$key})) {
      push @output, sprintf("alias %s %s\n", $key, $ethAliases->{$key});
      $emittedAliases{$key} = 1;
    }
  }
  if (keys(%emittedAliases) > 0) {
      push @output, "probeall $cNICAlias vmxnet pcnet32\n";
  }
  if ($soundfound == 0) {
    push @output, sprintf("alias char-major-14 %s\n", $sounddrv);
    push @output, $soundopt;
  }
  if (scalar @output) {
    print NEWMODCONF "# Added by " . vmware_product_name() . "\n";
    print NEWMODCONF join('', @output);
  }
  close (SYSMODCONF);
  close (NEWMODCONF);
}

#
# This is for module-init-tools (2.6 kernels)
# The first argument is a complete path to a file which will be read and
# overwritten with the result.
# The second argument will be only read and should be the system file present
# before configuration.
#
sub configure_modprobe_dot_conf {
  my ($newModprobeConf, $systemModprobeConf, $ethAliases, $sounddrv, $soundopt)
      = @_;
  my $inline;
  my $soundfound;
  my %emittedAliases = ();

  if (not open(SYSMODCONF, "<$systemModprobeConf")) {
    error('Unable to open the file "' . $systemModprobeConf . '".' . "\n\n");
  }

  if (not open(NEWMODCONF, ">$newModprobeConf")) {
    error('Unable to open the file "' . $newModprobeConf . '".' . "\n\n");
  }

  # Look for matches and selectively replace drivers
  $soundfound = 0;
  while (defined($inline = <SYSMODCONF>)) {
    if ($inline =~ /^\s*(\w+)\s+(\w+)/) {
      my ($cmd, $val) = ($1, $2);

      if ($cmd eq 'alias') {
	if ($val eq 'sound') {
	  $inline = sprintf 'alias sound %s' . "\n", $sounddrv;
	  $inline .= $soundopt;
	  $soundfound = 1;
	} elsif ($val eq 'char-major-14') {
	  $inline = sprintf 'alias char-major-14 %s' . "\n", $sounddrv;
          $inline .= $soundopt;
	  $soundfound = 1;
	} elsif (defined($ethAliases->{$val})) {
	    $inline = 'alias ' . $val . ' ' . $ethAliases->{$val} . "\n";
	    $emittedAliases{$val} = 1;
	}
      } elsif ($cmd eq 'options') {
	if ($soundfound and $inline eq $soundopt) {
	  # Silently remove sound options line if we already generated identical one
	  $inline = '';
	} elsif ($val eq $sounddrv) {
	  $inline = '# Commented out by ' . vmware_product_name() . "\n" . '# ' . $inline;
        }
      }
    }
    print NEWMODCONF $inline;
  }

  my @output;

  if (vmware_product() eq 'tools-for-linux'
      && $gSystem{'version_integer'} < kernel_version_integer(2, 6, 22) ) {
    push @output, "install pciehp /sbin/modprobe -q --ignore-install acpiphp; /bin/true\n"
  }
  push @output, 'install pcnet32 /sbin/modprobe -q --ignore-install vmxnet;'
                . '/sbin/modprobe -q --ignore-install pcnet32 $CMDLINE_OPTS;'
                . "/bin/true\n";

  if ($soundfound == 0) {
    push @output, sprintf("alias char-major-14 %s\n", $sounddrv);
    push @output, $soundopt;
  }
  if (scalar @output) {
    print NEWMODCONF "# Added by " . vmware_product_name() . "\n";
    print NEWMODCONF join('', @output);
  }
  close (SYSMODCONF);
  close (NEWMODCONF);
}

#
# This is for module-init-tools (2.6 kernels) and hotplug
# The first argument is a complete path to a file which will be read and
# overwritten with the result.
# The second argument will be only read and should be the system file present
# before configuration.
#
sub configure_pci_dot_handmap {
  my ($newPciHandmap, $systemPciHandmap)
      = @_;
  my $inline;
  my $emittedVmnics = 0;
  my $emittedVmxnet = 0;

  if (not open(SYSHANDMAP, "<$systemPciHandmap")) {
    error('Unable to open the file "' . $systemPciHandmap . '".' . "\n\n");
  }

  if (not open(NEWHANDMAP, ">$newPciHandmap")) {
    error('Unable to open the file "' . $newPciHandmap . '".' . "\n\n");
  }

  # Look for matches and selectively replace drivers
  while (defined($inline = <SYSHANDMAP>)) {
    if ($inline =~ /^\s*(\w+)\s+(\w+)/) {
      my ($cmd, $val) = ($1, $2);

      if ($cmd eq 'vmxnet') {
	  $inline = 'vmxnet\t\t0x000015ad 0x00000720 ' .
	      '0xffffffff 0xffffffff 0x00000000 0x00000000 0x0' . "\n";
	  $emittedVmxnet = 1;
      } elsif ($cmd eq 'vmnics') {
	  $inline = 'vmnics\t\t0x00001022 0x00002000 ' .
	      '0xffffffff 0xffffffff 0x00000000 0x00000000 0x0' . "\n";
	  $emittedVmnics = 1;
      }
    }
    print NEWHANDMAP $inline;
  }

  my @output;

  if ($emittedVmxnet == 0 ) {
      push @output, "vmxnet\t\t0x000015ad 0x00000720 0xffffffff 0xffffffff 0x00000000 0x00000000 0x0\n";
  }
  if ($emittedVmnics ==  0) {
      push @output, "vmnics\t\t0x00001022 0x00002000 0xffffffff 0xffffffff 0x00000000 0x00000000 0x0\n";
  }
  if (scalar @output) {
    print NEWHANDMAP "# Added by " . vmware_product_name() . "\n";
    print NEWHANDMAP join('', @output);
}
  close (SYSHANDMAP);
  close (NEWHANDMAP);
}


#
# The first argument is a complete path to a file which will be read and
# overwritten with the result.
# The second argument will be only read and should be the system file present
# before configuration.
#
sub configure_updatedb_dot_conf {
  my ($newUpdateDbConf, $systemUpdateDbConf) = @_;
  my $inline;

  if (not open(SYSUPDBCONF, "<$systemUpdateDbConf")) {
    error('Unable to open the file "' . $systemUpdateDbConf . '".' . "\n\n");
  }

  if (not open(NEWUPDBCONF, ">$newUpdateDbConf")) {
    error('Unable to open the file "' . $newUpdateDbConf . '".' . "\n\n");
  }

  # Copy each line from the old updatedb.conf file to the new one. If we find
  # the PRUNEFS definition, modify it slightly before copying it over.
  while (defined($inline = <SYSUPDBCONF>)) {

    # We expect to find the variable PRUNEFS followed with an equals sign
    # (perhaps with some whitespace sprinkled in), followed by a doubly quoted
    # list of filesystems, each represented by a word. For example:
    #
    # PRUNEFS = "afs nfs iso9660 cifs"
    #
    # And of course, if for some reason vmhgfs is already in the list of pruned
    # filesystems, don't add it again.
    if ($inline =~ /^(\s*PRUNEFS\s*=\s*)\"(.*)\"(.*)$/ &&
	$2 !~ /vmhgfs/) {
      my ($prefix, $fslist, $suffix) = ($1, $2, $3);

      $fslist .= " vmhgfs";
      $inline = $prefix . "\"$fslist\"" . $suffix . "\n";
    }
    print NEWUPDBCONF $inline;
  }

  close (SYSUPDBCONF);
  close (NEWUPDBCONF);
}


# Enumerate devices we are interested on. Returns array of 3 elements:
# 1. number of vmxnet adapters
# 2. number of pcnet32 adapters
# 3. number of es1371 adapters
# We do not attempt to find ISA vlance or sb.
sub get_devices_list {
  my ($vmxnet, $pcnet32, $es1371) = (0, 0, 0);
  my $line;

  # Per bug #41349 the lspci version provided by Mandrake has unwanted
  # interaction with the sound device. The alternate lspcidrake tool
  # works fine but it unnecessarily complicates the configuration script.
  # Using /proc/bus/pci/devices instead of the output of lspci/lspcidrake
  # is consistent on all supported distributions.
  if (open PCI, '</proc/bus/pci/devices') {
    while (defined($line = <PCI>)) {
      $line = lc($line);
      if ($line =~ /^[0-9a-f]*\t([0-9a-f]*)\t/) {
        my $dev = $1;
        if ($dev eq '10222000') {
          $pcnet32++;
        } elsif ($dev eq '15ad0720') {
          $vmxnet++;
        } elsif ($dev eq '12741371') {
          $es1371++;
        }
      }
    }
    close PCI;
  }
  return ($vmxnet, $pcnet32, $es1371);
}

# Configuration of drivers for PCI devices
sub write_module_config {
  my ($vmxnet, $pcnet32, $es1371) = get_devices_list();
  my %ethernet = ();
  my $ethidx = 0;
  my $sounddrv;
  my $soundopt;
  my $modprobe_file = '';
  my $result;

  if (vmware_product() ne 'tools-for-linux') {
    return;
  }

  if ($es1371) {
    $sounddrv = 'es1371';
    # No sound options for es1371
    $soundopt = '';
  } else {
    $sounddrv = 'sb';
    # Assume soundblaster if no es1371 found
    if ($gSystem{'version_integer'} < kernel_version_integer(2, 2, 0)) {
      $soundopt = 'options sb io=0x220 irq=5 dma=1,5' . "\n";
    } else {
      $soundopt = 'options sb io=0x220 irq=5 dma=1 dma16=5 mpu_io=0x330' . "\n";
    }
  }

  # modprobe looks for module info first in modprobe.conf and then
  # in modprobe.d/<vmware-tools>.  However, SLES9 includes a new
  # wrinkle: the file modprobe.conf.local.  That gets included into
  # modprobe.conf and SLES9 wants user modified entries in that
  # local file.
  if (file_name_exist('/etc/modprobe.conf.local')) {
    $modprobe_file = '/etc/modprobe.conf.local';
  } elsif (file_name_exist('/etc/modprobe.conf')) {
    $modprobe_file = '/etc/modprobe.conf';
  } elsif (file_name_exist('/etc/modprobe.d')) {
    $modprobe_file = '/etc/modprobe.d/vmware-tools';
  } elsif (file_name_exist('/etc/modules.conf')) {
    $modprobe_file = '/etc/modules.conf';
  } elsif (file_name_exist('/etc/conf.modules')) {
    $modprobe_file = '/etc/conf.modules';
  }

  if (($modprobe_file eq '/etc/modprobe.conf.local') ||
      ($modprobe_file eq '/etc/modprobe.conf')) {
    # Save the files we have to change.
    backup_file_to_restore($modprobe_file, 'MODPROBE_CONF');
    configure_modprobe_dot_conf($modprobe_file,
                                  $modprobe_file . $cBackupExtension,
			          \%ethernet, $sounddrv, $soundopt);
  } elsif ($modprobe_file eq '/etc/modprobe.d/vmware-tools') {
    my @netopt = ('install pcnet32 /sbin/modprobe -q --ignore-install vmxnet; ' .
                 '/sbin/modprobe --ignore-install pcnet32 $CMDLINE_OPTS' . "\n");
    if (vmware_product() eq 'tools-for-linux'
          && $gSystem{'version_integer'} < kernel_version_integer(2, 6, 22) ) {
      push(@netopt, 'install pciehp /sbin/modprobe -q --ignore-install acpiphp;'
                  . "/bin/true\n");
    }
    if (not open(NEWMODCONF, ">$modprobe_file")) {
      error('Unable to open the file "' . $modprobe_file . '".' . "\n\n");
    }

    print NEWMODCONF "# Created by " . vmware_product_name() . "\n";
    print NEWMODCONF join('', @netopt);
    # No need to add soundopt. The current distros can
    # detect well the WS 5 hardware.
    close(NEWMODCONF);

    db_add_file($modprobe_file, 0x0);
  } elsif (file_name_exist('/etc/conf.modules') ||
           file_name_exist('/etc/modules.conf')) {
    # Save the files we have to change.
    my $modules_file = file_name_exist('/etc/conf.modules') ?
	'/etc/conf.modules' : '/etc/modules.conf';

    backup_file_to_restore($modules_file, 'MODULES_CONF');

    configure_modules_dot_conf($modules_file,
			       $modules_file . $cBackupExtension,
			       \%ethernet, $sounddrv, $soundopt);

    db_add_file($modprobe_file, 0x0);
  }
  if (file_name_exist('/etc/hotplug/pci.handmap')) {
      my $handmap_file = '/etc/hotplug/pci.handmap';
      backup_file_to_restore($handmap_file, 'PCI_HANDMAP');
      configure_pci_dot_handmap($handmap_file,
				$handmap_file . $cBackupExtension);
  }

  $result = configure_module('vmxnet');
  if ($result eq 'no') {
    query('The fast network device driver (vmxnet module) is used only for '
	  . 'our fast networking interface. '
	  . 'The rest of the software provided by '
	  . vmware_product_name()
	  . ' is designed to work independently of '
	  . 'this feature.' . "\n"
	  . 'If you wish to have the fast network driver enabled,'
	  . $cModulesBuildEnv
	  . "\n", ' Press Enter key to continue ', 0);
    $vmxnet = 0;
  } else {
    my $initmodfile = '/etc/initramfs-tools/modules';
    if ( -f $initmodfile )
    {
      backup_file_to_restore($initmodfile, 'INITRAMFS_MODULES');
      system(shell_string($gHelper{'cp'}). ' ' . $initmodfile . $cBackupExtension . ' ' .
               $initmodfile);
      if (not block_match($initmodfile, '^vmxnet$')) {
        block_append($initmodfile,
                     $cMarkerBegin,
                     "vmxnet\n",
                     $cMarkerEnd);
      }
      system(shell_string($gHelper{'depmod'}) . ' -a');
      system('update-initramfs','-u','-k','all');
    }
    my $i;
    my $aliascount = $vmxnet;
    if ($pcnet32) {
      $aliascount += $pcnet32;
    }
    for ($i = 0; $i < $aliascount ; $i++) {
      $ethernet{'eth' . $ethidx} = $cNICAlias;
      $ethidx++;
    }
  }
  if ($result eq 'yes' && vmware_product() eq 'tools-for-linux') {
    set_manifest_component('vmxnet', 'TRUE');
  }
  db_add_answer('VMXNET_CONFED', $result);
}

# There is no /usr/X11R6 directory for X window in some distribution like
# Fedora 5. Instead binary files are put in /usr/bin. Please refer to bug
# 86254.

sub xserver_bin {
  my $path;

  if (vmware_product() eq 'tools-for-solaris' && -e '/usr/X11/bin') {
    return '/usr/X11/bin';
  }

  # Search PATH for Xorg then X, in case it is somewhere else.
  $path = internal_which('Xorg');
  if ($path eq '') {
    $path = internal_which('X')
  }
  if ($path ne '') {
    # Only return path, so remove file name.
    return internal_dirname($path);
  }

  if (-e '/usr/X11R6/bin') {
     return '/usr/X11R6/bin';
  }

  return '';
}

sub xserver_xorg {
  return xserver_bin() . '/Xorg';
}

sub xserver4 {
  return xserver_bin() . '/XFree86';
}

sub xserver3 {
  return xserver_bin() . '/XF86_VMware';
}

sub xconfig_file_abs_path {
  my $xconfig_path = shift;
  my $xconfig_file_name = shift;
  return $xconfig_path . '/' . $xconfig_file_name;
}

#
# path_compare(dir, path1, path2)
#
# Compare the two paths, and return true if they are identical
# Evaluate the paths with respect to the passed in directory
#
sub path_compare {
  my ($dir, $path1, $path2) = @_;

  # Prepend directory for relative paths
  $path1 =~ s|^([^/])|$dir/$1|;
  $path2 =~ s|^([^/])|$dir/$1|;

  # Squash out ..'s in paths
  while ($path1 =~ /\/.*\/\.\.\//) {
    $path1 =~ s|/[^/]*/\.\./|/|;
  }

  while ($path2 =~ /\/.*\/\.\.\//) {
    $path2 =~ s|/[^/]*/\.\./|/|;
  }

  # Squash out .'s in paths
  while ($path1 =~ /\/\.\//) {
    $path1 =~ s|/\./|/|;
  }

  while ($path2 =~ /\/\.\//) {
    $path2 =~ s|/\./|/|;
  }

  # Squash out //'s in paths
  while ($path1 =~ /\/\//) {
    $path1 =~ s|//|/|;
  }

  while ($path2 =~ /\/\//) {
    $path2 =~ s|//|/|;
  }

  if ($path1 eq $path2) {
    return 'yes';
  } else {
    return 'no';
  }
}

# check_link
# Checks that a given link is pointing to the given file.
sub check_link {
  my $file = shift;
  my $link = shift;
  my $linkDest;
  my $dirname;
  $linkDest = readlink($link);
  if (!defined $linkDest) {
    return 'no';
  }
  $dirname = internal_dirname($link);
  return path_compare($dirname, $linkDest, $file);
}

# Install one link, symbolic or hard
sub install_link {
   my $symbolic = shift;
   my $to = shift;
   my $name = shift;

   uninstall_file($name);
   if (file_check_exist($name)) {
      return;
   }
   # The file could be a link to another location.  Remove it
   unlink($name);
   if ($symbolic) {
      if (not symlink($to, $name)) {
         return 'no';
      }
   } else {
      if (not link($to, $name)) {
         return 'no';
      }
   }
   db_add_file($name, 0);
   return 'yes';
}

sub install_symlink {
   my $to = shift;
   my $from = shift;

   if (install_link(1, $to, $from) eq 'no') {
         error('Unable to create symlink "' . $from . '" pointing to file "'
               . $to . '".' . "\n\n");
   }
}

sub install_hardlink {
   my $to = shift;
   my $from = shift;

   return install_link(0, $to, $from);
}

my $gLinkCount = 0;
sub symlink_if_needed {
  my $file = shift;
  my $link = shift;
  if (file_name_exist($file)) {
    if (-l $link && check_link($file, $link) eq 'yes') {
      return;
    }
    $gLinkCount = $gLinkCount + 1;
    backup_file_to_restore($link, 'LINK_' . $gLinkCount);
    install_symlink($file, $link);
  }
}

sub set_uid_X_server {
  my $x_server_file = shift;
  if (!-u $x_server_file) {
    safe_chmod(04711, $x_server_file);
  }
}

sub getXorgVersionAll {

   my $packedVersion = direct_command(shell_string(xserver_xorg()) . ' -version 2>&1');
   if ($packedVersion =~ /X Protocol Version 11.* Release (\d+\.\d+)/) {
      $packedVersion = $1 ? $1 : '0.0.0';
   } elsif ($packedVersion =~ /X Server (\d+\.\d+\.?\d*)/) {
         $packedVersion = $1 ? $1 : '0.0.0';
   }
   my ($xorgMajorVer, $xorgMinorVer, $xorgSubVer) = split_X_version($packedVersion);
   if (!defined($xorgSubVer)) {
      $xorgSubVer = 0;
   }

   # The 1.3.0 release of the X-server had a little goof where it would say
   # X Window System, Release 1.3.0. so $major == 1 and $minor == 3. But
   # truly, it's a standalone X server release that came out after Xorg 7.2.
   # Similarly, Release 1.4.0 came out as part of Xorg 7.3.
   #
   # See bug#185281 for all the deets.
  if ($xorgMajorVer == 1) {
    $packedVersion = "7." . ($xorgMinorVer - 1) . "." . $xorgSubVer;
  }

  return $packedVersion;
}

sub split_X_version {

  my $xversionAll = shift;
  my $major;
  my $minor;
  my $sub;

  if ($xversionAll =~ /(\d+)\.(\d+)\.?(\d*)/) {
    $major = $1;
    $minor = $2;
    $sub = $3 eq '' ? 0 : $3;
  } else {
    $major = 0;
    $minor = 0;
    $sub = 0;
  }
  return ($major, $minor, $sub);
}

sub fix_X_link {
  my $x_version = shift;
  my $x_server_link;
  my $x_server_link_bin = xserver_bin() . '/X';
  my $x_wrapper_file_name = 'Xwrapper';
  my $x_wrapper_file = xserver_bin() . '/' . $x_wrapper_file_name;
  my $x_server_file;
  my $x_server_file_name;

  if ($x_version == 3) {
      $x_server_file = xserver3();
  } elsif ($x_version == 4) {
      $x_server_file = xserver4();
  } elsif ($x_version == 6) {
      $x_server_file = xserver_xorg();
  } elsif ($x_version == 7) {
      $x_server_file = xserver_xorg();
  }

  $x_server_file_name = internal_basename($x_server_file);

  # Case 1:
  # In this case, the Xwrapper is used if /etc/X11/X exists (could be broken)
  # _and_ /usr/X11R6/bin/X points to Xwrapper.
  # In this case, the Xwrapper will execute setuid anything /etc/X11/X
  # is pointing to. So /etc/X11/X has to be pointing to the correct X
  # server, this is XFree86 if XFree 4 is used, our driver if XFree 3 is used.
  # WARNING: In this case, someone could very easily create a link /etc/X11/X
  # pointing to the Xwrapper, which, of course creates and infinite loop.
  # On SuSE, this mechanism is completely broken because Xwrapper tries to run
  # /usr/X11R6/bin/X !
  # In general, The wrapper is stupid.
  $x_server_link = '/etc/X11/X';
  if (-l $x_server_link &&
      check_link($x_wrapper_file, $x_server_link_bin) eq 'yes') {
    symlink_if_needed($x_server_file, $x_server_link);
    set_uid_X_server($x_server_file);
    return;
  }

  # Case 2:
  # This case is often encountered on a SuSE system.
  # Where /var/X11R6/bin/X is a little like /etc/X11/X but the Xwrapper is
  # never used on a SuSE system, of course, there could be special cases.
  # We might be tempted to zap the use of this var place
  # but startx checks for X link and refuses to start if not present in var.
  # Of course, it doesn't check where it points to :-)
  $x_server_link = '/var/X11R6/bin/X';
  if (-d internal_dirname($x_server_link)) {
    symlink_if_needed($x_server_file, $x_server_link);
    symlink_if_needed($x_server_link, $x_server_link_bin);
    set_uid_X_server($x_server_file);
    return;
  }

  # Case 3:
  # All the remaining cases, where the /usr/X11R6/bin/X bin link should be
  # pointing to a setuid root X server.
  $x_server_link = '/usr/X11R6/bin/X';
  symlink_if_needed($x_server_file, $x_server_link_bin);
  set_uid_X_server($x_server_file);
}

sub xorg {
  my $xconfig_path = '/etc/X11';
  my $xconfig_file_name = 'xorg.conf';
  my $xversion = 6;
  my $xversionAll = '';
  my $xserver_link = '';
  my $major;
  my $minor;
  my $disableHotPlug = 'no';
  my $sub;
  my %p;
  undef %p;

  $xversionAll = getXorgVersionAll();

  if (defined $ENV{'XORGCONFIG'} && file_name_exist('/etc/X11/' .
      $ENV{'XORGCONFIG'})) {
    $xconfig_path = '/etc/X11';
    $xconfig_file_name = $ENV{'XORGCONFIG'};
  } elsif (defined $ENV{'XORGCONFIG'} &&
           file_name_exist('/usr/X11R6/etc/X11/' . $ENV{'XORGCONFIG'})) {
    $xconfig_path = '/usr/X11R6/etc/X11';
    $xconfig_file_name = $ENV{'XORGCONFIG'};
  } elsif (file_name_exist('/etc/X11/xorg.conf-4')) {
    $xconfig_path = '/etc/X11';
    $xconfig_file_name = 'xorg.conf-4';
  } elsif (file_name_exist('/etc/X11/xorg.conf')) {
    $xconfig_path = '/etc/X11';
    $xconfig_file_name = 'xorg.conf';
  } elsif (file_name_exist('/etc/xorg.conf')) {
    $xconfig_path = '/etc';
    $xconfig_file_name = 'xorg.conf';
  } elsif (file_name_exist('/usr/X11R6/etc/X11/xorg.conf-4')) {
    $xconfig_path = '/usr/X11R6/etc/X11';
    $xconfig_file_name = 'xorg.conf-4';
  } elsif (file_name_exist('/usr/X11R6/etc/X11/xorg.conf')) {
    $xconfig_path = '/usr/X11R6/etc/X11';
    $xconfig_file_name = 'xorg.conf';
  } elsif (file_name_exist('/usr/X11R6/lib/X11/xorg.conf-4')) {
    $xconfig_path = '/usr/X11R6/lib/X11';
    $xconfig_file_name = 'xorg.conf-4';
  } elsif (file_name_exist('/usr/X11R6/lib/X11/xorg.conf')) {
    $xconfig_path = '/usr/X11R6/lib/X11';
    $xconfig_file_name = 'xorg.conf';
  } elsif (file_name_exist('/etc/X11/.xorg.conf') && ! -e '/etc/X11/xorg.conf') {
    # For Solaris so that we patch the xorg file shipped
    install_file('/etc/X11/.xorg.conf', '/etc/X11/xorg.conf', \%p, 0);
    $xconfig_path = '/etc/X11';
    $xconfig_file_name = 'xorg.conf';
  }

  print wrap("\n\n" . 'Detected X.org version ' . $xversionAll . '.'
             . "\n\n", 0);

  ($major, $minor, $sub) = split_X_version($xversionAll);

  # The 1.3.0 release of the X-server had a little goof where it would say
  # X Window System, Release 1.3.0. so $major == 1 and $minor == 3. But
  # truly, it's part of Xorg 7.3. They are fixing this in the next release
  # of the server (before 7.3 proper comes out), so this is a very specific
  # workaround. Set $major == 1 and $minor == 3 to 7.1 (with which it is
  # known to be compatible.)
  #
  # See bug#185281 for all the deets.

  if ($major == 1 && $minor == 3) {
    $major = 7;
    $minor = 1;
  }

  # vmmouse binary shipped with some distribution is buggy
  # Input hotplug needs to be turned off for X Server > 1.4.0.
  # The workaround is to add 
  #       Option      "NoAutoAddDevices"
  # in ServerFlags section for build 1.4.0 and upwards.
  # See 291453 and
  # http://docs.fedoraproject.org/release-notes/f9/en_US/sn-Desktop.html#vmmouse-driver
  # Release 1.4.0 came out as part of Xorg 7.3
  if ($major == 7 && $minor >= 3) {
    $disableHotPlug = 'yes';
  }

  # If there is an existing driver, replace it by ours.
  if ($major == 6) {
    # If there is an existing driver replace it by ours, backing up the existing driver.
    backup_file_to_restore($gXVideoDriverFile, 'OLD_X4_DRV');
    backup_file_to_restore($gXMouseDriverFile, 'OLD_X4_MOUSEDRV');

    # Install the drivers.
    if ($minor == 7) {
      install_file(db_get_answer('LIBDIR')  . '/configurator/XOrg/6.7.x' .
		   ($gIs64BitX ? '_64' : '') . '/vmware_drv.o',
		   $gXVideoDriverFile, \%p, 1);
      install_file(db_get_answer('LIBDIR')  . '/configurator/XOrg/6.7.x' .
		   ($gIs64BitX ? '_64' : '') . '/vmmouse_drv.o',
		   $gXMouseDriverFile, \%p, 1);

      if (vmware_product() eq 'tools-for-linux') {
        if (!$gIs64BitX) {
          set_manifest_component('svga67', 'TRUE');
          set_manifest_component('vmmouse67', 'TRUE');
        } else {
          set_manifest_component('svga67_64', 'TRUE');
          set_manifest_component('vmmouse67_64', 'TRUE');
        }
      }
    } elsif ($minor == 8) {
      # Solaris is an early adopter and is using .so drivers on 6.8.x
      my $suffix = vmware_product() eq 'tools-for-solaris' ? '.so' : '.o';

      install_file(db_get_answer('LIBDIR')  . '/configurator/XOrg/6.8.x' .
		   ($gIs64BitX ? '_64' : '') . '/vmware_drv' . $suffix,
		   $gXVideoDriverFile, \%p, 1);
      install_file(db_get_answer('LIBDIR')  . '/configurator/XOrg/6.8.x' .
		   ($gIs64BitX ? '_64' : '') . '/vmmouse_drv' . $suffix,
		   $gXMouseDriverFile, \%p, 1);

      if (vmware_product() eq 'tools-for-linux') {
        if (!$gIs64BitX) {
          set_manifest_component('svga68', 'TRUE');
          set_manifest_component('vmmouse68', 'TRUE');
        } else {
          set_manifest_component('svga68_64', 'TRUE');
          set_manifest_component('vmmouse68_64', 'TRUE');
        }
      }
    } elsif ($minor == 9 && (vmware_product() eq 'tools-for-solaris'
			     || vmware_product() eq 'tools-for-linux')) {
       # The 7.0 drivers work on 6.9.x as well (see bug 92501)
       # gxMouseDriverFile and gxVideoDriverFile have already been set for Solaris
       # by configure_X().  Use xorg paths for 6.9 instead of old XFree ones.
       if (vmware_product() ne 'tools-for-solaris') {
          my $xorg_modules_dir = xorg_find_modules_dir();
          $gXMouseDriverFile = $xorg_modules_dir . '/input/vmmouse_drv.so';
          $gXVideoDriverFile = $xorg_modules_dir . '/drivers/vmware_drv.so';
          # If there is an existing driver replace it by ours, backing up the existing driver.
          backup_file_to_restore($gXVideoDriverFile, 'OLD_X4_DRV');
          backup_file_to_restore($gXMouseDriverFile, 'OLD_X4_MOUSEDRV');
       }
      install_file(db_get_answer('LIBDIR')  . '/configurator/XOrg/7.0' .
		   ($gIs64BitX ? '_64' : '') . '/vmware_drv.so',
		   $gXVideoDriverFile, \%p, 1);
      install_file(db_get_answer('LIBDIR')  . '/configurator/XOrg/7.0' .
		   ($gIs64BitX ? '_64' : '') . '/vmmouse_drv.so',
		   $gXMouseDriverFile, \%p, 1);
    } elsif ($minor == 9 && vmware_product() eq 'tools-for-freebsd') {
      install_file(db_get_answer('LIBDIR') . '/configurator/XOrg/6.9' .
		   ($gIs64BitX ? '_64' : '') . '/vmware_drv.so',
		   $gXVideoDriverFile, \%p, 1);
      install_file(db_get_answer('LIBDIR') . '/configurator/XOrg/6.9' .
		   ($gIs64BitX ? '_64' : '') . '/vmmouse_drv.so',
		   $gXMouseDriverFile, \%p, 1);
    } else {
      print wrap("\n\n" . 'No drivers for X.org version: ' . $xversionAll . '.'
		   . "\n\n", 0);
      $gNoXDrivers = 1; # Use this variable to alert about missing drivers
    }
    fix_X_link('6');
  # this used to be 0 <= $minor <=3, but fedora9's pre-release of X Server
  #  1.4.99.901 (1.5.0 RC 1) requires at least this much restriction.
  # This is now 0 <= $minor <= 4 with no support for 1.3.99+ as the 7.3
  #  driver does not work and the 7.4 is too new.
  } elsif ($major == 7 &&
           (($minor >= 0 && $minor <= 4)) ) {

       my $compat = $minor;

       if ($minor == 2) {
          $compat = 1;
       }

       # As far as I can tell, FreeBSD only ships with 7.3 as a package
       # all the way back to 5-STABLE. We don't have 7.1 modules.
       if (vmware_product() eq 'tools-for-freebsd' and $minor != 3) {
          print wrap("\n\n" . 'No drivers for X.org version: ' . $xversionAll
                   . ".\n\n", 0);
          $gNoXDrivers = 1; # Use this variable to alert about missing drivers
          return ($xversion,
                  xconfig_file_abs_path($xconfig_path, $xconfig_file_name),
                  $xversionAll, $disableHotPlug);
       }

       # gxMouseDriverFile and gxVideoDriverFile have already been set for Solaris
       # by configure_X().
       if (vmware_product() ne 'tools-for-solaris') {
          my $xorg_modules_dir = xorg_find_modules_dir();
          $gXMouseDriverFile = $xorg_modules_dir . '/input/vmmouse_drv.so';
          $gXVideoDriverFile = $xorg_modules_dir . '/drivers/vmware_drv.so';
       }

       # If there is an existing driver replace it by ours, backing up
       # the existing driver.
       backup_file_to_restore($gXVideoDriverFile, 'OLD_X4_DRV');
       backup_file_to_restore($gXMouseDriverFile, 'OLD_X4_MOUSEDRV');
       # use 7.1 drivers for 7.2
       # Install the drivers.
       my %p;
       undef %p;
       # 7.3.99 is a special case with a special driver
       if (($major == 7) && ($minor == 3) && ($sub == 99)) {
	 install_file(db_get_answer('LIBDIR') . "/configurator/XOrg/7.$compat.99" .
		      ($gIs64BitX ? '_64' : '') . '/vmware_drv.so',
		      $gXVideoDriverFile, \%p, 1);
	 install_file(db_get_answer('LIBDIR') . "/configurator/XOrg/7.$compat.99" .
		      ($gIs64BitX ? '_64' : '') . '/vmmouse_drv.so',
		      $gXMouseDriverFile, \%p, 1);
       }
       else {
	 install_file(db_get_answer('LIBDIR') . "/configurator/XOrg/7.$compat" .
		      ($gIs64BitX ? '_64' : '') . '/vmware_drv.so',
		      $gXVideoDriverFile, \%p, 1);
	 install_file(db_get_answer('LIBDIR') . "/configurator/XOrg/7.$compat" .
		      ($gIs64BitX ? '_64' : '') . '/vmmouse_drv.so',
		      $gXMouseDriverFile, \%p, 1);
       }

       if (vmware_product() eq 'tools-for-linux') {
	 if ($compat == 0) {
	   if (!$gIs64BitX) {
             set_manifest_component('svga70', 'TRUE');
             set_manifest_component('vmmouse70', 'TRUE');
	   } else {
             set_manifest_component('svga70_64', 'TRUE');
             set_manifest_component('vmmouse70_64', 'TRUE');
	   }
	 }
	 # Use 7.1 driver for 7.1 through 7.3.98
	 if ($compat == 1) {
	   if (!$gIs64BitX) {
	     set_manifest_component('svga71', 'TRUE');
	     set_manifest_component('vmmouse71', 'TRUE');
	   } else {
	     set_manifest_component('svga71_64', 'TRUE');
	     set_manifest_component('vmmouse71_64', 'TRUE');
	   }
	 }
	 # Use 7.3 driver for most 7.3,
	 # Use 7.3.99 driver for 7.3.99 only.
	 if ($compat == 3) {
	   if ($sub == 99) {
	     if (!$gIs64BitX) {
	       set_manifest_component('svga73_99', 'TRUE');
	       set_manifest_component('vmmouse73_99', 'TRUE');
	     } else {
	       set_manifest_component('svga73_99_64', 'TRUE');
	       set_manifest_component('vmmouse73_99_64', 'TRUE');
	     }
	   }
	   else {
	     if (!$gIs64BitX) {
	       set_manifest_component('svga73', 'TRUE');
	       set_manifest_component('vmmouse73', 'TRUE');
	     } else {
	       set_manifest_component('svga73_64', 'TRUE');
	       set_manifest_component('vmmouse73_64', 'TRUE');
	     }
	   }
	 }
	 if ($compat == 4) {
	   # Install additional files for 7.4
	   if (-d '/usr/lib/hal') { # Ubuntu puts hal files in /usr/lib/hal
	     install_file(db_get_answer('LIBDIR') . "/configurator/XOrg/7.$compat" .
			  ($gIs64BitX ? '_64' : '') . '/hal-probe-vmmouse',
			  '/usr/lib/hal/hal-probe-vmmouse', \%p, 1);
	   }
	   elsif (-d '/usr/libexec') { # Fedora in /usr/libexec
	     install_file(db_get_answer('LIBDIR') . "/configurator/XOrg/7.$compat" .
			  ($gIs64BitX ? '_64' : '') . '/hal-probe-vmmouse',
			  '/usr/libexec/hal-probe-vmmouse', \%p, 1);
	   }
	   else {
	     # Unknown distribution - Do not copy it.
	   }

	   backup_file_to_restore('/usr/bin/vmmouse_detect', 'VMMOUSE_DETECT');
	   install_file(db_get_answer('LIBDIR') . "/configurator/XOrg/7.$compat" .
			($gIs64BitX ? '_64' : '') . '/vmmouse_detect',
			'/usr/bin/vmmouse_detect', \%p, 1);
	   install_file(db_get_answer('LIBDIR') . "/configurator/XOrg/7.$compat" .
			($gIs64BitX ? '_64' : '') . '/11-x11-vmmouse.fdi',
			'/usr/share/hal/fdi/policy/20thirdparty/11-x11-vmmouse.fdi', \%p, 1);
	   if (!$gIs64BitX) {
	     set_manifest_component('svga74', 'TRUE');
	     set_manifest_component('vmmouse74', 'TRUE');
	   } else {
	     set_manifest_component('svga74_64', 'TRUE');
	     set_manifest_component('vmmouse74_64', 'TRUE');
	   }
	 }
      }
  } else {
    $gNoXDrivers = 1; # Use this variable to alert about missing drivers
    print wrap("\n\n" . 'No drivers for X.org version: ' . $xversionAll . '.'
	       . "\n\n", 0);
  }
  return ($xversion, xconfig_file_abs_path($xconfig_path, $xconfig_file_name),
          $xversionAll, $disableHotPlug);
}

# Different xorg installations may store their modules in different places.
sub xorg_find_modules_dir {
   # have to add /usr/X11R6/lib/modules to work with SLES 10 which has xorg
   # but uses this old path.  lib64 must come before lib because both are
   # present on x64 machines with drivers being in lib64.
   my @modDirs = qw(/usr/lib64/xorg/modules /usr/lib/xorg/modules
                    /usr/X11R6/lib64/modules /usr/local/lib/xorg/modules
                    /usr/X11R6/lib/modules /usr/X11R6/lib/xorg/modules);
   foreach my $modDir (@modDirs) {
      if (-d $modDir) {
         return $modDir;
      }
   }

   return get_persistent_answer('What is the location of the directory which contains ' .
      'your XOrg modules?', 'XORGMODULEDIR', 'dirpath_existing', '');
}

sub xfree_4 {
  my $xconfig_path;
  my $xconfig_file_name;
  my $xversionAll = '';
  my $xserver_link = '';
  my $major;
  my $minor;
  my $sub;

  $xversionAll = direct_command(shell_string(xserver4()) . ' -version 2>&1') =~
    /XFree86 Version (\d+\.\d+\.?\d*)/ ? $1: '0.0.0';

  # This search order is issued from the XF86Config man page.
  if (defined $ENV{'XF86CONFIG'} &&
      file_name_exist('/etc/X11/' . $ENV{'XF86CONFIG'})) {
    $xconfig_path = '/etc/X11';
    $xconfig_file_name = $ENV{'XF86CONFIG'};
  } elsif (defined $ENV{'XF86CONFIG'} &&
           file_name_exist('/usr/X11R6/etc/X11/' . $ENV{'XF86CONFIG'})) {
    $xconfig_path = '/usr/X11R6/etc/X11';
    $xconfig_file_name = $ENV{'XF86CONFIG'};
  } elsif (file_name_exist('/etc/X11/XF86Config-4')) {
    $xconfig_path = '/etc/X11';
    $xconfig_file_name = 'XF86Config-4';
  } elsif (file_name_exist('/etc/X11/XF86Config')) {
    # In this case, we are in the situation of having a mix between
    # XFree 3 and XFree 4, which is usually the case on RH 7.x and
    # Mandrake 7.x systems. As far as the syntax is concerned, XF86Config
    # is the 3.x version and XF86Config-4 is the 4.x version.
    # fix_X_conf patches some of the fields of the old config file into the new
    # one. There are issues if 3.x syntax fields are patched in a 4.x config
    # file. By providing a non existing file fix_X_conf will generate a correct
    # one or if the XF86Config file has the XFree 4 syntax, we can use it.
    # See bug 23196.
    $xconfig_path = '/etc/X11';
    if (direct_command(shell_string($gHelper{'grep'}) . ' '
                       . shell_string('.*') . ' '
                       . '/etc/X11/XF86Config') =~ /Section\s+\"ServerLayout\"/i) {
      $xconfig_file_name = 'XF86Config';
    } else {
      $xconfig_file_name = 'XF86Config-4';
    }
  } elsif (file_name_exist('/etc/XF86Config')) {
    $xconfig_path = '/etc';
    $xconfig_file_name = 'XF86Config';
  } elsif (file_name_exist('/usr/X11R6/etc/X11/XF86Config-4')) {
    $xconfig_path = '/usr/X11R6/etc/X11';
    $xconfig_file_name = 'XF86Config-4';
  } elsif (file_name_exist('/usr/X11R6/etc/X11/XF86Config')) {
    $xconfig_path = '/usr/X11R6/etc/X11';
    $xconfig_file_name = 'XF86Config';
  } elsif (file_name_exist('/usr/X11R6/lib/X11/XF86Config')) {
    # FreeBSD 5.2 after running xf86config in graphic mode
    $xconfig_path = '/usr/X11R6/lib/X11';
    $xconfig_file_name = 'XF86Config';
  } else {
    # X config file not found
    return (4, undef, $xversionAll);
  }

  if (defined $xconfig_file_name) {
     print wrap("\n\n" . 'Detected XFree86 version ' . $xversionAll . '.'
                . "\n\n", 0);
  }

  # If there is an existing driver, replace it by ours.
  backup_file_to_restore($gXVideoDriverFile, 'OLD_X4_DRV');
  if (file_name_exist($gXVideoDriverFile)) {
      unlink $gXVideoDriverFile;
  }

  ($major, $minor, $sub) = split_X_version($xversionAll);
  if ($major == 4) {
    if ($minor == 2) {
      # For XFree 4.2.x, we need to replace xaa and shadowfb
      my $xaaDrv = '/usr/X11R6/lib/modules/libxaa.a';
      my $shadowFbDrv = '/usr/X11R6/lib/modules/libshadowfb.a';
      backup_file_to_restore($xaaDrv, 'OLD_X4_XAA_DRV');
      backup_file_to_restore($shadowFbDrv, 'OLD_X4_SHADOW_FB_DRV');
      unlink $xaaDrv;
      unlink $shadowFbDrv;
      my %p;
      undef %p;
      install_file(db_get_answer('LIBDIR')
                   . '/configurator/XFree86-4/4.2.x/libxaa.a',
                   $xaaDrv, \%p, 1);
      install_file(db_get_answer('LIBDIR')
                   . '/configurator/XFree86-4/4.2.x/libshadowfb.a',
                   $shadowFbDrv, \%p, 1);
      install_file(db_get_answer('LIBDIR')
                   . '/configurator/XFree86-4/4.2.x/vmware_drv.o',
                   $gXVideoDriverFile, \%p, 1);

      if (vmware_product() eq 'tools-for-linux') {
        set_manifest_component('svga42', 'TRUE');
      }
    } elsif ($minor > 2) {
      # In this case, all the XAA and ShadowFB changes are present
      # in the XFree Code and we only need to install the latest
      # driver.
      my %p;
      undef %p;
      install_file(db_get_answer('LIBDIR') . '/configurator/XFree86-4/4.3.x' .
		   ($gIs64BitX ? '_64' : '') . '/vmware_drv.o',
		   $gXVideoDriverFile, \%p, 1);

      if ($minor == 3 && vmware_product() eq 'tools-for-linux') {
        if (!$gIs64BitX) {
          set_manifest_component('svga43', 'TRUE');
        } else {
          set_manifest_component('svga43_64', 'TRUE');
        }
      }
    } elsif ($minor < 2) {
      # The default, install the X free 4 driver which works with
      # the first versions of X.
      my %p;
      undef %p;
      install_file(db_get_answer('LIBDIR')
                   . '/configurator/XFree86-4/4.x/vmware_drv.o',
                   $gXVideoDriverFile, \%p, 1);

      if (vmware_product() eq 'tools-for-linux') {
        set_manifest_component('svga4', 'TRUE');
      }
    }
    # Absolute pointing device.
    if ($major == 4 && $minor == 2) {
      my %p;
      undef %p;
      install_file(db_get_answer('LIBDIR')
		   . '/configurator/XFree86-4/4.2.x/vmmouse_drv.o',
		   $gXMouseDriverFile, \%p, 1);

      if (vmware_product() eq 'tools-for-linux') {
        set_manifest_component('vmmouse42', 'TRUE');
      }
    } elsif ($major == 4 && $minor == 3) {
        my %p;
        undef %p;
        install_file(db_get_answer('LIBDIR') . '/configurator/XFree86-4/4.3.x' .
           ($gIs64BitX ? '_64' : '') . '/vmmouse_drv.o',
           $gXMouseDriverFile, \%p, 1);

        if (vmware_product() eq 'tools-for-linux') {
          if (!$gIs64BitX) {
            set_manifest_component('vmmouse43', 'TRUE');
          } else {
            set_manifest_component('vmmouse43_64', 'TRUE');
          }
        }
      }
    fix_X_link('4');
  } else {
    error ('Problem extracting version of XFree 4' . "\n\n");
  }
  return (4, xconfig_file_abs_path($xconfig_path, $xconfig_file_name),
          $xversionAll);
}

sub xfree_3 {
  my $xconfig_path = '/etc';
  my $xconfig_file_name = 'XF86Config';
  my $xversion = 3;
  my $xversionAll = 0;
  my $xserver3default = xserver_bin() . '/XF86_VGA16';
  my $xserver_link = '';

  $xversionAll = file_name_exist($xserver3default) ?
              direct_command(shell_string($xserver3default) .
                             ' -version 2>&1') =~
                             /XFree86 Version (\d+\.\d+\.?\d*)/ ? $1: '3.0'
                             : '3.0';

  if (file_name_exist('/etc/XF86Config')) {
    $xconfig_path = '/etc';
    $xconfig_file_name = 'XF86Config';
  } elsif (file_name_exist('/usr/X11R6/lib/X11/XF86Config') &&
           (not -l '/usr/X11R6/lib/X11/XF86Config')) {
    $xconfig_path = '/usr/X11R6/lib/X11';
    $xconfig_file_name = 'XF86Config';
  } else {
    $xconfig_path = '/etc';
    $xconfig_file_name = 'XF86Config';
  }
  print wrap("\n\n" . 'Detected XFree86 version ' . $xversionAll . '.'
             . "\n\n", 0);

  if (file_name_exist(xserver3())) {
    backup_file(xserver3());
    unlink xserver3();
  }

  if (vmware_product() eq 'tools-for-freebsd' &&
      $gSystem{'uts_release'} =~ /^(\d+)\.(\d+)/ &&
      $1 >= 4 && $2 >= 5) {
    my %p;
    undef %p;
    install_file(db_get_answer('LIBDIR')
                 . '/configurator/XFree86-3/XF86_VMware_4.5',
                 xserver3(), \%p, 1);
  } else {
    my %p;
    undef %p;
    install_file(db_get_answer('LIBDIR')
                 . '/configurator/XFree86-3/XF86_VMware',
                 xserver3(), \%p, 1);
  }

  if (vmware_product() eq 'tools-for-linux') {
    set_manifest_component('svga33', 'TRUE');
  }

  fix_X_link('3');
  return ($xversion, xconfig_file_abs_path($xconfig_path, $xconfig_file_name),
          $xversionAll);
}

sub fix_mouse_file {
  my $mouse_file = '/etc/sysconfig/mouse';
  #
  # If gpm supports imps2, use that as the gpm mouse driver
  # for both X & gpm. If gpm doesn't support imps2, or isn't set
  # in this mode, the mouse will be erratic when exiting X if
  # X was set to use imps2
  #
  my $enableXImps2 = 'no';
  my $GPMBinary = internal_which('gpm');
  if (file_name_exist($GPMBinary) && file_name_exist($mouse_file)) {
    my $enableGpmImps2;

    if (vmware_product() eq 'tools-for-solaris') {
      $enableGpmImps2 =
        (system(shell_string($GPMBinary) . ' -t help | ' . $gHelper{'grep'}
                . ' imps2 > /dev/null 2>&1')) == 0 ? 'yes': 'no';
    } else {
      $enableGpmImps2 =
        (system(shell_string($GPMBinary) . ' -t help | '
                . $gHelper{'grep'} . ' -q imps2')) == 0 ? 'yes': 'no';
    }
    $enableXImps2 = $enableGpmImps2;

    if ($enableGpmImps2 eq 'yes' ) {
      backup_file_to_restore($mouse_file, 'MOUSE_CONF');
      unlink $mouse_file;
      my %p;
      undef %p;
      $p{'^MOUSETYPE=.*$'} = 'MOUSETYPE=imps2';
      $p{'^XMOUSETYPE=.*$'} = 'XMOUSETYPE=IMPS/2';
      internal_sed($mouse_file . $cBackupExtension,
                   $mouse_file, 0, \%p);
    }
  }
  return $enableXImps2;
}

# Create a list of available resolutions for the VMware virtual monitor
sub get_suitable_resolutions {
  my $xf86config = shift;
  my @list;
  my $in;
  my $identifier;

  open(XF86CONFIG, '<' . $xf86config) or
   error('Unable to open the XFree86 configuration file "'
         . $xf86config . '" in read-mode.' . "\n\n");

  $in = 0;
  while (<XF86CONFIG>) {
    if (/^[ \t]*Section[ \t]*"Monitor"[ \t]*$/) {
      $in = 1;
      $identifier = '';
      @list = ();
    } elsif ($in) {
      if (/^[ \t]*Identifier[ \t]*"(\S+)"[ \t]*$/) {
        $identifier = $1;
      } elsif (/^[ \t]*ModeLine[ \t]*(\S+)[ \t]*(\S+)[ \t]*(\S+)[ \t]*(\S+)[ \t]*(\S+)[ \t]*(\S+)[ \t]*(\S+)[ \t]*(\S+)[ \t]*(\S+)[ \t]*(\S+)[ \t]*$/) {
        push(@list, ($1, $3, $7));
      } elsif (/^[ \t]*EndSection[ \t]*$/) {
        if ($identifier eq 'vmware') {
          last;
        }
        $in = 0;
      }
    }
  }

  close(XF86CONFIG);

  return @list;
}

# Pull data out of the config file.  The format is expected to be 'ModeLine "1111x9999"'
sub get_supported_modes {
   my $xf86config = shift;
   my @list;

   open(XF86CONFIG, '<' . $xf86config) or error("Unable to open " . $xf86config . ".\n");
   while (<XF86CONFIG>) {
      if (/ModeLine\D+(\d+)x(\d+)/) {
         push(@list, '"' . $1 . "x" . $2 . '"', $1, $2);
      }
   }
   close(XF86CONFIG);

   return @list;
}

# Determine the name of the maximum available resolution that can fit in the
# VMware virtual monitor
sub get_best_resolution {
  my $xf86config = shift;
  my $width = shift;
  my $height = shift;
  my @avail_modes;
  my $best_name;
  my $best_res;

  @avail_modes = get_supported_modes($xf86config);

  $best_name = '';
  $best_res = -1;
  while ($#avail_modes > -1) {
    my $mode_name = shift(@avail_modes);
    my $mode_width = shift(@avail_modes);
    my $mode_height = shift(@avail_modes);

    if (($mode_width < $width)
        && ($mode_height < $height)
        && ($mode_width * $mode_height > $best_res)) {
      $best_res = $mode_width * $mode_height;
      $best_name = $mode_name;
    }
  }
  return $best_name;
}

# Sort available resolutions for the VMware virtual monitor in increasing order
sub sort_resolutions {
  my $xf86config = shift;
  my @avail_modes;
  my %resolutions;
  my $res;
  my $names;

  @avail_modes = get_supported_modes($xf86config);

  undef %resolutions;
  while ($#avail_modes > -1) {
    my $mode_name = shift(@avail_modes);
    my $mode_width = shift(@avail_modes);
    my $mode_height = shift(@avail_modes);

    $resolutions{$mode_width * $mode_height} .= $mode_name . ' ';
  }

  $names = '';
  foreach $res (sort { $a <=> $b } keys %resolutions) {
    $names .= $resolutions{$res};
  }
  if (not ($names eq '')) {
    chomp($names);
  }
  return $names;
}

# Look for an x config file that first contains a list of modes the driver
# is expected to support, then look for the more general XF86Config(-4) file,
#  some of whose modes are unsupported by the driver.
sub find_suitable_XConfigFile {
   my $xversion = shift;
   my $XFile;

   # ModeList is based in bora/public/modelist.h.  It contains the resolutions
   # supported by the driver, whereas XF86Config is a more general list.
   $XFile = db_get_answer('LIBDIR') . '/configurator/ModeLines';
   if (!-f $XFile) {
      my $YFile = $XFile;
      $XFile = db_get_answer('LIBDIR') . '/configurator/XFree86-'
                         . ($xversion >= 4 ? '4' : $xversion) . '/XF86Config'
                         . ($xversion >= 4 ? '-4': '');
      if (!-f $XFile) {
        error("Unable to find " . $YFile . ' or ' . $XFile . ".\n");
      }
   }

   return $XFile;
}

#
# Try to determine the current screen size
#
sub get_screen_mode {
  my $xversion = shift;
  my $best_resolution = '';
  my $chosen_resolution = '';
  my $suggested_choice = 3;
  my $i = 1;
  my $n;
  my $mode;
  my $choice;
  my @mode_list;
  my $width;
  my $height;
  my $cXPreviousResolution = 'X_PREVIOUS_RES';
  my $cXConfigFile = find_suitable_XConfigFile($xversion);

  my $resolution_string = sort_resolutions($cXConfigFile);
  @mode_list = split(' ', $resolution_string);

  #
  # Set mode according to what was previously chosen in case of an upgrade
  # or ask the user a valid range of resolutions.
  #
  my $prev_res;
  if (defined(db_get_answer_if_exists($cXPreviousResolution))) {
      $prev_res = db_get_answer($cXPreviousResolution);
    if ($resolution_string =~ $prev_res) {
      if (get_answer("\n\n" .
                     'Do you want to change the starting screen display size? (yes/no)',
                     'yesno', 'no') eq 'no') {
        return $prev_res;
      }
    }
    query("Your current resolution choice is unsupported by this "
        . "video driver.  Please choose another resolution.\n"
        . "[Hit enter to continue.]", '', 0);
  }

  ($width, $height) = split(' ', $gSystem{'resolution'});

  $best_resolution =
    get_best_resolution($cXConfigFile, $width, $height);

  $n = scalar @mode_list;

  print wrap("\n" . 'Please choose one of the following display sizes that X '
                  . 'will start with (1 - ' . $n . '):' . "\n\n",
             0);
  foreach $mode (@mode_list) {
    my $header;
    if ($best_resolution eq $mode) {
      $suggested_choice = $i;
      $header = '<';
    } else {
      $header = ' ';
    }
    print wrap('[' . $i . ']' . $header . ' ' . $mode . "\n", 0);
    $i = $i + 1;
  }

  $gMaxNumber = $n;
  $gAnswerSize{'number'} = length($gMaxNumber);
  $choice = get_persistent_answer('Please enter a number between 1 and ' . $n
                                  . ':' . "\n\n", 'XRESNUMBER', 'number',
                                  $suggested_choice);

  $chosen_resolution = $mode_list[$choice - 1];
  db_add_answer($cXPreviousResolution, $chosen_resolution);
  return $chosen_resolution;
}

#
# The first argument is a complete path to a new xconfig file
# The second argument will be only read and should be current xconfig file
# The third argument is the version of XFree86
# The fourth is a boolean informing weather the Imwheel mouse is used
# in gpm or not.
# The fifth is a boolean informing weather an extra section must be added
# to disable hotplug (see bug 291453).
#
sub fix_X_conf {
  my ($newXF86Config, $existingXF86Config, $xversion, $enableXImps2,
  $xversionAll, $disableHotPlug) = @_;

  my $inSection = 0;
  my $inDevice = 0;
  my $inMonitor = 0;
  my @currentSection;
  my $sectionLine;
  my $sectionName;
  my $mouseRegex = '^\s*driver\s+\"(?:mouse|vmmouse)\"';
  my $isMouseSection = 0;

  my $XFree4_scanpci = xserver_bin() . '/scanpci';
  my $major;
  my $minor;
  my $sub;
  my $line;
  my $screen_mode = get_screen_mode($xversion);
  my $needMonitor = 0;
  my %mouseOption = ('"ZAxisMapping"' => '"4 5"',
                     '"Emulate3Buttons"' => '"true"');
  ($major, $minor, $sub) = split_X_version($xversionAll);

  #
  # Check to see if the vmware svga driver is non-unified b/c we have to
  # specifiy the BusId in the XF86Config-4 file in that case
  #
  my $writeBusIDLine = 0;
  if ($xversion >= 4 && file_name_exist($XFree4_scanpci)) {
    my $found = 0;
    if (vmware_product() eq 'tools-for-solaris') {
      if ((system(shell_string($XFree4_scanpci) . ' | '
                 . shell_string($gHelper{'grep'})
                 . ' 0x0710 > /dev/null 2>&1')/256) == 0 ) {
        $found = 1;
      }
    } elsif ((system(shell_string($XFree4_scanpci) . ' | '
                     . shell_string($gHelper{'grep'})
                     . ' -q 0x0710')/256) == 0 ) {
      $found = 1;
    }

    if ($found == 1) {
      $writeBusIDLine = 1;
      # print wrap ('Found the device 0x0710' . "\n\n", 0);
    }
  }

  if (not open(EXISTINGXF86CONFIG, "<$existingXF86Config")) {
    error('Unable to open the file "' . $existingXF86Config . '".' . "\n\n");
  }

  if (not open(NEWXF86CONFIG, ">$newXF86Config")) {
    error('Unable to open the file "' . $newXF86Config . '".' . "\n\n");
  }

  my $gotMouseSection = 0;
  my $gotServerFlagsSection = 0;
  while (defined($line = <EXISTINGXF86CONFIG>)) {
    if ($line =~ /^\s*Section\s*"([a-zA-Z]+)"/i) {
      # We only deal with lines within sections. For other lines,
      # just copy to new file.
      $sectionName = lc($1);
      $inSection = 1;
      push @currentSection, $line;
    } else {
      if ($inSection == 1) {
        if ($line =~ /^\s*EndSection/i) {
          # All lines within a section will first be read into
          # currentSection, then process those lines.
          push @currentSection, $line;

          if (($sectionName eq 'inputdevice') || ($sectionName eq 'pointer')) {
            # There are several different kinds of inputdevice section, such as
            # mouse, keyboard, and only mouse section will be re-processed,
            # others just copy to new file. 'pointer' is the mouse section name
            # for some x config file.
            $isMouseSection = 0;
            if ($sectionName eq 'pointer') {
              $isMouseSection = 1;
            }
            foreach $sectionLine (@currentSection) {
              if ($sectionLine =~ /$mouseRegex/i) {
                $isMouseSection = 1;
                last;
              }
            }

            if ($isMouseSection == 1) {
              $gotMouseSection = 1;
              my $seenDeviceSection = 0;
              foreach $sectionLine (@currentSection) {
                # Replace mouse driver
                if ($sectionLine =~ /^\s*Driver\s+\"(.+)\"/i) {
		  # Install a mouse driver for all X versions >= 4.2
                  if (file_name_exist($gXMouseDriverFile) &&
		      ($major > 4 || ($major == 4 && $minor >= 2))) {
                    $sectionLine =~ s/$1/vmmouse/g;
                  }
                }

                # Replace mouse protocol. There are 2 different formats for mouse
                # protocol, so should be handled seperately.
                if (($sectionLine =~ /^\s*Option\s+\"Protocol\"\s+\"(.+)\"/i) ||
                    ($sectionLine =~ /^\s*Protocol\s+\"(.+)\"/i)) {
                  my $tmpmouse = $1;
                  if (vmware_product() eq 'tools-for-freebsd') {
                    if (direct_command(shell_string($gHelper{'grep'}) . ' '
                                       . shell_string('moused_enable') . ' '
                                       . shell_string('/etc/rc.conf')) =~ /yes/i) {
                      $sectionLine =~ s/$tmpmouse/SysMouse/;
                    } else {
                      $sectionLine =~ s/$tmpmouse/ps\/2/;
                    }
                  } elsif ($enableXImps2 eq 'yes') {
                    $sectionLine =~ s/$tmpmouse/IMPS\/2/g;
                  } else {
                    $sectionLine =~ s/$tmpmouse/ps\/2/g;
                  }
                }

                if ($sectionLine =~ /^\s*Option\s+\"ZAxisMapping\"\s+\"(.+)\"/i) {
                   $mouseOption{'"ZAxisMapping"'} = "";
                }
                if ($sectionLine =~ /^\s*Option\s+\"Emulate3Buttons"\s+\"(.+)\"/i) {
                   $mouseOption{'"Emulate3Buttons"'} = "";
                }

                # Replace mouse device. There are 2 different formats for mouse
                # device, so should be handled seperately.
                if (($sectionLine =~ /^\s*Option\s+\"Device\"\s+\"(.+)\"/i) ||
                    ($sectionLine =~ /^\s*Device\s+\"(.+)\"/i)) {
                  $seenDeviceSection = 1;
                  my $tmpdev = $1;
                  if (vmware_product() eq 'tools-for-freebsd') {
                    if (direct_command(shell_string($gHelper{'grep'}) . ' '
                                       . shell_string('moused_enable') . ' '
                                       . shell_string('/etc/rc.conf')) =~ /yes/i) {
                      $sectionLine =~ s/$tmpdev/\/dev\/sysmouse/;
                    } else {
                      $sectionLine =~ s/$tmpdev/\/dev\/psm0/;
                    }
                  } elsif (vmware_product() eq 'tools-for-linux') {
                     # If we have to create a xorg.conf file, the default
                     # mouse device is /dev/mouse. Most machines don't have
                     # /dev/mouse these days, so check if it's going to fail
                     # and try the other alternatives.
                     if (!file_name_exist("$tmpdev")) {
                        # Most common case: /dev/input/mice
                        if (file_name_exist("/dev/input/mice")) {
                           $sectionLine =~ s/$tmpdev/\/dev\/input\/mice/;
                        # 2.4 case: /dev/psaux
                        } elsif (file_name_exist("/dev/psaux")) {
                           $sectionLine =~ s/$tmpdev/\/dev\/psaux/;
                        }
                     }
                  }
                }

                # Solaris guests should use the PS/2 device /dev/kdmouse
                if (vmware_product() eq 'tools-for-solaris') {
                  if (($sectionLine =~ /^\s*Option\s+\"Device\"\s+\"(.+)\"/i) ||
                      ($sectionLine =~ /^\s*Device\s+\"(.+)\"/i)) {
                    my $tmpdev = $1;
                    $sectionLine =~ s/$tmpdev/\/dev\/kdmouse/;
                  }
                }

                # Normalize the identifier name
                if ($sectionLine =~ /^\s*Identifier\s+\"/) {
                  $sectionLine = "$&VMware Mouse\"\n";
                }

                # In the mouse section, if we end the section and we haven't
                # yet seen a Device line, add it, because vmmouse will not
                # work without one. Not sure what to do if there is no mouse
                # at all, but this is better than nothing for now.
                #
                # Ubuntu 8.04's mouse config section by default does not
                # specify a device (the driver probes for one and finds it.)
                # 
                # To mitigate the risk of affecting behavior on other guests,
                # enable this behavior only for tools-for-linux. It hasn't
                # ever been a problem on other guests anyway AFAICT.

                if (vmware_product() eq 'tools-for-linux' &&
                    $sectionLine =~ /^\s*EndSection/i &&
                    $seenDeviceSection == 0) {
                  if (file_name_exist("/dev/input/mice")) {
                     print NEWXF86CONFIG "   Option \"Device\" \"/dev/input/mice\"\n";
                  # 2.4 case: /dev/psaux
                  } elsif (file_name_exist("/dev/psaux")) {
                     print NEWXF86CONFIG "   Option \"Device\" \"/dev/psaux\"\n";
                  }
                }

                # Insert any of the mouse options not already accounted for
                # before the end of the section
                if (vmware_product() eq 'tools-for-linux' &&
                  $sectionLine =~ /^\s*EndSection/i) {
                  foreach my $option (keys %mouseOption) {
                    if ($mouseOption{$option} eq '') {
                      next;
                    }
                    print NEWXF86CONFIG "\tOption\t\t" . $option . "\t"
                                        .  $mouseOption{$option} . "\n";
                  }
                }

                print NEWXF86CONFIG $sectionLine;
              }
            } else {
              # First check if it's keyboard and we can rename its identifier.
              my $isKeyboardSection = 0;
              foreach $sectionLine (@currentSection) {
                # SLES9-SP4 uses "Keyboard" instead "keyboard" as Driver name.
                # 'k' should be case-insensitive.
                if ($sectionLine =~ /^\s*Driver\s+\"((?i)keyboard|kbd)\"/) {
                  $isKeyboardSection = 1;
                }
              }

              # In inputdevice section, if it is not mouse or keyboard, just copy
              # to new file directly. Otherwise, rename the keyboard identifier
              foreach $sectionLine (@currentSection) {
                if ($isKeyboardSection && $sectionLine =~ /^\s*Identifier\s+\"/) {
                   print NEWXF86CONFIG "$&VMware Keyboard\"\n";
                } else {
                   print NEWXF86CONFIG $sectionLine;
                }
              }
            }
          } elsif ($sectionName eq 'device') {
            # Regenerate whole device section, and only one device section
            if ($inDevice == 0) {
              print NEWXF86CONFIG "Section \"Device\"\n";
              print NEWXF86CONFIG "    Identifier  \"VMware SVGA\"\n";
              if ($xversion >= 4) {
                # For config with newer format.
                print NEWXF86CONFIG "    Driver      \"vmware\"\n";
                if ($writeBusIDLine) {
                  print NEWXF86CONFIG "    BusID       \"PCI:0:15:0\"\n";
                }
              } else {
                # For config with old format.
                print NEWXF86CONFIG "    Chipset      \"generic\"\n";
              }
              print NEWXF86CONFIG "EndSection\n";
              $inDevice = 1;
            }
          } elsif ($sectionName eq 'screen') {
            # Regenerate whole screen section. All screen section will
            # be same except identifier name.
            print NEWXF86CONFIG "Section \"Screen\"\n";
            foreach $sectionLine (@currentSection) {
              if ($sectionLine =~ /^\s*Identifier\s+\"(.+)\"/i) {
                print NEWXF86CONFIG $sectionLine;
                last;
              }
            }
            if ($xversion >= 4) {
              # For config with newer format.
              print NEWXF86CONFIG <<EOF;
    Device      "VMware SVGA"
    Monitor     "vmware"
    # Don't specify DefaultColorDepth unless you know what you're
    # doing. It will override the driver's preferences which can
    # cause the X server not to run if the host doesn't support the
    # depth.
    Subsection "Display"
        # VGA mode: better left untouched
        Depth       4
        Modes       "640x480"
        ViewPort    0 0
    EndSubsection
    Subsection "Display"
        Depth       8
        Modes       $screen_mode
        ViewPort    0 0
    EndSubsection
    Subsection "Display"
        Depth       15
        Modes       $screen_mode
        ViewPort    0 0
    EndSubsection
    Subsection "Display"
        Depth       16
        Modes       $screen_mode
        ViewPort    0 0
    EndSubsection
    Subsection "Display"
        Depth       24
        Modes       $screen_mode
        ViewPort    0 0
    EndSubsection
EndSection
EOF
              $needMonitor = 1;
            } else {
              # For config with old format.
              print NEWXF86CONFIG <<EOF;
    Driver "accel"
    Device "VMware SVGA"
    Monitor "vmware"
    Subsection "Display"
	Modes $screen_mode
#	Modes "1600x1200" "1280x1024" "1152x864" "1024x768" "800x600" "640x480"
#	Modes "640x480"
#	Modes "800x600"
#	Modes "1024x768"
#	Modes "1152x864"
#	Modes "1152x900"
#	Modes "1280x1024"
#	Modes "1376x1032"
#	Modes "1600x1200"
#	Modes "2364x1773"
        ViewPort 0 0
    EndSubsection
EndSection
EOF
            }
          } elsif ($sectionName eq 'monitor') {
            # Regenerate whole monitor section, and only one monitor section
            if ($inMonitor == 0) {
              $inMonitor = 1;
              $needMonitor = 1;
            }
          } elsif ($sectionName eq 'serverlayout') {
            # Copy other sections directly to new file.
            foreach $sectionLine (@currentSection) {
              if ($sectionLine =~ 'EndSection') {
                # See matching 'XWorkAround' InputDevice section below.
                #
                # X 7.2(.X) thinks no default mouse is loaded even when
                # vmmouse has already loaded.  So provide a workaround void
                # driver InputDevice section in xorg.conf to fool X.
                #
                # Bug 156988 has all the cory details.
                if (($major == 7 && ($minor == 2 || ($minor == 1 && ($sub == 99 || distribution_info() eq 'redhat')))) || ($major == 1 && $minor == 3)) {
                  print NEWXF86CONFIG "	InputDevice	\"XWorkAround\"\n";
                }

                # Since we can't know for sure that all InputDevice sections have
                # been read before getting here, we have to just force these to
                # standard names.
                print NEWXF86CONFIG "	InputDevice	\"VMware Keyboard\"	\"CoreKeyboard\"\n";
                print NEWXF86CONFIG "	InputDevice \"VMware Mouse\"	\"CorePointer\"\n";
              }

              if (!($sectionLine =~ /^\s*InputDevice\s+/) &&
                  !($sectionLine =~ /^\s*Pointer\s+/)) {
                 print NEWXF86CONFIG $sectionLine;
              }
            }
          } elsif ($sectionName eq 'serverflags' && $disableHotPlug eq
                   'yes') {
            foreach $sectionLine (@currentSection) {
               # The option NoAutoAddDevices needs to be added.
               if ($sectionLine =~ 'EndSection' && $gotServerFlagsSection == 0) {
                  print NEWXF86CONFIG "    Option  \"NoAutoAddDevices\"\n";
                  print NEWXF86CONFIG "EndSection\n";
                  $gotServerFlagsSection = 1;
               }
               elsif ($sectionLine =~
                      /^\s*Option\s+\"AutoAddDevices\"\s+\"(.+)\"/i &&
                        $gotServerFlagsSection == 0) {
                  # The user specified AutoAddDevice something.
                  # Using input hotplug isn't recommended inside a VM anyways.
                  # We override his choice
                  print NEWXF86CONFIG "    Option  \"NoAutoAddDevices\"\n";
                  $gotServerFlagsSection = 1;
               }
               else {
                  print NEWXF86CONFIG $sectionLine;
                  # If the option was already there.
                  if ($sectionLine =~ /^\s*Option\s+\"NoAutoAddDevices\"/i) {
                     $gotServerFlagsSection = 1;
                  }
               }
            }
          } else {
            # Copy other sections directly to new file.
            foreach $sectionLine (@currentSection) {
              print NEWXF86CONFIG $sectionLine;
            }
          }
          # Reset for next section.
          $inSection = 0;
          @currentSection = ();
        } else {
          push @currentSection, $line;
        }
      } else {
        # Copy other lines outside sections directly to new file.
        print NEWXF86CONFIG $line;
      }
    }
  }

  if ($gotMouseSection == 0) {
    print NEWXF86CONFIG <<EOF;
Section "InputDevice"
    Driver "vmmouse"
    Identifier "VMware Mouse"
    Option "Buttons" "5"
    Option "Device" "/dev/input/mice"
    Option "Protocol" "IMPS/2"
    Option "ZAxisMapping" "4 5"
    Option "Emulate3Buttons" "true"
EndSection
EOF
    $gotMouseSection = 1;
  }
  if ($needMonitor == 1) {
    print NEWXF86CONFIG <<EOF;
Section "Monitor"
    Identifier      "vmware"
    VendorName      "VMware, Inc"
    HorizSync       1-10000
    VertRefresh     1-10000
EndSection
EOF
    $needMonitor = 0;
  }
  # See matching XWorkAround layout entry above.
  #
  if (($major == 7 && ($minor == 2 || ($minor == 1 && ($sub == 99 || distribution_info() eq 'redhat')))) || ($major == 1 && $minor == 3)) {
    print NEWXF86CONFIG <<EOF;

Section "InputDevice"
	Identifier  "XWorkAround"
	Driver      "void"
EndSection

EOF
  }
  # See bug 291453
  #
  if ($gotServerFlagsSection == 0 && $disableHotPlug eq 'yes') {
    print NEWXF86CONFIG <<EOF;
Section "ServerFlags"
    Option "NoAutoAddDevices"
EndSection
EOF
    $gotServerFlagsSection = 1;
  }
  close (EXISTINGXF86CONFIG);
  close (NEWXF86CONFIG);
}

sub try_X_conf {
  my $xConfigFile = shift;
  my $xLogFile = shift;
  my $childPid;
  my $childStatus;
  my $vtNext;

  unless(defined(&VT_OPENQRY)) {   # find available vt
    sub VT_OPENQRY () { 0x5600; }
  }

  my $TTY0;
  open($TTY0, '/dev/tty0') or die "open /dev/tty0 : $!\n";

  my $data = pack("I", 0);
  if (ioctl($TTY0, VT_OPENQRY, $data)) {
    $vtNext = unpack("I", $data);
  } else {
    error("VT_OPENQRY ioctl() error: $!\n");
  }

  close($TTY0);

  my @xargs = (xserver_bin() . '/X', ':1', 'vt' . $vtNext,
                '-xf86config',  $xConfigFile ,
                '-logfile', $xLogFile,
                '-once', '-logverbose', '3');

  if ($childPid = open(CHILD, '-|')) {
    # Run parent code, reading from child
    $SIG{ALRM} = sub { die "alarm\n" };

    my $buf;
    eval {
      alarm 5;   # Seconds
      do {
        $buf = <CHILD>;
        $childStatus = waitpid($childPid,0);
      } until $childStatus == -1;
      alarm 0;
    };

    if ($@) {
      # Propagate unexpected errors
      die unless $@ eq "alarm\n";
      # Timed out
      print wrap("\n" . 'X is running fine with the new config file.' .
                 "\n\n", 0);
      kill(15, $childPid);
      close(CHILD);
      return 1;
    } else {
      print wrap ('Error: ' . $childStatus . '. X did not start.' .
            -e $xLogFile ? ' Details in ' . $xLogFile . "\n" :
            "\n", 0);
      return 0;
    }
  } else {
    error('Can not fork: ' . "$!\n") unless defined $childPid;
    # Child code
    exec @xargs;
  }
}

sub configure_X {
  my $xversion = '';
  my $xconfig_file = '';
  my $enableXImps2 = '';
  my $disableHotPlug = 'no';
  my $xversionAll = '';
  my $xconfig_backup = '';
  my $createNewXConf = 0;
  my $addXconfToDb = 0;
  my $major;
  my $minor;
  my $sub;

  if (xserver_bin() eq '') {
     print wrap ('No X install found.' . "\n\n", 0);
     return 'no';
  }

  if (vmware_product() eq 'tools-for-solaris' &&
      solaris_10_or_greater() eq 'yes' &&
      direct_command(shell_string($gHelper{'svcprop'}) . ' -p options/server '
                     . 'application/x11/x11-server') =~ /Xsun/) {
     if (get_answer("\n\n" . 'You are currently using the Solaris Xsun server.  '
                    . 'VMware Tools for Solaris only supports the Xorg server '
                    . '(which can be switched to by running kdmconfig(1M) as '
                    . 'root).  Would you like to configure the Xorg server now '
                    . 'so that you have the option of switching to it in the '
                    . 'future? (yes/no)', 'yesno', 'yes') eq 'no') {
        print wrap('Skipping X configuration.' . "\n\n", 0);
        return 'no';
     }
  }

  if (file_name_exist(xserver_xorg())) {
    if (is64BitElf(xserver_xorg())) {
      $gIs64BitX = 1;
      # 64-bit FreeBSD puts it's 64-bit X modules in lib not lib64
      if (vmware_product() ne 'tools-for-freebsd') {
	  $gXMouseDriverFile = "$cX64ModulesDir/input/vmmouse_drv.o";
	  $gXVideoDriverFile = "$cX64ModulesDir/drivers/vmware_drv.o";
      } elsif (vmware_product() eq 'tools-for-solaris') {
          $gXMouseDriverFile = "/usr/X11/lib/modules/input/amd64/vmmouse_drv.so";
          $gXVideoDriverFile = "/usr/X11/lib/modules/drivers/amd64/vmware_drv.so";
      } else {
	  $gXMouseDriverFile = "$cXModulesDir/input/vmmouse_drv.o";
	  $gXVideoDriverFile = "$cXModulesDir/drivers/vmware_drv.o";
      }
    } else {
      # Solaris' Xorg installation is in /usr/X11 (not /usr/X11R6)
      if (vmware_product() eq 'tools-for-solaris') {
        $gXMouseDriverFile = "/usr/X11/lib/modules/input/vmmouse_drv.so";
        $gXVideoDriverFile = "/usr/X11/lib/modules/drivers/vmware_drv.so";
      } else {
        $gXMouseDriverFile = "$cXModulesDir/input/vmmouse_drv.o";
        $gXVideoDriverFile = "$cXModulesDir/drivers/vmware_drv.o";
      }
    }
    ($xversion, $xconfig_file, $xversionAll, $disableHotPlug) = xorg();
  } elsif (file_name_exist(xserver4())){
    if (is64BitElf(xserver4())) {
      $gIs64BitX = 1;
      $gXMouseDriverFile = "$cX64ModulesDir/input/vmmouse_drv.o";
      $gXVideoDriverFile = "$cX64ModulesDir/drivers/vmware_drv.o";
    } else {
      $gXMouseDriverFile = "$cXModulesDir/input/vmmouse_drv.o";
      $gXVideoDriverFile = "$cXModulesDir/drivers/vmware_drv.o";
    }
    ($xversion, $xconfig_file, $xversionAll) = xfree_4();
  } elsif (file_name_exist(xserver_bin() . '/xterm')) {
    ($xversion, $xconfig_file, $xversionAll) = xfree_3();
  } else {
     print wrap ('No X install found.' . "\n\n", 0);
     return 'no';
  }

  ($major, $minor, $sub) = split_X_version($xversionAll);

  # $gNoXDrivers set to 1 means VMware tools didn't include 
  # appropriate drivers for the detected X version.
  if ($gNoXDrivers == 1) {
    print wrap('Skipping X configuration because X drivers are not included.' . "\n\n", 0);
    return 'no';
  }

  if (not defined $xconfig_file) {
    if (get_answer("\n\n" . 'Could not locate X ' . $xversionAll
                   . ' configuration file. Do you want to create a new'
                   . ' one? (yes/no)', 'yesno', 'yes') eq 'no') {
      print wrap ("\n\n" . 'Could not locate X ' . $xversionAll .
                  ' configuration file. X configuration skipped.' . "\n\n", 0);
      return 'no';
    }
    $xconfig_file = "/etc/X11/XF86Config" . ($xversion >= 4 ? '-4' : '');
    $createNewXConf = 1;
  } elsif (not file_name_exist($xconfig_file)) {
    if (get_answer("\n\n" . 'The configuration file ' . $xconfig_file
                   . ' can not be found. Do you want to create a new'
                   . ' one? (yes/no)', 'yesno', 'yes') eq 'no') {
      print wrap ("\n\n" . 'The configuration file ' . $xconfig_file .
                  ' can not be found. X configuration skipped.' . "\n\n", 0);
      return 'no';
    }
    $createNewXConf = 1;
  }
  if (-l $xconfig_file && !-e $xconfig_file) {
     print wrap ("\n\n" . 'The configuration file ' . $xconfig_file .
		 ' is a broken symlink. X configuration skipped.' . "\n\n", 0);
     return 'no';
  }

  # 7.4 does not need a new xorg.conf file.
  #
  # See bug 360333
  # If the OS is Fedora 9 or SLES11, then it needs to have 
  # its xorg file modified.
  #
  my $changeXConf = 1;
  if ($major == 7 && $minor == 4 && system(shell_string($gHelper{'grep'}) 
     . " -q 'Fedora release 9' /etc/fedora-release") != 0 &&
     system(shell_string($gHelper{'grep'}) . 
     " -q 'SUSE Linux Enterprise Server 11' /etc/SuSE-release") != 0) {
    $createNewXConf = 0;
    $changeXConf = 0;
  }

  $enableXImps2 = fix_mouse_file();

  $xconfig_backup = $xconfig_file . $cBackupExtension;
  # -e must be also tested because we don't want to unlink
  # a non-existed file
  if ((-e $xconfig_backup) && (!-s $xconfig_backup)) {
    unlink $xconfig_backup or
      die "Failed to cleanup empty $xconfig_backup file : $!\n";
  }

  # If the X config file does not exist, we need to add it to our database.  If
  # the X config file does exist, there are two cases: 1) we created it from
  # scratch during a previous Tools configuration, or 2) we are about to or
  # have already backed up the existing X config file.  For case 1, we don't
  # want to backup the file since we created it; for case 2, we need to backup
  # the file for restoring (if the backup already exists, backup_file_to_restore
  # will do the right thing).
  if ($createNewXConf == 1) {
    $addXconfToDb = 1;
  }
  elsif ($changeXConf == 1) {
    # Only backup the file if we didn't previously add it to the database
    if (not db_file_in($xconfig_file)) {
      backup_file_to_restore($xconfig_file, 'XCONFIG_FILE');
    }
  }

  my $tmp_dir = make_tmp_dir($cTmpDirPrefix);
  my $XF86tmp = $tmp_dir . '/XF86Config.' . $$;
  my $xLogFile = $tmp_dir . '/XF86ConfigLog.' . $$;

  if (-e $XF86tmp) {
    unlink $XF86tmp or
      die "Failed to cleanup old $XF86tmp file : $!\n";
  }
  if (-e $xLogFile) {
    unlink $xLogFile or
      die "Failed to cleanup old $xLogFile file : $!\n";
  }
  if (-e "$xLogFile.old" ) {
    unlink "$xLogFile.old" or
      die "Failed to cleanup old $xLogFile.old file : $!\n";
  }

  # Change the test file if we're using the original xorg.conf
  my $xconfigTestFile = $XF86tmp;
  if ($major == 7 && $minor == 4 && file_name_exist($xconfig_file)) {
    $xconfigTestFile = $xconfig_file;
  }

  if ($changeXConf == 1) {
    if ($createNewXConf == 0) {
      # Before installation, if there is no backup one, $xconfig_file will
      # be renamed to $xconfig_backup. So first $xconfig_file should be
      # checked if existed.
      if (-e $xconfig_file) {
	fix_X_conf($XF86tmp, $xconfig_file,
                   $xversion, $enableXImps2, $xversionAll, $disableHotPlug);
      } else {
	fix_X_conf($XF86tmp, $xconfig_backup,
                   $xversion, $enableXImps2, $xversionAll, $disableHotPlug);
      }
    } else {
      # If failed with existing $xconfig_file, try to generate a new x config
      # file with template one. The template file is also modified with fix_X_conf
      # to set right mouse protocol, driver, display, etc.
      fix_X_conf($XF86tmp, db_get_answer('LIBDIR') . '/configurator/XFree86-'
		 . ($xversion >= 4 ? '4' : $xversion) . '/XF86Config'
		 . ($xversion >= 4 ? '-4': ''),
                 $xversion, $enableXImps2, $xversionAll, $disableHotPlug);
    }
  }

  # 7.4 depends on the HAL daemon to autoload the correct modules
  # Now that the drivers have been installed, The HAL service must
  # be restarted.
  if ($major == 7 && $minor == 4) {
    my $initDir = shell_string(db_get_answer('INITSCRIPTSDIR'));
    $initDir =~ s/\'//g; # Remove quotes
    my $halScript = "";
    # Different systems, different names...
    if (-f "$initDir/haldaemon") {
      $halScript = "$initDir/haldaemon";
    }
    elsif (-f "$initDir/hal") {
      $halScript = "$initDir/hal";
    }
    else {
      print "Could not locate hal daemon init script.\n";
      print "Checked: " . $initDir . "/haldaemon and $initDir/hal\n";
    }
    if ($halScript ne "") {
      system($halScript . ' restart');
    }
  }

  # try_X_conf has problem with old X window, please refer to bug 78985
  if (vmware_product() eq 'tools-for-linux' &&
      ($major > 4 || ($major == 4 && $minor >= 2)) &&
      !try_X_conf($xconfigTestFile, $xLogFile)) {
    if (get_answer("\n\n" . 'The updated X config file does not work well.'
                   . ' See '. $xLogFile . ' for details. Do you want to create'
                   . ' a new one from template? Warning: if you choose to'
                   . ' create a new one, all old settings will be gone! (yes/no)',
                   'yesno', 'yes') eq 'no') {
      print wrap ('X configuration failed! The updated X config file does '
		  . 'not work well. File saved as ' . $XF86tmp . '. See '
		  . $xLogFile . ' for details.' . "\n\n");
      # Here temp files are not removed because user may want to check the
      # files to see what is wrong.
      return 'no';
    }
    unlink $XF86tmp;
    unlink $xLogFile;
    # If test failed with $XF86tmp, try to test a new $XF86tmp from template one.
    fix_X_conf($XF86tmp, db_get_answer('LIBDIR') . '/configurator/XFree86-'
               . ($xversion >= 4 ? '4' : $xversion) . '/XF86Config'
               . ($xversion >= 4 ? '-4': ''),
               $xversion, $enableXImps2, $xversionAll, $disableHotPlug);
    if (!try_X_conf($XF86tmp, $xLogFile)) {
      # Here temp files are not removed because user may want to check the
      # files to see what is wrong.
      return 'no';
    }
  }
  if ($changeXConf == 1) {
    if (system(shell_string($gHelper{'cp'}) . ' -p ' . $XF86tmp . ' ' .
	       $xconfig_file)) {
      print wrap ('Unable to copy the updated X config file to '
		  . $xconfig_file . "\n\n");
      # Here temp files are not removed because user may manually copy files
      # to right place.
      return 'no';
    }
    if ($addXconfToDb == 1) {
      db_add_file($xconfig_file, 0x0);
    }
  }
  unlink $XF86tmp;
  unlink $xLogFile;
  remove_tmp_dir($tmp_dir);
  return 'yes';
}


# Set to CUPS in the guest to use thinprint
sub configure_thinprint {
  my $lpadmin;
  my $cupsenable;
  my $cupsaccept;
  my $configText;
  my $printerName = 'VMware_Virtual_Printer';
  my $printerURI = 'tpvmlp://VMware';
  my $cupsDir = '/usr/' . (is64BitUserLand() ? 'lib64' : 'lib')  .
                '/cups/backend';
  my $cupsConfDir = '/etc/cups';
  my $cupsPrinters = "$cupsConfDir/printers.conf";
  my $cupsConf = "$cupsConfDir/cupsd.conf";
  my @backends =  ("$cupsDir/tpvmlp", "$cupsDir/tpvmgp");
  my $addDummyPrinter = 'false';

  # To continue, CUPS must be where we expect it on the guest.
  if (!file_name_exist($cupsDir) || !file_name_exist($cupsConf)) {
    return 0;
  }

  if (!file_name_exist($cupsPrinters)) {
    system("touch $cupsPrinters");
    system("chmod --reference=$cupsConf $cupsPrinters");
    system("chown --reference=$cupsConf $cupsPrinters");
  }

  if (!file_name_exist($cupsPrinters)) {
    return 0;
  }

  $configText = "<Printer ${printerName}>\n" .
                 "   Info ${printerName}\n" .
                 "   DeviceURI ${printerURI}\n" .
                 "   State Idle\n" .
                 "   Accepting Yes\n" .
                 "</Printer>\n";

  install_symlink(db_get_answer('LIBDIR') . '/configurator/thinprint.ppd',
                  $cupsConfDir . "/ppd/" . $printerName . ".ppd");

  install_symlink(db_get_answer('LIBDIR') . '/bin/vmware-tpvmlp',
                  "/usr/bin/" ."tpvmlp");

  install_symlink(db_get_answer('LIBDIR') . '/bin/vmware-tpvmlpd',
                  "/usr/bin/" ."tpvmlpd");

  foreach(@backends) {
     install_symlink(db_get_answer('LIBDIR') . '/bin/vmware-tpvmlp', $_);
     if (-e '/dev/ttyS0') { # workaround for EACCES issue on SuSE
        system("chgrp --reference=/dev/ttyS0 $_");
        system("chmod 0700 $_");
     }
  }

  install_symlink($gRegistryDir . '/tpvmlp.conf', '/etc/tpvmlp.conf');

  if ($addDummyPrinter eq 'true') {
     block_remove($cupsPrinters, $cupsPrinters . '.bak',
                  $cMarkerBegin, $cMarkerEnd);
     block_append($cupsPrinters . '.bak' , $cMarkerBegin, $configText, $cMarkerEnd);
     rename($cupsPrinters . '.bak', $cupsPrinters);
  }
  db_add_answer('THINPRINT_CONFED', 'yes');
}


# Set to automatically start vmware-user
sub configure_autostart_solaris {
  my $name;
  my $autostart;
  my $permissions;

  $permissions = 0555;
  $name = "/usr/dt/config/Xsession.d/9999.autostart-vmware-user.sh";
  $autostart = db_get_answer('LIBDIR') . '/configurator/autostart-vmware-user.sh';
  my %patch;

  if (not file_name_exist($name)) {
    undef %patch;
    my $autostart_dir = internal_dirname($name);
    if (not -d $autostart_dir) {
      create_dir($autostart_dir, 0);
    }
    install_file($autostart, $name, \%patch, $cFlagTimestamp);
    safe_chmod($permissions, $name);
  }
}


#
# configure_autostart_xdg --
#
#    Tests for the existence of well-known paths used to support the XDG
#    autostart mechanism.  For each path encountered, a vmware-user.desktop
#    symlink is installed which will cause XDG autostart aware session managers
#    to launch vmware-user as part of the user's session.
#
# Results:
#    Returns the following triple:
#       ((int)  number of symlinks installed,
#        (bool) 1 if a GNOME-specific directory was encountered,
#        (bool) 1 if a KDE-specific directory was encountered)
#

sub configure_autostart_xdg {
   # /path/to/vmware-user.desktop.
   my $dotDesktop = "$gRegistryDir/vmware-user.desktop";
   my $numSymlinks = 0;

   my $foundGnome = 0;
   my $foundKde = 0;

   my %autodirs = (
      "/etc/xdg/autostart" => undef,
      "/usr/share/autostart" => undef,
      "/usr/share/gnome/autostart" => undef,
      # FreeBSD, compiled from source, and maybe Gentoo?
      "/usr/local/share/autostart" => undef,
      "/usr/local/share/gnome/autostart" => undef,
      # SuSE-style.
      "/opt/gnome/share/autostart" => undef,
      "/opt/kde/share/autostart" => undef,
      "/opt/kde3/share/autostart" => undef,
      "/opt/kde4/share/autostart" => undef,
   );

   # If KDE is available, use kde-config to search for its install path,
   # and add that to the list of autostart directories.
   #
   # Since PATH is overridden in main(), test for other common locations
   # of kde-config (using the augmented argument to internal_which).
   my $kdeConfig = internal_which("kde-config", 1);
   if ($kdeConfig ne '' && -x $kdeConfig) {
     #
     # Okay, we have a valid kde-config.  Query it for its installation
     # prefix, then if an autostart path exists, add it to autodirs.
     #
     my $kdePrefix = direct_command(shell_string($kdeConfig) . " --prefix");
     chomp($kdePrefix);
     my $kdeAutostart = "$kdePrefix/share/autostart";
     if (-d $kdeAutostart) {
        $autodirs{$kdeAutostart} = undef;
        $foundKde = 1;
     }
   }

   foreach (keys(%autodirs)) {
      if (-d $_) {
         install_symlink($dotDesktop, "$_/vmware-user.desktop");
         # At time of publishing, all versions of gnome-session supporting
         # XDG autostart do so via a GNOME-specific autostart directory.
         $foundGnome = 1 if $_ =~ /gnome/;
         $foundKde = 1 if $_ =~ /kde/;
         ++$numSymlinks;
      }
   }

   return ($numSymlinks, $foundGnome, $foundKde);
}


#
# configure_autostart_legacy_xdm --
#
#    Jump through hoops to launch vmware-user as part of xdm's Xsession script.
#
# Results:
#    If applicable, xdm will now launch vmware-user before executing its usual
#    Xsession script.
#
#    Returns the number of xdm-config files modified.
#

sub configure_autostart_legacy_xdm {
   my $x11Base = internal_dirname(xserver_bin());
   db_add_answer('X11DIR', $x11Base);

   my $chompedMarkerBegin = $cMarkerBegin;
   chomp($chompedMarkerBegin);

   my $modCount = 0;

   # X.Org's XDM
   #  - Determine X11BASE.
   #  - Touch xdm-config to source our Xresources.
   my $xResources = "$gRegistryDir/vmware-user.Xresources";
   my $xSessionXDM = "$gRegistryDir/xsession-xdm.sh";
   my @xdmcfgs = ("$x11Base/lib/X11/xdm/xdm-config", "/etc/X11/xdm/xdm-config");
   foreach (@xdmcfgs) {
      if (file_name_exist($_)) {
         if (block_match($_, "!$chompedMarkerBegin")) {
            block_restore($_, $cMarkerBegin, $cMarkerEnd);
         }
         block_append($_, "!$cMarkerBegin", "#include \"$xResources\"\n",
                      "!$cMarkerEnd");
         ++$modCount;
      }
   }

   return $modCount;
}


#
# configure_autostart_legacy_gdm --
#
#    Attempt to launch vmware-user via gdm.
#
# Results:
#    If applicable, we place a script in /etc/X11/xinit/xinitrc.d, causing gdm
#    to launch vmware-user before its usual Xsession script.
#
#    Returns 1 if the symlink was installed, and 0 otherwise.
#

sub configure_autostart_legacy_gdm {
   # GNOME's GDM (legacy)
   #  - Symlink xsession-gdm to /etc/X11/xinit/xinitrc.d/vmware-user.sh.
   #    (This is a hardcoded path in gdm sources.)
   my $xSessionGDM = "$gRegistryDir/xsession-gdm.sh";
   my $xinitrcd = "/etc/X11/xinit/xinitrc.d";
   if (-d $xinitrcd) {
      install_symlink($xSessionGDM, "$xinitrcd/vmware-xsession-gdm.sh");
      return 1;
   }

   return 0;
}


#
# configure_autostart_legacy_suse --
#
#    Hook into /etc/X11/xinit/xinitrc.common to launch vmware-user.
#
#     - All packaged display managers point users to a consolidated
#       /etc/X11/xdm/Xsession script.  (Even in the GDM case, SUSE's scheme
#       overrides what we'd use above.)
#     - This Xsession script sources /etc/X11/xinit/xinitrc.common for common
#       autostart tasks.  Seems like a perfect fit for us.
#
# Results:
#    If applicable, we'll insert a few lines into xinitrc.common, and all
#    display managers on SuSE 10 will launch vmware-user during X11 session
#    startup.
#
#    Returns 1 if xinitrc.common was modified, and 0 otherwise.
#

sub configure_autostart_legacy_suse {
   my $startCommand = shift;    # Bourne-shell compatible string used to launch
                                # vmware-user.

   my $xinitrcCommon = '/etc/X11/xinit/xinitrc.common';

   my $chompedMarkerBegin = $cMarkerBegin;
   chomp($chompedMarkerBegin);


   if (file_name_exist($xinitrcCommon)) {
      if (block_match($xinitrcCommon, $chompedMarkerBegin)) {
         block_restore($xinitrcCommon, $cMarkerBegin, $cMarkerEnd);
      }
      block_append($xinitrcCommon, $cMarkerBegin, $startCommand . "\n",
                   $cMarkerEnd);
      return 1;
   }

   return 0;
}


#
# configure_autostart_legacy_debian --
#
#    Hook into /etc/X11/Xsession.d/*.sh to launch vmware-user.
#
# Results:
#    If applicable, we'll create a 99-vmware_vmware-user script in the
#    Xsession.d directory, and vmware-user will launch during X11 session
#    startup.
#
#    Returns 1 if we dropped in a script, and 0 otherwise.
#

sub configure_autostart_legacy_debian {
   my $startCommand = shift;    # Bourne-shell compatible string used to launch
                                # vmware-user.

   my $chompedMarkerBegin = $cMarkerBegin;
   chomp($chompedMarkerBegin);

   my $xSessionD = '/etc/X11/Xsession.d';
   my $xSessionDst = "$xSessionD/99-vmware_vmware-user";

   my $tmpBlock = <<__EOF;
#
# This script is intended only as a last resort in order to launch the VMware
# User Agent (vmware-user) in legacy Debian and Ubuntu VMs whose shipped X11
# session managers may not support XDG/KDE-style autostart via .desktop files.
#
__EOF

   if (-d $xSessionD) {
      if (block_match($xSessionDst, $chompedMarkerBegin)) {
         block_restore($xSessionDst, $cMarkerBegin, $cMarkerEnd);
      }
      block_append($xSessionDst, $cMarkerBegin,
                   $tmpBlock . "\n" . $startCommand . "\n",
                   $cMarkerEnd);

      db_add_file($xSessionDst, 0);
      return 1;
   }

   return 0;
}


#
# configure_autostart_legacy --
#
#    Use unconventional hooks to launch vmware-user at X session startup.
#    This is intended only for guests which do not support XDG-style
#    autostart.
#
#    This routine will make use of hooks provided by the following:
#      - OpenSuSE 10's xinitrc.common
#      - Debian, Ubuntu via Xsession.d
#      - xdm (all known versions)
#      - gdm (2.2.3 and above)
#
#    The vendor specific methods are preferred, so if either of those succeeds,
#    we'll avoid calling into the xdm & gdm routines.
#
# Results:
#    If applicable, we may insert vmware-user autostart hooks.
#

sub configure_autostart_legacy {
   #
   # This is the vmware-user launch command for pre-XDG autostart guests.  The
   # delay is intended to prefer XDG-style launch for guests where we may
   # accidentally use both pre- and post-XDG autostart hooks.
   #
   my ($sleepingAgentDelay) = 15;       # Give session managers a 15s head start.
   my ($sleepingAgentCommand) =
      sprintf("{ sleep %d && %s/%s &>/dev/null ; } &",
              $sleepingAgentDelay, db_get_answer('BINDIR'), 'vmware-user');

   if ((configure_autostart_legacy_suse($sleepingAgentCommand) == 0) &&
       (configure_autostart_legacy_debian($sleepingAgentCommand) == 0)) {
      configure_autostart_legacy_xdm();
      configure_autostart_legacy_gdm();
   }
}


#
# configure_autostart --
#
#    Configures the system to launch vmware-user as part of users' graphical
#    sessions.
#
#    This routine is heuristically inclined.  We'll install XDG style .desktop
#    files if any paths exist, but we'll use legacy autostart hooks only if
#    we have reason to believe that the XDG solution didn't fully apply to
#    this guest.
#
#    E.g., GNOME was a little late to the .desktop autostart party.  So a
#    machine with both slightly older GNOME and KDE installed may have
#    an autostart directory present under $datadir/autostart, but it may
#    not be used by GNOME.  If this is the case (indicated by $foundGnomeStart
#    being false), then we opt to continue and use some of the legacy
#    install hooks.
#

sub configure_autostart {
   my @sessionsDirs;
   my $hasGnome = 0;
   my $hasKde = 0;

   my $numSymlinks;
   my $foundGnomeStart;
   my $foundKdeStart;

   my $existingDirs = 0;

   #
   # We forgot to fully clean up after ourselves when uninstalling Tools.  As a
   # result, users who upgrade Tools may find vmware-user launched via a
   # "legacy" autostart mechanism outside the context of their desktop (GNOME,
   # KDE, Xfce, etc.) session.  (This breaks features like GHI.)  The unfortunate
   # workaround is to simply take that step here.
   #
   # NB: This affects only users who installed Tools with beta versions of
   # Workstation and Fusion*, so we can likely pull this block out (while still
   # leaving the corresponding call in the uninstaller) in the next release.
   #
   # * This refers to any version of Tools that included the decoupled
   #   vmware-{user,guestd}.
   #
   unconfigure_autostart_legacy($cMarkerBegin, $cMarkerEnd);

   @sessionsDirs = ('/usr/share/xsessions',
                    '/usr/local/share/xsessions',
                    '/usr/X11R6/share/xsessions');

   foreach (@sessionsDirs) {
      next unless -d $_;
      my @tmpArray;

      @tmpArray = glob("$_/gnome.desktop");
      $hasGnome = 1 if $#tmpArray != -1;

      @tmpArray = glob("$_/kde*.desktop");
      $hasKde = 1 if $#tmpArray != -1;

      ++$existingDirs;
   }

   if ($existingDirs == 0) {
      $hasGnome =
         defined internal_which('gnome', 1) ||
         defined internal_which('gnome-session', 1);
      $hasKde =
         defined internal_which('startkde', 1) ||
         defined internal_which('ksmserver', 1);
   }

   ($numSymlinks, $foundGnomeStart, $foundKdeStart) = configure_autostart_xdg();

   # Fall back to legacy autostart if
   #    a.  no XDG symlinks were installed, or
   #    b.  user employs older GNOME but we were unable to find a GNOME-
   #        supported XDG path, or
   #    c.  s/GNOME/KDE/g  (less likely)
   if (($numSymlinks == 0) ||
       ($hasGnome && !$foundGnomeStart) ||
       ($hasKde && !$foundKdeStart)) {
      configure_autostart_legacy();
   }
}


# Get a network name from the user
sub get_network_name_answer {
  my $vHubNr = shift;
  my $default = shift;
  get_persistent_answer('Please specify a name for this network.',
                        'VNET_' . $vHubNr . '_NAME',
                        'netname', $default);
}

# Configuration of bridged networking
sub configure_bridged_net {
  my $vHubNr = shift;
  my $vHostIf = shift;
  my $answer;

  if ($vHubNr < $gMinVmnet || $vHubNr > $gMaxVmnet) {
    print wrap('Number of virtual networks exceeded.  Not creating virtual '
               . 'network.' . "\n\n", 0);
    return;
  }

  # If this wasn't a bridged network before, wipe out the old configuration
  # info as it may confuse us later.
  if (!is_bridged_network($vHubNr)) {
    remove_net($vHubNr, $vHostIf);
  }

  print wrap('Configuring a bridged network for vmnet' . $vHubNr . '.'
             . "\n\n", 0);

  if (vmware_product() eq 'wgs') {
     get_network_name_answer($vHubNr, 'Bridged');
  }

  if (count_all_networks() == 0 && $#gAllEthIf == -1) {
    # No interface.  We provide a valid default so that everything works.
    make_bridged_net($vHubNr, $vHostIf, "eth0");
    return;
  }

  if ($#gAvailEthIf == 0) {
    # Only one interface.  Use it.  This gives no choice even when the editor
    # is being used.
    make_bridged_net($vHubNr, $vHostIf,  $gAvailEthIf[0]);
    return;
  }

  if ($#gAvailEthIf == -1) {
    # We have other interfaces, but they have all been allocated.
    if (get_answer('All your ethernet interfaces are already bridged.  Are '
                   . 'you sure you want to configure a bridged ethernet '
                   . 'interface for vmnet' . $vHubNr . '? (yes/no)',
                   'yesno', 'no') eq 'no') {
      print wrap('Not changing network settings for vmnet' . $vHubNr . '.'
                 . "\n\n", 0);
      return;
    }
    $answer = get_persistent_answer('Your computer has the following ethernet '
                                    . 'devices: ' . join(', ', @gAllEthIf)
                                    . '. Which one do you want to bridge to '
                                    . 'vmnet' . $vHubNr . '?',
                                    'VNET_' . $vHubNr . '_INTERFACE',
                                    'anyethif', 'eth0');
    make_bridged_net($vHubNr, $vHostIf, $answer);
    return;
  }

  my $queryString = 'Your computer has multiple ethernet network interfaces '
                    . 'available: ' . join(', ', @gAvailEthIf) . '. Which one '
                    . 'do you want to bridge to vmnet' . $vHubNr . '?';
  $answer = get_persistent_answer($queryString,
                                  'VNET_' . $vHubNr . '_INTERFACE',
                                  'availethif', 'eth0');
  make_bridged_net($vHubNr, $vHostIf, $answer);
}

# Creates a bridged network.
sub make_bridged_net {
  my $vHubNr = shift;
  my $vHostIf = shift;
  my $ethIf = shift;

  # Need to make sure the NAME key is present so that netmap.conf is created properly.
  my $net_name = db_get_answer_if_exists('VNET_' . $vHubNr . '_NAME');
  if (not defined($net_name)) {
    db_add_answer('VNET_' . $vHubNr . '_NAME', 'Bridged-' . $vHubNr);
  }

  db_add_answer('VNET_' . $vHubNr . '_INTERFACE', $ethIf);
  db_remove_answer('VNET_' . $vHubNr . '_DHCP');
  configure_dev('/dev/' . $vHostIf, 119, $vHubNr, 1);

  # Reload the list of available ethernet adapters
  load_ethif_info();
}

# Probe for an unused private subnet
# Return value is (status, subnet, netmask).
#  status is 1 on success (subnet and netmask are set),
#  status is 0 on failure.
sub subnet_probe {
  my $vHubNr = shift;
  my $vHostIf = shift;
  # Ref to an array of used subnets
  my $usedSubnets = shift;
  my $i;
  my @subnets;
  my $tries;
  my $maxTries = 100;
  my $pings;
  my $maxPings = 10;
  # XXX We only consider class C subnets for the moment
  my $netmask = '255.255.255.0';
  my %used_subnets;

  # Generate the table of private class C subnets
  @subnets = ();
  for ($i = 0; $i < 255; $i++) {
    $subnets[2 * $i    ] = '192.168.' . $i . '.0';
    $subnets[2 * $i + 1] = '172.16.'  . $i . '.0';
  }

  # Generate a list of used subnets and clear out the ones that have already
  # been used
  foreach $i (@$usedSubnets) {
    $used_subnets{$i} = 1;
  }
  for ($i = 0; $i < $#subnets + 1; $i++) {
    if ($used_subnets{$subnets[$i]}) {
      $subnets[$i] = '';
    }
  }

  print wrap('Probing for an unused private subnet (this can take some '
             . 'time)...' . "\n\n", 0);
  $tries = 0;
  $pings = 0;
  srand(time);
  # Beware, 'last' doesn't seem to work in 'do'-'while' loops
  for (;;) {
    my $r;
    my $subnet;
    my $status;

    $tries++;

    $r = int(rand($#subnets + 1));
    if ($subnets[$r] eq '') {
      # Already tried
      if ($tries == $maxTries) {
        print STDERR wrap('We were unable to locate an unused Class C subnet '
                          . 'in the range of private network numbers.  For '
                          . 'each subnet that we tried we received a response '
                          . 'to our ICMP ping packets from a ' . $machine
                          . ' at the network address intended for assignment '
                          . 'to this machine.  Because no private subnet '
                          . 'appears to be unused you will need to explicitly '
                          . 'specify a network number by hand.' . "\n\n", 0);
        return (0, undef, undef);
      }
      next;
    }
    $subnet = $subnets[$r];
    $subnets[$r] = '';

    # Do a broadcast ping for <subnet>.255 .
    $status = system(shell_string(db_get_answer('BINDIR') . '/vmware-ping')
                     . ' -q ' . ' -b '
                     . shell_string(int_to_quaddot(quaddot_to_int($subnet)
                                                   + 255))) >> 8;

    if ($status == 1) {
      # Some OSes are configured to ignore broadcast ping,
      # so we ping <subnet>.1 .
      $status = system(shell_string(db_get_answer('BINDIR') . '/vmware-ping')
                       . ' -q '
                       . shell_string(int_to_quaddot(quaddot_to_int($subnet)
                                                   + 1))) >> 8;
      if ($status == 1) {
        print wrap('The subnet ' . $subnet . '/' . $netmask . ' appears to be '
                   . 'unused.' . "\n\n", 0);
        return (1, $subnet, $netmask);
      }
    }

    if ($status == 3) {
      print STDERR wrap('We were unable to locate an unused Class C subnet in '
                        . 'the range of private network numbers.  You will '
                        . 'need to explicitly specify a network number by '
                        . 'hand.' . "\n\n", 0);
      return (0, undef, undef);
    }

    if ($status == 2) {
      print STDERR wrap('Either your ' . $machine . ' is not connected to an '
                        . 'IP network, or its network configuration does not '
                        . 'specify a default IP route.  Consequently, the '
                        . 'subnet ' . $subnet . '/' . $netmask . ' appears to '
                        . 'be unused.' . "\n\n", 0);
      return (1, $subnet, $netmask);
    }

    $pings++;
    if (($pings == $maxPings) || ($tries == $maxTries)) {
      print STDERR wrap('We were unable to locate an unused Class C subnet in '
                        . 'the range of private network numbers.  For each '
                        . 'subnet that we tried we received a response to our '
                        . 'ICMP ping packets from a ' . $machine . ' at the '
                        . 'network address intended for assignment to this '
                        . 'machine.  Because no private subnet appears to be '
                        . 'unused you will need to explicitly specify a '
                        . 'network number by hand.' . "\n\n", 0);
      return (0, undef, undef);
    }
  }
}

# Converts an quad-dotted IPv4 address into a integer
sub quaddot_to_int {
  my $quaddot = shift;
  my @quaddot_a;
  my $int;
  my $i;

  @quaddot_a = split(/\./, $quaddot);
  $int = 0;
  for ($i = 0; $i < 4; $i++) {
    $int <<= 8;
    $int |= $quaddot_a[$i];
  }

  return $int;
}

# Converts an integer into a quad-dotted IPv4 address
sub int_to_quaddot {
  my $int = shift;
  my @quaddot_a;
  my $i;

  for ($i = 3; $i >= 0; $i--) {
    $quaddot_a[$i] = $int & 0xFF;
    $int >>= 8;
  }

  return join('.', @quaddot_a);
}

# Compute the subnet address associated to a couple IP/netmask
sub compute_subnet {
  my $ip = shift;
  my $netmask = shift;

  return int_to_quaddot(quaddot_to_int($ip) & quaddot_to_int($netmask));
}

# Compute the broadcast address associated to a couple IP/netmask
sub compute_broadcast {
  my $ip = shift;
  my $netmask = shift;

  return   int_to_quaddot(quaddot_to_int($ip)
         | (0xFFFFFFFF - quaddot_to_int($netmask)));
}

# Makes the patch hash that is used to replace the options in the dhcpd config
# file.
# These DHCP options are needed for the hostonly network.
sub make_dhcpd_patch {
  my $vHubNr = shift;
  my $vHostIf = shift;
  my %patch;

  undef %patch;
  $patch{'%vmnet%'} = $vHostIf;
  $patch{'%hostaddr%'} = db_get_answer('VNET_' . $vHubNr
                                       . '_HOSTONLY_HOSTADDR');
  $patch{'%netmask%'} = db_get_answer('VNET_' . $vHubNr . '_HOSTONLY_NETMASK');
  $patch{'%network%'} = db_get_answer_if_exists('VNET_' . $vHubNr . '_HOSTONLY_SUBNET');
  if (not defined($patch{'%network%'})) {
     $patch{'%network%'} = compute_subnet($patch{'%hostaddr%'}, $patch{'%netmask%'});
  }
  $patch{'%broadcast%'} = compute_broadcast($patch{'%hostaddr%'},
                                            $patch{'%netmask%'});
  # Median address in this subnet
  $patch{'%range_low%'} = int_to_quaddot(
    (quaddot_to_int($patch{'%network%'})
     + quaddot_to_int($patch{'%broadcast%'}) + 1) / 2);
  # Last normal address in this subnet
  $patch{'%range_high%'} = int_to_quaddot(
    quaddot_to_int($patch{'%broadcast%'}) - 1);
  $patch{'%router_option%'} = "";
  return %patch;
}

# Write VMware's DHCPd configuration files
sub write_dhcpd_config {
  my $vHubNr = shift;
  my $vHostIf = shift;
  # Function that makes the patch needed for the DHCP config file
  my $make_patch_func = shift;
  my $dhcpd_dir;
  my %patch;

  %patch = &$make_patch_func($vHubNr, $vHostIf);

  # Create the dhcpd config directory (one per virtual interface)
  $dhcpd_dir = $gRegistryDir . '/' . $vHostIf . '/dhcpd';
  create_dir($dhcpd_dir, $cFlagDirectoryMark);

  install_file(db_get_answer('LIBDIR') . '/configurator/vmnet-dhcpd.conf',
               $dhcpd_dir . '/dhcpd.conf', \%patch,
               $cFlagTimestamp | $cFlagConfig);

  # Create empty files that will be created by the daemon
  # They will be modified by the daemon, don't timestamp them
  undef %patch;
  install_file('/dev/null', $dhcpd_dir . '/dhcpd.leases', \%patch, 0);
  safe_chmod(0644, $dhcpd_dir . '/dhcpd.leases');
  undef %patch;
  install_file('/dev/null', $dhcpd_dir . '/dhcpd.leases~', \%patch, 0);
  safe_chmod(0644, $dhcpd_dir . '/dhcpd.leases~');
}

# Check the normal dhcp configuration and give advises
sub dhcpd_consultant {
  my $vHubNr = shift;
  my $vHostIf = shift;
  my $conf;
  my $network;
  my $netmask;

  if (-r '/etc/dhcpd.conf') {
    $conf = '/etc/dhcpd.conf';
  } else {
    return;
  }

  $netmask = db_get_answer('VNET_' . $vHubNr . '_HOSTONLY_NETMASK');
  $network = compute_subnet(db_get_answer('VNET_' . $vHubNr
                                          . '_HOSTONLY_HOSTADDR'), $netmask);

  # The host has a normal dhcpd setup
  if (direct_command(
    shell_string($gHelper{'grep'}) . ' '
    . shell_string('^[ ' . "\t" . ']*subnet[ ' . "\t" . ']*' . $network) . ' '
    . shell_string($conf)) eq '') {
    query('This system appears to have a DHCP server configured for normal '
          . 'use.  Beware that you should teach it how not to interfere with '
          . vmware_product_name() . '\'s DHCP server.  There are two ways to '
          . 'do this:' . "\n\n" . '1) Modify the file ' . $conf . ' to add '
          . 'something like:' . "\n\n" . 'subnet ' . $network . ' netmask '
          . $netmask . ' {' . "\n" . '    # Note: No range is given, '
          . 'vmnet-dhcpd will deal with this subnet.' . "\n" . '}' . "\n\n"
          . '2) Start your DHCP server with an explicit list of network '
          . 'interfaces to deal with (leaving out ' . $vHostIf . '). e.g.:'
          . "\n\n" . 'dhcpd eth0' . "\n\n" . 'Consult the dhcpd(8) and '
          . 'dhcpd.conf(5) manual pages for details.' . "\n\n"
          . 'Hit enter to continue.', '', 0);
  }
}

# Configuration of hostonly networking
sub configure_hostonly_net {
  my $vHubNr = shift;
  my $vHostIf = shift;
  my $run_dhcpd = shift;
  my $hostaddr;
  my $subnet;
  my $netmask;
  my $status;

  if ($vHubNr < $gMinVmnet || $vHubNr > $gMaxVmnet) {
    print wrap('Number of virtual networks exceeded.  Not creating virtual '
               . 'network.' . "\n\n", 0);
    return;
  }

  # If this wasn't a hostonly network before, wipe out the old configuration
  # info as it may confuse us later.
  if (!is_hostonly_network($vHubNr)) {
    remove_net($vHubNr, $vHostIf);
  }

  print wrap('Configuring a host-only network for vmnet' . $vHubNr . '.'
             . "\n\n", 0);

  if (vmware_product() eq 'wgs') {
     get_network_name_answer($vHubNr, 'HostOnly');
  }

  my $keep_settings;

  $keep_settings = 'no';
  $hostaddr = $gDBAnswer{'VNET_' . $vHubNr . '_HOSTONLY_HOSTADDR'};
  $netmask = $gDBAnswer{'VNET_' . $vHubNr . '_HOSTONLY_NETMASK'};

  if (defined($hostaddr) && defined($netmask)) {
    $subnet = db_get_answer_if_exists('VNET_' . $vHubNr . '_HOSTONLY_SUBNET');
    if (not defined($subnet)) {
       $subnet = compute_subnet($hostaddr, $netmask);
    }
    $keep_settings = get_answer('The host-only network is currently '
                                . 'configured to use the private subnet '
                                . $subnet . '/' . $netmask . '.  Do you want '
                                . 'to keep these settings?', 'yesno', 'yes');
  }

  if ($keep_settings eq 'no') {
    # Get the new settings
    for (;;) {
      my $answer;

      $answer = get_answer('Do you want this program to probe for an unused '
                           . 'private subnet? (yes/no/help)',
                           'yesnohelp', 'yes');

      if ($answer eq 'yes') {
        my @usedSubnets = get_used_subnets();
        ($status, $subnet, $netmask) = subnet_probe($vHubNr, $vHostIf,
                                                    \@usedSubnets);
        if ($status) {
          last;
        }
        # Fallback
        $answer = 'no';
      }

      if ($answer eq 'no') {
	do {
	  # An empty default answer is dangerous as running
	  # with --default can cause an infinite loop, however
	  # --default will never make it here, so we're safe.
	  $hostaddr = get_answer('What will be the IP address of '
				 . 'your ' . $machine . ' on the '
				 . 'private network?',
				 'ip', '');
	  $netmask = get_answer('What will be the netmask of your '
				. 'private network?',
				'ip', '');
        } until (is_good_network($hostaddr, $netmask) eq 'yes');
	$subnet = compute_subnet($hostaddr, $netmask);
	last;
      }

      print wrap('Virtual machines configured to use host-only networking are '
                 . 'placed on a virtual network that is confined to this '
                 . $machine . '.  Virtual machines on this network can '
                 . 'communicate with each other and the ' . $os . ', but no '
                 . 'one else.' . "\n\n" . 'To setup this host-only networking '
                 . 'you need to select a network number that is normally '
                 . 'unreachable from the ' . $machine . '.  We can '
                 . 'automatically select this number for you, or you can '
                 . 'specify a network number that you want.' . "\n\n" . 'The '
                 . 'automatic selection process works by testing a series of '
                 . 'Class C subnet numbers to see if they are reachable from '
                 . 'the ' . $machine . '.  The first one that is unreachable '
                 . 'is used.  The subnet numbers are chosen from the private '
                 . 'network numbers specified by the Internet Engineering '
                 . 'Task Force (IETF) in RFC 1918 (http://www.isi.edu/in-notes'
                 . '/rfc1918.txt).' . "\n\n" . 'Remember that the host-only '
                 . 'network that virtual machines reside on will not be '
                 . 'accessible outside the host machine.  This means that it '
                 . 'is OK to use the same number on different systems so long '
                 . 'as you do not enable communication between these networks.'
                 . "\n\n", 0);
    }
  }

  make_hostonly_net($vHubNr, $vHostIf, $subnet, $netmask, $run_dhcpd);

  if ($run_dhcpd) {
    dhcpd_consultant($vHubNr, $vHostIf);
  }
}

# Creates a hostonly network
sub make_hostonly_net {
  my $vHubNr = shift;
  my $vHostIf = shift;
  my $subnet = shift;
  my $netmask = shift;
  my $run_dhcpd = shift;

  my $hostaddr = int_to_quaddot(quaddot_to_int($subnet) + 1);

  configure_dev('/dev/' . $vHostIf, 119, $vHubNr, 1);

  db_add_answer('VNET_' . $vHubNr . '_HOSTONLY_HOSTADDR', $hostaddr);
  db_add_answer('VNET_' . $vHubNr . '_HOSTONLY_NETMASK', $netmask);
  db_add_answer('VNET_' . $vHubNr . '_HOSTONLY_SUBNET', $subnet);
  db_add_answer('VNET_' . $vHubNr . '_DHCP', 'yes');

  if ($run_dhcpd) {
    write_dhcpd_config($vHubNr, $vHostIf, \&make_dhcpd_patch);
  } else {
    # XXX NOT IMPLEMENTED
  }

  # Unmake Samba just in case they have it from a previous product version
  if (defined($gDBAnswer{'NETWORKING'}) && get_samba_net() != -1) {
    unmake_samba_net($vHubNr, $vHostIf);
  }
}

# Get the list of subnets used by all the hostonly networks
sub get_hostonly_subnets {
  my $vHubNr;
  my @subnets = ();
  for ($vHubNr = $gMinVmnet; $vHubNr <= $gMaxVmnet; $vHubNr++) {
    if (is_hostonly_network($vHubNr)) {
      my $hostaddr = db_get_answer('VNET_' . $vHubNr  . '_HOSTONLY_HOSTADDR');
      my $netmask = db_get_answer('VNET_' . $vHubNr  . '_HOSTONLY_NETMASK');
      push(@subnets, compute_subnet($hostaddr, $netmask));
    }
  }
  return @subnets;
}

# Unconfigures Samba from the hostonly network
sub unmake_samba_net {
  my $vHubNr = shift;
  my $vHostIf = shift;
  my $smb_dir = $gRegistryDir . '/' . $vHostIf . '/smb';
  if (is_samba_running($vHubNr)) {
    db_remove_answer('VNET_' . $vHubNr . '_SAMBA');
    db_remove_answer('VNET_' . $vHubNr . '_SAMBA_MACHINESID');
    db_remove_answer('VNET_' . $vHubNr . '_SAMBA_SMBPASSWD');
    uninstall_prefix($smb_dir);
  }
  db_add_answer('VNET_' . $vHubNr . '_SAMBA', 'no');
}

# Gets the virtual network number where Samba is located.
sub get_samba_net {
  my $vHubNr;

  for ($vHubNr = $gMinVmnet; $vHubNr <= $gMaxVmnet; $vHubNr++) {
    if (is_samba_running($vHubNr)) {
      return $vHubNr;
    }
  }

  return -1;
}

# Check to see if the Samba question was ever asked and answered.
# Returns 1 if it has. 0 otherwise.
sub was_samba_answered {
  my $vHubNr;

  for ($vHubNr = $gMinVmnet; $vHubNr <= $gMaxVmnet; $vHubNr++) {
    if (defined($gDBAnswer{'VNET_' . $vHubNr . '_SAMBA'})) {
      return 1;
    }
  }
  return 0;
}

# Configuration of NAT networking
sub configure_nat_net {
  my $vHubNr = shift;
  my $vHostIf = shift;
  my $hostaddr;
  my $subnet;
  my $netmask;
  my $status;

  if ($vHubNr < $gMinVmnet || $vHubNr > $gMaxVmnet) {
    print wrap('Number of virtual networks exceeded.  Not creating virtual '
               . 'network.' . "\n\n", 0);
    return;
  }

  # If this wasn't a NAT network before, wipe out the old configuration info
  # as it may confuse us later.
  if (!is_nat_network($vHubNr)) {
    remove_net($vHubNr, $vHostIf);
  }

  print wrap('Configuring a NAT network for vmnet' . "$vHubNr." . "\n\n", 0);

  if (vmware_product() eq 'wgs') {
     get_network_name_answer($vHubNr, 'NAT');
  }

  my $keep_settings;

  $keep_settings = 'no';
  $hostaddr = $gDBAnswer{'VNET_' . $vHubNr . '_HOSTONLY_HOSTADDR'};
  $netmask = $gDBAnswer{'VNET_' . $vHubNr . '_HOSTONLY_NETMASK'};

  if (defined($hostaddr) && defined($netmask)) {
    $subnet = $gDBAnswer{'VNET_' . $vHubNr . '_HOSTONLY_SUBNET'};
    if (not defined($subnet)) {
       $subnet = compute_subnet($hostaddr, $netmask);
    }
    $keep_settings = get_answer('The NAT network is currently configured to '
                                . 'use the private subnet ' . $subnet . '/'
                                . $netmask . '.  Do you want to keep these '
                                . 'settings?', 'yesno', 'yes');
  }

  if ($keep_settings eq 'no') {
    # Get the new settings
    for (;;) {
      my $answer;

      $answer = get_answer('Do you want this program to probe for an unused '
                           . 'private subnet? (yes/no/help)',
                           'yesnohelp', 'yes');

      if ($answer eq 'yes') {
        my @usedSubnets = get_used_subnets();
        ($status, $subnet, $netmask) = subnet_probe($vHubNr, $vHostIf,
                                                    \@usedSubnets);
        if ($status) {
          last;
        }
        # Fallback
        $answer = 'no';
      }

      if ($answer eq 'no') {
	do {
	  # An empty default answer is dangerous as running
	  # with --default can cause an infinite loop, however
	  # --default will never make it here, so we're safe.
	  $hostaddr = get_answer('What will be the IP address of '
				 . 'your ' . $machine . ' on the '
				 . 'private network?',
				 'ip', '');
	  $netmask = get_answer('What will be the netmask of your '
				. 'private network?',
				'ip', '');
        } until (is_good_network($hostaddr, $netmask) eq 'yes');
	$subnet = compute_subnet($hostaddr, $netmask);
	last;
      }

      print wrap('Virtual machines configured to use NAT networking are '
                 . 'placed on a virtual network that is confined to this '
                 . $machine . '.  Virtual machines on this network can '
                 . 'communicate with the network through the NAT process, '
                 . 'with each other, and with the ' . $os . '.' . "\n\n"
                 . 'To setup NAT networking you need to select a network '
                 . 'number that is normally unreachable from the ' . $machine
                 . '.  We can automatically select this number for you, or '
                 . 'you can specify a network number that you want.' . "\n\n"
                 . 'The automatic selection process works by testing a series '
                 . 'of Class C subnet numbers to see if they are reachable '
                 . 'from the ' . $machine . '.  The first one that is '
                 . 'unreachable is used.  The subnet numbers are chosen from '
                 . 'the private network numbers specified by the Internet '
                 . 'Engineering Task Force (IETF) in RFC 1918 (http://www.isi'
                 . '.edu/in-notes/rfc1918.txt).' . "\n\n" . 'Virtual machines '
                 . 'residing on the NAT network will appear as the host when '
                 . 'accessing the network.  These virtual machines on the NAT '
                 . 'network will not be accessible from outside the host '
                 . 'machine.  This means that it is OK to use the same number '
                 . 'on different systems so long as you do not enable IP '
                 . 'forwarding on the ' . $machine . '.' . "\n\n", 0);
    }
  }

  make_nat_net($vHubNr, $vHostIf, $subnet, $netmask);
  dhcpd_consultant($vHubNr, $vHostIf);
}

# Creates a NAT network
sub make_nat_net {
  my $vHubNr = shift;
  my $vHostIf = shift;
  my $subnet = shift;
  my $netmask = shift;

  my $hostaddr = int_to_quaddot(quaddot_to_int($subnet) + 1);
  my $nataddr = int_to_quaddot(quaddot_to_int($subnet) + 2);

  configure_dev('/dev/' . $vHostIf, 119, $vHubNr, 1);

  db_add_answer('VNET_' . $vHubNr . '_NAT', 'yes');
  db_add_answer('VNET_' . $vHubNr . '_HOSTONLY_HOSTADDR', $hostaddr);
  db_add_answer('VNET_' . $vHubNr . '_HOSTONLY_NETMASK', $netmask);
  db_add_answer('VNET_' . $vHubNr . '_HOSTONLY_SUBNET', $subnet);
  db_add_answer('VNET_' . $vHubNr . '_DHCP', 'yes');

  write_dhcpd_config($vHubNr, $vHostIf, \&make_nat_patch);
  write_nat_config($vHubNr, $vHostIf);
}

# Write NAT configuration files
sub write_nat_config {
  my $vHubNr = shift;
  my $vHostIf = shift;
  my $nat_dir;
  my %patch;

  # Create the nat config directory (one per virtual interface)
  $nat_dir = $gRegistryDir . '/' . $vHostIf . '/nat';
  create_dir($nat_dir, $cFlagDirectoryMark);

  undef %patch;

  my $hostaddr = db_get_answer('VNET_' . $vHubNr . '_HOSTONLY_HOSTADDR');
  my $netmask = db_get_answer('VNET_' . $vHubNr . '_HOSTONLY_NETMASK');
  my $network = db_get_answer_if_exists('VNET_' . $vHubNr . '_HOSTONLY_SUBNET');
  if (not defined($network)) {
     $network = compute_subnet($hostaddr, $netmask);
  }
  my $broadcast = compute_broadcast($hostaddr, $netmask);
  my $nataddr = int_to_quaddot(quaddot_to_int($network) + 2);

  $patch{'%nataddr%'} = $nataddr;
  $patch{'%netmask%'} = $netmask;
  $patch{'%sample%'} = int_to_quaddot(
    (quaddot_to_int($network) + quaddot_to_int($broadcast) + 1) / 2);
  $patch{'%vmnet%'} = "/dev/" . $vHostIf;
  install_file(db_get_answer('LIBDIR') . '/configurator/vmnet-nat.conf',
               $nat_dir . '/nat.conf', \%patch,
               $cFlagTimestamp | $cFlagConfig);
}

# Makes the patch hash that is used to replace the options in the dhcpd config
# file.
# These DHCP options are needed for the NAT network.
sub make_nat_patch {
  my $vHubNr = shift;
  my $vHostIf = shift;
  my %patch;

  my $hostaddr = db_get_answer('VNET_' . $vHubNr . '_HOSTONLY_HOSTADDR');
  my $netmask = db_get_answer('VNET_' . $vHubNr . '_HOSTONLY_NETMASK');
  my $subnet = db_get_answer_if_exists('VNET_' . $vHubNr . '_HOSTONLY_SUBNET');
  if (not defined($subnet)) {
     $subnet = compute_subnet($hostaddr, $netmask);
  }
  my $nataddr = int_to_quaddot(quaddot_to_int($subnet) + 2);

  undef %patch;
  $patch{'%vmnet%'} = $vHostIf;
  $patch{'%hostaddr%'} = $nataddr;
  $patch{'%netmask%'} = $netmask;
  $patch{'%network%'} = compute_subnet($nataddr, $netmask);
  $patch{'%broadcast%'} = compute_broadcast($nataddr, $netmask);
  # Median address in this subnet
  $patch{'%range_low%'} = int_to_quaddot(
    (quaddot_to_int($patch{'%network%'})
     + quaddot_to_int($patch{'%broadcast%'}) + 1) / 2);
  # Last normal address in this subnet
  $patch{'%range_high%'} = int_to_quaddot(quaddot_to_int($patch{'%broadcast%'})
                                          - 1);
  $patch{'%router_option%'} = "option routers $nataddr;";
  return %patch;
}

# Get the list of subnets used by all the nat networks
sub get_nat_subnets {
  my $vHubNr;
  my @subnets = ();
  for ($vHubNr = $gMinVmnet; $vHubNr <= $gMaxVmnet; $vHubNr++) {
    if (is_nat_network($vHubNr)) {
      my $subnet = db_get_answer_if_exists('VNET_' . $vHubNr . '_HOSTONLY_SUBNET');
      if (not defined($subnet)) {
         my $hostaddr = db_get_answer('VNET_' . $vHubNr . '_HOSTONLY_HOSTADDR');
         my $netmask = db_get_answer('VNET_' . $vHubNr . '_HOSTONLY_NETMASK');
         $subnet = compute_subnet($hostaddr, $netmask);
      }
      push(@subnets, $subnet);
    }
  }
  return @subnets;
}

# Get the list of subnets that are already used by virtual networks
sub get_used_subnets {
  return (&get_hostonly_subnets(), &get_nat_subnets());
}

# Return the specific VMware product
sub vmware_product {
  return 'tools-for-linux';
}

# This is a function in case a future product name contains language-specific
# escape characters.
sub vmware_product_name {
  return 'VMware Tools';
}

# Returns the name of the main binary for this install.
sub vmware_binary {
  return 'vmware-toolbox';
}

sub vmware_tools_app_name {
  return db_get_answer('BINDIR') . '/vmware-toolbox';
}

# Security configuration:  Add certificates for the remote console.
sub configure_security() {
  my $version = 'GSX';

  createSSLCertificates(db_get_answer('LIBDIR') . '/bin/openssl',
                        $version,
                        $gRegistryDir . '/ssl',
                        'rui',
                        'VMware Management Interface');
}

# Find binaries necessary for the server products (esx/gsx)
sub configure_server {
  my $program;

  # Create the /var/log/vmware directory for event logs
  create_dir($gLogDir, $cFlagDirectoryMark);

  # Kill any running vmware-hostd process.
  system(shell_string($gHelper{'killall'}) . ' -TERM vmware-hostd '
         . '>/dev/null 2>&1');

  configure_authd();
  configure_wgs_pam_d();
  fix_vmlist_permissions();
}

# Try to find a free port for authd use starting from default passed in
# If none are available, return default passed in
sub get_port_for_authd {
  my $base_port = shift;
  my $port = $base_port;
  my $max_range = 65536;
  while (check_answer_inetport($port, "default") ne $port) {
    $port = ($port + 1) % $max_range;
    if ($base_port == $port) {
      last;
    }
  }
  return $port;
}

# Find a suitable port for authd
sub configure_authd {
  my $success     = 0;
  my $port;

  # Initialize the port cache.  Contains the set of ports
  # known to be active on the system:  listed in /proc/net/tcp.
  get_proc_tcp_entries();

  if (defined(db_get_answer_if_exists("AUTHDPORT"))) {
    $port = db_get_answer_if_exists("AUTHDPORT");
  } else {
    # We'll try to find a good default port that is free
    $port = get_port_for_authd($gDefaultAuthdPort);
    if ($port != $gDefaultAuthdPort) {
      print wrap('The default port : '. $gDefaultAuthdPort. ' is not free.'
                 . ' We have selected a suitable alternative port for '
                 . vmware_product_name()
                 . ' use. You may override this value now.' . "\n", 0);
      print wrap(' Remember to use this port when installing'
                 . ' remote clients on other machines.' . "\n", 0);
    }
  }

  $port = get_persistent_answer('Please specify a port for remote'
                                . ' connections to use',
                                'AUTHDPORT',
                                'inetport',
                                $port);

  if ($gDefaultAuthdPort != $port) {
    print wrap('WARNING: ' . vmware_product_name() . ' has been configured to '
               . 'run on a port different from the default port. '
               . 'Please make sure to use this port when installing remote'
               . ' clients on other machines.' . "\n\n", 0);
  }

  db_add_answer('VMAUTHD_USE_LAUNCHER', 'yes');
}

# Setup the hostd configuration files
sub configure_hostd {
   my %patch;

   # Hostd config file
   %patch = ();
   $patch{'##{WORKINGDIR}##'} = $gLogDir . '/';
   $patch{'##{LOGDIR}##'} = $gLogDir . '/';
   $patch{'##{CFGDIR}##'} = $gRegistryDir . '/';
   $patch{'##{LIBDIR}##'} = db_get_answer('LIBDIR') . '/';
   $patch{'##{LIBDIR_INSTALLED}##'} = db_get_answer('LIBDIR') . '/';
   $patch{'##{BUILD_CFGDIR}##'} = $gRegistryDir . '/hostd/';
   $patch{'##{PLUGINDIR}##'} = db_get_answer('LIBDIR') . '/hostd/';
   $patch{'##{USE_DYNAMIC_PLUGIN_LOADING}##'} = 'false';
   $patch{'##{SHLIB_PREFIX}##'} = 'lib';
   $patch{'##{SHLIB_SUFFIX}##'} = '.so';
   $patch{'##{ENABLE_AUTH}##'} = 'true';
   $patch{'##{USE_PARTITIONSVC}##'} = 'false';
   $patch{'##{USE_NFCSVC}##'} = 'true';
   $patch{'##{USE_BLKLISTSVC}##'} = 'false';
   $patch{'##{USE_CIMSVC}##'} = 'false';
   $patch{'##{USE_HTTPNFCSVC}##'} = 'false';
   $patch{'##{USE_OVFMGRSVC}##'} = 'false';
   $patch{'##{USE_DIRECTORYSVC}##'} = 'false';
   $patch{'##{MOCKUP}##'} = 'mockup-linux.vha';
   $patch{'##{HOSTDMODE}##'} = 'server';
   $patch{'##{USE_VDISKSVC}##'} = 'false';
   $patch{'##{USE_HOSTSVC_MOCKUP}##'} = 'false';
   $patch{'##{USE_STATSSVC_MOCKUP}##'} = 'false';
   $patch{'##{PIPE_PREFIX}##'} = '/var/run/vmware/';
   $patch{'##{LOGLEVEL}##'} = 'info';
   $patch{'##{USE_SECURESOAP}##'} = 'false';


   # XXX Using mockups for now. Change these once they've been implemented.
   $patch{'##{USE_LICENSESVC_MOCKUP}##'} = 'true';

   install_template_file($gRegistryDir . '/hostd/config-template.xml', \%patch, 1);

   #Hostd proxy file:
   my $httpAnswer;
   my $httpsAnswer;
   ($httpAnswer, $httpsAnswer) = query_user_for_proxy_ports();
   if (!defined($httpAnswer)) {
      my $msg = "Unable to find a set of ports needed by the proxy file.\n";
      print wrap($msg, 0);
      return 0;
   }
   $patch{'##{HTTP_PORT}##'} = $httpAnswer;
   $patch{'##{HTTPS_PORT}##'} = $httpsAnswer;
   install_template_file($gRegistryDir . '/hostd/proxy-template.xml', \%patch, 0);

   # Update the port value for the WebAccess.properties file.
   my $file;
   my $jslib;
   my $property_file;
   my $app_dir = "modules/com.vmware.webaccess.app_1.0.0";
   my $ui_dir = db_get_answer('LIBDIR') . '/webAccess/tomcat/@@TOMCAT_DIST@@/webapps/ui';
   foreach $file (internal_ls($ui_dir)) {
      if ($file =~ /jslib-1./) {
         $jslib = $file;
         # Update any jslib directory.  The action will only happen for files
         # not already updated (patched) and leave previously configured files
         # alone.
         my $path = $ui_dir . "/" .  $jslib . "/" . $app_dir;
         my $template_file  = $path . "/WebAccess-template.properties";
         if (!-f $template_file) {
            next;
         } # Regenerate the property file from the template.
         install_template_file($template_file, \%patch, 0);
         $property_file  = $path . "/WebAccess.properties";
      }
   }

   # the jslib directory name contains the build number of the wbc built into
   # the server package.  And it is not necessarily the same as the build
   # number of this server package.
   if (!defined($jslib)) {
      my $msg =  "There is no jslib component directory in " . $ui_dir . "."
                 . "  The component directory should have the"
                 . " form:  jslib-1.xxxxx.\n";
      print(wrap($msg), 0);
      return 0;
   }

   if (!defined($property_file) || ! -f $property_file) {
      error("There is no " . $property_file . "!  This file is necessary for "
          . "the operation of the webAccess service.\n");
   }

   %patch = ();
   my $admin = 'root';
   my $currentAdmin = '';
   my $auth_file = $gRegistryDir . '/hostd/authorization.xml';
   if (file_name_exist($auth_file)) {
      open(ADMIN, $auth_file) or error "Could not open $auth_file.\n";
      while (<ADMIN>) {
         # $currentAdmin only changes if a user is listed.  Not for an empty user name.
         if (/ACEDataUser>(\w+)</) {
            $currentAdmin = $1;
            # Set and later check to see if the user set $admin to something else.
            $admin = $currentAdmin;
            last;
         }
      }
      close(ADMIN);
   }

   my $msg = "  The current administrative user for " . vmware_product_name() . "  "
           . "is '" . $currentAdmin . "'.  Would you like to specify a different "
           . "administrator?";
   if (get_answer($msg, 'yesno', 'no') eq 'yes') {
      $msg = "  Please specify the user whom you wish to be the " . vmware_product_name()
           . " administrator\n";
      $admin = get_answer($msg, 'usergrp', $currentAdmin);
   }
   print wrap("  Using " . $admin . " as the " . vmware_product_name()
            . " administrator.\n\n", 0);

   %patch = ('ACEDataUser>\w*<' => "ACEDataUser>$admin<");
   if (file_name_exist($auth_file) && $admin ne $currentAdmin) {
      # This value lives in a config file that we would normally not touch directly
      # instead makeing the mod via a template file.  But in this case the user has
      # made an explicit change, that must be placed in the config file directly.
      my $tmp_dir = make_tmp_dir('vmware-auth');
      my $tmp_file = $tmp_dir . '/tmp_auth';
      internal_sed($auth_file, $tmp_file, 0, \%patch);
      system(shell_string($gHelper{'mv'}) . ' ' . $tmp_file . ' ' . $auth_file);
      remove_tmp_dir($tmp_dir);
   }

   # authorization.xml is updated by the daemon after the file is installed..
   install_template_file($gRegistryDir . '/hostd/authorization-template.xml', \%patch, 1);

   # VM inventory file modifiable during use.
   install_template_file($gRegistryDir . '/hostd/vmInventory-template.xml', \%patch, 1);

   # Client information file modifiable during use.
   $patch{'@@AUTHD_PORT@@'} = db_get_answer('AUTHDPORT');
   my $docroot = db_get_answer('LIBDIR') . "/hostd/docroot";
   install_template_file($docroot . '/client/clients-template.xml', \%patch, 1);

   return 1;
}

sub install_template_file {
   my $template_file = shift;
   my $patchRef  = shift;
   my $preserve = shift;
   my $flags = $cFlagTimestamp | $cFlagConfig;
   my $dest_path = $template_file;

   $dest_path =~ s/-template//;
   my $dest_file = internal_basename($dest_path);
   my $dest_dir = internal_dirname($dest_path);

   if (file_name_exist($dest_path) && ($preserve & 1)) {
      my $msg = "You have a pre-existing " . $dest_file . ".  The new version will "
                . "be created as " . $dest_dir . "/NEW_" . $dest_file . ".  Please check "
                . "the new file for any new values that you may need to migrate to "
                . "your current " . $dest_file . ".\n\n";

      print wrap($msg, 0);
      $dest_path = $dest_dir . "/NEW_" . $dest_file;
   }

   install_file($template_file, $dest_path, \%$patchRef, $flags);
}

sub query_user_for_proxy_ports {
  my $httpProxy;
  my $httpsProxy;

  # If this is not the first time config has run, then use the port
  # values already chosen.  Count the ports as found if and only if
  # they're both specified and available or at least user approved.
  # If this is the first time, then there won't be any proxy port
  # values to get.
  if (defined(db_get_answer_if_exists('HTTP_PROXY_PORT')) &&
      defined(db_get_answer_if_exists('HTTPS_PROXY_PORT'))) {
    $httpProxy = db_get_answer('HTTP_PROXY_PORT');
    $httpsProxy = db_get_answer('HTTPS_PROXY_PORT');
    my $answer = get_persistent_answer("Do you want to use the current proxy port "
                                       . "values?", 'USE_CURRENT_PORTS', 'yesno', 'yes');
    if ($answer eq 'yes') {
      return ($httpProxy, $httpsProxy);
    }
  }

  # Update the port cache.  It has happened that services active when entries
  # were first retrieved are now no longer active.
  get_proc_tcp_entries();

  # Before we ask the user if he wants the default proxy ports, make sure
  # they are available.  Even if one of the pair is in use, treat the whole
  # pair as in use and go to the next set of proxy ports.
  while ($#gDefaultHttpProxy != -1) {
     $httpProxy = shift(@gDefaultHttpProxy);
     $httpsProxy = shift(@gDefaultHttpSProxy);
    if (!check_if_port_active($httpProxy) &&
        !check_if_port_active($httpsProxy)) {
      last;
    }
  }

  my $httpPort  = get_persistent_answer('Please specify a port for ' .
                                        'standard http connections to use',
                                        'HTTP_PROXY_PORT', 'inetport',
                                        $httpProxy);

  my $httpsPort = get_persistent_answer('Please specify a port for ' .
                                        'secure http (https) connections to use',
                                        'HTTPS_PROXY_PORT', 'inetport',
                                        $httpsProxy);
  return ($httpPort, $httpsPort);
}

sub configure_webAccess {
  my $libdir = db_get_answer("LIBDIR");
  my $webAccess_root = "$libdir/webAccess";
  my $tomcat = $webAccess_root . '/tomcat/@@TOMCAT_DIST@@';
  my $work_dir = $tomcat . '/work';
  my $prop_src_root = $tomcat . '/webapps/ui/WEB-INF/classes';
  my $prop_dst_root = "/etc/vmware/webAccess";
  my $webAccessLogDir = $gLogDir . '/webAccess';

  # make links to the config files
  create_dir($prop_dst_root, $cFlagDirectoryMark);
  install_symlink($prop_src_root . "/log4j.properties",
                                  $prop_dst_root . "/log4j.properties");
  install_symlink($prop_src_root . "/proxy.properties",
                                  $prop_dst_root . "/proxy.properties");
  install_symlink($prop_src_root . "/login.properties",
                                  $prop_dst_root . "/login.properties");

  # tomcat-users.xml needs to be unconditionally removed.  Since the tomcat
  # service touches the installed version of tomcat-users.xml, the file needs
  # to be removed from the db, along with its timestamp, and re-added without
  # a timestamp.
  my $webAccessRoot = db_get_answer('LIBDIR') . "/webAccess";
  db_remove_ts($tomcat . '/conf/tomcat-users.xml');

  if (-e $work_dir) {
    remove_tmp_dir($work_dir);
  }


  create_dir($webAccessLogDir . '/work', 0x0);
  install_symlink($webAccessLogDir, $tomcat . '/logs');
  install_symlink($webAccessLogDir . '/work', $work_dir);
}

#  Move the /etc/vmware/pam.d information to its real home in /etc/pam.d
sub configure_wgs_pam_d {
  my $dir = '/etc/pam.d';
  my $o_file = $gRegistryDir . '/pam.d/vmware-authd';

  if (system(shell_string($gHelper{'cp'}) . ' -p ' . $o_file . ' ' . $dir)) {
    error('Unable to copy the VMware vmware-authd PAM file to ' . $dir
          . "\n\n");
  }
}

# both configuration.
sub show_net_config {
  my $bridge_flag = shift;
  my $hostonly_flag = shift;
  my $nat_flag = shift;

  # Don't show anything
  if (!$hostonly_flag && !$bridge_flag && !$nat_flag) {
    return;
  }

  # Print a message describing what we are showing
  my $nettype = 'virtual';
  if (!$bridge_flag && !$nat_flag && $hostonly_flag) {
    $nettype = 'host-only';
  } elsif (!$hostonly_flag && !$nat_flag && $bridge_flag) {
    $nettype = 'bridged';
  } elsif (!$bridge_flag && !$hostonly_flag && $nat_flag) {
    $nettype = 'NAT';
  }
  print wrap('The following ' . $nettype . ' networks have been defined:'
             . "\n\n", 0);

  # Number of networks configured
  my $count = 0;

  local(*WFD);
  if (not open(WFD, '| ' . $gHelper{'more'})) {
    error("Could not print networking configuration.\n");
  }

  my $i;
  for ($i = $gMinVmnet; $i <= $gMaxVmnet; $i++) {
    if ($bridge_flag && is_bridged_network($i)) {
    my $bridge = $gDBAnswer{'VNET_' . $i . '_INTERFACE'};
      print WFD wrap(". vmnet" . $i . ' is bridged to ' . $bridge . "\n", 0);
      $count++;
    } elsif ($hostonly_flag && is_hostonly_network($i)) {
      my $hostonly_addr = $gDBAnswer{'VNET_' . $i . '_HOSTONLY_HOSTADDR'};
      my $hostonly_mask = $gDBAnswer{'VNET_' . $i . '_HOSTONLY_NETMASK'};
      my $hostonly_subnet = $gDBAnswer{'VNET_' . $i . '_HOSTONLY_SUBNET'};
      if (not defined($hostonly_subnet)) {
         $hostonly_subnet = compute_subnet($hostonly_addr, $hostonly_mask);
      }
      my $sambaInfo = '';
      if (is_samba_running($i)) {
        $sambaInfo = '  This network had a Samba server running to allow '
                     . 'virtual machines to share the ' . $os . '\'s '
                     . 'filesystem. This is now obsolete. Please use the '
                     . 'VMware shared folders feature.';
      }

      print WFD wrap(". vmnet" . $i . ' is a host-only network on private '
                     . 'subnet ' . $hostonly_subnet . '.'
                     . $sambaInfo . "\n", 0);
      $count++;
    } elsif ($nat_flag && is_nat_network($i)) {
      my $hostonly_addr = $gDBAnswer{'VNET_' . $i . '_HOSTONLY_HOSTADDR'};
      my $hostonly_mask = $gDBAnswer{'VNET_' . $i . '_HOSTONLY_NETMASK'};
      my $hostonly_subnet = $gDBAnswer{'VNET_' . $i . '_HOSTONLY_SUBNET'};
      if (not defined($hostonly_subnet)) {
         $hostonly_subnet = compute_subnet($hostonly_addr, $hostonly_mask);
      }
      print WFD wrap(". vmnet" . $i . ' is a NAT network on private subnet '
                     . $hostonly_subnet . '.'
                     . "\n", 0);
      $count++;
    }
  }

  if ($count == 0) {
    print WFD wrap(". No virtual networks configured.\n", 0);
  }

  print WFD wrap("\n", 0);

  close(WFD);
}

# Unconfigures the now obsolete Samba networking
sub unconfigure_samba {
  print wrap('Removing obsolete VMware Samba config info. To access the ' .
             'host filesystem please use the VMware shared folders.' .
             "\n\n", 0);
  unmake_samba_net($gDefHostOnly, 'vmnet' . $gDefHostOnly);
  return;
}

# Go through the /etc/vmware/vm-list file and set permissions correctly
#  also, upgrade vmkernel device names on ESX Server
sub fix_vmlist_permissions {
  my $file = '/etc/vmware/vm-list';
  my $cf;

  if (not -e $file) {
    return;
  }

  if (get_answer('Do you want this program to set up permissions for your '
                 . 'registered virtual machines?  This will be done by '
                 . 'setting new permissions on all files found in the "'
                 . $file . '" file.', 'yesno', 'no') eq 'no') {
    return;
  }

  if (not open(F, "$file")) {
    print wrap('Aborting attempt to change permissions on config files found '
               . 'in "' . $file . '": Cannot read the file.' . "\n\n", 0);
    return;
  }
  while (<F>) {
    s/"//g;
    # This comment fixes emacs's broken syntax highlighting"
    ($cf) = m/^config (.*)$/;
    if (!defined($cf) || (not -e $cf) || (not -f $cf)) {
      next;
    }
    if (chmod(0754, $cf) != 1) {
      print wrap('Cannot change permissions on file "' . $cf . '".' . "\n\n",
                 0);
    }
  }
  close(F);
}

# Check the system requirements to install this product
sub check_wgs_memory {
  my $availableRAMInMB;
  my $minRAMinMB = 256;

  $availableRAMInMB = memory_get_total_ram();
  if (not defined($availableRAMInMB)) {
    if (get_persistent_answer('Unable to determine the total amount of memory '
                              . 'on this system.  You need at least '
                              . $minRAMinMB . ' MB.  Do you really want to '
                              . 'continue?',
                              'PASS_RAM_CHECK', 'yesno', 'no') eq 'no') {
      exit(0);
    }
  } elsif ($availableRAMInMB < $minRAMinMB) {
    if (get_persistent_answer('There are only ' . $availableRAMInMB . ' MB '
                              . 'of memory on this system.  You need at least '
                              . $minRAMinMB . ' MB.  Do you really want to '
                              . 'continue?',
                              'PASS_RAM_CHECK', 'yesno', 'no') eq 'no') {
      exit(0);
    }
  }
}


# Retrieve the amount of RAM (in MB)
# Return undef if unable to determine
sub memory_get_total_ram {
  my $line;
  my $availableRAMInMB = undef;

  if (not open(MEMINFO, '</proc/meminfo')) {
    error('Unable to read the "/proc/meminfo" file.' . "\n\n");
  }
  while (defined($line = <MEMINFO>)) {
    chomp($line);

    if ($line =~ /^Mem:\s*(\d+)/) {
      $availableRAMInMB = $1 / (1024 * 1024);
      last;
    }
    if ($line =~ /^MemTotal:\s*(\d+)\s*kB/) {
      $availableRAMInMB = $1 / 1024;
      last;
    }
  }
  if (defined($availableRAMInMB)) {
    #
    # Round up total memory to the nearest multiple of 8 or 32 MB, since the
    # "total" amount of memory reported by Linux is the total physical memory
    # minus the amount used by the kernel
    #

    if ($availableRAMInMB < 128) {
      $availableRAMInMB = CEILDIV($availableRAMInMB, 8) * 8;
    } else {
      $availableRAMInMB = CEILDIV($availableRAMInMB, 32) * 32;
    }
  }
  close(MEMINFO);

  return $availableRAMInMB;
}

sub CEILDIV {
  my $left = shift;
  my $right = shift;

  return int(($left + $right - 1) / $right);
}

sub build_vmblock {
  my $result;
  my $canBuild = 'no';
  my $explain;

  if (vmware_product() eq 'tools-for-solaris') {
     $result = configure_module_solaris('vmblock');
  } elsif (vmware_product() eq 'tools-for-freebsd') {
     $result = configure_module_bsd('vmblock');
  } else {
     if ($gSystem{'version_integer'} < kernel_version_integer(2, 4, 0)) {
        print wrap("The vmblock module is not supported on kernels "
                   . "older than 2.4.0\n\n", 0);
        $result = 'no';
     } else {
        $result = configure_module('vmblock');
        $canBuild = 'yes';
     }
  }

  if ($result eq 'no') {
     my $src;
     my $dest;
     if (vmware_product() =~ /^tools-for-/) {
        $src = "host";
        $dest = "guest";
     } else {
        $src = "guest";
        $dest = "host";
     }
     $explain = 'The vmblock module enables dragging or copying files from '
                . 'within a ' . $src . ' and dropping or pasting them onto '
                . 'your ' . $dest . ' (' . $src . ' to ' . $dest
                . ' drag and drop and file copy/paste).  The rest of the '
                . 'software provided by ' . vmware_product_name()
                . ' is designed to work independently of this feature (including '
                . $dest . ' to ' . $src . ' drag and drop and file copy/paste).'
                . "\n\n";
     if ($canBuild eq 'yes') {
        $explain .=  'If you would like the ' . $src . ' to ' . $dest . ' drag '
                     . 'and drop and file copy/paste features, '
                     . $cModulesBuildEnv . "\n";
     }

     query($explain, ' Press Enter key to continue ', 0);
  }
  if ($result eq 'yes' && vmware_product() eq 'tools-for-linux') {
    set_manifest_component('vmblock', 'TRUE');
  }
  db_add_answer('VMBLOCK_CONFED', $result);
}

sub build_vmnet {
  if (db_get_answer('NETWORKING') ne 'no') {
    if (configure_module('vmnet') eq 'no') {
      module_error();
    }
  }
}

# Configuration related to networking
sub configure_net {
  my $i;

  # Fix for bug 15842.  Always regenerate the network settings because an
  # upgrade leaves us in an inconsistent state.  The database will have
  # the network settings, but the necessary configuration files such as
  # dhcpd.conf will not exist until we run make_all_net().
  make_all_net();

  if (defined($gDBAnswer{'NETWORKING'}) && count_all_networks() > 0) {
    print wrap('You have already setup networking.' . "\n\n", 0);
    if (get_persistent_answer('Would you like to skip networking setup and '
                              . 'keep your old settings as they are? (yes/no)',
                              'NETWORKING_SKIP_SETUP', 'yesno', 'yes')
                              eq 'yes') {
      return;
    }
  }

  for (;;) {
    my $answer;
    my $helpString;
    my $prompt = 'Do you want networking for your virtual machines? '
               . '(yes/no/help)';
    $helpString = 'Networking will allow your virtual machines to use a '
                  . 'virtual network. There are primarily two types of '
                  . 'networking available: bridged and host-only.  A '
                  . 'bridged network is a virtual network that is '
                  . 'connected to an existing ethernet device.  With a '
                  . 'bridged network, your virtual machines will be able '
                  . 'to communicate with other machines on the network to '
                  . 'which the ethernet card is attached.  A host-only '
                  . 'network is a private network between your virtual '
                  . 'machines and ' . $os . '.  Virtual machines connected '
                  . 'to a host-only network may only communicate directly '
                  . 'with other virtual machines or the ' . $os
                  . '.  A virtual machine may be '
                  . 'configured with more than one bridged or host-only '
                  . 'network.' . "\n\n" . 'If you want your virtual '
                  . 'machines to be connected to a network, say "yes" '
                  . 'here.' . "\n\n";

    $answer = get_persistent_answer($prompt, 'NETWORKING', 'yesnohelp',
                                    'yes');
    if (($answer eq 'no') || ($answer eq 'yes')) {
      last;
    }

    print wrap($helpString, 0);
  }

  if (db_get_answer('NETWORKING') eq 'no') {
    # Turning off networking turns off hostonly.
    remove_all_networks(1, 1, 1);
    return;
  }

  for ($i = 0; $i < $gNumVmnet; $i++) {
    configure_dev('/dev/vmnet' . $i, 119, $i, 1);
  }

  # If there is a previous network configuration, prompt the user to
  # see if the user would like to modify the existing configuration.
  # If the user chooses to modify the settings, give the choice of
  # either the wizard or the editor.
  #
  # If there is no previous network configuration, use the wizard.
  if (count_all_networks() > 0) {
    for (;;) {
      my $answer;
      $answer = get_persistent_answer('Would you prefer to modify your '
                                      . 'existing networking configuration '
                                      . 'using the wizard or the editor? '
                                      . '(wizard/editor/help)',
                                      'NETWORKING_EDITOR', 'editorwizardhelp',
                                      'wizard');
      if (($answer eq 'editor') || ($answer eq 'wizard')) {
        last;
      }

      print wrap('The wizard will present a series of questions that will '
                 . 'help you quickly add new virtual networks.  However, you '
                 . 'cannot remove networks or edit existing networks with the '
                 . 'wizard.  To remove or edit existing networks, you should '
                 . 'use the editor.' . "\n\n", 0);
    }

    # It doesn't make sense to launch the editor if we're not doing
    # an interactive installation.
    if (db_get_answer('NETWORKING_EDITOR') eq 'editor' &&
	$gOption{'default'} != 1) {
      configure_net_editor();
      return;
    }
  }
  configure_net_wizard();
}

# Network configuration wizard
sub configure_net_wizard() {
  my $answer;

  # Bridged Networking
  if (db_get_answer('NETWORKING') eq 'yes' && count_bridged_networks() == 0) {
    # Make a default one unless it exists already
    configure_bridged_net($gDefBridged, 'vmnet' . $gDefBridged);
  }

  show_net_config(1, 0, 0);
  while ($#gAvailEthIf > -1) {
    if (get_answer('Do you wish to configure another bridged network? '
                   . '(yes/no)', 'yesno', 'no') eq 'no') {
      last;
    }
    my $free = get_free_network();
    configure_bridged_net($free, 'vmnet' . $free);
    show_net_config(1, 0, 0);
  }
  if ($#gAvailEthIf == -1) {
    print wrap ('All your ethernet interfaces are already bridged.'
		. "\n\n", 0);
  }

  # NAT networking
  $answer = get_answer('Do you want to be able to use NAT networking '
                       . 'in your virtual machines? (yes/no)', 'yesno', 'yes');

  if ($answer eq 'yes' && count_nat_networks() == 0) {
    configure_nat_net($gDefNat, 'vmnet' . $gDefNat);
  } elsif ($answer  eq 'no') {
    remove_all_networks(0, 0, 1);
  }

  if ($answer eq 'yes') {
    while (1) {
      show_net_config(0, 0, 1);
      if (get_answer('Do you wish to configure another NAT network? '
                     . '(yes/no)', 'yesno', 'no') eq 'no') {
        last;
      }
      my $free = get_free_network();
      configure_nat_net($free, 'vmnet' . $free);
    }
  }

  # Host only networking
  $answer = get_answer('Do you want to be able to use host-only networking '
                       . 'in your virtual machines?', 'yesno', $answer);

  if ($answer eq 'yes' && count_hostonly_networks() == 0) {
    configure_hostonly_net($gDefHostOnly, 'vmnet' . $gDefHostOnly, 1);
  } elsif ($answer  eq 'no') {
    remove_all_networks(0, 1, 0);
  }

  if ($answer eq 'yes') {
    while (1) {
      show_net_config(0, 1, 0);
      if (get_answer('Do you wish to configure another host-only network? '
                     . '(yes/no)', 'yesno', 'no') eq 'no') {
        last;
      }
      my $free = get_free_network();
      configure_hostonly_net($free, 'vmnet' . $free, 1);
    }
  }
  if (vmware_product() eq 'wgs') {
     write_netmap_conf();
  }
}

# Network configuration editor
sub configure_net_editor() {
  my $answer = 'yes';
  my $first_time = 1;
  while ($answer ne 'no') {
    show_net_config(1, 1, 1);

    if (!$first_time) {
      $answer =
        get_persistent_answer('Do you wish to make additional changes to the '
                              . 'current virtual networks settings? (yes/no)',
                              'NETWORK_EDITOR_CHANGE', 'yesno', 'no');
    } else {
      $answer =
        get_persistent_answer('Do you wish to make any changes to the current '
                              . 'virtual networks settings? (yes/no)',
                              'NETWORK_EDITOR_CHANGE', 'yesno', 'no');
      $first_time = 0;
    }
    if ($answer eq 'no') {
      last;
    }

    # An empty default answer. We don't run the editor in --default, so
    # this is safe.
    my $vHubNr = get_answer('Which virtual network do you wish to configure? '
                            . '(' . $gMinVmnet . '-' . $gMaxVmnet . ')',
                            'vmnet', '');

    if ($vHubNr == $gDefBridged) {
      if (get_answer('The network vmnet' . $vHubNr . ' has been reserved for '
                     . 'a bridged network.  You may change it, but it is '
                     . 'highly recommended that you use it as a bridged '
                     . 'network.  Are you sure you want to modify it? '
                     . '(yes/no)', 'yesno', 'no') eq 'no') {
        next;
      }
    }

    if ($vHubNr == $gDefHostOnly) {
      if (get_answer('The network vmnet' . $vHubNr . ' has been reserved for '
                     . 'a host-only network.  You may change it, but it is '
                     . 'highly recommended that you use it as a host-only '
                     . 'network.  Are you sure you want to modify it? '
                     . '(yes/no)', 'yesno', 'no') eq 'no') {
        next;
      }
    }

    if ($vHubNr == $gDefNat) {
      if (get_answer('The network vmnet' . $vHubNr . ' has been reserved for '
                     . 'a NAT network.  You may change it, but it is highly '
                     . 'recommended that you use it as a NAT network.  Are '
                     . 'you sure you want to modify it? (yes/no)',
                     'yesno', 'no') eq 'no') {
        next;
      }
    }

    my $nettype = 'none';
    if (is_bridged_network($vHubNr)) {
      $nettype = 'bridged';
    } elsif (is_hostonly_network($vHubNr)) {
      $nettype = 'hostonly';
    } elsif (is_nat_network($vHubNr)) {
      $nettype = 'nat';
    }
    $answer = get_answer('What type of virtual network do you wish to set '
                         . 'vmnet' . $vHubNr . '? (bridged,hostonly,nat,none)',
                         'nettype', $nettype);

    if ($answer eq 'bridged') {
      # Special case: if we are changing a bridge network to another
      # bridge network, we need to make the interface that it used to
      # be defined as the correct one again.
      if (is_bridged_network($vHubNr)) {
        add_ethif_info(db_get_answer('VNET_' . $vHubNr . '_INTERFACE'));
      }
      configure_bridged_net($vHubNr, 'vmnet' . $vHubNr);
      # Reload available ethernet info in case user does make a change
      load_ethif_info();
    } elsif ($answer eq 'hostonly') {
      configure_hostonly_net($vHubNr, 'vmnet' . $vHubNr, 1);
    } elsif ($answer eq 'nat') {
      configure_nat_net($vHubNr, 'vmnet' . $vHubNr);
    } elsif ($answer eq 'none') {
      remove_net($vHubNr, 'vmnet' . $vHubNr);
    }
  }
  if (vmware_product() eq 'wgs') {
     write_netmap_conf();
  }
}

# Configure networking automatically with no input from the user, keeping the
# existing settings.
sub make_all_net() {
  my $vHubNr;
  for ($vHubNr = $gMinVmnet; $vHubNr <= $gMaxVmnet; $vHubNr++) {
    if (is_bridged_network($vHubNr)) {
      my $ethIf = db_get_answer('VNET_' . $vHubNr . '_INTERFACE');
      make_bridged_net($vHubNr, 'vmnet' . $vHubNr, $ethIf);
    } elsif (is_hostonly_network($vHubNr)) {
      my $hostaddr = db_get_answer('VNET_' . $vHubNr . '_HOSTONLY_HOSTADDR');
      my $netmask = db_get_answer('VNET_' . $vHubNr . '_HOSTONLY_NETMASK');
      my $subnet = db_get_answer_if_exists('VNET_' . $vHubNr . '_HOSTONLY_SUBNET');
      if (not defined($subnet)) {
         $subnet = compute_subnet($hostaddr, $netmask);
      }
      make_hostonly_net($vHubNr, 'vmnet' . $vHubNr, $subnet, $netmask, 1);
    } elsif (is_nat_network($vHubNr)) {
      my $hostaddr = db_get_answer('VNET_' . $vHubNr . '_HOSTONLY_HOSTADDR');
      my $netmask = db_get_answer('VNET_' . $vHubNr . '_HOSTONLY_NETMASK');
      my $subnet = db_get_answer_if_exists('VNET_' . $vHubNr . '_HOSTONLY_SUBNET');
      if (not defined($subnet)) {
         $subnet = compute_subnet($hostaddr, $netmask);
      }
      make_nat_net($vHubNr, 'vmnet' . $vHubNr, $subnet, $netmask);
    }
  }
  if (vmware_product() eq 'wgs') {
     write_netmap_conf();
  }
}

# Counts the number of bridged networks
sub count_bridged_networks {
  return count_networks(1, 0, 0, 0);
}

# Counts the number of hostonly networks
sub count_hostonly_networks {
  return count_networks(0, 1, 0, 0);
}

# Counts the number of hostonly networks running samba
sub count_samba_networks {
  return count_networks(0, 1, 0, 1);
}

# Counts the number of hostonly networks running NAT
sub count_nat_networks {
  return count_networks(0, 0, 1, 0);
}

# Counts the number of configured virtual networks
sub count_all_networks {
  return count_networks(1, 1, 1, 0);
}

# Counts the number of virtual networks that have been setup.
# bridged: Set to indicate a desire to count the number of bridged networks
# hostonly: Set to indicate a desire to count the number of hostonly networks
# nat: Set to indicate a desire to count the number of nat networks
# samba: Set to indicate a desire to count the number of hostonly networks
#        running Samba.
sub count_networks {
  my $bridged = shift;
  my $hostonly = shift;
  my $nat = shift;
  my $samba = shift;

  my $i;
  my $count = 0;
  for ($i = $gMinVmnet; $i <= $gMaxVmnet; $i++) {
    if (is_bridged_network($i) && $bridged) {
      $count++;
    } elsif (is_hostonly_network($i) && $hostonly) {
      if ($samba && is_samba_running($i)) {
        $count++;
      } elsif (!$samba) {
        $count++;
      }
    } elsif (is_nat_network($i) && $nat) {
      $count++;
    }
  }

  return $count;
}

# Indicates if a virtual network has been defined on this virtual net
sub is_network {
  my $vHubNr = shift;

  return    is_bridged_network($vHubNr)
         || is_hostonly_network($vHubNr)
         || is_nat_network($vHubNr);
}

# Indicates if a bridged virtual network is defined for a particular vnet
sub is_bridged_network {
  my $vHubNr = shift;
  my $bridged_ethIf = $gDBAnswer{'VNET_' . $vHubNr . '_INTERFACE'};

  return defined($bridged_ethIf);
}

# Indicates if a hostonly virtual network is defined for a particular vnet
sub is_hostonly_network {
  my $vHubNr = shift;
  my $hostonly_hostaddr = $gDBAnswer{'VNET_' . $vHubNr . '_HOSTONLY_HOSTADDR'};
  my $hostonly_netmask = $gDBAnswer{'VNET_' . $vHubNr . '_HOSTONLY_NETMASK'};
  my $nat_network = $gDBAnswer{'VNET_' . $vHubNr . '_NAT'};

  return    defined($hostonly_hostaddr)
         && defined($hostonly_netmask)
         && not (defined($nat_network) && $nat_network eq 'yes');
}

# Indicates if a NAT virtual network is defined for a particular vnet
sub is_nat_network {
  my $vHubNr = shift;
  my $nat_hostaddr = $gDBAnswer{'VNET_' . $vHubNr . '_HOSTONLY_HOSTADDR'};
  my $nat_netmask = $gDBAnswer{'VNET_' . $vHubNr . '_HOSTONLY_NETMASK'};
  my $nat_network = $gDBAnswer{'VNET_' . $vHubNr . '_NAT'};

  return    defined($nat_hostaddr)
         && defined($nat_netmask)
         && defined($nat_network) && $nat_network eq 'yes';
}

# Indicates if the given network collides with an existing network
# (and if the user is amenable to this)
sub is_good_network {
  my $new_hostaddr = shift;
  my $new_netmask = shift;
  my $new_subnet = compute_subnet($new_hostaddr, $new_netmask);
  my $new_broadcast = compute_broadcast($new_hostaddr, $new_netmask);
  my $vHubNr;

  # Get all networks
  # We can't use get_used_subnets because we want the broadcast address
  # for each subnet as well.
  my @networks = ();
  for ($vHubNr = $gMinVmnet; $vHubNr <= $gMaxVmnet; $vHubNr++) {
    my $hostaddr =
      db_get_answer_if_exists('VNET_' . $vHubNr . '_HOSTONLY_HOSTADDR');
    my $netmask =
      db_get_answer_if_exists('VNET_' . $vHubNr . '_HOSTONLY_NETMASK');
    if (not $hostaddr or not $netmask) {
      next;
    }
    my $subnet = compute_subnet($hostaddr, $netmask);
    my $broadcast = compute_broadcast($hostaddr, $netmask);

    # Check for collisions
    # Each network is defined by a range from subnet (low-end) to
    # broadcast (high-end). Collisions occur when the two ranges overlap.
    if (quaddot_to_int($new_subnet) <= quaddot_to_int($broadcast) &&
        quaddot_to_int($new_broadcast) >= quaddot_to_int($subnet)) {
      push(@networks, 'vmnet' . $vHubNr);
    }
  }

  if (scalar(@networks) != 0) {
    return get_answer(sprintf('The new private network has collided with '
			      . 'existing private %s %s. Are you sure you '
			      . 'wish to add it?', scalar(@networks) == 1 ?
			      'network' : 'networks', join(', ', @networks)),
		      'yesno', 'no');
  }
  return 'yes';
}

# Indicates if samba is running on a virtual network
sub is_samba_running {
  my $vHubNr = shift;
  my $hostonly = is_hostonly_network($vHubNr);
  my $samba = $gDBAnswer{'VNET_' . $vHubNr . '_SAMBA'};

  return    $hostonly
         && defined($samba) && $samba eq 'yes';
}

# Gets a free virtual network number.  Gets the lowest number available.
# Returns -1 on failure.
sub get_free_network {
  my $i;
  for ($i = $gMinVmnet; $i <= $gMaxVmnet; $i++) {
    if (grep($i == $_, @gReservedVmnet)) {
      next;
    }
    if (!is_network($i)) {
      return $i;
    }
  }

  return -1;
}

# Removes a bridged network
sub remove_bridged_network {
  my $vHubNr = shift;
  my $vHostIf = shift;

  if ($vHubNr < $gMinVmnet || $vHubNr > $gMaxVmnet) {
    return;
  }

  print wrap('Removing a bridged network for vmnet' . $vHubNr . '.' . "\n\n",
             0);
  db_remove_answer('VNET_' . $vHubNr . '_INTERFACE');
  if (vmware_product() eq 'wgs') {
     db_remove_answer('VNET_' . $vHubNr . '_NAME');
  }
  if ($vHubNr >= $gNumVmnet) {
    uninstall_file('/dev/' . $vHostIf);
  }

  # Reload the list of available ethernet adapters
  load_ethif_info();
}

# Removes a hostonly network
sub remove_hostonly_network {
  my $vHubNr = shift;
  my $vHostIf = shift;
  my $vmnet_dir = $gRegistryDir . '/' . $vHostIf;

  if ($vHubNr < $gMinVmnet || $vHubNr > $gMaxVmnet) {
    return;
  }

  print wrap('Removing a host-only network for vmnet' . $vHubNr . '.' .
             "\n\n", 0);
  # Remove the samba settings
  unmake_samba_net($vHubNr, $vHostIf);

  db_remove_answer('VNET_' . $vHubNr . '_HOSTONLY_HOSTADDR');
  db_remove_answer('VNET_' . $vHubNr . '_HOSTONLY_NETMASK');
  db_remove_answer('VNET_' . $vHubNr . '_HOSTONLY_SUBNET');
  db_remove_answer('VNET_' . $vHubNr . '_DHCP');
  if (vmware_product() eq 'wgs') {
     db_remove_answer('VNET_' . $vHubNr . '_NAME');
  }
  uninstall_prefix($vmnet_dir);

  if ($vHubNr >= $gNumVmnet) {
    uninstall_file('/dev/' . $vHostIf);
  }
}

# Removes a NAT network
sub remove_nat_network {
  my $vHubNr = shift;
  my $vHostIf = shift;
  my $vmnet_dir = $gRegistryDir . '/' . $vHostIf;

  if ($vHubNr < $gMinVmnet || $vHubNr > $gMaxVmnet) {
    return;
  }

  print wrap('Removing a NAT network for vmnet' . $vHubNr . '.' . "\n\n", 0);

  db_remove_answer('VNET_' . $vHubNr . '_NAT');
  db_remove_answer('VNET_' . $vHubNr . '_HOSTONLY_HOSTADDR');
  db_remove_answer('VNET_' . $vHubNr . '_HOSTONLY_NETMASK');
  db_remove_answer('VNET_' . $vHubNr . '_HOSTONLY_SUBNET');
  db_remove_answer('VNET_' . $vHubNr . '_DHCP');
  if (vmware_product() eq 'wgs') {
     db_remove_answer('VNET_' . $vHubNr . '_NAME');
  }
  uninstall_prefix($vmnet_dir);

  if ($vHubNr >= $gNumVmnet) {
    uninstall_file('/dev/' . $vHostIf);
  }
}

# Removes a network
sub remove_net {
  my $vHubNr = shift;
  my $vHostIf = shift;
  if (is_bridged_network($vHubNr)) {
    remove_bridged_network($vHubNr, $vHostIf);
  } elsif (is_hostonly_network($vHubNr)) {
    remove_hostonly_network($vHubNr, $vHostIf);
  } elsif (is_nat_network($vHubNr)) {
    remove_nat_network($vHubNr, $vHostIf);
  }
}

# Removes all networks on the system subject to the following
# types
sub remove_all_networks {
  my $bridged = shift;
  my $hostonly = shift;
  my $nat = shift;
  my $i;

  for ($i = $gMinVmnet; $i <= $gMaxVmnet; $i++) {
    if (is_network($i)) {
      if ($bridged && is_bridged_network($i)) {
        remove_bridged_network($i, 'vmnet' . $i);
      }
      if ($hostonly && is_hostonly_network($i)) {
        remove_hostonly_network($i, 'vmnet' . $i);
      }
      if ($nat && is_nat_network($i)) {
        remove_nat_network($i, 'vmnet' . $i);
      }
    }
  }
}

# Loads ethernet interface info into global variable
sub load_all_ethif_info() {
  # Get the list of available ethernet interfaces
  # The -a is important because it lists all interfaces (not only those
  # which are up).  The vmnet driver knows how to deal with down interfaces.
  open(IFCONFIG, 'LC_ALL=C ' . shell_string($gHelper{'ifconfig'}) . ' -a |');
  @gAllEthIf = ();
  while (<IFCONFIG>) {
    if (/^(\S+)\s+Link encap:Ethernet/) {
      my @fields;

      @fields = split(/[ ]+/);
      push(@gAllEthIf, $fields[0]);
    }
  }
  close(IFCONFIG);
}

# Determines the available ethernet interfaces
sub load_ethif_info() {
  # Get the list of available ethernet interfaces by checking the all
  # list and removing the ones that have already been allocated.
  @gAvailEthIf = ();

  my @usedEthIf = grep(/^VNET_\d+_INTERFACE$/, keys(%gDBAnswer));
  @usedEthIf = map($gDBAnswer{$_}, @usedEthIf);

  my $eth;
  foreach $eth (@gAllEthIf) {
    if (!grep($_ eq $eth, @usedEthIf)) {
      push(@gAvailEthIf, $eth);
    }
  }
}

# Adds an ethernet interface to the working list of ethernet interfaces
sub add_ethif_info {
  my $eth = shift;
  push(@gAvailEthIf, $eth);
}

# Write out the netmap.conf file
sub write_netmap_conf {
  if (not open(CONF, '>' . $cNetmapConf)) {
     print STDERR wrap("Unable to update the network configuration file.\n", 0);
     return;
  }

  print CONF "# This file is automatically generated.\n";
  print CONF "# Hand-editing this file is not recommended.\n";
  print CONF "\n";

  my %nameMap = ();
  my $i = 0;
  my $vHubNr;
  for ($vHubNr = $gMinVmnet; $vHubNr <= $gMaxVmnet; $vHubNr++) {
     my $dbKey = 'VNET_' . $vHubNr . '_NAME';
     my $name = db_get_answer_if_exists($dbKey);
     if (defined($name)) {
        my $device = 'vmnet' . $vHubNr;

        # Check for duplicate names
        if (defined($nameMap{$name})) {
           my $id = 1;
           my $newName;
           do {
              $id++;
              $newName = $name . ' (' . $id . ')';
           } while (defined($nameMap{$newName}));
           print STDERR wrap('Network name "' . $name . '" for ' . $device .
                             ' is already in use by ' . $nameMap{$name} .
                             ' -- renaming to "' . $newName . '"' . "\n", 0);
           db_add_answer($dbKey, $newName);
           $name = $newName;
        }
        $nameMap{$name} = $device;
        print CONF 'network' . $i . '.name = "' . $name . '"' . "\n";
        print CONF 'network' . $i . '.device = "' . $device . '"' . "\n";
        $i++;
     }
  }

  close(CONF);
}

# Create the links for VMware's services on a Solaris system
sub link_services_solaris {
   my $service = shift;
   my $S_level = shift;
   my $K_level = shift;
   my @S_runlevels = ('2');
   my @K_runlevels = ('0', '1', 'S');
   my $runlevel;

   foreach $runlevel (@S_runlevels) {
     install_symlink(db_get_answer('INITSCRIPTSDIR') . '/' . $service,
                     db_get_answer('INITDIR') . '/rc' . $runlevel
                     . '.d/S' . $S_level . $service);
   }

   foreach $runlevel (@K_runlevels) {
     install_symlink(db_get_answer('INITSCRIPTSDIR') . '/' . $service,
                     db_get_answer('INITDIR') . '/rc' . $runlevel
                     . '.d/K' . $K_level . $service);
   }
}

# Write the VMware host-wide configuration file
sub write_vmware_config {
  my $name;
  my $backupName;
  my $promoconfig;

  $name = $gRegistryDir . '/config';
  $backupName = $gStateDir . '/config';

  my $config = new VMware::Config;
  # First read in old config backed up from last uninstallation.
  if (file_name_exist($name)) {
    if (!$config->readin($name)) {
      error('Unable to read configuration file "' . $name . '".' . "\n\n");
    }
    db_remove_file($name);
  }

  my $bindir = db_get_answer('BINDIR');
  my $libdir = db_get_answer('LIBDIR');
  my $sbindir = db_get_answer('SBINDIR');

  $config->set('bindir', $bindir);

  # Here we set some defaults for guest.commands.*
  # The ->get with default is how we are sure to only change if it isn't
  # already set
  $config->set('guest.commands.enabledOnHost',
               $config->get('guest.commands.enabledOnHost','TRUE'));
  $config->set('guest.commands.allowAnonGuestCommandsOnHost',
               $config->get('guest.commands.allowAnonGuestCommandsOnHost',
                            'FALSE'));
  $config->set('guest.commands.allowAnonRootGuestCommandsOnHost',
               $config->get('guest.commands.allowAnonRootGuestCommandsOnHost',
                            'FALSE'));
  $config->set('guest.commands.anonGuestUserNameOnHost',
               $config->get('guest.commands.anonGuestUserNameOnHost',''));
  $config->set('guest.commands.anonGuestPasswordOnHost',
               $config->get('guest.commands.anonGuestPasswordOnHost',''));

  $config->set('vmware.fullpath', $bindir . '/vmware');
  $config->set('dhcpd.fullpath', $bindir . '/vmnet-dhcpd');
  $config->set('loop.fullpath', $bindir . '/vmware-loop');
  $config->set('control.fullpath', $bindir . '/vmware-cmd');
  $config->set('authd.fullpath', $sbindir . '/vmware-authd');
  $config->set('libdir', $libdir);
  $config->set('product.name', vmware_product_name());
  # Vix needs to know what version of workstation or server
  # it is installed with, even for dev builds.  So add it
  # here as an extra variable and vmware_version() wil return
  # its usual values.  Also, this allows other makefiles to
  # remain untouched.
  if ((vmware_product() eq 'ws') || (vmware_product() eq 'wgs')) {
    $config->set('product.version', '@@VERSIONNUMBER_FOR_VIX@@');
  } else {
    $config->set('product.version', '7.8.5');
  }
  $config->set('product.buildNumber', '156735');

  if ((vmware_product() eq 'wgs') || (vmware_product() eq 'server')) {
      $config->set('authd.client.port', db_get_answer('AUTHDPORT'));
  }
  if (vmware_product() eq 'wgs') {
    # XXX This part of the wizard needs some refinement:
    #     -- Let users specify the datastore name
    #     -- Provide a means of preserving existing datastores
    #     -- Come up with a better default name than "standard"
    my $answer;
    $answer = get_persistent_answer('In which directory do you want to keep your '
				    . 'virtual machine files?', 'VMDIR', 'dirpath',
				    '/var/lib/vmware/Virtual Machines');
    create_dir($answer, $cFlagDirectoryMark);
    $config->set('vmdir', $answer);
    safe_chmod(01777, $answer);
    write_datastore_config('standard', $answer);
  }
  my $vHubNr;
  for ($vHubNr = $gMinVmnet; $vHubNr <= $gMaxVmnet; $vHubNr++) {
    if (is_hostonly_network($vHubNr)) {
      my $hostaddr = db_get_answer('VNET_' . $vHubNr  . '_HOSTONLY_HOSTADDR');
      my $netmask = db_get_answer('VNET_' . $vHubNr  . '_HOSTONLY_NETMASK');
      # Used by the Linux wizard to determine if a hostonly network is
      # configured.
      $config->set('vmnet' . $vHubNr . '.HostOnlyAddress', $hostaddr);
      $config->set('vmnet' . $vHubNr . '.HostOnlyNetMask', $netmask);
    } else {
      $config->remove('vmnet' . $vHubNr . '.HostOnlyAddress');
      $config->remove('vmnet' . $vHubNr . '.HostOnlyNetMask');
    }
  }
  # Used by the Linux wizard to determine if Samba is configured on the
  # hostonly network.
  $config->remove('smbpasswd.fullpath');

  if ((vmware_product() eq 'wgs') || (vmware_product() eq 'server')) {
    $config->set('authd.proxy.vim', 'vmware-hostd:hostd-vmdb');
    $config->set('authd.proxy.nfc', 'vmware-hostd:ha-nfc');
    $config->set('authd.soapServer', 'TRUE');
  }

  $config->remove('serverd.fullpath');
  $config->remove('serverd.init.fullpath');

  if (!$config->writeout($name)) {
    error('Unable to write configuration file "' . $name . '".' . "\n\n");
  }
  db_add_file($name, $cFlagTimestamp | $cFlagConfig);
  safe_chmod(0644, $name);

  # Append the promotional configuration if it exists
  $promoconfig = $libdir . '/configurator/PROMOCONFIG';
  if (-e $promoconfig) {
    my %patch;

    undef %patch;
    internal_sed($promoconfig, $name, 1, \%patch);
  }

  if (!-d $gStateDir) {
    create_dir($gStateDir, 0x1);
  }
  system(shell_string($gHelper{'cp'}) . " " . $name . " " . $backupName);
}

# Write the VMware datastore configuration file
sub write_datastore_config {
  my %patch = ();
  $patch{'##{DS_NAME}##'} = shift;
  $patch{'##{DS_PATH}##'} = shift;

  my $start_file = $gRegistryDir . '/hostd/datastores-template.xml';
  install_template_file($start_file, \%patch, 1);
}

# Write the VMware tools configuration file
sub write_tools_config {
  my $name;
  my $backupName;

  $name = $gRegistryDir . '/tools.conf';
  $backupName = $gStateDir . '/tools.conf';

  my $config = new VMware::Config;
  # First read in old config backed up from last uninstallation.
  if (file_name_exist($backupName)) {
    if (!$config->readin($backupName)) {
      error('Unable to read configuration file "' . $backupName . '".' . "\n\n");
    }
    db_remove_file($backupName);
  }

  $config->set('bindir', db_get_answer('BINDIR'));
  $config->set('helpdir', db_get_answer('LIBDIR') . "/hlp");
  my $mountPoint = db_get_answer_if_exists('HGFS_MOUNT_POINT');
  if ($mountPoint) {
      $config->set('mount-point', $mountPoint);
  }

  if (!$config->writeout($name)) {
    error('Unable to write configuration file "' . $name . '".' . "\n\n");
  }
  db_add_file($name, $cFlagTimestamp);
  safe_chmod(0644, $name);
}

# Display the PROMOCODE information
sub show_PROMOCODE {
  my $promocode;

  $promocode = db_get_answer('DOCDIR') . '/PROMOCODE';
  if (-e $promocode) {
    # $gHelper{'more'} is already a shell string
    system($gHelper{'more'} . ' ' . shell_string($promocode));
    print "\n";
  }
}

# If needed, allow the sysadmin to unlock a site wide license. This must be
# called _after_ VMware's config file has been written
sub check_license {
  my $want_sn;
  my $sn;

  if (system(shell_string(vmware_vmx_app_name())
             . ' --can-run')) {
    $want_sn = 'yes';
  } else {
    for (;;) {
      $want_sn = get_answer('Do you want to enter a serial number now? '
                            . '(yes/no/help)', 'yesnohelp', 'no');
      if (not ($want_sn eq 'help')) {
        last;
      }

      print wrap('Answer "yes" if you have received a new serial number. '
                 . 'Otherwise answer "no", and ' . vmware_product_name()
                 . ' will continue to run just fine.' . "\n\n", 0);
    }
  }

  if ($want_sn eq 'yes') {
    $sn = '';
    while ($sn eq '') {
      $sn = get_answer("Please enter your 20-character serial number.\n\n"
		       . "Type XXXXX-XXXXX-XXXXX-XXXXX or 'Enter' to cancel: ",
		       'serialnum', '');
      if ($sn eq ' ') {
	print wrap ('You cannot power on any virtual machines until you enter a '
		    . 'valid serial number. To enter the serial number, run this '
		    . 'configuration program again. '
		    . "\n\n", 0);
      } elsif (system(shell_string(vmware_vmx_app_name()) . ' --new-sn ' . $sn) != 0) {
	print wrap('The serial number ' . $sn . ' is invalid.' . "\n\n", 0);
        $sn = '';
      }
    }
  }
}

# Display a usage error message for the configuration program and exit
sub config_usage {
   print STDERR wrap(vmware_longname() . ' configurator' . "\n" . 'Usage: '
                     . $0 . ' [[-][-]d[efault]] [[-][-]c[ompile]] '
                     . '[[-][-]p[rebuilt]]' . ' [[-][-]t[ry-modules]] '
                     . '[[-][-]p[reserve]] [[-][-]o[verwrite]'
                     . '[[-][-]m[odules-only]] [[-][-]k[ernel-version] version]'
                     . "\n" . '. default: Automatically '
                     . 'answer questions with the proposed answer.' . "\n"
                     . '. compile: Force the compilation of kernel modules.'
                     . "\n" . '. prebuilt: Force the use of pre-built kernel '
                     . 'modules.' . "\n"
                     . '. try-modules: Try to load all the compatible modules '
                     . '. preserve: Always preserve user-modified '
                     . 'configuration files.' . "\n"
                     . '. overwrite: Always overwrite user-modified '
                     . 'configuration files.'
                     . 'from the ' . vmware_product_name() . ' package.' . "\n"
                     . '. modules-only: Only build/install kernel modules, skip all '
                     . 'other configuration steps.' . "\n"
                     . '. kernel-version: Build/install modules for the given kernel '
                     . 'version instead of the running one, implies modules-only, skip-stop-start and compile.'
                     . "\n\n", 0);
  print STDERR wrap("Command line arguments:  The acceptable characters are:\n"
			 ."\tthe letters A, B, C, ...,\n\tthe letters a, b, c, ...,\n"
			 ."\tthe numbers 0, 1, 2, ...,\n"
			 ."\tand the special characters '_' and '-' and '='.\n", 0);
  exit 1;
}

# Return GSX or ESX for server products, Workstation for ws
sub installed_vmware_version {
  my $vmware_version;
  my $vmware_version_string;

  if (not defined($gHelper{"vmware"})) {
    $gHelper{"vmware"} = DoesBinaryExist_Prompt("vmware");
    if ($gHelper{"vmware"} eq '') {
      error('Unable to continue.' . "\n\n");
    }
  }

  $vmware_version_string = direct_command(shell_string($gHelper{"vmware"})
                                          . ' -v 2>&1 < /dev/null');
  if ($vmware_version_string =~ /.*VMware\s*(\S+)\s*Server.*/) {
    $vmware_version = $1;
  } else {
    $vmware_version = "Workstation";
  }
  return $vmware_version;
}

# switch_to_guest
# Sets links on configuration files we changed during configuration.
# If switch_to_host was never called, do nothing.
sub switch_to_guest {
  my %filesBackedUp;
  my $file;

  if (!defined(db_get_answer_if_exists($cSwitchedToHost))) {
    return;
  }

  %filesBackedUp = db_get_files_to_restore();

  foreach $file (keys %filesBackedUp) {
    if (-l $file) {
      if (check_link($file, $file . db_get_answer($cSwitchedToHost)) eq 'yes') {
        return;
      }
      unlink $file;
      symlink $file . db_get_answer($cSwitchedToHost), $file;
    }
  }
}

# switch_to_host
# Saves configuration files we changed during configuration.
# Sets links on configuration files we backed up during configuration.
sub switch_to_host {
  my $configuredExtension = '.AfterVMwareToolsInstall';
  my %filesBackedUp;
  my $file;

  if (!defined(db_get_answer_if_exists($cSwitchedToHost))) {
    db_add_answer($cSwitchedToHost, $configuredExtension);
  }

  %filesBackedUp = db_get_files_to_restore();

  foreach $file (keys %filesBackedUp) {
    if (-l $file) {
      if (check_link($file, $filesBackedUp{$file}) eq 'yes') {
        return;
      }
      unlink $file;
    } else {
      my %patch;
      undef %patch;
      install_file($file, $file . $configuredExtension, \%patch,
                   $cFlagTimestamp);
      unlink $file;
      # The link might change, do not keep the timestamp.
      db_add_file($file, 0);
    }
    symlink $filesBackedUp{$file}, $file;
  }
}

# update LIBDIR/libconf/etc/fonts/fonts.conf with system font dirs.  Take
# just after its first <dir> entry.  This does not yet handle commented out
# <dir> elements and assumes that <dir> elements are grouped together in
# the same heading.
#
# XXX Document return value(s).
#
sub configure_fonts_dot_conf {
   my $sys_font_path = "/etc/fonts/fonts.conf";
   my $font_path = db_get_answer('LIBDIR') . "/libconf/etc/fonts/fonts.conf";

   # If these files don't exist, there's nothing to do so our task is done.
   return 1 unless (-f $sys_font_path && -f $font_path);

   my $tmp_dir = make_tmp_dir("vmware-fonts");
   my $tmp_file = $tmp_dir . '/fonts.conf';
   my ($font_line, $sys_line);

   open(MYFONT, " < " . $font_path)
      || error "Error openning " . $font_path . "\n";
   open(SYSFONT, " < " . $sys_font_path)
      || error "Error openning " . $sys_font_path . "\n";
   open(OUTFONT, " > " . $tmp_file)
      || error "Error openning " . $tmp_file . "\n";

   # Read from our fonts.conf until reach a <dir> line.  Skip the dir.
   # We'll dump our '<dir>' lines and use the system's.
   while ($font_line = <MYFONT>) {
      if ($font_line =~ /Font\s+directory\s+list/) {
         print OUTFONT $font_line;
         # for readability, add a line to separate the above line from the
	 # following <dir> lines.
         print OUTFONT "\n";
         last;
      }
      if ($font_line =~ /<dir>/) {
         # Use the first '<dir>' as a marker for inserting the new '<dir>'
         # lines.
         last;
      }
      print OUTFONT $font_line;
   }

   # Write out only <dir> lines.
   while ($sys_line = <SYSFONT>) {
      if ($sys_line !~ /<dir>/) {
         next;
      }
      print OUTFONT $sys_line;
   }

   # Finally finish up copying our fonts.conf into the tmp file.
   while ($font_line = <MYFONT>) {
      if ($font_line =~ /<dir>/) {
         next;
      }
      print OUTFONT $font_line;
   }

   close(SYSFONT);
   close(MYFONT);
   close(OUTFONT);

   system(shell_string($gHelper{'cp'}) . " " . $tmp_file . " " . $font_path);
   remove_tmp_dir($tmp_dir);
}

# Tools configurator
sub configure_tools {
  my $vmwareToolsScript = vmware_product() eq 'tools-for-freebsd' ?
                          '/vmware-tools.sh' : '/vmware-tools';

  if ($gSystem{'invm'} eq 'no') {
    error('This configuration program is to be executed in a '
               . 'virtual machine.' . "\n\n");
  }

  # Check for running over a telnet, ssh or remote X session
  if (((defined $ENV{'REMOTEHOST'} and ($ENV{'REMOTEHOST'} ne '')) or
       defined $ENV{'SSH_CONNECTION'} or
       defined $ENV{'DISPLAY'} and $ENV{'DISPLAY'} !~ /^:\d/) and
       get_answer('It looks like you are trying to run this program in ' .
                  'a remote session. This program will temporarily shut ' .
                  'down your network connection, so you should only run ' .
                  'it from a local console session. Are you SURE you ' .
                  'want to continue?', 'yesno', 'no') eq 'no') {
    error('Please re-run this program from a local console shell.' . "\n");
  }

  #
  # Stop VMware's services
  # Also hand-remove vmxnet/vmxnet3 since it is no longer done in services.sh
  # However, do not fail on failing to rmmod as there are plenty of
  # totally reasonable cases where this might happen.
  #
  print "\n";
  if (!$gOption{'skip-stop-start'}) {
      kmod_unload('vmxnet', 0);
  }
  if (!$gOption{'skip-stop-start'} &&
      system(shell_string(db_get_answer('INITSCRIPTSDIR') . $vmwareToolsScript)
             . ' stop')) {
    print wrap('Making sure services for ' . vmware_product_name()
                . ' are stopped.' . "\n\n", 0);
    error('Unable to stop services for ' . vmware_product_name() . "\n\n");
  }

  if (!$gOption{'modules_only'})
  {
    if (vmware_product() eq 'tools-for-linux') {
      # We want to be after networking and syslog
      link_services('vmware-tools', '19', '08');
    } elsif (vmware_product() eq 'tools-for-solaris') {
      link_services_solaris('vmware-tools', '05', '65');
    }

    if (vmware_product() eq 'tools-for-freebsd') {
      configure_module_bsd('vmxnet');
    } elsif (vmware_product() eq 'tools-for-solaris') {
      configure_module_solaris('vmxnet');
    }

    configure_vmmemctl();

    configure_vmhgfs();
    write_module_config();
    build_vmblock();

   # configure the Linux-only drivers
   if ( vmware_product() eq 'tools-for-linux') {
      configure_vmsync();
      configure_vmdesched();    # experimental
      configure_vmci();
      configure_vsock();
   }

    write_tools_config();

    if (vmware_product() eq 'tools-for-linux') {
        configure_thinprint();
    }

    configure_X();

    # Configure autostart for vmware-user
    if (vmware_product() eq 'tools-for-linux') {
      configure_autostart();
    } elsif (vmware_product() eq 'tools-for-freebsd') {
      configure_autostart();
      configure_pango();
      configure_gdk_pixbuf();
    } elsif (vmware_product() eq 'tools-for-solaris') {
      configure_autostart_solaris();
    }
  } else {
    # Only build modules.
    # Right now, this is linux-only, not sure it even makes sense for other OS.

    configure_module('vmmemctl');

    configure_module('vmhgfs');
    configure_module('vmxnet');
    configure_module('vmblock');
    configure_module('vmci');

    # configure the experimental drivers
    configure_module('vmsync');
    configure_module('vmdesched');

    write_tools_config();
  }

  uninstall_file($gConfFlag);

  db_save();

  # Build dependency data for the new modules so that modprobe can find them.
  # Even though the Tools services script uses insmod and thus doesn't care for
  # module dependencies, it makes more sense for the dependencies to be rebuilt
  # prior to any module use.
  if (vmware_product() eq 'tools-for-linux') {
    system(shell_string($gHelper{'depmod'}) . ' -a');
  }

  # We don't ship libconf for Solaris, so we don't need to change the
  # fonts.conf being used.
  if (vmware_product() ne 'tools-for-solaris') {
    configure_fonts_dot_conf();
  }

  #
  # Then start VMware's services.
  if (!$gOption{'skip-stop-start'} &&
      system(shell_string(db_get_answer('INITSCRIPTSDIR') . $vmwareToolsScript)
             . ' start')) {
    error('Unable to start services for ' . vmware_product_name() . "\n\n");
  } else {
    print "\n";
  }

  print wrap('The configuration of ' . vmware_longname() . ' for this running '
             . 'kernel completed successfully.' . "\n\n", 0);
  # Remind Solaris users currently using the Xsun server to switch to Xorg
  if (vmware_product() eq 'tools-for-solaris' &&
      solaris_10_or_greater() eq 'yes' &&
      direct_command(shell_string($gHelper{'svcprop'}) . ' -p options/server '
                     . 'application/x11/x11-server') =~ /Xsun/) {
    print wrap('You must restart your X session under the Xorg X server before '
               . 'any mouse or graphics changes take effect.  Remember to run '
               . 'kdmconfig(1M) as root to switch from the Xsun server to the '
               . 'Xorg server.' . "\n\n", 0);
  } else {
    print wrap('You must restart your X session before any mouse or graphics changes '
               . 'take effect.' . "\n\n", 0);
  }
  print wrap('You can now run ' . vmware_product_name() . ' by invoking the '
             . 'following command: "' . vmware_tools_app_name() . '" during an '
             . 'X server session.' . "\n\n",
             0);

  my $bindir = db_get_answer('BINDIR');
  print wrap('To enable advanced X features (e.g., guest resolution fit, '
             . 'drag and drop, and file and text copy/paste), you will need '
             . 'to do one (or more) of the following:' . "\n"
             . '1. Manually start ' . $bindir . '/vmware-user' . "\n"
             . '2. Log out and log back into your desktop session; and,' . "\n"
             . '3. Restart your X session.' . "\n\n", 0);

  if (vmware_product() eq 'tools-for-linux') {
    if (defined(db_get_answer_if_exists('VMXNET_CONFED')) &&
       (db_get_answer('VMXNET_CONFED') eq 'yes')) {
      if (defined(isKernelBlacklisted())) {
        # Because there are problems rmmod'ing the pcnet32 module on some older
        # kernels the safest way to pick up the vmxnet module is to reboot.
        # DO NOT RMMOD pcnet32!  Even by hand! You will terminally confuse the
        # kernel which will panic or hang very unpredictably.
        print wrap('To make use of the vmxnet driver you will need to '
                   . 'reboot.' . "\n",0);
      } else {
        my ($vmxnet_dev, $pcnet32_dev, $es1371_dev) = get_devices_list();
        if ($vmxnet_dev or $pcnet32_dev) {
          my $network_path = find_first_exist("/etc/init.d/network",
                                              "/etc/init.d/networking");
          print wrap('To use the vmxnet driver, restart networking using the '
                     . 'following commands: ' . "\n"
                     . "$network_path stop" . "\n", 0);
          if ($pcnet32_dev) {
            print wrap('rmmod pcnet32' . "\n", 0);
          }
          print wrap('rmmod vmxnet' . "\n"
                     . 'modprobe vmxnet' . "\n"
                     . "$network_path start" . "\n\n", 0);
        }
      }
    }
    if (defined(db_get_answer_if_exists('THINPRINT_CONFED')) &&
        (db_get_answer('THINPRINT_CONFED') eq 'yes')) {
      print wrap('If the virtual printer feature is enabled, you will need to restart '.
                 'the CUPS service to make use of this feature.' . "\n\n", 0);
    }
  }
  if (vmware_product() eq 'tools-for-freebsd' and
      defined db_get_answer_if_exists('VMXNET_CONFED') and
      db_get_answer('VMXNET_CONFED') eq 'yes') {
    print wrap('Please remember to configure your network by adding:' . "\n"
               . 'ifconfig_vxn0="dhcp"' . "\n"
               . 'to the /etc/rc.conf file and start the network with:'
               . "\n"
               . '/etc/netstart'
               . "\n"
               . 'to use the vmxnet interface using DHCP.' . "\n\n", 0);
  }
  if (vmware_product() eq 'tools-for-solaris' and
      defined db_get_answer_if_exists('VMXNET_CONFED') and
      db_get_answer('VMXNET_CONFED') eq 'yes') {
    print wrap('The installed vmxnet driver will be used for all vlance and '
               . 'vmxnet network devices on this system.  Existing vlance '
               . 'devices will transition from the pcn driver to the vmxnet '
               . 'driver on the next reconfiguration reboot.  You will need '
               . 'to verify your network settings accordingly.'
               . "\n\n"
               . 'If you have configured a pcn interface, the corresponding '
               . 'files are now renamed to use the vmxnet device name to '
               . 'ensure the interface will be brought up properly upon reboot.'
               . '  For example, the following commands were performed:'
               . "\n", 0);
    print     (  '  # mv /etc/hostname.pcn0 /etc/hostname.vmxnet0' . "\n"
               . '  # mv /etc/hostname6.pcn0 /etc/hostname6.vmxnet0' . "\n"
               . '  # mv /etc/dhcp.pcn0 /etc/dhcp.vmxnet0'
               . "\n");
    print wrap(  'and will cause the Solaris Service Management Facility to '
               . 'bring up the first vmxnetX interface using the configuration '
               . 'of your current pcnX interface.'
               . "\n\n", 0);
  }
  print wrap('Enjoy,' . "\n\n" . '    --the VMware team' . "\n\n", 0);
}

#  Supporting the Gtk2 version of toolbox, we need to make sure pango.modules
#  is configured from our own pango modules, especially on FreeBSD 6.2 and higher.
sub configure_pango {
   my $is64BitUserland = is64BitUserLand();
   my $libdir = db_get_answer('LIBDIR');
   my $liblibbsd = ($is64BitUserland ? '/lib64' : '/lib32') . getFreeBSDLibSuffix();
   my $liblibdir = $libdir . $liblibbsd;
   my $liblibconf = $liblibdir . "/libconf";
   my $pango_module_file = $liblibdir . "/libconf/etc/pango/pango.modules";
   my $pangorc = $liblibdir . '/libconf/etc/pango/pangorc';
   my ($pango_path, $pango_version, $bsd_version);
   my %patch;
   my $tmpdir = make_tmp_dir('vmware-pango');
   $bsd_version = getFreeBSDVersion();
   if ("$bsd_version" eq "5.3" ||
       "$bsd_version" eq "6.0" ||
       "$bsd_version" eq "7.0") {
      $pango_version = "1.4.0";
   } elsif ("$bsd_version" eq "5.0")  {
      $pango_version = "1.0.0";
   }
   $pango_path = "pango/" . $pango_version . "/modules";

   # Point pangorc to the modules and to the pango.modules file.
   undef %patch;
   %patch = ('@@PANGO_MODULE_FILE@@' => $pango_module_file,
             '@@PANGO_MODULES@@' => $liblibdir . "/" . $pango_path);
   internal_sed($gRegistryDir . "/pangorc", $pangorc, 0, \%patch);
   db_add_file($pangorc, 0);

   # Update pango.modules so it knows where to find our modules.
   undef %patch;
   %patch = ('@@LIBDIR@@' => $liblibdir);
   install_template_file($pango_module_file . "-template", \%patch, 1);
   remove_tmp_dir($tmpdir);
}

sub configure_gdk_pixbuf {
   my $is64BitUserland = is64BitUserLand();
   my $libdir = db_get_answer('LIBDIR');
   my $liblibbsd = ($is64BitUserland ? '/lib64' : '/lib32') . getFreeBSDLibSuffix();
   my $liblibdir = $libdir . $liblibbsd;
   my $loader_file = $liblibdir . "/libconf/etc/gtk-2.0/gdk-pixbuf.loaders";
   my %patch;

   # FreeBSD 5.0 doesn't have gdk-pixbuf-query-modules or a gdk-pixbuf.loaders file.
   if (getFreeBSDVersion() ne "5.0") {
      # Point the GDK_MODULE_FILE to the loaders
      undef %patch;
      %patch = ('@@LIBDIR@@' => $liblibdir);
      install_template_file($loader_file . "-template", \%patch, 0);
   }
}

sub update_file {
   my $plain_file = shift;
   my $tmpfile = shift;
   my $flags = shift;
   my $patch_thru = shift;
   my @statbuf;

   @statbuf = stat($plain_file);

   db_remove_file($plain_file);
   internal_sed($plain_file, $tmpfile, 0, $patch_thru);
   install_file($tmpfile, $plain_file, $patch_thru, $flags);
   safe_chmod(($statbuf[2] & 07777) | 0555, $plain_file);
}

# switch_tools_config
# Called by the services.sh startup script.
# This allows a switch of configuration depending if the system is
# booted in a VM or natively.
sub switch_tools_config {
  if ($gSystem{'invm'} eq 'yes') {
    switch_to_guest();
  } else {
    switch_to_host();
  }
  db_save();
}

sub get_httpd_status() {
   my $command = "/etc/init.d/httpd.vmware status";
   local *FD;

   if (file_name_exist("/etc/init.d/httpd.vmware")) {
      if (!open(FD, "$command |")) {
         return 3;
      }
      while(<FD>) {
         if ( /\s*.*stopped.*/ ) {
            return 3;
         } else {
            return 0;
         }
      }
   }
   return 3;
}

sub configure_eclipse_plugin {
   my $eclipseDestDir;
   my $eclipseSrcDir = db_get_answer("LIBDIR") . '/eclipse-ivd';

   # Some builds won't have the eclipse plugin packaged (e.g player). Only install it
   # if we have it.
   if (! -d $eclipseSrcDir) {
     return;
   }

   if (get_persistent_answer("Do you want to install the Eclipse Integrated Virtual " .
			     "Debugger? You must have the Eclipse IDE installed.",
			     "ECLIPSEINSTALL", "yesno", "no") eq 'no') {
     return;
   }

   $eclipseDestDir = get_persistent_answer('Which directory contains your eclipse plugins?',
					   'ECLIPSEDIR', 'dirpath_existing', "");

   if ($eclipseDestDir eq "") {
     # don't install if the user (or --default) chose a bogus dir.
     return;
   }

   install_symlink($eclipseSrcDir . '/com.vmware.bfg_1.0.0',
		   $eclipseDestDir . '/com.vmware.bfg_1.0.0');
}

# Returns the console name of the product for use in a .desktop file
sub getDesktopConsoleName {
   if (vmware_product() eq "wgs" || vmware_product() eq "vserver") {
      return vmware_product_name() . " Console";
   } else {
      return vmware_product_name();
   }
}

# Returns the name of the .desktop file to produce
sub getDesktopFileName {
   if (vmware_product() eq "ws") {
      return "vmware-workstation.desktop";
   } elsif (vmware_product() eq "wgs") {
      return "vmware-gsx.desktop";
   }

   return undef;
}

# Returns the name of the icon file to produce
sub getIconFileName {
   if (vmware_product() eq "ws") {
      return "vmware-workstation.png";
   } elsif (vmware_product() eq "wgs") {
      return "vmware-gsx.png";
   }

   return undef;
}

# Creates a .desktop file
sub createDesktopFile {
   my $use_desktop_utils = shift;
   my $mime_support = shift;
   my $desktopFilename = shift;
   my $productName = shift;
   my $iconShortFile = shift;
   my $execName = shift;
   my $comment = shift;
   my $mimetypes = shift;
   my $visible = shift;
   my $desktopConf;
   my $tmpdir;
   my $iconFile = db_get_answer("ICONDIR") . "/hicolor/48x48/apps/$iconShortFile";
   my $pixmapFile = db_get_answer("PIXMAPDIR") . "/$iconShortFile";

   my $iconName = $iconShortFile;
   $iconName =~ s/\.[^.]*$//;

   $tmpdir = make_tmp_dir($cTmpDirPrefix);
   $desktopConf = "$tmpdir/$desktopFilename";

   if (!open(DESKTOP, ">$desktopConf")) {
        print STDERR wrap("Couldn't open \"$desktopConf\".\n"
                          . "Unable to create the .desktop menu entry file. "
                          . "You must add it to your menus by hand.\n", 0);
      remove_tmp_dir($tmpdir);
      return;
   }

   print DESKTOP <<EOF;
[Desktop Entry]
Encoding=UTF-8
Name=$productName
Comment=$comment
Exec=$execName
Terminal=false
Type=Application
Icon=$iconName
StartupNotify=true
Categories=System;
X-Desktop-File-Install-Version=0.9
MimeType=$mimetypes
EOF

   if ($visible == 0) {
      print DESKTOP "NoDisplay=true\n";
   }

   close(DESKTOP);

   safe_chmod(0644, $desktopConf);

   install_symlink($iconFile, $pixmapFile);

   my $desktopdir = db_get_answer("DESKTOPDIR");

   if ($use_desktop_utils == 1) {
      my $params = "";

      if ($mime_support == 1) {
         $params = "--rebuild-mime-info-cache ";
      }

      if (system("desktop-file-install --vendor=vmware " .
                 "--dir=" . shell_string($desktopdir) . " " .
                 $params . shell_string($desktopConf))) {
         print STDERR wrap("Unable to install the .desktop menu entry file. "
                           . "You must add it to your menus by hand.\n", 0);
         remove_tmp_dir($tmpdir);
         return;
      }
      db_add_file("$desktopdir/$desktopFilename", 1);
   } else {
      my %p;
      undef %p;
      install_file($desktopConf, "$desktopdir/$desktopFilename", \%p, 1);
   }

   remove_tmp_dir($tmpdir);
}

# Determine the directory for the icon and .desktop file, and install them
sub configureDesktopFiles {
   my $use_desktop_utils = 1;
   my $mime_support = 0;
   my $pixmapdir;
   my $desktopdir;
   my $vmwareBinary;

   if ((!isServerProduct() && !isDesktopProduct()) || !$gOption{'create_shortcuts'}) {
      return;
   }

   # NOTE: We don't uninstall the desktop file if we used
   #       desktop-file-install, because there is no desktop-file-uninstall.
   $desktopdir = db_get_answer_if_exists("DESKTOPDIR");
   if (defined($desktopdir)) {
      # Uninstall
      uninstall_prefix($desktopdir);
   }

   $pixmapdir = db_get_answer_if_exists("PIXMAPDIR");
   if (defined($pixmapdir)) {
      # Uninstall
      uninstall_prefix($pixmapdir);
   }

  $desktopdir = get_persistent_answer(
     "What directory contains your desktop menu entry files? "
     . "These files have a .desktop file extension.",
     "DESKTOPDIR", "dirpath",
     "/usr/share/applications");

   if (internal_which("desktop-file-install") eq "") {
      $use_desktop_utils = 0;
      create_dir($desktopdir, $cFlagDirectoryMark);
   } else {
      my $buf = `desktop-file-install --help 2>&1`;

      if ($buf =~ /--rebuild-mime-info-cache/) {
         $mime_support = 1;
      }
   }

   $pixmapdir = get_persistent_answer("In which directory do you want to "
                                      . "install the application's icon?",
                                      "PIXMAPDIR", "dirpath",
                                      "/usr/share/pixmaps");
   create_dir($pixmapdir, $cFlagDirectoryMark);

   my $vmwareBinPath = db_get_answer('BINDIR');
   if (vmware_binary() ne "vmplayer") {
      my $mimetypes = "application/x-vmware-vm;";

      if (vmware_product() eq "ws") {
         $mimetypes .= "application/x-vmware-team;";

	 if (defined db_get_answer_if_exists('VNETLIB_CONFED')) {
	     createDesktopFile($use_desktop_utils, $mime_support,
			       "vmware-netcfg.desktop", "Virtual Network Editor",
			       "vmware-netcfg.png", "$vmwareBinPath/vmware-netcfg",
			       "Manage networking for your virtual machines",
			       "", 1);
	 }

      }

      $vmwareBinary = $vmwareBinPath . '/' . vmware_binary();
      createDesktopFile($use_desktop_utils, $mime_support,
                        getDesktopFileName(), getDesktopConsoleName(),
                        getIconFileName(), $vmwareBinary,
                        "Run and manage virtual machines",
                        $mimetypes, 1);

      if (isServerProduct()) {
         createDesktopFile($use_desktop_utils, $mime_support,
                           "vmware-console-uri-handler.desktop",
                           getDesktopConsoleName(), getIconFileName(),
                           $vmwareBinary . " -o %f",
                           "Run and manage remote virtual machines",
                           "application/x-vmware-console;", 0);
      }
   }

   if (isDesktopProduct()) {
      $vmwareBinary = $vmwareBinPath . '/vmplayer';
      # Player is bundled with all desktop products.
      createDesktopFile($use_desktop_utils, $mime_support,
                        "vmware-player.desktop", "VMware Player",
                        "vmware-player.png",
                        $vmwareBinary, "Run a virtual machine",
                        "application/x-vmware-vm;", 1);
   }
}

# Creates a mimetype package description file
sub createMimePackageFile {
   my $tmpdir;
   my $mimeConf;
   my $mimePath;
   my $mimePackagePath;
   my $desticondir;
   my %p;

   if (!isServerProduct() && !isDesktopProduct()) {
      return;
   }

   $mimePath = "/usr/share/mime";
   $mimePackagePath = $mimePath . "/packages";

   # Uninstall
   uninstall_prefix($mimePackagePath);

   # Create the new mimetype package
   create_dir($mimePackagePath, $cFlagDirectoryMark);
   $tmpdir = make_tmp_dir($cTmpDirPrefix);
   $mimeConf = "$tmpdir/vmware.xml";

   if (!open(MIMEPACKAGE, ">$mimeConf")) {
      print STDERR wrap("Couldn't open \"$mimeConf\".\n"
                    . "Unable to create the MIME-Type package file.\n", 0);
      remove_tmp_dir($tmpdir);
      return;
   }

   print MIMEPACKAGE <<EOF;
<?xml version="1.0" encoding="UTF-8"?>

<mime-info xmlns="http://www.freedesktop.org/standards/shared-mime-info">
 <mime-type type="application/x-vmware-vm">
  <comment xml:lang="en">VMware virtual machine</comment>
  <magic priority="50">
   <match type="string" value='config.version = "' offset="0:4096"/>
  </magic>
  <glob pattern="*.vmx"/>
 </mime-type>

 <mime-type type="application/x-vmware-vmdisk">
  <comment xml:lang="en">VMware virtual disk</comment>
  <magic priority="50">
   <match type="string" value="# Disk DescriptorFile" offset="0"/>
   <match type="string" value="KDMV" offset="0"/>
  </magic>
  <glob pattern="*.vmdk"/>
 </mime-type>

 <mime-type type="application/x-vmware-team">
  <comment xml:lang="en">VMware team</comment>
  <magic priority="50">
   <match type="string" value='&lt;Foundry version="1"&gt;' offset="0">
    <match type="string" value="&lt;VMTeam&gt;" offset="23:24"/>
   </match>
  </magic>
  <glob pattern="*.vmtm"/>
 </mime-type>

 <mime-type type="application/x-vmware-snapshot">
  <comment xml:lang="en">VMware virtual machine snapshot</comment>
  <magic priority="50">
   <match type="string" value="\\0xD0\\0xBE\\0xD0\\0xBE" offset="0"/>
  </magic>
  <glob pattern="*.vmsn"/>
 </mime-type>

 <mime-type type="application/x-vmware-vmfoundry">
  <comment xml:lang="en">VMware virtual machine foundry</comment>
  <magic priority="50">
   <match type="string" value='&lt;Foundry version="1"&gt;' offset="0">
    <match type="string" value="&lt;VM&gt;" offset="23:24"/>
   </match>
  </magic>
  <glob pattern="*.vmxf"/>
 </mime-type>

EOF

   print MIMEPACKAGE "</mime-info>\n";

   close MIMEPACKAGE;

   safe_chmod(0644, $mimeConf);

   undef %p;
   install_file($mimeConf, $mimePackagePath . "/vmware.xml", \%p, 1);

   remove_tmp_dir($tmpdir);

   # Update the MIME database
   if (internal_which("update-mime-database") ne "") {
      if (system("update-mime-database " . shell_string($mimePath) .
                 " >/dev/null 2>&1")) {
         print STDERR wrap("Unable to update the MIME-Type database.\n", 0);
         return;
      }
   }

   $desticondir = get_persistent_answer(
      "In which directory do you want to install the theme icons?",
      "ICONDIR", "dirpath", "/usr/share/icons");

   undef %p;

   my $srcicondir = db_get_answer('LIBDIR') . '/share/icons/hicolor';

   $desticondir = $desticondir . '/hicolor';

   foreach my $sizedir (internal_ls($srcicondir)) {
      if (! -d $srcicondir . '/' . $sizedir) {
         next;
      }

      foreach my $category (qw(apps mimetypes)) {
         my $catdir = $sizedir . '/' . $category;
         if (! -d $srcicondir . '/' . $catdir) {
            next;
         }

         create_dir($desticondir . '/' . $catdir, $cFlagDirectoryMark);

         foreach my $icon (internal_ls($srcicondir . '/' . $catdir)) {
            my $iconpath = $catdir . '/' . $icon;
            install_symlink($srcicondir . '/' . $iconpath,
                            $desticondir . '/' . $iconpath);
            if ($category eq 'mimetypes') {
               install_symlink($desticondir . '/' . $iconpath,
                               $desticondir . '/' . $catdir . '/gnome-mime-' .
                               $icon);
            }
         }
      }
   }

   # Refresh icon cache. Some systems (Ubuntu) don't do it automatically
   system(internal_which('touch') . ' -m ' . shell_string($desticondir) . '>/dev/null 2>&1');
   system(internal_which('touch') . ' -m ' . shell_string($srcicondir) . '>/dev/null 2>&1');
   system(shell_string(internal_which('gtk-update-icon-cache')) . ' >/dev/null 2>&1');
   system(shell_string(internal_which('gtk-update-icon-cache')) . " -t $srcicondir >/dev/null 2>&1");
   db_add_file($srcicondir . "/icon-theme.cache", 0)
}

# Given a bunch of db vars, organize them into a sequence of val=key pairs so the
# resulting string can be used in a command line.
sub assemble_command_line {
  my @Args = @_;
  my $string = " ";
  my $flag;

  foreach $flag (@Args) {
    if (db_get_answer_if_exists($flag)) {
      $string .= $flag . '=' . db_get_answer($flag) . ' ';
    } elsif (defined($gOption{$flag})) {
      $string .= '--' . $flag;
      if ($gOption{$flag} =~ /\S/) {
        $string .=  '=' . $gOption{$flag} . ' ';
      }
    }
  }

  return $string;
}

sub install_vix {
  my $tmpDir = make_tmp_dir('vmware-vix-installer');
  my $vixFileRoot = db_get_answer('LIBDIR') . '/vmware-vix/vmware-vix';
  my $vixTarFile = $vixFileRoot . '.tar.gz';
  my $cmd;

  # Since we're not on Solaris, whose tar doesn't support '.gz' and
  # therefore needs gunzip, we need only look for a file ending in
  # '.tar.gz' and not worry about the '.tar' case.
  if (!-f $vixTarFile) {
    return 1;
  }

  my $opts = ' -zxopf  ';
  $opts = ' -C ' . $tmpDir . $opts;
  $cmd = shell_string($gHelper{'tar'}) . $opts . shell_string($vixTarFile);
  if (system($cmd)) {
    remove_tmp_dir($tmpDir);
    print wrap('Untarring ' . $vixTarFile . ' failed.' . ".\n", 0);
    return 1;
  }

  my $vixInstallFile = '/vmware-vix-distrib/vmware-install.pl';
  my $defaultOpts = ($gOption{'default'} == 1) ? ' --default' : '';
  $defaultOpts .= assemble_command_line(qw(EULA_AGREED NESTED UPGRADE prefix));

  # The tar installer for VIX is not deprecated for Server yet, so
  # pretend that the user accepted it.  That means we will have to
  # play more games when we do actually deprecate it.
  if (vmware_product() eq 'wgs' || vmware_product() eq 'ws') {
    $defaultOpts .= ' --no-dep-warning ';
  }

  # Reset the EULA value so the next install asks the question again.
  if (db_get_answer_if_exists('EULA_AGREED')) {
    db_remove_answer('EULA_AGREED');
  }

  if (system(shell_string($tmpDir . $vixInstallFile) . '  ' . $defaultOpts)) {
    remove_tmp_dir($tmpDir);
    return 1;
  }
  remove_tmp_dir($tmpDir);
  return 0;
}

# Check for kernels that won't tolerate removing pcnet32 from the
# list of in use modules.  If there is an entry in the blacklist
# and it is a 'yes', then that kernel is blacklisted.  If not a
# 'yes', then treat the value is more of the blacklisted version
# string. See if with the appended value, the blacklist string
# matches a part of the uts_release value of the system's kernel.
sub isKernelBlacklisted {
  my $result = $cPCnet32KernelBlacklist{$gSystem{'version_utsclean'}};
  if (!defined($result)) {
    return undef;
  }

  if ($result eq 'yes') {
    return $result;
  }

  # append extra version bit and see if a regexp finds it in
  # the current systems uts_release value.
  my $extendedVersion = $gSystem{'version_utsclean'} . $result;
  if ($gSystem{'uts_release'} =~ "^$extendedVersion") {
    return $extendedVersion;
  }

  return undef;
}

# Set manifest component info
sub set_manifest_component {
  my $comp_name = shift;
  my $inst_flag = shift;
  my $version = '';

  $version = get_component_version($comp_name);
  if ($version ne '') {
    my $found = 0;
    my $i;
    for $i (0 .. $#gManifestNames) {
      if ($gManifestNames[$i] eq $comp_name) {
        $gManifestVersions[$i] = $version;
        $gManifestInstFlags[$i] = $inst_flag;
        $found = 1;
        last;
      }
    }
    if (!$found) {
      push(@gManifestNames, $comp_name);
      push(@gManifestVersions, $version);
      push(@gManifestInstFlags, $inst_flag);
    }
  }
}

# Write component version info to the manifest file
sub write_manifest_file {
  my $line1;
  my $line2;
  my $i;

  if (!open(MANIFESTFILE, ">$gManifest")) {
    return;
  }
  for $i (0 .. $#gManifestNames) {
    $line1 = $gManifestNames[$i] . '.version = "' . $gManifestVersions[$i] . '"';
    $line2 = $gManifestNames[$i] . '.installed = "' . $gManifestInstFlags[$i] . '"';
    print MANIFESTFILE $line1 . "\n";
    print MANIFESTFILE $line2 . "\n";
  }
  close(MANIFESTFILE);
}

# Get manifest component version
sub get_component_version {
  my $name = shift;
  my @data_lines;
  my $result = '';
  my $manifest_shipped = $gRegistryDir . '/manifest.txt.shipped';
  my $tmp;

  $name = $name . '.version';

  if (open(VERSIONDATA, "<$manifest_shipped")) {
    @data_lines = <VERSIONDATA>;
    foreach (@data_lines) {
      chomp($_);
      $tmp = $_;
      if ($tmp =~ m/($name)/) {
        $tmp =~ /(\d+\.\d+\.\d+\.\d+)/;
        $result = $1;
      }
    }
    close(VERSIONDATA);
  }
  return $result;
}

# Initialize version manifest
sub init_version_manifest {
  $gManifest = $gRegistryDir . '/manifest.txt';

  set_manifest_component('vmwareuser', 'FALSE');
  set_manifest_component('toolboxgtk', 'FALSE');
  set_manifest_component('vmguestlib', 'FALSE');
  set_manifest_component('vmguestlibjava', 'FALSE');
  set_manifest_component('guestd', 'FALSE');
  set_manifest_component('upgrader', 'FALSE');
  set_manifest_component('hgfsclient', 'FALSE');
  set_manifest_component('hgfsmounter', 'FALSE');
  set_manifest_component('checkvm', 'FALSE');
  set_manifest_component('vmdesched_service', 'FALSE');
  set_manifest_component('vicf', 'FALSE');

  set_manifest_component('vmci', 'FALSE');
  set_manifest_component('vmdesched', 'FALSE');
  set_manifest_component('vmhgfs', 'FALSE');
  set_manifest_component('vmmemctl', 'FALSE');
  set_manifest_component('vmsync', 'FALSE');
  set_manifest_component('vmxnet', 'FALSE');
  set_manifest_component('vmxnet3', 'FALSE');
  set_manifest_component('vmblock', 'FALSE');
  set_manifest_component('vsock', 'FALSE');

  set_manifest_component('svga33', 'FALSE');
  set_manifest_component('svga4', 'FALSE');
  set_manifest_component('svga42', 'FALSE');
  set_manifest_component('vmmouse42', 'FALSE');
  set_manifest_component('svga43', 'FALSE');
  set_manifest_component('vmmouse43', 'FALSE');
  set_manifest_component('svga43_64', 'FALSE');
  set_manifest_component('vmmouse43_64', 'FALSE');
  set_manifest_component('svga67', 'FALSE');
  set_manifest_component('vmmouse67', 'FALSE');
  set_manifest_component('svga67_64', 'FALSE');
  set_manifest_component('vmmouse67_64', 'FALSE');
  set_manifest_component('svga68', 'FALSE');
  set_manifest_component('vmmouse68', 'FALSE');
  set_manifest_component('svga68_64', 'FALSE');
  set_manifest_component('vmmouse68_64', 'FALSE');
  set_manifest_component('svga70', 'FALSE');
  set_manifest_component('vmmouse70', 'FALSE');
  set_manifest_component('svga70_64', 'FALSE');
  set_manifest_component('vmmouse70_64', 'FALSE');
  set_manifest_component('svga71', 'FALSE');
  set_manifest_component('vmmouse71', 'FALSE');
  set_manifest_component('svga71_64', 'FALSE');
  set_manifest_component('vmmouse71_64', 'FALSE');
  set_manifest_component('svga73', 'FALSE');
  set_manifest_component('vmmouse73', 'FALSE');
  set_manifest_component('svga73_64', 'FALSE');
  set_manifest_component('vmmouse73_64', 'FALSE');
  set_manifest_component('svga73_99', 'FALSE');
  set_manifest_component('vmmouse73_99', 'FALSE');
  set_manifest_component('svga73_99_64', 'FALSE');
  set_manifest_component('vmmouse73_99_64', 'FALSE');
  set_manifest_component('svga74', 'FALSE');
  set_manifest_component('vmmouse74', 'FALSE');
  set_manifest_component('svga74_64', 'FALSE');
  set_manifest_component('vmmouse74_64', 'FALSE');
}

# Internationalization data file
sub symlink_icudt38l {
   my $libdir = db_get_answer('LIBDIR');
   install_symlink($libdir . '/icu', $gRegistryDir . '/icu');
}

# The VMware Tools for FreeBSD 6 and 7 are shared.  For FreeBSD 7 users,
# the Tools depend on the "misc/compat6x" package.  (This package contains
# libraries and other support files necessary to run FreeBSD 6 binaries.)
#
# This routine looks for the libraries, and if they aren't found, informs the
# user and prompts him to determine whether or not we continue with installation.
sub verify_bsd_libcompat {
   # Query ldconfig(1) for necessary FreeBSD 6 libraries.
   my ($ldconfigOutput);
   $ldconfigOutput = `ldconfig -r`;

   unless (($ldconfigOutput =~ /(^|\n)[ \t]*\d+:-lc\.6 => /) &&
           ($ldconfigOutput =~ /(^|\n)[ \t]*\d+:-lm\.4 => /)) {
      my ($msg);
      $msg = <<__EOF;
The VMware Tools for FreeBSD 7 depend on libraries provided by the "misc/compat6x"
package, but we were unable to locate them on your system.  While you may continue
to install the VMware Tools, it is STRONGLY recommended that you install the
"misc/compat6x" package from the FreeBSD Ports Tree first.  Would you like to
continue installation?
__EOF
      $msg =~ s/\n/ /g;

      if (get_answer($msg, 'yesno', 'no') eq 'no') {
         error('Please re-run this program after installing the "misc/compat6x" '.
               'package.' . "\n");
      }
   }
}


# Program entry point
sub main {
   my (@setOption, $opt);

   if (not is_root()) {
      error('Please re-run this program as the super user.' . "\n\n");
   }

   # Force the path to reduce the risk of using "modified" external helpers
   # If the user has a special system setup, he will will prompted for the
   # proper location anyway
   $ENV{'PATH'} = '/bin:/usr/bin:/sbin:/usr/sbin';
   initialize_globals();
   if (not (-e $gInstallerMainDB)) {
      error('Unable to find the database file (' . $gInstallerMainDB . ')'
            . "\n\n");
   }

   db_load();
   db_append();
   initialize_external_helpers();

   # If we are configuring the tools, and the installer instructed us to
   # send the end RPC, specify a signal handler in case the user Ctrl-C's
   # early. The handler will send the RPC before exiting.
   if ((vmware_product() eq 'tools-for-linux' ||
	vmware_product() eq 'tools-for-freebsd' ||
	vmware_product() eq 'tools-for-solaris') &&
       $gOption{'rpc-on-end'} == 1) {
       $SIG{INT} = \&sigint_handler;
       $SIG{QUIT} = \&sigint_handler;
   }

   # List of questions answered with command-line arguments
   @setOption = ();
   # Command line analysis
   while ($#ARGV != -1) {
      my $arg;

      $arg = shift(@ARGV);
      if ( $arg =~ /[^A-Za-z_0-9-=\/]/ ) {
        config_usage();
      }

      if (lc($arg) =~ /^(-)?(-)?d(efault)?$/) {
         $gOption{'default'} = 1;
      } elsif (lc($arg) =~ /^(-)?(-)?c(ompile)?$/) {
         $gOption{'compile'} = 1;
      } elsif (lc($arg) =~ /^(-)?(-)?p(rebuilt)?$/) {
         $gOption{'prebuilt'} = 1;
      } elsif (lc($arg) =~ /^(-)?(-)?t(ry-modules)?$/) {
         $gOption{'try-modules'} = 1;
      } elsif (lc($arg) =~ /^(-)?(-)?s(witch)?$/) {
         $gOption{'tools-switch'} = 1;
      } elsif (lc($arg) =~ /^(-)?(-)?o(verwritesvga)?$/) {
	 # This option is deprecated, we always overwrite the svga driver.
         $gOption{'overwriteSVGA'} = 1;
      } elsif (lc($arg) =~ /^(-)?(-)?skip-stop-start$/) {
         $gOption{'skip-stop-start'} = 1;
      } elsif (lc($arg) =~ /^(-)?(-)?make-all-net$/) {
         $gOption{'make-all-net'} = 1;
      } elsif (lc($arg) =~ /^(-)?(-)?r(pc-on-end)?$/) {
         $gOption{'rpc-on-end'} = 1;
      } elsif (lc($arg) =~ /^(-)?(-)?(no-create-shortcuts)$/) {
         $gOption{'create_shortcuts'} = 0;
      } elsif (lc($arg) =~ /^(-)?(-)?p(reserve)$/) {
         $gOption{'preserve'} = 1;
      } elsif (lc($arg) =~ /^(-)?(-)?prefix=(.+)$/) {
         $gOption{'prefix'} = $3;
      } elsif (lc($arg) =~ /^(-)?(-)?m(odules-only)$/) {
         if (vmware_product() ne 'tools-for-linux') {
           error("Cannot build modules only for non-linux OS.");
         }
         $gOption{'modules_only'} = 1;
      } elsif (lc($arg) =~ /^(-)?(-)?k(ernel-version)$/) {
         $gOption{'kernel_version'} = shift(@ARGV);
         $gOption{'modules_only'} = 1;
         $gOption{'compile'} = 1;
         $gOption{'skip-stop-start'} = 1;
         if (vmware_product() ne 'tools-for-linux') {
           error("Cannot build for non-running kernel on non-linux OS.");
         }
      } elsif (lc($arg) =~ /^(-)?(-)?o(verwrite)$/) {
         $gOption{'overwrite'} = 1;
      } elsif ($arg =~ /=yes/ || $arg =~ /=no/) {
         push(@setOption, $arg);
      } else {
         config_usage();
      }
   }

   # Be sure that this is called before anyone attempts to execute any of the
   # compiled binaries on FreeBSD 7.
   if ((vmware_product() eq 'tools-for-freebsd') &&
       (getFreeBSDVersion() eq '7.0')) {
       verify_bsd_libcompat();
   }

   if (vmware_product() eq 'tools-for-linux' &&
       $gOption{'tools-switch'} == 0) {
      init_version_manifest();
   }

   if ($gOption{'tools-switch'} == 0) {
      if (vmware_product() eq 'tools-for-linux' ||
          vmware_product() eq 'tools-for-freebsd' ||
          vmware_product() eq 'tools-for-solaris') {
          setupSymlinks();
      }
   }

   # this call MUST come after setupSymlinks (if setupSymlinks is deemed necessary)
   system_info();

   if ($gOption{'tools-switch'} == 0) {
      my @modules = non_vmware_modules_installed();
      if (scalar(@modules) > 0) {
         my $osVersion = direct_command(shell_string($gHelper{'uname'}) . ' -r');
         chomp($osVersion);
	 error("The following VMware kernel modules have been found on your " .
	       "system that were not installed by the VMware Installer.  " .
	       "Please remove them then run this installer again.\n\n" .
	       join("\n", @modules) . "\n\n" .
	       "I.e. - 'rm /lib/modules/" . $osVersion .
	       "/misc/<ModuleName>.{o,ko}'\n\n");
      }
   }

   if (vmware_product() eq 'ws' && $gOption{'make-all-net'}) {
      make_all_net();
      exit 0;
   }

   if (($gOption{'compile'} == 1) && ($gOption{'prebuilt'} == 1)) {
      print wrap('The "--compile" and "--prebuilt" command line options are '
                 . 'mutually exclusive.' . "\n\n", 0);
      config_usage();
   }

   $gFirstModuleBuild = 1;
   if (isDesktopProduct() || isServerProduct() || isToolsProduct()) {
      symlink_icudt38l();
   }


   # Tools configurator entry point
   if (vmware_product() eq 'tools-for-linux' ||
       vmware_product() eq 'tools-for-freebsd' ||
       vmware_product() eq 'tools-for-solaris') {
      if ($gOption{'tools-switch'} == 1) {
         switch_tools_config();
      } else {
         configure_tools();
         if (vmware_product() eq 'tools-for-linux') {
           write_manifest_file();
         }
         # Send the end RPC along with the results of the configurator run.
         if ($gOption{'rpc-on-end'} == 1) {
           send_rpc('toolinstall.end 1');
         }
      }
      exit 0;
   }

   # Build the list of all and available ethernet adapters
   # The first list is all the adapters that we have.  The
   # second are ones that we can still be bridged.
   load_all_ethif_info();
   load_ethif_info();

   # Stop VMware's services
   if (!$gOption{'skip-stop-start'}) {
      print wrap('Making sure services for ' . vmware_product_name()
                 . ' are stopped.' . "\n\n", 0);
      if (system(shell_string(db_get_answer('INITSCRIPTSDIR') . '/vmware') .
                  ' status vmcount') >> 8 == 2 &&
          get_answer('Do you want to force a shutdown on the running VMs?',
                     'yesno', 'no') eq 'no') {
         error('Please shut down any running VMs and run this script again.' .
               "\n\n");
      } else {
         if (system(shell_string(db_get_answer('INITSCRIPTSDIR') . '/vmware')
                    . ' stop')) {
            error('Unable to stop services for ' . vmware_product_name() .  "\n\n");
         }
      }
   }
   print "\n";

   if (@setOption > 0) {
      $gOption{'default'} = 1;
      # User must specify 'EULA_AGREED=yes' on the command line
      db_add_answer('EULA_AGREED', 'no');
   }
   # Install answers specified on the command line
   foreach $opt (@setOption) {
      my ($key, $val);
      ($key, $val) = ($opt =~ /^([^=]*)=([^=]*)/);
      print $key, ' = ', $val, "\n";
      db_add_answer($key, $val);
   }

   if (vmware_product() ne 'ws') {
      # For wgs, don't show the EULA for developers' builds.
      if (vmware_product() eq 'wgs') {
        if ('156735' != 0) {
          show_EULA();
        }
      } else {
        show_EULA();
      }
   }

  if (vmware_product() eq 'wgs') {
    # Check memory requirements for GSX/WGS
    check_wgs_memory();
  }

  if (vmware_product() ne 'server') {
    configure_mon();
    configure_vmci();
    configure_vsock();
    configure_pp();

    if (vmware_product() eq 'wgs') {
	configure_net();
    }

    build_vmnet();
  }

  if (isDesktopProduct()) {
    build_vmblock();
    createMimePackageFile();
    configureDesktopFiles();
    if (vmware_binary() ne "vmplayer") {
      configure_eclipse_plugin();
    }

    # record root access method for later use by module builder and other
    # programs that require root access
    if (defined $ENV{'SUDO_USER'}) {
       db_add_answer('ROOT_ACCESS_METHOD', 'sudo');
    } else {
       db_add_answer('ROOT_ACCESS_METHOD', 'su');
    }
  }

  # Create the directory for the UNIX domain sockets
  create_dir($cConnectSocketDir, $cFlagDirectoryMark);
  safe_chmod(0755, $cConnectSocketDir);

  if ((vmware_product() ne 'wgs') && (vmware_product() ne 'server') &&
      defined($gDBAnswer{'NETWORKING'}) && get_samba_net() != -1) {
     unconfigure_samba();
  }

  if ((vmware_product() eq 'wgs') || (vmware_product() eq 'server')) {
     configure_server();
  }

  if (vmware_product() eq 'wgs') {
    configure_security();
    if (configure_hostd()) {
      configure_webAccess();
    } else {
      my $msg = "Hostd is not configured properly.  Once you have corrected"
                . " the problem, run " . $cConfiguratorFileName . " again.\n";
      error(wrap($msg, 0));
    }
  }

  if (vmware_product() ne 'server') {
     # We want VMware to start before samba. If this becomes messy in the future
     # we will probably have to dynamically determine the right priority to use
     # based on dependencies on other services as we do in the tools install.
     my $S_priority;
     if ($gSystem{'distribution'} eq 'suse') {
        # samba is 20 SuSE
        $S_priority = '19';
     } else {
        # samba is 91 on RedHat
        $S_priority = '90';
     }
     link_services("vmware", $S_priority, "08");
  }
  if (vmware_product() ne 'server') {
     write_vmware_config();
     if (vmware_product() eq 'wgs' ) {
        # This must come after write_vmware_config()
	check_license();
     }
  }

  # Look for the Vix tar ball that may be hitching a ride in this installation.
  # If the product is workstation or server, and is not vmplayer, its installer
  # will be called.
  my $product = vmware_product();
  if ((defined(db_get_answer_if_exists('INSTALL_CYCLE')) &&
       db_get_answer('INSTALL_CYCLE')  eq 'yes') &&
      ($product eq 'ws' || $product eq 'wgs') &&
      (vmware_binary() ne 'vmplayer')) {
    # Tell vix install that it is in a nested install.  This flag will be passed
    # on the command line, bridging between the vix db and this one.  So, No
    # need to pollute the local db.
    $gDBAnswer{'NESTED'} = 'yes';
    if (install_vix()) {
      my $msg = 'The ' . $cVixProductName .  ' failed to install. Please '
                . "correct the problem and run vmware-config.pl again.\n\n";
      print wrap($msg, 0);
    } else {
      # Remove the answer only if the install succeeded.  If installing failed,
      # then vmware-config.pl should try the install next time around. E.G.
      # the user declined the EULA but now wants VIX installed.
      db_remove_answer('INSTALL_CYCLE');
    }
  }

  # We use modinfo to determine if a module is installed or not in modconfig
  # so we should update this.
  if (isDesktopProduct()) {
    system(shell_string($gHelper{'depmod'}) . ' -a');
  }

  if (isDesktopProduct() || isServerProduct() || isToolsProduct()) {
    symlink_icudt38l();
  }

  # Remove the flag _before_
  uninstall_file($gConfFlag);
  db_save();
  # Then start VMware's services
  if (!$gOption{'skip-stop-start'}) {
    system(shell_string(db_get_answer('INITSCRIPTSDIR') . '/vmware') . ' start');
    print "\n";
  }

  show_PROMOCODE();

  print wrap('The configuration of ' . vmware_longname() . ' for this ' .
             'running kernel completed successfully.' . "\n\n", 0);
  if ((vmware_product() ne 'wgs') && (vmware_product() ne 'server')) {
    print wrap('You can now run ' . vmware_product_name() . ' by invoking' .
               ' the following command: "' . db_get_answer('BINDIR') .
               '/vmware-toolbox".' . "\n\n", 0);
    print wrap('Enjoy,' . "\n\n" . '    --the VMware team' . "\n\n", 0);
  }
  exit(0);
}

main();
