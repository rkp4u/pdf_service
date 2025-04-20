FROM eclipse-temurin:17-jdk-focal

# Install Tesseract OCR and required dependencies
RUN apt-get update && \
    apt-get install -y --no-install-recommends \
    tesseract-ocr \
    libtesseract-dev \
    tesseract-ocr-eng \
    libleptonica-dev \
    libpng-dev \
    libjpeg-dev \
    libtiff-dev \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*

# Verify Tesseract installation
RUN tesseract --version

# Create directory for application
WORKDIR /app

# Copy the JAR file
COPY target/*.jar app.jar

# Expose the port the app runs on
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
# docker build -t myapp:latest .
# docker run -d -p 8080:8080 myapp:latest