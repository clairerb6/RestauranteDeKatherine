package me.katherineflores.restaurante.model

import java.text.NumberFormat
import java.util.Locale

class ItemMenu(val nombre: String, precio: String) {
    // Guardamos internamente el precio en pesos como Int (en pesos chilenos, sin decimales).
    private val precioPesos: Int = parsePrecio(precio)

    // Getter para mostrar el precio en formato moneda (String)
    fun getPrecioFormateado(): String {
        val nf = NumberFormat.getCurrencyInstance(Locale("es", "CL"))
        return nf.format(precioPesos)
    }

    fun getPrecioPesos(): Int = precioPesos

    // Asegura que el string tenga formato simple como "12000" o "$12.000"
    private fun parsePrecio(precioStr: String): Int {
        // eliminar caracteres no numéricos
        val digitsOnly = precioStr.filter { it.isDigit() }
        return if (digitsOnly.isEmpty()) 0 else digitsOnly.toInt()
    }
}