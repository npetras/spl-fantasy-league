#FROM eclipse-temurin:17-jdk-focal
#COPY build/libs/statistics-0.0.1-SNAPSHOT.jar app.jar
#ENTRYPOINT ["java", "-jar", "app.jar"]
# Use the official Ubuntu base image
# Use the official Ubuntu base image
FROM eclipse-temurin:17-jdk-focal

# Set environment variables
ENV DEBIAN_FRONTEND=noninteractive


# Install firefox and packages required to install geckodriver
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


# TODO: add coy of the JAR
# TODO: run jar instead of bash below
RUN firefox --version && geckodriver --version
CMD ["bash"]