FROM eclipse-temurin:17-jdk-focal

# set terminal to no-interactive, skipping any user prompts
ENV DEBIAN_FRONTEND=noninteractive

# install firefox and packages required to install geckodriver
RUN apt-get update && \
    apt-get install -y \
    firefox \
    wget \
    curl && \
    apt-get clean

RUN GECKODRIVER_VERSION=$(curl -s https://api.github.com/repos/mozilla/geckodriver/releases/latest | grep 'tag_name' | cut -d\" -f4) && \
    wget https://github.com/mozilla/geckodriver/releases/download/$GECKODRIVER_VERSION/geckodriver-$GECKODRIVER_VERSION-linux64.tar.gz && \
    tar -xzf geckodriver-$GECKODRIVER_VERSION-linux64.tar.gz -C /usr/local/bin && \
    rm geckodriver-$GECKODRIVER_VERSION-linux64.tar.gz

# copy JAR and run it
COPY build/libs/statistics-0.0.1-SNAPSHOT.jar app.jar
CMD ["java", "-jar", "app.jar"]