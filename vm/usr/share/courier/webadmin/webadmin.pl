#! /usr/bin/perl -T
# $Id: webadmin.pl.in,v 1.18 2005/02/16 23:42:14 mrsam Exp $
#
# Copyright 2001 Double Precision, Inc.  See COPYING for
# distribution information.

use CGI;
use FileHandle;

open (STDERR, ">&STDOUT");
local $cgi=new CGI;

local $webadmindir="/usr/share/courier/webadmin";
local $sysconfdir="/etc/courier";
local $prefix="/usr";
local $exec_prefix="/usr";
local $bindir="/usr/bin";
local $sbindir="${exec_prefix}/sbin";
local $libexecdir="${prefix}/lib/courier";

local $authdaemond="/usr/sbin/authdaemond";
local $authdaemonrc="/etc/courier/authdaemonrc";
local $authmysqlrc="/etc/courier/authmysqlrc";
local $authpgsqlrc="/etc/courier/authpgsqlrc";
local $authldaprc="/etc/courier/authldaprc";

$ENV{"PATH"}="/usr/bin:${exec_prefix}/sbin:/sbin:/usr/sbin:/usr/local/sbin:/bin:/usr/bin:/usr/local/bin";
$ENV{"SHELL"}="/bin/sh";

unshift @INC, $webadmindir;

my $passfile= "$sysconfdir/webadmin/password";

