#ifndef PARSER_CAFF_HPP
#define PARSER_CAFF_HPP

#include <string>
#include <unistd.h>
#include <vector>
#include <tuple>
#include "CIFF.hpp"
#include "Date.hpp"
#include <filesystem>

#include <nlohmann/json.hpp>

using json = nlohmann::json;

namespace fs = std::filesystem;

class CAFF {
public:
    CAFF(const std::string &path, const std::string &outputPath);

    void parseCaff();

    void generateFiles();

private:
    const fs::path file_path;
    fs::path output_path;

    int64_t num_anim = -1;
    std::vector<std::tuple<int64_t, CIFF>> ciff_list;
    Date date;
    std::string creator;

    enum FrameID : unsigned char {
        HEADER = 1,
        CREDITS = 2,
        ANIMATION = 3
    };

    void readFrame(std::ifstream &file);

    static FrameID readFrameID(std::ifstream &file);

    void parseHeader(std::ifstream &file, int64_t length);

    void parseCredits(std::ifstream &file, int64_t length);

    void parseAnimation(std::ifstream &file, int64_t length);

    void generateJson();

    void generateImage();

    void verifyOutputPath();

};

#endif //PARSER_CAFF_HPP
