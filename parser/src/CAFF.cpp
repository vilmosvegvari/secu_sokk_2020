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

    if (caffFile.is_open()) {
        file_size = fs::file_size(file_path);
        //Check first frame
        if (readFrame(caffFile) != HEADER) {
            throw BadFileFormatException("File must start with HEADER frame!");
        }

        // Read the rest of the frames
        while (caffFile.peek() != EOF) {
            readFrame(caffFile);
        }
        if (num_anim != ciff_list.size()) {
            throw BadFileFormatException("Not num_anim count CIFF in the CAFF file");
        }
    } else {
        throw BadFileFormatException("Can't open file!");
    }
    caffFile.close();
}

CAFF::FrameID CAFF::readFrame(std::istream &file) {
    const auto frameId = readFrameID(file);
    const int64_t length = readInt(file);
    if(isLengthTooLarge(length)){
        throw BadFileFormatException("Frame length is too big, frame id: " + std::to_string(frameId));
    }

    // save frame start
    auto frame_start = file.tellg();

    switch (frameId) {
        case HEADER:
            parseHeader(file, length);
            break;
        case CREDITS:
            parseCredits(file);
            break;
        case ANIMATION:
            parseAnimation(file);
            break;
        default:
            throw BadFileFormatException("Not valid frame id: " + std::to_string(frameId));
    }

    // check frame actual size equals length
    checkFrameLength(file, frame_start, length);

    return frameId;
}

CAFF::FrameID CAFF::readFrameID(std::istream &file) {
    return FrameID(file.get());
}

void CAFF::parseHeader(std::istream &file, int64_t length) {
    //Check if HEADER already exists
    if (isHeaderParsedAlready){
        throw BadFileFormatException("Duplicate HEADER frame");
    }

    // Check magic
    std::string magic = readString(file, MAGIC_L);
    if (magic != "CAFF") {
        throw BadFileFormatException("Wrong MAGIC in HEADER");
    }

    // Check header size correct
    auto header_size = readInt(file);
    if (header_size != length) {
        throw BadFileFormatException("Wrong header_size in HEADER");
    }

    // Save numbers of animated CIFFs
    num_anim = readInt(file);

    isHeaderParsedAlready = true;
}

void CAFF::parseCredits(std::istream &file) {
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
    if(isLengthTooLarge(creator_len)){
        throw BadFileFormatException("creator_length is too big");
    }

    // creator
    creator = readString(file, creator_len);
}

void CAFF::parseAnimation(std::istream &file) {
    // Duration
    auto duration = readInt(file);

    // Parse CIFF
    CIFF ciff(file_size);
    ciff.parseCiff(file);
    ciff_list.emplace_back(duration, ciff);
}

void CAFF::generateFiles() {
    verifyOutputPath();
    generateImage();
    generateJson();
}

void CAFF::generateJson() {
    json j = {{"creator", creator},
              {"date",    date.generateJson()},
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
        Magick::Image image(ciff.getWidth(), ciff.getHeight(), "RGB", MagickCore::CharPixel, ciff.getPixels().data());
        image.animationDelay(static_cast<const size_t>(std::get<0>(i) / 10));
        frames.emplace_back(image);
    }

    // generate gif
    Magick::writeImages(frames.begin(), frames.end(),
                        output_path / file_path.filename().replace_extension(".gif"));

    //generate png
    frames[0].write(output_path / file_path.filename().replace_extension(".png"));
}

void CAFF::verifyOutputPath() {
    if (!fs::exists(output_path)) {
        fs::create_directories(output_path);
    }
}

int64_t CAFF::getNumAnim() const {
    return num_anim;
}

const Date &CAFF::getDate() const {
    return date;
}

const std::string &CAFF::getCreator() const {
    return creator;
}

bool CAFF::isLengthTooLarge(int64_t length) const {
    return length > file_size;
}
