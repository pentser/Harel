# Dockerfile for running Harel Travel Insurance tests in Docker container
FROM maven:3.8.6-openjdk-11-slim

# Install Chrome and dependencies
RUN apt-get update && apt-get install -y \
    wget \
    gnupg \
    unzip \
    && wget -q -O - https://dl-ssl.google.com/linux/linux_signing_key.pub | apt-key add - \
    && echo "deb [arch=amd64] http://dl.google.com/linux/chrome/deb/ stable main" >> /etc/apt/sources.list.d/google.list \
    && apt-get update \
    && apt-get install -y google-chrome-stable \
    && rm -rf /var/lib/apt/lists/*

# Set working directory
WORKDIR /app

# Copy project files
COPY pom.xml .
COPY testng.xml .
COPY src ./src

# Download dependencies
RUN mvn dependency:resolve

# Run tests
CMD ["mvn", "clean", "test"]

