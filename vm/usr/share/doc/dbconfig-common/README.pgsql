helpful information for users who are using postgresql databases

AUTHENTICATION METHODS

postgres supports many types of authentication methods.  dbconfig-common
currently classifies them into two families: "ident", and "password".
the former consists of local and remote postgresql "ident" type connections.
and the latter consists of all the postgresql connections that require
a password (password, crypt, md5, maybe pam/krb5).

for "ident" connections on a local host, the server will check that the
owner of the local unix socket is authorized to connect to the database.

for "ident' connections on a remote host, the server will use an rfc 1413
based ident connection to establish identification.  this is probably a
bad idea, as ident is easily spoofable in non-secured networks.

for "password" connections, the psql client will send along passwords
with its connection attempts to the server.  the server will then
pass the password to whatever authentication backends it's configured
to use.  note that dbconfig-common has no control over whether or not
the password is sent cleartext over the network (see USING SSL), so
you should be aware of the security risks involved with network
based connections.

USING SSL

you must also configure your server to use SSL, which is beyond the
scope of this document.  see:

http://developer.postgresql.org/docs/postgres/ssl-tcp.html

for directions on how to do that.
