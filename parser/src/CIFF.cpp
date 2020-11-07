#include <algorithm>
#include <sstream>
#include "CIFF.hpp"


void CIFF::parseCiff(std::istream &file, int64_t length) {
    auto size = parseHeader(file);
    parseContent(file, size);
}

int64_t CIFF::parseHeader(std::istream &file) {
    auto header_start = file.tellg();
    // Check magic
    std::string magic = readString(file, MAGIC_L);
    if (magic != "CIFF") {
        std::cerr << "Wrong magic in header" << std::endl;
    }

    // Parse header_size
    const auto header_size = readInt(file);
    auto header_end = header_start + header_size;

    // Parse content_size
    const auto content_size = readInt(file);

    // Parse width
    width = readInt(file);

    // Parse height
    height = readInt(file);

    // Parse caption
    parseCaption(file, header_end);

    //Parse tags
    parseTags(file, header_end);

    return content_size;
}

void CIFF::parseContent(std::istream &file, int64_t size) {
    auto data = readData(file, size);
    pixels.insert(pixels.end(), data.begin(), data.end());
}

void CIFF::parseCaption(std::istream &file, std::streampos end) {
    std::getline(file, caption, '\n');
    //TODO error if bigger than end
}

void CIFF::parseTags(std::istream &file, std::streampos end) {
    std::string buffer;

    while (file.tellg() < end) {
        std::getline(file, buffer, '\000');
        tags.emplace_back(buffer);
    }
}

json CIFF::generateJson() {
    json j = {
            {"caption", caption},
            {"size",    {{"width", width}, {"height", height}}},
            {"tags",    tags}
    };
    return j;
}
