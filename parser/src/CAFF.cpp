#include <iostream>
#include <filesystem>
#include <fstream>

#include "CAFF.hpp"
#include "util.hpp"

#include <Magick++.h>


CAFF::CAFF(const std::string &path, const std::string &outputPath) :
        file_path(path) {
    if (outputPath.empty()) {
        output_path = fs::current_path() / "out";
    } else {
        output_path = outputPath;
    }
}


void CAFF::parseCaff() {
    std::ifstream caffFile(this->file_path, std::ios::binary);
    //TODO check if first frame Header & only one header
    if (caffFile.is_open()) {
        while (caffFile.peek() != EOF) {
            readFrame(caffFile);
        }
    }
    //TODO validate
    caffFile.close();
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
    auto data = std::vector<char>((unsigned long) length);
    file.read(data.data(), length);
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
    // Duration
    auto duration = convertVectorToInt(data, 0);

    // Parse CIFF
    CIFF ciff;
    auto ciff_data = std::vector(data.begin() + EIGHT_BYTE_L, data.end());
    ciff.parseCiff(ciff_data);
    ciff_list.emplace_back(duration, ciff);
}

void CAFF::generateFiles() {
    verifyOutputPath();
    generateImage();
    generateJson();
}

void CAFF::generateJson() {
    json j = {{"creator", creator},
              {"date",    {{"year", date.year},
                                  {"month", date.month},
                                  {"day", date.day},
                                  {"hour", date.hour},
                                  {"minute", date.minute}}},
    };
    j["ciff_s"] = json::array();
    for (auto &ciff : ciff_list) {
        auto ciff_j = std::get<1>(ciff).generateJson();
        ciff_j["duration"] = std::get<0>(ciff);
        j["ciff_s"].push_back(ciff_j);
    }

    std::ofstream json_file(output_path / file_path.filename().replace_extension(".json"));
    if (json_file.is_open()) {
        json_file << j.dump(4);
    }
    json_file.close();
}

void CAFF::generateImage() {
    std::vector<Magick::Image> frames;
    for (auto &i : ciff_list) {
        auto ciff = std::get<1>(i);
        Magick::Image image(ciff.width, ciff.height, "RGB", MagickCore::CharPixel, ciff.pixels.data());
        image.animationDelay(std::get<0>(i) / 10);
        frames.emplace_back(image);
    }
    Magick::writeImages(frames.begin(), frames.end(),
                        output_path / file_path.filename().replace_extension(".gif"));
}

void CAFF::verifyOutputPath() {
    if (!fs::exists(output_path)) {
        fs::create_directories(output_path);
    }
}
