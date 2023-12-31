/* $PostgreSQL: pgsql/contrib/pg_freespacemap/pg_freespacemap.sql.in,v 1.12 2009/06/10 22:12:28 tgl Exp $ */

-- Adjust this setting to control where the objects get created.
SET search_path = public;


-- Register the C function.
CREATE OR REPLACE FUNCTION pg_freespace(regclass, bigint)
RETURNS int2
AS '$libdir/pg_freespacemap', 'pg_freespace'
LANGUAGE C STRICT;

-- pg_freespace shows the recorded space avail at each block in a relation
CREATE OR REPLACE FUNCTION
  pg_freespace(rel regclass, blkno OUT bigint, avail OUT int2)
RETURNS SETOF RECORD
AS $$
  SELECT blkno, pg_freespace($1, blkno) AS avail
  FROM generate_series(0, pg_relation_size($1) / current_setting('block_size')::bigint - 1) AS blkno;
$$
LANGUAGE SQL;


-- Don't want these to be available to public.
REVOKE ALL ON FUNCTION pg_freespace(regclass, bigint) FROM PUBLIC;
REVOKE ALL ON FUNCTION pg_freespace(regclass) FROM PUBLIC;
