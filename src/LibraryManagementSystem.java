import java.util.*;

/*
 * Library Management System
 * Console-based application with menu interface
 *
 * STUDENT ID USAGE (GROUP PROJECT):
 * Student IDs:
 * 230315038
 * 230315020
 * 230315074
 *
 * These IDs are combined into a GROUP_SEED value
 * and used in ID generation logic.
 */

public class LibraryManagementSystem {

    /* ===================== STUDENT ID DEFINITIONS ===================== */

    static final int STUDENT_ID_1 = 230315038;
    static final int STUDENT_ID_2 = 230315020;
    static final int STUDENT_ID_3 = 230315074;

    static final int GROUP_SEED =
            (STUDENT_ID_1 + STUDENT_ID_2 + STUDENT_ID_3) % 1000000;

    /* ===================== DATA STRUCTURES ===================== */

    HashMap<Integer, Book> books = new HashMap<>();
    HashMap<Integer, Member> members = new HashMap<>();
    Stack<Action> undoStack = new Stack<>();
    BookBST bst = new BookBST();

    PriorityQueue<Book> popularBooks =
            new PriorityQueue<>((a, b) -> b.borrowCount - a.borrowCount);

    /* ===================== CATALOG MANAGEMENT SYSTEM ===================== */

    public void addBook(String title, String author) {
        int id = Math.abs((title + GROUP_SEED).hashCode());
        Book book = new Book(id, title, author);
        books.put(id, book);
        bst.insert(book);
        System.out.println("Book added successfully. ID: " + id);
    }

    public void removeBook(int bookId) {
        Book b = books.get(bookId);

        if (b == null) {
            System.out.println("Book not found.");
            return;
        }

        books.remove(bookId);
        undoStack.push(new Action("REMOVE_BOOK", b));
        System.out.println("Book removed successfully.");
    }

    /* ===================== MEMBER MANAGEMENT SYSTEM ===================== */

    public void registerMember(String name) {
        int id = Math.abs((name + GROUP_SEED).hashCode());
        members.put(id, new Member(id, name));
        System.out.println("Member registered successfully. ID: " + id);
    }

    public void removeMember(int memberId) {
        Member m = members.get(memberId);

        if (m == null) {
            System.out.println("Member not found.");
            return;
        }

        if (!m.borrowedBooks.isEmpty()) {
            System.out.println("Member has borrowed books. Cannot remove.");
            return;
        }

        members.remove(memberId);
        undoStack.push(new Action("REMOVE_MEMBER", m));
        System.out.println("Member removed successfully.");
    }

    /* ===================== CIRCULATION SYSTEM ===================== */

    public void borrowBook(int memberId, int bookId) {
        Member m = members.get(memberId);
        Book b = books.get(bookId);

        if (m == null || b == null) {
            System.out.println("Invalid member ID or book ID.");
            return;
        }

        if (b.available) {
            b.available = false;
            b.borrowCount++;
            m.borrowedBooks.add(bookId);
            popularBooks.add(b);
            undoStack.push(new Action("BORROW", memberId, bookId));
            System.out.println("Book borrowed successfully.");
        } else {
            b.waitList.add(memberId);
            System.out.println("Book not available. Added to waitlist.");
        }
    }

    public void returnBook(int memberId, int bookId) {
        Member m = members.get(memberId);
        Book b = books.get(bookId);

        if (m == null || b == null) {
            System.out.println("Invalid member ID or book ID.");
            return;
        }

        m.borrowedBooks.remove((Integer) bookId);

        if (!b.waitList.isEmpty()) {
            int nextMember = b.waitList.poll();
            members.get(nextMember).borrowedBooks.add(bookId);
            System.out.println("Book assigned to next member in waitlist.");
        } else {
            b.available = true;
            System.out.println("Book returned successfully.");
        }

        undoStack.push(new Action("RETURN", memberId, bookId));
    }

    /* ===================== SEARCH & DISCOVERY SYSTEM ===================== */

    public void searchBookByTitle(String title) {
        Book b = bst.search(title);
        if (b == null)
            System.out.println("Book not found.");
        else
            System.out.println("Found: " + b.title + " by " + b.author +
                    " | Available: " + b.available);
    }

    /* ===================== ANALYTICS SYSTEM ===================== */

    public void showPopularBooks() {
        if (popularBooks.isEmpty()) {
            System.out.println("No books borrowed yet.");
            return;
        }

        System.out.println("Most Popular Books:");
        for (Book b : popularBooks) {
            System.out.println(b.title + " (" + b.borrowCount + " borrows)");
        }
    }

    /* ===================== UNDO SYSTEM ===================== */

