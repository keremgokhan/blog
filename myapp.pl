#!/usr/bin/env perl
use strict;
use warnings;

# Documentation browser under "/perldoc"
# plugin 'PODRenderer';

use FindBin;
BEGIN { unshift @INC, "$FindBin::Bin/lib" }

require Mojolicious::Commands;
Mojolicious::Commands->start_app('Blog');
