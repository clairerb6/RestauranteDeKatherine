package me.katherineflores.restaurante.model

class CuentaMesa(mesa: Int) {
    private val _items: MutableList<ItemMesa> = mutableListOf()
    var aceptaPropina: Boolean = true
    // porcentaje de propina fijo del 10%
    private val porcentajePropina = 0.10

    // Agregar por nombre y cantidad
    fun agregarItem(itemMenu: ItemMenu, cantidad: Int) {
        val existente = _items.find { it.itemMenu.nombre == itemMenu.nombre }
        if (existente != null) {
            existente.cantidad += cantidad
        } else {
            _items.add(ItemMesa(cantidad, itemMenu))
        }
    }

    // Agregar ItemMesa directamente
    fun agregarItem(itemMesa: ItemMesa) {
        val existente = _items.find { it.itemMenu.nombre == itemMesa.itemMenu.nombre }
        if (existente != null) {
            existente.cantidad += itemMesa.cantidad
        } else {
            _items.add(itemMesa)
        }
    }

    fun actualizarCantidad(nombreItem: String, cantidad: Int) {
        val it = _items.find { it.itemMenu.nombre == nombreItem }
        if (it != null) it.cantidad = cantidad
        else {
            // Si no existía, lo agregamos como nueva entrada con precio 0 (no ideal)
        }
    }

    fun calcularTotalSinPropina(): Int {
        return _items.sumOf { it.calcularSubTotal() }
    }

    fun calcularPropina(): Int {
        if (!aceptaPropina) return 0
        val total = calcularTotalSinPropina()
        // calculo redondeado a entero
        return kotlin.math.round(total * porcentajePropina).toInt()
    }

    fun calcularTotalConPropina(): Int {
        return calcularTotalSinPropina() + calcularPropina()
    }

    fun getItems(): List<ItemMesa> = _items.toList()
}