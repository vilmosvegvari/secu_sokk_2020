#ifndef PARSER_CIFF_HPP
#define PARSER_CIFF_HPP


#include <vector>
#include <string>

class CIFF {
public:
    int width = -1;
    int height = -1;
    std::string caption;
    std::vector<std::string> tags;
    std::vector<unsigned char> pixels;

    void parseCiff(std::vector<char> &data);

private:
    std::vector<char> parseHeader(std::vector<char> &data);

    void parseContent(std::vector<char> &data);

    std::vector<char>::iterator parseCaption(std::vector<char> &data, int startIndex, long size);

    void parseTags(std::vector<char> &data, std::vector<char>::iterator startIndex, long size);
};

#endif //PARSER_CIFF_HPP
