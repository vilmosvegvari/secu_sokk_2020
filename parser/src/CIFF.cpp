#include <algorithm>
#include <sstream>
#include "CIFF.hpp"
#include "util.hpp"

void CIFF::parseCiff(std::vector<char> &data) {
    auto content = parseHeader(data);
    parseContent(content);
}

std::vector<char> CIFF::parseHeader(std::vector<char> &data) {
    // Check magic
    std::string magic(data.begin(), data.begin() + MAGIC_L);
    if (magic != "CIFF") {
        std::cerr << "Wrong magic in header" << std::endl;
    }

    // Parse header_size
    const auto header_size = convertVectorToInt(data, MAGIC_L);

    // Parse content_size
    const auto content_size = convertVectorToInt(data, MAGIC_L + EIGHT_BYTE_L);

    // Parse width
    width = convertVectorToInt(data, MAGIC_L + 2 * EIGHT_BYTE_L);

    // Parse height
    height = convertVectorToInt(data, MAGIC_L + 3 * EIGHT_BYTE_L);

    // Parse caption
    const auto caption_end = parseCaption(data, MAGIC_L + 4 * EIGHT_BYTE_L,
                                          header_size);

    //Parse tags
    parseTags(data, caption_end, header_size);

    return std::vector<char>(data.begin() + header_size, data.end());
}

void CIFF::parseContent(std::vector<char> &data) {
    pixels.insert(pixels.end(), data.begin(), data.end());
}

std::vector<char>::iterator CIFF::parseCaption(std::vector<char> &data, int startIndex,
                                               const long size) {
    auto n = std::find(data.begin() + startIndex, data.begin() + size, '\n');
    caption = std::string(data.begin() + startIndex, n);
    return n + 1; // plus 1 because of the /n
}

void CIFF::parseTags(std::vector<char> &data, std::vector<char>::iterator startIndex,
                     const long size) {
    std::stringstream tag_source(std::string(startIndex, data.begin() + size));
    std::string buffer;

    while (std::getline(tag_source, buffer, '\000')) {
        tags.emplace_back(buffer);
    }
}

json CIFF::generateJson() {
    json j = {
            {"caption", caption},
            {"size",    {{"width", width}, {"height", height}}},
            {"tags", tags}
    };
    return j;
}
