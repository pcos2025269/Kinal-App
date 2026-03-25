package com.pablocos.KinalApp1.service;

import com.pablocos.KinalApp1.entity.Cliente;

import java.util.List;
import java.util.Optional;

public interface IClienteService {
    /*
     *  Interfaz: es un contrato que dice Que metodos debe tener
     *  CUalquier servicio de Clientes, No tiene
     *  IMplementacion, solo la definicion de los metodos
     */
    //Metodo que devuelve una lista de todos los clientes
    List<Cliente> listarTodos();
    /*
     * List <>lo que hace es devolver una lista
     * de objetos de la entidad Clientes
     */

    //Metodo que Guarda un CLiente en la base de datos
    Cliente guardar(Cliente cliente);
    //Párametros: recibe un objeto cliente con los datos a guardar

    //OPtional - contenedor que puede o no tener valor
    //Evita el error de NUllPointerException
    Optional<Cliente> buscarPorDPI(String dpi);

    //Metodo que actualiza un Cliente
    Cliente actualizar(String dpi, Cliente cliente);
    /*
     *Parametros - dpi: DPI a acrializar
     *Cliente cliente: Objeto con los datos nuevos
     * Retorna un objeto de tipo CLiente ya actualizado
     * */

    /*
        Metodo de tipo void para eliminar un cliente
        void: no retona ningun valor o datos
        Elimina un Cliente por su DPI
     */
    void eliminar(String dpi);

    //boolean - Retornara true si existe y false si no exite
    boolean existePorDPI(String dpi);

    List<Cliente> buscarPorEstadoConFor(int estado);

}
