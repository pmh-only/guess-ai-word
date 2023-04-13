#!/bin/sh
cd src/main/resources/frontend || exit 1

if which pnpm > /dev/null; then
  pnpm install
  pnpm $1
  exit 0
fi

if which npm > /dev/null; then
  npm install
  npm run $1
  exit 0
fi

echo "NPM or pNPM not found. please install node.js LTS"
exit 1