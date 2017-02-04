#include "compass_CompassProxy.h"

//int main(int argc, char **argv)
JNIEXPORT jobject JNICALL Java_compass_CompassProxy_getCompassData(JNIEnv * env, jobject obj, jfloat xOffset, jfloat yOffset) {
	float angle = 0;
	float xAxis = 0;
	float yAxis = 0;
	float zAxis = 0;
	float xAxisRaw = 0;
	float yAxisRaw = 0;
	float zAxisRaw = 0;

	printf("Fake C-Lib on OSX was called.\n");

	jclass c = (*env)->FindClass(env, "compass/CompassResult");
	jmethodID m = (*env)->GetMethodID(env, c, "<init>", "(FFFFFFF)V");
	jobject result = (*env)->NewObject(env, c, m, angle, xAxis, yAxis, zAxis, xAxisRaw, yAxisRaw, zAxisRaw);
	return result;
}
