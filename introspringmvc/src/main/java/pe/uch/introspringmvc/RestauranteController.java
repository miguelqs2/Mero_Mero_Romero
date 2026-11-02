package pe.uch.introspringmvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

import pe.uch.introspringmvc.model.ItemPedido;
import pe.uch.introspringmvc.model.Pedido;
import pe.uch.introspringmvc.model.ProductoPrecio;
import pe.uch.introspringmvc.repository.CategoriaRepository;
import pe.uch.introspringmvc.repository.PedidoRepository;
import pe.uch.introspringmvc.repository.ProductoPrecioRepository;

@Controller
public class RestauranteController {

    private final ProductoPrecioRepository ppRepo;
    private final PedidoRepository pedidoRepo;
    
    @Autowired
    private CategoriaRepository categoriaRepo;

    public RestauranteController(ProductoPrecioRepository ppRepo, PedidoRepository pedidoRepo) {
        this.ppRepo = ppRepo;
        this.pedidoRepo = pedidoRepo;
    }

    @GetMapping("/menu")
    public String mostrarMenu(Model model, HttpSession session) {
        model.addAttribute("categorias", categoriaRepo.findAll());
        List<ItemPedido> carrito = (List<ItemPedido>) session.getAttribute("carrito");
        int totalItems = (carrito != null) ? carrito.stream().mapToInt(ItemPedido::getCantidad).sum() : 0;
        model.addAttribute("totalItems", totalItems);
        return "menu";
    }

    @PostMapping("/agregar")
    public String agregarAlCarrito(@RequestParam("idPrecio") Integer idPrecio, HttpSession session) {
        List<ItemPedido> carrito = (List<ItemPedido>) session.getAttribute("carrito");
        if (carrito == null) carrito = new ArrayList<>();

        ProductoPrecio pp = ppRepo.findById(idPrecio).orElse(null);
        if (pp == null || !"DISPONIBLE".equals(pp.getEstado()) || pp.getStock() <= 0) {
            return "redirect:/menu";
        }

        boolean encontrado = false;
        for (ItemPedido item : carrito) {
            if (item.getProductoPrecio().getIdPrecio().equals(idPrecio)) {
                item.setCantidad(item.getCantidad() + 1);
                encontrado = true;
                break;
            }
        }
        if (!encontrado) carrito.add(new ItemPedido(pp, 1));

        pp.setStock(pp.getStock() - 1);
        if (pp.getStock() == 0) pp.setEstado("AGOTADO");
        ppRepo.save(pp);

        session.setAttribute("carrito", carrito);
        return "redirect:/menu";
    }

    @PostMapping("/aumentar")
    public String aumentarCantidad(@RequestParam("idPrecio") Integer idPrecio, HttpSession session) {
        List<ItemPedido> carrito = (List<ItemPedido>) session.getAttribute("carrito");
        if (carrito == null) return "redirect:/carrito";
        ProductoPrecio pp = ppRepo.findById(idPrecio).orElse(null);
        if (pp == null || pp.getStock() <= 0) return "redirect:/carrito";
        
        for (ItemPedido item : carrito) {
            if (item.getProductoPrecio().getIdPrecio().equals(idPrecio)) {
                item.setCantidad(item.getCantidad() + 1);
                break;
            }
        }
        pp.setStock(pp.getStock() - 1);
        if (pp.getStock() == 0) pp.setEstado("AGOTADO");
        ppRepo.save(pp);
        return "redirect:/carrito";
    }

    @PostMapping("/disminuir")
    public String disminuirCantidad(@RequestParam("idPrecio") Integer idPrecio, HttpSession session) {
        List<ItemPedido> carrito = (List<ItemPedido>) session.getAttribute("carrito");
        if (carrito == null) return "redirect:/carrito";
        
        for (int i = 0; i < carrito.size(); i++) {
            if (carrito.get(i).getProductoPrecio().getIdPrecio().equals(idPrecio)) {
                ItemPedido item = carrito.get(i);
                if (item.getCantidad() > 1) {
                    item.setCantidad(item.getCantidad() - 1);
                } else {
                    carrito.remove(i);
                }
                ppRepo.findById(idPrecio).ifPresent(p -> {
                    p.setStock(p.getStock() + 1);
                    if (p.getStock() > 0) p.setEstado("DISPONIBLE");
                    ppRepo.save(p);
                });
                break;
            }
        }
        return "redirect:/carrito";
    }

