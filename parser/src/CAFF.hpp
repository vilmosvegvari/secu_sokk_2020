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

    [[nodiscard]] int64_t getNumAnim() const;

    [[nodiscard]] const Date &getDate() const;

    [[nodiscard]] const std::string &getCreator() const;

private:
    const fs::path file_path;
    fs::path output_path;
    uintmax_t file_size = 0;

    int64_t num_anim = -1;
    std::vector<std::tuple<int64_t, CIFF>> ciff_list;
    Date date;
    std::string creator;

    bool isHeaderParsedAlready = false;
    bool hasCredits = false;
    bool hasAnimations = false;

    enum FrameID : unsigned char {
        HEADER = 1,
        CREDITS = 2,
        ANIMATION = 3
    };

    FrameID readFrame(std::istream &file);

    static FrameID readFrameID(std::istream &file);

    void parseHeader(std::istream &file, int64_t length);

    void parseCredits(std::istream &file);

    void parseAnimation(std::istream &file);

    void generateJson();

    void generateImage();

    void verifyOutputPath();

    [[nodiscard]] bool isLengthTooLarge(int64_t length) const;
};

#endif //PARSER_CAFF_HPP
