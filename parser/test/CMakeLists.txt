cmake_minimum_required(VERSION 3.16.3)

project(googletest-git NONE)

include(FetchContent)
FetchContent_Declare(
        googletest
        GIT_REPOSITORY https://github.com/google/googletest.git
        GIT_TAG        master
)

FetchContent_MakeAvailable(googletest)
if(NOT googletest_POPULATED)
    FetchContent_Populate(googletest)
    add_subdirectory(${googletest_SOURCE_DIR} ${googletest_BINARY_DIR} EXCLUDE_FROM_ALL)
endif()

add_executable(googletest-git test.cpp)

target_compile_definitions(${CMAKE_PROJECT_NAME}_lib PUBLIC
        MAGICKCORE_QUANTUM_DEPTH=16
        MAGICKCORE_HDRI_ENABLE=1)

target_link_libraries(googletest-git gtest gtest_main ${CMAKE_PROJECT_NAME}_lib nlohmann_json::nlohmann_json)
