cmake_minimum_required(VERSION 3.16.3)
project(parser)

set(CMAKE_CXX_STANDARD 17)

find_package(ImageMagick COMPONENTS Magick++ REQUIRED)
include_directories(${ImageMagick_Magick++_INCLUDE_DIR})

add_executable(${PROJECT_NAME}
        src/main.cpp
        src/CAFF.cpp src/CAFF.hpp
        src/CIFF.cpp src/CIFF.hpp
        src/Date.hpp
        src/util.hpp)

target_compile_definitions(${PROJECT_NAME} PRIVATE
        MAGICKCORE_QUANTUM_DEPTH=16
        MAGICKCORE_HDRI_ENABLE=1)

target_link_libraries(${PROJECT_NAME} ${ImageMagick_LIBRARIES})
