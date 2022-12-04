#!/usr/bin/env bash

function make_package() {
    jpackage --type "$1" \
       --name MothDB \
       --input . \
       --main-jar build/libs/IA-0.1.0.jar \
       --main-class com.github.quaoz.Main \
       --license-file LICENSE \
       --about-url https://github.com/quaoz/IA \
       --icon src/main/resources/MothDB.icns \
       --dest build/package
}

mkdir -p build/package

for type in "$@"; do
    make_package "$type" &
done

wait
