find_package(ImageMagick COMPONENTS Magick++ MagickCore REQUIRED)
include_directories(${ImageMagick_Magick++_INCLUDE_DIR})

set(HEADER_FILES
        CAFF.hpp
        CIFF.hpp
        Date.hpp
        util.hpp
        )

set(SOURCE_FILES
        CAFF.cpp
        CIFF.cpp
        )

add_executable(${PROJECT_NAME}
        main.cpp ${SOURCE_FILES}
        ${HEADER_FILES}
        )

target_compile_definitions(${PROJECT_NAME} PUBLIC
        MAGICKCORE_QUANTUM_DEPTH=16
        MAGICKCORE_HDRI_ENABLE=1)

target_link_libraries(${PROJECT_NAME} ${ImageMagick_LIBRARIES})

add_library(${PROJECT_NAME}_lib STATIC
        ${SOURCE_FILES}
        ${HEADER_FILES}
        )

target_include_directories (${PROJECT_NAME}_lib PUBLIC ${CMAKE_CURRENT_SOURCE_DIR})

