# NOTE: Derived from blib/lib/RPC/XML/Server.pm.
# Changes made here will be lost when autosplit is run again.
# See AutoSplit.pm.
package RPC::XML::Server;

#line 2384 "blib/lib/RPC/XML/Server.pm (autosplit into blib/lib/auto/RPC/XML/Server/server_fault.al)"
###############################################################################
#
#   Sub Name:       server_fault
#
#   Description:    Create a RPC::XML::fault object for the class of error
#                   and specific message that are passed in.
#
#   Arguments:      NAME      IN/OUT  TYPE      DESCRIPTION
#                   $self     in      ref       Object of this class
#                   $err      in      scalar    Type of error/fault to generate
#                   $message  in      scalar    Error text for the fault
#
#   Returns:        RPC::XML::fault instance
#
###############################################################################
sub server_fault
{
    my ($self, $err, $message) = @_;
    $message ||= ''; # Avoid any "undef" warnings

    my ($code, $text);

    if (my $fault = $self->{__fault_table}->{$err})
    {
        if (ref $fault)
        {
            # This specifies both code and message
            ($code, $text) = @$fault;
            # Replace (the first) "%s" with $message
            $text =~ s/%s/$message/;
        }
        else
        {
            # This is just the code, use $message verbatim
            ($code, $text) = ($fault, $message);
        }
    }
    else
    {
        $code = -1;
        $text = "Unknown error class '$err' (message is '$message')";
    }

    RPC::XML::fault->new($code, $text);
}

1;
# end of RPC::XML::Server::server_fault
