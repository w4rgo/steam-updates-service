FROM java

RUN apt-get update && apt-get install -y lib32gcc1 lib32stdc++6 wget
RUN mkdir steamcmd
RUN cd /steamcmd \
    && wget http://media.steampowered.com/installer/steamcmd_linux.tar.gz \
    && tar -zxvf steamcmd_linux.tar.gz \
    && rm -f steamcmd_linux.tar.gz
RUN /steamcmd/steamcmd.sh +login anonymous +quit
RUN echo 405100 > steam_appid.txt

VOLUME /service

COPY ./service /service
COPY docker-entrypoint.sh /

WORKDIR /

EXPOSE 4567

#CMD ["bash"]
ENTRYPOINT ["/docker-entrypoint.sh"]