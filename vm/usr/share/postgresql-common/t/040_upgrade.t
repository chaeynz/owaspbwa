# Test upgrading from the oldest version to the latest, using the default
# configuration file.

use strict; 

use lib 't';
use TestLib;

use lib '/usr/share/postgresql-common';
use PgCommon;

use Test::More tests => ($#MAJORS == 0) ? 1 : 84;

if ($#MAJORS == 0) {
    pass 'only one major version installed, skipping upgrade tests';
    exit 0;
}

# create cluster
ok ((system "pg_createcluster $MAJORS[0] upgr >/dev/null") == 0,
    "pg_createcluster $MAJORS[0] upgr");
exec_as 'root', "sed -i '/^local.*postgres/ s/\$/\\nlocal all foo trust/' /etc/postgresql/$MAJORS[0]/upgr/pg_hba.conf";
is ((system "pg_ctlcluster $MAJORS[0] upgr start"), 0, 'Starting upgr cluster');

# Create nobody user, test database, and put a table into it
is ((exec_as 'postgres', 'createuser nobody -D -R -s && createdb -O nobody test && createdb -O nobody testnc'), 
	0, 'Create nobody user and test databases');
is ((exec_as 'nobody', 'psql test -c "create table phone (name varchar(255) PRIMARY KEY, tel int NOT NULL)"'), 
    0, 'create table');
is ((exec_as 'nobody', 'psql test -c "insert into phone values (\'Alice\', 2)"'), 0, 'insert Alice into phone table');
is ((exec_as 'postgres', 'psql template1 -c "update pg_database set datallowconn = \'f\' where datname = \'testnc\'"'), 
    0, 'disallow connection to testnc');

# create a sequence
is ((exec_as 'nobody', 'psql test -c "create sequence odd10 increment by 2 minvalue 1 maxvalue 10 cycle"'),
    0, 'create sequence');
is_program_out 'nobody', 'psql -Atc "select nextval(\'odd10\')" test', 0, "1\n",
    'check next sequence value';
is_program_out 'nobody', 'psql -Atc "select nextval(\'odd10\')" test', 0, "3\n",
    'check next sequence value';

# create stored procedures
is_program_out 'postgres', 'createlang plpgsql test', 0, '', 'createlang plpgsql test';
is_program_out 'nobody', 'psql test -c "CREATE FUNCTION inc2(integer) RETURNS integer LANGUAGE plpgsql AS \'BEGIN RETURN \$1 + 2; END;\';"',
    0, "CREATE FUNCTION\n", 'create function inc2';
is_program_out 'postgres', "psql -c \"update pg_proc set probin = '/usr/lib/postgresql/$MAJORS[0]/lib/plpgsql.so' where proname = 'plpgsql_call_handler';\" test",
    0, "UPDATE 1\n", 'hardcoding plpgsql lib path';
is_program_out 'nobody', 'psql test -c "CREATE FUNCTION inc3(integer) RETURNS integer LANGUAGE plpgsql AS \'BEGIN RETURN \$1 + 3; END;\';"',
    0, "CREATE FUNCTION\n", 'create function inc3';
is_program_out 'nobody', 'psql -Atc "select inc2(3)" test', 0, "5\n", 
    'call function inc2';
is_program_out 'nobody', 'psql -Atc "select inc3(3)" test', 0, "6\n", 
    'call function inc3';

# create user and group
is_program_out 'postgres', "psql -qc 'create user foo' template1", 0, '',
    'create user foo';
is_program_out 'postgres', "psql -qc 'create group gfoo' template1", 0, '', 
    'create group gfoo';

# create per-database and per-table ACL	
is_program_out 'postgres', "psql -qc 'GRANT CREATE ON DATABASE test TO foo'", 0, '',
    'GRANT CREATE ON DATABASE';
is_program_out 'postgres', "psql -qc 'GRANT INSERT ON phone TO foo' test", 0, '',
    'GRANT INSERT';

# exercise ACL on old database to ensure they are working
is_program_out 'nobody', 'psql -U foo -qc "CREATE SCHEMA s_foo" test', 0, '',
    'CREATE SCHEMA on old cluster (ACL)';
is_program_out 'nobody', 'psql -U foo -qc "INSERT INTO phone VALUES (\'Bob\', 1)" test', 
    0, '', 'insert Bob into phone table (ACL)';

# set database option
is_program_out 'postgres', 'psql -qc "ALTER DATABASE test SET DateStyle = \'ISO, YMD\'"',
    0, '', 'set database parameter';

# Check clusters
like_program_out 'nobody', 'pg_lsclusters -h', 0,
    qr/^$MAJORS[0]\s+upgr\s+5432 online postgres/;

# Check SELECT in original cluster
my $select_old;
is ((exec_as 'nobody', 'psql -tAc "select * from phone order by name" test', $select_old), 0, 'SELECT succeeds');
is ($$select_old, 'Alice|2
Bob|1
', 'check SELECT output');

# create inaccessible cwd, to check for confusing error messages
mkdir '/tmp/pgtest/' or die "Could not create temporary test directory /tmp/pgtest: $!";
chmod 0100, '/tmp/pgtest/';
chdir '/tmp/pgtest';

# Upgrade to latest version
my $outref;
is ((exec_as 0, "(pg_upgradecluster $MAJORS[0] upgr | sed -e 's/^/STDOUT: /')", $outref, 0), 0, 'pg_upgradecluster succeeds');
like $$outref, qr/Starting target cluster/, 'pg_upgradecluster reported cluster startup';
like $$outref, qr/Success. Please check/, 'pg_upgradecluster reported successful operation';
my @err = grep (!/^STDOUT: /, split (/\n/, $$outref));
if (@err) {
    fail 'no error messages during upgrade';
    print (join ("\n", @err));
} else {
    pass "no error messages during upgrade";
}

# remove inaccessible test cwd
chdir '/';
rmdir '/tmp/pgtest/';

# Check clusters
is_program_out 'nobody', 'pg_lsclusters -h', 0, 
    "$MAJORS[0]     upgr      5433 down   postgres /var/lib/postgresql/$MAJORS[0]/upgr       /var/log/postgresql/postgresql-$MAJORS[0]-upgr.log
$MAJORS[-1]     upgr      5432 online postgres /var/lib/postgresql/$MAJORS[-1]/upgr       /var/log/postgresql/postgresql-$MAJORS[-1]-upgr.log
", 'pg_lsclusters output';

# Check that SELECT output is identical
is_program_out 'nobody', 'psql -tAc "select * from phone order by name" test', 0,
    $$select_old, 'SELECT output is the same in original and upgraded cluster';

# Check sequence value
is_program_out 'nobody', 'psql -Atc "select nextval(\'odd10\')" test', 0, "5\n",
    'check next sequence value';
is_program_out 'nobody', 'psql -Atc "select nextval(\'odd10\')" test', 0, "7\n",
    'check next sequence value';
is_program_out 'nobody', 'psql -Atc "select nextval(\'odd10\')" test', 0, "9\n",
    'check next sequence value';
is_program_out 'nobody', 'psql -Atc "select nextval(\'odd10\')" test', 0, "1\n",
    'check next sequence value (wrap)';

# check stored procedures
is_program_out 'nobody', 'psql -Atc "select inc2(-3)" test', 0, "-1\n", 
    'call function inc2';
is_program_out 'nobody', 'psql -Atc "select inc3(1)" test', 0, "4\n", 
    'call function inc3 (formerly hardcoded path)';

# Check connection permissions
is_program_out 'nobody', 'psql -tAc "select datname, datallowconn from pg_database order by datname" template1', 0,
    'postgres|t
template0|f
template1|t
test|t
testnc|f
', 'dataallowconn values';

# check ACLs
is_program_out 'nobody', 'psql -U foo -qc "CREATE SCHEMA s_bar" test', 0, '',
    'CREATE SCHEMA on new cluster (ACL)';
is_program_out 'nobody', 'psql -U foo -qc "INSERT INTO phone VALUES (\'Chris\', 5)" test', 
    0, '', 'insert Chris into phone table (ACL)';

# check DB parameter
is_program_out 'postgres', 'psql -Atc "SHOW DateStyle" test', 0, 'ISO, YMD
', 'check database parameter';

# stop servers, clean up
is ((system "pg_dropcluster $MAJORS[0] upgr --stop"), 0, 'Dropping original cluster');
is ((system "pg_ctlcluster $MAJORS[-1] upgr restart"), 0, 'Restarting upgraded cluster');
is_program_out 'nobody', 'psql -Atc "select nextval(\'odd10\')" test', 0, "3\n",
    'upgraded cluster still works after removing old one';
is ((system "pg_dropcluster $MAJORS[-1] upgr --stop"), 0, 'Dropping upgraded cluster');

check_clean;

# vim: filetype=perl