    @PostMapping("/eliminar")
    public String eliminarDelCarrito(@RequestParam("idPrecio") Integer idPrecio, HttpSession session) {
        List<ItemPedido> carrito = (List<ItemPedido>) session.getAttribute("carrito");
        if (carrito == null) return "redirect:/carrito";
        
        for (int i = 0; i < carrito.size(); i++) {
            if (carrito.get(i).getProductoPrecio().getIdPrecio().equals(idPrecio)) {
                int cantidad = carrito.get(i).getCantidad();
                ppRepo.findById(idPrecio).ifPresent(p -> {
                    p.setStock(p.getStock() + cantidad);
                    if (p.getStock() > 0) p.setEstado("DISPONIBLE");
                    ppRepo.save(p);
                });
                carrito.remove(i);
                break;
            }
        }
        return "redirect:/carrito";
    }

    @GetMapping("/carrito")
    public String verCarrito(Model model, HttpSession session) {
        List<ItemPedido> carrito = (List<ItemPedido>) session.getAttribute("carrito");
        if (carrito == null) carrito = new ArrayList<>();
        double total = carrito.stream().mapToDouble(ItemPedido::getSubtotal).sum();
        
        model.addAttribute("carrito", carrito);
        model.addAttribute("total", total);
        model.addAttribute("mesas", java.util.stream.IntStream.rangeClosed(1, 25).boxed().collect(Collectors.toList()));
        
        return "carrito";
    }

    @GetMapping("/vaciar")
    public String vaciarCarrito(HttpSession session) {
        List<ItemPedido> carrito = (List<ItemPedido>) session.getAttribute("carrito");
        if (carrito != null) {
            for (ItemPedido item : carrito) {
                ppRepo.findById(item.getProductoPrecio().getIdPrecio()).ifPresent(p -> {
                    p.setStock(p.getStock() + item.getCantidad());
                    if (p.getStock() > 0) p.setEstado("DISPONIBLE");
                    ppRepo.save(p);
                });
            }
        }
        session.removeAttribute("carrito");
        return "redirect:/carrito";
    }

    @PostMapping("/confirmar")
    public String confirmarPedido(@RequestParam("cliente") String cliente, 
                                  @RequestParam("mesa") Integer mesa, 
                                  @RequestParam(value = "observaciones", required = false) String observaciones,
                                  HttpSession session, Model model) {
        List<ItemPedido> carrito = (List<ItemPedido>) session.getAttribute("carrito");
        if (carrito == null || carrito.isEmpty()) return "redirect:/carrito";

        List<Pedido> pedidosActivosMesa = pedidoRepo.findByNumeroMesaAndEstadoIn(mesa, List.of("PENDIENTE", "EN PROCESO", "SERVIDO"));
        
        if (!pedidosActivosMesa.isEmpty()) {
            Pedido pedidoExistente = pedidosActivosMesa.get(0);
            if (!pedidoExistente.getNombreMesera().equalsIgnoreCase(cliente)) {
                model.addAttribute("errorMesa", "La mesa " + mesa + " ya está siendo atendida por " + pedidoExistente.getNombreMesera() + ".");
                model.addAttribute("carrito", carrito);
                model.addAttribute("total", carrito.stream().mapToDouble(ItemPedido::getSubtotal).sum());
                model.addAttribute("mesas", java.util.stream.IntStream.rangeClosed(1, 25).boxed().collect(Collectors.toList()));
                model.addAttribute("observacionesTemp", observaciones);
                model.addAttribute("mesaSeleccionadaTemp", mesa);
                return "carrito"; 
            }
        }

        List<Pedido> pedidosMesa = pedidoRepo.findByNumeroMesaAndEstadoIn(mesa, List.of("PENDIENTE"));
        Pedido pedidoActual;

        if (!pedidosMesa.isEmpty()) {
            pedidoActual = pedidosMesa.get(0); 
        } else {
            pedidoActual = new Pedido(mesa, cliente, 0.0);
        }

        for (ItemPedido item : carrito) {
            ProductoPrecio ppFresco = ppRepo.findById(item.getProductoPrecio().getIdPrecio()).orElse(null);
            if (ppFresco != null) {
                boolean encontrado = false;
                for (ItemPedido itemExistente : pedidoActual.getItems()) {
                    if (itemExistente.getProductoPrecio().getIdPrecio().equals(ppFresco.getIdPrecio())) {
                        itemExistente.setCantidad(itemExistente.getCantidad() + item.getCantidad());
                        encontrado = true;
                        break;
                    }
                }
                if (!encontrado) {
                    ItemPedido nuevoItem = new ItemPedido(ppFresco, item.getCantidad());
                    nuevoItem.setPedido(pedidoActual);
                    pedidoActual.getItems().add(nuevoItem);
                }
            }
        }

        double nuevoTotal = pedidoActual.getItems().stream().mapToDouble(ItemPedido::getSubtotal).sum();
        pedidoActual.setTotal(nuevoTotal);
        pedidoActual.setNombreMesera(cliente);
        
        if (observaciones != null && !observaciones.trim().isEmpty()) {
            pedidoActual.setObservaciones(observaciones);
        }

        pedidoRepo.save(pedidoActual);
        
        session.setAttribute("meseraActual", cliente);
        session.setAttribute("mesaActual", mesa);
        session.removeAttribute("carrito");
        return "redirect:/exito?mesa=" + mesa;
    }

