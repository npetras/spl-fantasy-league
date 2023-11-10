### Step 1: Build the Angular application ###
FROM node:14 AS builder
WORKDIR /app
# copy dependecies file
COPY package*.json ./
# install necessary modules
RUN npm install -g @angular/cli
RUN npm install
# Copy the rest of the application code
COPY . .
# Build the Angular app for production
RUN ng build

### Step 2: Create the Nginx container ###
FROM nginx:alpine

# Remove the default Nginx configuration
# RUN rm -rf /etc/nginx/conf.d

# Copy the Angular app build from the builder stage to the Nginx container
COPY --from=builder /app/dist/frontend /usr/share/nginx/html

# Expose port 80 (the default port for Nginx)
EXPOSE 80

# Start Nginx when the container runs
CMD ["nginx", "-g", "daemon off;"]
