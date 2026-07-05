# 📚 Library Management System — Advanced Data Structures Implementation

<p align="left">
  <img src="https://img.shields.io/badge/Language-Java-orange?style=flat-square&logo=java" alt="Java">
  <img src="https://img.shields.io/badge/Architecture-Console--Based-lightgrey?style=flat-square" alt="Console">
  <img src="https://img.shields.io/badge/Course-Data_Structures-blue?style=flat-square" alt="Data Structures">
</p>

Bu proje, nesne yönelimli programlama (OOP) prensipleri ve gelişmiş veri yapıları kullanılarak Java dilinde geliştirilmiş konsol tabanlı bir **Kütüphane Yönetim Sistemidir**. Projenin temel amacı; arama, sıralama, geri alma (undo) ve kuyruk yönetimi gibi gerçek dünya senaryolarını, en optimum zaman ($Time$) ve alan ($Space$) karmaşıklığına sahip veri yapılarıyla çözmektir.

---

## 👥 Geliştiriciler (Grup Üyeleri)

| İsim | Öğrenci Numarası | Program / Öğretim |
| :--- | :--- | :--- |
| **Zeynep İclal BİLGİLİ** | `230315038` | 1st Ed. |
| **Sude Naz AY** | `230315074` | 1st Ed. |
| **Ayşe ARSLAN** | `230315020` | 1st Ed. |

---

## 🛠️ Kullanılan Veri Yapıları ve Tercih Nedenleri

Sistemdeki her bir fonksiyonel gereksinim için performansı maksimize edecek özel bir veri yapısı seçilmiştir:

- **⚡ HashMap (`O(1)`):** Kitap ve üye kayıtlarını benzersiz ID'leri ile saklamak, onlara anında erişmek için kullanılmıştır.
- **🌲 Binary Search Tree - BST (`O(log n)`):** Kitapları başlıklarına göre alfabetik ve hızlı bir şekilde arayabilmek (Case-insensitive) için entegre edilmiştir.
- **🔄 Stack (`O(1)`):** Son yapılan ödünç alma, iade etme veya silme işlemlerini LIFO (Last In, First Out) mantığıyla geriye alabilen gelişmiş bir **Undo** sistemi için kullanılmıştır.
- **🚶 Queue (`O(1)`):** Ödünç alınmak istenen kitap müsait olmadığında, üyeleri adil bir şekilde sıraya (FIFO) eklemek ve kitap iade edildiğinde sıradaki üyeye otomatik atamak için kullanılmıştır[cite: 2].
- **📊 Priority Queue (`O(log n)`):** Kitapların ödünç alınma sayılarını (`borrowCount`) takip ederek platformun en popüler kitaplarını dinamik olarak sıralamak için kullanılmıştır[cite: 2].

---

## 📋 Sistem Özellikleri & Menü Yapısı

Uygulama çalıştırıldığında kullanıcıyı karşılayan menü üzerinden şu işlemler gerçekleştirilebilir[cite: 2]:

1. **Add Book:** Sisteme yeni kitap ekler, başlık ve grup seed değerini harmanlayarak benzersiz bir hash ID üretir[cite: 2].
2. **Register Member:** Kütüphaneye yeni üye kaydeder[cite: 2].
3. **Borrow Book:** Kitap müsaitse üyeye ödünç verir, değilse üyeyi kitabın bekleme listesine (Queue) alır[cite: 2].
4. **Return Book:** Kitabı iade alır; eğer bekleme listesinde üye varsa kitabı doğrudan sıradaki üyeye atar[cite: 2].
5. **Search Book by Title:** BST kullanarak başlığa göre kitap detaylarını ve uygunluk durumunu anında getirir[cite: 2].
6. **Show Most Popular Books:** Kitapları popülerlik derecesine göre listeler[cite: 2].
7. **Remove Book / Member:** Kitapları veya üzerinde aktif ödünç kitap bulunmayan üyeleri sistemden siler[cite: 2].
8. **Undo Last Action:** Yapılan son kritik işlemi (Borrow, Return, Remove) başarıyla geri alır[cite: 2].

---

## 💻 Derleme ve Çalıştırma

Proje standart Java SE ortamında herhangi bir harici kütüphaneye ihtiyaç duymadan çalışacak şekilde tasarlanmıştır[cite: 2].

### 1. Derleme (Compile)
Terminal üzerinden projenin bulunduğu dizine giderek aşağıdaki komutu çalıştırın:
```bash
javac LibraryManagementSystem.java
