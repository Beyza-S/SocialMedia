# 📱 Social Media Uygulaması

Bu proje temel bir sosyal medya backend uygulamasıdır. **Java Spring Boot** kullanılarak geliştirilmiştir ve Spring Security kullanılmadan **token tabanlı kimlik doğrulama sistemi** uygulanmıştır.

Uygulama kullanıcıların post paylaşabildiği, yorum yapabildiği ve postları beğenebildiği bir sistemi içermektedir.

---

## 🛠️ Kullanılan Teknolojiler

- Java 17
- Spring Boot 3.5.11
- Spring Data JPA
- PostgreSQL
- Maven
- Lombok
- Postman *(API testleri)*

---

## 🚀 Kurulum ve Çalıştırma

### 1. PostgreSQL Veritabanı Oluşturma

Öncelikle PostgreSQL içinde bir database oluşturun:

```sql
CREATE DATABASE socialmediadb;
```

### 2. `application.properties` Ayarı

IntelliJ IDEA Community uygulamasında `src/main/resources/application.properties` dosyasına aşağıdaki kodları yazın:

```properties
spring.application.name=socialMedia

server.port=8080

spring.datasource.url=jdbc:postgresql://localhost:5432/socialmediadb
spring.datasource.username=postgres
spring.datasource.password=beyza123

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

spring.jpa.open-in-view=false

app.admin.username=admin
app.admin.password=admin123
```

### 3. Projeyi Çalıştırma

IntelliJ üzerinden `SocialMediaApplication.java` → **Run** denildiğinde uygulama aşağıdaki adreste çalışacaktır:

```
http://localhost:8080
```

---

## 🔐 Authentication Sistemi

Bu projede **Spring Security kullanılmamıştır**. Bunun yerine custom token-based authentication uygulanmıştır.

### Çalışma Mantığı

1. Kullanıcı login olur
2. Sistem bir access token üretir
3. Token veritabanında saklanır
4. Her korumalı endpoint çağrısında token doğrulanır
5. **Logout** ile token geçersiz hale gelir.
6. Token'ın **expiration süresi** vardır

### Token Doğrulaması

```java
authService.validateToken(token);
```

### Sistem Rolleri

Projede **Admin** ve **User** olmak üzere iki rol bulunmaktadır. Uygulama başlatıldığında sistemde hazır bir admin kullanıcı bulunur.

> Default Admin bilgileri `application.properties` üzerinden oluşturulmaktadır.

| Alan     | Değer       |
|----------|-------------|
| username | `admin`     |
| password | `admin123`|

---

## 📡 API Endpointleri (Postman)

**Base URL:** `http://localhost:8080`

---

### 🔑 AUTH

#### Kullanıcı Kaydı
```
POST /api/auth/signup
```
```json
{
  "username": "user1",
  "password": "user123"
}
```

#### Login
```
POST /api/auth/login
```
```json
{
  "username": "user1",
  "password": "user123"
}
```
**Response:**
```json
{
  "token": "generated-access-token",
  "expiresAt": "2026-01-01T20:00:00"
}
```

#### Logout
```
POST /api/auth/logout
```

#### Aktif Kullanıcı Bilgisi
```
GET /api/auth/me
```

---

### 👤 USERS

#### Profil
```
GET /api/users/{id}
```

#### Şifre Güncelle
```
PUT /api/users/me/password
```

#### Kendi Hesabını Sil
```
DELETE /api/users/me
```

#### Admin — Kullanıcı Silme
```
DELETE /api/admin/users/{id}
```

---

### 📝 POSTS

#### Post Oluştur
```
POST /api/posts
```
```json
{
  "imageUrl": "https://example.com/image.jpg",
  "postDescription": "User1 post"
}
```

#### Post Detayı (Tüm Bilgiler)
```
GET /api/posts/{id}
```
**Response:**
```json
{
  "id": 1,
  "imageUrl": "https://example.com/image.jpg",
  "postDescription": "User1 post",
  "userId": 2,
  "username": "user1",
  "likeCount": 3,
  "viewCount": 5,
  "commentCount": 1,
  "comments": [
    {
      "id": 10,
      "commentContent": "Nice post",
      "userId": 3,
      "username": "user2",
      "createdAt": "2026-01-01T10:20:30"
    }
  ],
  "createdAt": "2026-01-01T09:00:00"
}
```

#### Tüm Postları Listele
```
GET /api/posts
```

#### Post Güncelle
```
PUT /api/posts/{id}
```

#### Post Sil
```
DELETE /api/posts/{id}
```

#### Post View
```
POST /api/posts/{id}/view
```

#### Post Yorumlarını Listele
```
GET /api/posts/{id}/comments
```

---

### 💬 COMMENTS

#### Yorum Ekle
```
POST /api/posts/{id}/comments
```
**Body (TEXT):**
```
This is a comment
```

#### Yorum Sil
```
DELETE /api/comments/{id}
```

---

### ❤️ LIKES

#### Post Beğen
```
POST /api/posts/{id}/likes
```

#### Beğeni Kaldır
```
DELETE /api/posts/{id}/likes
```

---

## 🔒 Token Kullanımı

Bu projede kimlik doğrulama Spring Security kullanılmadan uygulanmıştır. Login işlemi sonrası oluşturulan token veritabanında saklanır ve korumalı endpoint çağrılarında **Authorization header içinde Bearer** formatında gönderilerek doğrulanır.

---

## ⚠️ Varsayımlar ve Kısıtlar

- Bir `username` sadece bir kez kullanılabilir.
- Bir kullanıcı aynı postu birden fazla kez görüntüleyebilir ve `viewCount` her görüntülemede artar.
- Bir kullanıcı bir postu yalnızca **bir kez** beğenebilir.
- Token süresi dolmuşsa korumalı endpointlere erişim reddedilir.
- Post silindiğinde ilişkili `comment` ve `like` kayıtları da silinir.

---

## 📦 Dependencies

- Spring Web
- Spring Data JPA
- PostgreSQL Driver
- Lombok
- Validation
- jBCrypt

---

## 📬 Postman Collection

Projede kullanılan tüm API endpointleri **Postman Collection** içinde bulunmaktadır. Postman üzerinden import edilerek tüm endpointler kolayca test edilebilir.

### Environment Variables

| Variable       | Value                        |
|----------------|------------------------------|
| `{{baseUrl}}`     | `http://localhost:8080`      |
| `{{accessToken}}` | login sonrası alınan token   |
