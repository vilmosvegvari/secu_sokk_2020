#include <iostream>
#include <filesystem>
#include <fstream>
#include "CAFF.hpp"

namespace fs = std::filesystem;

CAFF::CAFF(std::string path) : path(std::move(path)) {
}

void CAFF::parseCaff() {
    std::ifstream caffFile(this->path, std::ios::out | std::ios::binary);
    if (caffFile.is_open()) {

    }
    caffFile.close();
}

std::string CAFF::getFileName() {
    return fs::path(this->path).stem();
}

