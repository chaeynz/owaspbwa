#! /usr/bin/perl

foreach (keys %ENV)
{
    my $v=$ENV{$_};

    $v =~ s:\\:\\\\:g;
    $v =~ s:\n:\\n:g;

    print "$_=$v\n";
}
