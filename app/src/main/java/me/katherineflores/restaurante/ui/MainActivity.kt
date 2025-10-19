package me.katherineflores.restaurante.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import me.katherineflores.restaurante.R
import me.katherineflores.restaurante.databinding.ActivityMainBinding
import me.katherineflores.restaurante.model.CuentaMesa
import me.katherineflores.restaurante.model.ItemMenu
import me.katherineflores.restaurante.model.ItemMesa
import java.text.NumberFormat
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var cuenta: CuentaMesa
    private lateinit var pastelMenu: ItemMenu
    private lateinit var cazuelaMenu: ItemMenu
    private val nf = NumberFormat.getCurrencyInstance(Locale("es", "CL"))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializamos los platos
        pastelMenu = ItemMenu("Pastel de Choclo", "12000")
        cazuelaMenu = ItemMenu("Cazuela", "10000")

        // setear textos de UI con los precios formateados
        binding.tvPrecioPastel.text = pastelMenu.getPrecioFormateado()
        binding.tvPrecioCazuela.text = cazuelaMenu.getPrecioFormateado()
        binding.tvNombrePastel.text = pastelMenu.nombre
        binding.tvNombreCazuela.text = cazuelaMenu.nombre

        // Cuenta para la mesa 1
        cuenta = CuentaMesa(mesa = 1)
        // agregar items con cantidad 0 inicialmente (opcional)
        cuenta.agregarItem(ItemMesa(0, pastelMenu))
        cuenta.agregarItem(ItemMesa(0, cazuelaMenu))

        // Listeners para cambios en cantidades
        binding.etCantidadPastel.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val qty = parseCantidad(s?.toString())
                updateCantidad(pastelMenu.nombre, qty)
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        binding.etCantidadCazuela.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val qty = parseCantidad(s?.toString())
                updateCantidad(cazuelaMenu.nombre, qty)
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        // Switch para la propina
        binding.switchPropina.setOnCheckedChangeListener { _, isChecked ->
            cuenta.aceptaPropina = isChecked
            refreshTotales()
        }

        // Mostrar totales iniciales
        refreshTotales()
    }

    private fun parseCantidad(text: String?): Int {
        if (text.isNullOrBlank()) return 0
        return try {
            text.filter { it.isDigit() }.toInt()
        } catch (e: Exception) {
            0
        }
    }

    private fun updateCantidad(nombre: String, cantidad: Int) {
        // actualizar en cuenta
        cuenta.actualizarCantidad(nombre, cantidad)
        // actualizar subtotales en UI
        val item = cuenta.getItems().find { it.itemMenu.nombre == nombre }
        val subtotal = item?.calcularSubTotal() ?: 0
        val subtotalFormateado = nf.format(subtotal)
        if (nombre == pastelMenu.nombre) {
            binding.tvSubtotalPastel.text = getString(R.string.subtotal_format, subtotalFormateado)
        } else if (nombre == cazuelaMenu.nombre) {
            binding.tvSubtotalCazuela.text = getString(R.string.subtotal_format, subtotalFormateado)
        }
        // actualizar totales generales
        refreshTotales()
    }

    private fun refreshTotales() {
        val totalSin = cuenta.calcularTotalSinPropina()
        val propina = cuenta.calcularPropina()
        val totalCon = cuenta.calcularTotalConPropina()

        binding.tvTotalSinPropina.text = getString(R.string.total_sin_propina_format, nf.format(totalSin))
        binding.tvMontoPropina.text = getString(R.string.propina_format, nf.format(propina))
        binding.tvTotalConPropina.text = getString(R.string.total_con_propina_format, nf.format(totalCon))
    }
}