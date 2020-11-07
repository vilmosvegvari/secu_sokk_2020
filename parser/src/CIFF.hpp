#ifndef PARSER_CIFF_HPP
#define PARSER_CIFF_HPP

#include <vector>
#include <string>
#include "util.hpp"

#include <nlohmann/json.hpp>

using json = nlohmann::json;

class CIFF {
public:
    long width = -1;
    long height = -1;
    std::string caption;
    std::vector<std::string> tags;
    std::vector<unsigned char> pixels;

    void parseCiff(std::istream &file, int64_t length);

    json generateJson();

private:
    int64_t parseHeader(std::istream &file);

    void parseContent(std::istream &file, int64_t size);

    void parseCaption(std::istream &file , std::streampos end);

    void parseTags(std::istream &file, std::streampos end);
};

#endif //PARSER_CIFF_HPP
