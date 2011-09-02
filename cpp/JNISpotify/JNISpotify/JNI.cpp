#include "jni.h"
#include "api.h"

JNIEnv *cb_env;

void setEnv(JNIEnv * env) {
	cb_env = env;
}
JNIEnv* getEnv() {
	return cb_env;
}

JNIEnv* attachThread() {
	if (!cb_env) return NULL;
	JavaVM *vm;
	cb_env->GetJavaVM( &vm );
	JNIEnv* env = NULL;
	vm->AttachCurrentThread( (void**) &env, NULL );
	return env;
}

void detachThread() {
	JavaVM *vm;
	cb_env->GetJavaVM( &vm );
	vm->DetachCurrentThread();
}

void removeGlobalRef(jobject target) {
	JNIEnv *env = attachThread();
	env->DeleteGlobalRef(target);
	detachThread();
}

void callVoidMethod(jobject object, const char* methodName) {
	JNIEnv* env = attachThread();
	jclass cls = env->GetObjectClass(object);
	jmethodID methodId = env->GetMethodID(cls, methodName, "()V");
	env->CallVoidMethod(object, methodId);
	detachThread();
}

void callVoidMethod(jobject object, const char* methodName, int arg0) {
	JNIEnv* env = attachThread();
	jclass cls = env->GetObjectClass(object);
	jmethodID methodId = env->GetMethodID(cls, methodName, "(I)V");
	env->CallVoidMethod(object, methodId, arg0);
	detachThread();
}

void callVoidMethod(JNIEnv *env, jobject object, const char* methodName, int arg0) {
	jclass cls = env->GetObjectClass(object);
	jmethodID methodId = env->GetMethodID(cls, methodName, "(I)V");
	env->CallVoidMethod(object, methodId, arg0);
}

void callVoidMethod(jobject object, const char* methodName, int arg0, int arg1) {
	JNIEnv* env = attachThread();
	jclass cls = env->GetObjectClass(object);
	jmethodID methodId = env->GetMethodID(cls, methodName, "(II)V");
	env->CallVoidMethod(object, methodId, arg0, arg1);
	detachThread();
}

void callVoidMethod(jobject object, const char* methodName, const char* arg0) {
	JNIEnv* env = attachThread();
	jclass cls = env->GetObjectClass(object);
	jmethodID methodId = env->GetMethodID(cls, methodName, "(Ljava/lang/String;)V");
	jstring _arg0 = env->NewStringUTF(arg0);
	env->CallVoidMethod(object, methodId, _arg0);
	detachThread();
}

void callVoidMethod(jobject object, const char* methodName, jobject arg0) {
	JNIEnv* env = attachThread();
	jclass cls = env->GetObjectClass(object);
	jmethodID methodId = env->GetMethodID(cls, methodName, "(Ljava/lang/Object;)V");
	env->CallVoidMethod(object, methodId, arg0);
	detachThread();
}

void callVoidMethod(jobject object, const char* methodName, jobject arg0, int arg1) {
	JNIEnv* env = attachThread();
	jclass cls = env->GetObjectClass(object);
	jmethodID methodId = env->GetMethodID(cls, methodName, "(Ljava/lang/Object;I)V");
	env->CallVoidMethod(object, methodId, arg0, arg1);
	detachThread();
}

jobject callObjectMethod(jobject object, const char* methodName, const char* arg0) {
	JNIEnv* env = attachThread();
	jclass cls = env->GetObjectClass(object);
	jmethodID methodId = env->GetMethodID(cls, methodName, "(Ljava/lang/String;)Ljava/lang/Object;");
	jstring _arg0 = env->NewStringUTF(arg0);
	jobject result = env->CallObjectMethod(object, methodId, _arg0);
	detachThread();
	return result;
}

int callIntMethod(JNIEnv *env, jobject object, const char* methodName, jbyteArray arg0) {
	jclass cls = env->GetObjectClass(object);
	jmethodID methodId = env->GetMethodID(cls, methodName, "([B)I");
	return env->CallIntMethod(object, methodId, arg0);
}

void callVoidMethodB(JNIEnv *env, jobject object, const char* methodName, jbyteArray arg0) {
	jclass cls = env->GetObjectClass(object);
	jmethodID methodId = env->GetMethodID(cls, methodName, "([B)V");
	env->CallVoidMethod(object, methodId, arg0);
}

const char* callStringMethod(jobject object, const char* methodName) {
	JNIEnv* env = attachThread();
	jclass cls = env->GetObjectClass(object);
	jmethodID methodId = env->GetMethodID(cls, methodName, "()Ljava/lang/String;");
	
	jboolean iscopy;
	jobject result = env->CallObjectMethod(object, methodId);
	const char *resultStr = env->GetStringUTFChars((jstring) result, &iscopy);
	detachThread();
	return resultStr;
}
const char* callStringMethod(JNIEnv *env, jobject object, const char* methodName) {
	jclass cls = env->GetObjectClass(object);
	jmethodID methodId = env->GetMethodID(cls, methodName, "()Ljava/lang/String;");
	
	jboolean iscopy;
	jobject result = env->CallObjectMethod(object, methodId);
	const char *resultStr = env->GetStringUTFChars((jstring) result, &iscopy);
	return resultStr;
}