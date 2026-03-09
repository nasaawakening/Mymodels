cmake_minimum_required(VERSION 3.22)

project(mymodels)

add_library(
        llama
        SHARED
        main.cpp
        ggml.c
        llama.cpp
)

find_library(
        log-lib
        log
)

target_link_libraries(
        llama
        ${log-lib}
)