    @GetMapping("/exito")
    public String pedidoExitoso(@RequestParam("mesa") Integer mesa, Model model) {
        model.addAttribute("mesa", mesa);
        return "exito";
    }

    @PostMapping("/cobrarMesa")
    public String cobrarMesa(@RequestParam("numeroMesa") Integer numeroMesa, 
                             @RequestParam("metodoPago") String metodoPago) {
        List<Pedido> pedidosMesa = pedidoRepo.findByNumeroMesaAndEstadoIn(numeroMesa, List.of("PENDIENTE", "EN PROCESO", "SERVIDO"));
        
        boolean todosServidos = pedidosMesa.stream().allMatch(p -> "SERVIDO".equals(p.getEstado()));
        
        if (todosServidos && !pedidosMesa.isEmpty()) {
            for (Pedido p : pedidosMesa) {
                p.setEstado("REALIZADO");
                p.setMetodoPago(metodoPago);
                pedidoRepo.save(p);
            }
        }
        return "redirect:/mis-pedidos"; 
    }

    @GetMapping("/cocina")
    public String verCocina(Model model) {
        model.addAttribute("listaPlatos", ppRepo.findAll());
        return "cocina";
    }

    @GetMapping("/api/pedidos-activos")
    @ResponseBody
    public List<Pedido> apiPedidosActivos() {
        return pedidoRepo.findByEstadoInOrderByIdPedidoAsc(List.of("PENDIENTE", "EN PROCESO"));
    }

    @PostMapping("/cambiarEstado")
    public String cambiarEstado(@RequestParam("idPedido") Long idPedido, @RequestParam("nuevoEstado") String nuevoEstado) {
        pedidoRepo.findById(idPedido).ifPresent(p -> {
            p.setEstado(nuevoEstado);
            pedidoRepo.save(p);
        });
        return "redirect:/cocina";
    }

    @PostMapping("/actualizarPlato")
    public String actualizarPlato(@RequestParam("idPrecio") Integer idPrecio, @RequestParam("nuevoStock") Integer nuevoStock, @RequestParam("nuevoEstado") String nuevoEstado) {
        ppRepo.findById(idPrecio).ifPresent(pp -> {
            if (nuevoStock <= 0) {
                pp.setStock(0);
                pp.setEstado("AGOTADO");
            } else {
                pp.setStock(nuevoStock);
                pp.setEstado(nuevoEstado);
            }
            ppRepo.save(pp);
        });
        return "redirect:/cocina";
    }

