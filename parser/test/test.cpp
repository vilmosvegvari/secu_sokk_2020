#include <iostream>
#include <filesystem>
#include <fstream>
#include <iterator>
#include <string>
#include <algorithm>

#include <gtest/gtest.h>
#include "CAFF.hpp"

namespace fs = std::filesystem;

class CAFFTest : public ::testing::Test {
protected:
    void SetUp() override {
        // remove all generated files
        auto path = fs::path("res/out");
        if (fs::exists(path) && !fs::is_empty(path)) {
            fs::remove_all(path);
        }
    }

    void TearDown() override {
        Test::TearDown();
    }

public:

    static bool compareFiles(const std::string &p1, const std::string &p2) {
        std::ifstream f1(p1, std::ifstream::binary | std::ifstream::ate);
        std::ifstream f2(p2, std::ifstream::binary | std::ifstream::ate);

        if (f1.fail() || f2.fail()) {
            return false; //file problem
        }

        if (fs::file_size(p1) != fs::file_size(p2)) {
            return false; //size mismatch
        }

        //seek back to beginning and use std::equal to compare contents
        f1.seekg(0, std::ifstream::beg);
        f2.seekg(0, std::ifstream::beg);
        return std::equal(std::istreambuf_iterator<char>(f1.rdbuf()),
                          std::istreambuf_iterator<char>(),
                          std::istreambuf_iterator<char>(f2.rdbuf()));
    }
};


TEST_F (CAFFTest /*test suite name*/, GeneratesFiles /*test name*/) {
    fs::path caff_file_path = "res/1.caff";
    fs::path output_path = "res/out";
    CAFF caff(caff_file_path, output_path);
    caff.parseCaff();
    caff.generateFiles();

    ASSERT_FALSE(fs::is_empty(output_path));
    ASSERT_TRUE(CAFFTest::compareFiles("res/expected/1.gif", "res/out/1.gif"));
    ASSERT_TRUE(CAFFTest::compareFiles("res/expected/1.json", "res/out/1.json"));

}



