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

TEST_F(CAFFTest, TestParseCIFF){
    fs::path caff_file_path = "res/1.caff";
    std::ifstream caff_file(caff_file_path, std::ios::binary);

    caff_file.seekg(0x51);

    CIFF ciff;
    ciff.parseCiff(caff_file, 0x1E881C);

    std::vector<std::string> expected_tags{"landscape", "sunset", "mountains"};
    EXPECT_EQ(ciff.caption, "Beautiful scenery");
    EXPECT_EQ(ciff.pixels[0], 0x2D);
    EXPECT_EQ(ciff.height, 667);
    EXPECT_EQ(ciff.width, 1000);
    EXPECT_EQ(ciff.tags, expected_tags);
}

TEST_F(CAFFTest, TestGenerateJSONFromCIFF){
    fs::path caff_file_path = "res/1.caff";
    std::ifstream caff_file(caff_file_path, std::ios::binary);

    caff_file.seekg(0x51);

    CIFF ciff;
    ciff.parseCiff(caff_file, 0x1E881C);
    json out = ciff.generateJson();

    std::vector<std::string> expected_tags{"landscape", "sunset", "mountains"};
    json expected_json = json{
            {"caption", "Beautiful scenery"},
            {"size",    {{"width", 1000}, {"height", 667}}},
            {"tags",    expected_tags}
    };
    EXPECT_EQ(out, expected_json);
}

TEST_F(CAFFTest, TestParseCAFF){
    fs::path caff_file_path = "res/1.caff";
    fs::path output_path = "res/out";
    CAFF caff(caff_file_path, output_path);
    caff.parseCaff();

    EXPECT_EQ(caff.getCreator(), "Test Creator");
    EXPECT_EQ(caff.getNumAnim(), 2);
    EXPECT_EQ(caff.getDate().year, 2020);
    EXPECT_EQ(caff.getDate().month, 7);
    EXPECT_EQ(caff.getDate().day, 2);
    EXPECT_EQ(caff.getDate().hour, 14);
    EXPECT_EQ(caff.getDate().minute, 50);
}

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



