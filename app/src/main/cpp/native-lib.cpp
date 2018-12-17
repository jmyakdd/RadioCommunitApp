#include <jni.h>
#include <string>
#include <sys/socket.h>
#include <sys/types.h>
#include <arpa/inet.h>
#include <cstring>
#include <unistd.h>
#include <cstdio>

using namespace std;

extern "C" JNIEXPORT jstring

JNICALL
Java_radio_crte_com_radiocommunitapp_util_SocketConnectUtil_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

int sct = -1;
struct sockaddr_in server_addr;
bool isStart = false;

extern "C" JNIEXPORT jint
JNICALL
Java_radio_crte_com_radiocommunitapp_util_SocketConnectUtil_initSocket(JNIEnv *env, jobject instance,
                                                          jstring ip_, jint port) {
    if (sct != -1) {
        return -3;
    }
    const char *ip = env->GetStringUTFChars(ip_, 0);
    sct = socket(PF_LOCAL, SOCK_STREAM, 0);
    int ret = 0;
    if (-1 == sct) {
        return -1;
    }
    server_addr.sin_family = PF_LOCAL;
    server_addr.sin_port = htons(50050);
    inet_pton(PF_LOCAL, "192.168.2.132", &server_addr.sin_addr.s_addr);
    ret = connect(sct, (struct sockaddr *) &server_addr, sizeof(server_addr));
    if (ret == -1) {
        return -2;
    }
    env->ReleaseStringUTFChars(ip_, ip);
    isStart = true;
    return 1;
}extern "C"
JNIEXPORT jint JNICALL
Java_radio_crte_com_radiocommunitapp_util_SocketConnectUtil_sendData(JNIEnv *env, jobject instance,
                                                        jbyteArray data_) {
    jbyte *data = env->GetByteArrayElements(data_, NULL);
    int chars_len = env->GetArrayLength(data_);
    char *chars = NULL;
    chars = new char[chars_len + 1];
    memset(chars, 0, chars_len + 1);
    memcpy(chars, data, chars_len);
    send(sct, chars, strlen(chars), 0);
    env->ReleaseByteArrayElements(data_, data, 0);
    return 1;
}extern "C"
JNIEXPORT jint JNICALL
Java_radio_crte_com_radiocommunitapp_util_SocketConnectUtil_receiveData(JNIEnv *env, jobject instance) {
    jclass clazz = env->FindClass("radio/crte/com/myapplication/MainActivity");
    if (clazz == NULL) {
        return -1;
    }
    jmethodID id = env->GetStaticMethodID(clazz,"receiveDataCallback","([BI)V");
    jbyte* buff;
    while (isStart) {
        int length = recv(sct, buff, 1024, 0);
        jbyteArray out = env->NewByteArray(1024);
        env->SetByteArrayRegion(out,0,length,buff);
        env->CallStaticVoidMethod(clazz, id, out, length);
    }
    return 0;
}extern "C"
JNIEXPORT jint JNICALL
Java_radio_crte_com_radiocommunitapp_util_SocketConnectUtil_close(JNIEnv *env, jobject instance) {
    isStart = false;
    shutdown(sct,SHUT_RDWR);
}