FROM ubuntu:latest

ENV DEBIAN_FRONTEND="noninteractive" TZ="Europe/Budapest"

RUN apt-get -y update && \
    apt-get install -y \
    wget \
    git \
    make cmake \
    gcc g++\
    pkg-config autoconf \
    libpng16-16 libpng-dev \
    && rm -rf /var/lib/apt/lists/* && \
    # install ImageMagick
    wget -q https://www.imagemagick.org/download/ImageMagick.tar.gz && \
    tar xzf ImageMagick.tar.gz && \
    cd ImageMagick-7* && \
    ./configure --without-webp --with-utilities=no --disable-docs --disable-static&& \
    make && make install && \
    ldconfig /usr/local/lib && \
    cd .. && rm -rf ImageMagick-7* && rm ImageMagick.tar.gz

WORKDIR /app
COPY . .

RUN cd out && \
    cmake .. && \
    cmake --build . --target parser install -- -j 3