    public void undo() {
        if (undoStack.isEmpty()) {
            System.out.println("Nothing to undo.");
            return;
        }

        Action action = undoStack.pop();

        switch (action.type) {

            case "BORROW":
                Book b = books.get(action.bookId);
                Member m = members.get(action.memberId);
                if (b != null && m != null) {
                    b.available = true;
                    b.borrowCount--;
                    m.borrowedBooks.remove((Integer) action.bookId);
                }
                System.out.println("Undo borrow successful.");
                break;

            case "RETURN":
                b = books.get(action.bookId);
                m = members.get(action.memberId);
                if (b != null && m != null) {
                    b.available = false;
                    m.borrowedBooks.add(action.bookId);
                }
                System.out.println("Undo return successful.");
                break;

            case "REMOVE_BOOK":
                books.put(action.bookId, action.removedBook);
                bst.insert(action.removedBook);
                System.out.println("Undo remove book successful.");
                break;

            case "REMOVE_MEMBER":
                members.put(action.memberId, action.removedMember);
                System.out.println("Undo remove member successful.");
                break;
        }
    }

    /* ===================== MENU SYSTEM ===================== */

    public void startMenu() {
        Scanner sc = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n===== LIBRARY MANAGEMENT SYSTEM =====");
            System.out.println("1. Add Book");
            System.out.println("2. Register Member");
            System.out.println("3. Borrow Book");
            System.out.println("4. Return Book");
            System.out.println("5. Search Book by Title");
            System.out.println("6. Show Most Popular Books");
            System.out.println("7. Remove Book");
            System.out.println("8. Remove Member");
            System.out.println("9. Undo Last Action");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");

            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter book title: ");
                    String title = sc.nextLine();
                    System.out.print("Enter author: ");
                    String author = sc.nextLine();
                    addBook(title, author);
                    break;

                case 2:
                    System.out.print("Enter member name: ");
                    String name = sc.nextLine();
                    registerMember(name);
                    break;

                case 3:
                    System.out.print("Enter member ID: ");
                    int mId = sc.nextInt();
                    System.out.print("Enter book ID: ");
                    int bId = sc.nextInt();
                    borrowBook(mId, bId);
                    break;

                case 4:
                    System.out.print("Enter member ID: ");
                    mId = sc.nextInt();
                    System.out.print("Enter book ID: ");
                    bId = sc.nextInt();
                    returnBook(mId, bId);
                    break;

                case 5:
                    System.out.print("Enter book title: ");
                    String searchTitle = sc.nextLine();
                    searchBookByTitle(searchTitle);
                    break;

                case 6:
                    showPopularBooks();
                    break;

                case 7:
                    System.out.print("Enter book ID: ");
                    bId = sc.nextInt();
                    removeBook(bId);
                    break;

                case 8:
                    System.out.print("Enter member ID: ");
                    mId = sc.nextInt();
                    removeMember(mId);
                    break;

                case 9:
                    undo();
                    break;

                case 0:
                    System.out.println("Exiting system...");
                    break;

                default:
                    System.out.println("Invalid choice.");
            }

        } while (choice != 0);

        sc.close();
    }

    public static void main(String[] args) {
        LibraryManagementSystem system = new LibraryManagementSystem();
        system.startMenu();
    }
}

/* ===================== ENTITIES ===================== */

class Book {
    int id;
    String title;
    String author;
    boolean available = true;
    int borrowCount = 0;
    Queue<Integer> waitList = new LinkedList<>();

    public Book(int id, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;
    }
}

class Member {
    int id;
    String name;
    LinkedList<Integer> borrowedBooks = new LinkedList<>();

    public Member(int id, String name) {
        this.id = id;
        this.name = name;
    }
}

class Action {
    String type;
    int memberId;
    int bookId;
    Book removedBook;
    Member removedMember;

    public Action(String type, int memberId, int bookId) {
        this.type = type;
        this.memberId = memberId;
        this.bookId = bookId;
    }

    public Action(String type, Book book) {
        this.type = type;
        this.removedBook = book;
        this.bookId = book.id;
    }

    public Action(String type, Member member) {
        this.type = type;
        this.removedMember = member;
        this.memberId = member.id;
    }
}

/* ===================== BST ===================== */

class BSTNode {
    String title;
    Book book;
    BSTNode left, right;

    public BSTNode(Book book) {
        this.book = book;
        this.title = book.title;
    }
}

class BookBST {
    BSTNode root;

    public void insert(Book book) {
        root = insertRec(root, book);
    }

    private BSTNode insertRec(BSTNode node, Book book) {
        if (node == null) return new BSTNode(book);

        if (book.title.compareToIgnoreCase(node.title) < 0)
            node.left = insertRec(node.left, book);
        else
            node.right = insertRec(node.right, book);

        return node;
    }

    public Book search(String title) {
        return searchRec(root, title);
    }

    private Book searchRec(BSTNode node, String title) {
        if (node == null) return null;
        if (node.title.equalsIgnoreCase(title)) return node.book;

        return title.compareToIgnoreCase(node.title) < 0
                ? searchRec(node.left, title)
                : searchRec(node.right, title);
    }
}
