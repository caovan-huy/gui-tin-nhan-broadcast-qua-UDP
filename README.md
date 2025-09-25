<h2 align="center">
    <a href="https://dainam.edu.vn/vi/khoa-cong-nghe-thong-tin">
    🎓 Faculty of Information Technology (DaiNam University)
    </a>
</h2>
<h2 align="center">
   Gửi tin nhắn Broadcast qua UDP
</h2>
<div align="center">
    <p align="center">
        <img alt="AIoTLab Logo" width="170" src="https://github.com/user-attachments/assets/711a2cd8-7eb4-4dae-9d90-12c0a0a208a2" />
        <img alt="AIoTLab Logo" width="180" src="https://github.com/user-attachments/assets/dc2ef2b8-9a70-4cfa-9b4b-f6c2f25f1660" />
        <img alt="DaiNam University Logo" width="200" src="https://github.com/user-attachments/assets/77fe0fd1-2e55-4032-be3c-b1a705a1b574" />
    </p>

[![AIoTLab](https://img.shields.io/badge/AIoTLab-green?style=for-the-badge)](https://www.facebook.com/DNUAIoTLab)
[![Faculty of Information Technology](https://img.shields.io/badge/Faculty%20of%20Information%20Technology-blue?style=for-the-badge)](https://dainam.edu.vn/vi/khoa-cong-nghe-thong-tin)
[![DaiNam University](https://img.shields.io/badge/DaiNam%20University-orange?style=for-the-badge)](https://dainam.edu.vn)

</div>

## 📖 1. Giới thiệu
Hệ thống gửi tin nhắn broadcast qua UDP là một ứng dụng mô phỏng việc truyền thông tin trong mạng cục bộ (LAN) bằng cách sử dụng giao thức UDP kết hợp với kỹ thuật broadcast. Hệ thống được thiết kế với hai thành phần chính: **Server** và **Client**.

- **Server** có nhiệm vụ lắng nghe các gói tin được gửi đến, hiển thị nội dung tin nhắn và phản hồi cho các máy trong cùng mạng.

- **Client** cho phép người dùng nhập nội dung tin nhắn và gửi đi. Tin nhắn này sẽ được broadcast đến tất cả các thiết bị trong mạng LAN đang chạy ứng dụng.

Nguyên lý hoạt động của hệ thống dựa trên đặc điểm của UDP – một giao thức không kết nối, tốc độ xử lý nhanh và đơn giản. Khi người dùng gửi một gói tin dưới dạng broadcast, gói tin đó sẽ được gửi đến địa chỉ quảng bá (broadcast address) và tất cả các máy tính trong mạng sẽ có thể nhận được.

Hệ thống này có ưu điểm là:

- Đơn giản, dễ triển khai, không đòi hỏi kết nối phức tạp.

- Truyền tải nhanh, phù hợp với các ứng dụng cần thông báo tức thời.

- Minh họa rõ ràng kiến thức về socket lập trình mạng và cơ chế hoạt động của UDP.

Ứng dụng có thể được áp dụng trong thực tế để xây dựng các chương trình chat nội bộ, gửi thông báo trong mạng LAN, hoặc các công cụ quản trị hệ thống.
## 🔧 2. Ngôn ngữ lập trình sử dụng: [![Java](https://img.shields.io/badge/Java-007396?style=for-the-badge&logo=java&logoColor=white)](https://www.java.com/)
- **Language setting ""Java (JDK)"":** Ứng dụng được phát triển bằng ngôn ngữ Java, do đó cần cài đặt bộ công cụ Java Development Kit (JDK). JDK cung cấp trình biên dịch ``` javac ``` , thư viện chuẩn, và môi trường chạy chương trình ``` JRE ``` giúp quá trình lập trình và thực thi chương trình Java diễn ra thuận lợi.
- **Giao diện "Java Swing":** Phần giao diện người dùng được thiết kế bằng thư viện Java Swing, một bộ công cụ mạnh mẽ để xây dựng giao diện đồ họa (GUI). Swing cho phép tạo ra các thành phần trực quan như cửa sổ, nút bấm, hộp thoại nhập liệu, danh sách… giúp người dùng dễ dàng tương tác với ứng dụng.  
- **Giao thức mạng "UDP (User Datagram Protocol)":** Ứng dụng sử dụng giao thức UDP để truyền tải dữ liệu qua mạng. Đây là giao thức phi kết nối, có ưu điểm là tốc độ nhanh, độ trễ thấp và thích hợp cho các ứng dụng cần gửi/nhận dữ liệu liên tục như chat, truyền tín hiệu hoặc phát broadcast. Trong đề tài này, UDP được dùng để gửi và nhận tin nhắn giữa client và server.  
- **IDE "Eclipse":** Quá trình viết code và phát triển ứng dụng được thực hiện trên **Eclipse IDE**, một môi trường phát triển tích hợp phổ biến, hỗ trợ đầy đủ tính năng như quản lý project, tự động gợi ý cú pháp, debug, và tích hợp với nhiều thư viện ngoài. Eclipse giúp lập trình viên thuận tiện hơn trong việc triển khai, kiểm thử và bảo trì ứng dụng.  
## 🚀 3. Hình ảnh các chức năng
<p align="center">
  <img width="604" height="487" img src="https://github.com/caovan-huy/gui-tin-nhan-broadcast-qua-UDP/blob/main/docs/anh%201.png" alt="Ảnh 1" width="800"/> 
</p>
<p align = "center">Ảnh 1: Giao diện Client </p>

 <p align = "center"><img width="604" height="487" alt="image" src="https://github.com/caovan-huy/gui-tin-nhan-broadcast-qua-UDP/blob/main/docs/anh%202.png" /></p>
<p align = "center"> Ảnh 2: Giao diện Server </p>
<p align = "center"><img width="604" height="487" alt="image" src="https://github.com/caovan-huy/gui-tin-nhan-broadcast-qua-UDP/blob/main/docs/anh%203.png" /></p>
<p align = "center"> Ảnh 3: Thông báo Client đã kết nối với Server </p>
<p align = "center"><img width="604" height="487" alt="image" src="https://github.com/caovan-huy/gui-tin-nhan-broadcast-qua-UDP/blob/main/docs/anh%205.png" /></p>
<p align = "center"> Ảnh 4: Client nhắn tin với Server </p>
<p align = "center"><img width="660" height="530" alt="image" src="https://github.com/caovan-huy/gui-tin-nhan-broadcast-qua-UDP/blob/main/docs/anh%206.png" /></p>
<p align = "center"> Ảnh 5: Thông báo khi dừng chat giữa Client với Server </p>
## 📦4. Các bước cài 
### Yêu cầu hệ 
- Eclipse IDE (khuyến nghị bản mới nhất)
- JDK 21 hoặc cao hơn
- Git đã cài trên máy

Bước 1: Chọn project từ GitHub
```bash
https://github.com/caovan-huy/gui-tin-nhan-broadcast-qua-UDP/tree/main/Bai_Tap_Lon
```
Bước 2: Import project vào Eclipse

- Mở Eclipse
- Vào File → Import
- Chọn Existing Projects into Workspace
- Chọn thư mục project vừa clone về
- Nhấn Finish

Bước 3: Kiểm tra môi trường

- Đảm bảo project chạy trên JavaSE-21 (hoặc phiên bản JDK bạn đã cài).
- Nếu thiếu thư viện, vào Project → Properties → Java Build Path để thêm JDK phù hợp.

Bước 4: Chạy ứng dụng

- Mở class UDPSeverChat → Run để khởi động server.
- Mở class UDPClientChat → Run để khởi động client.

Bước 5: Gửi và nhận tin nhắn

- Nhập nội dung tin nhắn → nhấn "Gửi"
- Tất cả client khác trong cùng mạng LAN sẽ nhận được tin nhắn broadcast.
- Có thể ấn "Stop Server/ Stop Client" để đóng đoạn chat
##  📱5. Liên hệ
- Họ và tên: Cao Văn Huy
- Lớp: CNTT 16-
- 📧 huyhechbn@gmail.com
- ☎️ 0964611204

© 2025 AIoTLab, Faculty of Information Technology, DaiNam University. All rights reserved.

---
