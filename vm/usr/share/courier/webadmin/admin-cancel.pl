#!/usr/bin/perl
#
# $Id: admin-cancel.pl,v 1.2 2001/07/07 16:06:53 mrsam Exp $
#
# Copyright 2001 Double Precision, Inc.  See COPYING for
# distribution information.

foreach (ReadConfigFiles())
{
    unlink ("$sysconfdir/webadmin/added/$_",
	    "$sysconfdir/webadmin/removed/$_");
}
unlink("$sysconfdir/webadmin/subdirs-removed");
unlink("$sysconfdir/webadmin/subdirs-added");
unlink "$sysconfdir/webadmin/changed";

print $cgi->redirect( $cgi->url(-full=>1));
exit 0
