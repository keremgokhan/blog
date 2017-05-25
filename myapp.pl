#!/usr/bin/env perl
use Mojolicious::Lite;

# Documentation browser under "/perldoc"
plugin 'PODRenderer';

get '/' => sub {
  my $c = shift;
  $c->render(template => 'index');
};

app->start;
__DATA__

@@ not_found.html.ep
<!DOCTYPE html>
<html>
    <head><title>Page not found</title></head>
    <body>
        Page not found <%= $status %>
    </body>
</html>
