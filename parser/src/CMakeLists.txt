# protection
add_compile_options("-fstack-protector-strong")
set(CMAKE_POSITION_INDEPENDENT_CODE ON)

# main files
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

# Magick++
find_package(ImageMagick COMPONENTS Magick++ MagickCore REQUIRED)
include_directories(${ImageMagick_Magick++_INCLUDE_DIR})

target_compile_definitions(${PROJECT_NAME} PUBLIC
        MAGICKCORE_QUANTUM_DEPTH=16
        MAGICKCORE_HDRI_ENABLE=1)

# json
include(FetchContent)
FetchContent_Declare(json
        GIT_REPOSITORY https://github.com/ArthurSonzogni/nlohmann_json_cmake_fetchcontent
        GIT_TAG v3.9.1)

FetchContent_MakeAvailable(json)
if (NOT json_POPULATED)
    FetchContent_Populate(json)
    add_subdirectory(${json_SOURCE_DIR} ${json_BINARY_DIR} EXCLUDE_FROM_ALL)
endif ()

add_library(${PROJECT_NAME}_lib  STATIC
        ${SOURCE_FILES}
        ${HEADER_FILES}
        )

target_link_libraries(${PROJECT_NAME} PRIVATE ${ImageMagick_LIBRARIES})
target_link_libraries(${PROJECT_NAME} PUBLIC nlohmann_json::nlohmann_json)

target_link_libraries(${PROJECT_NAME}_lib PRIVATE ${ImageMagick_LIBRARIES})
target_link_libraries(${PROJECT_NAME}_lib PUBLIC nlohmann_json::nlohmann_json)

target_include_directories(${PROJECT_NAME}_lib PUBLIC ${CMAKE_CURRENT_SOURCE_DIR})

install (TARGETS ${PROJECT_NAME}
        ARCHIVE DESTINATION lib
        LIBRARY DESTINATION lib
        RUNTIME DESTINATION bin)