    @GetMapping("/api/historial-pedidos")
    @ResponseBody
    public List<Pedido> apiHistorialPedidos(
            @RequestParam(value = "mesa", required = false) Integer mesa, 
            @RequestParam(value = "mesera", required = false) String mesera,
            @RequestParam(value = "fecha", required = false) String fechaStr) {
        
        LocalDate fecha = (fechaStr == null || fechaStr.isEmpty()) ? LocalDate.now() : LocalDate.parse(fechaStr);
        LocalDateTime inicio = fecha.atStartOfDay();
        LocalDateTime fin = fecha.atTime(23, 59, 59);

        List<Pedido> pedidosRealizados = pedidoRepo.findByEstadoAndFechaHoraRealizadoBetween("REALIZADO", inicio, fin).stream()
                .filter(p -> mesa == null || mesa == 0 || p.getNumeroMesa().equals(mesa))
                .filter(p -> mesera == null || mesera.trim().isEmpty() || p.getNombreMesera().equalsIgnoreCase(mesera.trim()))
                .collect(Collectors.toList());

        Map<String, List<Pedido>> agrupados = pedidosRealizados.stream()
                .collect(Collectors.groupingBy(p -> p.getNumeroMesa() + "_" + p.getFechaHoraRealizado().withSecond(0).withNano(0)));

        List<Pedido> historialAgrupado = new ArrayList<>();

        for (List<Pedido> grupo : agrupados.values()) {
            Pedido pedidoUnificado = new Pedido();
            Pedido primero = grupo.get(0);
            Pedido ultimo = grupo.get(grupo.size() - 1);

            pedidoUnificado.setIdPedido(ultimo.getIdPedido()); 
            pedidoUnificado.setNumeroMesa(primero.getNumeroMesa());
            pedidoUnificado.setNombreMesera(ultimo.getNombreMesera());
            pedidoUnificado.setEstado("REALIZADO");
            pedidoUnificado.setFechaHoraPedido(primero.getFechaHoraPedido());
            pedidoUnificado.setFechaHoraRealizado(primero.getFechaHoraRealizado());
            pedidoUnificado.setMetodoPago(primero.getMetodoPago());
            pedidoUnificado.setObservaciones(primero.getObservaciones());

            double totalSumado = 0;
            List<ItemPedido> todosLosItems = new ArrayList<>();
            for (Pedido p : grupo) {
                todosLosItems.addAll(p.getItems());
                totalSumado += p.getTotal();
            }
            pedidoUnificado.setItems(todosLosItems);
            pedidoUnificado.setTotal(totalSumado);
            historialAgrupado.add(pedidoUnificado);
        }

        return historialAgrupado.stream()
                .sorted((a, b) -> b.getFechaHoraRealizado().compareTo(a.getFechaHoraRealizado()))
                .collect(Collectors.toList());
    }

