FROM nginx:latest
COPY ./target/site/jacoco /usr/share/nginx/html