#include <iostream>
#include <filesystem>
#include <fstream>
#include "CAFF.hpp"

namespace fs = std::filesystem;

CAFF::CAFF(std::string path) : path(std::move(path)) {
}

void CAFF::parseCaff() {
    std::ifstream caffFile(this->path, std::ios::binary);
    if (caffFile.is_open()) {
        while (caffFile.peek() != EOF) {
            readFrame(caffFile);
        }
    }
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
    file.read(reinterpret_cast<char *>(&number), 8);
    return number;
}

std::vector<char> CAFF::readData(std::ifstream &file, const int64_t length) {
    auto data = std::vector<char>(length);
    file.read(reinterpret_cast<char *>(data.data()), length);
    return data;
}

void CAFF::parseHeader(std::vector<char> data) {

}

void CAFF::parseCredits(std::vector<char> data) {

}

void CAFF::parseAnimation(std::vector<char> data) {

}
