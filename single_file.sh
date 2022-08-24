#!/usr/bin/env sh

rm code.txt
touch code.txt

LC_ALL=C tree -a -n --charset UTF-8 --gitignore -I '.git|gradle-wrapper.jar|docs|gradlew|gradlew.bat|LICENSE|*.db|code.txt|.editorconfig' | sed 's/\xc2\xa0/ /g' >> code.txt

OUTPUT="$(tree -a -n -f -i --charset UTF-8 --gitignore -I '.git|gradle-wrapper.jar|docs|gradlew|gradlew.bat|LICENSE|*.db|code.txt|.editorconfig' | xargs -L1 -I % sh -c '{ printf "\n========== File/Folder: % ==========\n\n"; cat %; }')"
echo "$OUTPUT" >> code.txt
