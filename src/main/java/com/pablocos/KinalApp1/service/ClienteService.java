package com.pablocos.KinalApp1.service;

import com.pablocos.KinalApp1.entity.Cliente;
import com.pablocos.KinalApp1.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

    /*
        Anotacion que registra un bean como una Bean de Spring
        Que la clase contiene la logica del negocio
    */
    @Service
    /*
        Por defecto todos los metodos de esta clase seran transaccionales
        una transaccion es que puede o no ocurrir algo
     */
    @Transactional
public class ClienteService implements IClienteService {
     /*
        private: solo es accesible dentro de la misma clase
        final: No puede cambiar porque es constante
        ClienteRepository: El repositorio para acceder a la base de datos
        Inyeccion de dependencias ya que Spring nos da el repositorio
      */
        private final ClienteRepository clienteRepository;
        /*
        Cosntructor: este se ejecuta al crea un objeto
        Spring pasa el repositorio automaticamente(Inyeccion de dependencias)
         */
        public ClienteService(ClienteRepository clienteRepository) {
            this.clienteRepository = clienteRepository;
            //Asignar el repositorio a nuestra variable de clase
        }
        // Indica que se esta implementando un metodo de la interfaz
        @Override
        // OPtimiza la consulta, solo la lectura, para que no bloquee la base de datos
        @Transactional(readOnly = true)
        public List<Cliente> listarTodos() {
            return clienteRepository.findAll();
            //findAll es un metodo de Spring que hace el select * from Clientes
            //este metodo es de JPARepository
        }

        @Override
        public Cliente guardar(Cliente cliente) {
            /*
             Metodo de guardar: crea un cliente
             Aca es donde colocamos la logica del negocio Antes de guardar pero
             Primero validamos el dato
             */
            validarCliente(cliente);
            if (cliente.getEstado() == 0 )
                cliente.setEstado(1L);
            return clienteRepository.save(cliente);
        }

        @Override
        @Transactional(readOnly = true)
        public Optional <Cliente> buscarPorDPI(String dpi) {
            //Busca un cliente por DPI
            return clienteRepository.findById(dpi);
            //Nos evita el nullpointer exception
        }

        @Override
        public Cliente actualizar(String dpi, Cliente cliente) {
            //Metodo para actualizar un cliente existente
            if(!clienteRepository.existsById(dpi)){
                throw new RuntimeException("El cliente no se encontro con el DPI:  " + dpi);
                //Si no existe se lanza una excepcion y a esto se le denomina(error controlado)
            }
            cliente.setDPICliente(dpi);
            //Asegurmos que el DPI del objeto coincida con el de la URL
            //Por seguridad usamos el DPI de la URL y no el que viene en el JSON
            validarCliente(cliente);
            return clienteRepository.save(cliente);
            /*
                save() este no solo sive para guardar sino tambien para actualizar si el dato
                Existe (dpi) entonces hace UPDATE pero si no existe hace un INSERT pero
                antes verificamos si existe o no el registro
             */
        }

        @Override
        public void eliminar(String dpi) {
            //Elimina un cliente
            if(!clienteRepository.existsById(dpi)){
                throw new RuntimeException("El cliente no se encontro con el DPI: " + dpi );
            }
            clienteRepository.deleteById(dpi);
        }

        @Override
        @Transactional(readOnly = true)
        public boolean existePorDPI(String dpi) {
            //verificar si existe un cliente
            return clienteRepository.existsById(dpi);
        }

        @Override
        @Transactional(readOnly = true)
        public List<Cliente> buscarPorEstadoConFor(Long estado) {
            List<Cliente> Clientes = clienteRepository.findAll();
            List<Cliente> filtrados = new ArrayList<>();
            for (Cliente c :  Clientes) {
                if (c.getEstado() == estado) {
                    filtrados.add(c);
                }
            }
            return filtrados;
        }

        //Metodo privado y solo puede utilizarse dentro de la clase
        private void validarCliente(Cliente cliente){
            /*
            Validaciones del negocio:
            Este metodo se hara privado porque es algo interno dedel servicio
             */
            if (cliente.getDPICliente() == null || cliente.getDPICliente().trim().isEmpty()){
                //Si el DPI es null o esta vacio despues de quitar espacios
                //Lanza una excepcion con mensaje
                throw new IllegalArgumentException("El DPI es obligatorio");
            }
            if (cliente.getNombreCliente() == null || cliente.getNombreCliente().trim().isEmpty()){
                throw new IllegalArgumentException("El nombre es obligatorio");
            }

            if (cliente.getApellidoCliente() == null || cliente.getApellidoCliente().trim().isEmpty()){
                throw new IllegalArgumentException("El apellido es un dato obligatorio");
            }
        }
}
