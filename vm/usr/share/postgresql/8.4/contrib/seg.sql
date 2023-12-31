/* $PostgreSQL: pgsql/contrib/seg/seg.sql.in,v 1.19 2009/06/11 18:30:03 tgl Exp $ */

-- Adjust this setting to control where the objects get created.
SET search_path = public;

-- Create the user-defined type for 1-D floating point intervals (seg)
-- 

CREATE OR REPLACE FUNCTION seg_in(cstring)
RETURNS seg
AS '$libdir/seg'
LANGUAGE C STRICT IMMUTABLE;

CREATE OR REPLACE FUNCTION seg_out(seg)
RETURNS cstring
AS '$libdir/seg'
LANGUAGE C STRICT IMMUTABLE;

CREATE TYPE seg (
	INTERNALLENGTH = 12,
	INPUT = seg_in,
	OUTPUT = seg_out
);

COMMENT ON TYPE seg IS
'floating point interval ''FLOAT .. FLOAT'', ''.. FLOAT'', ''FLOAT ..'' or ''FLOAT''';

--
-- External C-functions for R-tree methods
--

-- Left/Right methods

CREATE OR REPLACE FUNCTION seg_over_left(seg, seg)
RETURNS bool
AS '$libdir/seg'
LANGUAGE C STRICT IMMUTABLE;

COMMENT ON FUNCTION seg_over_left(seg, seg) IS
'overlaps or is left of';

CREATE OR REPLACE FUNCTION seg_over_right(seg, seg)
RETURNS bool
AS '$libdir/seg'
LANGUAGE C STRICT IMMUTABLE;

COMMENT ON FUNCTION seg_over_right(seg, seg) IS
'overlaps or is right of';

CREATE OR REPLACE FUNCTION seg_left(seg, seg)
RETURNS bool
AS '$libdir/seg'
LANGUAGE C STRICT IMMUTABLE;

COMMENT ON FUNCTION seg_left(seg, seg) IS
'is left of';

CREATE OR REPLACE FUNCTION seg_right(seg, seg)
RETURNS bool
AS '$libdir/seg'
LANGUAGE C STRICT IMMUTABLE;

COMMENT ON FUNCTION seg_right(seg, seg) IS
'is right of';


-- Scalar comparison methods

CREATE OR REPLACE FUNCTION seg_lt(seg, seg)
RETURNS bool
AS '$libdir/seg'
LANGUAGE C STRICT IMMUTABLE;

COMMENT ON FUNCTION seg_lt(seg, seg) IS
'less than';

CREATE OR REPLACE FUNCTION seg_le(seg, seg)
RETURNS bool
AS '$libdir/seg'
LANGUAGE C STRICT IMMUTABLE;

COMMENT ON FUNCTION seg_le(seg, seg) IS
'less than or equal';

CREATE OR REPLACE FUNCTION seg_gt(seg, seg)
RETURNS bool
AS '$libdir/seg'
LANGUAGE C STRICT IMMUTABLE;

COMMENT ON FUNCTION seg_gt(seg, seg) IS
'greater than';

CREATE OR REPLACE FUNCTION seg_ge(seg, seg)
RETURNS bool
AS '$libdir/seg'
LANGUAGE C STRICT IMMUTABLE;

COMMENT ON FUNCTION seg_ge(seg, seg) IS
'greater than or equal';

CREATE OR REPLACE FUNCTION seg_contains(seg, seg)
RETURNS bool
AS '$libdir/seg'
LANGUAGE C STRICT IMMUTABLE;

COMMENT ON FUNCTION seg_contains(seg, seg) IS
'contains';

CREATE OR REPLACE FUNCTION seg_contained(seg, seg)
RETURNS bool
AS '$libdir/seg'
LANGUAGE C STRICT IMMUTABLE;

COMMENT ON FUNCTION seg_contained(seg, seg) IS
'contained in';

CREATE OR REPLACE FUNCTION seg_overlap(seg, seg)
RETURNS bool
AS '$libdir/seg'
LANGUAGE C STRICT IMMUTABLE;

COMMENT ON FUNCTION seg_overlap(seg, seg) IS
'overlaps';

