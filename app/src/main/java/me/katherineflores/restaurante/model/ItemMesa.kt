package me.katherineflores.restaurante.model


class ItemMesa(var cantidad: Int, val itemMenu: ItemMenu) {

    fun calcularSubTotal(): Int {
        val precioUnit = itemMenu.getPrecioPesos()
        // subtotal en pesos (int)
        return cantidad * precioUnit
    }
}