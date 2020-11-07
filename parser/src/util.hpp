#ifndef PARSER_UTIL_HPP
#define PARSER_UTIL_HPP

#include <iostream>
#include <istream>
#include <vector>

static const int MAGIC_L = 4;

template<class T = int64_t>
T readInt(std::istream &file) {
    T number;
    file.read(reinterpret_cast<char *>(&number), sizeof(T));
    return number;
}

static std::vector<char> readData(std::istream &file, int64_t length){
    auto data = std::vector<char>((unsigned long) length);
    file.read(data.data(), length);
    return data;
}

static std::string readString(std::istream &file, int64_t length){
    auto data = readData(file, length);
    return std::string(data.begin(), data.end());
}

#endif //PARSER_UTIL_HPP
