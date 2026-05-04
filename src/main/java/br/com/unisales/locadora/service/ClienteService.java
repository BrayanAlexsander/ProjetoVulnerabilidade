package br.com.unisales.locadora.service;

import br.com.unisales.locadora.api.dto.ClienteRequest;
import br.com.unisales.locadora.domain.Cliente;
import br.com.unisales.locadora.repository.ClienteRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClienteService {
  private final ClienteRepository clienteRepository;

  public ClienteService(ClienteRepository clienteRepository) {
    this.clienteRepository = clienteRepository;
  }

  @Transactional(readOnly = true)
  public List<Cliente> listar() {
    return clienteRepository.findAll();
  }

  @Transactional
  public Cliente criar(ClienteRequest req) {
    Cliente c = new Cliente();
    c.setNome(req.nome());
    c.setDocumento(req.documento());
    return clienteRepository.save(c);
  }

  @Transactional(readOnly = true)
  public Cliente buscar(Long id) {
    return clienteRepository.findById(id).orElseThrow(() -> new NotFoundException("Cliente não encontrado"));
  }
}