    @GetMapping("/mis-pedidos")
    public String verMisPedidos(@RequestParam(value = "mesera", required = false) String mesera, HttpSession session, Model model) {
        if (mesera == null || mesera.isEmpty()) mesera = (String) session.getAttribute("meseraActual");

        if (mesera != null && !mesera.isEmpty()) {
            List<Pedido> mis = pedidoRepo.findByNombreMeseraOrderByIdPedidoDesc(mesera);
            
            List<Pedido> activos = mis.stream()
                    .filter(p -> !"REALIZADO".equals(p.getEstado()))
                    .collect(Collectors.toList());

            Map<Integer, List<Pedido>> agrupadosActivos = activos.stream()
                    .collect(Collectors.groupingBy(Pedido::getNumeroMesa));

            List<Pedido> pedidosActivosUnificados = new ArrayList<>();
            Map<Long, String> estadosItems = new HashMap<>();
            Map<Integer, List<String>> notasPorMesa = new HashMap<>();
            
            for (Map.Entry<Integer, List<Pedido>> entry : agrupadosActivos.entrySet()) {
                List<Pedido> pedidosDeLaMesa = entry.getValue();
                Pedido pedidoUnificado = new Pedido();
                pedidoUnificado.setIdPedido(pedidosDeLaMesa.get(pedidosDeLaMesa.size() - 1).getIdPedido()); 
                pedidoUnificado.setNumeroMesa(entry.getKey());
                pedidoUnificado.setNombreMesera(mesera);
                
                pedidosDeLaMesa.sort(Comparator.comparing(Pedido::getIdPedido));
                
                List<String> listaNotas = new ArrayList<>();
                for (Pedido p : pedidosDeLaMesa) {
                    if (p.getObservaciones() != null && !p.getObservaciones().trim().isEmpty()) {
                        listaNotas.add(p.getObservaciones());
                    }
                }
                notasPorMesa.put(entry.getKey(), listaNotas);
                
                boolean todosServidos = pedidosDeLaMesa.stream().allMatch(p -> "SERVIDO".equals(p.getEstado()));
                pedidoUnificado.setEstado(todosServidos ? "SERVIDO" : "EN PROCESO");
                
                double totalSumado = 0;
                List<ItemPedido> todosLosItems = new ArrayList<>();
                
                for (Pedido p : pedidosDeLaMesa) {
                    for (ItemPedido item : p.getItems()) {
                        todosLosItems.add(item);
                        estadosItems.put(item.getId(), p.getEstado()); 
                    }
                    totalSumado += p.getTotal();
                }
                
                pedidoUnificado.setItems(todosLosItems);
                pedidoUnificado.setTotal(totalSumado);
                pedidosActivosUnificados.add(pedidoUnificado);
            }

            List<Pedido> terminados = mis.stream()
                    .filter(p -> "REALIZADO".equals(p.getEstado()))
                    .collect(Collectors.toList());

            Map<String, List<Pedido>> agrupadosTerminados = terminados.stream()
                    .collect(Collectors.groupingBy(p -> 
                        p.getNumeroMesa() + "_" + p.getFechaHoraRealizado().withSecond(0).withNano(0)
                    ));

            List<Pedido> pedidosTerminadosUnificados = new ArrayList<>();
            
            for (Map.Entry<String, List<Pedido>> entry : agrupadosTerminados.entrySet()) {
                List<Pedido> pedidosDeLaMesa = entry.getValue();
                
                pedidosDeLaMesa.sort(Comparator.comparing(Pedido::getIdPedido));
                
                Pedido pedidoUnificado = new Pedido();
                pedidoUnificado.setIdPedido(pedidosDeLaMesa.get(pedidosDeLaMesa.size() - 1).getIdPedido()); 
                pedidoUnificado.setNumeroMesa(pedidosDeLaMesa.get(0).getNumeroMesa());
                pedidoUnificado.setNombreMesera(mesera);
                pedidoUnificado.setEstado("REALIZADO");
                pedidoUnificado.setFechaHoraPedido(pedidosDeLaMesa.get(0).getFechaHoraPedido());
                pedidoUnificado.setFechaHoraRealizado(pedidosDeLaMesa.get(0).getFechaHoraRealizado());
                pedidoUnificado.setMetodoPago(pedidosDeLaMesa.get(0).getMetodoPago());
                
                List<String> listaNotas = new ArrayList<>();
                for (Pedido p : pedidosDeLaMesa) {
                    if (p.getObservaciones() != null && !p.getObservaciones().trim().isEmpty()) {
                        listaNotas.add(p.getObservaciones());
                    }
                }
                pedidoUnificado.setListaNotasSeparadas(listaNotas);
                
                double totalSumado = 0;
                List<ItemPedido> todosLosItems = new ArrayList<>();
                for (Pedido p : pedidosDeLaMesa) {
                    todosLosItems.addAll(p.getItems());
                    totalSumado += p.getTotal();
                }
                pedidoUnificado.setItems(todosLosItems);
                pedidoUnificado.setTotal(totalSumado);
                pedidosTerminadosUnificados.add(pedidoUnificado);
            }

            pedidosTerminadosUnificados.sort((a, b) -> b.getFechaHoraRealizado().compareTo(a.getFechaHoraRealizado()));

            model.addAttribute("pedidosActivos", pedidosActivosUnificados);
            model.addAttribute("estadosItems", estadosItems);
            model.addAttribute("notasPorMesa", notasPorMesa);
            model.addAttribute("pedidosTerminados", pedidosTerminadosUnificados);
            model.addAttribute("nombreMesera", mesera);
        } else {
            model.addAttribute("pedidosActivos", new ArrayList<Pedido>());
            model.addAttribute("pedidosTerminados", new ArrayList<Pedido>());
            model.addAttribute("nombreMesera", "No identificada");
        }
        return "mis-pedidos";
    }
}