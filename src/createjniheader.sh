## sudo chmod 700 createjniheader.sh
## ./createjniheader.sh

javah -jni -o "clib/compass_CompassProxy.h"  compass.CompassProxy
echo "Created Header File (clib/compass_CompassProxy.h)"