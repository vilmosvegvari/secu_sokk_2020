#include <iostream>

#include "CAFF.hpp"

int main(int argc, char *argv[]) {
    if (argc > 1) {
        CAFF caff(argv[1]);
        caff.parseCaff();
        caff.generateFiles();
    } else {
        std::cerr << "No filepath in arguments!" << std::endl;
        return -1;
    }
    return 0;
}
