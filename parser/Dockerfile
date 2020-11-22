FROM ubuntu:latest as base

RUN apt-get -y update && \
    apt-get install -y \
    libpng16-16 libgomp1


FROM base as build

ENV DEBIAN_FRONTEND="noninteractive" TZ="Europe/Budapest"

RUN apt-get -y update && \
    apt-get install -y \
    wget \
    git \
    make cmake \
    gcc g++\
    pkg-config autoconf \
    libpng-dev \
    && rm -rf /var/lib/apt/lists/*

# install ImageMagick
RUN wget -q https://www.imagemagick.org/download/ImageMagick.tar.gz && \
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
    cmake --build . --target parser && \
    cmake --build . --target install

# Create final image
FROM base

COPY --from=build /usr/local/bin /usr/local/bin
COPY --from=build /usr/local/lib /usr/local/lib

RUN ldconfig /usr/local/lib

ENTRYPOINT ["parser"]
