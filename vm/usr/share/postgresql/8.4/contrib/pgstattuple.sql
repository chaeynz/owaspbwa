/* $PostgreSQL: pgsql/contrib/pgstattuple/pgstattuple.sql.in,v 1.16 2008/03/21 03:23:30 tgl Exp $ */

-- Adjust this setting to control where the objects get created.
SET search_path = public;

CREATE OR REPLACE FUNCTION pgstattuple(IN relname text,
    OUT table_len BIGINT,		-- physical table length in bytes
    OUT tuple_count BIGINT,		-- number of live tuples
    OUT tuple_len BIGINT,		-- total tuples length in bytes
    OUT tuple_percent FLOAT8,		-- live tuples in %
    OUT dead_tuple_count BIGINT,	-- number of dead tuples
    OUT dead_tuple_len BIGINT,		-- total dead tuples length in bytes
    OUT dead_tuple_percent FLOAT8,	-- dead tuples in %
    OUT free_space BIGINT,		-- free space in bytes
    OUT free_percent FLOAT8)		-- free space in %
AS '$libdir/pgstattuple', 'pgstattuple'
LANGUAGE C STRICT;

CREATE OR REPLACE FUNCTION pgstattuple(IN reloid oid,
    OUT table_len BIGINT,		-- physical table length in bytes
    OUT tuple_count BIGINT,		-- number of live tuples
    OUT tuple_len BIGINT,		-- total tuples length in bytes
    OUT tuple_percent FLOAT8,		-- live tuples in %
    OUT dead_tuple_count BIGINT,	-- number of dead tuples
    OUT dead_tuple_len BIGINT,		-- total dead tuples length in bytes
    OUT dead_tuple_percent FLOAT8,	-- dead tuples in %
    OUT free_space BIGINT,		-- free space in bytes
    OUT free_percent FLOAT8)		-- free space in %
AS '$libdir/pgstattuple', 'pgstattuplebyid'
LANGUAGE C STRICT;

CREATE OR REPLACE FUNCTION pgstatindex(IN relname text,
    OUT version INT,
    OUT tree_level INT,
    OUT index_size BIGINT,
    OUT root_block_no BIGINT,
    OUT internal_pages BIGINT,
    OUT leaf_pages BIGINT,
    OUT empty_pages BIGINT,
    OUT deleted_pages BIGINT,
    OUT avg_leaf_density FLOAT8,
    OUT leaf_fragmentation FLOAT8)
AS '$libdir/pgstattuple', 'pgstatindex'
LANGUAGE C STRICT;

CREATE OR REPLACE FUNCTION pg_relpages(IN relname text)
RETURNS BIGINT
AS '$libdir/pgstattuple', 'pg_relpages'
LANGUAGE C STRICT;
