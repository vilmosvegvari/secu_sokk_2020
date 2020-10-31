#include <iostream>
#include <filesystem>
#include <fstream>
#include "CAFF.hpp"
#include "util.hpp"

namespace fs = std::filesystem;

CAFF::CAFF(std::string path) : path(std::move(path)) {
}

void CAFF::parseCaff() {
    std::ifstream caffFile(this->path, std::ios::binary);
    //TODO check if first frame Header & only one header
    if (caffFile.is_open()) {
        while (caffFile.peek() != EOF) {
            readFrame(caffFile);
        }
    }
    //TODO validate
    caffFile.close();
}

std::string CAFF::getFileName() {
    return fs::path(this->path).stem();
}

void CAFF::readFrame(std::ifstream &file) {
    const auto frameId = readFrameID(file);
    const int64_t length = readLength(file);
    auto data = readData(file, length);
    switch (frameId) {
        case HEADER:
            parseHeader(data);
            break;
        case CREDITS:
            parseCredits(data);
            break;
        case ANIMATION:
            parseAnimation(data);
            break;
    }
}

CAFF::FrameID CAFF::readFrameID(std::ifstream &file) {
    return FrameID(file.get());
}

int64_t CAFF::readLength(std::ifstream &file) {
    int64_t number;
    file.read(reinterpret_cast<char *>(&number), EIGHT_BYTE_L);
    return number;
}

std::vector<char> CAFF::readData(std::ifstream &file, const int64_t length) {
    auto data = std::vector<char>(length);
    file.read(reinterpret_cast<char *>(data.data()), length);
    return data;
}

void CAFF::parseHeader(std::vector<char> &data) {
    // Check magic
    std::string magic(data.begin(), data.begin() + MAGIC_L);
    if (magic != "CAFF") {
        std::cerr << "Wrong magic in header" << std::endl;
    }

    // Check header size correct
    auto header_size = convertVectorToInt(data, MAGIC_L);
    if (header_size != data.size()) {
        std::cerr << "Wrong header_size in header" << std::endl;
    }

    // Save numbers of animated CIFFs
    num_anim = convertVectorToInt(data, MAGIC_L + EIGHT_BYTE_L);
}

void CAFF::parseCredits(std::vector<char> &data) {
    // Year
    date.year = convertVectorToInt<int16_t>(data, 0);
    // Month
    date.month = convertVectorToInt<u_int8_t>(data, TWO_BYTE_L);
    // Day
    date.day = convertVectorToInt<u_int8_t>(data, TWO_BYTE_L + BYTE_L);
    // Hour
    date.hour = convertVectorToInt<u_int8_t>(data, TWO_BYTE_L + 2 * BYTE_L);
    // Minute
    date.minute = convertVectorToInt<u_int8_t>(data, TWO_BYTE_L + 3 * BYTE_L);

    // creator_len
    auto creator_len = convertVectorToInt(data, TWO_BYTE_L + 4 * BYTE_L);

    // creator
    auto creator_start = data.begin() + TWO_BYTE_L + 4 * BYTE_L + EIGHT_BYTE_L;
    creator = std::string(creator_start, creator_start + creator_len);

}

void CAFF::parseAnimation(std::vector<char> &data) {
    auto duration = convertVectorToInt(data, 0);
    CIFF ciff;
    ciff.parseCiff(data);
    ciff_list.emplace_back(duration, ciff);
}
