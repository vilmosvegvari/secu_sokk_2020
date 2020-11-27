#ifndef PARSER_UTIL_HPP
#define PARSER_UTIL_HPP

#include <iostream>
#include <istream>
#include <utility>
#include <vector>
#include <regex>

static const int MAGIC_L = 4;

class BadFileFormatException : public std::exception {
public:
    std::string msg;

    explicit BadFileFormatException(std::string msg) : msg(std::move(msg)) {}

    [[nodiscard]] const char *what() const noexcept override {
        return msg.c_str();
    }
};

template<class T = int64_t>
inline T readInt(std::istream &file) {
    T number;
    file.read(reinterpret_cast<char *>(&number), sizeof(T));
    if (file.fail()) {
        throw BadFileFormatException("Too short file!");
    }
    return number;
}

static std::vector<char> readData(std::istream &file, int64_t length) {
    auto data = std::vector<char>((unsigned long) length);
    file.read(data.data(), length);
    if (file.fail()) {
        throw BadFileFormatException("Too short file!");
    }
    return data;
}

inline std::string convertString2Printable(const std::string &str) {
    return std::regex_replace(str, std::regex("[^ -~]+"), "");
}

static std::string readString(std::istream &file, int64_t length) {
    auto data = readData(file, length);
    auto str = std::string(data.begin(), data.end());
    str = convertString2Printable(str);
    return str;
}

inline void checkFrameLength(std::istream &file,
                             std::fpos<mbstate_t> &frame_start, int64_t length) {
    auto frame_end = file.tellg();
    if (frame_start + length != frame_end) {
        throw BadFileFormatException("Bad length in frame!");
    }
}

#endif //PARSER_UTIL_HPP
