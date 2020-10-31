#ifndef PARSER_UTIL_HPP
#define PARSER_UTIL_HPP

#include <iostream>
#include <vector>

static const int BYTE_L = 1;

static const int TWO_BYTE_L = 2;

static const int MAGIC_L = 4;

static const int EIGHT_BYTE_L = 8;

template<class T = int64_t>
T convertVectorToInt(std::vector<char> data, int start_index) {
    auto header_size_vec = std::vector(data.begin() + start_index,
                                       data.begin() + start_index + sizeof(T));
    return *reinterpret_cast<T *>(header_size_vec.data());
}

#endif //PARSER_UTIL_HPP
