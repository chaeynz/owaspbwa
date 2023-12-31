/* $PostgreSQL: pgsql/contrib/spi/refint.sql.in,v 1.7 2007/11/13 04:24:28 momjian Exp $ */

-- Adjust this setting to control where the objects get created.
SET search_path = public;

CREATE OR REPLACE FUNCTION check_primary_key()
RETURNS trigger
AS '$libdir/refint'
LANGUAGE C;

CREATE OR REPLACE FUNCTION check_foreign_key()
RETURNS trigger
AS '$libdir/refint'
LANGUAGE C;
