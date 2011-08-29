#include <jni.h>

void setEnv(JNIEnv *);
JNIEnv* getEnv();
JNIEnv* attachThread();
void removeGlobalRef(jobject);
void detachThread();
void callVoidMethod(jobject object, const char* methodName);
void callVoidMethod(jobject object, const char* methodName, int arg0);
void callVoidMethod(jobject object, const char* methodName, int arg0, int arg1, int arg2);
void callVoidMethod(jobject object, const char* methodName, const char* arg0);
void callVoidMethod(jobject object, const char* methodName, jobject arg0);
int callIntMethod(JNIEnv *env, jobject object, const char* methodName, jbyteArray arg0); 
jobject callObjectMethod(jobject object, const char* methodName, const char* arg0);
void callVoidMethodB(JNIEnv *env, jobject object, const char* methodName, jbyteArray arg0);
 