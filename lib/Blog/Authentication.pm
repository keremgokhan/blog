package Blog::Authentication;

use Crypt::PBKDF2;

use Blog::Db::User;

sub validate_user {
    my ($app, $username, $password, $extradata) = @_;

    my @result = Blog::Db::User->search(
        name => $username
    );

    return unless scalar @result;

    my $user = $result[0];

    my $pbkdf2 = Crypt::PBKDF2->new(
        hash_class => 'HMACSHA2',
        iterations => 10000,
        salt_len => 10,
    );

    return unless ($pbkdf2->validate($user->password, $password));

    return $user->id;
}

sub load_user {
    my ($app, $uid) = @_;

    my $user = Blog::Db::User->retrieve($uid);

    return $user;
}

1;
