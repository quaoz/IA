#!/usr/bin/env sh

# https://stackoverflow.com/questions/226703/how-do-i-prompt-for-yes-no-cancel-input-in-a-linux-shell-script/27875395#27875395
printf 'This will delete the database files. Do you want to continue (y/n)? '

old_stty_cfg=$(stty -g)
stty raw -echo ; answer=$(head -c 1) ; stty "$old_stty_cfg"

if echo "$answer" | grep -iq "^y" ;then
    rm moths.db moths.json records.db records.json users.db users.json
fi
