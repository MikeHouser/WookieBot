## sudo chmod 700 createlib.sh
## ./createlib.sh

gcc -I/Library/Java/JavaVirtualMachines/jdk1.8.0_91.jdk/Contents/Home/include/darwin -I/Library/Java/JavaVirtualMachines/jdk1.8.0_91.jdk/Contents/Home/include -dynamiclib -o clib/libhmc5883l_lib.dylib clib/hmc5883l.c
echo "Created clib/libhmc5883l_lib.dylib"