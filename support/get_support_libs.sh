#!/bin/bash

files=(
"eventbus.jar"
"gluegen-rt.jar"
"j-ogg-oggd.jar"
"j-ogg-vorbisd.jar"
"jME3-blender.jar"
"jME3-core.jar"
"jME3-desktop.jar"
"jME3-effects.jar"
"jME3-jbullet.jar"
"jME3-jogg.jar"
"jME3-lwjgl-natives.jar"
"jME3-lwjgl.jar"
"jME3-networking.jar"
"jME3-niftygui.jar"
"jME3-plugins.jar"
"jME3-terrain.jar"
"jMonkeyEngine3.jar"
"jbullet.jar"
"jinput.jar"
"lwjgl.jar"
"nifty-default-controls.jar"
"nifty-style-black.jar"
"nifty.jar"
"rsyntaxtextarea.jar"
"stack-alloc.jar"
"vecmath.jar"
"xmlpull-xpp3.jar"
)

mkdir -p ../libs
cd ../libs
for file in "${files[@]}"
do
    echo "copying $file"
    cp ../dist/env3d_template/+libs/$file .
done

# Create natives jar - use with applet and webstart deployment
jar xf jme3-lwjgl-natives.jar
jar cf windows_natives.jar -C native/windows/ .
jar cf macosx_natives.jar -C native/macosx/ .
jar cf linux_natives.jar -C native/linux/ .
jar cf solaris_natives.jar -C native/solaris/ .

rm -r native
rm jme3-lwjgl-natives.jar

files+=(
"windows_natives.jar" 
"macosx_natives.jar" 
"linux_natives.jar" 
"solaris_natives.jar"
)

# from the support directory
cp ../support/lwjgl_util_applet.jar .
cp ../support/lzma.jar .
cp ../support/bluejext.jar .

# sign them all
cp ../support/resign.sh .
cp ../support/env3dkey .

for file in "${files[@]}"
do
    echo "signing $file"
    ./resign.sh $file
done

