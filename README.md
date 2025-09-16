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
Đề tài “Gửi tin nhắn Broadcast qua UDP” nhằm nghiên cứu và xây dựng chương trình truyền tin trong mạng LAN bằng giao thức UDP với chế độ broadcast.

Trong mô hình này, một máy tính có thể gửi một gói tin đến **tất cả các thiết bị trong cùng mạng LAN** mà không cần biết địa chỉ IP cụ thể của từng máy. Điều này giúp việc **truyền thông điệp nhanh chóng và tiện lợi**, đặc biệt hữu ích trong các tình huống như:  
- Gửi thông báo hệ thống cho nhiều người dùng.  
- Ứng dụng chat nội bộ trong mạng LAN.  
- Tự động phát hiện dịch vụ (service discovery).  
## 🔧 2. Ngôn ngữ lập trình sử dụng: [![Java](https://img.shields.io/badge/Java-007396?style=for-the-badge&logo=java&logoColor=white)](https://www.java.com/)
- **Language setting** Java (JDK)
- **Giao diện:** Java Swing  
- **Giao thức mạng:** UDP (User Datagram Protocol)  
- **IDE:** Eclipse  
## 🚀 3. Hình ảnh các chức năng
Trong quá trình nghiên cứu và triển khai đề tài “Gửi tin nhắn Broadcast qua UDP”, em đã xây dựng và thử nghiệm một số project sau:

**1.Ứng dụng gửi thông báo trong mạng LAN**
- Cho phép một máy chủ gửi thông báo dạng văn bản đến toàn bộ các máy khách đang kết nối cùng mạng.
- Các thông báo hiển thị ngay lập tức trên giao diện của người dùng.

**2.Chương trình chat nội bộ (LAN Chat)**
- Xây dựng ứng dụng chat đơn giản cho phép nhiều máy tính trao đổi tin nhắn với nhau mà không cần biết IP cụ thể.
- Tin nhắn từ một máy sẽ được broadcast đến tất cả các máy còn lại.
  
**3.Ứng dụng phát hiện dịch vụ (Service Discovery)**
- Các máy tính trong mạng LAN tự động gửi gói tin broadcast để thông báo sự hiện diện.
- Nhờ đó, các thiết bị khác trong mạng có thể dễ dàng phát hiện và kết nối dịch vụ.

**4.Hệ thống demo thông báo sự kiện**
- Tạo ứng dụng mô phỏng việc gửi thông báo sự kiện (ví dụ: cảnh báo, nhắc nhở) từ server đến nhiều client trong lớp học hoặc văn phòng.
