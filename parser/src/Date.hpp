#ifndef PARSER_DATE_HPP
#define PARSER_DATE_HPP

#include <nlohmann/json.hpp>

class Date {
public:
    int year = -1;
    int month = -1;
    int day = -1;
    int hour = -1;
    int minute = -1;

    nlohmann::json generateJson(){
        return json{{"year",   year},
                    {"month",  month},
                    {"day",    day},
                    {"hour",   hour},
                    {"minute", minute}};
    }
};


#endif //PARSER_DATE_HPP
