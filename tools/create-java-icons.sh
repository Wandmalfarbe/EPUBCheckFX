#!/bin/bash
set -euo pipefail
IFS=$'\n\t'

export ICONDIR="./src/main/resources/img/icons"
export ORIGINAL_ICON="./icon.png"

if [ ! -d $ICONDIR ]; then
  mkdir "$ICONDIR"
fi

# normal screen icons
for SIZE in 16 20 32 40 44 64 128 256 512; do
  echo "Creating normal icon $ICONDIR/icon_${SIZE}x${SIZE}.png"
  convert "$ORIGINAL_ICON" -resize "${SIZE}x${SIZE}!" "$ICONDIR/icon_${SIZE}x${SIZE}.png"
done

echo "Done"
