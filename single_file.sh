#!/usr/bin/env bash

if ! hash fd 2>/dev/null; then
  echo "This script requires fd to run, please install fd (https://github.com/sharkdp/fd) and run again";
  exit 0;
fi

if ! hash as-tree 2>/dev/null; then
  echo "This script requires as-tree to run, please install as-tree (https://github.com/jez/as-tree) and run again";
  exit 0;
fi

if ! hash boxes 2>/dev/null; then
  echo "This script requires boxes to run, please install (https://github.com/ascii-boxes/boxes) and run again";
  exit 0;
fi

rm code.txt;
touch code.txt;

{
  echo "File Tree" | boxes -a hcvcjc -d stone;

  # Print file tree, uses fd and as-tree instead of tree as it has slightly cleaner syntax and ensures that the same
  # files are included in the tree as are included in the file
  fd -E "docs" -E "*.jar" -E "*.form" -E "gradlew" -E "*.sh" -E "*.txt" -E "LICENSE" --type file | as-tree --color never;
  echo;

  # Finds files, echos the name and path in a box and outputs the files contents
  fd -E "docs" -E "*.jar" -E "*.form" -E "gradlew" -E "*.sh" -E "*.txt" -E "LICENSE" --type file --exec bash -c \
  "echo -e 'File: {/}\nPath: {}' | boxes -a hcvcjc -d stone && cat {} && echo";
} >> code.txt
