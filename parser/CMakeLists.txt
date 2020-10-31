cmake_minimum_required(VERSION 3.10)
project(parser)

set(CMAKE_CXX_STANDARD 17)

add_executable(parser src/main.cpp src/CAFF.cpp src/CAFF.hpp src/CIFF.cpp src/CIFF.hpp src/Date.hpp src/util.hpp)