sub display_form {
    my $formname=shift;
    my $args=shift;
    my $fh=new FileHandle;

    die ($cgi->header("text/plain") . "Cannot open form $formname: $!\n")
	unless $fh->open("$webadmindir/$formname");

    print $cgi->header("text/html");

    if (! defined $args)
    {
	my %dummy;

	$args=\%dummy;
    }

    $$args{'VERSION'}="0.63.0";
    $$args{'PACKAGE'}="courier";

    $$args{'MAINMENU'}=$cgi->url(-full=>1);

    my %strings;

    while (<$fh>)
    {
	s/@([A-Za-z0-9_\-]+)@/$$args{$1}/ge;

	$strings{$1}=$2 if /\[\#([A-Z0-9_\-]+)=(.*)\#\]/;

	s/\[\#([A-Z0-9_\-]+)=(.*)\#\]//;

	if (/\[\#___\#\]/)
	{
	    foreach (keys %$args)
	    {
		$$args{$_} =~ s/@([A-Za-z0-9_\-]+)@/$strings{$1}/ge;
	    }
	    $_="";
	}

	print;
    }
    close($fh);
    exit(0);
}

sub htmlescape {
    my $s=shift;

    $s =~ s/&/&amp;/g;
    $s =~ s/@/&#64;/g;
    $s =~ s/</&lt;/g;
    $s =~ s/>/&gt;/g;
    $s =~ s/\"/&quot;/g;
    return $s;
}

sub createdirs {
    my @configname=split(/\//, shift);

    pop @configname;

    my $adddir="/etc/courier/webadmin/added";
    my $deldir="/etc/courier/webadmin/removed";

    for (;;)
    {
	unless (-d $adddir)
	{
	    mkdir ($adddir, 0700)
		|| die $cgi->header("text/plain") .
		    "Configuration error: unable to create $adddir: $!\n";
	}

	unless (-d $deldir)
	{
	    mkdir ($deldir, 0700)
		|| die $cgi->header("text/plain") .
		    "Configuration error: unable to create $deldir: $!\n";
	}

	last if $#configname < 0;

	$adddir .= "/$configname[0]";
	$deldir .= "/$configname[0]";
	shift @configname;
    }

    return 1;
}

sub changed {
    my @cmds=@_;

    my %curcmds;
    my @cmdlist;

    my $nfh=new FileHandle "/etc/courier/webadmin/changed";

    push @cmds, "";
    if (defined $nfh)
    {
	my $line;

	while (defined($line= <$nfh>))
	{
	    chomp $line;
	    $curcmds{$line}=1;
	    push @cmdlist, $line;
	}
	close ($nfh);
    }

    while ($#cmds >= 0)
    {
	my $cmd=pop @cmds;

	next if $curcmds{$cmd};

	$curcmds{$cmd}=1;
	unshift @cmdlist, $cmd;
    }

    $nfh=new FileHandle ">/etc/courier/webadmin/changed.new";

    die "Configuration error: cannot create /etc/courier/webadmin/changed.new: $!\n" unless defined $nfh;

    print $nfh join("\n", @cmdlist) . "\n" || die "$!\n";
    $nfh->flush() || die "$!\n";

    $nfh->close() || die "$!\n";

    rename ("/etc/courier/webadmin/changed.new",
	    "/etc/courier/webadmin/changed");
}

sub origname {
    my $n=shift @_;

    return $authdaemonrc if $n eq "authdaemonrc";
    return $authmysqlrc if $n eq "authmysqlrc";
    return $authpgsqlrc if $n eq "authpgsqlrc";
    return $authldaprc if $n eq "authldaprc";

    return "/etc/courier/$n";
}

sub OpenConfigFile {
    my $configname=shift;

    my $nfh=new FileHandle;

    unless ( -f "/etc/courier/webadmin/removed/$configname" )
    {
	$nfh=undef
	    if ( ! $nfh->open("/etc/courier/webadmin/added/$configname") &&
		 ! $nfh->open(origname($configname)));
    }
    else
    {
	$nfh=undef;
    }

    return $nfh;
}

sub ReadOneLineConfigFile {
    my $nfh=OpenConfigFile(shift);

    my $n=undef;

    if (defined $nfh)
    {
	$n=<$nfh>;
	chomp $n;
	close($nfh);
    }

    return $n;
}

sub ReadMultiLineConfigFile {
    my $fh=OpenConfigFile(shift);

    my @m;

    if (defined $fh)
    {
	foreach (<$fh>)
	{
	    my $f=$_;

	    chomp $f;
	    push @m, $f;
	}
	close($fh);
    }
    return @m;
}

sub OpenConfigDirFile {
    my $configfile=shift;
    my $configdir=$configfile;

    $configdir =~ s:/.*::;

    if (! -d "$sysconfdir/$configdir")
    {
	if ( -f "$sysconfdir/$configdir" )
	{
	    die $cgi->header("text/plain") .
		"Configuration error: $configfile: $!\n"
		    unless (rename("$sysconfdir/$configdir",
				   "$sysconfdir/$configdir.system") &&
			    mkdir("$sysconfdir/$configdir", 0755) &&
			    rename("$sysconfdir/$configdir.system",
				   "$sysconfdir/$configfile"));
	}
	else
	{
	    die $cgi->header("text/plain") .
		"Configuration error: $configfile: $!\n"
		    unless mkdir("$sysconfdir/$configdir", 0755);
	}
    }

    return OpenConfigFile($configfile);
}

sub ReadMultiLineConfigDirFile {
    my $fh=OpenConfigDirFile(shift);

    my @m;

    if (defined $fh)
    {
	foreach (<$fh>)
	{
	    my $f=$_;

	    chomp $f;
	    push @m, $f;
	}
	close($fh);
    }
    return @m;
}

sub ReadEnvVarConfigFile {
    my $filename=shift;

    my $f="/etc/courier/webadmin/added/$filename";

    $f=origname($filename) unless -f $f;

    my $fh=new FileHandle "env -i /bin/bash -c \"set -a ; . $f ; /usr/bin/perl /usr/share/courier/webadmin/dumpenv.pl\" |";

    die $cgi->header("text/plain") . "$!\n"
	unless defined $fh;

    my %vars;

    my %repl;

    $repl{"\\"}="\\";
    $repl{"n"}="\n";

    while (<$fh>)
    {
	chomp;

	next unless /^([^=]+)=(.*)$/;

	my ($n, $v)=($1, $2);

	$v =~ s/\\(.)/$repl{$1}/ge;

	$vars{$n}=$v;
    }
    close ($fh);
    return (\%vars);
}

sub ReadKWConfigFile {
    my $fh=OpenConfigFile(shift);

    my %vars;

    if (defined $fh)
    {
	while (<$fh>)
	{
	    next if /^#/;
	    next unless /^([A-Za-z0-9_\-]+)\s+([^\s].*)$/;

	    $vars{$1}=$2;
	}
	close ($fh);
    }
    return \%vars;
}

sub ReplaceEnvVarConfigFile {
    my $filename=shift;
    my $varname=shift;
    my $varvalue=shift;

    $varvalue =~ s:[\n\s]+$::;
    $varvalue =~ s:([\\\"\$\`]):"\\$1":ge;

    my $f="/etc/courier/webadmin/added/$filename";

    $f=origname($filename) unless -f $f;

    my $fh=new FileHandle $f || die $cgi->header("text/plain") . "$f: $!\n";

    my $newfile="";

    while (<$fh>)
    {

	unless (substr($_, 0, length($varname)+1) eq "${varname}=")
	{
	    $newfile .= $_;
	    next;
	}

	while (<$fh>)
	{
	    last if /^\#/ || $_ eq "\n";
	}
	$newfile .= "$varname=\"$varvalue\"\n$_";
	last;
    }

    while (<$fh>)
    {
	$newfile .= $_;
    }
    close ($fh);

    $fh=NewConfigFile($filename);
    print $fh $newfile;
    close($fh);
}

sub NewConfigFile {
    my $filename=shift;

    createdirs($filename);

    unlink("/etc/courier/webadmin/removed/$filename");

    my $nfh=new FileHandle ">/etc/courier/webadmin/added/$filename";

    die $cgi->header("text/plain") .
	"Configuration error: cannot create /etc/courier/webadmin/added/$filename: $!\n" unless defined $nfh;

    return $nfh;
}

sub DeleteConfigFile {
    my $filename=shift;

    createdirs($filename);

    unlink("/etc/courier/webadmin/added/$filename");
    my $nfh=new FileHandle ">/etc/courier/webadmin/removed/$filename"
	|| die $cgi->header("text/plain") .
	    "Configuration error: cannot create /etc/courier/webadmin/removed/$filename: $!\n";

    print $nfh "yes\n";
    close ($nfh);
}

sub ReadDirList {
    my $name=shift;

    my $fh=new FileHandle "/etc/courier/webadmin/subdirs-$name";

    return () unless defined $fh;

    my @l=<$fh>;

    grep { chomp; /(.*)/; $_=$1; } @l;
    # Trim newlines, untaint this list.

    close($fh);
    return @l;
}

sub WriteDirList {
    my $name=shift;
    my $list=shift;

    my $fh=new FileHandle ">/etc/courier/webadmin/subdirs-$name";

    die "Content-Type: text/plain\n\n$!\n"
	unless defined $fh;

    foreach ((@$list))
    {
	print $fh "$_\n";
    }
    close($fh);
}

sub NewConfigSubdir {
    my $subdir=shift;
    my $mode=shift;

    $mode= 0755 unless $mode;

    my @l=ReadDirList("added");

    my $found=0;

    grep { $found=1 if $_ eq $subdir; } @l;

    return if $found;

    push @l, $subdir;

    WriteDirList("added", \@l);

    my $path="/etc/courier/webadmin/added";

    foreach (split (/\//, $subdir))
    {
	$path .= "/$_";

	mkdir($path, $mode);
	chmod($path, $mode);
    }

    @l=();

    foreach(ReadDirList("removed"))
    {
	push @l, $_ unless $_ eq $subdir;
    }
    WriteDirList("removed", \@l);
}

sub DelConfigSubdir {
    my $subdir=shift;
    my $mode=shift;

    $mode= 0755 unless $mode;

    my @l=ReadDirList("removed");

    my $found=0;

    grep { $found=1 if $_ eq $subdir; } @l;

    return if $found;

    push @l, $subdir;
    WriteDirList("removed", \@l);

    my $path="/etc/courier/webadmin/removed";

    foreach (split (/\//, $subdir))
    {
	$path .= "/$_";

	mkdir($path, $mode);
    }

    @l=();

    foreach(ReadDirList("added"))
    {
	push @l, $_ unless $_ eq $subdir;
    }
    WriteDirList("added", \@l);
}

sub SaveOneLineConfigFile {
    my $filename=shift;
    my $line=shift;

    my $nfh= NewConfigFile($filename);
    print $nfh "$line\n";
    close ($nfh);
}

sub SaveMultiLineConfigFile {
    my $filename=shift;
    my $list=shift;

    my $nfh= NewConfigFile($filename);
    foreach (@$list)
    {
	print $nfh "$_\n";
    }
    close ($nfh);
}

sub SaveKWConfigFile {
    my $filename=shift;
    my $vars=shift;
    my $keepundef=shift;

    my $fh=OpenConfigFile($filename);

    die $cgi->header("text/plain") . "$filename: $!\n" unless defined $fh;

    my $newfile;

    my $line=<$fh>;

    while (defined $line)
    {
	unless ($line =~ /^\#\#NAME:\s+([^\s:]+):/)
	{
	    $newfile .= $line;
	    $line=<$fh>;
	    next;
	}

	my $section=$1;

	if ($keepundef)
	{
	    unless (defined $$vars{$section})
	    {
		$newfile .= $line;
		$line=<$fh>;
		next;
	    }
	}

	while ($line =~ /^\#/)
	{
	    $newfile .= $line;
	    $line=<$fh>;
	}

	$newfile .= "\n$$vars{$section}\n\n";

	while (defined $line)
	{
	    last if $line =~ /^\#\#/;
	    $line=<$fh>;
	}
    }
    close($fh);

    chomp $newfile;
    SaveOneLineConfigFile($filename, $newfile);
}

sub readconfigfiles {
    my @dirlist=@_;

    my $base=shift @dirlist;
    my @filelist;

    while ($#dirlist >= 0)
    {
	my $dir=shift @dirlist;

	my $nfh=new FileHandle;

	if (opendir($nfh, "$base/$dir"))
	{
	    my $file;

	    while (defined($file=readdir($nfh)))
	    {
		next if $file eq ".";
		next if $file eq "..";

		$file="$dir/$file";

		if ( -d "$base/$file" )
		{
		    push @dirlist, $file;
		}

		if ( -f "$base/$file" )
		{
		    push @filelist, $file;
		}
	    }
	    closedir($nfh);
	}
    }
    return @filelist;
}

sub ReadConfigFiles {
    my %files;

    foreach (readconfigfiles("/etc/courier/webadmin/added", "."))
    {
	$files{$_}=1;
    }

    foreach (readconfigfiles("/etc/courier/webadmin/removed", "."))
    {
	$files{$_}=1;
    }

    my @keys=sort keys %files;

    grep (s/^\.\///, @keys);

    return (@keys);
}


if ($cgi->user_agent("MSIE"))
{
    print "Cache-Control: private\n";
    print "Pragma: private\n";
}
else
{
    print "Cache-Control: no-store\n";
    print "Pragma: no-cache\n";
}

if ($cgi->param("do.login"))
{
    print $cgi->redirect( -uri => $cgi->url(-full=>1,-path=>1),
			  -cookie => $cgi->cookie( -name=>'courierwebadmin',
						   -secure => $ENV{'HTTPS'} ? 1:0,
						  -value=>$cgi->
						  param('loginpass')));
    exit (0);
}

display_form("unsecure.html")
    unless $ENV{'HTTPS'} ||
    $ENV{'REMOTE_ADDR'} eq $ENV{'SERVER_ADDR'} ||
    -f "$sysconfdir/webadmin/unsecureok";

{
    my $password=$cgi->cookie(-name=>'courierwebadmin');

    display_form("login.html",
		 {
		     "ME" => $cgi->url(-full=>1,-path=>1)
		     }
		 )
	unless defined $password;

    my $adminpw=undef;
    my $fh=new FileHandle "$passfile";

    if (defined $fh)
    {
	if ( (stat($fh))[2] & 0077)
	{
	    close($fh);
	    die $cgi->header("text/plain") . "ERROR: $passfile is world/group readable!\n";
	}

	$adminpw=<$fh>;
	chomp $adminpw;
	close($fh);

    }

    if ($adminpw eq $password)
    {
	;
    }
    else
    {
	display_form("login.html",
		     {
			 "ME" => $cgi->url(-full=>1,-path=>1),
			 "ERRMSG" => "\@BADPASS\@"
			 }
		     )
    }
}

my $form=$cgi->path_info();

if ($form)
{
    if ($form =~ /^\/?([0-9A-Za-z\-_]+)$/)
    {
	$form=$1;
    }
    else
    {
	print $cgi->redirect( -uri => $cgi->url(-full=>1));
	exit 0;
    }
}
else
{
    $form="main";
}

unless (-f "$webadmindir/admin-$form.pl" )
{
    print $cgi->redirect( -uri => $cgi->url(-full=>1));
    exit 0;
}

local $SELF=$cgi->url(-full=>1,-path=>1);


$|=1;

eval {
    do "$webadmindir/admin-$form.pl";

    die $@ if $@;
} ;

print $@ if $@;

exit 0;
