/* $PostgreSQL: pgsql/contrib/pgrowlocks/pgrowlocks.sql.in,v 1.3 2007/11/13 04:24:28 momjian Exp $ */

-- Adjust this setting to control where the objects get created.
SET search_path = public;

CREATE OR REPLACE FUNCTION pgrowlocks(IN relname text,
    OUT locked_row TID,		-- row TID
    OUT lock_type TEXT,		-- lock type
    OUT locker XID,		-- locking XID
    OUT multi bool,		-- multi XID?
    OUT xids xid[],		-- multi XIDs
    OUT pids INTEGER[])		-- locker's process id
RETURNS SETOF record
AS '$libdir/pgrowlocks', 'pgrowlocks'
LANGUAGE C STRICT;