CREATE OR REPLACE FUNCTION seg_same(seg, seg)
RETURNS bool
AS '$libdir/seg'
LANGUAGE C STRICT IMMUTABLE;

COMMENT ON FUNCTION seg_same(seg, seg) IS
'same as';

CREATE OR REPLACE FUNCTION seg_different(seg, seg)
RETURNS bool
AS '$libdir/seg'
LANGUAGE C STRICT IMMUTABLE;

COMMENT ON FUNCTION seg_different(seg, seg) IS
'different';

-- support routines for indexing

CREATE OR REPLACE FUNCTION seg_cmp(seg, seg)
RETURNS int4
AS '$libdir/seg'
LANGUAGE C STRICT IMMUTABLE;

COMMENT ON FUNCTION seg_cmp(seg, seg) IS 'btree comparison function';

CREATE OR REPLACE FUNCTION seg_union(seg, seg)
RETURNS seg
AS '$libdir/seg'
LANGUAGE C STRICT IMMUTABLE;

CREATE OR REPLACE FUNCTION seg_inter(seg, seg)
RETURNS seg
AS '$libdir/seg'
LANGUAGE C STRICT IMMUTABLE;

CREATE OR REPLACE FUNCTION seg_size(seg)
RETURNS float4
AS '$libdir/seg'
LANGUAGE C STRICT IMMUTABLE;

-- miscellaneous

CREATE OR REPLACE FUNCTION seg_center(seg)
RETURNS float4
AS '$libdir/seg'
LANGUAGE C STRICT IMMUTABLE;

CREATE OR REPLACE FUNCTION seg_upper(seg)
RETURNS float4
AS '$libdir/seg'
LANGUAGE C STRICT IMMUTABLE;

CREATE OR REPLACE FUNCTION seg_lower(seg)
RETURNS float4
AS '$libdir/seg'
LANGUAGE C STRICT IMMUTABLE;


--
-- OPERATORS
--

CREATE OPERATOR < (
	LEFTARG = seg,
	RIGHTARG = seg,
	PROCEDURE = seg_lt,
	COMMUTATOR = '>',
	NEGATOR = '>=',
	RESTRICT = scalarltsel,
	JOIN = scalarltjoinsel
);

CREATE OPERATOR <= (
	LEFTARG = seg,
	RIGHTARG = seg,
	PROCEDURE = seg_le,
	COMMUTATOR = '>=',
	NEGATOR = '>',
	RESTRICT = scalarltsel,
	JOIN = scalarltjoinsel
);

CREATE OPERATOR > (
	LEFTARG = seg,
	RIGHTARG = seg,
	PROCEDURE = seg_gt,
	COMMUTATOR = '<',
	NEGATOR = '<=',
	RESTRICT = scalargtsel,
	JOIN = scalargtjoinsel
);

CREATE OPERATOR >= (
	LEFTARG = seg,
	RIGHTARG = seg,
	PROCEDURE = seg_ge,
	COMMUTATOR = '<=',
	NEGATOR = '<',
	RESTRICT = scalargtsel,
	JOIN = scalargtjoinsel
);

CREATE OPERATOR << (
	LEFTARG = seg,
	RIGHTARG = seg,
	PROCEDURE = seg_left,
	COMMUTATOR = '>>',
	RESTRICT = positionsel,
	JOIN = positionjoinsel
);

CREATE OPERATOR &< (
	LEFTARG = seg,
	RIGHTARG = seg,
	PROCEDURE = seg_over_left,
	RESTRICT = positionsel,
	JOIN = positionjoinsel
);

CREATE OPERATOR && (
	LEFTARG = seg,
	RIGHTARG = seg,
	PROCEDURE = seg_overlap,
	COMMUTATOR = '&&',
	RESTRICT = areasel,
	JOIN = areajoinsel
);

CREATE OPERATOR &> (
	LEFTARG = seg,
	RIGHTARG = seg,
	PROCEDURE = seg_over_right,
	RESTRICT = positionsel,
	JOIN = positionjoinsel
);

CREATE OPERATOR >> (
	LEFTARG = seg,
	RIGHTARG = seg,
	PROCEDURE = seg_right,
	COMMUTATOR = '<<',
	RESTRICT = positionsel,
	JOIN = positionjoinsel
);

