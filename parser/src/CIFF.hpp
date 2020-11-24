#ifndef PARSER_CIFF_HPP
#define PARSER_CIFF_HPP

#include <vector>
#include <string>
#include "util.hpp"

#include <nlohmann/json.hpp>

using json = nlohmann::json;

class CIFF {
public:
    explicit CIFF(int64_t file_size);

    void parseCiff(std::istream &file);

    json generateJson();

    [[nodiscard]] int64_t getWidth() const;

    [[nodiscard]] int64_t getHeight() const;

    [[nodiscard]] const std::string &getCaption() const;

    [[nodiscard]] const std::vector<std::string> &getTags() const;

    [[nodiscard]] const std::vector<unsigned char> &getPixels() const;

private:
    int64_t width = -1;
    int64_t height = -1;
    std::string caption;
    std::vector<std::string> tags;
    std::vector<unsigned char> pixels;
    int64_t file_size = 0;

    int64_t parseHeader(std::istream &file);

    void parseContent(std::istream &file, int64_t size);

    void parseCaption(std::istream &file, std::streampos end);

    void parseTags(std::istream &file, std::streampos end);

    [[nodiscard]] bool isLengthTooLarge(int64_t length) const;
};

#endif //PARSER_CIFF_HPP
