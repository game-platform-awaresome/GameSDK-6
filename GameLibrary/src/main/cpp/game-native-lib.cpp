#include <jni.h>
#include <string>
#include <stdlib.h>

jstring getSignature(JNIEnv *env, jobject context) {
    jclass contextClass = env->FindClass("android/content/Context");
    jmethodID getPmMethod = env->GetMethodID(contextClass, "getPackageManager", "()Landroid/content/pm/PackageManager;");
    jobject pmObject = env->CallObjectMethod(context, getPmMethod);

    jclass pmClass = env->FindClass("android/content/pm/PackageManager");
    jmethodID getPackageInfoMethod = env->GetMethodID(pmClass, "getPackageInfo", "(Ljava/lang/String;I)Landroid/content/pm/PackageInfo;");

    jmethodID getPackageNameMethod = env->GetMethodID(contextClass, "getPackageName", "()Ljava/lang/String;");
    jobject pkgName = env->CallObjectMethod(context, getPackageNameMethod);
    jobject pkgInfo = env->CallObjectMethod(pmObject, getPackageInfoMethod, pkgName, 64);

    jclass packageInfoClass = env->FindClass("android/content/pm/PackageInfo");
    jfieldID signaturesField = env->GetFieldID(packageInfoClass, "signatures", "[Landroid/content/pm/Signature;");
    jobjectArray signArray = (jobjectArray) env->GetObjectField(pkgInfo, signaturesField);
    jobject signObject = env->GetObjectArrayElement(signArray, 0);

    jclass signatureClass = env->FindClass("android/content/pm/Signature");
    jmethodID toByteMethod = env->GetMethodID(signatureClass, "toByteArray", "()[B");
    jobject byteObject = env->CallObjectMethod(signObject, toByteMethod);

    jclass mdClass = env->FindClass("java/security/MessageDigest");
    jmethodID mdInstanceMethod = env->GetStaticMethodID(mdClass, "getInstance", "(Ljava/lang/String;)Ljava/security/MessageDigest;");
    jobject mdObject = env->CallStaticObjectMethod(mdClass, mdInstanceMethod, env->NewStringUTF("SHA"));

    jmethodID updateMethod = env->GetMethodID(mdClass, "update", "([B)V");
    env->CallVoidMethod(mdObject, updateMethod, byteObject);
    jmethodID digestMethod = env->GetMethodID(mdClass, "digest", "()[B");
    jobject digestByteObject = env->CallObjectMethod(mdObject, digestMethod);

    jclass base64Class = env->FindClass("android/util/Base64");
    jmethodID encodeMethod = env->GetStaticMethodID(base64Class, "encodeToString", "([BI)Ljava/lang/String;");
    jobject strObject = env->CallStaticObjectMethod(base64Class, encodeMethod, digestByteObject, 0);

    jclass stringClass = env->FindClass("java/lang/String");
    jmethodID trimMethod = env->GetMethodID(stringClass, "trim", "()Ljava/lang/String;");
    jstring result = (jstring) env->CallObjectMethod(strObject, trimMethod);

    return result;
}

jstring crypt(JNIEnv *env, jclass type, jstring before) {
    jclass stringClass = env->FindClass("java/lang/String");
    jmethodID getByteMethod = env->GetMethodID(stringClass, "getBytes", "()[B");
    jobject byteObject = env->CallObjectMethod(before, getByteMethod);

    jclass mdClass = env->FindClass("java/security/MessageDigest");
    jmethodID mdInstanceMethod = env->GetStaticMethodID(mdClass, "getInstance", "(Ljava/lang/String;)Ljava/security/MessageDigest;");
    jobject mdObject = env->CallStaticObjectMethod(mdClass, mdInstanceMethod, env->NewStringUTF("MD5"));

    jmethodID updateMethod = env->GetMethodID(mdClass, "update", "([B)V");
    env->CallVoidMethod(mdObject, updateMethod, byteObject);
    jmethodID digestMethod = env->GetMethodID(mdClass, "digest", "()[B");
    jobject digestByteObject = env->CallObjectMethod(mdObject, digestMethod);
    // 调用crypt静态方法加密
    jmethodID cryptMethod = env->GetStaticMethodID(type, "crypt", "([B)Ljava/lang/String;");
    return (jstring) env->CallStaticObjectMethod(type, cryptMethod, digestByteObject);
}

extern "C"
jstring
Java_com_game_tobin_common_GameEncrypt_encrypt(JNIEnv *env, jclass type, jobject context, jobjectArray params) {
    // 获得数组的长度
    jsize len = env->GetArrayLength(params);
    // 向系统申请分配指定size(1024*1024)个字节的内存空间
    char* dest = (char*)malloc(1024*1024);
    int i=0;
    for (i=0 ; i<len; i++) {
        jstring jstr = (jstring) env->GetObjectArrayElement(params, i);
        // GetStringUTFChars将jstring转换成为UTF-8格式的char*
        // strcat 将两个char类型链接
        strcat(dest, env->GetStringUTFChars(jstr, 0));
    }
    strcat(dest, env->GetStringUTFChars(getSignature(env, context), 0));
    jstring result = env->NewStringUTF(dest);
    free(dest);
    return crypt(env, type, result);
}