package com.pablocos.KinalApp1;

import com.pablocos.KinalApp1.entity.Cliente;
import com.pablocos.KinalApp1.entity.DetalleVenta;
import com.pablocos.KinalApp1.entity.Producto;
import com.pablocos.KinalApp1.entity.Usuario;
import com.pablocos.KinalApp1.entity.Venta;
import com.pablocos.KinalApp1.repository.ClienteRepository;
import com.pablocos.KinalApp1.repository.DetalleVentaRepository;
import com.pablocos.KinalApp1.repository.ProductoRepository;
import com.pablocos.KinalApp1.repository.UsuarioRepository;
import com.pablocos.KinalApp1.repository.VentaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * DataSeeder – inserta 1000 registros en cada entidad principal.
 * Solo se activa con el perfil "dev": ejecutar con --spring.profiles.active=dev
 * O agregar en application.properties: spring.profiles.active=dev
 */
@Component
public class DataSeeder implements CommandLineRunner {

    private final ClienteRepository clienteRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProductoRepository productoRepository;
    private final VentaRepository ventaRepository;
    private final DetalleVentaRepository detalleVentaRepository;
    private final PasswordEncoder passwordEncoder;

    private final Random random = new Random();

    public DataSeeder(ClienteRepository clienteRepository,
                      UsuarioRepository usuarioRepository,
                      ProductoRepository productoRepository,
                      VentaRepository ventaRepository,
                      DetalleVentaRepository detalleVentaRepository,
                      PasswordEncoder passwordEncoder) {
        this.clienteRepository = clienteRepository;
        this.usuarioRepository = usuarioRepository;
        this.productoRepository = productoRepository;
        this.ventaRepository = ventaRepository;
        this.detalleVentaRepository = detalleVentaRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        // Si ya hay datos, no volver a insertar
        if (clienteRepository.count() > 0) {
            System.out.println("[DataSeeder] Ya existen datos, se omite la carga.");
            return;
        }

        System.out.println("[DataSeeder] Iniciando carga de datos...");

        List<Cliente>  clientes  = seedClientes(1000);
        // List<Usuario>  usuarios  = seedUsuarios(1000);
        List<Usuario>  usuarios  = usuarioRepository.findAll();
        List<Producto> productos = seedProductos(1000);
        List<Venta>    ventas    = seedVentas(1000, clientes, usuarios);
        seedDetalles(1000, ventas, productos);

        System.out.println("[DataSeeder] Carga completada:");
        System.out.println("  Clientes  : " + clienteRepository.count());
        System.out.println("  Usuarios  : " + usuarioRepository.count());
        System.out.println("  Productos : " + productoRepository.count());
        System.out.println("  Ventas    : " + ventaRepository.count());
        System.out.println("  Detalles  : " + detalleVentaRepository.count());
    }

    // ─── CLIENTES ────────────────────────────────────────────────────────────
    private List<Cliente> seedClientes(int cantidad) {
        String[] nombres   = {"Ana","Luis","Carlos","María","Pedro","Sofia","Jorge","Laura","Diego","Claudia"};
        String[] apellidos = {"García","López","Martínez","Pérez","Rodríguez","Sánchez","Ramírez","Torres","Flores","Morales"};
        String[] ciudades  = {"Guatemala","Mixco","Villa Nueva","Quetzaltenango","Escuintla","Cobán","Antigua","Chiquimula"};

        List<Cliente> lista = new ArrayList<>();
        for (int i = 1; i <= cantidad; i++) {
            Cliente c = new Cliente();
            // DPI de 13 dígitos único
            c.setDPICliente(String.format("%013d", 1000000000000L + i));
            c.setNombreCliente(nombres[i % nombres.length]);
            c.setApellidoCliente(apellidos[i % apellidos.length]);
            c.setDireccionCliente(i + " Calle " + (i % 20 + 1) + " Zona " + (i % 10 + 1) + ", " + ciudades[i % ciudades.length]);
            c.setEstado(1L);
            lista.add(c);
            if (i % 100 == 0) clienteRepository.saveAll(lista).forEach(x -> {}); // batch cada 100
        }
        return clienteRepository.saveAll(lista);
    }

    // ─── USUARIOS ────────────────────────────────────────────────────────────

    // ─── PRODUCTOS ───────────────────────────────────────────────────────────
    private List<Producto> seedProductos(int cantidad) {
        String[] categorias = {"Laptop","Monitor","Teclado","Mouse","Audífono","Webcam","Disco SSD","Memoria RAM","Cable HDMI","Hub USB"};
        String[] marcas     = {"Samsung","LG","Logitech","HP","Dell","Lenovo","Kingston","Corsair","Anker","Asus"};

        List<Producto> lista = new ArrayList<>();
        for (int i = 1; i <= cantidad; i++) {
            Producto p = new Producto();
            p.setNombreProducto(marcas[i % marcas.length] + " " + categorias[i % categorias.length] + " " + i);
            // Precio entre Q50 y Q5000
            double precio = 50 + (random.nextInt(4950));
            p.setPrecio(BigDecimal.valueOf(precio));
            p.setStock((long)(random.nextInt(200) + 10));
            p.setEstado(1L);
            lista.add(p);
        }
        return productoRepository.saveAll(lista);
    }

    // ─── VENTAS ──────────────────────────────────────────────────────────────
    private List<Venta> seedVentas(int cantidad, List<Cliente> clientes, List<Usuario> usuarios) {
        List<Venta> lista = new ArrayList<>();
        LocalDate base = LocalDate.of(2024, 1, 1);

        for (int i = 0; i < cantidad; i++) {
            Venta v = new Venta();
            v.setClienteVenta(clientes.get(i % clientes.size()));
            v.setUsuarioVenta(usuarios.get(i % usuarios.size()));
            v.setFechaVenta(base.plusDays(i % 365));
            // Total provisional; se actualizará cuando se calculen los detalles
            v.setTotal(BigDecimal.ZERO);
            v.setEstado(1L);
            lista.add(v);
        }
        return ventaRepository.saveAll(lista);
    }

    // ─── DETALLES ────────────────────────────────────────────────────────────
    private void seedDetalles(int cantidad, List<Venta> ventas, List<Producto> productos) {
        List<DetalleVenta> lista = new ArrayList<>();

        for (int i = 0; i < cantidad; i++) {
            Venta   venta    = ventas.get(i % ventas.size());
            Producto producto = productos.get(random.nextInt(productos.size()));
            long     cant    = random.nextInt(5) + 1;
            BigDecimal precio   = producto.getPrecio();
            BigDecimal subTotal = precio.multiply(BigDecimal.valueOf(cant));

            DetalleVenta d = new DetalleVenta();
            d.setDetalleVenta(venta);
            d.setDetalleproducto(producto);
            d.setCantidad(cant);
            d.setPrecioUnitario(precio);
            d.setSubTotal(subTotal);
            lista.add(d);

            // Actualizar total de la venta
            venta.setTotal(venta.getTotal().add(subTotal));
        }

        detalleVentaRepository.saveAll(lista);
        ventaRepository.saveAll(ventas); // persistir totales actualizados
    }
}
