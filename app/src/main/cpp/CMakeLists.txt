cmake_minimum_required(VERSION 3.22)

project(mymodels)

add_library(
        llama
        SHARED
        llama.cpp
        ggml.c
        ggml-alloc.c
        ggml-backend.c
        ggml-quants.c
        ggml-cpu.c
)

find_library(
        log-lib
        log
)

target_link_libraries(
        llama
        ${log-lib}
)
