#include <iostream>
#include <fstream>
#include <sstream>
#include <filesystem>

#include "CAFF.hpp"

#include <Magick++.h>

CAFF::CAFF(const std::string &path, const std::string &outputPath) : file_path(path) {
    if (outputPath.empty()) {
        output_path = fs::current_path() / "out";
    } else {
        output_path = outputPath;
    }
}

void CAFF::parseCaff() {
    std::ifstream caffFile(file_path, std::ios::binary);
    //TODO check if first frame Header & only one header
    if (caffFile.is_open()) {
        while (caffFile.peek() != EOF) {
            readFrame(caffFile);
        }
    }
    //TODO validate
    caffFile.close();
}

void CAFF::readFrame(std::istream &file) {
    const auto frameId = readFrameID(file);
    const int64_t length = readInt(file);
    switch (frameId) {
        case HEADER:
            parseHeader(file, length);
            break;
        case CREDITS:
            parseCredits(file, length);
            break;
        case ANIMATION:
            parseAnimation(file, length);
            break;
    }
}

CAFF::FrameID CAFF::readFrameID(std::istream &file) {
    return FrameID(file.get());
}

void CAFF::parseHeader(std::istream &file, int64_t length) {
    // Check magic
    std::string magic = readString(file, MAGIC_L);
    if (magic != "CAFF") {
        std::cerr << "Wrong magic in header" << std::endl;
    }

    // Check header size correct
    auto header_size = readInt(file);
    if (header_size != length) {
        std::cerr << "Wrong header_size in header" << std::endl;
    }

    // Save numbers of animated CIFFs
    num_anim = readInt(file);
}

void CAFF::parseCredits(std::istream &file, int64_t length) {
    // Year
    date.year = readInt<int16_t>(file);
    // Month
    date.month = readInt<u_int8_t>(file);
    // Day
    date.day = readInt<u_int8_t>(file);
    // Hour
    date.hour = readInt<u_int8_t>(file);
    // Minute
    date.minute = readInt<u_int8_t>(file);

    // creator_len
    auto creator_len = readInt(file);

    // creator
    creator = readString(file, creator_len);

}

void CAFF::parseAnimation(std::istream &file, int64_t length) {
    // Duration
    auto duration = readInt(file);

    // Parse CIFF
    CIFF ciff;
    ciff.parseCiff(file, length);
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
