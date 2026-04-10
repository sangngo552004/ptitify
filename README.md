# PTITify - Music Streaming App 🎵

Đây là toàn bộ mã nguồn (Source Code) cho website nghe nhạc PTITify. 
Làm theo các bước sau để chạy toàn bộ hệ thống trên máy tính của bạn.

## Yêu cầu cài đặt trước:
- **Node.js** (Phiên bản >= 18)
- **Hệ điều hành:** Windows (hoặc Mac/Linux thì thay dấu `\` thành `/` cho đường dẫn)
- **Docker Desktop:** Bắt buộc (Phải đang mở phần mềm Docker chạy ngầm)
- **Lưu ý:** Không cần cài MySQL vì đã được cấu hình nối thẳng lên Database Cloud.

---

## 🚀 Bước 1: Build & Chạy Backend (API Server)
*Backend được viết bằng Java (JAX-RS) và chạy trên máy chủ WildFly (Docker).*

1. Mở Terminal (PowerShell / Command Prompt), di chuyển vào thư mục **backend**:
```bash
cd backend
```

2. Chạy lệnh Maven để đóng gói source code Java thành file WAR:
```bash
.\mvnw clean package -DskipTests
```
*(Đợi đến khi thấy chữ `BUILD SUCCESS`)*

3. Dựng máy chủ WildFly và nạp nhạc bằng Docker Compose:
```bash
docker-compose up -d --build
```
> **Lưu ý quan trọng**: Lệnh này sẽ tự động build image Docker (kèm driver MySQL) và gắn thư mục `muzik` (chứa các bài mp3) vào trong máy ảo để backend có thể phát nhạc. File `docker-compose.yml` đã thiết lập toàn bộ!

🔥 API của Backend lúc này đã chạy tại: **http://localhost:8080/music-app/api**

---

## 🎨 Bước 2: Chạy Frontend (Giao diện React)
*Giao diện người dùng viết ReactJS kết hợp với Vite.*

1. Mở một Tab Terminal mới, di chuyển vào thư mục **client**:
```bash
cd client
```

2. Cài đặt các thư viện cần thiết:
```bash
npm install
```

3. Khởi động Web:
```bash
npm run dev
```

Website sẽ hiển thị tại đường dẫn: **http://localhost:3000** (hoặc `5173` tuỳ vào terminal thông báo).

---

## 🛠 Nếu bạn muốn code và update (Dành cho Developer)
1. **Frontend:** Khi code file `.tsx`, Vite sẽ tự động cập nhật (Hot-reload) ngay lập tức.
2. **Backend:** Khi bạn thay đổi code Java, bạn phải build lại để có tác dụng. 
   Chỉ cần đi vào thư mục `backend`, chạy lại script:
   ```bash
   .\fast-deploy.ps1
   ```
   Script này sẽ tự động build code và chép file WAR mới tinh đè vào container `ptitify-backend` đang chạy. Server WildFly sẽ tự nạp lại mà không cần bạn phải tắt bật lệnh `docker-compose`.

Chúc bạn nghe nhạc vui vẻ! 🎧
