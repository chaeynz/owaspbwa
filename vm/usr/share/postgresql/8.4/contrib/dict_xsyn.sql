/* $PostgreSQL: pgsql/contrib/dict_xsyn/dict_xsyn.sql.in,v 1.3 2007/11/13 04:24:27 momjian Exp $ */

-- Adjust this setting to control where the objects get created.
SET search_path = public;

CREATE OR REPLACE FUNCTION dxsyn_init(internal)
        RETURNS internal
        AS '$libdir/dict_xsyn'
        LANGUAGE C STRICT;

CREATE OR REPLACE FUNCTION dxsyn_lexize(internal, internal, internal, internal)
        RETURNS internal
        AS '$libdir/dict_xsyn'
        LANGUAGE C STRICT;

CREATE TEXT SEARCH TEMPLATE xsyn_template (
        LEXIZE = dxsyn_lexize,
	INIT   = dxsyn_init
);

CREATE TEXT SEARCH DICTIONARY xsyn (
	TEMPLATE = xsyn_template
);

COMMENT ON TEXT SEARCH DICTIONARY xsyn IS 'eXtended synonym dictionary';
