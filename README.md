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
Trong thời đại công nghệ thông tin phát triển mạnh mẽ, việc truyền tải dữ liệu nhanh chóng và hiệu quả giữa các thiết bị trong mạng máy tính ngày càng trở nên quan trọng. Một trong những phương thức được sử dụng phổ biến để gửi dữ liệu trong mạng cục bộ (LAN) là gửi tin nhắn broadcast qua giao thức UDP (User Datagram Protocol).

Broadcast là kỹ thuật truyền dữ liệu từ một thiết bị tới tất cả các thiết bị khác trong cùng mạng mà không cần xác định địa chỉ cụ thể của từng máy. Kết hợp với UDP – một giao thức hướng datagram, không kết nối, nhẹ và có tốc độ xử lý nhanh – việc gửi tin nhắn broadcast mang lại khả năng truyền thông tin tức thời, thuận tiện và tiết kiệm tài nguyên hệ thống.

Đề tài **Gửi tin nhắn broadcast qua UDP** được lựa chọn nhằm tìm hiểu cơ chế hoạt động của giao thức UDP, cách triển khai việc gửi và nhận tin nhắn trên môi trường mạng, cũng như xây dựng một ứng dụng nhỏ minh họa chức năng này. Qua đó, đề tài giúp người học củng cố kiến thức về mạng máy tính, lập trình socket, đồng thời có thể áp dụng vào các ứng dụng thực tế như chat trong mạng LAN, thông báo hệ thống hoặc các ứng dụng giám sát và điều khiển từ xa.

Với tính đơn giản, hiệu quả và khả năng mở rộng, việc nghiên cứu gửi tin nhắn broadcast qua UDP không chỉ mang tính học tập mà còn có giá trị thực tiễn trong phát triển các hệ thống truyền thông nhanh gọn và hiệu quả.  
## 🔧 2. Ngôn ngữ lập trình sử dụng: [![Java](https://img.shields.io/badge/Java-007396?style=for-the-badge&logo=java&logoColor=white)](https://www.java.com/)
- **Language setting** Java (JDK)
- **Giao diện:** Java Swing  
- **Giao thức mạng:** UDP (User Datagram Protocol)  
- **IDE:** Eclipse  
## 🚀 3. Hình ảnh các chức năng
<p align="center">
  <img width="604" height="487" img src="https://github.com/caovan-huy/gui-tin-nhan-broadcast-qua-UDP/blob/main/docs/anh%201.png" alt="Ảnh 1" width="800"/> 
</p>
<p align = "center">Ảnh 1: Giao diện Server </p>

 <p align = "center"><img width="450" height="262" alt="image" src="https://github.com/caovan-huy/gui-tin-nhan-broadcast-qua-UDP/blob/main/docs/anh%202.png" /></p>
<p align = "center"> Ảnh 2: Giao diên nhập IP Server </p>
<p align = "center"><img width="604" height="487" alt="image" src="https://github.com/caovan-huy/gui-tin-nhan-broadcast-qua-UDP/blob/main/docs/anh%203.png" /></p>
<p align = "center"> Ảnh 3: Giao diện Client </p>

## 📦4. Các bước cài 
### Yêu cầu hệ 
- Eclipse IDE (khuyến nghị bản mới nhất)
- JDK 21 hoặc cao hơn
- Git đã cài trên máy

Bước 1: Clone project từ GitHub
```bash
git clone https://github.com/caovan-/Gui-tin-nhan-broadcast-qua-UDP.git
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

- Nhập nội dung tin nhắn → nhấn Gửi
- Tất cả client khác trong cùng mạng LAN sẽ nhận được tin nhắn broadcast.
- Có thể ấn "Stop Server/ Stop Client" để đóng đoạn chat
##  📱5. Liên hệ
- Họ và tên: Cao Văn Huy
- Lớp: CNTT 16-
- 📧 huyhechbn@gmail.com
- ☎️ 0964611204

© 2025 AIoTLab, Faculty of Information Technology, DaiNam University. All rights reserved.

---























