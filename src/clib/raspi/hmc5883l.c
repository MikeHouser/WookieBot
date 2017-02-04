#define _GNU_SOURCE

#include <stdio.h>
#include <stdlib.h>
#define _USE_MATH_DEFINES
#include <math.h>
#include <fcntl.h>
#include <unistd.h>
#include <string.h>
#include <sys/ioctl.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <sys/time.h>
#include <linux/i2c-dev.h>

#include <jni.h>

#include "compass_CompassProxy.h"

const int HMC5883L_I2C_ADDR = 0x1E;

#define CONFIG_A 0x00
#define CONFIG_B 0x01
#define MODE 0x02
#define DATA 0x03 //read 6 bytes: x msb, x lsb, z msb, z lsb, y msb, y lsb
#define STATUS 0x09
#define ID_A 0x0A
#define ID_B 0x0B
#define ID_C 0x0C
#define ID_STRING "H43"
#define GAIN 1370 //000 setting

int isInitComplete = 0;
int fd;
unsigned char buf[16];

void selectDevice(int fd, int addr, char * name) {
	if (ioctl(fd, I2C_SLAVE, addr) < 0) {
		fprintf(stderr, "%s not present\n", name);
		//exit(1);
	}
}

void writeToDevice(int fd, int reg, int val) {
	char buf[2];
	buf[0] = reg;
	buf[1] = val;

	if (write(fd, buf, 2) != 2) {
		fprintf(stderr, "Can't write to ADXL345\n");
		//exit(1);
	}
}

void initCompass() {
	printf("hmc5883l.so -> VERSION 0.1 -> INIT \n");


	if ((fd = open("/dev/i2c-1", O_RDWR)) < 0) {
		// Open port for reading and writing
		fprintf(stderr, "Failed to open i2c bus\n");

		exit(1);
	}

	/* initialise HMC5883L */

	selectDevice(fd, HMC5883L_I2C_ADDR, "HMC5883L");

	//first read the 3 ID bytes
	buf[0] = ID_A;
	if ((write(fd, buf, 1)) != 1) {
		// Send the register to read from
		fprintf(stderr, "Error writing to i2c slave\n");
	}

	if (read(fd, buf, 3) != 3) {
		fprintf(stderr, "Unable to read from HMC5883L\n");
	}
	buf[3] = 0;

	//printf("Identification: '%s' ",buf);
	if (strncmp(buf, ID_STRING, 3) != 0) {
		printf("unknown sensor. Exiting.\n");
		exit(1);
	}

	//Configuration

	//writeToDevice(fd, 0x01, 0);
	writeToDevice(fd, CONFIG_A, 0b01101000); //8 sample averaging
	writeToDevice(fd, CONFIG_B, 0b00000000); //max gain
	writeToDevice(fd, MODE, 0b00000011); //idle mode

	//initiate single conversion
	writeToDevice(fd, MODE, 0b00000000); //continious sampling

	//wait 7 milliseconds
	usleep(7000);

}

//int main(int argc, char **argv)
JNIEXPORT jobject JNICALL Java_compass_CompassProxy_getCompassData(JNIEnv * env, jobject obj, jfloat xOffset, jfloat yOffset) {
	float angle = 0;
	float xAxis = 0;
	float yAxis = 0;
	float zAxis = 0;
	float xAxisRaw = 0;
	float yAxisRaw = 0;
	float zAxisRaw = 0;

	if (isInitComplete==0) {
		initCompass();
		isInitComplete = 1;
	}

	unsigned char buf[16];
	buf[0] = DATA;

	if ((write(fd, buf, 1)) != 1) {
		// Send the register to read from
		fprintf(stderr, "Error writing to i2c slave\n");
	}

	if (read(fd, buf, 6) != 6) {
		fprintf(stderr, "Unable to read from HMC5883L\n");
	} else {

		short x = (buf[0] << 8) | buf[1];
		short y = (buf[4] << 8) | buf[5];
		short z = (buf[2] << 8) | buf[3];

		xAxisRaw = (float)x;
		yAxisRaw = (float)y;
		zAxisRaw = (float)z;

		float scale = 0.92;

		x = (x - xOffset) * scale;
		y = (y - yOffset) * scale;
		z = z * scale;

		xAxis = (float)x;
		yAxis = (float)y;
		zAxis = (float)z;

		angle = atan2(y, x);

		// declination for vienna +4degrees +1minutes +0seconds => 4.016667 decimal degress => 0.070104064106 rad => 0.701
		//angle += 0.070104064106;

		if (angle < 0) {
			angle += (2 * M_PI);
		}

		/*
		if (angle > (2 * M_PI)) {
			angle -= (2 * M_PI);
		}
		*/

		angle = angle * 180 / M_PI;
	}

	jclass c = (*env)->FindClass(env, "compass/CompassResult");
	jmethodID m = (*env)->GetMethodID(env, c, "<init>", "(FFFFFFF)V");
	jobject result = (*env)->NewObject(env, c, m, angle, xAxis, yAxis, zAxis, xAxisRaw, yAxisRaw, zAxisRaw);
	return result;
}
