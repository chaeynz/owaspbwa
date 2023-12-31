/* $PostgreSQL: pgsql/contrib/sslinfo/sslinfo.sql.in,v 1.4 2007/11/13 04:24:29 momjian Exp $ */

-- Adjust this setting to control where the objects get created.
SET search_path = public;

CREATE OR REPLACE FUNCTION ssl_client_serial() RETURNS numeric
AS '$libdir/sslinfo', 'ssl_client_serial'
LANGUAGE C STRICT;

CREATE OR REPLACE FUNCTION ssl_is_used() RETURNS boolean
AS '$libdir/sslinfo', 'ssl_is_used'
LANGUAGE C STRICT;

CREATE OR REPLACE FUNCTION ssl_client_cert_present() RETURNS boolean
AS '$libdir/sslinfo', 'ssl_client_cert_present'
LANGUAGE C STRICT;

CREATE OR REPLACE FUNCTION ssl_client_dn_field(text) RETURNS text
AS '$libdir/sslinfo', 'ssl_client_dn_field'
LANGUAGE C STRICT;

CREATE OR REPLACE FUNCTION ssl_issuer_field(text) RETURNS text
AS '$libdir/sslinfo', 'ssl_issuer_field'
LANGUAGE C STRICT;

CREATE OR REPLACE FUNCTION ssl_client_dn() RETURNS text
AS '$libdir/sslinfo', 'ssl_client_dn'
LANGUAGE C STRICT;

CREATE OR REPLACE FUNCTION ssl_issuer_dn() RETURNS text
AS '$libdir/sslinfo', 'ssl_issuer_dn'
LANGUAGE C STRICT;
