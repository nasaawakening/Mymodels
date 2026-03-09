#include <jni.h>
#include <string>
#include "llama.h"

static struct llama_model *model;
static struct llama_context *ctx;

extern "C"
JNIEXPORT void JNICALL
Java_com_mymodels_ai_engine_GGUFRunner_loadModel(
        JNIEnv *env,
        jobject thiz,
        jstring path) {

    const char *model_path = env->GetStringUTFChars(path, 0);

    llama_backend_init(false);

    model = llama_load_model_from_file(model_path, llama_model_default_params());

    ctx = llama_new_context_with_model(model, llama_context_default_params());

}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_mymodels_ai_engine_GGUFRunner_generate(
        JNIEnv *env,
        jobject thiz,
        jstring prompt) {

    const char *input = env->GetStringUTFChars(prompt, 0);

    std::string response = "Model response placeholder";

    return env->NewStringUTF(response.c_str());
}
