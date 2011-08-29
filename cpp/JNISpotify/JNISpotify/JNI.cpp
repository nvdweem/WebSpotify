#include "jni.h"
#include "api.h";

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
//fprintf(stderr, "%s\n", methodName);
	JNIEnv* env = attachThread();
	jclass cls = env->GetObjectClass(object);
	jmethodID methodId = env->GetMethodID(cls, methodName, "()V");
	env->CallVoidMethod(object, methodId);
	detachThread();
}

void callVoidMethod(jobject object, const char* methodName, int arg0) {
//fprintf(stderr, "%s\n", methodName);
	JNIEnv* env = attachThread();
	jclass cls = env->GetObjectClass(object);
	jmethodID methodId = env->GetMethodID(cls, methodName, "(I)V");
	env->CallVoidMethod(object, methodId, arg0);
	detachThread();
}

void callVoidMethod(jobject object, const char* methodName, int arg0, int arg1, int arg2) {
//fprintf(stderr, "%s\n", methodName);
	JNIEnv* env = attachThread();
	jclass cls = env->GetObjectClass(object);
	jmethodID methodId = env->GetMethodID(cls, methodName, "(III)V");
	env->CallVoidMethod(object, methodId, arg0, arg1, arg2);
	detachThread();
}

void callVoidMethod(jobject object, const char* methodName, const char* arg0) {
//fprintf(stderr, "%s\n", methodName);
	JNIEnv* env = attachThread();
	jclass cls = env->GetObjectClass(object);
	jmethodID methodId = env->GetMethodID(cls, methodName, "(Ljava/lang/String;)V");
	jstring _arg0 = env->NewStringUTF(arg0);
	env->CallVoidMethod(object, methodId, _arg0);
	detachThread();
}

void callVoidMethod(jobject object, const char* methodName, jobject arg0) {
//fprintf(stderr, "%s\n", methodName);
	JNIEnv* env = attachThread();
//fprintf(stderr, "Attached: ");
	jclass cls = env->GetObjectClass(object);
//fprintf(stderr, "Class %s: ", cls);
	jmethodID methodId = env->GetMethodID(cls, methodName, "(Ljava/lang/Object;)V");
//fprintf(stderr, "Method %s: ", methodId);
	env->CallVoidMethod(object, methodId, arg0);
//fprintf(stderr, "Method called: ");
	detachThread();
//fprintf(stderr, "Detached\n");
}

jobject callObjectMethod(jobject object, const char* methodName, const char* arg0) {
//fprintf(stderr, "%s\n", methodName);
	JNIEnv* env = attachThread();
//fprintf(stderr, "Attached: ");
	jclass cls = env->GetObjectClass(object);
//fprintf(stderr, "Class %s: ", cls);
	jmethodID methodId = env->GetMethodID(cls, methodName, "(Ljava/lang/String;)Ljava/lang/Object;");
//fprintf(stderr, "Method %s: ", methodId);
	jstring _arg0 = env->NewStringUTF(arg0);
//fprintf(stderr, "Arg %s: ", _arg0);
	jobject result = env->CallObjectMethod(object, methodId, _arg0);
//fprintf(stderr, "Result %s: ", result);
	detachThread();
//fprintf(stderr, "Detached\n");
	return result;
}

int callIntMethod(JNIEnv *env, jobject object, const char* methodName, jbyteArray arg0) {
//fprintf(stderr, "%s\n", methodName);

jclass cls = env->GetObjectClass(object);
//fprintf(stderr, "Class %s: ", cls);
	jmethodID methodId = env->GetMethodID(cls, methodName, "([B)I");
//fprintf(stderr, "Method %s: ", methodId);
	return env->CallIntMethod(object, methodId, arg0);
//fprintf(stderr, "Method called\n");
}

void callVoidMethodB(JNIEnv *env, jobject object, const char* methodName, jbyteArray arg0) {
//fprintf(stderr, "%s\n", methodName);

jclass cls = env->GetObjectClass(object);
//fprintf(stderr, "Class %s: ", cls);
	jmethodID methodId = env->GetMethodID(cls, methodName, "([B)V");
//fprintf(stderr, "Method %s: ", methodId);
	env->CallVoidMethod(object, methodId, arg0);
//fprintf(stderr, "Method called\n");
}