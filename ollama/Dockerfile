FROM ollama/ollama:latest

# Create a directory for scripts
WORKDIR /app

# Add a startup script
RUN echo '#!/bin/sh\n\
ollama serve &\n\
sleep 5\n\
ollama pull llama3.2:3b\n\
tail -f /dev/null' > /app/start.sh && \
    chmod +x /app/start.sh

# Expose the Ollama API port
EXPOSE 11434

# Set the entry point to the startup script
ENTRYPOINT ["/app/start.sh"]