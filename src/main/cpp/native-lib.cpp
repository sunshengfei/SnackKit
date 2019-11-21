#include <jni.h>
#include <string>
#define LEN 512
extern "C" JNIEXPORT jstring JNICALL
Java_com_freddon_android_snackkit_extension_tools_DesUtils_encryptPk(
        JNIEnv *env,
        jclass /* this */) {
    return env->NewStringUTF("");
}
