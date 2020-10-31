#ifndef PARSER_CAFF_HPP
#define PARSER_CAFF_HPP


#include <string>

class CAFF {
    std::string path;

public:
    explicit CAFF(std::string path);

    void parseCaff();

    std::string getFileName();
};


#endif //PARSER_CAFF_HPP
