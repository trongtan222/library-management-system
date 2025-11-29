# Stage 1: Build Angular
FROM node:22-alpine AS build
WORKDIR /app
COPY package*.json ./

# --- THÊM ĐOẠN NÀY ---
# 1. Tăng thời gian chờ mạng (Timeout) để không bị lỗi khi mạng chậm
RUN npm config set fetch-retry-maxtimeout 600000
RUN npm config set fetch-retry-mintimeout 10000

# 2. Dùng 'npm ci' thay vì 'npm install' (Nhanh hơn & ổn định hơn)
# Thêm --verbose để hiện log chi tiết (nhìn thấy nó đang chạy chứ không bị đứng hình)
RUN npm ci --legacy-peer-deps --verbose
# ---------------------

COPY . .
RUN npm run build --configuration=production

# Stage 2: Serve bằng Nginx (GIỮ NGUYÊN)
FROM nginx:stable-alpine
COPY --from=build /app/dist/lms-frontend /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80