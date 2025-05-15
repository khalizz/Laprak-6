import java.util.*;

// Generic class Product<T> yg menyimpan data produk
class Product<T extends Comparable<T>> implements Comparable<Product<T>> {
    private int id;             // ID produk
    private String name;        // Nama produk
    private T category;         // Kategori produk
    private double price;       // Harga produk

    public Product(int id, String name, T category, double price) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public T getCategory() {
        return category;
    }

    public double getPrice() {
        return price;
    }

    // mengurutkan berdasarkan kategori
    @Override
    public int compareTo(Product<T> other) {
        return this.category.compareTo(other.category);
    }

    // Menampilkan data produk 
    @Override
    public String toString() {
        return "ID: " + id + ", Nama: " + name + ", Kategori: " + category + ", Harga: " + price;
    }
}

// Kelas untuk mengelola daftar produk
class ProductManager {
    private List<Product<?>> products = new ArrayList<>();             // Daftar semua produk
    private Set<String> categories = new HashSet<>();                  // Kategori produk
    private Queue<Product<?>> processedQueue = new LinkedList<>();    // Antrian produk yang telah diproses

    // Menambahkan produk baru
    public void addProduct(Product<?> product) {
        products.add(product);
        categories.add(product.getCategory().toString());
    }

    // Menghapus produk berdasarkan ID
    public void removeProductById(int id) {
        products.removeIf(p -> p.getId() == id);
    }

    // Mencari produk berdasarkan nama dan kategori
    public Product<?> searchProduct(String name, String category) {
        for (Product<?> p : products) {
            if (p.getName().equalsIgnoreCase(name) && p.getCategory().toString().equalsIgnoreCase(category)) {
                return p;
            }
        }
        return null;
    }

    // Menampilkan seluruh produk (diurutkan berdasarkan kategori)
    public void displayProducts() {
        products.sort(Comparator.comparing(p -> p.getCategory().toString()));
        for (Product<?> p : products) {
            System.out.println(p);
        }
    }

    // Menampilkan kategori 
    public void displayCategories() {
        for (String cat : categories) {
            System.out.println(cat);
        }
    }

    // Menambahkan ke antrian produk yg sudah diproses
    public void addToProcessedQueue(Product<?> product) {
        processedQueue.offer(product);
    }

    // Menampilkan isi antrian produk yg telah diproses
    public void displayProcessedQueue() {
        if (processedQueue.isEmpty()) {
            System.out.println("Belum ada produk yang diproses.");
        } else {
            for (Product<?> p : processedQueue) {
                System.out.println(p);
            }
        }
    }

    // Fitur tambahan: Menampilkan produk termahal dari setiap kategori
    public void displayMostExpensivePerCategory() {
        System.out.println("\n== Produk Termahal di Tiap Kategori ==");
        Map<String, Product<?>> maxByCategory = new HashMap<>();

        for (Product<?> p : products) {
            String cat = p.getCategory().toString();
            if (!maxByCategory.containsKey(cat) || p.getPrice() > maxByCategory.get(cat).getPrice()) {
                maxByCategory.put(cat, p);
            }
        }

        for (Map.Entry<String, Product<?>> entry : maxByCategory.entrySet()) {
            System.out.println("Kategori: " + entry.getKey() + " -> " + entry.getValue());
        }
    }
}

// Kelas utama 
public class Marketplace {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        ProductManager manager = new ProductManager();

        // Menambahkan data produk awal
        manager.addProduct(new Product<>(1, "Nasi Ayam Geprek", "Makanan", 15000));
        manager.addProduct(new Product<>(2, "Es Teh", "Minuman", 5000));
        manager.addProduct(new Product<>(3, "Nasi Bebek Geprek", "Makanan", 20000));
        manager.addProduct(new Product<>(4, "Es Teler", "Minuman", 12000));
        manager.addProduct(new Product<>(5, "Nasi Sapi Geprek", "Makanan", 25000));

        // Menampilkan seluruh produk
        System.out.println("== Daftar Menu Marketplace ==");
        manager.displayProducts();

        // Menampilkan semua kategori
        System.out.println("\n== Kategori Menu ==");
        manager.displayCategories();

        // Memproses sekaligus (satu makanan dan satu minuman)
        for (int i = 1; i <= 2; i++) {
            System.out.println("\n== Pilih Menu ke-" + i + " untuk Dipesan ==");
            System.out.print("Masukkan nama produk: ");
            String inputNama = scanner.nextLine();
            System.out.print("Masukkan kategori Menu : ");
            String inputKategori = scanner.nextLine();

            // Mencari produk berdasarkan nama dan kategori
            Product<?> selected = manager.searchProduct(inputNama, inputKategori);

            if (selected != null) {
                System.out.println("pesanan ditemukan dan dimasak.");
                manager.addToProcessedQueue(selected);
            } else {
                System.out.println("Pesanan tidak ditemukan.");
            }
        }

        // Menampilkan produk yang telah diproses
        System.out.println("\n== Pesananan yang Diproses ==");
        manager.displayProcessedQueue();

        // Menampilkan produk termahal per kategori (fitur tambahan)
        manager.displayMostExpensivePerCategory();

        scanner.close();
    }
}
