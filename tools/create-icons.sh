#!/bin/bash

export PROJECT="EPUBCheckFX"
export ICONDIR="./src/main/assembly/dist-macos/$PROJECT.iconset"
export ORIGICON="./icon.png"
#export ORIGICON16="./$PROJECT 32x32.png"

mkdir "$ICONDIR"

#cp "./$PROJECT 16x16.png" "$ICONDIR/icon_16x16.png"
#cp "./$PROJECT 32x32.png" "$ICONDIR/icon_16x16@2x.png"

# normal screen icons
for SIZE in 32 64 128 256 512; do
sips -z $SIZE $SIZE "$ORIGICON" --out "$ICONDIR/icon_${SIZE}x${SIZE}.png" ;
done

# high resolution icons
for SIZE in 64 128 256 512 1024; do
sips -z $SIZE $SIZE "$ORIGICON" --out "$ICONDIR/icon_$(expr $SIZE / 2)x$(expr $SIZE / 2)@2x.png" ;
done

# make a multi-resolution icon (icns)
iconutil -c icns -o "./src/main/assembly/$PROJECT.icns" "$ICONDIR"
#rm -rf "$ICONDIR"
echo "Done"
