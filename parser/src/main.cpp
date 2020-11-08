#include <iostream>
#include <string>
#include <unistd.h>

#include "CAFF.hpp"

int main(int argc, char *argv[]) {
    std::string caff_file_path, output_path;

    if (argc <= 1) {
        std::cerr << "No parameter provided!";
        return -1;
    }

    int c;
    while ((c = getopt(argc, argv, "f:o:")) != -1) {
        switch (c) {
            case 'f':
                caff_file_path = optarg;
                continue;
            case 'o':
                output_path = optarg;
                continue;
            default:
                std::cerr << "Unknown parameter" << c;
                return -1;
        }
    }

    try {
        CAFF caff(caff_file_path, output_path);
        caff.parseCaff();
        caff.generateFiles();
    } catch (BadFileFormatException &e) {
        std::cout << "Bad file format!: " << e.what();
        return -1;
    } catch (...) {
        //TODO
    }
    return 0;
}
