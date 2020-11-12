#include <algorithm>
#include <sstream>
#include "CIFF.hpp"

CIFF::CIFF(int64_t file_size) : file_size(file_size) {}

void CIFF::parseCiff(std::istream &file, int64_t length) {
    auto size = parseHeader(file);
    parseContent(file, size);

    //TODO length check
}

int64_t CIFF::parseHeader(std::istream &file) {
    auto header_start = file.tellg();
    // Check magic
    std::string magic = readString(file, MAGIC_L);
    if (magic != "CIFF") {
        throw BadFileFormatException("Wrong magic in CIFF HEADER");
    }

    // Parse header_size
    const auto header_size = readInt(file);
    if (isLengthTooLarge(header_size)) {
        throw BadFileFormatException("CIFF header_size is too big");
    }

    auto header_end = header_start + header_size;

    // Parse content_size
    const auto content_size = readInt(file);
    if (isLengthTooLarge(content_size)) {
        throw BadFileFormatException("CIFF content_size is too big");
    }

    // Parse width
    width = readInt(file);

    // Parse height
    height = readInt(file);

    if ((width * height * 3) != content_size) {
        throw BadFileFormatException("width*height*3 not equal to content_size");
    }

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
    std::string buffer;

    std::getline(file, buffer, '\n');

    caption = convertString2Printable(buffer);

    if (file.tellg() > end || file.eof()) {
        throw BadFileFormatException("Missing '\\n' or too long Caption!");
    }
}

void CIFF::parseTags(std::istream &file, std::streampos end) {
    std::string buffer;

    while (file.tellg() < end && !file.eof()) {
        std::getline(file, buffer, '\000');

        buffer = convertString2Printable(buffer);

        if (file.tellg() > end || file.eof()) {
            throw BadFileFormatException("Missing '\\0' or too long Tag section!");
        }

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

const size_t CIFF::getWidth() const {
    return width;
}

const size_t CIFF::getHeight() const {
    return height;
}

const std::string &CIFF::getCaption() const {
    return caption;
}

const std::vector<std::string> &CIFF::getTags() const {
    return tags;
}

const std::vector<unsigned char> &CIFF::getPixels() const {
    return pixels;
}

bool CIFF::isLengthTooLarge(int64_t length) const {
    return length > file_size;
}
