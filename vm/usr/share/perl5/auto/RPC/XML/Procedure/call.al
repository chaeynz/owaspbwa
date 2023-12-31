# NOTE: Derived from blib/lib/RPC/XML/Procedure.pm.
# Changes made here will be lost when autosplit is run again.
# See AutoSplit.pm.
package RPC::XML::Procedure;

#line 1002 "blib/lib/RPC/XML/Procedure.pm (autosplit into blib/lib/auto/RPC/XML/Procedure/call.al)"
###############################################################################
#
#   Sub Name:       call
#
#   Description:    Encapsulates the invocation of the code block that the
#                   object is abstracting. Manages parameters, signature
#                   checking, etc.
#
#   Arguments:      NAME      IN/OUT  TYPE      DESCRIPTION
#                   $self     in      ref       Object of this class
#                   $srv      in      ref       An object derived from the
#                                                 RPC::XML::Server class
#                   @dafa     in      list      The params for the call itself
#
#   Globals:        None.
#
#   Environment:    None.
#
#   Returns:        Success:    value
#                   Failure:    dies with RPC::XML::Fault object as message
#
###############################################################################
sub call
{
    my ($self, $srv, @data) = @_;

    my (@paramtypes, @params, $signature, $resptype, $response, $name, $noinc);

    $name = $self->name;
    # Create the param list.
    # The type for the response will be derived from the matching signature
    @paramtypes = map { $_->type } @data;
    @params     = map { $_->value } @data;
    $signature = join(' ', @paramtypes);
    $resptype = $self->match_signature($signature);
    # Since there must be at least one signature with a return value (even
    # if the param list is empty), this tells us if the signature matches:
    return $srv->server_fault(badsignature => "method $name has no matching " .
            "signature for the argument list: [$signature]")
        unless ($resptype);
    # Make sure that the response-type is a valid XML-RPC type
    if (($resptype ne 'scalar') && (! "RPC::XML::$resptype"->can('new')))
    {
        return $srv->server_fault(badsignature =>
            "Signature [$signature] for method $name has unknown " .
            "return-type '$resptype'");
    }

    # Set these in case the server object is part of the param list
    local $srv->{signature} = [$resptype, @paramtypes];
    local $srv->{method_name} = $name;
    # If the method being called is "system.status", check to see if we should
    # increment the server call-count.
    $noinc =
        (($name eq 'system.status') &&
            @data &&
            ($paramtypes[0] eq 'boolean') &&
            $params[0]) ? 1 : 0;
    # For RPC::XML::Method (and derivatives), pass the server object
    unshift(@params, $srv) if ($self->isa('RPC::XML::Method'));

    # Now take a deep breath and call the method with the arguments
    eval { $response = $self->{code}->(@params); };
    # On failure, propagate user-generated RPC::XML::fault exceptions, or
    # transform Perl-level error/failure into such an object
    if ($@)
    {
        return (blessed $@ and $@->isa('RPC::XML::fault')) ?
            $@ : $srv->server_fault(execerror =>
                                    "Method $name returned error: $@");
    }

    $self->{called}++ unless $noinc;
    # Create a suitable return value
    if (! ref($response))
    {
        if ($resptype eq 'scalar')
        {
            # Server code from the RPC::XML::Function class doesn't use
            # signatures, so if they didn't encode the returned value
            # themselves they're trusting smart_encode() to get it right.
            $response = smart_encode($response);
        }
        else
        {
            # We checked that this was valid earlier, so no need for further
            # tests here.
            $response = "RPC::XML::$resptype"->new($response);
        }
    }

    $response;
}

1;
# end of RPC::XML::Procedure::call
