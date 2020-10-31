#ifndef PARSER_CAFF_HPP
#define PARSER_CAFF_HPP


#include <string>
#include <vector>
#include <tuple>
#include "CIFF.hpp"
#include "Date.hpp"

class CAFF {
public:
    explicit CAFF(std::string path);

    void parseCaff();

    std::string getFileName();

private:
    const std::string path;
    int64_t num_anim;
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

    static int64_t readLength(std::ifstream &file);

    static std::vector<char> readData(std::ifstream &file, int64_t length);

    void parseHeader(std::vector<char> &data);

    void parseCredits(std::vector<char> &data);

    void parseAnimation(std::vector<char> &data);
};

#endif //PARSER_CAFF_HPP
