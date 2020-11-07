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

    void parseCiff(std::ifstream &file, int64_t length);

    json generateJson();

private:
    int64_t parseHeader(std::ifstream &file);

    void parseContent(std::ifstream &file, int64_t size);

    void parseCaption(std::ifstream &file , std::streampos end);

    void parseTags(std::ifstream &file, std::streampos end);
};

#endif //PARSER_CIFF_HPP
