#!/bin/bash
set -euo pipefail
IFS=$'\n\t'

export PROJECT_NAME="EPUBCheckFX"
export TEMP_WORKING_DIR="./target/icons"
export ICONSET_DIRECTORY="$TEMP_WORKING_DIR/$PROJECT_NAME.iconset"
export ORIGINAL_ICON="./icon.png"

if [ ! -d $TEMP_WORKING_DIR ]; then
  mkdir "$TEMP_WORKING_DIR"
fi

if [ ! -d $ICONSET_DIRECTORY ]; then
  mkdir "$ICONSET_DIRECTORY"
fi

# normal screen icons
for SIZE in 32 64 128 256 512; do
  echo "Creating normal icon '$ICONSET_DIRECTORY/icon_${SIZE}x${SIZE}.png'"
  convert "$ORIGINAL_ICON" -resize "${SIZE}x${SIZE}!" "$ICONSET_DIRECTORY/icon_${SIZE}x${SIZE}.png"
done

# high resolution icons
for SIZE in 64 128 256 512 1024; do
  echo "Creating high resolution icon '$ICONSET_DIRECTORY/icon_${SIZE}x${SIZE}.png'"
  convert "$ORIGINAL_ICON" -resize "${SIZE}x${SIZE}!" "$ICONSET_DIRECTORY/icon_$(expr $SIZE / 2)x$(expr $SIZE / 2)@2x.png"
done

# make a multi-resolution icon (icns)
iconutil -c icns -o "$TEMP_WORKING_DIR/$PROJECT_NAME.icns" "$ICONSET_DIRECTORY"

# make a multi-resolution icon (ico, requires ImageMagick)
convert "${ICONSET_DIRECTORY}/"* "$TEMP_WORKING_DIR/$PROJECT_NAME.ico"

# install the icons in their respective locations
cp "$TEMP_WORKING_DIR/$PROJECT_NAME.icns" "./src/main/assembly/dist-macos/"
cp "$TEMP_WORKING_DIR/$PROJECT_NAME.ico" "./src/main/assembly/dist-windows/"

echo "Done"
