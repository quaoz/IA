#!/usr/bin/env sh

rm code.txt
touch code.txt

LC_ALL=C tree -a -n --charset UTF-8 --gitignore -I '.git|gradle|docs|gradlew|gradlew.bat|LICENSE|*.db|code.txt' | sed 's/\xc2\xa0/ /g' >> code.txt

OUTPUT="$(tree -a -n -f -i --charset UTF-8 --gitignore -I '.git|gradle|docs|gradlew|gradlew.bat|LICENSE|*.db|code.txt' | xargs -L1 -I % sh -c '{ printf "\n===== % =====\n\n"; cat %; }')"
echo "$OUTPUT" >> code.txt
