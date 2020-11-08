#ifndef PARSER_UTIL_HPP
#define PARSER_UTIL_HPP

#include <iostream>
#include <istream>
#include <vector>

static const int MAGIC_L = 4;

class BadFileFormatException : public std::exception {
public:
    std::string msg;

    explicit BadFileFormatException(const std::string& msg): msg(msg) {}

    const char * what() const noexcept override{
        return msg.c_str();
    }
};

template<class T = int64_t>
T readInt(std::istream &file) {
    T number;
    file.read(reinterpret_cast<char *>(&number), sizeof(T));
    if (file.fail()) {
        throw BadFileFormatException("Too short file!");
    }
    return number;
}

static std::vector<char> readData(std::istream &file, int64_t length){
    auto data = std::vector<char>((unsigned long) length);
    file.read(data.data(), length);
    if (file.fail()) {
        throw BadFileFormatException("Too short file!");
    }
    return data;
}

static std::string readString(std::istream &file, int64_t length){
    auto data = readData(file, length);
    return std::string(data.begin(), data.end());
}

#endif //PARSER_UTIL_HPP
