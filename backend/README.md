# 🎵 PTITIFY - Jakarta EE & WildFly

Dự án PTITIFY được xây dựng dựa trên kiến trúc Java/Jakarta EE, triển khai trên server WildFly và sử dụng cơ sở dữ liệu MySQL (Aiven Cloud). Toàn bộ dự án đã được đóng gói bằng Docker để giúp team dễ dàng triển khai (Plug and Play).

## 🛠 Công nghệ sử dụng
- **Backend:** Java, Jakarta EE (JAX-RS, JPA, Hibernate, CDI)
- **Application Server:** WildFly 39 (quay.io/wildfly/wildfly:latest)
- **Database:** MySQL 8.0 (Aiven Cloud Database)
- **DevOps:** Docker, Docker Compose, Maven

---

## 📋 Yêu cầu hệ thống (Prerequisites)
Trước khi chạy dự án, máy tính của bạn cần được cài đặt sẵn:
1. [Git](https://git-scm.com/)
2. [Docker](https://www.docker.com/products/docker-desktop/) và Docker Compose
3. [Java JDK 17](https://adoptium.net/) (hoặc bản mới hơn)

---

## 🚀 Hướng dẫn Cài đặt & Chạy dự án (Getting Started)

Chỉ cần làm theo 4 bước dưới đây để khởi chạy dự án trên máy của bạn:

### Bước 1: Clone mã nguồn
Mở Terminal / Command Prompt và chạy lệnh:
```bash
git clone <đường-dẫn-repo-của-bạn>
cd music-app
```
### Bước 2: Thiết lập Biến môi trường (Quan trọng ⚠️)
Dự án sử dụng cơ sở dữ liệu trên Cloud nên cần có thông tin kết nối.

Tạo một file mới tên là .env tại thư mục gốc của dự án (cấp ngang với file docker-compose.yml).

Copy toàn bộ nội dung từ file .env.example sang file .env.

Điền mật khẩu database vào biến DB_PASS (Hãy hỏi Leader hoặc người thiết lập dự án để lấy mật khẩu này).

Ví dụ nội dung file .env:
```bash
Đoạn mã
DB_URL=jdbc:mysql://[music-app-music-app.i.aivencloud.com:18811/defaultdb?sslMode=REQUIRED](https://music-app-music-app.i.aivencloud.com:18811/defaultdb?sslMode=REQUIRED)
DB_USER=avnadmin
DB_PASS="mật_khẩu_được_cấp"
```
### Bước 3: Build mã nguồn Java ra file WAR
Sử dụng Maven Wrapper có sẵn trong dự án để đóng gói code:

Trên Mac/Linux:
```bash
Bash
./mvnw clean package
```
Trên Windows (CMD/PowerShell):
```bash
DOS
mvnw.cmd clean package
```
(Lệnh này sẽ tạo ra file music-app-1.0-SNAPSHOT.war nằm trong thư mục target)

### Bước 4: Khởi chạy Server bằng Docker
Chạy lệnh sau để Docker tự động cấu hình WildFly, kết nối Database và Deploy ứng dụng:

```bash
Bash
docker-compose up --build -d
```
⏳ Vui lòng đợi khoảng 10-15 giây để WildFly khởi động hoàn tất.

## 🌐 Kiểm tra ứng dụng
Sau khi server chạy thành công, bạn có thể kiểm tra các API tại:

API Test cơ bản: http://localhost:8080/music-app-1.0-SNAPSHOT/api/hello-world

(Cần điều chỉnh đường dẫn /api tùy theo cấu hình trong file RestApplication.java của bạn).

Trang quản trị WildFly Admin (Nếu cần): http://localhost:9990

## 🛑 Dừng ứng dụng
Để tắt server và dọn dẹp container, chạy lệnh:

```bash
docker-compose down
```
## 💡 Lưu ý dành cho Team (Team Notes)
Database dùng chung: Chúng ta đang dùng chung một Database MySQL trên Aiven Cloud. Bất kỳ thay đổi nào về cấu trúc bảng (Table) hoặc dữ liệu (Insert/Update/Delete) đều sẽ ảnh hưởng đến tất cả các thành viên khác.

Cập nhật code: Mỗi khi bạn pull code mới từ Git về hoặc sửa code Java, bắt buộc phải chạy lại Bước 3 (mvnw clean package) và Bước 4 (docker-compose up --build -d) để server nhận code mới.


