version: "3.9"

services:
  api:
    build:
      context: ./testes-energia/testes-energia
      dockerfile: Dockerfile
    container_name: testes-energia
    environment:
      - ASPNETCORE_ENVIRONMENT=Development
      - ASPNETCORE_URLS=http://0.0.0.0:8080
      - ConnectionStrings__DatabaseConnection=User Id=RM554565;Password=060202;Data Source=oracle.fiap.com.br:1521/ORCL;Pooling=True;Connection Timeout=60;
      - JwtSettings__Secret=uma-chave-bem-grande-com-32-caracteres-minimos-xxxxxxxx
      - JwtSettings__Issuer=EnergiaAPI
      - JwtSettings__Audience=testesenergiaUser
      - JwtSettings__ExpireMinutes=60
    ports:
      - "8080:8080"
    healthcheck:
      test: ["CMD", "curl", "-fsS", "http://localhost:8080/health || exit 1"]
      interval: 15s
      timeout: 5s
      retries: 10
    restart: unless-stopped
    networks:
      - energia-net

networks:
  energia-net:
    driver: bridge
