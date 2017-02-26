#!/bin/bash
HOST='env3d.com'
USER='env3d-org@env3d.com'
PASSWD='applet'


cd ../libs/
ftp -n $HOST <<EOF
quote USER $USER
quote PASS $PASSWD
prompt off
binary
cd lib4
mput *.lzma
mput *.gz
quit
exit 0

EOF