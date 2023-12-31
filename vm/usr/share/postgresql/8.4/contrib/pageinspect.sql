/* $PostgreSQL: pgsql/contrib/pageinspect/pageinspect.sql.in,v 1.7 2009/06/08 16:22:44 tgl Exp $ */

-- Adjust this setting to control where the objects get created.
SET search_path = public;

--
-- get_raw_page()
--
CREATE OR REPLACE FUNCTION get_raw_page(text, int4)
RETURNS bytea
AS '$libdir/pageinspect', 'get_raw_page'
LANGUAGE C STRICT;

CREATE OR REPLACE FUNCTION get_raw_page(text, text, int4)
RETURNS bytea
AS '$libdir/pageinspect', 'get_raw_page_fork'
LANGUAGE C STRICT;

--
-- page_header()
--
CREATE OR REPLACE FUNCTION page_header(IN page bytea,
    OUT lsn text,
    OUT tli smallint,
    OUT flags smallint,
    OUT lower smallint,
    OUT upper smallint,
    OUT special smallint,
    OUT pagesize smallint,
    OUT version smallint,
    OUT prune_xid xid)
AS '$libdir/pageinspect', 'page_header'
LANGUAGE C STRICT;

--
-- heap_page_items()
--
CREATE OR REPLACE FUNCTION heap_page_items(IN page bytea,
    OUT lp smallint,
    OUT lp_off smallint,
    OUT lp_flags smallint,
    OUT lp_len smallint,
    OUT t_xmin xid,
    OUT t_xmax xid,
    OUT t_field3 int4,
    OUT t_ctid tid,
    OUT t_infomask2 smallint,
    OUT t_infomask smallint,
    OUT t_hoff smallint,
    OUT t_bits text,
    OUT t_oid oid)
RETURNS SETOF record
AS '$libdir/pageinspect', 'heap_page_items'
LANGUAGE C STRICT;

--
-- bt_metap()
--
CREATE OR REPLACE FUNCTION bt_metap(IN relname text,
    OUT magic int4,
    OUT version int4,
    OUT root int4,
    OUT level int4,
    OUT fastroot int4,
    OUT fastlevel int4)
AS '$libdir/pageinspect', 'bt_metap'
LANGUAGE C STRICT;

--
-- bt_page_stats()
--
CREATE OR REPLACE FUNCTION bt_page_stats(IN relname text, IN blkno int4,
    OUT blkno int4,
    OUT type "char",
    OUT live_items int4,
    OUT dead_items int4,
    OUT avg_item_size int4,
    OUT page_size int4,
    OUT free_size int4,
    OUT btpo_prev int4,
    OUT btpo_next int4,
    OUT btpo int4,
    OUT btpo_flags int4)
AS '$libdir/pageinspect', 'bt_page_stats'
LANGUAGE C STRICT;

--
-- bt_page_items()
--
CREATE OR REPLACE FUNCTION bt_page_items(IN relname text, IN blkno int4,
    OUT itemoffset smallint,
    OUT ctid tid,
    OUT itemlen smallint,
    OUT nulls bool,
    OUT vars bool,
    OUT data text)
RETURNS SETOF record
AS '$libdir/pageinspect', 'bt_page_items'
LANGUAGE C STRICT;

--
-- fsm_page_contents()
--
CREATE OR REPLACE FUNCTION fsm_page_contents(IN page bytea)
RETURNS text
AS '$libdir/pageinspect', 'fsm_page_contents'
LANGUAGE C STRICT;
