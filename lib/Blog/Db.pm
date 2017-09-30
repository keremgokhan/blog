package Blog::Db;

use strict;
use warnings;

use base 'Class::DBI';

use Configuration;

my $db          = $Configuration::db_name;
my $dsn         = "dbi:mysql:$db;host=localhost";
my $user        = $Configuration::db_user;
my $password    = $Configuration::db_password;

__PACKAGE__->set_db( 'Main', $dsn, $user, $password );

1;
