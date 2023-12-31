/* $PostgreSQL: pgsql/contrib/lo/lo.sql.in,v 1.15 2007/11/13 04:24:28 momjian Exp $ */

-- Adjust this setting to control where the objects get created.
SET search_path = public;

--
--	Create the data type ... now just a domain over OID
--

CREATE DOMAIN lo AS pg_catalog.oid;

--
-- For backwards compatibility, define a function named lo_oid.
--
-- The other functions that formerly existed are not needed because
-- the implicit casts between a domain and its underlying type handle them.
--
CREATE OR REPLACE FUNCTION lo_oid(lo) RETURNS pg_catalog.oid AS
'SELECT $1::pg_catalog.oid' LANGUAGE SQL STRICT IMMUTABLE;

-- This is used in triggers
CREATE OR REPLACE FUNCTION lo_manage()
RETURNS pg_catalog.trigger
AS '$libdir/lo'
LANGUAGE C;
