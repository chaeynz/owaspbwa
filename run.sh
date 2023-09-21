#!/bin/bash
# Start Apache
/usr/local/apache2/bin/apachectl start

# Start MySQL
/usr/local/mysql/bin/mysqld_safe &

tail -f /dev/null
