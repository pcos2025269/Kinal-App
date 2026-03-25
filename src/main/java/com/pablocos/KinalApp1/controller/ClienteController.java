package com.pablocos.KinalApp1.controller;

import com.pablocos.KinalApp1.entity.Cliente;
import com.pablocos.KinalApp1.repository.ClienteRepository;
import com.pablocos.KinalApp1.service.IClienteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@RestController = @Controller +  @RequestMapping
@RequestMapping("/clientes")
//Todas las rutas de este controlador deben empezar por /clientes
public class ClienteController {
    //Inyectamos el servicio y NO el repositorio
    //El controlador solo debe de tener conexion con el sercixio

    private final IClienteService clienteService;
    // como buena practica la Inyeccion de dependencias debe hacerse por el constructor
    public ClienteController(IClienteService clienteService) {
        this.clienteService = clienteService;
    }
    @GetMapping
    //ResponseEntity nos permite controlar el codigo HTTPy el cuerpo
    public ResponseEntity <List<Cliente>> listar(){
        List<Cliente> clientes = clienteService.listarTodos();
        //deleganmos el servicio
        return ResponseEntity.ok(clientes);
        //200 OK con la
    }
    //{dpi} es una variable de ruta : el valor a buscar
    @GetMapping("/{dpi}")
    public ResponseEntity<Cliente> buscarDPI(@PathVariable String dpi){
        //@pathVariable: toma el valor de la URL y lo asigna al DPi

        return clienteService.buscarPorDPI(dpi)
                //Si optional tiene valor , devuelve 200 ok con el cliente
                .map(ResponseEntity::ok)
                //Si optional esta vacio, devuelve 404 NOT FOUND
                .orElse(ResponseEntity.notFound().build());
    }

    //Post Crea un nuevo Cliente    
    @PostMapping
    public ResponseEntity<?> guardar(@RequestBody Cliente cliente){
        //@RequesrBody: toma el JSON del cuerpo y lo convierte a un objeto de tipo CLiente
        //<?> significa "tipo generico " puede ser un cliente o un String
        try {
            Cliente nuevoCliente = clienteService.guardar(cliente);
            //Intentamos guardar un cliente pero puede lanzar una excepcion de IllegalArgumentException
            return new ResponseEntity<>(nuevoCliente, HttpStatus.CREATED);
            //201 CREATED(mucho mas especifico que el 200 para la creacion de un cliente)
        }catch (IllegalArgumentException e){
            //Si hay un error de validacion
            //400 BADREQUEST con el mensaje de Error
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //DELETE elimina un cliente
     @DeleteMapping("/{dpi}")
    public ResponseEntity<Void> eliminar (@PathVariable String dpi){
        //ResponseEntity<void>: No devuelve cuerpo en la respuesta
         try {
            if (!clienteService.existePorDPI(dpi)){
                return ResponseEntity.notFound().build();
            }
            clienteService.eliminar(dpi);
            return ResponseEntity.noContent().build();
            //204 NO CONTENT (Se ejecuto correctamente y no devuelve cuerpo)
         }catch (RuntimeException e){
             return ResponseEntity.notFound().build();
             // 404 NOT FOUND
         }

    }

    //Actualiza un cliente a traves del DPI
    @PutMapping("/{dpi}")
    public ResponseEntity<?> actualizar(@PathVariable String dpi,@RequestBody Cliente cliente){
        try {
            if (!clienteService.existePorDPI(dpi)){

                return ResponseEntity.notFound().build();
            }
            //Actualizamos el cliente pero puede lanzar una exception
            Cliente clieteActualizado = clienteService.actualizar(dpi,cliente);
            return ResponseEntity.ok(clieteActualizado);
            //200 ok con el cliente ya actualizado
        }catch (IllegalArgumentException e){
            //Error cuando los datos sean incorrectos
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            //Posiblemente cualquier otro error como cliente no encontrado, etc
            //404 NOT FOUND
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Cliente>> buscarPorEstado(@PathVariable int estado) {
        List<Cliente> clientes = clienteService.buscarPorEstadoConFor(estado);
        if (clientes.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(clientes);
    }
}
