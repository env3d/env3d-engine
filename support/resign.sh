#!/bin/bash

mkdir tmp
cd tmp
jar xf ../$1
rm -r META-INF
jar cf $1 *
pack200 --no-gzip --repack $1
jarsigner -keystore ../env3dkey -storepass 123456 $1 mykey
pack200 $1.pack.gz $1
pack200 -g $1.pack $1
/opt/local/bin/lzma_alone e $1.pack $1.pack.lzma
mv $1* ../

cd ..
rm -r tmp
