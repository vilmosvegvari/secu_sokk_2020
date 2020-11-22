cmake_minimum_required(VERSION 3.16.3)
project(parser)

set(CMAKE_CXX_STANDARD 17)

enable_testing()

add_subdirectory(src)
add_subdirectory(test EXCLUDE_FROM_ALL)