CREATE OPERATOR = (
	LEFTARG = seg,
	RIGHTARG = seg,
	PROCEDURE = seg_same,
	COMMUTATOR = '=',
	NEGATOR = '<>',
	RESTRICT = eqsel,
	JOIN = eqjoinsel,
	MERGES
);

CREATE OPERATOR <> (
	LEFTARG = seg,
	RIGHTARG = seg,
	PROCEDURE = seg_different,
	COMMUTATOR = '<>',
	NEGATOR = '=',
	RESTRICT = neqsel,
	JOIN = neqjoinsel
);

CREATE OPERATOR @> (
	LEFTARG = seg,
	RIGHTARG = seg,
	PROCEDURE = seg_contains,
	COMMUTATOR = '<@',
	RESTRICT = contsel,
	JOIN = contjoinsel
);

CREATE OPERATOR <@ (
	LEFTARG = seg,
	RIGHTARG = seg,
	PROCEDURE = seg_contained,
	COMMUTATOR = '@>',
	RESTRICT = contsel,
	JOIN = contjoinsel
);

-- obsolete:
CREATE OPERATOR @ (
	LEFTARG = seg,
	RIGHTARG = seg,
	PROCEDURE = seg_contains,
	COMMUTATOR = '~',
	RESTRICT = contsel,
	JOIN = contjoinsel
);

CREATE OPERATOR ~ (
	LEFTARG = seg,
	RIGHTARG = seg,
	PROCEDURE = seg_contained,
	COMMUTATOR = '@',
	RESTRICT = contsel,
	JOIN = contjoinsel
);


-- define the GiST support methods
CREATE OR REPLACE FUNCTION gseg_consistent(internal,seg,int,oid,internal)
RETURNS bool
AS '$libdir/seg'
LANGUAGE C IMMUTABLE STRICT;

CREATE OR REPLACE FUNCTION gseg_compress(internal)
RETURNS internal 
AS '$libdir/seg'
LANGUAGE C IMMUTABLE STRICT;

CREATE OR REPLACE FUNCTION gseg_decompress(internal)
RETURNS internal 
AS '$libdir/seg'
LANGUAGE C IMMUTABLE STRICT;

CREATE OR REPLACE FUNCTION gseg_penalty(internal,internal,internal)
RETURNS internal
AS '$libdir/seg'
LANGUAGE C IMMUTABLE STRICT;

CREATE OR REPLACE FUNCTION gseg_picksplit(internal, internal)
RETURNS internal
AS '$libdir/seg'
LANGUAGE C IMMUTABLE STRICT;

CREATE OR REPLACE FUNCTION gseg_union(internal, internal)
RETURNS seg 
AS '$libdir/seg'
LANGUAGE C IMMUTABLE STRICT;

CREATE OR REPLACE FUNCTION gseg_same(seg, seg, internal)
RETURNS internal 
AS '$libdir/seg'
LANGUAGE C IMMUTABLE STRICT;


-- Create the operator classes for indexing

CREATE OPERATOR CLASS seg_ops
    DEFAULT FOR TYPE seg USING btree AS
        OPERATOR        1       < ,
        OPERATOR        2       <= ,
        OPERATOR        3       = ,
        OPERATOR        4       >= ,
        OPERATOR        5       > ,
        FUNCTION        1       seg_cmp(seg, seg);

CREATE OPERATOR CLASS gist_seg_ops
DEFAULT FOR TYPE seg USING gist 
AS
	OPERATOR	1	<< ,
	OPERATOR	2	&< ,
	OPERATOR	3	&& ,
	OPERATOR	4	&> ,
	OPERATOR	5	>> ,
	OPERATOR	6	= ,
	OPERATOR	7	@> ,
	OPERATOR	8	<@ ,
	OPERATOR	13	@ ,
	OPERATOR	14	~ ,
	FUNCTION	1	gseg_consistent (internal, seg, int, oid, internal),
	FUNCTION	2	gseg_union (internal, internal),
	FUNCTION	3	gseg_compress (internal),
	FUNCTION	4	gseg_decompress (internal),
	FUNCTION	5	gseg_penalty (internal, internal, internal),
	FUNCTION	6	gseg_picksplit (internal, internal),
	FUNCTION	7	gseg_same (seg, seg, internal);
