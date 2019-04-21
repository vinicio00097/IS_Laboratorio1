package gt.edu.umg.ingenieria.sistemas.laboratorio1.controller;

import gt.edu.umg.ingenieria.sistemas.laboratorio1.model.Client;
import gt.edu.umg.ingenieria.sistemas.laboratorio1.service.ClientService;
import gt.edu.umg.ingenieria.sistemas.laboratorio1.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletContext;
import java.util.List;

/**
 *
 * @author Josué Barillas (jbarillas)
 */
@RestController
@RequestMapping("/clientes")
public class ClientController {

    private final ReportService _reportService;
    private final ClientService _clientService;
    private final ServletContext _servletContext;

    @Autowired
    public ClientController(ReportService reportService, ClientService clientService, ServletContext servletContext) {
        this._reportService = reportService;
        this._clientService = clientService;
        this._servletContext = servletContext;
    }

    @GetMapping("/getById")
    public Client getById(@RequestParam(name = "id") long id) {
        return this._clientService.getClientById(id);
    }

    @GetMapping("/buscarPorNit")
    public Client getByNit(@RequestParam(name = "nit") String nit) {
        return this._clientService.getClientByNit(nit);
    }

    @GetMapping("/buscarPorNombreApellido")
    public List<Client> getByNameAndLastName(@RequestParam(name = "query") String nameAndLastName) {
        return this._clientService.getClientsByNameAndLastName(nameAndLastName);
    }

    @GetMapping("/buscarTodos")
    private List<Client> getByAll() {
        return this._clientService.getAllClients();
    }

    @PostMapping("/crearCliente")
    public Object create(@RequestBody Client client) {
        return this._clientService.createClient(client);
    }

    @PutMapping("/editarCliente/{id}/{nit}")
    public Client updateNit(@PathVariable long id, @PathVariable String nit) {
        Client client = getById(id);
        client.setNit(nit);
        return this._clientService.updateClient(client);
    }

    @PutMapping("/editarCliente/{id}/{name}/{lastName}")
    public Client updateNameAndLastName(@PathVariable long id, @PathVariable String name, @PathVariable String lastName) {
        Client client = getById(id);
        client.setFirstName(name);
        client.setLastName(lastName);
        return this._clientService.updateClient(client);
    }

    @GetMapping("/generarReporteClientes")
    public String generateReport() {
        return this._reportService.generateReport(this._clientService.getAllClients(), _servletContext);
    }
}
