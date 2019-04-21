package gt.edu.umg.ingenieria.sistemas.laboratorio1.service;

import gt.edu.umg.ingenieria.sistemas.laboratorio1.dao.ClientRepository;
import gt.edu.umg.ingenieria.sistemas.laboratorio1.model.Client;
import gt.edu.umg.ingenieria.sistemas.laboratorio1.model.ErrorModel;
import gt.edu.umg.ingenieria.sistemas.laboratorio1.utils.Helpers;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Josué Barillas (jbarillas)
 */
public class ClientService {
    private final ClientRepository clientRepository;

    @Autowired
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Client getClientByNit (String nit) {
        List<Client> clients = getAllClients();
        return clients.stream()
                .filter(client -> Objects.equals(client.getNit(), nit))
                .findFirst()
                .orElse(new Client());
    }

    public List<Client> getClientsByNameAndLastName(String nameAndLastName) {
        List<Client> clients = getAllClients();
        List<Client> result = new ArrayList<>();
        for (Client a: clients) {
            if(a.getFirstName().matches(nameAndLastName) || a.getLastName().matches(nameAndLastName)){
                result.add(a);
            }
        }
        return result;
    }

    public Client getClientById(long id) {
        return this.clientRepository.findById(id).get();
    }

    public List<Client> getAllClients() {
        return (List<Client>) this.clientRepository.findAll();
    }

    public Object createClient(Client client) {
        String msg = "Lo sentimos. El sistema solo permite el registro de usuarios mayores de edad.";
        if(!Helpers.isOverOrEqualsNYears(client.getBirthDate(), 18)){
            try {
                throw new Exception(msg);
            } catch (Exception e) {
                e.printStackTrace();
                return new ErrorModel("Menor de edad", msg);
            }
        } else if (!(client.getNit().matches("\\d+") && client.getNit().length() == 10)) {
            msg = "NIT invalido.";
            try {
                throw new Exception(msg);
            } catch (Exception e) {
                e.printStackTrace();
                return new ErrorModel(msg, "Nit contiene caracteres invalidos o es de diferente longitud a 10");
            }
        } else {
            client.setFirstName(
                    client.getFirstName().substring(0, 1).toUpperCase() + client.getFirstName().substring(1).toLowerCase()
            );
            client.setLastName(
                    client.getLastName().substring(0, 1).toUpperCase() + client.getLastName().substring(1).toLowerCase()
            );
            return this.clientRepository.save(client);
        }
    }

    public Client updateClient(Client client) {
        return this.clientRepository.save(client);
    }
}
