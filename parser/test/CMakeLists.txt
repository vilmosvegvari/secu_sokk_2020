cmake_minimum_required(VERSION 3.16.3)

project(googletest-git NONE)
set(TESTNAME "test")

include(FetchContent)
FetchContent_Declare(
        googletest
        GIT_REPOSITORY https://github.com/google/googletest.git
        GIT_TAG        release-1.10.0
)

FetchContent_MakeAvailable(googletest)
if(NOT googletest_POPULATED)
    FetchContent_Populate(googletest)
    add_subdirectory(${googletest_SOURCE_DIR} ${googletest_BINARY_DIR} EXCLUDE_FROM_ALL)
endif()

add_executable(${TESTNAME} test.cpp)

target_compile_definitions(${CMAKE_PROJECT_NAME}_lib PUBLIC
        MAGICKCORE_QUANTUM_DEPTH=16
        MAGICKCORE_HDRI_ENABLE=1)

target_link_libraries(${TESTNAME} gtest gtest_main ${CMAKE_PROJECT_NAME}_lib)
