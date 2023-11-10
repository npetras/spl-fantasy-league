FROM eclipse-temurin:8-jdk
VOLUME /tmp
ARG JAR_FILE
COPY ${JAR_FILE} app.jar

# install chrome
RUN apt update
RUN apt install wget
RUN wget https://dl.google.com/linux/direct/google-chrome-stable_current_amd64.deb
RUN dpkg -i google-chrome-stable_current_amd64.deb
RUN sudo apt-get install -f

# run the jar
ENTRYPOINT ["java","-jar","/app.jar"]
