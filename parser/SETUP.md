# Setup on Windows

This guide shows how to set up the project using **Cygwin** on Windows.

1. Download Cygwin from: https://www.cygwin.com/
2. Start the installer and select the default settings until **Select Packages**. For help, see: http://www.mcclean-cooper.com/valentino/cygwin_install/
3. Select the following packages whit the newest available package:
    * ImageMagick
    * ImageMagick-debuginfo
    * cmake
    * gcc-core
    * gcc-g++
    * gdb
    * libMagick-devel
    * libMagickC++7_4
    * libMagickCore7_7
    * make
    
    Then finish the installation.
4. Add Cygwin to Clion Toolchains with the following settings:<br />
![Toolchain settings](/parser/doc/img/clion_toolchain_settings.png)
5. Create new CMake profile with the currently added toolchain:<br />
![Cmake profile](/parser/doc/img/clion_cmake_profile.png)
6. Reload CMake project
7. Run with the new profile:<br />
![Run](/parser/doc/img/clion_run.png)
