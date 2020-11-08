#ifndef PARSER_CIFF_HPP
#define PARSER_CIFF_HPP

#include <vector>
#include <string>
#include "util.hpp"

#include <nlohmann/json.hpp>

using json = nlohmann::json;

class CIFF {
public:
    void parseCiff(std::istream &file, int64_t length);

    json generateJson();

    long getWidth() const;

    long getHeight() const;

    const std::string &getCaption() const;

    const std::vector<std::string> &getTags() const;

    const std::vector<unsigned char> &getPixels() const;

private:
    long width = -1;
    long height = -1;
    std::string caption;
    std::vector<std::string> tags;
    std::vector<unsigned char> pixels;
    uintmax_t file_size = 0;
public:
    void setFileSize(uintmax_t fileSize);

private:

    int64_t parseHeader(std::istream &file);

    void parseContent(std::istream &file, int64_t size);

    void parseCaption(std::istream &file , std::streampos end);

    void parseTags(std::istream &file, std::streampos end);

    bool isLengthTooLarge(int64_t length) const;
};

#endif //PARSER_CIFF_HPP
